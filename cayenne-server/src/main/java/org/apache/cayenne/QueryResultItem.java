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
comment|/**  * Represents a single item in a multipart query execution. Can be either an  * update count or a list of objects.  *  * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|QueryResultItem
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Returns true if encapsulated result is a select result.      */
name|boolean
name|isSelectResult
parameter_list|()
function_decl|;
comment|/**      * Returns true if encapsulated result is a batch update result.      */
name|boolean
name|isBatchUpdate
parameter_list|()
function_decl|;
comment|/**      * Returns a list of selected objects. Throws unless      * {@link #isSelectResult()} returns true.      */
name|List
argument_list|<
name|T
argument_list|>
name|getSelectResult
parameter_list|()
function_decl|;
comment|/**      * Returns an update count.      */
name|int
name|getUpdateCount
parameter_list|()
function_decl|;
comment|/**      * Returns batch update result in a form of array of individual update counts.      */
name|int
index|[]
name|getBatchUpdateCounts
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

