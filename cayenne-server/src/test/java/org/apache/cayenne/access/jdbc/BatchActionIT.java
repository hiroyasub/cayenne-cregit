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
name|reader
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|di
operator|.
name|AdhocObjectFactory
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
name|di
operator|.
name|Inject
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
name|testdo
operator|.
name|testmap
operator|.
name|Artist
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|BatchActionIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testHasGeneratedKeys1
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getChannel
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
comment|// test with adapter that supports keys
name|JdbcAdapter
name|adapter
init|=
name|buildAdapter
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|DataNode
name|node
init|=
operator|new
name|DataNode
argument_list|()
decl_stmt|;
name|node
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|node
operator|.
name|setEntityResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|node
operator|.
name|setRowReaderFactory
argument_list|(
name|mock
argument_list|(
name|RowReaderFactory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|InsertBatchQuery
name|batch2
init|=
operator|new
name|InsertBatchQuery
argument_list|(
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|getDbEntity
argument_list|()
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
operator|new
name|BatchAction
argument_list|(
name|batch2
argument_list|,
name|node
argument_list|,
literal|false
argument_list|)
operator|.
name|hasGeneratedKeys
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHasGeneratedKeys2
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getChannel
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
comment|// test with adapter that does not support keys...
name|JdbcAdapter
name|adapter
init|=
name|buildAdapter
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|DataNode
name|node
init|=
operator|new
name|DataNode
argument_list|()
decl_stmt|;
name|node
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|node
operator|.
name|setEntityResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|node
operator|.
name|setRowReaderFactory
argument_list|(
name|mock
argument_list|(
name|RowReaderFactory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|InsertBatchQuery
name|batch2
init|=
operator|new
name|InsertBatchQuery
argument_list|(
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|getDbEntity
argument_list|()
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
operator|new
name|BatchAction
argument_list|(
name|batch2
argument_list|,
name|node
argument_list|,
literal|false
argument_list|)
operator|.
name|hasGeneratedKeys
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|JdbcAdapter
name|buildAdapter
parameter_list|(
name|boolean
name|supportGeneratedKeys
parameter_list|)
block|{
name|JdbcAdapter
name|adapter
init|=
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|JdbcAdapter
operator|.
name|class
argument_list|,
name|JdbcAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|adapter
operator|.
name|setSupportsGeneratedKeys
argument_list|(
name|supportGeneratedKeys
argument_list|)
expr_stmt|;
return|return
name|adapter
return|;
block|}
block|}
end_class

end_unit

