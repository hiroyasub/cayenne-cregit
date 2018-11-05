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
name|DataRow
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
name|ResultBatchIterator
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
name|ResultIterator
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
name|ResultIteratorCallback
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
name|EntityResolver
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

begin_comment
comment|/**  * A selecting query based on raw SQL and featuring fluent API.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|SQLSelect
parameter_list|<
name|T
parameter_list|>
extends|extends
name|IndirectQuery
implements|implements
name|Select
argument_list|<
name|T
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|7074293371883740872L
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|resultColumnsTypes
decl_stmt|;
specifier|private
name|boolean
name|useScalar
decl_stmt|;
specifier|private
name|boolean
name|isFetchingDataRows
decl_stmt|;
comment|/** 	 * Creates a query that selects DataRows and uses default routing. 	 */
specifier|public
specifier|static
name|SQLSelect
argument_list|<
name|DataRow
argument_list|>
name|dataRowQuery
parameter_list|(
name|String
name|sql
parameter_list|)
block|{
return|return
operator|new
name|SQLSelect
argument_list|<>
argument_list|(
name|sql
argument_list|)
operator|.
name|fetchingDataRows
argument_list|()
return|;
block|}
comment|/** 	 * Creates a query that selects DataRows and uses default routing. 	 * @since 4.1 	 */
specifier|public
specifier|static
name|SQLSelect
argument_list|<
name|DataRow
argument_list|>
name|dataRowQuery
parameter_list|(
name|String
name|sql
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|types
parameter_list|)
block|{
return|return
operator|new
name|SQLSelect
argument_list|<>
argument_list|(
name|sql
argument_list|)
operator|.
name|resultColumnsTypes
argument_list|(
name|types
argument_list|)
operator|.
name|fetchingDataRows
argument_list|()
return|;
block|}
comment|/** 	 * Creates a query that selects DataRows and uses routing based on the 	 * provided DataMap name. 	 * @since 4.1 	 */
specifier|public
specifier|static
name|SQLSelect
argument_list|<
name|DataRow
argument_list|>
name|dataRowQuery
parameter_list|(
name|String
name|dataMapName
parameter_list|,
name|String
name|sql
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|types
parameter_list|)
block|{
return|return
name|dataRowQuery
argument_list|(
name|dataMapName
argument_list|,
name|sql
argument_list|)
operator|.
name|resultColumnsTypes
argument_list|(
name|types
argument_list|)
return|;
block|}
comment|/** 	 * Creates a query that selects DataRows and uses routing based on the 	 * provided DataMap name. 	 */
specifier|public
specifier|static
name|SQLSelect
argument_list|<
name|DataRow
argument_list|>
name|dataRowQuery
parameter_list|(
name|String
name|dataMapName
parameter_list|,
name|String
name|sql
parameter_list|)
block|{
name|SQLSelect
argument_list|<
name|DataRow
argument_list|>
name|query
init|=
operator|new
name|SQLSelect
argument_list|<>
argument_list|(
name|sql
argument_list|)
decl_stmt|;
name|query
operator|.
name|dataMapName
operator|=
name|dataMapName
expr_stmt|;
return|return
name|query
operator|.
name|fetchingDataRows
argument_list|()
return|;
block|}
comment|/** 	 * Creates a query that selects DataObjects. 	 */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|query
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|sql
parameter_list|)
block|{
return|return
operator|new
name|SQLSelect
argument_list|<>
argument_list|(
name|type
argument_list|,
name|sql
argument_list|)
return|;
block|}
comment|/** 	 * Creates a query that selects scalar values and uses default routing. 	 */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|scalarQuery
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|sql
parameter_list|)
block|{
return|return
name|scalarQuery
argument_list|(
name|sql
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/** 	 * Creates a query that selects scalar values and uses routing based on the 	 * provided DataMap name. 	 */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|scalarQuery
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|dataMapName
parameter_list|,
name|String
name|sql
parameter_list|)
block|{
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|query
init|=
operator|new
name|SQLSelect
argument_list|<>
argument_list|(
name|sql
argument_list|)
decl_stmt|;
name|query
operator|.
name|dataMapName
operator|=
name|dataMapName
expr_stmt|;
return|return
name|query
operator|.
name|resultColumnsTypes
argument_list|(
name|type
argument_list|)
operator|.
name|useScalar
argument_list|()
return|;
block|}
comment|/** 	 * Creates query that selects scalar value and uses default routing 	 * 	 * @since 4.1 	 */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|scalarQuery
parameter_list|(
name|String
name|sql
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|query
init|=
operator|new
name|SQLSelect
argument_list|<>
argument_list|(
name|sql
argument_list|)
decl_stmt|;
return|return
name|query
operator|.
name|resultColumnsTypes
argument_list|(
name|type
argument_list|)
operator|.
name|useScalar
argument_list|()
return|;
block|}
comment|/** 	 * Creates query that selects scalar value and uses default routing 	 * 	 * @since 4.1 	 */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|scalarQuery
parameter_list|(
name|String
name|sql
parameter_list|,
name|String
name|dataMapName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|query
init|=
operator|new
name|SQLSelect
argument_list|<>
argument_list|(
name|sql
argument_list|)
decl_stmt|;
name|query
operator|.
name|dataMapName
operator|=
name|dataMapName
expr_stmt|;
return|return
name|query
operator|.
name|resultColumnsTypes
argument_list|(
name|type
argument_list|)
operator|.
name|useScalar
argument_list|()
return|;
block|}
comment|/** 	 * Creates query that selects scalar value and uses default routing 	 * 	 * @since 4.1 	 */
specifier|public
specifier|static
name|SQLSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|scalarQuery
parameter_list|(
name|String
name|sql
parameter_list|)
block|{
name|SQLSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|query
init|=
operator|new
name|SQLSelect
argument_list|<>
argument_list|(
name|sql
argument_list|)
decl_stmt|;
return|return
name|query
operator|.
name|useScalar
argument_list|()
return|;
block|}
comment|/** 	 * Creates query that selects scalar values (as Object[]) and uses routing based on the 	 * provided DataMap name. 	 * @since 4.1 	 */
specifier|public
specifier|static
name|SQLSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|scalarQuery
parameter_list|(
name|String
name|sql
parameter_list|,
name|String
name|dataMapName
parameter_list|)
block|{
name|SQLSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|query
init|=
operator|new
name|SQLSelect
argument_list|<>
argument_list|(
name|sql
argument_list|)
decl_stmt|;
name|query
operator|.
name|dataMapName
operator|=
name|dataMapName
expr_stmt|;
return|return
name|query
operator|.
name|useScalar
argument_list|()
return|;
block|}
comment|/** 	 * Creates query that selects scalar values (as Object[]) and uses default routing 	 * 	 * @since 4.1 	 */
specifier|public
specifier|static
name|SQLSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|scalarQuery
parameter_list|(
name|String
name|sql
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|firstType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|types
parameter_list|)
block|{
name|SQLSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|query
init|=
operator|new
name|SQLSelect
argument_list|<>
argument_list|(
name|sql
argument_list|)
decl_stmt|;
return|return
name|query
operator|.
name|resultColumnsTypes
argument_list|(
name|firstType
argument_list|)
operator|.
name|resultColumnsTypes
argument_list|(
name|types
argument_list|)
operator|.
name|useScalar
argument_list|()
return|;
block|}
comment|/** 	 * Creates query that selects scalar values (as Object[]) and uses routing based on the 	 * provided DataMap name. 	 * 	 * @since 4.1 	 */
specifier|public
specifier|static
name|SQLSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|scalarQuery
parameter_list|(
name|String
name|sql
parameter_list|,
name|String
name|dataMapName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|firstType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|types
parameter_list|)
block|{
name|SQLSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|query
init|=
operator|new
name|SQLSelect
argument_list|<>
argument_list|(
name|sql
argument_list|)
decl_stmt|;
name|query
operator|.
name|dataMapName
operator|=
name|dataMapName
expr_stmt|;
return|return
name|query
operator|.
name|resultColumnsTypes
argument_list|(
name|firstType
argument_list|)
operator|.
name|resultColumnsTypes
argument_list|(
name|types
argument_list|)
operator|.
name|useScalar
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|E
parameter_list|>
name|SQLSelect
argument_list|<
name|E
argument_list|>
name|resultColumnsTypes
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|types
parameter_list|)
block|{
if|if
condition|(
name|resultColumnsTypes
operator|==
literal|null
condition|)
block|{
name|resultColumnsTypes
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|types
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|addAll
argument_list|(
name|resultColumnsTypes
argument_list|,
name|types
argument_list|)
expr_stmt|;
return|return
operator|(
name|SQLSelect
argument_list|<
name|E
argument_list|>
operator|)
name|this
return|;
block|}
specifier|protected
name|Class
argument_list|<
name|T
argument_list|>
name|persistentType
decl_stmt|;
specifier|protected
name|String
name|dataMapName
decl_stmt|;
specifier|protected
name|StringBuilder
name|sqlBuffer
decl_stmt|;
specifier|protected
name|QueryCacheStrategy
name|cacheStrategy
decl_stmt|;
specifier|protected
name|String
name|cacheGroup
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|positionalParams
decl_stmt|;
specifier|protected
name|CapsStrategy
name|columnNameCaps
decl_stmt|;
specifier|protected
name|int
name|limit
decl_stmt|;
specifier|protected
name|int
name|offset
decl_stmt|;
specifier|protected
name|int
name|pageSize
decl_stmt|;
specifier|protected
name|int
name|statementFetchSize
decl_stmt|;
specifier|protected
name|PrefetchTreeNode
name|prefetches
decl_stmt|;
specifier|public
name|SQLSelect
parameter_list|(
name|String
name|sql
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|sql
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SQLSelect
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|persistentType
parameter_list|,
name|String
name|sql
parameter_list|)
block|{
name|this
operator|.
name|persistentType
operator|=
name|persistentType
expr_stmt|;
name|this
operator|.
name|sqlBuffer
operator|=
name|sql
operator|!=
literal|null
condition|?
operator|new
name|StringBuilder
argument_list|(
name|sql
argument_list|)
else|:
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|this
operator|.
name|limit
operator|=
name|QueryMetadata
operator|.
name|FETCH_LIMIT_DEFAULT
expr_stmt|;
name|this
operator|.
name|offset
operator|=
name|QueryMetadata
operator|.
name|FETCH_OFFSET_DEFAULT
expr_stmt|;
name|this
operator|.
name|pageSize
operator|=
name|QueryMetadata
operator|.
name|PAGE_SIZE_DEFAULT
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|select
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|context
operator|.
name|select
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|selectOne
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|context
operator|.
name|selectOne
argument_list|(
name|this
argument_list|)
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
annotation|@
name|Override
specifier|public
name|void
name|iterate
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|ResultIteratorCallback
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
block|{
name|context
operator|.
name|iterate
argument_list|(
name|this
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ResultIterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|context
operator|.
name|iterator
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResultBatchIterator
argument_list|<
name|T
argument_list|>
name|batchIterator
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|int
name|size
parameter_list|)
block|{
return|return
name|context
operator|.
name|batchIterator
argument_list|(
name|this
argument_list|,
name|size
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isFetchingDataRows
parameter_list|()
block|{
return|return
name|isFetchingDataRows
return|;
block|}
specifier|public
name|String
name|getSql
parameter_list|()
block|{
name|String
name|sql
init|=
name|sqlBuffer
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|sql
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|sql
else|:
literal|null
return|;
block|}
comment|/** 	 * Appends a piece of SQL to the previously stored SQL template. 	 */
specifier|public
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|append
parameter_list|(
name|String
name|sqlChunk
parameter_list|)
block|{
name|sqlBuffer
operator|.
name|append
argument_list|(
name|sqlChunk
argument_list|)
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
specifier|public
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|params
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|params
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
specifier|public
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|params
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|params
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|params
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|params
operator|.
name|putAll
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
comment|// since named parameters are specified, resetting positional parameters
name|this
operator|.
name|positionalParams
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Initializes positional parameters of the query. Parameters are bound in 	 * the order they are found in the SQL template. If a given parameter name 	 * is used more than once, only the first occurrence is treated as 	 * "position", subsequent occurrences are bound with the same value as the 	 * first one. If template parameters count is different from the array 	 * parameter count, an exception will be thrown. 	 *<p> 	 * Note that calling this method will reset any previously set *named* 	 * parameters. 	 */
specifier|public
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|paramsArray
parameter_list|(
name|Object
modifier|...
name|params
parameter_list|)
block|{
return|return
name|paramsList
argument_list|(
name|params
operator|!=
literal|null
condition|?
name|Arrays
operator|.
name|asList
argument_list|(
name|params
argument_list|)
else|:
literal|null
argument_list|)
return|;
block|}
comment|/** 	 * Initializes positional parameters of the query. Parameters are bound in 	 * the order they are found in the SQL template. If a given parameter name 	 * is used more than once, only the first occurrence is treated as 	 * "position", subsequent occurrences are bound with the same value as the 	 * first one. If template parameters count is different from the list 	 * parameter count, an exception will be thrown. 	 *<p> 	 * Note that calling this method will reset any previously set *named* 	 * parameters. 	 */
specifier|public
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|paramsList
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|params
parameter_list|)
block|{
name|this
operator|.
name|positionalParams
operator|=
name|params
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
comment|// since named parameters are specified, resetting positional parameters
name|this
operator|.
name|params
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Returns a potentially immmutable map of named parameters that will be 	 * bound to SQL. 	 */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParams
parameter_list|()
block|{
return|return
name|params
operator|!=
literal|null
condition|?
name|params
else|:
name|Collections
operator|.
expr|<
name|String
operator|,
name|Object
operator|>
name|emptyMap
argument_list|()
return|;
block|}
comment|/** 	 * Returns a potentially immmutable list of positional parameters that will 	 * be bound to SQL. 	 */
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getPositionalParams
parameter_list|()
block|{
return|return
name|positionalParams
operator|!=
literal|null
condition|?
name|positionalParams
else|:
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
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
name|Object
name|root
decl_stmt|;
if|if
condition|(
name|persistentType
operator|!=
literal|null
condition|)
block|{
name|root
operator|=
name|persistentType
expr_stmt|;
block|}
if|else if
condition|(
name|dataMapName
operator|!=
literal|null
condition|)
block|{
name|DataMap
name|map
init|=
name|resolver
operator|.
name|getDataMap
argument_list|(
name|dataMapName
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid dataMapName '%s'"
argument_list|,
name|dataMapName
argument_list|)
throw|;
block|}
name|root
operator|=
name|map
expr_stmt|;
block|}
else|else
block|{
comment|// will route via default node. TODO: allow explicit node name?
name|root
operator|=
literal|null
expr_stmt|;
block|}
name|SQLTemplate
name|template
init|=
operator|new
name|SQLTemplate
argument_list|()
decl_stmt|;
name|template
operator|.
name|setFetchingDataRows
argument_list|(
name|isFetchingDataRows
argument_list|)
expr_stmt|;
name|template
operator|.
name|setRoot
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|template
operator|.
name|setDefaultTemplate
argument_list|(
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|setCacheGroup
argument_list|(
name|cacheGroup
argument_list|)
expr_stmt|;
name|template
operator|.
name|setCacheStrategy
argument_list|(
name|cacheStrategy
argument_list|)
expr_stmt|;
name|template
operator|.
name|setResultColumnsTypes
argument_list|(
name|resultColumnsTypes
argument_list|)
expr_stmt|;
if|if
condition|(
name|prefetches
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|addPrefetch
argument_list|(
name|prefetches
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|positionalParams
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|setParamsList
argument_list|(
name|positionalParams
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|setParams
argument_list|(
name|params
argument_list|)
expr_stmt|;
block|}
name|template
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|columnNameCaps
argument_list|)
expr_stmt|;
name|template
operator|.
name|setFetchLimit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
name|template
operator|.
name|setFetchOffset
argument_list|(
name|offset
argument_list|)
expr_stmt|;
name|template
operator|.
name|setPageSize
argument_list|(
name|pageSize
argument_list|)
expr_stmt|;
name|template
operator|.
name|setStatementFetchSize
argument_list|(
name|statementFetchSize
argument_list|)
expr_stmt|;
name|template
operator|.
name|setUseScalar
argument_list|(
name|useScalar
argument_list|)
expr_stmt|;
return|return
name|template
return|;
block|}
comment|/** 	 * Instructs Cayenne to look for query results in the "local" cache when 	 * running the query. This is a short-hand notation for: 	 *  	 *<pre> 	 * query.cacheStrategy(QueryCacheStrategy.LOCAL_CACHE); 	 *</pre> 	 */
specifier|public
name|SQLSelect
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
comment|/** 	 * Instructs Cayenne to look for query results in the "local" cache when 	 * running the query. This is a short-hand notation for: 	 * 	 *<pre> 	 * query.cacheStrategy(QueryCacheStrategy.LOCAL_CACHE, cacheGroup); 	 *</pre> 	 */
specifier|public
name|SQLSelect
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
comment|/** 	 * Instructs Cayenne to look for query results in the "shared" cache when 	 * running the query. This is a short-hand notation for: 	 *  	 *<pre> 	 * query.cacheStrategy(QueryCacheStrategy.SHARED_CACHE); 	 *</pre> 	 */
specifier|public
name|SQLSelect
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
comment|/** 	 * Instructs Cayenne to look for query results in the "shared" cache when 	 * running the query. This is a short-hand notation for: 	 * 	 *<pre> 	 * query.cacheStrategy(QueryCacheStrategy.SHARED_CACHE, cacheGroup); 	 *</pre> 	 */
specifier|public
name|SQLSelect
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
specifier|public
name|QueryCacheStrategy
name|getCacheStrategy
parameter_list|()
block|{
return|return
name|cacheStrategy
return|;
block|}
specifier|public
name|SQLSelect
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
name|cacheStrategy
operator|!=
name|strategy
condition|)
block|{
name|cacheStrategy
operator|=
name|strategy
expr_stmt|;
name|replacementQuery
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|cacheGroup
operator|!=
literal|null
condition|)
block|{
name|cacheGroup
operator|=
literal|null
expr_stmt|;
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
name|SQLSelect
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
name|String
name|getCacheGroup
parameter_list|()
block|{
return|return
name|cacheGroup
return|;
block|}
specifier|public
name|SQLSelect
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
comment|/** 	 * Returns a column name capitalization policy applied to selecting queries. 	 * This is used to simplify mapping of the queries like "SELECT * FROM ...", 	 * ensuring that a chosen Cayenne column mapping strategy (e.g. all column 	 * names in uppercase) is portable across database engines that can have 	 * varying default capitalization. Default (null) value indicates that 	 * column names provided in result set are used unchanged. 	 */
specifier|public
name|CapsStrategy
name|getColumnNameCaps
parameter_list|()
block|{
return|return
name|columnNameCaps
return|;
block|}
comment|/** 	 * Sets a column name capitalization policy applied to selecting queries. 	 * This is used to simplify mapping of the queries like "SELECT * FROM ...", 	 * ensuring that a chosen Cayenne column mapping strategy (e.g. all column 	 * names in uppercase) is portable across database engines that can have 	 * varying default capitalization. Default (null) value indicates that 	 * column names provided in result set are used unchanged. 	 *<p> 	 * Note that while a non-default setting is useful for queries that do not 	 * rely on a #result directive to describe columns, it works for all 	 * SQLTemplates the same way. 	 */
specifier|public
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|columnNameCaps
parameter_list|(
name|CapsStrategy
name|columnNameCaps
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|columnNameCaps
operator|!=
name|columnNameCaps
condition|)
block|{
name|this
operator|.
name|columnNameCaps
operator|=
name|columnNameCaps
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
comment|/** 	 * Equivalent of setting {@link CapsStrategy#UPPER} 	 */
specifier|public
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|upperColumnNames
parameter_list|()
block|{
return|return
name|columnNameCaps
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
return|;
block|}
comment|/** 	 * Equivalent of setting {@link CapsStrategy#LOWER} 	 */
specifier|public
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|lowerColumnNames
parameter_list|()
block|{
return|return
name|columnNameCaps
argument_list|(
name|CapsStrategy
operator|.
name|LOWER
argument_list|)
return|;
block|}
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
specifier|public
name|SQLSelect
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
specifier|public
name|int
name|getOffset
parameter_list|()
block|{
return|return
name|offset
return|;
block|}
specifier|public
name|SQLSelect
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
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|pageSize
return|;
block|}
specifier|public
name|SQLSelect
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
comment|/** 	 * Sets JDBC statement's fetch size (0 for no default size) 	 */
specifier|public
name|SQLSelect
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
comment|/** 	 * @return JBDC statement's fetch size 	 */
specifier|public
name|int
name|getStatementFetchSize
parameter_list|()
block|{
return|return
name|statementFetchSize
return|;
block|}
comment|/** 	 * Merges a prefetch path with specified semantics into the query prefetch tree. 	 * @param path Path expression 	 * @param semantics Defines a strategy to prefetch relationships. See {@link PrefetchTreeNode} 	 * @return this object 	 * @since 4.1 	 */
specifier|public
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|addPrefetch
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
comment|/** 	 * Merges a prefetch into the query prefetch tree. 	 * @param node Prefetch which will added to query prefetch tree 	 * @return this object 	 * @since 4.1 	 */
specifier|public
name|SQLSelect
argument_list|<
name|T
argument_list|>
name|addPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
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
name|node
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|E
parameter_list|>
name|SQLSelect
argument_list|<
name|E
argument_list|>
name|fetchingDataRows
parameter_list|()
block|{
name|this
operator|.
name|isFetchingDataRows
operator|=
literal|true
expr_stmt|;
return|return
operator|(
name|SQLSelect
argument_list|<
name|E
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
specifier|private
parameter_list|<
name|E
parameter_list|>
name|SQLSelect
argument_list|<
name|E
argument_list|>
name|useScalar
parameter_list|()
block|{
name|this
operator|.
name|useScalar
operator|=
literal|true
expr_stmt|;
return|return
operator|(
name|SQLSelect
argument_list|<
name|E
argument_list|>
operator|)
name|this
return|;
block|}
block|}
end_class

end_unit

