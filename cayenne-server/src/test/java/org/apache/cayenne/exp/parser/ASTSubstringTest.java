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
operator|.
name|parser
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
name|Artist
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
name|assertEquals
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ASTSubstringTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateSubstring
parameter_list|()
throws|throws
name|Exception
block|{
name|ASTObjPath
name|path
init|=
operator|new
name|ASTObjPath
argument_list|(
literal|"artistName"
argument_list|)
decl_stmt|;
name|ASTScalar
name|offset
init|=
operator|new
name|ASTScalar
argument_list|(
operator|(
name|Integer
operator|)
literal|2
argument_list|)
decl_stmt|;
name|ASTScalar
name|length
init|=
operator|new
name|ASTScalar
argument_list|(
operator|(
name|Integer
operator|)
literal|8
argument_list|)
decl_stmt|;
name|ASTSubstring
name|exp
init|=
operator|new
name|ASTSubstring
argument_list|(
name|path
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
decl_stmt|;
name|Artist
name|a
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"1234567890xyz"
argument_list|)
expr_stmt|;
name|Object
name|res
init|=
name|exp
operator|.
name|evaluateNode
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|res
operator|instanceof
name|String
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"34567890"
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

