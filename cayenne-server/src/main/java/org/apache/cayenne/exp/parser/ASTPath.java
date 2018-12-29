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
name|map
operator|.
name|Entity
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
name|CayenneMapEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Generic path expression.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ASTPath
extends|extends
name|SimpleNode
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|8099822503585617295L
decl_stmt|;
specifier|protected
name|String
name|path
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|pathAliases
decl_stmt|;
name|ASTPath
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|super
argument_list|(
name|i
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
specifier|public
name|Object
name|getOperand
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|index
operator|==
literal|0
condition|)
block|{
return|return
name|path
return|;
block|}
throw|throw
operator|new
name|ArrayIndexOutOfBoundsException
argument_list|(
name|index
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setOperand
parameter_list|(
name|int
name|index
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|index
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|ArrayIndexOutOfBoundsException
argument_list|(
name|index
argument_list|)
throw|;
block|}
name|setPath
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setPath
parameter_list|(
name|Object
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
operator|(
name|path
operator|!=
literal|null
operator|)
condition|?
name|path
operator|.
name|toString
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
comment|/** 	 * @since 3.0 	 */
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getPathAliases
parameter_list|()
block|{
return|return
name|pathAliases
operator|!=
literal|null
condition|?
name|pathAliases
else|:
name|super
operator|.
name|getPathAliases
argument_list|()
return|;
block|}
comment|/** 	 * @since 3.0 	 */
specifier|public
name|void
name|setPathAliases
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|pathAliases
parameter_list|)
block|{
name|this
operator|.
name|pathAliases
operator|=
name|pathAliases
expr_stmt|;
block|}
comment|/** 	 * Helper method to evaluate path expression with Cayenne Entity. 	 */
specifier|protected
name|CayenneMapEntry
name|evaluateEntityNode
parameter_list|(
name|Entity
name|entity
parameter_list|)
block|{
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|path
init|=
name|entity
operator|.
name|resolvePathComponents
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|CayenneMapEntry
name|next
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|path
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|next
operator|=
name|path
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
return|return
name|next
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
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|path
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

