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
name|java
operator|.
name|util
operator|.
name|prefs
operator|.
name|Preferences
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
name|access
operator|.
name|DataContext
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
name|pref
operator|.
name|DBConnectionInfo
import|;
end_import

begin_comment
comment|/**  * An editor for modifying CayennePreferenceService.  *   */
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
specifier|protected
name|CayenneProjectPreferences
name|cayenneProjectPreferences
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Preferences
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|changedPreferences
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Preferences
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|removedPreferences
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Preferences
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|>
name|changedBooleanPreferences
decl_stmt|;
specifier|public
name|CayennePreferenceEditor
parameter_list|(
name|CayennePreferenceService
name|service
parameter_list|,
name|CayenneProjectPreferences
name|cayenneProjectPreferences
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
name|this
operator|.
name|cayenneProjectPreferences
operator|=
name|cayenneProjectPreferences
expr_stmt|;
name|this
operator|.
name|changedPreferences
operator|=
operator|new
name|HashMap
argument_list|<
name|Preferences
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|removedPreferences
operator|=
operator|new
name|HashMap
argument_list|<
name|Preferences
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|changedBooleanPreferences
operator|=
operator|new
name|HashMap
argument_list|<
name|Preferences
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|Preferences
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getRemovedPreferences
parameter_list|()
block|{
return|return
name|removedPreferences
return|;
block|}
specifier|public
name|Map
argument_list|<
name|Preferences
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getChangedPreferences
parameter_list|()
block|{
return|return
name|changedPreferences
return|;
block|}
specifier|public
name|Map
argument_list|<
name|Preferences
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|>
name|getChangedBooleanPreferences
parameter_list|()
block|{
return|return
name|changedBooleanPreferences
return|;
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
name|cayenneProjectPreferences
operator|.
name|getDetailObject
argument_list|(
name|DBConnectionInfo
operator|.
name|class
argument_list|)
operator|.
name|save
argument_list|()
expr_stmt|;
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
comment|// update boolean preferences
name|Iterator
name|it
init|=
name|changedBooleanPreferences
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
name|Preferences
name|pref
init|=
operator|(
name|Preferences
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|map
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Iterator
name|iterator
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
name|en
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
init|=
operator|(
name|String
operator|)
name|en
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Boolean
name|value
init|=
operator|(
name|Boolean
operator|)
name|en
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|pref
operator|.
name|putBoolean
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|// update string preferences
name|Iterator
name|iter
init|=
name|changedPreferences
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
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
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Preferences
name|pref
init|=
operator|(
name|Preferences
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Iterator
name|iterator
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
name|en
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
init|=
operator|(
name|String
operator|)
name|en
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|value
init|=
operator|(
name|String
operator|)
name|en
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|pref
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|// remove string preferences
name|Iterator
name|iterator
init|=
name|removedPreferences
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
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
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Preferences
name|pref
init|=
operator|(
name|Preferences
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Iterator
name|itRem
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itRem
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
name|en
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|itRem
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
init|=
operator|(
name|String
operator|)
name|en
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|pref
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|initClassLoader
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|revert
parameter_list|()
block|{
name|cayenneProjectPreferences
operator|.
name|getDetailObject
argument_list|(
name|DBConnectionInfo
operator|.
name|class
argument_list|)
operator|.
name|cancel
argument_list|()
expr_stmt|;
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

