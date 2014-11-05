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
name|cayenne
operator|.
name|DataRow
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
name|DataMap
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
name|map
operator|.
name|EntityResolver
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
name|ObjEntity
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
name|MockQueryMetadata
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
name|PrefetchTreeNode
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
name|QueryMetadata
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
name|testdo
operator|.
name|testmap
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
name|testdo
operator|.
name|testmap
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
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|List
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
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|PrefetchProcessorTreeBuilderIT
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
name|Inject
specifier|private
name|EntityResolver
name|resolver
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testBuildTreeNoPrefetches
parameter_list|()
block|{
specifier|final
name|ClassDescriptor
name|descriptor
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|dataRows
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|dataRows
operator|.
name|add
argument_list|(
operator|new
name|DataRow
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|dataRows
operator|.
name|add
argument_list|(
operator|new
name|DataRow
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|QueryMetadata
name|metadata
init|=
operator|new
name|MockQueryMetadata
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
block|{
return|return
name|descriptor
return|;
block|}
annotation|@
name|Override
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|()
block|{
return|return
name|descriptor
operator|.
name|getEntity
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
return|return
name|getObjEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|getObjEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isRefreshingObjects
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isResolvingInherited
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|PrefetchTreeNode
name|tree
init|=
operator|new
name|PrefetchTreeNode
argument_list|()
decl_stmt|;
name|HierarchicalObjectResolver
name|resolver
init|=
operator|new
name|HierarchicalObjectResolver
argument_list|(
name|context
argument_list|,
name|metadata
argument_list|)
decl_stmt|;
name|PrefetchProcessorTreeBuilder
name|builder
init|=
operator|new
name|PrefetchProcessorTreeBuilder
argument_list|(
name|resolver
argument_list|,
name|dataRows
argument_list|,
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|PrefetchProcessorNode
name|processingTree
init|=
name|builder
operator|.
name|buildTree
argument_list|(
name|tree
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|processingTree
operator|.
name|getChildren
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|processingTree
operator|.
name|isPhantom
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|processingTree
operator|.
name|isPartitionedByParent
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|processingTree
operator|.
name|isDisjointPrefetch
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|dataRows
argument_list|,
name|processingTree
operator|.
name|getDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|descriptor
operator|.
name|getEntity
argument_list|()
argument_list|,
name|processingTree
operator|.
name|getResolver
argument_list|()
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|processingTree
operator|.
name|getIncoming
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildTreeWithPrefetches
parameter_list|()
block|{
specifier|final
name|ClassDescriptor
name|descriptor
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|ObjEntity
name|e2
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|ObjEntity
name|e3
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
literal|"Gallery"
argument_list|)
decl_stmt|;
name|ObjEntity
name|e4
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
literal|"Exhibit"
argument_list|)
decl_stmt|;
name|ObjEntity
name|e5
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
literal|"ArtistExhibit"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|mainRows
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|extraRows
init|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|PrefetchTreeNode
name|tree
init|=
operator|new
name|PrefetchTreeNode
argument_list|()
decl_stmt|;
name|tree
operator|.
name|addPath
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|)
operator|.
name|setPhantom
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|tree
operator|.
name|addPath
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
operator|+
literal|"."
operator|+
name|Painting
operator|.
name|TO_GALLERY_PROPERTY
operator|+
literal|"."
operator|+
name|Gallery
operator|.
name|EXHIBIT_ARRAY_PROPERTY
argument_list|)
operator|.
name|setPhantom
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|tree
operator|.
name|addPath
argument_list|(
name|Artist
operator|.
name|ARTIST_EXHIBIT_ARRAY_PROPERTY
argument_list|)
operator|.
name|setPhantom
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|QueryMetadata
name|metadata
init|=
operator|new
name|MockQueryMetadata
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ClassDescriptor
name|getClassDescriptor
parameter_list|()
block|{
return|return
name|descriptor
return|;
block|}
annotation|@
name|Override
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|()
block|{
return|return
name|descriptor
operator|.
name|getEntity
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
return|return
name|getObjEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|getObjEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isRefreshingObjects
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isResolvingInherited
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|HierarchicalObjectResolver
name|resolver
init|=
operator|new
name|HierarchicalObjectResolver
argument_list|(
name|context
argument_list|,
name|metadata
argument_list|)
decl_stmt|;
name|PrefetchProcessorTreeBuilder
name|builder
init|=
operator|new
name|PrefetchProcessorTreeBuilder
argument_list|(
name|resolver
argument_list|,
name|mainRows
argument_list|,
name|extraRows
argument_list|)
decl_stmt|;
name|PrefetchProcessorNode
name|n1
init|=
name|builder
operator|.
name|buildTree
argument_list|(
name|tree
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|mainRows
argument_list|,
name|n1
operator|.
name|getDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|descriptor
operator|.
name|getEntity
argument_list|()
argument_list|,
name|n1
operator|.
name|getResolver
argument_list|()
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|PrefetchProcessorNode
name|n2
init|=
operator|(
name|PrefetchProcessorNode
operator|)
name|n1
operator|.
name|getNode
argument_list|(
literal|"paintingArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|n2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e2
argument_list|,
name|n2
operator|.
name|getResolver
argument_list|()
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|n2
operator|.
name|isPhantom
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|n2
operator|.
name|isPartitionedByParent
argument_list|()
argument_list|)
expr_stmt|;
name|PrefetchProcessorNode
name|n3
init|=
operator|(
name|PrefetchProcessorNode
operator|)
name|n1
operator|.
name|getNode
argument_list|(
literal|"paintingArray.toGallery"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|n3
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e3
argument_list|,
name|n3
operator|.
name|getResolver
argument_list|()
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|n3
operator|.
name|isPhantom
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|n3
operator|.
name|isPartitionedByParent
argument_list|()
argument_list|)
expr_stmt|;
name|PrefetchProcessorNode
name|n4
init|=
operator|(
name|PrefetchProcessorNode
operator|)
name|n1
operator|.
name|getNode
argument_list|(
literal|"paintingArray.toGallery.exhibitArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|n4
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e4
argument_list|,
name|n4
operator|.
name|getResolver
argument_list|()
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|n4
operator|.
name|isPhantom
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|n4
operator|.
name|isPartitionedByParent
argument_list|()
argument_list|)
expr_stmt|;
name|PrefetchProcessorNode
name|n5
init|=
operator|(
name|PrefetchProcessorNode
operator|)
name|n1
operator|.
name|getNode
argument_list|(
literal|"artistExhibitArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|n5
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e5
argument_list|,
name|n5
operator|.
name|getResolver
argument_list|()
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|n5
operator|.
name|isPhantom
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|n5
operator|.
name|isPartitionedByParent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

