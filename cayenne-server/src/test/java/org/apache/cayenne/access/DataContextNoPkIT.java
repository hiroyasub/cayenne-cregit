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
name|CayenneRuntimeException
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
name|DataRow
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
name|no_pk
operator|.
name|NoPkTestEntity
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
name|List
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
name|NO_PK_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextNoPkIT
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
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|TableHelper
name|noPkTestTable
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"NO_PK_TEST"
argument_list|,
literal|"ATTRIBUTE1"
argument_list|)
decl_stmt|;
name|noPkTestTable
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|noPkTestTable
operator|.
name|insert
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|noPkTestTable
operator|.
name|insert
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoPkFetchObjects
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|NoPkTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|fail
argument_list|(
literal|"Query for entity with no primary key must have failed, instead we got "
operator|+
name|objects
operator|.
name|size
argument_list|()
operator|+
literal|" rows."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
comment|// exception expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoPkFetchDataRows
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQuery
argument_list|<
name|DataRow
argument_list|>
name|query
init|=
name|SelectQuery
operator|.
name|dataRowQuery
argument_list|(
name|NoPkTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|rows
init|=
name|query
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rows
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|rows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataRow
name|row1
init|=
name|rows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|DataRow
name|row2
init|=
name|rows
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// assert that rows have different values
comment|// (there was a bug earlier that fetched distinct rows for
comment|// entities with no primary key.
name|assertTrue
argument_list|(
operator|!
name|row1
operator|.
name|get
argument_list|(
literal|"ATTRIBUTE1"
argument_list|)
operator|.
name|equals
argument_list|(
name|row2
operator|.
name|get
argument_list|(
literal|"ATTRIBUTE1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

