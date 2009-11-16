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
name|CharFkTestEntity
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
name|CharPkTestEntity
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
name|CompoundFkTestEntity
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
name|CayenneDataObject
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
name|SortOrder
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
comment|/**  * Test prefetching of various obscure cases.  *   */
end_comment

begin_class
specifier|public
class|class
name|DataContextPrefetchExtrasTest
extends|extends
name|CayenneCase
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
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
name|testPrefetchToManyOnCharKey
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPrefetchToManyOnCharKey"
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|CharPkTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"charFKs"
argument_list|)
expr_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
name|CharPkTestEntity
operator|.
name|OTHER_COL_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|List
name|pks
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
literal|2
argument_list|,
name|pks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CharPkTestEntity
name|pk1
init|=
operator|(
name|CharPkTestEntity
operator|)
name|pks
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"n1"
argument_list|,
name|pk1
operator|.
name|getOtherCol
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|toMany
init|=
operator|(
name|List
operator|)
name|pk1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"charFKs"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toMany
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|toMany
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
name|toMany
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CharFkTestEntity
name|fk1
init|=
operator|(
name|CharFkTestEntity
operator|)
name|toMany
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|fk1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|pk1
argument_list|,
name|fk1
operator|.
name|getToCharPK
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests to-one prefetching over relationships with compound keys.      */
specifier|public
name|void
name|testPrefetch10
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testCompound"
argument_list|)
expr_stmt|;
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"name"
argument_list|,
literal|"CFK2"
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|CompoundFkTestEntity
operator|.
name|class
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"toCompoundPk"
argument_list|)
expr_stmt|;
name|List
name|objects
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CayenneDataObject
name|fk1
init|=
operator|(
name|CayenneDataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
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
name|fk1
operator|.
name|readNestedProperty
argument_list|(
literal|"toCompoundPk"
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
name|pk1
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
name|pk1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CPK2"
argument_list|,
name|pk1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"name"
argument_list|)
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
comment|/**      * Tests to-many prefetching over relationships with compound keys.      */
specifier|public
name|void
name|testPrefetch11
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testCompound"
argument_list|)
expr_stmt|;
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"name"
argument_list|,
literal|"CPK2"
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"compoundFkArray"
argument_list|)
expr_stmt|;
name|List
name|pks
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
name|pks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CayenneDataObject
name|pk1
init|=
operator|(
name|CayenneDataObject
operator|)
name|pks
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
name|toMany
init|=
operator|(
name|List
operator|)
name|pk1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"compoundFkArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toMany
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|toMany
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
name|toMany
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CayenneDataObject
name|fk1
init|=
operator|(
name|CayenneDataObject
operator|)
name|toMany
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|fk1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|CayenneDataObject
name|fk2
init|=
operator|(
name|CayenneDataObject
operator|)
name|toMany
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|fk2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

