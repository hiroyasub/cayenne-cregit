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
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|access
operator|.
name|DataDomainSyncBucket
operator|.
name|PropagatedValueFactory
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
name|DbJoin
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

begin_comment
comment|/**  * Processes object diffs, generating DB diffs. Can be used for both UPDATE and INSERT.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|DataDomainDBDiffBuilder
implements|implements
name|GraphChangeHandler
block|{
specifier|private
name|ObjEntity
name|objEntity
decl_stmt|;
specifier|private
name|DbEntity
name|dbEntity
decl_stmt|;
comment|// diff snapshot expressed in terms of object properties.
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|currentPropertyDiff
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|currentArcDiff
decl_stmt|;
specifier|private
name|Object
name|currentId
decl_stmt|;
comment|/**      * Resets the builder to process a new combination of objEntity/dbEntity.      */
name|void
name|reset
parameter_list|(
name|DbEntityClassDescriptor
name|descriptor
parameter_list|)
block|{
name|this
operator|.
name|objEntity
operator|=
name|descriptor
operator|.
name|getEntity
argument_list|()
expr_stmt|;
name|this
operator|.
name|dbEntity
operator|=
name|descriptor
operator|.
name|getDbEntity
argument_list|()
expr_stmt|;
block|}
comment|/**      * Resets the builder to process a new object for the previously set combination of      * objEntity/dbEntity.      */
specifier|private
name|void
name|reset
parameter_list|()
block|{
name|currentPropertyDiff
operator|=
literal|null
expr_stmt|;
name|currentArcDiff
operator|=
literal|null
expr_stmt|;
name|currentId
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Processes GraphDiffs of a single object, converting them to DB diff.      */
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|buildDBDiff
parameter_list|(
name|GraphDiff
name|singleObjectDiff
parameter_list|)
block|{
name|reset
argument_list|()
expr_stmt|;
name|singleObjectDiff
operator|.
name|apply
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|currentPropertyDiff
operator|==
literal|null
operator|&&
name|currentArcDiff
operator|==
literal|null
operator|&&
name|currentId
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dbDiff
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|appendSimpleProperties
argument_list|(
name|dbDiff
argument_list|)
expr_stmt|;
name|appendForeignKeys
argument_list|(
name|dbDiff
argument_list|)
expr_stmt|;
name|appendPrimaryKeys
argument_list|(
name|dbDiff
argument_list|)
expr_stmt|;
return|return
name|dbDiff
return|;
block|}
specifier|private
name|void
name|appendSimpleProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dbDiff
parameter_list|)
block|{
comment|// populate changed columns
if|if
condition|(
name|currentPropertyDiff
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|currentPropertyDiff
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ObjAttribute
name|attribute
init|=
operator|(
name|ObjAttribute
operator|)
name|objEntity
operator|.
name|getAttribute
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
comment|// in case of a flattened attribute, ensure that it belongs to this
comment|// bucket...
name|DbAttribute
name|dbAttribute
init|=
name|attribute
operator|.
name|getDbAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbAttribute
operator|.
name|getEntity
argument_list|()
operator|==
name|dbEntity
condition|)
block|{
name|dbDiff
operator|.
name|put
argument_list|(
name|dbAttribute
operator|.
name|getName
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|appendForeignKeys
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dbDiff
parameter_list|)
block|{
comment|// populate changed FKs
if|if
condition|(
name|currentArcDiff
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|currentArcDiff
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ObjRelationship
name|relation
init|=
operator|(
name|ObjRelationship
operator|)
name|objEntity
operator|.
name|getRelationship
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|DbRelationship
name|dbRelation
init|=
name|relation
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ObjectId
name|targetId
init|=
operator|(
name|ObjectId
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|dbRelation
operator|.
name|getJoins
argument_list|()
control|)
block|{
name|Object
name|value
init|=
operator|(
name|targetId
operator|!=
literal|null
operator|)
condition|?
operator|new
name|PropagatedValueFactory
argument_list|(
name|targetId
argument_list|,
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
else|:
literal|null
decl_stmt|;
name|dbDiff
operator|.
name|put
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|appendPrimaryKeys
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dbDiff
parameter_list|)
block|{
comment|// populate changed PKs; note that we might end up overriding some values taken
comment|// from the object (e.g. zero PK's).
if|if
condition|(
name|currentId
operator|!=
literal|null
condition|)
block|{
name|dbDiff
operator|.
name|putAll
argument_list|(
operator|(
operator|(
name|ObjectId
operator|)
name|currentId
operator|)
operator|.
name|getIdSnapshot
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ==================================================
comment|// GraphChangeHandler methods.
comment|// ==================================================
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
comment|// note - no checking for phantom mod... assuming there is no phantom diffs
if|if
condition|(
name|currentPropertyDiff
operator|==
literal|null
condition|)
block|{
name|currentPropertyDiff
operator|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|currentPropertyDiff
operator|.
name|put
argument_list|(
name|property
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
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
name|ObjRelationship
name|relationship
init|=
operator|(
name|ObjRelationship
operator|)
name|objEntity
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
operator|!
name|relationship
operator|.
name|isSourceIndependentFromTargetChange
argument_list|()
condition|)
block|{
if|if
condition|(
name|currentArcDiff
operator|==
literal|null
condition|)
block|{
name|currentArcDiff
operator|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|currentArcDiff
operator|.
name|put
argument_list|(
name|arcId
argument_list|,
name|targetNodeId
argument_list|)
expr_stmt|;
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
name|ObjRelationship
name|relationship
init|=
operator|(
name|ObjRelationship
operator|)
name|objEntity
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
operator|!
name|relationship
operator|.
name|isSourceIndependentFromTargetChange
argument_list|()
condition|)
block|{
if|if
condition|(
name|currentArcDiff
operator|==
literal|null
condition|)
block|{
name|currentArcDiff
operator|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|currentArcDiff
operator|.
name|put
argument_list|(
name|arcId
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// skip deletion record if a substitute arc was created prior to deleting
comment|// the old arc...
name|Object
name|existingTargetId
init|=
name|currentArcDiff
operator|.
name|get
argument_list|(
name|arcId
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingTargetId
operator|==
literal|null
operator|||
name|targetNodeId
operator|.
name|equals
argument_list|(
name|existingTargetId
argument_list|)
condition|)
block|{
name|currentArcDiff
operator|.
name|put
argument_list|(
name|arcId
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
comment|// need to append PK columns
name|this
operator|.
name|currentId
operator|=
name|nodeId
expr_stmt|;
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
block|}
end_class

end_unit

