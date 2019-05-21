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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ObjectContext
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
name|ResultBatchIterator
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
name|ResultIteratorCallback
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

begin_comment
comment|/**  * A common interface for grouping together different kinds of queries that  * return results.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Select
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Query
block|{
comment|/** 	 * Selects objects using provided context. 	 *<p> 	 * Essentially the inversion of "ObjectContext.select(Select)". 	 * 	 * @since 4.0 	 */
name|List
argument_list|<
name|T
argument_list|>
name|select
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
function_decl|;
comment|/** 	 * Selects a single object using provided context. The query is expected to 	 * match zero or one object. It returns null if no objects were matched. If 	 * query matched more than one object, 	 * {@link org.apache.cayenne.CayenneRuntimeException} is thrown. 	 *<p> 	 * Essentially the inversion of "ObjectContext.selectOne(Select)". 	 * 	 * @since 4.0 	 */
name|T
name|selectOne
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
function_decl|;
comment|/** 	 * Selects a single object using provided context. The query itself can 	 * match any number of objects, but will return only the first one. It 	 * returns null if no objects were matched. 	 *<p> 	 * If it matched more than one object, the first object from the list is 	 * returned. This makes 'selectFirst' different from 	 * {@link #selectOne(ObjectContext)}, which would throw in this situation. 	 * 'selectFirst' is useful e.g. when the query is ordered and we only want 	 * to see the first object (e.g. "most recent news article"), etc. 	 *<p> 	 * Selecting the first object via "Select.selectFirst(ObjectContext)" is 	 * more comprehensible than selecting via 	 * "ObjectContext.selectFirst(Select)", because implementations of "Select" 	 * set fetch size limit to one. 	 * 	 * @since 4.0 	 */
name|T
name|selectFirst
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
function_decl|;
comment|/** 	 * Creates a ResultIterator based on the provided context and passes it to a 	 * callback for processing. The caller does not need to worry about closing 	 * the iterator. This method takes care of it. 	 *<p> 	 * Essentially the inversion of 	 * "ObjectContext.iterate(Select, ResultIteratorCallback)". 	 * 	 * @since 4.0 	 */
name|void
name|iterate
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|ResultIteratorCallback
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
function_decl|;
comment|/** 	 * Creates a ResultIterator based on the provided context. It is usually 	 * backed by an open result set and is useful for processing of large data 	 * sets, preserving a constant memory footprint. The caller must wrap 	 * iteration in try/finally (or try-with-resources for Java 1.7 and higher) 	 * and close the ResultIterator explicitly. Or use 	 * {@link #iterate(ObjectContext, ResultIteratorCallback)} as an 	 * alternative. 	 *<p> 	 * Essentially the inversion of "ObjectContext.iterator(Select)". 	 * 	 * @since 4.0 	 */
name|ResultIterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
function_decl|;
comment|/** 	 * Creates a ResultBatchIterator based on the provided context and batch 	 * size. It is usually backed by an open result set and is useful for 	 * processing of large data sets, preserving a constant memory footprint. 	 * The caller must wrap iteration in try/finally (or try-with-resources for 	 * Java 1.7 and higher) and close the ResultBatchIterator explicitly. 	 * 	 * @since 4.0 	 */
name|ResultBatchIterator
argument_list|<
name|T
argument_list|>
name|batchIterator
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|int
name|size
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

