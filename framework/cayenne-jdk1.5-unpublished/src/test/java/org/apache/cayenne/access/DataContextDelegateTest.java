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
name|art
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_comment
comment|/**  * Tests various DataContextDelegate methods invocation and consequences on DataContext  * behavior.  *   */
end_comment

begin_class
specifier|public
class|class
name|DataContextDelegateTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|DataContext
name|context
decl_stmt|;
specifier|protected
name|Gallery
name|gallery
decl_stmt|;
specifier|protected
name|Artist
name|artist
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|context
operator|=
name|createDataContextWithSharedCache
argument_list|()
expr_stmt|;
comment|// prepare a single gallery record
name|gallery
operator|=
operator|(
name|Gallery
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Gallery"
argument_list|)
expr_stmt|;
name|gallery
operator|.
name|setGalleryName
argument_list|(
literal|"version1"
argument_list|)
expr_stmt|;
comment|// prepare a single artist record
name|artist
operator|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Artist"
argument_list|)
expr_stmt|;
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
comment|// test that delegate is consulted before select
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
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
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
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

