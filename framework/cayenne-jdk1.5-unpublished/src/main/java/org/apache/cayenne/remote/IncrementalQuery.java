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
comment|/**  * A client wrapper for the incremental query that overrides the metadata to ensure that  * query result is cached on the server, so that subranges could be retrieved at a later  * time.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|IncrementalQuery
implements|implements
name|Query
block|{
specifier|private
name|Query
name|query
decl_stmt|;
specifier|private
name|String
name|cacheKey
decl_stmt|;
name|IncrementalQuery
parameter_list|(
name|Query
name|query
parameter_list|,
name|String
name|cacheKey
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
name|this
operator|.
name|cacheKey
operator|=
name|cacheKey
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
name|metadata
init|=
name|query
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
comment|// the way paginated queries work on the server is that they are never cached
comment|// (IncrementalFaultList interception happens before cache interception). So
comment|// overriding caching settings in the metadata will only affect
comment|// ClientServerChannel behavior
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
name|SQLResultSetMapping
name|getResultSetMapping
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getResultSetMapping
argument_list|()
return|;
block|}
specifier|public
name|String
index|[]
name|getCacheGroups
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getCacheGroups
argument_list|()
return|;
block|}
comment|/**              * @deprecated since 3.0 in favor of 'getCacheStrategy'.              */
specifier|public
name|String
name|getCachePolicy
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getCachePolicy
argument_list|()
return|;
block|}
comment|/**              * @since 3.0              */
specifier|public
name|QueryCacheStrategy
name|getCacheStrategy
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getCacheStrategy
argument_list|()
return|;
block|}
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getDataMap
argument_list|()
return|;
block|}
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getDbEntity
argument_list|()
return|;
block|}
specifier|public
name|int
name|getFetchLimit
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getFetchLimit
argument_list|()
return|;
block|}
specifier|public
name|int
name|getFetchOffset
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getFetchOffset
argument_list|()
return|;
block|}
comment|/**              * @deprecated since 3.0              */
specifier|public
name|int
name|getFetchStartIndex
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getFetchStartIndex
argument_list|()
return|;
block|}
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getObjEntity
argument_list|()
return|;
block|}
specifier|public
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getClassDescriptor
argument_list|()
return|;
block|}
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getPageSize
argument_list|()
return|;
block|}
specifier|public
name|PrefetchTreeNode
name|getPrefetchTree
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getPrefetchTree
argument_list|()
return|;
block|}
specifier|public
name|Procedure
name|getProcedure
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getProcedure
argument_list|()
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
name|metadata
operator|.
name|getPathSplitAliases
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isFetchingDataRows
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|isFetchingDataRows
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isRefreshingObjects
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|isRefreshingObjects
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isResolvingInherited
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|isResolvingInherited
argument_list|()
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
name|query
operator|.
name|route
argument_list|(
name|router
argument_list|,
name|resolver
argument_list|,
name|substitutedQuery
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SQLAction
name|createSQLAction
parameter_list|(
name|SQLActionVisitor
name|visitor
parameter_list|)
block|{
return|return
name|query
operator|.
name|createSQLAction
argument_list|(
name|visitor
argument_list|)
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|query
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

