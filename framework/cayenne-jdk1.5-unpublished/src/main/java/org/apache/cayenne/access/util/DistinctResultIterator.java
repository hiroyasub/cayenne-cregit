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
name|util
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
name|Set
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
name|CayenneException
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
name|access
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

begin_comment
comment|/**  * A ResultIterator that does in-memory filtering of rows to return only distinct rows.  * Distinct comparison is done by comparing ObjectIds created from each row. Internally  * DistinctResultIterator wraps another ResultIterator that provides the actual rows. The  * current limitation is that once switched to reading ids instead of rows (i.e. when  * "nextObjectId()" is called for the first time), it can't be used to read data rows  * again. This is pretty sensible for most things in Cayenne.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DistinctResultIterator
implements|implements
name|ResultIterator
block|{
specifier|protected
name|ResultIterator
name|wrappedIterator
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
specifier|protected
name|boolean
name|readingIds
decl_stmt|;
comment|/**      * Creates new DistinctResultIterator wrapping another ResultIterator.      *       * @param wrappedIterator      * @param defaultEntity an entity needed to build ObjectIds for distinct comparison.      */
specifier|public
name|DistinctResultIterator
parameter_list|(
name|ResultIterator
name|wrappedIterator
parameter_list|,
name|DbEntity
name|defaultEntity
parameter_list|,
name|boolean
name|compareFullRows
parameter_list|)
throws|throws
name|CayenneException
block|{
if|if
condition|(
name|wrappedIterator
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneException
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
name|CayenneException
argument_list|(
literal|"Null defaultEntity."
argument_list|)
throw|;
block|}
name|this
operator|.
name|wrappedIterator
operator|=
name|wrappedIterator
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
comment|/**      * CLoses underlying ResultIterator.      */
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|CayenneException
block|{
name|wrappedIterator
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns all data rows.      */
specifier|public
name|List
name|dataRows
parameter_list|(
name|boolean
name|close
parameter_list|)
throws|throws
name|CayenneException
block|{
name|List
argument_list|<
name|Map
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
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
name|this
operator|.
name|nextDataRow
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|close
condition|)
block|{
name|this
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|int
name|getDataRowWidth
parameter_list|()
block|{
return|return
name|wrappedIterator
operator|.
name|getDataRowWidth
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|hasNextRow
parameter_list|()
throws|throws
name|CayenneException
block|{
return|return
name|nextDataRow
operator|!=
literal|null
return|;
block|}
specifier|public
name|Map
name|nextDataRow
parameter_list|()
throws|throws
name|CayenneException
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
name|CayenneException
argument_list|(
literal|"An attempt to read uninitialized row or past the end of the iterator."
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
name|nextDataRow
decl_stmt|;
name|checkNextRow
argument_list|()
expr_stmt|;
return|return
name|row
return|;
block|}
comment|/**      * Returns a Map for the next ObjectId. After calling this method, calls to      * "nextDataRow()" will result in exceptions.      */
specifier|public
name|Map
name|nextObjectId
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
throws|throws
name|CayenneException
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
name|CayenneException
argument_list|(
literal|"An attempt to read uninitialized row or past the end of the iterator."
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
name|nextDataRow
decl_stmt|;
comment|// if we were previously reading full rows, we need to strip extra keys...
if|if
condition|(
operator|!
name|readingIds
condition|)
block|{
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|it
init|=
name|row
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|DbAttribute
name|attribute
init|=
operator|(
name|DbAttribute
operator|)
name|entity
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|==
literal|null
operator|||
operator|!
name|attribute
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|checkNextId
argument_list|(
name|entity
argument_list|)
expr_stmt|;
return|return
name|row
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|Object
name|nextId
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
throws|throws
name|CayenneException
block|{
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|pk
init|=
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|pk
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
return|return
name|nextObjectId
argument_list|(
name|entity
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
name|hasNextRow
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"An attempt to read uninitialized row or past the end of the iterator."
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
name|nextDataRow
decl_stmt|;
name|checkNextId
argument_list|(
name|entity
argument_list|)
expr_stmt|;
comment|// TODO: andrus 3/6/2008: not very efficient ... a better algorithm would've
comment|// relied on wrapped iterator 'nextId' method.
return|return
name|row
operator|.
name|get
argument_list|(
name|pk
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|skipDataRow
parameter_list|()
throws|throws
name|CayenneException
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
name|CayenneException
argument_list|(
literal|"An attempt to read uninitialized row or past the end of the iterator."
argument_list|)
throw|;
block|}
if|if
condition|(
name|readingIds
condition|)
block|{
name|checkNextId
argument_list|(
name|defaultEntity
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|checkNextRow
argument_list|()
expr_stmt|;
block|}
block|}
name|void
name|checkNextRow
parameter_list|()
throws|throws
name|CayenneException
block|{
if|if
condition|(
name|readingIds
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Can't go back from reading ObjectIds to reading rows."
argument_list|)
throw|;
block|}
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
throws|throws
name|CayenneException
block|{
name|nextDataRow
operator|=
literal|null
expr_stmt|;
while|while
condition|(
name|wrappedIterator
operator|.
name|hasNextRow
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|next
init|=
name|wrappedIterator
operator|.
name|nextDataRow
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
throws|throws
name|CayenneException
block|{
name|nextDataRow
operator|=
literal|null
expr_stmt|;
while|while
condition|(
name|wrappedIterator
operator|.
name|hasNextRow
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|next
init|=
name|wrappedIterator
operator|.
name|nextDataRow
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
name|void
name|checkNextId
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
throws|throws
name|CayenneException
block|{
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Null DbEntity, can't create id."
argument_list|)
throw|;
block|}
name|this
operator|.
name|readingIds
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|nextDataRow
operator|=
literal|null
expr_stmt|;
while|while
condition|(
name|wrappedIterator
operator|.
name|hasNextRow
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|next
init|=
name|wrappedIterator
operator|.
name|nextObjectId
argument_list|(
name|entity
argument_list|)
decl_stmt|;
comment|// if we are reading ids, we ignore "compareFullRows" setting
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
block|}
end_class

end_unit

