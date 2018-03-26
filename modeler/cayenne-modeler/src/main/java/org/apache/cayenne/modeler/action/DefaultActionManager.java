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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|ConfigurationNameMapper
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
name|di
operator|.
name|Inject
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
name|graph
operator|.
name|action
operator|.
name|ShowGraphEntityAction
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
name|ConfigurationNodeParentGetter
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
name|javax
operator|.
name|swing
operator|.
name|ActionMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JComponent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|TransferHandler
import|;
end_import

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
name|Map
import|;
end_import

begin_comment
comment|/**  * Stores a map of modeler actions, and deals with activating/deactivating those actions  * on state changes.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultActionManager
implements|implements
name|ActionManager
block|{
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|SPECIAL_ACTIONS
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|PROJECT_ACTIONS
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|DOMAIN_ACTIONS
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|DATA_NODE_ACTIONS
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|DATA_MAP_ACTIONS
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|OBJ_ENTITY_ACTIONS
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|DB_ENTITY_ACTIONS
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|EMBEDDABLE_ACTIONS
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|PROCEDURE_ACTIONS
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|MULTIPLE_OBJECTS_ACTIONS
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Action
argument_list|>
name|actionMap
decl_stmt|;
specifier|public
name|DefaultActionManager
parameter_list|(
annotation|@
name|Inject
name|Application
name|application
parameter_list|,
annotation|@
name|Inject
name|ConfigurationNameMapper
name|nameMapper
parameter_list|)
block|{
name|initActions
argument_list|()
expr_stmt|;
name|this
operator|.
name|actionMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|40
argument_list|)
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
argument_list|,
name|nameMapper
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
name|CreateObjEntityFromDbAction
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
name|RemoveAttributeRelationshipAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
comment|// start callback-related actions
name|registerAction
argument_list|(
operator|new
name|CreateCallbackMethodAction
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
name|RemoveCallbackMethodAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
comment|// end callback-related actions
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
name|DbEntityCounterpartAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|ObjEntityCounterpartAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|ObjEntityToSuperEntityAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|ReverseEngineeringAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|InferRelationshipsAction
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
name|AddSchemaAction
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
name|AddCatalogAction
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
name|AddIncludeTableAction
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
name|AddExcludeTableAction
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
name|AddIncludeColumnAction
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
name|AddExcludeColumnAction
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
name|AddIncludeProcedureAction
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
name|AddExcludeProcedureAction
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
name|GetDbConnectionAction
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
name|EditNodeAction
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
name|DeleteNodeAction
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
name|MoveImportNodeAction
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
name|LoadDbSchemaAction
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
name|MoveInvertNodeAction
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
name|DocumentationAction
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
name|registerAction
argument_list|(
operator|new
name|ShowLogConsoleAction
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
name|CutAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CutAttributeAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CutRelationshipAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CutAttributeRelationshipAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CutProcedureParameterAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CutCallbackMethodAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CopyAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CopyAttributeAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CopyRelationshipAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CopyAttributeRelationshipAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CopyCallbackMethodAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CopyProcedureParameterAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|PasteAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|UndoAction
name|undoAction
init|=
operator|new
name|UndoAction
argument_list|(
name|application
argument_list|)
decl_stmt|;
name|undoAction
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
name|undoAction
argument_list|)
expr_stmt|;
name|RedoAction
name|redoAction
init|=
operator|new
name|RedoAction
argument_list|(
name|application
argument_list|)
decl_stmt|;
name|redoAction
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
name|redoAction
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CreateEmbeddableAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|ShowGraphEntityAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|CollapseTreeAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|FilterAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|LinkDataMapAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|registerAction
argument_list|(
operator|new
name|LinkDataMapsAction
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initActions
parameter_list|()
block|{
name|SPECIAL_ACTIONS
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|SPECIAL_ACTIONS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|SaveAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|UndoAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|RedoAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|PROJECT_ACTIONS
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|PROJECT_ACTIONS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|RevertAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ProjectAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ValidateAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|SaveAsAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|FindAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|DOMAIN_ACTIONS
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|PROJECT_ACTIONS
argument_list|)
expr_stmt|;
name|DOMAIN_ACTIONS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ImportDataMapAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CreateDataMapAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CreateNodeAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ReverseEngineeringAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ImportEOModelAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|GenerateCodeAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|GenerateDBAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|PasteAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|DATA_NODE_ACTIONS
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|DOMAIN_ACTIONS
argument_list|)
expr_stmt|;
name|DATA_NODE_ACTIONS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|LinkDataMapsAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|RemoveAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|DATA_MAP_ACTIONS
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|DOMAIN_ACTIONS
argument_list|)
expr_stmt|;
name|DATA_MAP_ACTIONS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|CreateEmbeddableAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CreateObjEntityAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CreateDbEntityAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CreateQueryAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CreateProcedureAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|MigrateAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|RemoveAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|InferRelationshipsAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CutAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CopyAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|OBJ_ENTITY_ACTIONS
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|DATA_MAP_ACTIONS
argument_list|)
expr_stmt|;
name|OBJ_ENTITY_ACTIONS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ObjEntitySyncAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CreateAttributeAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CreateRelationshipAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ObjEntityCounterpartAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ObjEntityToSuperEntityAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ShowGraphEntityAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|DB_ENTITY_ACTIONS
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|DATA_MAP_ACTIONS
argument_list|)
expr_stmt|;
name|DB_ENTITY_ACTIONS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|CreateAttributeAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CreateRelationshipAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|DbEntitySyncAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|DbEntityCounterpartAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ShowGraphEntityAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CreateObjEntityFromDbAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|EMBEDDABLE_ACTIONS
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|DATA_MAP_ACTIONS
argument_list|)
expr_stmt|;
name|EMBEDDABLE_ACTIONS
operator|.
name|addAll
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|CreateAttributeAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|PROCEDURE_ACTIONS
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|DATA_MAP_ACTIONS
argument_list|)
expr_stmt|;
name|PROCEDURE_ACTIONS
operator|.
name|addAll
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|CreateProcedureParameterAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|MULTIPLE_OBJECTS_ACTIONS
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|PROJECT_ACTIONS
argument_list|)
expr_stmt|;
name|MULTIPLE_OBJECTS_ACTIONS
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|RemoveAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CutAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|CopyAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|PasteAction
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|CayenneAction
name|registerAction
parameter_list|(
name|CayenneAction
name|action
parameter_list|)
block|{
name|Action
name|oldAction
init|=
name|actionMap
operator|.
name|put
argument_list|(
name|action
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|action
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldAction
operator|!=
literal|null
operator|&&
name|oldAction
operator|!=
name|action
condition|)
block|{
name|actionMap
operator|.
name|put
argument_list|(
name|action
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|oldAction
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"There is already an action of type "
operator|+
name|action
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|", attempt to register a second instance."
argument_list|)
throw|;
block|}
return|return
name|action
return|;
block|}
specifier|public
name|void
name|addProjectAction
parameter_list|(
name|String
name|actionName
parameter_list|)
block|{
if|if
condition|(
operator|!
name|PROJECT_ACTIONS
operator|.
name|contains
argument_list|(
name|actionName
argument_list|)
condition|)
block|{
name|PROJECT_ACTIONS
operator|.
name|add
argument_list|(
name|actionName
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|removeProjectaction
parameter_list|(
name|String
name|actionName
parameter_list|)
block|{
if|if
condition|(
name|PROJECT_ACTIONS
operator|.
name|contains
argument_list|(
name|actionName
argument_list|)
condition|)
block|{
name|PROJECT_ACTIONS
operator|.
name|remove
argument_list|(
name|actionName
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
parameter_list|<
name|T
extends|extends
name|Action
parameter_list|>
name|T
name|getAction
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|actionClass
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
name|actionMap
operator|.
name|get
argument_list|(
name|actionClass
operator|.
name|getName
argument_list|()
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
name|updateActions
argument_list|(
literal|""
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
name|emptySet
argument_list|()
argument_list|)
expr_stmt|;
name|updateActions
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
comment|/**      * Updates actions state to reflect DataDomain selection.      */
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
name|updateActions
argument_list|(
literal|"DataDomain"
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
name|DATA_NODE_ACTIONS
argument_list|)
expr_stmt|;
name|updateActions
argument_list|(
literal|"DataNode"
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
name|updateActions
argument_list|(
literal|"DataMap"
argument_list|)
expr_stmt|;
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
name|updateActions
argument_list|(
literal|"ObjEntity"
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
name|updateActions
argument_list|(
literal|"DbEntity"
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
name|updateActions
argument_list|(
literal|"Procedure"
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
name|updateActions
argument_list|(
literal|"Query"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|embeddableSelected
parameter_list|()
block|{
name|processActionsState
argument_list|(
name|EMBEDDABLE_ACTIONS
argument_list|)
expr_stmt|;
name|updateActions
argument_list|(
literal|"Embeddable"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Invoked when several objects were selected in ProjectTree at time      */
specifier|public
name|void
name|multipleObjectsSelected
parameter_list|(
name|ConfigurationNode
index|[]
name|objects
parameter_list|,
name|Application
name|application
parameter_list|)
block|{
name|processActionsState
argument_list|(
name|MULTIPLE_OBJECTS_ACTIONS
argument_list|)
expr_stmt|;
name|updateActions
argument_list|(
literal|"Selected Objects"
argument_list|)
expr_stmt|;
name|CayenneAction
name|cutAction
init|=
name|getAction
argument_list|(
name|CutAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|canCopy
init|=
literal|true
decl_stmt|;
comment|// cut/copy can be performed if selected objects are on
comment|// the same level
if|if
condition|(
operator|!
name|cutAction
operator|.
name|enableForPath
argument_list|(
name|objects
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|canCopy
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|ConfigurationNodeParentGetter
name|parentGetter
init|=
name|application
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|ConfigurationNodeParentGetter
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|parent
init|=
name|parentGetter
operator|.
name|getParent
argument_list|(
name|objects
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|objects
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|parentGetter
operator|.
name|getParent
argument_list|(
name|objects
index|[
name|i
index|]
argument_list|)
operator|!=
name|parent
operator|||
operator|!
name|cutAction
operator|.
name|enableForPath
argument_list|(
name|objects
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|canCopy
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
block|}
name|cutAction
operator|.
name|setEnabled
argument_list|(
name|canCopy
argument_list|)
expr_stmt|;
name|getAction
argument_list|(
name|CopyAction
operator|.
name|class
argument_list|)
operator|.
name|setEnabled
argument_list|(
name|canCopy
argument_list|)
expr_stmt|;
block|}
comment|/**      * Updates Remove, Cut and Copy actions' names      */
specifier|private
name|void
name|updateActions
parameter_list|(
name|String
name|postfix
parameter_list|)
block|{
if|if
condition|(
name|postfix
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|postfix
operator|=
literal|" "
operator|+
name|postfix
expr_stmt|;
block|}
name|getAction
argument_list|(
name|RemoveAction
operator|.
name|class
argument_list|)
operator|.
name|setName
argument_list|(
literal|"Remove"
operator|+
name|postfix
argument_list|)
expr_stmt|;
name|getAction
argument_list|(
name|CutAction
operator|.
name|class
argument_list|)
operator|.
name|setName
argument_list|(
literal|"Cut"
operator|+
name|postfix
argument_list|)
expr_stmt|;
name|getAction
argument_list|(
name|CopyAction
operator|.
name|class
argument_list|)
operator|.
name|setName
argument_list|(
literal|"Copy"
operator|+
name|postfix
argument_list|)
expr_stmt|;
name|getAction
argument_list|(
name|PasteAction
operator|.
name|class
argument_list|)
operator|.
name|updateState
argument_list|()
expr_stmt|;
block|}
comment|/**      * Sets the state of all controlled actions, flipping it to "enabled" for all actions      * in provided collection and to "disabled" for the rest.      */
specifier|protected
name|void
name|processActionsState
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|namesOfEnabled
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Action
argument_list|>
name|entry
range|:
name|actionMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
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
name|entry
operator|.
name|getValue
argument_list|()
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
specifier|public
name|void
name|setupCutCopyPaste
parameter_list|(
name|JComponent
name|comp
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|cutActionType
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Action
argument_list|>
name|copyActionType
parameter_list|)
block|{
name|ActionMap
name|map
init|=
name|comp
operator|.
name|getActionMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|TransferHandler
operator|.
name|getCutAction
argument_list|()
operator|.
name|getValue
argument_list|(
name|Action
operator|.
name|NAME
argument_list|)
argument_list|,
name|getAction
argument_list|(
name|cutActionType
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|TransferHandler
operator|.
name|getCopyAction
argument_list|()
operator|.
name|getValue
argument_list|(
name|Action
operator|.
name|NAME
argument_list|)
argument_list|,
name|getAction
argument_list|(
name|copyActionType
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|TransferHandler
operator|.
name|getPasteAction
argument_list|()
operator|.
name|getValue
argument_list|(
name|Action
operator|.
name|NAME
argument_list|)
argument_list|,
name|getAction
argument_list|(
name|PasteAction
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

