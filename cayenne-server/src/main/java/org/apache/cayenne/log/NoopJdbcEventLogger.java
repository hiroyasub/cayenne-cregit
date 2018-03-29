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
name|map
operator|.
name|DbAttribute
import|;
end_import

begin_comment
comment|/**  * Noop implementation of JdbcEventLogger  *  * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|NoopJdbcEventLogger
implements|implements
name|JdbcEventLogger
block|{
specifier|private
specifier|static
specifier|final
name|NoopJdbcEventLogger
name|instance
init|=
operator|new
name|NoopJdbcEventLogger
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|NoopJdbcEventLogger
name|getInstance
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
specifier|private
name|NoopJdbcEventLogger
parameter_list|()
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|log
parameter_list|(
name|String
name|message
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|logGeneratedKey
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|logQuery
parameter_list|(
name|String
name|sql
parameter_list|,
name|ParameterBinding
index|[]
name|bindings
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
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
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|logSelectCount
parameter_list|(
name|int
name|count
parameter_list|,
name|long
name|time
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
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
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|logUpdateCount
parameter_list|(
name|int
name|count
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|logBeginTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|logCommitTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|logRollbackTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|logQueryError
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isLoggable
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

