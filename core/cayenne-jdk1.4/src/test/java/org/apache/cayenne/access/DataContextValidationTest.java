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
name|Artist
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
name|Painting
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
name|unit
operator|.
name|util
operator|.
name|ValidationDelegate
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
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DataContextValidationTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testValidatingObjectsOnCommitProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setValidatingObjectsOnCommit
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|isValidatingObjectsOnCommit
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setValidatingObjectsOnCommit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|isValidatingObjectsOnCommit
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidatingObjectsOnCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
comment|// test that validation is called properly
name|context
operator|.
name|setValidatingObjectsOnCommit
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|a1
operator|.
name|isValidateForSaveCalled
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setValidatingObjectsOnCommit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|a2
operator|.
name|isValidateForSaveCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidationModifyingContext
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|ValidationDelegate
name|delegate
init|=
operator|new
name|ValidationDelegate
argument_list|()
block|{
specifier|public
name|void
name|validateForSave
parameter_list|(
name|Object
name|object
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|Artist
name|a
init|=
operator|(
name|Artist
operator|)
name|object
decl_stmt|;
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|a
operator|.
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p
operator|.
name|setPaintingTitle
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setToArtist
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setValidatingObjectsOnCommit
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setValidationDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
comment|// add another artist to ensure that modifying context works when more than one
comment|// object is committed
name|Artist
name|a2
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a2
operator|.
name|setValidationDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
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

