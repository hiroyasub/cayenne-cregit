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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|ClientServerChannel
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
name|remote
operator|.
name|ClientChannel
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
name|ClientConnection
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
name|service
operator|.
name|LocalConnection
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
name|testdo
operator|.
name|mt
operator|.
name|MtTable2
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
name|PersistentObjectHolder
import|;
end_import

begin_class
specifier|public
class|class
name|PersistentObjectInContextTest
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
specifier|protected
name|ObjectContext
name|createObjectContext
parameter_list|()
block|{
comment|// wrap ClientServerChannel in LocalConnection to enable logging...
name|ClientConnection
name|connector
init|=
operator|new
name|LocalConnection
argument_list|(
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
argument_list|,
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
argument_list|)
decl_stmt|;
return|return
operator|new
name|CayenneContext
argument_list|(
operator|new
name|ClientChannel
argument_list|(
name|connector
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|testResolveToManyReverseResolved
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createObjectContext
argument_list|()
decl_stmt|;
name|ObjectId
name|gid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"MtTable1"
argument_list|,
name|MtTable1
operator|.
name|TABLE1_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|ClientMtTable1
name|t1
init|=
operator|(
name|ClientMtTable1
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
name|gid
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|t1
argument_list|)
expr_stmt|;
name|List
name|t2s
init|=
name|t1
operator|.
name|getTable2Array
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|t2s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|t2s
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
name|t2
init|=
operator|(
name|ClientMtTable2
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|PersistentObjectHolder
name|holder
init|=
operator|(
name|PersistentObjectHolder
operator|)
name|t2
operator|.
name|getTable1Direct
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|holder
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t1
argument_list|,
name|holder
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testToOneRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createObjectContext
argument_list|()
decl_stmt|;
name|ObjectId
name|gid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"MtTable2"
argument_list|,
name|MtTable2
operator|.
name|TABLE2_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|ClientMtTable2
name|mtTable21
init|=
operator|(
name|ClientMtTable2
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
name|gid
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mtTable21
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|mtTable1
init|=
name|mtTable21
operator|.
name|getTable1
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"To one relationship incorrectly resolved to null"
argument_list|,
name|mtTable1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
name|mtTable1
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveToOneReverseResolved
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"prepare"
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createObjectContext
argument_list|()
decl_stmt|;
name|ObjectId
name|gid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"MtTable2"
argument_list|,
name|MtTable2
operator|.
name|TABLE2_ID_PK_COLUMN
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|ClientMtTable2
name|mtTable21
init|=
operator|(
name|ClientMtTable2
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|ObjectIdQuery
argument_list|(
name|gid
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mtTable21
argument_list|)
expr_stmt|;
name|ClientMtTable1
name|mtTable1
init|=
name|mtTable21
operator|.
name|getTable1
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"To one relationship incorrectly resolved to null"
argument_list|,
name|mtTable1
argument_list|)
expr_stmt|;
name|List
name|list
init|=
name|mtTable1
operator|.
name|getTable2Array
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
name|assertTrue
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
comment|// resolve it here...
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
name|it
init|=
name|list
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
name|t2
init|=
operator|(
name|ClientMtTable2
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|PersistentObjectHolder
name|holder
init|=
operator|(
name|PersistentObjectHolder
operator|)
name|t2
operator|.
name|getTable1Direct
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|holder
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|mtTable1
argument_list|,
name|holder
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
name|mtTable1
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

