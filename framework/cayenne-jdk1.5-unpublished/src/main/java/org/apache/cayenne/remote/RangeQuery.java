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
name|remote
package|;
end_package

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
name|query
operator|.
name|SQLResultSetMapping
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
comment|/**  * A Query that fetches a range of objects from a previously fetched server-side paginated  * list. This query is client-only and can't be executed on the server.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|RangeQuery
implements|implements
name|Query
block|{
specifier|private
name|String
name|cacheKey
decl_stmt|;
specifier|private
name|int
name|fetchOffset
decl_stmt|;
specifier|private
name|int
name|fetchLimit
decl_stmt|;
specifier|private
name|Query
name|originatingQuery
decl_stmt|;
comment|// exists for hessian serialization.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|RangeQuery
parameter_list|()
block|{
block|}
comment|/**      * Creates a query that returns a single page from an existing cached server-side      * result list.      */
name|RangeQuery
parameter_list|(
name|String
name|cacheKey
parameter_list|,
name|int
name|fetchStartIndex
parameter_list|,
name|int
name|fetchLimit
parameter_list|,
name|Query
name|originatingQuery
parameter_list|)
block|{
name|this
operator|.
name|cacheKey
operator|=
name|cacheKey
expr_stmt|;
name|this
operator|.
name|fetchOffset
operator|=
name|fetchStartIndex
expr_stmt|;
name|this
operator|.
name|fetchLimit
operator|=
name|fetchLimit
expr_stmt|;
name|this
operator|.
name|originatingQuery
operator|=
name|originatingQuery
expr_stmt|;
block|}
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
specifier|final
name|QueryMetadata
name|originatingMetadata
init|=
name|originatingQuery
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
return|return
operator|new
name|QueryMetadata
argument_list|()
block|{
specifier|public
name|Query
name|getOrginatingQuery
parameter_list|()
block|{
return|return
name|originatingQuery
return|;
block|}
specifier|public
name|SQLResultSetMapping
name|getResultSetMapping
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
name|cacheKey
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
name|int
name|getFetchOffset
parameter_list|()
block|{
return|return
name|fetchOffset
return|;
block|}
comment|/**              * @deprecated since 3.0              */
specifier|public
name|int
name|getFetchStartIndex
parameter_list|()
block|{
return|return
name|getFetchOffset
argument_list|()
return|;
block|}
specifier|public
name|int
name|getFetchLimit
parameter_list|()
block|{
return|return
name|fetchLimit
return|;
block|}
specifier|public
name|boolean
name|isFetchingDataRows
parameter_list|()
block|{
return|return
name|originatingMetadata
operator|.
name|isFetchingDataRows
argument_list|()
return|;
block|}
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
comment|/**              * @deprecated since 3.0 in favor of 'getCacheStrategy'.              */
specifier|public
name|String
name|getCachePolicy
parameter_list|()
block|{
return|return
name|QueryMetadata
operator|.
name|NO_CACHE
return|;
block|}
comment|/**              * @since 3.0              */
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
name|PrefetchTreeNode
name|getPrefetchTree
parameter_list|()
block|{
return|return
name|originatingMetadata
operator|.
name|getPrefetchTree
argument_list|()
return|;
block|}
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Procedure
name|getProcedure
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|boolean
name|isRefreshingObjects
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|boolean
name|isResolvingInherited
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
return|;
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
argument_list|()
throw|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

