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
name|unit
operator|.
name|testcontainers
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dba
operator|.
name|JdbcAdapter
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
name|dba
operator|.
name|sqlserver
operator|.
name|SQLServerAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|containers
operator|.
name|JdbcDatabaseContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|containers
operator|.
name|MSSQLServerContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|utility
operator|.
name|DockerImageName
import|;
end_import

begin_class
specifier|public
class|class
name|SqlServerContainerProvider
extends|extends
name|TestContainerProvider
block|{
annotation|@
name|Override
name|JdbcDatabaseContainer
argument_list|<
name|?
argument_list|>
name|createContainer
parameter_list|(
name|DockerImageName
name|dockerImageName
parameter_list|)
block|{
return|return
operator|new
name|MSSQLServerContainer
argument_list|<>
argument_list|(
name|dockerImageName
argument_list|)
operator|.
name|acceptLicense
argument_list|()
return|;
block|}
annotation|@
name|Override
name|String
name|getDockerImage
parameter_list|()
block|{
return|return
literal|"mcr.microsoft.com/mssql/server"
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|JdbcAdapter
argument_list|>
name|getAdapterClass
parameter_list|()
block|{
return|return
name|SQLServerAdapter
operator|.
name|class
return|;
block|}
block|}
end_class

end_unit

