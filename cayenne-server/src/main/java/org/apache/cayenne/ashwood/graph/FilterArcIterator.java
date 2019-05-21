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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
import|;
end_import

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|FilterArcIterator
parameter_list|<
name|E
parameter_list|,
name|V
parameter_list|>
implements|implements
name|ArcIterator
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
block|{
specifier|private
name|ArcIterator
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|iterator
decl_stmt|;
specifier|private
name|Predicate
argument_list|<
name|E
argument_list|>
name|acceptOrigin
decl_stmt|,
name|acceptDestination
decl_stmt|;
specifier|private
name|Predicate
argument_list|<
name|V
argument_list|>
name|acceptArc
decl_stmt|;
specifier|private
name|E
name|nextOrigin
decl_stmt|,
name|nextDst
decl_stmt|;
specifier|private
name|V
name|nextArc
decl_stmt|;
specifier|private
name|boolean
name|nextObjectSet
init|=
literal|false
decl_stmt|;
specifier|public
name|FilterArcIterator
parameter_list|(
name|ArcIterator
argument_list|<
name|E
argument_list|,
name|V
argument_list|>
name|iterator
parameter_list|,
name|Predicate
argument_list|<
name|E
argument_list|>
name|acceptOrigin
parameter_list|,
name|Predicate
argument_list|<
name|E
argument_list|>
name|acceptDestination
parameter_list|,
name|Predicate
argument_list|<
name|V
argument_list|>
name|acceptArc
parameter_list|)
block|{
name|this
operator|.
name|iterator
operator|=
name|iterator
expr_stmt|;
name|this
operator|.
name|acceptOrigin
operator|=
name|acceptOrigin
expr_stmt|;
name|this
operator|.
name|acceptDestination
operator|=
name|acceptDestination
expr_stmt|;
name|this
operator|.
name|acceptArc
operator|=
name|acceptArc
expr_stmt|;
name|nextOrigin
operator|=
name|iterator
operator|.
name|getOrigin
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|acceptOrigin
operator|.
name|test
argument_list|(
name|nextOrigin
argument_list|)
condition|)
block|{
name|nextOrigin
operator|=
literal|null
expr_stmt|;
block|}
name|nextDst
operator|=
name|iterator
operator|.
name|getDestination
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|acceptDestination
operator|.
name|test
argument_list|(
name|nextDst
argument_list|)
condition|)
block|{
name|nextDst
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|E
name|getOrigin
parameter_list|()
block|{
return|return
name|nextOrigin
return|;
block|}
specifier|public
name|E
name|getDestination
parameter_list|()
block|{
return|return
name|nextDst
return|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
if|if
condition|(
name|nextObjectSet
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
name|setNextObject
argument_list|()
return|;
block|}
block|}
specifier|public
name|V
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|nextObjectSet
condition|)
block|{
if|if
condition|(
operator|!
name|setNextObject
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
block|}
name|nextObjectSet
operator|=
literal|false
expr_stmt|;
return|return
name|nextArc
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
argument_list|()
throw|;
block|}
specifier|private
name|boolean
name|setNextObject
parameter_list|()
block|{
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|V
name|arc
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|E
name|origin
init|=
name|iterator
operator|.
name|getOrigin
argument_list|()
decl_stmt|;
name|E
name|dst
init|=
name|iterator
operator|.
name|getDestination
argument_list|()
decl_stmt|;
if|if
condition|(
name|acceptOrigin
operator|.
name|test
argument_list|(
name|origin
argument_list|)
operator|&&
name|acceptArc
operator|.
name|test
argument_list|(
name|arc
argument_list|)
operator|&&
name|acceptDestination
operator|.
name|test
argument_list|(
name|dst
argument_list|)
condition|)
block|{
name|nextArc
operator|=
name|arc
expr_stmt|;
name|nextOrigin
operator|=
name|origin
expr_stmt|;
name|nextDst
operator|=
name|dst
expr_stmt|;
name|nextObjectSet
operator|=
literal|true
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

