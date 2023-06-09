begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
package|;
end_package

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
name|map
operator|.
name|EntityResult
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
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_comment
comment|/**  * A "compiled" version of a {@link EntityResult} descriptor.  *   * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|EntityResultSegment
block|{
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
function_decl|;
comment|/**      * Returns a map of ResultSet labels keyed by column paths. Note that ordering of      * fields in the returned map is generally undefined and should not be relied upon      * when processing query result sets.      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getFields
parameter_list|()
function_decl|;
comment|/**      * Performs a reverse lookup of the column path for a given ResultSet label.      */
name|String
name|getColumnPath
parameter_list|(
name|String
name|resultSetLabel
parameter_list|)
function_decl|;
comment|/**      * Returns a zero-based column index of the first column of this segment in the      * ResultSet.      */
name|int
name|getColumnOffset
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

