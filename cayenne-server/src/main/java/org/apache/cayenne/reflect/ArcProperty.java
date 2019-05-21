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
name|reflect
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
name|map
operator|.
name|DbRelationship
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
name|ObjRelationship
import|;
end_import

begin_comment
comment|/**  * A Property that represents an "arc" connecting source node to the target node  * in the graph.  *   * @since 1.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArcProperty
extends|extends
name|PropertyDescriptor
block|{
comment|/**      * Returns a relationship associated with this arc.      *       * @since 3.0      */
name|ObjRelationship
name|getRelationship
parameter_list|()
function_decl|;
comment|/**      * Returns a path over reverse DbRelationships for this arc's      * ObjRelationship.      *       * @since 4.0      */
name|String
name|getComplimentaryReverseDbRelationshipPath
parameter_list|()
function_decl|;
comment|/**      * Returns a complimentary reverse ArcProperty or null if no reverse arc      * exists.      */
name|ArcProperty
name|getComplimentaryReverseArc
parameter_list|()
function_decl|;
comment|/**      * Returns a ClassDescriptor for the type of graph nodes pointed to by this      * arc property. Note that considering that a target object may be a      * subclass of the class handled by the descriptor, users of this method may      * need to call {@link ClassDescriptor#getSubclassDescriptor(Class)} before      * using the descriptor to access objects.      */
name|ClassDescriptor
name|getTargetDescriptor
parameter_list|()
function_decl|;
comment|/**      * Returns whether a target node connected to a given object is an      * unresolved fault.      *       * @param source      *            an object that is a source object of the relationship.      */
name|boolean
name|isFault
parameter_list|(
name|Object
name|source
parameter_list|)
function_decl|;
comment|/**      * Turns a property of an object into a fault.      *       * @since 3.0      */
name|void
name|invalidate
parameter_list|(
name|Object
name|object
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

