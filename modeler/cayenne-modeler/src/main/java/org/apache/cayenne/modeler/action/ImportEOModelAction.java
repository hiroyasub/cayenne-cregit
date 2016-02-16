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
name|action
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
name|awt
operator|.
name|HeadlessException
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
name|ActionEvent
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
name|net
operator|.
name|URL
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
name|Iterator
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
name|JDialog
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JFileChooser
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|filechooser
operator|.
name|FileFilter
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
name|DataNodeDescriptor
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
name|DataNodeEvent
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
name|QueryEvent
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
name|server
operator|.
name|JNDIDataSourceFactory
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
name|server
operator|.
name|XMLPoolingDataSourceFactory
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
name|conn
operator|.
name|DataSourceInfo
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
name|event
operator|.
name|DataNodeDisplayEvent
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
name|FSPath
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
name|AdapterMapping
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
name|CayenneAction
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
name|FileFilters
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
name|query
operator|.
name|QueryDescriptor
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
name|wocompat
operator|.
name|EOModelProcessor
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
name|collections
operator|.
name|CollectionUtils
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

begin_comment
comment|/**  * Action handler for WebObjects EOModel import function.  *   */
end_comment

begin_class
specifier|public
class|class
name|ImportEOModelAction
extends|extends
name|CayenneAction
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
name|ImportEOModelAction
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Import EOModel"
return|;
block|}
specifier|protected
name|JFileChooser
name|eoModelChooser
decl_stmt|;
specifier|public
name|ImportEOModelAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|getActionName
argument_list|()
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-eomodel.gif"
return|;
block|}
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|event
parameter_list|)
block|{
name|importEOModel
argument_list|()
expr_stmt|;
block|}
comment|/**      * Allows user to select an EOModel, then imports it as a DataMap.      */
specifier|protected
name|void
name|importEOModel
parameter_list|()
block|{
name|JFileChooser
name|fileChooser
init|=
name|getEOModelChooser
argument_list|()
decl_stmt|;
name|int
name|status
init|=
name|fileChooser
operator|.
name|showOpenDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|status
operator|==
name|JFileChooser
operator|.
name|APPROVE_OPTION
condition|)
block|{
comment|// save preferences
name|FSPath
name|lastDir
init|=
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getLastEOModelDirectory
argument_list|()
decl_stmt|;
name|lastDir
operator|.
name|updateFromChooser
argument_list|(
name|fileChooser
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|fileChooser
operator|.
name|getSelectedFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|file
operator|=
name|file
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
block|}
name|DataMap
name|currentMap
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
try|try
block|{
name|URL
name|url
init|=
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|EOModelProcessor
name|processor
init|=
operator|new
name|EOModelProcessor
argument_list|()
decl_stmt|;
comment|// load DataNode if we are not merging with an existing map
if|if
condition|(
name|currentMap
operator|==
literal|null
condition|)
block|{
name|loadDataNode
argument_list|(
name|processor
operator|.
name|loadModeIndex
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// load DataMap
name|DataMap
name|map
init|=
name|processor
operator|.
name|loadEOModel
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|addDataMap
argument_list|(
name|map
argument_list|,
name|currentMap
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|logObj
operator|.
name|info
argument_list|(
literal|"EOModel Loading Exception"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|ErrorDebugDialog
operator|.
name|guiException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|loadDataNode
parameter_list|(
name|Map
name|eomodelIndex
parameter_list|)
block|{
comment|// if this is JDBC or JNDI node and connection dictionary is specified, load a
comment|// DataNode, otherwise ignore it (meaning that pre 5.* EOModels will not have a
comment|// node).
name|String
name|adapter
init|=
operator|(
name|String
operator|)
name|eomodelIndex
operator|.
name|get
argument_list|(
literal|"adaptorName"
argument_list|)
decl_stmt|;
name|Map
name|connection
init|=
operator|(
name|Map
operator|)
name|eomodelIndex
operator|.
name|get
argument_list|(
literal|"connectionDictionary"
argument_list|)
decl_stmt|;
if|if
condition|(
name|adapter
operator|!=
literal|null
operator|&&
name|connection
operator|!=
literal|null
condition|)
block|{
name|CreateNodeAction
name|nodeBuilder
init|=
operator|(
name|CreateNodeAction
operator|)
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|CreateNodeAction
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// this should make created node current, resulting in the new map being added
comment|// to the node automatically once it is loaded
name|DataNodeDescriptor
name|node
init|=
name|nodeBuilder
operator|.
name|buildDataNode
argument_list|()
decl_stmt|;
comment|// configure node...
if|if
condition|(
literal|"JNDI"
operator|.
name|equalsIgnoreCase
argument_list|(
name|adapter
argument_list|)
condition|)
block|{
name|node
operator|.
name|setDataSourceFactoryType
argument_list|(
name|JNDIDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|.
name|setParameters
argument_list|(
operator|(
name|String
operator|)
name|connection
operator|.
name|get
argument_list|(
literal|"serverUrl"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// guess adapter from plugin or driver
name|AdapterMapping
name|adapterDefaults
init|=
name|getApplication
argument_list|()
operator|.
name|getAdapterMapping
argument_list|()
decl_stmt|;
name|String
name|cayenneAdapter
init|=
name|adapterDefaults
operator|.
name|adapterForEOFPluginOrDriver
argument_list|(
operator|(
name|String
operator|)
name|connection
operator|.
name|get
argument_list|(
literal|"plugin"
argument_list|)
argument_list|,
operator|(
name|String
operator|)
name|connection
operator|.
name|get
argument_list|(
literal|"driver"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cayenneAdapter
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|DbAdapter
argument_list|>
name|adapterClass
init|=
name|getApplication
argument_list|()
operator|.
name|getClassLoadingService
argument_list|()
operator|.
name|loadClass
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|,
name|cayenneAdapter
argument_list|)
decl_stmt|;
name|node
operator|.
name|setAdapterType
argument_list|(
name|adapterClass
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|// ignore...
block|}
block|}
name|node
operator|.
name|setDataSourceFactoryType
argument_list|(
name|XMLPoolingDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|DataSourceInfo
name|dsi
init|=
name|node
operator|.
name|getDataSourceDescriptor
argument_list|()
decl_stmt|;
name|dsi
operator|.
name|setDataSourceUrl
argument_list|(
name|keyAsString
argument_list|(
name|connection
argument_list|,
literal|"URL"
argument_list|)
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|setJdbcDriver
argument_list|(
name|keyAsString
argument_list|(
name|connection
argument_list|,
literal|"driver"
argument_list|)
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|setPassword
argument_list|(
name|keyAsString
argument_list|(
name|connection
argument_list|,
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|setUserName
argument_list|(
name|keyAsString
argument_list|(
name|connection
argument_list|,
literal|"username"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|DataChannelDescriptor
name|domain
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|getProjectController
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
name|domain
operator|.
name|getNodeDescriptors
argument_list|()
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
comment|// send events after the node creation is complete
name|getProjectController
argument_list|()
operator|.
name|fireDataNodeEvent
argument_list|(
operator|new
name|DataNodeEvent
argument_list|(
name|this
argument_list|,
name|node
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
name|getProjectController
argument_list|()
operator|.
name|fireDataNodeDisplayEvent
argument_list|(
operator|new
name|DataNodeDisplayEvent
argument_list|(
name|this
argument_list|,
operator|(
name|DataChannelDescriptor
operator|)
name|getProjectController
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
argument_list|,
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// CAY-246 - if user name or password is all numeric, it will
comment|// be returned as number, so we can't cast dictionary keys to String
specifier|private
name|String
name|keyAsString
parameter_list|(
name|Map
name|map
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|Object
name|value
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|value
operator|!=
literal|null
operator|)
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * Adds DataMap into the project.      */
specifier|protected
name|void
name|addDataMap
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|DataMap
name|currentMap
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentMap
operator|!=
literal|null
condition|)
block|{
comment|// merge with existing map... have to memorize map state before and after
comment|// to do the right events
name|Collection
name|originalOE
init|=
operator|new
name|ArrayList
argument_list|(
name|currentMap
operator|.
name|getObjEntities
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
name|originalDE
init|=
operator|new
name|ArrayList
argument_list|(
name|currentMap
operator|.
name|getDbEntities
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
name|originalQueries
init|=
operator|new
name|ArrayList
argument_list|(
name|currentMap
operator|.
name|getQueryDescriptors
argument_list|()
argument_list|)
decl_stmt|;
name|currentMap
operator|.
name|mergeWithDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|map
operator|=
name|currentMap
expr_stmt|;
comment|// postprocess changes
name|Collection
name|newOE
init|=
operator|new
name|ArrayList
argument_list|(
name|currentMap
operator|.
name|getObjEntities
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
name|newDE
init|=
operator|new
name|ArrayList
argument_list|(
name|currentMap
operator|.
name|getDbEntities
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
name|newQueries
init|=
operator|new
name|ArrayList
argument_list|(
name|currentMap
operator|.
name|getQueryDescriptors
argument_list|()
argument_list|)
decl_stmt|;
name|EntityEvent
name|entityEvent
init|=
operator|new
name|EntityEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|QueryEvent
name|queryEvent
init|=
operator|new
name|QueryEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Collection
name|addedOE
init|=
name|CollectionUtils
operator|.
name|subtract
argument_list|(
name|newOE
argument_list|,
name|originalOE
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|addedOE
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entity
name|e
init|=
operator|(
name|Entity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|entityEvent
operator|.
name|setEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|entityEvent
operator|.
name|setId
argument_list|(
name|MapEvent
operator|.
name|ADD
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireObjEntityEvent
argument_list|(
name|entityEvent
argument_list|)
expr_stmt|;
block|}
name|Collection
name|removedOE
init|=
name|CollectionUtils
operator|.
name|subtract
argument_list|(
name|originalOE
argument_list|,
name|newOE
argument_list|)
decl_stmt|;
name|it
operator|=
name|removedOE
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entity
name|e
init|=
operator|(
name|Entity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|entityEvent
operator|.
name|setEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|entityEvent
operator|.
name|setId
argument_list|(
name|MapEvent
operator|.
name|REMOVE
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireObjEntityEvent
argument_list|(
name|entityEvent
argument_list|)
expr_stmt|;
block|}
name|Collection
name|addedDE
init|=
name|CollectionUtils
operator|.
name|subtract
argument_list|(
name|newDE
argument_list|,
name|originalDE
argument_list|)
decl_stmt|;
name|it
operator|=
name|addedDE
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entity
name|e
init|=
operator|(
name|Entity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|entityEvent
operator|.
name|setEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|entityEvent
operator|.
name|setId
argument_list|(
name|MapEvent
operator|.
name|ADD
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireDbEntityEvent
argument_list|(
name|entityEvent
argument_list|)
expr_stmt|;
block|}
name|Collection
name|removedDE
init|=
name|CollectionUtils
operator|.
name|subtract
argument_list|(
name|originalDE
argument_list|,
name|newDE
argument_list|)
decl_stmt|;
name|it
operator|=
name|removedDE
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entity
name|e
init|=
operator|(
name|Entity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|entityEvent
operator|.
name|setEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|entityEvent
operator|.
name|setId
argument_list|(
name|MapEvent
operator|.
name|REMOVE
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireDbEntityEvent
argument_list|(
name|entityEvent
argument_list|)
expr_stmt|;
block|}
comment|// queries
name|Collection
name|addedQueries
init|=
name|CollectionUtils
operator|.
name|subtract
argument_list|(
name|newQueries
argument_list|,
name|originalQueries
argument_list|)
decl_stmt|;
name|it
operator|=
name|addedQueries
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|QueryDescriptor
name|q
init|=
operator|(
name|QueryDescriptor
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|queryEvent
operator|.
name|setQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|queryEvent
operator|.
name|setId
argument_list|(
name|MapEvent
operator|.
name|ADD
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireQueryEvent
argument_list|(
name|queryEvent
argument_list|)
expr_stmt|;
block|}
name|Collection
name|removedQueries
init|=
name|CollectionUtils
operator|.
name|subtract
argument_list|(
name|originalQueries
argument_list|,
name|newQueries
argument_list|)
decl_stmt|;
name|it
operator|=
name|removedQueries
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|QueryDescriptor
name|q
init|=
operator|(
name|QueryDescriptor
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|queryEvent
operator|.
name|setQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|queryEvent
operator|.
name|setId
argument_list|(
name|MapEvent
operator|.
name|REMOVE
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireQueryEvent
argument_list|(
name|queryEvent
argument_list|)
expr_stmt|;
block|}
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
name|map
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
comment|// fix DataMap name, as there maybe a map with the same name already
name|DataChannelDescriptor
name|domain
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
name|map
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
name|domain
argument_list|,
name|map
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// side effect of this operation is that if a node was created, this DataMap
comment|// will be linked with it...
name|mediator
operator|.
name|addDataMap
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns EOModel chooser.      */
specifier|public
name|JFileChooser
name|getEOModelChooser
parameter_list|()
block|{
if|if
condition|(
name|eoModelChooser
operator|==
literal|null
condition|)
block|{
name|eoModelChooser
operator|=
operator|new
name|EOModelChooser
argument_list|(
literal|"Select EOModel"
argument_list|)
expr_stmt|;
block|}
name|FSPath
name|lastDir
init|=
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getLastEOModelDirectory
argument_list|()
decl_stmt|;
name|lastDir
operator|.
name|updateChooser
argument_list|(
name|eoModelChooser
argument_list|)
expr_stmt|;
return|return
name|eoModelChooser
return|;
block|}
comment|/**      * Custom file chooser that will pop up again if a bad directory is selected.      */
class|class
name|EOModelChooser
extends|extends
name|JFileChooser
block|{
specifier|protected
name|FileFilter
name|selectFilter
decl_stmt|;
specifier|protected
name|JDialog
name|cachedDialog
decl_stmt|;
specifier|public
name|EOModelChooser
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|super
operator|.
name|setFileFilter
argument_list|(
name|FileFilters
operator|.
name|getEOModelFilter
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|setDialogTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|super
operator|.
name|setFileSelectionMode
argument_list|(
name|JFileChooser
operator|.
name|FILES_AND_DIRECTORIES
argument_list|)
expr_stmt|;
name|this
operator|.
name|selectFilter
operator|=
name|FileFilters
operator|.
name|getEOModelSelectFilter
argument_list|()
expr_stmt|;
block|}
specifier|public
name|int
name|showOpenDialog
parameter_list|(
name|Component
name|parent
parameter_list|)
block|{
name|int
name|status
init|=
name|super
operator|.
name|showOpenDialog
argument_list|(
name|parent
argument_list|)
decl_stmt|;
if|if
condition|(
name|status
operator|!=
name|JFileChooser
operator|.
name|APPROVE_OPTION
condition|)
block|{
name|cachedDialog
operator|=
literal|null
expr_stmt|;
return|return
name|status
return|;
block|}
comment|// make sure invalid directory is not selected
name|File
name|file
init|=
name|this
operator|.
name|getSelectedFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|selectFilter
operator|.
name|accept
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|cachedDialog
operator|=
literal|null
expr_stmt|;
return|return
name|JFileChooser
operator|.
name|APPROVE_OPTION
return|;
block|}
else|else
block|{
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|this
operator|.
name|setCurrentDirectory
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
return|return
name|this
operator|.
name|showOpenDialog
argument_list|(
name|parent
argument_list|)
return|;
block|}
block|}
specifier|protected
name|JDialog
name|createDialog
parameter_list|(
name|Component
name|parent
parameter_list|)
throws|throws
name|HeadlessException
block|{
if|if
condition|(
name|cachedDialog
operator|==
literal|null
condition|)
block|{
name|cachedDialog
operator|=
name|super
operator|.
name|createDialog
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
return|return
name|cachedDialog
return|;
block|}
block|}
block|}
end_class

end_unit

