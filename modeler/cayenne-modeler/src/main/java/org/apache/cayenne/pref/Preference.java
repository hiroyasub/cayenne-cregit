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
name|prefs
operator|.
name|Preferences
import|;
end_import

begin_interface
specifier|public
interface|interface
name|Preference
block|{
comment|/**      * Key for preferences.      */
name|String
name|CAYENNE_PREFERENCES_PATH
init|=
literal|"org/apache/cayenne"
decl_stmt|;
comment|/**      * Preferences node name for the editor      */
name|String
name|EDITOR
init|=
literal|"editor"
decl_stmt|;
comment|/**      * Preferences node name for list of the last 12 opened project files.      */
name|String
name|LAST_PROJ_FILES
init|=
literal|"lastSeveralProjectFiles"
decl_stmt|;
name|Preferences
name|getRootPreference
parameter_list|()
function_decl|;
name|Preferences
name|getCayennePreference
parameter_list|()
function_decl|;
name|Preferences
name|getCurrentPreference
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

