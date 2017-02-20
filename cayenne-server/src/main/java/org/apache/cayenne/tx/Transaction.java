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
name|tx
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Cayenne Transaction interface.  *  * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|Transaction
block|{
comment|/**      * Starts a Transaction. If Transaction is not started explicitly, it will be started when the first connection is      * added.      */
name|void
name|begin
parameter_list|()
function_decl|;
name|void
name|commit
parameter_list|()
function_decl|;
name|void
name|rollback
parameter_list|()
function_decl|;
name|void
name|setRollbackOnly
parameter_list|()
function_decl|;
name|boolean
name|isRollbackOnly
parameter_list|()
function_decl|;
comment|/**      * Retrieves a connection for the given symbolic name. If it does not exists, creates a new connection using      * provided DataSource, and registers it internally.      *      * @param connectionName a symbolic name of the connection. Cayenne DataNodes generate a name in the form of      *                       "DataNode.Connection.nodename".      * @param dataSource     DataSource that provides new connections.      * @return a connection that participates in the current transaction.      */
name|Connection
name|getOrCreateConnection
parameter_list|(
name|String
name|connectionName
parameter_list|,
name|DataSource
name|dataSource
parameter_list|)
throws|throws
name|SQLException
function_decl|;
comment|/**      * Returns all connections associated with the transaction.      *      * @return connections associated with the transaction.      */
name|Map
argument_list|<
name|String
argument_list|,
name|Connection
argument_list|>
name|getConnections
parameter_list|()
function_decl|;
name|void
name|addListener
parameter_list|(
name|TransactionListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Is this transaction managed by external transaction manager      */
name|boolean
name|isExternal
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

