begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|datasource
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLFeatureNotSupportedException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ArrayBlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|BlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Semaphore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A non-blocking {@link DataSource} with a pool of connections.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|UnmanagedPoolingDataSource
implements|implements
name|PoolingDataSource
block|{
comment|// An old hack that fixes Sybase problems with autocommit. Used idea from
comment|// Jonas org.objectweb.jonas.jdbc_xa.ConnectionImpl
comment|// (http://www.objectweb.org/jonas/).
comment|//
comment|// If problem is not the one that can be fixed by this patch, original
comment|// exception is rethrown. If exception occurs when fixing the problem, new
comment|// exception is thrown.
comment|//
specifier|static
name|void
name|sybaseAutoCommitPatch
parameter_list|(
name|Connection
name|c
parameter_list|,
name|SQLException
name|e
parameter_list|,
name|boolean
name|autoCommit
parameter_list|)
throws|throws
name|SQLException
block|{
name|String
name|s
init|=
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|contains
argument_list|(
literal|"set chained command not allowed"
argument_list|)
condition|)
block|{
comment|// TODO: doing 'commit' here is extremely dangerous... we need to
comment|// get a hold of Sybase instance and verify whether this issue is
comment|// still there, and fix it differently (and perhaps generically) by
comment|// calling 'rollback' on connections (can we do it when getting
comment|// connection from the pool? returning it to the pool?)
name|c
operator|.
name|commit
argument_list|()
expr_stmt|;
name|c
operator|.
name|setAutoCommit
argument_list|(
name|autoCommit
argument_list|)
expr_stmt|;
comment|// Shouldn't fail now.
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
comment|/**      * An exception indicating that a connection request waiting in the queue      * timed out and was unable to obtain a connection.      */
specifier|public
specifier|static
class|class
name|ConnectionUnavailableException
extends|extends
name|SQLException
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1063973806941023165L
decl_stmt|;
specifier|public
name|ConnectionUnavailableException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Defines a maximum time in milliseconds that a connection request could 	 * wait in the connection queue. After this period expires, an exception 	 * will be thrown in the calling method. 	 */
specifier|public
specifier|static
specifier|final
name|int
name|MAX_QUEUE_WAIT_DEFAULT
init|=
literal|20000
decl_stmt|;
specifier|private
specifier|static
name|Log
name|LOGGER
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|UnmanagedPoolingDataSource
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|DataSource
name|nonPoolingDataSource
decl_stmt|;
specifier|private
name|long
name|maxQueueWaitTime
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|PoolAwareConnection
argument_list|,
name|Object
argument_list|>
name|pool
decl_stmt|;
specifier|private
name|Semaphore
name|poolCap
decl_stmt|;
specifier|private
name|BlockingQueue
argument_list|<
name|PoolAwareConnection
argument_list|>
name|available
decl_stmt|;
specifier|private
name|int
name|maxIdleConnections
decl_stmt|;
specifier|private
name|int
name|minConnections
decl_stmt|;
specifier|private
name|int
name|maxConnections
decl_stmt|;
specifier|private
name|String
name|validationQuery
decl_stmt|;
specifier|static
name|int
name|maxIdleConnections
parameter_list|(
name|int
name|min
parameter_list|,
name|int
name|max
parameter_list|)
block|{
return|return
name|min
operator|==
name|max
condition|?
name|min
else|:
name|min
operator|+
operator|(
name|int
operator|)
name|Math
operator|.
name|ceil
argument_list|(
operator|(
name|max
operator|-
name|min
operator|)
operator|/
literal|2d
argument_list|)
return|;
block|}
specifier|public
name|UnmanagedPoolingDataSource
parameter_list|(
name|DataSource
name|nonPoolingDataSource
parameter_list|,
name|PoolingDataSourceParameters
name|parameters
parameter_list|)
block|{
name|int
name|minConnections
init|=
name|parameters
operator|.
name|getMinConnections
argument_list|()
decl_stmt|;
name|int
name|maxConnections
init|=
name|parameters
operator|.
name|getMaxConnections
argument_list|()
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|minConnections
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Negative min connections: "
operator|+
name|minConnections
argument_list|)
throw|;
block|}
if|if
condition|(
name|maxConnections
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Negative max connections: "
operator|+
name|maxConnections
argument_list|)
throw|;
block|}
if|if
condition|(
name|minConnections
operator|>
name|maxConnections
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Min connections ("
operator|+
name|minConnections
operator|+
literal|") is greater than max connections ("
operator|+
name|maxConnections
operator|+
literal|")"
argument_list|)
throw|;
block|}
name|this
operator|.
name|nonPoolingDataSource
operator|=
name|nonPoolingDataSource
expr_stmt|;
name|this
operator|.
name|maxQueueWaitTime
operator|=
name|parameters
operator|.
name|getMaxQueueWaitTime
argument_list|()
expr_stmt|;
name|this
operator|.
name|validationQuery
operator|=
name|parameters
operator|.
name|getValidationQuery
argument_list|()
expr_stmt|;
name|this
operator|.
name|minConnections
operator|=
name|minConnections
expr_stmt|;
name|this
operator|.
name|maxConnections
operator|=
name|maxConnections
expr_stmt|;
name|this
operator|.
name|pool
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|PoolAwareConnection
argument_list|,
name|Object
argument_list|>
argument_list|(
operator|(
name|int
operator|)
operator|(
name|maxConnections
operator|/
literal|0.75
operator|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|available
operator|=
operator|new
name|ArrayBlockingQueue
argument_list|<
name|PoolAwareConnection
argument_list|>
argument_list|(
name|maxConnections
argument_list|)
expr_stmt|;
name|this
operator|.
name|poolCap
operator|=
operator|new
name|Semaphore
argument_list|(
name|maxConnections
argument_list|)
expr_stmt|;
name|this
operator|.
name|maxIdleConnections
operator|=
name|maxIdleConnections
argument_list|(
name|minConnections
argument_list|,
name|maxConnections
argument_list|)
expr_stmt|;
comment|// grow pull to min connections
try|try
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|minConnections
condition|;
name|i
operator|++
control|)
block|{
name|PoolAwareConnection
name|c
init|=
name|createUnchecked
argument_list|()
decl_stmt|;
name|reclaim
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|BadValidationQueryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Bad validation query: "
operator|+
name|validationQuery
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Error creating new connection when starting connection pool, ignoring"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|int
name|poolSize
parameter_list|()
block|{
return|return
name|pool
operator|.
name|size
argument_list|()
return|;
block|}
name|int
name|availableSize
parameter_list|()
block|{
return|return
name|available
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|// expecting surrounding environment to block new requests for
comment|// connections before calling this method. Still previously unchecked
comment|// connections may be returned. I.e. "pool" will not grow during
comment|// shutdown, which is the only thing that we need
for|for
control|(
name|PoolAwareConnection
name|c
range|:
name|pool
operator|.
name|keySet
argument_list|()
control|)
block|{
name|retire
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|available
operator|.
name|clear
argument_list|()
expr_stmt|;
name|pool
operator|=
name|Collections
operator|.
name|emptyMap
argument_list|()
expr_stmt|;
block|}
name|void
name|managePool
parameter_list|()
block|{
comment|// do not grow or shrink abruptly ... open or close 1 connection on
comment|// each call
if|if
condition|(
name|available
operator|.
name|size
argument_list|()
operator|<
name|minConnections
condition|)
block|{
try|try
block|{
name|PoolAwareConnection
name|c
init|=
name|createUnchecked
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|reclaim
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Error creating new connection when managing connection pool, ignoring"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|available
operator|.
name|size
argument_list|()
operator|>
name|maxIdleConnections
condition|)
block|{
name|PoolAwareConnection
name|c
init|=
name|uncheckNonBlocking
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|retire
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** 	 * Closes the connection and removes it from the pool. The connection must 	 * be an unchecked connection. 	 */
name|void
name|retire
parameter_list|(
name|PoolAwareConnection
name|connection
parameter_list|)
block|{
name|pool
operator|.
name|remove
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|poolCap
operator|.
name|release
argument_list|()
expr_stmt|;
try|try
block|{
name|connection
operator|.
name|getConnection
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// ignore?
block|}
block|}
comment|/** 	 * Returns connection back to the pool if possible. The connection must be 	 * an unchecked connection. 	 */
name|void
name|reclaim
parameter_list|(
name|PoolAwareConnection
name|connection
parameter_list|)
block|{
comment|// TODO: rollback any in-process tx?
comment|// the queue may overflow potentially and we won't be able to add the
comment|// object
if|if
condition|(
operator|!
name|available
operator|.
name|offer
argument_list|(
name|connection
argument_list|)
condition|)
block|{
name|retire
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
block|}
name|PoolAwareConnection
name|uncheckNonBlocking
parameter_list|(
name|boolean
name|validate
parameter_list|)
block|{
name|PoolAwareConnection
name|c
init|=
name|available
operator|.
name|poll
argument_list|()
decl_stmt|;
return|return
name|validate
condition|?
name|validateUnchecked
argument_list|(
name|c
argument_list|)
else|:
name|c
return|;
block|}
name|PoolAwareConnection
name|uncheckBlocking
parameter_list|(
name|boolean
name|validate
parameter_list|)
block|{
name|PoolAwareConnection
name|c
decl_stmt|;
try|try
block|{
name|c
operator|=
name|available
operator|.
name|poll
argument_list|(
name|maxQueueWaitTime
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|validate
condition|?
name|validateUnchecked
argument_list|(
name|c
argument_list|)
else|:
name|c
return|;
block|}
name|PoolAwareConnection
name|validateUnchecked
parameter_list|(
name|PoolAwareConnection
name|c
parameter_list|)
block|{
if|if
condition|(
name|c
operator|==
literal|null
operator|||
name|c
operator|.
name|validate
argument_list|()
condition|)
block|{
return|return
name|c
return|;
block|}
comment|// this will recursively validate all connections that exist in the pool
comment|// until a valid one is found or a pool is exhausted
name|retire
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|validateUnchecked
argument_list|(
name|available
operator|.
name|poll
argument_list|()
argument_list|)
return|;
block|}
name|PoolAwareConnection
name|createUnchecked
parameter_list|()
throws|throws
name|SQLException
block|{
if|if
condition|(
operator|!
name|poolCap
operator|.
name|tryAcquire
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|PoolAwareConnection
name|c
init|=
name|createWrapped
argument_list|()
decl_stmt|;
name|pool
operator|.
name|put
argument_list|(
name|c
argument_list|,
literal|1
argument_list|)
expr_stmt|;
comment|// even though we got a fresh connection, let's still validate it...
comment|// This will provide consistent behavior between cached and uncached
comment|// connections in respect to invalid validation queries
if|if
condition|(
operator|!
name|c
operator|.
name|validate
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadValidationQueryException
argument_list|(
literal|"Can't validate a fresh connection. Likely validation query is wrong: "
operator|+
name|validationQuery
argument_list|)
throw|;
block|}
return|return
name|c
return|;
block|}
name|PoolAwareConnection
name|createWrapped
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
operator|new
name|PoolAwareConnection
argument_list|(
name|this
argument_list|,
name|createUnwrapped
argument_list|()
argument_list|,
name|validationQuery
argument_list|)
return|;
block|}
comment|/** 	 * Creates a new connection. 	 */
name|Connection
name|createUnwrapped
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|nonPoolingDataSource
operator|.
name|getConnection
argument_list|()
return|;
block|}
comment|/** 	 * Updates connection state to a default state. 	 */
name|Connection
name|resetState
parameter_list|(
name|Connection
name|c
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// TODO: tx isolation level?
if|if
condition|(
operator|!
name|c
operator|.
name|getAutoCommit
argument_list|()
condition|)
block|{
try|try
block|{
name|c
operator|.
name|setAutoCommit
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|UnmanagedPoolingDataSource
operator|.
name|sybaseAutoCommitPatch
argument_list|(
name|c
argument_list|,
name|e
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|c
operator|.
name|clearWarnings
argument_list|()
expr_stmt|;
return|return
name|c
return|;
block|}
annotation|@
name|Override
specifier|public
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|SQLException
block|{
comment|// strategy for getting a connection -
comment|// 1. quick peek for available connections
comment|// 2. create new one
comment|// 3. wait for a user to return connection
name|PoolAwareConnection
name|c
decl_stmt|;
name|c
operator|=
name|uncheckNonBlocking
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
return|return
name|resetState
argument_list|(
name|c
argument_list|)
return|;
block|}
name|c
operator|=
name|createUnchecked
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
return|return
name|resetState
argument_list|(
name|c
argument_list|)
return|;
block|}
name|c
operator|=
name|uncheckBlocking
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
return|return
name|resetState
argument_list|(
name|c
argument_list|)
return|;
block|}
throw|throw
operator|new
name|ConnectionUnavailableException
argument_list|(
literal|"Can't obtain connection. Request to pool timed out. Total pool size: "
operator|+
name|pool
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Connection
name|getConnection
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|SQLException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Connections for a specific user are not supported by the pooled DataSource"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getLoginTimeout
parameter_list|()
throws|throws
name|java
operator|.
name|sql
operator|.
name|SQLException
block|{
return|return
name|nonPoolingDataSource
operator|.
name|getLoginTimeout
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLoginTimeout
parameter_list|(
name|int
name|seconds
parameter_list|)
throws|throws
name|java
operator|.
name|sql
operator|.
name|SQLException
block|{
name|nonPoolingDataSource
operator|.
name|setLoginTimeout
argument_list|(
name|seconds
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|PrintWriter
name|getLogWriter
parameter_list|()
throws|throws
name|java
operator|.
name|sql
operator|.
name|SQLException
block|{
return|return
name|nonPoolingDataSource
operator|.
name|getLogWriter
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLogWriter
parameter_list|(
name|PrintWriter
name|out
parameter_list|)
throws|throws
name|java
operator|.
name|sql
operator|.
name|SQLException
block|{
name|nonPoolingDataSource
operator|.
name|setLogWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isWrapperFor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|iface
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
operator|(
name|UnmanagedPoolingDataSource
operator|.
name|class
operator|.
name|equals
argument_list|(
name|iface
argument_list|)
operator|)
condition|?
literal|true
else|:
name|nonPoolingDataSource
operator|.
name|isWrapperFor
argument_list|(
name|iface
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|unwrap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|iface
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|UnmanagedPoolingDataSource
operator|.
name|class
operator|.
name|equals
argument_list|(
name|iface
argument_list|)
condition|?
operator|(
name|T
operator|)
name|this
else|:
name|nonPoolingDataSource
operator|.
name|unwrap
argument_list|(
name|iface
argument_list|)
return|;
block|}
comment|// JDBC 4.1 compatibility under Java pre 1.7
specifier|public
name|Logger
name|getParentLogger
parameter_list|()
throws|throws
name|SQLFeatureNotSupportedException
block|{
throw|throw
operator|new
name|SQLFeatureNotSupportedException
argument_list|()
throw|;
block|}
name|String
name|getValidationQuery
parameter_list|()
block|{
return|return
name|validationQuery
return|;
block|}
name|long
name|getMaxQueueWaitTime
parameter_list|()
block|{
return|return
name|maxQueueWaitTime
return|;
block|}
name|int
name|getMaxIdleConnections
parameter_list|()
block|{
return|return
name|maxIdleConnections
return|;
block|}
name|int
name|getMinConnections
parameter_list|()
block|{
return|return
name|minConnections
return|;
block|}
name|int
name|getMaxConnections
parameter_list|()
block|{
return|return
name|maxConnections
return|;
block|}
block|}
end_class

end_unit

