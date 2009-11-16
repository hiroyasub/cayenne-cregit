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

begin_class
specifier|public
class|class
name|MockQueryMetadata
implements|implements
name|QueryMetadata
block|{
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
name|DbEntity
name|getDbEntity
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
literal|null
return|;
block|}
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
name|QueryCacheStrategy
name|getCacheStrategy
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
name|ClassDescriptor
name|getClassDescriptor
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
literal|false
return|;
block|}
specifier|public
name|boolean
name|isRefreshingObjects
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|isResolvingInherited
parameter_list|()
block|{
return|return
literal|false
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
literal|0
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
name|Query
name|getOrginatingQuery
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|int
name|getStatementFetchSize
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

