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
name|unit
operator|.
name|di
operator|.
name|server
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
name|di
operator|.
name|Provider
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

begin_class
specifier|public
class|class
name|ServerCaseDataSourceInfoProvider
implements|implements
name|Provider
argument_list|<
name|DataSourceInfo
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CONNECTION_NAME_KEY
init|=
literal|"cayenneTestConnection"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CONNECTION_KEY
init|=
literal|"internal_embedded_datasource"
decl_stmt|;
specifier|private
specifier|static
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ServerCaseDataSourceInfoProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|DataSourceInfo
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
name|String
name|connectionKey
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|CONNECTION_NAME_KEY
argument_list|)
decl_stmt|;
name|DataSourceInfo
name|connectionInfo
init|=
name|ConnectionProperties
operator|.
name|getInstance
argument_list|()
operator|.
name|getConnectionInfo
argument_list|(
name|connectionKey
argument_list|)
decl_stmt|;
comment|// attempt default if invalid key is specified
if|if
condition|(
name|connectionInfo
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Invalid connection key '"
operator|+
name|connectionKey
operator|+
literal|"', trying default: "
operator|+
name|DEFAULT_CONNECTION_KEY
argument_list|)
expr_stmt|;
name|connectionInfo
operator|=
name|ConnectionProperties
operator|.
name|getInstance
argument_list|()
operator|.
name|getConnectionInfo
argument_list|(
name|DEFAULT_CONNECTION_KEY
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|connectionInfo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Null connection info for key: "
operator|+
name|connectionKey
argument_list|)
throw|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"loaded connection info: "
operator|+
name|connectionInfo
argument_list|)
expr_stmt|;
return|return
name|connectionInfo
return|;
block|}
block|}
end_class

end_unit

