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
name|codegen
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
name|swing
operator|.
name|components
operator|.
name|TopBorder
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
name|JButton
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
name|JTabbedPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ScrollPaneConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|SwingConstants
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|CodeGeneratorDialog
extends|extends
name|JDialog
block|{
specifier|protected
name|JTabbedPane
name|tabs
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
name|classesCount
decl_stmt|;
specifier|public
name|CodeGeneratorDialog
parameter_list|(
name|Component
name|generatorPanel
parameter_list|,
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
name|tabs
operator|=
operator|new
name|JTabbedPane
argument_list|(
name|SwingConstants
operator|.
name|TOP
argument_list|)
expr_stmt|;
name|this
operator|.
name|tabs
operator|.
name|setFocusable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|generateButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Generate"
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
name|classesCount
operator|=
operator|new
name|JLabel
argument_list|(
literal|"No classes selected"
argument_list|)
expr_stmt|;
name|classesCount
operator|.
name|setFont
argument_list|(
name|classesCount
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
name|JScrollPane
name|scrollPane
init|=
operator|new
name|JScrollPane
argument_list|(
name|generatorPanel
argument_list|,
name|ScrollPaneConstants
operator|.
name|VERTICAL_SCROLLBAR_AS_NEEDED
argument_list|,
name|ScrollPaneConstants
operator|.
name|HORIZONTAL_SCROLLBAR_AS_NEEDED
argument_list|)
decl_stmt|;
name|scrollPane
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|800
argument_list|,
literal|400
argument_list|)
argument_list|)
expr_stmt|;
comment|// assemble
name|tabs
operator|.
name|addTab
argument_list|(
literal|"Code Generator"
argument_list|,
name|scrollPane
argument_list|)
expr_stmt|;
name|tabs
operator|.
name|addTab
argument_list|(
literal|"Classes"
argument_list|,
name|entitySelectorPanel
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
name|classesCount
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
name|setBorder
argument_list|(
name|TopBorder
operator|.
name|create
argument_list|()
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|classesCount
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
name|tabs
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
name|setTitle
argument_list|(
literal|"Code Generation"
argument_list|)
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
name|getClassesCount
parameter_list|()
block|{
return|return
name|classesCount
return|;
block|}
block|}
end_class

end_unit

