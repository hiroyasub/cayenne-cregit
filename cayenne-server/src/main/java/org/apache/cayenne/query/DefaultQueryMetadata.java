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
comment|/**  * A QueryMetadata implementation that returns all the defaults.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|DefaultQueryMetadata
implements|implements
name|QueryMetadata
block|{
specifier|static
specifier|final
name|QueryMetadata
name|defaultMetadata
init|=
operator|new
name|DefaultQueryMetadata
argument_list|()
decl_stmt|;
comment|/**      * To simplify overriding this implementation checks whether there is a non-null      * entity or procedure, and uses its DataMap.      */
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
if|if
condition|(
name|getObjEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getObjEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
return|;
block|}
if|if
condition|(
name|getDbEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getDbEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
return|;
block|}
if|if
condition|(
name|getProcedure
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getProcedure
argument_list|()
operator|.
name|getDataMap
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * @since 3.0      */
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
comment|/**      * @since 4.0      */
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
comment|/**      * @since 3.0      */
specifier|public
name|Query
name|getOrginatingQuery
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * @since 3.0      */
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
index|[]
name|getCacheGroups
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
return|return
name|QueryMetadata
operator|.
name|FETCHING_DATA_ROWS_DEFAULT
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
block|}
end_class

end_unit

