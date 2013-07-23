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
name|Collections
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
name|ejbql
operator|.
name|EJBQLCompiledExpression
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
comment|/**  * A metadata object for the {@link EJBQLQuery}.  *   * @since 3.0  */
end_comment

begin_class
class|class
name|EJBQLQueryMetadata
extends|extends
name|BaseQueryMetadata
block|{
name|boolean
name|resolve
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|,
name|EJBQLQuery
name|query
parameter_list|)
block|{
name|EJBQLCompiledExpression
name|expression
init|=
name|query
operator|.
name|getExpression
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|setPrefetchTree
argument_list|(
name|expression
operator|.
name|getPrefetchTree
argument_list|()
argument_list|)
expr_stmt|;
name|resultSetMapping
operator|=
name|expression
operator|.
name|getResult
argument_list|()
operator|!=
literal|null
condition|?
name|expression
operator|.
name|getResult
argument_list|()
operator|.
name|getResolvedComponents
argument_list|(
name|resolver
argument_list|)
else|:
literal|null
expr_stmt|;
name|ObjEntity
name|root
init|=
name|expression
operator|.
name|getRootDescriptor
argument_list|()
operator|.
name|getEntity
argument_list|()
decl_stmt|;
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
comment|// create a unique key based on entity, EJBQL, and parameters
name|StringBuilder
name|key
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|.
name|getEjbqlStatement
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
operator|.
name|append
argument_list|(
name|query
operator|.
name|getEjbqlStatement
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
literal|'/'
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|namedParameters
init|=
name|query
operator|.
name|getNamedParameters
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|namedParameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|namedParameters
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|keys
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|parameterKey
range|:
name|keys
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
name|parameterKey
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|namedParameters
operator|.
name|get
argument_list|(
name|parameterKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|positionalParameters
init|=
name|query
operator|.
name|getPositionalParameters
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|positionalParameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|positionalParameters
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|keys
argument_list|)
expr_stmt|;
for|for
control|(
name|Integer
name|parameterKey
range|:
name|keys
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
name|parameterKey
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|positionalParameters
operator|.
name|get
argument_list|(
name|parameterKey
argument_list|)
argument_list|)
expr_stmt|;
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
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit
