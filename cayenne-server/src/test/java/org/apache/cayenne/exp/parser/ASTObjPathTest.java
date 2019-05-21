begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Artist
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
name|util
operator|.
name|TstBean
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

begin_class
specifier|public
class|class
name|ASTObjPathTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"x.y"
argument_list|,
operator|new
name|ASTObjPath
argument_list|(
literal|"x.y"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToEJBQL
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"r.x.y"
argument_list|,
operator|new
name|ASTObjPath
argument_list|(
literal|"x.y"
argument_list|)
operator|.
name|toEJBQL
argument_list|(
literal|"r"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToEJBQL_OuterJoin
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"r.x+.y"
argument_list|,
operator|new
name|ASTObjPath
argument_list|(
literal|"x+.y"
argument_list|)
operator|.
name|toEJBQL
argument_list|(
literal|"r"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAppendAsString
parameter_list|()
throws|throws
name|IOException
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
operator|new
name|ASTObjPath
argument_list|(
literal|"x.y"
argument_list|)
operator|.
name|appendAsString
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x.y"
argument_list|,
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluate_DataObject
parameter_list|()
block|{
name|ASTObjPath
name|node
init|=
operator|new
name|ASTObjPath
argument_list|(
literal|"artistName"
argument_list|)
decl_stmt|;
name|Artist
name|a1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|node
operator|.
name|evaluate
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|node
operator|.
name|evaluate
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluate_JavaBean
parameter_list|()
block|{
name|ASTObjPath
name|node
init|=
operator|new
name|ASTObjPath
argument_list|(
literal|"property2"
argument_list|)
decl_stmt|;
name|TstBean
name|b1
init|=
operator|new
name|TstBean
argument_list|()
decl_stmt|;
name|b1
operator|.
name|setProperty2
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|node
operator|.
name|evaluate
argument_list|(
name|b1
argument_list|)
argument_list|)
expr_stmt|;
name|TstBean
name|b2
init|=
operator|new
name|TstBean
argument_list|()
decl_stmt|;
name|b2
operator|.
name|setProperty2
argument_list|(
operator|-
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|3
argument_list|,
name|node
operator|.
name|evaluate
argument_list|(
name|b2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

