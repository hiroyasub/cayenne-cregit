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
name|tx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_comment
comment|/**  * A Cayenne Transaction interface.  *   * @since 3.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|Transaction
block|{
comment|/**      * Starts a Transaction. If Transaction is not started explicitly, it will      * be started when the first connection is added.      */
name|void
name|begin
parameter_list|()
function_decl|;
name|void
name|commit
parameter_list|()
function_decl|;
name|void
name|rollback
parameter_list|()
function_decl|;
name|void
name|setRollbackOnly
parameter_list|()
function_decl|;
name|boolean
name|isRollbackOnly
parameter_list|()
function_decl|;
name|Connection
name|getConnection
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
name|void
name|addConnection
parameter_list|(
name|String
name|name
parameter_list|,
name|Connection
name|connection
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

