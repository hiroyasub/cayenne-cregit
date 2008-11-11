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
name|dba
operator|.
name|sqlserver
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|DatabaseMetaData
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dba
operator|.
name|DbAdapter
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
name|DbAdapterFactory
import|;
end_import

begin_comment
comment|/**  * Detects SQLServer database from JDBC metadata.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|SQLServerSniffer
implements|implements
name|DbAdapterFactory
block|{
specifier|public
name|DbAdapter
name|createAdapter
parameter_list|(
name|DatabaseMetaData
name|md
parameter_list|)
throws|throws
name|SQLException
block|{
name|String
name|dbName
init|=
name|md
operator|.
name|getDatabaseProductName
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbName
operator|==
literal|null
operator|||
operator|!
name|dbName
operator|.
name|toUpperCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"MICROSOFT SQL SERVER"
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|SQLServerAdapter
name|adapter
init|=
operator|new
name|SQLServerAdapter
argument_list|()
decl_stmt|;
comment|// detect whether generated keys are supported
name|boolean
name|generatedKeys
init|=
literal|false
decl_stmt|;
try|try
block|{
name|generatedKeys
operator|=
name|md
operator|.
name|supportsGetGeneratedKeys
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
comment|// catch exceptions resulting from incomplete JDBC3 implementation
comment|// ** we have to catch Throwable, as unimplemented methods would result in
comment|// "AbstractMethodError".
block|}
name|adapter
operator|.
name|setSupportsGeneratedKeys
argument_list|(
name|generatedKeys
argument_list|)
expr_stmt|;
return|return
name|adapter
return|;
block|}
block|}
end_class

end_unit

