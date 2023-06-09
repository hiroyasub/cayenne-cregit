begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DepthFirstStampSearch
parameter_list|<
name|E
parameter_list|>
extends|extends
name|DepthFirstSearch
argument_list|<
name|E
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|int
name|UNDEFINED_STAMP
init|=
operator|-
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|GROW_DEPTH_STAMP
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|GROW_BREADTH_STAMP
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|SHRINK_STAMP
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|LEAF_STAMP
init|=
literal|3
decl_stmt|;
specifier|private
name|int
name|stamp
init|=
name|UNDEFINED_STAMP
decl_stmt|;
specifier|public
name|DepthFirstStampSearch
parameter_list|(
name|DigraphIteration
argument_list|<
name|E
argument_list|,
name|?
argument_list|>
name|factory
parameter_list|,
name|E
name|firstVertex
parameter_list|)
block|{
name|super
argument_list|(
name|factory
argument_list|,
name|firstVertex
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getStamp
parameter_list|()
block|{
return|return
name|stamp
return|;
block|}
annotation|@
name|Override
specifier|public
name|E
name|next
parameter_list|()
block|{
name|ArcIterator
argument_list|<
name|E
argument_list|,
name|?
argument_list|>
name|i
init|=
operator|(
name|ArcIterator
argument_list|<
name|E
argument_list|,
name|?
argument_list|>
operator|)
name|stack
operator|.
name|peek
argument_list|()
decl_stmt|;
name|E
name|origin
init|=
name|i
operator|.
name|getOrigin
argument_list|()
decl_stmt|;
name|E
name|dst
init|=
name|i
operator|.
name|getDestination
argument_list|()
decl_stmt|;
if|if
condition|(
name|dst
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
name|dst
operator|=
name|i
operator|.
name|getDestination
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|stack
operator|.
name|pop
argument_list|()
expr_stmt|;
comment|// shrink
name|stamp
operator|=
name|LEAF_STAMP
expr_stmt|;
return|return
name|origin
return|;
block|}
block|}
if|if
condition|(
name|seen
operator|.
name|add
argument_list|(
name|dst
argument_list|)
condition|)
block|{
name|stack
operator|.
name|push
argument_list|(
name|factory
operator|.
name|outgoingIterator
argument_list|(
name|dst
argument_list|)
argument_list|)
expr_stmt|;
comment|// grow depth
name|stamp
operator|=
name|GROW_DEPTH_STAMP
expr_stmt|;
if|if
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
comment|// grow breadth
name|stamp
operator|=
name|GROW_BREADTH_STAMP
expr_stmt|;
block|}
else|else
block|{
name|stack
operator|.
name|pop
argument_list|()
expr_stmt|;
comment|// shrink
name|stamp
operator|=
name|SHRINK_STAMP
expr_stmt|;
block|}
block|}
return|return
name|origin
return|;
block|}
block|}
end_class

end_unit

