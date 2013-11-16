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
name|graph
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * Represents a change in an object graph. This can be a simple change (like a node  * property update) or a composite change that consists of a number of smaller changes.  *   * @since 1.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|GraphDiff
extends|extends
name|Serializable
block|{
comment|/**      * Returns true if this diff is simply a placeholder and does not perform any actual      * operation.      */
name|boolean
name|isNoop
parameter_list|()
function_decl|;
comment|/**      * Calls appropriate methods on the handler to "replay" this change.      */
name|void
name|apply
parameter_list|(
name|GraphChangeHandler
name|handler
parameter_list|)
function_decl|;
comment|/**      * Calls appropriate methods on the handler to revert this change.      */
name|void
name|undo
parameter_list|(
name|GraphChangeHandler
name|handler
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

