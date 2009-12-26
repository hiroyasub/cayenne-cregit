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
name|runtime
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|dbsync
operator|.
name|CreateIfNoSchemaStrategy
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
name|dbsync
operator|.
name|SchemaUpdateStrategy
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
name|itest
operator|.
name|ItestDBUtils
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
name|itest
operator|.
name|ItestTableUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|CayenneServerRuntimeCase
extends|extends
name|TestCase
block|{
specifier|static
specifier|final
name|Map
argument_list|<
name|RuntimeName
argument_list|,
name|CayenneServerRuntime
argument_list|>
name|runtimeCache
decl_stmt|;
static|static
block|{
name|runtimeCache
operator|=
operator|new
name|HashMap
argument_list|<
name|RuntimeName
argument_list|,
name|CayenneServerRuntime
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|CayenneServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|RuntimeName
name|name
init|=
name|getRuntimeName
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|runtime
operator|=
name|runtimeCache
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|runtime
operator|==
literal|null
condition|)
block|{
name|runtime
operator|=
operator|new
name|CayenneServerRuntime
argument_list|(
name|name
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|runtimeCache
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|runtime
argument_list|)
expr_stmt|;
comment|// setup schema
comment|// TODO: should that be drop/create?
name|SchemaUpdateStrategy
name|dbCreator
init|=
operator|new
name|CreateIfNoSchemaStrategy
argument_list|()
decl_stmt|;
name|dbCreator
operator|.
name|updateSchema
argument_list|(
name|getDataNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|runtime
operator|=
literal|null
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|RuntimeName
name|getRuntimeName
parameter_list|()
function_decl|;
specifier|protected
name|ItestDBUtils
name|getDbUtils
parameter_list|()
block|{
return|return
operator|new
name|ItestDBUtils
argument_list|(
name|getDataNode
argument_list|()
operator|.
name|getDataSource
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|ItestTableUtils
name|getTableHelper
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
return|return
operator|new
name|ItestTableUtils
argument_list|(
name|getDbUtils
argument_list|()
argument_list|,
name|tableName
argument_list|)
return|;
block|}
specifier|private
name|DataNode
name|getDataNode
parameter_list|()
block|{
name|Collection
argument_list|<
name|DataNode
argument_list|>
name|nodes
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getDataNodes
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Can't find DataSource - no nodes configured"
argument_list|,
name|nodes
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Can't find DataSource - multiple nodes found"
argument_list|,
literal|1
argument_list|,
name|nodes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|nodes
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
block|}
end_class

end_unit

