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
name|postgres
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
name|DbAdapter
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
name|PreparedStatement
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_class
specifier|public
class|class
name|PostgresBatchAction
extends|extends
name|BatchAction
block|{
comment|/**      * @since 4.0      */
specifier|public
name|PostgresBatchAction
parameter_list|(
name|BatchQuery
name|query
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
name|query
argument_list|,
name|dataNode
argument_list|,
name|runningAsBatch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|PreparedStatement
name|prepareStatement
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|String
name|queryStr
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|boolean
name|generatedKeys
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|generatedKeys
condition|)
block|{
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|generatedAttributes
init|=
name|query
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getGeneratedAttributes
argument_list|()
decl_stmt|;
name|String
index|[]
name|generatedPKColumns
init|=
operator|new
name|String
index|[
name|generatedAttributes
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|DbAttribute
name|generatedAttribute
range|:
name|generatedAttributes
control|)
block|{
if|if
condition|(
name|generatedAttribute
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
name|generatedPKColumns
index|[
name|i
operator|++
index|]
operator|=
name|generatedAttribute
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|connection
operator|.
name|prepareStatement
argument_list|(
name|queryStr
argument_list|,
name|Arrays
operator|.
name|copyOf
argument_list|(
name|generatedPKColumns
argument_list|,
name|i
argument_list|)
argument_list|)
return|;
block|}
return|return
name|connection
operator|.
name|prepareStatement
argument_list|(
name|queryStr
argument_list|)
return|;
block|}
block|}
end_class

end_unit

