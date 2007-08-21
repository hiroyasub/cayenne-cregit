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
name|Set
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
name|testdo
operator|.
name|relationship
operator|.
name|SetToMany
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
name|SetToManyTarget
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

begin_class
specifier|public
class|class
name|CDOSetRelationshipTest
extends|extends
name|RelationshipCase
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
name|testReadToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|SetToMany
name|o1
init|=
operator|(
name|SetToMany
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|createDataContext
argument_list|()
argument_list|,
name|SetToMany
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Set
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
name|assertTrue
argument_list|(
name|targets
operator|.
name|contains
argument_list|(
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|,
name|SetToManyTarget
operator|.
name|class
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|targets
operator|.
name|contains
argument_list|(
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|,
name|SetToManyTarget
operator|.
name|class
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|targets
operator|.
name|contains
argument_list|(
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|,
name|SetToManyTarget
operator|.
name|class
argument_list|,
literal|3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReadToManyPrefetching
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|SetToMany
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchDbExp
argument_list|(
name|SetToMany
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
name|SetToMany
operator|.
name|TARGETS_PROPERTY
argument_list|)
expr_stmt|;
name|SetToMany
name|o1
init|=
operator|(
name|SetToMany
operator|)
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|createDataContext
argument_list|()
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|Set
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
name|assertTrue
argument_list|(
name|targets
operator|.
name|contains
argument_list|(
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|,
name|SetToManyTarget
operator|.
name|class
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|targets
operator|.
name|contains
argument_list|(
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|,
name|SetToManyTarget
operator|.
name|class
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|targets
operator|.
name|contains
argument_list|(
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|,
name|SetToManyTarget
operator|.
name|class
argument_list|,
literal|3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|SetToMany
name|o1
init|=
operator|(
name|SetToMany
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|createDataContext
argument_list|()
argument_list|,
name|SetToMany
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Set
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
name|SetToManyTarget
name|newTarget
init|=
operator|(
name|SetToManyTarget
operator|)
name|o1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|SetToManyTarget
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|assertTrue
argument_list|(
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|contains
argument_list|(
name|newTarget
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
argument_list|,
name|newTarget
operator|.
name|getSetToMany
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
specifier|public
name|void
name|testRemoveToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|SetToMany
name|o1
init|=
operator|(
name|SetToMany
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|createDataContext
argument_list|()
argument_list|,
name|SetToMany
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Set
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
name|SetToManyTarget
name|target
init|=
operator|(
name|SetToManyTarget
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|,
name|SetToManyTarget
operator|.
name|class
argument_list|,
literal|2
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
name|assertFalse
argument_list|(
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|contains
argument_list|(
name|target
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|target
operator|.
name|getSetToMany
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
name|assertFalse
argument_list|(
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|contains
argument_list|(
name|target
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddToManyViaReverse
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|SetToMany
name|o1
init|=
operator|(
name|SetToMany
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|createDataContext
argument_list|()
argument_list|,
name|SetToMany
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Set
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
name|SetToManyTarget
name|newTarget
init|=
operator|(
name|SetToManyTarget
operator|)
name|o1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|SetToManyTarget
operator|.
name|class
argument_list|)
decl_stmt|;
name|newTarget
operator|.
name|setSetToMany
argument_list|(
name|o1
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
name|assertTrue
argument_list|(
name|o1
operator|.
name|getTargets
argument_list|()
operator|.
name|contains
argument_list|(
name|newTarget
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
argument_list|,
name|newTarget
operator|.
name|getSetToMany
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
block|}
end_class

end_unit

