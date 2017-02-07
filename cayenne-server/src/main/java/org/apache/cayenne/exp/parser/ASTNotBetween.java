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
comment|/**  * "Not Between" expression.  *   */
end_comment

begin_class
specifier|public
class|class
name|ASTNotBetween
extends|extends
name|ConditionNode
block|{
name|ASTNotBetween
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
name|ASTNotBetween
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTNOTBETWEEN
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTNotBetween
parameter_list|(
name|SimpleNode
name|path
parameter_list|,
name|Object
name|value1
parameter_list|,
name|Object
name|value2
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTNOTBETWEEN
argument_list|)
expr_stmt|;
name|jjtAddChild
argument_list|(
name|path
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|jjtAddChild
argument_list|(
operator|new
name|ASTScalar
argument_list|(
name|value1
argument_list|)
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|jjtAddChild
argument_list|(
operator|new
name|ASTScalar
argument_list|(
name|value2
argument_list|)
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|connectChildren
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|int
name|getRequiredChildrenCount
parameter_list|()
block|{
return|return
literal|3
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|evaluateSubNode
parameter_list|(
name|Object
name|o
parameter_list|,
name|Object
index|[]
name|evaluatedChildren
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|o1
init|=
name|evaluatedChildren
index|[
literal|1
index|]
decl_stmt|;
name|Object
name|o2
init|=
name|evaluatedChildren
index|[
literal|2
index|]
decl_stmt|;
name|Evaluator
name|e
init|=
name|Evaluator
operator|.
name|evaluator
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|Integer
name|c1
init|=
name|e
operator|.
name|compare
argument_list|(
name|o
argument_list|,
name|o1
argument_list|)
decl_stmt|;
if|if
condition|(
name|c1
operator|==
literal|null
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
name|Integer
name|c2
init|=
name|e
operator|.
name|compare
argument_list|(
name|o
argument_list|,
name|o2
argument_list|)
decl_stmt|;
if|if
condition|(
name|c2
operator|==
literal|null
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
return|return
name|c1
operator|>=
literal|0
operator|&&
name|c2
operator|<=
literal|0
condition|?
name|Boolean
operator|.
name|FALSE
else|:
name|Boolean
operator|.
name|TRUE
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
name|ASTNotBetween
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
operator|(
name|index
operator|==
literal|2
operator|)
condition|?
literal|"and"
else|:
literal|"not between"
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
name|NOT_BETWEEN
return|;
block|}
block|}
end_class

end_unit

