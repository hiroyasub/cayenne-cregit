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
name|select
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneException
import|;
end_import

begin_comment
comment|/**  * Describes a mapping between JDBC ResultSet and object select result. In general terms,  * SelectDescriptor creates a mapping between a tree (object result metadata, including  * support for scalar/entity results as well as inheritance) and a 2D matrix, representing  * metadata of the SELECT clause of a UNION select query.  *   * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|SelectDescriptor
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Returns a {@link RowReader} for the result set..      */
name|RowReader
argument_list|<
name|T
argument_list|>
name|getRowReader
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|)
throws|throws
name|CayenneException
function_decl|;
comment|/**      * Returns a list of result set columns.      */
name|List
argument_list|<
name|?
extends|extends
name|SelectColumn
argument_list|>
name|getColumns
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

