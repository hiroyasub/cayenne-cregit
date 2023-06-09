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
name|undo
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|AbstractUndoableEdit
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
name|action
operator|.
name|ActionManager
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|CayenneUndoableEdit
extends|extends
name|AbstractUndoableEdit
block|{
specifier|protected
name|ActionManager
name|actionManager
decl_stmt|;
specifier|protected
name|ProjectController
name|controller
decl_stmt|;
specifier|public
name|CayenneUndoableEdit
parameter_list|()
block|{
name|this
operator|.
name|actionManager
operator|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getActionManager
argument_list|()
expr_stmt|;
name|this
operator|.
name|controller
operator|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|canRedo
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|canUndo
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

