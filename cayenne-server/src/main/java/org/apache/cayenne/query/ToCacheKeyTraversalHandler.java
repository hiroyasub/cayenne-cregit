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
name|query
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
name|access
operator|.
name|types
operator|.
name|ValueObjectType
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
name|access
operator|.
name|types
operator|.
name|ValueObjectTypeRegistry
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
name|TraversalHandler
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
name|parser
operator|.
name|ASTFunctionCall
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
name|parser
operator|.
name|ASTScalar
import|;
end_import

begin_comment
comment|/**  * Expression traverse handler to create cache key string out of Expression.  * {@link Expression#appendAsString(Appendable)} where previously used for that,  * but it can't handle custom value objects properly (see CAY-2210).  *  * @see ValueObjectTypeRegistry  * @since 4.2  */
end_comment

begin_class
class|class
name|ToCacheKeyTraversalHandler
implements|implements
name|TraversalHandler
block|{
specifier|private
name|ValueObjectTypeRegistry
name|registry
decl_stmt|;
specifier|private
name|StringBuilder
name|out
decl_stmt|;
name|ToCacheKeyTraversalHandler
parameter_list|(
name|ValueObjectTypeRegistry
name|registry
parameter_list|,
name|StringBuilder
name|out
parameter_list|)
block|{
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|finishedChild
parameter_list|(
name|Expression
name|node
parameter_list|,
name|int
name|childIndex
parameter_list|,
name|boolean
name|hasMoreChildren
parameter_list|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|startNode
parameter_list|(
name|Expression
name|node
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|FUNCTION_CALL
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
operator|(
operator|(
name|ASTFunctionCall
operator|)
name|node
operator|)
operator|.
name|getFunctionName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|append
argument_list|(
name|node
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|endNode
parameter_list|(
name|Expression
name|node
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
block|{
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
name|objectNode
parameter_list|(
name|Object
name|leaf
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
block|{
if|if
condition|(
name|leaf
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
return|return;
block|}
if|if
condition|(
name|leaf
operator|instanceof
name|ASTScalar
condition|)
block|{
name|leaf
operator|=
operator|(
operator|(
name|ASTScalar
operator|)
name|leaf
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|leaf
operator|instanceof
name|Object
index|[]
condition|)
block|{
for|for
control|(
name|Object
name|value
range|:
operator|(
name|Object
index|[]
operator|)
name|leaf
control|)
block|{
name|objectNode
argument_list|(
name|value
argument_list|,
name|parentNode
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
if|if
condition|(
name|leaf
operator|instanceof
name|Persistent
condition|)
block|{
name|ObjectId
name|id
init|=
operator|(
operator|(
name|Persistent
operator|)
name|leaf
operator|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|Object
name|encode
init|=
operator|(
name|id
operator|!=
literal|null
operator|)
condition|?
name|id
else|:
name|leaf
decl_stmt|;
name|out
operator|.
name|append
argument_list|(
name|encode
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|leaf
operator|instanceof
name|Enum
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|Enum
argument_list|<
name|?
argument_list|>
name|e
init|=
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
name|leaf
decl_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|"e:"
argument_list|)
operator|.
name|append
argument_list|(
name|leaf
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
operator|.
name|append
argument_list|(
name|e
operator|.
name|ordinal
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ValueObjectType
argument_list|<
name|Object
argument_list|,
name|?
argument_list|>
name|valueObjectType
decl_stmt|;
if|if
condition|(
name|registry
operator|==
literal|null
operator|||
operator|(
name|valueObjectType
operator|=
name|registry
operator|.
name|getValueType
argument_list|(
name|leaf
operator|.
name|getClass
argument_list|()
argument_list|)
operator|)
operator|==
literal|null
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
name|leaf
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|append
argument_list|(
name|valueObjectType
operator|.
name|toCacheKey
argument_list|(
name|leaf
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

