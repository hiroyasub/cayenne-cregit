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
name|autorelationship
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
name|NameGeneratorPreferences
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|Box
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|DefaultComboBoxModel
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
name|Container
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

begin_class
specifier|public
class|class
name|InferRelationshipsDialog
extends|extends
name|JDialog
block|{
specifier|public
specifier|static
specifier|final
name|int
name|SELECT
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|CANCEL
init|=
literal|0
decl_stmt|;
specifier|protected
name|int
name|choice
decl_stmt|;
specifier|protected
name|JButton
name|generateButton
decl_stmt|;
specifier|protected
name|JButton
name|cancelButton
decl_stmt|;
specifier|protected
name|JLabel
name|entityCount
decl_stmt|;
specifier|protected
name|JLabel
name|strategyLabel
decl_stmt|;
specifier|protected
name|JComboBox
argument_list|<
name|String
argument_list|>
name|strategyCombo
decl_stmt|;
specifier|public
name|InferRelationshipsDialog
parameter_list|(
name|Component
name|entitySelectorPanel
parameter_list|)
block|{
name|super
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|generateButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Create DbRelationships"
argument_list|)
expr_stmt|;
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
name|entityCount
operator|=
operator|new
name|JLabel
argument_list|(
literal|"No DbRelationships selected"
argument_list|)
expr_stmt|;
name|entityCount
operator|.
name|setFont
argument_list|(
name|entityCount
operator|.
name|getFont
argument_list|()
operator|.
name|deriveFont
argument_list|(
literal|10f
argument_list|)
argument_list|)
expr_stmt|;
name|getRootPane
argument_list|()
operator|.
name|setDefaultButton
argument_list|(
name|generateButton
argument_list|)
expr_stmt|;
name|this
operator|.
name|strategyCombo
operator|=
operator|new
name|JComboBox
argument_list|<>
argument_list|()
expr_stmt|;
name|strategyCombo
operator|.
name|setEditable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|strategyLabel
operator|=
operator|new
name|JLabel
argument_list|(
literal|"Naming Strategy:  "
argument_list|)
expr_stmt|;
comment|// assemble
name|JPanel
name|strategyPanel
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|LEFT
argument_list|)
argument_list|)
decl_stmt|;
name|strategyPanel
operator|.
name|add
argument_list|(
name|strategyLabel
argument_list|)
expr_stmt|;
name|strategyPanel
operator|.
name|add
argument_list|(
name|strategyCombo
argument_list|)
expr_stmt|;
name|JPanel
name|messages
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
decl_stmt|;
name|messages
operator|.
name|add
argument_list|(
name|entityCount
argument_list|,
name|BorderLayout
operator|.
name|WEST
argument_list|)
expr_stmt|;
name|JPanel
name|buttons
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|RIGHT
argument_list|)
argument_list|)
decl_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|entityCount
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|Box
operator|.
name|createHorizontalStrut
argument_list|(
literal|50
argument_list|)
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|cancelButton
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|generateButton
argument_list|)
expr_stmt|;
name|Container
name|contentPane
init|=
name|getContentPane
argument_list|()
decl_stmt|;
name|contentPane
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|contentPane
operator|.
name|add
argument_list|(
name|strategyPanel
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
name|contentPane
operator|.
name|add
argument_list|(
name|entitySelectorPanel
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|contentPane
operator|.
name|add
argument_list|(
name|buttons
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
name|this
operator|.
name|choice
operator|=
name|CANCEL
expr_stmt|;
name|strategyCombo
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|NameGeneratorPreferences
operator|.
name|getInstance
argument_list|()
operator|.
name|getLastUsedStrategies
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setTitle
argument_list|(
literal|"Infer Relationships"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getChoice
parameter_list|()
block|{
return|return
name|choice
return|;
block|}
specifier|public
name|void
name|setChoice
parameter_list|(
name|int
name|choice
parameter_list|)
block|{
name|this
operator|.
name|choice
operator|=
name|choice
expr_stmt|;
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
name|getGenerateButton
parameter_list|()
block|{
return|return
name|generateButton
return|;
block|}
specifier|public
name|JLabel
name|getEntityCount
parameter_list|()
block|{
return|return
name|entityCount
return|;
block|}
specifier|public
name|JComboBox
argument_list|<
name|String
argument_list|>
name|getStrategyCombo
parameter_list|()
block|{
return|return
name|strategyCombo
return|;
block|}
block|}
end_class

end_unit

