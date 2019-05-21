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
comment|/**  * Represents a collection of items which are results of a multipart query execution.  *  * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|QueryResult
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Iterable
argument_list|<
name|QueryResultItem
argument_list|>
block|{
comment|/**      * Returns a number of results in the response.      */
name|int
name|size
parameter_list|()
function_decl|;
comment|/**      * Returns whether current iteration result is a list or an update count.      */
name|boolean
name|isList
parameter_list|()
function_decl|;
comment|/**      * A utility method for quickly retrieving the first list in the response. Returns      * null if the query has no lists.      */
name|List
argument_list|<
name|T
argument_list|>
name|firstList
parameter_list|()
function_decl|;
comment|/**      * A utility method for quickly retrieving the first batch update count array from the response.      */
name|int
index|[]
name|firstBatchUpdateCount
parameter_list|()
function_decl|;
comment|/**      * A utility method for quick retrieval of the first update count from the response.      */
name|int
name|firstUpdateCount
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

