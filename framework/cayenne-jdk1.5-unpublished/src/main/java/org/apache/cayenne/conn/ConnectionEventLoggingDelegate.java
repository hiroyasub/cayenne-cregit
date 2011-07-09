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
name|conn
package|;
end_package

begin_comment
comment|/**  * A callback API to used by {@link org.apache.cayenne.conn.DriverDataSource} and  * {@link org.apache.cayenne.conn.PoolManager} to notify of connection events. Used  * mainly for logging.  *   * @deprecated since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|ConnectionEventLoggingDelegate
block|{
name|void
name|logPoolCreated
parameter_list|(
name|DataSourceInfo
name|info
parameter_list|)
function_decl|;
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
name|void
name|logConnectSuccess
parameter_list|()
function_decl|;
name|void
name|logConnectFailure
parameter_list|(
name|Throwable
name|th
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

