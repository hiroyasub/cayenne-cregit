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
name|jdbc
operator|.
name|ColumnDescriptor
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
name|Expression
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
name|ExpressionFactory
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
name|ASTPath
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
name|query
operator|.
name|PrefetchTreeNode
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
name|ArcProperty
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
name|PropertyVisitor
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
name|ToManyProperty
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
name|ToOneProperty
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_comment
comment|/**  * A specialized PrefetchTreeNode used for joint prefetch resolving.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|PrefetchProcessorJointNode
extends|extends
name|PrefetchProcessorNode
block|{
name|ColumnDescriptor
index|[]
name|columns
decl_stmt|;
name|int
index|[]
name|idIndices
decl_stmt|;
name|int
name|rowCapacity
decl_stmt|;
name|Map
argument_list|<
name|Map
argument_list|,
name|Persistent
argument_list|>
name|resolved
decl_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|resolvedRows
decl_stmt|;
name|PrefetchProcessorJointNode
parameter_list|(
name|PrefetchProcessorNode
name|parent
parameter_list|,
name|String
name|segmentPath
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
name|segmentPath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
name|void
name|afterInit
parameter_list|()
block|{
name|super
operator|.
name|afterInit
argument_list|()
expr_stmt|;
comment|// as node will be resolved one row at a time, init objects array here
comment|// list may shrink as a result of duplicates in flattened rows.. so don't
comment|// allocate too much space
name|int
name|capacity
init|=
name|dataRows
operator|!=
literal|null
condition|?
name|dataRows
operator|.
name|size
argument_list|()
else|:
literal|10
decl_stmt|;
if|if
condition|(
name|capacity
operator|>
literal|100
condition|)
block|{
name|capacity
operator|=
name|capacity
operator|/
literal|2
expr_stmt|;
block|}
name|objects
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|capacity
argument_list|)
expr_stmt|;
name|resolved
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|capacity
argument_list|)
expr_stmt|;
name|resolvedRows
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|capacity
argument_list|)
expr_stmt|;
name|buildRowMapping
argument_list|()
expr_stmt|;
name|buildPKIndex
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|DataRow
argument_list|>
name|getResolvedRows
parameter_list|()
block|{
return|return
name|resolvedRows
return|;
block|}
name|void
name|addObject
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|DataRow
name|row
parameter_list|)
block|{
name|objects
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|resolvedRows
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns an ObjectId map from the flat row.      */
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|idFromFlatRow
parameter_list|(
name|DataRow
name|flatRow
parameter_list|)
block|{
comment|// TODO: should we also check for nulls in ID (and skip such rows) - this will
comment|// likely be an indicator of an outer join ... and considering SQLTemplate,
comment|// this is reasonable to expect...
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|id
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|idIndex
range|:
name|idIndices
control|)
block|{
name|Object
name|value
init|=
name|flatRow
operator|.
name|get
argument_list|(
name|columns
index|[
name|idIndex
index|]
operator|.
name|getDataRowKey
argument_list|()
argument_list|)
decl_stmt|;
name|id
operator|.
name|put
argument_list|(
name|columns
index|[
name|idIndex
index|]
operator|.
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|id
return|;
block|}
comment|/**      * Looks up a previously resolved object using an ObjectId map as a key. Returns null      * if no matching object exists.      */
name|Persistent
name|getResolved
parameter_list|(
name|Map
name|id
parameter_list|)
block|{
return|return
name|resolved
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
comment|/**      * Registers an object in a map of resolved objects, connects this object to parent if      * parent exists.      */
name|void
name|putResolved
parameter_list|(
name|Map
name|id
parameter_list|,
name|Persistent
name|object
parameter_list|)
block|{
name|resolved
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a DataRow from the flat row.      */
name|DataRow
name|rowFromFlatRow
parameter_list|(
name|DataRow
name|flatRow
parameter_list|)
block|{
name|DataRow
name|row
init|=
operator|new
name|DataRow
argument_list|(
name|rowCapacity
argument_list|)
decl_stmt|;
comment|// extract subset of flat row columns, recasting to the target keys
for|for
control|(
name|ColumnDescriptor
name|column
range|:
name|columns
control|)
block|{
name|row
operator|.
name|put
argument_list|(
name|column
operator|.
name|getName
argument_list|()
argument_list|,
name|flatRow
operator|.
name|get
argument_list|(
name|column
operator|.
name|getDataRowKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// since JDBC row reader won't inject JOINED entity name, we have to detect it here...
name|ClassDescriptor
name|descriptor
init|=
name|resolver
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
name|ObjEntity
name|entity
init|=
name|descriptor
operator|.
name|getEntityInheritanceTree
argument_list|()
operator|.
name|entityMatchingRow
argument_list|(
name|row
argument_list|)
decl_stmt|;
name|row
operator|.
name|setEntityName
argument_list|(
name|entity
operator|==
literal|null
condition|?
literal|null
else|:
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|row
return|;
block|}
comment|/**      * Configures row columns mapping for this node entity.      */
specifier|private
name|void
name|buildRowMapping
parameter_list|()
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ColumnDescriptor
argument_list|>
name|targetSource
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// build a DB path .. find parent node that terminates the joint group...
name|PrefetchTreeNode
name|jointRoot
init|=
name|this
decl_stmt|;
while|while
condition|(
name|jointRoot
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|jointRoot
operator|.
name|isDisjointPrefetch
argument_list|()
operator|&&
operator|!
name|jointRoot
operator|.
name|isDisjointByIdPrefetch
argument_list|()
condition|)
block|{
name|jointRoot
operator|=
name|jointRoot
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
specifier|final
name|String
name|prefix
decl_stmt|;
if|if
condition|(
name|jointRoot
operator|!=
name|this
condition|)
block|{
name|Expression
name|objectPath
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|getPath
argument_list|(
name|jointRoot
argument_list|)
argument_list|)
decl_stmt|;
name|ASTPath
name|translated
init|=
operator|(
name|ASTPath
operator|)
operator|(
operator|(
name|PrefetchProcessorNode
operator|)
name|jointRoot
operator|)
operator|.
name|getResolver
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|.
name|translateToDbPath
argument_list|(
name|objectPath
argument_list|)
decl_stmt|;
comment|// make sure we do not include "db:" prefix
name|prefix
operator|=
name|translated
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
operator|+
literal|"."
expr_stmt|;
block|}
else|else
block|{
name|prefix
operator|=
literal|""
expr_stmt|;
block|}
comment|// find propagated keys, assuming that only one-step joins
comment|// share their column(s) with parent
if|if
condition|(
name|getParent
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|getParent
argument_list|()
operator|.
name|isPhantom
argument_list|()
operator|&&
name|getIncoming
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|getIncoming
argument_list|()
operator|.
name|getRelationship
argument_list|()
operator|.
name|isFlattened
argument_list|()
condition|)
block|{
name|DbRelationship
name|r
init|=
name|getIncoming
argument_list|()
operator|.
name|getRelationship
argument_list|()
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|DbJoin
name|join
range|:
name|r
operator|.
name|getJoins
argument_list|()
control|)
block|{
name|appendColumn
argument_list|(
name|targetSource
argument_list|,
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|,
name|prefix
operator|+
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|ClassDescriptor
name|descriptor
init|=
name|resolver
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
name|descriptor
operator|.
name|visitAllProperties
argument_list|(
operator|new
name|PropertyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
name|String
name|target
init|=
name|property
operator|.
name|getAttribute
argument_list|()
operator|.
name|getDbAttributePath
argument_list|()
decl_stmt|;
name|appendColumn
argument_list|(
name|targetSource
argument_list|,
name|target
argument_list|,
name|prefix
operator|+
name|target
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
return|return
name|visitRelationship
argument_list|(
name|property
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
return|return
name|visitRelationship
argument_list|(
name|property
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|visitRelationship
parameter_list|(
name|ArcProperty
name|arc
parameter_list|)
block|{
name|DbRelationship
name|dbRel
init|=
name|arc
operator|.
name|getRelationship
argument_list|()
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|dbRel
operator|.
name|getSourceAttributes
argument_list|()
control|)
block|{
name|String
name|target
init|=
name|attribute
operator|.
name|getName
argument_list|()
decl_stmt|;
name|appendColumn
argument_list|(
name|targetSource
argument_list|,
name|target
argument_list|,
name|prefix
operator|+
name|target
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// append id columns ... (some may have been appended already via relationships)
for|for
control|(
name|String
name|pkName
range|:
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getPrimaryKeyNames
argument_list|()
control|)
block|{
name|appendColumn
argument_list|(
name|targetSource
argument_list|,
name|pkName
argument_list|,
name|prefix
operator|+
name|pkName
argument_list|)
expr_stmt|;
block|}
comment|// append inheritance discriminator columns...
for|for
control|(
name|ObjAttribute
name|column
range|:
name|descriptor
operator|.
name|getDiscriminatorColumns
argument_list|()
control|)
block|{
name|String
name|target
init|=
name|column
operator|.
name|getDbAttributePath
argument_list|()
decl_stmt|;
name|appendColumn
argument_list|(
name|targetSource
argument_list|,
name|target
argument_list|,
name|prefix
operator|+
name|target
argument_list|)
expr_stmt|;
block|}
name|int
name|size
init|=
name|targetSource
operator|.
name|size
argument_list|()
decl_stmt|;
name|this
operator|.
name|rowCapacity
operator|=
operator|(
name|int
operator|)
name|Math
operator|.
name|ceil
argument_list|(
name|size
operator|/
literal|0.75
argument_list|)
expr_stmt|;
name|this
operator|.
name|columns
operator|=
operator|new
name|ColumnDescriptor
index|[
name|size
index|]
expr_stmt|;
name|targetSource
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|(
name|columns
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ColumnDescriptor
name|appendColumn
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|ColumnDescriptor
argument_list|>
name|map
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|label
parameter_list|)
block|{
name|ColumnDescriptor
name|column
init|=
name|map
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|column
operator|==
literal|null
condition|)
block|{
name|column
operator|=
operator|new
name|ColumnDescriptor
argument_list|()
expr_stmt|;
name|column
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|column
operator|.
name|setDataRowKey
argument_list|(
name|label
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|column
argument_list|)
expr_stmt|;
block|}
return|return
name|column
return|;
block|}
comment|/**      * Creates an internal index of PK columns in the result.      */
specifier|private
name|void
name|buildPKIndex
parameter_list|()
block|{
comment|// index PK
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|pks
init|=
name|getResolver
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
name|this
operator|.
name|idIndices
operator|=
operator|new
name|int
index|[
name|pks
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
comment|// this is needed for checking that a valid index is made
name|Arrays
operator|.
name|fill
argument_list|(
name|idIndices
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|it
init|=
name|pks
operator|.
name|iterator
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|idIndices
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|DbAttribute
name|pk
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|columns
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|pk
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|columns
index|[
name|j
index|]
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|idIndices
index|[
name|i
index|]
operator|=
name|j
expr_stmt|;
break|break;
block|}
block|}
comment|// sanity check
if|if
condition|(
name|idIndices
index|[
name|i
index|]
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"PK column is not part of result row: %s"
argument_list|,
name|pk
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

