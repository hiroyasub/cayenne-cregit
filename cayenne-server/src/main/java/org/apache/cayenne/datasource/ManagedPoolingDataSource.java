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
name|di
operator|.
name|ScopeEventListener
import|;
end_import

begin_comment
comment|/**  * A wrapper for {@link PoolingDataSourceManager} that manages the underlying  * connection pool size.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ManagedPoolingDataSource
implements|implements
name|DataSource
implements|,
name|ScopeEventListener
block|{
specifier|private
name|PoolingDataSourceManager
name|dataSourceManager
decl_stmt|;
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
specifier|public
name|ManagedPoolingDataSource
parameter_list|(
name|PoolingDataSource
name|dataSource
parameter_list|)
block|{
comment|// wake every 2 minutes...
name|this
argument_list|(
name|dataSource
argument_list|,
literal|120000
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ManagedPoolingDataSource
parameter_list|(
name|PoolingDataSource
name|dataSource
parameter_list|,
name|long
name|managerWakeTime
parameter_list|)
block|{
name|this
operator|.
name|dataSource
operator|=
name|dataSource
expr_stmt|;
name|this
operator|.
name|dataSourceManager
operator|=
operator|new
name|PoolingDataSourceManager
argument_list|(
name|dataSource
argument_list|,
name|managerWakeTime
argument_list|)
expr_stmt|;
name|dataSourceManager
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|PoolingDataSourceManager
name|getDataSourceManager
parameter_list|()
block|{
return|return
name|dataSourceManager
return|;
block|}
comment|/** 	 * Calls {@link #shutdown()} to drain the underlying pool, close open 	 * connections and block the DataSource from creating any new connections. 	 */
annotation|@
name|Override
specifier|public
name|void
name|beforeScopeEnd
parameter_list|()
block|{
name|shutdown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
comment|// swap the underlying DataSource to prevent further interaction with
comment|// the callers
name|this
operator|.
name|dataSource
operator|=
operator|new
name|StoppedDataSource
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
comment|// shut down the thread..
name|this
operator|.
name|dataSourceManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
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
return|return
name|dataSource
operator|.
name|getConnection
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Connection
name|getConnection
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|dataSource
operator|.
name|getConnection
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|PrintWriter
name|getLogWriter
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|dataSource
operator|.
name|getLogWriter
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getLoginTimeout
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|dataSource
operator|.
name|getLoginTimeout
argument_list|()
return|;
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
name|ManagedPoolingDataSource
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
name|dataSource
operator|.
name|isWrapperFor
argument_list|(
name|iface
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLogWriter
parameter_list|(
name|PrintWriter
name|arg0
parameter_list|)
throws|throws
name|SQLException
block|{
name|dataSource
operator|.
name|setLogWriter
argument_list|(
name|arg0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLoginTimeout
parameter_list|(
name|int
name|arg0
parameter_list|)
throws|throws
name|SQLException
block|{
name|dataSource
operator|.
name|setLoginTimeout
argument_list|(
name|arg0
argument_list|)
expr_stmt|;
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
name|ManagedPoolingDataSource
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
name|dataSource
operator|.
name|unwrap
argument_list|(
name|iface
argument_list|)
return|;
block|}
comment|// JDBC 4.1 compatibility under Java 1.6 and newer
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
block|}
end_class

end_unit

