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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|modeler
operator|.
name|pref
operator|.
name|DataMapDefaults
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|swing
operator|.
name|ObjectBinding
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * A controller for the custom generation mode.  *   */
end_comment

begin_class
specifier|public
class|class
name|CustomModeController
extends|extends
name|GeneratorController
block|{
comment|// correspond to non-public constants on MapClassGenerator.
specifier|static
specifier|final
name|String
name|MODE_DATAMAP
init|=
literal|"datamap"
decl_stmt|;
specifier|static
specifier|final
name|String
name|MODE_ENTITY
init|=
literal|"entity"
decl_stmt|;
specifier|static
specifier|final
name|String
name|MODE_ALL
init|=
literal|"all"
decl_stmt|;
specifier|static
specifier|final
name|String
name|DATA_MAP_MODE_LABEL
init|=
literal|"DataMap generation"
decl_stmt|;
specifier|static
specifier|final
name|String
name|ENTITY_MODE_LABEL
init|=
literal|"Entity and Embeddable generation"
decl_stmt|;
specifier|static
specifier|final
name|String
name|ALL_MODE_LABEL
init|=
literal|"Generate all"
decl_stmt|;
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|modesByLabel
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|modesByLabel
operator|.
name|put
argument_list|(
name|DATA_MAP_MODE_LABEL
argument_list|,
name|MODE_DATAMAP
argument_list|)
expr_stmt|;
name|modesByLabel
operator|.
name|put
argument_list|(
name|ENTITY_MODE_LABEL
argument_list|,
name|MODE_ENTITY
argument_list|)
expr_stmt|;
name|modesByLabel
operator|.
name|put
argument_list|(
name|ALL_MODE_LABEL
argument_list|,
name|MODE_ALL
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|CustomModePanel
name|view
decl_stmt|;
specifier|protected
name|CodeTemplateManager
name|templateManager
decl_stmt|;
specifier|protected
name|ObjectBinding
name|superTemplate
decl_stmt|;
specifier|protected
name|ObjectBinding
name|subTemplate
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
name|Object
index|[]
name|modeChoices
init|=
operator|new
name|Object
index|[]
block|{
name|ENTITY_MODE_LABEL
block|,
name|DATA_MAP_MODE_LABEL
block|,
name|ALL_MODE_LABEL
block|}
decl_stmt|;
name|view
operator|.
name|getGenerationMode
argument_list|()
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|modeChoices
argument_list|)
argument_list|)
expr_stmt|;
comment|// bind preferences and init defaults...
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|preferences
operator|.
name|getSuperclassTemplate
argument_list|()
argument_list|)
condition|)
block|{
name|preferences
operator|.
name|setSuperclassTemplate
argument_list|(
name|CodeTemplateManager
operator|.
name|STANDARD_SERVER_SUPERCLASS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|preferences
operator|.
name|getSubclassTemplate
argument_list|()
argument_list|)
condition|)
block|{
name|preferences
operator|.
name|setSubclassTemplate
argument_list|(
name|CodeTemplateManager
operator|.
name|STANDARD_SERVER_SUBCLASS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|preferences
operator|.
name|getProperty
argument_list|(
literal|"mode"
argument_list|)
argument_list|)
condition|)
block|{
name|preferences
operator|.
name|setProperty
argument_list|(
literal|"mode"
argument_list|,
name|MODE_ENTITY
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|preferences
operator|.
name|getProperty
argument_list|(
literal|"overwrite"
argument_list|)
argument_list|)
condition|)
block|{
name|preferences
operator|.
name|setBooleanProperty
argument_list|(
literal|"overwrite"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|preferences
operator|.
name|getProperty
argument_list|(
literal|"pairs"
argument_list|)
argument_list|)
condition|)
block|{
name|preferences
operator|.
name|setBooleanProperty
argument_list|(
literal|"pairs"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|preferences
operator|.
name|getProperty
argument_list|(
literal|"usePackagePath"
argument_list|)
argument_list|)
condition|)
block|{
name|preferences
operator|.
name|setBooleanProperty
argument_list|(
literal|"usePackagePath"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|preferences
operator|.
name|getProperty
argument_list|(
literal|"outputPattern"
argument_list|)
argument_list|)
condition|)
block|{
name|preferences
operator|.
name|setProperty
argument_list|(
literal|"outputPattern"
argument_list|,
literal|"*.java"
argument_list|)
expr_stmt|;
block|}
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
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getSuperclassPackage
argument_list|()
argument_list|,
literal|"preferences.superclassPackage"
argument_list|)
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|builder
operator|.
name|bindToComboSelection
argument_list|(
name|view
operator|.
name|getGenerationMode
argument_list|()
argument_list|,
literal|"preferences.property['mode']"
argument_list|)
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|builder
operator|.
name|bindToStateChange
argument_list|(
name|view
operator|.
name|getOverwrite
argument_list|()
argument_list|,
literal|"preferences.booleanProperty['overwrite']"
argument_list|)
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|builder
operator|.
name|bindToStateChange
argument_list|(
name|view
operator|.
name|getPairs
argument_list|()
argument_list|,
literal|"preferences.booleanProperty['pairs']"
argument_list|)
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|builder
operator|.
name|bindToStateChange
argument_list|(
name|view
operator|.
name|getUsePackagePath
argument_list|()
argument_list|,
literal|"preferences.booleanProperty['usePackagePath']"
argument_list|)
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|subTemplate
operator|=
name|builder
operator|.
name|bindToComboSelection
argument_list|(
name|view
operator|.
name|getSubclassTemplate
argument_list|()
argument_list|,
literal|"preferences.subclassTemplate"
argument_list|)
expr_stmt|;
name|superTemplate
operator|=
name|builder
operator|.
name|bindToComboSelection
argument_list|(
name|view
operator|.
name|getSuperclassTemplate
argument_list|()
argument_list|,
literal|"preferences.superclassTemplate"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getOutputPattern
argument_list|()
argument_list|,
literal|"preferences.property['outputPattern']"
argument_list|)
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|updateTemplates
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|DataMapDefaults
name|createDefaults
parameter_list|()
block|{
name|Class
name|obt
init|=
name|this
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|DataMapDefaults
name|prefs
init|=
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|getDataMapPreferences
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|"."
argument_list|,
literal|"/"
argument_list|)
argument_list|)
decl_stmt|;
name|prefs
operator|.
name|updateSuperclassPackage
argument_list|(
name|getParentController
argument_list|()
operator|.
name|getDataMap
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|preferences
operator|=
name|prefs
expr_stmt|;
return|return
name|prefs
return|;
block|}
specifier|protected
name|void
name|updateTemplates
parameter_list|()
block|{
name|this
operator|.
name|templateManager
operator|=
name|getApplication
argument_list|()
operator|.
name|getCodeTemplateManager
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|customTemplates
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
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
argument_list|<
name|String
argument_list|>
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
argument_list|<
name|String
argument_list|>
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
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|subTemplates
operator|.
name|toArray
argument_list|()
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
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|superTemplates
operator|.
name|toArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|superTemplate
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|subTemplate
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
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
argument_list|()
expr_stmt|;
return|return
name|view
return|;
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
annotation|@
name|Override
specifier|protected
name|ClassGenerationAction
name|newGenerator
parameter_list|()
block|{
return|return
operator|new
name|ClassGenerationAction
argument_list|()
return|;
block|}
specifier|public
name|ClassGenerationAction
name|createGenerator
parameter_list|()
block|{
name|mode
operator|=
name|modesByLabel
operator|.
name|get
argument_list|(
name|view
operator|.
name|getGenerationMode
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
name|ClassGenerationAction
name|generator
init|=
name|super
operator|.
name|createGenerator
argument_list|()
decl_stmt|;
name|String
name|superKey
init|=
name|view
operator|.
name|getSuperclassTemplate
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|superTemplate
init|=
name|templateManager
operator|.
name|getTemplatePath
argument_list|(
name|superKey
argument_list|)
decl_stmt|;
name|generator
operator|.
name|setSuperTemplate
argument_list|(
name|superTemplate
argument_list|)
expr_stmt|;
name|String
name|subKey
init|=
name|view
operator|.
name|getSubclassTemplate
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|subTemplate
init|=
name|templateManager
operator|.
name|getTemplatePath
argument_list|(
name|subKey
argument_list|)
decl_stmt|;
name|generator
operator|.
name|setTemplate
argument_list|(
name|subTemplate
argument_list|)
expr_stmt|;
name|generator
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
name|generator
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
name|generator
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
name|Util
operator|.
name|isEmptyString
argument_list|(
name|view
operator|.
name|getOutputPattern
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
condition|)
block|{
name|generator
operator|.
name|setOutputPattern
argument_list|(
name|view
operator|.
name|getOutputPattern
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|generator
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
block|}
block|}
end_class

end_unit

