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
name|derby
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
comment|/**  * Creates a DerbyAdapter if Apache Derby database is detected.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DerbySniffer
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
literal|"APACHE DERBY"
argument_list|)
condition|?
operator|new
name|DerbyAdapter
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

