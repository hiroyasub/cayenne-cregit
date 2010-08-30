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
name|Collection
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
name|Map
operator|.
name|Entry
import|;
end_import

begin_class
specifier|public
class|class
name|MapDigraph
parameter_list|<
name|E
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Digraph
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
block|{
specifier|private
name|Map
argument_list|<
name|E
argument_list|,
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
argument_list|>
name|graph
decl_stmt|;
specifier|private
name|int
name|size
decl_stmt|;
specifier|public
name|MapDigraph
parameter_list|()
block|{
name|graph
operator|=
operator|new
name|HashMap
argument_list|<
name|E
argument_list|,
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|addVertex
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
if|if
condition|(
name|graph
operator|.
name|containsKey
argument_list|(
name|vertex
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|graph
operator|.
name|put
argument_list|(
name|vertex
argument_list|,
operator|new
name|HashMap
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|addAllVertices
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|vertices
parameter_list|)
block|{
if|if
condition|(
name|graph
operator|.
name|keySet
argument_list|()
operator|.
name|containsAll
argument_list|(
name|vertices
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|E
name|vertex
range|:
name|vertices
control|)
block|{
name|addVertex
argument_list|(
name|vertex
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|V
name|putArc
parameter_list|(
name|E
name|origin
parameter_list|,
name|E
name|destination
parameter_list|,
name|V
name|arc
parameter_list|)
block|{
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|destinations
init|=
name|graph
operator|.
name|get
argument_list|(
name|origin
argument_list|)
decl_stmt|;
if|if
condition|(
name|destinations
operator|==
literal|null
condition|)
block|{
name|destinations
operator|=
operator|new
name|HashMap
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
argument_list|()
expr_stmt|;
name|graph
operator|.
name|put
argument_list|(
name|origin
argument_list|,
name|destinations
argument_list|)
expr_stmt|;
block|}
name|addVertex
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|V
name|oldArc
init|=
name|destinations
operator|.
name|put
argument_list|(
name|destination
argument_list|,
name|arc
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldArc
operator|==
literal|null
condition|)
block|{
name|size
operator|++
expr_stmt|;
block|}
return|return
name|oldArc
return|;
block|}
specifier|public
name|V
name|getArc
parameter_list|(
name|Object
name|origin
parameter_list|,
name|Object
name|destination
parameter_list|)
block|{
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|destinations
init|=
name|graph
operator|.
name|get
argument_list|(
name|origin
argument_list|)
decl_stmt|;
if|if
condition|(
name|destinations
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|destinations
operator|.
name|get
argument_list|(
name|destination
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|removeVertex
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|destination
init|=
name|graph
operator|.
name|remove
argument_list|(
name|vertex
argument_list|)
decl_stmt|;
if|if
condition|(
name|destination
operator|!=
literal|null
condition|)
name|size
operator|-=
name|destination
operator|.
name|size
argument_list|()
expr_stmt|;
else|else
return|return
literal|false
return|;
name|removeIncoming
argument_list|(
name|vertex
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|removeAllVertices
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|vertices
parameter_list|)
block|{
name|boolean
name|modified
init|=
literal|false
decl_stmt|;
for|for
control|(
name|E
name|vertex
range|:
name|vertices
control|)
block|{
name|modified
operator||=
name|removeVertex
argument_list|(
name|vertex
argument_list|)
expr_stmt|;
block|}
return|return
name|modified
return|;
block|}
specifier|public
name|Object
name|removeArc
parameter_list|(
name|E
name|origin
parameter_list|,
name|E
name|destination
parameter_list|)
block|{
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|destinations
init|=
name|graph
operator|.
name|get
argument_list|(
name|origin
argument_list|)
decl_stmt|;
if|if
condition|(
name|destinations
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|V
name|arc
init|=
name|destinations
operator|.
name|remove
argument_list|(
name|destination
argument_list|)
decl_stmt|;
if|if
condition|(
name|arc
operator|!=
literal|null
condition|)
name|size
operator|--
expr_stmt|;
return|return
name|arc
return|;
block|}
specifier|public
name|boolean
name|removeIncoming
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
name|boolean
name|modified
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|destinations
range|:
name|graph
operator|.
name|values
argument_list|()
control|)
block|{
name|Object
name|arc
init|=
name|destinations
operator|.
name|remove
argument_list|(
name|vertex
argument_list|)
decl_stmt|;
if|if
condition|(
name|arc
operator|!=
literal|null
condition|)
name|size
operator|--
expr_stmt|;
name|modified
operator||=
operator|(
name|arc
operator|!=
literal|null
operator|)
expr_stmt|;
block|}
return|return
name|modified
return|;
block|}
specifier|public
name|boolean
name|removeOutgoing
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|destinations
init|=
name|graph
operator|.
name|remove
argument_list|(
name|vertex
argument_list|)
decl_stmt|;
if|if
condition|(
name|destinations
operator|!=
literal|null
condition|)
name|size
operator|-=
name|destinations
operator|.
name|size
argument_list|()
expr_stmt|;
else|else
return|return
literal|false
return|;
name|boolean
name|modified
init|=
operator|!
name|destinations
operator|.
name|isEmpty
argument_list|()
decl_stmt|;
name|destinations
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
name|modified
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|vertexIterator
parameter_list|()
block|{
return|return
name|graph
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|ArcIterator
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|arcIterator
parameter_list|()
block|{
return|return
operator|new
name|AllArcIterator
argument_list|()
return|;
block|}
specifier|public
name|ArcIterator
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|outgoingIterator
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
if|if
condition|(
operator|!
name|containsVertex
argument_list|(
name|vertex
argument_list|)
condition|)
block|{
return|return
name|ArcIterator
operator|.
name|EMPTY_ITERATOR
return|;
block|}
return|return
operator|new
name|OutgoingArcIterator
argument_list|(
name|vertex
argument_list|)
return|;
block|}
specifier|public
name|ArcIterator
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|incomingIterator
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
if|if
condition|(
operator|!
name|containsVertex
argument_list|(
name|vertex
argument_list|)
condition|)
return|return
name|ArcIterator
operator|.
name|EMPTY_ITERATOR
return|;
return|return
operator|new
name|IncomingArcIterator
argument_list|(
name|vertex
argument_list|)
return|;
block|}
specifier|public
name|int
name|order
parameter_list|()
block|{
return|return
name|graph
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|size
return|;
block|}
specifier|public
name|int
name|outgoingSize
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|destinations
init|=
name|graph
operator|.
name|get
argument_list|(
name|vertex
argument_list|)
decl_stmt|;
if|if
condition|(
name|destinations
operator|==
literal|null
condition|)
return|return
literal|0
return|;
else|else
return|return
name|destinations
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|int
name|incomingSize
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
if|if
condition|(
operator|!
name|graph
operator|.
name|containsKey
argument_list|(
name|vertex
argument_list|)
condition|)
return|return
literal|0
return|;
for|for
control|(
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|destinations
range|:
name|graph
operator|.
name|values
argument_list|()
control|)
block|{
name|count
operator|+=
operator|(
name|destinations
operator|.
name|containsKey
argument_list|(
name|vertex
argument_list|)
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
block|}
return|return
name|count
return|;
block|}
specifier|public
name|boolean
name|containsVertex
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
return|return
name|graph
operator|.
name|containsKey
argument_list|(
name|vertex
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|containsAllVertices
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|vertices
parameter_list|)
block|{
return|return
name|graph
operator|.
name|keySet
argument_list|()
operator|.
name|containsAll
argument_list|(
name|vertices
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasArc
parameter_list|(
name|E
name|origin
parameter_list|,
name|E
name|destination
parameter_list|)
block|{
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|destinations
init|=
name|graph
operator|.
name|get
argument_list|(
name|origin
argument_list|)
decl_stmt|;
if|if
condition|(
name|destinations
operator|==
literal|null
condition|)
return|return
literal|false
return|;
return|return
name|destinations
operator|.
name|containsKey
argument_list|(
name|destination
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|graph
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isOutgoingEmpty
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
return|return
name|outgoingSize
argument_list|(
name|vertex
argument_list|)
operator|==
literal|0
return|;
block|}
specifier|public
name|boolean
name|isIncomingEmpty
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
return|return
name|incomingSize
argument_list|(
name|vertex
argument_list|)
operator|==
literal|0
return|;
block|}
specifier|private
class|class
name|AllArcIterator
implements|implements
name|ArcIterator
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
block|{
specifier|private
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|,
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|>
name|originIterator
decl_stmt|;
specifier|private
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
argument_list|>
name|destinationIterator
decl_stmt|;
specifier|private
name|E
name|origin
decl_stmt|,
name|nextOrigin
decl_stmt|;
specifier|private
name|E
name|destination
decl_stmt|,
name|nextDst
decl_stmt|;
specifier|private
name|V
name|arc
decl_stmt|,
name|nextArc
decl_stmt|;
specifier|private
name|AllArcIterator
parameter_list|()
block|{
name|originIterator
operator|=
name|graph
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|next
argument_list|()
expr_stmt|;
block|}
specifier|public
name|E
name|getOrigin
parameter_list|()
block|{
return|return
name|origin
return|;
block|}
specifier|public
name|E
name|getDestination
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|nextArc
operator|!=
literal|null
return|;
block|}
specifier|public
name|V
name|next
parameter_list|()
block|{
name|origin
operator|=
name|nextOrigin
expr_stmt|;
name|destination
operator|=
name|nextDst
expr_stmt|;
name|arc
operator|=
name|nextArc
expr_stmt|;
if|if
condition|(
name|destinationIterator
operator|==
literal|null
operator|||
operator|!
name|destinationIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|nextOrigin
operator|=
literal|null
expr_stmt|;
name|nextDst
operator|=
literal|null
expr_stmt|;
name|nextArc
operator|=
literal|null
expr_stmt|;
while|while
condition|(
name|originIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entry
argument_list|<
name|E
argument_list|,
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
argument_list|>
name|entry
init|=
name|originIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|destinationIterator
operator|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
if|if
condition|(
name|destinationIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|nextOrigin
operator|=
name|entry
operator|.
name|getKey
argument_list|()
expr_stmt|;
name|Entry
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|entry1
init|=
name|destinationIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|nextDst
operator|=
name|entry1
operator|.
name|getKey
argument_list|()
expr_stmt|;
name|nextArc
operator|=
name|entry1
operator|.
name|getValue
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
else|else
block|{
name|Entry
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|entry1
init|=
name|destinationIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|nextDst
operator|=
name|entry1
operator|.
name|getKey
argument_list|()
expr_stmt|;
name|nextArc
operator|=
name|entry1
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
return|return
name|arc
return|;
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
literal|"Method remove() not yet implemented."
argument_list|)
throw|;
block|}
block|}
specifier|private
class|class
name|OutgoingArcIterator
implements|implements
name|ArcIterator
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
block|{
specifier|private
name|E
name|origin
decl_stmt|;
specifier|private
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
argument_list|>
name|dstIt
decl_stmt|;
specifier|private
name|Entry
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|entry
decl_stmt|;
specifier|private
name|OutgoingArcIterator
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
name|origin
operator|=
name|vertex
expr_stmt|;
name|dstIt
operator|=
name|graph
operator|.
name|get
argument_list|(
name|vertex
argument_list|)
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
specifier|public
name|E
name|getOrigin
parameter_list|()
block|{
return|return
name|origin
return|;
block|}
specifier|public
name|E
name|getDestination
parameter_list|()
block|{
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
return|return
literal|null
return|;
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|dstIt
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|V
name|next
parameter_list|()
block|{
name|entry
operator|=
name|dstIt
operator|.
name|next
argument_list|()
expr_stmt|;
return|return
name|entry
operator|.
name|getValue
argument_list|()
return|;
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
literal|"Method remove() not yet implemented."
argument_list|)
throw|;
block|}
block|}
specifier|private
class|class
name|IncomingArcIterator
implements|implements
name|ArcIterator
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
block|{
specifier|private
name|E
name|dst
decl_stmt|;
specifier|private
name|E
name|origin
decl_stmt|,
name|nextOrigin
decl_stmt|;
specifier|private
name|V
name|arc
decl_stmt|,
name|nextArc
decl_stmt|;
specifier|private
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|,
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|>
name|graphIt
decl_stmt|;
specifier|private
name|IncomingArcIterator
parameter_list|(
name|E
name|vertex
parameter_list|)
block|{
name|dst
operator|=
name|vertex
expr_stmt|;
name|graphIt
operator|=
name|graph
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|next
argument_list|()
expr_stmt|;
block|}
specifier|public
name|E
name|getOrigin
parameter_list|()
block|{
return|return
name|origin
return|;
block|}
specifier|public
name|E
name|getDestination
parameter_list|()
block|{
return|return
name|dst
return|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
operator|(
name|nextArc
operator|!=
literal|null
operator|)
return|;
block|}
specifier|public
name|V
name|next
parameter_list|()
block|{
name|origin
operator|=
name|nextOrigin
expr_stmt|;
name|arc
operator|=
name|nextArc
expr_stmt|;
name|nextArc
operator|=
literal|null
expr_stmt|;
name|nextOrigin
operator|=
literal|null
expr_stmt|;
while|while
condition|(
name|graphIt
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entry
argument_list|<
name|E
argument_list|,
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
argument_list|>
name|entry
init|=
name|graphIt
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|destinations
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|nextArc
operator|=
name|destinations
operator|.
name|get
argument_list|(
name|dst
argument_list|)
expr_stmt|;
if|if
condition|(
name|nextArc
operator|!=
literal|null
condition|)
block|{
name|nextOrigin
operator|=
name|entry
operator|.
name|getKey
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
return|return
name|arc
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|java
operator|.
name|lang
operator|.
name|UnsupportedOperationException
argument_list|(
literal|"Method remove() not yet implemented."
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

