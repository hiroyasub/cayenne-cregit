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
name|project
operator|.
name|extension
operator|.
name|info
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
name|configuration
operator|.
name|ConfigurationNode
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
name|configuration
operator|.
name|xml
operator|.
name|DataChannelMetaData
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
name|DbAttribute
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
name|DbRelationship
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
name|Embeddable
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
name|EmbeddableAttribute
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
name|ObjAttribute
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
name|ObjRelationship
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
name|map
operator|.
name|ProcedureParameter
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
name|QueryDescriptor
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
name|project
operator|.
name|extension
operator|.
name|BaseSaverDelegate
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
name|Util
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
class|class
name|InfoSaverDelegate
extends|extends
name|BaseSaverDelegate
block|{
specifier|private
name|DataChannelMetaData
name|metaData
decl_stmt|;
name|InfoSaverDelegate
parameter_list|(
name|DataChannelMetaData
name|metaData
parameter_list|)
block|{
name|this
operator|.
name|metaData
operator|=
name|metaData
expr_stmt|;
block|}
specifier|private
name|Void
name|printComment
parameter_list|(
name|ConfigurationNode
name|entity
parameter_list|)
block|{
name|ObjectInfo
name|info
init|=
name|metaData
operator|.
name|get
argument_list|(
name|entity
argument_list|,
name|ObjectInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|info
operator|.
name|getSortedValues
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|encoder
operator|.
name|start
argument_list|(
literal|"info:property"
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"xmlns:info"
argument_list|,
name|InfoExtension
operator|.
name|NAMESPACE
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"name"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"value"
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
return|return
name|printComment
argument_list|(
name|dataMap
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitObjEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
return|return
name|printComment
argument_list|(
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitDbEntity
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
name|printComment
argument_list|(
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitEmbeddable
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|)
block|{
return|return
name|printComment
argument_list|(
name|embeddable
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitEmbeddableAttribute
parameter_list|(
name|EmbeddableAttribute
name|attribute
parameter_list|)
block|{
return|return
name|printComment
argument_list|(
name|attribute
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitObjAttribute
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
return|return
name|printComment
argument_list|(
name|attribute
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitDbAttribute
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
return|return
name|printComment
argument_list|(
name|attribute
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitObjRelationship
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
return|return
name|printComment
argument_list|(
name|relationship
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitDbRelationship
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|)
block|{
return|return
name|printComment
argument_list|(
name|relationship
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitProcedure
parameter_list|(
name|Procedure
name|procedure
parameter_list|)
block|{
return|return
name|printComment
argument_list|(
name|procedure
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitProcedureParameter
parameter_list|(
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
return|return
name|printComment
argument_list|(
name|parameter
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitQuery
parameter_list|(
name|QueryDescriptor
name|query
parameter_list|)
block|{
return|return
name|printComment
argument_list|(
name|query
argument_list|)
return|;
block|}
block|}
end_class

end_unit

