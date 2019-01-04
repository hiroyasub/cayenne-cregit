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
name|access
operator|.
name|sqlbuilder
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|Node
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|NodeTreeVisitor
block|{
comment|/**      * @param node to visit      * @return false if visitor should stop      */
name|boolean
name|onNodeStart
parameter_list|(
name|Node
name|node
parameter_list|)
function_decl|;
comment|/**      * @param parent node      * @param child node      * @param index of this child in parent      * @param hasMore true if more children after this child      * @return false if visitor should stop      */
name|boolean
name|onChildNodeStart
parameter_list|(
name|Node
name|parent
parameter_list|,
name|Node
name|child
parameter_list|,
name|int
name|index
parameter_list|,
name|boolean
name|hasMore
parameter_list|)
function_decl|;
name|void
name|onChildNodeEnd
parameter_list|(
name|Node
name|parent
parameter_list|,
name|Node
name|child
parameter_list|,
name|int
name|index
parameter_list|,
name|boolean
name|hasMore
parameter_list|)
function_decl|;
name|void
name|onNodeEnd
parameter_list|(
name|Node
name|node
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

