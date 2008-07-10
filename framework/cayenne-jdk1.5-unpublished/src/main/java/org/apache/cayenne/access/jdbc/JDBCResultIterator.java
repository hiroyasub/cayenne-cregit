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
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
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
name|CayenneRuntimeException
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
name|access
operator|.
name|types
operator|.
name|ExtendedType
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
name|Util
import|;
end_import

begin_comment
comment|/**  * A ResultIterator over the underlying JDBC ResultSet.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_comment
comment|// Replaces DefaultResultIterator
end_comment

begin_class
specifier|public
class|class
name|JDBCResultIterator
implements|implements
name|ResultIterator
block|{
comment|// Connection information
specifier|protected
name|Connection
name|connection
decl_stmt|;
specifier|protected
name|Statement
name|statement
decl_stmt|;
specifier|protected
name|ResultSet
name|resultSet
decl_stmt|;
specifier|protected
name|RowDescriptor
name|rowDescriptor
decl_stmt|;
name|DataRowPostProcessor
name|postProcessor
decl_stmt|;
comment|// last indexed PK
specifier|protected
name|DbEntity
name|rootEntity
decl_stmt|;
specifier|protected
name|int
index|[]
name|pkIndices
decl_stmt|;
specifier|protected
name|int
name|mapCapacity
decl_stmt|;
specifier|protected
name|boolean
name|closingConnection
decl_stmt|;
specifier|protected
name|boolean
name|closed
decl_stmt|;
specifier|protected
name|boolean
name|nextRow
decl_stmt|;
specifier|protected
name|int
name|fetchedSoFar
decl_stmt|;
specifier|protected
name|int
name|fetchLimit
decl_stmt|;
specifier|protected
name|int
name|fetchOffset
decl_stmt|;
specifier|private
name|String
index|[]
name|labels
decl_stmt|;
specifier|private
name|int
index|[]
name|types
decl_stmt|;
comment|/**      * Creates new JDBCResultIterator that reads from provided ResultSet.      *       * @since 3.0      */
specifier|public
name|JDBCResultIterator
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|Statement
name|statement
parameter_list|,
name|ResultSet
name|resultSet
parameter_list|,
name|RowDescriptor
name|descriptor
parameter_list|,
name|int
name|fetchLimit
parameter_list|,
name|int
name|fetchOffset
parameter_list|)
throws|throws
name|CayenneException
block|{
name|this
operator|.
name|fetchOffset
operator|=
name|fetchOffset
expr_stmt|;
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
name|this
operator|.
name|statement
operator|=
name|statement
expr_stmt|;
name|this
operator|.
name|resultSet
operator|=
name|resultSet
expr_stmt|;
name|this
operator|.
name|rowDescriptor
operator|=
name|descriptor
expr_stmt|;
name|this
operator|.
name|fetchLimit
operator|=
name|fetchLimit
expr_stmt|;
name|this
operator|.
name|mapCapacity
operator|=
operator|(
name|int
operator|)
name|Math
operator|.
name|ceil
argument_list|(
operator|(
name|descriptor
operator|.
name|getWidth
argument_list|()
operator|)
operator|/
literal|0.75
argument_list|)
expr_stmt|;
try|try
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|i
operator|++
operator|<
name|fetchOffset
operator|&&
name|resultSet
operator|.
name|next
argument_list|()
condition|)
empty_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Error rewinding ResultSet"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|checkNextRow
argument_list|()
expr_stmt|;
if|if
condition|(
name|nextRow
condition|)
block|{
comment|// extract column parameters to speed up processing...
name|ColumnDescriptor
index|[]
name|columns
init|=
name|descriptor
operator|.
name|getColumns
argument_list|()
decl_stmt|;
name|int
name|width
init|=
name|columns
operator|.
name|length
decl_stmt|;
name|labels
operator|=
operator|new
name|String
index|[
name|width
index|]
expr_stmt|;
name|types
operator|=
operator|new
name|int
index|[
name|width
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|width
condition|;
name|i
operator|++
control|)
block|{
name|labels
index|[
name|i
index|]
operator|=
name|columns
index|[
name|i
index|]
operator|.
name|getLabel
argument_list|()
expr_stmt|;
name|types
index|[
name|i
index|]
operator|=
name|columns
index|[
name|i
index|]
operator|.
name|getJdbcType
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Creates new JDBCResultIterator that reads from provided ResultSet.      */
specifier|public
name|JDBCResultIterator
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|Statement
name|statement
parameter_list|,
name|ResultSet
name|resultSet
parameter_list|,
name|RowDescriptor
name|descriptor
parameter_list|,
name|int
name|fetchLimit
parameter_list|)
throws|throws
name|CayenneException
block|{
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
name|this
operator|.
name|statement
operator|=
name|statement
expr_stmt|;
name|this
operator|.
name|resultSet
operator|=
name|resultSet
expr_stmt|;
name|this
operator|.
name|rowDescriptor
operator|=
name|descriptor
expr_stmt|;
name|this
operator|.
name|fetchLimit
operator|=
name|fetchLimit
expr_stmt|;
name|this
operator|.
name|mapCapacity
operator|=
operator|(
name|int
operator|)
name|Math
operator|.
name|ceil
argument_list|(
operator|(
name|descriptor
operator|.
name|getWidth
argument_list|()
operator|)
operator|/
literal|0.75
argument_list|)
expr_stmt|;
name|checkNextRow
argument_list|()
expr_stmt|;
if|if
condition|(
name|nextRow
condition|)
block|{
comment|// extract column parameters to speed up processing...
name|ColumnDescriptor
index|[]
name|columns
init|=
name|descriptor
operator|.
name|getColumns
argument_list|()
decl_stmt|;
name|int
name|width
init|=
name|columns
operator|.
name|length
decl_stmt|;
name|labels
operator|=
operator|new
name|String
index|[
name|width
index|]
expr_stmt|;
name|types
operator|=
operator|new
name|int
index|[
name|width
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|width
condition|;
name|i
operator|++
control|)
block|{
name|labels
index|[
name|i
index|]
operator|=
name|columns
index|[
name|i
index|]
operator|.
name|getLabel
argument_list|()
expr_stmt|;
name|types
index|[
name|i
index|]
operator|=
name|columns
index|[
name|i
index|]
operator|.
name|getJdbcType
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Returns all unread data rows from ResultSet, closing this iterator if needed.      */
specifier|public
name|List
argument_list|<
name|DataRow
argument_list|>
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
name|DataRow
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|DataRow
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
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
operator|(
name|DataRow
operator|)
name|nextDataRow
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
return|return
name|list
return|;
block|}
comment|/**      * Returns true if there is at least one more record that can be read from the      * iterator.      */
specifier|public
name|boolean
name|hasNextRow
parameter_list|()
block|{
return|return
name|nextRow
return|;
block|}
comment|/**      * Returns the next result row as a Map.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
comment|// read
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
name|readDataRow
argument_list|()
decl_stmt|;
comment|// rewind
name|checkNextRow
argument_list|()
expr_stmt|;
return|return
name|row
return|;
block|}
comment|/**      * Returns a map of ObjectId values from the next result row. Primary key columns are      * determined from the provided DbEntity.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
comment|// index id
if|if
condition|(
name|rootEntity
operator|!=
name|entity
operator|||
name|pkIndices
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|rootEntity
operator|=
name|entity
expr_stmt|;
name|indexPK
argument_list|()
expr_stmt|;
block|}
comment|// read ...
comment|// TODO: note a mismatch with 1.1 API - ID positions are preset and are
comment|// not affected by the entity specified (think of deprecating/replacing this)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
name|readIdRow
argument_list|()
decl_stmt|;
comment|// rewind
name|checkNextRow
argument_list|()
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
comment|// index id
if|if
condition|(
name|rootEntity
operator|!=
name|entity
operator|||
name|pkIndices
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|rootEntity
operator|=
name|entity
expr_stmt|;
name|indexPK
argument_list|()
expr_stmt|;
block|}
name|Object
name|id
init|=
name|readId
argument_list|()
decl_stmt|;
comment|// rewind
name|checkNextRow
argument_list|()
expr_stmt|;
return|return
name|id
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
name|checkNextRow
argument_list|()
expr_stmt|;
block|}
comment|/**      * Closes ResultIterator and associated ResultSet. This method must be called      * explicitly when the user is finished processing the records. Otherwise unused      * database resources will not be released properly.      */
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|CayenneException
block|{
if|if
condition|(
operator|!
name|closed
condition|)
block|{
name|nextRow
operator|=
literal|false
expr_stmt|;
name|StringBuffer
name|errors
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
try|try
block|{
name|resultSet
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e1
parameter_list|)
block|{
name|errors
operator|.
name|append
argument_list|(
literal|"Error closing ResultSet."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|statement
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|statement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e2
parameter_list|)
block|{
name|errors
operator|.
name|append
argument_list|(
literal|"Error closing PreparedStatement."
argument_list|)
expr_stmt|;
block|}
block|}
comment|// TODO: andrus, 5/8/2006 - closing connection within JDBCResultIterator is
comment|// obsolete as this is bound to transaction closing in DataContext. Deprecate
comment|// this after 1.2
comment|// close connection, if this object was explicitly configured to be
comment|// responsible for doing it
if|if
condition|(
name|connection
operator|!=
literal|null
operator|&&
name|isClosingConnection
argument_list|()
condition|)
block|{
try|try
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e3
parameter_list|)
block|{
name|errors
operator|.
name|append
argument_list|(
literal|"Error closing Connection."
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|errors
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Error closing ResultIterator: "
operator|+
name|errors
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|closed
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/**      * Returns the number of columns in the result row.      */
specifier|public
name|int
name|getDataRowWidth
parameter_list|()
block|{
return|return
name|rowDescriptor
operator|.
name|getWidth
argument_list|()
return|;
block|}
comment|/**      * Moves internal ResultSet cursor position down one row. Checks if the next row is      * available.      */
specifier|protected
name|void
name|checkNextRow
parameter_list|()
throws|throws
name|CayenneException
block|{
name|nextRow
operator|=
literal|false
expr_stmt|;
try|try
block|{
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
name|resultSet
operator|.
name|next
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
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Error rewinding ResultSet"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Reads a row from the internal ResultSet at the current cursor position.      */
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|readDataRow
parameter_list|()
throws|throws
name|CayenneException
block|{
try|try
block|{
name|DataRow
name|dataRow
init|=
operator|new
name|DataRow
argument_list|(
name|mapCapacity
argument_list|)
decl_stmt|;
name|ExtendedType
index|[]
name|converters
init|=
name|rowDescriptor
operator|.
name|getConverters
argument_list|()
decl_stmt|;
name|int
name|resultWidth
init|=
name|labels
operator|.
name|length
decl_stmt|;
comment|// process result row columns,
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|resultWidth
condition|;
name|i
operator|++
control|)
block|{
comment|// note: jdbc column indexes start from 1, not 0 unlike everywhere else
name|Object
name|val
init|=
name|converters
index|[
name|i
index|]
operator|.
name|materializeObject
argument_list|(
name|resultSet
argument_list|,
name|i
operator|+
literal|1
argument_list|,
name|types
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|dataRow
operator|.
name|put
argument_list|(
name|labels
index|[
name|i
index|]
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|postProcessor
operator|!=
literal|null
condition|)
block|{
name|postProcessor
operator|.
name|postprocessRow
argument_list|(
name|resultSet
argument_list|,
name|dataRow
argument_list|)
expr_stmt|;
block|}
return|return
name|dataRow
return|;
block|}
catch|catch
parameter_list|(
name|CayenneException
name|cex
parameter_list|)
block|{
comment|// rethrow unmodified
throw|throw
name|cex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|otherex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Exception materializing column."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|otherex
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|Object
name|readId
parameter_list|()
throws|throws
name|CayenneException
block|{
name|int
name|len
init|=
name|pkIndices
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|len
operator|!=
literal|1
condition|)
block|{
return|return
name|readIdRow
argument_list|()
return|;
block|}
name|ExtendedType
index|[]
name|converters
init|=
name|rowDescriptor
operator|.
name|getConverters
argument_list|()
decl_stmt|;
try|try
block|{
comment|// dereference column index
name|int
name|index
init|=
name|pkIndices
index|[
literal|0
index|]
decl_stmt|;
comment|// note: jdbc column indexes start from 1, not 0 as in arrays
name|Object
name|val
init|=
name|converters
index|[
name|index
index|]
operator|.
name|materializeObject
argument_list|(
name|resultSet
argument_list|,
name|index
operator|+
literal|1
argument_list|,
name|types
index|[
name|index
index|]
argument_list|)
decl_stmt|;
comment|// note that postProcessor overrides are not applied. ID mapping must be the
comment|// same across inheritance hierarchy, so overrides do not make sense.
return|return
name|val
return|;
block|}
catch|catch
parameter_list|(
name|CayenneException
name|cex
parameter_list|)
block|{
comment|// rethrow unmodified
throw|throw
name|cex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|otherex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Exception materializing id column."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|otherex
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**      * Reads a row from the internal ResultSet at the current cursor position, processing      * only columns that are part of the ObjectId of a target class.      */
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|readIdRow
parameter_list|()
throws|throws
name|CayenneException
block|{
try|try
block|{
name|DataRow
name|idRow
init|=
operator|new
name|DataRow
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|ExtendedType
index|[]
name|converters
init|=
name|rowDescriptor
operator|.
name|getConverters
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|pkIndices
operator|.
name|length
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
comment|// dereference column index
name|int
name|index
init|=
name|pkIndices
index|[
name|i
index|]
decl_stmt|;
comment|// note: jdbc column indexes start from 1, not 0 as in arrays
name|Object
name|val
init|=
name|converters
index|[
name|index
index|]
operator|.
name|materializeObject
argument_list|(
name|resultSet
argument_list|,
name|index
operator|+
literal|1
argument_list|,
name|types
index|[
name|index
index|]
argument_list|)
decl_stmt|;
name|idRow
operator|.
name|put
argument_list|(
name|labels
index|[
name|index
index|]
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|postProcessor
operator|!=
literal|null
condition|)
block|{
name|postProcessor
operator|.
name|postprocessRow
argument_list|(
name|resultSet
argument_list|,
name|idRow
argument_list|)
expr_stmt|;
block|}
return|return
name|idRow
return|;
block|}
catch|catch
parameter_list|(
name|CayenneException
name|cex
parameter_list|)
block|{
comment|// rethrow unmodified
throw|throw
name|cex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|otherex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Exception materializing id column."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|otherex
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**      * Creates an index of PK columns in the RowDescriptor.      */
specifier|protected
name|void
name|indexPK
parameter_list|()
block|{
if|if
condition|(
name|rootEntity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null root DbEntity, can't index PK"
argument_list|)
throw|;
block|}
name|int
name|len
init|=
name|rootEntity
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|len
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Root DbEntity has no PK defined: "
operator|+
name|rootEntity
argument_list|)
throw|;
block|}
name|int
index|[]
name|pk
init|=
operator|new
name|int
index|[
name|len
index|]
decl_stmt|;
name|ColumnDescriptor
index|[]
name|columns
init|=
name|rowDescriptor
operator|.
name|getColumns
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|j
init|=
literal|0
init|;
name|i
operator|<
name|columns
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|DbAttribute
name|a
init|=
operator|(
name|DbAttribute
operator|)
name|rootEntity
operator|.
name|getAttribute
argument_list|(
name|columns
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|!=
literal|null
operator|&&
name|a
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
name|pk
index|[
name|j
operator|++
index|]
operator|=
name|i
expr_stmt|;
block|}
block|}
name|this
operator|.
name|pkIndices
operator|=
name|pk
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if this iterator is responsible for closing its      * connection, otherwise a user of the iterator must close the connection after      * closing the iterator.      */
specifier|public
name|boolean
name|isClosingConnection
parameter_list|()
block|{
return|return
name|closingConnection
return|;
block|}
comment|/**      * Sets the<code>closingConnection</code> property.      */
specifier|public
name|void
name|setClosingConnection
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|this
operator|.
name|closingConnection
operator|=
name|flag
expr_stmt|;
block|}
specifier|public
name|RowDescriptor
name|getRowDescriptor
parameter_list|()
block|{
return|return
name|rowDescriptor
return|;
block|}
name|void
name|setPostProcessor
parameter_list|(
name|DataRowPostProcessor
name|postProcessor
parameter_list|)
block|{
name|this
operator|.
name|postProcessor
operator|=
name|postProcessor
expr_stmt|;
block|}
block|}
end_class

end_unit

