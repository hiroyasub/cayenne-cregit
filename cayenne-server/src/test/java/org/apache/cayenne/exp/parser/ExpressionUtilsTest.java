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

begin_class
specifier|public
class|class
name|ExpressionUtilsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testParsePath
parameter_list|()
throws|throws
name|ParseException
block|{
name|ASTPath
name|path
init|=
operator|new
name|ASTObjPath
argument_list|()
decl_stmt|;
name|ExpressionUtils
operator|.
name|parsePath
argument_list|(
name|path
argument_list|,
literal|"a.b.c.d"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a.b.c.d"
argument_list|,
name|path
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|getPathAliases
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParsePathOuterJoin
parameter_list|()
throws|throws
name|ParseException
block|{
name|ASTPath
name|path
init|=
operator|new
name|ASTObjPath
argument_list|()
decl_stmt|;
name|ExpressionUtils
operator|.
name|parsePath
argument_list|(
name|path
argument_list|,
literal|"a.b+.c+.d"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a.b+.c+.d"
argument_list|,
name|path
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|getPathAliases
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParsePathWithAlias
parameter_list|()
throws|throws
name|ParseException
block|{
name|ASTPath
name|path
init|=
operator|new
name|ASTObjPath
argument_list|()
decl_stmt|;
name|ExpressionUtils
operator|.
name|parsePath
argument_list|(
name|path
argument_list|,
literal|"a.b.c#p1.d#p2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a.b.p1.p2"
argument_list|,
name|path
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|path
operator|.
name|getPathAliases
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|path
operator|.
name|getPathAliases
argument_list|()
operator|.
name|get
argument_list|(
literal|"p1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"d"
argument_list|,
name|path
operator|.
name|getPathAliases
argument_list|()
operator|.
name|get
argument_list|(
literal|"p2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParsePathWithAliasAndOuterJoin
parameter_list|()
throws|throws
name|ParseException
block|{
name|ASTPath
name|path
init|=
operator|new
name|ASTObjPath
argument_list|()
decl_stmt|;
name|ExpressionUtils
operator|.
name|parsePath
argument_list|(
name|path
argument_list|,
literal|"a.b+.c#p1+.d#p2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a.b+.p1.p2"
argument_list|,
name|path
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|path
operator|.
name|getPathAliases
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c+"
argument_list|,
name|path
operator|.
name|getPathAliases
argument_list|()
operator|.
name|get
argument_list|(
literal|"p1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"d"
argument_list|,
name|path
operator|.
name|getPathAliases
argument_list|()
operator|.
name|get
argument_list|(
literal|"p2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testParseInvalidPath
parameter_list|()
throws|throws
name|ParseException
block|{
name|ASTPath
name|path
init|=
operator|new
name|ASTObjPath
argument_list|()
decl_stmt|;
name|ExpressionUtils
operator|.
name|parsePath
argument_list|(
name|path
argument_list|,
literal|"a.b.c#p1.d#p1"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

