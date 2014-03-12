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
name|util
operator|.
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|List
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
name|CayenneRuntimeException
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
name|ResultIterator
import|;
end_import

begin_comment
comment|/**  * Decorates ResultIterator to close active transaction when the iterator is  * closed.  *   * @since 1.2  */
end_comment

begin_class
specifier|final
class|class
name|TransactionResultIteratorDecorator
parameter_list|<
name|T
parameter_list|>
implements|implements
name|ResultIterator
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|ResultIterator
argument_list|<
name|T
argument_list|>
name|result
decl_stmt|;
specifier|private
name|Transaction
name|tx
decl_stmt|;
specifier|public
name|TransactionResultIteratorDecorator
parameter_list|(
name|ResultIterator
argument_list|<
name|T
argument_list|>
name|result
parameter_list|,
name|Transaction
name|tx
parameter_list|)
block|{
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
name|this
operator|.
name|tx
operator|=
name|tx
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|result
operator|.
name|iterator
argument_list|()
return|;
block|}
comment|/**      * Closes the result and commits the transaction.      */
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
try|try
block|{
name|result
operator|.
name|close
argument_list|()
expr_stmt|;
name|tx
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
try|try
block|{
name|tx
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|rollbackEx
parameter_list|)
block|{
block|}
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|Transaction
operator|.
name|getThreadTransaction
argument_list|()
operator|==
name|tx
condition|)
block|{
name|Transaction
operator|.
name|bindThreadTransaction
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|allRows
parameter_list|()
block|{
name|List
argument_list|<
name|T
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|hasNextRow
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|nextRow
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasNextRow
parameter_list|()
block|{
return|return
name|result
operator|.
name|hasNextRow
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|T
name|nextRow
parameter_list|()
block|{
return|return
name|result
operator|.
name|nextRow
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|void
name|skipRow
parameter_list|()
block|{
name|result
operator|.
name|skipRow
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

