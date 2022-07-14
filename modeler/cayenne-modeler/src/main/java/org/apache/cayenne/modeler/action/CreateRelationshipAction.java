begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|configuration
operator|.
name|ConfigurationNode
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
name|DbRelationship
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
name|ObjEntity
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
name|ObjRelationship
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
name|Relationship
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
name|event
operator|.
name|RelationshipEvent
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
name|DbRelationshipDialog
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
name|objentity
operator|.
name|ObjRelationshipInfo
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
name|RelationshipDisplayEvent
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
name|util
operator|.
name|DeleteRuleUpdater
import|;
end_import

begin_class
specifier|public
class|class
name|CreateRelationshipAction
extends|extends
name|CayenneAction
block|{
comment|/**      * Constructor for CreateRelationshipAction.      */
specifier|public
name|CreateRelationshipAction
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
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Create Relationship"
return|;
block|}
comment|/**      * Fires events when a obj rel was added      */
specifier|static
name|void
name|fireObjRelationshipEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|ProjectController
name|mediator
parameter_list|,
name|ObjEntity
name|objEntity
parameter_list|,
name|ObjRelationship
name|rel
parameter_list|)
block|{
name|mediator
operator|.
name|fireObjRelationshipEvent
argument_list|(
operator|new
name|RelationshipEvent
argument_list|(
name|src
argument_list|,
name|rel
argument_list|,
name|objEntity
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
name|RelationshipDisplayEvent
name|rde
init|=
operator|new
name|RelationshipDisplayEvent
argument_list|(
name|src
argument_list|,
name|rel
argument_list|,
name|objEntity
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
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
decl_stmt|;
name|mediator
operator|.
name|fireObjRelationshipDisplayEvent
argument_list|(
name|rde
argument_list|)
expr_stmt|;
block|}
comment|/**      * Fires events when a db rel was added      */
specifier|static
name|void
name|fireDbRelationshipEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|ProjectController
name|mediator
parameter_list|,
name|DbEntity
name|dbEntity
parameter_list|,
name|DbRelationship
name|rel
parameter_list|)
block|{
name|mediator
operator|.
name|fireDbRelationshipEvent
argument_list|(
operator|new
name|RelationshipEvent
argument_list|(
name|src
argument_list|,
name|rel
argument_list|,
name|dbEntity
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
name|RelationshipDisplayEvent
name|rde
init|=
operator|new
name|RelationshipDisplayEvent
argument_list|(
name|src
argument_list|,
name|rel
argument_list|,
name|dbEntity
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
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
decl_stmt|;
name|mediator
operator|.
name|fireDbRelationshipDisplayEvent
argument_list|(
name|rde
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-relationship.png"
return|;
block|}
comment|/**      * @see org.apache.cayenne.modeler.util.CayenneAction#performAction(ActionEvent)      */
annotation|@
name|Override
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|ObjEntity
name|objEnt
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentObjEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|objEnt
operator|!=
literal|null
condition|)
block|{
operator|new
name|ObjRelationshipInfo
argument_list|(
name|getProjectController
argument_list|()
argument_list|)
operator|.
name|createRelationship
argument_list|(
name|objEnt
argument_list|)
operator|.
name|startupAction
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|DbEntity
name|dbEnt
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbEnt
operator|!=
literal|null
condition|)
block|{
operator|new
name|DbRelationshipDialog
argument_list|(
name|getProjectController
argument_list|()
argument_list|)
operator|.
name|createNewRelationship
argument_list|(
name|dbEnt
argument_list|)
operator|.
name|startUp
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|createObjRelationship
parameter_list|(
name|ObjEntity
name|objEntity
parameter_list|,
name|ObjRelationship
name|rel
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|rel
operator|.
name|setSourceEntity
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
name|DeleteRuleUpdater
operator|.
name|updateObjRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|addRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
name|fireObjRelationshipEvent
argument_list|(
name|this
argument_list|,
name|mediator
argument_list|,
name|objEntity
argument_list|,
name|rel
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|createDbRelationship
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|,
name|DbRelationship
name|rel
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|dbEntity
operator|.
name|addRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
name|fireDbRelationshipEvent
argument_list|(
name|this
argument_list|,
name|mediator
argument_list|,
name|dbEntity
argument_list|,
name|rel
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if path contains an Entity object.      */
annotation|@
name|Override
specifier|public
name|boolean
name|enableForPath
parameter_list|(
name|ConfigurationNode
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Relationship
condition|)
block|{
return|return
operator|(
operator|(
name|Relationship
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
operator|)
operator|.
name|getParent
argument_list|()
operator|instanceof
name|Entity
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

