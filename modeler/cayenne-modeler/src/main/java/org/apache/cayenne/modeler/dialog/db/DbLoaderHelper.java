begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
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
name|db
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
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
name|Arrays
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
name|javax
operator|.
name|swing
operator|.
name|JFrame
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
name|javax
operator|.
name|swing
operator|.
name|SwingUtilities
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
name|CayenneException
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
name|CayenneRuntimeException
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
name|access
operator|.
name|DbLoader
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
name|access
operator|.
name|DbLoaderDelegate
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
name|access
operator|.
name|loader
operator|.
name|DbLoaderConfiguration
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
name|access
operator|.
name|loader
operator|.
name|filters
operator|.
name|EntityFilters
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
name|access
operator|.
name|loader
operator|.
name|filters
operator|.
name|FilterFactory
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
name|access
operator|.
name|loader
operator|.
name|filters
operator|.
name|FiltersConfig
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
name|dba
operator|.
name|DbAdapter
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
name|DbEntity
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
name|MapEvent
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
name|naming
operator|.
name|DefaultUniqueNameGenerator
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
name|naming
operator|.
name|NameCheckers
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
name|event
operator|.
name|DataMapDisplayEvent
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
name|LongRunningTask
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
name|resource
operator|.
name|Resource
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
name|dbimport
operator|.
name|config
operator|.
name|FiltersConfigBuilder
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
name|dbimport
operator|.
name|config
operator|.
name|ReverseEngineering
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
name|DeleteRuleUpdater
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|loader
operator|.
name|filters
operator|.
name|FilterFactory
operator|.
name|NULL
import|;
end_import

begin_comment
comment|/**  * Stateful helper class that encapsulates access to DbLoader.  *   */
end_comment

