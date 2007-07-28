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
name|cayenne
operator|.
name|util
operator|.
name|ConversionUtil
import|;
end_import

begin_comment
comment|/**  * "Greate Than" expression.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ASTGreater
extends|extends
name|ConditionNode
block|{
comment|/**      * Constructor used by expression parser. Do not invoke directly.      */
name|ASTGreater
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
name|ASTGreater
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTGREATER
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTGreater
parameter_list|(
name|ASTPath
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
name|JJTGREATER
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
name|Comparable
name|c1
init|=
name|ConversionUtil
operator|.
name|toComparable
argument_list|(
name|evaluateChild
argument_list|(
literal|0
argument_list|,
name|o
argument_list|)
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
name|Comparable
name|c2
init|=
name|ConversionUtil
operator|.
name|toComparable
argument_list|(
name|evaluateChild
argument_list|(
literal|1
argument_list|,
name|o
argument_list|)
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
operator|.
name|compareTo
argument_list|(
name|c2
argument_list|)
operator|>
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
comment|/**      * Creates a copy of this expression node, without copying children.      */
specifier|public
name|Expression
name|shallowCopy
parameter_list|()
block|{
return|return
operator|new
name|ASTGreater
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
literal|">"
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
name|GREATER_THAN
return|;
block|}
block|}
end_class

end_unit

