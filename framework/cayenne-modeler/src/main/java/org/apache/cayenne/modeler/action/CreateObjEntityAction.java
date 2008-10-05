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
name|DeleteRuleUpdater
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
name|util
operator|.
name|NameConverter
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

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|CreateObjEntityAction
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
literal|"Create ObjEntity"
return|;
block|}
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
annotation|@
name|Override
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-new_objentity.gif"
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
operator|(
name|ObjEntity
operator|)
name|NamedObjectFactory
operator|.
name|createObject
argument_list|(
name|ObjEntity
operator|.
name|class
argument_list|,
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
decl_stmt|;
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
name|String
name|baseName
init|=
name|NameConverter
operator|.
name|underscoredToJava
argument_list|(
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|String
name|entityName
init|=
name|NamedObjectFactory
operator|.
name|createName
argument_list|(
name|ObjEntity
operator|.
name|class
argument_list|,
name|dbEntity
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|baseName
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setName
argument_list|(
name|entityName
argument_list|)
expr_stmt|;
block|}
name|String
name|pkg
init|=
name|dataMap
operator|.
name|getDefaultPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|pkg
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|pkg
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|pkg
operator|=
name|pkg
operator|+
literal|"."
expr_stmt|;
block|}
name|entity
operator|.
name|setClassName
argument_list|(
name|pkg
operator|+
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dataMap
operator|.
name|isClientSupported
argument_list|()
condition|)
block|{
name|String
name|clientPkg
init|=
name|dataMap
operator|.
name|getDefaultClientPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|clientPkg
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|clientPkg
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|clientPkg
operator|=
name|clientPkg
operator|+
literal|"."
expr_stmt|;
block|}
name|entity
operator|.
name|setClientClassName
argument_list|(
name|clientPkg
operator|+
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|entity
operator|.
name|setClientSuperClassName
argument_list|(
name|dataMap
operator|.
name|getDefaultClientSuperclass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|dataMap
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
comment|// perform the merge
name|EntityMergeSupport
name|merger
init|=
operator|new
name|EntityMergeSupport
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
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
block|}
comment|/**      * Fires events when a obj entity was added      */
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
name|mediator
operator|.
name|getCurrentDataDomain
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
comment|/**      * Returns<code>true</code> if path contains a DataMap object.      */
annotation|@
name|Override
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

