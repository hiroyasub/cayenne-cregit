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
name|editor
operator|.
name|cgen
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
name|JPanel
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
name|gen
operator|.
name|CgenConfiguration
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
name|ComboBoxAdapter
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
name|TextAdapter
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
name|JCayenneCheckBox
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
name|validation
operator|.
name|ValidationException
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|CustomModePanel
extends|extends
name|GeneratorControllerPanel
block|{
specifier|private
name|ComboBoxAdapter
argument_list|<
name|String
argument_list|>
name|subclassTemplate
decl_stmt|;
specifier|private
name|ComboBoxAdapter
argument_list|<
name|String
argument_list|>
name|superclassTemplate
decl_stmt|;
specifier|private
name|ComboBoxAdapter
argument_list|<
name|String
argument_list|>
name|embeddableTemplate
decl_stmt|;
specifier|private
name|ComboBoxAdapter
argument_list|<
name|String
argument_list|>
name|embeddableSuperTemplate
decl_stmt|;
specifier|private
name|ComboBoxAdapter
argument_list|<
name|String
argument_list|>
name|queryTemplate
decl_stmt|;
specifier|private
name|ComboBoxAdapter
argument_list|<
name|String
argument_list|>
name|querySuperTemplate
decl_stmt|;
specifier|private
name|JCheckBox
name|pairs
decl_stmt|;
specifier|private
name|JCheckBox
name|overwrite
decl_stmt|;
specifier|private
name|JCheckBox
name|usePackagePath
decl_stmt|;
specifier|private
name|TextAdapter
name|outputPattern
decl_stmt|;
specifier|private
name|JCheckBox
name|createPropertyNames
decl_stmt|;
specifier|private
name|JCheckBox
name|pkProperties
decl_stmt|;
specifier|private
name|TextAdapter
name|superPkg
decl_stmt|;
specifier|private
name|JButton
name|manageTemplatesLink
decl_stmt|;
name|CustomModePanel
parameter_list|(
name|ProjectController
name|projectController
parameter_list|,
name|CodeGeneratorController
name|codeGeneratorControllerBase
parameter_list|)
block|{
name|super
argument_list|(
name|projectController
argument_list|,
name|codeGeneratorControllerBase
argument_list|)
expr_stmt|;
name|JComboBox
argument_list|<
name|String
argument_list|>
name|superclassField
init|=
operator|new
name|JComboBox
argument_list|<>
argument_list|()
decl_stmt|;
name|this
operator|.
name|superclassTemplate
operator|=
operator|new
name|ComboBoxAdapter
argument_list|<>
argument_list|(
name|superclassField
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|item
parameter_list|)
throws|throws
name|ValidationException
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
name|getCgenConfig
argument_list|()
decl_stmt|;
name|cgenConfiguration
operator|.
name|setSuperTemplate
argument_list|(
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getCodeTemplateManager
argument_list|()
operator|.
name|getTemplatePath
argument_list|(
name|item
argument_list|,
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
operator|.
name|getConfigurationSource
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|checkConfigDirty
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|JComboBox
argument_list|<
name|String
argument_list|>
name|subclassField
init|=
operator|new
name|JComboBox
argument_list|<>
argument_list|()
decl_stmt|;
name|this
operator|.
name|subclassTemplate
operator|=
operator|new
name|ComboBoxAdapter
argument_list|<>
argument_list|(
name|subclassField
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|item
parameter_list|)
throws|throws
name|ValidationException
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
name|getCgenConfig
argument_list|()
decl_stmt|;
name|cgenConfiguration
operator|.
name|setTemplate
argument_list|(
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getCodeTemplateManager
argument_list|()
operator|.
name|getTemplatePath
argument_list|(
name|item
argument_list|,
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
operator|.
name|getConfigurationSource
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|checkConfigDirty
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|JComboBox
argument_list|<
name|String
argument_list|>
name|embeddableField
init|=
operator|new
name|JComboBox
argument_list|<>
argument_list|()
decl_stmt|;
name|this
operator|.
name|embeddableTemplate
operator|=
operator|new
name|ComboBoxAdapter
argument_list|<>
argument_list|(
name|embeddableField
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|item
parameter_list|)
throws|throws
name|ValidationException
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
name|getCgenConfig
argument_list|()
decl_stmt|;
name|cgenConfiguration
operator|.
name|setEmbeddableTemplate
argument_list|(
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getCodeTemplateManager
argument_list|()
operator|.
name|getTemplatePath
argument_list|(
name|item
argument_list|,
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
operator|.
name|getConfigurationSource
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|checkConfigDirty
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|JComboBox
argument_list|<
name|String
argument_list|>
name|embeddableSuperField
init|=
operator|new
name|JComboBox
argument_list|<>
argument_list|()
decl_stmt|;
name|this
operator|.
name|embeddableSuperTemplate
operator|=
operator|new
name|ComboBoxAdapter
argument_list|<>
argument_list|(
name|embeddableSuperField
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|item
parameter_list|)
throws|throws
name|ValidationException
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
name|getCgenConfig
argument_list|()
decl_stmt|;
name|cgenConfiguration
operator|.
name|setEmbeddableSuperTemplate
argument_list|(
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getCodeTemplateManager
argument_list|()
operator|.
name|getTemplatePath
argument_list|(
name|item
argument_list|,
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
operator|.
name|getConfigurationSource
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|checkConfigDirty
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|JComboBox
argument_list|<
name|String
argument_list|>
name|queryField
init|=
operator|new
name|JComboBox
argument_list|<>
argument_list|()
decl_stmt|;
name|this
operator|.
name|queryTemplate
operator|=
operator|new
name|ComboBoxAdapter
argument_list|<>
argument_list|(
name|queryField
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|item
parameter_list|)
throws|throws
name|ValidationException
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
name|getCgenConfig
argument_list|()
decl_stmt|;
name|cgenConfiguration
operator|.
name|setDataMapTemplate
argument_list|(
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getCodeTemplateManager
argument_list|()
operator|.
name|getTemplatePath
argument_list|(
name|item
argument_list|,
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
operator|.
name|getConfigurationSource
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|checkConfigDirty
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|JComboBox
argument_list|<
name|String
argument_list|>
name|querySuperField
init|=
operator|new
name|JComboBox
argument_list|<>
argument_list|()
decl_stmt|;
name|this
operator|.
name|querySuperTemplate
operator|=
operator|new
name|ComboBoxAdapter
argument_list|<>
argument_list|(
name|querySuperField
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|item
parameter_list|)
throws|throws
name|ValidationException
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
name|getCgenConfig
argument_list|()
decl_stmt|;
name|cgenConfiguration
operator|.
name|setDataMapSuperTemplate
argument_list|(
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getCodeTemplateManager
argument_list|()
operator|.
name|getTemplatePath
argument_list|(
name|item
argument_list|,
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
operator|.
name|getConfigurationSource
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|checkConfigDirty
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|this
operator|.
name|pairs
operator|=
operator|new
name|JCayenneCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|overwrite
operator|=
operator|new
name|JCayenneCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|usePackagePath
operator|=
operator|new
name|JCayenneCheckBox
argument_list|()
expr_stmt|;
name|JTextField
name|outputPatternField
init|=
operator|new
name|JTextField
argument_list|()
decl_stmt|;
name|this
operator|.
name|outputPattern
operator|=
operator|new
name|TextAdapter
argument_list|(
name|outputPatternField
argument_list|)
block|{
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|getCgenConfig
argument_list|()
operator|.
name|setOutputPattern
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|checkConfigDirty
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|JTextField
name|superPkgField
init|=
operator|new
name|JTextField
argument_list|()
decl_stmt|;
name|this
operator|.
name|superPkg
operator|=
operator|new
name|TextAdapter
argument_list|(
name|superPkgField
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|ValidationException
block|{
name|getCgenConfig
argument_list|()
operator|.
name|setSuperPkg
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|checkConfigDirty
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|this
operator|.
name|createPropertyNames
operator|=
operator|new
name|JCayenneCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|pkProperties
operator|=
operator|new
name|JCayenneCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|manageTemplatesLink
operator|=
operator|new
name|JButton
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
name|pairs
operator|.
name|addChangeListener
argument_list|(
name|e
lambda|->
block|{
name|setDisableSuperComboBoxes
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
literal|"right:105dlu, 1dlu, fill:240:grow, 1dlu, left:100dlu, 100dlu"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|DefaultFormBuilder
name|builder
init|=
operator|new
name|DefaultFormBuilder
argument_list|(
name|layout
argument_list|)
decl_stmt|;
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
operator|.
name|getComponent
argument_list|()
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
literal|"Subclass Template:"
argument_list|,
name|subclassTemplate
operator|.
name|getComboBox
argument_list|()
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
operator|.
name|getComboBox
argument_list|()
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
literal|"Embeddable Template:"
argument_list|,
name|embeddableTemplate
operator|.
name|getComboBox
argument_list|()
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
literal|"Embeddable Superclass Template:"
argument_list|,
name|embeddableSuperTemplate
operator|.
name|getComboBox
argument_list|()
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
literal|"DataMap Template:"
argument_list|,
name|queryTemplate
operator|.
name|getComboBox
argument_list|()
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
literal|"DataMap Superclass Template:"
argument_list|,
name|querySuperTemplate
operator|.
name|getComboBox
argument_list|()
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
operator|.
name|getComponent
argument_list|()
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
literal|"Create PK properties:"
argument_list|,
name|pkProperties
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
literal|"Superclass package:"
argument_list|,
name|superPkg
operator|.
name|getComponent
argument_list|()
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
block|}
specifier|public
name|void
name|setDisableSuperComboBoxes
parameter_list|(
name|boolean
name|val
parameter_list|)
block|{
name|superclassTemplate
operator|.
name|getComboBox
argument_list|()
operator|.
name|setEnabled
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|embeddableSuperTemplate
operator|.
name|getComboBox
argument_list|()
operator|.
name|setEnabled
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|querySuperTemplate
operator|.
name|getComboBox
argument_list|()
operator|.
name|setEnabled
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JButton
name|getManageTemplatesLink
parameter_list|()
block|{
return|return
name|manageTemplatesLink
return|;
block|}
specifier|public
name|ComboBoxAdapter
argument_list|<
name|String
argument_list|>
name|getSubclassTemplate
parameter_list|()
block|{
return|return
name|subclassTemplate
return|;
block|}
specifier|public
name|ComboBoxAdapter
argument_list|<
name|String
argument_list|>
name|getSuperclassTemplate
parameter_list|()
block|{
return|return
name|superclassTemplate
return|;
block|}
specifier|public
name|ComboBoxAdapter
argument_list|<
name|String
argument_list|>
name|getEmbeddableTemplate
parameter_list|()
block|{
return|return
name|embeddableTemplate
return|;
block|}
specifier|public
name|ComboBoxAdapter
argument_list|<
name|String
argument_list|>
name|getEmbeddableSuperTemplate
parameter_list|()
block|{
return|return
name|embeddableSuperTemplate
return|;
block|}
specifier|public
name|ComboBoxAdapter
argument_list|<
name|String
argument_list|>
name|getQueryTemplate
parameter_list|()
block|{
return|return
name|queryTemplate
return|;
block|}
specifier|public
name|ComboBoxAdapter
argument_list|<
name|String
argument_list|>
name|getQuerySuperTemplate
parameter_list|()
block|{
return|return
name|querySuperTemplate
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
name|TextAdapter
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
name|JCheckBox
name|getPkProperties
parameter_list|()
block|{
return|return
name|pkProperties
return|;
block|}
specifier|public
name|TextAdapter
name|getSuperPkg
parameter_list|()
block|{
return|return
name|superPkg
return|;
block|}
block|}
end_class

end_unit

