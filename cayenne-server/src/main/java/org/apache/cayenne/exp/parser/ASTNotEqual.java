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
comment|/**  * "Not equal to" expression.  *   */
end_comment

begin_class
specifier|public
class|class
name|ASTNotEqual
extends|extends
name|ConditionNode
block|{
name|ASTNotEqual
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
name|ASTNotEqual
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTNOTEQUAL
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates "Not Equal To" expression.      */
specifier|public
name|ASTNotEqual
parameter_list|(
name|SimpleNode
name|path
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTNOTEQUAL
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
name|value
argument_list|)
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
name|Boolean
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
name|o2
init|=
name|evaluatedChildren
index|[
literal|1
index|]
decl_stmt|;
return|return
name|ASTEqual
operator|.
name|evaluateImpl
argument_list|(
name|o
argument_list|,
name|o2
argument_list|)
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
name|ASTNotEqual
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
literal|"!="
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getEJBQLExpressionOperator
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|jjtGetChild
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|ASTScalar
operator|&&
operator|(
operator|(
name|ASTScalar
operator|)
name|jjtGetChild
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|//for ejbql, we need "is not null" instead of "!= null"
return|return
literal|"is not"
return|;
block|}
return|return
literal|"<>"
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
name|NOT_EQUAL_TO
return|;
block|}
block|}
end_class

end_unit

