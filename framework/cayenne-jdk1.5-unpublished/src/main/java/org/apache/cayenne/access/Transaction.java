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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|CayenneException
import|;
end_import

begin_comment
comment|/**  * A Cayenne transaction. Currently supports managing JDBC connections.  *   * @author Andrus Adamchik  * @since 1.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|Transaction
block|{
comment|/**      * A ThreadLocal that stores current thread transaction.      *       * @since 1.2      */
specifier|static
specifier|final
name|ThreadLocal
name|currentTransaction
init|=
operator|new
name|InheritableThreadLocal
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Transaction
name|NO_TRANSACTION
init|=
operator|new
name|Transaction
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|begin
parameter_list|()
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|commit
parameter_list|()
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|rollback
parameter_list|()
block|{
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|STATUS_ACTIVE
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|STATUS_COMMITTING
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|STATUS_COMMITTED
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|STATUS_ROLLEDBACK
init|=
literal|4
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|STATUS_ROLLING_BACK
init|=
literal|5
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|STATUS_NO_TRANSACTION
init|=
literal|6
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|STATUS_MARKED_ROLLEDBACK
init|=
literal|7
decl_stmt|;
specifier|protected
name|Map
name|connections
decl_stmt|;
specifier|protected
name|int
name|status
decl_stmt|;
specifier|protected
name|TransactionDelegate
name|delegate
decl_stmt|;
specifier|static
name|String
name|decodeStatus
parameter_list|(
name|int
name|status
parameter_list|)
block|{
switch|switch
condition|(
name|status
condition|)
block|{
case|case
name|STATUS_ACTIVE
case|:
return|return
literal|"STATUS_ACTIVE"
return|;
case|case
name|STATUS_COMMITTING
case|:
return|return
literal|"STATUS_COMMITTING"
return|;
case|case
name|STATUS_COMMITTED
case|:
return|return
literal|"STATUS_COMMITTED"
return|;
case|case
name|STATUS_ROLLEDBACK
case|:
return|return
literal|"STATUS_ROLLEDBACK"
return|;
case|case
name|STATUS_ROLLING_BACK
case|:
return|return
literal|"STATUS_ROLLING_BACK"
return|;
case|case
name|STATUS_NO_TRANSACTION
case|:
return|return
literal|"STATUS_NO_TRANSACTION"
return|;
case|case
name|STATUS_MARKED_ROLLEDBACK
case|:
return|return
literal|"STATUS_MARKED_ROLLEDBACK"
return|;
default|default:
return|return
literal|"Unknown Status - "
operator|+
name|status
return|;
block|}
block|}
comment|/**      * Binds a Transaction to the current thread.      *       * @since 1.2      */
specifier|public
specifier|static
name|void
name|bindThreadTransaction
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
block|{
name|currentTransaction
operator|.
name|set
argument_list|(
name|transaction
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a Transaction associated with the current thread, or null if there is no      * such Transaction.      *       * @since 1.2      */
specifier|public
specifier|static
name|Transaction
name|getThreadTransaction
parameter_list|()
block|{
return|return
operator|(
name|Transaction
operator|)
name|currentTransaction
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**      * Factory method returning a new transaction instance that would propagate      * commit/rollback to participating connections. Connections will be closed when      * commit or rollback is called.      */
specifier|public
specifier|static
name|Transaction
name|internalTransaction
parameter_list|(
name|TransactionDelegate
name|delegate
parameter_list|)
block|{
return|return
operator|new
name|InternalTransaction
argument_list|(
name|delegate
argument_list|)
return|;
block|}
comment|/**      * Factory method returning a new transaction instance that would NOT propagate      * commit/rollback to participating connections. Connections will still be closed when      * commit or rollback is called.      */
specifier|public
specifier|static
name|Transaction
name|externalTransaction
parameter_list|(
name|TransactionDelegate
name|delegate
parameter_list|)
block|{
return|return
operator|new
name|ExternalTransaction
argument_list|(
name|delegate
argument_list|)
return|;
block|}
comment|/**      * Factory method returning a transaction instance that does not alter the state of      * participating connections in any way. Commit and rollback methods do not do      * anything.      */
specifier|public
specifier|static
name|Transaction
name|noTransaction
parameter_list|()
block|{
return|return
name|NO_TRANSACTION
return|;
block|}
comment|/**      * Creates new inactive transaction.      */
specifier|protected
name|Transaction
parameter_list|()
block|{
name|status
operator|=
name|STATUS_NO_TRANSACTION
expr_stmt|;
block|}
specifier|public
name|TransactionDelegate
name|getDelegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
specifier|public
name|void
name|setDelegate
parameter_list|(
name|TransactionDelegate
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
specifier|public
name|int
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|setRollbackOnly
parameter_list|()
block|{
name|setStatus
argument_list|(
name|STATUS_MARKED_ROLLEDBACK
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|setStatus
parameter_list|(
name|int
name|status
parameter_list|)
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
operator|&&
name|status
operator|==
name|STATUS_MARKED_ROLLEDBACK
operator|&&
operator|!
name|delegate
operator|.
name|willMarkAsRollbackOnly
argument_list|(
name|this
argument_list|)
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
block|}
comment|/**      * Starts a Transaction. If Transaction is not started explicitly, it will be started      * when the first connection is added.      */
specifier|public
specifier|abstract
name|void
name|begin
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|commit
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|SQLException
throws|,
name|CayenneException
function_decl|;
specifier|public
specifier|abstract
name|void
name|rollback
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|SQLException
throws|,
name|CayenneException
function_decl|;
comment|/**      * @since 1.2      */
specifier|public
name|Connection
name|getConnection
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|connections
operator|!=
literal|null
operator|)
condition|?
operator|(
name|Connection
operator|)
name|connections
operator|.
name|get
argument_list|(
name|name
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|boolean
name|addConnection
parameter_list|(
name|String
name|name
parameter_list|,
name|Connection
name|connection
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
operator|&&
operator|!
name|delegate
operator|.
name|willAddConnection
argument_list|(
name|this
argument_list|,
name|connection
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|connections
operator|==
literal|null
condition|)
block|{
name|connections
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
block|}
return|return
name|connections
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|connection
argument_list|)
operator|!=
name|connection
return|;
block|}
block|}
end_class

end_unit

