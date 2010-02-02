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
name|project
operator|.
name|NamedObjectFactory
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
name|AbstractQuery
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
name|EJBQLQuery
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
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|controller
operator|.
name|basic
operator|.
name|BasicController
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|core
operator|.
name|Control
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|core
operator|.
name|ControlException
import|;
end_import

begin_comment
comment|/**  * A Scope controller for QueryTypeDialog.  *   */
end_comment

begin_class
specifier|public
class|class
name|QueryTypeController
extends|extends
name|BasicController
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CANCEL_CONTROL
init|=
literal|"cayenne.modeler.queryType.cancel.button"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CREATE_CONTROL
init|=
literal|"cayenne.modeler.queryType.create.button"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OBJECT_QUERY_CONTROL
init|=
literal|"cayenne.modeler.queryType.selectQuery.radio"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SQL_QUERY_CONTROL
init|=
literal|"cayenne.modeler.queryType.sqlQuery.radio"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROCEDURE_QUERY_CONTROL
init|=
literal|"cayenne.modeler.queryType.procedureQuery.radio"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EJBQL_QUERY_CONTROL
init|=
literal|"cayenne.modeler.queryType.ejbqlQuery.radio"
decl_stmt|;
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
name|Query
name|query
decl_stmt|;
specifier|public
name|QueryTypeController
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
specifier|protected
name|void
name|doHandleControl
parameter_list|(
name|Control
name|control
parameter_list|)
throws|throws
name|ControlException
block|{
if|if
condition|(
name|control
operator|.
name|matchesID
argument_list|(
name|CANCEL_CONTROL
argument_list|)
condition|)
block|{
name|shutdown
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|control
operator|.
name|matchesID
argument_list|(
name|CREATE_CONTROL
argument_list|)
condition|)
block|{
name|createQuery
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|control
operator|.
name|matchesID
argument_list|(
name|OBJECT_QUERY_CONTROL
argument_list|)
condition|)
block|{
comment|// do nothing... need to match control
block|}
if|else if
condition|(
name|control
operator|.
name|matchesID
argument_list|(
name|SQL_QUERY_CONTROL
argument_list|)
condition|)
block|{
comment|// do nothing... need to match control
block|}
if|else if
condition|(
name|control
operator|.
name|matchesID
argument_list|(
name|PROCEDURE_QUERY_CONTROL
argument_list|)
condition|)
block|{
comment|// do nothing... need to match control
block|}
if|else if
condition|(
name|control
operator|.
name|matchesID
argument_list|(
name|EJBQL_QUERY_CONTROL
argument_list|)
condition|)
block|{
comment|// do nothing... need to match control
block|}
block|}
comment|/** 	 * Creates and runs QueryTypeDialog. 	 */
annotation|@
name|Override
specifier|public
name|void
name|startup
parameter_list|()
block|{
name|setModel
argument_list|(
operator|new
name|QueryTypeModel
argument_list|(
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setView
argument_list|(
operator|new
name|QueryTypeDialog
argument_list|()
argument_list|)
expr_stmt|;
name|showView
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * Action method that creates a query for the specified query type. 	 */
specifier|public
name|void
name|createQuery
parameter_list|()
block|{
name|QueryTypeModel
name|model
init|=
operator|(
name|QueryTypeModel
operator|)
name|getModel
argument_list|()
decl_stmt|;
name|Query
name|query
init|=
name|model
operator|.
name|getSelectedQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
comment|// wha?
return|return;
block|}
comment|// update query...
name|String
name|queryName
init|=
name|NamedObjectFactory
operator|.
name|createName
argument_list|(
name|Query
operator|.
name|class
argument_list|,
name|dataMap
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|instanceof
name|EJBQLQuery
condition|)
block|{
operator|(
operator|(
name|EJBQLQuery
operator|)
name|query
operator|)
operator|.
name|setName
argument_list|(
name|queryName
argument_list|)
expr_stmt|;
operator|(
operator|(
name|EJBQLQuery
operator|)
name|query
operator|)
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|(
operator|(
name|AbstractQuery
operator|)
name|query
operator|)
operator|.
name|setName
argument_list|(
name|queryName
argument_list|)
expr_stmt|;
operator|(
operator|(
name|AbstractQuery
operator|)
name|query
operator|)
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
block|}
name|dataMap
operator|.
name|addQuery
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
name|shutdown
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * Fires events when a query was added 	 */
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
name|Query
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
block|}
end_class

end_unit

