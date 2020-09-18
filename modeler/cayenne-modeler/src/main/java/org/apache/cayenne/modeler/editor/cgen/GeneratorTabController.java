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
name|pref
operator|.
name|PreferenceDetail
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
name|Map
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|GeneratorTabController
extends|extends
name|CayenneController
block|{
specifier|private
specifier|static
specifier|final
name|String
name|STANDARD_OBJECTS_MODE
init|=
literal|"Standard Persistent Objects"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_OBJECTS_MODE
init|=
literal|"Client Persistent Objects"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ADVANCED_MODE
init|=
literal|"Advanced"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GENERATOR_PROPERTY
init|=
literal|"generator"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|GENERATION_MODES
init|=
operator|new
name|String
index|[]
block|{
name|STANDARD_OBJECTS_MODE
block|,
name|CLIENT_OBJECTS_MODE
block|,
name|ADVANCED_MODE
block|}
decl_stmt|;
specifier|protected
name|GeneratorTabPanel
name|view
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|GeneratorController
argument_list|>
name|controllers
decl_stmt|;
specifier|protected
name|PreferenceDetail
name|preferences
decl_stmt|;
specifier|public
name|GeneratorTabController
parameter_list|(
name|CodeGeneratorController
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|controllers
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|controllers
operator|.
name|put
argument_list|(
name|STANDARD_OBJECTS_MODE
argument_list|,
operator|new
name|StandardModeController
argument_list|(
name|parent
argument_list|)
argument_list|)
expr_stmt|;
name|controllers
operator|.
name|put
argument_list|(
name|CLIENT_OBJECTS_MODE
argument_list|,
operator|new
name|ClientModeController
argument_list|(
name|parent
argument_list|)
argument_list|)
expr_stmt|;
name|controllers
operator|.
name|put
argument_list|(
name|ADVANCED_MODE
argument_list|,
operator|new
name|CustomModeController
argument_list|(
name|parent
argument_list|)
argument_list|)
expr_stmt|;
name|Component
index|[]
name|modePanels
init|=
operator|new
name|Component
index|[
name|GENERATION_MODES
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|GENERATION_MODES
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|modePanels
index|[
name|i
index|]
operator|=
name|controllers
operator|.
name|get
argument_list|(
name|GENERATION_MODES
index|[
name|i
index|]
argument_list|)
operator|.
name|getView
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|view
operator|=
operator|new
name|GeneratorTabPanel
argument_list|(
name|GENERATION_MODES
argument_list|,
name|modePanels
argument_list|)
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
name|view
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|550
argument_list|,
literal|480
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|GeneratorTabPanel
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|protected
name|CodeGeneratorController
name|getParentController
parameter_list|()
block|{
return|return
operator|(
name|CodeGeneratorController
operator|)
name|getParent
argument_list|()
return|;
block|}
specifier|public
name|PreferenceDetail
name|getPreferences
parameter_list|()
block|{
return|return
name|preferences
return|;
block|}
specifier|protected
name|void
name|initBindings
parameter_list|()
block|{
name|view
operator|.
name|getGenerationMode
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|action
lambda|->
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|view
operator|.
name|getGenerationMode
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
name|GeneratorController
name|modeController
init|=
name|getGeneratorController
argument_list|()
decl_stmt|;
name|CgenConfiguration
name|cgenConfiguration
init|=
name|getParentController
argument_list|()
operator|.
name|getCgenConfiguration
argument_list|()
decl_stmt|;
name|modeController
operator|.
name|updateConfiguration
argument_list|(
name|cgenConfiguration
argument_list|)
expr_stmt|;
name|controllers
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|.
name|initForm
argument_list|(
name|cgenConfiguration
argument_list|)
expr_stmt|;
name|getParentController
argument_list|()
operator|.
name|getPrevGeneratorController
argument_list|()
operator|.
name|put
argument_list|(
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|modeController
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSelectedController
parameter_list|(
name|GeneratorController
name|generatorController
parameter_list|)
block|{
for|for
control|(
name|String
name|key
range|:
name|controllers
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|generatorController
operator|.
name|equals
argument_list|(
name|controllers
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
condition|)
block|{
name|getView
argument_list|()
operator|.
name|getGenerationMode
argument_list|()
operator|.
name|setSelectedItem
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|GeneratorController
name|getGeneratorController
parameter_list|()
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|view
operator|.
name|getGenerationMode
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
return|return
name|controllers
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
name|GeneratorController
name|getStandartController
parameter_list|()
block|{
return|return
name|controllers
operator|.
name|get
argument_list|(
name|STANDARD_OBJECTS_MODE
argument_list|)
return|;
block|}
name|GeneratorController
name|getCustomModeController
parameter_list|()
block|{
return|return
name|controllers
operator|.
name|get
argument_list|(
name|ADVANCED_MODE
argument_list|)
return|;
block|}
name|GeneratorController
name|getClientGeneratorController
parameter_list|()
block|{
return|return
name|controllers
operator|.
name|get
argument_list|(
name|CLIENT_OBJECTS_MODE
argument_list|)
return|;
block|}
block|}
end_class

end_unit

