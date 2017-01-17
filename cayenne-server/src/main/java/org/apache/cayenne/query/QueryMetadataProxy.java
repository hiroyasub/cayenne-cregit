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
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|QueryMetadataProxy
implements|implements
name|QueryMetadata
block|{
specifier|protected
name|QueryMetadata
name|mdDelegate
decl_stmt|;
specifier|protected
name|QueryMetadataProxy
parameter_list|(
name|QueryMetadata
name|mdDelegate
parameter_list|)
block|{
name|this
operator|.
name|mdDelegate
operator|=
name|mdDelegate
expr_stmt|;
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
name|mdDelegate
operator|.
name|getCacheGroups
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getCacheKey
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|QueryCacheStrategy
name|getCacheStrategy
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getCacheStrategy
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getClassDescriptor
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getDataMap
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getDbEntity
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
name|mdDelegate
operator|.
name|getFetchLimit
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getFetchOffset
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getFetchOffset
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getObjEntity
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Query
name|getOrginatingQuery
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getOrginatingQuery
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
name|mdDelegate
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
name|mdDelegate
operator|.
name|getPrefetchTree
argument_list|()
return|;
block|}
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
name|mdDelegate
operator|.
name|getPathSplitAliases
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Procedure
name|getProcedure
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getProcedure
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getResultSetMapping
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getResultSetMapping
argument_list|()
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
name|mdDelegate
operator|.
name|isSingleResultSetMapping
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
name|mdDelegate
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
name|mdDelegate
operator|.
name|isRefreshingObjects
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getStatementFetchSize
parameter_list|()
block|{
return|return
name|mdDelegate
operator|.
name|getStatementFetchSize
argument_list|()
return|;
block|}
block|}
end_class

end_unit

