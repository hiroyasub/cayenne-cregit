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
name|project
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

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
name|DbAttribute
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
name|DbEntity
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
name|ObjAttribute
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
name|map
operator|.
name|ObjRelationship
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
name|project
operator|.
name|ApplicationProject
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ValidatorTestBase
extends|extends
name|TestCase
block|{
specifier|protected
specifier|static
name|int
name|counter
init|=
literal|1
decl_stmt|;
specifier|protected
name|Validator
name|validator
decl_stmt|;
specifier|protected
name|ApplicationProject
name|project
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|project
operator|=
operator|new
name|ApplicationProject
argument_list|(
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.dir"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|validator
operator|=
operator|new
name|Validator
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertValidator
parameter_list|(
name|int
name|errorLevel
parameter_list|)
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|errorLevel
argument_list|,
name|validator
operator|.
name|getMaxSeverity
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|DbRelationship
name|buildValidDbRelationship
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|DbEntity
name|src
init|=
operator|new
name|DbEntity
argument_list|(
literal|"e1"
operator|+
name|counter
operator|++
argument_list|)
decl_stmt|;
name|DbEntity
name|target
init|=
operator|new
name|DbEntity
argument_list|(
literal|"e2"
operator|+
name|counter
operator|++
argument_list|)
decl_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|src
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|DbRelationship
name|dr1
init|=
operator|new
name|DbRelationship
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|dr1
operator|.
name|setSourceEntity
argument_list|(
name|src
argument_list|)
expr_stmt|;
name|dr1
operator|.
name|setTargetEntity
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|src
operator|.
name|addRelationship
argument_list|(
name|dr1
argument_list|)
expr_stmt|;
return|return
name|dr1
return|;
block|}
specifier|protected
name|ObjRelationship
name|buildValidObjRelationship
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|DbRelationship
name|dr1
init|=
name|buildValidDbRelationship
argument_list|(
name|map
argument_list|,
literal|"d"
operator|+
name|name
argument_list|)
decl_stmt|;
name|ObjEntity
name|src
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"ey"
operator|+
name|counter
operator|++
argument_list|)
decl_stmt|;
name|src
operator|.
name|setClassName
argument_list|(
literal|"src"
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|src
argument_list|)
expr_stmt|;
name|src
operator|.
name|setDbEntity
argument_list|(
operator|(
name|DbEntity
operator|)
name|dr1
operator|.
name|getSourceEntity
argument_list|()
argument_list|)
expr_stmt|;
name|ObjEntity
name|target
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"oey"
operator|+
name|counter
operator|++
argument_list|)
decl_stmt|;
name|target
operator|.
name|setClassName
argument_list|(
literal|"target"
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|target
operator|.
name|setDbEntity
argument_list|(
operator|(
name|DbEntity
operator|)
name|dr1
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
expr_stmt|;
name|ObjRelationship
name|r1
init|=
operator|new
name|ObjRelationship
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|r1
operator|.
name|setTargetEntity
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|src
operator|.
name|addRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
name|r1
operator|.
name|addDbRelationship
argument_list|(
name|dr1
argument_list|)
expr_stmt|;
return|return
name|r1
return|;
block|}
specifier|protected
name|ObjAttribute
name|buildValidObjAttribute
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|DbAttribute
name|a1
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|a1
operator|.
name|setName
argument_list|(
literal|"d"
operator|+
name|name
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setType
argument_list|(
name|Types
operator|.
name|CHAR
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setMaxLength
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|DbEntity
name|e1
init|=
operator|new
name|DbEntity
argument_list|(
literal|"ex"
operator|+
name|counter
operator|++
argument_list|)
decl_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|e1
operator|.
name|addAttribute
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|ObjEntity
name|oe1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"oex"
operator|+
name|counter
operator|++
argument_list|)
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|oe1
argument_list|)
expr_stmt|;
name|oe1
operator|.
name|setDbEntity
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|ObjAttribute
name|oa1
init|=
operator|new
name|ObjAttribute
argument_list|(
name|name
argument_list|,
literal|"java.lang.Integer"
argument_list|,
name|oe1
argument_list|)
decl_stmt|;
name|oe1
operator|.
name|addAttribute
argument_list|(
name|oa1
argument_list|)
expr_stmt|;
name|oa1
operator|.
name|setDbAttribute
argument_list|(
name|a1
argument_list|)
expr_stmt|;
return|return
name|oa1
return|;
block|}
comment|// dummy test case so junit does not complain about missing tests
specifier|public
name|void
name|testNoOperation
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

