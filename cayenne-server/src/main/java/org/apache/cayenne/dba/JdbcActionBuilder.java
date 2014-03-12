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
name|access
operator|.
name|jdbc
operator|.
name|EJBQLAction
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
name|jdbc
operator|.
name|ProcedureAction
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
name|jdbc
operator|.
name|RowReaderFactory
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
name|jdbc
operator|.
name|SQLTemplateAction
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
name|jdbc
operator|.
name|SelectAction
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
name|log
operator|.
name|JdbcEventLogger
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
name|EJBQLQuery
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
name|SQLActionVisitor
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
name|SQLTemplate
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
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * A factory of default SQLActions. Adapters usually subclass JdbcActionBuilder to provide  * custom actions for various query types.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|JdbcActionBuilder
implements|implements
name|SQLActionVisitor
block|{
specifier|protected
name|JdbcAdapter
name|adapter
decl_stmt|;
specifier|protected
name|EntityResolver
name|entityResolver
decl_stmt|;
specifier|protected
name|JdbcEventLogger
name|logger
decl_stmt|;
specifier|protected
name|RowReaderFactory
name|rowReaderFactory
decl_stmt|;
comment|/**      * @since 3.2      */
specifier|public
name|JdbcActionBuilder
parameter_list|(
name|JdbcAdapter
name|adapter
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|RowReaderFactory
name|rowReaderFactory
parameter_list|)
block|{
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
name|this
operator|.
name|entityResolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|rowReaderFactory
operator|=
name|rowReaderFactory
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|adapter
operator|.
name|getJdbcEventLogger
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
name|BatchAction
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|,
name|entityResolver
argument_list|,
name|rowReaderFactory
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
name|ProcedureAction
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|,
name|entityResolver
argument_list|,
name|rowReaderFactory
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|SQLAction
name|objectSelectAction
parameter_list|(
name|SelectQuery
argument_list|<
name|T
argument_list|>
name|query
parameter_list|)
block|{
return|return
operator|new
name|SelectAction
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|,
name|entityResolver
argument_list|,
name|rowReaderFactory
argument_list|)
return|;
block|}
specifier|public
name|SQLAction
name|sqlAction
parameter_list|(
name|SQLTemplate
name|query
parameter_list|)
block|{
return|return
operator|new
name|SQLTemplateAction
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|,
name|entityResolver
argument_list|,
name|rowReaderFactory
argument_list|)
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|SQLAction
name|ejbqlAction
parameter_list|(
name|EJBQLQuery
name|query
parameter_list|)
block|{
return|return
operator|new
name|EJBQLAction
argument_list|(
name|query
argument_list|,
name|this
argument_list|,
name|adapter
argument_list|,
name|entityResolver
argument_list|,
name|rowReaderFactory
argument_list|)
return|;
block|}
comment|/**      * Returns DbAdapter used associated with this action builder.      */
specifier|public
name|JdbcAdapter
name|getAdapter
parameter_list|()
block|{
return|return
name|adapter
return|;
block|}
comment|/**      * Returns EntityResolver that can be used to gain access to the mapping objects.      */
specifier|public
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
return|return
name|entityResolver
return|;
block|}
block|}
end_class

end_unit

