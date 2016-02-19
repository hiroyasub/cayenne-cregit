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
name|javax
operator|.
name|swing
operator|.
name|JScrollPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTabbedPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|ChangeEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|ChangeListener
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
name|QueryDisplayEvent
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
name|QueryDisplayListener
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
name|QueryDescriptor
import|;
end_import

begin_class
specifier|public
class|class
name|EjbqlTabbedView
extends|extends
name|JTabbedPane
block|{
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|protected
name|EjbqlQueryMainTab
name|mainTab
decl_stmt|;
specifier|protected
name|EjbqlQueryScriptsTab
name|scriptsTab
decl_stmt|;
specifier|protected
name|int
name|lastSelectionIndex
decl_stmt|;
specifier|public
name|EjbqlTabbedView
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
name|initView
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initView
parameter_list|()
block|{
name|setTabPlacement
argument_list|(
name|JTabbedPane
operator|.
name|TOP
argument_list|)
expr_stmt|;
name|this
operator|.
name|mainTab
operator|=
operator|new
name|EjbqlQueryMainTab
argument_list|(
name|mediator
argument_list|)
expr_stmt|;
name|addTab
argument_list|(
literal|"General"
argument_list|,
operator|new
name|JScrollPane
argument_list|(
name|mainTab
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|scriptsTab
operator|=
operator|new
name|EjbqlQueryScriptsTab
argument_list|(
name|mediator
argument_list|)
expr_stmt|;
name|addTab
argument_list|(
literal|"EJBQL"
argument_list|,
name|scriptsTab
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initController
parameter_list|()
block|{
name|mediator
operator|.
name|addQueryDisplayListener
argument_list|(
operator|new
name|QueryDisplayListener
argument_list|()
block|{
specifier|public
name|void
name|currentQueryChanged
parameter_list|(
name|QueryDisplayEvent
name|e
parameter_list|)
block|{
name|initFromModel
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|this
operator|.
name|addChangeListener
argument_list|(
operator|new
name|ChangeListener
argument_list|()
block|{
specifier|public
name|void
name|stateChanged
parameter_list|(
name|ChangeEvent
name|e
parameter_list|)
block|{
name|lastSelectionIndex
operator|=
name|getSelectedIndex
argument_list|()
expr_stmt|;
name|updateTabs
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|void
name|initFromModel
parameter_list|()
block|{
if|if
condition|(
operator|!
name|QueryDescriptor
operator|.
name|EJBQL_QUERY
operator|.
name|equals
argument_list|(
name|mediator
operator|.
name|getCurrentQuery
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// tab did not change - force update
if|if
condition|(
name|getSelectedIndex
argument_list|()
operator|==
name|lastSelectionIndex
condition|)
block|{
name|updateTabs
argument_list|()
expr_stmt|;
block|}
comment|// change tab, this will update newly displayed tab...
else|else
block|{
name|setSelectedIndex
argument_list|(
name|lastSelectionIndex
argument_list|)
expr_stmt|;
block|}
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|void
name|updateTabs
parameter_list|()
block|{
switch|switch
condition|(
name|lastSelectionIndex
condition|)
block|{
case|case
literal|0
case|:
name|mainTab
operator|.
name|initFromModel
argument_list|()
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|scriptsTab
operator|.
name|initFromModel
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
end_class

end_unit

