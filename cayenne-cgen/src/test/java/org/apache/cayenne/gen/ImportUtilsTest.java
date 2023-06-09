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
name|gen
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|assertFalse
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

begin_class
specifier|public
class|class
name|ImportUtilsTest
block|{
specifier|protected
name|ImportUtils
name|importUtils
init|=
literal|null
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|importUtils
operator|=
operator|new
name|ImportUtils
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|importUtils
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetPackageGeneratesPackageStatement
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|packageName
init|=
literal|"org.myPackage"
decl_stmt|;
specifier|final
name|String
name|expectedPackageStatement
init|=
literal|"package "
operator|+
name|packageName
operator|+
literal|";"
decl_stmt|;
name|importUtils
operator|.
name|setPackage
argument_list|(
name|packageName
argument_list|)
expr_stmt|;
name|String
name|generatedStatements
init|=
name|importUtils
operator|.
name|generate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"<"
operator|+
name|generatedStatements
operator|+
literal|"> does not start with<"
operator|+
name|expectedPackageStatement
operator|+
literal|">"
argument_list|,
name|generatedStatements
operator|.
name|startsWith
argument_list|(
name|expectedPackageStatement
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"package statement appears multiple times."
argument_list|,
name|generatedStatements
operator|.
name|lastIndexOf
argument_list|(
name|expectedPackageStatement
argument_list|)
argument_list|,
name|generatedStatements
operator|.
name|lastIndexOf
argument_list|(
name|expectedPackageStatement
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddTypeGeneratesImportStatement
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|type
init|=
literal|"org.myPackage.myType"
decl_stmt|;
specifier|final
name|String
name|expectedImportStatement
init|=
literal|"import "
operator|+
name|type
operator|+
literal|";"
decl_stmt|;
name|importUtils
operator|.
name|addType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|String
name|generatedStatements
init|=
name|importUtils
operator|.
name|generate
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"<"
operator|+
name|generatedStatements
operator|+
literal|"> does not contain<"
operator|+
name|expectedImportStatement
operator|+
literal|">"
argument_list|,
operator|!
name|generatedStatements
operator|.
name|contains
argument_list|(
name|expectedImportStatement
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"import statement appears multiple times."
argument_list|,
name|generatedStatements
operator|.
name|lastIndexOf
argument_list|(
name|expectedImportStatement
argument_list|)
argument_list|,
name|generatedStatements
operator|.
name|lastIndexOf
argument_list|(
name|expectedImportStatement
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddReservedTypeGeneratesNoImportStatement
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|type
init|=
literal|"org.myPackage.myType"
decl_stmt|;
name|importUtils
operator|.
name|addReservedType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|String
name|generatedStatements
init|=
name|importUtils
operator|.
name|generate
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<"
operator|+
name|generatedStatements
operator|+
literal|"> contains<"
operator|+
name|type
operator|+
literal|">"
argument_list|,
operator|-
literal|1
argument_list|,
name|generatedStatements
operator|.
name|indexOf
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddTypeAfterReservedTypeGeneratesNoImportStatement
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|baseType
init|=
literal|"myType"
decl_stmt|;
specifier|final
name|String
name|reservedType
init|=
literal|"org.myPackage."
operator|+
name|baseType
decl_stmt|;
specifier|final
name|String
name|nonReservedType
init|=
literal|"org.myPackage2."
operator|+
name|baseType
decl_stmt|;
name|importUtils
operator|.
name|addReservedType
argument_list|(
name|reservedType
argument_list|)
expr_stmt|;
name|importUtils
operator|.
name|addType
argument_list|(
name|nonReservedType
argument_list|)
expr_stmt|;
name|String
name|generatedStatements
init|=
name|importUtils
operator|.
name|generate
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<"
operator|+
name|generatedStatements
operator|+
literal|"> contains<"
operator|+
name|reservedType
operator|+
literal|">"
argument_list|,
operator|-
literal|1
argument_list|,
name|generatedStatements
operator|.
name|indexOf
argument_list|(
name|reservedType
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<"
operator|+
name|generatedStatements
operator|+
literal|"> contains<"
operator|+
name|nonReservedType
operator|+
literal|">"
argument_list|,
operator|-
literal|1
argument_list|,
name|generatedStatements
operator|.
name|indexOf
argument_list|(
name|nonReservedType
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddTypeAfterPackageReservedTypeGeneratesNoImportStatement
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|baseType
init|=
literal|"myType"
decl_stmt|;
specifier|final
name|String
name|packageType
init|=
literal|"org.myPackage"
decl_stmt|;
specifier|final
name|String
name|reservedType
init|=
name|packageType
operator|+
literal|"."
operator|+
name|baseType
decl_stmt|;
specifier|final
name|String
name|nonReservedType
init|=
literal|"org.myPackage2."
operator|+
name|baseType
decl_stmt|;
name|importUtils
operator|.
name|setPackage
argument_list|(
name|packageType
argument_list|)
expr_stmt|;
name|importUtils
operator|.
name|addReservedType
argument_list|(
name|reservedType
argument_list|)
expr_stmt|;
name|importUtils
operator|.
name|addType
argument_list|(
name|nonReservedType
argument_list|)
expr_stmt|;
name|String
name|generatedStatements
init|=
name|importUtils
operator|.
name|generate
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<"
operator|+
name|generatedStatements
operator|+
literal|"> contains<"
operator|+
name|reservedType
operator|+
literal|">"
argument_list|,
operator|-
literal|1
argument_list|,
name|generatedStatements
operator|.
name|indexOf
argument_list|(
name|reservedType
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<"
operator|+
name|generatedStatements
operator|+
literal|"> contains<"
operator|+
name|nonReservedType
operator|+
literal|">"
argument_list|,
operator|-
literal|1
argument_list|,
name|generatedStatements
operator|.
name|indexOf
argument_list|(
name|nonReservedType
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddTypeAfterTypeGeneratesNoImportStatement
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|baseType
init|=
literal|"myType"
decl_stmt|;
specifier|final
name|String
name|firstType
init|=
literal|"org.myPackage."
operator|+
name|baseType
decl_stmt|;
specifier|final
name|String
name|secondType
init|=
literal|"org.myPackage2."
operator|+
name|baseType
decl_stmt|;
specifier|final
name|String
name|expectedImportStatement
init|=
literal|"import "
operator|+
name|firstType
operator|+
literal|";"
decl_stmt|;
name|importUtils
operator|.
name|addType
argument_list|(
name|firstType
argument_list|)
expr_stmt|;
name|importUtils
operator|.
name|addType
argument_list|(
name|secondType
argument_list|)
expr_stmt|;
name|String
name|generatedStatements
init|=
name|importUtils
operator|.
name|generate
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"<"
operator|+
name|generatedStatements
operator|+
literal|"> does not contain<"
operator|+
name|expectedImportStatement
operator|+
literal|">"
argument_list|,
operator|!
name|generatedStatements
operator|.
name|contains
argument_list|(
name|expectedImportStatement
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"import statement appears multiple times."
argument_list|,
name|generatedStatements
operator|.
name|lastIndexOf
argument_list|(
name|expectedImportStatement
argument_list|)
argument_list|,
name|generatedStatements
operator|.
name|lastIndexOf
argument_list|(
name|expectedImportStatement
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<"
operator|+
name|generatedStatements
operator|+
literal|"> contains<"
operator|+
name|secondType
operator|+
literal|">"
argument_list|,
operator|-
literal|1
argument_list|,
name|generatedStatements
operator|.
name|indexOf
argument_list|(
name|secondType
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddSimilarTypeTwiceBeforeFormatJavaTypeGeneratesCorrectFQNs
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|baseType
init|=
literal|"myType"
decl_stmt|;
specifier|final
name|String
name|firstType
init|=
literal|"org.myPackage."
operator|+
name|baseType
decl_stmt|;
specifier|final
name|String
name|secondType
init|=
literal|"org.myPackage2."
operator|+
name|baseType
decl_stmt|;
name|importUtils
operator|.
name|addType
argument_list|(
name|firstType
argument_list|)
expr_stmt|;
name|importUtils
operator|.
name|addType
argument_list|(
name|secondType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|baseType
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|firstType
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|secondType
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|secondType
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddTypeBeforeFormatJavaTypeGeneratesCorrectFQNs
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|baseType
init|=
literal|"myType"
decl_stmt|;
specifier|final
name|String
name|fullyQualifiedType
init|=
literal|"org.myPackage."
operator|+
name|baseType
decl_stmt|;
name|importUtils
operator|.
name|addType
argument_list|(
name|fullyQualifiedType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|baseType
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|fullyQualifiedType
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddReservedTypeBeforeFormatJavaTypeGeneratesCorrectFQNs
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|baseType
init|=
literal|"myType"
decl_stmt|;
specifier|final
name|String
name|fullyQualifiedType
init|=
literal|"org.myPackage."
operator|+
name|baseType
decl_stmt|;
name|importUtils
operator|.
name|addReservedType
argument_list|(
name|fullyQualifiedType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|fullyQualifiedType
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|fullyQualifiedType
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFormatJavaTypeWithPrimitives
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"int"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
literal|"int"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Integer"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
literal|"int"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"char"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
literal|"char"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Character"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
literal|"java.lang.Character"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"double"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
literal|"java.lang.Double"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Double"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
literal|"java.lang.Double"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a.b.C"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
literal|"a.b.C"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a.b.C"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
literal|"a.b.C"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFormatJavaTypeWithoutAddTypeGeneratesCorrectFQNs
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|baseType
init|=
literal|"myType"
decl_stmt|;
specifier|final
name|String
name|fullyQualifiedType
init|=
literal|"org.myPackage."
operator|+
name|baseType
decl_stmt|;
name|assertEquals
argument_list|(
name|fullyQualifiedType
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|fullyQualifiedType
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPackageFormatJavaTypeWithoutAddTypeGeneratesCorrectFQNs
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|baseType
init|=
literal|"myType"
decl_stmt|;
specifier|final
name|String
name|packageType
init|=
literal|"org.myPackage"
decl_stmt|;
specifier|final
name|String
name|fullyQualifiedType
init|=
name|packageType
operator|+
literal|"."
operator|+
name|baseType
decl_stmt|;
name|importUtils
operator|.
name|setPackage
argument_list|(
name|packageType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|baseType
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|fullyQualifiedType
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFormatJavaType
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"x.X"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
literal|"x.X"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
literal|"java.lang.X"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.x.X"
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
literal|"java.lang.x.X"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJavaLangTypeFormatJavaTypeWithoutAddTypeGeneratesCorrectFQNs
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|baseType
init|=
literal|"myType"
decl_stmt|;
specifier|final
name|String
name|packageType
init|=
literal|"java.lang"
decl_stmt|;
specifier|final
name|String
name|fullyQualifiedType
init|=
name|packageType
operator|+
literal|"."
operator|+
name|baseType
decl_stmt|;
name|assertEquals
argument_list|(
name|baseType
argument_list|,
name|importUtils
operator|.
name|formatJavaType
argument_list|(
name|fullyQualifiedType
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

