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
name|PrintWriter
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
comment|/**  * "Not" expression.  *   * @since 1.1  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ASTNot
extends|extends
name|AggregateConditionNode
block|{
name|ASTNot
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
name|ASTNot
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTNOT
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTNot
parameter_list|(
name|Node
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTNOT
argument_list|)
expr_stmt|;
name|jjtAddChild
argument_list|(
name|expression
argument_list|,
literal|0
argument_list|)
expr_stmt|;
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
name|Boolean
operator|.
name|FALSE
return|;
block|}
return|return
name|ConversionUtil
operator|.
name|toBoolean
argument_list|(
name|evaluateChild
argument_list|(
literal|0
argument_list|,
name|o
argument_list|)
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
specifier|public
name|Expression
name|shallowCopy
parameter_list|()
block|{
return|return
operator|new
name|ASTNot
argument_list|(
name|id
argument_list|)
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
name|NOT
return|;
block|}
specifier|public
name|void
name|encodeAsString
parameter_list|(
name|PrintWriter
name|pw
parameter_list|)
block|{
name|pw
operator|.
name|print
argument_list|(
literal|"not "
argument_list|)
expr_stmt|;
name|super
operator|.
name|encodeAsString
argument_list|(
name|pw
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|void
name|encodeAsEJBQL
parameter_list|(
name|PrintWriter
name|pw
parameter_list|,
name|String
name|rootId
parameter_list|)
block|{
name|encodeAsString
argument_list|(
name|pw
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

