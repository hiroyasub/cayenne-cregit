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
operator|.
name|translator
operator|.
name|ejbql
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
name|ejbql
operator|.
name|EJBQLBaseVisitor
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
name|ejbql
operator|.
name|EJBQLException
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
name|ejbql
operator|.
name|EJBQLExpression
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
name|ejbql
operator|.
name|EJBQLExpressionVisitor
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|EJBQLOrderByTranslator
extends|extends
name|EJBQLBaseVisitor
block|{
specifier|private
name|EJBQLTranslationContext
name|context
decl_stmt|;
specifier|private
name|int
name|itemCount
decl_stmt|;
name|EJBQLOrderByTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitOrderByItem
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
if|if
condition|(
name|itemCount
operator|++
operator|>
literal|0
condition|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitDescending
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|" DESC"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitAggregate
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|expression
operator|.
name|visit
argument_list|(
name|context
operator|.
name|getTranslatorFactory
argument_list|()
operator|.
name|getAggregateColumnTranslator
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitPath
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
name|EJBQLExpressionVisitor
name|childVisitor
init|=
operator|new
name|EJBQLPathTranslator
argument_list|(
name|context
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|appendMultiColumnPath
parameter_list|(
name|EJBQLMultiColumnOperand
name|operand
parameter_list|)
block|{
throw|throw
operator|new
name|EJBQLException
argument_list|(
literal|"Can't order on multi-column paths or objects"
argument_list|)
throw|;
block|}
block|}
decl_stmt|;
name|expression
operator|.
name|visit
argument_list|(
name|childVisitor
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

