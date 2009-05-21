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
name|art
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
name|access
operator|.
name|DataContext
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|PersistentObjectTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testObjectContext
parameter_list|()
block|{
name|MockObjectContext
name|context
init|=
operator|new
name|MockObjectContext
argument_list|()
decl_stmt|;
name|PersistentObject
name|object
init|=
operator|new
name|MockPersistentObject
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|object
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|object
operator|.
name|setObjectContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|object
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPersistenceState
parameter_list|()
block|{
name|PersistentObject
name|object
init|=
operator|new
name|MockPersistentObject
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|object
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testObjectID
parameter_list|()
block|{
name|ObjectId
name|id
init|=
operator|new
name|ObjectId
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|PersistentObject
name|object
init|=
operator|new
name|MockPersistentObject
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|object
operator|.
name|setObjectId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|id
argument_list|,
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetObjEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|PersistentObject
name|a
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|getObjEntity
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|DataContext
operator|.
name|createDataContext
argument_list|()
operator|.
name|registerNewObject
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|getObjEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Artist"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

