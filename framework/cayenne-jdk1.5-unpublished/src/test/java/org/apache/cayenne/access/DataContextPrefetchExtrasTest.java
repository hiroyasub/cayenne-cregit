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
name|testmap
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
name|cayenne
operator|.
name|testdo
operator|.
name|testmap
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
name|cayenne
operator|.
name|testdo
operator|.
name|testmap
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
name|cayenne
operator|.
name|testdo
operator|.
name|testmap
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

begin_comment
comment|/**  * Test prefetching of various obscure cases.  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextPrefetchExtrasTest
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
name|tCharPkTest
decl_stmt|;
specifier|protected
name|TableHelper
name|tCharFkTest
decl_stmt|;
specifier|protected
name|TableHelper
name|tCompoundPkTest
decl_stmt|;
specifier|protected
name|TableHelper
name|tCompoundFkTest
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
literal|"CHAR_PK_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CHAR_FK_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"COMPOUND_PK_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"COMPOUND_FK_TEST"
argument_list|)
expr_stmt|;
name|tCharPkTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"CHAR_PK_TEST"
argument_list|)
expr_stmt|;
name|tCharPkTest
operator|.
name|setColumns
argument_list|(
literal|"PK_COL"
argument_list|,
literal|"OTHER_COL"
argument_list|)
expr_stmt|;
name|tCharFkTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"CHAR_FK_TEST"
argument_list|)
expr_stmt|;
name|tCharFkTest
operator|.
name|setColumns
argument_list|(
literal|"PK"
argument_list|,
literal|"FK_COL"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tCompoundPkTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"COMPOUND_PK_TEST"
argument_list|)
expr_stmt|;
name|tCompoundPkTest
operator|.
name|setColumns
argument_list|(
literal|"KEY1"
argument_list|,
literal|"KEY2"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tCompoundFkTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"COMPOUND_FK_TEST"
argument_list|)
expr_stmt|;
name|tCompoundFkTest
operator|.
name|setColumns
argument_list|(
literal|"PKEY"
argument_list|,
literal|"F_KEY1"
argument_list|,
literal|"F_KEY2"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createPrefetchToManyOnCharKeyDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tCharPkTest
operator|.
name|insert
argument_list|(
literal|"k1"
argument_list|,
literal|"n1"
argument_list|)
expr_stmt|;
name|tCharPkTest
operator|.
name|insert
argument_list|(
literal|"k2"
argument_list|,
literal|"n2"
argument_list|)
expr_stmt|;
name|tCharFkTest
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"k1"
argument_list|,
literal|"fn1"
argument_list|)
expr_stmt|;
name|tCharFkTest
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"k1"
argument_list|,
literal|"fn2"
argument_list|)
expr_stmt|;
name|tCharFkTest
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"k2"
argument_list|,
literal|"fn3"
argument_list|)
expr_stmt|;
name|tCharFkTest
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|"k2"
argument_list|,
literal|"fn4"
argument_list|)
expr_stmt|;
name|tCharFkTest
operator|.
name|insert
argument_list|(
literal|5
argument_list|,
literal|"k1"
argument_list|,
literal|"fn5"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createCompoundDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tCompoundPkTest
operator|.
name|insert
argument_list|(
literal|"101"
argument_list|,
literal|"201"
argument_list|,
literal|"CPK1"
argument_list|)
expr_stmt|;
name|tCompoundPkTest
operator|.
name|insert
argument_list|(
literal|"102"
argument_list|,
literal|"202"
argument_list|,
literal|"CPK2"
argument_list|)
expr_stmt|;
name|tCompoundPkTest
operator|.
name|insert
argument_list|(
literal|"103"
argument_list|,
literal|"203"
argument_list|,
literal|"CPK3"
argument_list|)
expr_stmt|;
name|tCompoundFkTest
operator|.
name|insert
argument_list|(
literal|301
argument_list|,
literal|"102"
argument_list|,
literal|"202"
argument_list|,
literal|"CFK1"
argument_list|)
expr_stmt|;
name|tCompoundFkTest
operator|.
name|insert
argument_list|(
literal|302
argument_list|,
literal|"102"
argument_list|,
literal|"202"
argument_list|,
literal|"CFK2"
argument_list|)
expr_stmt|;
name|tCompoundFkTest
operator|.
name|insert
argument_list|(
literal|303
argument_list|,
literal|"101"
argument_list|,
literal|"201"
argument_list|,
literal|"CFK3"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPrefetchToManyOnCharKey
parameter_list|()
throws|throws
name|Exception
block|{
name|createPrefetchToManyOnCharKeyDataSet
argument_list|()
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
argument_list|<
name|?
argument_list|>
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
argument_list|<
name|?
argument_list|>
name|toMany
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
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
name|createCompoundDataSet
argument_list|()
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
argument_list|<
name|?
argument_list|>
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
comment|/**      * Tests to-many prefetching over relationships with compound keys.      */
specifier|public
name|void
name|testPrefetch11
parameter_list|()
throws|throws
name|Exception
block|{
name|createCompoundDataSet
argument_list|()
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
argument_list|<
name|?
argument_list|>
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
argument_list|<
name|?
argument_list|>
name|toMany
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
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

