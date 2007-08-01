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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|BooleanTestEntity
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
name|CompoundPkTestEntity
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
name|ObjectContext
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
name|QueryResponse
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
name|DataContextEJBQLUpdateTest
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
name|testUpdateQualifier
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|EJBQLQuery
name|check
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(p) from Painting p "
operator|+
literal|"WHERE p.paintingTitle is NULL or p.paintingTitle<> 'XX'"
argument_list|)
decl_stmt|;
name|Object
name|notUpdated
init|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|2l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"UPDATE Painting AS p SET p.paintingTitle = 'XX' WHERE p.paintingTitle = 'P1'"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|QueryResponse
name|result
init|=
name|context
operator|.
name|performGenericQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|int
index|[]
name|count
init|=
name|result
operator|.
name|firstUpdateCount
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|notUpdated
operator|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|1l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateNoQualifierString
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|EJBQLQuery
name|check
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(p) from Painting p "
operator|+
literal|"WHERE p.paintingTitle is NULL or p.paintingTitle<> 'XX'"
argument_list|)
decl_stmt|;
name|Object
name|notUpdated
init|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|2l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"UPDATE Painting AS p SET p.paintingTitle = 'XX'"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|QueryResponse
name|result
init|=
name|context
operator|.
name|performGenericQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|int
index|[]
name|count
init|=
name|result
operator|.
name|firstUpdateCount
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|count
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|notUpdated
operator|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|0l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateNoQualifierNull
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|EJBQLQuery
name|check
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(p) from Painting p "
operator|+
literal|"WHERE p.estimatedPrice is not null"
argument_list|)
decl_stmt|;
name|Object
name|notUpdated
init|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|2l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"UPDATE Painting AS p SET p.estimatedPrice = NULL"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|QueryResponse
name|result
init|=
name|context
operator|.
name|performGenericQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|int
index|[]
name|count
init|=
name|result
operator|.
name|firstUpdateCount
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|count
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|notUpdated
operator|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|0l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
block|}
comment|// This fails until we implement arithmetic exps
comment|//    public void testUpdateNoQualifierArithmeticExpression() throws Exception {
comment|//        createTestData("prepare");
comment|//
comment|//        ObjectContext context = createDataContext();
comment|//
comment|//        EJBQLQuery check = new EJBQLQuery("select count(p) from Painting p "
comment|//                + "WHERE p.paintingTitle is NULL or p.estimatedPrice<= 5000");
comment|//
comment|//        Object notUpdated = DataObjectUtils.objectForQuery(context, check);
comment|//        assertEquals(new Long(2l), notUpdated);
comment|//
comment|//        String ejbql = "UPDATE Painting AS p SET p.estimatedPrice = p.estimatedPrice * 2";
comment|//        EJBQLQuery query = new EJBQLQuery(ejbql);
comment|//
comment|//        QueryResponse result = context.performGenericQuery(query);
comment|//
comment|//        int[] count = result.firstUpdateCount();
comment|//        assertNotNull(count);
comment|//        assertEquals(1, count.length);
comment|//        assertEquals(2, count[0]);
comment|//
comment|//        notUpdated = DataObjectUtils.objectForQuery(context, check);
comment|//        assertEquals(new Long(0l), notUpdated);
comment|//    }
specifier|public
name|void
name|testUpdateNoQualifierMultipleItems
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|EJBQLQuery
name|check
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(p) from Painting p "
operator|+
literal|"WHERE p.estimatedPrice is NULL or p.estimatedPrice<> 1"
argument_list|)
decl_stmt|;
name|Object
name|notUpdated
init|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|2l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"UPDATE Painting AS p SET p.paintingTitle = 'XX', p.estimatedPrice = 1"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|QueryResponse
name|result
init|=
name|context
operator|.
name|performGenericQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|int
index|[]
name|count
init|=
name|result
operator|.
name|firstUpdateCount
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|count
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|notUpdated
operator|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|0l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateNoQualifierDecimal
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|EJBQLQuery
name|check
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(p) from Painting p "
operator|+
literal|"WHERE p.estimatedPrice is NULL or p.estimatedPrice<> 1.1"
argument_list|)
decl_stmt|;
name|Object
name|notUpdated
init|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|2l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"UPDATE Painting AS p SET p.estimatedPrice = 1.1"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|QueryResponse
name|result
init|=
name|context
operator|.
name|performGenericQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|int
index|[]
name|count
init|=
name|result
operator|.
name|firstUpdateCount
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|count
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|notUpdated
operator|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|0l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateNoQualifierBoolean
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|BooleanTestEntity
name|o1
init|=
operator|(
name|BooleanTestEntity
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|BooleanTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setBooleanColumn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|BooleanTestEntity
name|o2
init|=
operator|(
name|BooleanTestEntity
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|BooleanTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|o2
operator|.
name|setBooleanColumn
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|BooleanTestEntity
name|o3
init|=
operator|(
name|BooleanTestEntity
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|BooleanTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|o3
operator|.
name|setBooleanColumn
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|check
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(p) from BooleanTestEntity p "
operator|+
literal|"WHERE p.booleanColumn = true"
argument_list|)
decl_stmt|;
name|Object
name|notUpdated
init|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|1l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"UPDATE BooleanTestEntity AS p SET p.booleanColumn = true"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|QueryResponse
name|result
init|=
name|context
operator|.
name|performGenericQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|int
index|[]
name|count
init|=
name|result
operator|.
name|firstUpdateCount
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|count
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|notUpdated
operator|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|3l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateNoQualifierToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|object
init|=
operator|(
name|Artist
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|33003
argument_list|)
decl_stmt|;
name|EJBQLQuery
name|check
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(p) from Painting p "
operator|+
literal|"WHERE p.toArtist<> :artist"
argument_list|)
decl_stmt|;
name|check
operator|.
name|setParameter
argument_list|(
literal|"artist"
argument_list|,
name|object
argument_list|)
expr_stmt|;
name|Object
name|notUpdated
init|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|2l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"UPDATE Painting AS p SET p.toArtist = :artist"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|"artist"
argument_list|,
name|object
argument_list|)
expr_stmt|;
name|QueryResponse
name|result
init|=
name|context
operator|.
name|performGenericQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|int
index|[]
name|count
init|=
name|result
operator|.
name|firstUpdateCount
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|count
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|notUpdated
operator|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|0l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateNoQualifierToOneCompoundPK
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepareCompound"
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Map
name|key1
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|key1
operator|.
name|put
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY1_PK_COLUMN
argument_list|,
literal|"b1"
argument_list|)
expr_stmt|;
name|key1
operator|.
name|put
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY2_PK_COLUMN
argument_list|,
literal|"b2"
argument_list|)
expr_stmt|;
name|CompoundPkTestEntity
name|object
init|=
operator|(
name|CompoundPkTestEntity
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|CompoundPkTestEntity
operator|.
name|class
argument_list|,
name|key1
argument_list|)
decl_stmt|;
name|EJBQLQuery
name|check
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select count(e) from CompoundFkTestEntity e WHERE e.toCompoundPk<> :param"
argument_list|)
decl_stmt|;
name|check
operator|.
name|setParameter
argument_list|(
literal|"param"
argument_list|,
name|object
argument_list|)
expr_stmt|;
name|Object
name|notUpdated
init|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|1l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"UPDATE CompoundFkTestEntity e SET e.toCompoundPk = :param"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|"param"
argument_list|,
name|object
argument_list|)
expr_stmt|;
name|QueryResponse
name|result
init|=
name|context
operator|.
name|performGenericQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|int
index|[]
name|count
init|=
name|result
operator|.
name|firstUpdateCount
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|count
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|notUpdated
operator|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|check
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|0l
argument_list|)
argument_list|,
name|notUpdated
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

