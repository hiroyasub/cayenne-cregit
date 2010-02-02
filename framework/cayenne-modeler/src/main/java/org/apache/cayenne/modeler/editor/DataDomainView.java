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
name|java
operator|.
name|awt
operator|.
name|BorderLayout
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
name|awt
operator|.
name|event
operator|.
name|ActionListener
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
name|javax
operator|.
name|swing
operator|.
name|JButton
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JCheckBox
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JComboBox
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTextField
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
name|DataRowStore
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
name|cache
operator|.
name|MapQueryCacheFactory
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
name|cache
operator|.
name|OSQueryCacheFactory
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
name|DomainEvent
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
name|datadomain
operator|.
name|CacheSyncConfigController
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
name|DomainDisplayEvent
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
name|DomainDisplayListener
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
name|CayenneWidgetFactory
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
name|modeler
operator|.
name|util
operator|.
name|TextAdapter
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
name|cayenne
operator|.
name|validation
operator|.
name|ValidationException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|builder
operator|.
name|PanelBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|CellConstraints
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|FormLayout
import|;
end_import

begin_comment
comment|/**  * Panel for editing DataDomain.  */
end_comment

begin_class
specifier|public
class|class
name|DataDomainView
extends|extends
name|JPanel
implements|implements
name|DomainDisplayListener
block|{
specifier|final
specifier|static
name|String
index|[]
name|QUERY_CACHE_FACTORIES
init|=
operator|new
name|String
index|[]
block|{
name|MapQueryCacheFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|OSQueryCacheFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
decl_stmt|;
specifier|protected
name|ProjectController
name|projectController
decl_stmt|;
specifier|protected
name|TextAdapter
name|name
decl_stmt|;
specifier|protected
name|TextAdapter
name|cacheSize
decl_stmt|;
specifier|protected
name|JCheckBox
name|objectValidation
decl_stmt|;
specifier|protected
name|JCheckBox
name|externalTransactions
decl_stmt|;
specifier|protected
name|TextAdapter
name|dataContextFactory
decl_stmt|;
specifier|protected
name|JComboBox
name|queryCacheFactory
decl_stmt|;
specifier|protected
name|JCheckBox
name|sharedCache
decl_stmt|;
specifier|protected
name|JCheckBox
name|remoteUpdates
decl_stmt|;
specifier|protected
name|JButton
name|configRemoteUpdates
decl_stmt|;
specifier|public
name|DataDomainView
parameter_list|(
name|ProjectController
name|projectController
parameter_list|)
block|{
name|this
operator|.
name|projectController
operator|=
name|projectController
expr_stmt|;
comment|// Create and layout components
name|initView
argument_list|()
expr_stmt|;
comment|// hook up listeners to widgets
name|initController
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|initView
parameter_list|()
block|{
comment|// create widgets
name|this
operator|.
name|name
operator|=
operator|new
name|TextAdapter
argument_list|(
operator|new
name|JTextField
argument_list|()
argument_list|)
block|{
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|setDomainName
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|this
operator|.
name|cacheSize
operator|=
operator|new
name|TextAdapter
argument_list|(
operator|new
name|JTextField
argument_list|(
literal|10
argument_list|)
argument_list|)
block|{
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|setCacheSize
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|this
operator|.
name|dataContextFactory
operator|=
operator|new
name|TextAdapter
argument_list|(
operator|new
name|JTextField
argument_list|()
argument_list|)
block|{
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|setDomainProperty
argument_list|(
name|DataDomain
operator|.
name|DATA_CONTEXT_FACTORY_PROPERTY
argument_list|,
name|text
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|this
operator|.
name|objectValidation
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|externalTransactions
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|queryCacheFactory
operator|=
name|CayenneWidgetFactory
operator|.
name|createUndoableComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|sharedCache
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|remoteUpdates
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|configRemoteUpdates
operator|=
operator|new
name|JButton
argument_list|(
literal|"Configure..."
argument_list|)
expr_stmt|;
name|configRemoteUpdates
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// assemble
name|CellConstraints
name|cc
init|=
operator|new
name|CellConstraints
argument_list|()
decl_stmt|;
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"right:pref, 3dlu, fill:50dlu, 3dlu, fill:47dlu, 3dlu, fill:100"
argument_list|,
literal|"p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p"
argument_list|)
decl_stmt|;
name|PanelBuilder
name|builder
init|=
operator|new
name|PanelBuilder
argument_list|(
name|layout
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setDefaultDialogBorder
argument_list|()
expr_stmt|;
name|builder
operator|.
name|addSeparator
argument_list|(
literal|"DataDomain Configuration"
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|7
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"DataDomain Name:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|name
operator|.
name|getComponent
argument_list|()
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|3
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"DataContext Factory:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|dataContextFactory
operator|.
name|getComponent
argument_list|()
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Object Validation:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|objectValidation
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Container-Managed Transactions:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|9
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|externalTransactions
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|9
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addSeparator
argument_list|(
literal|"Cache Configuration"
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|11
argument_list|,
literal|7
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Query Cache Factory:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|13
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|queryCacheFactory
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|13
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Size of Object Cache:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|15
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|cacheSize
operator|.
name|getComponent
argument_list|()
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|15
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Use Shared Cache:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|17
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|sharedCache
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|17
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Remote Change Notifications:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|19
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|remoteUpdates
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|19
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|configRemoteUpdates
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|7
argument_list|,
literal|19
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|builder
operator|.
name|getPanel
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initController
parameter_list|()
block|{
name|projectController
operator|.
name|addDomainDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|queryCacheFactory
operator|.
name|setEditable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|queryCacheFactory
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|QUERY_CACHE_FACTORIES
argument_list|)
argument_list|)
expr_stmt|;
name|queryCacheFactory
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|setDomainProperty
argument_list|(
name|DataDomain
operator|.
name|QUERY_CACHE_FACTORY_PROPERTY
argument_list|,
operator|(
name|String
operator|)
name|queryCacheFactory
operator|.
name|getModel
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
argument_list|,
name|MapQueryCacheFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// add action listener to checkboxes
name|objectValidation
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|String
name|value
init|=
name|objectValidation
operator|.
name|isSelected
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
decl_stmt|;
name|setDomainProperty
argument_list|(
name|DataDomain
operator|.
name|VALIDATING_OBJECTS_ON_COMMIT_PROPERTY
argument_list|,
name|value
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|DataDomain
operator|.
name|VALIDATING_OBJECTS_ON_COMMIT_DEFAULT
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|externalTransactions
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|String
name|value
init|=
name|externalTransactions
operator|.
name|isSelected
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
decl_stmt|;
name|setDomainProperty
argument_list|(
name|DataDomain
operator|.
name|USING_EXTERNAL_TRANSACTIONS_PROPERTY
argument_list|,
name|value
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|DataDomain
operator|.
name|USING_EXTERNAL_TRANSACTIONS_DEFAULT
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|sharedCache
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|String
name|value
init|=
name|sharedCache
operator|.
name|isSelected
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
decl_stmt|;
name|setDomainProperty
argument_list|(
name|DataDomain
operator|.
name|SHARED_CACHE_ENABLED_PROPERTY
argument_list|,
name|value
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|DataDomain
operator|.
name|SHARED_CACHE_ENABLED_DEFAULT
argument_list|)
argument_list|)
expr_stmt|;
comment|// turning off shared cache should result in disabling remote events
name|remoteUpdates
operator|.
name|setEnabled
argument_list|(
name|sharedCache
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|sharedCache
operator|.
name|isSelected
argument_list|()
condition|)
block|{
comment|// uncheck remote updates...
name|remoteUpdates
operator|.
name|setSelected
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|setDomainProperty
argument_list|(
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_PROPERTY
argument_list|,
literal|"false"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_DEFAULT
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// depending on final remote updates status change button status
name|configRemoteUpdates
operator|.
name|setEnabled
argument_list|(
name|remoteUpdates
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|remoteUpdates
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|String
name|value
init|=
name|remoteUpdates
operator|.
name|isSelected
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
decl_stmt|;
comment|// update config button state
name|configRemoteUpdates
operator|.
name|setEnabled
argument_list|(
name|remoteUpdates
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
name|setDomainProperty
argument_list|(
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_PROPERTY
argument_list|,
name|value
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_DEFAULT
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|configRemoteUpdates
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
operator|new
name|CacheSyncConfigController
argument_list|(
name|projectController
argument_list|)
operator|.
name|startup
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Helper method that updates domain properties. If a value equals to default, null      * value is used instead.      */
specifier|protected
name|void
name|setDomainProperty
parameter_list|(
name|String
name|property
parameter_list|,
name|String
name|value
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|DataChannelDescriptor
name|domain
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|projectController
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// no empty strings
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|value
operator|=
literal|null
expr_stmt|;
block|}
comment|// use NULL for defaults
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|value
operator|.
name|equals
argument_list|(
name|defaultValue
argument_list|)
condition|)
block|{
name|value
operator|=
literal|null
expr_stmt|;
block|}
name|Map
name|properties
init|=
name|domain
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|Object
name|oldValue
init|=
name|properties
operator|.
name|get
argument_list|(
name|property
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|value
argument_list|,
name|oldValue
argument_list|)
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|property
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|DomainEvent
name|e
init|=
operator|new
name|DomainEvent
argument_list|(
name|this
argument_list|,
name|domain
argument_list|)
decl_stmt|;
name|projectController
operator|.
name|fireDomainEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getDomainProperty
parameter_list|(
name|String
name|property
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|DataChannelDescriptor
name|domain
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|projectController
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|value
init|=
operator|(
name|String
operator|)
name|domain
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|property
argument_list|)
decl_stmt|;
return|return
name|value
operator|!=
literal|null
condition|?
name|value
else|:
name|defaultValue
return|;
block|}
specifier|public
name|boolean
name|getDomainBooleanProperty
parameter_list|(
name|String
name|property
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
return|return
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|getDomainProperty
argument_list|(
name|property
argument_list|,
name|defaultValue
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Invoked on domain selection event. Updates view with the values from the currently      * selected domain.      */
specifier|public
name|void
name|currentDomainChanged
parameter_list|(
name|DomainDisplayEvent
name|e
parameter_list|)
block|{
name|DataChannelDescriptor
name|domain
init|=
name|e
operator|.
name|getDomain
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|domain
condition|)
block|{
return|return;
block|}
comment|// extract values from the new domain object
name|name
operator|.
name|setText
argument_list|(
name|domain
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|cacheSize
operator|.
name|setText
argument_list|(
name|getDomainProperty
argument_list|(
name|DataRowStore
operator|.
name|SNAPSHOT_CACHE_SIZE_PROPERTY
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|DataRowStore
operator|.
name|SNAPSHOT_CACHE_SIZE_DEFAULT
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|objectValidation
operator|.
name|setSelected
argument_list|(
name|getDomainBooleanProperty
argument_list|(
name|DataDomain
operator|.
name|VALIDATING_OBJECTS_ON_COMMIT_PROPERTY
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|DataDomain
operator|.
name|VALIDATING_OBJECTS_ON_COMMIT_DEFAULT
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|externalTransactions
operator|.
name|setSelected
argument_list|(
name|getDomainBooleanProperty
argument_list|(
name|DataDomain
operator|.
name|USING_EXTERNAL_TRANSACTIONS_PROPERTY
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|DataDomain
operator|.
name|USING_EXTERNAL_TRANSACTIONS_DEFAULT
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|dataContextFactory
operator|.
name|setText
argument_list|(
name|getDomainProperty
argument_list|(
name|DataDomain
operator|.
name|DATA_CONTEXT_FACTORY_PROPERTY
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|sharedCache
operator|.
name|setSelected
argument_list|(
name|getDomainBooleanProperty
argument_list|(
name|DataDomain
operator|.
name|SHARED_CACHE_ENABLED_PROPERTY
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|DataDomain
operator|.
name|SHARED_CACHE_ENABLED_DEFAULT
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|remoteUpdates
operator|.
name|setSelected
argument_list|(
name|getDomainBooleanProperty
argument_list|(
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_PROPERTY
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_DEFAULT
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|remoteUpdates
operator|.
name|setEnabled
argument_list|(
name|sharedCache
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
name|configRemoteUpdates
operator|.
name|setEnabled
argument_list|(
name|remoteUpdates
operator|.
name|isEnabled
argument_list|()
operator|&&
name|remoteUpdates
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
name|queryCacheFactory
operator|.
name|setSelectedItem
argument_list|(
name|getDomainProperty
argument_list|(
name|DataDomain
operator|.
name|QUERY_CACHE_FACTORY_PROPERTY
argument_list|,
name|MapQueryCacheFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|void
name|setDomainName
parameter_list|(
name|String
name|newName
parameter_list|)
block|{
if|if
condition|(
name|newName
operator|==
literal|null
operator|||
name|newName
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Enter name for DataDomain"
argument_list|)
throw|;
block|}
name|DataChannelDescriptor
name|dataChannelDescriptor
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|Application
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
name|Domain
name|prefs
init|=
name|projectController
operator|.
name|getPreferenceDomainForDataDomain
argument_list|()
decl_stmt|;
name|DomainEvent
name|e
init|=
operator|new
name|DomainEvent
argument_list|(
name|this
argument_list|,
name|dataChannelDescriptor
argument_list|,
name|dataChannelDescriptor
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|ProjectUtil
operator|.
name|setDataDomainName
argument_list|(
name|dataChannelDescriptor
argument_list|,
name|newName
argument_list|)
expr_stmt|;
name|prefs
operator|.
name|rename
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|projectController
operator|.
name|fireDomainEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|void
name|setCacheSize
parameter_list|(
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|Integer
operator|.
name|parseInt
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Cache size must be an integer: "
operator|+
name|text
argument_list|)
throw|;
block|}
block|}
name|setDomainProperty
argument_list|(
name|DataRowStore
operator|.
name|SNAPSHOT_CACHE_SIZE_PROPERTY
argument_list|,
name|text
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|DataRowStore
operator|.
name|SNAPSHOT_CACHE_SIZE_DEFAULT
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

