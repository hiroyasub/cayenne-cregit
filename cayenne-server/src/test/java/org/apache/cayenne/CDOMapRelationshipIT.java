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
name|RefreshQuery
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
name|relationship
operator|.
name|IdMapToMany
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
name|MapToMany
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
name|MapToManyTarget
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
name|Test
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|RELATIONSHIPS_PROJECT
argument_list|)
specifier|public
class|class
name|CDOMapRelationshipIT
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
name|tMapToMany
decl_stmt|;
specifier|protected
name|TableHelper
name|tMapToManyTarget
decl_stmt|;
specifier|protected
name|TableHelper
name|tIdMapToMany
decl_stmt|;
specifier|protected
name|TableHelper
name|tIdMapToManyTarget
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MAP_TO_MANY_TARGET"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MAP_TO_MANY"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ID_MAP_TO_MANY_TARGET"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ID_MAP_TO_MANY"
argument_list|)
expr_stmt|;
name|tMapToMany
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MAP_TO_MANY"
argument_list|)
expr_stmt|;
name|tMapToMany
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|tMapToManyTarget
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MAP_TO_MANY_TARGET"
argument_list|)
expr_stmt|;
name|tMapToManyTarget
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"MAP_TO_MANY_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tIdMapToMany
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ID_MAP_TO_MANY"
argument_list|)
expr_stmt|;
name|tIdMapToMany
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|tIdMapToManyTarget
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ID_MAP_TO_MANY_TARGET"
argument_list|)
expr_stmt|;
name|tIdMapToManyTarget
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"MAP_TO_MANY_ID"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createTestDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tMapToMany
operator|.
name|insert
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|tMapToMany
operator|.
name|insert
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|tMapToManyTarget
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|tMapToManyTarget
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|tMapToManyTarget
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|1
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
name|tMapToManyTarget
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|2
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createTestIdDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tIdMapToMany
operator|.
name|insert
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|tIdMapToMany
operator|.
name|insert
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|tIdMapToManyTarget
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tIdMapToManyTarget
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tIdMapToManyTarget
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tIdMapToManyTarget
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|MapToMany
name|o1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|MapToMany
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Map
name|targets
init|=
name|o1
operator|.
name|getTargets
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|targets
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|targets
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|targets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|targets
operator|.
name|get
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|targets
operator|.
name|get
argument_list|(
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|targets
operator|.
name|get
argument_list|(
literal|"C"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|targets
operator|.
name|get
argument_list|(
literal|"A"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|targets
operator|.
name|get
argument_list|(
literal|"B"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|targets
operator|.
name|get
argument_list|(
literal|"C"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadToManyId
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestIdDataSet
argument_list|()
expr_stmt|;
name|IdMapToMany
name|o1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|IdMapToMany
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Map
name|targets
init|=
name|o1
operator|.
name|getTargets
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|targets
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|targets
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|targets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|targets
operator|.
name|get
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|targets
operator|.
name|get
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|targets
operator|.
name|get
argument_list|(
operator|new
name|Integer
argument_list|(
literal|3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|targets
operator|.
name|get
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|targets
operator|.
name|get
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
operator|(
name|Persistent
operator|)
name|targets
operator|.
name|get
argument_list|(
operator|new
name|Integer
argument_list|(
literal|3
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadToManyPrefetching
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|MapToMany
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchDbExp
argument_list|(
name|MapToMany
operator|.
name|ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
name|MapToMany
operator|.
name|TARGETS_PROPERTY
argument_list|)
expr_stmt|;
name|MapToMany
name|o1
init|=
operator|(
name|MapToMany
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|Map
name|targets
init|=
name|o1
operator|.
name|getTargets
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|targets
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|targets
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|targets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|targets
operator|.
name|get
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|targets
operator|.
name|get
argument_list|(
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|targets
operator|.
name|get
argument_list|(
literal|"C"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|MapToMany
name|o1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|MapToMany
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Map
name|targets
init|=
name|o1
operator|.
name|getTargets
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|targets
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|targets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|MapToManyTarget
name|newTarget
init|=
name|o1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|MapToManyTarget
operator|.
name|class
argument_list|)
decl_stmt|;
name|newTarget
operator|.
name|setName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|addToTargets
argument_list|(
name|newTarget
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|targets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|newTarget
argument_list|,
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|get
argument_list|(
literal|"X"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
argument_list|,
name|newTarget
operator|.
name|getMapToMany
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|o1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|RefreshQuery
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|o1
operator|.
name|getTargets
argument_list|()
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
name|testRemoveToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|MapToMany
name|o1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|MapToMany
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Map
name|targets
init|=
name|o1
operator|.
name|getTargets
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|targets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|MapToManyTarget
name|target
init|=
operator|(
name|MapToManyTarget
operator|)
name|targets
operator|.
name|get
argument_list|(
literal|"B"
argument_list|)
decl_stmt|;
name|o1
operator|.
name|removeFromTargets
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|targets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|get
argument_list|(
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|target
operator|.
name|getMapToMany
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|o1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|RefreshQuery
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|get
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|get
argument_list|(
literal|"C"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddToManyViaReverse
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|MapToMany
name|o1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|MapToMany
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Map
name|targets
init|=
name|o1
operator|.
name|getTargets
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|targets
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|targets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|MapToManyTarget
name|newTarget
init|=
name|o1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|MapToManyTarget
operator|.
name|class
argument_list|)
decl_stmt|;
name|newTarget
operator|.
name|setName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|newTarget
operator|.
name|setMapToMany
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
argument_list|,
name|newTarget
operator|.
name|getMapToMany
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|targets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|newTarget
argument_list|,
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|get
argument_list|(
literal|"X"
argument_list|)
argument_list|)
expr_stmt|;
name|o1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|o1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|RefreshQuery
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|o1
operator|.
name|getTargets
argument_list|()
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
name|testModifyToManyKey
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestDataSet
argument_list|()
expr_stmt|;
name|MapToMany
name|o1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|MapToMany
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Map
name|targets
init|=
name|o1
operator|.
name|getTargets
argument_list|()
decl_stmt|;
name|MapToManyTarget
name|target
init|=
operator|(
name|MapToManyTarget
operator|)
name|targets
operator|.
name|get
argument_list|(
literal|"B"
argument_list|)
decl_stmt|;
name|target
operator|.
name|setName
argument_list|(
literal|"B1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|get
argument_list|(
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|target
argument_list|,
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|get
argument_list|(
literal|"B1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

