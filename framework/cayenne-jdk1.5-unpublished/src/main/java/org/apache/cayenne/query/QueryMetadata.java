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
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_comment
comment|/**  * Provides a common interface for accessing query metadata.  *   * @since 1.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|QueryMetadata
block|{
comment|/**      * A cache policy that disables caching of query results.      *       * @deprecated since 3.0 use {@link QueryCacheStrategy} enum.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|NO_CACHE
init|=
literal|"nocache"
decl_stmt|;
comment|/**      * A cache policy ruling that query results shall be cached separately for each      * DataContext.      *       * @deprecated since 3.0 use {@link QueryCacheStrategy} enum.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|LOCAL_CACHE
init|=
literal|"localcache"
decl_stmt|;
comment|/**      * Same as {@link #LOCAL_CACHE}, only forcing any current cache expiration.      *       * @deprecated since 3.0 use {@link QueryCacheStrategy} enum.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|LOCAL_CACHE_REFRESH
init|=
literal|"localcache_refresh"
decl_stmt|;
comment|/**      * A cache policy ruling that query results shall be stored in a shared cache      * accessible by all DataContexts.      *       * @deprecated since 3.0 use {@link QueryCacheStrategy} enum.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|SHARED_CACHE
init|=
literal|"sharedcache"
decl_stmt|;
comment|/**      * Same as {@link #SHARED_CACHE}, only forcing any current cache expiration.      *       * @deprecated since 3.0 use {@link QueryCacheStrategy} enum.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|SHARED_CACHE_REFRESH
init|=
literal|"sharedcache_refresh"
decl_stmt|;
comment|/**      * Defines the name of the property for the query {@link #getFetchLimit() fetch limit}      * .      */
specifier|public
specifier|static
specifier|final
name|String
name|FETCH_LIMIT_PROPERTY
init|=
literal|"cayenne.GenericSelectQuery.fetchLimit"
decl_stmt|;
comment|/**      * Defines default query fetch limit, which is zero, meaning that all matching rows      * should be fetched.      */
specifier|public
specifier|static
specifier|final
name|int
name|FETCH_LIMIT_DEFAULT
init|=
literal|0
decl_stmt|;
comment|/**      * Defines the name of the property for the query {@link #getFetchOffset() fetch      * offset}.      *       * @since 3.0      */
specifier|public
specifier|static
specifier|final
name|String
name|FETCH_OFFSET_PROPERTY
init|=
literal|"cayenne.GenericSelectQuery.fetchOffset"
decl_stmt|;
comment|/**      * Defines default query fetch start index, which is 0, meaning that matching rows      * selected starting from the first.      *       * @since 3.0      */
specifier|public
specifier|static
specifier|final
name|int
name|FETCH_OFFSET_DEFAULT
init|=
literal|0
decl_stmt|;
comment|/**      * Defines the name of the property for the query {@link #getPageSize() page size}.      */
specifier|public
specifier|static
specifier|final
name|String
name|PAGE_SIZE_PROPERTY
init|=
literal|"cayenne.GenericSelectQuery.pageSize"
decl_stmt|;
comment|/**      * Defines default query page size, which is zero for no pagination.      */
specifier|public
specifier|static
specifier|final
name|int
name|PAGE_SIZE_DEFAULT
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FETCHING_DATA_ROWS_PROPERTY
init|=
literal|"cayenne.GenericSelectQuery.fetchingDataRows"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|boolean
name|FETCHING_DATA_ROWS_DEFAULT
init|=
literal|false
decl_stmt|;
comment|/**      * @deprecated since 3.0      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|REFRESHING_OBJECTS_PROPERTY
init|=
literal|"cayenne.GenericSelectQuery.refreshingObjects"
decl_stmt|;
comment|/**      * @deprecated since 3.0      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|boolean
name|REFRESHING_OBJECTS_DEFAULT
init|=
literal|true
decl_stmt|;
comment|/**      * @deprecated since 3.0. Inheritance resolving is not optional anymore.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|RESOLVING_INHERITED_PROPERTY
init|=
literal|"cayenne.GenericSelectQuery.resolvingInherited"
decl_stmt|;
comment|/**      * @deprecated since 3.0. Inheritance resolving is not optional anymore.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|boolean
name|RESOLVING_INHERITED_DEFAULT
init|=
literal|true
decl_stmt|;
comment|/**      * @deprecated since 3.0 use {@value #CACHE_STRATEGY_PROPERTY}      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|CACHE_POLICY_PROPERTY
init|=
literal|"cayenne.GenericSelectQuery.cachePolicy"
decl_stmt|;
comment|/**      * @since 3.0      */
specifier|public
specifier|static
specifier|final
name|String
name|CACHE_STRATEGY_PROPERTY
init|=
literal|"cayenne.GenericSelectQuery.cacheStrategy"
decl_stmt|;
comment|/**      * @since 3.0      */
specifier|public
specifier|static
specifier|final
name|String
name|CACHE_GROUPS_PROPERTY
init|=
literal|"cayenne.GenericSelectQuery.cacheGroups"
decl_stmt|;
comment|/**      * @deprecated since 3.0 use {@link QueryCacheStrategy} enum.      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|CACHE_POLICY_DEFAULT
init|=
name|NO_CACHE
decl_stmt|;
comment|/**      * Defines the name of the property for the query {@link #getStatementFetchSize() fetch      * size}.      *       * @since 3.0      */
specifier|public
specifier|static
specifier|final
name|String
name|STATEMENT_FETCH_SIZE_PROPERTY
init|=
literal|"cayenne.GenericSelectQuery.statementFetchSize"
decl_stmt|;
comment|/**      * Defines default query fetch start index, which is 0, meaning that matching rows      * selected starting from the first.      *       * @since 3.0      */
specifier|public
specifier|static
specifier|final
name|int
name|STATEMENT_FETCH_SIZE_DEFAULT
init|=
literal|0
decl_stmt|;
comment|/**      * @since 3.0      */
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
function_decl|;
comment|/**      * Returns an ObjEntity associated with a query or null if no such entity exists.      */
name|ObjEntity
name|getObjEntity
parameter_list|()
function_decl|;
comment|/**      * Returns a DbEntity associated with a query or null if no such entity exists.      */
name|DbEntity
name|getDbEntity
parameter_list|()
function_decl|;
comment|/**      * Returns a Procedure associated with a query or null if no such procedure exists.      */
name|Procedure
name|getProcedure
parameter_list|()
function_decl|;
comment|/**      * Returns a DataMap associated with a query or null if no such DataMap exists.      */
name|DataMap
name|getDataMap
parameter_list|()
function_decl|;
comment|/**      * Returns query cache policy, which can be one of {@link #NO_CACHE},      * {@link #LOCAL_CACHE}, or {@link #SHARED_CACHE}. NO_CACHE is generally a default      * policy.      *       * @deprecated since 3.0 {@link #getCacheStrategy()} replaces this method.      */
annotation|@
name|Deprecated
name|String
name|getCachePolicy
parameter_list|()
function_decl|;
comment|/**      * Returns a caching strategy for this query.      *       * @since 3.0      */
name|QueryCacheStrategy
name|getCacheStrategy
parameter_list|()
function_decl|;
comment|/**      * Returns a String that uniquely identifies this query for the purposes of result      * caching. If null is returned, no caching is performed.      */
name|String
name|getCacheKey
parameter_list|()
function_decl|;
comment|/**      * Returns an optional array of cache "groups". Cache groups allow to invalidate query      * caches in bulk on different events. Usually the first group in the array is      * considered to be the "main" group that is used for declarative cache invalidation      * with some cache providers.      *       * @since 3.0      */
name|String
index|[]
name|getCacheGroups
parameter_list|()
function_decl|;
comment|/**      * Returns<code>true</code> if this query should produce a list of data rows as      * opposed to DataObjects,<code>false</code> for DataObjects. This is a hint to      * QueryEngine executing this query.      */
name|boolean
name|isFetchingDataRows
parameter_list|()
function_decl|;
comment|/**      * Returns<code>true</code> if the query results should replace any currently cached      * values, returns<code>false</code> otherwise. If {@link #isFetchingDataRows()}      * returns<code>true</code>, this setting is not applicable and has no effect.      */
name|boolean
name|isRefreshingObjects
parameter_list|()
function_decl|;
comment|/**      * Returns true if objects fetched via this query should be fully resolved according      * to the inheritance hierarchy.      *       * @deprecated since 3.0. Inheritance resolving is not optional anymore.      */
annotation|@
name|Deprecated
name|boolean
name|isResolvingInherited
parameter_list|()
function_decl|;
comment|/**      * Returns query page size. Page size is a hint to Cayenne that query should be      * performed page by page, instead of retrieving all results at once. If the value      * returned is less than or equal to zero, no paging should occur.      */
name|int
name|getPageSize
parameter_list|()
function_decl|;
comment|/**      * Specifies a start of a range when fetching a subset of records.      *       * @since 3.0      */
name|int
name|getFetchOffset
parameter_list|()
function_decl|;
comment|/**      * @deprecated since 3.0, renamed to {@link #getFetchOffset()}.      */
annotation|@
name|Deprecated
name|int
name|getFetchStartIndex
parameter_list|()
function_decl|;
comment|/**      * Returns the limit on the maximum number of records that can be returned by this      * query. If the actual number of rows in the result exceeds the fetch limit, they      * will be discarded. One possible use of fetch limit is using it as a safeguard      * against large result sets that may lead to the application running out of memory,      * etc. If a fetch limit is greater or equal to zero, all rows will be returned.      *       * @return the limit on the maximum number of records that can be returned by this      *         query      */
name|int
name|getFetchLimit
parameter_list|()
function_decl|;
comment|/**      * Returns a query that originated this query. Originating query is a query whose      * result is needed to obtain the result of the query owning this metadata. Most often      * than not the returned value is null. One example of non-null originating query is a      * query for a range of objects in a previously fetched paginated list. The query that      * fetched the original paginated list is an "originated" query. It may be used to      * restore a list that got lost due to a cache overflow, etc.      *       * @since 3.0      */
name|Query
name|getOrginatingQuery
parameter_list|()
function_decl|;
comment|/**      * Returns a root node of prefetch tree used by this query, or null of no prefetches      * are configured.      */
name|PrefetchTreeNode
name|getPrefetchTree
parameter_list|()
function_decl|;
comment|/**      * Returns a map of aliases vs. expression subpaths that is used to build split joins.      *       * @since 3.0      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getPathSplitAliases
parameter_list|()
function_decl|;
comment|/**      * Returns an optional list of result set mapping hints. Elements in the list can be      * either {@link EntityResultSegment} or {@link ScalarResultSegment}. The returned      * list can be null.      *       * @since 3.0      */
name|List
argument_list|<
name|Object
argument_list|>
name|getResultSetMapping
parameter_list|()
function_decl|;
comment|/**      * @return statement's fetch size      * @since 3.0      */
specifier|public
name|int
name|getStatementFetchSize
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

