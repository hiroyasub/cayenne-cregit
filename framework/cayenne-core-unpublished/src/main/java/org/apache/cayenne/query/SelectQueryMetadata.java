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
name|query
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
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
name|map
operator|.
name|EntityResolver
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
name|map
operator|.
name|ObjEntity
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|SelectQueryMetadata
extends|extends
name|BaseQueryMetadata
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|pathSplitAliases
decl_stmt|;
annotation|@
name|Override
name|void
name|copyFromInfo
parameter_list|(
name|QueryMetadata
name|info
parameter_list|)
block|{
name|super
operator|.
name|copyFromInfo
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|this
operator|.
name|pathSplitAliases
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|info
operator|.
name|getPathSplitAliases
argument_list|()
argument_list|)
expr_stmt|;
block|}
parameter_list|<
name|T
parameter_list|>
name|boolean
name|resolve
parameter_list|(
name|Object
name|root
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|SelectQuery
argument_list|<
name|T
argument_list|>
name|query
parameter_list|)
block|{
if|if
condition|(
name|super
operator|.
name|resolve
argument_list|(
name|root
argument_list|,
name|resolver
argument_list|,
literal|null
argument_list|)
condition|)
block|{
comment|// generate unique cache key...
if|if
condition|(
name|QueryCacheStrategy
operator|.
name|NO_CACHE
operator|==
name|getCacheStrategy
argument_list|()
condition|)
block|{
block|}
else|else
block|{
comment|// create a unique key based on entity, qualifier, ordering and
comment|// fetch offset and limit
name|StringBuilder
name|key
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|ObjEntity
name|entity
init|=
name|getObjEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|dbEntity
operator|!=
literal|null
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|"db:"
argument_list|)
operator|.
name|append
argument_list|(
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|query
operator|.
name|getQualifier
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
try|try
block|{
name|query
operator|.
name|getQualifier
argument_list|()
operator|.
name|appendAsString
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unexpected IO Exception appending to StringBuilder"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
operator|!
name|query
operator|.
name|getOrderings
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Ordering
name|o
range|:
name|query
operator|.
name|getOrderings
argument_list|()
control|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|o
operator|.
name|getSortSpecString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|o
operator|.
name|isAscending
argument_list|()
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|":d"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|.
name|isCaseInsensitive
argument_list|()
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|":i"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|query
operator|.
name|getFetchOffset
argument_list|()
operator|>
literal|0
operator|||
name|query
operator|.
name|getFetchLimit
argument_list|()
operator|>
literal|0
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
if|if
condition|(
name|query
operator|.
name|getFetchOffset
argument_list|()
operator|>
literal|0
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|'o'
argument_list|)
operator|.
name|append
argument_list|(
name|query
operator|.
name|getFetchOffset
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|query
operator|.
name|getFetchLimit
argument_list|()
operator|>
literal|0
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
literal|'l'
argument_list|)
operator|.
name|append
argument_list|(
name|query
operator|.
name|getFetchLimit
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|cacheKey
operator|=
name|key
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|resolveAutoAliases
argument_list|(
name|query
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|resolveAutoAliases
parameter_list|(
name|SelectQuery
argument_list|<
name|T
argument_list|>
name|query
parameter_list|)
block|{
name|Expression
name|qualifier
init|=
name|query
operator|.
name|getQualifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|qualifier
operator|!=
literal|null
condition|)
block|{
name|resolveAutoAliases
argument_list|(
name|qualifier
argument_list|)
expr_stmt|;
block|}
comment|// TODO: include aliases in prefetches? flattened attributes?
block|}
specifier|private
name|void
name|resolveAutoAliases
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|aliases
init|=
name|expression
operator|.
name|getPathAliases
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|aliases
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|pathSplitAliases
operator|==
literal|null
condition|)
block|{
name|pathSplitAliases
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|pathSplitAliases
operator|.
name|putAll
argument_list|(
name|aliases
argument_list|)
expr_stmt|;
block|}
name|int
name|len
init|=
name|expression
operator|.
name|getOperandCount
argument_list|()
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
name|Object
name|operand
init|=
name|expression
operator|.
name|getOperand
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|operand
operator|instanceof
name|Expression
condition|)
block|{
name|resolveAutoAliases
argument_list|(
operator|(
name|Expression
operator|)
name|operand
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getPathSplitAliases
parameter_list|()
block|{
return|return
name|pathSplitAliases
operator|!=
literal|null
condition|?
name|pathSplitAliases
else|:
name|Collections
operator|.
expr|<
name|String
operator|,
name|String
operator|>
name|emptyMap
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|addPathSplitAliases
parameter_list|(
name|String
name|path
parameter_list|,
name|String
modifier|...
name|aliases
parameter_list|)
block|{
if|if
condition|(
name|aliases
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null aliases"
argument_list|)
throw|;
block|}
if|if
condition|(
name|aliases
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No aliases specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|pathSplitAliases
operator|==
literal|null
condition|)
block|{
name|pathSplitAliases
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|String
name|alias
range|:
name|aliases
control|)
block|{
name|pathSplitAliases
operator|.
name|put
argument_list|(
name|alias
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
