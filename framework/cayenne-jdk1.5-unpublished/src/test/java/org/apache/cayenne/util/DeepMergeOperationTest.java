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
name|util
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
name|cayenne
operator|.
name|DataObjectUtils
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
name|PersistenceState
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
name|reflect
operator|.
name|ClassDescriptor
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
name|DeepMergeOperation
import|;
end_import

begin_class
specifier|public
class|class
name|DeepMergeOperationTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testDeepMergeNonExistent
parameter_list|()
block|{
name|ClassDescriptor
name|d
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|DataContext
name|context1
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"AAA"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|DeepMergeOperation
name|op
init|=
operator|new
name|DeepMergeOperation
argument_list|(
name|context1
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|Artist
name|a2
init|=
operator|(
name|Artist
operator|)
name|op
operator|.
name|merge
argument_list|(
name|a
argument_list|,
name|d
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|a2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|getArtistName
argument_list|()
argument_list|,
name|a2
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testDeepMergeModified
parameter_list|()
block|{
name|ClassDescriptor
name|d
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|DataContext
name|context1
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"AAA"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context1
argument_list|,
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"BBB"
argument_list|)
expr_stmt|;
name|DeepMergeOperation
name|op
init|=
operator|new
name|DeepMergeOperation
argument_list|(
name|context1
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|Artist
name|a2
init|=
operator|(
name|Artist
operator|)
name|op
operator|.
name|merge
argument_list|(
name|a
argument_list|,
name|d
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|a2
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|a2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BBB"
argument_list|,
name|a2
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

