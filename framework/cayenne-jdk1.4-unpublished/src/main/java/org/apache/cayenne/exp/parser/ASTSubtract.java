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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|ConversionUtil
import|;
end_import

begin_comment
comment|/**  * "Subtract" expression.  *   * @since 1.1  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ASTSubtract
extends|extends
name|SimpleNode
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
name|length
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
name|nodes
index|[
name|i
index|]
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
specifier|public
name|ASTSubtract
parameter_list|(
name|Collection
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
specifier|protected
name|Object
name|evaluateNode
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|len
init|=
name|jjtGetNumChildren
argument_list|()
decl_stmt|;
if|if
condition|(
name|len
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|BigDecimal
name|result
init|=
literal|null
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
name|BigDecimal
name|value
init|=
name|ConversionUtil
operator|.
name|toBigDecimal
argument_list|(
name|evaluateChild
argument_list|(
name|i
argument_list|,
name|o
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|result
operator|=
operator|(
name|i
operator|==
literal|0
operator|)
condition|?
name|value
else|:
name|result
operator|.
name|subtract
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Creates a copy of this expression node, without copying children.      */
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

