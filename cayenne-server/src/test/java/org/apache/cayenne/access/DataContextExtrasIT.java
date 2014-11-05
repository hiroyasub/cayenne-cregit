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
name|Cayenne
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
name|dba
operator|.
name|JdbcAdapter
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
name|dba
operator|.
name|JdbcPkGenerator
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
name|dba
operator|.
name|PkGenerator
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
name|AdhocObjectFactory
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
name|log
operator|.
name|JdbcEventLogger
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|test
operator|.
name|jdbc
operator|.
name|TableHelper
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
name|sql
operator|.
name|Types
import|;
end_import

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
name|assertNotSame
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
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
name|DataContextExtrasIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|JdbcEventLogger
name|logger
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
specifier|protected
name|TableHelper
name|tArtist
decl_stmt|;
specifier|protected
name|TableHelper
name|tPainting
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PAINTING_INFO"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PAINTING1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_EXHIBIT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_GROUP"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST"
argument_list|)
expr_stmt|;
name|tArtist
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|setColumns
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_NAME"
argument_list|)
expr_stmt|;
name|tPainting
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|setColumns
argument_list|(
literal|"PAINTING_ID"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"PAINTING_TITLE"
argument_list|,
literal|"ESTIMATED_PRICE"
argument_list|)
operator|.
name|setColumnTypes
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|BIGINT
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|Types
operator|.
name|DECIMAL
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createPhantomModificationDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"artist1"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createPhantomModificationsValidateToOneDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"artist1"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|33001
argument_list|,
literal|"P1"
argument_list|,
literal|3000
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createValidateOnToManyChangeDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"artist1"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createPhantomRelationshipModificationCommitDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|"artist1"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|33002
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|33001
argument_list|,
literal|33001
argument_list|,
literal|"P1"
argument_list|,
literal|3000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testManualIdProcessingOnCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|object
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
name|object
operator|.
name|setArtistName
argument_list|(
literal|"ABC"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
comment|// do a manual ID substitution
name|ObjectId
name|manualId
init|=
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|77777
argument_list|)
decl_stmt|;
name|object
operator|.
name|setObjectId
argument_list|(
name|manualId
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|object
argument_list|,
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|manualId
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|manualId
argument_list|,
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveFault
parameter_list|()
block|{
name|Artist
name|o1
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
name|o1
operator|.
name|setArtistName
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|o1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|o1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"artistName"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|prepareForAccess
argument_list|(
name|o1
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|o1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|o1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"artistName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveFaultFailure
parameter_list|()
block|{
name|Persistent
name|o1
init|=
name|context
operator|.
name|findOrCreateObject
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|234
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|context
operator|.
name|prepareForAccess
argument_list|(
name|o1
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Must blow on non-existing fault."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUserProperties
parameter_list|()
block|{
name|assertNull
argument_list|(
name|context
operator|.
name|getUserProperty
argument_list|(
literal|"ABC"
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|object
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|context
operator|.
name|setUserProperty
argument_list|(
literal|"ABC"
argument_list|,
name|object
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|object
argument_list|,
name|context
operator|.
name|getUserProperty
argument_list|(
literal|"ABC"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHasChangesNew
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"No changes expected in context"
argument_list|,
operator|!
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
literal|"Artist"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Object added to context, expected to report changes"
argument_list|,
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNewObject
parameter_list|()
block|{
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
name|assertTrue
argument_list|(
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|registeredNodes
argument_list|()
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|newObjects
argument_list|()
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNewObjectWithClass
parameter_list|()
block|{
name|Artist
name|a1
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
name|assertTrue
argument_list|(
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|registeredNodes
argument_list|()
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|newObjects
argument_list|()
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIdObjectFromDataRow
parameter_list|()
block|{
name|DataRow
name|row
init|=
operator|new
name|DataRow
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"ARTIST_ID"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|100000
argument_list|)
argument_list|)
expr_stmt|;
name|DataObject
name|obj
init|=
name|context
operator|.
name|objectFromDataRow
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|row
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|registeredNodes
argument_list|()
operator|.
name|contains
argument_list|(
name|obj
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|obj
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|obj
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPartialObjectFromDataRow
parameter_list|()
block|{
name|DataRow
name|row
init|=
operator|new
name|DataRow
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"ARTIST_ID"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|100001
argument_list|)
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"ARTIST_NAME"
argument_list|,
literal|"ArtistXYZ"
argument_list|)
expr_stmt|;
name|DataObject
name|obj
init|=
name|context
operator|.
name|objectFromDataRow
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|row
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|registeredNodes
argument_list|()
operator|.
name|contains
argument_list|(
name|obj
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|obj
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|obj
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFullObjectFromDataRow
parameter_list|()
block|{
name|DataRow
name|row
init|=
operator|new
name|DataRow
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"ARTIST_ID"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|123456
argument_list|)
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"ARTIST_NAME"
argument_list|,
literal|"ArtistXYZ"
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|obj
init|=
name|context
operator|.
name|objectFromDataRow
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|row
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|registeredNodes
argument_list|()
operator|.
name|contains
argument_list|(
name|obj
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|obj
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|obj
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ArtistXYZ"
argument_list|,
name|obj
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCommitChangesError
parameter_list|()
block|{
name|DataDomain
name|domain
init|=
name|context
operator|.
name|getParentDataDomain
argument_list|()
decl_stmt|;
comment|// setup mockup PK generator that will blow on PK request
comment|// to emulate an exception
name|JdbcAdapter
name|jdbcAdapter
init|=
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|JdbcAdapter
operator|.
name|class
argument_list|,
name|JdbcAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|PkGenerator
name|newGenerator
init|=
operator|new
name|JdbcPkGenerator
argument_list|(
name|jdbcAdapter
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Object
name|generatePk
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DbAttribute
name|pk
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Intentional"
argument_list|)
throw|;
block|}
block|}
decl_stmt|;
name|PkGenerator
name|oldGenerator
init|=
name|domain
operator|.
name|getDataNodes
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getAdapter
argument_list|()
operator|.
name|getPkGenerator
argument_list|()
decl_stmt|;
name|JdbcAdapter
name|adapter
init|=
operator|(
name|JdbcAdapter
operator|)
name|domain
operator|.
name|getDataNodes
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|adapter
operator|.
name|setPkGenerator
argument_list|(
name|newGenerator
argument_list|)
expr_stmt|;
try|try
block|{
name|Artist
name|newArtist
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
name|newArtist
operator|.
name|setArtistName
argument_list|(
literal|"aaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Exception expected but not thrown due to missing PK generation routine."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
comment|// exception expected
block|}
finally|finally
block|{
name|adapter
operator|.
name|setPkGenerator
argument_list|(
name|oldGenerator
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Testing behavior of Cayenne when a database exception is thrown in SELECT query.      */
annotation|@
name|Test
specifier|public
name|void
name|testSelectException
parameter_list|()
block|{
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
literal|"SELECT * FROM NON_EXISTENT_TABLE"
argument_list|)
decl_stmt|;
try|try
block|{
name|context
operator|.
name|performGenericQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Query was invalid and was supposed to fail."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
comment|// exception expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEntityResolver
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPhantomModificationsValidate
parameter_list|()
throws|throws
name|Exception
block|{
name|createPhantomModificationDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Artist
name|a1
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
name|Artist
name|a2
init|=
operator|(
name|Artist
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
name|a1
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|a1
operator|.
name|resetValidationFlags
argument_list|()
expr_stmt|;
name|a2
operator|.
name|resetValidationFlags
argument_list|()
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|a1
operator|.
name|isValidateForSaveCalled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|a2
operator|.
name|isValidateForSaveCalled
argument_list|()
argument_list|)
expr_stmt|;
comment|// "phantom" modification - the property is really unchanged
name|a1
operator|.
name|setArtistName
argument_list|(
name|a1
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
comment|// some other unrelated object modification caused phantom modification to be
comment|// committed as well...
comment|// (see CAY-355)
name|a2
operator|.
name|setArtistName
argument_list|(
name|a2
operator|.
name|getArtistName
argument_list|()
operator|+
literal|"_x"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|resetValidationFlags
argument_list|()
expr_stmt|;
name|a2
operator|.
name|resetValidationFlags
argument_list|()
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|a2
operator|.
name|isValidateForSaveCalled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|a1
operator|.
name|isValidateForSaveCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPhantomModificationsValidateToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createPhantomModificationsValidateToOneDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
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
decl_stmt|;
name|Painting
name|p1
init|=
operator|(
name|Painting
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|p1
operator|.
name|setPaintingTitle
argument_list|(
name|p1
operator|.
name|getPaintingTitle
argument_list|()
argument_list|)
expr_stmt|;
name|p1
operator|.
name|resetValidationFlags
argument_list|()
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"To-one relationship presence caused incorrect validation call."
argument_list|,
name|p1
operator|.
name|isValidateForSaveCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testValidateOnToManyChange
parameter_list|()
throws|throws
name|Exception
block|{
name|createValidateOnToManyChangeDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Artist
name|a1
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
name|Painting
name|p1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p1
operator|.
name|setPaintingTitle
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|addToPaintingArray
argument_list|(
name|p1
argument_list|)
expr_stmt|;
name|a1
operator|.
name|resetValidationFlags
argument_list|()
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|a1
operator|.
name|isValidateForSaveCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPhantomAttributeModificationCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|createPhantomModificationDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Artist
name|a1
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
name|String
name|oldName
init|=
name|a1
operator|.
name|getArtistName
argument_list|()
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
name|oldName
operator|+
literal|".mod"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
name|oldName
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|a1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPhantomRelationshipModificationCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|createPhantomRelationshipModificationCommitDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
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
name|Painting
name|p1
init|=
operator|(
name|Painting
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Artist
name|oldArtist
init|=
name|p1
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|Artist
name|newArtist
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|33002
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|oldArtist
argument_list|,
name|newArtist
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setToArtist
argument_list|(
name|newArtist
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setToArtist
argument_list|(
name|oldArtist
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|p1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|oldArtist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|newArtist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPhantomRelationshipModificationValidate
parameter_list|()
throws|throws
name|Exception
block|{
name|createPhantomRelationshipModificationCommitDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
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
name|Painting
name|p1
init|=
operator|(
name|Painting
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Artist
name|oldArtist
init|=
name|p1
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|Artist
name|newArtist
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|33002
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|oldArtist
argument_list|,
name|newArtist
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setToArtist
argument_list|(
name|newArtist
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setToArtist
argument_list|(
name|oldArtist
argument_list|)
expr_stmt|;
name|p1
operator|.
name|resetValidationFlags
argument_list|()
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|p1
operator|.
name|isValidateForSaveCalled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

