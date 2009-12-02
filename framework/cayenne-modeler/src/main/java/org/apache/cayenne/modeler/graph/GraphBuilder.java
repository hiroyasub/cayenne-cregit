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

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|UndoableEditListener
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
name|DataDomain
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
name|map
operator|.
name|Entity
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
name|ProjectController
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
name|util
operator|.
name|XMLSerializable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgraph
operator|.
name|JGraph
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgraph
operator|.
name|graph
operator|.
name|DefaultGraphCell
import|;
end_import

begin_comment
comment|/**  * Interface for building graphs which represent some prespective of a domain  */
end_comment

begin_interface
specifier|public
interface|interface
name|GraphBuilder
extends|extends
name|Serializable
extends|,
name|XMLSerializable
extends|,
name|UndoableEditListener
block|{
specifier|public
specifier|static
specifier|final
name|double
name|ZOOM_FACTOR
init|=
literal|1.3
decl_stmt|;
comment|/**      * Builds graph      */
specifier|public
name|void
name|buildGraph
parameter_list|(
name|ProjectController
name|mediator
parameter_list|,
name|DataDomain
name|domain
parameter_list|,
name|boolean
name|layout
parameter_list|)
function_decl|;
comment|/**      * Invoked at destroying of the builder      */
specifier|public
name|void
name|destroy
parameter_list|()
function_decl|;
comment|/**      * Returns built graph for this builder      */
specifier|public
name|JGraph
name|getGraph
parameter_list|()
function_decl|;
comment|/**      * Returns domain.      */
specifier|public
name|DataDomain
name|getDataDomain
parameter_list|()
function_decl|;
comment|/**      * Returns type of the graph      */
specifier|public
name|GraphType
name|getType
parameter_list|()
function_decl|;
comment|/**      * Returns selected entity,<code>null</code> if none is selected      */
specifier|public
name|Entity
name|getSelectedEntity
parameter_list|()
function_decl|;
comment|/**      * Returns cell of an entity      */
specifier|public
name|DefaultGraphCell
name|getEntityCell
parameter_list|(
name|String
name|entityName
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

