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
name|hsqldb
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

begin_comment
comment|/**  * Detects HSQLDB database from JDBC metadata.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|HSQLDBSniffer
implements|implements
name|DbAdapterDetector
block|{
specifier|protected
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
specifier|public
name|HSQLDBSniffer
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
literal|"HSQL"
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|boolean
name|supportsSchema
init|=
name|md
operator|.
name|getDriverMajorVersion
argument_list|()
operator|<
literal|1
operator|||
name|md
operator|.
name|getDriverMajorVersion
argument_list|()
operator|==
literal|1
operator|&&
name|md
operator|.
name|getDriverMinorVersion
argument_list|()
operator|<=
literal|8
decl_stmt|;
return|return
name|supportsSchema
condition|?
operator|(
name|DbAdapter
operator|)
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|,
name|HSQLDBAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
else|:
operator|(
name|DbAdapter
operator|)
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|,
name|HSQLDBNoSchemaAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

