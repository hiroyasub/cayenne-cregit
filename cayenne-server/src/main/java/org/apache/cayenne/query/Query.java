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
name|query
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|access
operator|.
name|QueryEngine
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
name|DataMap
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
name|map
operator|.
name|QueryDescriptor
import|;
end_import

begin_comment
comment|/**  * Defines minimal API of a query descriptor that is executable via Cayenne.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Query
extends|extends
name|Serializable
block|{
comment|/**      * Returns query runtime parameters. The method is called at various stages of the      * execution by Cayenne access stack to retrieve query parameters. EntityResolver      * instance is passed to this method, meaning that the query doesn't need to store      * direct references to Cayenne mapping objects and can resolve them at runtime.      *       * @since 1.2      */
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**      * A callback method invoked by Cayenne during the routing phase of the query      * execution. Mapping of DataNodes is provided by QueryRouter. Query should use a      * {@link QueryRouter#route(QueryEngine, Query, Query)} callback method to route      * itself. Query can create one or more substitute queries or even provide its own      * QueryEngine to execute itself.      *       * @since 1.2      */
name|void
name|route
parameter_list|(
name|QueryRouter
name|router
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|Query
name|substitutedQuery
parameter_list|)
function_decl|;
comment|/**      * A callback method invoked by Cayenne during the final execution phase of the query      * run. A concrete query implementation is given a chance to decide how it should be      * handled. Implementors can pick an appropriate method of the SQLActionVisitor to      * handle itself, create a custom SQLAction of its own, or substitute itself with      * another query that should be used for SQLAction construction.      *       * @since 1.2      */
name|SQLAction
name|createSQLAction
parameter_list|(
name|SQLActionVisitor
name|visitor
parameter_list|)
function_decl|;
comment|/**      * Returns a symbolic name of the query. The name is normally used as a key to find      * queries stored in the DataMap.      *       * @since 1.1      * @deprecated {@link QueryDescriptor#getName()} should be used instead      */
annotation|@
name|Deprecated
name|String
name|getName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

