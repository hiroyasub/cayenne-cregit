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
comment|/**  * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|ASTBitwiseXor
extends|extends
name|SimpleNode
block|{
name|ASTBitwiseXor
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
name|ASTBitwiseXor
parameter_list|()
block|{
comment|// TODO: parser support
name|super
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTBitwiseXor
parameter_list|(
name|SimpleNode
name|left
parameter_list|,
name|SimpleNode
name|right
parameter_list|)
block|{
comment|// TODO: parser support
name|super
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|jjtAddChild
argument_list|(
name|left
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|jjtAddChild
argument_list|(
name|right
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
name|long
name|result
init|=
name|Long
operator|.
name|MIN_VALUE
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
name|long
name|value
init|=
name|ConversionUtil
operator|.
name|toLong
argument_list|(
name|evaluateChild
argument_list|(
name|i
argument_list|,
name|o
argument_list|)
argument_list|,
name|Long
operator|.
name|MIN_VALUE
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
name|Long
operator|.
name|MIN_VALUE
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
operator|^
name|value
expr_stmt|;
block|}
return|return
name|result
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
name|ASTBitwiseXor
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
literal|"^"
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"EJBQL 'bitwise xor' is not supported"
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
name|BITWISE_XOR
return|;
block|}
block|}
end_class

end_unit

