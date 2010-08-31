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
name|Arrays
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
name|testdo
operator|.
name|testmap
operator|.
name|ExtendedTypeEntity
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
name|StringET1
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
name|DataContextExtendedTypeOperationsTest
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
literal|"EXTENDED_TYPE_TEST"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testStoreExtendedType
parameter_list|()
block|{
name|ExtendedTypeEntity
name|e1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ExtendedTypeEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|e1
operator|.
name|setName
argument_list|(
operator|new
name|StringET1
argument_list|(
literal|"X"
argument_list|)
argument_list|)
expr_stmt|;
name|e1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SQLTemplate
name|checkQ
init|=
operator|new
name|SQLTemplate
argument_list|(
name|ExtendedTypeEntity
operator|.
name|class
argument_list|,
literal|"SELECT * FROM EXTENDED_TYPE_TEST WHERE NAME = 'X'"
argument_list|)
decl_stmt|;
name|checkQ
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|checkQ
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|e1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|checkQ
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInExpressionExtendedTypeArray
parameter_list|()
block|{
name|ExtendedTypeEntity
name|e1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ExtendedTypeEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|e1
operator|.
name|setName
argument_list|(
operator|new
name|StringET1
argument_list|(
literal|"X"
argument_list|)
argument_list|)
expr_stmt|;
name|ExtendedTypeEntity
name|e2
init|=
name|e1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|ExtendedTypeEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|e2
operator|.
name|setName
argument_list|(
operator|new
name|StringET1
argument_list|(
literal|"Y"
argument_list|)
argument_list|)
expr_stmt|;
name|ExtendedTypeEntity
name|e3
init|=
name|e1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|ExtendedTypeEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|e3
operator|.
name|setName
argument_list|(
operator|new
name|StringET1
argument_list|(
literal|"Z"
argument_list|)
argument_list|)
expr_stmt|;
name|e1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Expression
name|in
init|=
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
name|ExtendedTypeEntity
operator|.
name|NAME_PROPERTY
argument_list|,
operator|new
name|StringET1
argument_list|(
literal|"X"
argument_list|)
argument_list|,
operator|new
name|StringET1
argument_list|(
literal|"Y"
argument_list|)
argument_list|)
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ExtendedTypeEntity
operator|.
name|class
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInExpressionExtendedTypeList
parameter_list|()
block|{
name|ExtendedTypeEntity
name|e1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ExtendedTypeEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|e1
operator|.
name|setName
argument_list|(
operator|new
name|StringET1
argument_list|(
literal|"X"
argument_list|)
argument_list|)
expr_stmt|;
name|ExtendedTypeEntity
name|e2
init|=
name|e1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|ExtendedTypeEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|e2
operator|.
name|setName
argument_list|(
operator|new
name|StringET1
argument_list|(
literal|"Y"
argument_list|)
argument_list|)
expr_stmt|;
name|ExtendedTypeEntity
name|e3
init|=
name|e1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|ExtendedTypeEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|e3
operator|.
name|setName
argument_list|(
operator|new
name|StringET1
argument_list|(
literal|"Z"
argument_list|)
argument_list|)
expr_stmt|;
name|e1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Expression
name|in
init|=
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
name|ExtendedTypeEntity
operator|.
name|NAME_PROPERTY
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|StringET1
argument_list|(
literal|"X"
argument_list|)
argument_list|,
operator|new
name|StringET1
argument_list|(
literal|"Y"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ExtendedTypeEntity
operator|.
name|class
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

