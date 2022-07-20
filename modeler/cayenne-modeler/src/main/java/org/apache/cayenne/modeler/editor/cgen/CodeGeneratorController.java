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
name|JOptionPane
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Files
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
name|Path
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
name|Collection
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|prefs
operator|.
name|Preferences
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|configuration
operator|.
name|event
operator|.
name|DataMapEvent
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
name|configuration
operator|.
name|event
operator|.
name|DataMapListener
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
name|configuration
operator|.
name|xml
operator|.
name|DataChannelMetaData
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
name|gen
operator|.
name|ClassGenerationActionFactory
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
name|map
operator|.
name|Embeddable
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
name|Entity
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
name|ObjEntity
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
name|event
operator|.
name|EmbeddableEvent
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
name|event
operator|.
name|EmbeddableListener
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
name|event
operator|.
name|EntityEvent
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
name|event
operator|.
name|ObjEntityListener
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
name|modeler
operator|.
name|dialog
operator|.
name|pref
operator|.
name|GeneralPreferences
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
name|editor
operator|.
name|DbImportController
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
name|util
operator|.
name|ModelerUtil
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
name|tools
operator|.
name|ToolsInjectorBuilder
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

begin_comment
comment|/**  * Main controller for the code generation UI.  *  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|CodeGeneratorController
extends|extends
name|CayenneController
implements|implements
name|ObjEntityListener
implements|,
name|EmbeddableListener
implements|,
name|DataMapListener
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
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
specifier|protected
specifier|final
name|ProjectController
name|projectController
decl_stmt|;
specifier|protected
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|classes
decl_stmt|;
specifier|protected
specifier|final
name|SelectionModel
name|selectionModel
decl_stmt|;
specifier|protected
specifier|final
name|CodeGeneratorPane
name|view
decl_stmt|;
specifier|protected
specifier|final
name|ClassesTabController
name|classesSelector
decl_stmt|;
specifier|protected
specifier|final
name|GeneratorTabController
name|generatorSelector
decl_stmt|;
specifier|protected
specifier|final
name|ConcurrentMap
argument_list|<
name|DataMap
argument_list|,
name|GeneratorController
argument_list|>
name|prevGeneratorController
decl_stmt|;
specifier|private
name|Object
name|currentClass
decl_stmt|;
specifier|private
name|CgenConfiguration
name|cgenConfiguration
decl_stmt|;
specifier|private
name|boolean
name|initFromModel
decl_stmt|;
specifier|public
name|CodeGeneratorController
parameter_list|(
name|ProjectController
name|projectController
parameter_list|)
block|{
name|super
argument_list|(
name|projectController
argument_list|)
expr_stmt|;
name|this
operator|.
name|classesSelector
operator|=
operator|new
name|ClassesTabController
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|generatorSelector
operator|=
operator|new
name|GeneratorTabController
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|=
operator|new
name|CodeGeneratorPane
argument_list|(
name|generatorSelector
operator|.
name|getView
argument_list|()
argument_list|,
name|classesSelector
operator|.
name|getView
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|prevGeneratorController
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|projectController
operator|=
name|projectController
expr_stmt|;
name|this
operator|.
name|classes
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|selectionModel
operator|=
operator|new
name|SelectionModel
argument_list|()
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
name|initListeners
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|initFromModel
parameter_list|()
block|{
name|initFromModel
operator|=
literal|true
expr_stmt|;
name|DataMap
name|dataMap
init|=
name|projectController
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|prepareClasses
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|createConfiguration
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|GeneratorController
name|modeController
init|=
name|prevGeneratorController
operator|.
name|get
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
if|if
condition|(
name|modeController
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|cgenConfiguration
operator|.
name|isDefault
argument_list|()
condition|)
block|{
name|modeController
operator|=
name|generatorSelector
operator|.
name|getStandartController
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|modeController
operator|=
name|generatorSelector
operator|.
name|getCustomModeController
argument_list|()
expr_stmt|;
block|}
block|}
name|prevGeneratorController
operator|.
name|put
argument_list|(
name|dataMap
argument_list|,
name|modeController
argument_list|)
expr_stmt|;
name|generatorSelector
operator|.
name|setSelectedController
argument_list|(
name|modeController
argument_list|)
expr_stmt|;
name|classesSelector
operator|.
name|startup
argument_list|()
expr_stmt|;
name|initFromModel
operator|=
literal|false
expr_stmt|;
name|classesSelector
operator|.
name|validate
argument_list|(
name|classes
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initListeners
parameter_list|()
block|{
name|projectController
operator|.
name|addObjEntityListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|projectController
operator|.
name|addEmbeddableListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|projectController
operator|.
name|addDataMapListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|CodeGeneratorPane
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
name|getGenerateButton
argument_list|()
argument_list|,
literal|"generateAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|generatorSelector
argument_list|,
literal|"generatorSelectedAction()"
argument_list|,
name|GeneratorTabController
operator|.
name|GENERATOR_PROPERTY
argument_list|)
expr_stmt|;
name|generatorSelectedAction
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|generatorSelectedAction
parameter_list|()
block|{
name|GeneratorController
name|controller
init|=
name|generatorSelector
operator|.
name|getGeneratorController
argument_list|()
decl_stmt|;
name|classesSelector
operator|.
name|validate
argument_list|(
name|classes
argument_list|)
expr_stmt|;
name|Predicate
argument_list|<
name|Object
argument_list|>
name|defaultPredicate
init|=
name|object
lambda|->
block|{
if|if
condition|(
name|object
operator|instanceof
name|ObjEntity
condition|)
block|{
return|return
name|classesSelector
operator|.
name|getProblem
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|==
literal|null
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Embeddable
condition|)
block|{
return|return
name|classesSelector
operator|.
name|getProblem
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|object
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
operator|==
literal|null
return|;
block|}
return|return
literal|false
return|;
block|}
decl_stmt|;
name|Predicate
argument_list|<
name|Object
argument_list|>
name|predicate
init|=
name|controller
operator|!=
literal|null
condition|?
name|defaultPredicate
else|:
name|o
lambda|->
literal|false
decl_stmt|;
name|updateSelection
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
name|classesSelector
operator|.
name|classSelectedAction
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|public
name|void
name|generateAction
parameter_list|()
block|{
name|ClassGenerationAction
name|generator
init|=
operator|new
name|ToolsInjectorBuilder
argument_list|()
operator|.
name|addModule
argument_list|(
name|binder
lambda|->
name|binder
operator|.
name|bind
argument_list|(
name|DataChannelMetaData
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
argument_list|)
argument_list|)
operator|.
name|create
argument_list|()
operator|.
name|getInstance
argument_list|(
name|ClassGenerationActionFactory
operator|.
name|class
argument_list|)
operator|.
name|createAction
argument_list|(
name|cgenConfiguration
argument_list|)
decl_stmt|;
try|try
block|{
name|generator
operator|.
name|prepareArtifacts
argument_list|()
expr_stmt|;
name|generator
operator|.
name|execute
argument_list|()
expr_stmt|;
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
literal|"Class generation finished"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error generating classes"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
literal|"Error generating classes - "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ConcurrentMap
argument_list|<
name|DataMap
argument_list|,
name|GeneratorController
argument_list|>
name|getPrevGeneratorController
parameter_list|()
block|{
return|return
name|prevGeneratorController
return|;
block|}
specifier|public
name|void
name|enableGenerateButton
parameter_list|(
name|boolean
name|enable
parameter_list|)
block|{
name|view
operator|.
name|getGenerateButton
argument_list|()
operator|.
name|setEnabled
argument_list|(
name|enable
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|prepareClasses
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|classes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|classes
operator|.
name|addAll
argument_list|(
name|dataMap
operator|.
name|getObjEntities
argument_list|()
argument_list|)
expr_stmt|;
name|classes
operator|.
name|addAll
argument_list|(
name|dataMap
operator|.
name|getEmbeddables
argument_list|()
argument_list|)
expr_stmt|;
name|selectionModel
operator|.
name|initCollectionsForSelection
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a class generator for provided selections.      */
specifier|public
name|void
name|createConfiguration
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|cgenConfiguration
operator|=
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
operator|.
name|get
argument_list|(
name|map
argument_list|,
name|CgenConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|cgenConfiguration
operator|!=
literal|null
condition|)
block|{
name|addToSelectedEntities
argument_list|(
name|cgenConfiguration
operator|.
name|getEntities
argument_list|()
argument_list|)
expr_stmt|;
name|addToSelectedEmbeddables
argument_list|(
name|cgenConfiguration
operator|.
name|getEmbeddables
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setForce
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return;
block|}
name|cgenConfiguration
operator|=
operator|new
name|CgenConfiguration
argument_list|()
expr_stmt|;
name|cgenConfiguration
operator|.
name|setForce
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|Path
name|basePath
init|=
name|Paths
operator|.
name|get
argument_list|(
name|ModelerUtil
operator|.
name|initOutputFolder
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO: this should be done in actual generation, not here
comment|// no such folder
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
try|try
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|getView
argument_list|()
argument_list|,
literal|"Can't create directory. Select a different one."
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|// not a directory
if|if
condition|(
operator|!
name|Files
operator|.
name|isDirectory
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
name|basePath
operator|+
literal|" is not a valid directory."
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|map
operator|.
name|getLocation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|setRootPath
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
block|}
name|Preferences
name|preferences
init|=
name|application
operator|.
name|getPreferencesNode
argument_list|(
name|GeneralPreferences
operator|.
name|class
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|preferences
operator|!=
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|setEncoding
argument_list|(
name|preferences
operator|.
name|get
argument_list|(
name|GeneralPreferences
operator|.
name|ENCODING_PREFERENCE
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|addToSelectedEntities
argument_list|(
name|map
operator|.
name|getObjEntities
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Entity
operator|::
name|getName
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|addToSelectedEmbeddables
argument_list|(
name|map
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Embeddable
operator|::
name|getClassName
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getClasses
parameter_list|()
block|{
return|return
name|classes
return|;
block|}
specifier|public
name|boolean
name|updateSelection
parameter_list|(
name|Predicate
argument_list|<
name|Object
argument_list|>
name|predicate
parameter_list|)
block|{
name|boolean
name|modified
init|=
name|selectionModel
operator|.
name|updateSelection
argument_list|(
name|predicate
argument_list|,
name|classes
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|classObj
range|:
name|classes
control|)
block|{
if|if
condition|(
name|classObj
operator|instanceof
name|DataMap
condition|)
block|{
name|boolean
name|selected
init|=
name|predicate
operator|.
name|test
argument_list|(
name|classObj
argument_list|)
decl_stmt|;
name|updateArtifactGenerationMode
argument_list|(
name|selected
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|modified
return|;
block|}
specifier|private
name|void
name|updateArtifactGenerationMode
parameter_list|(
name|boolean
name|selected
parameter_list|)
block|{
if|if
condition|(
name|selected
condition|)
block|{
name|cgenConfiguration
operator|.
name|setArtifactsGenerationMode
argument_list|(
literal|"all"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cgenConfiguration
operator|.
name|setArtifactsGenerationMode
argument_list|(
literal|"entity"
argument_list|)
expr_stmt|;
block|}
name|checkCgenConfigDirty
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSelected
parameter_list|()
block|{
return|return
name|selectionModel
operator|.
name|isSelected
argument_list|(
name|currentClass
argument_list|)
return|;
block|}
specifier|public
name|void
name|setSelected
parameter_list|(
name|boolean
name|selectedFlag
parameter_list|)
block|{
if|if
condition|(
name|currentClass
operator|instanceof
name|DataMap
condition|)
block|{
name|updateArtifactGenerationMode
argument_list|(
name|selectedFlag
argument_list|)
expr_stmt|;
block|}
name|selectionModel
operator|.
name|setSelected
argument_list|(
name|currentClass
argument_list|,
name|selectedFlag
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setCurrentClass
parameter_list|(
name|Object
name|currentClass
parameter_list|)
block|{
name|this
operator|.
name|currentClass
operator|=
name|currentClass
expr_stmt|;
block|}
specifier|public
name|void
name|updateSelectedEntities
parameter_list|()
block|{
name|updateEntities
argument_list|()
expr_stmt|;
name|updateEmbeddables
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|checkCgenConfigDirty
parameter_list|()
block|{
if|if
condition|(
name|initFromModel
operator|||
name|cgenConfiguration
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|DataMap
name|map
init|=
name|projectController
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|CgenConfiguration
name|existingConfig
init|=
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
operator|.
name|get
argument_list|(
name|map
argument_list|,
name|CgenConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingConfig
operator|==
literal|null
condition|)
block|{
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
operator|.
name|add
argument_list|(
name|map
argument_list|,
name|cgenConfiguration
argument_list|)
expr_stmt|;
block|}
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|updateEntities
parameter_list|()
block|{
if|if
condition|(
name|cgenConfiguration
operator|!=
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|getEntities
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|ObjEntity
name|entity
range|:
name|selectionModel
operator|.
name|getSelectedEntities
argument_list|(
name|classes
argument_list|)
control|)
block|{
name|cgenConfiguration
operator|.
name|loadEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
name|checkCgenConfigDirty
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|updateEmbeddables
parameter_list|()
block|{
if|if
condition|(
name|cgenConfiguration
operator|!=
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|Embeddable
name|embeddable
range|:
name|selectionModel
operator|.
name|getSelectedEmbeddables
argument_list|(
name|classes
argument_list|)
control|)
block|{
name|cgenConfiguration
operator|.
name|loadEmbeddable
argument_list|(
name|embeddable
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|checkCgenConfigDirty
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|addToSelectedEntities
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|entities
parameter_list|)
block|{
name|selectionModel
operator|.
name|addSelectedEntities
argument_list|(
name|entities
argument_list|)
expr_stmt|;
name|updateEntities
argument_list|()
expr_stmt|;
block|}
name|void
name|addEntity
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|ObjEntity
name|objEntity
parameter_list|)
block|{
name|prepareClasses
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|selectionModel
operator|.
name|addSelectedEntity
argument_list|(
name|objEntity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cgenConfiguration
operator|!=
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|loadEntity
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
block|}
name|checkCgenConfigDirty
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|addToSelectedEmbeddables
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|embeddables
parameter_list|)
block|{
name|selectionModel
operator|.
name|addSelectedEmbeddables
argument_list|(
name|embeddables
argument_list|)
expr_stmt|;
name|updateEmbeddables
argument_list|()
expr_stmt|;
block|}
specifier|public
name|int
name|getSelectedEntitiesSize
parameter_list|()
block|{
return|return
name|selectionModel
operator|.
name|getSelectedEntitiesCount
argument_list|()
return|;
block|}
specifier|public
name|int
name|getSelectedEmbeddablesSize
parameter_list|()
block|{
return|return
name|selectionModel
operator|.
name|getSelecetedEmbeddablesCount
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isDataMapSelected
parameter_list|()
block|{
return|return
name|selectionModel
operator|.
name|getSelectedDataMapsCount
argument_list|()
operator|>
literal|0
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
specifier|public
name|boolean
name|isInitFromModel
parameter_list|()
block|{
return|return
name|initFromModel
return|;
block|}
specifier|public
name|void
name|setInitFromModel
parameter_list|(
name|boolean
name|initFromModel
parameter_list|)
block|{
name|this
operator|.
name|initFromModel
operator|=
name|initFromModel
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|objEntityChanged
parameter_list|(
name|EntityEvent
name|e
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|objEntityAdded
parameter_list|(
name|EntityEvent
name|e
parameter_list|)
block|{
name|addEntity
argument_list|(
name|e
operator|.
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
argument_list|,
operator|(
name|ObjEntity
operator|)
name|e
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|objEntityRemoved
parameter_list|(
name|EntityEvent
name|e
parameter_list|)
block|{
name|selectionModel
operator|.
name|removeFromSelectedEntities
argument_list|(
operator|(
name|ObjEntity
operator|)
name|e
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cgenConfiguration
operator|!=
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|getEntities
argument_list|()
operator|.
name|remove
argument_list|(
name|e
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|checkCgenConfigDirty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|embeddableChanged
parameter_list|(
name|EmbeddableEvent
name|e
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|embeddableAdded
parameter_list|(
name|EmbeddableEvent
name|e
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
name|prepareClasses
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|String
name|embeddableClassName
init|=
name|e
operator|.
name|getEmbeddable
argument_list|()
operator|.
name|getClassName
argument_list|()
decl_stmt|;
name|selectionModel
operator|.
name|addSelectedEmbeddable
argument_list|(
name|embeddableClassName
argument_list|)
expr_stmt|;
if|if
condition|(
name|cgenConfiguration
operator|!=
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|loadEmbeddable
argument_list|(
name|embeddableClassName
argument_list|)
expr_stmt|;
block|}
name|checkCgenConfigDirty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|embeddableRemoved
parameter_list|(
name|EmbeddableEvent
name|e
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
name|selectionModel
operator|.
name|removeFromSelectedEmbeddables
argument_list|(
name|e
operator|.
name|getEmbeddable
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cgenConfiguration
operator|!=
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|remove
argument_list|(
name|e
operator|.
name|getEmbeddable
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|checkCgenConfigDirty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|dataMapChanged
parameter_list|(
name|DataMapEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getSource
argument_list|()
operator|instanceof
name|DbImportController
condition|)
block|{
if|if
condition|(
name|cgenConfiguration
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ObjEntity
name|objEntity
range|:
name|e
operator|.
name|getDataMap
argument_list|()
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|cgenConfiguration
operator|.
name|getExcludeEntityArtifacts
argument_list|()
operator|.
name|contains
argument_list|(
name|objEntity
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|addEntity
argument_list|(
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|objEntity
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|checkCgenConfigDirty
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|dataMapAdded
parameter_list|(
name|DataMapEvent
name|e
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|dataMapRemoved
parameter_list|(
name|DataMapEvent
name|e
parameter_list|)
block|{
block|}
specifier|public
name|CgenConfiguration
name|getCgenConfiguration
parameter_list|()
block|{
return|return
name|cgenConfiguration
return|;
block|}
block|}
end_class

end_unit

