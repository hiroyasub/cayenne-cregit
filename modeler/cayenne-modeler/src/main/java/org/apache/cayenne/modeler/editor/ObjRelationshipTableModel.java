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
name|project
operator|.
name|extension
operator|.
name|info
operator|.
name|ObjectInfo
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
comment|/**  * Table model to display ObjRelationships.  *   */
end_comment

begin_class
specifier|public
class|class
name|ObjRelationshipTableModel
extends|extends
name|CayenneTableModel
argument_list|<
name|ObjRelationship
argument_list|>
block|{
comment|// Columns
specifier|public
specifier|static
specifier|final
name|int
name|REL_NAME
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|REL_TARGET
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|REL_TARGET_PATH
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|REL_SEMANTICS
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|REL_DELETE_RULE
init|=
literal|4
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|REL_LOCKING
init|=
literal|5
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|REL_COMMENT
init|=
literal|6
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|COLUMN_COUNT
init|=
literal|7
decl_stmt|;
specifier|private
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
argument_list|<>
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
name|objectList
operator|.
name|sort
argument_list|(
operator|new
name|RelationshipComparator
argument_list|()
argument_list|)
expr_stmt|;
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
name|COLUMN_COUNT
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
name|REL_DELETE_RULE
case|:
return|return
literal|"Delete Rule"
return|;
case|case
name|REL_TARGET_PATH
case|:
return|return
literal|"DbRelationship Path"
return|;
case|case
name|REL_COMMENT
case|:
return|return
literal|"Comment"
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
switch|switch
condition|(
name|column
condition|)
block|{
case|case
name|REL_NAME
case|:
return|return
name|relationship
operator|.
name|getName
argument_list|()
return|;
case|case
name|REL_TARGET
case|:
return|return
name|relationship
operator|.
name|getTargetEntity
argument_list|()
return|;
case|case
name|REL_LOCKING
case|:
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
case|case
name|REL_SEMANTICS
case|:
return|return
name|getSemantics
argument_list|(
name|relationship
argument_list|)
return|;
case|case
name|REL_DELETE_RULE
case|:
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
case|case
name|REL_TARGET_PATH
case|:
return|return
name|relationship
operator|.
name|getDbRelationshipPath
argument_list|()
return|;
case|case
name|REL_COMMENT
case|:
return|return
name|getComment
argument_list|(
name|relationship
argument_list|)
return|;
default|default:
return|return
literal|null
return|;
block|}
block|}
specifier|private
specifier|static
name|String
name|getSemantics
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|StringBuilder
name|semantics
init|=
operator|new
name|StringBuilder
argument_list|(
literal|20
argument_list|)
decl_stmt|;
name|semantics
operator|.
name|append
argument_list|(
name|relationship
operator|.
name|isToMany
argument_list|()
condition|?
literal|"to many"
else|:
literal|"to one"
argument_list|)
expr_stmt|;
if|if
condition|(
name|relationship
operator|.
name|isReadOnly
argument_list|()
condition|)
block|{
name|semantics
operator|.
name|append
argument_list|(
literal|", read-only"
argument_list|)
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
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|collection
argument_list|)
expr_stmt|;
block|}
return|return
name|semantics
operator|.
name|toString
argument_list|()
return|;
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
switch|switch
condition|(
name|column
condition|)
block|{
case|case
name|REL_NAME
case|:
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
break|break;
case|case
name|REL_TARGET
case|:
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
name|setTargetEntityName
argument_list|(
name|target
argument_list|)
expr_stmt|;
comment|// Clear existing relationships, otherwise addDbRelationship() might fail
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
name|relationship
operator|.
name|getSourceEntity
argument_list|()
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
break|break;
case|case
name|REL_DELETE_RULE
case|:
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
break|break;
case|case
name|REL_LOCKING
case|:
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
name|Boolean
operator|)
name|value
argument_list|)
expr_stmt|;
name|fireTableCellUpdated
argument_list|(
name|row
argument_list|,
name|column
argument_list|)
expr_stmt|;
break|break;
case|case
name|REL_TARGET_PATH
case|:
name|relationship
operator|.
name|setDbRelationshipPath
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
name|fireTableCellUpdated
argument_list|(
name|row
argument_list|,
name|column
argument_list|)
expr_stmt|;
break|break;
case|case
name|REL_COMMENT
case|:
name|setComment
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|,
name|relationship
argument_list|)
expr_stmt|;
name|fireTableRowsUpdated
argument_list|(
name|row
argument_list|,
name|row
argument_list|)
expr_stmt|;
break|break;
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
block|{
return|return;
block|}
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
operator|&&
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|!=
name|entity
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
operator|&&
name|col
operator|!=
name|REL_TARGET
return|;
block|}
specifier|final
class|class
name|RelationshipComparator
implements|implements
name|Comparator
argument_list|<
name|ObjRelationship
argument_list|>
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|ObjRelationship
name|o1
parameter_list|,
name|ObjRelationship
name|o2
parameter_list|)
block|{
name|int
name|delta
init|=
name|getWeight
argument_list|(
name|o1
argument_list|)
operator|-
name|getWeight
argument_list|(
name|o2
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
name|o1
operator|.
name|getName
argument_list|()
argument_list|,
name|o2
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
annotation|@
name|Override
specifier|public
name|boolean
name|isColumnSortable
parameter_list|(
name|int
name|sortCol
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|sortByColumn
parameter_list|(
specifier|final
name|int
name|sortCol
parameter_list|,
name|boolean
name|isAscent
parameter_list|)
block|{
switch|switch
condition|(
name|sortCol
condition|)
block|{
case|case
name|REL_NAME
case|:
name|sortByElementProperty
argument_list|(
literal|"name"
argument_list|,
name|isAscent
argument_list|)
expr_stmt|;
break|break;
case|case
name|REL_TARGET
case|:
name|sortByElementProperty
argument_list|(
literal|"targetEntityName"
argument_list|,
name|isAscent
argument_list|)
expr_stmt|;
break|break;
case|case
name|REL_LOCKING
case|:
name|sortByElementProperty
argument_list|(
literal|"usedForLocking"
argument_list|,
name|isAscent
argument_list|)
expr_stmt|;
break|break;
case|case
name|REL_SEMANTICS
case|:
case|case
name|REL_DELETE_RULE
case|:
case|case
name|REL_TARGET_PATH
case|:
name|objectList
operator|.
name|sort
argument_list|(
operator|new
name|ObjRelationshipTableComparator
argument_list|(
name|sortCol
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isAscent
condition|)
block|{
name|Collections
operator|.
name|reverse
argument_list|(
name|objectList
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
break|break;
block|}
block|}
specifier|private
specifier|static
class|class
name|ObjRelationshipTableComparator
implements|implements
name|Comparator
argument_list|<
name|ObjRelationship
argument_list|>
block|{
specifier|private
name|int
name|sortCol
decl_stmt|;
name|ObjRelationshipTableComparator
parameter_list|(
name|int
name|sortCol
parameter_list|)
block|{
name|this
operator|.
name|sortCol
operator|=
name|sortCol
expr_stmt|;
block|}
specifier|public
name|int
name|compare
parameter_list|(
name|ObjRelationship
name|o1
parameter_list|,
name|ObjRelationship
name|o2
parameter_list|)
block|{
if|if
condition|(
name|o1
operator|==
name|o2
condition|)
block|{
return|return
literal|0
return|;
block|}
if|else if
condition|(
name|o1
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|else if
condition|(
name|o2
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
switch|switch
condition|(
name|sortCol
condition|)
block|{
case|case
name|REL_SEMANTICS
case|:
return|return
name|compareColumnsData
argument_list|(
name|getSemantics
argument_list|(
name|o1
argument_list|)
argument_list|,
name|getSemantics
argument_list|(
name|o2
argument_list|)
argument_list|)
return|;
case|case
name|REL_DELETE_RULE
case|:
return|return
name|compareColumnsData
argument_list|(
name|DeleteRule
operator|.
name|deleteRuleName
argument_list|(
name|o1
operator|.
name|getDeleteRule
argument_list|()
argument_list|)
argument_list|,
name|DeleteRule
operator|.
name|deleteRuleName
argument_list|(
name|o2
operator|.
name|getDeleteRule
argument_list|()
argument_list|)
argument_list|)
return|;
case|case
name|REL_TARGET_PATH
case|:
return|return
name|compareColumnsData
argument_list|(
name|o1
operator|.
name|getDbRelationshipPath
argument_list|()
argument_list|,
name|o2
operator|.
name|getDbRelationshipPath
argument_list|()
argument_list|)
return|;
default|default:
return|return
name|compareColumnsData
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|)
return|;
block|}
block|}
block|}
specifier|private
specifier|static
name|int
name|compareColumnsData
parameter_list|(
name|String
name|value1
parameter_list|,
name|String
name|value2
parameter_list|)
block|{
if|if
condition|(
name|value1
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|else if
condition|(
name|value2
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
else|else
block|{
return|return
name|value1
operator|.
name|compareTo
argument_list|(
name|value2
argument_list|)
return|;
block|}
block|}
specifier|private
name|String
name|getComment
parameter_list|(
name|ObjRelationship
name|rel
parameter_list|)
block|{
return|return
name|ObjectInfo
operator|.
name|getFromMetaData
argument_list|(
name|mediator
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
argument_list|,
name|rel
argument_list|,
name|ObjectInfo
operator|.
name|COMMENT
argument_list|)
return|;
block|}
specifier|private
name|void
name|setComment
parameter_list|(
name|String
name|newVal
parameter_list|,
name|ObjRelationship
name|rel
parameter_list|)
block|{
name|ObjectInfo
operator|.
name|putToMetaData
argument_list|(
name|mediator
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
argument_list|,
name|rel
argument_list|,
name|ObjectInfo
operator|.
name|COMMENT
argument_list|,
name|newVal
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

