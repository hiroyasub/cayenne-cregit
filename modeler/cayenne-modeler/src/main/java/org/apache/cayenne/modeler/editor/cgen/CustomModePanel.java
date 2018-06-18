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
name|cgen
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
name|DefaultFormBuilder
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
name|swing
operator|.
name|control
operator|.
name|ActionLink
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|CustomModePanel
extends|extends
name|GeneratorControllerPanel
block|{
specifier|protected
name|JComboBox
name|generationMode
decl_stmt|;
specifier|protected
name|JComboBox
name|subclassTemplate
decl_stmt|;
specifier|protected
name|JComboBox
name|superclassTemplate
decl_stmt|;
specifier|protected
name|JCheckBox
name|pairs
decl_stmt|;
specifier|protected
name|JCheckBox
name|overwrite
decl_stmt|;
specifier|protected
name|JCheckBox
name|usePackagePath
decl_stmt|;
specifier|protected
name|JTextField
name|outputPattern
decl_stmt|;
specifier|protected
name|JCheckBox
name|createPropertyNames
decl_stmt|;
specifier|private
name|JTextField
name|superclassPackage
decl_stmt|;
specifier|private
name|JTextField
name|additionalMaps
decl_stmt|;
specifier|private
name|JButton
name|selectAdditionalMaps
decl_stmt|;
specifier|private
name|JCheckBox
name|client
decl_stmt|;
specifier|private
name|JTextField
name|encoding
decl_stmt|;
specifier|private
name|JComboBox
name|embeddableTemplate
decl_stmt|;
specifier|private
name|JComboBox
name|embeddableSuperTemplate
decl_stmt|;
specifier|private
name|JLabel
name|dataMapName
decl_stmt|;
specifier|private
name|DefaultFormBuilder
name|builder
decl_stmt|;
specifier|protected
name|ActionLink
name|manageTemplatesLink
decl_stmt|;
specifier|public
name|CustomModePanel
parameter_list|()
block|{
name|this
operator|.
name|generationMode
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|superclassTemplate
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|subclassTemplate
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|pairs
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|overwrite
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|usePackagePath
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|outputPattern
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|this
operator|.
name|createPropertyNames
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|manageTemplatesLink
operator|=
operator|new
name|ActionLink
argument_list|(
literal|"Customize Templates..."
argument_list|)
expr_stmt|;
name|this
operator|.
name|manageTemplatesLink
operator|.
name|setFont
argument_list|(
name|manageTemplatesLink
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
name|this
operator|.
name|superclassPackage
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|this
operator|.
name|additionalMaps
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|this
operator|.
name|selectAdditionalMaps
operator|=
operator|new
name|JButton
argument_list|(
literal|"Select"
argument_list|)
expr_stmt|;
name|this
operator|.
name|client
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|encoding
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|this
operator|.
name|embeddableTemplate
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|embeddableSuperTemplate
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataMapName
operator|=
operator|new
name|JLabel
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataMapName
operator|.
name|setFont
argument_list|(
name|dataMapName
operator|.
name|getFont
argument_list|()
operator|.
name|deriveFont
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|pairs
operator|.
name|addChangeListener
argument_list|(
name|e
lambda|->
block|{
name|superclassTemplate
operator|.
name|setEnabled
argument_list|(
name|pairs
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
name|overwrite
operator|.
name|setEnabled
argument_list|(
operator|!
name|pairs
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
comment|// assemble
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"right:77dlu, 3dlu, fill:200:grow, 6dlu, fill:50dlu, 3dlu"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|builder
operator|=
operator|new
name|DefaultFormBuilder
argument_list|(
name|layout
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setDefaultDialogBorder
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Output Directory:"
argument_list|,
name|outputFolder
argument_list|,
name|selectOutputFolder
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Additional DataMaps"
argument_list|,
name|additionalMaps
argument_list|,
name|selectAdditionalMaps
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Generation Mode:"
argument_list|,
name|generationMode
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Subclass Template:"
argument_list|,
name|subclassTemplate
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Superclass Template:"
argument_list|,
name|superclassTemplate
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Embeddable Template"
argument_list|,
name|embeddableTemplate
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Embeddable Super Template"
argument_list|,
name|embeddableSuperTemplate
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Output Pattern:"
argument_list|,
name|outputPattern
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Encoding"
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Make Pairs:"
argument_list|,
name|pairs
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Use Package Path:"
argument_list|,
name|usePackagePath
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Overwrite Subclasses:"
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Create Property Names:"
argument_list|,
name|createPropertyNames
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Client"
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|dataMapName
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Superclass package"
argument_list|,
name|superclassPackage
argument_list|)
expr_stmt|;
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
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
name|JPanel
name|links
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|TRAILING
argument_list|)
argument_list|)
decl_stmt|;
name|links
operator|.
name|add
argument_list|(
name|manageTemplatesLink
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|links
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
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
block|}
specifier|public
name|JComboBox
name|getGenerationMode
parameter_list|()
block|{
return|return
name|generationMode
return|;
block|}
specifier|public
name|ActionLink
name|getManageTemplatesLink
parameter_list|()
block|{
return|return
name|manageTemplatesLink
return|;
block|}
specifier|public
name|JComboBox
name|getSubclassTemplate
parameter_list|()
block|{
return|return
name|subclassTemplate
return|;
block|}
specifier|public
name|JComboBox
name|getSuperclassTemplate
parameter_list|()
block|{
return|return
name|superclassTemplate
return|;
block|}
specifier|public
name|JCheckBox
name|getOverwrite
parameter_list|()
block|{
return|return
name|overwrite
return|;
block|}
specifier|public
name|JCheckBox
name|getPairs
parameter_list|()
block|{
return|return
name|pairs
return|;
block|}
specifier|public
name|JCheckBox
name|getUsePackagePath
parameter_list|()
block|{
return|return
name|usePackagePath
return|;
block|}
specifier|public
name|JTextField
name|getOutputPattern
parameter_list|()
block|{
return|return
name|outputPattern
return|;
block|}
specifier|public
name|JCheckBox
name|getCreatePropertyNames
parameter_list|()
block|{
return|return
name|createPropertyNames
return|;
block|}
specifier|public
name|JTextField
name|getSuperclassPackage
parameter_list|()
block|{
return|return
name|superclassPackage
return|;
block|}
specifier|public
name|void
name|setDataMapName
parameter_list|(
name|String
name|mapName
parameter_list|)
block|{
name|dataMapName
operator|.
name|setText
argument_list|(
name|mapName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSuperclassPackage
parameter_list|(
name|String
name|pack
parameter_list|)
block|{
name|superclassPackage
operator|.
name|setText
argument_list|(
name|pack
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setPairs
parameter_list|(
name|boolean
name|val
parameter_list|)
block|{
name|pairs
operator|.
name|setSelected
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setOverwrite
parameter_list|(
name|boolean
name|val
parameter_list|)
block|{
name|overwrite
operator|.
name|setSelected
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setUsePackagePath
parameter_list|(
name|boolean
name|val
parameter_list|)
block|{
name|usePackagePath
operator|.
name|setSelected
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setCreatePropertyNames
parameter_list|(
name|boolean
name|val
parameter_list|)
block|{
name|createPropertyNames
operator|.
name|setSelected
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setOutputPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|outputPattern
operator|.
name|setText
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSuperclassTemplate
parameter_list|(
name|String
name|template
parameter_list|)
block|{
name|superclassTemplate
operator|.
name|setSelectedItem
argument_list|(
name|template
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setTemplate
parameter_list|(
name|String
name|template
parameter_list|)
block|{
name|subclassTemplate
operator|.
name|setSelectedItem
argument_list|(
name|template
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setGenerationMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|generationMode
operator|.
name|setSelectedItem
argument_list|(
name|mode
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

