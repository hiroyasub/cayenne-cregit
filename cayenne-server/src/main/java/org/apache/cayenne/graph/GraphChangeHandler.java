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

begin_comment
comment|/**  * Defines callback API that can be used by object graph nodes to notify of their state  * changes. Graph nodes can be any objects as long as each node supports a notion of a  * unique id within the graph and each directional arc has a unique identifier within its  * source node.  *   * @since 1.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|GraphChangeHandler
block|{
comment|/**      * Notifies implementing object that a node was assigned a new id.      */
name|void
name|nodeIdChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|newId
parameter_list|)
function_decl|;
comment|/**      * Notifies implementing object that a new node was created in the graph.      */
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
function_decl|;
comment|/**      * Notifies implementing object that a node was removed from the graph.      */
name|void
name|nodeRemoved
parameter_list|(
name|Object
name|nodeId
parameter_list|)
function_decl|;
comment|/**      * Notifies implementing object that a node's property was modified.      */
name|void
name|nodePropertyChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
function_decl|;
comment|/**      * Notifies implementing object that a new arc was created between two nodes.      */
name|void
name|arcCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|ArcId
name|arcId
parameter_list|)
function_decl|;
comment|/**      * Notifies implementing object that an arc between two nodes was deleted.      */
name|void
name|arcDeleted
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|ArcId
name|arcId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

