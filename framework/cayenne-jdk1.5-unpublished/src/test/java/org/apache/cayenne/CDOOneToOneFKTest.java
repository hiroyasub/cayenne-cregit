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
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|DataContext
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
name|testdo
operator|.
name|relationship
operator|.
name|ToOneFK1
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
name|relationship
operator|.
name|ToOneFK2
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
name|RelationshipCase
import|;
end_import

begin_comment
comment|/**  * Tests the behavior of one-to-one relationship where to-one is pointing to an FK.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|CDOOneToOneFKTest
extends|extends
name|RelationshipCase
block|{
specifier|protected
name|DataContext
name|context
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
name|deleteTestData
argument_list|()
expr_stmt|;
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testReadRelationship
parameter_list|()
block|{
name|ToOneFK2
name|src
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ToOneFK2
operator|.
name|class
argument_list|)
decl_stmt|;
name|ToOneFK1
name|target
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ToOneFK1
operator|.
name|class
argument_list|)
decl_stmt|;
name|src
operator|.
name|setToOneToFK
argument_list|(
name|target
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
name|Arrays
operator|.
name|asList
argument_list|(
name|src
argument_list|,
name|target
argument_list|)
argument_list|)
expr_stmt|;
name|ToOneFK2
name|src1
init|=
operator|(
name|ToOneFK2
operator|)
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|src
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|src1
operator|.
name|getToOneToFK
argument_list|()
argument_list|)
expr_stmt|;
comment|// resolve HOLLOW
name|assertSame
argument_list|(
name|src1
argument_list|,
name|src1
operator|.
name|getToOneToFK
argument_list|()
operator|.
name|getToPK
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|src1
argument_list|,
name|src1
operator|.
name|getToOneToFK
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ToOneFK1
name|target2
init|=
operator|(
name|ToOneFK1
operator|)
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|target
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|target2
operator|.
name|getToPK
argument_list|()
argument_list|)
expr_stmt|;
comment|// resolve HOLLOW
name|assertSame
argument_list|(
name|target2
argument_list|,
name|target2
operator|.
name|getToPK
argument_list|()
operator|.
name|getToOneToFK
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test2Null
parameter_list|()
throws|throws
name|Exception
block|{
name|ToOneFK2
name|src
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ToOneFK2
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
comment|// test database data
name|ObjectIdQuery
name|refetch
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|src
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE_REFRESH
argument_list|)
decl_stmt|;
name|ToOneFK2
name|src2
init|=
operator|(
name|ToOneFK2
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|refetch
argument_list|)
decl_stmt|;
comment|// *** TESTING THIS ***
name|assertNull
argument_list|(
name|src2
operator|.
name|getToOneToFK
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReplaceNull1
parameter_list|()
throws|throws
name|Exception
block|{
name|ToOneFK2
name|src
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ToOneFK2
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
comment|// test database data
name|ObjectIdQuery
name|refetch
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|src
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE_REFRESH
argument_list|)
decl_stmt|;
name|ToOneFK2
name|src2
init|=
operator|(
name|ToOneFK2
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|refetch
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|src
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|src2
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
comment|// *** TESTING THIS ***
name|src2
operator|.
name|setToOneToFK
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|src2
operator|.
name|getToOneToFK
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReplaceNull2
parameter_list|()
throws|throws
name|Exception
block|{
name|ToOneFK2
name|src
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ToOneFK2
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ToOneFK1
name|target
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ToOneFK1
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// *** TESTING THIS ***
name|src
operator|.
name|setToOneToFK
argument_list|(
name|target
argument_list|)
expr_stmt|;
comment|// test before save
name|assertSame
argument_list|(
name|target
argument_list|,
name|src
operator|.
name|getToOneToFK
argument_list|()
argument_list|)
expr_stmt|;
comment|// do save
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
comment|// test database data
name|ObjectIdQuery
name|refetch
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|src
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE_REFRESH
argument_list|)
decl_stmt|;
name|ToOneFK2
name|src2
init|=
operator|(
name|ToOneFK2
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|refetch
argument_list|)
decl_stmt|;
name|ToOneFK1
name|target2
init|=
name|src2
operator|.
name|getToOneToFK
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|target2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|src
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|src2
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|target
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|target2
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNewAdd
parameter_list|()
throws|throws
name|Exception
block|{
name|ToOneFK2
name|src
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ToOneFK2
operator|.
name|class
argument_list|)
decl_stmt|;
name|ToOneFK1
name|target
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ToOneFK1
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// *** TESTING THIS ***
name|src
operator|.
name|setToOneToFK
argument_list|(
name|target
argument_list|)
expr_stmt|;
comment|// test before save
name|assertSame
argument_list|(
name|target
argument_list|,
name|src
operator|.
name|getToOneToFK
argument_list|()
argument_list|)
expr_stmt|;
comment|// do save
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
comment|// test database data
name|ObjectIdQuery
name|refetch
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|src
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE_REFRESH
argument_list|)
decl_stmt|;
name|ToOneFK2
name|src2
init|=
operator|(
name|ToOneFK2
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|refetch
argument_list|)
decl_stmt|;
name|ToOneFK1
name|target2
init|=
name|src2
operator|.
name|getToOneToFK
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|target2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|src
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|src2
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|target
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|target2
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTakeObjectSnapshotDependentFault
parameter_list|()
throws|throws
name|Exception
block|{
name|ToOneFK2
name|src
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ToOneFK2
operator|.
name|class
argument_list|)
decl_stmt|;
name|ToOneFK1
name|target
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ToOneFK1
operator|.
name|class
argument_list|)
decl_stmt|;
name|src
operator|.
name|setToOneToFK
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
name|ObjectIdQuery
name|refetch
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|src
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE_REFRESH
argument_list|)
decl_stmt|;
name|ToOneFK2
name|src2
init|=
operator|(
name|ToOneFK2
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|refetch
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|src2
operator|.
name|readPropertyDirectly
argument_list|(
literal|"toOneToFK"
argument_list|)
operator|instanceof
name|Fault
argument_list|)
expr_stmt|;
comment|// test that taking a snapshot does not trigger a fault, and generally works well
name|context
operator|.
name|currentSnapshot
argument_list|(
name|src2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|src2
operator|.
name|readPropertyDirectly
argument_list|(
literal|"toOneToFK"
argument_list|)
operator|instanceof
name|Fault
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|ToOneFK2
name|src
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ToOneFK2
operator|.
name|class
argument_list|)
decl_stmt|;
name|ToOneFK1
name|target
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ToOneFK1
operator|.
name|class
argument_list|)
decl_stmt|;
name|src
operator|.
name|setToOneToFK
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|src
operator|.
name|setToOneToFK
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObject
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// test database data
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
name|ObjectIdQuery
name|refetch
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|src
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE_REFRESH
argument_list|)
decl_stmt|;
name|ToOneFK2
name|src2
init|=
operator|(
name|ToOneFK2
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|refetch
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|src
operator|.
name|getToOneToFK
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|src
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|src2
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

