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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|DataRow
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
name|map
operator|.
name|DbAttribute
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
name|map
operator|.
name|DbEntity
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * A ResultIterator that does in-memory filtering of rows to return only  * distinct rows. Distinct comparison is done by comparing ObjectIds created  * from each row. Internally DistinctResultIterator wraps another ResultIterator  * that provides the actual rows.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|DistinctResultIterator
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
name|Set
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|fetchedIds
decl_stmt|;
specifier|protected
name|DataRow
name|nextDataRow
decl_stmt|;
specifier|protected
name|DbEntity
name|defaultEntity
decl_stmt|;
specifier|protected
name|boolean
name|compareFullRows
decl_stmt|;
comment|/**      * Creates new DistinctResultIterator wrapping another ResultIterator.      *       * @param delegate      * @param defaultEntity      *            an entity needed to build ObjectIds for distinct comparison.      */
specifier|public
name|DistinctResultIterator
parameter_list|(
name|ResultIterator
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|,
name|DbEntity
name|defaultEntity
parameter_list|,
name|boolean
name|compareFullRows
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
literal|"Null wrapped iterator."
argument_list|)
throw|;
block|}
if|if
condition|(
name|defaultEntity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null defaultEntity."
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
name|defaultEntity
operator|=
name|defaultEntity
expr_stmt|;
name|this
operator|.
name|fetchedIds
operator|=
operator|new
name|HashSet
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|compareFullRows
operator|=
name|compareFullRows
expr_stmt|;
name|checkNextRow
argument_list|()
expr_stmt|;
block|}
comment|/**      * @since 4.0      */
annotation|@
name|Override
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
comment|/**      * Closes underlying ResultIterator.      */
annotation|@
name|Override
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
annotation|@
name|Override
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
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|this
operator|.
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
annotation|@
name|Override
specifier|public
name|boolean
name|hasNextRow
parameter_list|()
block|{
return|return
name|nextDataRow
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
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
comment|// TODO:
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
name|row
init|=
operator|(
name|T
operator|)
name|nextDataRow
decl_stmt|;
name|checkNextRow
argument_list|()
expr_stmt|;
return|return
name|row
return|;
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|void
name|skipRow
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
name|checkNextRow
argument_list|()
expr_stmt|;
block|}
name|void
name|checkNextRow
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|compareFullRows
condition|)
block|{
name|checkNextUniqueRow
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|checkNextRowWithUniqueId
argument_list|()
expr_stmt|;
block|}
block|}
name|void
name|checkNextUniqueRow
parameter_list|()
block|{
name|nextDataRow
operator|=
literal|null
expr_stmt|;
while|while
condition|(
name|delegate
operator|.
name|hasNextRow
argument_list|()
condition|)
block|{
name|DataRow
name|next
init|=
operator|(
name|DataRow
operator|)
name|delegate
operator|.
name|nextRow
argument_list|()
decl_stmt|;
if|if
condition|(
name|fetchedIds
operator|.
name|add
argument_list|(
name|next
argument_list|)
condition|)
block|{
name|this
operator|.
name|nextDataRow
operator|=
name|next
expr_stmt|;
break|break;
block|}
block|}
block|}
name|void
name|checkNextRowWithUniqueId
parameter_list|()
block|{
name|nextDataRow
operator|=
literal|null
expr_stmt|;
while|while
condition|(
name|delegate
operator|.
name|hasNextRow
argument_list|()
condition|)
block|{
name|DataRow
name|next
init|=
operator|(
name|DataRow
operator|)
name|delegate
operator|.
name|nextRow
argument_list|()
decl_stmt|;
comment|// create id map...
comment|// TODO: this can be optimized by creating an array with id keys
comment|// to avoid iterating over default entity attributes...
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|id
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|DbAttribute
name|pk
range|:
name|defaultEntity
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
name|id
operator|.
name|put
argument_list|(
name|pk
operator|.
name|getName
argument_list|()
argument_list|,
name|next
operator|.
name|get
argument_list|(
name|pk
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fetchedIds
operator|.
name|add
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|this
operator|.
name|nextDataRow
operator|=
name|next
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
end_class

end_unit

