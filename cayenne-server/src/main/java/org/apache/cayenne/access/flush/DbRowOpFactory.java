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
name|Collection
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|ObjectDiff
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
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_comment
comment|/**  * Factory that produces a collection of {@link DbRowOp} from given {@link ObjectDiff}.  *  * @since 4.2  */
end_comment

begin_class
class|class
name|DbRowOpFactory
block|{
specifier|private
specifier|final
name|EntityResolver
name|resolver
decl_stmt|;
specifier|private
specifier|final
name|ObjectStore
name|store
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|ArcTarget
argument_list|>
name|processedArcs
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|DbRowOp
argument_list|>
name|dbRows
decl_stmt|;
specifier|private
specifier|final
name|RootRowOpProcessor
name|rootRowOpProcessor
decl_stmt|;
specifier|private
name|ClassDescriptor
name|descriptor
decl_stmt|;
specifier|private
name|Persistent
name|object
decl_stmt|;
specifier|private
name|ObjectDiff
name|diff
decl_stmt|;
name|DbRowOpFactory
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|,
name|ObjectStore
name|store
parameter_list|,
name|Set
argument_list|<
name|ArcTarget
argument_list|>
name|processedArcs
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|store
operator|=
name|store
expr_stmt|;
name|this
operator|.
name|dbRows
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|this
operator|.
name|processedArcs
operator|=
name|processedArcs
expr_stmt|;
name|this
operator|.
name|rootRowOpProcessor
operator|=
operator|new
name|RootRowOpProcessor
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|updateDiff
parameter_list|(
name|ObjectDiff
name|diff
parameter_list|)
block|{
name|ObjectId
name|id
init|=
operator|(
name|ObjectId
operator|)
name|diff
operator|.
name|getNodeId
argument_list|()
decl_stmt|;
name|this
operator|.
name|diff
operator|=
name|diff
expr_stmt|;
name|this
operator|.
name|descriptor
operator|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
name|id
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|object
operator|=
operator|(
name|Persistent
operator|)
name|store
operator|.
name|getNode
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|this
operator|.
name|dbRows
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|Collection
argument_list|<
name|?
extends|extends
name|DbRowOp
argument_list|>
name|createRows
parameter_list|(
name|ObjectDiff
name|diff
parameter_list|)
block|{
name|updateDiff
argument_list|(
name|diff
argument_list|)
expr_stmt|;
name|DbEntity
name|rootEntity
init|=
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|DbRowOp
name|row
init|=
name|getOrCreate
argument_list|(
name|rootEntity
argument_list|,
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|DbRowOpType
operator|.
name|forObject
argument_list|(
name|object
argument_list|)
argument_list|)
decl_stmt|;
name|rootRowOpProcessor
operator|.
name|setDiff
argument_list|(
name|diff
argument_list|)
expr_stmt|;
name|row
operator|.
name|accept
argument_list|(
name|rootRowOpProcessor
argument_list|)
expr_stmt|;
return|return
name|dbRows
operator|.
name|values
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
parameter_list|<
name|E
extends|extends
name|DbRowOp
parameter_list|>
name|E
name|get
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
return|return
name|Objects
operator|.
name|requireNonNull
argument_list|(
operator|(
name|E
operator|)
name|dbRows
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
parameter_list|<
name|E
extends|extends
name|DbRowOp
parameter_list|>
name|E
name|getOrCreate
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|ObjectId
name|id
parameter_list|,
name|DbRowOpType
name|type
parameter_list|)
block|{
return|return
operator|(
name|E
operator|)
name|dbRows
operator|.
name|computeIfAbsent
argument_list|(
name|id
argument_list|,
name|nextId
lambda|->
name|createRow
argument_list|(
name|entity
argument_list|,
name|id
argument_list|,
name|type
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|DbRowOp
name|createRow
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|ObjectId
name|id
parameter_list|,
name|DbRowOpType
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|INSERT
case|:
return|return
operator|new
name|InsertDbRowOp
argument_list|(
name|object
argument_list|,
name|entity
argument_list|,
name|id
argument_list|)
return|;
case|case
name|UPDATE
case|:
return|return
operator|new
name|UpdateDbRowOp
argument_list|(
name|object
argument_list|,
name|entity
argument_list|,
name|id
argument_list|)
return|;
case|case
name|DELETE
case|:
return|return
operator|new
name|DeleteDbRowOp
argument_list|(
name|object
argument_list|,
name|entity
argument_list|,
name|id
argument_list|)
return|;
block|}
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unknown DbRowType '%s'"
argument_list|,
name|type
argument_list|)
throw|;
block|}
name|ClassDescriptor
name|getDescriptor
parameter_list|()
block|{
return|return
name|descriptor
return|;
block|}
name|Persistent
name|getObject
parameter_list|()
block|{
return|return
name|object
return|;
block|}
name|ObjectStore
name|getStore
parameter_list|()
block|{
return|return
name|store
return|;
block|}
name|ObjectDiff
name|getDiff
parameter_list|()
block|{
return|return
name|diff
return|;
block|}
name|DbEntity
name|getDbEntity
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
name|String
name|entityName
init|=
name|id
operator|.
name|getEntityName
argument_list|()
decl_stmt|;
if|if
condition|(
name|entityName
operator|.
name|startsWith
argument_list|(
name|ASTDbPath
operator|.
name|DB_PREFIX
argument_list|)
condition|)
block|{
name|entityName
operator|=
name|entityName
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
expr_stmt|;
return|return
name|resolver
operator|.
name|getDbEntity
argument_list|(
name|entityName
argument_list|)
return|;
block|}
else|else
block|{
name|ObjEntity
name|objEntity
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
return|return
name|objEntity
operator|.
name|getDbEntity
argument_list|()
return|;
block|}
block|}
name|Set
argument_list|<
name|ArcTarget
argument_list|>
name|getProcessedArcs
parameter_list|()
block|{
return|return
name|processedArcs
return|;
block|}
block|}
end_class

end_unit

