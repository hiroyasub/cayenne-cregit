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
name|query
operator|.
name|QueryDescriptor
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
name|EntityResolverIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
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
name|testGetObjEntity
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|runtime
operator|.
name|getDataDomain
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
annotation|@
name|Test
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
name|runtime
operator|.
name|getDataDomain
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
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
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
name|runtime
operator|.
name|getDataDomain
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
operator|new
name|Artist
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
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
name|runtime
operator|.
name|getDataDomain
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
name|context
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
name|getObjEntity
argument_list|(
name|artist
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
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
argument_list|<
name|?
argument_list|>
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
annotation|@
name|Test
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
name|getObjEntity
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
name|getObjEntity
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
annotation|@
name|Test
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
name|getObjEntity
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
name|getObjEntity
argument_list|(
name|Object
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
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
name|getObjEntity
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
name|getObjEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
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
name|QueryDescriptor
name|q
init|=
name|QueryDescriptor
operator|.
name|selectQueryDescriptor
argument_list|()
decl_stmt|;
name|q
operator|.
name|setName
argument_list|(
literal|"query1"
argument_list|)
expr_stmt|;
name|m1
operator|.
name|addQueryDescriptor
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
name|getQueryDescriptor
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
name|getQueryDescriptor
argument_list|(
literal|"query2"
argument_list|)
argument_list|)
expr_stmt|;
name|QueryDescriptor
name|q2
init|=
name|QueryDescriptor
operator|.
name|selectQueryDescriptor
argument_list|()
decl_stmt|;
name|q2
operator|.
name|setName
argument_list|(
literal|"query2"
argument_list|)
expr_stmt|;
name|m1
operator|.
name|addQueryDescriptor
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
name|getQueryDescriptor
argument_list|(
literal|"query2"
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
specifier|private
name|ObjEntity
name|getObjEntity
parameter_list|(
name|String
name|objEntityName
parameter_list|)
block|{
for|for
control|(
name|DataMap
name|map
range|:
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
for|for
control|(
name|ObjEntity
name|e
range|:
name|map
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
if|if
condition|(
name|objEntityName
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|e
return|;
block|}
block|}
block|}
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No ObjEntity found: "
operator|+
name|objEntityName
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

