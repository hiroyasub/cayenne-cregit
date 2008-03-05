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
name|access
operator|.
name|IncrementalFaultList
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
name|Ordering
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
name|query
operator|.
name|SelectQuery
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|XMLEncoder
import|;
end_import

begin_comment
comment|/**  * A SelectQuery decorator that overrides the metadata to ensure that query result is  * cached on the server, so that subranges could be retrieved at a later time. Note that a  * special decorator that is a subclass of SelectQuery is needed so that  * {@link IncrementalFaultList} on the server-side could apply SelectQuery-specific  * optimizations.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|IncrementalSelectQuery
extends|extends
name|SelectQuery
block|{
specifier|private
name|SelectQuery
name|query
decl_stmt|;
specifier|private
name|String
name|cacheKey
decl_stmt|;
name|IncrementalSelectQuery
parameter_list|(
name|SelectQuery
name|delegate
parameter_list|,
name|String
name|cacheKey
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|cacheKey
operator|=
name|cacheKey
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|void
name|addCustomDbAttribute
parameter_list|(
name|String
name|attributePath
parameter_list|)
block|{
name|query
operator|.
name|addCustomDbAttribute
argument_list|(
name|attributePath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addCustomDbAttributes
parameter_list|(
name|List
name|attrPaths
parameter_list|)
block|{
name|query
operator|.
name|addCustomDbAttributes
argument_list|(
name|attrPaths
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addOrdering
parameter_list|(
name|Ordering
name|ordering
parameter_list|)
block|{
name|query
operator|.
name|addOrdering
argument_list|(
name|ordering
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addOrdering
parameter_list|(
name|String
name|sortPathSpec
parameter_list|,
name|boolean
name|isAscending
parameter_list|,
name|boolean
name|ignoreCase
parameter_list|)
block|{
name|query
operator|.
name|addOrdering
argument_list|(
name|sortPathSpec
argument_list|,
name|isAscending
argument_list|,
name|ignoreCase
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addOrdering
parameter_list|(
name|String
name|sortPathSpec
parameter_list|,
name|boolean
name|isAscending
parameter_list|)
block|{
name|query
operator|.
name|addOrdering
argument_list|(
name|sortPathSpec
argument_list|,
name|isAscending
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addOrderings
parameter_list|(
name|List
name|orderings
parameter_list|)
block|{
name|query
operator|.
name|addOrderings
argument_list|(
name|orderings
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|PrefetchTreeNode
name|addPrefetch
parameter_list|(
name|String
name|prefetchPath
parameter_list|)
block|{
return|return
name|query
operator|.
name|addPrefetch
argument_list|(
name|prefetchPath
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|andQualifier
parameter_list|(
name|Expression
name|e
parameter_list|)
block|{
name|query
operator|.
name|andQualifier
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clearOrderings
parameter_list|()
block|{
name|query
operator|.
name|clearOrderings
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clearPrefetches
parameter_list|()
block|{
name|query
operator|.
name|clearPrefetches
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Query
name|createQuery
parameter_list|(
name|Map
name|parameters
parameter_list|)
block|{
return|return
name|query
operator|.
name|createQuery
argument_list|(
name|parameters
argument_list|)
return|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
name|query
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|query
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
index|[]
name|getCacheGroups
parameter_list|()
block|{
return|return
name|query
operator|.
name|getCacheGroups
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getCachePolicy
parameter_list|()
block|{
return|return
name|query
operator|.
name|getCachePolicy
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getCustomDbAttributes
parameter_list|()
block|{
return|return
name|query
operator|.
name|getCustomDbAttributes
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getFetchLimit
parameter_list|()
block|{
return|return
name|query
operator|.
name|getFetchLimit
argument_list|()
return|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Ordering
argument_list|>
name|getOrderings
parameter_list|()
block|{
return|return
name|query
operator|.
name|getOrderings
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|query
operator|.
name|getPageSize
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|PrefetchTreeNode
name|getPrefetchTree
parameter_list|()
block|{
return|return
name|query
operator|.
name|getPrefetchTree
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Expression
name|getQualifier
parameter_list|()
block|{
return|return
name|query
operator|.
name|getQualifier
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getRoot
parameter_list|()
block|{
return|return
name|query
operator|.
name|getRoot
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|query
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|initWithProperties
parameter_list|(
name|Map
name|properties
parameter_list|)
block|{
name|query
operator|.
name|initWithProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDistinct
parameter_list|()
block|{
return|return
name|query
operator|.
name|isDistinct
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isFetchingCustomAttributes
parameter_list|()
block|{
return|return
name|query
operator|.
name|isFetchingCustomAttributes
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isFetchingDataRows
parameter_list|()
block|{
return|return
name|query
operator|.
name|isFetchingDataRows
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isRefreshingObjects
parameter_list|()
block|{
return|return
name|query
operator|.
name|isRefreshingObjects
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isResolvingInherited
parameter_list|()
block|{
return|return
name|query
operator|.
name|isResolvingInherited
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|orQualifier
parameter_list|(
name|Expression
name|e
parameter_list|)
block|{
name|query
operator|.
name|orQualifier
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|SelectQuery
name|queryWithParameters
parameter_list|(
name|Map
name|parameters
parameter_list|,
name|boolean
name|pruneMissing
parameter_list|)
block|{
return|return
name|query
operator|.
name|queryWithParameters
argument_list|(
name|parameters
argument_list|,
name|pruneMissing
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|SelectQuery
name|queryWithParameters
parameter_list|(
name|Map
name|parameters
parameter_list|)
block|{
return|return
name|query
operator|.
name|queryWithParameters
argument_list|(
name|parameters
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeOrdering
parameter_list|(
name|Ordering
name|ordering
parameter_list|)
block|{
name|query
operator|.
name|removeOrdering
argument_list|(
name|ordering
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removePrefetch
parameter_list|(
name|String
name|prefetchPath
parameter_list|)
block|{
name|query
operator|.
name|removePrefetch
argument_list|(
name|prefetchPath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|void
name|setCacheGroups
parameter_list|(
name|String
index|[]
name|cachGroups
parameter_list|)
block|{
name|query
operator|.
name|setCacheGroups
argument_list|(
name|cachGroups
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setCachePolicy
parameter_list|(
name|String
name|policy
parameter_list|)
block|{
name|query
operator|.
name|setCachePolicy
argument_list|(
name|policy
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setDistinct
parameter_list|(
name|boolean
name|distinct
parameter_list|)
block|{
name|query
operator|.
name|setDistinct
argument_list|(
name|distinct
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setFetchingDataRows
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|query
operator|.
name|setFetchingDataRows
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setFetchLimit
parameter_list|(
name|int
name|fetchLimit
parameter_list|)
block|{
name|query
operator|.
name|setFetchLimit
argument_list|(
name|fetchLimit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|query
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setPageSize
parameter_list|(
name|int
name|pageSize
parameter_list|)
block|{
name|query
operator|.
name|setPageSize
argument_list|(
name|pageSize
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setPrefetchTree
parameter_list|(
name|PrefetchTreeNode
name|prefetchTree
parameter_list|)
block|{
name|query
operator|.
name|setPrefetchTree
argument_list|(
name|prefetchTree
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setQualifier
parameter_list|(
name|Expression
name|qualifier
parameter_list|)
block|{
name|query
operator|.
name|setQualifier
argument_list|(
name|qualifier
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setRefreshingObjects
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|query
operator|.
name|setRefreshingObjects
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setResolvingInherited
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|query
operator|.
name|setResolvingInherited
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setRoot
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|query
operator|.
name|setRoot
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|query
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

