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
name|PersistenceState
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
name|query
operator|.
name|SelectQuery
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|BinaryPKTest1
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
name|BinaryPKTest2
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
name|UnitDbAdapter
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
name|DataContextBinaryPKTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataContext
name|context1
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|accessStackAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsBinaryPK
argument_list|()
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BINARY_PK_TEST2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BINARY_PK_TEST1"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testInsertBinaryPK
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsBinaryPK
argument_list|()
condition|)
block|{
name|BinaryPKTest1
name|master
init|=
operator|(
name|BinaryPKTest1
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"BinaryPKTest1"
argument_list|)
decl_stmt|;
name|master
operator|.
name|setName
argument_list|(
literal|"master1"
argument_list|)
expr_stmt|;
name|BinaryPKTest2
name|detail
init|=
operator|(
name|BinaryPKTest2
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"BinaryPKTest2"
argument_list|)
decl_stmt|;
name|detail
operator|.
name|setDetailName
argument_list|(
literal|"detail2"
argument_list|)
expr_stmt|;
name|master
operator|.
name|addToBinaryPKDetails
argument_list|(
name|detail
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testFetchRelationshipBinaryPK
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsBinaryPK
argument_list|()
condition|)
block|{
name|BinaryPKTest1
name|master
init|=
operator|(
name|BinaryPKTest1
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"BinaryPKTest1"
argument_list|)
decl_stmt|;
name|master
operator|.
name|setName
argument_list|(
literal|"master1"
argument_list|)
expr_stmt|;
name|BinaryPKTest2
name|detail
init|=
operator|(
name|BinaryPKTest2
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"BinaryPKTest2"
argument_list|)
decl_stmt|;
name|detail
operator|.
name|setDetailName
argument_list|(
literal|"detail2"
argument_list|)
expr_stmt|;
name|master
operator|.
name|addToBinaryPKDetails
argument_list|(
name|detail
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|master
argument_list|,
name|detail
argument_list|)
expr_stmt|;
name|BinaryPKTest2
name|fetchedDetail
init|=
operator|(
name|BinaryPKTest2
operator|)
name|context1
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|BinaryPKTest2
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|fetchedDetail
operator|.
name|readPropertyDirectly
argument_list|(
literal|"toBinaryPKMaster"
argument_list|)
argument_list|)
expr_stmt|;
name|BinaryPKTest1
name|fetchedMaster
init|=
name|fetchedDetail
operator|.
name|getToBinaryPKMaster
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|fetchedMaster
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|fetchedMaster
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"master1"
argument_list|,
name|fetchedMaster
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
