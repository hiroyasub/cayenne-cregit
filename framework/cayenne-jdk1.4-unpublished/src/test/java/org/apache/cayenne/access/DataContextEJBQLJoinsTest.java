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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|DataObjectUtils
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
name|EJBQLQuery
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

begin_class
specifier|public
class|class
name|DataContextEJBQLJoinsTest
extends|extends
name|CayenneCase
block|{
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
block|}
specifier|public
name|void
name|testThetaJoins
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testThetaJoins"
argument_list|)
expr_stmt|;
comment|//        String ejbql = "SELECT DISTINCT a "
comment|//                + "FROM Artist a, Painting b "
comment|//                + "WHERE a.artistName = b.paintingTitle";
comment|//
comment|//        List artists = createDataContext().performQuery(new EJBQLQuery(ejbql));
comment|//        assertEquals(2, artists.size());
comment|//
comment|//        Set names = new HashSet(2);
comment|//        Iterator it = artists.iterator();
comment|//        while (it.hasNext()) {
comment|//            Artist a = (Artist) it.next();
comment|//            names.add(a.getArtistName());
comment|//        }
comment|//
comment|//        assertTrue(names.contains("AA1"));
comment|//        assertTrue(names.contains("BB2"));
block|}
specifier|public
name|void
name|testInnerJoins
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testInnerJoins"
argument_list|)
expr_stmt|;
comment|//        String ejbql = "SELECT a "
comment|//                + "FROM Artist a INNER JOIN a.paintingArray p "
comment|//                + "WHERE a.artistName = 'A1'";
comment|//
comment|//        List artists = createDataContext().performQuery(new EJBQLQuery(ejbql));
comment|//        assertEquals(1, artists.size());
comment|//        assertEquals(33001, DataObjectUtils.intPKForObject((Artist) artists.get(0)));
block|}
specifier|public
name|void
name|testOuterJoins
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testInnerJoins"
argument_list|)
expr_stmt|;
comment|// String ejbql = "SELECT a "
comment|// + "FROM Artist a LEFT JOIN a.paintingArray p "
comment|// + "WHERE a.artistName = 'A1'";
comment|//
comment|// List artists = createDataContext().performQuery(new EJBQLQuery(ejbql));
comment|// assertEquals(2, artists.size());
comment|// Set ids = new HashSet(2);
comment|// Iterator it = artists.iterator();
comment|// while (it.hasNext()) {
comment|// Artist a = (Artist) it.next();
comment|// ids.add(DataObjectUtils.pkForObject(a));
comment|// }
comment|//
comment|//        assertTrue(ids.contains(new Integer(33001)));
comment|//        assertTrue(ids.contains(new Integer(33005)));
block|}
block|}
end_class

end_unit

