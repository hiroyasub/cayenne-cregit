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
operator|.
name|event
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
name|ObjectId
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

begin_class
specifier|public
class|class
name|SnapshotEventTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testRootEvent
parameter_list|()
block|{
name|Object
name|source
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ObjectId
argument_list|>
name|deleted
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ObjectId
argument_list|>
name|invalidated
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|DataRow
argument_list|>
name|modified
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ObjectId
argument_list|>
name|related
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|SnapshotEvent
name|event
init|=
operator|new
name|SnapshotEvent
argument_list|(
name|source
argument_list|,
name|source
argument_list|,
name|modified
argument_list|,
name|deleted
argument_list|,
name|invalidated
argument_list|,
name|related
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|source
argument_list|,
name|event
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|source
argument_list|,
name|event
operator|.
name|getPostedBy
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|deleted
argument_list|,
name|event
operator|.
name|getDeletedIds
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|invalidated
argument_list|,
name|event
operator|.
name|getInvalidatedIds
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|modified
argument_list|,
name|event
operator|.
name|getModifiedDiffs
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|related
argument_list|,
name|event
operator|.
name|getIndirectlyModifiedIds
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

