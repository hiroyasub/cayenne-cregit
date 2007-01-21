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
name|java
operator|.
name|util
operator|.
name|Date
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
name|Exhibit
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
name|Gallery
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
name|map
operator|.
name|DbAttribute
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
name|map
operator|.
name|DbEntity
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
name|validation
operator|.
name|BeanValidationFailure
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
name|CayenneDataObjectValidationTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testValidateForSaveMandatoryToOneMissing
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
name|Exhibit
name|exhibit
init|=
operator|(
name|Exhibit
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Exhibit
operator|.
name|class
argument_list|)
decl_stmt|;
name|exhibit
operator|.
name|setOpeningDate
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|exhibit
operator|.
name|setClosingDate
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|ValidationResult
name|result
init|=
operator|new
name|ValidationResult
argument_list|()
decl_stmt|;
name|exhibit
operator|.
name|validateForSave
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Validation of 'toGallery' should've failed."
argument_list|,
name|result
operator|.
name|hasFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|hasFailures
argument_list|(
name|exhibit
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|failures
init|=
name|result
operator|.
name|getFailures
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|failures
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|BeanValidationFailure
name|failure
init|=
operator|(
name|BeanValidationFailure
operator|)
name|failures
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Exhibit
operator|.
name|TO_GALLERY_PROPERTY
argument_list|,
name|failure
operator|.
name|getProperty
argument_list|()
argument_list|)
expr_stmt|;
comment|// fix the problem and see if it goes away
name|Gallery
name|gallery
init|=
operator|(
name|Gallery
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
decl_stmt|;
name|exhibit
operator|.
name|setToGallery
argument_list|(
name|gallery
argument_list|)
expr_stmt|;
name|result
operator|=
operator|new
name|ValidationResult
argument_list|()
expr_stmt|;
name|exhibit
operator|.
name|validateForSave
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"No failures expected: "
operator|+
name|result
argument_list|,
name|result
operator|.
name|hasFailures
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidateForSaveMandatoryAttributeMissing
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
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|ValidationResult
name|result
init|=
operator|new
name|ValidationResult
argument_list|()
decl_stmt|;
name|artist
operator|.
name|validateForSave
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Validation of 'artistName' should've failed."
argument_list|,
name|result
operator|.
name|hasFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|hasFailures
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|failures
init|=
name|result
operator|.
name|getFailures
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|failures
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|BeanValidationFailure
name|failure
init|=
operator|(
name|BeanValidationFailure
operator|)
name|failures
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
name|failure
operator|.
name|getProperty
argument_list|()
argument_list|)
expr_stmt|;
comment|// fix the problem and see if it goes away
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"aa"
argument_list|)
expr_stmt|;
name|result
operator|=
operator|new
name|ValidationResult
argument_list|()
expr_stmt|;
name|artist
operator|.
name|validateForSave
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|hasFailures
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidateForSaveAttributeTooLong
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
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|DbEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupDbEntity
argument_list|(
name|artist
argument_list|)
decl_stmt|;
name|int
name|len
init|=
operator|(
operator|(
name|DbAttribute
operator|)
name|entity
operator|.
name|getAttribute
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
operator|)
operator|.
name|getMaxLength
argument_list|()
decl_stmt|;
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|(
name|len
argument_list|)
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
name|len
operator|+
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
block|}
name|artist
operator|.
name|setArtistName
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ValidationResult
name|result
init|=
operator|new
name|ValidationResult
argument_list|()
decl_stmt|;
name|artist
operator|.
name|validateForSave
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|hasFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|hasFailures
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|failures
init|=
name|result
operator|.
name|getFailures
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|failures
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|BeanValidationFailure
name|failure
init|=
operator|(
name|BeanValidationFailure
operator|)
name|failures
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
name|failure
operator|.
name|getProperty
argument_list|()
argument_list|)
expr_stmt|;
comment|// fix the problem and see if it goes away
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"aa"
argument_list|)
expr_stmt|;
name|result
operator|=
operator|new
name|ValidationResult
argument_list|()
expr_stmt|;
name|artist
operator|.
name|validateForSave
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|hasFailures
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

