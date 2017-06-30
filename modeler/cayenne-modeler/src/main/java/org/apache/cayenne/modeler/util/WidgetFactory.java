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
name|java
operator|.
name|util
operator|.
name|Collection
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
name|javax
operator|.
name|swing
operator|.
name|JTextField
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|table
operator|.
name|TableCellEditor
import|;
end_import

begin_comment
comment|/**  * Utility class to create standard Swing widgets following default look-and-feel of  * CayenneModeler.  */
end_comment

begin_interface
specifier|public
interface|interface
name|WidgetFactory
block|{
comment|/**      * Creates a new JComboBox with a collection of model objects.      */
name|JComboBox
argument_list|<
name|String
argument_list|>
name|createComboBox
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|model
parameter_list|,
name|boolean
name|sort
parameter_list|)
function_decl|;
comment|/**      * Creates a new JComboBox with an array of model objects.      */
parameter_list|<
name|E
parameter_list|>
name|JComboBox
argument_list|<
name|E
argument_list|>
name|createComboBox
parameter_list|(
name|E
index|[]
name|model
parameter_list|,
name|boolean
name|sort
parameter_list|)
function_decl|;
comment|/**      * Creates a new JComboBox.      */
parameter_list|<
name|E
parameter_list|>
name|JComboBox
argument_list|<
name|E
argument_list|>
name|createComboBox
parameter_list|()
function_decl|;
comment|/**      * Creates undoable JComboBox.      */
parameter_list|<
name|E
parameter_list|>
name|JComboBox
argument_list|<
name|E
argument_list|>
name|createUndoableComboBox
parameter_list|()
function_decl|;
comment|/**      * Creates cell editor for text field      */
name|DefaultCellEditor
name|createCellEditor
parameter_list|(
name|JTextField
name|textField
parameter_list|)
function_decl|;
comment|/**      * Creates cell editor for a table with combo as editor component. Type of this editor      * depends on auto-completion behavior of JComboBox      *       * @param combo JComboBox to be used as editor component      */
name|TableCellEditor
name|createCellEditor
parameter_list|(
name|JComboBox
argument_list|<
name|?
argument_list|>
name|combo
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

