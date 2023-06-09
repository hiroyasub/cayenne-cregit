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
name|objentity
package|;
end_package

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
name|JCheckBox
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
name|Component
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DeleteRule
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
name|util
operator|.
name|DefaultWidgetFactory
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
name|WidgetFactory
import|;
end_import

begin_class
specifier|public
class|class
name|ObjRelationshipInfoView
extends|extends
name|JDialog
block|{
specifier|private
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
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|DELETE_RULES
init|=
operator|new
name|String
index|[]
block|{
name|DeleteRule
operator|.
name|deleteRuleName
argument_list|(
name|DeleteRule
operator|.
name|NO_ACTION
argument_list|)
block|,
name|DeleteRule
operator|.
name|deleteRuleName
argument_list|(
name|DeleteRule
operator|.
name|NULLIFY
argument_list|)
block|,
name|DeleteRule
operator|.
name|deleteRuleName
argument_list|(
name|DeleteRule
operator|.
name|CASCADE
argument_list|)
block|,
name|DeleteRule
operator|.
name|deleteRuleName
argument_list|(
name|DeleteRule
operator|.
name|DENY
argument_list|)
block|,     }
decl_stmt|;
specifier|private
name|MultiColumnBrowser
name|pathBrowser
decl_stmt|;
specifier|private
name|Component
name|collectionTypeLabel
decl_stmt|;
specifier|private
name|JComboBox
argument_list|<
name|String
argument_list|>
name|collectionTypeCombo
decl_stmt|;
specifier|private
name|Component
name|mapKeysLabel
decl_stmt|;
specifier|private
name|JComboBox
argument_list|<
name|String
argument_list|>
name|mapKeysCombo
decl_stmt|;
specifier|private
name|JButton
name|saveButton
decl_stmt|;
specifier|private
name|JButton
name|cancelButton
decl_stmt|;
specifier|private
name|JButton
name|newRelButton
decl_stmt|;
specifier|private
name|JTextField
name|relationshipName
decl_stmt|;
specifier|private
name|JLabel
name|semanticsLabel
decl_stmt|;
specifier|private
name|JLabel
name|sourceEntityLabel
decl_stmt|;
specifier|private
name|JComboBox
argument_list|<
name|String
argument_list|>
name|targetCombo
decl_stmt|;
specifier|private
name|JComboBox
argument_list|<
name|String
argument_list|>
name|deleteRule
decl_stmt|;
specifier|private
name|JCheckBox
name|usedForLocking
decl_stmt|;
specifier|private
name|JTextField
name|comment
decl_stmt|;
specifier|public
name|ObjRelationshipInfoView
parameter_list|()
block|{
name|super
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|)
expr_stmt|;
name|WidgetFactory
name|widgetFactory
init|=
operator|new
name|DefaultWidgetFactory
argument_list|()
decl_stmt|;
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
name|newRelButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"New DbRelationship"
argument_list|)
expr_stmt|;
name|this
operator|.
name|relationshipName
operator|=
operator|new
name|JTextField
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|this
operator|.
name|semanticsLabel
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
name|cancelButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|getRootPane
argument_list|()
operator|.
name|setDefaultButton
argument_list|(
name|saveButton
argument_list|)
expr_stmt|;
name|saveButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|newRelButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|collectionTypeCombo
operator|=
name|widgetFactory
operator|.
name|createComboBox
argument_list|()
expr_stmt|;
name|collectionTypeCombo
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|targetCombo
operator|=
name|widgetFactory
operator|.
name|createComboBox
argument_list|()
expr_stmt|;
name|targetCombo
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|mapKeysCombo
operator|=
name|widgetFactory
operator|.
name|createComboBox
argument_list|()
expr_stmt|;
name|mapKeysCombo
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|pathBrowser
operator|=
operator|new
name|ObjRelationshipPathBrowser
argument_list|()
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
name|this
operator|.
name|deleteRule
operator|=
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createComboBox
argument_list|(
name|DELETE_RULES
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|usedForLocking
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|comment
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|setTitle
argument_list|(
literal|"ObjRelationship Inspector"
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
name|PanelBuilder
name|builder
init|=
operator|new
name|PanelBuilder
argument_list|(
operator|new
name|FormLayout
argument_list|(
literal|"right:max(50dlu;pref), 3dlu, fill:min(150dlu;pref), 3dlu, 300dlu, 3dlu, fill:min(120dlu;pref)"
argument_list|,
literal|"p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, top:14dlu, 3dlu, top:p:grow"
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
literal|"ObjRelationship Information"
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|1
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
literal|"Source Entity:"
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
name|sourceEntityLabel
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
literal|"Target Entity:"
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
name|targetCombo
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|5
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
literal|"Relationship Name:"
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
name|relationshipName
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
literal|"Semantics:"
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
name|semanticsLabel
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|9
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|collectionTypeLabel
operator|=
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Collection Type:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|11
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|collectionTypeCombo
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|11
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|mapKeysLabel
operator|=
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Map Key:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|13
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|mapKeysCombo
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|13
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
literal|"Delete rule:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|15
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|deleteRule
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|15
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
literal|"Used for locking:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|17
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|usedForLocking
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|17
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
literal|"Comment:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|19
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|comment
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|19
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
literal|"Mapping to DbRelationships"
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|21
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
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
name|newRelButton
argument_list|)
expr_stmt|;
name|builder
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
literal|23
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
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
literal|25
argument_list|,
literal|5
argument_list|,
literal|3
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
name|JButton
index|[]
name|buttons
init|=
block|{
name|cancelButton
block|,
name|saveButton
block|}
decl_stmt|;
name|add
argument_list|(
name|PanelFactory
operator|.
name|createButtonPanel
argument_list|(
name|buttons
argument_list|)
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
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
name|getCancelButton
parameter_list|()
block|{
return|return
name|cancelButton
return|;
block|}
specifier|public
name|JButton
name|getNewRelButton
parameter_list|()
block|{
return|return
name|newRelButton
return|;
block|}
specifier|public
name|JTextField
name|getRelationshipName
parameter_list|()
block|{
return|return
name|relationshipName
return|;
block|}
specifier|public
name|JLabel
name|getSemanticsLabel
parameter_list|()
block|{
return|return
name|semanticsLabel
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
specifier|public
name|JComboBox
argument_list|<
name|String
argument_list|>
name|getTargetCombo
parameter_list|()
block|{
return|return
name|targetCombo
return|;
block|}
specifier|public
name|JComboBox
argument_list|<
name|String
argument_list|>
name|getCollectionTypeCombo
parameter_list|()
block|{
return|return
name|collectionTypeCombo
return|;
block|}
specifier|public
name|JComboBox
argument_list|<
name|String
argument_list|>
name|getMapKeysCombo
parameter_list|()
block|{
return|return
name|mapKeysCombo
return|;
block|}
specifier|public
name|JComboBox
argument_list|<
name|String
argument_list|>
name|getDeleteRule
parameter_list|()
block|{
return|return
name|deleteRule
return|;
block|}
specifier|public
name|JCheckBox
name|getUsedForLocking
parameter_list|()
block|{
return|return
name|usedForLocking
return|;
block|}
specifier|public
name|JTextField
name|getComment
parameter_list|()
block|{
return|return
name|comment
return|;
block|}
specifier|public
name|Component
name|getMapKeysLabel
parameter_list|()
block|{
return|return
name|mapKeysLabel
return|;
block|}
specifier|public
name|Component
name|getCollectionTypeLabel
parameter_list|()
block|{
return|return
name|collectionTypeLabel
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
block|}
end_class

end_unit

