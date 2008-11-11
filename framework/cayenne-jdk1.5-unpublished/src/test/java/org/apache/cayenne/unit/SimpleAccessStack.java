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
name|unit
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|StringET1ExtendedType
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
name|DataDomain
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
name|UnitTestDomain
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
name|event
operator|.
name|EventManager
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
name|DataMap
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
name|Procedure
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
name|Query
import|;
end_import

begin_comment
comment|/**  * Default implementation of the AccessStack that has a single DataNode per DataMap.  *   */
end_comment

begin_class
specifier|public
class|class
name|SimpleAccessStack
extends|extends
name|AbstractAccessStack
implements|implements
name|AccessStack
block|{
specifier|protected
name|UnitTestDomain
name|domain
decl_stmt|;
specifier|protected
name|DataSetFactory
name|dataSetFactory
decl_stmt|;
specifier|public
name|SimpleAccessStack
parameter_list|(
name|CayenneResources
name|resources
parameter_list|,
name|DataSetFactory
name|dataSetFactory
parameter_list|,
name|DataMap
index|[]
name|maps
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|dataSetFactory
operator|=
name|dataSetFactory
expr_stmt|;
name|this
operator|.
name|resources
operator|=
name|resources
expr_stmt|;
name|this
operator|.
name|domain
operator|=
operator|new
name|UnitTestDomain
argument_list|(
literal|"domain"
argument_list|)
expr_stmt|;
name|domain
operator|.
name|setEventManager
argument_list|(
operator|new
name|EventManager
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|DataMap
name|map
range|:
name|maps
control|)
block|{
name|initNode
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|DataDomain
name|getDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
specifier|protected
name|void
name|initNode
parameter_list|(
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|DataNode
name|node
init|=
name|resources
operator|.
name|newDataNode
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// setup test extended types
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
operator|.
name|registerType
argument_list|(
operator|new
name|StringET1ExtendedType
argument_list|()
argument_list|)
expr_stmt|;
comment|// tweak mapping with a delegate
for|for
control|(
name|Procedure
name|proc
range|:
name|map
operator|.
name|getProcedures
argument_list|()
control|)
block|{
name|getAdapter
argument_list|(
name|node
argument_list|)
operator|.
name|tweakProcedure
argument_list|(
name|proc
argument_list|)
expr_stmt|;
block|}
name|node
operator|.
name|addDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
comment|// use shared data source in all cases but the multi-node...
if|if
condition|(
name|MultiNodeCase
operator|.
name|NODE1
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|MultiNodeCase
operator|.
name|NODE2
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|node
operator|.
name|setDataSource
argument_list|(
name|resources
operator|.
name|createDataSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|node
operator|.
name|setDataSource
argument_list|(
name|resources
operator|.
name|getDataSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|domain
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns DataDomain for this AccessStack.      */
specifier|public
name|UnitTestDomain
name|getDataDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
specifier|public
name|void
name|createTestData
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|testCase
parameter_list|,
name|String
name|testName
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|Query
name|query
init|=
name|dataSetFactory
operator|.
name|getDataSetQuery
argument_list|(
name|testCase
argument_list|,
name|testName
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|getDataDomain
argument_list|()
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
comment|/**      * Deletes all data from the database tables mentioned in the DataMap.      */
specifier|public
name|void
name|deleteTestData
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|deleteTestData
argument_list|(
name|node
argument_list|,
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Drops all test tables. */
specifier|public
name|void
name|dropSchema
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|dropSchema
argument_list|(
name|node
argument_list|,
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates all test tables in the database.      */
specifier|public
name|void
name|createSchema
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|createSchema
argument_list|(
name|node
argument_list|,
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|dropPKSupport
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|dropPKSupport
argument_list|(
name|node
argument_list|,
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates primary key support for all node DbEntities. Will use its facilities      * provided by DbAdapter to generate any necessary database objects and data for      * primary key support.      */
specifier|public
name|void
name|createPKSupport
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|createPKSupport
argument_list|(
name|node
argument_list|,
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

