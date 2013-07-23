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
name|io
operator|.
name|IOException
import|;
end_import

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
comment|/**  * "Negate" expression.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ASTNegate
extends|extends
name|SimpleNode
block|{
name|ASTNegate
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
name|ASTNegate
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTNEGATE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTNegate
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTNEGATE
argument_list|)
expr_stmt|;
name|jjtAddChild
argument_list|(
name|wrapChild
argument_list|(
name|node
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|connectChildren
argument_list|()
expr_stmt|;
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
name|ASTNegate
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|ConversionUtil
operator|.
name|toBigDecimal
argument_list|(
name|evaluateChild
argument_list|(
literal|0
argument_list|,
name|o
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|result
operator|!=
literal|null
condition|?
name|result
operator|.
name|negate
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * @since 3.2      */
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
if|if
condition|(
operator|(
name|children
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|children
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
expr_stmt|;
name|SimpleNode
name|child
init|=
operator|(
name|SimpleNode
operator|)
name|children
index|[
literal|0
index|]
decl_stmt|;
comment|// don't call super - we have our own parenthesis policy
name|boolean
name|useParen
init|=
name|parent
operator|!=
literal|null
operator|&&
operator|!
operator|(
operator|(
name|child
operator|instanceof
name|ASTScalar
operator|)
operator|||
operator|(
name|child
operator|instanceof
name|ASTPath
operator|)
operator|)
decl_stmt|;
if|if
condition|(
name|useParen
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
block|}
name|child
operator|.
name|appendAsString
argument_list|(
name|out
argument_list|)
expr_stmt|;
if|if
condition|(
name|useParen
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @since 3.2      */
annotation|@
name|Override
specifier|public
name|void
name|appendAsEJBQL
parameter_list|(
name|Appendable
name|out
parameter_list|,
name|String
name|rootId
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|(
name|children
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|children
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
expr_stmt|;
name|SimpleNode
name|child
init|=
operator|(
name|SimpleNode
operator|)
name|children
index|[
literal|0
index|]
decl_stmt|;
comment|// don't call super - we have our own parenthesis policy
name|boolean
name|useParen
init|=
name|parent
operator|!=
literal|null
operator|&&
operator|!
operator|(
operator|(
name|child
operator|instanceof
name|ASTScalar
operator|)
operator|||
operator|(
name|child
operator|instanceof
name|ASTPath
operator|)
operator|)
decl_stmt|;
if|if
condition|(
name|useParen
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
block|}
name|child
operator|.
name|appendAsEJBQL
argument_list|(
name|out
argument_list|,
name|rootId
argument_list|)
expr_stmt|;
if|if
condition|(
name|useParen
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
block|}
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"No operator for '"
operator|+
name|ExpressionParserTreeConstants
operator|.
name|jjtNodeName
index|[
name|id
index|]
operator|+
literal|"'"
argument_list|)
throw|;
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
name|NEGATIVE
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getOperandCount
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit
