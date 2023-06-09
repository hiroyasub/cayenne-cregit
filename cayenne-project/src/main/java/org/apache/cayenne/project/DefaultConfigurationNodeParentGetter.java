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
name|configuration
operator|.
name|BaseConfigurationNodeVisitor
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
name|ConfigurationNodeVisitor
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
name|DataNodeDescriptor
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

begin_class
specifier|public
class|class
name|DefaultConfigurationNodeParentGetter
implements|implements
name|ConfigurationNodeParentGetter
block|{
specifier|private
name|ConfigurationNodeVisitor
argument_list|<
name|ConfigurationNode
argument_list|>
name|parentGetter
decl_stmt|;
specifier|public
name|DefaultConfigurationNodeParentGetter
parameter_list|()
block|{
name|parentGetter
operator|=
operator|new
name|ParentGetter
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ConfigurationNode
name|getParent
parameter_list|(
name|ConfigurationNode
name|node
parameter_list|)
block|{
return|return
name|node
operator|.
name|acceptVisitor
argument_list|(
name|parentGetter
argument_list|)
return|;
block|}
class|class
name|ParentGetter
extends|extends
name|BaseConfigurationNodeVisitor
argument_list|<
name|ConfigurationNode
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|ConfigurationNode
name|visitDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
return|return
name|dataMap
operator|.
name|getDataChannelDescriptor
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNode
name|visitDataNodeDescriptor
parameter_list|(
name|DataNodeDescriptor
name|nodeDescriptor
parameter_list|)
block|{
return|return
name|nodeDescriptor
operator|.
name|getDataChannelDescriptor
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNode
name|visitDbAttribute
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
return|return
operator|(
name|ConfigurationNode
operator|)
name|attribute
operator|.
name|getParent
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNode
name|visitDbEntity
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
name|entity
operator|.
name|getDataMap
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNode
name|visitDbRelationship
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|)
block|{
return|return
operator|(
name|ConfigurationNode
operator|)
name|relationship
operator|.
name|getParent
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNode
name|visitEmbeddable
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|)
block|{
return|return
name|embeddable
operator|.
name|getDataMap
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNode
name|visitEmbeddableAttribute
parameter_list|(
name|EmbeddableAttribute
name|attribute
parameter_list|)
block|{
return|return
name|attribute
operator|.
name|getEmbeddable
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNode
name|visitObjAttribute
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
return|return
operator|(
name|ConfigurationNode
operator|)
name|attribute
operator|.
name|getParent
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNode
name|visitObjEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
return|return
operator|(
name|ConfigurationNode
operator|)
name|entity
operator|.
name|getParent
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNode
name|visitObjRelationship
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
return|return
operator|(
name|ConfigurationNode
operator|)
name|relationship
operator|.
name|getParent
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNode
name|visitProcedure
parameter_list|(
name|Procedure
name|procedure
parameter_list|)
block|{
return|return
operator|(
name|ConfigurationNode
operator|)
name|procedure
operator|.
name|getParent
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNode
name|visitProcedureParameter
parameter_list|(
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
return|return
operator|(
name|ConfigurationNode
operator|)
name|parameter
operator|.
name|getParent
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNode
name|visitQuery
parameter_list|(
name|QueryDescriptor
name|query
parameter_list|)
block|{
return|return
name|query
operator|.
name|getDataMap
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

