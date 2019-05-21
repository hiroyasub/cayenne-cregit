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
name|pref
package|;
end_package

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

begin_class
specifier|public
specifier|abstract
class|class
name|PreferenceDecorator
implements|implements
name|Preference
block|{
specifier|protected
name|Preference
name|delegate
decl_stmt|;
specifier|public
name|PreferenceDecorator
parameter_list|(
name|Preference
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
specifier|public
name|Preferences
name|getCayennePreference
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getCayennePreference
argument_list|()
return|;
block|}
specifier|public
name|Preferences
name|getCurrentPreference
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getCurrentPreference
argument_list|()
return|;
block|}
specifier|public
name|Preferences
name|getRootPreference
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getRootPreference
argument_list|()
return|;
block|}
block|}
end_class

end_unit

