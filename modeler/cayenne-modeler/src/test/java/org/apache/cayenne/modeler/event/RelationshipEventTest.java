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
name|modeler
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
name|map
operator|.
name|DbRelationship
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
name|Relationship
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
name|event
operator|.
name|RelationshipEvent
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|RelationshipEventTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|src
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Relationship
name|r
init|=
operator|new
name|DbRelationship
argument_list|()
decl_stmt|;
name|r
operator|.
name|setName
argument_list|(
literal|"xyz"
argument_list|)
expr_stmt|;
name|RelationshipEvent
name|e
init|=
operator|new
name|RelationshipEvent
argument_list|(
name|src
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|e
operator|.
name|setRelationship
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|r
argument_list|,
name|e
operator|.
name|getRelationship
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

