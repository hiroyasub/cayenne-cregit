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
name|ResultSet
import|;
end_import

begin_comment
comment|/**  * A strategy class that encapsulates an algorithm for converting a single ResultSet row  * into a DataRow.  *   * @since 3.0  */
end_comment

begin_interface
interface|interface
name|RowReader
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Extracts a DataRow from the ResultSet at its current position.      */
name|T
name|readRow
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|)
function_decl|;
comment|// TODO: andrus 11/27/2008 refactor the postprocessor hack into a special row reader.
name|void
name|setPostProcessor
parameter_list|(
name|DataRowPostProcessor
name|postProcessor
parameter_list|)
function_decl|;
block|}
end_interface

end_unit
