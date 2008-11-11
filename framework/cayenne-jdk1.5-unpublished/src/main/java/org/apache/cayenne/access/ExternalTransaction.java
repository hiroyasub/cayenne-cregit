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
name|Iterator
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
comment|/**  * Represents a container-managed transaction.  *   * @since 1.2 moved to a top-level class.  */
end_comment

begin_class
class|class
name|ExternalTransaction
extends|extends
name|Transaction
block|{
name|ExternalTransaction
parameter_list|()
block|{
block|}
name|ExternalTransaction
parameter_list|(
name|TransactionDelegate
name|delegate
parameter_list|)
block|{
name|setDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|begin
parameter_list|()
block|{
if|if
condition|(
name|status
operator|!=
name|Transaction
operator|.
name|STATUS_NO_TRANSACTION
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Transaction must have 'STATUS_NO_TRANSACTION' to begin. "
operator|+
literal|"Current status: "
operator|+
name|Transaction
operator|.
name|decodeStatus
argument_list|(
name|status
argument_list|)
argument_list|)
throw|;
block|}
name|status
operator|=
name|Transaction
operator|.
name|STATUS_ACTIVE
expr_stmt|;
block|}
annotation|@
name|Override
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
name|super
operator|.
name|addConnection
argument_list|(
name|name
argument_list|,
name|connection
argument_list|)
condition|)
block|{
comment|// implicitly begin transaction
if|if
condition|(
name|status
operator|==
name|Transaction
operator|.
name|STATUS_NO_TRANSACTION
condition|)
block|{
name|begin
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|status
operator|!=
name|Transaction
operator|.
name|STATUS_ACTIVE
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Transaction must have 'STATUS_ACTIVE' to add a connection. "
operator|+
literal|"Current status: "
operator|+
name|Transaction
operator|.
name|decodeStatus
argument_list|(
name|status
argument_list|)
argument_list|)
throw|;
block|}
name|fixConnectionState
argument_list|(
name|connection
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|commit
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|SQLException
throws|,
name|CayenneException
block|{
if|if
condition|(
name|status
operator|==
name|Transaction
operator|.
name|STATUS_NO_TRANSACTION
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
operator|&&
operator|!
name|delegate
operator|.
name|willCommit
argument_list|(
name|this
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|status
operator|!=
name|Transaction
operator|.
name|STATUS_ACTIVE
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Transaction must have 'STATUS_ACTIVE' to be committed. "
operator|+
literal|"Current status: "
operator|+
name|Transaction
operator|.
name|decodeStatus
argument_list|(
name|status
argument_list|)
argument_list|)
throw|;
block|}
name|processCommit
argument_list|()
expr_stmt|;
name|status
operator|=
name|Transaction
operator|.
name|STATUS_COMMITTED
expr_stmt|;
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|didCommit
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|rollback
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|SQLException
throws|,
name|CayenneException
block|{
try|try
block|{
if|if
condition|(
name|status
operator|==
name|Transaction
operator|.
name|STATUS_NO_TRANSACTION
operator|||
name|status
operator|==
name|Transaction
operator|.
name|STATUS_ROLLEDBACK
operator|||
name|status
operator|==
name|Transaction
operator|.
name|STATUS_ROLLING_BACK
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
operator|&&
operator|!
name|delegate
operator|.
name|willRollback
argument_list|(
name|this
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|status
operator|!=
name|Transaction
operator|.
name|STATUS_ACTIVE
operator|&&
name|status
operator|!=
name|Transaction
operator|.
name|STATUS_MARKED_ROLLEDBACK
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Transaction must have 'STATUS_ACTIVE' or 'STATUS_MARKED_ROLLEDBACK' to be rolled back. "
operator|+
literal|"Current status: "
operator|+
name|Transaction
operator|.
name|decodeStatus
argument_list|(
name|status
argument_list|)
argument_list|)
throw|;
block|}
name|processRollback
argument_list|()
expr_stmt|;
name|status
operator|=
name|Transaction
operator|.
name|STATUS_ROLLEDBACK
expr_stmt|;
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|didRollback
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|close
argument_list|()
expr_stmt|;
block|}
block|}
name|void
name|fixConnectionState
parameter_list|(
name|Connection
name|connection
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// NOOP
block|}
name|void
name|processCommit
parameter_list|()
throws|throws
name|SQLException
throws|,
name|CayenneException
block|{
name|QueryLogger
operator|.
name|logCommitTransaction
argument_list|(
literal|"no commit - transaction controlled externally."
argument_list|)
expr_stmt|;
block|}
name|void
name|processRollback
parameter_list|()
throws|throws
name|SQLException
throws|,
name|CayenneException
block|{
name|QueryLogger
operator|.
name|logRollbackTransaction
argument_list|(
literal|"no rollback - transaction controlled externally."
argument_list|)
expr_stmt|;
block|}
comment|/**      * Closes all connections associated with transaction.      */
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|connections
operator|==
literal|null
operator|||
name|connections
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|Iterator
name|it
init|=
name|connections
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
try|try
block|{
operator|(
operator|(
name|Connection
operator|)
name|it
operator|.
name|next
argument_list|()
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
comment|// TODO: chain exceptions...
comment|// ignore for now
block|}
block|}
block|}
block|}
end_class

end_unit

