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
name|configuration
operator|.
name|server
package|;
end_package

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
name|ConfigurationException
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
name|configuration
operator|.
name|Constants
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
name|configuration
operator|.
name|DataNodeDescriptor
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
name|configuration
operator|.
name|RuntimeProperties
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
name|conn
operator|.
name|DataSourceInfo
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
name|datasource
operator|.
name|DataSourceBuilder
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
name|datasource
operator|.
name|PoolingDataSource
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
name|AdhocObjectFactory
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
name|Inject
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
comment|/**  * A {@link DataSourceFactory} that loads JDBC connection information from an  * XML resource associated with the DataNodeDescriptor, returning a DataSource  * with simple connection pooling.  *   * @since 3.1  */
end_comment

begin_comment
comment|// TODO: this factory does not read XML anymore, should we rename it to
end_comment

begin_comment
comment|// something else?
end_comment

begin_class
specifier|public
class|class
name|XMLPoolingDataSourceFactory
implements|implements
name|DataSourceFactory
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|XMLPoolingDataSourceFactory
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RuntimeProperties
name|properties
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
annotation|@
name|Override
specifier|public
name|DataSource
name|getDataSource
parameter_list|(
name|DataNodeDescriptor
name|nodeDescriptor
parameter_list|)
throws|throws
name|Exception
block|{
name|DataSourceInfo
name|descriptor
init|=
name|nodeDescriptor
operator|.
name|getDataSourceDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|descriptor
operator|==
literal|null
condition|)
block|{
name|String
name|message
init|=
literal|"Null dataSourceDescriptor for nodeDescriptor '"
operator|+
name|nodeDescriptor
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ConfigurationException
argument_list|(
name|message
argument_list|)
throw|;
block|}
name|long
name|maxQueueWaitTime
init|=
name|properties
operator|.
name|getLong
argument_list|(
name|Constants
operator|.
name|SERVER_MAX_QUEUE_WAIT_TIME
argument_list|,
name|PoolingDataSource
operator|.
name|MAX_QUEUE_WAIT_DEFAULT
argument_list|)
decl_stmt|;
return|return
name|DataSourceBuilder
operator|.
name|builder
argument_list|(
name|objectFactory
argument_list|)
operator|.
name|driver
argument_list|(
name|descriptor
operator|.
name|getJdbcDriver
argument_list|()
argument_list|)
operator|.
name|url
argument_list|(
name|descriptor
operator|.
name|getDataSourceUrl
argument_list|()
argument_list|)
operator|.
name|userName
argument_list|(
name|descriptor
operator|.
name|getUserName
argument_list|()
argument_list|)
operator|.
name|password
argument_list|(
name|descriptor
operator|.
name|getPassword
argument_list|()
argument_list|)
operator|.
name|minConnections
argument_list|(
name|descriptor
operator|.
name|getMinConnections
argument_list|()
argument_list|)
operator|.
name|maxConnections
argument_list|(
name|descriptor
operator|.
name|getMaxConnections
argument_list|()
argument_list|)
operator|.
name|maxQueueWaitTime
argument_list|(
name|maxQueueWaitTime
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

