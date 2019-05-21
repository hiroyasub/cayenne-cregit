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
name|configuration
operator|.
name|server
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

begin_comment
comment|/**  * A factory interface providing DbAdapter based on JDBC metadata. It allows custom  * DbAdapters to contribute database detection algorithms to  * {@link DefaultDbAdapterFactory}.  *   * @since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|DbAdapterDetector
block|{
comment|/**      * Returns an instance of DbAdapter if the factory detects that it knows how to handle      * the database or null if the database is not known to the factory, thus allowing      * multiple factories to be chained.      */
name|DbAdapter
name|createAdapter
parameter_list|(
name|DatabaseMetaData
name|md
parameter_list|)
throws|throws
name|SQLException
function_decl|;
block|}
end_interface

end_unit

