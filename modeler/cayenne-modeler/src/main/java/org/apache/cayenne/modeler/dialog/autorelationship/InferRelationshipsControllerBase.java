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
name|modeler
operator|.
name|dialog
operator|.
name|autorelationship
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
name|dbsync
operator|.
name|naming
operator|.
name|ObjectNameGenerator
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
name|modeler
operator|.
name|util
operator|.
name|CayenneController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|*
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
name|Set
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
name|Predicate
import|;
end_import

begin_class
specifier|public
class|class
name|InferRelationshipsControllerBase
extends|extends
name|CayenneController
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SELECTED_PROPERTY
init|=
literal|"selected"
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|InferredRelationship
argument_list|>
name|inferredRelationships
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|DbEntity
argument_list|>
name|entities
decl_stmt|;
specifier|protected
name|Set
argument_list|<
name|InferredRelationship
argument_list|>
name|selectedEntities
decl_stmt|;
specifier|protected
name|int
name|index
init|=
literal|0
decl_stmt|;
specifier|protected
name|ObjectNameGenerator
name|strategy
decl_stmt|;
specifier|protected
specifier|transient
name|InferredRelationship
name|currentEntity
decl_stmt|;
specifier|public
name|InferRelationshipsControllerBase
parameter_list|(
name|CayenneController
name|parent
parameter_list|,
name|DataMap
name|dataMap
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
name|this
operator|.
name|entities
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|dataMap
operator|.
name|getDbEntities
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|selectedEntities
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setRelationships
parameter_list|()
block|{
name|inferredRelationships
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|DbEntity
name|entity
range|:
name|entities
control|)
block|{
name|createRelationships
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
name|createJoins
argument_list|()
expr_stmt|;
name|createNames
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|createRelationships
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|entity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|attribute
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|length
argument_list|()
operator|<
literal|4
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
operator|!
name|name
operator|.
name|substring
argument_list|(
name|name
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|,
name|name
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"_ID"
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|String
name|baseName
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|)
decl_stmt|;
for|for
control|(
name|DbEntity
name|targetEntity
range|:
name|entities
control|)
block|{
comment|// TODO: should we handle relationships to self??
if|if
condition|(
name|targetEntity
operator|==
name|entity
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|baseName
operator|.
name|equalsIgnoreCase
argument_list|(
name|targetEntity
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
operator|!
name|attribute
operator|.
name|isPrimaryKey
argument_list|()
operator|&&
operator|!
name|targetEntity
operator|.
name|getAttributes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|attribute
operator|.
name|isForeignKey
argument_list|()
condition|)
block|{
name|InferredRelationship
name|myir
init|=
operator|new
name|InferredRelationship
argument_list|()
decl_stmt|;
name|myir
operator|.
name|setSource
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|myir
operator|.
name|setTarget
argument_list|(
name|targetEntity
argument_list|)
expr_stmt|;
name|inferredRelationships
operator|.
name|add
argument_list|(
name|myir
argument_list|)
expr_stmt|;
block|}
name|createReversRelationship
argument_list|(
name|targetEntity
argument_list|,
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|createReversRelationship
parameter_list|(
name|DbEntity
name|eSourse
parameter_list|,
name|DbEntity
name|eTarget
parameter_list|)
block|{
name|InferredRelationship
name|myir
init|=
operator|new
name|InferredRelationship
argument_list|()
decl_stmt|;
for|for
control|(
name|DbRelationship
name|relationship
range|:
name|eSourse
operator|.
name|getRelationships
argument_list|()
control|)
block|{
for|for
control|(
name|DbJoin
name|join
range|:
name|relationship
operator|.
name|getJoins
argument_list|()
control|)
block|{
if|if
condition|(
operator|(
operator|(
name|DbEntity
operator|)
name|join
operator|.
name|getSource
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|)
operator|.
name|equals
argument_list|(
name|eSourse
argument_list|)
operator|&&
operator|(
operator|(
name|DbEntity
operator|)
name|join
operator|.
name|getTarget
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|)
operator|.
name|equals
argument_list|(
name|eTarget
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
block|}
name|myir
operator|.
name|setSource
argument_list|(
name|eSourse
argument_list|)
expr_stmt|;
name|myir
operator|.
name|setTarget
argument_list|(
name|eTarget
argument_list|)
expr_stmt|;
name|inferredRelationships
operator|.
name|add
argument_list|(
name|myir
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getJoin
parameter_list|(
name|InferredRelationship
name|irItem
parameter_list|)
block|{
return|return
name|irItem
operator|.
name|getJoinSource
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" : "
operator|+
name|irItem
operator|.
name|getJoinTarget
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|String
name|getToMany
parameter_list|(
name|InferredRelationship
name|irItem
parameter_list|)
block|{
if|if
condition|(
name|irItem
operator|.
name|isToMany
argument_list|()
condition|)
block|{
return|return
literal|"to many"
return|;
block|}
else|else
block|{
return|return
literal|"to one"
return|;
block|}
block|}
specifier|protected
name|DbAttribute
name|getJoinAttribute
parameter_list|(
name|DbEntity
name|sEntity
parameter_list|,
name|DbEntity
name|tEntity
parameter_list|)
block|{
if|if
condition|(
name|sEntity
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|sEntity
operator|.
name|getAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
else|else
block|{
for|for
control|(
name|DbAttribute
name|attr
range|:
name|sEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|attr
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|tEntity
operator|.
name|getName
argument_list|()
operator|+
literal|"_ID"
argument_list|)
condition|)
block|{
return|return
name|attr
return|;
block|}
block|}
for|for
control|(
name|DbAttribute
name|attr
range|:
name|sEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
operator|(
name|attr
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|sEntity
operator|.
name|getName
argument_list|()
operator|+
literal|"_ID"
argument_list|)
operator|)
operator|&&
operator|(
operator|!
name|attr
operator|.
name|isPrimaryKey
argument_list|()
operator|)
condition|)
block|{
return|return
name|attr
return|;
block|}
block|}
for|for
control|(
name|DbAttribute
name|attr
range|:
name|sEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|attr
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
return|return
name|attr
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|createJoins
parameter_list|()
block|{
name|Iterator
argument_list|<
name|InferredRelationship
argument_list|>
name|it
init|=
name|inferredRelationships
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|InferredRelationship
name|inferred
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|DbAttribute
name|src
init|=
name|getJoinAttribute
argument_list|(
name|inferred
operator|.
name|getSource
argument_list|()
argument_list|,
name|inferred
operator|.
name|getTarget
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|src
operator|==
literal|null
condition|)
block|{
comment|// TODO: andrus 03/28/2010 this is pretty inefficient I guess... We should
comment|// check for this condition earlier. See CAY-1405 for the map that caused
comment|// this issue
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
continue|continue;
block|}
name|DbAttribute
name|target
init|=
name|getJoinAttribute
argument_list|(
name|inferred
operator|.
name|getTarget
argument_list|()
argument_list|,
name|inferred
operator|.
name|getSource
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
comment|// TODO: andrus 03/28/2010 this is pretty inefficient I guess... We should
comment|// check for this condition earlier. See CAY-1405 for the map that caused
comment|// this issue
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
continue|continue;
block|}
name|inferred
operator|.
name|setJoinSource
argument_list|(
name|src
argument_list|)
expr_stmt|;
if|if
condition|(
name|src
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
name|inferred
operator|.
name|setToMany
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|inferred
operator|.
name|setJoinTarget
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|createNames
parameter_list|()
block|{
for|for
control|(
name|InferredRelationship
name|myir
range|:
name|inferredRelationships
control|)
block|{
name|DbRelationship
name|localRelationship
init|=
operator|new
name|DbRelationship
argument_list|()
decl_stmt|;
name|localRelationship
operator|.
name|setToMany
argument_list|(
name|myir
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|myir
operator|.
name|getJoinSource
argument_list|()
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
name|localRelationship
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|localRelationship
argument_list|,
name|myir
operator|.
name|getJoinSource
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|myir
operator|.
name|getJoinTarget
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|localRelationship
operator|.
name|setSourceEntity
argument_list|(
name|myir
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
name|localRelationship
operator|.
name|setTargetEntityName
argument_list|(
name|myir
operator|.
name|getTarget
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|localRelationship
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|localRelationship
argument_list|,
name|myir
operator|.
name|getJoinTarget
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|myir
operator|.
name|getJoinSource
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|localRelationship
operator|.
name|setSourceEntity
argument_list|(
name|myir
operator|.
name|getTarget
argument_list|()
argument_list|)
expr_stmt|;
name|localRelationship
operator|.
name|setTargetEntityName
argument_list|(
name|myir
operator|.
name|getSource
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|myir
operator|.
name|setName
argument_list|(
name|strategy
operator|.
name|relationshipName
argument_list|(
name|localRelationship
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|InferredRelationship
argument_list|>
name|getSelectedEntities
parameter_list|()
block|{
name|List
argument_list|<
name|InferredRelationship
argument_list|>
name|selected
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|selectedEntities
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|InferredRelationship
name|entity
range|:
name|inferredRelationships
control|)
block|{
if|if
condition|(
name|selectedEntities
operator|.
name|contains
argument_list|(
name|entity
argument_list|)
condition|)
block|{
name|selected
operator|.
name|add
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|selected
return|;
block|}
specifier|public
name|boolean
name|updateSelection
parameter_list|(
name|Predicate
argument_list|<
name|InferredRelationship
argument_list|>
name|predicate
parameter_list|)
block|{
name|boolean
name|modified
init|=
literal|false
decl_stmt|;
for|for
control|(
name|InferredRelationship
name|entity
range|:
name|inferredRelationships
control|)
block|{
name|boolean
name|select
init|=
name|predicate
operator|.
name|test
argument_list|(
name|entity
argument_list|)
decl_stmt|;
if|if
condition|(
name|select
condition|)
block|{
if|if
condition|(
name|selectedEntities
operator|.
name|add
argument_list|(
name|entity
argument_list|)
condition|)
block|{
name|modified
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|selectedEntities
operator|.
name|remove
argument_list|(
name|entity
argument_list|)
condition|)
block|{
name|modified
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|modified
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|modified
return|;
block|}
specifier|public
name|boolean
name|isSelected
parameter_list|()
block|{
return|return
name|currentEntity
operator|!=
literal|null
operator|&&
name|selectedEntities
operator|.
name|contains
argument_list|(
name|currentEntity
argument_list|)
return|;
block|}
specifier|public
name|void
name|setSelected
parameter_list|(
name|boolean
name|selectedFlag
parameter_list|)
block|{
if|if
condition|(
name|currentEntity
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|selectedFlag
condition|)
block|{
if|if
condition|(
name|selectedEntities
operator|.
name|add
argument_list|(
name|currentEntity
argument_list|)
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|selectedEntities
operator|.
name|remove
argument_list|(
name|currentEntity
argument_list|)
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|int
name|getSelectedEntitiesSize
parameter_list|()
block|{
return|return
name|selectedEntities
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|List
name|getEntities
parameter_list|()
block|{
return|return
name|inferredRelationships
return|;
block|}
specifier|public
name|InferredRelationship
name|getCurrentEntity
parameter_list|()
block|{
return|return
name|currentEntity
return|;
block|}
specifier|public
name|void
name|setCurrentEntity
parameter_list|(
name|InferredRelationship
name|currentEntity
parameter_list|)
block|{
name|this
operator|.
name|currentEntity
operator|=
name|currentEntity
expr_stmt|;
block|}
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
annotation|@
name|Override
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setNamingStrategy
parameter_list|(
name|ObjectNameGenerator
name|namestr
parameter_list|)
block|{
name|strategy
operator|=
name|namestr
expr_stmt|;
block|}
block|}
end_class

end_unit

