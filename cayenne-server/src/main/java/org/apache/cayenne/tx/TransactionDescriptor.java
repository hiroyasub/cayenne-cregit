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
name|util
operator|.
name|function
operator|.
name|Supplier
import|;
end_import

begin_comment
comment|/**  * Descriptor that allows to customize transaction logic.  * It provides following options:  *<ul>  *<li> transaction isolation level  *<li> transaction propagation logic.  *<li> custom connection to use in a transaction  *</ul>  * @see TransactionManager#performInTransaction(TransactionalOperation, TransactionDescriptor)  * @see org.apache.cayenne.configuration.server.ServerRuntime#performInTransaction(TransactionalOperation, TransactionDescriptor)  * @since 4.1  */
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
specifier|static
specifier|final
name|TransactionDescriptor
name|DEFAULT_DESCRIPTOR
init|=
name|builder
argument_list|()
operator|.
name|propagation
argument_list|(
name|TransactionPropagation
operator|.
name|NESTED
argument_list|)
operator|.
name|isolation
argument_list|(
name|TransactionDescriptor
operator|.
name|ISOLATION_DEFAULT
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|private
name|int
name|isolation
decl_stmt|;
specifier|private
name|TransactionPropagation
name|propagation
decl_stmt|;
specifier|private
name|Supplier
argument_list|<
name|Connection
argument_list|>
name|connectionSupplier
decl_stmt|;
specifier|protected
name|TransactionDescriptor
parameter_list|()
block|{
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
comment|/**      * @return custom connection supplier, passed by user      * @since 4.2      */
specifier|public
name|Supplier
argument_list|<
name|Connection
argument_list|>
name|getConnectionSupplier
parameter_list|()
block|{
return|return
name|connectionSupplier
return|;
block|}
comment|/**      * @return TransactionDescriptor Builder      * @since 4.2      */
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|()
return|;
block|}
comment|/**      * Returns descriptor with the {@link TransactionPropagation#NESTED} propagation      * and the {@link #ISOLATION_DEFAULT} isolation level      * @return default descriptor      * @since 4.2      */
specifier|public
specifier|static
name|TransactionDescriptor
name|defaultDescriptor
parameter_list|()
block|{
return|return
name|DEFAULT_DESCRIPTOR
return|;
block|}
comment|/**      * Builder class for the TransactionDescriptor.      * @since 4.2      */
specifier|public
specifier|static
class|class
name|Builder
block|{
specifier|private
specifier|final
name|TransactionDescriptor
name|transactionDescriptor
init|=
operator|new
name|TransactionDescriptor
argument_list|()
decl_stmt|;
specifier|private
name|Builder
parameter_list|()
block|{
block|}
comment|/**          * @param isolation one of the following<code>Connection</code> constants:          *<code>Connection.TRANSACTION_READ_UNCOMMITTED</code>,          *<code>Connection.TRANSACTION_READ_COMMITTED</code>,          *<code>Connection.TRANSACTION_REPEATABLE_READ</code>,          *<code>Connection.TRANSACTION_SERIALIZABLE</code>, or          *<code>TransactionDescriptor.ISOLATION_DEFAULT</code>          */
specifier|public
name|Builder
name|isolation
parameter_list|(
name|int
name|isolation
parameter_list|)
block|{
name|transactionDescriptor
operator|.
name|isolation
operator|=
name|isolation
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A custom connection provided by the TransactionDescriptor will be used          * instead of any other connection provided by tbe connection pool.          *          * @param connection custom connection          * @see #connectionSupplier(Supplier)          */
specifier|public
name|Builder
name|connection
parameter_list|(
name|Connection
name|connection
parameter_list|)
block|{
name|transactionDescriptor
operator|.
name|connectionSupplier
operator|=
parameter_list|()
lambda|->
name|connection
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A custom connection provided by the TransactionDescriptor will be used          * instead of any other connection provided by tbe connection pool.          *          * @param connectionSupplier custom connection supplier          * @see #connection(Connection)          */
specifier|public
name|Builder
name|connectionSupplier
parameter_list|(
name|Supplier
argument_list|<
name|Connection
argument_list|>
name|connectionSupplier
parameter_list|)
block|{
name|transactionDescriptor
operator|.
name|connectionSupplier
operator|=
name|connectionSupplier
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * @param propagation transaction propagation behaviour          * @see TransactionPropagation          */
specifier|public
name|Builder
name|propagation
parameter_list|(
name|TransactionPropagation
name|propagation
parameter_list|)
block|{
name|transactionDescriptor
operator|.
name|propagation
operator|=
name|propagation
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|TransactionDescriptor
name|build
parameter_list|()
block|{
return|return
name|transactionDescriptor
return|;
block|}
block|}
block|}
end_class

end_unit

