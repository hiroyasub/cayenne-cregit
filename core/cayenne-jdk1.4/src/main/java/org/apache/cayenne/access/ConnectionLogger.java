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
name|access
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
name|conn
operator|.
name|ConnectionEventLoggingDelegate
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

begin_comment
comment|/**  * Adapts {@link org.apache.cayenne.access.QueryLogger} to be used as a  * {@link org.apache.cayenne.conn.ConnectionEventLoggingDelegate} with Cayenne  * connection pools.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ConnectionLogger
implements|implements
name|ConnectionEventLoggingDelegate
block|{
specifier|public
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
block|{
name|QueryLogger
operator|.
name|logConnect
argument_list|(
name|url
argument_list|,
name|userName
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|logConnectFailure
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|QueryLogger
operator|.
name|logConnectFailure
argument_list|(
name|th
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|logConnectSuccess
parameter_list|()
block|{
name|QueryLogger
operator|.
name|logConnectSuccess
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|logPoolCreated
parameter_list|(
name|DataSourceInfo
name|info
parameter_list|)
block|{
name|QueryLogger
operator|.
name|logPoolCreated
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

