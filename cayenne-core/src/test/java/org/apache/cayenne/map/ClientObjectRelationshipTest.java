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
name|util
operator|.
name|Util
import|;
end_import

begin_class
specifier|public
class|class
name|ClientObjectRelationshipTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSerializability
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientObjRelationship
name|r1
init|=
operator|new
name|ClientObjRelationship
argument_list|(
literal|"r1"
argument_list|,
literal|"rr1"
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ClientObjRelationship
name|r2
init|=
operator|(
name|ClientObjRelationship
operator|)
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|r1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|r1
operator|.
name|getName
argument_list|()
argument_list|,
name|r2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r1
operator|.
name|getReverseRelationship
argument_list|()
argument_list|,
name|r2
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r1
operator|.
name|isToMany
argument_list|()
argument_list|,
name|r2
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r1
operator|.
name|isReadOnly
argument_list|()
argument_list|,
name|r2
operator|.
name|isReadOnly
argument_list|()
argument_list|)
expr_stmt|;
name|ClientObjRelationship
name|r3
init|=
operator|new
name|ClientObjRelationship
argument_list|(
literal|"r3"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ClientObjRelationship
name|r4
init|=
operator|(
name|ClientObjRelationship
operator|)
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|r3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|r3
operator|.
name|getName
argument_list|()
argument_list|,
name|r4
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|r4
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r3
operator|.
name|isToMany
argument_list|()
argument_list|,
name|r4
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r3
operator|.
name|isReadOnly
argument_list|()
argument_list|,
name|r4
operator|.
name|isReadOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