begin_class
specifier|public
class|class
name|DbLoaderHelper
block|{
specifier|private
specifier|static
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DbLoaderHelper
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// TODO: this is a temp hack... need to delegate to DbAdapter, or
comment|// configurable in
comment|// preferences...
specifier|private
specifier|static
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|EXCLUDED_TABLES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"AUTO_PK_SUPPORT"
argument_list|,
literal|"auto_pk_support"
argument_list|)
decl_stmt|;
specifier|static
name|DbLoaderMergeDialog
name|mergeDialog
decl_stmt|;
specifier|protected
name|boolean
name|overwritePreferenceSet
decl_stmt|;
specifier|protected
name|boolean
name|overwritingEntities
decl_stmt|;
specifier|protected
name|boolean
name|stoppingReverseEngineering
decl_stmt|;
specifier|protected
name|boolean
name|existingMap
decl_stmt|;
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|protected
name|String
name|dbUserName
decl_stmt|;
specifier|protected
name|DbLoader
name|loader
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
name|boolean
name|meaningfulPk
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|schemas
decl_stmt|;
specifier|private
specifier|final
name|EntityFilters
operator|.
name|Builder
name|filterBuilder
init|=
operator|new
name|EntityFilters
operator|.
name|Builder
argument_list|()
decl_stmt|;
specifier|protected
name|String
name|loadStatusNote
decl_stmt|;
comment|/**      * ObjEntities which were added to project during reverse engineering      */
specifier|protected
name|List
argument_list|<
name|ObjEntity
argument_list|>
name|addedObjEntities
decl_stmt|;
specifier|static
name|DbLoaderMergeDialog
name|getMergeDialogInstance
parameter_list|()
block|{
if|if
condition|(
name|mergeDialog
operator|==
literal|null
condition|)
block|{
name|mergeDialog
operator|=
operator|new
name|DbLoaderMergeDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|mergeDialog
return|;
block|}
specifier|public
name|DbLoaderHelper
parameter_list|(
name|ProjectController
name|mediator
parameter_list|,
name|Connection
name|connection
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|String
name|dbUserName
parameter_list|)
block|{
name|this
operator|.
name|dbUserName
operator|=
name|dbUserName
expr_stmt|;
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
name|this
operator|.
name|loader
operator|=
operator|new
name|DbLoader
argument_list|(
name|connection
argument_list|,
name|adapter
argument_list|,
operator|new
name|LoaderDelegate
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setOverwritingEntities
parameter_list|(
name|boolean
name|overwritePreference
parameter_list|)
block|{
name|this
operator|.
name|overwritingEntities
operator|=
name|overwritePreference
expr_stmt|;
block|}
specifier|public
name|void
name|setOverwritePreferenceSet
parameter_list|(
name|boolean
name|overwritePreferenceSet
parameter_list|)
block|{
name|this
operator|.
name|overwritePreferenceSet
operator|=
name|overwritePreferenceSet
expr_stmt|;
block|}
specifier|public
name|void
name|setStoppingReverseEngineering
parameter_list|(
name|boolean
name|stopReverseEngineering
parameter_list|)
block|{
name|this
operator|.
name|stoppingReverseEngineering
operator|=
name|stopReverseEngineering
expr_stmt|;
block|}
specifier|public
name|boolean
name|isOverwritePreferenceSet
parameter_list|()
block|{
return|return
name|overwritePreferenceSet
return|;
block|}
specifier|public
name|boolean
name|isOverwritingEntities
parameter_list|()
block|{
return|return
name|overwritingEntities
return|;
block|}
specifier|public
name|boolean
name|isStoppingReverseEngineering
parameter_list|()
block|{
return|return
name|stoppingReverseEngineering
return|;
block|}
comment|/**      * Performs reverse engineering of the DB using internal DbLoader. This      * method should be invoked outside EventDispatchThread, or it will throw an      * exception.      */
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|stoppingReverseEngineering
operator|=
literal|false
expr_stmt|;
comment|// load schemas...
name|LongRunningTask
name|loadSchemasTask
init|=
operator|new
name|LoadSchemasTask
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|"Loading Schemas"
argument_list|)
decl_stmt|;
name|loadSchemasTask
operator|.
name|startAndWait
argument_list|()
expr_stmt|;
if|if
condition|(
name|stoppingReverseEngineering
condition|)
block|{
return|return;
block|}
specifier|final
name|DbLoaderOptionsDialog
name|dialog
init|=
operator|new
name|DbLoaderOptionsDialog
argument_list|(
name|schemas
argument_list|,
name|dbUserName
argument_list|,
literal|false
argument_list|)
decl_stmt|;
try|try
block|{
comment|// since we are not inside EventDisptahcer Thread, must run it via
comment|// SwingUtilities
name|SwingUtilities
operator|.
name|invokeAndWait
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|dialog
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dialog
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|processException
argument_list|(
name|th
argument_list|,
literal|"Error Reengineering Database"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|dialog
operator|.
name|getChoice
argument_list|()
operator|==
name|DbLoaderOptionsDialog
operator|.
name|CANCEL
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|filterBuilder
operator|.
name|schema
argument_list|(
name|dialog
operator|.
name|getSelectedSchema
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|filterBuilder
operator|.
name|includeTables
argument_list|(
name|dialog
operator|.
name|getTableNamePattern
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|filterBuilder
operator|.
name|setProceduresFilters
argument_list|(
name|dialog
operator|.
name|isLoadingProcedures
argument_list|()
condition|?
name|FilterFactory
operator|.
name|TRUE
else|:
name|FilterFactory
operator|.
name|NULL
argument_list|)
expr_stmt|;
name|this
operator|.
name|filterBuilder
operator|.
name|includeProcedures
argument_list|(
name|dialog
operator|.
name|getProcedureNamePattern
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|meaningfulPk
operator|=
name|dialog
operator|.
name|isMeaningfulPk
argument_list|()
expr_stmt|;
name|this
operator|.
name|addedObjEntities
operator|=
operator|new
name|ArrayList
argument_list|<
name|ObjEntity
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|loader
operator|.
name|setNameGenerator
argument_list|(
name|dialog
operator|.
name|getNamingStrategy
argument_list|()
argument_list|)
expr_stmt|;
comment|// load DataMap...
name|LongRunningTask
name|loadDataMapTask
init|=
operator|new
name|LoadDataMapTask
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|"Reengineering DB"
argument_list|)
decl_stmt|;
name|loadDataMapTask
operator|.
name|startAndWait
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|processException
parameter_list|(
specifier|final
name|Throwable
name|th
parameter_list|,
specifier|final
name|String
name|message
parameter_list|)
block|{
name|logObj
operator|.
name|info
argument_list|(
literal|"Exception on reverse engineering"
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|th
argument_list|)
argument_list|)
expr_stmt|;
name|cleanup
argument_list|()
expr_stmt|;
name|SwingUtilities
operator|.
name|invokeLater
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|th
operator|.
name|getMessage
argument_list|()
argument_list|,
name|message
argument_list|,
name|JOptionPane
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|cleanup
parameter_list|()
block|{
name|loadStatusNote
operator|=
literal|"Closing connection..."
expr_stmt|;
try|try
block|{
if|if
condition|(
name|loader
operator|.
name|getConnection
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|loader
operator|.
name|getConnection
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|logObj
operator|.
name|warn
argument_list|(
literal|"Error closing connection."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
class|class
name|LoaderDelegate
implements|implements
name|DbLoaderDelegate
block|{
specifier|public
name|boolean
name|overwriteDbEntity
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
throws|throws
name|CayenneException
block|{
name|checkCanceled
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|overwritePreferenceSet
condition|)
block|{
name|DbLoaderMergeDialog
name|dialog
init|=
name|DbLoaderHelper
operator|.
name|getMergeDialogInstance
argument_list|()
decl_stmt|;
name|dialog
operator|.
name|initFromModel
argument_list|(
name|DbLoaderHelper
operator|.
name|this
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dialog
operator|.
name|centerWindow
argument_list|()
expr_stmt|;
name|dialog
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dialog
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|stoppingReverseEngineering
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Should stop DB import."
argument_list|)
throw|;
block|}
return|return
name|overwritingEntities
return|;
block|}
specifier|public
name|void
name|dbEntityAdded
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|checkCanceled
argument_list|()
expr_stmt|;
name|loadStatusNote
operator|=
literal|"Importing table '"
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|"'..."
expr_stmt|;
comment|// TODO: hack to prevent PK tables from being visible... this should
comment|// really be
comment|// delegated to DbAdapter to decide...
if|if
condition|(
name|EXCLUDED_TABLES
operator|.
name|contains
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|entity
operator|.
name|getDataMap
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|removeDbEntity
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|existingMap
condition|)
block|{
name|mediator
operator|.
name|fireDbEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|this
argument_list|,
name|entity
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|objEntityAdded
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|checkCanceled
argument_list|()
expr_stmt|;
name|loadStatusNote
operator|=
literal|"Creating ObjEntity '"
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|"'..."
expr_stmt|;
name|addedObjEntities
operator|.
name|add
argument_list|(
name|entity
argument_list|)
expr_stmt|;
if|if
condition|(
name|existingMap
condition|)
block|{
name|mediator
operator|.
name|fireObjEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|this
argument_list|,
name|entity
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|dbEntityRemoved
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|checkCanceled
argument_list|()
expr_stmt|;
if|if
condition|(
name|existingMap
condition|)
block|{
name|mediator
operator|.
name|fireDbEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|entity
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|objEntityRemoved
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|checkCanceled
argument_list|()
expr_stmt|;
if|if
condition|(
name|existingMap
condition|)
block|{
name|mediator
operator|.
name|fireObjEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|entity
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|checkCanceled
parameter_list|()
block|{
if|if
condition|(
name|isStoppingReverseEngineering
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Reengineering was canceled."
argument_list|)
throw|;
block|}
block|}
block|}
specifier|abstract
class|class
name|DbLoaderTask
extends|extends
name|LongRunningTask
block|{
specifier|public
name|DbLoaderTask
parameter_list|(
name|JFrame
name|frame
parameter_list|,
name|String
name|title
parameter_list|)
block|{
name|super
argument_list|(
name|frame
argument_list|,
name|title
argument_list|)
expr_stmt|;
name|setMinValue
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|setMaxValue
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getCurrentNote
parameter_list|()
block|{
return|return
name|loadStatusNote
return|;
block|}
annotation|@
name|Override
specifier|protected
name|int
name|getCurrentValue
parameter_list|()
block|{
return|return
name|getMinValue
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isIndeterminate
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isCanceled
parameter_list|()
block|{
return|return
name|isStoppingReverseEngineering
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setCanceled
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
if|if
condition|(
name|b
condition|)
block|{
name|loadStatusNote
operator|=
literal|"Canceling.."
expr_stmt|;
block|}
name|setStoppingReverseEngineering
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
class|class
name|LoadSchemasTask
extends|extends
name|DbLoaderTask
block|{
specifier|public
name|LoadSchemasTask
parameter_list|(
name|JFrame
name|frame
parameter_list|,
name|String
name|title
parameter_list|)
block|{
name|super
argument_list|(
name|frame
argument_list|,
name|title
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|execute
parameter_list|()
block|{
name|loadStatusNote
operator|=
literal|"Loading available schemas..."
expr_stmt|;
try|try
block|{
name|schemas
operator|=
name|loader
operator|.
name|getSchemas
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|processException
argument_list|(
name|th
argument_list|,
literal|"Error Loading Schemas"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|final
class|class
name|LoadDataMapTask
extends|extends
name|DbLoaderTask
block|{
specifier|public
name|LoadDataMapTask
parameter_list|(
name|JFrame
name|frame
parameter_list|,
name|String
name|title
parameter_list|)
block|{
name|super
argument_list|(
name|frame
argument_list|,
name|title
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|execute
parameter_list|()
block|{
name|loadStatusNote
operator|=
literal|"Preparing..."
expr_stmt|;
name|DbLoaderHelper
operator|.
name|this
operator|.
name|dataMap
operator|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
expr_stmt|;
name|DbLoaderHelper
operator|.
name|this
operator|.
name|existingMap
operator|=
name|dataMap
operator|!=
literal|null
expr_stmt|;
if|if
condition|(
operator|!
name|existingMap
condition|)
block|{
name|dataMap
operator|=
operator|new
name|DataMap
argument_list|(
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|dataMap
argument_list|)
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setName
argument_list|(
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|dataMap
argument_list|,
name|mediator
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setDefaultSchema
argument_list|(
name|filterBuilder
operator|.
name|schema
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isCanceled
argument_list|()
condition|)
block|{
return|return;
block|}
name|importingTables
argument_list|()
expr_stmt|;
name|importingProcedures
argument_list|()
expr_stmt|;
name|cleanup
argument_list|()
expr_stmt|;
comment|// fire up events
name|loadStatusNote
operator|=
literal|"Updating view..."
expr_stmt|;
if|if
condition|(
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mediator
operator|.
name|fireDataMapEvent
argument_list|(
operator|new
name|DataMapEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|dataMap
argument_list|,
name|MapEvent
operator|.
name|CHANGE
argument_list|)
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireDataMapDisplayEvent
argument_list|(
operator|new
name|DataMapDisplayEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|dataMap
argument_list|,
operator|(
name|DataChannelDescriptor
operator|)
name|mediator
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
argument_list|,
name|mediator
operator|.
name|getCurrentDataNode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|DataChannelDescriptor
name|currentDomain
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|mediator
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
name|Resource
name|baseResource
init|=
name|currentDomain
operator|.
name|getConfigurationSource
argument_list|()
decl_stmt|;
comment|// this will be new data map so need to set configuration source
comment|// for it
if|if
condition|(
name|baseResource
operator|!=
literal|null
condition|)
block|{
name|Resource
name|dataMapResource
init|=
name|baseResource
operator|.
name|getRelativeResource
argument_list|(
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|setConfigurationSource
argument_list|(
name|dataMapResource
argument_list|)
expr_stmt|;
block|}
name|mediator
operator|.
name|addDataMap
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|importingProcedures
parameter_list|()
block|{
if|if
condition|(
operator|!
name|filterBuilder
operator|.
name|proceduresFilters
argument_list|()
operator|.
name|equals
argument_list|(
name|NULL
argument_list|)
condition|)
block|{
return|return;
block|}
name|loadStatusNote
operator|=
literal|"Importing procedures..."
expr_stmt|;
try|try
block|{
name|DbLoaderConfiguration
name|configuration
init|=
operator|new
name|DbLoaderConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setFiltersConfig
argument_list|(
operator|new
name|FiltersConfig
argument_list|(
name|filterBuilder
operator|.
name|build
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|loader
operator|.
name|loadProcedures
argument_list|(
name|dataMap
argument_list|,
operator|new
name|DbLoaderConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isCanceled
argument_list|()
condition|)
block|{
name|processException
argument_list|(
name|th
argument_list|,
literal|"Error Reengineering Database"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|importingTables
parameter_list|()
block|{
name|loadStatusNote
operator|=
literal|"Importing tables..."
expr_stmt|;
try|try
block|{
name|loader
operator|.
name|setCreatingMeaningfulPK
argument_list|(
name|meaningfulPk
argument_list|)
expr_stmt|;
name|DbLoaderConfiguration
name|configuration
init|=
operator|new
name|DbLoaderConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setFiltersConfig
argument_list|(
operator|new
name|FiltersConfigBuilder
argument_list|(
operator|new
name|ReverseEngineering
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
name|filterBuilder
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|filtersConfig
argument_list|()
argument_list|)
expr_stmt|;
name|loader
operator|.
name|load
argument_list|(
name|dataMap
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
comment|/**                  * Update default rules for relationships                  */
for|for
control|(
name|ObjEntity
name|addedObjEntity
range|:
name|addedObjEntities
control|)
block|{
name|DeleteRuleUpdater
operator|.
name|updateObjEntity
argument_list|(
name|addedObjEntity
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isCanceled
argument_list|()
condition|)
block|{
name|processException
argument_list|(
name|th
argument_list|,
literal|"Error Reengineering Database"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

