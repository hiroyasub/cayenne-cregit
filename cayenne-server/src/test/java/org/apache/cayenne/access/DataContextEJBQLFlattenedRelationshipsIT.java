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
name|Persistent
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|test
operator|.
name|jdbc
operator|.
name|TableHelper
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
name|relationships_flattened
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_FLATTENED_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextEJBQLFlattenedRelationshipsIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|ft1Helper
decl_stmt|;
specifier|protected
name|TableHelper
name|ft2Helper
decl_stmt|;
specifier|protected
name|TableHelper
name|ft3Helper
decl_stmt|;
specifier|protected
name|TableHelper
name|ft4Helper
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
name|ft1Helper
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"FLATTENED_TEST_1"
argument_list|,
literal|"FT1_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|ft2Helper
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"FLATTENED_TEST_2"
argument_list|)
expr_stmt|;
name|ft2Helper
operator|.
name|setColumns
argument_list|(
literal|"FT2_ID"
argument_list|,
literal|"FT1_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|ft3Helper
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"FLATTENED_TEST_3"
argument_list|)
expr_stmt|;
name|ft3Helper
operator|.
name|setColumns
argument_list|(
literal|"FT3_ID"
argument_list|,
literal|"FT2_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|ft4Helper
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"FLATTENED_TEST_4"
argument_list|)
expr_stmt|;
name|ft4Helper
operator|.
name|setColumns
argument_list|(
literal|"FT4_ID"
argument_list|,
literal|"FT3_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createFt123
parameter_list|()
throws|throws
name|Exception
block|{
name|ft1Helper
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"ft1"
argument_list|)
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"ft12"
argument_list|)
expr_stmt|;
name|ft2Helper
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"ft2"
argument_list|)
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|,
literal|"ft22"
argument_list|)
expr_stmt|;
name|ft3Helper
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"ft3"
argument_list|)
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|,
literal|"ft3-a"
argument_list|)
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|2
argument_list|,
literal|"ft3-b"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createFt1234
parameter_list|()
throws|throws
name|Exception
block|{
name|createFt123
argument_list|()
expr_stmt|;
name|ft4Helper
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"ft4"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionMemberOfThetaJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|createFt123
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT f FROM FlattenedTest3 f, FlattenedTest1 ft "
operator|+
literal|"WHERE f MEMBER OF ft.ft3Array AND ft = :ft"
decl_stmt|;
name|FlattenedTest1
name|ft
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
literal|"ft"
argument_list|,
name|ft
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionMemberOfThetaJoinLongRelationshipSequence
parameter_list|()
throws|throws
name|Exception
block|{
name|createFt1234
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT f FROM FlattenedTest4 f, FlattenedTest1 ft "
operator|+
literal|"WHERE f MEMBER OF ft.ft4ArrayFor1 AND ft = :ft"
decl_stmt|;
name|FlattenedTest1
name|ft
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
literal|1
argument_list|)
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
literal|"ft"
argument_list|,
name|ft
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|ft
operator|=
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
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|"ft"
argument_list|,
name|ft
argument_list|)
expr_stmt|;
name|objects
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionInnerJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|createFt123
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT ft FROM FlattenedTest1 ft INNER JOIN ft.ft3Array f WHERE ft = :ft"
decl_stmt|;
name|FlattenedTest1
name|ft
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
literal|1
argument_list|)
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
literal|"ft"
argument_list|,
name|ft
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|objects
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionAsInnerJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|createFt123
argument_list|()
expr_stmt|;
comment|// this query is equivalent to the previous INNER JOIN example
name|String
name|ejbql
init|=
literal|"SELECT OBJECT(ft) FROM FlattenedTest1 ft, IN(ft.ft3Array) f WHERE ft = :ft"
decl_stmt|;
name|FlattenedTest1
name|ft
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
literal|1
argument_list|)
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
literal|"ft"
argument_list|,
name|ft
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|objects
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionThetaJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|createFt123
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT DISTINCT ft FROM FlattenedTest1 ft , FlattenedTest3 f3 WHERE f3.toFT1 = ft"
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
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|objects
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionIdentificationVariable
parameter_list|()
throws|throws
name|Exception
block|{
name|createFt123
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT ft.ft3Array FROM FlattenedTest1 ft WHERE ft = :ft"
decl_stmt|;
name|FlattenedTest1
name|ft
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
literal|"ft"
argument_list|,
name|ft
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|objects
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAssociationFieldSelect
parameter_list|()
throws|throws
name|Exception
block|{
name|createFt123
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT ft3.toFT1 FROM FlattenedTest3 ft3 WHERE ft3.toFT1 = :ft"
decl_stmt|;
name|FlattenedTest1
name|ft
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
literal|1
argument_list|)
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
literal|"ft"
argument_list|,
name|ft
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionSubquery
parameter_list|()
throws|throws
name|Exception
block|{
name|createFt123
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT ft FROM FlattenedTest1 ft "
operator|+
literal|"WHERE (SELECT COUNT(f) FROM ft.ft3Array f) = 1"
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
comment|// TODO fails but not because of flattened relationship,
comment|// the reason is that property "ft3Array" inside the subquery
comment|// parses as unmapped
comment|/*          * List<?> objects = context.performQuery(query); assertNotNull(objects);          * assertFalse(objects.isEmpty()); assertEquals(1, objects.size()); Set<Object>          * ids = new HashSet<>(); Iterator<?> it = objects.iterator(); while          * (it.hasNext()) { Object id = Cayenne.pkForObject((Persistent) it.next());          * ids.add(id); } assertTrue(ids.contains(2));          */
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionSubquery1
parameter_list|()
throws|throws
name|Exception
block|{
name|createFt123
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT ft FROM FlattenedTest1 ft "
operator|+
literal|"WHERE (SELECT COUNT(f3) FROM FlattenedTest3 f3 WHERE f3 MEMBER OF ft.ft3Array)> 1"
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
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|objects
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|objects
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|id
init|=
name|Cayenne
operator|.
name|pkForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGroupByFlattenedRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|createFt123
argument_list|()
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT COUNT(ft3), ft3.toFT1 FROM FlattenedTest3 ft3  GROUP BY ft3.toFT1 "
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
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

