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
comment|/**  * "Less Then" expression.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ASTLess
extends|extends
name|ConditionNode
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|3846841914847489596L
decl_stmt|;
comment|/** 	 * Constructor used by expression parser. Do not invoke directly. 	 */
name|ASTLess
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
name|ASTLess
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTLESS
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTLess
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
name|JJTLESS
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
operator|!=
literal|2
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
name|Object
name|o1
init|=
name|evaluateChild
argument_list|(
literal|0
argument_list|,
name|o
argument_list|)
decl_stmt|;
name|Object
name|o2
init|=
name|evaluateChild
argument_list|(
literal|1
argument_list|,
name|o
argument_list|)
decl_stmt|;
name|Integer
name|c
init|=
name|Evaluator
operator|.
name|evaluator
argument_list|(
name|o1
argument_list|)
operator|.
name|compare
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
decl_stmt|;
return|return
name|c
operator|!=
literal|null
operator|&&
name|c
operator|<
literal|0
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
return|;
block|}
comment|/** 	 * Creates a copy of this expression node, without copying children. 	 */
annotation|@
name|Override
specifier|public
name|Expression
name|shallowCopy
parameter_list|()
block|{
return|return
operator|new
name|ASTLess
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
literal|"<"
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
name|LESS_THAN
return|;
block|}
block|}
end_class

end_unit

