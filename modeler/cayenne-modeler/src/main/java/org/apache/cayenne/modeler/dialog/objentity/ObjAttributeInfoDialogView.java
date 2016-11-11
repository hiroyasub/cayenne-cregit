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
name|dialog
operator|.
name|objentity
package|;
end_package

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|builder
operator|.
name|PanelBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|CellConstraints
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|FormLayout
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|RowSpec
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
name|pref
operator|.
name|TableColumnPreferences
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
name|MultiColumnBrowser
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
name|PanelFactory
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
name|combo
operator|.
name|AutoCompletion
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JButton
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
name|JDialog
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JLabel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JScrollPane
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
name|java
operator|.
name|awt
operator|.
name|BorderLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|CardLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Dimension
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|FlowLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ComponentEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ComponentListener
import|;
end_import

begin_class
specifier|public
class|class
name|ObjAttributeInfoDialogView
extends|extends
name|JDialog
block|{
comment|/**      * // * Browser to select path for attribute //      */
specifier|protected
name|MultiColumnBrowser
name|pathBrowser
decl_stmt|;
specifier|protected
name|JButton
name|cancelButton
decl_stmt|;
specifier|protected
name|JButton
name|saveButton
decl_stmt|;
specifier|protected
name|JButton
name|selectPathButton
decl_stmt|;
specifier|protected
name|JTextField
name|attributeName
decl_stmt|;
specifier|protected
name|JLabel
name|currentPathLabel
decl_stmt|;
specifier|protected
name|JLabel
name|sourceEntityLabel
decl_stmt|;
specifier|protected
name|JComboBox
name|typeComboBox
decl_stmt|;
specifier|protected
name|JPanel
name|typeManagerPane
decl_stmt|;
specifier|protected
name|CayenneTable
name|overrideAttributeTable
decl_stmt|;
specifier|protected
name|TableColumnPreferences
name|tablePreferences
decl_stmt|;
name|ProjectController
name|mediator
decl_stmt|;
specifier|static
specifier|final
name|Dimension
name|BROWSER_CELL_DIM
init|=
operator|new
name|Dimension
argument_list|(
literal|130
argument_list|,
literal|200
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|EMBEDDABLE_PANEL
init|=
literal|"EMBEDDABLE_PANEL"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FLATTENED_PANEL
init|=
literal|"FLATTENED_PANEL"
decl_stmt|;
specifier|public
name|ObjAttributeInfoDialogView
parameter_list|(
specifier|final
name|ProjectController
name|mediator
parameter_list|)
block|{
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
comment|// create widgets
name|this
operator|.
name|cancelButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Cancel"
argument_list|)
expr_stmt|;
name|this
operator|.
name|saveButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Done"
argument_list|)
expr_stmt|;
name|this
operator|.
name|selectPathButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Select path"
argument_list|)
expr_stmt|;
name|this
operator|.
name|attributeName
operator|=
operator|new
name|JTextField
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|this
operator|.
name|currentPathLabel
operator|=
operator|new
name|JLabel
argument_list|()
expr_stmt|;
name|this
operator|.
name|sourceEntityLabel
operator|=
operator|new
name|JLabel
argument_list|()
expr_stmt|;
name|this
operator|.
name|typeComboBox
operator|=
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createComboBox
argument_list|(
name|ModelerUtil
operator|.
name|getRegisteredTypeNames
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|AutoCompletion
operator|.
name|enable
argument_list|(
name|typeComboBox
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|typeComboBox
operator|.
name|getRenderer
argument_list|()
expr_stmt|;
name|overrideAttributeTable
operator|=
operator|new
name|CayenneTable
argument_list|()
expr_stmt|;
name|tablePreferences
operator|=
operator|new
name|TableColumnPreferences
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|"overrideAttributeTable"
argument_list|)
expr_stmt|;
name|saveButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|cancelButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|selectPathButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|setTitle
argument_list|(
literal|"ObjAttribute Inspector"
argument_list|)
expr_stmt|;
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|CellConstraints
name|cc
init|=
operator|new
name|CellConstraints
argument_list|()
decl_stmt|;
specifier|final
name|PanelBuilder
name|builder
init|=
operator|new
name|PanelBuilder
argument_list|(
operator|new
name|FormLayout
argument_list|(
literal|"right:max(50dlu;pref), 3dlu, 200dlu, 15dlu, right:max(30dlu;pref), 3dlu, 200dlu"
argument_list|,
literal|"p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 6dlu, p, 6dlu, p, 3dlu, fill:p:grow"
argument_list|)
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setDefaultDialogBorder
argument_list|()
expr_stmt|;
name|builder
operator|.
name|addSeparator
argument_list|(
literal|"ObjAttribute Information"
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|7
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Attribute:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|attributeName
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Current Db Path:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|currentPathLabel
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Source:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|sourceEntityLabel
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|7
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Type:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|9
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|typeComboBox
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|9
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addSeparator
argument_list|(
literal|"Mapping to Attributes"
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|11
argument_list|,
literal|7
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|typeManagerPane
operator|=
operator|new
name|JPanel
argument_list|()
expr_stmt|;
name|typeManagerPane
operator|.
name|setLayout
argument_list|(
operator|new
name|CardLayout
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|FormLayout
name|fL
init|=
operator|new
name|FormLayout
argument_list|(
literal|"493dlu "
argument_list|,
literal|"p, 3dlu, fill:min(128dlu;pref):grow"
argument_list|)
decl_stmt|;
comment|// panel for Flattened attribute
specifier|final
name|PanelBuilder
name|builderPathPane
init|=
operator|new
name|PanelBuilder
argument_list|(
name|fL
argument_list|)
decl_stmt|;
name|JPanel
name|buttonsPane
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|LEADING
argument_list|)
argument_list|)
decl_stmt|;
name|buttonsPane
operator|.
name|add
argument_list|(
name|selectPathButton
argument_list|)
expr_stmt|;
name|builderPathPane
operator|.
name|add
argument_list|(
name|buttonsPane
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|pathBrowser
operator|=
operator|new
name|ObjAttributePathBrowser
argument_list|(
name|selectPathButton
argument_list|,
name|saveButton
argument_list|)
expr_stmt|;
name|pathBrowser
operator|.
name|setPreferredColumnSize
argument_list|(
name|BROWSER_CELL_DIM
argument_list|)
expr_stmt|;
name|pathBrowser
operator|.
name|setDefaultRenderer
argument_list|()
expr_stmt|;
name|builderPathPane
operator|.
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|pathBrowser
argument_list|,
name|JScrollPane
operator|.
name|VERTICAL_SCROLLBAR_AS_NEEDED
argument_list|,
name|JScrollPane
operator|.
name|HORIZONTAL_SCROLLBAR_AS_NEEDED
argument_list|)
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
comment|// panel for embeddable attribute
specifier|final
name|FormLayout
name|fLEmb
init|=
operator|new
name|FormLayout
argument_list|(
literal|"493dlu "
argument_list|,
literal|"fill:min(140dlu;pref):grow"
argument_list|)
decl_stmt|;
specifier|final
name|PanelBuilder
name|embeddablePane
init|=
operator|new
name|PanelBuilder
argument_list|(
name|fLEmb
argument_list|)
decl_stmt|;
name|embeddablePane
operator|.
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|overrideAttributeTable
argument_list|,
name|JScrollPane
operator|.
name|VERTICAL_SCROLLBAR_AS_NEEDED
argument_list|,
name|JScrollPane
operator|.
name|HORIZONTAL_SCROLLBAR_AS_NEEDED
argument_list|)
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|typeManagerPane
operator|.
name|add
argument_list|(
name|builderPathPane
operator|.
name|getPanel
argument_list|()
argument_list|,
name|FLATTENED_PANEL
argument_list|)
expr_stmt|;
name|typeManagerPane
operator|.
name|add
argument_list|(
name|embeddablePane
operator|.
name|getPanel
argument_list|()
argument_list|,
name|EMBEDDABLE_PANEL
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|typeManagerPane
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|13
argument_list|,
literal|7
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|builder
operator|.
name|getPanel
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|this
operator|.
name|addComponentListener
argument_list|(
operator|new
name|ComponentListener
argument_list|()
block|{
name|int
name|height
decl_stmt|;
specifier|public
name|void
name|componentHidden
parameter_list|(
name|ComponentEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|componentMoved
parameter_list|(
name|ComponentEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|void
name|componentResized
parameter_list|(
name|ComponentEvent
name|e
parameter_list|)
block|{
name|int
name|delta
init|=
name|e
operator|.
name|getComponent
argument_list|()
operator|.
name|getHeight
argument_list|()
operator|-
name|height
decl_stmt|;
if|if
condition|(
name|delta
operator|<
literal|0
condition|)
block|{
name|fL
operator|.
name|setRowSpec
argument_list|(
literal|3
argument_list|,
name|RowSpec
operator|.
name|decode
argument_list|(
literal|"fill:min(10dlu;pref):grow"
argument_list|)
argument_list|)
expr_stmt|;
name|fLEmb
operator|.
name|setRowSpec
argument_list|(
literal|1
argument_list|,
name|RowSpec
operator|.
name|decode
argument_list|(
literal|"fill:min(10dlu;pref):grow"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|componentShown
parameter_list|(
name|ComponentEvent
name|e
parameter_list|)
block|{
name|height
operator|=
name|e
operator|.
name|getComponent
argument_list|()
operator|.
name|getHeight
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|PanelFactory
operator|.
name|createButtonPanel
argument_list|(
operator|new
name|JButton
index|[]
block|{
name|saveButton
block|,
name|cancelButton
block|}
argument_list|)
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
name|typeComboBox
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|boolean
name|isType
init|=
literal|false
decl_stmt|;
name|String
index|[]
name|typeNames
init|=
name|ModelerUtil
operator|.
name|getRegisteredTypeNames
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
name|typeNames
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|typeComboBox
operator|.
name|getSelectedItem
argument_list|()
operator|==
literal|null
operator|||
name|typeNames
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|typeComboBox
operator|.
name|getSelectedItem
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|isType
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|isType
operator|||
operator|!
name|mediator
operator|.
name|getEmbeddableNamesInCurrentDataDomain
argument_list|()
operator|.
name|contains
argument_list|(
name|typeComboBox
operator|.
name|getSelectedItem
argument_list|()
argument_list|)
condition|)
block|{
operator|(
operator|(
name|CardLayout
operator|)
name|typeManagerPane
operator|.
name|getLayout
argument_list|()
operator|)
operator|.
name|show
argument_list|(
name|typeManagerPane
argument_list|,
name|FLATTENED_PANEL
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|(
operator|(
name|CardLayout
operator|)
name|typeManagerPane
operator|.
name|getLayout
argument_list|()
operator|)
operator|.
name|show
argument_list|(
name|typeManagerPane
argument_list|,
name|EMBEDDABLE_PANEL
argument_list|)
expr_stmt|;
name|getCurrentPathLabel
argument_list|()
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CayenneTable
name|getOverrideAttributeTable
parameter_list|()
block|{
return|return
name|overrideAttributeTable
return|;
block|}
specifier|public
name|TableColumnPreferences
name|getTablePreferences
parameter_list|()
block|{
return|return
name|tablePreferences
return|;
block|}
specifier|public
name|JComboBox
name|getTypeComboBox
parameter_list|()
block|{
return|return
name|typeComboBox
return|;
block|}
specifier|public
name|MultiColumnBrowser
name|getPathBrowser
parameter_list|()
block|{
return|return
name|pathBrowser
return|;
block|}
specifier|public
name|JButton
name|getCancelButton
parameter_list|()
block|{
return|return
name|cancelButton
return|;
block|}
specifier|public
name|JButton
name|getSaveButton
parameter_list|()
block|{
return|return
name|saveButton
return|;
block|}
specifier|public
name|JButton
name|getSelectPathButton
parameter_list|()
block|{
return|return
name|selectPathButton
return|;
block|}
specifier|public
name|JTextField
name|getAttributeName
parameter_list|()
block|{
return|return
name|attributeName
return|;
block|}
specifier|public
name|JLabel
name|getCurrentPathLabel
parameter_list|()
block|{
return|return
name|currentPathLabel
return|;
block|}
specifier|public
name|JLabel
name|getSourceEntityLabel
parameter_list|()
block|{
return|return
name|sourceEntityLabel
return|;
block|}
block|}
end_class

end_unit

