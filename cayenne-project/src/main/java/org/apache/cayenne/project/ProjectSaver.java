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
name|project
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
name|resource
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * Defines API of a project saver.  *   * @since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|ProjectSaver
block|{
comment|/**      * Returns a version of the project configuration supported by the current runtime.      */
name|String
name|getSupportedVersion
parameter_list|()
function_decl|;
comment|/**      * Saves project in the location of its current configuration sources. Since resource      * names are determined using a naming convention based on the project node names, if      * any of the nodes were renamed, the old locations will be deleted. After saving,      * resets configuration sources of all project objects to the new Resources.      */
name|void
name|save
parameter_list|(
name|Project
name|project
parameter_list|)
function_decl|;
comment|/**      * Saves project in a location defined by the 'baseDirectory' Resource. Does not      * delete the old resource locations. After saving, resets configuration sources of      * all project objects to the new Resources.      */
name|void
name|saveAs
parameter_list|(
name|Project
name|project
parameter_list|,
name|Resource
name|baseDirectory
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

