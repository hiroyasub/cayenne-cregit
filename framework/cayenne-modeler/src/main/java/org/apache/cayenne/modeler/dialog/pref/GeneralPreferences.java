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
name|pref
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
name|util
operator|.
name|CayenneController
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
name|pref
operator|.
name|CayennePreferenceEditor
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
name|pref
operator|.
name|CayennePreferenceService
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
name|pref
operator|.
name|Domain
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
name|pref
operator|.
name|PreferenceDetail
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
name|pref
operator|.
name|PreferenceEditor
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
name|validation
operator|.
name|ValidationException
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|GeneralPreferences
extends|extends
name|CayenneController
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DELETE_PROMPT_PREFERENCE
init|=
literal|"deletePrompt"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ENCODING_PREFERENCE
init|=
literal|"encoding"
decl_stmt|;
specifier|protected
name|GeneralPreferencesView
name|view
decl_stmt|;
specifier|protected
name|CayennePreferenceEditor
name|editor
decl_stmt|;
specifier|protected
name|PreferenceDetail
name|classGeneratorPreferences
decl_stmt|;
specifier|protected
name|PreferenceDetail
name|deletePromptPreference
decl_stmt|;
specifier|protected
name|ObjectBinding
name|saveIntervalBinding
decl_stmt|;
specifier|protected
name|ObjectBinding
name|encodingBinding
decl_stmt|;
specifier|protected
name|ObjectBinding
name|deletePromptBinding
decl_stmt|;
specifier|public
name|GeneralPreferences
parameter_list|(
name|PreferenceDialog
name|parentController
parameter_list|)
block|{
name|super
argument_list|(
name|parentController
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|=
operator|new
name|GeneralPreferencesView
argument_list|()
expr_stmt|;
name|PreferenceEditor
name|editor
init|=
name|parentController
operator|.
name|getEditor
argument_list|()
decl_stmt|;
if|if
condition|(
name|editor
operator|instanceof
name|CayennePreferenceEditor
condition|)
block|{
name|this
operator|.
name|editor
operator|=
operator|(
name|CayennePreferenceEditor
operator|)
name|editor
expr_stmt|;
name|this
operator|.
name|view
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
name|saveIntervalBinding
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|encodingBinding
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|deletePromptBinding
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|view
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
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
specifier|protected
name|void
name|initBindings
parameter_list|()
block|{
comment|// init model objects
name|Domain
name|classGeneratorDomain
init|=
name|editor
operator|.
name|editableInstance
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getPreferenceDomain
argument_list|()
argument_list|)
operator|.
name|getSubdomain
argument_list|(
name|ClassGenerationAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|this
operator|.
name|classGeneratorPreferences
operator|=
name|classGeneratorDomain
operator|.
name|getDetail
argument_list|(
name|ENCODING_PREFERENCE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|deletePromptPreference
operator|=
name|editor
operator|.
name|editableInstance
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getPreferenceDomain
argument_list|()
argument_list|)
operator|.
name|getDetail
argument_list|(
name|DELETE_PROMPT_PREFERENCE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// build child controllers...
name|EncodingSelector
name|encodingSelector
init|=
operator|new
name|EncodingSelector
argument_list|(
name|this
argument_list|,
name|view
operator|.
name|getEncodingSelector
argument_list|()
argument_list|)
decl_stmt|;
comment|// create bindings...
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
name|this
operator|.
name|saveIntervalBinding
operator|=
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getSaveInterval
argument_list|()
argument_list|,
literal|"timeInterval"
argument_list|)
expr_stmt|;
name|this
operator|.
name|encodingBinding
operator|=
name|builder
operator|.
name|bindToProperty
argument_list|(
name|encodingSelector
argument_list|,
literal|"classGeneratorPreferences.property[\"encoding\"]"
argument_list|,
name|EncodingSelector
operator|.
name|ENCODING_PROPERTY_BINDING
argument_list|)
expr_stmt|;
name|this
operator|.
name|deletePromptBinding
operator|=
name|builder
operator|.
name|bindToCheckBox
argument_list|(
name|view
operator|.
name|getDeletePrompt
argument_list|()
argument_list|,
literal|"deletePrompt"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|double
name|getTimeInterval
parameter_list|()
block|{
return|return
name|this
operator|.
name|editor
operator|.
name|getSaveInterval
argument_list|()
operator|/
literal|1000.0
return|;
block|}
specifier|public
name|void
name|setTimeInterval
parameter_list|(
name|double
name|d
parameter_list|)
block|{
name|int
name|ms
init|=
operator|(
name|int
operator|)
operator|(
name|d
operator|*
literal|1000.0
operator|)
decl_stmt|;
if|if
condition|(
name|ms
operator|<
name|CayennePreferenceService
operator|.
name|MIN_SAVE_INTERVAL
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Time interval is too small, minimum allowed is "
operator|+
operator|(
name|CayennePreferenceService
operator|.
name|MIN_SAVE_INTERVAL
operator|/
literal|1000.0
operator|)
argument_list|)
throw|;
block|}
name|this
operator|.
name|editor
operator|.
name|setSaveInterval
argument_list|(
name|ms
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getDeletePrompt
parameter_list|()
block|{
return|return
name|deletePromptPreference
operator|.
name|getBooleanProperty
argument_list|(
name|GeneralPreferences
operator|.
name|DELETE_PROMPT_PREFERENCE
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDeletePrompt
parameter_list|(
name|boolean
name|deletePrompt
parameter_list|)
block|{
name|deletePromptPreference
operator|.
name|setBooleanProperty
argument_list|(
name|GeneralPreferences
operator|.
name|DELETE_PROMPT_PREFERENCE
argument_list|,
name|deletePrompt
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PreferenceDetail
name|getClassGeneratorPreferences
parameter_list|()
block|{
return|return
name|classGeneratorPreferences
return|;
block|}
block|}
end_class

end_unit

