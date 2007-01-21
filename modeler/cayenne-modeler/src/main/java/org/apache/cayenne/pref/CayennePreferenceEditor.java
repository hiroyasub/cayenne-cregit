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
name|pref
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
name|access
operator|.
name|DataContext
import|;
end_import

begin_comment
comment|/**  * An editor for modifying CayennePreferenceService.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CayennePreferenceEditor
implements|implements
name|PreferenceEditor
block|{
specifier|protected
name|CayennePreferenceService
name|service
decl_stmt|;
specifier|protected
name|DataContext
name|editingContext
decl_stmt|;
specifier|protected
name|boolean
name|restartRequired
decl_stmt|;
specifier|protected
name|int
name|saveInterval
decl_stmt|;
specifier|public
name|CayennePreferenceEditor
parameter_list|(
name|CayennePreferenceService
name|service
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
name|this
operator|.
name|editingContext
operator|=
name|service
operator|.
name|getDataContext
argument_list|()
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|createDataContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|saveInterval
operator|=
name|service
operator|.
name|getSaveInterval
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isRestartRequired
parameter_list|()
block|{
return|return
name|restartRequired
return|;
block|}
specifier|protected
name|void
name|setRestartRequired
parameter_list|(
name|boolean
name|restartOnSave
parameter_list|)
block|{
name|this
operator|.
name|restartRequired
operator|=
name|restartOnSave
expr_stmt|;
block|}
specifier|protected
name|DataContext
name|getEditingContext
parameter_list|()
block|{
return|return
name|editingContext
return|;
block|}
specifier|public
name|Domain
name|editableInstance
parameter_list|(
name|Domain
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|.
name|getObjectContext
argument_list|()
operator|==
name|getEditingContext
argument_list|()
condition|)
block|{
return|return
name|object
return|;
block|}
return|return
operator|(
name|Domain
operator|)
name|getEditingContext
argument_list|()
operator|.
name|localObject
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|PreferenceDetail
name|createDetail
parameter_list|(
name|Domain
name|domain
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|domain
operator|=
name|editableInstance
argument_list|(
name|domain
argument_list|)
expr_stmt|;
name|DomainPreference
name|preference
init|=
operator|(
name|DomainPreference
operator|)
name|getEditingContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|DomainPreference
operator|.
name|class
argument_list|)
decl_stmt|;
name|preference
operator|.
name|setDomain
argument_list|(
name|domain
argument_list|)
expr_stmt|;
name|preference
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
name|preference
operator|.
name|getPreference
argument_list|()
return|;
block|}
specifier|public
name|PreferenceDetail
name|createDetail
parameter_list|(
name|Domain
name|domain
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
name|javaClass
parameter_list|)
block|{
name|domain
operator|=
name|editableInstance
argument_list|(
name|domain
argument_list|)
expr_stmt|;
name|DomainPreference
name|preferenceLink
init|=
operator|(
name|DomainPreference
operator|)
name|getEditingContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|DomainPreference
operator|.
name|class
argument_list|)
decl_stmt|;
name|preferenceLink
operator|.
name|setDomain
argument_list|(
name|domain
argument_list|)
expr_stmt|;
name|preferenceLink
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|PreferenceDetail
name|detail
init|=
operator|(
name|PreferenceDetail
operator|)
name|getEditingContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|javaClass
argument_list|)
decl_stmt|;
name|detail
operator|.
name|setDomainPreference
argument_list|(
name|preferenceLink
argument_list|)
expr_stmt|;
return|return
name|detail
return|;
block|}
specifier|public
name|PreferenceDetail
name|deleteDetail
parameter_list|(
name|Domain
name|domain
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|domain
operator|=
name|editableInstance
argument_list|(
name|domain
argument_list|)
expr_stmt|;
name|PreferenceDetail
name|detail
init|=
name|domain
operator|.
name|getDetail
argument_list|(
name|key
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|detail
operator|!=
literal|null
condition|)
block|{
name|DomainPreference
name|preference
init|=
name|detail
operator|.
name|getDomainPreference
argument_list|()
decl_stmt|;
name|preference
operator|.
name|setDomain
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|getEditingContext
argument_list|()
operator|.
name|deleteObject
argument_list|(
name|preference
argument_list|)
expr_stmt|;
name|getEditingContext
argument_list|()
operator|.
name|deleteObject
argument_list|(
name|detail
argument_list|)
expr_stmt|;
block|}
return|return
name|detail
return|;
block|}
specifier|public
name|int
name|getSaveInterval
parameter_list|()
block|{
return|return
name|saveInterval
return|;
block|}
specifier|public
name|void
name|setSaveInterval
parameter_list|(
name|int
name|ms
parameter_list|)
block|{
if|if
condition|(
name|saveInterval
operator|!=
name|ms
condition|)
block|{
name|saveInterval
operator|=
name|ms
expr_stmt|;
name|restartRequired
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|public
name|PreferenceService
name|getService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
specifier|public
name|void
name|save
parameter_list|()
block|{
name|service
operator|.
name|setSaveInterval
argument_list|(
name|saveInterval
argument_list|)
expr_stmt|;
name|editingContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
if|if
condition|(
name|restartRequired
condition|)
block|{
name|restart
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|revert
parameter_list|()
block|{
name|editingContext
operator|.
name|rollbackChanges
argument_list|()
expr_stmt|;
name|restartRequired
operator|=
literal|false
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|void
name|restart
parameter_list|()
function_decl|;
block|}
end_class

end_unit

