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
name|EntityDisplayEvent
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
name|ProjectPath
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|CreateDbEntityAction
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
literal|"Create DbEntity"
return|;
block|}
comment|/**      * Constructor for CreateDbEntityAction.      */
specifier|public
name|CreateDbEntityAction
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
literal|"icon-dbentity.gif"
return|;
block|}
comment|/**      * Creates new DbEntity, adds it to the current DataMap, fires DbEntityEvent and      * DbEntityDisplayEvent.      *       * @see org.apache.cayenne.modeler.util.CayenneAction#performAction(ActionEvent)      */
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
name|DbEntity
name|entity
init|=
name|createEntity
argument_list|(
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
decl_stmt|;
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
name|EntityDisplayEvent
name|displayEvent
init|=
operator|new
name|EntityDisplayEvent
argument_list|(
name|this
argument_list|,
name|entity
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|,
name|mediator
operator|.
name|getCurrentDataNode
argument_list|()
argument_list|,
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireDbEntityDisplayEvent
argument_list|(
name|displayEvent
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructs and returns a new DbEntity. Entity returned is added to the DataMap.      */
specifier|protected
name|DbEntity
name|createEntity
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|DbEntity
name|entity
init|=
operator|(
name|DbEntity
operator|)
name|NamedObjectFactory
operator|.
name|createObject
argument_list|(
name|DbEntity
operator|.
name|class
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setSchema
argument_list|(
name|map
operator|.
name|getDefaultSchema
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
return|return
name|entity
return|;
block|}
comment|/**      * Returns<code>true</code> if path contains a DataMap object.      */
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
name|DataMap
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

