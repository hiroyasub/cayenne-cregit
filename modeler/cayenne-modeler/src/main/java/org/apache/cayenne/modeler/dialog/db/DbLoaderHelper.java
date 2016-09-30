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
name|dbimport
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
name|dbsync
operator|.
name|CayenneDbSyncModule
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
name|dbsync
operator|.
name|reverse
operator|.
name|db
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
name|dbsync
operator|.
name|reverse
operator|.
name|db
operator|.
name|DefaultDbLoaderDelegate
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
name|dbsync
operator|.
name|reverse
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
name|di
operator|.
name|DIBootstrap
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
name|di
operator|.
name|Injector
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
name|DbRelationship
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
name|dbsync
operator|.
name|naming
operator|.
name|DuplicateNameResolver
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
name|dbsync
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
name|pref
operator|.
name|DBConnectionInfo
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
name|tools
operator|.
name|configuration
operator|.
name|ToolsModule
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
name|DbImportConfiguration
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
name|DbImportModule
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
name|io
operator|.
name|File
import|;
end_import

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

begin_comment
comment|/**  * Stateful helper class that encapsulates access to DbLoader.  */
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
name|dbCatalog
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
name|List
argument_list|<
name|String
argument_list|>
name|schemas
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|catalogs
decl_stmt|;
specifier|protected
name|DbAdapter
name|adapter
decl_stmt|;
specifier|protected
name|DbImportConfiguration
name|config
decl_stmt|;
specifier|protected
name|ReverseEngineering
name|reverseEngineering
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
name|DBConnectionInfo
name|dbConnectionInfo
parameter_list|,
name|ReverseEngineering
name|reverseEngineering
parameter_list|)
block|{
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
try|try
block|{
name|this
operator|.
name|dbCatalog
operator|=
name|connection
operator|.
name|getCatalog
argument_list|()
expr_stmt|;
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
literal|"Error getting catalog."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
name|this
operator|.
name|reverseEngineering
operator|=
name|reverseEngineering
expr_stmt|;
name|this
operator|.
name|config
operator|=
operator|new
name|DbImportConfiguration
argument_list|()
expr_stmt|;
name|this
operator|.
name|config
operator|.
name|setAdapter
argument_list|(
name|adapter
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|.
name|setUsername
argument_list|(
name|dbConnectionInfo
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|.
name|setPassword
argument_list|(
name|dbConnectionInfo
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|.
name|setDriver
argument_list|(
name|dbConnectionInfo
operator|.
name|getJdbcDriver
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|.
name|setUrl
argument_list|(
name|dbConnectionInfo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|dbCatalog
operator|=
name|connection
operator|.
name|getCatalog
argument_list|()
expr_stmt|;
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
literal|"Error getting catalog."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|this
operator|.
name|loader
operator|=
name|config
operator|.
name|createLoader
argument_list|(
name|adapter
argument_list|,
name|connection
argument_list|,
operator|new
name|LoaderDelegate
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
name|processException
argument_list|(
name|th
argument_list|,
literal|"Error creating DbLoader."
argument_list|)
expr_stmt|;
block|}
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
name|isStoppingReverseEngineering
parameter_list|()
block|{
return|return
name|stoppingReverseEngineering
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
comment|// load catalogs...
if|if
condition|(
name|adapter
operator|.
name|supportsCatalogsOnReverseEngineering
argument_list|()
condition|)
block|{
name|LongRunningTask
name|loadCatalogsTask
init|=
operator|new
name|LoadCatalogsTask
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|"Loading Catalogs"
argument_list|)
decl_stmt|;
name|loadCatalogsTask
operator|.
name|startAndWait
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|stoppingReverseEngineering
condition|)
block|{
return|return;
block|}
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
name|this
operator|.
name|loader
operator|.
name|setCreatingMeaningfulPK
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
specifier|private
specifier|final
class|class
name|LoaderDelegate
extends|extends
name|DefaultDbLoaderDelegate
block|{
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|boolean
name|dbRelationship
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
literal|"Load relationships for '"
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|"'..."
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|dbRelationshipLoaded
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbRelationship
name|relationship
parameter_list|)
block|{
name|checkCanceled
argument_list|()
expr_stmt|;
name|loadStatusNote
operator|=
literal|"Load relationship: '"
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|"'; '"
operator|+
name|relationship
operator|.
name|getName
argument_list|()
operator|+
literal|"'..."
expr_stmt|;
return|return
literal|true
return|;
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
name|loadSchemas
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
name|LoadCatalogsTask
extends|extends
name|DbLoaderTask
block|{
specifier|public
name|LoadCatalogsTask
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
literal|"Loading available catalogs..."
expr_stmt|;
try|try
block|{
name|catalogs
operator|=
name|loader
operator|.
name|loadCatalogs
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
literal|"Error Loading Catalogs"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
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
name|DuplicateNameResolver
operator|.
name|resolve
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
name|DuplicateNameResolver
operator|.
name|resolve
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
block|}
if|if
condition|(
name|isCanceled
argument_list|()
condition|)
block|{
return|return;
block|}
name|DataMap
name|dataMap
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|DataChannelDescriptor
name|dataChannelDescriptor
init|=
name|mediator
operator|.
name|getCurrentDataChanel
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataMap
operator|.
name|getReverseEngineering
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dataMap
operator|.
name|getReverseEngineering
argument_list|()
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|reverseEngineering
operator|.
name|setName
argument_list|(
name|dataMap
operator|.
name|getReverseEngineering
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|setConfigurationSource
argument_list|(
name|dataMap
operator|.
name|getReverseEngineering
argument_list|()
operator|.
name|getConfigurationSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|reverseEngineering
operator|.
name|setName
argument_list|(
name|DuplicateNameResolver
operator|.
name|resolve
argument_list|(
name|NameCheckers
operator|.
name|reverseEngineering
argument_list|,
name|dataChannelDescriptor
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dataMap
operator|.
name|getConfigurationSource
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|config
operator|.
name|setDataMapFile
argument_list|(
operator|new
name|File
argument_list|(
name|dataMap
operator|.
name|getConfigurationSource
argument_list|()
operator|.
name|getURL
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|FiltersConfigBuilder
name|filtersConfigBuilder
init|=
operator|new
name|FiltersConfigBuilder
argument_list|(
name|reverseEngineering
argument_list|)
decl_stmt|;
name|config
operator|.
name|getDbLoaderConfig
argument_list|()
operator|.
name|setFiltersConfig
argument_list|(
name|filtersConfigBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|ModelerDbImportAction
name|importAction
init|=
operator|new
name|ModelerDbImportAction
argument_list|(
name|logObj
argument_list|,
name|DbLoaderHelper
operator|.
name|this
argument_list|)
decl_stmt|;
comment|// TODO: we can keep all these things in the Modeler Injector instead of creating a new one?
comment|// we already have CayenneDbSyncModule in there
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|CayenneDbSyncModule
argument_list|()
argument_list|,
operator|new
name|ToolsModule
argument_list|(
name|logObj
argument_list|)
argument_list|,
operator|new
name|DbImportModule
argument_list|()
argument_list|)
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|importAction
argument_list|)
expr_stmt|;
try|try
block|{
name|importAction
operator|.
name|execute
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setReverseEngineering
argument_list|(
name|reverseEngineering
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|processException
argument_list|(
name|e
argument_list|,
literal|"Error importing database schema."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|ProjectController
name|getMediator
parameter_list|()
block|{
return|return
name|mediator
return|;
block|}
specifier|protected
name|DbLoader
name|getLoader
parameter_list|()
block|{
return|return
name|loader
return|;
block|}
block|}
end_class

end_unit

