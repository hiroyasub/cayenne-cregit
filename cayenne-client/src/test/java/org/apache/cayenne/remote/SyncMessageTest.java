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
name|remote
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
name|DataChannel
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
name|ObjectContext
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
name|graph
operator|.
name|CompoundDiff
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
name|graph
operator|.
name|GraphDiff
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
name|graph
operator|.
name|NodeCreateOperation
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
name|remote
operator|.
name|hessian
operator|.
name|service
operator|.
name|HessianUtil
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
name|assertEquals
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
name|assertNotNull
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
name|assertNull
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
name|assertSame
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
name|assertTrue
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
name|fail
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
specifier|public
class|class
name|SyncMessageTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testConstructor
parameter_list|()
block|{
name|ObjectContext
name|source
init|=
name|mock
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|GraphDiff
name|diff
init|=
operator|new
name|CompoundDiff
argument_list|()
decl_stmt|;
name|SyncMessage
name|message
init|=
operator|new
name|SyncMessage
argument_list|(
name|source
argument_list|,
name|DataChannel
operator|.
name|FLUSH_NOCASCADE_SYNC
argument_list|,
name|diff
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|source
argument_list|,
name|message
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DataChannel
operator|.
name|FLUSH_NOCASCADE_SYNC
argument_list|,
name|message
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|diff
argument_list|,
name|message
operator|.
name|getSenderChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHessianSerialization
parameter_list|()
throws|throws
name|Exception
block|{
comment|// id must be a serializable object; source doesn't have to be
name|ObjectContext
name|source
init|=
name|mock
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|GraphDiff
name|diff
init|=
operator|new
name|NodeCreateOperation
argument_list|(
literal|"id-string"
argument_list|)
decl_stmt|;
name|SyncMessage
name|message
init|=
operator|new
name|SyncMessage
argument_list|(
name|source
argument_list|,
name|DataChannel
operator|.
name|FLUSH_NOCASCADE_SYNC
argument_list|,
name|diff
argument_list|)
decl_stmt|;
name|Object
name|d
init|=
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|message
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d
operator|instanceof
name|SyncMessage
argument_list|)
expr_stmt|;
name|SyncMessage
name|ds
init|=
operator|(
name|SyncMessage
operator|)
name|d
decl_stmt|;
name|assertNull
argument_list|(
name|ds
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|message
operator|.
name|getType
argument_list|()
argument_list|,
name|ds
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ds
operator|.
name|getSenderChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConstructorInvalid
parameter_list|()
block|{
name|ObjectContext
name|source
init|=
name|mock
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
decl_stmt|;
operator|new
name|SyncMessage
argument_list|(
name|source
argument_list|,
name|DataChannel
operator|.
name|FLUSH_NOCASCADE_SYNC
argument_list|,
operator|new
name|CompoundDiff
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|SyncMessage
argument_list|(
name|source
argument_list|,
name|DataChannel
operator|.
name|FLUSH_CASCADE_SYNC
argument_list|,
operator|new
name|CompoundDiff
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|SyncMessage
argument_list|(
literal|null
argument_list|,
name|DataChannel
operator|.
name|ROLLBACK_CASCADE_SYNC
argument_list|,
operator|new
name|CompoundDiff
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|bogusType
init|=
literal|45678
decl_stmt|;
try|try
block|{
operator|new
name|SyncMessage
argument_list|(
name|source
argument_list|,
name|bogusType
argument_list|,
operator|new
name|CompoundDiff
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"invalid type was allowed to go unnoticed..."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
block|}
block|}
block|}
end_class

end_unit

