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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|parser
operator|.
name|PatternMatchNode
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
name|reflect
operator|.
name|TstJavaBean
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
name|reflect
operator|.
name|UnresolvablePathException
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
name|java
operator|.
name|util
operator|.
name|List
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
name|assertNull
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|PropertyTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testIn
parameter_list|()
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|p
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"x.y"
argument_list|)
decl_stmt|;
name|Expression
name|e1
init|=
name|p
operator|.
name|in
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"x.y in (\"a\")"
argument_list|,
name|e1
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|e2
init|=
name|p
operator|.
name|in
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"x.y in (\"a\", \"b\")"
argument_list|,
name|e2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Expression
name|e3
init|=
name|p
operator|.
name|in
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"x.y in (\"a\", \"b\")"
argument_list|,
name|e3
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
name|testGetFrom
parameter_list|()
block|{
name|TstJavaBean
name|bean
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setIntField
argument_list|(
literal|7
argument_list|)
expr_stmt|;
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|INT_FIELD
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"intField"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|7
argument_list|)
argument_list|,
name|INT_FIELD
operator|.
name|getFrom
argument_list|(
name|bean
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetFromNestedProperty
parameter_list|()
block|{
name|TstJavaBean
name|bean
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|TstJavaBean
name|nestedBean
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|nestedBean
operator|.
name|setIntField
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setObjectField
argument_list|(
name|nestedBean
argument_list|)
expr_stmt|;
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|OBJECT_FIELD_INT_FIELD
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"objectField.intField"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|7
argument_list|)
argument_list|,
name|OBJECT_FIELD_INT_FIELD
operator|.
name|getFrom
argument_list|(
name|bean
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetFromNestedNull
parameter_list|()
block|{
name|TstJavaBean
name|bean
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setObjectField
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Property
argument_list|<
name|Integer
argument_list|>
name|OBJECT_FIELD_INT_FIELD
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"objectField.intField"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|OBJECT_FIELD_INT_FIELD
operator|.
name|getFrom
argument_list|(
name|bean
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetFromAll
parameter_list|()
block|{
name|TstJavaBean
name|bean
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setIntField
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|TstJavaBean
name|bean2
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|bean2
operator|.
name|setIntField
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|TstJavaBean
argument_list|>
name|beans
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|bean
argument_list|,
name|bean2
argument_list|)
decl_stmt|;
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|INT_FIELD
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"intField"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|7
argument_list|,
literal|8
argument_list|)
argument_list|,
name|INT_FIELD
operator|.
name|getFromAll
argument_list|(
name|beans
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetIn
parameter_list|()
block|{
name|TstJavaBean
name|bean
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|INT_FIELD
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"intField"
argument_list|)
decl_stmt|;
name|INT_FIELD
operator|.
name|setIn
argument_list|(
name|bean
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|bean
operator|.
name|getIntField
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetInNestedProperty
parameter_list|()
block|{
name|TstJavaBean
name|bean
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setObjectField
argument_list|(
operator|new
name|TstJavaBean
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|OBJECT_FIELD_INT_FIELD
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"objectField.intField"
argument_list|)
decl_stmt|;
name|OBJECT_FIELD_INT_FIELD
operator|.
name|setIn
argument_list|(
name|bean
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
operator|(
operator|(
name|TstJavaBean
operator|)
name|bean
operator|.
name|getObjectField
argument_list|()
operator|)
operator|.
name|getIntField
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetInNestedNull
parameter_list|()
block|{
name|TstJavaBean
name|bean
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setObjectField
argument_list|(
literal|null
argument_list|)
expr_stmt|;
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|OBJECT_FIELD_INT_FIELD
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"objectField.intField"
argument_list|)
decl_stmt|;
try|try
block|{
name|OBJECT_FIELD_INT_FIELD
operator|.
name|setIn
argument_list|(
name|bean
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Throwable
name|rootException
init|=
name|Util
operator|.
name|unwindException
argument_list|(
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|rootException
operator|instanceof
name|UnresolvablePathException
operator|)
condition|)
block|{
name|fail
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetInAll
parameter_list|()
block|{
name|TstJavaBean
name|bean
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|TstJavaBean
name|bean2
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|TstJavaBean
argument_list|>
name|beans
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|bean
argument_list|,
name|bean2
argument_list|)
decl_stmt|;
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|INT_FIELD
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"intField"
argument_list|)
decl_stmt|;
name|INT_FIELD
operator|.
name|setInAll
argument_list|(
name|beans
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|bean
operator|.
name|getIntField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|bean2
operator|.
name|getIntField
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|INT_FIELD
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"intField"
argument_list|)
decl_stmt|;
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|INT_FIELD2
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"intField"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|INT_FIELD
operator|!=
name|INT_FIELD2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|INT_FIELD
operator|.
name|equals
argument_list|(
name|INT_FIELD2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHashCode
parameter_list|()
block|{
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|INT_FIELD
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"intField"
argument_list|)
decl_stmt|;
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|INT_FIELD2
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"intField"
argument_list|)
decl_stmt|;
specifier|final
name|Property
argument_list|<
name|Long
argument_list|>
name|LONG_FIELD
init|=
operator|new
name|Property
argument_list|<
name|Long
argument_list|>
argument_list|(
literal|"longField"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|INT_FIELD
operator|.
name|hashCode
argument_list|()
operator|==
name|INT_FIELD2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|INT_FIELD
operator|.
name|hashCode
argument_list|()
operator|!=
name|LONG_FIELD
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOuter
parameter_list|()
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|inner
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"xyz"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"xyz+"
argument_list|,
name|inner
operator|.
name|outer
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Property
argument_list|<
name|String
argument_list|>
name|inner1
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"xyz.xxx"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"xyz.xxx+"
argument_list|,
name|inner1
operator|.
name|outer
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Property
argument_list|<
name|String
argument_list|>
name|outer
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"xyz+"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"xyz+"
argument_list|,
name|outer
operator|.
name|outer
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLike
parameter_list|()
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|p
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"prop"
argument_list|)
decl_stmt|;
name|Expression
name|e
init|=
name|p
operator|.
name|like
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"prop like \"abc\""
argument_list|,
name|e
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
name|testLikeIgnoreCase
parameter_list|()
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|p
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"prop"
argument_list|)
decl_stmt|;
name|Expression
name|e
init|=
name|p
operator|.
name|likeIgnoreCase
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"prop likeIgnoreCase \"abc\""
argument_list|,
name|e
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
name|testLike_NoEscape
parameter_list|()
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|p
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"prop"
argument_list|)
decl_stmt|;
name|Expression
name|e
init|=
name|p
operator|.
name|like
argument_list|(
literal|"ab%c"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"prop like \"ab%c\""
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
operator|(
operator|(
name|PatternMatchNode
operator|)
name|e
operator|)
operator|.
name|getEscapeChar
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContains
parameter_list|()
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|p
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"prop"
argument_list|)
decl_stmt|;
name|Expression
name|e
init|=
name|p
operator|.
name|contains
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"prop like \"%abc%\""
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
operator|(
operator|(
name|PatternMatchNode
operator|)
name|e
operator|)
operator|.
name|getEscapeChar
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStartsWith
parameter_list|()
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|p
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"prop"
argument_list|)
decl_stmt|;
name|Expression
name|e
init|=
name|p
operator|.
name|startsWith
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"prop like \"abc%\""
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
operator|(
operator|(
name|PatternMatchNode
operator|)
name|e
operator|)
operator|.
name|getEscapeChar
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEndsWith
parameter_list|()
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|p
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"prop"
argument_list|)
decl_stmt|;
name|Expression
name|e
init|=
name|p
operator|.
name|endsWith
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"prop like \"%abc\""
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
operator|(
operator|(
name|PatternMatchNode
operator|)
name|e
operator|)
operator|.
name|getEscapeChar
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContains_Escape1
parameter_list|()
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|p
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"prop"
argument_list|)
decl_stmt|;
name|Expression
name|e
init|=
name|p
operator|.
name|contains
argument_list|(
literal|"a%bc"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"prop like \"%a!%bc%\""
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'!'
argument_list|,
operator|(
operator|(
name|PatternMatchNode
operator|)
name|e
operator|)
operator|.
name|getEscapeChar
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContains_Escape2
parameter_list|()
block|{
name|Property
argument_list|<
name|String
argument_list|>
name|p
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"prop"
argument_list|)
decl_stmt|;
name|Expression
name|e
init|=
name|p
operator|.
name|contains
argument_list|(
literal|"a_!bc"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"prop like \"%a#_!bc%\""
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'#'
argument_list|,
operator|(
operator|(
name|PatternMatchNode
operator|)
name|e
operator|)
operator|.
name|getEscapeChar
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

