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
name|EmbeddableAttribute
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
name|EmbeddableAttributeEvent
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

begin_class
specifier|public
class|class
name|EmbeddableAttributeTableModel
extends|extends
name|CayenneTableModel
block|{
specifier|private
name|Embeddable
name|embeddable
decl_stmt|;
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
specifier|private
name|CellEditorForAttributeTable
name|cellEditor
decl_stmt|;
specifier|public
name|EmbeddableAttributeTableModel
parameter_list|(
name|Embeddable
name|embeddable
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
name|EmbeddableAttribute
argument_list|>
argument_list|(
name|embeddable
operator|.
name|getAttributes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|embeddable
operator|=
name|embeddable
expr_stmt|;
comment|// order using local comparator
name|Collections
operator|.
name|sort
argument_list|(
name|objectList
argument_list|,
operator|new
name|EmbeddableAttributeComparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EmbeddableAttribute
name|getEmbeddableAttribute
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
name|EmbeddableAttribute
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
name|EmbeddableAttribute
operator|.
name|class
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
name|col
parameter_list|)
block|{
name|EmbeddableAttribute
name|attribute
init|=
name|getEmbeddableAttribute
argument_list|(
name|row
argument_list|)
decl_stmt|;
name|EmbeddableAttributeEvent
name|event
init|=
operator|new
name|EmbeddableAttributeEvent
argument_list|(
name|eventSource
argument_list|,
name|embeddable
argument_list|,
name|attribute
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
name|col
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
name|setEmbeddableAttributeName
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
name|col
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|col
operator|==
name|OBJ_ATTRIBUTE_TYPE
condition|)
block|{
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
name|fireTableCellUpdated
argument_list|(
name|row
argument_list|,
name|col
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|col
operator|==
name|DB_ATTRIBUTE
condition|)
block|{
name|attribute
operator|.
name|setDbAttributeName
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
name|fireTableCellUpdated
argument_list|(
name|row
argument_list|,
name|col
argument_list|)
expr_stmt|;
block|}
name|mediator
operator|.
name|fireEmbeddableAttributeEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getColumnCount
parameter_list|()
block|{
return|return
literal|4
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
name|EmbeddableAttribute
name|attribute
init|=
name|getEmbeddableAttribute
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
literal|null
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
name|DB_ATTRIBUTE
condition|)
block|{
return|return
name|attribute
operator|.
name|getDbAttributeName
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
literal|true
return|;
block|}
specifier|final
class|class
name|EmbeddableAttributeComparator
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
name|EmbeddableAttribute
name|a1
init|=
operator|(
name|EmbeddableAttribute
operator|)
name|o1
decl_stmt|;
name|EmbeddableAttribute
name|a2
init|=
operator|(
name|EmbeddableAttribute
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
name|EmbeddableAttribute
name|a
parameter_list|)
block|{
return|return
name|a
operator|.
name|getEmbeddable
argument_list|()
operator|==
name|embeddable
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

