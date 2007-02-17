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
name|map
package|;
end_package

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
name|Collection
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
name|List
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
name|query
operator|.
name|MockQuery
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
name|Query
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
name|mt
operator|.
name|ClientMtTable1
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
name|mt
operator|.
name|MtTable1
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
name|AccessStack
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
name|unit
operator|.
name|CayenneResources
import|;
end_import

begin_class
specifier|public
class|class
name|EntityResolverTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testObjEntityLookupDuplicates
parameter_list|()
block|{
name|AccessStack
name|stack
init|=
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getAccessStack
argument_list|(
literal|"GenericStack"
argument_list|)
decl_stmt|;
name|DataMap
name|generic
init|=
name|stack
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getMap
argument_list|(
literal|"generic"
argument_list|)
decl_stmt|;
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|generic
argument_list|)
argument_list|)
decl_stmt|;
name|ObjEntity
name|g1
init|=
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
literal|"Generic1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|g1
argument_list|)
expr_stmt|;
name|ObjEntity
name|g2
init|=
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
literal|"Generic2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|g2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|g1
argument_list|,
name|g2
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|Object
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|CayenneDataObject
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"two entities mapped to the same class... resolver must have thrown."
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
comment|/**      * @deprecated since 3.0      */
specifier|public
name|void
name|testDbEntityLookupDuplicates
parameter_list|()
block|{
name|AccessStack
name|stack
init|=
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getAccessStack
argument_list|(
literal|"GenericStack"
argument_list|)
decl_stmt|;
name|DataMap
name|generic
init|=
name|stack
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getMap
argument_list|(
literal|"generic"
argument_list|)
decl_stmt|;
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|generic
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|Object
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|resolver
operator|.
name|lookupDbEntity
argument_list|(
name|CayenneDataObject
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"two entities mapped to the same class... resolver must have thrown."
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
name|testGetClientEntityResolver
parameter_list|()
block|{
name|AccessStack
name|stack
init|=
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getAccessStack
argument_list|(
name|MULTI_TIER_ACCESS_STACK
argument_list|)
decl_stmt|;
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|stack
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getDataMaps
argument_list|()
argument_list|)
decl_stmt|;
name|EntityResolver
name|clientResolver
init|=
name|resolver
operator|.
name|getClientEntityResolver
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|clientResolver
argument_list|)
expr_stmt|;
comment|// make sure that client entities got translated properly...
try|try
block|{
name|assertNotNull
argument_list|(
name|clientResolver
operator|.
name|lookupObjEntity
argument_list|(
literal|"MtTable1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"'MtTable1' entity is not mapped. All entities: "
operator|+
name|clientResolver
operator|.
name|getObjEntities
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|clientResolver
operator|.
name|lookupObjEntity
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|clientResolver
operator|.
name|lookupObjEntity
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated since 3.0      */
specifier|public
name|void
name|testLookupDbEntityByClass
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|getDomain
argument_list|()
operator|.
name|getDataMaps
argument_list|()
argument_list|)
decl_stmt|;
name|assertIsArtistDbEntity
argument_list|(
name|resolver
operator|.
name|lookupDbEntity
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated since 3.0      */
specifier|public
name|void
name|testLookupDbEntityByDataobject
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|getDomain
argument_list|()
operator|.
name|getDataMaps
argument_list|()
argument_list|)
decl_stmt|;
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|this
operator|.
name|createDataContext
argument_list|()
operator|.
name|newObject
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|assertIsArtistDbEntity
argument_list|(
name|resolver
operator|.
name|lookupDbEntity
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// //Test ObjEntity lookups
specifier|public
name|void
name|testGetObjEntity
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|getDomain
argument_list|()
operator|.
name|getDataMaps
argument_list|()
argument_list|)
decl_stmt|;
name|assertIsArtistObjEntity
argument_list|(
name|resolver
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLookupObjEntityByClass
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|getDomain
argument_list|()
operator|.
name|getDataMaps
argument_list|()
argument_list|)
decl_stmt|;
name|assertIsArtistObjEntity
argument_list|(
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLookupObjEntityByInstance
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|getDomain
argument_list|()
operator|.
name|getDataMaps
argument_list|()
argument_list|)
decl_stmt|;
name|assertIsArtistObjEntity
argument_list|(
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
operator|new
name|Artist
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLookupObjEntityByDataobject
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|getDomain
argument_list|()
operator|.
name|getDataMaps
argument_list|()
argument_list|)
decl_stmt|;
name|Artist
name|artist
init|=
operator|(
name|Artist
operator|)
name|this
operator|.
name|createDataContext
argument_list|()
operator|.
name|newObject
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|assertIsArtistObjEntity
argument_list|(
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetDataMapList
parameter_list|()
block|{
name|DataMap
name|m1
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|DataMap
name|m2
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|m2
argument_list|)
expr_stmt|;
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|Collection
name|maps
init|=
name|resolver
operator|.
name|getDataMaps
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|maps
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|maps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|maps
operator|.
name|containsAll
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddDataMap
parameter_list|()
block|{
comment|// create empty resolver
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resolver
operator|.
name|getDataMaps
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|Object
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|DataMap
name|m1
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|ObjEntity
name|oe1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|oe1
operator|.
name|setClassName
argument_list|(
name|Object
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|m1
operator|.
name|addObjEntity
argument_list|(
name|oe1
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addDataMap
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resolver
operator|.
name|getDataMaps
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|oe1
argument_list|,
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|Object
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resolver
argument_list|,
name|m1
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveDataMap
parameter_list|()
block|{
comment|// create a resolver with a single map
name|DataMap
name|m1
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|ObjEntity
name|oe1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|oe1
operator|.
name|setClassName
argument_list|(
name|Object
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|m1
operator|.
name|addObjEntity
argument_list|(
name|oe1
argument_list|)
expr_stmt|;
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resolver
operator|.
name|getDataMaps
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|oe1
argument_list|,
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|Object
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|removeDataMap
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resolver
operator|.
name|getDataMaps
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|Object
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddObjEntity
parameter_list|()
block|{
comment|// create a resolver with a single map
name|DataMap
name|m1
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|ObjEntity
name|oe1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"test1"
argument_list|)
decl_stmt|;
name|oe1
operator|.
name|setClassName
argument_list|(
name|Object
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|m1
operator|.
name|addObjEntity
argument_list|(
name|oe1
argument_list|)
expr_stmt|;
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|oe1
argument_list|,
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|Object
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|ObjEntity
name|oe2
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"test2"
argument_list|)
decl_stmt|;
name|oe2
operator|.
name|setClassName
argument_list|(
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|m1
operator|.
name|addObjEntity
argument_list|(
name|oe2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|oe2
argument_list|,
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetQuery
parameter_list|()
block|{
comment|// create a resolver with a single map
name|DataMap
name|m1
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|Query
name|q
init|=
operator|new
name|MockQuery
argument_list|(
literal|"query1"
argument_list|)
decl_stmt|;
name|m1
operator|.
name|addQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|m1
argument_list|)
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|q
argument_list|,
name|resolver
operator|.
name|getQuery
argument_list|(
literal|"query1"
argument_list|)
argument_list|)
expr_stmt|;
comment|// check that the query added on-the-fly will be recognized
name|assertNull
argument_list|(
name|resolver
operator|.
name|getQuery
argument_list|(
literal|"query2"
argument_list|)
argument_list|)
expr_stmt|;
name|Query
name|q2
init|=
operator|new
name|MockQuery
argument_list|(
literal|"query2"
argument_list|)
decl_stmt|;
name|m1
operator|.
name|addQuery
argument_list|(
name|q2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|q2
argument_list|,
name|resolver
operator|.
name|getQuery
argument_list|(
literal|"query2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertIsArtistDbEntity
parameter_list|(
name|DbEntity
name|ae
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|ae
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ae
argument_list|,
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertIsArtistObjEntity
parameter_list|(
name|ObjEntity
name|ae
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|ae
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ae
argument_list|,
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

