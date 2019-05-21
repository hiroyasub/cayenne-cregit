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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|CayenneRuntimeException
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
name|DataRow
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
name|ObjectId
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
name|Persistent
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
name|graph
operator|.
name|GraphManager
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
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_comment
comment|/**  * A ParentAttachmentStrategy that extracts parent ObjectId from the joined columns in the  * child snapshot.  */
end_comment

begin_class
class|class
name|JoinedIdParentAttachementStrategy
implements|implements
name|ParentAttachmentStrategy
block|{
specifier|private
name|String
name|relatedIdPrefix
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|sourceEntities
decl_stmt|;
specifier|private
name|PrefetchProcessorNode
name|node
decl_stmt|;
specifier|private
name|GraphManager
name|graphManager
decl_stmt|;
name|JoinedIdParentAttachementStrategy
parameter_list|(
name|GraphManager
name|graphManager
parameter_list|,
name|PrefetchProcessorNode
name|node
parameter_list|)
block|{
name|ClassDescriptor
name|parentDescriptor
init|=
operator|(
operator|(
name|PrefetchProcessorNode
operator|)
name|node
operator|.
name|getParent
argument_list|()
operator|)
operator|.
name|getResolver
argument_list|()
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
name|relatedIdPrefix
operator|=
name|node
operator|.
name|getIncoming
argument_list|()
operator|.
name|getRelationship
argument_list|()
operator|.
name|getReverseDbRelationshipPath
argument_list|()
operator|+
literal|"."
expr_stmt|;
name|sourceEntities
operator|=
name|parentDescriptor
operator|.
name|getEntityInheritanceTree
argument_list|()
operator|.
name|allSubEntities
argument_list|()
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
name|this
operator|.
name|graphManager
operator|=
name|graphManager
expr_stmt|;
block|}
specifier|public
name|void
name|linkToParent
parameter_list|(
name|DataRow
name|row
parameter_list|,
name|Persistent
name|object
parameter_list|)
block|{
name|Persistent
name|parentObject
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ObjEntity
name|entity
range|:
name|sourceEntities
control|)
block|{
if|if
condition|(
name|entity
operator|.
name|isAbstract
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|ObjectId
name|id
init|=
name|node
operator|.
name|getResolver
argument_list|()
operator|.
name|createObjectId
argument_list|(
name|row
argument_list|,
name|entity
argument_list|,
name|relatedIdPrefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't build ObjectId from row: %s, entity: %s, prefix: %s"
argument_list|,
name|row
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|relatedIdPrefix
argument_list|)
throw|;
block|}
name|parentObject
operator|=
operator|(
name|Persistent
operator|)
name|graphManager
operator|.
name|getNode
argument_list|(
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|parentObject
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
name|node
operator|.
name|linkToParent
argument_list|(
name|object
argument_list|,
name|parentObject
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

