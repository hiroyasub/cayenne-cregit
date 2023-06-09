begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|MockQuery
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
name|ObjectSelect
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
name|Gallery
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Tests various DataContextDelegate methods invocation and consequences on DataContext  * behavior.  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextDelegateIT
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
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prepare a single gallery record
name|Gallery
name|gallery
init|=
operator|(
name|Gallery
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Gallery"
argument_list|)
decl_stmt|;
name|gallery
operator|.
name|setGalleryName
argument_list|(
literal|"version1"
argument_list|)
expr_stmt|;
comment|// prepare a single artist record
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"version1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWillPerformGenericQuery
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|List
argument_list|<
name|Query
argument_list|>
name|queriesPerformed
init|=
operator|new
name|ArrayList
argument_list|<
name|Query
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|DataContextDelegate
name|delegate
init|=
operator|new
name|MockDataContextDelegate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Query
name|willPerformGenericQuery
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
name|queriesPerformed
operator|.
name|add
argument_list|(
name|query
argument_list|)
expr_stmt|;
return|return
name|query
return|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|setDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
comment|// test that delegate is consulted before select
name|MockQuery
name|query
init|=
operator|new
name|MockQuery
argument_list|()
decl_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Delegate is not notified of a query being run."
argument_list|,
name|queriesPerformed
operator|.
name|contains
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queriesPerformed
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Delegate unexpectedly blocked the query."
argument_list|,
name|query
operator|.
name|isRouteCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWillPerformGenericQueryBlocked
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|List
argument_list|<
name|Query
argument_list|>
name|queriesPerformed
init|=
operator|new
name|ArrayList
argument_list|<
name|Query
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|DataContextDelegate
name|delegate
init|=
operator|new
name|MockDataContextDelegate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Query
name|willPerformGenericQuery
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
name|queriesPerformed
operator|.
name|add
argument_list|(
name|query
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|setDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|MockQuery
name|query
init|=
operator|new
name|MockQuery
argument_list|()
decl_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Delegate is not notified of a query being run."
argument_list|,
name|queriesPerformed
operator|.
name|contains
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queriesPerformed
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Delegate couldn't block the query."
argument_list|,
name|query
operator|.
name|isRouteCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWillPerformQuery
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|List
argument_list|<
name|Query
argument_list|>
name|queriesPerformed
init|=
operator|new
name|ArrayList
argument_list|<
name|Query
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|DataContextDelegate
name|delegate
init|=
operator|new
name|MockDataContextDelegate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Query
name|willPerformQuery
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
name|queriesPerformed
operator|.
name|add
argument_list|(
name|query
argument_list|)
expr_stmt|;
return|return
name|query
return|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|setDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
comment|// test that delegate is consulted before select;
name|ObjectSelect
argument_list|<
name|Gallery
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Gallery
argument_list|>
name|results
init|=
name|query
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Delegate is not notified of a query being run."
argument_list|,
name|queriesPerformed
operator|.
name|contains
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queriesPerformed
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWillPerformQueryBlocked
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|List
argument_list|<
name|Query
argument_list|>
name|queriesPerformed
init|=
operator|new
name|ArrayList
argument_list|<
name|Query
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|DataContextDelegate
name|delegate
init|=
operator|new
name|MockDataContextDelegate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Query
name|willPerformQuery
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
name|queriesPerformed
operator|.
name|add
argument_list|(
name|query
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|setDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|ObjectSelect
argument_list|<
name|Gallery
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Gallery
argument_list|>
name|results
init|=
name|query
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Delegate is not notified of a query being run."
argument_list|,
name|queriesPerformed
operator|.
name|contains
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queriesPerformed
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|results
argument_list|)
expr_stmt|;
comment|// blocked
name|assertEquals
argument_list|(
literal|"Delegate couldn't block the query."
argument_list|,
literal|0
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

