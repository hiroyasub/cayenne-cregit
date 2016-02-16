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
name|query
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
name|javax
operator|.
name|swing
operator|.
name|WindowConstants
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
name|undo
operator|.
name|CreateQueryUndoableEdit
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
name|query
operator|.
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|QueryType
extends|extends
name|CayenneController
block|{
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
name|DataChannelDescriptor
name|domain
decl_stmt|;
specifier|protected
name|QueryTypeView
name|view
decl_stmt|;
specifier|protected
name|String
name|type
decl_stmt|;
specifier|public
name|QueryType
parameter_list|(
name|ProjectController
name|mediator
parameter_list|,
name|DataMap
name|root
parameter_list|)
block|{
name|super
argument_list|(
name|mediator
argument_list|)
expr_stmt|;
name|view
operator|=
operator|new
name|QueryTypeView
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
comment|// by default use object query...
name|this
operator|.
name|type
operator|=
name|QueryDescriptor
operator|.
name|SELECT_QUERY
expr_stmt|;
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|domain
operator|=
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
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|private
name|void
name|initController
parameter_list|()
block|{
name|view
operator|.
name|getCancelButton
argument_list|()
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
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getSaveButton
argument_list|()
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
name|createQuery
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getObjectSelect
argument_list|()
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
name|setObjectSelectQuery
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getSqlSelect
argument_list|()
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
name|setRawSQLQuery
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getProcedureSelect
argument_list|()
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
name|setProcedureQuery
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getEjbqlSelect
argument_list|()
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
name|setEjbqlQuery
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|startupAction
parameter_list|()
block|{
name|view
operator|.
name|pack
argument_list|()
expr_stmt|;
name|view
operator|.
name|setDefaultCloseOperation
argument_list|(
name|WindowConstants
operator|.
name|DISPOSE_ON_CLOSE
argument_list|)
expr_stmt|;
name|view
operator|.
name|setModal
argument_list|(
literal|true
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
block|}
comment|/**      * Action method that creates a query for the specified query type.      */
specifier|public
name|void
name|createQuery
parameter_list|()
block|{
name|String
name|queryType
init|=
name|getSelectedQuery
argument_list|()
decl_stmt|;
comment|// update query...
name|String
name|queryName
init|=
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|query
argument_list|,
name|dataMap
argument_list|)
decl_stmt|;
name|QueryDescriptor
name|query
init|=
name|QueryDescriptor
operator|.
name|descriptor
argument_list|(
name|queryType
argument_list|)
decl_stmt|;
name|query
operator|.
name|setName
argument_list|(
name|queryName
argument_list|)
expr_stmt|;
name|query
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|addQueryDescriptor
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|getApplication
argument_list|()
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|CreateQueryUndoableEdit
argument_list|(
name|domain
argument_list|,
name|dataMap
argument_list|,
name|query
argument_list|)
argument_list|)
expr_stmt|;
comment|// notify listeners
name|fireQueryEvent
argument_list|(
name|this
argument_list|,
name|mediator
argument_list|,
name|dataMap
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
comment|/**      * Fires events when a query was added      */
specifier|public
specifier|static
name|void
name|fireQueryEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|ProjectController
name|mediator
parameter_list|,
name|DataMap
name|dataMap
parameter_list|,
name|QueryDescriptor
name|query
parameter_list|)
block|{
name|mediator
operator|.
name|fireQueryEvent
argument_list|(
operator|new
name|QueryEvent
argument_list|(
name|src
argument_list|,
name|query
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|,
name|dataMap
argument_list|)
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireQueryDisplayEvent
argument_list|(
operator|new
name|QueryDisplayEvent
argument_list|(
name|src
argument_list|,
name|query
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getSelectedQuery
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setObjectSelectQuery
parameter_list|()
block|{
name|this
operator|.
name|type
operator|=
name|QueryDescriptor
operator|.
name|SELECT_QUERY
expr_stmt|;
block|}
specifier|public
name|void
name|setRawSQLQuery
parameter_list|()
block|{
name|this
operator|.
name|type
operator|=
name|QueryDescriptor
operator|.
name|SQL_TEMPLATE
expr_stmt|;
block|}
specifier|public
name|void
name|setProcedureQuery
parameter_list|()
block|{
name|this
operator|.
name|type
operator|=
name|QueryDescriptor
operator|.
name|PROCEDURE_QUERY
expr_stmt|;
block|}
specifier|public
name|void
name|setEjbqlQuery
parameter_list|()
block|{
name|this
operator|.
name|type
operator|=
name|QueryDescriptor
operator|.
name|EJBQL_QUERY
expr_stmt|;
block|}
block|}
end_class

end_unit

