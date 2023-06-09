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
name|access
operator|.
name|translator
operator|.
name|select
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
name|query
operator|.
name|MockQueryMetadata
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

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
class|class
name|MockQueryMetadataBuilder
block|{
specifier|private
name|ObjEntity
name|objEntity
decl_stmt|;
specifier|private
name|DbEntity
name|dbEntity
decl_stmt|;
specifier|private
name|int
name|limit
decl_stmt|;
specifier|private
name|int
name|offset
decl_stmt|;
specifier|private
name|boolean
name|suppressDistinct
decl_stmt|;
name|MockQueryMetadataBuilder
name|withDbEntity
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|this
operator|.
name|dbEntity
operator|=
name|entity
expr_stmt|;
return|return
name|this
return|;
block|}
name|MockQueryMetadataBuilder
name|withObjEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|this
operator|.
name|objEntity
operator|=
name|entity
expr_stmt|;
return|return
name|this
return|;
block|}
name|MockQueryMetadataBuilder
name|withLimitOffset
parameter_list|(
name|int
name|limit
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
name|this
operator|.
name|offset
operator|=
name|offset
expr_stmt|;
return|return
name|this
return|;
block|}
name|MockQueryMetadataBuilder
name|withSuppressDistinct
parameter_list|()
block|{
name|this
operator|.
name|suppressDistinct
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
name|QueryMetadata
name|build
parameter_list|()
block|{
return|return
operator|new
name|MockQueryMetadata
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|()
block|{
return|return
name|objEntity
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
name|dbEntity
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
name|offset
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
name|limit
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
name|Collections
operator|.
name|emptyMap
argument_list|()
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
name|suppressDistinct
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

