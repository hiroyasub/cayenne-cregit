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
operator|.
name|dbentity
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
name|javax
operator|.
name|swing
operator|.
name|JOptionPane
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
name|DataDomain
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
name|map
operator|.
name|Relationship
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
name|Application
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

begin_comment
comment|/**  * Table model for DbRelationship table.  *   */
end_comment

begin_class
specifier|public
class|class
name|DbRelationshipTableModel
extends|extends
name|CayenneTableModel
block|{
comment|// Columns
specifier|static
specifier|final
name|int
name|NAME
init|=
literal|0
decl_stmt|;
specifier|static
specifier|final
name|int
name|TARGET
init|=
literal|1
decl_stmt|;
specifier|static
specifier|final
name|int
name|TO_DEPENDENT_KEY
init|=
literal|2
decl_stmt|;
specifier|static
specifier|final
name|int
name|CARDINALITY
init|=
literal|3
decl_stmt|;
specifier|protected
name|DbEntity
name|entity
decl_stmt|;
specifier|public
name|DbRelationshipTableModel
parameter_list|(
name|DbEntity
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
block|}
comment|/**      * Returns DbRelationship class.      */
specifier|public
name|Class
name|getElementsClass
parameter_list|()
block|{
return|return
name|DbRelationship
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
literal|4
return|;
block|}
specifier|public
name|String
name|getColumnName
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
name|NAME
case|:
return|return
literal|"Name"
return|;
case|case
name|TARGET
case|:
return|return
literal|"Target"
return|;
case|case
name|TO_DEPENDENT_KEY
case|:
return|return
literal|"To Dep PK"
return|;
case|case
name|CARDINALITY
case|:
return|return
literal|"To Many"
return|;
default|default:
return|return
literal|null
return|;
block|}
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
name|TARGET
case|:
return|return
name|DbEntity
operator|.
name|class
return|;
case|case
name|TO_DEPENDENT_KEY
case|:
case|case
name|CARDINALITY
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
name|DbRelationship
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
name|DbRelationship
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
name|col
parameter_list|)
block|{
name|DbRelationship
name|rel
init|=
name|getRelationship
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|rel
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
switch|switch
condition|(
name|col
condition|)
block|{
case|case
name|NAME
case|:
return|return
name|rel
operator|.
name|getName
argument_list|()
return|;
case|case
name|TARGET
case|:
return|return
name|rel
operator|.
name|getTargetEntity
argument_list|()
return|;
case|case
name|TO_DEPENDENT_KEY
case|:
return|return
name|rel
operator|.
name|isToDependentPK
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
name|CARDINALITY
case|:
return|return
name|rel
operator|.
name|isToMany
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
default|default:
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setUpdatedValueAt
parameter_list|(
name|Object
name|aValue
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
name|DbRelationship
name|rel
init|=
name|getRelationship
argument_list|(
name|row
argument_list|)
decl_stmt|;
comment|// If name column
if|if
condition|(
name|column
operator|==
name|NAME
condition|)
block|{
name|RelationshipEvent
name|e
init|=
operator|new
name|RelationshipEvent
argument_list|(
name|eventSource
argument_list|,
name|rel
argument_list|,
name|entity
argument_list|,
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|rel
operator|.
name|setName
argument_list|(
operator|(
name|String
operator|)
name|aValue
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireDbRelationshipEvent
argument_list|(
name|e
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
comment|// If target column
if|else if
condition|(
name|column
operator|==
name|TARGET
condition|)
block|{
name|DbEntity
name|target
init|=
operator|(
name|DbEntity
operator|)
name|aValue
decl_stmt|;
comment|// clear joins...
name|rel
operator|.
name|removeAllJoins
argument_list|()
expr_stmt|;
name|rel
operator|.
name|setTargetEntity
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|RelationshipEvent
name|e
init|=
operator|new
name|RelationshipEvent
argument_list|(
name|eventSource
argument_list|,
name|rel
argument_list|,
name|entity
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireDbRelationshipEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|column
operator|==
name|TO_DEPENDENT_KEY
condition|)
block|{
name|boolean
name|flag
init|=
operator|(
operator|(
name|Boolean
operator|)
name|aValue
operator|)
operator|.
name|booleanValue
argument_list|()
decl_stmt|;
comment|// make sure reverse relationship "to-dep-pk" is unset.
if|if
condition|(
name|flag
condition|)
block|{
name|DbRelationship
name|reverse
init|=
name|rel
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverse
operator|!=
literal|null
operator|&&
name|reverse
operator|.
name|isToDependentPK
argument_list|()
condition|)
block|{
name|String
name|message
init|=
literal|"Unset reverse relationship's \"To Dep PK\" setting?"
decl_stmt|;
name|int
name|answer
init|=
name|JOptionPane
operator|.
name|showConfirmDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
name|JOptionPane
operator|.
name|YES_OPTION
condition|)
block|{
comment|// no action needed
return|return;
block|}
comment|// unset reverse
name|reverse
operator|.
name|setToDependentPK
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
name|rel
operator|.
name|setToDependentPK
argument_list|(
name|flag
argument_list|)
expr_stmt|;
name|RelationshipEvent
name|e
init|=
operator|new
name|RelationshipEvent
argument_list|(
name|eventSource
argument_list|,
name|rel
argument_list|,
name|entity
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireDbRelationshipEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|column
operator|==
name|CARDINALITY
condition|)
block|{
name|Boolean
name|temp
init|=
operator|(
name|Boolean
operator|)
name|aValue
decl_stmt|;
name|rel
operator|.
name|setToMany
argument_list|(
name|temp
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|RelationshipEvent
name|e
init|=
operator|new
name|RelationshipEvent
argument_list|(
name|eventSource
argument_list|,
name|rel
argument_list|,
name|entity
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireDbRelationshipEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|updateDependentObjRelationships
argument_list|(
name|rel
argument_list|)
expr_stmt|;
block|}
name|fireTableRowsUpdated
argument_list|(
name|row
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
comment|/**      * Relationship just needs to be removed from the model. It is already removed from      * the DataMap.      */
name|void
name|removeRelationship
parameter_list|(
name|Relationship
name|rel
parameter_list|)
block|{
name|objectList
operator|.
name|remove
argument_list|(
name|rel
argument_list|)
expr_stmt|;
name|fireTableDataChanged
argument_list|()
expr_stmt|;
block|}
name|void
name|updateDependentObjRelationships
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|)
block|{
name|DataDomain
name|domain
init|=
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
decl_stmt|;
if|if
condition|(
name|domain
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ObjEntity
name|entity
range|:
name|domain
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
for|for
control|(
name|ObjRelationship
name|objRelationship
range|:
name|entity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
for|for
control|(
name|DbRelationship
name|dbRelationship
range|:
name|objRelationship
operator|.
name|getDbRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|dbRelationship
operator|==
name|relationship
condition|)
block|{
name|objRelationship
operator|.
name|recalculateToManyValue
argument_list|()
expr_stmt|;
name|objRelationship
operator|.
name|recalculateReadOnlyValue
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
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
name|DbRelationship
name|rel
init|=
name|getRelationship
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|rel
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|else if
condition|(
name|col
operator|==
name|TO_DEPENDENT_KEY
condition|)
block|{
return|return
name|rel
operator|.
name|isValidForDepPk
argument_list|()
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

