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
name|datanode
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
name|event
operator|.
name|ComponentAdapter
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
name|ComponentEvent
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
name|access
operator|.
name|DataDomain
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
name|DataNode
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
name|dbsync
operator|.
name|CreateIfNoSchemaStrategy
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
name|dbsync
operator|.
name|ThrowOnPartialOrCreateSchemaStrategy
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
name|dbsync
operator|.
name|SkipSchemaUpdateStrategy
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
name|dbsync
operator|.
name|ThrowOnPartialSchemaStrategy
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
name|conf
operator|.
name|Configuration
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
name|conf
operator|.
name|DBCPDataSourceFactory
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
name|conf
operator|.
name|DriverDataSourceFactory
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
name|conf
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
name|map
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
name|event
operator|.
name|DataNodeDisplayListener
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
name|ProjectUtil
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
name|ApplicationProject
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
name|BindingDelegate
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
comment|/**  * A controller for the main tab of the DataNode editor panel.  *   */
end_comment

begin_class
specifier|public
class|class
name|MainDataNodeEditor
extends|extends
name|CayenneController
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|NO_LOCAL_DATA_SOURCE
init|=
literal|"Select DataSource for Local Work..."
decl_stmt|;
specifier|final
specifier|static
name|String
index|[]
name|standardDataSourceFactories
init|=
operator|new
name|String
index|[]
block|{
name|DriverDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|JNDIDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|DBCPDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
decl_stmt|;
specifier|final
specifier|static
name|String
index|[]
name|standardSchemaUpdateStrategy
init|=
operator|new
name|String
index|[]
block|{
name|SkipSchemaUpdateStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|CreateIfNoSchemaStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|ThrowOnPartialSchemaStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|ThrowOnPartialOrCreateSchemaStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
decl_stmt|;
specifier|protected
name|MainDataNodeView
name|view
decl_stmt|;
specifier|protected
name|DataNodeEditor
name|tabbedPaneController
decl_stmt|;
specifier|protected
name|DataNode
name|node
decl_stmt|;
specifier|protected
name|Map
name|datasourceEditors
decl_stmt|;
specifier|protected
name|Map
name|localDataSources
decl_stmt|;
specifier|protected
name|DataSourceEditor
name|defaultSubeditor
decl_stmt|;
specifier|protected
name|BindingDelegate
name|nodeChangeProcessor
decl_stmt|;
specifier|protected
name|ObjectBinding
index|[]
name|bindings
decl_stmt|;
specifier|protected
name|ObjectBinding
name|localDataSourceBinding
decl_stmt|;
specifier|public
name|MainDataNodeEditor
parameter_list|(
name|ProjectController
name|parent
parameter_list|,
name|DataNodeEditor
name|tabController
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|tabbedPaneController
operator|=
name|tabController
expr_stmt|;
name|this
operator|.
name|view
operator|=
operator|new
name|MainDataNodeView
argument_list|(
operator|(
name|ProjectController
operator|)
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|datasourceEditors
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|localDataSources
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|nodeChangeProcessor
operator|=
operator|new
name|BindingDelegate
argument_list|()
block|{
specifier|public
name|void
name|modelUpdated
parameter_list|(
name|ObjectBinding
name|binding
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
name|DataNodeEvent
name|e
init|=
operator|new
name|DataNodeEvent
argument_list|(
name|MainDataNodeEditor
operator|.
name|this
argument_list|,
name|node
argument_list|)
decl_stmt|;
if|if
condition|(
name|binding
operator|!=
literal|null
operator|&&
name|binding
operator|.
name|getView
argument_list|()
operator|==
name|view
operator|.
name|getDataNodeName
argument_list|()
condition|)
block|{
name|e
operator|.
name|setOldName
argument_list|(
name|oldValue
operator|!=
literal|null
condition|?
name|oldValue
operator|.
name|toString
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|ProjectController
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|fireDataNodeEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|this
operator|.
name|defaultSubeditor
operator|=
operator|new
name|CustomDataSourceEditor
argument_list|(
name|parent
argument_list|,
name|nodeChangeProcessor
argument_list|)
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
block|}
comment|// ======= properties
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
name|String
name|getFactoryName
parameter_list|()
block|{
return|return
operator|(
name|node
operator|!=
literal|null
operator|)
condition|?
name|node
operator|.
name|getDataSourceFactory
argument_list|()
else|:
literal|null
return|;
block|}
specifier|public
name|void
name|setFactoryName
parameter_list|(
name|String
name|factoryName
parameter_list|)
block|{
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|setDataSourceFactory
argument_list|(
name|factoryName
argument_list|)
expr_stmt|;
name|showDataSourceSubview
argument_list|(
name|factoryName
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getSchemaUpdateStrategy
parameter_list|()
block|{
return|return
operator|(
name|node
operator|!=
literal|null
operator|)
condition|?
name|node
operator|.
name|getSchemaUpdateStrategyName
argument_list|()
else|:
literal|null
return|;
block|}
specifier|public
name|void
name|setSchemaUpdateStrategy
parameter_list|(
name|String
name|schemaUpdateStrategy
parameter_list|)
block|{
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|setSchemaUpdateStrategyName
argument_list|(
name|schemaUpdateStrategy
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getNodeName
parameter_list|()
block|{
return|return
operator|(
name|node
operator|!=
literal|null
operator|)
condition|?
name|node
operator|.
name|getName
argument_list|()
else|:
literal|null
return|;
block|}
specifier|public
name|void
name|setNodeName
parameter_list|(
name|String
name|newName
parameter_list|)
block|{
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// validate...
if|if
condition|(
name|newName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Empty DataNode Name"
argument_list|)
throw|;
block|}
name|ProjectController
name|parent
init|=
operator|(
name|ProjectController
operator|)
name|getParent
argument_list|()
decl_stmt|;
name|Configuration
name|config
init|=
operator|(
operator|(
name|ApplicationProject
operator|)
name|parent
operator|.
name|getProject
argument_list|()
operator|)
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|DataNode
name|matchingNode
init|=
literal|null
decl_stmt|;
for|for
control|(
name|DataDomain
name|domain
range|:
name|config
operator|.
name|getDomains
argument_list|()
control|)
block|{
name|DataNode
name|nextNode
init|=
name|domain
operator|.
name|getNode
argument_list|(
name|newName
argument_list|)
decl_stmt|;
if|if
condition|(
name|nextNode
operator|==
name|node
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|nextNode
operator|!=
literal|null
condition|)
block|{
name|matchingNode
operator|=
name|nextNode
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|matchingNode
operator|!=
literal|null
condition|)
block|{
comment|// there is an entity with the same name
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"There is another DataNode named '"
operator|+
name|newName
operator|+
literal|"'. Use a different name."
argument_list|)
throw|;
block|}
comment|// passed validation, set value...
comment|// TODO: fixme....there is a slight chance that domain is different than the one
comment|// cached node belongs to
name|ProjectUtil
operator|.
name|setDataNodeName
argument_list|(
name|parent
operator|.
name|getCurrentDataDomain
argument_list|()
argument_list|,
name|node
argument_list|,
name|newName
argument_list|)
expr_stmt|;
block|}
comment|// ======== other stuff
specifier|protected
name|void
name|initController
parameter_list|()
block|{
name|view
operator|.
name|getDataSourceDetail
argument_list|()
operator|.
name|add
argument_list|(
name|defaultSubeditor
operator|.
name|getView
argument_list|()
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
name|view
operator|.
name|getFactories
argument_list|()
operator|.
name|setEditable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// init combo box choices
name|view
operator|.
name|getFactories
argument_list|()
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|standardDataSourceFactories
argument_list|)
argument_list|)
expr_stmt|;
name|view
operator|.
name|getSchemaUpdateStrategy
argument_list|()
operator|.
name|setEditable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|view
operator|.
name|getSchemaUpdateStrategy
argument_list|()
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|standardSchemaUpdateStrategy
argument_list|)
argument_list|)
expr_stmt|;
comment|// init listeners
operator|(
operator|(
name|ProjectController
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|addDataNodeDisplayListener
argument_list|(
operator|new
name|DataNodeDisplayListener
argument_list|()
block|{
specifier|public
name|void
name|currentDataNodeChanged
parameter_list|(
name|DataNodeDisplayEvent
name|e
parameter_list|)
block|{
name|refreshView
argument_list|(
name|e
operator|.
name|getDataNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|getView
argument_list|()
operator|.
name|addComponentListener
argument_list|(
operator|new
name|ComponentAdapter
argument_list|()
block|{
specifier|public
name|void
name|componentShown
parameter_list|(
name|ComponentEvent
name|e
parameter_list|)
block|{
name|refreshView
argument_list|(
name|node
operator|!=
literal|null
condition|?
name|node
else|:
operator|(
operator|(
name|ProjectController
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|getCurrentDataNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
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
name|localDataSourceBinding
operator|=
name|builder
operator|.
name|bindToComboSelection
argument_list|(
name|view
operator|.
name|getLocalDataSources
argument_list|()
argument_list|,
literal|"parent.dataNodePreferences.localDataSource"
argument_list|,
name|NO_LOCAL_DATA_SOURCE
argument_list|)
expr_stmt|;
comment|// use delegate for the rest of them
name|builder
operator|.
name|setDelegate
argument_list|(
name|nodeChangeProcessor
argument_list|)
expr_stmt|;
name|bindings
operator|=
operator|new
name|ObjectBinding
index|[
literal|3
index|]
expr_stmt|;
name|bindings
index|[
literal|0
index|]
operator|=
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getDataNodeName
argument_list|()
argument_list|,
literal|"nodeName"
argument_list|)
expr_stmt|;
name|bindings
index|[
literal|1
index|]
operator|=
name|builder
operator|.
name|bindToComboSelection
argument_list|(
name|view
operator|.
name|getFactories
argument_list|()
argument_list|,
literal|"factoryName"
argument_list|)
expr_stmt|;
name|bindings
index|[
literal|2
index|]
operator|=
name|builder
operator|.
name|bindToComboSelection
argument_list|(
name|view
operator|.
name|getSchemaUpdateStrategy
argument_list|()
argument_list|,
literal|"schemaUpdateStrategy"
argument_list|)
expr_stmt|;
comment|// one way bindings
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getConfigLocalDataSources
argument_list|()
argument_list|,
literal|"dataSourceConfigAction()"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dataSourceConfigAction
parameter_list|()
block|{
name|PreferenceDialog
name|prefs
init|=
operator|new
name|PreferenceDialog
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|prefs
operator|.
name|showDataSourceEditorAction
argument_list|(
name|view
operator|.
name|getLocalDataSources
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
argument_list|)
expr_stmt|;
name|refreshLocalDataSources
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|refreshLocalDataSources
parameter_list|()
block|{
name|localDataSources
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Collection
name|sources
init|=
name|getApplication
argument_list|()
operator|.
name|getPreferenceDomain
argument_list|()
operator|.
name|getDetails
argument_list|(
name|DBConnectionInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|len
init|=
name|sources
operator|.
name|size
argument_list|()
decl_stmt|;
name|Object
index|[]
name|keys
init|=
operator|new
name|Object
index|[
name|len
operator|+
literal|1
index|]
decl_stmt|;
comment|// a slight chance that a real datasource is called NO_LOCAL_DATA_SOURCE...
name|keys
index|[
literal|0
index|]
operator|=
name|NO_LOCAL_DATA_SOURCE
expr_stmt|;
name|Iterator
name|it
init|=
name|sources
operator|.
name|iterator
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|len
condition|;
name|i
operator|++
control|)
block|{
name|DBConnectionInfo
name|info
init|=
operator|(
name|DBConnectionInfo
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|keys
index|[
name|i
index|]
operator|=
name|info
operator|.
name|getKey
argument_list|()
expr_stmt|;
name|localDataSources
operator|.
name|put
argument_list|(
name|keys
index|[
name|i
index|]
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
name|view
operator|.
name|getLocalDataSources
argument_list|()
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|keys
argument_list|)
argument_list|)
expr_stmt|;
name|localDataSourceBinding
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
comment|/**      * Reinitializes widgets to display selected DataNode.      */
specifier|protected
name|void
name|refreshView
parameter_list|(
name|DataNode
name|node
parameter_list|)
block|{
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
name|getView
argument_list|()
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
name|refreshLocalDataSources
argument_list|()
expr_stmt|;
for|for
control|(
name|ObjectBinding
name|binding
range|:
name|bindings
control|)
block|{
name|binding
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
name|showDataSourceSubview
argument_list|(
name|getFactoryName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Selects a subview for a currently selected DataSource factory.      */
specifier|protected
name|void
name|showDataSourceSubview
parameter_list|(
name|String
name|factoryName
parameter_list|)
block|{
name|DataSourceEditor
name|c
init|=
operator|(
name|DataSourceEditor
operator|)
name|datasourceEditors
operator|.
name|get
argument_list|(
name|factoryName
argument_list|)
decl_stmt|;
comment|// create subview dynamically...
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|DriverDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|factoryName
argument_list|)
condition|)
block|{
name|c
operator|=
operator|new
name|JDBCDataSourceEditor
argument_list|(
operator|(
name|ProjectController
operator|)
name|getParent
argument_list|()
argument_list|,
name|nodeChangeProcessor
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|JNDIDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|factoryName
argument_list|)
condition|)
block|{
name|c
operator|=
operator|new
name|JNDIDataSourceEditor
argument_list|(
operator|(
name|ProjectController
operator|)
name|getParent
argument_list|()
argument_list|,
name|nodeChangeProcessor
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|DBCPDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|factoryName
argument_list|)
condition|)
block|{
name|c
operator|=
operator|new
name|DBCPDataSourceEditor
argument_list|(
operator|(
name|ProjectController
operator|)
name|getParent
argument_list|()
argument_list|,
name|nodeChangeProcessor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// special case - no detail view, just show it and bail..
name|defaultSubeditor
operator|.
name|setNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|disabledTab
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|view
operator|.
name|getDataSourceDetailLayout
argument_list|()
operator|.
name|show
argument_list|(
name|view
operator|.
name|getDataSourceDetail
argument_list|()
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
return|return;
block|}
name|datasourceEditors
operator|.
name|put
argument_list|(
name|factoryName
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|view
operator|.
name|getDataSourceDetail
argument_list|()
operator|.
name|add
argument_list|(
name|c
operator|.
name|getView
argument_list|()
argument_list|,
name|factoryName
argument_list|)
expr_stmt|;
comment|// this is needed to display freshly added panel...
name|view
operator|.
name|getDataSourceDetail
argument_list|()
operator|.
name|getParent
argument_list|()
operator|.
name|validate
argument_list|()
expr_stmt|;
block|}
comment|// this will refresh subview...
name|c
operator|.
name|setNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|disabledTab
argument_list|(
name|factoryName
argument_list|)
expr_stmt|;
comment|// display the right subview...
name|view
operator|.
name|getDataSourceDetailLayout
argument_list|()
operator|.
name|show
argument_list|(
name|view
operator|.
name|getDataSourceDetail
argument_list|()
argument_list|,
name|factoryName
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|disabledTab
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|standardDataSourceFactories
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|tabbedPaneController
operator|.
name|getTabComponent
argument_list|()
operator|.
name|setEnabledAt
argument_list|(
literal|2
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tabbedPaneController
operator|.
name|getTabComponent
argument_list|()
operator|.
name|setEnabledAt
argument_list|(
literal|2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

