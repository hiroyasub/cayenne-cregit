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
name|conn
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
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|ConnectionEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|ConnectionEventListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|ConnectionPoolDataSource
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
name|javax
operator|.
name|sql
operator|.
name|PooledConnection
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
name|di
operator|.
name|BeforeScopeEnd
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
name|log
operator|.
name|JdbcEventLogger
import|;
end_import

begin_comment
comment|/**  * PoolManager is a Cayenne implementation of a pooling DataSource.  */
end_comment

begin_class
specifier|public
class|class
name|PoolManager
implements|implements
name|DataSource
implements|,
name|ConnectionEventListener
block|{
comment|/**      * Defines a maximum time in milliseconds that a connection request could wait in the      * connection queue. After this period expires, an exception will be thrown in the      * calling method. In the future this parameter should be made configurable.      */
specifier|public
specifier|static
specifier|final
name|int
name|MAX_QUEUE_WAIT
init|=
literal|20000
decl_stmt|;
specifier|protected
name|ConnectionPoolDataSource
name|poolDataSource
decl_stmt|;
specifier|protected
name|int
name|minConnections
decl_stmt|;
specifier|protected
name|int
name|maxConnections
decl_stmt|;
specifier|protected
name|String
name|dataSourceUrl
decl_stmt|;
specifier|protected
name|String
name|jdbcDriver
decl_stmt|;
specifier|protected
name|String
name|password
decl_stmt|;
specifier|protected
name|String
name|userName
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|PooledConnection
argument_list|>
name|unusedPool
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|PooledConnection
argument_list|>
name|usedPool
decl_stmt|;
specifier|private
name|PoolMaintenanceThread
name|poolMaintenanceThread
decl_stmt|;
specifier|private
name|boolean
name|shuttingDown
decl_stmt|;
comment|/**      * Creates new PoolManager using org.apache.cayenne.conn.PoolDataSource for an      * underlying ConnectionPoolDataSource.      */
specifier|public
name|PoolManager
parameter_list|(
name|String
name|jdbcDriver
parameter_list|,
name|String
name|dataSourceUrl
parameter_list|,
name|int
name|minCons
parameter_list|,
name|int
name|maxCons
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|SQLException
block|{
name|this
argument_list|(
name|jdbcDriver
argument_list|,
name|dataSourceUrl
argument_list|,
name|minCons
argument_list|,
name|maxCons
argument_list|,
name|userName
argument_list|,
name|password
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PoolManager
parameter_list|(
name|String
name|jdbcDriver
parameter_list|,
name|String
name|dataSourceUrl
parameter_list|,
name|int
name|minCons
parameter_list|,
name|int
name|maxCons
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|,
name|JdbcEventLogger
name|logger
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|logger
operator|!=
literal|null
condition|)
block|{
name|DataSourceInfo
name|info
init|=
operator|new
name|DataSourceInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|setJdbcDriver
argument_list|(
name|jdbcDriver
argument_list|)
expr_stmt|;
name|info
operator|.
name|setDataSourceUrl
argument_list|(
name|dataSourceUrl
argument_list|)
expr_stmt|;
name|info
operator|.
name|setMinConnections
argument_list|(
name|minCons
argument_list|)
expr_stmt|;
name|info
operator|.
name|setMaxConnections
argument_list|(
name|maxCons
argument_list|)
expr_stmt|;
name|info
operator|.
name|setUserName
argument_list|(
name|userName
argument_list|)
expr_stmt|;
name|info
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|logger
operator|.
name|logPoolCreated
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|jdbcDriver
operator|=
name|jdbcDriver
expr_stmt|;
name|this
operator|.
name|dataSourceUrl
operator|=
name|dataSourceUrl
expr_stmt|;
name|DriverDataSource
name|driverDS
init|=
operator|new
name|DriverDataSource
argument_list|(
name|jdbcDriver
argument_list|,
name|dataSourceUrl
argument_list|)
decl_stmt|;
name|driverDS
operator|.
name|setLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
name|PoolDataSource
name|poolDS
init|=
operator|new
name|PoolDataSource
argument_list|(
name|driverDS
argument_list|)
decl_stmt|;
name|init
argument_list|(
name|poolDS
argument_list|,
name|minCons
argument_list|,
name|maxCons
argument_list|,
name|userName
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates new PoolManager with the specified policy for connection pooling and a      * ConnectionPoolDataSource object.      *       * @param poolDataSource data source for pooled connections      * @param minCons Non-negative integer that specifies a minimum number of open      *            connections to keep in the pool at all times      * @param maxCons Non-negative integer that specifies maximum number of simultaneuosly      *            open connections      * @throws SQLException if pool manager can not be created.      */
specifier|public
name|PoolManager
parameter_list|(
name|ConnectionPoolDataSource
name|poolDataSource
parameter_list|,
name|int
name|minCons
parameter_list|,
name|int
name|maxCons
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|SQLException
block|{
name|init
argument_list|(
name|poolDataSource
argument_list|,
name|minCons
argument_list|,
name|maxCons
argument_list|,
name|userName
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
comment|/** Initializes pool. Normally called from constructor. */
specifier|protected
name|void
name|init
parameter_list|(
name|ConnectionPoolDataSource
name|poolDataSource
parameter_list|,
name|int
name|minCons
parameter_list|,
name|int
name|maxCons
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// do sanity checks...
if|if
condition|(
name|maxConnections
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Maximum number of connections can not be negative ("
operator|+
name|maxCons
operator|+
literal|")."
argument_list|)
throw|;
block|}
if|if
condition|(
name|minConnections
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Minimum number of connections can not be negative ("
operator|+
name|minCons
operator|+
literal|")."
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
name|SQLException
argument_list|(
literal|"Minimum number of connections can not be bigger then maximum."
argument_list|)
throw|;
block|}
comment|// init properties
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
name|this
operator|.
name|minConnections
operator|=
name|minCons
expr_stmt|;
name|this
operator|.
name|maxConnections
operator|=
name|maxCons
expr_stmt|;
name|this
operator|.
name|poolDataSource
operator|=
name|poolDataSource
expr_stmt|;
comment|// init pool... use linked lists to use the queue in the FIFO manner
name|usedPool
operator|=
operator|new
name|LinkedList
argument_list|<
name|PooledConnection
argument_list|>
argument_list|()
expr_stmt|;
name|unusedPool
operator|=
operator|new
name|LinkedList
argument_list|<
name|PooledConnection
argument_list|>
argument_list|()
expr_stmt|;
name|growPool
argument_list|(
name|minConnections
argument_list|,
name|userName
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|startMaintenanceThread
argument_list|()
expr_stmt|;
block|}
specifier|protected
specifier|synchronized
name|void
name|startMaintenanceThread
parameter_list|()
block|{
name|disposeOfMaintenanceThread
argument_list|()
expr_stmt|;
name|this
operator|.
name|poolMaintenanceThread
operator|=
operator|new
name|PoolMaintenanceThread
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|poolMaintenanceThread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates and returns new PooledConnection object, adding itself as a listener for      * connection events.      */
specifier|protected
name|PooledConnection
name|newPooledConnection
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
name|PooledConnection
name|connection
init|=
operator|(
name|userName
operator|!=
literal|null
operator|)
condition|?
name|poolDataSource
operator|.
name|getPooledConnection
argument_list|(
name|userName
argument_list|,
name|password
argument_list|)
else|:
name|poolDataSource
operator|.
name|getPooledConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|addConnectionEventListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|connection
return|;
block|}
comment|/**      * Closes all existing connections, removes them from the pool.      *       * @deprecated since 3.1 replaced with {@link #shutdown()} method for naming      *             consistency.      */
specifier|public
name|void
name|dispose
parameter_list|()
throws|throws
name|SQLException
block|{
name|shutdown
argument_list|()
expr_stmt|;
block|}
comment|/**      * Closes all existing connections, drains the pool and stops the maintenance thread.      *       * @since 3.1      */
annotation|@
name|BeforeScopeEnd
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|SQLException
block|{
comment|// disposing maintenance thread first to avoid any changes to pools
comment|// during shutdown
name|disposeOfMaintenanceThread
argument_list|()
expr_stmt|;
comment|// using boolean variable instead of locking PoolManager instance due to
comment|// possible deadlock during shutdown when one of connections locks its
comment|// event listeners list trying to invoke locked PoolManager's listener methods
name|shuttingDown
operator|=
literal|true
expr_stmt|;
name|ListIterator
argument_list|<
name|PooledConnection
argument_list|>
name|unusedIterator
init|=
name|unusedPool
operator|.
name|listIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|unusedIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|PooledConnection
name|con
init|=
name|unusedIterator
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// close connection
name|con
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// remove connection from the list
name|unusedIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
comment|// clean used connections
name|ListIterator
argument_list|<
name|PooledConnection
argument_list|>
name|usedIterator
init|=
name|usedPool
operator|.
name|listIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|usedIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|PooledConnection
name|con
init|=
name|usedIterator
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// stop listening for connection events
name|con
operator|.
name|removeConnectionEventListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// close connection
name|con
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// remove connection from the list
name|usedIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|disposeOfMaintenanceThread
parameter_list|()
block|{
if|if
condition|(
name|poolMaintenanceThread
operator|!=
literal|null
condition|)
block|{
name|poolMaintenanceThread
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|poolMaintenanceThread
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * @return true if at least one more connection can be added to the pool.      */
specifier|protected
specifier|synchronized
name|boolean
name|canGrowPool
parameter_list|()
block|{
return|return
name|getPoolSize
argument_list|()
operator|<
name|maxConnections
return|;
block|}
comment|/**      * Increases connection pool by the specified number of connections.      *       * @return the actual number of created connections.      * @throws SQLException if an error happens when creating a new connection.      */
specifier|protected
specifier|synchronized
name|int
name|growPool
parameter_list|(
name|int
name|addConnections
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|SQLException
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
name|int
name|startPoolSize
init|=
name|getPoolSize
argument_list|()
decl_stmt|;
for|for
control|(
init|;
name|i
operator|<
name|addConnections
operator|&&
name|startPoolSize
operator|+
name|i
operator|<
name|maxConnections
condition|;
name|i
operator|++
control|)
block|{
name|PooledConnection
name|newConnection
init|=
name|newPooledConnection
argument_list|(
name|userName
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|unusedPool
operator|.
name|add
argument_list|(
name|newConnection
argument_list|)
expr_stmt|;
block|}
return|return
name|i
return|;
block|}
specifier|protected
specifier|synchronized
name|void
name|shrinkPool
parameter_list|(
name|int
name|closeConnections
parameter_list|)
block|{
name|int
name|idleSize
init|=
name|unusedPool
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|closeConnections
operator|&&
name|i
operator|<
name|idleSize
condition|;
name|i
operator|++
control|)
block|{
name|PooledConnection
name|con
init|=
name|unusedPool
operator|.
name|remove
argument_list|(
name|i
argument_list|)
decl_stmt|;
try|try
block|{
name|con
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
comment|/**      * Returns maximum number of connections this pool can keep. This parameter when      * configured allows to limit the number of simultaneously open connections.      */
specifier|public
name|int
name|getMaxConnections
parameter_list|()
block|{
return|return
name|maxConnections
return|;
block|}
specifier|public
name|void
name|setMaxConnections
parameter_list|(
name|int
name|maxConnections
parameter_list|)
block|{
name|this
operator|.
name|maxConnections
operator|=
name|maxConnections
expr_stmt|;
block|}
comment|/**      * Returns the absolute minimum number of connections allowed in this pool at any      * moment in time.      */
specifier|public
name|int
name|getMinConnections
parameter_list|()
block|{
return|return
name|minConnections
return|;
block|}
specifier|public
name|void
name|setMinConnections
parameter_list|(
name|int
name|minConnections
parameter_list|)
block|{
name|this
operator|.
name|minConnections
operator|=
name|minConnections
expr_stmt|;
block|}
comment|/**      * Returns a database URL used to initialize this pool. Will return null if the pool      * was initialized with ConnectionPoolDataSource.      */
specifier|public
name|String
name|getDataSourceUrl
parameter_list|()
block|{
return|return
name|dataSourceUrl
return|;
block|}
comment|/**      * Returns a name of a JDBC driver used to initialize this pool. Will return null if      * the pool was initialized with ConnectionPoolDataSource.      */
specifier|public
name|String
name|getJdbcDriver
parameter_list|()
block|{
return|return
name|jdbcDriver
return|;
block|}
comment|/** Returns a data source password used to initialize this pool. */
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/** Returns a data source user name used to initialize this pool. */
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
comment|/**      * Returns current number of connections.      */
specifier|public
specifier|synchronized
name|int
name|getPoolSize
parameter_list|()
block|{
return|return
name|usedPool
operator|.
name|size
argument_list|()
operator|+
name|unusedPool
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Returns the number of connections obtained via this DataSource that are currently      * in use by the DataSource clients.      */
specifier|public
specifier|synchronized
name|int
name|getCurrentlyInUse
parameter_list|()
block|{
return|return
name|usedPool
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Returns the number of connections maintained in the pool that are currently not      * used by any clients and are available immediately via<code>getConnection</code>      * method.      */
specifier|public
specifier|synchronized
name|int
name|getCurrentlyUnused
parameter_list|()
block|{
return|return
name|unusedPool
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Returns connection from the pool using internal values of user name and password.      * Equivalent to calling:      *<p>      *<code>ds.getConnection(ds.getUserName(), ds.getPassword())</code>      *</p>      */
specifier|public
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|getConnection
argument_list|(
name|userName
argument_list|,
name|password
argument_list|)
return|;
block|}
comment|/** Returns connection from the pool. */
specifier|public
specifier|synchronized
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
if|if
condition|(
name|shuttingDown
condition|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Pool manager is shutting down."
argument_list|)
throw|;
block|}
name|PooledConnection
name|pooledConnection
init|=
name|uncheckPooledConnection
argument_list|(
name|userName
argument_list|,
name|password
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|uncheckConnection
argument_list|(
name|pooledConnection
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
try|try
block|{
name|pooledConnection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ignored
parameter_list|)
block|{
block|}
comment|// do one reconnect attempt...
name|pooledConnection
operator|=
name|uncheckPooledConnection
argument_list|(
name|userName
argument_list|,
name|password
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|uncheckConnection
argument_list|(
name|pooledConnection
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|reconnectEx
parameter_list|)
block|{
try|try
block|{
name|pooledConnection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ignored
parameter_list|)
block|{
block|}
throw|throw
name|reconnectEx
throw|;
block|}
block|}
block|}
specifier|private
name|Connection
name|uncheckConnection
parameter_list|(
name|PooledConnection
name|pooledConnection
parameter_list|)
throws|throws
name|SQLException
block|{
name|Connection
name|c
init|=
name|pooledConnection
operator|.
name|getConnection
argument_list|()
decl_stmt|;
comment|// only do that on successfully unchecked connection...
name|usedPool
operator|.
name|add
argument_list|(
name|pooledConnection
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
specifier|private
name|PooledConnection
name|uncheckPooledConnection
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
comment|// wait for returned connections or the maintenance thread
comment|// to bump the pool size...
if|if
condition|(
name|unusedPool
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// first try to open a new connection
if|if
condition|(
name|canGrowPool
argument_list|()
condition|)
block|{
return|return
name|newPooledConnection
argument_list|(
name|userName
argument_list|,
name|password
argument_list|)
return|;
block|}
comment|// can't open no more... will have to wait for others to return a connection
comment|// note that if we were woken up
comment|// before the full wait period expired, and no connections are
comment|// available yet, go back to sleep. Otherwise we don't give a maintenance
comment|// thread a chance to increase pool size
name|long
name|waitTill
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
name|MAX_QUEUE_WAIT
decl_stmt|;
do|do
block|{
try|try
block|{
name|wait
argument_list|(
name|MAX_QUEUE_WAIT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|iex
parameter_list|)
block|{
comment|// ignoring
block|}
block|}
do|while
condition|(
name|unusedPool
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|&&
name|waitTill
operator|>
name|System
operator|.
name|currentTimeMillis
argument_list|()
condition|)
do|;
if|if
condition|(
name|unusedPool
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Can't obtain connection. Request timed out. Total used connections: "
operator|+
name|usedPool
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|// get first connection... lets cycle them in FIFO manner
return|return
name|unusedPool
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
return|;
block|}
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
name|poolDataSource
operator|.
name|getLoginTimeout
argument_list|()
return|;
block|}
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
name|poolDataSource
operator|.
name|setLoginTimeout
argument_list|(
name|seconds
argument_list|)
expr_stmt|;
block|}
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
name|poolDataSource
operator|.
name|getLogWriter
argument_list|()
return|;
block|}
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
name|poolDataSource
operator|.
name|setLogWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns closed connection to the pool.      */
specifier|public
specifier|synchronized
name|void
name|connectionClosed
parameter_list|(
name|ConnectionEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|shuttingDown
condition|)
block|{
return|return;
block|}
comment|// return connection to the pool
name|PooledConnection
name|closedConn
init|=
operator|(
name|PooledConnection
operator|)
name|event
operator|.
name|getSource
argument_list|()
decl_stmt|;
comment|// remove this connection from the list of connections
comment|// managed by this pool...
name|int
name|usedInd
init|=
name|usedPool
operator|.
name|indexOf
argument_list|(
name|closedConn
argument_list|)
decl_stmt|;
if|if
condition|(
name|usedInd
operator|>=
literal|0
condition|)
block|{
name|usedPool
operator|.
name|remove
argument_list|(
name|usedInd
argument_list|)
expr_stmt|;
name|unusedPool
operator|.
name|add
argument_list|(
name|closedConn
argument_list|)
expr_stmt|;
comment|// notify threads waiting for connections
name|notifyAll
argument_list|()
expr_stmt|;
block|}
comment|// else ....
comment|// other possibility is that this is a bad connection, so just ignore its closing
comment|// event,
comment|// since it was unregistered in "connectionErrorOccurred"
block|}
comment|/**      * Removes connection with an error from the pool. This method is called by      * PoolManager connections on connection errors to notify PoolManager that connection      * is in invalid state.      */
specifier|public
specifier|synchronized
name|void
name|connectionErrorOccurred
parameter_list|(
name|ConnectionEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|shuttingDown
condition|)
block|{
return|return;
block|}
comment|// later on we should analyze the error to see if this
comment|// is fatal... right now just kill this PooledConnection
name|PooledConnection
name|errorSrc
init|=
operator|(
name|PooledConnection
operator|)
name|event
operator|.
name|getSource
argument_list|()
decl_stmt|;
comment|// remove this connection from the list of connections
comment|// managed by this pool...
name|int
name|usedInd
init|=
name|usedPool
operator|.
name|indexOf
argument_list|(
name|errorSrc
argument_list|)
decl_stmt|;
if|if
condition|(
name|usedInd
operator|>=
literal|0
condition|)
block|{
name|usedPool
operator|.
name|remove
argument_list|(
name|usedInd
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|unusedInd
init|=
name|unusedPool
operator|.
name|indexOf
argument_list|(
name|errorSrc
argument_list|)
decl_stmt|;
if|if
condition|(
name|unusedInd
operator|>=
literal|0
condition|)
name|unusedPool
operator|.
name|remove
argument_list|(
name|unusedInd
argument_list|)
expr_stmt|;
block|}
comment|// do not close connection,
comment|// let the code that catches the exception handle it
comment|// ....
block|}
specifier|static
class|class
name|PoolMaintenanceThread
extends|extends
name|Thread
block|{
specifier|private
name|boolean
name|shouldDie
decl_stmt|;
specifier|private
name|PoolManager
name|pool
decl_stmt|;
name|PoolMaintenanceThread
parameter_list|(
name|PoolManager
name|pool
parameter_list|)
block|{
name|super
operator|.
name|setName
argument_list|(
literal|"PoolManagerCleanup-"
operator|+
name|pool
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|pool
operator|=
name|pool
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// periodically wakes up to check if the pool should grow or shrink
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
comment|// don't do it too often
name|sleep
argument_list|(
literal|600000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|iex
parameter_list|)
block|{
comment|// ignore...
block|}
if|if
condition|(
name|shouldDie
condition|)
block|{
break|break;
block|}
synchronized|synchronized
init|(
name|pool
init|)
block|{
comment|// TODO: implement a smarter algorithm for pool management...
comment|// right now it will simply close one connection if the count is
comment|// above median and there are any idle connections.
name|int
name|unused
init|=
name|pool
operator|.
name|getCurrentlyUnused
argument_list|()
decl_stmt|;
name|int
name|used
init|=
name|pool
operator|.
name|getCurrentlyInUse
argument_list|()
decl_stmt|;
name|int
name|total
init|=
name|unused
operator|+
name|used
decl_stmt|;
name|int
name|median
init|=
name|pool
operator|.
name|minConnections
operator|+
literal|1
operator|+
operator|(
name|pool
operator|.
name|maxConnections
operator|-
name|pool
operator|.
name|minConnections
operator|)
operator|/
literal|2
decl_stmt|;
if|if
condition|(
name|unused
operator|>
literal|0
operator|&&
name|total
operator|>
name|median
condition|)
block|{
name|pool
operator|.
name|shrinkPool
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**          * Stops the maintenance thread.          */
name|void
name|shutdown
parameter_list|()
block|{
name|shouldDie
operator|=
literal|true
expr_stmt|;
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @since 3.0      */
comment|// JDBC 4 compatibility under Java 1.5
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**      * @since 3.0      */
comment|// JDBC 4 compatibility under Java 1.5
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

