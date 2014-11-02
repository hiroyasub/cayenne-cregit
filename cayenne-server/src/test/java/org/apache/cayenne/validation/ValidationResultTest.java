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
name|validation
package|;
end_package

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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ValidationResultTest
block|{
specifier|private
name|ValidationResult
name|res
decl_stmt|;
specifier|private
name|Object
name|obj1
decl_stmt|;
specifier|private
name|Object
name|obj2
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
name|obj1
operator|=
operator|new
name|Object
argument_list|()
expr_stmt|;
name|obj2
operator|=
operator|new
name|Object
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHasFailures
parameter_list|()
block|{
name|res
operator|=
operator|new
name|ValidationResult
argument_list|()
expr_stmt|;
name|res
operator|.
name|addFailure
argument_list|(
operator|new
name|BeanValidationFailure
argument_list|(
name|obj1
argument_list|,
literal|"obj1 1"
argument_list|,
literal|"mes obj1 1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|res
operator|.
name|hasFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|res
operator|.
name|hasFailures
argument_list|(
name|obj1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|res
operator|.
name|hasFailures
argument_list|(
name|obj2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetFailures
parameter_list|()
block|{
name|res
operator|=
operator|new
name|ValidationResult
argument_list|()
expr_stmt|;
name|res
operator|.
name|addFailure
argument_list|(
operator|new
name|BeanValidationFailure
argument_list|(
name|obj1
argument_list|,
literal|"obj1 1"
argument_list|,
literal|"mes obj1 1"
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|.
name|addFailure
argument_list|(
operator|new
name|BeanValidationFailure
argument_list|(
name|obj1
argument_list|,
literal|"obj1 1"
argument_list|,
literal|"mes obj1 1"
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|.
name|addFailure
argument_list|(
operator|new
name|BeanValidationFailure
argument_list|(
name|obj2
argument_list|,
literal|"obj1 1"
argument_list|,
literal|"mes obj1 1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|res
operator|.
name|getFailures
argument_list|(
name|obj1
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|res
operator|.
name|getFailures
argument_list|(
name|obj2
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|res
operator|.
name|getFailures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmpty
parameter_list|()
block|{
name|res
operator|=
operator|new
name|ValidationResult
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|res
operator|.
name|hasFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|res
operator|.
name|hasFailures
argument_list|(
name|obj1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|res
operator|.
name|hasFailures
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

