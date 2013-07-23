begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_comment
comment|/* ====================================================================  *  * Copyright(c) 2003, Andriy Shapochka  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions  * are met:  *  * 1. Redistributions of source code must retain the above  *    copyright notice, this list of conditions and the following  *    disclaimer.  *  * 2. Redistributions in binary form must reproduce the above  *    copyright notice, this list of conditions and the following  *    disclaimer in the documentation and/or other materials  *    provided with the distribution.  *  * 3. Neither the name of the ASHWOOD nor the  *    names of its contributors may be used to endorse or  *    promote products derived from this software without  *    specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE  * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL  * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR  * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER  * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,  * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * ====================================================================  *  * This software consists of voluntary contributions made by  * individuals on behalf of the ASHWOOD Project and was originally  * created by Andriy Shapochka.  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ashwood
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
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

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|IndegreeTopologicalSort
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|E
argument_list|>
block|{
specifier|private
name|Digraph
argument_list|<
name|E
argument_list|,
name|?
argument_list|>
name|digraph
decl_stmt|;
specifier|private
name|List
argument_list|<
name|E
argument_list|>
name|vertices
init|=
operator|new
name|LinkedList
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|E
argument_list|,
name|InDegree
argument_list|>
name|inDegrees
init|=
operator|new
name|HashMap
argument_list|<
name|E
argument_list|,
name|InDegree
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ListIterator
argument_list|<
name|E
argument_list|>
name|current
decl_stmt|;
specifier|public
name|IndegreeTopologicalSort
parameter_list|(
name|Digraph
argument_list|<
name|E
argument_list|,
name|?
argument_list|>
name|digraph
parameter_list|)
block|{
name|this
operator|.
name|digraph
operator|=
name|digraph
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|E
argument_list|>
name|i
init|=
name|digraph
operator|.
name|vertexIterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|E
name|vertex
init|=
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|vertices
operator|.
name|add
argument_list|(
name|vertex
argument_list|)
expr_stmt|;
name|inDegrees
operator|.
name|put
argument_list|(
name|vertex
argument_list|,
operator|new
name|InDegree
argument_list|(
name|digraph
operator|.
name|incomingSize
argument_list|(
name|vertex
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|current
operator|=
name|vertices
operator|.
name|listIterator
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
operator|!
name|vertices
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|E
name|next
parameter_list|()
block|{
name|boolean
name|progress
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|current
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|progress
condition|)
break|break;
name|progress
operator|=
literal|false
expr_stmt|;
name|current
operator|=
name|vertices
operator|.
name|listIterator
argument_list|()
expr_stmt|;
block|}
name|E
name|vertex
init|=
name|current
operator|.
name|next
argument_list|()
decl_stmt|;
name|InDegree
name|indegree
init|=
name|inDegrees
operator|.
name|get
argument_list|(
name|vertex
argument_list|)
decl_stmt|;
if|if
condition|(
name|indegree
operator|.
name|value
operator|==
literal|0
condition|)
block|{
name|removeVertex
argument_list|(
name|vertex
argument_list|)
expr_stmt|;
name|current
operator|.
name|remove
argument_list|()
expr_stmt|;
return|return
name|vertex
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|removeVertex
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
for|for
control|(
name|ArcIterator
argument_list|<
name|E
argument_list|,
name|?
argument_list|>
name|i
init|=
name|digraph
operator|.
name|outgoingIterator
argument_list|(
name|vertex
argument_list|)
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
name|E
name|dst
init|=
name|i
operator|.
name|getDestination
argument_list|()
decl_stmt|;
name|InDegree
name|indegree
init|=
name|inDegrees
operator|.
name|get
argument_list|(
name|dst
argument_list|)
decl_stmt|;
name|indegree
operator|.
name|value
operator|--
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Method remove() not supported."
argument_list|)
throw|;
block|}
specifier|private
specifier|static
class|class
name|InDegree
block|{
name|int
name|value
decl_stmt|;
name|InDegree
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|InDegree
operator|.
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
