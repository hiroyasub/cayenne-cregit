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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|access
operator|.
name|ObjectStore
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
name|CompoundDiff
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
name|NodeIdChangeOperation
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
name|reflect
operator|.
name|AttributeProperty
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
class|class
name|ReplacementIdVisitor
implements|implements
name|DbRowOpVisitor
argument_list|<
name|Void
argument_list|>
block|{
specifier|private
specifier|final
name|ObjectStore
name|store
decl_stmt|;
specifier|private
specifier|final
name|EntityResolver
name|resolver
decl_stmt|;
specifier|private
specifier|final
name|CompoundDiff
name|result
decl_stmt|;
name|ReplacementIdVisitor
parameter_list|(
name|ObjectStore
name|store
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|CompoundDiff
name|result
parameter_list|)
block|{
name|this
operator|.
name|store
operator|=
name|store
expr_stmt|;
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|result
operator|=
name|result
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
name|updateId
argument_list|(
name|dbRow
argument_list|)
expr_stmt|;
name|dbRow
operator|.
name|getValues
argument_list|()
operator|.
name|getFlattenedIds
argument_list|()
operator|.
name|forEach
argument_list|(
parameter_list|(
name|path
parameter_list|,
name|id
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|id
operator|.
name|isTemporary
argument_list|()
operator|&&
name|id
operator|.
name|isReplacementIdAttached
argument_list|()
condition|)
block|{
comment|// resolve lazy suppliers
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|next
range|:
name|id
operator|.
name|getReplacementIdMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|next
operator|.
name|getValue
argument_list|()
operator|instanceof
name|Supplier
condition|)
block|{
name|next
operator|.
name|setValue
argument_list|(
operator|(
operator|(
name|Supplier
operator|)
name|next
operator|.
name|getValue
argument_list|()
operator|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|store
operator|.
name|markFlattenedPath
argument_list|(
name|dbRow
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|path
argument_list|,
name|id
operator|.
name|createReplacementId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"PK for flattened path '%s' of object %s is not set during insert."
argument_list|,
name|path
argument_list|,
name|dbRow
operator|.
name|getObject
argument_list|()
argument_list|)
throw|;
block|}
block|}
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
name|updateId
argument_list|(
name|dbRow
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|updateId
parameter_list|(
name|DbRowOp
name|dbRow
parameter_list|)
block|{
name|ObjectId
name|id
init|=
name|dbRow
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|id
operator|.
name|isReplacementIdAttached
argument_list|()
condition|)
block|{
if|if
condition|(
name|id
operator|==
name|dbRow
operator|.
name|getObject
argument_list|()
operator|.
name|getObjectId
argument_list|()
operator|&&
name|id
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"PK for the object %s is not set during insert."
argument_list|,
name|dbRow
operator|.
name|getObject
argument_list|()
argument_list|)
throw|;
block|}
return|return;
block|}
name|Persistent
name|object
init|=
name|dbRow
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|replacement
init|=
name|id
operator|.
name|getReplacementIdMap
argument_list|()
decl_stmt|;
name|ObjectId
name|replacementId
init|=
name|id
operator|.
name|createReplacementId
argument_list|()
decl_stmt|;
if|if
condition|(
name|object
operator|.
name|getObjectId
argument_list|()
operator|==
name|id
operator|&&
operator|!
name|replacementId
operator|.
name|getEntityName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|ASTDbPath
operator|.
name|DB_PREFIX
argument_list|)
condition|)
block|{
name|object
operator|.
name|setObjectId
argument_list|(
name|replacementId
argument_list|)
expr_stmt|;
comment|// update meaningful PKs
for|for
control|(
name|AttributeProperty
name|property
range|:
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
name|replacementId
operator|.
name|getEntityName
argument_list|()
argument_list|)
operator|.
name|getIdProperties
argument_list|()
control|)
block|{
if|if
condition|(
name|property
operator|.
name|getAttribute
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|value
init|=
name|replacement
operator|.
name|get
argument_list|(
name|property
operator|.
name|getAttribute
argument_list|()
operator|.
name|getDbAttributeName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|property
operator|.
name|writePropertyDirectly
argument_list|(
name|object
argument_list|,
literal|null
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|result
operator|.
name|add
argument_list|(
operator|new
name|NodeIdChangeOperation
argument_list|(
name|id
argument_list|,
name|replacementId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

