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
name|flush
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|flush
operator|.
name|operation
operator|.
name|DbRowOp
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
name|flush
operator|.
name|operation
operator|.
name|DbRowOpType
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
name|flush
operator|.
name|operation
operator|.
name|DbRowOpVisitor
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
name|flush
operator|.
name|operation
operator|.
name|DbRowOpWithValues
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
name|flush
operator|.
name|operation
operator|.
name|DeleteDbRowOp
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
name|flush
operator|.
name|operation
operator|.
name|InsertDbRowOp
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
name|flush
operator|.
name|operation
operator|.
name|UpdateDbRowOp
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
name|parser
operator|.
name|ASTDbPath
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
name|ArcId
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
name|util
operator|.
name|CayenneMapEntry
import|;
end_import

begin_comment
comment|/**  * Graph handler that collects information about arc changes into  * {@link org.apache.cayenne.access.flush.operation.Values} and/or {@link org.apache.cayenne.access.flush.operation.Qualifier}.  *  * @since 4.2  */
end_comment

begin_class
class|class
name|ArcValuesCreationHandler
implements|implements
name|GraphChangeHandler
block|{
specifier|final
name|DbRowOpFactory
name|factory
decl_stmt|;
specifier|final
name|DbRowOpType
name|defaultType
decl_stmt|;
name|ArcValuesCreationHandler
parameter_list|(
name|DbRowOpFactory
name|factory
parameter_list|,
name|DbRowOpType
name|defaultType
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|this
operator|.
name|defaultType
operator|=
name|defaultType
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
name|ArcId
name|arcId
parameter_list|)
block|{
name|processArcChange
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|,
literal|true
argument_list|)
expr_stmt|;
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
name|ArcId
name|arcId
parameter_list|)
block|{
name|processArcChange
argument_list|(
name|nodeId
argument_list|,
name|targetNodeId
argument_list|,
name|arcId
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processArcChange
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|ArcId
name|arcId
parameter_list|,
name|boolean
name|created
parameter_list|)
block|{
name|ObjectId
name|actualTargetId
init|=
operator|(
name|ObjectId
operator|)
name|targetNodeId
decl_stmt|;
name|ObjectId
name|snapshotId
init|=
name|factory
operator|.
name|getDiff
argument_list|()
operator|.
name|getCurrentArcSnapshotValue
argument_list|(
name|arcId
operator|.
name|getForwardArc
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|snapshotId
operator|!=
literal|null
condition|)
block|{
name|actualTargetId
operator|=
name|snapshotId
expr_stmt|;
block|}
name|ArcTarget
name|arcTarget
init|=
operator|new
name|ArcTarget
argument_list|(
operator|(
name|ObjectId
operator|)
name|nodeId
argument_list|,
name|actualTargetId
argument_list|,
name|arcId
argument_list|,
operator|!
name|created
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|.
name|getProcessedArcs
argument_list|()
operator|.
name|contains
argument_list|(
name|arcTarget
operator|.
name|getReversed
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|ObjEntity
name|entity
init|=
name|factory
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|ObjRelationship
name|objRelationship
init|=
name|entity
operator|.
name|getRelationship
argument_list|(
name|arcTarget
operator|.
name|getArcId
argument_list|()
operator|.
name|getForwardArc
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|objRelationship
operator|==
literal|null
condition|)
block|{
name|String
name|arc
init|=
name|arcId
operator|.
name|getForwardArc
argument_list|()
decl_stmt|;
if|if
condition|(
name|arc
operator|.
name|startsWith
argument_list|(
name|ASTDbPath
operator|.
name|DB_PREFIX
argument_list|)
condition|)
block|{
name|String
name|relName
init|=
name|arc
operator|.
name|substring
argument_list|(
name|ASTDbPath
operator|.
name|DB_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|DbRelationship
name|dbRelationship
init|=
name|entity
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getRelationship
argument_list|(
name|relName
argument_list|)
decl_stmt|;
name|processRelationship
argument_list|(
name|dbRelationship
argument_list|,
name|arcTarget
operator|.
name|getSourceId
argument_list|()
argument_list|,
name|arcTarget
operator|.
name|getTargetId
argument_list|()
argument_list|,
name|created
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
if|if
condition|(
name|objRelationship
operator|.
name|isFlattened
argument_list|()
condition|)
block|{
name|processFlattenedPath
argument_list|(
name|arcTarget
operator|.
name|getSourceId
argument_list|()
argument_list|,
name|arcTarget
operator|.
name|getTargetId
argument_list|()
argument_list|,
name|entity
operator|.
name|getDbEntity
argument_list|()
argument_list|,
name|objRelationship
operator|.
name|getDbRelationshipPath
argument_list|()
argument_list|,
name|created
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|DbRelationship
name|dbRelationship
init|=
name|objRelationship
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|processRelationship
argument_list|(
name|dbRelationship
argument_list|,
name|arcTarget
operator|.
name|getSourceId
argument_list|()
argument_list|,
name|arcTarget
operator|.
name|getTargetId
argument_list|()
argument_list|,
name|created
argument_list|)
expr_stmt|;
block|}
name|factory
operator|.
name|getProcessedArcs
argument_list|()
operator|.
name|add
argument_list|(
name|arcTarget
argument_list|)
expr_stmt|;
block|}
name|ObjectId
name|processFlattenedPath
parameter_list|(
name|ObjectId
name|id
parameter_list|,
name|ObjectId
name|finalTargetId
parameter_list|,
name|DbEntity
name|entity
parameter_list|,
name|String
name|dbPath
parameter_list|,
name|boolean
name|add
parameter_list|)
block|{
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|dbPathIterator
init|=
name|entity
operator|.
name|resolvePathComponents
argument_list|(
name|dbPath
argument_list|)
decl_stmt|;
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|ObjectId
name|srcId
init|=
name|id
decl_stmt|;
name|ObjectId
name|targetId
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|dbPathIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CayenneMapEntry
name|entry
init|=
name|dbPathIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|path
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
name|path
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|entry
operator|instanceof
name|DbRelationship
condition|)
block|{
name|DbRelationship
name|relationship
init|=
operator|(
name|DbRelationship
operator|)
name|entry
decl_stmt|;
comment|// intermediate db entity to be inserted
name|DbEntity
name|target
init|=
name|relationship
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
comment|// if ID is present, just use it, otherwise create new
name|String
name|flattenedPath
init|=
name|path
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// if this is last segment and it's a relationship, use known target id from arc creation
if|if
condition|(
operator|!
name|dbPathIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|targetId
operator|=
name|finalTargetId
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|targetId
operator|=
name|factory
operator|.
name|getStore
argument_list|()
operator|.
name|getFlattenedId
argument_list|(
name|id
argument_list|,
name|flattenedPath
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|targetId
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|if
condition|(
name|targetId
operator|==
literal|null
condition|)
block|{
comment|// should insert, regardless of original operation (insert/update)
name|targetId
operator|=
name|ObjectId
operator|.
name|of
argument_list|(
name|ASTDbPath
operator|.
name|DB_PREFIX
operator|+
name|target
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|factory
operator|.
name|getStore
argument_list|()
operator|.
name|markFlattenedPath
argument_list|(
name|id
argument_list|,
name|flattenedPath
argument_list|,
name|targetId
argument_list|)
expr_stmt|;
block|}
name|DbRowOpType
name|type
decl_stmt|;
if|if
condition|(
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|type
operator|=
name|add
condition|?
name|DbRowOpType
operator|.
name|INSERT
else|:
name|DbRowOpType
operator|.
name|DELETE
expr_stmt|;
name|factory
operator|.
name|getOrCreate
argument_list|(
name|target
argument_list|,
name|targetId
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
name|add
condition|?
name|DbRowOpType
operator|.
name|INSERT
else|:
name|DbRowOpType
operator|.
name|UPDATE
expr_stmt|;
name|factory
operator|.
expr|<
name|DbRowOpWithValues
operator|>
name|getOrCreate
argument_list|(
name|target
argument_list|,
name|targetId
argument_list|,
name|type
argument_list|)
operator|.
name|getValues
argument_list|()
operator|.
name|addFlattenedId
argument_list|(
name|flattenedPath
argument_list|,
name|targetId
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|dbPathIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// should update existing DB row
name|factory
operator|.
name|getOrCreate
argument_list|(
name|target
argument_list|,
name|targetId
argument_list|,
name|add
condition|?
name|DbRowOpType
operator|.
name|UPDATE
else|:
name|defaultType
argument_list|)
expr_stmt|;
block|}
name|processRelationship
argument_list|(
name|relationship
argument_list|,
name|srcId
argument_list|,
name|targetId
argument_list|,
name|add
argument_list|)
expr_stmt|;
name|srcId
operator|=
name|targetId
expr_stmt|;
comment|// use target as next source..
block|}
block|}
return|return
name|targetId
return|;
block|}
specifier|protected
name|void
name|processRelationship
parameter_list|(
name|DbRelationship
name|dbRelationship
parameter_list|,
name|ObjectId
name|srcId
parameter_list|,
name|ObjectId
name|targetId
parameter_list|,
name|boolean
name|add
parameter_list|)
block|{
for|for
control|(
name|DbJoin
name|join
range|:
name|dbRelationship
operator|.
name|getJoins
argument_list|()
control|)
block|{
name|boolean
name|srcPK
init|=
name|join
operator|.
name|getSource
argument_list|()
operator|.
name|isPrimaryKey
argument_list|()
decl_stmt|;
name|boolean
name|targetPK
init|=
name|join
operator|.
name|getTarget
argument_list|()
operator|.
name|isPrimaryKey
argument_list|()
decl_stmt|;
name|Object
name|valueToUse
decl_stmt|;
name|DbRowOp
name|rowOp
decl_stmt|;
name|DbAttribute
name|attribute
decl_stmt|;
name|ObjectId
name|id
decl_stmt|;
name|boolean
name|processDelete
decl_stmt|;
comment|// We manage 3 cases here:
comment|// 1. PK -> FK: just propagate value from PK and to FK
comment|// 2. PK -> PK: check isToDep flag and set dependent one
comment|// 3. NON-PK -> FK (not supported fully for now, see CAY-2488): also check isToDep flag,
comment|//    but get value from DbRow, not ObjID
if|if
condition|(
name|srcPK
operator|!=
name|targetPK
condition|)
block|{
comment|// case 1
name|processDelete
operator|=
literal|true
expr_stmt|;
name|id
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|srcPK
condition|)
block|{
name|valueToUse
operator|=
name|ObjectIdValueSupplier
operator|.
name|getFor
argument_list|(
name|srcId
argument_list|,
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
expr_stmt|;
name|rowOp
operator|=
name|factory
operator|.
name|getOrCreate
argument_list|(
name|dbRelationship
operator|.
name|getTargetEntity
argument_list|()
argument_list|,
name|targetId
argument_list|,
name|DbRowOpType
operator|.
name|UPDATE
argument_list|)
expr_stmt|;
name|attribute
operator|=
name|join
operator|.
name|getTarget
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|valueToUse
operator|=
name|ObjectIdValueSupplier
operator|.
name|getFor
argument_list|(
name|targetId
argument_list|,
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
expr_stmt|;
name|rowOp
operator|=
name|factory
operator|.
name|getOrCreate
argument_list|(
name|dbRelationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|srcId
argument_list|,
name|defaultType
argument_list|)
expr_stmt|;
name|attribute
operator|=
name|join
operator|.
name|getSource
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// case 2 and 3
name|processDelete
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|dbRelationship
operator|.
name|isToDependentPK
argument_list|()
condition|)
block|{
name|valueToUse
operator|=
name|ObjectIdValueSupplier
operator|.
name|getFor
argument_list|(
name|srcId
argument_list|,
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
expr_stmt|;
name|rowOp
operator|=
name|factory
operator|.
name|getOrCreate
argument_list|(
name|dbRelationship
operator|.
name|getTargetEntity
argument_list|()
argument_list|,
name|targetId
argument_list|,
name|DbRowOpType
operator|.
name|UPDATE
argument_list|)
expr_stmt|;
name|attribute
operator|=
name|join
operator|.
name|getTarget
argument_list|()
expr_stmt|;
name|id
operator|=
name|targetId
expr_stmt|;
if|if
condition|(
name|dbRelationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
comment|// strange mapping toDepPK and toMany, but just skip it
name|rowOp
operator|=
literal|null
expr_stmt|;
block|}
block|}
else|else
block|{
name|valueToUse
operator|=
name|ObjectIdValueSupplier
operator|.
name|getFor
argument_list|(
name|targetId
argument_list|,
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
expr_stmt|;
name|rowOp
operator|=
name|factory
operator|.
name|getOrCreate
argument_list|(
name|dbRelationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|srcId
argument_list|,
name|defaultType
argument_list|)
expr_stmt|;
name|attribute
operator|=
name|join
operator|.
name|getSource
argument_list|()
expr_stmt|;
name|id
operator|=
name|srcId
expr_stmt|;
if|if
condition|(
name|dbRelationship
operator|.
name|getReverseRelationship
argument_list|()
operator|.
name|isToMany
argument_list|()
condition|)
block|{
comment|// strange mapping toDepPK and toMany, but just skip it
name|rowOp
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
comment|// propagated master -> child PK
if|if
condition|(
name|id
operator|!=
literal|null
operator|&&
name|attribute
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
name|id
operator|.
name|getReplacementIdMap
argument_list|()
operator|.
name|put
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|valueToUse
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rowOp
operator|!=
literal|null
condition|)
block|{
name|rowOp
operator|.
name|accept
argument_list|(
operator|new
name|ValuePropagationVisitor
argument_list|(
name|attribute
argument_list|,
name|add
argument_list|,
name|valueToUse
argument_list|,
name|processDelete
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// not interested in following events in this handler
annotation|@
name|Override
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
block|}
annotation|@
name|Override
specifier|public
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|nodeRemoved
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
block|}
annotation|@
name|Override
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
block|}
specifier|private
specifier|static
class|class
name|ValuePropagationVisitor
implements|implements
name|DbRowOpVisitor
argument_list|<
name|Void
argument_list|>
block|{
specifier|private
specifier|final
name|DbAttribute
name|attribute
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|add
decl_stmt|;
specifier|private
specifier|final
name|Object
name|valueToUse
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|processDelete
decl_stmt|;
specifier|private
name|ValuePropagationVisitor
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|,
name|boolean
name|add
parameter_list|,
name|Object
name|valueToUse
parameter_list|,
name|boolean
name|processDelete
parameter_list|)
block|{
name|this
operator|.
name|attribute
operator|=
name|attribute
expr_stmt|;
name|this
operator|.
name|add
operator|=
name|add
expr_stmt|;
name|this
operator|.
name|valueToUse
operator|=
name|valueToUse
expr_stmt|;
name|this
operator|.
name|processDelete
operator|=
name|processDelete
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitInsert
parameter_list|(
name|InsertDbRowOp
name|dbRow
parameter_list|)
block|{
name|dbRow
operator|.
name|getValues
argument_list|()
operator|.
name|addValue
argument_list|(
name|attribute
argument_list|,
name|add
condition|?
name|valueToUse
else|:
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitUpdate
parameter_list|(
name|UpdateDbRowOp
name|dbRow
parameter_list|)
block|{
name|dbRow
operator|.
name|getValues
argument_list|()
operator|.
name|addValue
argument_list|(
name|attribute
argument_list|,
name|add
condition|?
name|valueToUse
else|:
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|visitDelete
parameter_list|(
name|DeleteDbRowOp
name|dbRow
parameter_list|)
block|{
if|if
condition|(
name|processDelete
condition|)
block|{
name|dbRow
operator|.
name|getQualifier
argument_list|()
operator|.
name|addAdditionalQualifier
argument_list|(
name|attribute
argument_list|,
name|valueToUse
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

