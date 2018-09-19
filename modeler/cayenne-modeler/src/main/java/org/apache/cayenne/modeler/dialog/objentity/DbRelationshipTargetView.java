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

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ButtonGroup
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
name|JRadioButton
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

begin_class
specifier|public
class|class
name|DbRelationshipTargetView
extends|extends
name|JDialog
block|{
specifier|protected
name|WidgetFactory
name|widgetFactory
decl_stmt|;
specifier|protected
name|JCheckBox
name|toManyCheckBox
decl_stmt|;
specifier|protected
name|JButton
name|saveButton
decl_stmt|;
specifier|protected
name|JButton
name|cancelButton
decl_stmt|;
specifier|protected
name|JRadioButton
name|source1Button
decl_stmt|;
specifier|protected
name|JRadioButton
name|source2Button
decl_stmt|;
specifier|protected
name|JComboBox
argument_list|<
name|String
argument_list|>
name|targetCombo
decl_stmt|;
specifier|public
name|DbRelationshipTargetView
parameter_list|(
name|DbEntity
name|source1
parameter_list|,
name|DbEntity
name|source2
parameter_list|)
block|{
name|widgetFactory
operator|=
operator|new
name|DefaultWidgetFactory
argument_list|()
expr_stmt|;
comment|// create widgets
name|saveButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Continue"
argument_list|)
expr_stmt|;
name|saveButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|cancelButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Cancel"
argument_list|)
expr_stmt|;
name|cancelButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|source1Button
operator|=
operator|new
name|JRadioButton
argument_list|()
expr_stmt|;
name|source2Button
operator|=
operator|new
name|JRadioButton
argument_list|()
expr_stmt|;
name|source2Button
operator|.
name|setEnabled
argument_list|(
name|source2
operator|!=
literal|null
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
name|ButtonGroup
name|bg
init|=
operator|new
name|ButtonGroup
argument_list|()
decl_stmt|;
name|bg
operator|.
name|add
argument_list|(
name|source1Button
argument_list|)
expr_stmt|;
name|bg
operator|.
name|add
argument_list|(
name|source2Button
argument_list|)
expr_stmt|;
name|toManyCheckBox
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|setTitle
argument_list|(
literal|"Create New DbRelationship"
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
literal|"right:max(100dlu;pref), 3dlu, fill:min(150dlu;pref)"
argument_list|,
literal|"p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, top:p:grow"
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
name|addLabel
argument_list|(
literal|"Source: "
operator|+
name|source1
operator|.
name|getName
argument_list|()
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|source1Button
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Source: "
operator|+
operator|(
name|source2
operator|==
literal|null
condition|?
literal|""
else|:
name|source2
operator|.
name|getName
argument_list|()
operator|)
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
name|source2Button
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Target:"
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
literal|"To Many:"
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
name|toManyCheckBox
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
name|JRadioButton
name|getSource1Button
parameter_list|()
block|{
return|return
name|source1Button
return|;
block|}
specifier|public
name|JRadioButton
name|getSource2Button
parameter_list|()
block|{
return|return
name|source2Button
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
name|getCancelButton
parameter_list|()
block|{
return|return
name|cancelButton
return|;
block|}
specifier|public
name|JCheckBox
name|getToManyCheckBox
parameter_list|()
block|{
return|return
name|toManyCheckBox
return|;
block|}
block|}
end_class

end_unit

