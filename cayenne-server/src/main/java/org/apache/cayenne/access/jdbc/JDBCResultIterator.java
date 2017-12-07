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
name|jdbc
operator|.
name|reader
operator|.
name|RowReader
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
name|NoSuchElementException
import|;
end_import

begin_comment
comment|/**  * A ResultIterator over the underlying JDBC ResultSet.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|JDBCResultIterator
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
name|Statement
name|statement
decl_stmt|;
specifier|protected
name|ResultSet
name|resultSet
decl_stmt|;
specifier|protected
name|boolean
name|closed
decl_stmt|;
specifier|protected
name|boolean
name|nextRow
decl_stmt|;
specifier|private
name|RowReader
argument_list|<
name|T
argument_list|>
name|rowReader
decl_stmt|;
comment|/**      * Creates new JDBCResultIterator that reads from provided ResultSet.      *       * @since 4.0      */
specifier|public
name|JDBCResultIterator
parameter_list|(
name|Statement
name|statement
parameter_list|,
name|ResultSet
name|resultSet
parameter_list|,
name|RowReader
argument_list|<
name|T
argument_list|>
name|rowReader
parameter_list|)
block|{
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
name|rowReader
operator|=
name|rowReader
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
comment|/**      * Returns true if there is at least one more record that can be read from      * the iterator.      */
annotation|@
name|Override
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
name|T
name|row
init|=
name|rowReader
operator|.
name|readRow
argument_list|(
name|resultSet
argument_list|)
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
comment|/**      * Closes ResultIterator and associated ResultSet. This method must be      * called explicitly when the user is finished processing the records.      * Otherwise unused database resources will not be released properly.      */
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|NoSuchElementException
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
name|StringBuilder
name|errors
init|=
operator|new
name|StringBuilder
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
name|CayenneRuntimeException
argument_list|(
literal|"Error closing ResultIterator: %s"
argument_list|,
name|errors
argument_list|)
throw|;
block|}
name|closed
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/**      * Moves internal ResultSet cursor position down one row. Checks if the next      * row is available.      */
specifier|protected
name|void
name|checkNextRow
parameter_list|()
block|{
name|nextRow
operator|=
literal|false
expr_stmt|;
try|try
block|{
if|if
condition|(
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
name|CayenneRuntimeException
argument_list|(
literal|"Error rewinding ResultSet"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

