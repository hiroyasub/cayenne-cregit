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
name|table_primitives
operator|.
name|ClientTablePrimitives
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
name|table_primitives
operator|.
name|TablePrimitives
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
name|UnitDbAdapter
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
name|client
operator|.
name|ClientCase
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
name|sql
operator|.
name|Types
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
name|assertTrue
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TABLE_PRIMITIVES_PROJECT
argument_list|)
specifier|public
class|class
name|CayenneContextPrimitiveIT
extends|extends
name|ClientCase
block|{
annotation|@
name|Inject
specifier|private
name|CayenneContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|accessStackAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|private
name|TableHelper
name|tTablePrimitives
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|testSetUp
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|bool
init|=
name|accessStackAdapter
operator|.
name|supportsBoolean
argument_list|()
condition|?
name|Types
operator|.
name|BOOLEAN
else|:
name|Types
operator|.
name|INTEGER
decl_stmt|;
name|tTablePrimitives
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"TABLE_PRIMITIVES"
argument_list|)
expr_stmt|;
name|tTablePrimitives
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"BOOLEAN_COLUMN"
argument_list|,
literal|"INT_COLUMN"
argument_list|)
operator|.
name|setColumnTypes
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|bool
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createTwoPrimitivesDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tTablePrimitives
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
name|accessStackAdapter
operator|.
name|supportsBoolean
argument_list|()
condition|?
literal|true
else|:
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|tTablePrimitives
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
name|accessStackAdapter
operator|.
name|supportsBoolean
argument_list|()
condition|?
literal|false
else|:
literal|0
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectPrimitives
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoPrimitivesDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientTablePrimitives
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addOrdering
argument_list|(
literal|"db:"
operator|+
name|TablePrimitives
operator|.
name|ID_PK_COLUMN
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ClientTablePrimitives
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isBooleanColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|results
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|isBooleanColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIntColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|results
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getIntColumn
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCommitChangesPrimitives
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientTablePrimitives
name|object
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientTablePrimitives
operator|.
name|class
argument_list|)
decl_stmt|;
name|object
operator|.
name|setBooleanColumn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|object
operator|.
name|setIntColumn
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|tTablePrimitives
operator|.
name|getBoolean
argument_list|(
literal|"BOOLEAN_COLUMN"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|tTablePrimitives
operator|.
name|getInt
argument_list|(
literal|"INT_COLUMN"
argument_list|)
argument_list|)
expr_stmt|;
name|object
operator|.
name|setBooleanColumn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|object
operator|.
name|setIntColumn
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|tTablePrimitives
operator|.
name|getBoolean
argument_list|(
literal|"BOOLEAN_COLUMN"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|tTablePrimitives
operator|.
name|getInt
argument_list|(
literal|"INT_COLUMN"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

