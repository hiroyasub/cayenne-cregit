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
name|dialog
operator|.
name|objentity
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
name|modeler
operator|.
name|util
operator|.
name|CayenneController
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ClassNameUpdater
extends|extends
name|CayenneController
block|{
specifier|protected
name|ClassNameUpdaterView
name|view
decl_stmt|;
specifier|protected
name|ObjEntity
name|entity
decl_stmt|;
specifier|protected
name|boolean
name|updatePerformed
decl_stmt|;
specifier|public
name|ClassNameUpdater
parameter_list|(
name|CayenneController
name|parent
parameter_list|,
name|ObjEntity
name|entity
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
comment|// don't init view here... we may simply skip update if there is nothing to do
block|}
comment|/**      * Executes entity class and client class name update. Returns true if entity was      * changed, false otherwise.      */
specifier|public
name|boolean
name|doNameUpdate
parameter_list|()
block|{
name|this
operator|.
name|view
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|updatePerformed
operator|=
literal|false
expr_stmt|;
name|boolean
name|askForServerUpdate
init|=
literal|true
decl_stmt|;
name|boolean
name|askForClientUpdate
init|=
literal|true
decl_stmt|;
name|String
name|oldServerName
init|=
name|entity
operator|.
name|getClassName
argument_list|()
decl_stmt|;
name|String
name|suggestedServerName
init|=
name|suggestedServerClassName
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldServerName
operator|==
literal|null
operator|||
name|oldServerName
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// generic entity...
name|askForServerUpdate
operator|=
literal|false
expr_stmt|;
block|}
if|else if
condition|(
name|suggestedServerName
operator|==
literal|null
operator|||
name|suggestedServerName
operator|.
name|equals
argument_list|(
name|oldServerName
argument_list|)
condition|)
block|{
name|askForServerUpdate
operator|=
literal|false
expr_stmt|;
block|}
if|else if
condition|(
name|oldServerName
operator|.
name|contains
argument_list|(
literal|"UntitledObjEntity"
argument_list|)
condition|)
block|{
comment|// update without user interaction
name|entity
operator|.
name|setClassName
argument_list|(
name|suggestedServerName
argument_list|)
expr_stmt|;
name|updatePerformed
operator|=
literal|true
expr_stmt|;
name|askForServerUpdate
operator|=
literal|false
expr_stmt|;
block|}
name|String
name|suggestedClientName
init|=
name|suggestedClientClassName
argument_list|()
decl_stmt|;
name|String
name|oldClientName
init|=
name|entity
operator|.
name|getClientClassName
argument_list|()
decl_stmt|;
if|if
condition|(
name|suggestedClientName
operator|==
literal|null
operator|||
name|suggestedClientName
operator|.
name|equals
argument_list|(
name|oldClientName
argument_list|)
condition|)
block|{
name|askForClientUpdate
operator|=
literal|false
expr_stmt|;
block|}
if|else if
condition|(
name|oldClientName
operator|==
literal|null
operator|||
name|oldClientName
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|||
name|oldClientName
operator|.
name|contains
argument_list|(
literal|"UntitledObjEntity"
argument_list|)
condition|)
block|{
comment|// update without user interaction
name|entity
operator|.
name|setClientClassName
argument_list|(
name|suggestedClientName
argument_list|)
expr_stmt|;
name|updatePerformed
operator|=
literal|true
expr_stmt|;
name|askForClientUpdate
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
name|askForClientUpdate
operator|||
name|askForServerUpdate
condition|)
block|{
comment|// start dialog
name|view
operator|=
operator|new
name|ClassNameUpdaterView
argument_list|()
expr_stmt|;
if|if
condition|(
name|askForServerUpdate
condition|)
block|{
name|view
operator|.
name|getServerClass
argument_list|()
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|view
operator|.
name|getServerClass
argument_list|()
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|view
operator|.
name|getServerClass
argument_list|()
operator|.
name|setText
argument_list|(
literal|"Change Class Name to '"
operator|+
name|suggestedServerName
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|askForClientUpdate
condition|)
block|{
name|view
operator|.
name|getClientClass
argument_list|()
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|view
operator|.
name|getClientClass
argument_list|()
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|view
operator|.
name|getClientClass
argument_list|()
operator|.
name|setText
argument_list|(
literal|"Change Client Class Name to '"
operator|+
name|suggestedClientName
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
name|initBindings
argument_list|(
name|suggestedServerName
argument_list|,
name|suggestedClientName
argument_list|)
expr_stmt|;
name|view
operator|.
name|pack
argument_list|()
expr_stmt|;
name|view
operator|.
name|setModal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|centerView
argument_list|()
expr_stmt|;
name|makeCloseableOnEscape
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
return|return
name|this
operator|.
name|updatePerformed
return|;
block|}
specifier|private
name|String
name|suggestedServerClassName
parameter_list|()
block|{
name|String
name|pkg
init|=
name|entity
operator|.
name|getDataMap
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|getDefaultPackage
argument_list|()
decl_stmt|;
return|return
name|suggestedClassName
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|pkg
argument_list|,
name|entity
operator|.
name|getClassName
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|suggestedClientClassName
parameter_list|()
block|{
comment|// do not updated client class name if it is not allowed
if|if
condition|(
operator|!
name|entity
operator|.
name|isClientAllowed
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|pkg
init|=
name|entity
operator|.
name|getDataMap
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|getDefaultClientPackage
argument_list|()
decl_stmt|;
return|return
name|suggestedClassName
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|pkg
argument_list|,
name|entity
operator|.
name|getClientClassName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Suggests a new class name based on new entity name and current selections.      */
specifier|private
specifier|static
name|String
name|suggestedClassName
parameter_list|(
name|String
name|entityName
parameter_list|,
name|String
name|suggestedPackage
parameter_list|,
name|String
name|oldClassName
parameter_list|)
block|{
if|if
condition|(
name|entityName
operator|==
literal|null
operator|||
name|entityName
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// build suggested package...
name|String
name|pkg
init|=
name|suggestedPackage
decl_stmt|;
if|if
condition|(
name|oldClassName
operator|!=
literal|null
operator|&&
name|oldClassName
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|>
literal|0
condition|)
block|{
name|pkg
operator|=
name|oldClassName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|oldClassName
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// build suggested class name
name|int
name|lastDotIndex
init|=
name|entityName
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastDotIndex
operator|>=
literal|0
operator|&&
name|lastDotIndex
operator|<
name|entityName
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
name|entityName
operator|=
name|entityName
operator|.
name|substring
argument_list|(
name|lastDotIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|DataMap
operator|.
name|getNameWithPackage
argument_list|(
name|pkg
argument_list|,
name|entityName
argument_list|)
return|;
block|}
specifier|protected
name|void
name|initBindings
parameter_list|(
specifier|final
name|String
name|suggestedServerName
parameter_list|,
specifier|final
name|String
name|suggestedClientName
parameter_list|)
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
block|{
if|if
condition|(
name|view
operator|.
name|getClientClass
argument_list|()
operator|.
name|isSelected
argument_list|()
condition|)
block|{
name|entity
operator|.
name|setClientClassName
argument_list|(
name|suggestedClientName
argument_list|)
expr_stmt|;
name|updatePerformed
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|view
operator|.
name|getServerClass
argument_list|()
operator|.
name|isSelected
argument_list|()
condition|)
block|{
name|entity
operator|.
name|setClassName
argument_list|(
name|suggestedServerName
argument_list|)
expr_stmt|;
name|updatePerformed
operator|=
literal|true
expr_stmt|;
block|}
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
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
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
block|}
end_class

end_unit

