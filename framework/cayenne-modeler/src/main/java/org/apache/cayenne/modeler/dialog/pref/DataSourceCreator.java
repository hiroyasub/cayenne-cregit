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
name|JDialog
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
name|DbAdapterInfo
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DataSourceCreator
extends|extends
name|CayenneController
block|{
specifier|private
specifier|static
specifier|final
name|String
name|NO_ADAPTER
init|=
literal|"Custom / Undefined"
decl_stmt|;
specifier|protected
name|DataSourceCreatorView
name|view
decl_stmt|;
specifier|protected
name|PreferenceEditor
name|editor
decl_stmt|;
specifier|protected
name|Domain
name|domain
decl_stmt|;
specifier|protected
name|boolean
name|canceled
decl_stmt|;
specifier|protected
name|Map
name|dataSources
decl_stmt|;
specifier|public
name|DataSourceCreator
parameter_list|(
name|DataSourcePreferences
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
name|view
operator|=
operator|new
name|DataSourceCreatorView
argument_list|(
operator|(
name|JDialog
operator|)
name|SwingUtilities
operator|.
name|getWindowAncestor
argument_list|(
name|parent
operator|.
name|getView
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|editor
operator|=
name|parent
operator|.
name|getEditor
argument_list|()
expr_stmt|;
name|this
operator|.
name|domain
operator|=
name|parent
operator|.
name|getDataSourceDomain
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataSources
operator|=
name|parent
operator|.
name|getDataSources
argument_list|()
expr_stmt|;
name|DefaultComboBoxModel
name|model
init|=
operator|new
name|DefaultComboBoxModel
argument_list|(
name|DbAdapterInfo
operator|.
name|getStandardAdapters
argument_list|()
argument_list|)
decl_stmt|;
name|model
operator|.
name|insertElementAt
argument_list|(
name|NO_ADAPTER
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|.
name|getAdapters
argument_list|()
operator|.
name|setModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|.
name|getAdapters
argument_list|()
operator|.
name|setSelectedIndex
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|String
name|suggestion
init|=
literal|"DataSource0"
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
name|dataSources
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|suggestion
operator|=
literal|"DataSource"
operator|+
name|i
expr_stmt|;
if|if
condition|(
operator|!
name|dataSources
operator|.
name|containsKey
argument_list|(
name|suggestion
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
name|this
operator|.
name|view
operator|.
name|getDataSourceName
argument_list|()
operator|.
name|setText
argument_list|(
name|suggestion
argument_list|)
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
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
name|getCancelButton
argument_list|()
argument_list|,
literal|"cancelAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getOkButton
argument_list|()
argument_list|,
literal|"okAction()"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|okAction
parameter_list|()
block|{
if|if
condition|(
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|view
argument_list|,
literal|"Enter DataSource Name"
argument_list|,
literal|null
argument_list|,
name|JOptionPane
operator|.
name|WARNING_MESSAGE
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|dataSources
operator|.
name|containsKey
argument_list|(
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|view
argument_list|,
literal|"'"
operator|+
name|getName
argument_list|()
operator|+
literal|"' is already in use, enter a different name"
argument_list|,
literal|null
argument_list|,
name|JOptionPane
operator|.
name|WARNING_MESSAGE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|canceled
operator|=
literal|false
expr_stmt|;
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|cancelAction
parameter_list|()
block|{
name|canceled
operator|=
literal|true
expr_stmt|;
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
comment|/**      * Pops up a dialog and blocks current thread until the dialog is closed.      */
specifier|public
name|DBConnectionInfo
name|startupAction
parameter_list|()
block|{
comment|// this should handle closing via ESC
name|canceled
operator|=
literal|true
expr_stmt|;
name|view
operator|.
name|setModal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|view
operator|.
name|pack
argument_list|()
expr_stmt|;
name|view
operator|.
name|setResizable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|makeCloseableOnEscape
argument_list|()
expr_stmt|;
name|centerView
argument_list|()
expr_stmt|;
name|view
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|createDataSource
argument_list|()
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
name|String
name|name
init|=
name|view
operator|.
name|getDataSourceName
argument_list|()
operator|.
name|getText
argument_list|()
decl_stmt|;
return|return
operator|(
name|name
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
condition|?
name|name
else|:
literal|null
return|;
block|}
specifier|protected
name|DBConnectionInfo
name|createDataSource
parameter_list|()
block|{
if|if
condition|(
name|canceled
condition|)
block|{
return|return
literal|null
return|;
block|}
name|DBConnectionInfo
name|dataSource
init|=
operator|(
name|DBConnectionInfo
operator|)
name|getApplication
argument_list|()
operator|.
name|getCayenneProjectPreferences
argument_list|()
operator|.
name|getDetailObject
argument_list|(
name|DBConnectionInfo
operator|.
name|class
argument_list|)
operator|.
name|create
argument_list|(
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|adapter
init|=
name|view
operator|.
name|getAdapters
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
if|if
condition|(
name|NO_ADAPTER
operator|.
name|equals
argument_list|(
name|adapter
argument_list|)
condition|)
block|{
name|adapter
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|adapter
operator|!=
literal|null
condition|)
block|{
name|String
name|adapterString
init|=
name|adapter
operator|.
name|toString
argument_list|()
decl_stmt|;
name|dataSource
operator|.
name|setDbAdapter
argument_list|(
name|adapterString
argument_list|)
expr_stmt|;
comment|// guess adapter defaults...
name|AdapterMapping
name|defaultMap
init|=
name|getApplication
argument_list|()
operator|.
name|getAdapterMapping
argument_list|()
decl_stmt|;
name|dataSource
operator|.
name|setJdbcDriver
argument_list|(
name|defaultMap
operator|.
name|jdbcDriverForAdapter
argument_list|(
name|adapterString
argument_list|)
argument_list|)
expr_stmt|;
name|dataSource
operator|.
name|setUrl
argument_list|(
name|defaultMap
operator|.
name|jdbcURLForAdapter
argument_list|(
name|adapterString
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|dataSource
return|;
block|}
block|}
end_class

end_unit

