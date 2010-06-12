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
name|ArtGroup
import|;
end_import

begin_comment
comment|/**  * Some more tests regarding reflexive relationships, especially related to delete rules  * etc.  The implementation is hairy, and so needs a really good workout.  */
end_comment

begin_class
specifier|public
class|class
name|CDOReflexiveRelTest
extends|extends
name|CayenneDOTestBase
block|{
specifier|private
name|void
name|failWithException
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should not have thrown an exception :"
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddDeleteNoCommit
parameter_list|()
block|{
name|ArtGroup
name|parentGroup
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|parentGroup
operator|.
name|setName
argument_list|(
literal|"parent"
argument_list|)
expr_stmt|;
name|ArtGroup
name|childGroup1
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|childGroup1
operator|.
name|setName
argument_list|(
literal|"child1"
argument_list|)
expr_stmt|;
name|childGroup1
operator|.
name|setToParentGroup
argument_list|(
name|parentGroup
argument_list|)
expr_stmt|;
try|try
block|{
name|ctxt
operator|.
name|deleteObject
argument_list|(
name|parentGroup
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|this
operator|.
name|failWithException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testAddDeleteWithCommit
parameter_list|()
block|{
name|ArtGroup
name|parentGroup
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|parentGroup
operator|.
name|setName
argument_list|(
literal|"parent"
argument_list|)
expr_stmt|;
name|ArtGroup
name|childGroup1
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|childGroup1
operator|.
name|setName
argument_list|(
literal|"child1"
argument_list|)
expr_stmt|;
name|childGroup1
operator|.
name|setToParentGroup
argument_list|(
name|parentGroup
argument_list|)
expr_stmt|;
name|ctxt
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
try|try
block|{
name|ctxt
operator|.
name|deleteObject
argument_list|(
name|parentGroup
argument_list|)
expr_stmt|;
name|ctxt
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|this
operator|.
name|failWithException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testReplaceDeleteNoCommit
parameter_list|()
block|{
name|ArtGroup
name|parentGroup1
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|parentGroup1
operator|.
name|setName
argument_list|(
literal|"parent1"
argument_list|)
expr_stmt|;
name|ArtGroup
name|parentGroup2
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|parentGroup2
operator|.
name|setName
argument_list|(
literal|"parent2"
argument_list|)
expr_stmt|;
name|ArtGroup
name|childGroup1
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|childGroup1
operator|.
name|setName
argument_list|(
literal|"child1"
argument_list|)
expr_stmt|;
name|childGroup1
operator|.
name|setToParentGroup
argument_list|(
name|parentGroup1
argument_list|)
expr_stmt|;
name|childGroup1
operator|.
name|setToParentGroup
argument_list|(
name|parentGroup2
argument_list|)
expr_stmt|;
try|try
block|{
name|ctxt
operator|.
name|deleteObject
argument_list|(
name|parentGroup1
argument_list|)
expr_stmt|;
name|ctxt
operator|.
name|deleteObject
argument_list|(
name|parentGroup2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|this
operator|.
name|failWithException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testReplaceDeleteWithCommit
parameter_list|()
block|{
name|ArtGroup
name|parentGroup1
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|parentGroup1
operator|.
name|setName
argument_list|(
literal|"parent1"
argument_list|)
expr_stmt|;
name|ArtGroup
name|parentGroup2
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|parentGroup2
operator|.
name|setName
argument_list|(
literal|"parent2"
argument_list|)
expr_stmt|;
name|ArtGroup
name|childGroup1
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|childGroup1
operator|.
name|setName
argument_list|(
literal|"child1"
argument_list|)
expr_stmt|;
name|childGroup1
operator|.
name|setToParentGroup
argument_list|(
name|parentGroup1
argument_list|)
expr_stmt|;
name|childGroup1
operator|.
name|setToParentGroup
argument_list|(
name|parentGroup2
argument_list|)
expr_stmt|;
name|ctxt
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
try|try
block|{
name|ctxt
operator|.
name|deleteObject
argument_list|(
name|parentGroup1
argument_list|)
expr_stmt|;
name|ctxt
operator|.
name|deleteObject
argument_list|(
name|parentGroup2
argument_list|)
expr_stmt|;
name|ctxt
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|this
operator|.
name|failWithException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testCommitReplaceCommit
parameter_list|()
block|{
name|ArtGroup
name|parentGroup1
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|parentGroup1
operator|.
name|setName
argument_list|(
literal|"parent1"
argument_list|)
expr_stmt|;
name|ArtGroup
name|parentGroup2
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|parentGroup2
operator|.
name|setName
argument_list|(
literal|"parent2"
argument_list|)
expr_stmt|;
name|ArtGroup
name|childGroup1
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|childGroup1
operator|.
name|setName
argument_list|(
literal|"child1"
argument_list|)
expr_stmt|;
name|childGroup1
operator|.
name|setToParentGroup
argument_list|(
name|parentGroup1
argument_list|)
expr_stmt|;
name|ctxt
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|childGroup1
operator|.
name|setToParentGroup
argument_list|(
name|parentGroup2
argument_list|)
expr_stmt|;
name|ctxt
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testComplexInsertUpdateOrdering
parameter_list|()
block|{
name|ArtGroup
name|parentGroup
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|parentGroup
operator|.
name|setName
argument_list|(
literal|"parent"
argument_list|)
expr_stmt|;
name|ctxt
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|//Check that the update and insert both work write
name|ArtGroup
name|childGroup1
init|=
operator|(
name|ArtGroup
operator|)
name|ctxt
operator|.
name|newObject
argument_list|(
literal|"ArtGroup"
argument_list|)
decl_stmt|;
name|childGroup1
operator|.
name|setName
argument_list|(
literal|"child1"
argument_list|)
expr_stmt|;
name|childGroup1
operator|.
name|setToParentGroup
argument_list|(
name|parentGroup
argument_list|)
expr_stmt|;
name|ctxt
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

