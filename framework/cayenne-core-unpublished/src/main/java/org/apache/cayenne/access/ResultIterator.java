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

begin_comment
comment|/**  * @deprecated since 3.2 moved to {@link org.apache.cayenne.ResultIterator} and  *             significantly reworked.  */
end_comment

begin_interface
annotation|@
name|Deprecated
specifier|public
interface|interface
name|ResultIterator
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Returns all yet unread rows from ResultSet without closing it.      *       * @since 3.0      */
name|List
argument_list|<
name|T
argument_list|>
name|allRows
parameter_list|()
function_decl|;
comment|/**      * Returns true if there is at least one more record that can be read from      * the iterator.      */
name|boolean
name|hasNextRow
parameter_list|()
function_decl|;
comment|/**      * Returns the next result row that is, depending on the query, may be a      * scalar value, a DataRow, or an Object[] array containing a mix of scalars      * and DataRows.      *       * @since 3.0      */
name|T
name|nextRow
parameter_list|()
function_decl|;
comment|/**      * Goes past current row. If the row is not needed, this may save some time      * on data conversion.      *       * @since 3.0      */
name|void
name|skipRow
parameter_list|()
function_decl|;
comment|/**      * Closes ResultIterator and associated ResultSet. This method must be      * called explicitly when the user is finished processing the records.      * Otherwise unused database resources will not be released properly.      */
name|void
name|close
parameter_list|()
function_decl|;
block|}
end_interface

end_unit
