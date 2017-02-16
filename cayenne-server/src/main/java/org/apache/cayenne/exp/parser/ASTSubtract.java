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
name|exp
operator|.
name|parser
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|exp
operator|.
name|Expression
import|;
end_import

begin_comment
comment|/**  * "Subtract" expression.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ASTSubtract
extends|extends
name|EvaluatedMathNode
block|{
name|ASTSubtract
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTSubtract
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTSUBTRACT
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTSubtract
parameter_list|(
name|Object
index|[]
name|nodes
parameter_list|)
block|{
name|this
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|nodes
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTSubtract
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|nodes
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTSUBTRACT
argument_list|)
expr_stmt|;
name|int
name|len
init|=
name|nodes
operator|.
name|size
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|nodes
operator|.
name|iterator
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|jjtAddChild
argument_list|(
name|wrapChild
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|connectChildren
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|BigDecimal
name|op
parameter_list|(
name|BigDecimal
name|result
parameter_list|,
name|BigDecimal
name|arg
parameter_list|)
block|{
return|return
name|result
operator|.
name|subtract
argument_list|(
name|arg
argument_list|)
return|;
block|}
comment|/**      * Creates a copy of this expression node, without copying children.      */
annotation|@
name|Override
specifier|public
name|Expression
name|shallowCopy
parameter_list|()
block|{
return|return
operator|new
name|ASTSubtract
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getExpressionOperator
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
literal|"-"
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|Expression
operator|.
name|SUBTRACT
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|jjtClose
parameter_list|()
block|{
name|super
operator|.
name|jjtClose
argument_list|()
expr_stmt|;
name|flattenTree
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

