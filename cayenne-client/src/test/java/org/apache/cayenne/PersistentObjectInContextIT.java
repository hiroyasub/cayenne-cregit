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
name|ObjectIdQuery
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
name|test
operator|.
name|jdbc
operator|.
name|TableHelper
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
name|mt
operator|.
name|ClientMtTable1
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
name|mt
operator|.
name|ClientMtTable2
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
name|mt
operator|.
name|MtTable1
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
name|mt
operator|.
name|MtTable2
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
name|client
operator|.
name|ClientCase
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
name|CayenneProjects
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
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|PersistentObjectHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|assertFalse
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|MULTI_TIER_PROJECT
argument_list|)
specifier|public
class|class
name|PersistentObjectInContextIT
extends|extends
name|ClientCase
block|{
annotation|@
name|Inject
specifier|private
name|CayenneContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable1
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable2
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MT_TABLE1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|setColumns
argument_list|(
literal|"TABLE1_ID"
argument_list|,
literal|"GLOBAL_ATTRIBUTE1"
argument_list|,
literal|"SERVER_ATTRIBUTE1"
argument_list|)
expr_stmt|;
name|tMtTable2
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MT_TABLE2"
argument_list|)
expr_stmt|;
name|tMtTable2
operator|.
name|setColumns
argument_list|(
literal|"TABLE2_ID"
argument_list|,
literal|"TABLE1_ID"
argument_list|,
literal|"GLOBAL_ATTRIBUTE"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createTwoMtTable1sAnd2sDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"g1"
argument_list|,
literal|"s1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"g2"
argument_list|,
literal|"s2"
argument_list|)
expr_stmt|;
name|tMtTable2
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"g1"
argument_list|)
expr_stmt|;
name|tMtTable2
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"g2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveToManyReverseResolved
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
name|ObjectId
name|gid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
name|MtTable1
operator|.
name|TABLE1_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|ClientMtTable1
name|t1
init|=
operator|(
name|ClientMtTable1
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
name|gid
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|t1
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ClientMtTable2
argument_list|>
name|t2s
init|=
name|t1
operator|.
name|getTable2Array
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|t2s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ClientMtTable2
name|t2
range|:
name|t2s
control|)
block|{
name|PersistentObjectHolder
name|holder
init|=
operator|(
name|PersistentObjectHolder
operator|)
name|t2
operator|.
name|getTable1Direct
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|holder
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t1
argument_list|,
name|holder
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToOneRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
name|ObjectId
name|gid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"MtTable2"
argument_list|,
name|MtTable2
operator|.
name|TABLE2_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|ClientMtTable2
name|mtTable21
init|=
operator|(
name|ClientMtTable2
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
name|gid
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mtTable21
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|mtTable1
init|=
name|mtTable21
operator|.
name|getTable1
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"To one relationship incorrectly resolved to null"
argument_list|,
name|mtTable1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
name|mtTable1
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveToOneReverseResolved
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoMtTable1sAnd2sDataSet
argument_list|()
expr_stmt|;
name|ObjectId
name|gid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"MtTable2"
argument_list|,
name|MtTable2
operator|.
name|TABLE2_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|ClientMtTable2
name|mtTable21
init|=
operator|(
name|ClientMtTable2
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
name|gid
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mtTable21
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|mtTable1
init|=
name|mtTable21
operator|.
name|getTable1
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"To one relationship incorrectly resolved to null"
argument_list|,
name|mtTable1
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ClientMtTable2
argument_list|>
name|list
init|=
name|mtTable1
operator|.
name|getTable2Array
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|list
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
comment|// resolve it here...
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ClientMtTable2
name|t2
range|:
name|list
control|)
block|{
name|PersistentObjectHolder
name|holder
init|=
operator|(
name|PersistentObjectHolder
operator|)
name|t2
operator|.
name|getTable1Direct
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|holder
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|mtTable1
argument_list|,
name|holder
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
name|mtTable1
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

