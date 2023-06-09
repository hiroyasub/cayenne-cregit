begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *    Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
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
name|dbsync
operator|.
name|filter
operator|.
name|NamePatternMatcher
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
name|dbsync
operator|.
name|merge
operator|.
name|context
operator|.
name|EntityMergeSupport
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
name|dbsync
operator|.
name|naming
operator|.
name|DefaultObjectNameGenerator
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
name|dbsync
operator|.
name|naming
operator|.
name|NameBuilder
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
name|dbsync
operator|.
name|naming
operator|.
name|NoStemStemmer
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
name|undo
operator|.
name|CreateObjEntityUndoableEdit
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

begin_class
specifier|public
class|class
name|CreateObjEntityAction
extends|extends
name|CayenneAction
block|{
comment|/**      * Constructor for CreateObjEntityAction.      */
specifier|public
name|CreateObjEntityAction
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
literal|"Create ObjEntity"
return|;
block|}
specifier|static
name|void
name|fireObjEntityEvent
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
name|ObjEntity
name|entity
parameter_list|)
block|{
name|mediator
operator|.
name|fireObjEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|src
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
name|src
argument_list|,
name|entity
argument_list|,
name|dataMap
argument_list|,
name|mediator
operator|.
name|getCurrentDataNode
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
name|displayEvent
operator|.
name|setMainTabFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|mediator
operator|.
name|fireObjEntityDisplayEvent
argument_list|(
name|displayEvent
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
literal|"icon-objentity.png"
return|;
block|}
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
name|createObjEntity
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|createObjEntity
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|DataMap
name|dataMap
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|()
decl_stmt|;
name|entity
operator|.
name|setName
argument_list|(
name|NameBuilder
operator|.
name|builder
argument_list|(
name|entity
argument_list|,
name|dataMap
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
comment|// init defaults
name|entity
operator|.
name|setSuperClassName
argument_list|(
name|dataMap
operator|.
name|getDefaultSuperclass
argument_list|()
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setDeclaredLockType
argument_list|(
name|dataMap
operator|.
name|getDefaultLockType
argument_list|()
argument_list|)
expr_stmt|;
name|DbEntity
name|dbEntity
init|=
name|mediator
operator|.
name|getCurrentDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbEntity
operator|!=
literal|null
condition|)
block|{
name|entity
operator|.
name|setDbEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
comment|// TODO: use injectable name generator
name|String
name|baseName
init|=
operator|new
name|DefaultObjectNameGenerator
argument_list|(
name|NoStemStemmer
operator|.
name|getInstance
argument_list|()
argument_list|)
operator|.
name|objEntityName
argument_list|(
name|dbEntity
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setName
argument_list|(
name|NameBuilder
operator|.
name|builder
argument_list|(
name|entity
argument_list|,
name|dbEntity
operator|.
name|getDataMap
argument_list|()
argument_list|)
operator|.
name|baseName
argument_list|(
name|baseName
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|entity
operator|.
name|setClassName
argument_list|(
name|dataMap
operator|.
name|getNameWithDefaultPackage
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
comment|// TODO: Modeler-controlled defaults for all the hardcoded boolean flags here.
name|EntityMergeSupport
name|merger
init|=
operator|new
name|EntityMergeSupport
argument_list|(
operator|new
name|DefaultObjectNameGenerator
argument_list|(
name|NoStemStemmer
operator|.
name|getInstance
argument_list|()
argument_list|)
argument_list|,
name|NamePatternMatcher
operator|.
name|EXCLUDE_ALL
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|merger
operator|.
name|setNameGenerator
argument_list|(
operator|new
name|DbEntitySyncAction
operator|.
name|PreserveRelationshipNameGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|merger
operator|.
name|addEntityMergeListener
argument_list|(
name|DeleteRuleUpdater
operator|.
name|getEntityMergeListener
argument_list|()
argument_list|)
expr_stmt|;
name|merger
operator|.
name|synchronizeWithDbEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|fireObjEntityEvent
argument_list|(
name|this
argument_list|,
name|mediator
argument_list|,
name|dataMap
argument_list|,
name|entity
argument_list|)
expr_stmt|;
name|application
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|CreateObjEntityUndoableEdit
argument_list|(
name|dataMap
argument_list|,
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|createObjEntity
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|ObjEntity
name|entity
parameter_list|)
block|{
name|ProjectController
name|mediator
init|=
name|getProjectController
argument_list|()
decl_stmt|;
name|dataMap
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|fireObjEntityEvent
argument_list|(
name|this
argument_list|,
name|mediator
argument_list|,
name|dataMap
argument_list|,
name|entity
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if path contains a DataMap object.      */
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
name|ObjEntity
condition|)
block|{
return|return
operator|(
operator|(
name|ObjEntity
operator|)
name|object
operator|)
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
operator|(
name|ObjEntity
operator|)
name|object
operator|)
operator|.
name|getParent
argument_list|()
operator|instanceof
name|DataMap
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

