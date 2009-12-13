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
name|access
operator|.
name|ConnectionLogger
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
name|access
operator|.
name|QueryLogger
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
name|conn
operator|.
name|PoolManager
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
name|cayenne
operator|.
name|resource
operator|.
name|ResourceLocator
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
comment|/**  * A {@link DataSourceFactory} that loads JDBC connection information from an XML resource  * associated with the DataNodeDescriptor, returning a DataSource with simple connection  * pooling.  *   * @since 3.1  */
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
specifier|protected
name|ResourceLocator
name|resourceLocator
decl_stmt|;
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
name|dataSourceDescriptor
init|=
name|nodeDescriptor
operator|.
name|getDataSourceDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataSourceDescriptor
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
name|ConnectionLogger
name|logger
init|=
operator|new
name|ConnectionLogger
argument_list|()
decl_stmt|;
try|try
block|{
return|return
operator|new
name|PoolManager
argument_list|(
name|dataSourceDescriptor
operator|.
name|getJdbcDriver
argument_list|()
argument_list|,
name|dataSourceDescriptor
operator|.
name|getDataSourceUrl
argument_list|()
argument_list|,
name|dataSourceDescriptor
operator|.
name|getMinConnections
argument_list|()
argument_list|,
name|dataSourceDescriptor
operator|.
name|getMaxConnections
argument_list|()
argument_list|,
name|dataSourceDescriptor
operator|.
name|getUserName
argument_list|()
argument_list|,
name|dataSourceDescriptor
operator|.
name|getPassword
argument_list|()
argument_list|,
name|logger
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|QueryLogger
operator|.
name|logConnectFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
end_class

end_unit

