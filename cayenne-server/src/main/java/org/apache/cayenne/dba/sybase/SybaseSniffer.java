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
name|dba
operator|.
name|sybase
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
name|configuration
operator|.
name|server
operator|.
name|DbAdapterDetector
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

begin_comment
comment|/**  * Detects Sybase database from JDBC metadata.  *  * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|SybaseSniffer
implements|implements
name|DbAdapterDetector
block|{
specifier|protected
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
specifier|public
name|SybaseSniffer
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
annotation|@
name|Override
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
comment|// JTDS driver returns "sql server" for Sybase, so need to handle it differently
name|String
name|driver
init|=
name|md
operator|.
name|getDriverName
argument_list|()
decl_stmt|;
if|if
condition|(
name|driver
operator|!=
literal|null
operator|&&
name|driver
operator|.
name|toLowerCase
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"jtds"
argument_list|)
condition|)
block|{
name|String
name|url
init|=
name|md
operator|.
name|getURL
argument_list|()
decl_stmt|;
return|return
name|url
operator|!=
literal|null
operator|&&
name|url
operator|.
name|toLowerCase
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"jdbc:jtds:sybase:"
argument_list|)
condition|?
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|,
name|SybaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
else|else
block|{
name|String
name|dbName
init|=
name|md
operator|.
name|getDatabaseProductName
argument_list|()
decl_stmt|;
return|return
name|dbName
operator|!=
literal|null
operator|&&
name|dbName
operator|.
name|toUpperCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"ADAPTIVE SERVER"
argument_list|)
condition|?
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|,
name|SybaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

