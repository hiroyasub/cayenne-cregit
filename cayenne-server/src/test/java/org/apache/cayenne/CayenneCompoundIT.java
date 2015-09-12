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
name|compound
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
name|compound
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
name|HashMap
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
name|Map
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|COMPOUND_PROJECT
argument_list|)
specifier|public
class|class
name|CayenneCompoundIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
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
name|tCompoundPKTest
decl_stmt|;
specifier|protected
name|TableHelper
name|tCharPKTest
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
name|tCompoundPKTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"COMPOUND_PK_TEST"
argument_list|)
expr_stmt|;
name|tCompoundPKTest
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
name|tCharPKTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"CHAR_PK_TEST"
argument_list|)
expr_stmt|;
name|tCharPKTest
operator|.
name|setColumns
argument_list|(
literal|"PK_COL"
argument_list|,
literal|"OTHER_COL"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createOneCompoundPK
parameter_list|()
throws|throws
name|Exception
block|{
name|tCompoundPKTest
operator|.
name|insert
argument_list|(
literal|"PK1"
argument_list|,
literal|"PK2"
argument_list|,
literal|"BBB"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createOneCharPK
parameter_list|()
throws|throws
name|Exception
block|{
name|tCharPKTest
operator|.
name|insert
argument_list|(
literal|"CPK"
argument_list|,
literal|"AAAA"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectForPKEntityMapCompound
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneCompoundPK
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|pk
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|pk
operator|.
name|put
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY1_PK_COLUMN
argument_list|,
literal|"PK1"
argument_list|)
expr_stmt|;
name|pk
operator|.
name|put
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY2_PK_COLUMN
argument_list|,
literal|"PK2"
argument_list|)
expr_stmt|;
name|Object
name|object
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|CompoundPkTestEntity
operator|.
name|class
argument_list|,
name|pk
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|CompoundPkTestEntity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BBB"
argument_list|,
operator|(
operator|(
name|CompoundPkTestEntity
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompoundPKForObject
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneCompoundPK
argument_list|()
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
operator|new
name|SelectQuery
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
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
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|pk
init|=
name|Cayenne
operator|.
name|compoundPKForObject
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pk
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|pk
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PK1"
argument_list|,
name|pk
operator|.
name|get
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY1_PK_COLUMN
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PK2"
argument_list|,
name|pk
operator|.
name|get
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY2_PK_COLUMN
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIntPKForObjectFailureForCompound
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneCompoundPK
argument_list|()
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
operator|new
name|SelectQuery
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
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
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"intPKForObject must fail for compound key"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIntPKForObjectFailureForNonNumeric
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneCharPK
argument_list|()
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
operator|new
name|SelectQuery
argument_list|(
name|CharPkTestEntity
operator|.
name|class
argument_list|)
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
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"intPKForObject must fail for non-numeric key"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPKForObjectFailureForCompound
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneCompoundPK
argument_list|()
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
operator|new
name|SelectQuery
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
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
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|Cayenne
operator|.
name|pkForObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"pkForObject must fail for compound key"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIntPKForObjectNonNumeric
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneCharPK
argument_list|()
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
operator|new
name|SelectQuery
argument_list|(
name|CharPkTestEntity
operator|.
name|class
argument_list|)
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
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CPK"
argument_list|,
name|Cayenne
operator|.
name|pkForObject
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

