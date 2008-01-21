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
name|Arrays
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
name|Painting1
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_comment
comment|/**  * Tests DataObjects with no reverse relationships.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|CDOMany2OneNoRevTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testNewAdd
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
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
literal|"Artist"
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|Painting1
name|p1
init|=
operator|(
name|Painting1
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Painting1"
argument_list|)
decl_stmt|;
name|p1
operator|.
name|setPaintingTitle
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
comment|// *** TESTING THIS ***
name|p1
operator|.
name|setToArtist
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|p1
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectId
name|aid
init|=
name|a1
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|ObjectId
name|pid
init|=
name|p1
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|a1
argument_list|,
name|p1
argument_list|)
argument_list|)
expr_stmt|;
name|Painting1
name|p2
init|=
operator|(
name|Painting1
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|pid
argument_list|)
decl_stmt|;
name|Artist
name|a2
init|=
name|p2
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|aid
argument_list|,
name|a2
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

