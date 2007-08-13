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
name|dba
operator|.
name|sqlserver
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
name|jdbc
operator|.
name|BatchAction
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
name|dba
operator|.
name|JdbcActionBuilder
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
name|dba
operator|.
name|JdbcAdapter
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
name|BatchQuery
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
name|ProcedureQuery
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
name|SQLAction
import|;
end_import

begin_comment
comment|/**  * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|SQLServerActionBuilder
extends|extends
name|JdbcActionBuilder
block|{
specifier|public
name|SQLServerActionBuilder
parameter_list|(
name|JdbcAdapter
name|adapter
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|super
argument_list|(
name|adapter
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SQLAction
name|batchAction
parameter_list|(
name|BatchQuery
name|query
parameter_list|)
block|{
comment|// check run strategy...
comment|// optimistic locking is not supported in batches due to JDBC driver limitations
name|boolean
name|useOptimisticLock
init|=
name|query
operator|.
name|isUsingOptimisticLocking
argument_list|()
decl_stmt|;
name|boolean
name|runningAsBatch
init|=
operator|!
name|useOptimisticLock
operator|&&
name|adapter
operator|.
name|supportsBatchUpdates
argument_list|()
decl_stmt|;
name|BatchAction
name|action
init|=
operator|new
name|SQLServerBatchAction
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|,
name|entityResolver
argument_list|)
decl_stmt|;
name|action
operator|.
name|setBatch
argument_list|(
name|runningAsBatch
argument_list|)
expr_stmt|;
return|return
name|action
return|;
block|}
specifier|public
name|SQLAction
name|procedureAction
parameter_list|(
name|ProcedureQuery
name|query
parameter_list|)
block|{
return|return
operator|new
name|SQLServerProcedureAction
argument_list|(
name|query
argument_list|,
name|getAdapter
argument_list|()
argument_list|,
name|getEntityResolver
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

