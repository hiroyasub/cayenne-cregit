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
operator|.
name|jdbc
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
name|access
operator|.
name|trans
operator|.
name|DeleteBatchQueryBuilder
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
name|InsertBatchQueryBuilder
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
name|UpdateBatchQueryBuilder
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

begin_comment
comment|/**  * Default implementation of {@link BatchQueryBuilderFactory}.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultBatchQueryBuilderFactory
implements|implements
name|BatchQueryBuilderFactory
block|{
specifier|public
name|BatchQueryBuilder
name|createDeleteQueryBuilder
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
return|return
operator|new
name|DeleteBatchQueryBuilder
argument_list|(
name|adapter
argument_list|)
return|;
block|}
specifier|public
name|BatchQueryBuilder
name|createInsertQueryBuilder
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
return|return
operator|new
name|InsertBatchQueryBuilder
argument_list|(
name|adapter
argument_list|)
return|;
block|}
specifier|public
name|BatchQueryBuilder
name|createUpdateQueryBuilder
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
return|return
operator|new
name|UpdateBatchQueryBuilder
argument_list|(
name|adapter
argument_list|)
return|;
block|}
block|}
end_class

end_unit
