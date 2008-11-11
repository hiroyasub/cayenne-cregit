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

begin_comment
comment|/**  * Defines an API of a preferences editor used for editing preferences without affecting  * the rest of the application until the editing is finished.  *   */
end_comment

begin_interface
specifier|public
interface|interface
name|PreferenceEditor
block|{
comment|/**      * Returns an underlying PreferenceService which is a parent of this editor.      */
specifier|public
name|PreferenceService
name|getService
parameter_list|()
function_decl|;
comment|/**      * Creates a generic PreferenceDetail.      */
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
function_decl|;
comment|/**      * Creates PreferenceDetail of specified class.      */
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
function_decl|;
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
function_decl|;
specifier|public
name|Domain
name|editableInstance
parameter_list|(
name|Domain
name|domain
parameter_list|)
function_decl|;
specifier|public
name|void
name|save
parameter_list|()
function_decl|;
specifier|public
name|void
name|revert
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

