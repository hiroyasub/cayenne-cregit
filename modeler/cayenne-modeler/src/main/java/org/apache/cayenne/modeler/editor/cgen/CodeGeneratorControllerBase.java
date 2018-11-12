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
name|CellRenderers
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
name|validation
operator|.
name|ValidationFailure
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
name|ValidationResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|Icon
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
name|JOptionPane
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
name|HashMap
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
name|Map
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
name|List
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

begin_comment
comment|/**  * @since 4.1  * A base superclass of a top controller for the code generator. Defines all common model  * parts used in class generation.  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CodeGeneratorControllerBase
extends|extends
name|CayenneController
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SELECTED_PROPERTY
init|=
literal|"selected"
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
name|ValidationResult
name|validation
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|classes
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|selectedEntities
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|selectedEmbeddables
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|isDataMapSelected
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|DataMap
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|selectedEntitiesForDataMap
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|DataMap
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|selectedEmbeddablesForDataMap
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|DataMap
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|selectedDataMaps
decl_stmt|;
specifier|protected
specifier|transient
name|Object
name|currentClass
decl_stmt|;
specifier|protected
name|ProjectController
name|projectController
decl_stmt|;
specifier|protected
name|boolean
name|initFromModel
decl_stmt|;
specifier|public
name|CodeGeneratorControllerBase
parameter_list|(
name|CayenneController
name|parent
parameter_list|,
name|ProjectController
name|projectController
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
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
name|selectedEntitiesForDataMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|selectedEmbeddablesForDataMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|selectedDataMaps
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|startup
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|initFromModel
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
name|prepareClasses
argument_list|(
name|dataMap
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
name|this
operator|.
name|classes
operator|.
name|add
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|this
operator|.
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
name|this
operator|.
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
name|initCollectionsForSelection
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initCollectionsForSelection
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|selectedEntities
operator|=
name|selectedEntitiesForDataMap
operator|.
name|compute
argument_list|(
name|dataMap
argument_list|,
operator|(
name|key
operator|,
name|value
operator|)
operator|->
name|value
operator|==
literal|null
condition|?
operator|new
name|HashSet
argument_list|<>
argument_list|()
else|:
name|value
argument_list|)
expr_stmt|;
name|selectedEmbeddables
operator|=
name|selectedEmbeddablesForDataMap
operator|.
name|compute
argument_list|(
name|dataMap
argument_list|,
operator|(
name|key
operator|,
name|value
operator|)
operator|->
name|value
operator|==
literal|null
condition|?
operator|new
name|HashSet
argument_list|<>
argument_list|()
else|:
name|value
argument_list|)
expr_stmt|;
name|isDataMapSelected
operator|=
name|selectedDataMaps
operator|.
name|compute
argument_list|(
name|dataMap
argument_list|,
operator|(
name|key
operator|,
name|value
operator|)
operator|->
name|value
operator|==
literal|null
condition|?
operator|new
name|HashSet
argument_list|<>
argument_list|()
else|:
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a class generator for provided selections.      */
specifier|public
name|CgenConfiguration
name|createConfiguration
parameter_list|()
block|{
name|DataMap
name|map
init|=
name|projectController
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|CgenConfiguration
name|cgenConfiguration
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
name|cgenConfiguration
operator|!=
literal|null
condition|)
block|{
name|addToSelectedEntities
argument_list|(
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
argument_list|,
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
name|getDataMap
argument_list|()
argument_list|,
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
return|return
name|cgenConfiguration
return|;
block|}
try|try
block|{
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
comment|// no destination folder
if|if
condition|(
name|basePath
operator|==
literal|null
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
literal|"Select directory for source files."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
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
name|Files
operator|.
name|createDirectories
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
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
return|return
literal|null
return|;
block|}
name|cgenConfiguration
operator|.
name|setRootPath
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
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
argument_list|,
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
argument_list|,
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
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
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
literal|"Can't create directory. "
operator|+
literal|". Select a different one."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|cgenConfiguration
return|;
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
specifier|abstract
name|Component
name|getView
parameter_list|()
function_decl|;
specifier|public
name|void
name|validate
parameter_list|(
name|GeneratorController
name|validator
parameter_list|)
block|{
name|ValidationResult
name|validationBuffer
init|=
operator|new
name|ValidationResult
argument_list|()
decl_stmt|;
if|if
condition|(
name|validator
operator|!=
literal|null
condition|)
block|{
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
name|ObjEntity
condition|)
block|{
name|validator
operator|.
name|validateEntity
argument_list|(
name|validationBuffer
argument_list|,
operator|(
name|ObjEntity
operator|)
name|classObj
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|classObj
operator|instanceof
name|Embeddable
condition|)
block|{
name|validator
operator|.
name|validateEmbeddable
argument_list|(
name|validationBuffer
argument_list|,
operator|(
name|Embeddable
operator|)
name|classObj
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|this
operator|.
name|validation
operator|=
name|validationBuffer
expr_stmt|;
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
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|classObj
range|:
name|classes
control|)
block|{
name|boolean
name|select
init|=
name|predicate
operator|.
name|test
argument_list|(
name|classObj
argument_list|)
decl_stmt|;
if|if
condition|(
name|classObj
operator|instanceof
name|ObjEntity
condition|)
block|{
if|if
condition|(
name|select
condition|)
block|{
if|if
condition|(
name|selectedEntities
operator|.
name|add
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|classObj
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|modified
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|selectedEntities
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|classObj
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|modified
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|else if
condition|(
name|classObj
operator|instanceof
name|Embeddable
condition|)
block|{
if|if
condition|(
name|select
condition|)
block|{
if|if
condition|(
name|selectedEmbeddables
operator|.
name|add
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|classObj
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|modified
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|selectedEmbeddables
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|classObj
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|modified
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|else if
condition|(
name|classObj
operator|instanceof
name|DataMap
condition|)
block|{
name|updateArtifactGenerationMode
argument_list|(
name|classObj
argument_list|,
name|select
argument_list|)
expr_stmt|;
if|if
condition|(
name|select
condition|)
block|{
if|if
condition|(
name|isDataMapSelected
operator|.
name|add
argument_list|(
operator|(
operator|(
name|DataMap
operator|)
name|classObj
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|modified
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|isDataMapSelected
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|DataMap
operator|)
name|classObj
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|modified
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|modified
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|modified
return|;
block|}
specifier|public
name|List
argument_list|<
name|Embeddable
argument_list|>
name|getSelectedEmbeddables
parameter_list|()
block|{
name|List
argument_list|<
name|Embeddable
argument_list|>
name|selected
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|selectedEmbeddables
operator|.
name|size
argument_list|()
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
name|Embeddable
operator|&&
name|selectedEmbeddables
operator|.
name|contains
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|classObj
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|selected
operator|.
name|add
argument_list|(
operator|(
name|Embeddable
operator|)
name|classObj
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|selected
return|;
block|}
specifier|public
name|List
argument_list|<
name|ObjEntity
argument_list|>
name|getSelectedEntities
parameter_list|()
block|{
name|List
argument_list|<
name|ObjEntity
argument_list|>
name|selected
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|selectedEntities
operator|.
name|size
argument_list|()
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
name|ObjEntity
operator|&&
name|selectedEntities
operator|.
name|contains
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|classObj
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|selected
operator|.
name|add
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|classObj
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|selected
return|;
block|}
comment|/**      * Returns the first encountered validation problem for an antity matching the name or      * null if the entity is valid or the entity is not present.      */
specifier|public
name|String
name|getProblem
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
name|String
name|name
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|ObjEntity
condition|)
block|{
name|name
operator|=
operator|(
operator|(
name|ObjEntity
operator|)
name|obj
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|obj
operator|instanceof
name|Embeddable
condition|)
block|{
name|name
operator|=
operator|(
operator|(
name|Embeddable
operator|)
name|obj
operator|)
operator|.
name|getClassName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|validation
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
name|failures
init|=
name|validation
operator|.
name|getFailures
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|failures
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
operator|(
name|ValidationFailure
operator|)
name|failures
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getDescription
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isSelected
parameter_list|()
block|{
if|if
condition|(
name|currentClass
operator|instanceof
name|ObjEntity
condition|)
block|{
return|return
name|selectedEntities
operator|.
name|contains
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|currentClass
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|currentClass
operator|instanceof
name|Embeddable
condition|)
block|{
return|return
name|selectedEmbeddables
operator|.
name|contains
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|currentClass
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|currentClass
operator|instanceof
name|DataMap
condition|)
block|{
return|return
name|isDataMapSelected
operator|.
name|contains
argument_list|(
operator|(
operator|(
name|DataMap
operator|)
name|currentClass
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
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
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|currentClass
operator|instanceof
name|ObjEntity
condition|)
block|{
if|if
condition|(
name|selectedFlag
condition|)
block|{
if|if
condition|(
name|selectedEntities
operator|.
name|add
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|currentClass
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|selectedEntities
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|ObjEntity
operator|)
name|currentClass
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|currentClass
operator|instanceof
name|Embeddable
condition|)
block|{
if|if
condition|(
name|selectedFlag
condition|)
block|{
if|if
condition|(
name|selectedEmbeddables
operator|.
name|add
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|currentClass
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|selectedEmbeddables
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|Embeddable
operator|)
name|currentClass
operator|)
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|currentClass
operator|instanceof
name|DataMap
condition|)
block|{
name|updateArtifactGenerationMode
argument_list|(
name|currentClass
argument_list|,
name|selectedFlag
argument_list|)
expr_stmt|;
if|if
condition|(
name|selectedFlag
condition|)
block|{
if|if
condition|(
name|isDataMapSelected
operator|.
name|add
argument_list|(
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|isDataMapSelected
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|DataMap
operator|)
name|currentClass
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|firePropertyChange
argument_list|(
name|SELECTED_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|updateArtifactGenerationMode
parameter_list|(
name|Object
name|classObj
parameter_list|,
name|boolean
name|selected
parameter_list|)
block|{
name|DataMap
name|dataMap
init|=
operator|(
name|DataMap
operator|)
name|classObj
decl_stmt|;
name|CgenConfiguration
name|cgenConfiguration
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
name|dataMap
argument_list|,
name|CgenConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
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
block|}
specifier|public
name|JLabel
name|getItemName
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
name|String
name|className
decl_stmt|;
name|Icon
name|icon
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|Embeddable
condition|)
block|{
name|className
operator|=
operator|(
operator|(
name|Embeddable
operator|)
name|obj
operator|)
operator|.
name|getClassName
argument_list|()
expr_stmt|;
name|icon
operator|=
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
operator|new
name|Embeddable
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|obj
operator|instanceof
name|ObjEntity
condition|)
block|{
name|className
operator|=
operator|(
operator|(
name|ObjEntity
operator|)
name|obj
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
name|icon
operator|=
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
operator|new
name|ObjEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|className
operator|=
operator|(
operator|(
name|DataMap
operator|)
name|obj
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
name|icon
operator|=
name|CellRenderers
operator|.
name|iconForObject
argument_list|(
operator|new
name|DataMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|JLabel
name|labelIcon
init|=
operator|new
name|JLabel
argument_list|()
decl_stmt|;
name|labelIcon
operator|.
name|setIcon
argument_list|(
name|icon
argument_list|)
expr_stmt|;
name|labelIcon
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|labelIcon
operator|.
name|setText
argument_list|(
name|className
argument_list|)
expr_stmt|;
return|return
name|labelIcon
return|;
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
name|updateEntities
parameter_list|()
block|{
name|DataMap
name|map
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|CgenConfiguration
name|cgenConfiguration
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
name|getSelectedEntities
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|entity
operator|.
name|isGeneric
argument_list|()
condition|)
block|{
name|cgenConfiguration
operator|.
name|loadEntity
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|updateEmbeddables
parameter_list|()
block|{
name|DataMap
name|map
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|CgenConfiguration
name|cgenConfiguration
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
name|getSelectedEmbeddables
argument_list|()
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
block|}
name|void
name|addToSelectedEntities
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|entities
parameter_list|)
block|{
name|prepareClasses
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|selectedEntities
operator|.
name|addAll
argument_list|(
name|entities
argument_list|)
expr_stmt|;
name|updateEntities
argument_list|()
expr_stmt|;
block|}
name|void
name|addToSelectedEmbeddables
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|embeddables
parameter_list|)
block|{
name|prepareClasses
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|selectedEmbeddables
operator|.
name|addAll
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
name|selectedEntities
operator|!=
literal|null
condition|?
name|selectedEntities
operator|.
name|size
argument_list|()
else|:
literal|0
return|;
block|}
specifier|public
name|int
name|getSelectedEmbeddablesSize
parameter_list|()
block|{
return|return
name|selectedEmbeddables
operator|!=
literal|null
condition|?
name|selectedEmbeddables
operator|.
name|size
argument_list|()
else|:
literal|0
return|;
block|}
specifier|public
name|boolean
name|isDataMapSelected
parameter_list|()
block|{
return|return
name|isDataMapSelected
operator|!=
literal|null
operator|&&
name|isDataMapSelected
operator|.
name|size
argument_list|()
operator|==
literal|1
return|;
block|}
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
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
name|Object
name|getCurrentClass
parameter_list|()
block|{
return|return
name|currentClass
return|;
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
specifier|public
specifier|abstract
name|void
name|enableGenerateButton
parameter_list|(
name|boolean
name|enabled
parameter_list|)
function_decl|;
block|}
end_class

end_unit

