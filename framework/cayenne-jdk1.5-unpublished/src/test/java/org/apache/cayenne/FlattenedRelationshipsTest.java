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
name|List
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
name|SQLTemplate
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
name|testdo
operator|.
name|relationship
operator|.
name|FlattenedCircular
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
name|FlattenedTest1
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
name|FlattenedTest2
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
name|FlattenedTest3
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Cayenne
import|;
end_import

begin_comment
comment|/**  * Test case for objects with flattened relationships.  *   */
end_comment

begin_class
specifier|public
class|class
name|FlattenedRelationshipsTest
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
name|testInsertJoinWithPK
parameter_list|()
throws|throws
name|Exception
block|{
name|FlattenedTest1
name|obj01
init|=
name|context
operator|.
name|newObject
argument_list|(
name|FlattenedTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|FlattenedTest3
name|obj11
init|=
name|context
operator|.
name|newObject
argument_list|(
name|FlattenedTest3
operator|.
name|class
argument_list|)
decl_stmt|;
name|FlattenedTest3
name|obj12
init|=
name|context
operator|.
name|newObject
argument_list|(
name|FlattenedTest3
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj01
operator|.
name|setName
argument_list|(
literal|"t01"
argument_list|)
expr_stmt|;
name|obj11
operator|.
name|setName
argument_list|(
literal|"t11"
argument_list|)
expr_stmt|;
name|obj12
operator|.
name|setName
argument_list|(
literal|"t12"
argument_list|)
expr_stmt|;
name|obj01
operator|.
name|addToFt3OverComplex
argument_list|(
name|obj11
argument_list|)
expr_stmt|;
name|obj01
operator|.
name|addToFt3OverComplex
argument_list|(
name|obj12
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|int
name|pk
init|=
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|obj01
argument_list|)
decl_stmt|;
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
name|FlattenedTest1
name|fresh01
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|FlattenedTest1
operator|.
name|class
argument_list|,
name|pk
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"t01"
argument_list|,
name|fresh01
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ValueHolder
name|related
init|=
operator|(
name|ValueHolder
operator|)
name|fresh01
operator|.
name|getFt3OverComplex
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|related
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
operator|(
name|List
operator|)
name|related
operator|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUnsetJoinWithPK
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testUnsetJoinWithPK"
argument_list|)
expr_stmt|;
name|SQLTemplate
name|joinSelect
init|=
operator|new
name|SQLTemplate
argument_list|(
name|FlattenedTest1
operator|.
name|class
argument_list|,
literal|"SELECT * FROM COMPLEX_JOIN"
argument_list|)
decl_stmt|;
name|joinSelect
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|context
operator|.
name|performQuery
argument_list|(
name|joinSelect
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|FlattenedTest1
name|ft1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|FlattenedTest1
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ft12"
argument_list|,
name|ft1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|related
init|=
name|ft1
operator|.
name|getFt3OverComplex
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|related
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|related
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|FlattenedTest3
name|ft3
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|FlattenedTest3
operator|.
name|class
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|related
operator|.
name|contains
argument_list|(
name|ft3
argument_list|)
argument_list|)
expr_stmt|;
name|ft1
operator|.
name|removeFromFt3OverComplex
argument_list|(
name|ft3
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|related
operator|.
name|contains
argument_list|(
name|ft3
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// the thing here is that there are two join records between
comment|// FT1 and FT3 (emulating invalid data or extras in the join table that
comment|// are ignored in the object model).. all (2) joins must be deleted
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|performQuery
argument_list|(
name|joinSelect
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testQualifyOnToManyFlattened
parameter_list|()
throws|throws
name|Exception
block|{
name|FlattenedTest1
name|obj01
init|=
name|context
operator|.
name|newObject
argument_list|(
name|FlattenedTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|FlattenedTest2
name|obj02
init|=
name|context
operator|.
name|newObject
argument_list|(
name|FlattenedTest2
operator|.
name|class
argument_list|)
decl_stmt|;
name|FlattenedTest3
name|obj031
init|=
name|context
operator|.
name|newObject
argument_list|(
name|FlattenedTest3
operator|.
name|class
argument_list|)
decl_stmt|;
name|FlattenedTest3
name|obj032
init|=
name|context
operator|.
name|newObject
argument_list|(
name|FlattenedTest3
operator|.
name|class
argument_list|)
decl_stmt|;
name|FlattenedTest1
name|obj11
init|=
name|context
operator|.
name|newObject
argument_list|(
name|FlattenedTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|FlattenedTest2
name|obj12
init|=
name|context
operator|.
name|newObject
argument_list|(
name|FlattenedTest2
operator|.
name|class
argument_list|)
decl_stmt|;
name|FlattenedTest3
name|obj131
init|=
name|context
operator|.
name|newObject
argument_list|(
name|FlattenedTest3
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj01
operator|.
name|setName
argument_list|(
literal|"t01"
argument_list|)
expr_stmt|;
name|obj02
operator|.
name|setName
argument_list|(
literal|"t02"
argument_list|)
expr_stmt|;
name|obj031
operator|.
name|setName
argument_list|(
literal|"t031"
argument_list|)
expr_stmt|;
name|obj032
operator|.
name|setName
argument_list|(
literal|"t032"
argument_list|)
expr_stmt|;
name|obj02
operator|.
name|setToFT1
argument_list|(
name|obj01
argument_list|)
expr_stmt|;
name|obj02
operator|.
name|addToFt3Array
argument_list|(
name|obj031
argument_list|)
expr_stmt|;
name|obj02
operator|.
name|addToFt3Array
argument_list|(
name|obj032
argument_list|)
expr_stmt|;
name|obj11
operator|.
name|setName
argument_list|(
literal|"t11"
argument_list|)
expr_stmt|;
name|obj131
operator|.
name|setName
argument_list|(
literal|"t131"
argument_list|)
expr_stmt|;
name|obj12
operator|.
name|setName
argument_list|(
literal|"t12"
argument_list|)
expr_stmt|;
name|obj12
operator|.
name|addToFt3Array
argument_list|(
name|obj131
argument_list|)
expr_stmt|;
name|obj12
operator|.
name|setToFT1
argument_list|(
name|obj11
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// test 1: qualify on flattened attribute
name|Expression
name|qual1
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"ft3Array.name"
argument_list|,
literal|"t031"
argument_list|)
decl_stmt|;
name|SelectQuery
name|query1
init|=
operator|new
name|SelectQuery
argument_list|(
name|FlattenedTest1
operator|.
name|class
argument_list|,
name|qual1
argument_list|)
decl_stmt|;
name|List
name|objects1
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|obj01
argument_list|,
name|objects1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// test 2: qualify on flattened relationship
name|Expression
name|qual2
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"ft3Array"
argument_list|,
name|obj131
argument_list|)
decl_stmt|;
name|SelectQuery
name|query2
init|=
operator|new
name|SelectQuery
argument_list|(
name|FlattenedTest1
operator|.
name|class
argument_list|,
name|qual2
argument_list|)
decl_stmt|;
name|List
name|objects2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|obj11
argument_list|,
name|objects2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToOneSeriesFlattenedRel
parameter_list|()
block|{
name|FlattenedTest1
name|ft1
init|=
operator|(
name|FlattenedTest1
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"FlattenedTest1"
argument_list|)
decl_stmt|;
name|ft1
operator|.
name|setName
argument_list|(
literal|"FT1Name"
argument_list|)
expr_stmt|;
name|FlattenedTest2
name|ft2
init|=
operator|(
name|FlattenedTest2
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"FlattenedTest2"
argument_list|)
decl_stmt|;
name|ft2
operator|.
name|setName
argument_list|(
literal|"FT2Name"
argument_list|)
expr_stmt|;
name|FlattenedTest3
name|ft3
init|=
operator|(
name|FlattenedTest3
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"FlattenedTest3"
argument_list|)
decl_stmt|;
name|ft3
operator|.
name|setName
argument_list|(
literal|"FT3Name"
argument_list|)
expr_stmt|;
name|ft2
operator|.
name|setToFT1
argument_list|(
name|ft1
argument_list|)
expr_stmt|;
name|ft2
operator|.
name|addToFt3Array
argument_list|(
name|ft3
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
comment|// We need a new context
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|FlattenedTest3
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"name"
argument_list|,
literal|"FT3Name"
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|FlattenedTest3
name|fetchedFT3
init|=
operator|(
name|FlattenedTest3
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|FlattenedTest1
name|fetchedFT1
init|=
name|fetchedFT3
operator|.
name|getToFT1
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"FT1Name"
argument_list|,
name|fetchedFT1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTakeObjectSnapshotFlattenedFault
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
comment|// fetch
name|List
name|ft3s
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|FlattenedTest3
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ft3s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|FlattenedTest3
name|ft3
init|=
operator|(
name|FlattenedTest3
operator|)
name|ft3s
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ft3
operator|.
name|readPropertyDirectly
argument_list|(
literal|"toFT1"
argument_list|)
operator|instanceof
name|Fault
argument_list|)
expr_stmt|;
comment|// test that taking a snapshot does not trigger a fault, and generally works well
name|Map
name|snapshot
init|=
name|context
operator|.
name|currentSnapshot
argument_list|(
name|ft3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ft3"
argument_list|,
name|snapshot
operator|.
name|get
argument_list|(
literal|"NAME"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ft3
operator|.
name|readPropertyDirectly
argument_list|(
literal|"toFT1"
argument_list|)
operator|instanceof
name|Fault
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRefetchWithFlattenedFaultToOneTarget1
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
comment|// fetch
name|List
name|ft3s
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|FlattenedTest3
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ft3s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|FlattenedTest3
name|ft3
init|=
operator|(
name|FlattenedTest3
operator|)
name|ft3s
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ft3
operator|.
name|readPropertyDirectly
argument_list|(
literal|"toFT1"
argument_list|)
operator|instanceof
name|Fault
argument_list|)
expr_stmt|;
comment|// refetch
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|FlattenedTest3
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ft3
operator|.
name|readPropertyDirectly
argument_list|(
literal|"toFT1"
argument_list|)
operator|instanceof
name|Fault
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFlattenedCircular
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testFlattenedCircular"
argument_list|)
expr_stmt|;
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
name|FlattenedCircular
name|fc1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|FlattenedCircular
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|FlattenedCircular
argument_list|>
name|side2s
init|=
name|fc1
operator|.
name|getSide2s
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|side2s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|FlattenedCircular
argument_list|>
name|side1s
init|=
name|fc1
operator|.
name|getSide1s
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|side1s
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

