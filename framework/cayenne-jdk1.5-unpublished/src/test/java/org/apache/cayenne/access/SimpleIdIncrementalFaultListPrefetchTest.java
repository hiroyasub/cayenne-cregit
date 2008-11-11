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
name|cayenne
operator|.
name|DataObject
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
name|ValueHolder
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionFactory
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|SimpleIdIncrementalFaultListPrefetchTest
extends|extends
name|DataContextCase
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
name|createTestData
argument_list|(
literal|"testPaintings"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testListType
parameter_list|()
block|{
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"artist1%"
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Artist"
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|q
operator|.
name|setPageSize
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|SimpleIdIncrementalFaultList
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test that all queries specified in prefetch are executed with a single prefetch      * path.      */
specifier|public
name|void
name|testPrefetch1
parameter_list|()
block|{
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"artist1%"
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Artist"
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"paintingArray"
argument_list|)
expr_stmt|;
name|q
operator|.
name|setPageSize
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|IncrementalFaultList
name|result
init|=
operator|(
name|IncrementalFaultList
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// currently queries with prefetch do not resolve their first page
name|assertEquals
argument_list|(
name|result
operator|.
name|size
argument_list|()
argument_list|,
name|result
operator|.
name|getUnfetchedObjects
argument_list|()
argument_list|)
expr_stmt|;
comment|// go through the second page objects and count queries
name|getDomain
argument_list|()
operator|.
name|restartQueryCounter
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|4
init|;
name|i
operator|<
literal|8
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
comment|// within the same page only one query should've been executed
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getDomain
argument_list|()
operator|.
name|getQueryCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test that a to-many relationship is initialized.      */
specifier|public
name|void
name|testPrefetch3
parameter_list|()
block|{
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"artistName"
argument_list|,
literal|"artist1%"
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Artist"
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"paintingArray"
argument_list|)
expr_stmt|;
name|q
operator|.
name|setPageSize
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|IncrementalFaultList
name|result
init|=
operator|(
name|IncrementalFaultList
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// currently queries with prefetch do not resolve their first page
name|assertEquals
argument_list|(
name|result
operator|.
name|size
argument_list|()
argument_list|,
name|result
operator|.
name|getUnfetchedObjects
argument_list|()
argument_list|)
expr_stmt|;
comment|// go through the second page objects and check their to many
for|for
control|(
name|int
name|i
init|=
literal|4
init|;
name|i
operator|<
literal|8
condition|;
name|i
operator|++
control|)
block|{
name|Artist
name|a
init|=
operator|(
name|Artist
operator|)
name|result
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|List
name|paintings
init|=
name|a
operator|.
name|getPaintingArray
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|paintings
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|paintings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test that a to-one relationship is initialized.      */
specifier|public
name|void
name|testPrefetch4
parameter_list|()
block|{
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|q
operator|.
name|setPageSize
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"toArtist"
argument_list|)
expr_stmt|;
name|IncrementalFaultList
name|result
init|=
operator|(
name|IncrementalFaultList
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
comment|// get an objects from the second page
name|DataObject
name|p1
init|=
operator|(
name|DataObject
operator|)
name|result
operator|.
name|get
argument_list|(
name|q
operator|.
name|getPageSize
argument_list|()
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|Object
name|toOnePrefetch
init|=
name|p1
operator|.
name|readNestedProperty
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toOnePrefetch
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected DataObject, got: "
operator|+
name|toOnePrefetch
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|toOnePrefetch
operator|instanceof
name|DataObject
argument_list|)
expr_stmt|;
name|DataObject
name|a1
init|=
operator|(
name|DataObject
operator|)
name|toOnePrefetch
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|a1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

