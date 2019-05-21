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
comment|/**  * Represents a result of query execution. It potentially contain a mix of update counts  * and lists of selected values. Provides API somewhat similar to java.util.Iterator or  * java.sql.ResultSet for scanning through the individual results.  *<p>  * An example of iterating through a response:  *</p>  *   *<pre>  * QueryResponse response = context.performGenericQuery(query);  * for (response.reset(); response.next();) {  *     if (response.isList()) {  *         List list = response.currentList();  *         // ...  *     }  *     else {  *         int[] updateCounts = response.currentUpdateCount();  *         // ...  *     }  * }  *</pre>  *   *<p>  * In case the structure of the result is known, and only a single list or an update count  * is expected, there is a simpler API to access them:  *</p>  *   *<pre>  * QueryResponse response = context.performGenericQuery(query);  * List list = response.firstList();  * int[] count = response.firstUpdateCount();  *</pre>  *   * @since 1.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|QueryResponse
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
comment|/**      * Returns a List under the current iterator position. Use {@link #isList()} to check      * the result type before calling this method.      */
name|List
argument_list|<
name|?
argument_list|>
name|currentList
parameter_list|()
function_decl|;
comment|/**      * Returns an update count under the current iterator position. Returned value is an      * int[] to accommodate batch queries. For a regular update result, the value will be      * an int[1]. Use {@link #isList()} to check the result type before calling this      * method.      */
name|int
index|[]
name|currentUpdateCount
parameter_list|()
function_decl|;
comment|/**      * Rewinds response iterator to the next result, returning true if it is available.      */
name|boolean
name|next
parameter_list|()
function_decl|;
comment|/**      * Restarts response iterator.      */
name|void
name|reset
parameter_list|()
function_decl|;
comment|/**      * A utility method for quickly retrieving the first list in the response. Returns      * null if the query has no lists. Note that this method resets current iterator to an      * undefined state.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|List
name|firstList
parameter_list|()
function_decl|;
comment|/**      * A utility method for quickly retrieving the first update count from the response.      * Note that this method resets current iterator to an undefined state.      */
name|int
index|[]
name|firstUpdateCount
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

