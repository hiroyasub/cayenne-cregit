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
name|PersistenceByReachabilityTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context1
decl_stmt|;
specifier|public
name|void
name|testToOneTargetTransient
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|persistentDO
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|transientDO
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|persistentDO
operator|.
name|setToOneTarget
argument_list|(
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|,
name|transientDO
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|transientDO
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToOneTargetPersistent
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|transientDO
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|persistentDO
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|transientDO
operator|.
name|setToOneTarget
argument_list|(
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|,
name|persistentDO
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|transientDO
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToOneTargetDifferentContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|doC1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|doC2
init|=
name|context1
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// this is the case where exception must be thrown as DataContexts are
comment|// different
try|try
block|{
name|doC1
operator|.
name|setToOneTarget
argument_list|(
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|,
name|doC2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"failed to detect relationship between objects in different DataContexts"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|public
name|void
name|testToManyTargetDifferentContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|doC1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|doC2
init|=
name|context1
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// this is the case where exception must be thrown as DataContexts are
comment|// different
try|try
block|{
name|doC2
operator|.
name|addToManyTarget
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|,
name|doC1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"failed to detect relationship between objects in different DataContexts"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
block|}
block|}
specifier|public
name|void
name|testToManyTargetTransient
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|transientDO
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|persistentDO
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|persistentDO
operator|.
name|addToManyTarget
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|,
name|transientDO
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|transientDO
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToManyTargetPersistent
parameter_list|()
throws|throws
name|Exception
block|{
name|Painting
name|persistentDO
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|transientDO
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|transientDO
operator|.
name|addToManyTarget
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|,
name|persistentDO
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|transientDO
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

