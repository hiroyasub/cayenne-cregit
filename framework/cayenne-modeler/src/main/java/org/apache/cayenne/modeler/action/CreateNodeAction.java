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
name|event
operator|.
name|ActionEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|ModelerDbAdapter
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
name|project
operator|.
name|ProjectDataSource
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
name|ProjectPath
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|CreateNodeAction
extends|extends
name|CayenneAction
block|{
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Create DataNode"
return|;
block|}
comment|/**      * Constructor for CreateNodeAction.      *       * @param name      */
specifier|public
name|CreateNodeAction
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
literal|"icon-node.gif"
return|;
block|}
comment|/**      * @see org.apache.cayenne.modeler.util.CayenneAction#performAction(ActionEvent)      */
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DataDomain
name|domain
init|=
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
decl_stmt|;
comment|// use domain name as DataNode base, as node names must be unique across the
comment|// project...
name|DataNode
name|node
init|=
name|buildDataNode
argument_list|()
decl_stmt|;
name|fireDataNodeEvent
argument_list|(
name|this
argument_list|,
name|mediator
argument_list|,
name|domain
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
comment|/**      * Fires events when a obj entity was added      */
specifier|static
name|void
name|fireDataNodeEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|ProjectController
name|mediator
parameter_list|,
name|DataDomain
name|domain
parameter_list|,
name|DataNode
name|node
parameter_list|)
block|{
name|mediator
operator|.
name|fireDataNodeEvent
argument_list|(
operator|new
name|DataNodeEvent
argument_list|(
name|src
argument_list|,
name|node
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireDataNodeDisplayEvent
argument_list|(
operator|new
name|DataNodeDisplayEvent
argument_list|(
name|src
argument_list|,
name|domain
argument_list|,
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new DataNode, adding to the current domain, but doesn't send any events.      */
specifier|protected
name|DataNode
name|buildDataNode
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DataDomain
name|domain
init|=
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
decl_stmt|;
comment|// use domain name as DataNode base, as node names must be unique across the
comment|// project...
name|DataNode
name|node
init|=
name|createDataNode
argument_list|(
name|domain
argument_list|)
decl_stmt|;
name|ProjectDataSource
name|src
init|=
operator|new
name|ProjectDataSource
argument_list|(
operator|new
name|DataSourceInfo
argument_list|()
argument_list|)
decl_stmt|;
name|node
operator|.
name|setDataSource
argument_list|(
name|src
argument_list|)
expr_stmt|;
name|node
operator|.
name|setAdapter
argument_list|(
operator|new
name|ModelerDbAdapter
argument_list|(
name|src
argument_list|)
argument_list|)
expr_stmt|;
comment|// by default create JDBC Node
name|node
operator|.
name|setDataSourceFactory
argument_list|(
name|DriverDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|domain
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
comment|/**      * A factory method that makes a new DataNode.      */
specifier|protected
name|DataNode
name|createDataNode
parameter_list|(
name|DataDomain
name|domain
parameter_list|)
block|{
name|String
name|name
init|=
name|NamedObjectFactory
operator|.
name|createName
argument_list|(
name|DataNode
operator|.
name|class
argument_list|,
name|domain
argument_list|,
name|domain
operator|.
name|getName
argument_list|()
operator|+
literal|"Node"
argument_list|)
decl_stmt|;
comment|// ensure that DataNode exposes DataSource directly, so that UI widgets could work
comment|// with it.
return|return
operator|new
name|DataNode
argument_list|(
name|name
argument_list|)
block|{
specifier|public
name|DataSource
name|getDataSource
parameter_list|()
block|{
return|return
name|dataSource
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns<code>true</code> if path contains a DataDomain object.      */
specifier|public
name|boolean
name|enableForPath
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|DataDomain
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

