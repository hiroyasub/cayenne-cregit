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
name|map
operator|.
name|DbEntity
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

begin_comment
comment|/**  * Insert query which contains information about DataDomain and creates special SQLAction  */
end_comment

begin_class
class|class
name|DataDomainInsertQuery
extends|extends
name|InsertBatchQuery
block|{
name|DataDomain
name|domain
decl_stmt|;
specifier|public
name|DataDomainInsertQuery
parameter_list|(
name|DataDomain
name|domain
parameter_list|,
name|DbEntity
name|entity
parameter_list|,
name|int
name|batchCapacity
parameter_list|)
block|{
name|super
argument_list|(
name|entity
argument_list|,
name|batchCapacity
argument_list|)
expr_stmt|;
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
block|}
specifier|public
name|DataDomain
name|getDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
annotation|@
name|Override
specifier|public
name|SQLAction
name|createSQLAction
parameter_list|(
name|SQLActionVisitor
name|visitor
parameter_list|)
block|{
return|return
operator|new
name|DataDomainActionBuilder
argument_list|(
name|domain
argument_list|,
name|visitor
argument_list|)
operator|.
name|batchAction
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

