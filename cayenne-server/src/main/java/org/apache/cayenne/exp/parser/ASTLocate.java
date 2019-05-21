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
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ASTLocate
extends|extends
name|ASTFunctionCall
block|{
name|ASTLocate
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|,
literal|"LOCATE"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTLocate
parameter_list|(
name|Expression
name|substring
parameter_list|,
name|Expression
name|path
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTLOCATE
argument_list|,
literal|"LOCATE"
argument_list|,
name|substring
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTLocate
parameter_list|(
name|Expression
name|substring
parameter_list|,
name|Expression
name|path
parameter_list|,
name|Expression
name|offset
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTLOCATE
argument_list|,
literal|"LOCATE"
argument_list|,
name|substring
argument_list|,
name|path
argument_list|,
name|offset
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
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
name|String
name|substr
init|=
name|ConversionUtil
operator|.
name|toString
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|String
name|str
init|=
name|ConversionUtil
operator|.
name|toString
argument_list|(
name|evaluatedChildren
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|int
name|offset
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|evaluatedChildren
operator|.
name|length
operator|>
literal|2
condition|)
block|{
name|offset
operator|=
name|ConversionUtil
operator|.
name|toInt
argument_list|(
name|evaluatedChildren
index|[
literal|2
index|]
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|// +1 to comply with SQL
return|return
name|str
operator|.
name|indexOf
argument_list|(
name|substr
argument_list|,
name|offset
argument_list|)
operator|+
literal|1
return|;
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
specifier|public
name|Expression
name|shallowCopy
parameter_list|()
block|{
return|return
operator|new
name|ASTLocate
argument_list|(
name|id
argument_list|)
return|;
block|}
block|}
end_class

end_unit

