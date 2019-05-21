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
name|graph
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

begin_comment
comment|/**  * Represents a generic "managed" graph with nodes mapped by their ids. Inherited  * GraphChangeHandler methods are intended as callbacks for graph node objects to notify  * graph of their changes.  *   * @since 1.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|GraphManager
extends|extends
name|GraphChangeHandler
block|{
comment|/**      * Returns a graph node given an id.      */
name|Object
name|getNode
parameter_list|(
name|Object
name|nodeId
parameter_list|)
function_decl|;
comment|/**      * "Registers" a graph node, usually storing the node in some internal map using its      * id as a key.      */
name|void
name|registerNode
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|nodeObject
parameter_list|)
function_decl|;
comment|/**      * "Unregisters" a graph node, forgetting any information associated with nodeId.      */
name|Object
name|unregisterNode
parameter_list|(
name|Object
name|nodeId
parameter_list|)
function_decl|;
comment|/**      * Returns all graph nodes registered with GraphManager.      */
name|Collection
argument_list|<
name|Object
argument_list|>
name|registeredNodes
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

