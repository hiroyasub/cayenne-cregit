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
name|access
operator|.
name|DataNode
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
name|testdo
operator|.
name|testmap
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|CayenneProjects
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
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  * This test case ensures that PK pre-generated for the entity manually before commit is  * used during commit as well.  */
end_comment

begin_comment
comment|// TODO: 1/16/2006 - the algorithm used to generate the PK may be included in
end_comment

begin_comment
comment|// DataObjectUtils to pull the PK on demand. A caveat - we need to analyze DataObject in
end_comment

begin_comment
comment|// question to see if a PK is numeric and not propagated.
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|PregeneratedPKIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testLongPk
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"XXX"
argument_list|)
expr_stmt|;
name|updateId
argument_list|(
name|context
argument_list|,
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|pk
init|=
name|a
operator|.
name|getObjectId
argument_list|()
operator|.
name|getReplacementIdMap
argument_list|()
operator|.
name|get
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pk
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pk
argument_list|,
name|Cayenne
operator|.
name|longPKForObject
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Object
name|pkAfterCommit
init|=
name|a
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|pk
argument_list|,
name|pkAfterCommit
argument_list|)
expr_stmt|;
block|}
name|void
name|updateId
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|ObjectId
name|id
parameter_list|)
throws|throws
name|Exception
block|{
name|DbEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
decl_stmt|;
name|DataNode
name|node
init|=
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|lookupDataNode
argument_list|(
name|entity
operator|.
name|getDataMap
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|pk
init|=
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|generatePk
argument_list|(
name|node
argument_list|,
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|id
operator|.
name|getReplacementIdMap
argument_list|()
operator|.
name|put
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
name|pk
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

