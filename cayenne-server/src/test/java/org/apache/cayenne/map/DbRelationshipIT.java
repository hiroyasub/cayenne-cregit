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
name|map
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
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
name|assertSame
import|;
end_import

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
name|DbRelationshipIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
specifier|protected
name|DbEntity
name|artistEnt
decl_stmt|;
specifier|protected
name|DbEntity
name|paintingEnt
decl_stmt|;
specifier|protected
name|DbEntity
name|galleryEnt
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
name|artistEnt
operator|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
expr_stmt|;
name|paintingEnt
operator|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|galleryEnt
operator|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
literal|"GALLERY"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSrcFkSnapshotWithTargetSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Integer
name|id
init|=
operator|new
name|Integer
argument_list|(
literal|44
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"GALLERY_ID"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|DbRelationship
name|dbRel
init|=
name|galleryEnt
operator|.
name|getRelationship
argument_list|(
literal|"paintingArray"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|targetMap
init|=
name|dbRel
operator|.
name|getReverseRelationship
argument_list|()
operator|.
name|srcFkSnapshotWithTargetSnapshot
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|targetMap
operator|.
name|get
argument_list|(
literal|"GALLERY_ID"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetReverseRelationship1
parameter_list|()
throws|throws
name|Exception
block|{
comment|// start with "to many"
name|DbRelationship
name|r1
init|=
name|artistEnt
operator|.
name|getRelationship
argument_list|(
literal|"paintingArray"
argument_list|)
decl_stmt|;
name|DbRelationship
name|r2
init|=
name|r1
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|paintingEnt
operator|.
name|getRelationship
argument_list|(
literal|"toArtist"
argument_list|)
argument_list|,
name|r2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetReverseRelationship2
parameter_list|()
throws|throws
name|Exception
block|{
comment|// start with "to one"
name|DbRelationship
name|r1
init|=
name|paintingEnt
operator|.
name|getRelationship
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
name|DbRelationship
name|r2
init|=
name|r1
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|artistEnt
operator|.
name|getRelationship
argument_list|(
literal|"paintingArray"
argument_list|)
argument_list|,
name|r2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetReverseRelationshipToSelf
parameter_list|()
block|{
comment|// assemble mockup entity
name|DataMap
name|namespace
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|DbEntity
name|e
init|=
operator|new
name|DbEntity
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|namespace
operator|.
name|addDbEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|DbRelationship
name|rforward
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"rforward"
argument_list|)
decl_stmt|;
name|e
operator|.
name|addRelationship
argument_list|(
name|rforward
argument_list|)
expr_stmt|;
name|rforward
operator|.
name|setSourceEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|rforward
operator|.
name|setTargetEntityName
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|rforward
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
comment|// add a joins
name|e
operator|.
name|addAttribute
argument_list|(
operator|new
name|DbAttribute
argument_list|(
literal|"a1"
argument_list|)
argument_list|)
expr_stmt|;
name|e
operator|.
name|addAttribute
argument_list|(
operator|new
name|DbAttribute
argument_list|(
literal|"a2"
argument_list|)
argument_list|)
expr_stmt|;
name|rforward
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|rforward
argument_list|,
literal|"a1"
argument_list|,
literal|"a2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|rforward
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
comment|// create reverse
name|DbRelationship
name|rback
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"rback"
argument_list|)
decl_stmt|;
name|e
operator|.
name|addRelationship
argument_list|(
name|rback
argument_list|)
expr_stmt|;
name|rback
operator|.
name|setSourceEntity
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|rback
operator|.
name|setTargetEntityName
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|rforward
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
comment|// create reverse join
name|rback
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|rback
argument_list|,
literal|"a2"
argument_list|,
literal|"a1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|rback
argument_list|,
name|rforward
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|rforward
argument_list|,
name|rback
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

