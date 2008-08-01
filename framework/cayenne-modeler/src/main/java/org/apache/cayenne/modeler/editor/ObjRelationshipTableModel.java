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
name|editor
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
name|*
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
name|event
operator|.
name|RelationshipEvent
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
name|ProjectController
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
name|CayenneTableModel
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
name|ProjectUtil
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
name|Util
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_comment
comment|/**  * Table model to display ObjRelationships.  *   * @author Misha Shengaout  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ObjRelationshipTableModel
extends|extends
name|CayenneTableModel
block|{
comment|// Columns
specifier|static
specifier|final
name|int
name|REL_NAME
init|=
literal|0
decl_stmt|;
specifier|static
specifier|final
name|int
name|REL_TARGET
init|=
literal|1
decl_stmt|;
specifier|static
specifier|final
name|int
name|REL_SEMANTICS
init|=
literal|2
decl_stmt|;
specifier|static
specifier|final
name|int
name|REL_DELETERULE
init|=
literal|3
decl_stmt|;
specifier|static
specifier|final
name|int
name|REL_LOCKING
init|=
literal|4
decl_stmt|;
specifier|protected
name|ObjEntity
name|entity
decl_stmt|;
specifier|public
name|ObjRelationshipTableModel
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|ProjectController
name|mediator
parameter_list|,
name|Object
name|eventSource
parameter_list|)
block|{
name|super
argument_list|(
name|mediator
argument_list|,
name|eventSource
argument_list|,
operator|new
name|ArrayList
argument_list|(
name|entity
operator|.
name|getRelationships
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
comment|// order using local comparator
name|Collections
operator|.
name|sort
argument_list|(
name|objectList
argument_list|,
operator|new
name|RelationshipComparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|orderList
parameter_list|()
block|{
comment|// NOOP
block|}
specifier|public
name|ObjEntity
name|getEntity
parameter_list|()
block|{
return|return
name|entity
return|;
block|}
comment|/**      * Returns ObjRelationship class.      */
annotation|@
name|Override
specifier|public
name|Class
name|getElementsClass
parameter_list|()
block|{
return|return
name|ObjRelationship
operator|.
name|class
return|;
block|}
specifier|public
name|int
name|getColumnCount
parameter_list|()
block|{
return|return
literal|5
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getColumnName
parameter_list|(
name|int
name|column
parameter_list|)
block|{
switch|switch
condition|(
name|column
condition|)
block|{
case|case
name|REL_NAME
case|:
return|return
literal|"Name"
return|;
case|case
name|REL_TARGET
case|:
return|return
literal|"Target"
return|;
case|case
name|REL_LOCKING
case|:
return|return
literal|"Used for Locking"
return|;
case|case
name|REL_SEMANTICS
case|:
return|return
literal|"Semantics"
return|;
case|case
name|REL_DELETERULE
case|:
return|return
literal|"Delete Rule"
return|;
default|default:
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Class
name|getColumnClass
parameter_list|(
name|int
name|col
parameter_list|)
block|{
switch|switch
condition|(
name|col
condition|)
block|{
case|case
name|REL_TARGET
case|:
return|return
name|ObjEntity
operator|.
name|class
return|;
case|case
name|REL_LOCKING
case|:
return|return
name|Boolean
operator|.
name|class
return|;
default|default:
return|return
name|String
operator|.
name|class
return|;
block|}
block|}
specifier|public
name|ObjRelationship
name|getRelationship
parameter_list|(
name|int
name|row
parameter_list|)
block|{
return|return
operator|(
name|row
operator|>=
literal|0
operator|&&
name|row
operator|<
name|objectList
operator|.
name|size
argument_list|()
operator|)
condition|?
operator|(
name|ObjRelationship
operator|)
name|objectList
operator|.
name|get
argument_list|(
name|row
argument_list|)
else|:
literal|null
return|;
block|}
specifier|public
name|Object
name|getValueAt
parameter_list|(
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|ObjRelationship
name|relationship
init|=
name|getRelationship
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|column
operator|==
name|REL_NAME
condition|)
block|{
return|return
name|relationship
operator|.
name|getName
argument_list|()
return|;
block|}
if|else if
condition|(
name|column
operator|==
name|REL_TARGET
condition|)
block|{
return|return
name|relationship
operator|.
name|getTargetEntity
argument_list|()
return|;
block|}
if|else if
condition|(
name|column
operator|==
name|REL_LOCKING
condition|)
block|{
return|return
name|relationship
operator|.
name|isUsedForLocking
argument_list|()
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
return|;
block|}
if|else if
condition|(
name|column
operator|==
name|REL_SEMANTICS
condition|)
block|{
name|String
name|semantics
init|=
name|relationship
operator|.
name|isToMany
argument_list|()
condition|?
literal|"to many"
else|:
literal|"to one"
decl_stmt|;
if|if
condition|(
name|relationship
operator|.
name|isReadOnly
argument_list|()
condition|)
block|{
name|semantics
operator|+=
literal|", read-only"
expr_stmt|;
block|}
if|if
condition|(
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|String
name|collection
init|=
literal|"list"
decl_stmt|;
if|if
condition|(
name|relationship
operator|.
name|getCollectionType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|int
name|dot
init|=
name|relationship
operator|.
name|getCollectionType
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|collection
operator|=
name|relationship
operator|.
name|getCollectionType
argument_list|()
operator|.
name|substring
argument_list|(
name|dot
operator|+
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
block|}
name|semantics
operator|+=
literal|", "
operator|+
name|collection
expr_stmt|;
block|}
return|return
name|semantics
return|;
block|}
if|else if
condition|(
name|column
operator|==
name|REL_DELETERULE
condition|)
block|{
return|return
name|DeleteRule
operator|.
name|deleteRuleName
argument_list|(
name|relationship
operator|.
name|getDeleteRule
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setUpdatedValueAt
parameter_list|(
name|Object
name|value
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|ObjRelationship
name|relationship
init|=
name|getRelationship
argument_list|(
name|row
argument_list|)
decl_stmt|;
name|RelationshipEvent
name|event
init|=
operator|new
name|RelationshipEvent
argument_list|(
name|eventSource
argument_list|,
name|relationship
argument_list|,
name|entity
argument_list|)
decl_stmt|;
if|if
condition|(
name|column
operator|==
name|REL_NAME
condition|)
block|{
name|String
name|text
init|=
operator|(
name|String
operator|)
name|value
decl_stmt|;
name|event
operator|.
name|setOldName
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ProjectUtil
operator|.
name|setRelationshipName
argument_list|(
name|entity
argument_list|,
name|relationship
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|fireTableCellUpdated
argument_list|(
name|row
argument_list|,
name|column
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|column
operator|==
name|REL_TARGET
condition|)
block|{
name|ObjEntity
name|target
init|=
operator|(
name|ObjEntity
operator|)
name|value
decl_stmt|;
name|relationship
operator|.
name|setTargetEntity
argument_list|(
name|target
argument_list|)
expr_stmt|;
comment|/**              * Clear existing relationships, otherwise addDbRelationship() might fail              */
name|relationship
operator|.
name|clearDbRelationships
argument_list|()
expr_stmt|;
comment|// now try to connect DbEntities if we can do it in one step
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|DbEntity
name|srcDB
init|=
operator|(
operator|(
name|ObjEntity
operator|)
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|)
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|DbEntity
name|targetDB
init|=
name|target
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|srcDB
operator|!=
literal|null
operator|&&
name|targetDB
operator|!=
literal|null
condition|)
block|{
name|Relationship
name|anyConnector
init|=
name|srcDB
operator|.
name|getAnyRelationship
argument_list|(
name|targetDB
argument_list|)
decl_stmt|;
if|if
condition|(
name|anyConnector
operator|!=
literal|null
condition|)
block|{
name|relationship
operator|.
name|addDbRelationship
argument_list|(
operator|(
name|DbRelationship
operator|)
name|anyConnector
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|fireTableRowsUpdated
argument_list|(
name|row
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|column
operator|==
name|REL_DELETERULE
condition|)
block|{
name|relationship
operator|.
name|setDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|deleteRuleForName
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
argument_list|)
expr_stmt|;
name|fireTableCellUpdated
argument_list|(
name|row
argument_list|,
name|column
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|column
operator|==
name|REL_LOCKING
condition|)
block|{
name|relationship
operator|.
name|setUsedForLocking
argument_list|(
operator|(
name|value
operator|instanceof
name|Boolean
operator|)
operator|&&
operator|(
operator|(
name|Boolean
operator|)
name|value
operator|)
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|fireTableCellUpdated
argument_list|(
name|row
argument_list|,
name|column
argument_list|)
expr_stmt|;
block|}
name|mediator
operator|.
name|fireObjRelationshipEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeRow
parameter_list|(
name|int
name|row
parameter_list|)
block|{
if|if
condition|(
name|row
operator|<
literal|0
condition|)
return|return;
name|Relationship
name|rel
init|=
name|getRelationship
argument_list|(
name|row
argument_list|)
decl_stmt|;
name|RelationshipEvent
name|e
decl_stmt|;
name|e
operator|=
operator|new
name|RelationshipEvent
argument_list|(
name|eventSource
argument_list|,
name|rel
argument_list|,
name|entity
argument_list|,
name|RelationshipEvent
operator|.
name|REMOVE
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireObjRelationshipEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|objectList
operator|.
name|remove
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|entity
operator|.
name|removeRelationship
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|fireTableRowsDeleted
argument_list|(
name|row
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|isInherited
parameter_list|(
name|int
name|row
parameter_list|)
block|{
name|ObjRelationship
name|relationship
init|=
name|getRelationship
argument_list|(
name|row
argument_list|)
decl_stmt|;
return|return
operator|(
name|relationship
operator|!=
literal|null
operator|)
condition|?
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|!=
name|entity
else|:
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isCellEditable
parameter_list|(
name|int
name|row
parameter_list|,
name|int
name|col
parameter_list|)
block|{
return|return
operator|!
name|isInherited
argument_list|(
name|row
argument_list|)
operator|&&
name|col
operator|!=
name|REL_SEMANTICS
return|;
block|}
specifier|final
class|class
name|RelationshipComparator
implements|implements
name|Comparator
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|ObjRelationship
name|r1
init|=
operator|(
name|ObjRelationship
operator|)
name|o1
decl_stmt|;
name|ObjRelationship
name|r2
init|=
operator|(
name|ObjRelationship
operator|)
name|o2
decl_stmt|;
name|int
name|delta
init|=
name|getWeight
argument_list|(
name|r1
argument_list|)
operator|-
name|getWeight
argument_list|(
name|r2
argument_list|)
decl_stmt|;
return|return
operator|(
name|delta
operator|!=
literal|0
operator|)
condition|?
name|delta
else|:
name|Util
operator|.
name|nullSafeCompare
argument_list|(
literal|true
argument_list|,
name|r1
operator|.
name|getName
argument_list|()
argument_list|,
name|r2
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|int
name|getWeight
parameter_list|(
name|ObjRelationship
name|r
parameter_list|)
block|{
return|return
name|r
operator|.
name|getSourceEntity
argument_list|()
operator|==
name|entity
condition|?
literal|1
else|:
operator|-
literal|1
return|;
block|}
block|}
block|}
end_class

end_unit

