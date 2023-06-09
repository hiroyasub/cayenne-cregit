begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbsync
operator|.
name|reverse
operator|.
name|configuration
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Driver
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
name|DataSourceDescriptor
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
name|server
operator|.
name|DataSourceFactory
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
name|DriverDataSource
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DriverDataSourceFactory
implements|implements
name|DataSourceFactory
block|{
specifier|private
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
specifier|public
name|DriverDataSourceFactory
parameter_list|(
annotation|@
name|Inject
name|AdhocObjectFactory
name|objectFactory
parameter_list|)
block|{
name|this
operator|.
name|objectFactory
operator|=
name|objectFactory
expr_stmt|;
block|}
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
name|DataSourceDescriptor
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
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"'nodeDescriptor' contains no datasource descriptor"
argument_list|)
throw|;
block|}
name|Driver
name|driver
init|=
operator|(
name|Driver
operator|)
name|objectFactory
operator|.
name|getJavaClass
argument_list|(
name|dataSourceDescriptor
operator|.
name|getJdbcDriver
argument_list|()
argument_list|)
operator|.
name|getDeclaredConstructor
argument_list|()
operator|.
name|newInstance
argument_list|()
decl_stmt|;
return|return
operator|new
name|DriverDataSource
argument_list|(
name|driver
argument_list|,
name|dataSourceDescriptor
operator|.
name|getDataSourceUrl
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
argument_list|)
return|;
block|}
block|}
end_class

end_unit

