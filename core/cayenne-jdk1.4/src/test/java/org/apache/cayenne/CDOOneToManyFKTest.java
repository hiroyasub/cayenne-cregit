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
name|Arrays
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
name|testdo
operator|.
name|relationship
operator|.
name|ToManyFkDep
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
name|relationship
operator|.
name|ToManyFkRoot
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
name|relationship
operator|.
name|ToManyRoot2
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
name|RelationshipCase
import|;
end_import

begin_comment
comment|// TODO: this mapping scenario is really unsupported ... this is just an attempt at
end_comment

begin_comment
comment|// partial solution
end_comment

begin_class
specifier|public
class|class
name|CDOOneToManyFKTest
extends|extends
name|RelationshipCase
block|{
specifier|public
name|void
name|testReadRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|ToManyRoot2
name|src2
init|=
operator|(
name|ToManyRoot2
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|ToManyRoot2
operator|.
name|class
argument_list|)
decl_stmt|;
name|ToManyFkRoot
name|src
init|=
operator|(
name|ToManyFkRoot
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|ToManyFkRoot
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// this should go away when such mapping becomes fully supported
name|src
operator|.
name|setDepId
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|ToManyFkDep
name|target
init|=
operator|(
name|ToManyFkDep
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|ToManyFkDep
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// this should go away when such mapping becomes fully supported
name|target
operator|.
name|setDepId
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|target
operator|.
name|setRoot2
argument_list|(
name|src2
argument_list|)
expr_stmt|;
name|src
operator|.
name|addToDeps
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|src
block|,
name|target
block|,
name|src2
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|ToManyFkRoot
name|src1
init|=
operator|(
name|ToManyFkRoot
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|src
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|src1
operator|.
name|getDeps
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|src1
operator|.
name|getDeps
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// resolve HOLLOW
name|assertSame
argument_list|(
name|src1
argument_list|,
operator|(
operator|(
name|ToManyFkDep
operator|)
name|src1
operator|.
name|getDeps
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|src1
block|,
name|src1
operator|.
name|getDeps
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|ToManyFkDep
name|target2
init|=
operator|(
name|ToManyFkDep
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|target
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|target2
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
comment|// resolve HOLLOW
name|assertSame
argument_list|(
name|target2
argument_list|,
name|target2
operator|.
name|getRoot
argument_list|()
operator|.
name|getDeps
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

