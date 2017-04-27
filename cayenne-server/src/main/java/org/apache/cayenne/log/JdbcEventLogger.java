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
name|log
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
name|translator
operator|.
name|ParameterBinding
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
name|conn
operator|.
name|DataSourceInfo
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
name|DbAttribute
import|;
end_import

begin_comment
comment|/**  * A logging service used by Cayenne to output database interactions.  *   * @since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|JdbcEventLogger
block|{
comment|/** 	 * Logs an arbitrary message. 	 */
name|void
name|log
parameter_list|(
name|String
name|message
parameter_list|)
function_decl|;
comment|/** 	 * Logs database connection event using container data source. 	 *  	 * @deprecated since 4.0 connection events are logged by the DataSources 	 *             using their own logger. 	 */
annotation|@
name|Deprecated
name|void
name|logConnect
parameter_list|(
name|String
name|dataSource
parameter_list|)
function_decl|;
comment|/** 	 * @deprecated since 4.0 connection events are logged by the DataSources 	 *             using their own logger. 	 */
annotation|@
name|Deprecated
name|void
name|logConnect
parameter_list|(
name|String
name|url
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
function_decl|;
comment|/** 	 * @deprecated since 4.0 connection events are logged by the DataSources 	 *             using their own logger. 	 */
annotation|@
name|Deprecated
name|void
name|logPoolCreated
parameter_list|(
name|DataSourceInfo
name|dsi
parameter_list|)
function_decl|;
comment|/** 	 * @deprecated since 4.0 connection events are logged by the DataSources 	 *             using their own logger. 	 */
annotation|@
name|Deprecated
name|void
name|logConnectSuccess
parameter_list|()
function_decl|;
comment|/** 	 * @deprecated since 4.0 connection events are logged by the DataSources 	 *             using their own logger. 	 */
annotation|@
name|Deprecated
name|void
name|logConnectFailure
parameter_list|(
name|Throwable
name|th
parameter_list|)
function_decl|;
name|void
name|logGeneratedKey
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/** 	 * @deprecated since 4.0 use {@link #logQuery(String, ParameterBinding[], long)}. 	 */
annotation|@
name|Deprecated
name|void
name|logQuery
parameter_list|(
name|String
name|sql
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|params
parameter_list|)
function_decl|;
comment|/** 	 * @deprecated since 4.0 use {@link #logQuery(String, ParameterBinding[], long)}. 	 */
annotation|@
name|Deprecated
name|void
name|logQuery
parameter_list|(
name|String
name|sql
parameter_list|,
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|attrs
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|params
parameter_list|,
name|long
name|time
parameter_list|)
function_decl|;
comment|/** 	 * @since 4.0 	 */
name|void
name|logQuery
parameter_list|(
name|String
name|sql
parameter_list|,
name|ParameterBinding
index|[]
name|bindings
parameter_list|,
name|long
name|translatedIn
parameter_list|)
function_decl|;
comment|/** 	 * @since 4.0 	 */
name|void
name|logQueryParameters
parameter_list|(
name|String
name|label
parameter_list|,
name|ParameterBinding
index|[]
name|bindings
parameter_list|)
function_decl|;
comment|/** 	 * @deprecated since 4.0 use {@link #logQueryParameters(String, ParameterBinding[])} 	 */
annotation|@
name|Deprecated
name|void
name|logQueryParameters
parameter_list|(
name|String
name|label
parameter_list|,
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|attrs
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|parameters
parameter_list|,
name|boolean
name|isInserting
parameter_list|)
function_decl|;
name|void
name|logSelectCount
parameter_list|(
name|int
name|count
parameter_list|,
name|long
name|time
parameter_list|)
function_decl|;
comment|/** 	 * @param count of selected rows 	 * @param time (milliseconds) time query took to run 	 * @param sql SQL that was executed, printed when time exceeds timeThreshold 	 *  	 * @since 4.0 	 */
name|void
name|logSelectCount
parameter_list|(
name|int
name|count
parameter_list|,
name|long
name|time
parameter_list|,
name|String
name|sql
parameter_list|)
function_decl|;
name|void
name|logUpdateCount
parameter_list|(
name|int
name|count
parameter_list|)
function_decl|;
name|void
name|logBeginTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
function_decl|;
name|void
name|logCommitTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
function_decl|;
name|void
name|logRollbackTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
function_decl|;
name|void
name|logQueryError
parameter_list|(
name|Throwable
name|th
parameter_list|)
function_decl|;
comment|/** 	 * Returns true if current thread default log level is high enough to 	 * generate output. 	 */
name|boolean
name|isLoggable
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

