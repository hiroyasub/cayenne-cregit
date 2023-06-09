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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|List
import|;
end_import

begin_comment
comment|/**  * A leaf expression representing an immutable collection of values.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ASTList
extends|extends
name|SimpleNode
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|6045178972189002055L
decl_stmt|;
specifier|protected
name|Object
index|[]
name|values
decl_stmt|;
name|ASTList
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
name|ASTList
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTLIST
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Initializes a list expression with an Object[]. 	 */
specifier|public
name|ASTList
parameter_list|(
name|Object
index|[]
name|objects
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTLIST
argument_list|)
expr_stmt|;
name|setValues
argument_list|(
name|objects
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Initializes a list expression with a Java Collection 	 */
specifier|public
name|ASTList
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|objects
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTLIST
argument_list|)
expr_stmt|;
name|setValues
argument_list|(
name|objects
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Initializes a list expression with a Java Iterator. 	 */
specifier|public
name|ASTList
parameter_list|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|objects
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTLIST
argument_list|)
expr_stmt|;
name|setValues
argument_list|(
name|objects
argument_list|)
expr_stmt|;
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
name|ASTList
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
return|return
name|values
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
name|LIST
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
literal|","
return|;
block|}
comment|/** 	 * @since 4.0 	 */
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
name|out
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|values
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|values
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|values
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
name|getExpressionOperator
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|values
index|[
name|i
index|]
operator|instanceof
name|Expression
condition|)
block|{
operator|(
operator|(
name|Expression
operator|)
name|values
index|[
name|i
index|]
operator|)
operator|.
name|appendAsString
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appendScalarAsString
argument_list|(
name|out
argument_list|,
name|values
index|[
name|i
index|]
argument_list|,
literal|'\"'
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|out
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|parent
operator|!=
literal|null
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
if|if
condition|(
operator|(
name|values
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|values
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|values
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
name|getEJBQLExpressionOperator
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|values
index|[
name|i
index|]
operator|==
literal|null
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"null"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SimpleNode
operator|.
name|encodeScalarAsEJBQL
argument_list|(
name|parameterAccumulator
argument_list|,
name|out
argument_list|,
name|values
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|parent
operator|!=
literal|null
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
name|values
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
name|setValues
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Sets an internal collection of values. Value argument can be an Object[], 	 * a Collection or an iterator. 	 */
specifier|protected
name|void
name|setValues
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
name|this
operator|.
name|values
operator|=
literal|null
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|Object
index|[]
condition|)
block|{
name|int
name|size
init|=
operator|(
operator|(
name|Object
index|[]
operator|)
name|value
operator|)
operator|.
name|length
decl_stmt|;
name|this
operator|.
name|values
operator|=
operator|new
name|Object
index|[
name|size
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|value
argument_list|,
literal|0
argument_list|,
name|this
operator|.
name|values
argument_list|,
literal|0
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|c
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|value
decl_stmt|;
name|this
operator|.
name|values
operator|=
name|c
operator|.
name|toArray
argument_list|(
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|Iterator
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
operator|(
name|Iterator
argument_list|<
name|?
argument_list|>
operator|)
name|value
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|values
operator|.
name|add
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|values
operator|=
name|values
operator|.
name|toArray
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid value class '"
operator|+
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"', expected null, Object[], Collection, Iterator"
argument_list|)
throw|;
block|}
name|convertValues
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|convertValues
parameter_list|()
block|{
if|if
condition|(
name|values
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|values
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|values
index|[
name|i
index|]
operator|instanceof
name|Persistent
condition|)
block|{
name|values
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|Persistent
operator|)
name|values
index|[
name|i
index|]
operator|)
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|jjtClose
parameter_list|()
block|{
name|super
operator|.
name|jjtClose
argument_list|()
expr_stmt|;
comment|// For backwards compatibility set a List value wrapping the nodes.
comment|// or maybe we should rewrite the parser spec to insert children
comment|// directly into internal collection?
name|int
name|size
init|=
name|jjtGetNumChildren
argument_list|()
decl_stmt|;
name|Object
index|[]
name|listValue
init|=
operator|new
name|Object
index|[
name|size
index|]
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|listValue
index|[
name|i
index|]
operator|=
name|unwrapChild
argument_list|(
name|jjtGetChild
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setValues
argument_list|(
name|listValue
argument_list|)
expr_stmt|;
comment|// clean children - we are not supposed to use them anymore
name|children
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|hashCode
argument_list|(
name|values
argument_list|)
return|;
block|}
block|}
end_class

end_unit

