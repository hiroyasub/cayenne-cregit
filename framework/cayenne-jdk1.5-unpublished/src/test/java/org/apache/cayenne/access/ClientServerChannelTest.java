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
name|HashMap
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
name|DataChannel
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
name|MockDataChannel
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
name|QueryResponse
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
name|graph
operator|.
name|MockGraphDiff
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
name|graph
operator|.
name|NodeCreateOperation
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
name|query
operator|.
name|SortOrder
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
name|remote
operator|.
name|QueryMessage
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
name|remote
operator|.
name|hessian
operator|.
name|service
operator|.
name|HessianUtil
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
name|ClientMtTable1Subclass
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
name|ClientMtTable2
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
name|ClientMtTable3
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|EqualsBuilder
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ClientServerChannelTest
extends|extends
name|CayenneCase
block|{
annotation|@
name|Override
specifier|protected
name|AccessStack
name|buildAccessStack
parameter_list|()
block|{
return|return
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getAccessStack
argument_list|(
name|MULTI_TIER_ACCESS_STACK
argument_list|)
return|;
block|}
specifier|public
name|void
name|testGetEntityResolver
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityResolver
name|resolver
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resolver
operator|.
name|lookupObjEntity
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resolver
operator|.
name|getClientEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSynchronizeCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// no changes...
name|ClientServerChannel
name|channel
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|channel
operator|.
name|onSync
argument_list|(
name|context
argument_list|,
operator|new
name|MockGraphDiff
argument_list|()
argument_list|,
name|DataChannel
operator|.
name|FLUSH_CASCADE_SYNC
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// introduce changes
name|channel
operator|.
name|onSync
argument_list|(
name|context
argument_list|,
operator|new
name|NodeCreateOperation
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|)
argument_list|)
argument_list|,
name|DataChannel
operator|.
name|FLUSH_CASCADE_SYNC
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPerformQueryObjectIDInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testOnSelectQueryObjectIDInjection"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Query
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"MtTable1"
argument_list|)
decl_stmt|;
name|QueryResponse
name|response
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|context
argument_list|)
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|response
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|results
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|ClientMtTable1
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|clientObject
init|=
operator|(
name|ClientMtTable1
operator|)
name|result
decl_stmt|;
name|assertNotNull
argument_list|(
name|clientObject
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
name|MtTable1
operator|.
name|TABLE1_ID_PK_COLUMN
argument_list|,
literal|55
argument_list|)
argument_list|,
name|clientObject
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPerformQueryValuePropagation
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|bytes
init|=
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
decl_stmt|;
name|String
name|chars
init|=
literal|"abc"
decl_stmt|;
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"bytes"
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"chars"
argument_list|,
name|chars
argument_list|)
expr_stmt|;
name|createTestData
argument_list|(
literal|"testOnSelectQueryValuePropagation"
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Query
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"MtTable3"
argument_list|)
decl_stmt|;
name|QueryResponse
name|response
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|context
argument_list|)
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|response
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|results
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Result is of wrong type: "
operator|+
name|result
argument_list|,
name|result
operator|instanceof
name|ClientMtTable3
argument_list|)
expr_stmt|;
name|ClientMtTable3
name|clientObject
init|=
operator|(
name|ClientMtTable3
operator|)
name|result
decl_stmt|;
name|assertEquals
argument_list|(
name|chars
argument_list|,
name|clientObject
operator|.
name|getCharColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|4
argument_list|)
argument_list|,
name|clientObject
operator|.
name|getIntColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|EqualsBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|clientObject
operator|.
name|getBinaryColumn
argument_list|()
argument_list|,
name|bytes
argument_list|)
operator|.
name|isEquals
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPerformQueryPropagationInheritance
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"GLOBAL_ATTRIBUTE1"
argument_list|,
literal|"sub1"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"SERVER_ATTRIBUTE1"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|createTestData
argument_list|(
literal|"testOnSelectQueryValuePropagationInheritance"
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
comment|// must use real SelectQuery instead of mockup as root overriding depends on the
comment|// fact that Query inherits from AbstractQuery.
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// must pass through the serialization pipe before running query as
comment|// HessianSerializer has needed preprocessing hooks...
name|Query
name|preprocessedQuery
init|=
operator|(
name|Query
operator|)
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|query
argument_list|,
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|QueryResponse
name|response
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|context
argument_list|)
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|preprocessedQuery
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|response
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|results
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Result is of wrong type: "
operator|+
name|result
argument_list|,
name|result
operator|instanceof
name|ClientMtTable1Subclass
argument_list|)
expr_stmt|;
name|ClientMtTable1Subclass
name|clientObject
init|=
operator|(
name|ClientMtTable1Subclass
operator|)
name|result
decl_stmt|;
name|assertEquals
argument_list|(
literal|"sub1"
argument_list|,
name|clientObject
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOnQuery
parameter_list|()
block|{
specifier|final
name|boolean
index|[]
name|genericDone
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|MockDataChannel
name|parent
init|=
operator|new
name|MockDataChannel
argument_list|(
operator|new
name|EntityResolver
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|QueryResponse
name|onQuery
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
name|genericDone
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
return|return
name|super
operator|.
name|onQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|DataContext
name|context
init|=
operator|new
name|DataContext
argument_list|(
name|parent
argument_list|,
operator|new
name|ObjectStore
argument_list|(
operator|new
name|MockDataRowStore
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|QueryMessage
name|message
init|=
operator|new
name|QueryMessage
argument_list|(
operator|new
name|MockQuery
argument_list|()
argument_list|)
decl_stmt|;
operator|new
name|ClientServerChannel
argument_list|(
name|context
argument_list|)
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|message
operator|.
name|getQuery
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|genericDone
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOnQueryPrefetchingToMany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPrefetching"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|ClientServerChannel
name|channel
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|ClientMtTable1
operator|.
name|TABLE2ARRAY_PROPERTY
argument_list|)
expr_stmt|;
comment|// must pass through the serialization pipe before running query as
comment|// HessianSerializer has needed preprocessing hooks...
name|Query
name|preprocessedQuery
init|=
operator|(
name|Query
operator|)
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|q
argument_list|,
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|channel
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|preprocessedQuery
argument_list|)
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|ClientMtTable1
name|o1
init|=
operator|(
name|ClientMtTable1
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|o1
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|children1
init|=
name|o1
operator|.
name|getTable2Array
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|children1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|children1
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
name|ClientMtTable2
name|o
init|=
operator|(
name|ClientMtTable2
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|o
operator|.
name|getObjectContext
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
specifier|public
name|void
name|testOnQueryPrefetchingToManyEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testPrefetching"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|ClientServerChannel
name|channel
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addOrdering
argument_list|(
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1_PROPERTY
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
name|q
operator|.
name|addPrefetch
argument_list|(
name|ClientMtTable1
operator|.
name|TABLE2ARRAY_PROPERTY
argument_list|)
expr_stmt|;
comment|// must pass through the serialization pipe before running query as
comment|// HessianSerializer has needed preprocessing hooks...
name|Query
name|preprocessedQuery
init|=
operator|(
name|Query
operator|)
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|q
argument_list|,
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|channel
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|preprocessedQuery
argument_list|)
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|blockQueries
argument_list|()
expr_stmt|;
try|try
block|{
name|ClientMtTable1
name|o2
init|=
operator|(
name|ClientMtTable1
operator|)
name|results
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|o2
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|children2
init|=
name|o2
operator|.
name|getTable2Array
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|children2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ValueHolder
operator|)
name|children2
operator|)
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|children2
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
block|}
end_class

end_unit

