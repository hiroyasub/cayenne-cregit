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
name|ArrayList
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
name|table
operator|.
name|AbstractTableModel
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
name|CodeTemplateManager
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
name|PreferenceDetail
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
name|swing
operator|.
name|TableBindingBuilder
import|;
end_import

begin_class
specifier|public
class|class
name|TemplatePreferences
extends|extends
name|CayenneController
block|{
specifier|protected
name|TemplatePreferencesView
name|view
decl_stmt|;
specifier|protected
name|PreferenceEditor
name|editor
decl_stmt|;
specifier|protected
name|List
name|templateEntries
decl_stmt|;
specifier|protected
name|ObjectBinding
name|tableBinding
decl_stmt|;
specifier|public
name|TemplatePreferences
parameter_list|(
name|PreferenceDialog
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
name|TemplatePreferencesView
argument_list|()
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
name|templateEntries
operator|=
operator|new
name|ArrayList
argument_list|(
name|getTemplateDomain
argument_list|()
operator|.
name|getDetails
argument_list|(
name|FSPath
operator|.
name|class
argument_list|)
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
name|Domain
name|getTemplateDomain
parameter_list|()
block|{
name|Domain
name|domain
init|=
name|CodeTemplateManager
operator|.
name|getTemplateDomain
argument_list|(
name|getApplication
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|editor
operator|.
name|editableInstance
argument_list|(
name|domain
argument_list|)
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
name|getAddButton
argument_list|()
argument_list|,
literal|"addTemplateAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getRemoveButton
argument_list|()
argument_list|,
literal|"removeTemplateAction()"
argument_list|)
expr_stmt|;
name|TableBindingBuilder
name|tableBuilder
init|=
operator|new
name|TableBindingBuilder
argument_list|(
name|builder
argument_list|)
decl_stmt|;
name|tableBuilder
operator|.
name|addColumn
argument_list|(
literal|"Name"
argument_list|,
literal|"#item.key"
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|false
argument_list|,
literal|"XXXXXXXXXXXXXXX"
argument_list|)
expr_stmt|;
name|tableBuilder
operator|.
name|addColumn
argument_list|(
literal|"Path"
argument_list|,
literal|"#item.path"
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|false
argument_list|,
literal|"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
argument_list|)
expr_stmt|;
name|tableBuilder
operator|.
name|bindToTable
argument_list|(
name|view
operator|.
name|getTable
argument_list|()
argument_list|,
literal|"templateEntries"
argument_list|)
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
specifier|public
name|List
name|getTemplateEntries
parameter_list|()
block|{
return|return
name|templateEntries
return|;
block|}
specifier|public
name|PreferenceEditor
name|getEditor
parameter_list|()
block|{
return|return
name|editor
return|;
block|}
specifier|public
name|void
name|addTemplateAction
parameter_list|()
block|{
name|FSPath
name|path
init|=
operator|new
name|TemplateCreator
argument_list|(
name|this
argument_list|)
operator|.
name|startupAction
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|int
name|len
init|=
name|templateEntries
operator|.
name|size
argument_list|()
decl_stmt|;
name|templateEntries
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
operator|(
operator|(
name|AbstractTableModel
operator|)
name|view
operator|.
name|getTable
argument_list|()
operator|.
name|getModel
argument_list|()
operator|)
operator|.
name|fireTableRowsInserted
argument_list|(
name|len
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|removeTemplateAction
parameter_list|()
block|{
name|int
name|selected
init|=
name|view
operator|.
name|getTable
argument_list|()
operator|.
name|getSelectedRow
argument_list|()
decl_stmt|;
if|if
condition|(
name|selected
operator|<
literal|0
condition|)
block|{
return|return;
block|}
name|PreferenceDetail
name|selection
init|=
operator|(
name|PreferenceDetail
operator|)
name|templateEntries
operator|.
name|remove
argument_list|(
name|selected
argument_list|)
decl_stmt|;
name|editor
operator|.
name|deleteDetail
argument_list|(
name|getTemplateDomain
argument_list|()
argument_list|,
name|selection
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|AbstractTableModel
operator|)
name|view
operator|.
name|getTable
argument_list|()
operator|.
name|getModel
argument_list|()
operator|)
operator|.
name|fireTableRowsDeleted
argument_list|(
name|selected
argument_list|,
name|selected
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

