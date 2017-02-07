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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|Transformer
import|;
end_import

begin_comment
comment|/**  * "Not In" expression.  *   */
end_comment

begin_class
specifier|public
class|class
name|ASTNotIn
extends|extends
name|ConditionNode
block|{
name|ASTNotIn
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
name|ASTNotIn
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTNOTIN
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTNotIn
parameter_list|(
name|SimpleNode
name|path
parameter_list|,
name|ASTList
name|list
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTNOTIN
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
name|list
argument_list|,
literal|1
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
literal|2
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
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|evaluatedChildren
index|[
literal|1
index|]
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Object
index|[]
name|objects
init|=
operator|(
name|Object
index|[]
operator|)
name|evaluatedChildren
index|[
literal|1
index|]
decl_stmt|;
for|for
control|(
name|Object
name|object
range|:
name|objects
control|)
block|{
if|if
condition|(
name|object
operator|!=
literal|null
operator|&&
name|Evaluator
operator|.
name|evaluator
argument_list|(
name|o
argument_list|)
operator|.
name|eq
argument_list|(
name|o
argument_list|,
name|object
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
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
name|ASTNotIn
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
literal|"not in"
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
name|NOT_IN
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|transformExpression
parameter_list|(
name|Transformer
name|transformer
parameter_list|)
block|{
name|Object
name|transformed
init|=
name|super
operator|.
name|transformExpression
argument_list|(
name|transformer
argument_list|)
decl_stmt|;
comment|// transform empty ASTNotIn to ASTTrue
if|if
condition|(
name|transformed
operator|instanceof
name|ASTNotIn
condition|)
block|{
name|ASTNotIn
name|exp
init|=
operator|(
name|ASTNotIn
operator|)
name|transformed
decl_stmt|;
if|if
condition|(
name|exp
operator|.
name|jjtGetNumChildren
argument_list|()
operator|==
literal|2
condition|)
block|{
name|ASTList
name|list
init|=
operator|(
name|ASTList
operator|)
name|exp
operator|.
name|jjtGetChild
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Object
index|[]
name|objects
init|=
operator|(
name|Object
index|[]
operator|)
name|list
operator|.
name|evaluate
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|objects
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|transformed
operator|=
operator|new
name|ASTTrue
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|transformed
return|;
block|}
block|}
end_class

end_unit

