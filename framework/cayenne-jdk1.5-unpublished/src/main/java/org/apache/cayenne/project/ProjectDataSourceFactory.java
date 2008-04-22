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
name|project
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|conf
operator|.
name|DriverDataSourceFactory
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

begin_comment
comment|/**  * Factory of DataSource objects used by the project model. Always tries to locate file  * with direct connection info.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ProjectDataSourceFactory
extends|extends
name|DriverDataSourceFactory
block|{
specifier|protected
name|File
name|projectDir
decl_stmt|;
specifier|public
name|ProjectDataSourceFactory
parameter_list|(
name|File
name|projectDir
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|projectDir
operator|=
name|projectDir
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|DataSource
name|getDataSource
parameter_list|(
name|String
name|location
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|this
operator|.
name|load
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|e
parameter_list|)
block|{
comment|// ignoring
block|}
return|return
operator|new
name|ProjectDataSource
argument_list|(
name|getDriverInfo
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|DataSourceInfo
name|getDriverInfo
parameter_list|()
block|{
name|DataSourceInfo
name|info
init|=
name|super
operator|.
name|getDriverInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
name|info
operator|=
operator|new
name|DataSourceInfo
argument_list|()
expr_stmt|;
block|}
return|return
name|info
return|;
block|}
block|}
end_class

end_unit

