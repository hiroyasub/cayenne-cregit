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
name|java
operator|.
name|sql
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
name|Collections
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Set
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
name|Gallery
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
name|DataObject
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
name|ObjectId
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
name|Persistent
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
name|ValueHolder
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionFactory
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
name|ObjAttribute
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
name|SQLTemplate
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

begin_comment
comment|/**  * Tests joint prefetch handling by Cayenne access stack.  */
end_comment

begin_class
specifier|public
class|class
name|JointPrefetchTest
extends|extends
name|CayenneCase
block|{
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteTestData
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testJointPrefetchDataRows
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testJointPrefetch1"
argument_list|)
expr_stmt|;
comment|// query with to-many joint prefetches
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
literal|"db:PAINTING_ID"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|q
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|List
name|rows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|rows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// row should contain columns from both entities minus those duplicated in a
comment|// join...
name|int
name|rowWidth
init|=
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
operator|+
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|rows
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataRow
name|row
init|=
operator|(
name|DataRow
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|""
operator|+
name|row
argument_list|,
name|rowWidth
argument_list|,
name|row
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// assert columns presence
name|assertTrue
argument_list|(
name|row
operator|+
literal|""
argument_list|,
name|row
operator|.
name|containsKey
argument_list|(
literal|"PAINTING_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|row
operator|+
literal|""
argument_list|,
name|row
operator|.
name|containsKey
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|row
operator|+
literal|""
argument_list|,
name|row
operator|.
name|containsKey
argument_list|(
literal|"GALLERY_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|row
operator|+
literal|""
argument_list|,
name|row
operator|.
name|containsKey
argument_list|(
literal|"PAINTING_TITLE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|row
operator|+
literal|""
argument_list|,
name|row
operator|.
name|containsKey
argument_list|(
literal|"ESTIMATED_PRICE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|row
operator|+
literal|""
argument_list|,
name|row
operator|.
name|containsKey
argument_list|(
literal|"toArtist.ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|row
operator|+
literal|""
argument_list|,
name|row
operator|.
name|containsKey
argument_list|(
literal|"toArtist.DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|testJointPrefetchSQLTemplate
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testJointPrefetch1"
argument_list|)
expr_stmt|;
comment|// correctly naming columns is the key..
name|SQLTemplate
name|q
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"SELECT distinct "
operator|+
literal|"#result('ESTIMATED_PRICE' 'BigDecimal' '' 'paintingArray.ESTIMATED_PRICE'), "
operator|+
literal|"#result('PAINTING_TITLE' 'String' '' 'paintingArray.PAINTING_TITLE'), "
operator|+
literal|"#result('GALLERY_ID' 'int' '' 'paintingArray.GALLERY_ID'), "
operator|+
literal|"#result('PAINTING_ID' 'int' '' 'paintingArray.PAINTING_ID'), "
operator|+
literal|"#result('ARTIST_NAME' 'String'), "
operator|+
literal|"#result('DATE_OF_BIRTH' 'java.util.Date'), "
operator|+
literal|"#result('t0.ARTIST_ID' 'int' '' 'ARTIST_ID') "
operator|+
literal|"FROM ARTIST t0, PAINTING t1 "
operator|+
literal|"WHERE t0.ARTIST_ID = t1.ARTIST_ID"
argument_list|)
decl_stmt|;
name|PrefetchTreeNode
name|prefetch
init|=
name|q
operator|.
name|addPrefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Default semantics for SQLTemplate is assumed to be joint."
argument_list|,
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|,
name|prefetch
operator|.
name|getSemantics
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|setFetchingDataRows
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
comment|// without OUTER join we will get fewer objects...
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Artist
name|a
init|=
operator|(
name|Artist
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|List
name|list
init|=
name|a
operator|.
name|getPaintingArray
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|list
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|Iterator
name|children
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|children
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|children
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|p
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// make sure properties are not null..
name|assertNotNull
argument_list|(
name|p
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
name|testJointPrefetchToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testJointPrefetch1"
argument_list|)
expr_stmt|;
comment|// query with to-many joint prefetches
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
literal|"db:PAINTING_ID"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Artist
name|target
init|=
name|p
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|target
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Tests that joined entities can have non-standard type mappings.      */
specifier|public
name|void
name|testJointPrefetchDataTypes
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prepare... can't load from XML, as it doesn't yet support dates..
name|SQLTemplate
name|artistSQL
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"insert into ARTIST (ARTIST_ID, ARTIST_NAME, DATE_OF_BIRTH) "
operator|+
literal|"values (33001, 'a1', #bind($date 'DATE'))"
argument_list|)
decl_stmt|;
name|artistSQL
operator|.
name|setParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"date"
argument_list|,
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|SQLTemplate
name|paintingSQL
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"INSERT INTO PAINTING (PAINTING_ID, PAINTING_TITLE, ARTIST_ID, ESTIMATED_PRICE) "
operator|+
literal|"VALUES (33001, 'p1', 33001, 1000)"
argument_list|)
decl_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
name|artistSQL
argument_list|)
expr_stmt|;
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
name|paintingSQL
argument_list|)
expr_stmt|;
comment|// test
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Painting
operator|.
name|TO_ARTIST_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|ObjEntity
name|artistE
init|=
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|ObjAttribute
name|dateOfBirth
init|=
operator|(
name|ObjAttribute
operator|)
name|artistE
operator|.
name|getAttribute
argument_list|(
literal|"dateOfBirth"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java.util.Date"
argument_list|,
name|dateOfBirth
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|dateOfBirth
operator|.
name|setType
argument_list|(
literal|"java.sql.Date"
argument_list|)
expr_stmt|;
try|try
block|{
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Artist
name|a
init|=
name|p
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|a
operator|.
name|getDateOfBirth
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a
operator|.
name|getDateOfBirth
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|Date
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|a
operator|.
name|getDateOfBirth
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|unblockQueries
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|dateOfBirth
operator|.
name|setType
argument_list|(
literal|"java.util.Date"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testJointPrefetchToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testJointPrefetch1"
argument_list|)
expr_stmt|;
comment|// query with to-many joint prefetches
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Artist
name|a
init|=
operator|(
name|Artist
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|List
name|list
init|=
name|a
operator|.
name|getPaintingArray
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|list
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|children
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|children
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|children
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|p
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// make sure properties are not null..
name|assertNotNull
argument_list|(
name|p
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
name|testJointPrefetchToManyNonConflictingQualifier
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testJointPrefetch1"
argument_list|)
expr_stmt|;
comment|// query with to-many joint prefetches and qualifier that doesn't match
comment|// prefetch....
name|Expression
name|qualifier
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME_PROPERTY
argument_list|,
literal|"artist1"
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|qualifier
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY_PROPERTY
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a
init|=
operator|(
name|Artist
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
name|list
init|=
name|a
operator|.
name|getPaintingArray
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|list
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|children
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|children
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Painting
name|p
init|=
operator|(
name|Painting
operator|)
name|children
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|p
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// make sure properties are not null..
name|assertNotNull
argument_list|(
name|p
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// assert no duplicates
name|Set
name|s
init|=
operator|new
name|HashSet
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|s
operator|.
name|size
argument_list|()
argument_list|,
name|list
operator|.
name|size
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
name|testJointPrefetchMultiStep
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testJointPrefetch2"
argument_list|)
expr_stmt|;
comment|// query with to-many joint prefetches
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addPrefetch
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
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
comment|// make sure phantomly prefetched objects are not deallocated
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|objectMap
operator|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Persistent
argument_list|>
argument_list|()
expr_stmt|;
comment|// sanity check...
name|DataObject
name|g1
init|=
operator|(
name|DataObject
operator|)
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"Gallery"
argument_list|,
name|Gallery
operator|.
name|GALLERY_ID_PK_COLUMN
argument_list|,
literal|33001
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|g1
argument_list|)
expr_stmt|;
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Artist
name|a
init|=
operator|(
name|Artist
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ValueHolder
name|list
init|=
operator|(
name|ValueHolder
operator|)
name|a
operator|.
name|getPaintingArray
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
comment|// intermediate relationship is not fetched...
name|assertTrue
argument_list|(
name|list
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// however both galleries must be in memory...
name|g1
operator|=
operator|(
name|DataObject
operator|)
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"Gallery"
argument_list|,
name|Gallery
operator|.
name|GALLERY_ID_PK_COLUMN
argument_list|,
literal|33001
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|g1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|g1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|DataObject
name|g2
init|=
operator|(
name|DataObject
operator|)
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"Gallery"
argument_list|,
name|Gallery
operator|.
name|GALLERY_ID_PK_COLUMN
argument_list|,
literal|33002
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|g2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|g2
operator|.
name|getPersistenceState
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

