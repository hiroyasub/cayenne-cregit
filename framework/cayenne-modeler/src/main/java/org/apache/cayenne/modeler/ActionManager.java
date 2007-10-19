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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Collections
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
name|HashSet
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
name|Action
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
name|action
operator|.
name|*
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

begin_comment
comment|/**  * An object that manages CayenneModeler actions.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ActionManager
block|{
specifier|static
specifier|final
name|Collection
name|SPECIAL_ACTIONS
init|=
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|SaveAction
operator|.
name|getActionName
argument_list|()
block|,
name|RevertAction
operator|.
name|getActionName
argument_list|()
block|}
argument_list|)
argument_list|)
decl_stmt|;
comment|// search action added to project actions
specifier|static
specifier|final
name|Collection
name|PROJECT_ACTIONS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|CreateDomainAction
operator|.
name|getActionName
argument_list|()
block|,
name|ProjectAction
operator|.
name|getActionName
argument_list|()
block|,
name|ValidateAction
operator|.
name|getActionName
argument_list|()
block|,
name|SaveAsAction
operator|.
name|getActionName
argument_list|()
block|,
name|FindAction
operator|.
name|getActionName
argument_list|()
block|}
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|Collection
name|DOMAIN_ACTIONS
init|=
operator|new
name|HashSet
argument_list|(
name|PROJECT_ACTIONS
argument_list|)
decl_stmt|;
static|static
block|{
name|DOMAIN_ACTIONS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|ImportDataMapAction
operator|.
name|getActionName
argument_list|()
block|,
name|CreateDataMapAction
operator|.
name|getActionName
argument_list|()
block|,
name|RemoveAction
operator|.
name|getActionName
argument_list|()
block|,
name|CreateNodeAction
operator|.
name|getActionName
argument_list|()
block|,
name|ImportDBAction
operator|.
name|getActionName
argument_list|()
block|,
name|ImportEOModelAction
operator|.
name|getActionName
argument_list|()
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|static
specifier|final
name|Collection
name|DATA_MAP_ACTIONS
init|=
operator|new
name|HashSet
argument_list|(
name|DOMAIN_ACTIONS
argument_list|)
decl_stmt|;
static|static
block|{
name|DATA_MAP_ACTIONS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|GenerateCodeAction
operator|.
name|getActionName
argument_list|()
block|,
name|CreateObjEntityAction
operator|.
name|getActionName
argument_list|()
block|,
name|CreateDbEntityAction
operator|.
name|getActionName
argument_list|()
block|,
name|CreateDerivedDbEntityAction
operator|.
name|getActionName
argument_list|()
block|,
name|CreateQueryAction
operator|.
name|getActionName
argument_list|()
block|,
name|CreateProcedureAction
operator|.
name|getActionName
argument_list|()
block|,
name|GenerateDBAction
operator|.
name|getActionName
argument_list|()
block|,
name|MigrateAction
operator|.
name|getActionName
argument_list|()
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|static
specifier|final
name|Collection
name|OBJ_ENTITY_ACTIONS
init|=
operator|new
name|HashSet
argument_list|(
name|DATA_MAP_ACTIONS
argument_list|)
decl_stmt|;
static|static
block|{
name|OBJ_ENTITY_ACTIONS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|ObjEntitySyncAction
operator|.
name|getActionName
argument_list|()
block|,
name|CreateAttributeAction
operator|.
name|getActionName
argument_list|()
block|,
name|CreateRelationshipAction
operator|.
name|getActionName
argument_list|()
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|static
specifier|final
name|Collection
name|DB_ENTITY_ACTIONS
init|=
operator|new
name|HashSet
argument_list|(
name|DATA_MAP_ACTIONS
argument_list|)
decl_stmt|;
static|static
block|{
name|DB_ENTITY_ACTIONS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|CreateAttributeAction
operator|.
name|getActionName
argument_list|()
block|,
name|CreateRelationshipAction
operator|.
name|getActionName
argument_list|()
block|,
name|DbEntitySyncAction
operator|.
name|getActionName
argument_list|()
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|static
specifier|final
name|Collection
name|PROCEDURE_ACTIONS
init|=
operator|new
name|HashSet
argument_list|(
name|DATA_MAP_ACTIONS
argument_list|)
decl_stmt|;
static|static
block|{
name|PROCEDURE_ACTIONS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|CreateProcedureParameterAction
operator|.
name|getActionName
argument_list|()
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Map
name|actionMap
decl_stmt|;
specifier|public
name|ActionManager
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|this
operator|.
name|actionMap
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|ProjectAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|NewProjectAction
argument_list|(
name|application
argument_list|)
argument_list|)
operator|.
name|setAlwaysOn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|OpenProjectAction
argument_list|(
name|application
argument_list|)
argument_list|)
operator|.
name|setAlwaysOn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|ImportDataMapAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|SaveAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|SaveAsAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|RevertAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|ValidateAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|RemoveAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CreateDomainAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CreateNodeAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CreateDataMapAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|GenerateCodeAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CreateObjEntityAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CreateDbEntityAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CreateDerivedDbEntityAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CreateProcedureAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CreateProcedureParameterAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|RemoveProcedureParameterAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CreateQueryAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CreateAttributeAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|RemoveAttributeAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CreateRelationshipAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|RemoveRelationshipAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|DbEntitySyncAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|ObjEntitySyncAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|DerivedEntitySyncAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|ImportDBAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|ImportEOModelAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|GenerateDBAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|MigrateAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|AboutAction
argument_list|(
name|application
argument_list|)
argument_list|)
operator|.
name|setAlwaysOn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|ConfigurePreferencesAction
argument_list|(
name|application
argument_list|)
argument_list|)
operator|.
name|setAlwaysOn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|ExitAction
argument_list|(
name|application
argument_list|)
argument_list|)
operator|.
name|setAlwaysOn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|NavigateBackwardAction
argument_list|(
name|application
argument_list|)
argument_list|)
operator|.
name|setAlwaysOn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|NavigateForwardAction
argument_list|(
name|application
argument_list|)
argument_list|)
operator|.
name|setAlwaysOn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// search action registered
name|registerAction
argument_list|(
operator|new
name|FindAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|CayenneAction
name|registerAction
parameter_list|(
name|CayenneAction
name|action
parameter_list|)
block|{
name|actionMap
operator|.
name|put
argument_list|(
name|action
operator|.
name|getKey
argument_list|()
argument_list|,
name|action
argument_list|)
expr_stmt|;
return|return
name|action
return|;
block|}
comment|/**      * Returns an action for key.      */
specifier|public
name|CayenneAction
name|getAction
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
operator|(
name|CayenneAction
operator|)
name|actionMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * Updates actions state to reflect an open project.      */
specifier|public
name|void
name|projectOpened
parameter_list|()
block|{
name|processActionsState
argument_list|(
name|PROJECT_ACTIONS
argument_list|)
expr_stmt|;
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setName
argument_list|(
literal|"Remove"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|projectClosed
parameter_list|()
block|{
name|processActionsState
argument_list|(
name|Collections
operator|.
name|EMPTY_SET
argument_list|)
expr_stmt|;
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setName
argument_list|(
literal|"Remove"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Updates actions state to reflect DataDomain selecttion.      */
specifier|public
name|void
name|domainSelected
parameter_list|()
block|{
name|processActionsState
argument_list|(
name|DOMAIN_ACTIONS
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dataNodeSelected
parameter_list|()
block|{
name|processActionsState
argument_list|(
name|DOMAIN_ACTIONS
argument_list|)
expr_stmt|;
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setName
argument_list|(
literal|"Remove DataNode"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dataMapSelected
parameter_list|()
block|{
name|processActionsState
argument_list|(
name|DATA_MAP_ACTIONS
argument_list|)
expr_stmt|;
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setName
argument_list|(
literal|"Remove DataMap"
argument_list|)
expr_stmt|;
comment|// reset
comment|// getAction(CreateAttributeAction.getActionName()).setName("Create Attribute");
block|}
specifier|public
name|void
name|objEntitySelected
parameter_list|()
block|{
name|processActionsState
argument_list|(
name|OBJ_ENTITY_ACTIONS
argument_list|)
expr_stmt|;
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setName
argument_list|(
literal|"Remove ObjEntity"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dbEntitySelected
parameter_list|()
block|{
name|processActionsState
argument_list|(
name|DB_ENTITY_ACTIONS
argument_list|)
expr_stmt|;
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setName
argument_list|(
literal|"Remove DbEntity"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|derivedDbEntitySelected
parameter_list|()
block|{
name|processActionsState
argument_list|(
name|DB_ENTITY_ACTIONS
argument_list|)
expr_stmt|;
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setName
argument_list|(
literal|"Remove Derived DbEntity"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|procedureSelected
parameter_list|()
block|{
name|processActionsState
argument_list|(
name|PROCEDURE_ACTIONS
argument_list|)
expr_stmt|;
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setName
argument_list|(
literal|"Remove Procedure"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|querySelected
parameter_list|()
block|{
name|processActionsState
argument_list|(
name|DATA_MAP_ACTIONS
argument_list|)
expr_stmt|;
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|setName
argument_list|(
literal|"Remove Query"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the state of all controlled actions, flipping it to "enabled" for all actions      * in provided collection and to "disabled" for the rest.      */
specifier|protected
name|void
name|processActionsState
parameter_list|(
name|Collection
name|namesOfEnabled
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|actionMap
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|SPECIAL_ACTIONS
operator|.
name|contains
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
operator|(
operator|(
name|Action
operator|)
name|entry
operator|.
name|getValue
argument_list|()
operator|)
operator|.
name|setEnabled
argument_list|(
name|namesOfEnabled
operator|.
name|contains
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

