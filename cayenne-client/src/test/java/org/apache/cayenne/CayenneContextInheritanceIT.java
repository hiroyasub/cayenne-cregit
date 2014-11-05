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
name|mt
operator|.
name|ClientMtTable1
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
name|mt
operator|.
name|ClientMtTable1Subclass1
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ClientCase
operator|.
name|MULTI_TIER_PROJECT
argument_list|)
specifier|public
class|class
name|CayenneContextInheritanceIT
extends|extends
name|ClientCase
block|{
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|CayenneContext
name|context
decl_stmt|;
specifier|private
name|TableHelper
name|tMtTable1
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
literal|"MT_TABLE2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_TABLE1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"MT_TABLE1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|setColumns
argument_list|(
literal|"TABLE1_ID"
argument_list|,
literal|"GLOBAL_ATTRIBUTE1"
argument_list|,
literal|"SERVER_ATTRIBUTE1"
argument_list|,
literal|"SUBCLASS_ATTRIBUTE1"
argument_list|)
operator|.
name|setColumnTypes
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInsertSubclass
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientMtTable1Subclass1
name|object
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1Subclass1
operator|.
name|class
argument_list|)
decl_stmt|;
name|object
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"sub1"
argument_list|)
expr_stmt|;
name|object
operator|.
name|setServerAttribute1
argument_list|(
literal|"sa1"
argument_list|)
expr_stmt|;
name|object
operator|.
name|setSubclass1Attribute1
argument_list|(
literal|"suba1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tMtTable1
operator|.
name|getRowCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sub1"
argument_list|,
name|tMtTable1
operator|.
name|getString
argument_list|(
literal|"GLOBAL_ATTRIBUTE1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sa1"
argument_list|,
name|tMtTable1
operator|.
name|getString
argument_list|(
literal|"SERVER_ATTRIBUTE1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"suba1"
argument_list|,
name|tMtTable1
operator|.
name|getString
argument_list|(
literal|"SUBCLASS_ATTRIBUTE1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPerformQueryInheritanceLeaf
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"xxx"
argument_list|,
literal|"yyy"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"sub1"
argument_list|,
literal|"zzz"
argument_list|,
literal|"sa1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"1111"
argument_list|,
literal|"aaa"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1Subclass1
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ClientMtTable1Subclass1
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
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
name|assertEquals
argument_list|(
literal|"sa1"
argument_list|,
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getSubclass1Attribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPerformQueryInheritanceSuper
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"a"
argument_list|,
literal|"yyy"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"sub1"
argument_list|,
literal|"zzz"
argument_list|,
literal|"sa1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"z"
argument_list|,
literal|"aaa"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ClientMtTable1
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|checkedFields
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|objects
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Integer
name|id
init|=
operator|(
name|Integer
operator|)
name|objects
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
literal|"TABLE1_ID"
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|1
condition|)
block|{
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|objects
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|checkedFields
operator|++
expr_stmt|;
block|}
if|else if
condition|(
name|id
operator|==
literal|2
condition|)
block|{
name|assertEquals
argument_list|(
literal|"sa1"
argument_list|,
operator|(
operator|(
name|ClientMtTable1Subclass1
operator|)
name|objects
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|getSubclass1Attribute1
argument_list|()
argument_list|)
expr_stmt|;
name|checkedFields
operator|++
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|checkedFields
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPerformQueryWithQualifierInheritanceSuper
parameter_list|()
throws|throws
name|Exception
block|{
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"a"
argument_list|,
literal|"XX"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"sub1"
argument_list|,
literal|"XXA"
argument_list|,
literal|"sa1"
argument_list|)
expr_stmt|;
name|tMtTable1
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"z"
argument_list|,
literal|"MM"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
name|ClientMtTable1
operator|.
name|SERVER_ATTRIBUTE1_PROPERTY
argument_list|,
literal|"X%"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ClientMtTable1
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|checkedFields
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|objects
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Integer
name|id
init|=
operator|(
name|Integer
operator|)
name|objects
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
literal|"TABLE1_ID"
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|1
condition|)
block|{
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|objects
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|checkedFields
operator|++
expr_stmt|;
block|}
if|else if
condition|(
name|id
operator|==
literal|2
condition|)
block|{
name|assertEquals
argument_list|(
literal|"sa1"
argument_list|,
operator|(
operator|(
name|ClientMtTable1Subclass1
operator|)
name|objects
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|getSubclass1Attribute1
argument_list|()
argument_list|)
expr_stmt|;
name|checkedFields
operator|++
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|checkedFields
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

