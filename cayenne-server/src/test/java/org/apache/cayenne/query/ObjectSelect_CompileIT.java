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
name|query
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
name|CayenneDataObject
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
name|CayenneRuntimeException
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
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|instanceOf
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
name|assertArrayEquals
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
name|assertThat
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
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|ObjectSelect_CompileIT
extends|extends
name|ServerCase
block|{
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
name|testCreateReplacementQuery_Bare
parameter_list|()
block|{
comment|// use only a minimal number of attributes, with null/defaults for
comment|// everything else
name|ObjectSelect
argument_list|<
name|Artist
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|Query
name|replacement
init|=
name|q
operator|.
name|createReplacementQuery
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|replacement
argument_list|,
name|instanceOf
argument_list|(
name|SelectQuery
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|selectQuery
init|=
operator|(
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
operator|)
name|replacement
decl_stmt|;
name|assertNull
argument_list|(
name|selectQuery
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|selectQuery
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|selectQuery
operator|.
name|getOrderings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|selectQuery
operator|.
name|getPrefetchTree
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|NO_CACHE
argument_list|,
name|selectQuery
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|selectQuery
operator|.
name|getCacheGroups
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|selectQuery
operator|.
name|getFetchLimit
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|selectQuery
operator|.
name|getFetchOffset
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|selectQuery
operator|.
name|getPageSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|selectQuery
operator|.
name|getStatementFetchSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateReplacementQuery_Full
parameter_list|()
block|{
comment|// add all possible attributes to the query and make sure they got
comment|// propagated
name|ObjectSelect
argument_list|<
name|Artist
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|exp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|eq
argument_list|(
literal|"me"
argument_list|)
argument_list|)
operator|.
name|orderBy
argument_list|(
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|asc
argument_list|()
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|desc
argument_list|()
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|joint
argument_list|()
argument_list|)
operator|.
name|localCache
argument_list|(
literal|"cg2"
argument_list|,
literal|"cg1"
argument_list|)
operator|.
name|limit
argument_list|(
literal|46
argument_list|)
operator|.
name|offset
argument_list|(
literal|9
argument_list|)
operator|.
name|pageSize
argument_list|(
literal|6
argument_list|)
operator|.
name|statementFetchSize
argument_list|(
literal|789
argument_list|)
decl_stmt|;
name|Query
name|replacement
init|=
name|q
operator|.
name|createReplacementQuery
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|replacement
argument_list|,
name|instanceOf
argument_list|(
name|SelectQuery
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|selectQuery
init|=
operator|(
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
operator|)
name|replacement
decl_stmt|;
name|assertEquals
argument_list|(
literal|"artistName = \"me\""
argument_list|,
name|selectQuery
operator|.
name|getQualifier
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|selectQuery
operator|.
name|getOrderings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
name|Artist
operator|.
name|DATE_OF_BIRTH
operator|.
name|asc
argument_list|()
block|,
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|desc
argument_list|()
block|}
argument_list|,
name|selectQuery
operator|.
name|getOrderings
argument_list|()
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|prefetch
init|=
name|selectQuery
operator|.
name|getPrefetchTree
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|prefetch
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|prefetch
operator|.
name|getChildren
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|childPrefetch
init|=
name|prefetch
operator|.
name|getNode
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|getName
argument_list|()
argument_list|,
name|childPrefetch
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|,
name|childPrefetch
operator|.
name|getSemantics
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|,
name|selectQuery
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"cg2"
block|,
literal|"cg1"
block|}
argument_list|,
name|selectQuery
operator|.
name|getCacheGroups
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|46
argument_list|,
name|selectQuery
operator|.
name|getFetchLimit
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|selectQuery
operator|.
name|getFetchOffset
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|selectQuery
operator|.
name|getPageSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|789
argument_list|,
name|selectQuery
operator|.
name|getStatementFetchSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateReplacementQuery_RootClass
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|Artist
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|SelectQuery
name|qr
init|=
operator|(
name|SelectQuery
operator|)
name|q
operator|.
name|createReplacementQuery
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|qr
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|qr
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateReplacementQuery_RootDataRow
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|SelectQuery
name|qr
init|=
operator|(
name|SelectQuery
operator|)
name|q
operator|.
name|createReplacementQuery
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|qr
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|qr
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateReplacementQuery_RootDbEntity
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|dbQuery
argument_list|(
literal|"ARTIST"
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|SelectQuery
name|qr
init|=
operator|(
name|SelectQuery
operator|)
name|q
operator|.
name|createReplacementQuery
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|resolver
operator|.
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
argument_list|,
name|qr
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|qr
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateReplacementQuery_RootObjEntity
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|CayenneDataObject
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|CayenneDataObject
operator|.
name|class
argument_list|,
literal|"Artist"
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|SelectQuery
name|qr
init|=
operator|(
name|SelectQuery
operator|)
name|q
operator|.
name|createReplacementQuery
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|,
name|qr
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|qr
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CayenneRuntimeException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testCreateReplacementQuery_RootAbscent
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|entityName
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|q
operator|.
name|createReplacementQuery
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateReplacementQuery_DataRows
parameter_list|()
block|{
name|ObjectSelect
argument_list|<
name|Artist
argument_list|>
name|q
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|SelectQuery
name|selectQuery1
init|=
operator|(
name|SelectQuery
operator|)
name|q
operator|.
name|createReplacementQuery
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|selectQuery1
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|fetchDataRows
argument_list|()
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|SelectQuery
name|selectQuery2
init|=
operator|(
name|SelectQuery
operator|)
name|q
operator|.
name|createReplacementQuery
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|selectQuery2
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

