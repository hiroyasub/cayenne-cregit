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

begin_comment
comment|/**  * A controller for the custom generation mode.  */
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
specifier|private
name|void
name|updateComboBoxes
parameter_list|()
block|{
name|view
operator|.
name|getSubclassTemplate
argument_list|()
operator|.
name|setItem
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getCodeTemplateManager
argument_list|()
operator|.
name|getNameByPath
argument_list|(
name|classGenerationAction
operator|.
name|getTemplate
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|view
operator|.
name|getSuperclassTemplate
argument_list|()
operator|.
name|setItem
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getCodeTemplateManager
argument_list|()
operator|.
name|getNameByPath
argument_list|(
name|classGenerationAction
operator|.
name|getSuperclassTemplate
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
specifier|protected
name|ClassGenerationAction
name|newGenerator
parameter_list|()
block|{
name|ClassGenerationAction
name|action
init|=
operator|new
name|ClassGenerationAction
argument_list|()
decl_stmt|;
name|getApplication
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|injectMembers
argument_list|(
name|action
argument_list|)
expr_stmt|;
return|return
name|action
return|;
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
name|classGenerationAction
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
name|classGenerationAction
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
name|classGenerationAction
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
name|classGenerationAction
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
name|classGenerationAction
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
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initForm
parameter_list|(
name|ClassGenerationAction
name|classGenerationAction
parameter_list|)
block|{
name|super
operator|.
name|initForm
argument_list|(
name|classGenerationAction
argument_list|)
expr_stmt|;
name|view
operator|.
name|getOutputPattern
argument_list|()
operator|.
name|setText
argument_list|(
name|classGenerationAction
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
name|classGenerationAction
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
name|classGenerationAction
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
name|classGenerationAction
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
name|classGenerationAction
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
name|classGenerationAction
operator|.
name|isCreatePKProperties
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|classGenerationAction
operator|.
name|getArtifactsGenerationMode
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"all"
argument_list|)
condition|)
block|{
operator|(
operator|(
name|CodeGeneratorControllerBase
operator|)
name|parent
operator|)
operator|.
name|setCurrentClass
argument_list|(
name|classGenerationAction
operator|.
name|getDataMap
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|CodeGeneratorControllerBase
operator|)
name|parent
operator|)
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|updateComboBoxes
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

