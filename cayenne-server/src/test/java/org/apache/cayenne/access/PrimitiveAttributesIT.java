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
name|ObjectSelect
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
name|primitive
operator|.
name|PrimitivesTestEntity
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
name|Test
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
name|PRIMITIVE_PROJECT
argument_list|)
specifier|public
class|class
name|PrimitiveAttributesIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|unitDbAdapter
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testCommit
parameter_list|()
block|{
name|PrimitivesTestEntity
name|e
init|=
name|context
operator|.
name|newObject
argument_list|(
name|PrimitivesTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|e
operator|.
name|setBooleanColumn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|e
operator|.
name|setIntColumn
argument_list|(
literal|88
argument_list|)
expr_stmt|;
name|e
operator|.
name|setCharColumn
argument_list|(
literal|'B'
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelect
parameter_list|()
throws|throws
name|Exception
block|{
name|TableHelper
name|tPrimitives
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PRIMITIVES_TEST"
argument_list|)
decl_stmt|;
name|tPrimitives
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"BOOLEAN_COLUMN"
argument_list|,
literal|"INT_COLUMN"
argument_list|,
literal|"CHAR_COLUMN"
argument_list|)
expr_stmt|;
name|tPrimitives
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|true
argument_list|,
operator|-
literal|100
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
literal|'a'
argument_list|)
argument_list|)
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
literal|'~'
argument_list|)
argument_list|)
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|true
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
literal|'Z'
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PrimitivesTestEntity
argument_list|>
name|result
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|PrimitivesTestEntity
operator|.
name|class
argument_list|)
operator|.
name|orderBy
argument_list|(
name|PrimitivesTestEntity
operator|.
name|INT_COLUMN
operator|.
name|asc
argument_list|()
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|100
argument_list|,
name|result
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
literal|'a'
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCharColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
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
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|result
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
name|assertEquals
argument_list|(
literal|'~'
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getCharColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|result
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
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getIntColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'Z'
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getCharColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|isBooleanColumn
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

