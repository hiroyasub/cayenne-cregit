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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|FilteredPrefetchTest
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
comment|/**      * Test prefetching with qualifier on the root query containing the path to the      * prefetch.      */
specifier|public
name|void
name|testDisjointToManyConflictingQualifier
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: CAY-319 - this is a known nasty limitation.
comment|// createTestData("testDisjointToManyConflictingQualifier");
comment|//
comment|// DataContext context = createDataContext();
comment|// Expression exp = ExpressionFactory.matchExp("paintingArray.paintingTitle",
comment|// "P_artist12");
comment|//
comment|// SelectQuery q = new SelectQuery(Artist.class, exp);
comment|// q.addPrefetch("paintingArray");
comment|//
comment|// List results = context.performQuery(q);
comment|//
comment|// // block further queries
comment|// context.setDelegate(new QueryBlockingDelegate());
comment|// assertEquals(1, results.size());
comment|//
comment|// Artist a = (Artist) results.get(0);
comment|//
comment|// List paintings = a.getPaintingArray();
comment|// assertFalse(((ToManyList) paintings).needsFetch());
comment|// assertEquals(2, paintings.size());
block|}
block|}
end_class

end_unit

