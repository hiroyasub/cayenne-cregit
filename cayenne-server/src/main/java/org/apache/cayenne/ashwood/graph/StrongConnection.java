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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

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
name|HashSet
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|ArrayStack
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|CollectionUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|functors
operator|.
name|TruePredicate
import|;
end_import

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|StrongConnection
parameter_list|<
name|E
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|Collection
argument_list|<
name|E
argument_list|>
argument_list|>
block|{
specifier|private
name|DigraphIteration
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|digraph
decl_stmt|;
specifier|private
name|DigraphIteration
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|reverseDigraph
decl_stmt|;
specifier|private
name|DigraphIteration
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|filteredDigraph
decl_stmt|;
specifier|private
name|DepthFirstStampSearch
argument_list|<
name|E
argument_list|>
name|directDfs
decl_stmt|;
specifier|private
name|DepthFirstSearch
argument_list|<
name|E
argument_list|>
name|reverseDfs
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|E
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Iterator
argument_list|<
name|E
argument_list|>
name|vertexIterator
decl_stmt|;
specifier|private
name|ArrayStack
name|dfsStack
decl_stmt|;
specifier|private
name|DFSSeenVerticesPredicate
name|reverseDFSFilter
decl_stmt|;
specifier|public
name|StrongConnection
parameter_list|(
name|DigraphIteration
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|digraph
parameter_list|)
block|{
name|this
operator|.
name|dfsStack
operator|=
operator|new
name|ArrayStack
argument_list|()
expr_stmt|;
name|this
operator|.
name|reverseDFSFilter
operator|=
operator|new
name|DFSSeenVerticesPredicate
argument_list|()
expr_stmt|;
name|this
operator|.
name|digraph
operator|=
name|digraph
expr_stmt|;
name|this
operator|.
name|filteredDigraph
operator|=
operator|new
name|FilterIteration
argument_list|<>
argument_list|(
name|digraph
argument_list|,
operator|new
name|NotSeenPredicate
argument_list|()
argument_list|,
name|TruePredicate
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|this
operator|.
name|reverseDigraph
operator|=
operator|new
name|FilterIteration
argument_list|<>
argument_list|(
operator|new
name|ReversedIteration
argument_list|<>
argument_list|(
name|digraph
argument_list|)
argument_list|,
name|reverseDFSFilter
argument_list|,
name|TruePredicate
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|this
operator|.
name|vertexIterator
operator|=
name|filteredDigraph
operator|.
name|vertexIterator
argument_list|()
expr_stmt|;
name|runDirectDFS
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
operator|!
name|dfsStack
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|E
argument_list|>
name|next
parameter_list|()
block|{
name|Collection
argument_list|<
name|E
argument_list|>
name|component
init|=
name|buildStronglyConnectedComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|dfsStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|runDirectDFS
argument_list|()
expr_stmt|;
block|}
return|return
name|component
return|;
block|}
annotation|@
name|Override
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
specifier|public
name|Digraph
argument_list|<
name|Collection
argument_list|<
name|E
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|contract
parameter_list|(
name|Digraph
argument_list|<
name|Collection
argument_list|<
name|E
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|contractedDigraph
parameter_list|)
block|{
name|Collection
argument_list|<
name|Collection
argument_list|<
name|E
argument_list|>
argument_list|>
name|components
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|CollectionUtils
operator|.
name|addAll
argument_list|(
name|components
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|E
argument_list|,
name|Collection
argument_list|<
name|E
argument_list|>
argument_list|>
name|memberToComponent
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Collection
argument_list|<
name|E
argument_list|>
name|c
range|:
name|components
control|)
block|{
for|for
control|(
name|E
name|e
range|:
name|c
control|)
block|{
name|memberToComponent
operator|.
name|put
argument_list|(
name|e
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Collection
argument_list|<
name|E
argument_list|>
name|origin
range|:
name|components
control|)
block|{
name|contractedDigraph
operator|.
name|addVertex
argument_list|(
name|origin
argument_list|)
expr_stmt|;
for|for
control|(
name|E
name|member
range|:
name|origin
control|)
block|{
for|for
control|(
name|ArcIterator
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|k
init|=
name|digraph
operator|.
name|outgoingIterator
argument_list|(
name|member
argument_list|)
init|;
name|k
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|V
name|arc
init|=
name|k
operator|.
name|next
argument_list|()
decl_stmt|;
name|E
name|dst
init|=
name|k
operator|.
name|getDestination
argument_list|()
decl_stmt|;
if|if
condition|(
name|origin
operator|.
name|contains
argument_list|(
name|dst
argument_list|)
condition|)
continue|continue;
name|Collection
argument_list|<
name|E
argument_list|>
name|destination
init|=
name|memberToComponent
operator|.
name|get
argument_list|(
name|dst
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|contractedArc
init|=
name|contractedDigraph
operator|.
name|getArc
argument_list|(
name|origin
argument_list|,
name|destination
argument_list|)
decl_stmt|;
if|if
condition|(
name|contractedArc
operator|==
literal|null
condition|)
block|{
name|contractedArc
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|arc
argument_list|)
expr_stmt|;
name|contractedDigraph
operator|.
name|putArc
argument_list|(
name|origin
argument_list|,
name|destination
argument_list|,
name|contractedArc
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|contractedArc
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|tmp
init|=
name|contractedArc
decl_stmt|;
name|contractedArc
operator|=
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
argument_list|()
expr_stmt|;
name|contractedArc
operator|.
name|addAll
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|contractedDigraph
operator|.
name|putArc
argument_list|(
name|origin
argument_list|,
name|destination
argument_list|,
name|contractedArc
argument_list|)
expr_stmt|;
block|}
name|contractedArc
operator|.
name|add
argument_list|(
name|arc
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|contractedDigraph
return|;
block|}
specifier|private
name|E
name|nextDFSRoot
parameter_list|()
block|{
return|return
name|vertexIterator
operator|.
name|hasNext
argument_list|()
condition|?
name|vertexIterator
operator|.
name|next
argument_list|()
else|:
literal|null
return|;
block|}
specifier|private
name|boolean
name|runDirectDFS
parameter_list|()
block|{
name|dfsStack
operator|.
name|clear
argument_list|()
expr_stmt|;
name|reverseDFSFilter
operator|.
name|seenVertices
operator|.
name|clear
argument_list|()
expr_stmt|;
name|E
name|root
init|=
name|nextDFSRoot
argument_list|()
decl_stmt|;
if|if
condition|(
name|root
operator|==
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|directDfs
operator|==
literal|null
condition|)
name|directDfs
operator|=
operator|new
name|DepthFirstStampSearch
argument_list|<>
argument_list|(
name|filteredDigraph
argument_list|,
name|root
argument_list|)
expr_stmt|;
else|else
name|directDfs
operator|.
name|reset
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|int
name|stamp
decl_stmt|;
name|E
name|vertex
decl_stmt|;
while|while
condition|(
name|directDfs
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|vertex
operator|=
name|directDfs
operator|.
name|next
argument_list|()
expr_stmt|;
name|stamp
operator|=
name|directDfs
operator|.
name|getStamp
argument_list|()
expr_stmt|;
if|if
condition|(
name|stamp
operator|==
name|DepthFirstStampSearch
operator|.
name|SHRINK_STAMP
operator|||
name|stamp
operator|==
name|DepthFirstStampSearch
operator|.
name|LEAF_STAMP
condition|)
block|{
comment|// if (seen.add(vertex)) {
name|dfsStack
operator|.
name|push
argument_list|(
name|vertex
argument_list|)
expr_stmt|;
name|reverseDFSFilter
operator|.
name|seenVertices
operator|.
name|add
argument_list|(
name|vertex
argument_list|)
expr_stmt|;
comment|// }
block|}
block|}
name|seen
operator|.
name|addAll
argument_list|(
name|dfsStack
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|Collection
argument_list|<
name|E
argument_list|>
name|buildStronglyConnectedComponent
parameter_list|()
block|{
name|E
name|root
init|=
operator|(
name|E
operator|)
name|dfsStack
operator|.
name|pop
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|E
argument_list|>
name|component
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|boolean
name|singleton
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|reverseDfs
operator|==
literal|null
condition|)
name|reverseDfs
operator|=
operator|new
name|DepthFirstSearch
argument_list|<
name|E
argument_list|>
argument_list|(
name|reverseDigraph
argument_list|,
name|root
argument_list|)
expr_stmt|;
else|else
name|reverseDfs
operator|.
name|reset
argument_list|(
name|root
argument_list|)
expr_stmt|;
while|while
condition|(
name|reverseDfs
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|E
name|vertex
init|=
name|reverseDfs
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|vertex
operator|!=
name|root
condition|)
block|{
if|if
condition|(
name|singleton
condition|)
block|{
name|Collection
argument_list|<
name|E
argument_list|>
name|tmp
init|=
name|component
decl_stmt|;
name|component
operator|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|()
expr_stmt|;
name|component
operator|.
name|addAll
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|singleton
operator|=
literal|false
expr_stmt|;
block|}
name|component
operator|.
name|add
argument_list|(
name|vertex
argument_list|)
expr_stmt|;
name|dfsStack
operator|.
name|remove
argument_list|(
name|vertex
argument_list|)
expr_stmt|;
block|}
block|}
name|reverseDFSFilter
operator|.
name|seenVertices
operator|.
name|removeAll
argument_list|(
name|component
argument_list|)
expr_stmt|;
return|return
name|component
return|;
block|}
specifier|private
class|class
name|DFSSeenVerticesPredicate
implements|implements
name|Predicate
block|{
specifier|private
name|Set
argument_list|<
name|E
argument_list|>
name|seenVertices
init|=
operator|new
name|HashSet
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|evaluate
parameter_list|(
name|Object
name|vertex
parameter_list|)
block|{
return|return
name|seenVertices
operator|.
name|contains
argument_list|(
name|vertex
argument_list|)
return|;
block|}
block|}
specifier|private
class|class
name|NotSeenPredicate
implements|implements
name|Predicate
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|evaluate
parameter_list|(
name|Object
name|vertex
parameter_list|)
block|{
return|return
operator|!
name|seen
operator|.
name|contains
argument_list|(
name|vertex
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

