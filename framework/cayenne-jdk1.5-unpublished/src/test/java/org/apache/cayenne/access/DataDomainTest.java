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
name|Map
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
name|ObjectContext
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
name|annotation
operator|.
name|PostAdd
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
name|event
operator|.
name|DefaultEventManager
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
name|Exhibit
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
name|testdo
operator|.
name|testmap
operator|.
name|annotations
operator|.
name|Tag1
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
name|DataDomainTest
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
name|JdbcEventLogger
name|logger
decl_stmt|;
specifier|public
name|void
name|testName
parameter_list|()
throws|throws
name|Exception
block|{
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"some name"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"some name"
argument_list|,
name|domain
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|domain
operator|.
name|setName
argument_list|(
literal|"tst_name"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tst_name"
argument_list|,
name|domain
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLookupDataNode
parameter_list|()
block|{
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|DataMap
name|m1
init|=
operator|new
name|DataMap
argument_list|(
literal|"m1"
argument_list|)
decl_stmt|;
name|DataNode
name|n1
init|=
operator|new
name|DataNode
argument_list|(
literal|"n1"
argument_list|)
decl_stmt|;
name|n1
operator|.
name|addDataMap
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|domain
operator|.
name|addNode
argument_list|(
name|n1
argument_list|)
expr_stmt|;
name|DataMap
name|m2
init|=
operator|new
name|DataMap
argument_list|(
literal|"m2"
argument_list|)
decl_stmt|;
name|DataNode
name|n2
init|=
operator|new
name|DataNode
argument_list|(
literal|"n2"
argument_list|)
decl_stmt|;
name|n2
operator|.
name|addDataMap
argument_list|(
name|m2
argument_list|)
expr_stmt|;
name|domain
operator|.
name|addNode
argument_list|(
name|n2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|n1
argument_list|,
name|domain
operator|.
name|lookupDataNode
argument_list|(
name|m1
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|n2
argument_list|,
name|domain
operator|.
name|lookupDataNode
argument_list|(
name|m2
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|domain
operator|.
name|lookupDataNode
argument_list|(
operator|new
name|DataMap
argument_list|(
literal|"m3"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"must have thrown on missing Map to Node maping"
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
name|testLookupDataNode_Default
parameter_list|()
block|{
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|DataMap
name|m1
init|=
operator|new
name|DataMap
argument_list|(
literal|"m1"
argument_list|)
decl_stmt|;
name|DataNode
name|n1
init|=
operator|new
name|DataNode
argument_list|(
literal|"n1"
argument_list|)
decl_stmt|;
name|n1
operator|.
name|addDataMap
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|domain
operator|.
name|setDefaultNode
argument_list|(
name|n1
argument_list|)
expr_stmt|;
name|DataMap
name|m2
init|=
operator|new
name|DataMap
argument_list|(
literal|"m2"
argument_list|)
decl_stmt|;
name|DataNode
name|n2
init|=
operator|new
name|DataNode
argument_list|(
literal|"n2"
argument_list|)
decl_stmt|;
name|n2
operator|.
name|addDataMap
argument_list|(
name|m2
argument_list|)
expr_stmt|;
name|domain
operator|.
name|addNode
argument_list|(
name|n2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|n1
argument_list|,
name|domain
operator|.
name|lookupDataNode
argument_list|(
name|m1
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|n2
argument_list|,
name|domain
operator|.
name|lookupDataNode
argument_list|(
name|m2
argument_list|)
argument_list|)
expr_stmt|;
comment|// must map to default
name|assertSame
argument_list|(
name|n1
argument_list|,
name|domain
operator|.
name|lookupDataNode
argument_list|(
operator|new
name|DataMap
argument_list|(
literal|"m3"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNodes
parameter_list|()
throws|throws
name|Exception
block|{
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"dom1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|domain
operator|.
name|getDataNodes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataNode
name|node
init|=
operator|new
name|DataNode
argument_list|(
literal|"1"
argument_list|)
decl_stmt|;
name|node
operator|.
name|setJdbcEventLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
name|domain
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|domain
operator|.
name|getDataNodes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|=
operator|new
name|DataNode
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|node
operator|.
name|setJdbcEventLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
name|domain
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|domain
operator|.
name|getDataNodes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNodeMaps
parameter_list|()
throws|throws
name|Exception
block|{
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"dom1"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|domain
operator|.
name|getDataMap
argument_list|(
literal|"map"
argument_list|)
argument_list|)
expr_stmt|;
name|DataNode
name|node
init|=
operator|new
name|DataNode
argument_list|(
literal|"1"
argument_list|)
decl_stmt|;
name|node
operator|.
name|setJdbcEventLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
name|node
operator|.
name|addDataMap
argument_list|(
operator|new
name|DataMap
argument_list|(
literal|"map"
argument_list|)
argument_list|)
expr_stmt|;
name|domain
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|domain
operator|.
name|getDataMap
argument_list|(
literal|"map"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMaps
parameter_list|()
throws|throws
name|Exception
block|{
name|DataDomain
name|d1
init|=
operator|new
name|DataDomain
argument_list|(
literal|"dom1"
argument_list|)
decl_stmt|;
name|DataMap
name|m1
init|=
operator|new
name|DataMap
argument_list|(
literal|"m1"
argument_list|)
decl_stmt|;
name|d1
operator|.
name|addDataMap
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|m1
argument_list|,
name|d1
operator|.
name|getDataMap
argument_list|(
name|m1
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|d1
operator|.
name|removeDataMap
argument_list|(
name|m1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|d1
operator|.
name|getDataMap
argument_list|(
name|m1
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testEntityResolverRefresh
parameter_list|()
throws|throws
name|Exception
block|{
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"dom1"
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|EntityResolver
name|resolver
init|=
name|domain
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|(
literal|"map"
argument_list|)
decl_stmt|;
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"TestEntity"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|domain
operator|.
name|addDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|entity
argument_list|,
name|resolver
operator|.
name|getObjEntity
argument_list|(
literal|"TestEntity"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testEntityResolver
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"dom1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|domain
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInitDataDomainWithSharedCache
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|properties
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
name|properties
operator|.
name|put
argument_list|(
name|DataDomain
operator|.
name|SHARED_CACHE_ENABLED_PROPERTY
argument_list|,
name|Boolean
operator|.
name|TRUE
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"d1"
argument_list|,
name|properties
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|domain
operator|.
name|isSharedCacheEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInitDataDomainWithDedicatedCache
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|properties
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
name|properties
operator|.
name|put
argument_list|(
name|DataDomain
operator|.
name|SHARED_CACHE_ENABLED_PROPERTY
argument_list|,
name|Boolean
operator|.
name|FALSE
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"d1"
argument_list|,
name|properties
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|domain
operator|.
name|isSharedCacheEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInitDataDomainValidation
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|properties
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
name|properties
operator|.
name|put
argument_list|(
name|DataDomain
operator|.
name|VALIDATING_OBJECTS_ON_COMMIT_PROPERTY
argument_list|,
name|Boolean
operator|.
name|TRUE
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"d1"
argument_list|,
name|properties
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|domain
operator|.
name|isValidatingObjectsOnCommit
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInitDataDomainNoValidation
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|properties
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
name|properties
operator|.
name|put
argument_list|(
name|DataDomain
operator|.
name|VALIDATING_OBJECTS_ON_COMMIT_PROPERTY
argument_list|,
name|Boolean
operator|.
name|FALSE
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"d1"
argument_list|,
name|properties
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|domain
operator|.
name|isValidatingObjectsOnCommit
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDataDomainInternalTransactions
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|properties
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
name|properties
operator|.
name|put
argument_list|(
name|DataDomain
operator|.
name|USING_EXTERNAL_TRANSACTIONS_PROPERTY
argument_list|,
name|Boolean
operator|.
name|FALSE
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"d1"
argument_list|,
name|properties
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|domain
operator|.
name|isUsingExternalTransactions
argument_list|()
argument_list|)
expr_stmt|;
name|Transaction
name|transaction
init|=
name|domain
operator|.
name|createTransaction
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|transaction
operator|instanceof
name|InternalTransaction
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDataDomainExternalTransactions
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|properties
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
name|properties
operator|.
name|put
argument_list|(
name|DataDomain
operator|.
name|USING_EXTERNAL_TRANSACTIONS_PROPERTY
argument_list|,
name|Boolean
operator|.
name|TRUE
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"d1"
argument_list|,
name|properties
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|domain
operator|.
name|isUsingExternalTransactions
argument_list|()
argument_list|)
expr_stmt|;
name|Transaction
name|transaction
init|=
name|domain
operator|.
name|createTransaction
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|transaction
operator|instanceof
name|ExternalTransaction
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testShutdownCache
parameter_list|()
block|{
name|DataDomain
name|domain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
specifier|final
name|boolean
index|[]
name|cacheShutdown
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|DataRowStore
name|cache
init|=
operator|new
name|DataRowStore
argument_list|(
literal|"Y"
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
argument_list|,
operator|new
name|DefaultEventManager
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|cacheShutdown
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
block|}
block|}
decl_stmt|;
name|domain
operator|.
name|setSharedSnapshotCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|domain
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|cacheShutdown
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddListener
parameter_list|()
block|{
name|DataDomain
name|domain
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
decl_stmt|;
name|PostAddListener
name|listener
init|=
operator|new
name|PostAddListener
argument_list|()
decl_stmt|;
name|domain
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e:Gallery;"
argument_list|,
name|listener
operator|.
name|getAndReset
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a:Artist;"
argument_list|,
name|listener
operator|.
name|getAndReset
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|Exhibit
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|listener
operator|.
name|getAndReset
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e:Painting;"
argument_list|,
name|listener
operator|.
name|getAndReset
argument_list|()
argument_list|)
expr_stmt|;
block|}
class|class
name|PostAddListener
block|{
name|StringBuilder
name|callbackBuffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
annotation|@
name|PostAdd
argument_list|(
block|{
name|Gallery
operator|.
name|class
block|,
name|Painting
operator|.
name|class
block|}
argument_list|)
name|void
name|postAddEntities
parameter_list|(
name|Persistent
name|object
parameter_list|)
block|{
name|callbackBuffer
operator|.
name|append
argument_list|(
literal|"e:"
operator|+
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
operator|+
literal|";"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PostAdd
argument_list|(
name|entityAnnotations
operator|=
name|Tag1
operator|.
name|class
argument_list|)
name|void
name|postAddAnnotated
parameter_list|(
name|Persistent
name|object
parameter_list|)
block|{
name|callbackBuffer
operator|.
name|append
argument_list|(
literal|"a:"
operator|+
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
operator|+
literal|";"
argument_list|)
expr_stmt|;
block|}
name|String
name|getAndReset
parameter_list|()
block|{
name|String
name|v
init|=
name|callbackBuffer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|callbackBuffer
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
return|return
name|v
return|;
block|}
block|}
block|}
end_class

end_unit

