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
name|dba
operator|.
name|sqlserver
package|;
end_package

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
name|sql
operator|.
name|Statement
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
name|DataNode
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
name|OperationObserver
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
name|map
operator|.
name|DbAttribute
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
name|InsertBatchQuery
import|;
end_import

begin_comment
comment|/**  * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|SQLServerBatchAction
extends|extends
name|BatchAction
block|{
specifier|public
name|SQLServerBatchAction
parameter_list|(
name|BatchQuery
name|batchQuery
parameter_list|,
name|DataNode
name|dataNode
parameter_list|,
name|boolean
name|runningAsBatch
parameter_list|)
block|{
name|super
argument_list|(
name|batchQuery
argument_list|,
name|dataNode
argument_list|,
name|runningAsBatch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|performAction
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|OperationObserver
name|observer
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
comment|// this condition checks if identity columns are present in the query
comment|// and adapter
comment|// is not ready to process them... e.g. if we are using a MS driver...
name|boolean
name|identityOverride
init|=
name|expectsToOverrideIdentityColumns
argument_list|()
decl_stmt|;
if|if
condition|(
name|identityOverride
condition|)
block|{
name|setIdentityInsert
argument_list|(
name|connection
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|super
operator|.
name|performAction
argument_list|(
name|connection
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// important: turn off IDENTITY_INSERT as SQL Server won't be able
comment|// to process
comment|// other identity columns in the same transaction
comment|// TODO: if an error happens here this would mask the parent error
if|if
condition|(
name|identityOverride
condition|)
block|{
name|setIdentityInsert
argument_list|(
name|connection
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|setIdentityInsert
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|boolean
name|on
parameter_list|)
throws|throws
name|SQLException
block|{
name|String
name|flag
init|=
name|on
condition|?
literal|" ON"
else|:
literal|" OFF"
decl_stmt|;
name|String
name|configSQL
init|=
literal|"SET IDENTITY_INSERT "
operator|+
name|query
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
name|flag
decl_stmt|;
name|dataNode
operator|.
name|getJdbcEventLogger
argument_list|()
operator|.
name|log
argument_list|(
name|configSQL
argument_list|)
expr_stmt|;
try|try
init|(
name|Statement
name|statement
init|=
name|connection
operator|.
name|createStatement
argument_list|()
init|)
block|{
name|statement
operator|.
name|execute
argument_list|(
name|configSQL
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Returns whether a table has identity columns. 	 */
specifier|protected
name|boolean
name|expectsToOverrideIdentityColumns
parameter_list|()
block|{
comment|// jTDS driver supports identity columns, no need for tricks...
if|if
condition|(
name|dataNode
operator|.
name|getAdapter
argument_list|()
operator|.
name|supportsGeneratedKeys
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|query
operator|instanceof
name|InsertBatchQuery
operator|)
operator|||
name|query
operator|.
name|getDbEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// find identity attributes
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|query
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|attribute
operator|.
name|isGenerated
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

