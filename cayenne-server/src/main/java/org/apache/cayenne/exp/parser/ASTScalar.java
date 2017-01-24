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
name|ObjectId
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
name|Persistent
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
comment|/**  * A scalar value wrapper expression.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ASTScalar
extends|extends
name|SimpleNode
block|{
specifier|protected
name|Object
name|value
decl_stmt|;
comment|/**      * Constructor used by expression parser. Do not invoke directly.      */
name|ASTScalar
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
name|ASTScalar
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTSCALAR
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTScalar
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTSCALAR
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
return|return
name|value
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
name|ASTScalar
name|copy
init|=
operator|new
name|ASTScalar
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
comment|/**      * @since 4.0      */
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
name|SimpleNode
operator|.
name|appendScalarAsString
argument_list|(
name|out
argument_list|,
name|value
argument_list|,
literal|'\"'
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 4.0      */
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
comment|// TODO: see CAY-1111
comment|// Persistent processing is a hack for a rather special case of a single
comment|// column PK
comment|// object.. full implementation pending...
comment|//
comment|// cay1796 : change check for Persistent object by check for ObjectId
name|Object
name|scalar
init|=
name|value
decl_stmt|;
if|if
condition|(
name|scalar
operator|instanceof
name|ObjectId
condition|)
block|{
name|ObjectId
name|temp
init|=
operator|(
name|ObjectId
operator|)
name|value
decl_stmt|;
if|if
condition|(
operator|!
name|temp
operator|.
name|isTemporary
argument_list|()
operator|&&
name|temp
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|scalar
operator|=
name|temp
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
name|SimpleNode
operator|.
name|encodeScalarAsEJBQL
argument_list|(
name|parameterAccumulator
argument_list|,
name|out
argument_list|,
name|scalar
argument_list|)
expr_stmt|;
block|}
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
operator|instanceof
name|Persistent
condition|)
block|{
name|this
operator|.
name|value
operator|=
operator|(
operator|(
name|Persistent
operator|)
name|value
operator|)
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|value
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
name|ASTScalar
name|other
init|=
operator|(
name|ASTScalar
operator|)
name|o
decl_stmt|;
return|return
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|equals
argument_list|(
name|other
operator|.
name|value
argument_list|)
else|:
name|other
operator|.
name|value
operator|==
literal|null
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
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|hashCode
argument_list|()
else|:
literal|0
return|;
block|}
block|}
end_class

end_unit

