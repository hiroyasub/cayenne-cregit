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

begin_comment
comment|/**  * A GraphDiff that is a list of other GraphDiffs.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|CompoundDiff
implements|implements
name|GraphDiff
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|5930690302335603082L
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|GraphDiff
argument_list|>
name|diffs
decl_stmt|;
comment|/** 	 * Creates an empty CompoundDiff instance. 	 */
specifier|public
name|CompoundDiff
parameter_list|()
block|{
block|}
comment|/** 	 * Creates CompoundDiff instance. Note that a List is not cloned in this 	 * constructor, so subsequent calls to add and addAll would modify the 	 * original list. 	 */
specifier|public
name|CompoundDiff
parameter_list|(
name|List
argument_list|<
name|GraphDiff
argument_list|>
name|diffs
parameter_list|)
block|{
name|this
operator|.
name|diffs
operator|=
name|diffs
expr_stmt|;
block|}
comment|/** 	 * Returns true if this diff has no other diffs or if all of its diffs are 	 * noops. 	 */
specifier|public
name|boolean
name|isNoop
parameter_list|()
block|{
if|if
condition|(
name|diffs
operator|==
literal|null
operator|||
name|diffs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|GraphDiff
name|diff
range|:
name|diffs
control|)
block|{
if|if
condition|(
operator|!
name|diff
operator|.
name|isNoop
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|List
argument_list|<
name|GraphDiff
argument_list|>
name|getDiffs
parameter_list|()
block|{
return|return
operator|(
name|diffs
operator|!=
literal|null
operator|)
condition|?
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|diffs
argument_list|)
else|:
name|Collections
operator|.
expr|<
name|GraphDiff
operator|>
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|void
name|add
parameter_list|(
name|GraphDiff
name|diff
parameter_list|)
block|{
name|nonNullDiffs
argument_list|()
operator|.
name|add
argument_list|(
name|diff
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|GraphDiff
argument_list|>
name|diffs
parameter_list|)
block|{
name|nonNullDiffs
argument_list|()
operator|.
name|addAll
argument_list|(
name|diffs
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Iterates over diffs list, calling "apply" on each individual diff. 	 */
specifier|public
name|void
name|apply
parameter_list|(
name|GraphChangeHandler
name|tracker
parameter_list|)
block|{
if|if
condition|(
name|diffs
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// implements a naive linear commit - simply replay stored operations
for|for
control|(
name|GraphDiff
name|change
range|:
name|diffs
control|)
block|{
name|change
operator|.
name|apply
argument_list|(
name|tracker
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Iterates over diffs list in reverse order, calling "apply" on each 	 * individual diff. 	 */
specifier|public
name|void
name|undo
parameter_list|(
name|GraphChangeHandler
name|tracker
parameter_list|)
block|{
if|if
condition|(
name|diffs
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|ListIterator
argument_list|<
name|GraphDiff
argument_list|>
name|it
init|=
name|diffs
operator|.
name|listIterator
argument_list|(
name|diffs
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasPrevious
argument_list|()
condition|)
block|{
name|GraphDiff
name|change
init|=
name|it
operator|.
name|previous
argument_list|()
decl_stmt|;
name|change
operator|.
name|undo
argument_list|(
name|tracker
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|GraphDiff
argument_list|>
name|nonNullDiffs
parameter_list|()
block|{
if|if
condition|(
name|diffs
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|diffs
operator|==
literal|null
condition|)
block|{
name|diffs
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|diffs
return|;
block|}
block|}
end_class

end_unit

