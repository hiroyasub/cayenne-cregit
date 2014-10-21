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
name|editor
operator|.
name|ObjEntityAttributePanel
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
name|editor
operator|.
name|dbentity
operator|.
name|DbEntityAttributePanel
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
name|CutAttributeRelationshipAction
extends|extends
name|CutAction
implements|implements
name|MultipleObjectsAction
block|{
specifier|private
name|CutAttributeAction
name|cutAttributeAction
decl_stmt|;
specifier|private
name|CutRelationshipAction
name|cutRelationshipAction
decl_stmt|;
specifier|private
name|JComponent
name|currentSelectedPanel
decl_stmt|;
specifier|public
name|CutAttributeRelationshipAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|application
argument_list|)
expr_stmt|;
name|cutAttributeAction
operator|=
operator|new
name|CutAttributeAction
argument_list|(
name|application
argument_list|)
expr_stmt|;
name|cutRelationshipAction
operator|=
operator|new
name|CutRelationshipAction
argument_list|(
name|application
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JComponent
name|getCurrentSelectedPanel
parameter_list|()
block|{
return|return
name|currentSelectedPanel
return|;
block|}
specifier|public
name|void
name|setCurrentSelectedPanel
parameter_list|(
name|JComponent
name|currentSelectedPanel
parameter_list|)
block|{
name|this
operator|.
name|currentSelectedPanel
operator|=
name|currentSelectedPanel
expr_stmt|;
block|}
specifier|public
name|String
name|getActionName
parameter_list|(
name|boolean
name|multiple
parameter_list|)
block|{
if|if
condition|(
name|currentSelectedPanel
operator|instanceof
name|ObjEntityAttributePanel
operator|||
name|currentSelectedPanel
operator|instanceof
name|DbEntityAttributePanel
condition|)
block|{
return|return
name|cutAttributeAction
operator|.
name|getActionName
argument_list|(
name|multiple
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|cutRelationshipAction
operator|.
name|getActionName
argument_list|(
name|multiple
argument_list|)
return|;
block|}
block|}
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
name|currentSelectedPanel
operator|instanceof
name|ObjEntityAttributePanel
operator|||
name|currentSelectedPanel
operator|instanceof
name|DbEntityAttributePanel
condition|)
block|{
return|return
name|cutAttributeAction
operator|.
name|enableForPath
argument_list|(
name|object
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|cutRelationshipAction
operator|.
name|enableForPath
argument_list|(
name|object
argument_list|)
return|;
block|}
block|}
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|currentSelectedPanel
operator|instanceof
name|ObjEntityAttributePanel
operator|||
name|currentSelectedPanel
operator|instanceof
name|DbEntityAttributePanel
condition|)
block|{
name|cutAttributeAction
operator|.
name|performAction
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cutRelationshipAction
operator|.
name|performAction
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

