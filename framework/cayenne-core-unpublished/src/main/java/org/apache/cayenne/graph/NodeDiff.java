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
comment|/**  * An abstract superclass of operations on individual nodes and arcs in a digraph.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|NodeDiff
implements|implements
name|GraphDiff
implements|,
name|Comparable
argument_list|<
name|NodeDiff
argument_list|>
block|{
specifier|protected
name|int
name|diffId
decl_stmt|;
specifier|protected
name|Object
name|nodeId
decl_stmt|;
specifier|public
name|NodeDiff
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|this
operator|.
name|nodeId
operator|=
name|nodeId
expr_stmt|;
block|}
specifier|public
name|NodeDiff
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|int
name|diffId
parameter_list|)
block|{
name|this
operator|.
name|nodeId
operator|=
name|nodeId
expr_stmt|;
name|this
operator|.
name|diffId
operator|=
name|diffId
expr_stmt|;
block|}
specifier|public
name|boolean
name|isNoop
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
specifier|abstract
name|void
name|apply
parameter_list|(
name|GraphChangeHandler
name|tracker
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|undo
parameter_list|(
name|GraphChangeHandler
name|tracker
parameter_list|)
function_decl|;
specifier|public
name|Object
name|getNodeId
parameter_list|()
block|{
return|return
name|nodeId
return|;
block|}
comment|/**      * Returns an id of this diff that can be used for various purposes, such as      * identifying the order of the diff in a sequence.      */
specifier|public
name|int
name|getDiffId
parameter_list|()
block|{
return|return
name|diffId
return|;
block|}
comment|/**      * Sets an id of this diff that can be used for various purposes, such as identifying      * the order of the diff in a sequence.      */
specifier|public
name|void
name|setDiffId
parameter_list|(
name|int
name|diffId
parameter_list|)
block|{
name|this
operator|.
name|diffId
operator|=
name|diffId
expr_stmt|;
block|}
comment|/**      * Implements a Comparable interface method to compare based on diffId property.      */
specifier|public
name|int
name|compareTo
parameter_list|(
name|NodeDiff
name|o
parameter_list|)
block|{
return|return
name|diffId
operator|-
name|o
operator|.
name|getDiffId
argument_list|()
return|;
block|}
block|}
end_class

end_unit
