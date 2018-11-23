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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|DataMap
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
name|dialog
operator|.
name|ErrorDebugDialog
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
name|project
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ItemEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|GeneratorsTabController
block|{
specifier|public
specifier|static
name|Logger
name|logObj
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ErrorDebugDialog
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|ProjectController
name|projectController
decl_stmt|;
specifier|public
name|GeneratorsTab
name|view
decl_stmt|;
specifier|private
name|Class
name|type
decl_stmt|;
specifier|public
name|ConcurrentMap
argument_list|<
name|DataMap
argument_list|,
name|GeneratorsPanel
argument_list|>
name|generatorsPanels
decl_stmt|;
specifier|public
name|Set
argument_list|<
name|DataMap
argument_list|>
name|selectedDataMaps
decl_stmt|;
specifier|public
name|GeneratorsTabController
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|generatorsPanels
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|selectedDataMaps
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|icon
decl_stmt|;
specifier|public
specifier|abstract
name|void
name|runGenerators
parameter_list|(
name|Set
argument_list|<
name|DataMap
argument_list|>
name|dataMaps
parameter_list|)
function_decl|;
specifier|public
name|void
name|createPanels
parameter_list|()
block|{
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|dataMaps
init|=
name|getDataMaps
argument_list|()
decl_stmt|;
name|generatorsPanels
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|DataMap
name|dataMap
range|:
name|dataMaps
control|)
block|{
name|GeneratorsPanel
name|generatorPanel
init|=
operator|new
name|GeneratorsPanel
argument_list|(
name|dataMap
argument_list|,
literal|"icon-datamap.png"
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|initListenersForPanel
argument_list|(
name|generatorPanel
argument_list|)
expr_stmt|;
name|generatorsPanels
operator|.
name|put
argument_list|(
name|dataMap
argument_list|,
name|generatorPanel
argument_list|)
expr_stmt|;
block|}
name|selectedDataMaps
operator|.
name|forEach
argument_list|(
name|dataMap
lambda|->
block|{
if|if
condition|(
name|generatorsPanels
operator|.
name|get
argument_list|(
name|dataMap
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|GeneratorsPanel
name|currPanel
init|=
name|generatorsPanels
operator|.
name|get
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
name|currPanel
operator|.
name|getCheckConfig
argument_list|()
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initListenersForPanel
parameter_list|(
name|GeneratorsPanel
name|panel
parameter_list|)
block|{
name|panel
operator|.
name|getCheckConfig
argument_list|()
operator|.
name|addItemListener
argument_list|(
name|e
lambda|->
block|{
if|if
condition|(
name|e
operator|.
name|getStateChange
argument_list|()
operator|==
name|ItemEvent
operator|.
name|SELECTED
condition|)
block|{
name|selectedDataMaps
operator|.
name|add
argument_list|(
name|panel
operator|.
name|getDataMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|e
operator|.
name|getStateChange
argument_list|()
operator|==
name|ItemEvent
operator|.
name|DESELECTED
condition|)
block|{
name|selectedDataMaps
operator|.
name|remove
argument_list|(
name|panel
operator|.
name|getDataMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setGenerateButtonDisabled
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|panel
operator|.
name|getToConfigButton
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|action
lambda|->
name|showConfig
argument_list|(
name|panel
operator|.
name|getDataMap
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|view
operator|.
name|getGenerationPanel
argument_list|()
operator|.
name|getSelectAll
argument_list|()
operator|.
name|addItemListener
argument_list|(
name|e
lambda|->
block|{
if|if
condition|(
name|e
operator|.
name|getStateChange
argument_list|()
operator|==
name|ItemEvent
operator|.
name|SELECTED
condition|)
block|{
name|getGeneratorsPanels
argument_list|()
operator|.
name|forEach
argument_list|(
parameter_list|(
name|key
parameter_list|,
name|value
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|value
operator|.
name|getCheckConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
condition|)
block|{
name|value
operator|.
name|getCheckConfig
argument_list|()
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|e
operator|.
name|getStateChange
argument_list|()
operator|==
name|ItemEvent
operator|.
name|DESELECTED
condition|)
block|{
name|getGeneratorsPanels
argument_list|()
operator|.
name|forEach
argument_list|(
parameter_list|(
name|key
parameter_list|,
name|value
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|value
operator|.
name|getCheckConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
condition|)
block|{
name|value
operator|.
name|getCheckConfig
argument_list|()
operator|.
name|setSelected
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|setGenerateButtonDisabled
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|void
name|showConfig
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
function_decl|;
specifier|private
name|void
name|setGenerateButtonDisabled
parameter_list|()
block|{
if|if
condition|(
name|selectedDataMaps
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|view
operator|.
name|getGenerationPanel
argument_list|()
operator|.
name|getGenerateAll
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|view
operator|.
name|getGenerationPanel
argument_list|()
operator|.
name|getGenerateAll
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|getDataMaps
parameter_list|()
block|{
name|Project
name|project
init|=
name|projectController
operator|.
name|getProject
argument_list|()
decl_stmt|;
return|return
operator|(
operator|(
name|DataChannelDescriptor
operator|)
name|project
operator|.
name|getRootNode
argument_list|()
operator|)
operator|.
name|getDataMaps
argument_list|()
return|;
block|}
specifier|public
name|GeneratorsTab
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|public
name|ProjectController
name|getProjectController
parameter_list|()
block|{
return|return
name|projectController
return|;
block|}
name|ConcurrentMap
argument_list|<
name|DataMap
argument_list|,
name|GeneratorsPanel
argument_list|>
name|getGeneratorsPanels
parameter_list|()
block|{
return|return
name|generatorsPanels
return|;
block|}
name|Set
argument_list|<
name|DataMap
argument_list|>
name|getSelectedDataMaps
parameter_list|()
block|{
return|return
name|selectedDataMaps
return|;
block|}
block|}
end_class

end_unit

