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
name|commitlog
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ObjectId
import|;
end_import

begin_comment
comment|/**  * Represents a map of changes for a graph of persistent objects.  *   * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|ChangeMap
block|{
comment|/** 	 * Returns a map of changes. Note the same change sometimes can be present 	 * in the map twice. If ObjectId of an object has changed during the commit, 	 * the change will be accessible by both pre-commit and post-commit ID. To 	 * get unique changes, call {@link #getUniqueChanges()}. 	 */
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|?
extends|extends
name|ObjectChange
argument_list|>
name|getChanges
parameter_list|()
function_decl|;
name|Collection
argument_list|<
name|?
extends|extends
name|ObjectChange
argument_list|>
name|getUniqueChanges
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

