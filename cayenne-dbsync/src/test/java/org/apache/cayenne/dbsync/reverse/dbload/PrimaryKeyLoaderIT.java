begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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

begin_class
specifier|public
class|class
name|PrimaryKeyLoaderIT
extends|extends
name|BaseLoaderIT
block|{
annotation|@
name|Test
specifier|public
name|void
name|testPrimaryKeyLoad
parameter_list|()
throws|throws
name|Exception
block|{
name|createDbEntities
argument_list|()
expr_stmt|;
name|DbEntity
name|artist
init|=
name|getDbEntity
argument_list|(
name|nameForDb
argument_list|(
literal|"ARTIST"
argument_list|)
argument_list|)
decl_stmt|;
name|DbAttribute
name|artistId
init|=
operator|new
name|DbAttribute
argument_list|(
name|nameForDb
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
decl_stmt|;
name|DbAttribute
name|artistName
init|=
operator|new
name|DbAttribute
argument_list|(
name|nameForDb
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
decl_stmt|;
name|DbAttribute
name|artistId1
init|=
operator|new
name|DbAttribute
argument_list|(
name|nameForDb
argument_list|(
literal|"ARTIST_ID1"
argument_list|)
argument_list|)
decl_stmt|;
name|artist
operator|.
name|addAttribute
argument_list|(
name|artistId
argument_list|)
expr_stmt|;
name|artist
operator|.
name|addAttribute
argument_list|(
name|artistName
argument_list|)
expr_stmt|;
name|artist
operator|.
name|addAttribute
argument_list|(
name|artistId1
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artistId
operator|.
name|isPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artistName
operator|.
name|isPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artistId1
operator|.
name|isPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
name|PrimaryKeyLoader
name|loader
init|=
operator|new
name|PrimaryKeyLoader
argument_list|(
name|EMPTY_CONFIG
argument_list|,
operator|new
name|DefaultDbLoaderDelegate
argument_list|()
argument_list|)
decl_stmt|;
name|loader
operator|.
name|load
argument_list|(
name|connection
operator|.
name|getMetaData
argument_list|()
argument_list|,
name|store
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|artistId
operator|.
name|isPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artistId1
operator|.
name|isPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artistName
operator|.
name|isPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|pk
init|=
name|artist
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pk
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artistId
argument_list|,
name|pk
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

