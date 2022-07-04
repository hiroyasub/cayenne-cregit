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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|log
operator|.
name|JdbcEventLogger
import|;
end_import

begin_comment
comment|/**  * Represents a container-managed transaction.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ExternalTransaction
extends|extends
name|BaseTransaction
block|{
specifier|protected
name|JdbcEventLogger
name|logger
decl_stmt|;
specifier|public
name|ExternalTransaction
parameter_list|(
name|JdbcEventLogger
name|jdbcEventLogger
parameter_list|)
block|{
name|this
argument_list|(
name|jdbcEventLogger
argument_list|,
name|TransactionDescriptor
operator|.
name|defaultDescriptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 4.1      */
specifier|public
name|ExternalTransaction
parameter_list|(
name|JdbcEventLogger
name|jdbcEventLogger
parameter_list|,
name|TransactionDescriptor
name|descriptor
parameter_list|)
block|{
name|super
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|jdbcEventLogger
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processCommit
parameter_list|()
block|{
name|logger
operator|.
name|logCommitTransaction
argument_list|(
literal|"no commit - transaction controlled externally."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processRollback
parameter_list|()
block|{
name|logger
operator|.
name|logRollbackTransaction
argument_list|(
literal|"no rollback - transaction controlled externally."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isExternal
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

