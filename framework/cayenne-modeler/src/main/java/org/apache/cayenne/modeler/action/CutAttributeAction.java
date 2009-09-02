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
name|Attribute
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
name|EmbeddableAttribute
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
name|project
operator|.
name|ProjectPath
import|;
end_import

begin_comment
comment|/**  * Action for cutting attribute(s)  */
end_comment

begin_class
specifier|public
class|class
name|CutAttributeAction
extends|extends
name|CutAction
implements|implements
name|MultipleObjectsAction
block|{
specifier|private
specifier|final
specifier|static
name|String
name|ACTION_NAME
init|=
literal|"Cut Attribute"
decl_stmt|;
comment|/**      * Name of action if multiple attrs are selected      */
specifier|private
specifier|final
specifier|static
name|String
name|ACTION_NAME_MULTIPLE
init|=
literal|"Cut Attributes"
decl_stmt|;
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
name|ACTION_NAME
return|;
block|}
specifier|public
name|String
name|getActionName
parameter_list|(
name|boolean
name|multiple
parameter_list|)
block|{
return|return
name|multiple
condition|?
name|ACTION_NAME_MULTIPLE
else|:
name|ACTION_NAME
return|;
block|}
specifier|public
name|CutAttributeAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|ACTION_NAME
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if last object in the path contains a removable      * attribute.      */
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
name|boolean
name|isEnable
init|=
name|path
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Attribute
decl_stmt|;
if|if
condition|(
operator|!
name|isEnable
condition|)
block|{
name|isEnable
operator|=
name|path
operator|.
name|getObject
argument_list|()
operator|instanceof
name|EmbeddableAttribute
expr_stmt|;
block|}
return|return
name|isEnable
return|;
block|}
comment|/**      * Performs cutting of items      */
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
name|application
operator|.
name|getAction
argument_list|(
name|CopyAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|.
name|performAction
argument_list|(
name|e
argument_list|)
expr_stmt|;
operator|(
operator|(
name|RemoveAction
operator|)
name|application
operator|.
name|getAction
argument_list|(
name|RemoveAttributeAction
operator|.
name|getActionName
argument_list|()
argument_list|)
operator|)
operator|.
name|performAction
argument_list|(
name|e
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

