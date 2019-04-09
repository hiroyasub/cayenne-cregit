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
name|javax
operator|.
name|swing
operator|.
name|DefaultComboBoxModel
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
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

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
name|gen
operator|.
name|ClassGenerationAction
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
name|CodeTemplateManager
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
name|dialog
operator|.
name|cgen
operator|.
name|TemplateDialog
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
name|dialog
operator|.
name|pref
operator|.
name|PreferenceDialog
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
name|BindingBuilder
import|;
end_import

begin_comment
comment|/**  * @since 4.1  * A controller for the custom generation mode.  */
end_comment

begin_class
specifier|public
class|class
name|CustomModeController
extends|extends
name|GeneratorController
block|{
specifier|protected
name|CustomModePanel
name|view
decl_stmt|;
specifier|public
name|CustomModeController
parameter_list|(
name|CodeGeneratorControllerBase
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|bind
argument_list|()
expr_stmt|;
name|initListeners
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|GeneratorControllerPanel
name|createView
parameter_list|()
block|{
name|this
operator|.
name|view
operator|=
operator|new
name|CustomModePanel
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
argument_list|,
name|getParentController
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|view
return|;
block|}
specifier|private
name|void
name|bind
parameter_list|()
block|{
name|BindingBuilder
name|builder
init|=
operator|new
name|BindingBuilder
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getBindingFactory
argument_list|()
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getManageTemplatesLink
argument_list|()
argument_list|,
literal|"popPreferencesAction()"
argument_list|)
expr_stmt|;
name|updateTemplates
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|updateTemplates
parameter_list|()
block|{
name|CodeTemplateManager
name|templateManager
init|=
name|getApplication
argument_list|()
operator|.
name|getCodeTemplateManager
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|customTemplates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|templateManager
operator|.
name|getCustomTemplates
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|customTemplates
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|superTemplates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|templateManager
operator|.
name|getStandardSuperclassTemplates
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|superTemplates
argument_list|)
expr_stmt|;
name|superTemplates
operator|.
name|addAll
argument_list|(
name|customTemplates
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|subTemplates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|templateManager
operator|.
name|getStandardSubclassTemplates
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|subTemplates
argument_list|)
expr_stmt|;
name|subTemplates
operator|.
name|addAll
argument_list|(
name|customTemplates
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|.
name|getSubclassTemplate
argument_list|()
operator|.
name|getComboBox
argument_list|()
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|<>
argument_list|(
name|subTemplates
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|.
name|getSuperclassTemplate
argument_list|()
operator|.
name|getComboBox
argument_list|()
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|<>
argument_list|(
name|superTemplates
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|public
name|void
name|missTemplateDialog
parameter_list|(
name|CgenConfiguration
name|cgenConfiguration
parameter_list|,
name|String
name|template
parameter_list|,
name|String
name|superTemplate
parameter_list|)
block|{
operator|new
name|TemplateDialog
argument_list|(
name|this
argument_list|,
name|cgenConfiguration
argument_list|,
name|template
argument_list|,
name|superTemplate
argument_list|)
operator|.
name|startupAction
argument_list|()
expr_stmt|;
name|updateComboBoxes
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|popPreferencesAction
parameter_list|()
block|{
operator|new
name|PreferenceDialog
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
argument_list|)
operator|.
name|startupAction
argument_list|(
name|PreferenceDialog
operator|.
name|TEMPLATES_KEY
argument_list|)
expr_stmt|;
name|updateTemplates
argument_list|()
expr_stmt|;
name|updateComboBoxes
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|addTemplateAction
parameter_list|(
name|String
name|template
parameter_list|,
name|String
name|superTemplate
parameter_list|)
block|{
operator|new
name|PreferenceDialog
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
argument_list|)
operator|.
name|startupToCreateTemplate
argument_list|(
name|template
argument_list|,
name|superTemplate
argument_list|)
expr_stmt|;
name|updateTemplates
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|updateComboBoxes
parameter_list|()
block|{
name|String
name|templateName
init|=
name|getApplication
argument_list|()
operator|.
name|getCodeTemplateManager
argument_list|()
operator|.
name|getNameByPath
argument_list|(
name|cgenConfiguration
operator|.
name|getTemplate
argument_list|()
argument_list|,
name|cgenConfiguration
operator|.
name|getRootPath
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|superTemplateName
init|=
name|getApplication
argument_list|()
operator|.
name|getCodeTemplateManager
argument_list|()
operator|.
name|getNameByPath
argument_list|(
name|cgenConfiguration
operator|.
name|getSuperTemplate
argument_list|()
argument_list|,
name|cgenConfiguration
operator|.
name|getRootPath
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|cgenConfiguration
operator|.
name|getRootPath
argument_list|()
operator|.
name|resolve
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|cgenConfiguration
operator|.
name|getTemplate
argument_list|()
argument_list|)
argument_list|)
operator|.
name|normalize
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|superPath
init|=
name|cgenConfiguration
operator|.
name|getRootPath
argument_list|()
operator|.
name|resolve
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|cgenConfiguration
operator|.
name|getSuperTemplate
argument_list|()
argument_list|)
argument_list|)
operator|.
name|normalize
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|templateName
operator|==
literal|null
operator|&&
name|superTemplateName
operator|==
literal|null
condition|)
block|{
name|view
operator|.
name|getSubclassTemplate
argument_list|()
operator|.
name|setItem
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|view
operator|.
name|getSuperclassTemplate
argument_list|()
operator|.
name|setItem
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|missTemplateDialog
argument_list|(
name|cgenConfiguration
argument_list|,
name|path
argument_list|,
name|superPath
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|templateName
operator|==
literal|null
condition|)
block|{
name|view
operator|.
name|getSubclassTemplate
argument_list|()
operator|.
name|setItem
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|missTemplateDialog
argument_list|(
name|cgenConfiguration
argument_list|,
name|path
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|superTemplateName
operator|==
literal|null
condition|)
block|{
name|view
operator|.
name|getSuperclassTemplate
argument_list|()
operator|.
name|setItem
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|missTemplateDialog
argument_list|(
name|cgenConfiguration
argument_list|,
literal|null
argument_list|,
name|superPath
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|view
operator|.
name|getSubclassTemplate
argument_list|()
operator|.
name|setItem
argument_list|(
name|templateName
argument_list|)
expr_stmt|;
name|view
operator|.
name|getSuperclassTemplate
argument_list|()
operator|.
name|setItem
argument_list|(
name|superTemplateName
argument_list|)
expr_stmt|;
block|}
name|view
operator|.
name|setDisableSuperComboBoxes
argument_list|(
name|view
operator|.
name|getPairs
argument_list|()
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initListeners
parameter_list|()
block|{
name|view
operator|.
name|getPairs
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|val
lambda|->
block|{
name|cgenConfiguration
operator|.
name|setMakePairs
argument_list|(
name|view
operator|.
name|getPairs
argument_list|()
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|view
operator|.
name|getPairs
argument_list|()
operator|.
name|isSelected
argument_list|()
condition|)
block|{
name|cgenConfiguration
operator|.
name|setTemplate
argument_list|(
name|ClassGenerationAction
operator|.
name|SINGLE_CLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setEmbeddableTemplate
argument_list|(
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SINGLE_CLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setQueryTemplate
argument_list|(
name|ClassGenerationAction
operator|.
name|DATAMAP_SINGLE_CLASS_TEMPLATE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cgenConfiguration
operator|.
name|setTemplate
argument_list|(
name|ClassGenerationAction
operator|.
name|SUBCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setEmbeddableTemplate
argument_list|(
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SUBCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setQueryTemplate
argument_list|(
name|ClassGenerationAction
operator|.
name|DATAMAP_SUBCLASS_TEMPLATE
argument_list|)
expr_stmt|;
block|}
name|initForm
argument_list|(
name|cgenConfiguration
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|getParentController
argument_list|()
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|getParentController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getOverwrite
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|val
lambda|->
block|{
name|cgenConfiguration
operator|.
name|setOverwrite
argument_list|(
name|view
operator|.
name|getOverwrite
argument_list|()
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|getParentController
argument_list|()
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|getParentController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getCreatePropertyNames
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|val
lambda|->
block|{
name|cgenConfiguration
operator|.
name|setCreatePropertyNames
argument_list|(
name|view
operator|.
name|getCreatePropertyNames
argument_list|()
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|getParentController
argument_list|()
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|getParentController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getUsePackagePath
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|val
lambda|->
block|{
name|cgenConfiguration
operator|.
name|setUsePkgPath
argument_list|(
name|view
operator|.
name|getUsePackagePath
argument_list|()
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|getParentController
argument_list|()
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|getParentController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getPkProperties
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|val
lambda|->
block|{
name|cgenConfiguration
operator|.
name|setCreatePKProperties
argument_list|(
name|view
operator|.
name|getPkProperties
argument_list|()
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|getParentController
argument_list|()
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|getParentController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initForm
parameter_list|(
name|CgenConfiguration
name|cgenConfiguration
parameter_list|)
block|{
name|super
operator|.
name|initForm
argument_list|(
name|cgenConfiguration
argument_list|)
expr_stmt|;
name|view
operator|.
name|getOutputPattern
argument_list|()
operator|.
name|setText
argument_list|(
name|cgenConfiguration
operator|.
name|getOutputPattern
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|getPairs
argument_list|()
operator|.
name|setSelected
argument_list|(
name|cgenConfiguration
operator|.
name|isMakePairs
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|getUsePackagePath
argument_list|()
operator|.
name|setSelected
argument_list|(
name|cgenConfiguration
operator|.
name|isUsePkgPath
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|getOverwrite
argument_list|()
operator|.
name|setSelected
argument_list|(
name|cgenConfiguration
operator|.
name|isOverwrite
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|getCreatePropertyNames
argument_list|()
operator|.
name|setSelected
argument_list|(
name|cgenConfiguration
operator|.
name|isCreatePropertyNames
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|getPkProperties
argument_list|()
operator|.
name|setSelected
argument_list|(
name|cgenConfiguration
operator|.
name|isCreatePKProperties
argument_list|()
argument_list|)
expr_stmt|;
name|updateComboBoxes
argument_list|()
expr_stmt|;
name|getParentController
argument_list|()
operator|.
name|setInitFromModel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateConfiguration
parameter_list|(
name|CgenConfiguration
name|cgenConfiguration
parameter_list|)
block|{
name|cgenConfiguration
operator|.
name|setClient
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setTemplate
argument_list|(
name|ClassGenerationAction
operator|.
name|SUBCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setSuperTemplate
argument_list|(
name|ClassGenerationAction
operator|.
name|SUPERCLASS_TEMPLATE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

