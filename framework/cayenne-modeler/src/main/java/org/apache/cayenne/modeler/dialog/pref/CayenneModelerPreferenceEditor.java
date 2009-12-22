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
name|FileClassLoadingService
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
name|pref
operator|.
name|Domain
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
name|pref
operator|.
name|HSQLEmbeddedPreferenceEditor
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
name|pref
operator|.
name|HSQLEmbeddedPreferenceService
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
name|pref
operator|.
name|PreferenceDetail
import|;
end_import

begin_comment
comment|/**  * Specialized preferences editor for CayenneModeler.  *   */
end_comment

begin_class
specifier|public
class|class
name|CayenneModelerPreferenceEditor
extends|extends
name|HSQLEmbeddedPreferenceEditor
block|{
specifier|protected
name|boolean
name|refreshingClassLoader
decl_stmt|;
specifier|protected
name|Application
name|application
decl_stmt|;
specifier|public
name|CayenneModelerPreferenceEditor
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|HSQLEmbeddedPreferenceService
operator|)
name|application
operator|.
name|getPreferenceService
argument_list|()
argument_list|,
name|application
operator|.
name|getCayenneProjectPreferences
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|application
operator|=
name|application
expr_stmt|;
block|}
specifier|public
name|boolean
name|isRefreshingClassLoader
parameter_list|()
block|{
return|return
name|refreshingClassLoader
return|;
block|}
specifier|public
name|void
name|setRefreshingClassLoader
parameter_list|(
name|boolean
name|refreshingClassLoader
parameter_list|)
block|{
name|this
operator|.
name|refreshingClassLoader
operator|=
name|refreshingClassLoader
expr_stmt|;
block|}
specifier|public
name|void
name|save
parameter_list|()
block|{
name|super
operator|.
name|save
argument_list|()
expr_stmt|;
if|if
condition|(
name|isRefreshingClassLoader
argument_list|()
condition|)
block|{
name|application
operator|.
name|initClassLoader
argument_list|()
expr_stmt|;
name|refreshingClassLoader
operator|=
literal|false
expr_stmt|;
block|}
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
name|changeInDomain
argument_list|(
name|domain
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|createDetail
argument_list|(
name|domain
argument_list|,
name|key
argument_list|)
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
name|changeInDomain
argument_list|(
name|domain
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|deleteDetail
argument_list|(
name|domain
argument_list|,
name|key
argument_list|)
return|;
block|}
specifier|protected
name|void
name|changeInDomain
parameter_list|(
name|Domain
name|domain
parameter_list|)
block|{
if|if
condition|(
operator|!
name|refreshingClassLoader
operator|&&
name|domain
operator|!=
literal|null
operator|&&
name|FileClassLoadingService
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|domain
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|refreshingClassLoader
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

