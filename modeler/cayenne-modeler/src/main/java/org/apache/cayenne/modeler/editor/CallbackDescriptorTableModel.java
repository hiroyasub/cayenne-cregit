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
name|List
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
name|CallbackDescriptor
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
name|CallbackMethodEvent
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
comment|/**  * Table model for displaying methods list for a particular CallbackDescriptor  */
end_comment

begin_class
specifier|public
class|class
name|CallbackDescriptorTableModel
extends|extends
name|CayenneTableModel
argument_list|<
name|String
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|int
name|COLUMN_COUNT
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|METHOD_NAME
init|=
literal|0
decl_stmt|;
specifier|protected
name|ObjEntity
name|entity
decl_stmt|;
specifier|protected
name|CallbackDescriptor
name|callbackDescriptor
decl_stmt|;
specifier|protected
name|CallbackType
name|callbackType
decl_stmt|;
comment|/**      * constructor      *       * @param mediator mediator instance      * @param eventSource event source      * @param objectList default objects list      * @param callbackDescriptor callback descriptor instance      */
specifier|public
name|CallbackDescriptorTableModel
parameter_list|(
name|ProjectController
name|mediator
parameter_list|,
name|Object
name|eventSource
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|objectList
parameter_list|,
name|CallbackDescriptor
name|callbackDescriptor
parameter_list|,
name|CallbackType
name|callbackType
parameter_list|)
block|{
name|super
argument_list|(
name|mediator
argument_list|,
name|eventSource
argument_list|,
name|objectList
argument_list|)
expr_stmt|;
name|this
operator|.
name|callbackDescriptor
operator|=
name|callbackDescriptor
expr_stmt|;
name|this
operator|.
name|callbackType
operator|=
name|callbackType
expr_stmt|;
block|}
comment|/**      * does nothing      *       * @param newVal newVal      * @param row row      * @param col col      */
specifier|public
name|void
name|setUpdatedValueAt
parameter_list|(
name|Object
name|newVal
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|col
parameter_list|)
block|{
comment|// do nothing
block|}
comment|/**      * Returns Java class of the internal list elements.      */
specifier|public
name|Class
name|getElementsClass
parameter_list|()
block|{
return|return
name|String
operator|.
name|class
return|;
block|}
comment|/**      * @param rowIndex method index      * @return callback method for the specified index      */
specifier|public
name|String
name|getCallbackMethod
parameter_list|(
name|int
name|rowIndex
parameter_list|)
block|{
return|return
name|objectList
operator|.
name|get
argument_list|(
name|rowIndex
argument_list|)
return|;
block|}
comment|/**      * Returns the number of columns in the model. A<code>JTable</code> uses this method      * to determine how many columns it should create and display by default.      *       * @return the number of columns in the model      * @see #getRowCount      */
specifier|public
name|int
name|getColumnCount
parameter_list|()
block|{
return|return
name|COLUMN_COUNT
return|;
block|}
comment|/**      * Returns the value for the cell at<code>columnIndex</code> and      *<code>rowIndex</code>.      *       * @param rowIndex the row whose value is to be queried      * @param columnIndex the column whose value is to be queried      * @return the value Object at the specified cell      */
specifier|public
name|Object
name|getValueAt
parameter_list|(
name|int
name|rowIndex
parameter_list|,
name|int
name|columnIndex
parameter_list|)
block|{
return|return
name|getCallbackMethod
argument_list|(
name|rowIndex
argument_list|)
return|;
block|}
comment|/**      * @param column column index      * @return column name      */
specifier|public
name|String
name|getColumnName
parameter_list|(
name|int
name|column
parameter_list|)
block|{
return|return
name|callbackType
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * all cells are editable      *       * @param rowIndex row index      * @param columnIndex column index      * @return true      */
specifier|public
name|boolean
name|isCellEditable
parameter_list|(
name|int
name|rowIndex
parameter_list|,
name|int
name|columnIndex
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
comment|/**      * stores edited value      *       * @param newVal new value      * @param row row      * @param col column      */
specifier|public
name|void
name|setValueAt
parameter_list|(
name|Object
name|newVal
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|col
parameter_list|)
block|{
name|String
name|method
init|=
operator|(
name|String
operator|)
name|newVal
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|method
operator|=
name|method
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
name|String
name|prevMethod
init|=
name|getObjectList
argument_list|()
operator|.
name|get
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
operator|&&
name|method
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// check that method changed and name is not duplicate
if|if
condition|(
operator|!
name|method
operator|.
name|equals
argument_list|(
name|prevMethod
argument_list|)
operator|&&
operator|!
name|getCallbackDescriptor
argument_list|()
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|contains
argument_list|(
name|method
argument_list|)
condition|)
block|{
comment|// update model
name|getObjectList
argument_list|()
operator|.
name|set
argument_list|(
name|row
argument_list|,
name|method
argument_list|)
expr_stmt|;
comment|// update entity
name|getCallbackDescriptor
argument_list|()
operator|.
name|setCallbackMethodAt
argument_list|(
name|row
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|fireTableRowsUpdated
argument_list|(
name|row
argument_list|,
name|row
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireCallbackMethodEvent
argument_list|(
operator|new
name|CallbackMethodEvent
argument_list|(
name|eventSource
argument_list|,
name|prevMethod
argument_list|,
name|method
argument_list|,
name|MapEvent
operator|.
name|CHANGE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @return CallbackDescriptor of the model      */
specifier|public
name|CallbackDescriptor
name|getCallbackDescriptor
parameter_list|()
block|{
return|return
name|callbackDescriptor
return|;
block|}
comment|/**      * @return CallbackType of the model      */
specifier|public
name|CallbackType
name|getCallbackType
parameter_list|()
block|{
return|return
name|callbackType
return|;
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
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|sortByColumn
parameter_list|(
name|int
name|sortCol
parameter_list|,
name|boolean
name|isAscent
parameter_list|)
block|{
block|}
block|}
end_class

end_unit

