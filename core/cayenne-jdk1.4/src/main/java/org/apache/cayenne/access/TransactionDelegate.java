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
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_comment
comment|/**  * Defines callback methods for tracking and customizing Transactions execution.  *   * @author Andrus Adamchik  * @since 1.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|TransactionDelegate
block|{
comment|/**      * Called within a context of a Transaction before the transaction is committed.      * Delegate can do its own processing, and optionally suppress further commit      * processing by Cayenne by returning<code>false</code>.      */
specifier|public
name|boolean
name|willCommit
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
function_decl|;
comment|/**      * Called within a context of a Transaction before transaction is marked as "rollback      * only", meaning that further commit is not possible. Delegate can do its own      * processing, and optionally suppress setting transaction status by returning      *<code>false</code>.      */
specifier|public
name|boolean
name|willMarkAsRollbackOnly
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
function_decl|;
comment|/**      * Called within a context of a Transaction before the transaction is rolledback.      * Delegate can do its own processing, and optionally suppress further rollback      * processing by Cayenne by returning<code>false</code>.      */
specifier|public
name|boolean
name|willRollback
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
function_decl|;
comment|/**      * Called after a Transaction commit.      */
specifier|public
name|void
name|didCommit
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
function_decl|;
comment|/**      * Called after a Transaction is rolledback.      */
specifier|public
name|void
name|didRollback
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
function_decl|;
comment|/**      * Called within a context of a Transaction when a new JDBC onnection is added to the      * the transaction. Delegate can do its own processing, and optionally suppress      * connection registration with the transaction by returning<code>false</code>.      */
specifier|public
name|boolean
name|willAddConnection
parameter_list|(
name|Transaction
name|transaction
parameter_list|,
name|Connection
name|connection
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

