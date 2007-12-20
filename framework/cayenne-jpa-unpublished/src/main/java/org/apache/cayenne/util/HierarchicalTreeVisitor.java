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
name|util
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
name|project
operator|.
name|ProjectPath
import|;
end_import

begin_comment
comment|/**  * A hierarchical visitor interface used for tree structures traversal.  *   * @author Andrus Adamchik  */
end_comment

begin_comment
comment|// TODO, andrus, 4/24/2006 - move to Cayenne core in 2.0
end_comment

begin_interface
specifier|public
interface|interface
name|HierarchicalTreeVisitor
block|{
comment|/**      * Invoked in the beginning of the element subtree traversal. Traversal is done in a      * depth-first manner, so returning false from this method would suppress child      * elements traversal.      */
name|boolean
name|onStartNode
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
function_decl|;
comment|/**      * Invoked at the end of the annotation tree traversal, which is done in a depth-first      * manner.      */
name|void
name|onFinishNode
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
function_decl|;
comment|/**      * Returns an instance of the child visitor for a given project path and a type of      * child. Returning null would result in skipping the particular child type.      */
name|HierarchicalTreeVisitor
name|childVisitor
parameter_list|(
name|ProjectPath
name|path
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|childType
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

