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
name|CapsStrategy
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
name|enum_test
operator|.
name|Enum1
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
name|enum_test
operator|.
name|EnumEntity
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
name|enum_test
operator|.
name|EnumEntity2
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
name|enum_test
operator|.
name|EnumEntity3
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
name|assertSame
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|ENUM_PROJECT
argument_list|)
specifier|public
class|class
name|EnumIT
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
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|private
name|void
name|createDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|TableHelper
name|tEnumEntity
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ENUM_ENTITY"
argument_list|)
decl_stmt|;
name|tEnumEntity
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"ENUM_ATTRIBUTE"
argument_list|)
expr_stmt|;
name|tEnumEntity
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|tEnumEntity
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInsert
parameter_list|()
block|{
name|EnumEntity
name|e
init|=
name|context
operator|.
name|newObject
argument_list|(
name|EnumEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|e
operator|.
name|setEnumAttribute
argument_list|(
name|Enum1
operator|.
name|one
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
name|testObjectSelect
parameter_list|()
throws|throws
name|Exception
block|{
name|createDataSet
argument_list|()
expr_stmt|;
name|EnumEntity
name|e
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|EnumEntity
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|EnumEntity
operator|.
name|ENUM_ATTRIBUTE
operator|.
name|eq
argument_list|(
name|Enum1
operator|.
name|one
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Enum1
operator|.
name|one
argument_list|,
name|e
operator|.
name|getEnumAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQLTemplate
parameter_list|()
throws|throws
name|Exception
block|{
name|createDataSet
argument_list|()
expr_stmt|;
name|SQLTemplate
name|q
init|=
operator|new
name|SQLTemplate
argument_list|(
name|EnumEntity
operator|.
name|class
argument_list|,
literal|"SELECT * FROM ENUM_ENTITY WHERE ENUM_ATTRIBUTE = 'one'"
argument_list|)
decl_stmt|;
name|q
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
expr_stmt|;
name|EnumEntity
name|e
init|=
operator|(
name|EnumEntity
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|q
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Enum1
operator|.
name|one
argument_list|,
name|e
operator|.
name|getEnumAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createObjectWithEnumQualifier
parameter_list|()
block|{
name|EnumEntity2
name|test
init|=
name|context
operator|.
name|newObject
argument_list|(
name|EnumEntity2
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Enum1
operator|.
name|two
argument_list|,
name|test
operator|.
name|getEnumAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEnumMappedToChar
parameter_list|()
block|{
name|EnumEntity3
name|enumEntity3
init|=
name|context
operator|.
name|newObject
argument_list|(
name|EnumEntity3
operator|.
name|class
argument_list|)
decl_stmt|;
name|enumEntity3
operator|.
name|setEnumAttribute
argument_list|(
name|Enum1
operator|.
name|two
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|EnumEntity3
argument_list|>
name|enumEntity3s
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|EnumEntity3
operator|.
name|class
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|enumEntity3s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Enum1
operator|.
name|two
argument_list|,
name|enumEntity3s
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEnumAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

