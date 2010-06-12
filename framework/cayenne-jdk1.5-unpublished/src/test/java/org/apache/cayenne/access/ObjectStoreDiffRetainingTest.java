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
name|Cayenne
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
name|testdo
operator|.
name|testmap
operator|.
name|Painting
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
name|CayenneCase
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ObjectStoreDiffRetainingTest
extends|extends
name|CayenneCase
block|{
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteTestData
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSnapshotRetainedOnPropertyModification
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|2000
argument_list|)
decl_stmt|;
name|ObjectStore
name|objectStore
init|=
name|context
operator|.
name|getObjectStore
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|objectStore
operator|.
name|getChangesByObjectId
argument_list|()
operator|.
name|get
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"some other name"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|objectStore
operator|.
name|getChangesByObjectId
argument_list|()
operator|.
name|get
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSnapshotRetainedOnRelAndPropertyModification
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|2000
argument_list|)
decl_stmt|;
name|ObjectStore
name|objectStore
init|=
name|context
operator|.
name|getObjectStore
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|objectStore
operator|.
name|getChangesByObjectId
argument_list|()
operator|.
name|get
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// we are trying to reproduce the bug CAY-213 - relationship modification puts
comment|// object in a modified state, so later when object is really modified, its
comment|// snapshot is not retained... in testing this I am leaving some flexibility for
comment|// the framework to retain a snapshot when it deems appropriate...
name|a
operator|.
name|addToPaintingArray
argument_list|(
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"some other name"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Snapshot wasn't retained - CAY-213"
argument_list|,
name|objectStore
operator|.
name|getChangesByObjectId
argument_list|()
operator|.
name|get
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

