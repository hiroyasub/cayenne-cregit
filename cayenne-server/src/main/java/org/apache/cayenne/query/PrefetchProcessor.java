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
name|query
package|;
end_package

begin_comment
comment|/**  * A hierarchical visitor interface for traversing a tree of PrefetchTreeNodes. If any of  * the processing methods return false, node's children will be skipped from traversal.  *  * @since 1.2  * @see org.apache.cayenne.query.PrefetchTreeNode#traverse(PrefetchProcessor)  */
end_comment

begin_interface
specifier|public
interface|interface
name|PrefetchProcessor
block|{
name|boolean
name|startPhantomPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
function_decl|;
name|boolean
name|startDisjointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
function_decl|;
comment|/**      * @since 3.1      */
name|boolean
name|startDisjointByIdPrefetch
parameter_list|(
name|PrefetchTreeNode
name|prefetchTreeNode
parameter_list|)
function_decl|;
name|boolean
name|startJointPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
function_decl|;
name|boolean
name|startUnknownPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
function_decl|;
name|void
name|finishPrefetch
parameter_list|(
name|PrefetchTreeNode
name|node
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

