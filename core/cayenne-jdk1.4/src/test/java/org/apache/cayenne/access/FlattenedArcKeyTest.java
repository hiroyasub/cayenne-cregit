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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|map
operator|.
name|MockObjRelationship
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|FlattenedArcKeyTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testAttributes
parameter_list|()
block|{
name|ObjectId
name|src
init|=
operator|new
name|ObjectId
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
name|ObjectId
name|target
init|=
operator|new
name|ObjectId
argument_list|(
literal|"Y"
argument_list|)
decl_stmt|;
name|MockObjRelationship
name|r1
init|=
operator|new
name|MockObjRelationship
argument_list|(
literal|"r1"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|setReverseRelationship
argument_list|(
operator|new
name|MockObjRelationship
argument_list|(
literal|"r2"
argument_list|)
argument_list|)
expr_stmt|;
name|FlattenedArcKey
name|update
init|=
operator|new
name|FlattenedArcKey
argument_list|(
name|src
argument_list|,
name|target
argument_list|,
name|r1
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|src
argument_list|,
name|update
operator|.
name|sourceId
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|target
argument_list|,
name|update
operator|.
name|destinationId
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|r1
argument_list|,
name|update
operator|.
name|relationship
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|r1
operator|.
name|getReverseRelationship
argument_list|()
argument_list|,
name|update
operator|.
name|reverseRelationship
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|update
operator|.
name|isBidirectional
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
name|ObjectId
name|src
init|=
operator|new
name|ObjectId
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
name|ObjectId
name|target
init|=
operator|new
name|ObjectId
argument_list|(
literal|"Y"
argument_list|)
decl_stmt|;
name|MockObjRelationship
name|r1
init|=
operator|new
name|MockObjRelationship
argument_list|(
literal|"r1"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|setReverseRelationship
argument_list|(
operator|new
name|MockObjRelationship
argument_list|(
literal|"r2"
argument_list|)
argument_list|)
expr_stmt|;
name|FlattenedArcKey
name|update
init|=
operator|new
name|FlattenedArcKey
argument_list|(
name|src
argument_list|,
name|target
argument_list|,
name|r1
argument_list|)
decl_stmt|;
name|FlattenedArcKey
name|update1
init|=
operator|new
name|FlattenedArcKey
argument_list|(
name|target
argument_list|,
name|src
argument_list|,
name|r1
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
decl_stmt|;
name|FlattenedArcKey
name|update2
init|=
operator|new
name|FlattenedArcKey
argument_list|(
name|target
argument_list|,
name|src
argument_list|,
operator|new
name|MockObjRelationship
argument_list|(
literal|"r3"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|update
operator|.
name|equals
argument_list|(
name|update1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|update
operator|.
name|equals
argument_list|(
name|update2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

