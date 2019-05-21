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

begin_comment
comment|/**  * An interface used by Queries to route themselves to an appropriate QueryEngine. As of  * 1.2 QueryRouter only supports routing by DataMap.  *   * @since 1.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|QueryRouter
block|{
comment|/**      * A callback method that allows a query to set its preferred engine during the      * routing phase. It allows query to further customize its routing, e.g. it is      * possible to implement query chains that pass multiple queries for execution.      *       * @param engine engine to use for query execution      * @param query A query to execute.      * @param substitutedQuery a query that was substituted for "query". Results must be      *            mapped back to substituted query.      */
name|void
name|route
parameter_list|(
name|QueryEngine
name|engine
parameter_list|,
name|Query
name|query
parameter_list|,
name|Query
name|substitutedQuery
parameter_list|)
function_decl|;
comment|/**      * Returns a QueryEngine for a given name. If the name is null, a default      * QueryEngine is returned. If there's no default engine, an exception is      * thrown.      *       * @since 4.0      */
name|QueryEngine
name|engineForName
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns a QueryEngine that is configured to handle a given DataMap.      *       * @throws org.apache.cayenne.CayenneRuntimeException if an engine can't be found.      * @throws NullPointerException if a map parameter is null.      */
name|QueryEngine
name|engineForDataMap
parameter_list|(
name|DataMap
name|map
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

