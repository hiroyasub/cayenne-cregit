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
package|;
end_package

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
name|map
operator|.
name|DbEntity
import|;
end_import

begin_comment
comment|/**  * Defines API of an iterator over the records returned as a result of SelectQuery  * execution. Usually a ResultIterator is supported by an open java.sql.ResultSet,  * therefore most of the methods would throw checked exceptions. ResultIterators must be  * explicitly closed when the user is done working with them.  *<p>  * Result "rows", depending on the query, may be represented as scalar values, DataRows,  * or Object[] arrays containing a mix of scalars and DataRows.  *</p>  */
end_comment

begin_interface
specifier|public
interface|interface
name|ResultIterator
block|{
comment|/**      * Returns all unread data rows from ResultSet and closes this iterator if asked to do      * so.      *       * @deprecated since 3.0 use {@link #allRows(boolean)}.      */
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
function_decl|;
comment|/**      * Returns all yet unread rows from ResultSet and closes this iterator if asked to do      * so.      *       * @since 3.0      */
name|List
name|allRows
parameter_list|(
name|boolean
name|close
parameter_list|)
throws|throws
name|CayenneException
function_decl|;
comment|/**      * Returns true if there is at least one more record that can be read from the      * iterator.      */
name|boolean
name|hasNextRow
parameter_list|()
throws|throws
name|CayenneException
function_decl|;
comment|/**      * Returns the next result row as a Map.      *       * @deprecated since 3.0 use {@link #nextRow()}.      */
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
function_decl|;
comment|/**      * Returns the next result row that is, depending on the query, may be a scalar value,      * a DataRow, or an Object[] array containing a mix of scalars and DataRows.      *       * @since 3.0      */
name|Object
name|nextRow
parameter_list|()
throws|throws
name|CayenneException
function_decl|;
comment|/**      * Returns a map of ObjectId values from the next result row. Primary key columns are      * determined from the provided DbEntity.      *       * @since 1.1      * @deprecated since 3.0 in favor of {@link #nextId(DbEntity)}.      */
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
function_decl|;
comment|/**      * Reads and returns an id column or columns for the current row DbEntity. If an      * entity has a single column id, the return value is an Object matching the column      * type (e.g. java.lang.Long). If an entity has a compound PK, the return value is a      * DataRow.      *       * @since 3.0      */
name|Object
name|nextId
parameter_list|()
throws|throws
name|CayenneException
function_decl|;
comment|/**      * Skips current data row instead of reading it.      *       * @deprecated since 3.0 use {@link #skipRow()} instead.      */
name|void
name|skipDataRow
parameter_list|()
throws|throws
name|CayenneException
function_decl|;
comment|/**      * @since 3.0      */
name|void
name|skipRow
parameter_list|()
throws|throws
name|CayenneException
function_decl|;
comment|/**      * Closes ResultIterator and associated ResultSet. This method must be called      * explicitly when the user is finished processing the records. Otherwise unused      * database resources will not be released properly.      */
name|void
name|close
parameter_list|()
throws|throws
name|CayenneException
function_decl|;
comment|/**      * Returns the number of columns in the result row.      *       * @since 1.0.6      * @deprecated since 3.0 in favor of {@link #getResultSetWidth()}.      */
name|int
name|getDataRowWidth
parameter_list|()
function_decl|;
comment|/**      * Returns a number of columns in the underlying ResultSet.      *       * @since 3.0      */
name|int
name|getResultSetWidth
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

