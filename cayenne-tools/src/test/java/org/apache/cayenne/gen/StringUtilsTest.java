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
name|gen
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
specifier|public
class|class
name|StringUtilsTest
block|{
specifier|protected
name|StringUtils
name|stringUtils
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
name|stringUtils
operator|=
operator|new
name|StringUtils
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|stringUtils
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPluralize
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"Words"
argument_list|,
name|stringUtils
operator|.
name|pluralize
argument_list|(
literal|"Word"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Statuses"
argument_list|,
name|stringUtils
operator|.
name|pluralize
argument_list|(
literal|"Status"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Indexes"
argument_list|,
name|stringUtils
operator|.
name|pluralize
argument_list|(
literal|"Index"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Factories"
argument_list|,
name|stringUtils
operator|.
name|pluralize
argument_list|(
literal|"Factory"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|stringUtils
operator|.
name|pluralize
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|stringUtils
operator|.
name|pluralize
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCapitalizedAsConstant1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expected
init|=
literal|"LAST_NAME"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|stringUtils
operator|.
name|capitalizedAsConstant
argument_list|(
literal|"LastName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCapitalizedAsConstant2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expected
init|=
literal|"A_CLASS"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|stringUtils
operator|.
name|capitalizedAsConstant
argument_list|(
literal|"aClass"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCapitalizedAsConstant3
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expected
init|=
literal|"VAR_A"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|stringUtils
operator|.
name|capitalizedAsConstant
argument_list|(
literal|"varA"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCapitalizedAsConstant4
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expected
init|=
literal|"LAST_NAME"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|stringUtils
operator|.
name|capitalizedAsConstant
argument_list|(
literal|"LAST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCapitalizedAsConstant5
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expected
init|=
literal|"ABC_A"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|stringUtils
operator|.
name|capitalizedAsConstant
argument_list|(
literal|"abc_A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCapitalizedAsConstant6
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expected
init|=
literal|"A123"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|stringUtils
operator|.
name|capitalizedAsConstant
argument_list|(
literal|"a123"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCapitalizedAsConstant7
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expected
init|=
literal|"AB_CDEF"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|stringUtils
operator|.
name|capitalizedAsConstant
argument_list|(
literal|"abCDEF"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCapitalizedAsConstant8
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expected
init|=
literal|"AB_CE"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|stringUtils
operator|.
name|capitalizedAsConstant
argument_list|(
literal|"abCe"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStripGeneric
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"List"
argument_list|,
name|stringUtils
operator|.
name|stripGeneric
argument_list|(
literal|"List"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"List"
argument_list|,
name|stringUtils
operator|.
name|stripGeneric
argument_list|(
literal|"List<Integer>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"List"
argument_list|,
name|stringUtils
operator|.
name|stripGeneric
argument_list|(
literal|"List<List<Map<Integer,List<String>>>>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"List123"
argument_list|,
name|stringUtils
operator|.
name|stripGeneric
argument_list|(
literal|"List<List<Map<Integer,List<String>>>>123"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"List<Integer"
argument_list|,
name|stringUtils
operator|.
name|stripGeneric
argument_list|(
literal|"List<Integer"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

