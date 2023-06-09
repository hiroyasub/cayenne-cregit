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
name|ExpressionException
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
name|ExpressionParameter
import|;
end_import

begin_comment
comment|/**  * A named expression parameter.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ASTNamedParameter
extends|extends
name|ASTScalar
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|3965588358977704022L
decl_stmt|;
name|ASTNamedParameter
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
name|ASTNamedParameter
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTNAMEDPARAMETER
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTNamedParameter
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTNAMEDPARAMETER
argument_list|)
expr_stmt|;
name|setValue
argument_list|(
name|value
argument_list|)
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
throw|throw
operator|new
name|ExpressionException
argument_list|(
literal|"Uninitialized parameter: "
operator|+
name|value
operator|+
literal|", call 'expWithParameters' first."
argument_list|)
throw|;
block|}
comment|/** 	 * Creates a copy of this expression node, without copying children. 	 */
annotation|@
name|Override
specifier|public
name|Expression
name|shallowCopy
parameter_list|()
block|{
name|ASTNamedParameter
name|copy
init|=
operator|new
name|ASTNamedParameter
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|copy
operator|.
name|value
operator|=
name|value
expr_stmt|;
return|return
name|copy
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ExpressionException
argument_list|(
literal|"Null Parameter value"
argument_list|)
throw|;
block|}
name|String
name|name
init|=
name|value
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|ExpressionException
argument_list|(
literal|"Empty Parameter value"
argument_list|)
throw|;
block|}
name|super
operator|.
name|setValue
argument_list|(
operator|new
name|ExpressionParameter
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @since 4.0 	 */
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
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|String
name|valueString
init|=
name|value
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|valueString
operator|.
name|length
argument_list|()
operator|>
literal|1
operator|&&
name|valueString
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'$'
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|valueString
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|super
operator|.
name|appendAsEJBQL
argument_list|(
name|parameterAccumulator
argument_list|,
name|out
argument_list|,
name|rootId
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

