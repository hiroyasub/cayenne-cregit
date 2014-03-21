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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|CayenneRuntime
import|;
end_import

begin_comment
comment|/**  * An optional utility service that simplifies wrapping multiple operations in  * transactions. Users only rarely need to invoke it directly, as all standard  * Cayenne operations are managing their own transactions internally.  *   * @since 3.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|TransactionManager
block|{
comment|/**      * Starts a new transaction (or joins an existing one) calling      * {@link org.apache.cayenne.tx.TransactionalOperation#perform()}, and then      * committing or rolling back the transaction. Frees the user      */
parameter_list|<
name|T
parameter_list|>
name|T
name|performInTransaction
parameter_list|(
name|TransactionalOperation
argument_list|<
name|T
argument_list|>
name|op
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

