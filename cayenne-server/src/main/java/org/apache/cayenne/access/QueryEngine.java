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
name|access
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|EntityResolver
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
name|query
operator|.
name|Query
import|;
end_import

begin_comment
comment|/**  * Defines methods used to run Cayenne queries.  */
end_comment

begin_interface
specifier|public
interface|interface
name|QueryEngine
block|{
comment|/**      * Executes a list of queries wrapping them in its own transaction. Results of      * execution are passed to {@link OperationObserver}object via its callback methods.      *       * @since 1.1 The signature has changed from List to Collection.      */
name|void
name|performQueries
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Query
argument_list|>
name|queries
parameter_list|,
name|OperationObserver
name|resultConsumer
parameter_list|)
function_decl|;
comment|/**      * Returns a resolver for this query engine that is capable of resolving between      * classes, entity names, and obj/db entities      */
name|EntityResolver
name|getEntityResolver
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

