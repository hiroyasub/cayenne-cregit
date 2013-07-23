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
name|access
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
name|graph
operator|.
name|GraphChangeHandler
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
name|GraphDiff
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
name|ObjRelationship
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_comment
comment|/**  * A processor of ObjectStore indirect changes, such as flattened relationships and  * to-many relationships.  *   * @since 1.2  */
end_comment

begin_class
specifier|final
class|class
name|DataDomainIndirectDiffBuilder
implements|implements
name|GraphChangeHandler
block|{
specifier|private
specifier|final
name|DataDomainFlushAction
name|parent
decl_stmt|;
specifier|private
specifier|final
name|EntityResolver
name|resolver
decl_stmt|;
specifier|private
specifier|final
name|Collection
name|indirectModifications
decl_stmt|;
specifier|private
specifier|final
name|Collection
argument_list|<
name|FlattenedArcKey
argument_list|>
name|flattenedInserts
decl_stmt|;
specifier|private
specifier|final
name|Collection
argument_list|<
name|FlattenedArcKey
argument_list|>
name|flattenedDeletes
decl_stmt|;
name|DataDomainIndirectDiffBuilder
parameter_list|(
name|DataDomainFlushAction
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|this
operator|.
name|indirectModifications
operator|=
name|parent
operator|.
name|getResultIndirectlyModifiedIds
argument_list|()
expr_stmt|;
name|this
operator|.
name|resolver
operator|=
name|parent
operator|.
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
expr_stmt|;
name|this
operator|.
name|flattenedInserts
operator|=
operator|new
name|HashSet
argument_list|<
name|FlattenedArcKey
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|flattenedDeletes
operator|=
operator|new
name|HashSet
argument_list|<
name|FlattenedArcKey
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|void
name|processIndirectChanges
parameter_list|(
name|GraphDiff
name|allChanges
parameter_list|)
block|{
comment|// extract flattened and indirect changes and remove duplicate changes...
name|allChanges
operator|.
name|apply
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|flattenedInserts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
specifier|final
name|FlattenedArcKey
name|key
range|:
name|flattenedInserts
control|)
block|{
name|DbEntity
name|entity
init|=
name|key
operator|.
name|getJoinEntity
argument_list|()
decl_stmt|;
name|parent
operator|.
name|addFlattenedInsert
argument_list|(
name|entity
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|flattenedDeletes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
specifier|final
name|FlattenedArcKey
name|key
range|:
name|flattenedDeletes
control|)
block|{
name|DbEntity
name|entity
init|=
name|key
operator|.
name|getJoinEntity
argument_list|()
decl_stmt|;
name|parent
operator|.
name|addFlattenedDelete
argument_list|(
name|entity
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|arcCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|Object
name|arcId
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
operator|(
operator|(
name|ObjectId
operator|)
name|nodeId
operator|)
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|ObjRelationship
name|relationship
init|=
operator|(
name|ObjRelationship
operator|)
name|entity
operator|.
name|getRelationship
argument_list|(
name|arcId
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|relationship
operator|.
name|isSourceIndependentFromTargetChange
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
operator|(
operator|(
name|ObjectId
operator|)
name|nodeId
operator|)
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
name|indirectModifications
operator|.
name|add
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|relationship
operator|.
name|isFlattened
argument_list|()
condition|)
block|{
if|if
condition|(
name|relationship
operator|.
name|isReadOnly
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Cannot set the read-only flattened relationship '"
operator|+
name|relationship
operator|.
name|getName
argument_list|()
operator|+
literal|"' in ObjEntity '"
operator|+
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"'."
argument_list|)
throw|;
block|}
comment|// Register this combination (so we can remove it later if an insert
comment|// occurs before commit)
name|FlattenedArcKey
name|key
init|=
operator|new
name|FlattenedArcKey
argument_list|(
operator|(
name|ObjectId
operator|)
name|nodeId
argument_list|,
operator|(
name|ObjectId
operator|)
name|targetNodeId
argument_list|,
name|relationship
argument_list|)
decl_stmt|;
comment|// If this combination has already been deleted, simply undelete it.
if|if
condition|(
operator|!
name|flattenedDeletes
operator|.
name|remove
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|flattenedInserts
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|arcDeleted
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|Object
name|arcId
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
operator|(
operator|(
name|ObjectId
operator|)
name|nodeId
operator|)
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|ObjRelationship
name|relationship
init|=
operator|(
name|ObjRelationship
operator|)
name|entity
operator|.
name|getRelationship
argument_list|(
name|arcId
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|relationship
operator|.
name|isSourceIndependentFromTargetChange
argument_list|()
condition|)
block|{
comment|// do not record temporary id mods...
if|if
condition|(
operator|!
operator|(
operator|(
name|ObjectId
operator|)
name|nodeId
operator|)
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
name|indirectModifications
operator|.
name|add
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|relationship
operator|.
name|isFlattened
argument_list|()
condition|)
block|{
if|if
condition|(
name|relationship
operator|.
name|isReadOnly
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Cannot unset the read-only flattened relationship "
operator|+
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
comment|// Register this combination (so we can remove it later if an insert
comment|// occurs before commit)
name|FlattenedArcKey
name|key
init|=
operator|new
name|FlattenedArcKey
argument_list|(
operator|(
name|ObjectId
operator|)
name|nodeId
argument_list|,
operator|(
name|ObjectId
operator|)
name|targetNodeId
argument_list|,
name|relationship
argument_list|)
decl_stmt|;
comment|// If this combination has already been inserted, simply "uninsert" it
comment|// also do not delete it twice
if|if
condition|(
operator|!
name|flattenedInserts
operator|.
name|remove
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|flattenedDeletes
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|nodeIdChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|newId
parameter_list|)
block|{
comment|// noop
block|}
specifier|public
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
comment|// noop
block|}
specifier|public
name|void
name|nodeRemoved
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
comment|// noop
block|}
specifier|public
name|void
name|nodePropertyChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit
