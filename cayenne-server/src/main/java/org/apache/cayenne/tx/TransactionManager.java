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
name|tx
package|;
end_package

begin_comment
comment|/**  * An optional utility service that simplifies wrapping multiple operations in  * transactions. Users only rarely need to invoke it directly, as all standard  * Cayenne operations are managing their own transactions internally.  *  * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|TransactionManager
block|{
comment|/**      * Starts a new transaction (or joins an existing one) calling      * {@link org.apache.cayenne.tx.TransactionalOperation#perform()}, and then committing or rolling back the      * transaction.      *      * @param op an operation to perform within the transaction.      * @param<T> returned value type      * @return a value returned by the "op" operation.      */
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
comment|/**      * Starts a new transaction (or joins an existing one) calling      * {@link org.apache.cayenne.tx.TransactionalOperation#perform()}, and then committing or rolling back the      * transaction. As transaction goes through stages, callback methods are invoked allowing the caller to customize      * transaction parameters.      *      * @param op       an operation to perform within the transaction.      * @param callback a callback to notify as transaction progresses through stages.      * @param<T> returned value type      * @return a value returned by the "op" operation.      */
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
parameter_list|,
name|TransactionListener
name|callback
parameter_list|)
function_decl|;
comment|/**      * Performs operation in a transaction which parameters described by descriptor.      *      * @param op         an operation to perform within the transaction.      * @param descriptor transaction descriptor      * @param<T> result type      * @return a value returned by the "op" operation.      *      * @since 4.1      */
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
parameter_list|,
name|TransactionDescriptor
name|descriptor
parameter_list|)
function_decl|;
comment|/**      * Performs operation in a transaction which parameters described by descriptor.      * As transaction goes through stages, callback methods are invoked allowing the caller to customize      * transaction parameters.      *      * @param op         an operation to perform within the transaction.      * @param callback   a callback to notify as transaction progresses through stages.      * @param descriptor transaction descriptor      * @param<T> returned value type      * @return a value returned by the "op" operation.      *      * @since 4.1      */
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
parameter_list|,
name|TransactionListener
name|callback
parameter_list|,
name|TransactionDescriptor
name|descriptor
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

