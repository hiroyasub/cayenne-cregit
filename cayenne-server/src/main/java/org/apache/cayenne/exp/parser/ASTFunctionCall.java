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
name|exp
operator|.
name|parser
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|exp
operator|.
name|Expression
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ASTFunctionCall
extends|extends
name|EvaluatedNode
block|{
specifier|protected
name|String
name|functionName
decl_stmt|;
name|ASTFunctionCall
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|functionName
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|setFunctionName
argument_list|(
name|functionName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTFunctionCall
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|functionName
parameter_list|,
name|Object
modifier|...
name|nodes
parameter_list|)
block|{
name|this
argument_list|(
name|id
argument_list|,
name|functionName
argument_list|)
expr_stmt|;
name|setFunctionName
argument_list|(
name|functionName
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
name|FUNCTION_CALL
return|;
block|}
specifier|public
name|boolean
name|needParenthesis
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|getFunctionName
parameter_list|()
block|{
return|return
name|functionName
return|;
block|}
specifier|protected
name|void
name|setFunctionName
parameter_list|(
name|String
name|functionName
parameter_list|)
block|{
name|this
operator|.
name|functionName
operator|=
name|functionName
expr_stmt|;
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
literal|","
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|super
operator|.
name|equals
argument_list|(
name|o
argument_list|)
condition|)
return|return
literal|false
return|;
name|ASTFunctionCall
name|that
init|=
operator|(
name|ASTFunctionCall
operator|)
name|o
decl_stmt|;
return|return
name|functionName
operator|.
name|equals
argument_list|(
name|that
operator|.
name|functionName
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
literal|31
operator|*
name|super
operator|.
name|hashCode
argument_list|()
operator|+
name|functionName
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|protected
name|void
name|appendFunctionNameAsString
parameter_list|(
name|Appendable
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|append
argument_list|(
name|nameToCamelCase
argument_list|(
name|getFunctionName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|appendAsString
parameter_list|(
name|Appendable
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|appendFunctionNameAsString
argument_list|(
name|out
argument_list|)
expr_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
comment|// else call to super method will append parenthesis
name|out
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|appendAsString
argument_list|(
name|out
argument_list|)
expr_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|appendAsEJBQL
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|parameterAccumulator
parameter_list|,
name|Appendable
name|out
parameter_list|,
name|String
name|rootId
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|append
argument_list|(
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
name|super
operator|.
name|appendChildrenAsEJBQL
argument_list|(
name|parameterAccumulator
argument_list|,
name|out
argument_list|,
name|rootId
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
comment|/**      *      * @param functionName in UPPER_UNDERSCORE convention      * @return functionName in camelCase convention      */
specifier|protected
specifier|static
name|String
name|nameToCamelCase
parameter_list|(
name|String
name|functionName
parameter_list|)
block|{
name|String
index|[]
name|parts
init|=
name|functionName
operator|.
name|split
argument_list|(
literal|"_"
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|part
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|char
index|[]
name|chars
init|=
name|part
operator|.
name|toLowerCase
argument_list|()
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|chars
index|[
literal|0
index|]
operator|=
name|Character
operator|.
name|toTitleCase
argument_list|(
name|chars
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|chars
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

