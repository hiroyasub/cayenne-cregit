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
name|modeler
operator|.
name|util
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
name|map
operator|.
name|Attribute
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
name|Entity
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
name|Relationship
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|TreeModelListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreeModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreePath
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
name|Vector
import|;
end_import

begin_comment
comment|/**  * Swing TreeModel for Entity attributes and relationships  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|EntityTreeModel
implements|implements
name|TreeModel
block|{
specifier|protected
name|Entity
name|root
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
index|[]
argument_list|>
name|sortedChildren
decl_stmt|;
comment|/**      * Filter for checking attributes and relationships      */
specifier|protected
name|EntityTreeFilter
name|filter
decl_stmt|;
specifier|public
name|EntityTreeModel
parameter_list|(
name|Entity
name|root
parameter_list|)
block|{
name|this
operator|.
name|root
operator|=
name|root
expr_stmt|;
name|sortedChildren
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Object
name|getRoot
parameter_list|()
block|{
return|return
name|root
return|;
block|}
specifier|public
name|Object
name|getChild
parameter_list|(
name|Object
name|node
parameter_list|,
name|int
name|index
parameter_list|)
block|{
return|return
name|sortedChildren
argument_list|(
name|node
argument_list|)
index|[
name|index
index|]
return|;
block|}
specifier|public
name|int
name|getChildCount
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
operator|(
name|node
operator|instanceof
name|Attribute
operator|)
condition|?
literal|0
else|:
name|sortedChildren
argument_list|(
name|node
argument_list|)
operator|.
name|length
return|;
block|}
specifier|public
name|boolean
name|isLeaf
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
return|return
name|getChildCount
argument_list|(
name|node
argument_list|)
operator|==
literal|0
return|;
block|}
specifier|public
name|void
name|valueForPathChanged
parameter_list|(
name|TreePath
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|)
block|{
comment|// do nothing...
block|}
specifier|public
name|int
name|getIndexOfChild
parameter_list|(
name|Object
name|node
parameter_list|,
name|Object
name|child
parameter_list|)
block|{
if|if
condition|(
name|node
operator|instanceof
name|Attribute
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
comment|// wonder if linear search will be faster, considering that
comment|// this comparator uses reflection?
return|return
name|Arrays
operator|.
name|binarySearch
argument_list|(
name|sortedChildren
argument_list|(
name|node
argument_list|)
argument_list|,
name|child
argument_list|,
name|Comparators
operator|.
name|getNamedObjectComparator
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|addTreeModelListener
parameter_list|(
name|TreeModelListener
name|listener
parameter_list|)
block|{
comment|// do nothing...
block|}
specifier|public
name|void
name|removeTreeModelListener
parameter_list|(
name|TreeModelListener
name|listener
parameter_list|)
block|{
comment|// do nothing...
block|}
specifier|private
name|Object
index|[]
name|sortedChildren
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
name|Entity
name|entity
init|=
name|entityForNonLeafNode
argument_list|(
name|node
argument_list|)
decl_stmt|;
comment|// may happen in incomplete relationships
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|Object
index|[
literal|0
index|]
return|;
block|}
name|Object
name|key
init|=
name|node
decl_stmt|;
name|Object
index|[]
name|sortedForNode
init|=
name|sortedChildren
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|sortedForNode
operator|==
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|?
extends|extends
name|Attribute
argument_list|>
name|attributes
init|=
name|entity
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|?
extends|extends
name|Relationship
argument_list|>
name|relationships
init|=
name|entity
operator|.
name|getRelationships
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|nodes
init|=
operator|new
name|Vector
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// combine two collections in an array
for|for
control|(
name|Attribute
name|attr
range|:
name|attributes
control|)
block|{
if|if
condition|(
name|filter
operator|==
literal|null
operator|||
name|filter
operator|.
name|attributeMatch
argument_list|(
name|node
argument_list|,
name|attr
argument_list|)
condition|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Relationship
name|rel
range|:
name|relationships
control|)
block|{
if|if
condition|(
name|filter
operator|==
literal|null
operator|||
name|filter
operator|.
name|relationshipMatch
argument_list|(
name|node
argument_list|,
name|rel
argument_list|)
condition|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|rel
argument_list|)
expr_stmt|;
block|}
block|}
name|sortedForNode
operator|=
name|nodes
operator|.
name|toArray
argument_list|()
expr_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|sortedForNode
argument_list|,
name|Comparators
operator|.
name|getEntityChildrenComparator
argument_list|()
argument_list|)
expr_stmt|;
name|sortedChildren
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|sortedForNode
argument_list|)
expr_stmt|;
block|}
return|return
name|sortedForNode
return|;
block|}
comment|/**      * Removes children cache for specified entity.      */
specifier|public
name|void
name|invalidate
parameter_list|()
block|{
name|sortedChildren
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Removes children cache for specified entity.      */
specifier|public
name|void
name|invalidateChildren
parameter_list|(
name|Entity
name|entity
parameter_list|)
block|{
name|sortedChildren
operator|.
name|remove
argument_list|(
name|entity
argument_list|)
expr_stmt|;
for|for
control|(
name|Relationship
name|rel
range|:
name|entity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
name|sortedChildren
operator|.
name|remove
argument_list|(
name|rel
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Entity
name|entityForNonLeafNode
parameter_list|(
name|Object
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|instanceof
name|Entity
condition|)
block|{
return|return
operator|(
name|Entity
operator|)
name|node
return|;
block|}
if|else if
condition|(
name|node
operator|instanceof
name|Relationship
condition|)
block|{
return|return
operator|(
operator|(
name|Relationship
operator|)
name|node
operator|)
operator|.
name|getTargetEntity
argument_list|()
return|;
block|}
name|String
name|className
init|=
operator|(
name|node
operator|!=
literal|null
operator|)
condition|?
name|node
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|"null"
decl_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unexpected non-leaf node: "
operator|+
name|className
argument_list|)
throw|;
block|}
comment|/**      * Sets filter for attrs and rels      */
specifier|public
name|void
name|setFilter
parameter_list|(
name|EntityTreeFilter
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
comment|/**      * Returns filter for attrs and rels      */
specifier|public
name|EntityTreeFilter
name|getFilter
parameter_list|()
block|{
return|return
name|filter
return|;
block|}
block|}
end_class

end_unit

