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
name|javax
operator|.
name|swing
operator|.
name|DefaultCellEditor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JComboBox
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
name|dba
operator|.
name|TypesMapping
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
name|Embeddable
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
name|EmbeddedAttribute
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
name|AttributeEvent
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
name|EntityEvent
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
name|MapEvent
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
name|event
operator|.
name|AttributeDisplayEvent
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
name|event
operator|.
name|EntityDisplayEvent
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
name|CayenneTable
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
name|CayenneWidgetFactory
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
name|CellEditorForAttributeTable
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
name|ModelerUtil
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
import|;
end_import

begin_comment
comment|/**  * Model for the Object Entity attributes and for Obj to DB Attribute Mapping tables.  * Allows adding/removing attributes, modifying the types and the names.  *   */
end_comment

begin_class
specifier|public
class|class
name|ObjAttributeTableModel
extends|extends
name|CayenneTableModel
block|{
comment|// Columns
specifier|static
specifier|final
name|int
name|INHERITED
init|=
literal|0
decl_stmt|;
specifier|static
specifier|final
name|int
name|OBJ_ATTRIBUTE
init|=
literal|1
decl_stmt|;
specifier|static
specifier|final
name|int
name|OBJ_ATTRIBUTE_TYPE
init|=
literal|2
decl_stmt|;
specifier|static
specifier|final
name|int
name|DB_ATTRIBUTE
init|=
literal|3
decl_stmt|;
specifier|static
specifier|final
name|int
name|DB_ATTRIBUTE_TYPE
init|=
literal|4
decl_stmt|;
specifier|static
specifier|final
name|int
name|LOCKING
init|=
literal|5
decl_stmt|;
specifier|protected
name|ObjEntity
name|entity
decl_stmt|;
specifier|protected
name|DbEntity
name|dbEntity
decl_stmt|;
specifier|private
name|CellEditorForAttributeTable
name|cellEditor
decl_stmt|;
specifier|private
name|CayenneTable
name|table
decl_stmt|;
specifier|public
name|ObjAttributeTableModel
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
argument_list|<
name|Attribute
argument_list|>
argument_list|(
name|entity
operator|.
name|getAttributes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// take a copy
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
name|this
operator|.
name|dbEntity
operator|=
name|entity
operator|.
name|getDbEntity
argument_list|()
expr_stmt|;
comment|// order using local comparator
name|Collections
operator|.
name|sort
argument_list|(
name|objectList
argument_list|,
operator|new
name|AttributeComparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|orderList
parameter_list|()
block|{
comment|// NOOP
block|}
specifier|public
name|CayenneTable
name|getTable
parameter_list|()
block|{
return|return
name|table
return|;
block|}
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
name|LOCKING
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
comment|/**      * Returns ObjAttribute class.      */
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getElementsClass
parameter_list|()
block|{
return|return
name|ObjAttribute
operator|.
name|class
return|;
block|}
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
return|return
name|dbEntity
return|;
block|}
specifier|public
name|ObjAttribute
name|getAttribute
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
name|ObjAttribute
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
comment|/** Refreshes DbEntity to current db entity within ObjEntity. */
specifier|public
name|void
name|resetDbEntity
parameter_list|()
block|{
if|if
condition|(
name|dbEntity
operator|==
name|entity
operator|.
name|getDbEntity
argument_list|()
condition|)
block|{
return|return;
block|}
name|boolean
name|wasShowing
init|=
name|isShowingDb
argument_list|()
decl_stmt|;
name|dbEntity
operator|=
name|entity
operator|.
name|getDbEntity
argument_list|()
expr_stmt|;
name|boolean
name|isShowing
init|=
name|isShowingDb
argument_list|()
decl_stmt|;
if|if
condition|(
name|wasShowing
operator|!=
name|isShowing
condition|)
block|{
name|fireTableStructureChanged
argument_list|()
expr_stmt|;
block|}
name|fireTableDataChanged
argument_list|()
expr_stmt|;
block|}
specifier|private
name|boolean
name|isShowingDb
parameter_list|()
block|{
return|return
name|dbEntity
operator|!=
literal|null
return|;
block|}
specifier|public
name|int
name|getColumnCount
parameter_list|()
block|{
return|return
literal|6
return|;
block|}
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
name|INHERITED
case|:
return|return
literal|"In"
return|;
case|case
name|OBJ_ATTRIBUTE
case|:
return|return
literal|"ObjAttribute"
return|;
case|case
name|OBJ_ATTRIBUTE_TYPE
case|:
return|return
literal|"Java Type"
return|;
case|case
name|DB_ATTRIBUTE
case|:
return|return
literal|"DbAttribute"
return|;
case|case
name|DB_ATTRIBUTE_TYPE
case|:
return|return
literal|"DB Type"
return|;
case|case
name|LOCKING
case|:
return|return
literal|"Used for Locking"
return|;
default|default:
return|return
literal|""
return|;
block|}
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
name|ObjAttribute
name|attribute
init|=
name|getAttribute
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|column
operator|==
name|INHERITED
condition|)
block|{
return|return
name|attribute
operator|.
name|isInherited
argument_list|()
return|;
block|}
if|else if
condition|(
name|column
operator|==
name|OBJ_ATTRIBUTE
condition|)
block|{
return|return
name|attribute
operator|.
name|getName
argument_list|()
return|;
block|}
if|else if
condition|(
name|column
operator|==
name|OBJ_ATTRIBUTE_TYPE
condition|)
block|{
return|return
name|attribute
operator|.
name|getType
argument_list|()
return|;
block|}
if|else if
condition|(
name|column
operator|==
name|LOCKING
condition|)
block|{
return|return
name|attribute
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
else|else
block|{
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
name|column
operator|==
name|DB_ATTRIBUTE
condition|)
block|{
return|return
name|getDBAttribute
argument_list|(
name|attribute
argument_list|,
name|dbAttribute
argument_list|)
return|;
block|}
if|else if
condition|(
name|column
operator|==
name|DB_ATTRIBUTE_TYPE
condition|)
block|{
return|return
name|getDBAttributeType
argument_list|(
name|attribute
argument_list|,
name|dbAttribute
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
block|}
specifier|private
name|String
name|getDBAttribute
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|,
name|DbAttribute
name|dbAttribute
parameter_list|)
block|{
if|if
condition|(
name|dbAttribute
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|attribute
operator|.
name|isInherited
argument_list|()
operator|&&
operator|(
operator|(
name|ObjEntity
operator|)
name|attribute
operator|.
name|getEntity
argument_list|()
operator|)
operator|.
name|isAbstract
argument_list|()
condition|)
block|{
return|return
name|attribute
operator|.
name|getDbAttributePath
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
if|else if
condition|(
name|attribute
operator|.
name|getDbAttributePath
argument_list|()
operator|!=
literal|null
operator|&&
name|attribute
operator|.
name|getDbAttributePath
argument_list|()
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
return|return
name|attribute
operator|.
name|getDbAttributePath
argument_list|()
return|;
block|}
return|return
name|dbAttribute
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|private
name|String
name|getDBAttributeType
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|,
name|DbAttribute
name|dbAttribute
parameter_list|)
block|{
name|int
name|type
decl_stmt|;
if|if
condition|(
name|dbAttribute
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
operator|(
name|attribute
operator|instanceof
name|EmbeddedAttribute
operator|)
condition|)
block|{
try|try
block|{
name|type
operator|=
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|attribute
operator|.
name|getJavaClass
argument_list|()
argument_list|)
expr_stmt|;
comment|// have to catch the exception here to make sure that
comment|// exceptional situations
comment|// (class doesn't exist, for example) don't prevent the gui
comment|// from properly updating.
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|cre
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
name|type
operator|=
name|dbAttribute
operator|.
name|getType
argument_list|()
expr_stmt|;
block|}
return|return
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|public
name|CellEditorForAttributeTable
name|setCellEditor
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|nameAttr
parameter_list|,
name|CayenneTable
name|table
parameter_list|)
block|{
name|this
operator|.
name|cellEditor
operator|=
operator|new
name|CellEditorForAttributeTable
argument_list|(
name|table
argument_list|,
name|CayenneWidgetFactory
operator|.
name|createComboBox
argument_list|(
name|nameAttr
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
return|return
name|cellEditor
return|;
block|}
specifier|public
name|CellEditorForAttributeTable
name|getCellEditor
parameter_list|()
block|{
return|return
name|cellEditor
return|;
block|}
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
name|ObjAttribute
name|attribute
init|=
name|getAttribute
argument_list|(
name|row
argument_list|)
decl_stmt|;
name|AttributeEvent
name|event
init|=
operator|new
name|AttributeEvent
argument_list|(
name|eventSource
argument_list|,
name|attribute
argument_list|,
name|entity
argument_list|)
decl_stmt|;
name|String
name|path
init|=
literal|null
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|nameAttr
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|column
operator|==
name|OBJ_ATTRIBUTE
condition|)
block|{
name|event
operator|.
name|setOldName
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ProjectUtil
operator|.
name|setAttributeName
argument_list|(
name|attribute
argument_list|,
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
else|:
literal|null
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
name|OBJ_ATTRIBUTE_TYPE
condition|)
block|{
name|String
name|oldType
init|=
name|attribute
operator|.
name|getType
argument_list|()
decl_stmt|;
name|attribute
operator|.
name|setType
argument_list|(
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
name|String
name|newType
init|=
name|attribute
operator|.
name|getType
argument_list|()
decl_stmt|;
name|String
index|[]
name|registeredTypes
init|=
name|ModelerUtil
operator|.
name|getRegisteredTypeNames
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|registeredTypesList
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|registeredTypes
argument_list|)
decl_stmt|;
empty_stmt|;
if|if
condition|(
name|oldType
operator|!=
literal|null
operator|&&
name|newType
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|registeredTypesList
operator|.
name|contains
argument_list|(
name|oldType
argument_list|)
operator|==
name|registeredTypesList
operator|.
name|contains
argument_list|(
name|newType
argument_list|)
operator|)
condition|)
block|{
name|ObjAttribute
name|attributeNew
decl_stmt|;
name|ArrayList
argument_list|<
name|Embeddable
argument_list|>
name|embs
init|=
name|mediator
operator|.
name|getEmbeddableNamesInCurRentDataDomain
argument_list|()
decl_stmt|;
name|ArrayList
argument_list|<
name|String
argument_list|>
name|embNames
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Embeddable
argument_list|>
name|it
init|=
name|embs
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
name|embNames
operator|.
name|add
argument_list|(
name|it
operator|.
name|next
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|registeredTypesList
operator|.
name|contains
argument_list|(
name|newType
argument_list|)
operator|||
operator|!
name|embNames
operator|.
name|contains
argument_list|(
name|newType
argument_list|)
condition|)
block|{
name|attributeNew
operator|=
operator|new
name|ObjAttribute
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|attributeNew
operator|=
operator|new
name|EmbeddedAttribute
argument_list|()
expr_stmt|;
name|attribute
operator|.
name|setDbAttributePath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|attributeNew
operator|.
name|setDbAttributePath
argument_list|(
name|attribute
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
expr_stmt|;
name|attributeNew
operator|.
name|setName
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|attributeNew
operator|.
name|setEntity
argument_list|(
name|attribute
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|attributeNew
operator|.
name|setParent
argument_list|(
name|attribute
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
name|attributeNew
operator|.
name|setType
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|attributeNew
operator|.
name|setUsedForLocking
argument_list|(
name|attribute
operator|.
name|isUsedForLocking
argument_list|()
argument_list|)
expr_stmt|;
name|Entity
name|ent
init|=
name|attribute
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|ent
operator|.
name|removeAttribute
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ent
operator|.
name|addAttribute
argument_list|(
name|attributeNew
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireObjEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|this
argument_list|,
name|ent
argument_list|,
name|MapEvent
operator|.
name|CHANGE
argument_list|)
argument_list|)
expr_stmt|;
name|EntityDisplayEvent
name|ev
init|=
operator|new
name|EntityDisplayEvent
argument_list|(
name|this
argument_list|,
name|mediator
operator|.
name|getCurrentObjEntity
argument_list|()
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|,
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireObjEntityDisplayEvent
argument_list|(
name|ev
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireObjAttributeEvent
argument_list|(
operator|new
name|AttributeEvent
argument_list|(
name|this
argument_list|,
name|attributeNew
argument_list|,
name|ent
argument_list|,
name|MapEvent
operator|.
name|CHANGE
argument_list|)
argument_list|)
expr_stmt|;
name|AttributeDisplayEvent
name|eventAttr
init|=
operator|new
name|AttributeDisplayEvent
argument_list|(
name|this
argument_list|,
name|attributeNew
argument_list|,
name|mediator
operator|.
name|getCurrentObjEntity
argument_list|()
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|,
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireObjAttributeDisplayEvent
argument_list|(
name|eventAttr
argument_list|)
expr_stmt|;
block|}
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
name|LOCKING
condition|)
block|{
name|attribute
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
else|else
block|{
if|if
condition|(
name|column
operator|==
name|DB_ATTRIBUTE
condition|)
block|{
comment|// If db attrib exist, associate it with obj attribute
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|path
operator|=
name|value
operator|.
name|toString
argument_list|()
expr_stmt|;
name|String
index|[]
name|pathSplit
init|=
name|path
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
comment|// If flattened attribute
if|if
condition|(
name|pathSplit
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|DbEntity
name|currentEnt
init|=
name|dbEntity
decl_stmt|;
name|StringBuilder
name|pathBuf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|isTruePath
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|dbEntity
operator|!=
literal|null
condition|)
block|{
name|nameAttr
operator|=
name|ModelerUtil
operator|.
name|getDbAttributeNames
argument_list|(
name|mediator
argument_list|,
name|dbEntity
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|pathSplit
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|j
operator|==
name|pathSplit
operator|.
name|length
operator|-
literal|1
operator|&&
name|isTruePath
condition|)
block|{
name|DbAttribute
name|dbAttribute
init|=
operator|(
name|DbAttribute
operator|)
name|currentEnt
operator|.
name|getAttribute
argument_list|(
name|pathSplit
index|[
name|j
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|dbAttribute
operator|!=
literal|null
condition|)
block|{
name|pathBuf
operator|.
name|append
argument_list|(
name|dbAttribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|isTruePath
operator|=
literal|false
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|isTruePath
condition|)
block|{
name|DbRelationship
name|dbRelationship
init|=
operator|(
name|DbRelationship
operator|)
name|currentEnt
operator|.
name|getRelationship
argument_list|(
name|pathSplit
index|[
name|j
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|dbRelationship
operator|!=
literal|null
condition|)
block|{
name|currentEnt
operator|=
operator|(
name|DbEntity
operator|)
name|dbRelationship
operator|.
name|getTargetEntity
argument_list|()
expr_stmt|;
name|pathBuf
operator|.
name|append
argument_list|(
name|dbRelationship
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|pathBuf
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|isTruePath
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
name|path
operator|=
name|isTruePath
condition|?
name|pathBuf
operator|.
name|toString
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|dbEntity
operator|!=
literal|null
condition|)
block|{
name|DbAttribute
name|dbAttribute
init|=
operator|(
name|DbAttribute
operator|)
name|dbEntity
operator|.
name|getAttribute
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|path
operator|=
name|dbAttribute
operator|!=
literal|null
condition|?
name|dbAttribute
operator|.
name|getName
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
block|}
name|attribute
operator|.
name|setDbAttributePath
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
comment|// If name is erased, remove db attribute from obj attribute.
if|else if
condition|(
name|attribute
operator|.
name|getDbAttribute
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|attribute
operator|.
name|setDbAttributePath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// If path is flattened attribute, update the JComboBox
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
name|path
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
operator|.
name|length
operator|>
literal|1
operator|&&
name|dbEntity
operator|!=
literal|null
condition|)
block|{
name|setComboBoxes
argument_list|(
name|nameAttr
argument_list|,
name|column
argument_list|)
expr_stmt|;
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
name|mediator
operator|.
name|fireObjAttributeEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setComboBoxes
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|nameAttr
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|int
name|count
init|=
name|getRowCount
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
name|count
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|getAttribute
argument_list|(
name|i
argument_list|)
operator|.
name|getDbAttributePath
argument_list|()
operator|!=
literal|null
operator|&&
name|getAttribute
argument_list|(
name|i
argument_list|)
operator|.
name|getDbAttributePath
argument_list|()
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|attributeComboForRow
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|attributeComboForRow
operator|.
name|addAll
argument_list|(
name|nameAttr
argument_list|)
expr_stmt|;
name|attributeComboForRow
operator|.
name|add
argument_list|(
name|getAttribute
argument_list|(
name|i
argument_list|)
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
expr_stmt|;
name|JComboBox
name|comboBoxForRow
init|=
name|CayenneWidgetFactory
operator|.
name|createComboBox
argument_list|(
name|attributeComboForRow
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|cellEditor
operator|.
name|setEditorAt
argument_list|(
operator|new
name|Integer
argument_list|(
name|i
argument_list|)
argument_list|,
operator|new
name|DefaultCellEditor
argument_list|(
name|comboBoxForRow
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|table
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
name|column
argument_list|)
operator|.
name|setCellEditor
argument_list|(
name|cellEditor
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|getAttribute
argument_list|(
name|row
argument_list|)
operator|.
name|isInherited
argument_list|()
condition|)
block|{
return|return
name|col
operator|==
name|DB_ATTRIBUTE
return|;
block|}
return|return
name|col
operator|!=
name|DB_ATTRIBUTE_TYPE
operator|&&
name|col
operator|!=
name|INHERITED
return|;
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
specifier|final
class|class
name|AttributeComparator
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
name|Attribute
name|a1
init|=
operator|(
name|Attribute
operator|)
name|o1
decl_stmt|;
name|Attribute
name|a2
init|=
operator|(
name|Attribute
operator|)
name|o2
decl_stmt|;
name|int
name|delta
init|=
name|getWeight
argument_list|(
name|a1
argument_list|)
operator|-
name|getWeight
argument_list|(
name|a2
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
name|a1
operator|.
name|getName
argument_list|()
argument_list|,
name|a2
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
name|Attribute
name|a
parameter_list|)
block|{
return|return
name|a
operator|.
name|getEntity
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
name|INHERITED
case|:
name|sortByElementProperty
argument_list|(
literal|"inherited"
argument_list|,
name|isAscent
argument_list|)
expr_stmt|;
break|break;
case|case
name|OBJ_ATTRIBUTE
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
name|OBJ_ATTRIBUTE_TYPE
case|:
name|sortByElementProperty
argument_list|(
literal|"type"
argument_list|,
name|isAscent
argument_list|)
expr_stmt|;
break|break;
case|case
name|LOCKING
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
name|DB_ATTRIBUTE
case|:
case|case
name|DB_ATTRIBUTE_TYPE
case|:
name|Collections
operator|.
name|sort
argument_list|(
name|objectList
argument_list|,
operator|new
name|Comparator
argument_list|<
name|ObjAttribute
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|ObjAttribute
name|o1
parameter_list|,
name|ObjAttribute
name|o2
parameter_list|)
block|{
name|Integer
name|compareObjAttributesVal
init|=
name|compareObjAttributes
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
decl_stmt|;
if|if
condition|(
name|compareObjAttributesVal
operator|!=
literal|null
condition|)
block|{
return|return
name|compareObjAttributesVal
return|;
block|}
name|String
name|valToCompare1
init|=
name|getDBAttribute
argument_list|(
name|o1
argument_list|,
name|o1
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|valToCompare2
init|=
name|getDBAttribute
argument_list|(
name|o2
argument_list|,
name|o2
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|sortCol
condition|)
block|{
case|case
name|DB_ATTRIBUTE
case|:
name|valToCompare1
operator|=
name|getDBAttribute
argument_list|(
name|o1
argument_list|,
name|o1
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|valToCompare2
operator|=
name|getDBAttribute
argument_list|(
name|o2
argument_list|,
name|o2
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|DB_ATTRIBUTE_TYPE
case|:
name|valToCompare1
operator|=
name|getDBAttributeType
argument_list|(
name|o1
argument_list|,
name|o1
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|valToCompare2
operator|=
name|getDBAttributeType
argument_list|(
name|o2
argument_list|,
name|o2
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
return|return
operator|(
name|valToCompare1
operator|==
literal|null
operator|)
condition|?
operator|-
literal|1
else|:
operator|(
name|valToCompare2
operator|==
literal|null
operator|)
condition|?
literal|1
else|:
name|valToCompare1
operator|.
name|compareTo
argument_list|(
name|valToCompare2
argument_list|)
return|;
block|}
block|}
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
specifier|private
name|Integer
name|compareObjAttributes
parameter_list|(
name|ObjAttribute
name|o1
parameter_list|,
name|ObjAttribute
name|o2
parameter_list|)
block|{
if|if
condition|(
operator|(
name|o1
operator|==
literal|null
operator|&&
name|o2
operator|==
literal|null
operator|)
operator|||
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
operator|&&
name|o2
operator|!=
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
name|o1
operator|!=
literal|null
operator|&&
name|o2
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

