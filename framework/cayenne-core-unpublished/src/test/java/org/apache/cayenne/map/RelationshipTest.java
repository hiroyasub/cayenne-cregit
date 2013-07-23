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

begin_class
specifier|public
class|class
name|RelationshipTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testName
parameter_list|()
throws|throws
name|Exception
block|{
name|Relationship
name|rel
init|=
operator|new
name|MockRelationship
argument_list|()
decl_stmt|;
name|String
name|tstName
init|=
literal|"tst_name"
decl_stmt|;
name|rel
operator|.
name|setName
argument_list|(
name|tstName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstName
argument_list|,
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSourceEntity
parameter_list|()
block|{
name|Relationship
name|rel
init|=
operator|new
name|MockRelationship
argument_list|()
decl_stmt|;
name|Entity
name|tstEntity
init|=
operator|new
name|MockEntity
argument_list|()
decl_stmt|;
name|rel
operator|.
name|setSourceEntity
argument_list|(
name|tstEntity
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|tstEntity
argument_list|,
name|rel
operator|.
name|getSourceEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTargetEntity
parameter_list|()
block|{
name|Relationship
name|rel
init|=
operator|new
name|MockRelationship
argument_list|()
decl_stmt|;
name|Entity
name|tstEntity
init|=
operator|new
name|MockEntity
argument_list|()
decl_stmt|;
name|tstEntity
operator|.
name|setName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setTargetEntity
argument_list|(
name|tstEntity
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"abc"
argument_list|,
name|rel
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTargetEntityName
parameter_list|()
block|{
name|Relationship
name|rel
init|=
operator|new
name|MockRelationship
argument_list|()
decl_stmt|;
name|rel
operator|.
name|setTargetEntityName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"abc"
argument_list|,
name|rel
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
