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
name|testdo
operator|.
name|relationship
operator|.
name|MeaningfulFK
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
name|relationship
operator|.
name|RelationshipHelper
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
name|RelationshipCase
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|MeaningfulFKTest
extends|extends
name|RelationshipCase
block|{
specifier|public
name|void
name|testValidateForSave1
parameter_list|()
throws|throws
name|Exception
block|{
name|MeaningfulFK
name|testObject
init|=
name|createDataContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|MeaningfulFK
operator|.
name|class
argument_list|)
decl_stmt|;
name|ValidationResult
name|validation
init|=
operator|new
name|ValidationResult
argument_list|()
decl_stmt|;
name|testObject
operator|.
name|validateForSave
argument_list|(
name|validation
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Must fail validation due to missing required relationship"
argument_list|,
name|validation
operator|.
name|hasFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Must fail validation due to missing required relationship"
argument_list|,
literal|1
argument_list|,
name|validation
operator|.
name|getFailures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidateForSave2
parameter_list|()
throws|throws
name|Exception
block|{
name|MeaningfulFK
name|testObject
init|=
name|createDataContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|MeaningfulFK
operator|.
name|class
argument_list|)
decl_stmt|;
name|RelationshipHelper
name|related
init|=
name|testObject
operator|.
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|RelationshipHelper
operator|.
name|class
argument_list|)
decl_stmt|;
name|testObject
operator|.
name|setToRelationshipHelper
argument_list|(
name|related
argument_list|)
expr_stmt|;
name|ValidationResult
name|validation
init|=
operator|new
name|ValidationResult
argument_list|()
decl_stmt|;
name|testObject
operator|.
name|validateForSave
argument_list|(
name|validation
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|validation
operator|.
name|hasFailures
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

