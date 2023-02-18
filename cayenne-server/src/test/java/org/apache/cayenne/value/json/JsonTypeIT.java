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
name|value
operator|.
name|json
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
name|access
operator|.
name|DataContext
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
name|SelectById
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
name|json
operator|.
name|JsonOther
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
name|json
operator|.
name|JsonVarchar
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
name|apache
operator|.
name|cayenne
operator|.
name|value
operator|.
name|Json
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|JSON_PROJECT
argument_list|)
specifier|public
class|class
name|JsonTypeIT
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
specifier|private
name|TableHelper
name|tJsonVarchar
decl_stmt|;
specifier|private
name|TableHelper
name|tJsonOther
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
name|tJsonVarchar
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"JSON_VARCHAR"
argument_list|)
expr_stmt|;
name|tJsonVarchar
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"DATA"
argument_list|)
expr_stmt|;
name|tJsonOther
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"JSON_OTHER"
argument_list|)
expr_stmt|;
name|tJsonOther
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"DATA"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJson
parameter_list|()
block|{
name|String
name|jsonString
init|=
literal|"{\"id\": 1, \"property\": \"value\"}"
decl_stmt|;
name|testJsonVarchar
argument_list|(
name|jsonString
argument_list|)
expr_stmt|;
if|if
condition|(
name|unitDbAdapter
operator|.
name|supportsJsonType
argument_list|()
condition|)
block|{
name|testJsonOther
argument_list|(
name|jsonString
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"gg ez"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testJsonOther
parameter_list|(
name|String
name|jsonString
parameter_list|)
block|{
name|JsonOther
name|jsonInsert
init|=
name|context
operator|.
name|newObject
argument_list|(
name|JsonOther
operator|.
name|class
argument_list|)
decl_stmt|;
name|jsonInsert
operator|.
name|setData
argument_list|(
operator|new
name|Json
argument_list|(
name|jsonString
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|JsonOther
name|jsonSelect
init|=
name|context
operator|.
name|selectOne
argument_list|(
name|SelectById
operator|.
name|query
argument_list|(
name|JsonOther
operator|.
name|class
argument_list|,
name|jsonInsert
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|jsonInsert
operator|.
name|getData
argument_list|()
argument_list|,
name|jsonSelect
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testJsonVarchar
parameter_list|(
name|String
name|jsonString
parameter_list|)
block|{
name|JsonVarchar
name|jsonInsert
init|=
name|context
operator|.
name|newObject
argument_list|(
name|JsonVarchar
operator|.
name|class
argument_list|)
decl_stmt|;
name|jsonInsert
operator|.
name|setData
argument_list|(
operator|new
name|Json
argument_list|(
name|jsonString
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|JsonVarchar
name|jsonSelect
init|=
name|context
operator|.
name|selectOne
argument_list|(
name|SelectById
operator|.
name|query
argument_list|(
name|JsonVarchar
operator|.
name|class
argument_list|,
name|jsonInsert
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|jsonInsert
operator|.
name|getData
argument_list|()
argument_list|,
name|jsonSelect
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

