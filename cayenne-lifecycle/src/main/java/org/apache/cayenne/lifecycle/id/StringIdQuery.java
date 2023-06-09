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
name|lifecycle
operator|.
name|id
package|;
end_package

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
name|HashSet
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
name|ObjectContext
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
name|ExpressionFactory
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
name|DataMap
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
name|DbEntity
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
name|Procedure
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
name|query
operator|.
name|ObjectSelect
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
name|query
operator|.
name|PrefetchTreeNode
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
name|query
operator|.
name|Query
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
name|query
operator|.
name|QueryCacheStrategy
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
name|query
operator|.
name|QueryChain
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
name|query
operator|.
name|QueryMetadata
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
name|query
operator|.
name|QueryRouter
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
name|query
operator|.
name|SQLAction
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
name|query
operator|.
name|SQLActionVisitor
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
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_comment
comment|/**  * A query that allows to fetch objects based on one or more String IDs. The returned  * objects do not have to be of the same type, be related via inheritance, or come from  * the same DB table. Note that if you expect multiple types of objects, use  * {@link ObjectContext#performGenericQuery(Query)}. The returned QueryResponse will  * contain separate lists of DataRows for each type in no particular order.  *<p>  * As of this writing, a limitation of this query is that it returns DataRows that need to  * be manually converted to objects if needed. In that it is similar to {@link QueryChain}.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|StringIdQuery
implements|implements
name|Query
block|{
specifier|private
specifier|static
name|Collection
argument_list|<
name|String
argument_list|>
name|toCollection
parameter_list|(
name|String
modifier|...
name|stringIds
parameter_list|)
block|{
if|if
condition|(
name|stringIds
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null stringIds"
argument_list|)
throw|;
block|}
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|stringIds
argument_list|)
return|;
block|}
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|stringIds
decl_stmt|;
specifier|protected
specifier|transient
name|Map
argument_list|<
name|String
argument_list|,
name|ObjectSelect
argument_list|>
name|idQueriesByEntity
decl_stmt|;
specifier|public
name|StringIdQuery
parameter_list|(
name|String
modifier|...
name|stringIds
parameter_list|)
block|{
name|this
argument_list|(
name|toCollection
argument_list|(
name|stringIds
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|StringIdQuery
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|stringIds
parameter_list|)
block|{
comment|// using a Set to ensure that duplicates do not result in a longer invariant
comment|// qualifier
name|this
operator|.
name|stringIds
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|stringIds
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getStringIds
parameter_list|()
block|{
return|return
name|stringIds
return|;
block|}
specifier|public
name|void
name|addStringIds
parameter_list|(
name|String
modifier|...
name|ids
parameter_list|)
block|{
if|if
condition|(
name|ids
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null ids"
argument_list|)
throw|;
block|}
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|ids
control|)
block|{
if|if
condition|(
name|stringIds
operator|.
name|add
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|changed
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|changed
condition|)
block|{
name|idQueriesByEntity
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|ObjectSelect
argument_list|>
name|getIdQueriesByEntity
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|idQueriesByEntity
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|ObjectSelect
argument_list|>
name|idQueriesByEntity
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|EntityIdCoder
argument_list|>
name|codersByEntity
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|stringIds
control|)
block|{
name|String
name|entityName
init|=
name|EntityIdCoder
operator|.
name|getEntityName
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|EntityIdCoder
name|coder
init|=
name|codersByEntity
operator|.
name|get
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
name|ObjectSelect
name|query
decl_stmt|;
if|if
condition|(
name|coder
operator|==
literal|null
condition|)
block|{
name|coder
operator|=
operator|new
name|EntityIdCoder
argument_list|(
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|entityName
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|entityName
argument_list|)
expr_stmt|;
name|codersByEntity
operator|.
name|put
argument_list|(
name|entityName
argument_list|,
name|coder
argument_list|)
expr_stmt|;
name|idQueriesByEntity
operator|.
name|put
argument_list|(
name|entityName
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|query
operator|=
name|idQueriesByEntity
operator|.
name|get
argument_list|(
name|entityName
argument_list|)
expr_stmt|;
block|}
name|Expression
name|idExp
init|=
name|ExpressionFactory
operator|.
name|matchAllDbExp
argument_list|(
name|coder
operator|.
name|toObjectId
argument_list|(
name|id
argument_list|)
operator|.
name|getIdSnapshot
argument_list|()
argument_list|,
name|Expression
operator|.
name|EQUAL_TO
argument_list|)
decl_stmt|;
name|query
operator|.
name|or
argument_list|(
name|idExp
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|idQueriesByEntity
operator|=
name|idQueriesByEntity
expr_stmt|;
block|}
return|return
name|this
operator|.
name|idQueriesByEntity
return|;
block|}
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
comment|// Cayenne doesn't know how to handle multiple root entities, so this
comment|// QueryMetadata, just like QueryChain's metadata is not very precise and won't
comment|// result in correct PersistentObjects...
return|return
operator|new
name|QueryMetadata
argument_list|()
block|{
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getResultSetMapping
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isSingleResultSetMapping
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|Query
name|getOriginatingQuery
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QueryCacheStrategy
name|getCacheStrategy
parameter_list|()
block|{
return|return
name|QueryCacheStrategy
operator|.
name|getDefaultStrategy
argument_list|()
return|;
block|}
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Procedure
name|getProcedure
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getCacheGroup
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isFetchingDataRows
parameter_list|()
block|{
comment|// overriding this... Can't fetch objects until DataDomainQueryAction
comment|// starts converting multiple ResultSets to object... Same as QueryChain
comment|// essentially.
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|isRefreshingObjects
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|QueryMetadata
operator|.
name|PAGE_SIZE_DEFAULT
return|;
block|}
specifier|public
name|int
name|getFetchOffset
parameter_list|()
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|int
name|getFetchLimit
parameter_list|()
block|{
return|return
name|QueryMetadata
operator|.
name|FETCH_LIMIT_DEFAULT
return|;
block|}
specifier|public
name|PrefetchTreeNode
name|getPrefetchTree
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
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
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
specifier|public
name|int
name|getStatementFetchSize
parameter_list|()
block|{
return|return
name|QueryMetadata
operator|.
name|STATEMENT_FETCH_SIZE_DEFAULT
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getQueryTimeout
parameter_list|()
block|{
return|return
name|QueryMetadata
operator|.
name|QUERY_TIMEOUT_DEFAULT
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isSuppressingDistinct
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
return|;
block|}
specifier|public
name|void
name|route
parameter_list|(
name|QueryRouter
name|router
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|Query
name|substitutedQuery
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|ObjectSelect
argument_list|>
name|queries
init|=
name|getIdQueriesByEntity
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjectSelect
name|query
range|:
name|queries
operator|.
name|values
argument_list|()
control|)
block|{
name|query
operator|.
name|route
argument_list|(
name|router
argument_list|,
name|resolver
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|SQLAction
name|createSQLAction
parameter_list|(
name|SQLActionVisitor
name|visitor
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"This query was supposed to be replace with a set of SelectQueries during the route phase"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

