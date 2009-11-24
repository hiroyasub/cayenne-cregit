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
name|art
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
name|art
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
name|unit
operator|.
name|CayenneCase
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
name|util
operator|.
name|Cayenne
import|;
end_import

begin_class
specifier|public
class|class
name|EnumTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testInsert
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
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
specifier|public
name|void
name|testSelectQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|EnumEntity
operator|.
name|class
argument_list|,
literal|"insert into ENUM_ENTITY (ID, ENUM_ATTRIBUTE) VALUES (1, 'two')"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|EnumEntity
operator|.
name|class
argument_list|,
literal|"insert into ENUM_ENTITY (ID, ENUM_ATTRIBUTE) VALUES (2, 'one')"
argument_list|)
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|EnumEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|EnumEntity
operator|.
name|ENUM_ATTRIBUTE_PROPERTY
argument_list|,
name|Enum1
operator|.
name|one
argument_list|)
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
specifier|public
name|void
name|testSQLTemplate
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|EnumEntity
operator|.
name|class
argument_list|,
literal|"insert into ENUM_ENTITY (ID, ENUM_ATTRIBUTE) VALUES (1, 'two')"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|EnumEntity
operator|.
name|class
argument_list|,
literal|"insert into ENUM_ENTITY (ID, ENUM_ATTRIBUTE) VALUES (2, 'one')"
argument_list|)
argument_list|)
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
block|}
end_class

end_unit

