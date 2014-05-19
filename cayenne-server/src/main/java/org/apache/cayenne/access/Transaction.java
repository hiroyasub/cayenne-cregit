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
name|access
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
name|ServerRuntime
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
name|tx
operator|.
name|BaseTransaction
import|;
end_import

begin_comment
comment|/**  * @deprecated since 3.2. For manual transaction management use  *             {@link BaseTransaction} static methods or better -  *             {@link ServerRuntime#performInTransaction(org.apache.cayenne.tx.TransactionalOperation)}  *             . Also note that since 3.2 an actual Transaction is an interface  *             located in a different package:  *             {@link org.apache.cayenne.tx.Transaction}  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
specifier|abstract
class|class
name|Transaction
block|{
comment|/**      * Binds a Transaction to the current thread.      *       * @since 1.2      */
annotation|@
name|Deprecated
specifier|public
specifier|static
name|void
name|bindThreadTransaction
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|tx
operator|.
name|Transaction
name|transaction
parameter_list|)
block|{
name|BaseTransaction
operator|.
name|bindThreadTransaction
argument_list|(
name|transaction
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a Transaction associated with the current thread, or null if      * there is no such Transaction.      *       * @since 1.2      */
annotation|@
name|Deprecated
specifier|public
specifier|static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|tx
operator|.
name|Transaction
name|getThreadTransaction
parameter_list|()
block|{
return|return
name|BaseTransaction
operator|.
name|getThreadTransaction
argument_list|()
return|;
block|}
block|}
end_class

end_unit

