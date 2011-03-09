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
name|oracle
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
name|CayenneException
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
name|access
operator|.
name|trans
operator|.
name|BatchQueryBuilder
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

begin_comment
comment|/**  * @since 1.2  */
end_comment

begin_class
class|class
name|OracleBatchAction
extends|extends
name|BatchAction
block|{
name|OracleBatchAction
parameter_list|(
name|BatchQuery
name|batchQuery
parameter_list|,
name|JdbcAdapter
name|adapter
parameter_list|,
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
name|super
argument_list|(
name|batchQuery
argument_list|,
name|adapter
argument_list|,
name|entityResolver
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|BatchQueryBuilder
name|createBuilder
parameter_list|()
throws|throws
name|CayenneException
block|{
comment|// intercept super call to configure the builder...
name|BatchQueryBuilder
name|builder
init|=
name|super
operator|.
name|createBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setTrimFunction
argument_list|(
name|OracleAdapter
operator|.
name|TRIM_FUNCTION
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
block|}
end_class

end_unit

