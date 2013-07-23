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
name|exp
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
name|testdo
operator|.
name|testmap
operator|.
name|BooleanTestEntity
import|;
end_import

begin_class
specifier|public
class|class
name|BooleanExpressionTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testCAY1185
parameter_list|()
block|{
name|Expression
name|expTrue
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"booleanColumn = true"
argument_list|)
decl_stmt|;
name|Expression
name|expFalse
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"booleanColumn = false"
argument_list|)
decl_stmt|;
name|BooleanTestEntity
name|entity
init|=
operator|new
name|BooleanTestEntity
argument_list|()
decl_stmt|;
name|entity
operator|.
name|setBooleanColumn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expTrue
operator|.
name|match
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|expFalse
operator|.
name|match
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setBooleanColumn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|expTrue
operator|.
name|match
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expFalse
operator|.
name|match
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
