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

begin_comment
comment|/**  *  * Descriptor that provide desired transaction isolation level and propagation logic.  *  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|TransactionDescriptor
block|{
comment|/**      * Keep database default isolation level      */
specifier|public
specifier|static
specifier|final
name|int
name|ISOLATION_DEFAULT
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
specifier|final
name|int
name|isolation
decl_stmt|;
specifier|private
specifier|final
name|TransactionPropagation
name|propagation
decl_stmt|;
comment|/**      * @param isolation one of the following<code>Connection</code> constants:      *<code>Connection.TRANSACTION_READ_UNCOMMITTED</code>,      *<code>Connection.TRANSACTION_READ_COMMITTED</code>,      *<code>Connection.TRANSACTION_REPEATABLE_READ</code>,      *<code>Connection.TRANSACTION_SERIALIZABLE</code>, or      *<code>TransactionDescriptor.ISOLATION_DEFAULT</code>      *      * @param propagation transaction propagation behaviour      *      * @see TransactionPropagation      */
specifier|public
name|TransactionDescriptor
parameter_list|(
name|int
name|isolation
parameter_list|,
name|TransactionPropagation
name|propagation
parameter_list|)
block|{
name|this
operator|.
name|isolation
operator|=
name|isolation
expr_stmt|;
name|this
operator|.
name|propagation
operator|=
name|propagation
expr_stmt|;
block|}
comment|/**      *      * Create transaction descriptor with desired isolation level and<code>NESTED</code> propagation      *      * @param isolation one of the following<code>Connection</code> constants:      *<code>Connection.TRANSACTION_READ_UNCOMMITTED</code>,      *<code>Connection.TRANSACTION_READ_COMMITTED</code>,      *<code>Connection.TRANSACTION_REPEATABLE_READ</code>,      *<code>Connection.TRANSACTION_SERIALIZABLE</code>, or      *<code>TransactionDescriptor.ISOLATION_DEFAULT</code>      */
specifier|public
name|TransactionDescriptor
parameter_list|(
name|int
name|isolation
parameter_list|)
block|{
name|this
argument_list|(
name|isolation
argument_list|,
name|TransactionPropagation
operator|.
name|NESTED
argument_list|)
expr_stmt|;
block|}
comment|/**      *      * @param propagation transaction propagation behaviour      * @see TransactionPropagation      */
specifier|public
name|TransactionDescriptor
parameter_list|(
name|TransactionPropagation
name|propagation
parameter_list|)
block|{
name|this
argument_list|(
name|ISOLATION_DEFAULT
argument_list|,
name|propagation
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return required isolation level      */
specifier|public
name|int
name|getIsolation
parameter_list|()
block|{
return|return
name|isolation
return|;
block|}
comment|/**      * @return required propagation behaviour      */
specifier|public
name|TransactionPropagation
name|getPropagation
parameter_list|()
block|{
return|return
name|propagation
return|;
block|}
block|}
end_class

end_unit

