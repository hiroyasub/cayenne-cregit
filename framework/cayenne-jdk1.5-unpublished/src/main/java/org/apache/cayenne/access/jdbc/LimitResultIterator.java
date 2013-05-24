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
name|jdbc
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
name|Iterator
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
name|Map
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ResultIterator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|ResultIteratorIterator
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|LimitResultIterator
parameter_list|<
name|T
parameter_list|>
implements|implements
name|ResultIterator
argument_list|<
name|T
argument_list|>
block|{
specifier|protected
name|ResultIterator
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|nextDataObjectIds
decl_stmt|;
specifier|protected
name|int
name|fetchLimit
decl_stmt|;
specifier|protected
name|int
name|offset
decl_stmt|;
specifier|protected
name|int
name|fetchedSoFar
decl_stmt|;
specifier|protected
name|boolean
name|nextRow
decl_stmt|;
specifier|public
name|LimitResultIterator
parameter_list|(
name|ResultIterator
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|fetchLimit
parameter_list|)
block|{
if|if
condition|(
name|delegate
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null delegate iterator."
argument_list|)
throw|;
block|}
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|offset
operator|=
name|offset
expr_stmt|;
name|this
operator|.
name|fetchLimit
operator|=
name|fetchLimit
expr_stmt|;
name|checkOffset
argument_list|()
expr_stmt|;
name|checkNextRow
argument_list|()
expr_stmt|;
block|}
comment|/**      * @since 3.2      */
specifier|public
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|ResultIteratorIterator
argument_list|<
name|T
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|private
name|void
name|checkOffset
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|offset
operator|&&
name|delegate
operator|.
name|hasNextRow
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|delegate
operator|.
name|nextRow
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkNextRow
parameter_list|()
block|{
name|nextRow
operator|=
literal|false
expr_stmt|;
if|if
condition|(
operator|(
name|fetchLimit
operator|<=
literal|0
operator|||
name|fetchedSoFar
operator|<
name|fetchLimit
operator|)
operator|&&
name|this
operator|.
name|delegate
operator|.
name|hasNextRow
argument_list|()
condition|)
block|{
name|nextRow
operator|=
literal|true
expr_stmt|;
name|fetchedSoFar
operator|++
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
name|delegate
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|allRows
parameter_list|()
block|{
name|List
argument_list|<
name|T
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|hasNextRow
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|nextRow
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
specifier|public
name|boolean
name|hasNextRow
parameter_list|()
block|{
return|return
name|nextRow
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|T
name|nextRow
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNextRow
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|(
literal|"An attempt to read uninitialized row or past the end of the iterator."
argument_list|)
throw|;
block|}
name|T
name|row
init|=
name|delegate
operator|.
name|nextRow
argument_list|()
decl_stmt|;
name|checkNextRow
argument_list|()
expr_stmt|;
return|return
name|row
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|skipRow
parameter_list|()
block|{
name|delegate
operator|.
name|skipRow
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

