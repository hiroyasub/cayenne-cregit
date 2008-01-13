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
name|CharPkTestEntity
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
name|CompoundPkTestEntity
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
name|dba
operator|.
name|frontbase
operator|.
name|FrontBaseAdapter
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
name|openbase
operator|.
name|OpenBaseAdapter
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
name|query
operator|.
name|EJBQLQuery
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
name|ObjectIdQuery
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
name|SQLResultSetMapping
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
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DataObjectUtilsTest
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
name|testScalarObjectForQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testScalarObjectForQuery"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|String
name|sql
init|=
literal|"SELECT count(1) AS X FROM ARTIST"
decl_stmt|;
name|DataMap
name|map
init|=
name|getDomain
argument_list|()
operator|.
name|getMap
argument_list|(
literal|"testmap"
argument_list|)
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|map
argument_list|,
name|sql
argument_list|)
decl_stmt|;
name|query
operator|.
name|setTemplate
argument_list|(
name|FrontBaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"SELECT COUNT(ARTIST_ID) AS X FROM ARTIST"
argument_list|)
expr_stmt|;
name|query
operator|.
name|setTemplate
argument_list|(
name|OpenBaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"SELECT COUNT(ARTIST_ID) AS X FROM ARTIST"
argument_list|)
expr_stmt|;
name|query
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|SQLTemplate
operator|.
name|UPPERCASE_COLUMN_NAMES
argument_list|)
expr_stmt|;
name|SQLResultSetMapping
name|rsMap
init|=
operator|new
name|SQLResultSetMapping
argument_list|()
decl_stmt|;
name|rsMap
operator|.
name|addColumnResult
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|query
operator|.
name|setResultSetMapping
argument_list|(
name|rsMap
argument_list|)
expr_stmt|;
name|Object
name|object
init|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Number
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|object
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testScalarObjectForQuery2
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testScalarObjectForQuery"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT count(a) from Artist a"
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|Object
name|object
init|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Number
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|object
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testObjectForQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testObjectForPKInt"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|ObjectId
name|id
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
operator|new
name|Integer
argument_list|(
literal|33002
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|object
init|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
name|id
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
operator|(
operator|(
name|Artist
operator|)
name|object
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testObjectForQueryNoObject
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
name|ObjectId
name|id
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
operator|new
name|Integer
argument_list|(
literal|44001
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|object
init|=
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
name|id
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNoObjectForPK
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testObjectForPKInt"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
comment|// use bogus non-existent PK
name|Object
name|object
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|44001
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testObjectForPKTemporary
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
name|Persistent
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
name|Persistent
name|o2
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
name|assertSame
argument_list|(
name|o1
argument_list|,
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o2
argument_list|,
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|o2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|assertNull
argument_list|(
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"An attempt to fetch an object for "
operator|+
literal|"the non-existent temp id should have failed..."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|public
name|void
name|testObjectForPKObjectId
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testObjectForPKInt"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Object
name|object
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|33002
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
operator|(
operator|(
name|Artist
operator|)
name|object
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testObjectForPKClassInt
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testObjectForPKInt"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Object
name|object
init|=
name|DataObjectUtils
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
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
operator|(
operator|(
name|Artist
operator|)
name|object
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testObjectForPKEntityInt
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testObjectForPKInt"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Object
name|object
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
literal|"Artist"
argument_list|,
literal|33002
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
operator|(
operator|(
name|Artist
operator|)
name|object
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testObjectForPKClassMap
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testObjectForPKInt"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Map
name|pk
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|33002
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|object
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
name|pk
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|Artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artist2"
argument_list|,
operator|(
operator|(
name|Artist
operator|)
name|object
operator|)
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testObjectForPKEntityMapCompound
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testObjectForPKCompound"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Map
name|pk
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|pk
operator|.
name|put
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY1_PK_COLUMN
argument_list|,
literal|"PK1"
argument_list|)
expr_stmt|;
name|pk
operator|.
name|put
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY2_PK_COLUMN
argument_list|,
literal|"PK2"
argument_list|)
expr_stmt|;
name|Object
name|object
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|CompoundPkTestEntity
operator|.
name|class
argument_list|,
name|pk
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|CompoundPkTestEntity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BBB"
argument_list|,
operator|(
operator|(
name|CompoundPkTestEntity
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCompoundPKForObject
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testCompoundPKForObject"
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
operator|new
name|SelectQuery
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
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
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Map
name|pk
init|=
name|DataObjectUtils
operator|.
name|compoundPKForObject
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pk
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|pk
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PK1"
argument_list|,
name|pk
operator|.
name|get
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY1_PK_COLUMN
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PK2"
argument_list|,
name|pk
operator|.
name|get
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY2_PK_COLUMN
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIntPKForObjectFailureForCompound
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testCompoundPKForObject"
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
operator|new
name|SelectQuery
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
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
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|DataObjectUtils
operator|.
name|intPKForObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"intPKForObject must fail for compound key"
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
specifier|public
name|void
name|testIntPKForObjectFailureForNonNumeric
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testIntPKForObjectNonNumeric"
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
operator|new
name|SelectQuery
argument_list|(
name|CharPkTestEntity
operator|.
name|class
argument_list|)
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
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|DataObjectUtils
operator|.
name|intPKForObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"intPKForObject must fail for non-numeric key"
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
specifier|public
name|void
name|testPKForObjectFailureForCompound
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testCompoundPKForObject"
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
operator|new
name|SelectQuery
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
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
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
name|DataObjectUtils
operator|.
name|pkForObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"pkForObject must fail for compound key"
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
specifier|public
name|void
name|testIntPKForObject
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testIntPKForObject"
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
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
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
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|33001
argument_list|,
name|DataObjectUtils
operator|.
name|intPKForObject
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPKForObject
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testIntPKForObject"
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
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
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
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|33001
argument_list|)
argument_list|,
name|DataObjectUtils
operator|.
name|pkForObject
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIntPKForObjectNonNumeric
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testIntPKForObjectNonNumeric"
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
operator|new
name|SelectQuery
argument_list|(
name|CharPkTestEntity
operator|.
name|class
argument_list|)
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
name|DataObject
name|object
init|=
operator|(
name|DataObject
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CPK"
argument_list|,
name|DataObjectUtils
operator|.
name|pkForObject
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

