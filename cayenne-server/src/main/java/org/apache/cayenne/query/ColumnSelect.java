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
name|sql
operator|.
name|Statement
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
name|Collections
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
name|exp
operator|.
name|Property
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

begin_comment
comment|/**  *<p>A helper builder for queries selecting individual properties based on the root object.</p>  *<p>  *     It can be used to select properties of the object itself, properties of related entities  *     or some function calls (including aggregate functions).  *</p>  *<p>  * Usage examples:<pre>  * {@code  *      // select list of names:  *      List<String> names = ObjectSelect.columnQuery(Artist.class, Artist.ARTIST_NAME).select(context);  *  *      // select count:  *      long count = ObjectSelect.columnQuery(Artist.class, Property.COUNT).selectOne();  *  *      // select only required properties of an entity:  *      List<Object[]> data = ObjectSelect.columnQuery(Artist.class, Artist.ARTIST_NAME, Artist.DATE_OF_BIRTH)  *                                  .where(Artist.ARTIST_NAME.like("Picasso%))  *                                  .select(context);  * }  *</pre>  *</p>  *<p><b>Note: this class can't be instantiated directly. Use {@link ObjectSelect}.</b></p>  * @see ObjectSelect#columnQuery(Class, Property)  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ColumnSelect
parameter_list|<
name|T
parameter_list|>
extends|extends
name|FluentSelect
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|Collection
argument_list|<
name|Property
argument_list|<
name|?
argument_list|>
argument_list|>
name|columns
decl_stmt|;
specifier|private
name|boolean
name|havingExpressionIsActive
init|=
literal|false
decl_stmt|;
comment|// package private for tests
name|boolean
name|singleColumn
init|=
literal|true
decl_stmt|;
specifier|private
name|Expression
name|having
decl_stmt|;
name|boolean
name|distinct
decl_stmt|;
name|boolean
name|suppressDistinct
decl_stmt|;
specifier|protected
name|ColumnSelect
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Copy constructor to convert ObjectSelect to ColumnSelect      */
specifier|protected
name|ColumnSelect
parameter_list|(
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|select
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|select
operator|.
name|name
expr_stmt|;
name|this
operator|.
name|entityType
operator|=
name|select
operator|.
name|entityType
expr_stmt|;
name|this
operator|.
name|entityName
operator|=
name|select
operator|.
name|entityName
expr_stmt|;
name|this
operator|.
name|dbEntityName
operator|=
name|select
operator|.
name|dbEntityName
expr_stmt|;
name|this
operator|.
name|where
operator|=
name|select
operator|.
name|where
expr_stmt|;
name|this
operator|.
name|orderings
operator|=
name|select
operator|.
name|orderings
expr_stmt|;
name|this
operator|.
name|prefetches
operator|=
name|select
operator|.
name|prefetches
expr_stmt|;
name|this
operator|.
name|limit
operator|=
name|select
operator|.
name|limit
expr_stmt|;
name|this
operator|.
name|offset
operator|=
name|select
operator|.
name|offset
expr_stmt|;
name|this
operator|.
name|pageSize
operator|=
name|select
operator|.
name|pageSize
expr_stmt|;
name|this
operator|.
name|statementFetchSize
operator|=
name|select
operator|.
name|statementFetchSize
expr_stmt|;
name|this
operator|.
name|cacheStrategy
operator|=
name|select
operator|.
name|cacheStrategy
expr_stmt|;
name|this
operator|.
name|cacheGroup
operator|=
name|select
operator|.
name|cacheGroup
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Query
name|createReplacementQuery
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|replacement
init|=
operator|(
name|SelectQuery
operator|)
name|super
operator|.
name|createReplacementQuery
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|replacement
operator|.
name|setColumns
argument_list|(
name|columns
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|setHavingQualifier
argument_list|(
name|having
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|setCanReturnScalarValue
argument_list|(
name|singleColumn
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|setDistinct
argument_list|(
name|distinct
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|setSuppressDistinct
argument_list|(
name|suppressDistinct
argument_list|)
expr_stmt|;
return|return
name|replacement
return|;
block|}
comment|/**      * Sets the type of the entity to fetch without changing the return type of      * the query.      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|entityType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityType
parameter_list|)
block|{
return|return
name|resetEntity
argument_list|(
name|entityType
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Sets the {@link ObjEntity} name to fetch without changing the return type      * of the query. This form is most often used for generic entities that      * don't map to a distinct class.      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|entityName
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
return|return
name|resetEntity
argument_list|(
literal|null
argument_list|,
name|entityName
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Sets the {@link DbEntity} name to fetch without changing the return type      * of the query. This form is most often used for generic entities that      * don't map to a distinct class.      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|dbEntityName
parameter_list|(
name|String
name|dbEntityName
parameter_list|)
block|{
return|return
name|resetEntity
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|dbEntityName
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|resetEntity
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityType
parameter_list|,
name|String
name|entityName
parameter_list|,
name|String
name|dbEntityName
parameter_list|)
block|{
name|this
operator|.
name|entityType
operator|=
name|entityType
expr_stmt|;
name|this
operator|.
name|entityName
operator|=
name|entityName
expr_stmt|;
name|this
operator|.
name|dbEntityName
operator|=
name|dbEntityName
expr_stmt|;
return|return
operator|(
name|ColumnSelect
argument_list|<
name|T
argument_list|>
operator|)
name|this
return|;
block|}
comment|/**      * Appends a qualifier expression of this query. An equivalent to      * {@link #and(Expression...)} that can be used a syntactic sugar.      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|where
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
return|return
name|and
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Appends a qualifier expression of this query, using provided expression      * String and an array of position parameters. This is an equivalent to      * calling "and".      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|where
parameter_list|(
name|String
name|expressionString
parameter_list|,
name|Object
modifier|...
name|parameters
parameter_list|)
block|{
return|return
name|and
argument_list|(
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expressionString
argument_list|,
name|parameters
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * AND's provided expressions to the existing WHERE clause expression.      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|and
parameter_list|(
name|Expression
modifier|...
name|expressions
parameter_list|)
block|{
if|if
condition|(
name|expressions
operator|==
literal|null
operator|||
name|expressions
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
return|return
name|and
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|expressions
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * OR's provided expressions to the existing WHERE clause expression.      *      * @return this object      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|or
parameter_list|(
name|Expression
modifier|...
name|expressions
parameter_list|)
block|{
if|if
condition|(
name|expressions
operator|==
literal|null
operator|||
name|expressions
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
return|return
name|or
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|expressions
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Add an ascending ordering on the given property. If there is already an ordering      * on this query then add this ordering with a lower priority.      *      * @param property the property to sort on      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|orderBy
parameter_list|(
name|String
name|property
parameter_list|)
block|{
return|return
name|orderBy
argument_list|(
operator|new
name|Ordering
argument_list|(
name|property
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Add an ordering on the given property. If there is already an ordering      * on this query then add this ordering with a lower priority.      *      * @param property  the property to sort on      * @param sortOrder the direction of the ordering      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|orderBy
parameter_list|(
name|String
name|property
parameter_list|,
name|SortOrder
name|sortOrder
parameter_list|)
block|{
return|return
name|orderBy
argument_list|(
operator|new
name|Ordering
argument_list|(
name|property
argument_list|,
name|sortOrder
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Add one or more orderings to this query.      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|orderBy
parameter_list|(
name|Ordering
modifier|...
name|orderings
parameter_list|)
block|{
if|if
condition|(
name|orderings
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|this
operator|.
name|orderings
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|orderings
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|orderings
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|addAll
argument_list|(
name|this
operator|.
name|orderings
argument_list|,
name|orderings
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds a list of orderings to this query.      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|orderBy
parameter_list|(
name|Collection
argument_list|<
name|Ordering
argument_list|>
name|orderings
parameter_list|)
block|{
if|if
condition|(
name|orderings
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|this
operator|.
name|orderings
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|orderings
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|orderings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|orderings
operator|.
name|addAll
argument_list|(
name|orderings
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Merges prefetch into the query prefetch tree.      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|prefetch
parameter_list|(
name|PrefetchTreeNode
name|prefetch
parameter_list|)
block|{
if|if
condition|(
name|prefetch
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|prefetches
operator|==
literal|null
condition|)
block|{
name|prefetches
operator|=
operator|new
name|PrefetchTreeNode
argument_list|()
expr_stmt|;
block|}
name|prefetches
operator|.
name|merge
argument_list|(
name|prefetch
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Merges a prefetch path with specified semantics into the query prefetch      * tree.      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|prefetch
parameter_list|(
name|String
name|path
parameter_list|,
name|int
name|semantics
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|prefetches
operator|==
literal|null
condition|)
block|{
name|prefetches
operator|=
operator|new
name|PrefetchTreeNode
argument_list|()
expr_stmt|;
block|}
name|prefetches
operator|.
name|addPath
argument_list|(
name|path
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|semantics
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Resets query fetch limit - a parameter that defines max number of objects      * that should be ever be fetched from the database.      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|limit
parameter_list|(
name|int
name|fetchLimit
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|limit
operator|!=
name|fetchLimit
condition|)
block|{
name|this
operator|.
name|limit
operator|=
name|fetchLimit
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Resets query fetch offset - a parameter that defines how many objects      * should be skipped when reading data from the database.      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|offset
parameter_list|(
name|int
name|fetchOffset
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|offset
operator|!=
name|fetchOffset
condition|)
block|{
name|this
operator|.
name|offset
operator|=
name|fetchOffset
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Resets query page size. A non-negative page size enables query result      * pagination that saves memory and processing time for large lists if only      * parts of the result are ever going to be accessed.      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|pageSize
parameter_list|(
name|int
name|pageSize
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|pageSize
operator|!=
name|pageSize
condition|)
block|{
name|this
operator|.
name|pageSize
operator|=
name|pageSize
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Sets fetch size of the PreparedStatement generated for this query. Only      * non-negative values would change the default size.      *      * @see Statement#setFetchSize(int)      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|statementFetchSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|statementFetchSize
operator|!=
name|size
condition|)
block|{
name|this
operator|.
name|statementFetchSize
operator|=
name|size
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|cacheStrategy
parameter_list|(
name|QueryCacheStrategy
name|strategy
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|cacheStrategy
operator|!=
name|strategy
condition|)
block|{
name|this
operator|.
name|cacheStrategy
operator|=
name|strategy
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|cacheGroup
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|cacheGroup
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|cacheStrategy
parameter_list|(
name|QueryCacheStrategy
name|strategy
parameter_list|,
name|String
name|cacheGroup
parameter_list|)
block|{
return|return
name|cacheStrategy
argument_list|(
name|strategy
argument_list|)
operator|.
name|cacheGroup
argument_list|(
name|cacheGroup
argument_list|)
return|;
block|}
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|cacheGroup
parameter_list|(
name|String
name|cacheGroup
parameter_list|)
block|{
name|this
operator|.
name|cacheGroup
operator|=
name|cacheGroup
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Instructs Cayenne to look for query results in the "local" cache when      * running the query. This is a short-hand notation for:      *<p>      *<pre>      * query.cacheStrategy(QueryCacheStrategy.LOCAL_CACHE, cacheGroup);      *</pre>      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|localCache
parameter_list|(
name|String
name|cacheGroup
parameter_list|)
block|{
return|return
name|cacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|,
name|cacheGroup
argument_list|)
return|;
block|}
comment|/**      * Instructs Cayenne to look for query results in the "local" cache when      * running the query. This is a short-hand notation for:      *<p>      *<pre>      * query.cacheStrategy(QueryCacheStrategy.LOCAL_CACHE);      *</pre>      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|localCache
parameter_list|()
block|{
return|return
name|cacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
return|;
block|}
comment|/**      * Instructs Cayenne to look for query results in the "shared" cache when      * running the query. This is a short-hand notation for:      *<p>      *<pre>      * query.cacheStrategy(QueryCacheStrategy.SHARED_CACHE, cacheGroup);      *</pre>      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|sharedCache
parameter_list|(
name|String
name|cacheGroup
parameter_list|)
block|{
return|return
name|cacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|,
name|cacheGroup
argument_list|)
return|;
block|}
comment|/**      * Instructs Cayenne to look for query results in the "shared" cache when      * running the query. This is a short-hand notation for:      *<p>      *<pre>      * query.cacheStrategy(QueryCacheStrategy.SHARED_CACHE);      *</pre>      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|sharedCache
parameter_list|()
block|{
return|return
name|cacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|)
return|;
block|}
comment|/**      *<p>Add properties to select.</p>      *<p>Can be any properties that can be resolved against root entity type      * (root entity properties, function call expressions, properties of relationships, etc).</p>      *<p>      *<pre>      * {@code      * List<Object[]> columns = ObjectSelect.columnQuery(Artist.class, Artist.ARTIST_NAME)      *                                    .columns(Artist.ARTIST_SALARY, Artist.DATE_OF_BIRTH)      *                                    .select(context);      * }      *</pre>      *      * @param firstProperty first property      * @param otherProperties array of properties to select      * @see ColumnSelect#column(Property)      * @see ColumnSelect#columns(Collection)      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|ColumnSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|columns
parameter_list|(
name|Property
argument_list|<
name|?
argument_list|>
name|firstProperty
parameter_list|,
name|Property
argument_list|<
name|?
argument_list|>
modifier|...
name|otherProperties
parameter_list|)
block|{
if|if
condition|(
name|columns
operator|==
literal|null
condition|)
block|{
name|columns
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|otherProperties
operator|.
name|length
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|columns
operator|.
name|add
argument_list|(
name|firstProperty
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|columns
argument_list|,
name|otherProperties
argument_list|)
expr_stmt|;
name|singleColumn
operator|=
literal|false
expr_stmt|;
return|return
operator|(
name|ColumnSelect
argument_list|<
name|Object
index|[]
argument_list|>
operator|)
name|this
return|;
block|}
comment|/**      *<p>Add properties to select.</p>      *<p>Can be any properties that can be resolved against root entity type      * (root entity properties, function call expressions, properties of relationships, etc).</p>      *<p>      * @param properties collection of properties,<b>must</b> contain at least one element      * @see ColumnSelect#columns(Property, Property[])      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|ColumnSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|columns
parameter_list|(
name|Collection
argument_list|<
name|Property
argument_list|<
name|?
argument_list|>
argument_list|>
name|properties
parameter_list|)
block|{
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"properties is null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|properties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"properties must contain at least one element"
argument_list|)
throw|;
block|}
if|if
condition|(
name|this
operator|.
name|columns
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|columns
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|properties
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|columns
operator|.
name|addAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|singleColumn
operator|=
literal|false
expr_stmt|;
return|return
operator|(
name|ColumnSelect
argument_list|<
name|Object
index|[]
argument_list|>
operator|)
name|this
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
parameter_list|<
name|E
parameter_list|>
name|ColumnSelect
argument_list|<
name|E
argument_list|>
name|column
parameter_list|(
name|Property
argument_list|<
name|E
argument_list|>
name|property
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|columns
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|columns
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|columns
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// if we don't clear then return type will be incorrect
block|}
name|this
operator|.
name|columns
operator|.
name|add
argument_list|(
name|property
argument_list|)
expr_stmt|;
return|return
operator|(
name|ColumnSelect
argument_list|<
name|E
argument_list|>
operator|)
name|this
return|;
block|}
comment|/**      *<p>Shortcut for {@link #columns(Property, Property[])} columns}(Property.COUNT)</p>      */
specifier|public
name|ColumnSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|count
parameter_list|()
block|{
return|return
name|columns
argument_list|(
name|Property
operator|.
name|COUNT
argument_list|)
return|;
block|}
comment|/**      *<p>Select COUNT(property)</p>      *<p>Can return different result than COUNT(*) as it will count only non null values</p>      * @see ColumnSelect#count()      */
specifier|public
name|ColumnSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|count
parameter_list|(
name|Property
argument_list|<
name|?
argument_list|>
name|property
parameter_list|)
block|{
return|return
name|columns
argument_list|(
name|property
operator|.
name|count
argument_list|()
argument_list|)
return|;
block|}
comment|/**      *<p>Select minimum value of property</p>      * @see ColumnSelect#columns(Property, Property[])      */
specifier|public
name|ColumnSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|min
parameter_list|(
name|Property
argument_list|<
name|?
argument_list|>
name|property
parameter_list|)
block|{
return|return
name|columns
argument_list|(
name|property
operator|.
name|min
argument_list|()
argument_list|)
return|;
block|}
comment|/**      *<p>Select maximum value of property</p>      * @see ColumnSelect#columns(Property, Property[])      */
specifier|public
name|ColumnSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|max
parameter_list|(
name|Property
argument_list|<
name|?
argument_list|>
name|property
parameter_list|)
block|{
return|return
name|columns
argument_list|(
name|property
operator|.
name|max
argument_list|()
argument_list|)
return|;
block|}
comment|/**      *<p>Select average value of property</p>      * @see ColumnSelect#columns(Property, Property[])      */
specifier|public
name|ColumnSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|avg
parameter_list|(
name|Property
argument_list|<
name|?
argument_list|>
name|property
parameter_list|)
block|{
return|return
name|columns
argument_list|(
name|property
operator|.
name|avg
argument_list|()
argument_list|)
return|;
block|}
comment|/**      *<p>Select sum of values</p>      * @see ColumnSelect#columns(Property, Property[])      */
specifier|public
parameter_list|<
name|E
extends|extends
name|Number
parameter_list|>
name|ColumnSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|sum
parameter_list|(
name|Property
argument_list|<
name|E
argument_list|>
name|property
parameter_list|)
block|{
return|return
name|columns
argument_list|(
name|property
operator|.
name|sum
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Appends a having qualifier expression of this query. An equivalent to      * {@link #and(Expression...)} that can be used a syntactic sugar.      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|having
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|havingExpressionIsActive
operator|=
literal|true
expr_stmt|;
return|return
name|and
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Appends a having qualifier expression of this query, using provided expression      * String and an array of position parameters. This is an equivalent to      * calling "and".      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|having
parameter_list|(
name|String
name|expressionString
parameter_list|,
name|Object
modifier|...
name|parameters
parameter_list|)
block|{
name|havingExpressionIsActive
operator|=
literal|true
expr_stmt|;
return|return
name|and
argument_list|(
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expressionString
argument_list|,
name|parameters
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * AND's provided expressions to the existing WHERE or HAVING clause expression.      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|and
parameter_list|(
name|Collection
argument_list|<
name|Expression
argument_list|>
name|expressions
parameter_list|)
block|{
if|if
condition|(
name|expressions
operator|==
literal|null
operator|||
name|expressions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
name|Collection
argument_list|<
name|Expression
argument_list|>
name|all
decl_stmt|;
name|Expression
name|activeExpression
init|=
name|getActiveExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|activeExpression
operator|!=
literal|null
condition|)
block|{
name|all
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|expressions
operator|.
name|size
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|all
operator|.
name|add
argument_list|(
name|activeExpression
argument_list|)
expr_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|expressions
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|all
operator|=
name|expressions
expr_stmt|;
block|}
name|setActiveExpression
argument_list|(
name|ExpressionFactory
operator|.
name|and
argument_list|(
name|all
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * OR's provided expressions to the existing WHERE or HAVING clause expression.      *      * @return this object      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|or
parameter_list|(
name|Collection
argument_list|<
name|Expression
argument_list|>
name|expressions
parameter_list|)
block|{
if|if
condition|(
name|expressions
operator|==
literal|null
operator|||
name|expressions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
name|Collection
argument_list|<
name|Expression
argument_list|>
name|all
decl_stmt|;
name|Expression
name|activeExpression
init|=
name|getActiveExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|activeExpression
operator|!=
literal|null
condition|)
block|{
name|all
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|expressions
operator|.
name|size
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|all
operator|.
name|add
argument_list|(
name|activeExpression
argument_list|)
expr_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|expressions
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|all
operator|=
name|expressions
expr_stmt|;
block|}
name|setActiveExpression
argument_list|(
name|ExpressionFactory
operator|.
name|or
argument_list|(
name|all
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Explicitly request distinct in query.      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|distinct
parameter_list|()
block|{
name|this
operator|.
name|suppressDistinct
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|distinct
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Explicitly suppress distinct in query.      */
specifier|public
name|ColumnSelect
argument_list|<
name|T
argument_list|>
name|suppressDistinct
parameter_list|()
block|{
name|this
operator|.
name|suppressDistinct
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|distinct
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|private
name|void
name|setActiveExpression
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
if|if
condition|(
name|havingExpressionIsActive
condition|)
block|{
name|having
operator|=
name|exp
expr_stmt|;
block|}
else|else
block|{
name|where
operator|=
name|exp
expr_stmt|;
block|}
block|}
specifier|private
name|Expression
name|getActiveExpression
parameter_list|()
block|{
if|if
condition|(
name|havingExpressionIsActive
condition|)
block|{
return|return
name|having
return|;
block|}
else|else
block|{
return|return
name|where
return|;
block|}
block|}
specifier|public
name|Collection
argument_list|<
name|Property
argument_list|<
name|?
argument_list|>
argument_list|>
name|getColumns
parameter_list|()
block|{
return|return
name|columns
return|;
block|}
comment|/**      * Returns a HAVING clause Expression of this query.      */
specifier|public
name|Expression
name|getHaving
parameter_list|()
block|{
return|return
name|having
return|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|selectFirst
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|context
operator|.
name|selectFirst
argument_list|(
name|limit
argument_list|(
literal|1
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

