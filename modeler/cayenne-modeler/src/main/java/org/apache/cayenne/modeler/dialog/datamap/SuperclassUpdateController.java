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
name|dialog
operator|.
name|datamap
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
name|util
operator|.
name|Comparators
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
name|Util
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|SuperclassUpdateController
extends|extends
name|DefaultsPreferencesController
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ALL_CONTROL
init|=
literal|"Set/update superclass for all ObjEntities"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UNINIT_CONTROL
init|=
literal|"Do not override existing non-empty superclasses"
decl_stmt|;
specifier|protected
name|DefaultsPreferencesView
name|view
decl_stmt|;
specifier|public
name|SuperclassUpdateController
parameter_list|(
name|ProjectController
name|mediator
parameter_list|,
name|DataMap
name|dataMap
parameter_list|)
block|{
name|super
argument_list|(
name|mediator
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates and runs superclass update dialog.      */
specifier|public
name|void
name|startupAction
parameter_list|()
block|{
name|view
operator|=
operator|new
name|DefaultsPreferencesView
argument_list|(
name|ALL_CONTROL
argument_list|,
name|UNINIT_CONTROL
argument_list|)
expr_stmt|;
name|view
operator|.
name|setTitle
argument_list|(
literal|"Update DataObjects Superclass"
argument_list|)
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
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
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|this
operator|.
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
name|getUpdateButton
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
name|updateSuperclass
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|getCancelButton
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
name|view
operator|.
name|dispose
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|updateSuperclass
parameter_list|()
block|{
name|boolean
name|doAll
init|=
name|isAllEntities
argument_list|()
decl_stmt|;
name|String
name|defaultSuperclass
init|=
name|getSuperclass
argument_list|()
decl_stmt|;
name|dataMap
operator|.
name|getObjEntities
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|sorted
argument_list|(
name|Comparators
operator|.
name|getDataMapChildrenComparator
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|entity
lambda|->
block|{
if|if
condition|(
name|doAll
operator|||
name|Util
operator|.
name|isEmptyString
argument_list|(
name|getSuperClassName
argument_list|(
name|entity
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|defaultSuperclass
argument_list|,
name|getSuperClassName
argument_list|(
name|entity
argument_list|)
argument_list|)
condition|)
block|{
name|setSuperClassName
argument_list|(
name|entity
argument_list|,
name|defaultSuperclass
argument_list|)
expr_stmt|;
comment|// any way to batch events, a big change will flood the app with
comment|// entity events..?
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|String
name|getSuperclass
parameter_list|()
block|{
return|return
name|dataMap
operator|.
name|getDefaultSuperclass
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getSuperClassName
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
return|return
name|entity
operator|.
name|getSuperClassName
argument_list|()
return|;
block|}
specifier|protected
name|void
name|setSuperClassName
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|String
name|superClassName
parameter_list|)
block|{
name|entity
operator|.
name|setSuperClassName
argument_list|(
name|superClassName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

