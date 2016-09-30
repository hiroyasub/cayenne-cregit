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
name|dbsync
operator|.
name|merge
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
name|dba
operator|.
name|QuotingStrategy
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
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
name|map
operator|.
name|DbEntity
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * A {@link MergerToken} to add a "not null" clause to a column.  */
end_comment

begin_class
specifier|public
class|class
name|SetNotNullToDb
extends|extends
name|AbstractToDbToken
operator|.
name|EntityAndColumn
block|{
specifier|public
name|SetNotNullToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
name|super
argument_list|(
literal|"Set Not Null"
argument_list|,
name|entity
argument_list|,
name|column
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|createSql
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|QuotingStrategy
name|context
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
decl_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"ALTER TABLE "
operator|+
name|context
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|getEntity
argument_list|()
argument_list|)
operator|+
literal|" ALTER COLUMN "
operator|+
name|context
operator|.
name|quotedName
argument_list|(
name|getColumn
argument_list|()
argument_list|)
operator|+
literal|" SET NOT NULL"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|MergerToken
name|createReverse
parameter_list|(
name|MergerTokenFactory
name|factory
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createSetAllowNullToModel
argument_list|(
name|getEntity
argument_list|()
argument_list|,
name|getColumn
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

