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
name|wocompat
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
name|wocompat
operator|.
name|unit
operator|.
name|WOCompatCase
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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|PropertyListSerializationTest
extends|extends
name|WOCompatCase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testListPlist
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|plistFile
init|=
operator|new
name|File
argument_list|(
name|setupTestDirectory
argument_list|(
literal|"testListPlist"
argument_list|)
argument_list|,
literal|"test-array.plist"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"str"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyListSerialization
operator|.
name|propertyListToFile
argument_list|(
name|plistFile
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|readList
init|=
name|PropertyListSerialization
operator|.
name|propertyListFromFile
argument_list|(
name|plistFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|readList
operator|instanceof
name|List
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
argument_list|,
name|readList
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMapPlist
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|plistFile
init|=
operator|new
name|File
argument_list|(
name|setupTestDirectory
argument_list|(
literal|"testMapPlist"
argument_list|)
argument_list|,
literal|"test-map.plist"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
literal|"val"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key2"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyListSerialization
operator|.
name|propertyListToFile
argument_list|(
name|plistFile
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|readMap
init|=
name|PropertyListSerialization
operator|.
name|propertyListFromFile
argument_list|(
name|plistFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|readMap
operator|instanceof
name|Map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
argument_list|,
name|readMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmptyString
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|plistFile
init|=
operator|new
name|File
argument_list|(
name|setupTestDirectory
argument_list|(
literal|"testEmptyString"
argument_list|)
argument_list|,
literal|"test-empty-string.plist"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyListSerialization
operator|.
name|propertyListToFile
argument_list|(
name|plistFile
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|readMap
init|=
name|PropertyListSerialization
operator|.
name|propertyListFromFile
argument_list|(
name|plistFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|readMap
operator|instanceof
name|Map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
argument_list|,
name|readMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStringWithQuotes
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|plistFile
init|=
operator|new
name|File
argument_list|(
name|setupTestDirectory
argument_list|(
literal|"testStringWithQuotes"
argument_list|)
argument_list|,
literal|"test-quotes.plist"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"s\"tr"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyListSerialization
operator|.
name|propertyListToFile
argument_list|(
name|plistFile
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|readList
init|=
name|PropertyListSerialization
operator|.
name|propertyListFromFile
argument_list|(
name|plistFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|readList
operator|instanceof
name|List
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
argument_list|,
name|readList
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNestedPlist
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|plistFile
init|=
operator|new
name|File
argument_list|(
name|setupTestDirectory
argument_list|(
literal|"testNestedPlist"
argument_list|)
argument_list|,
literal|"test-nested.plist"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
literal|"val"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key2"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"str"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key3"
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyListSerialization
operator|.
name|propertyListToFile
argument_list|(
name|plistFile
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|readMap
init|=
name|PropertyListSerialization
operator|.
name|propertyListFromFile
argument_list|(
name|plistFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|readMap
operator|instanceof
name|Map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
argument_list|,
name|readMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStringWithSpaces
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|plistFile
init|=
operator|new
name|File
argument_list|(
name|setupTestDirectory
argument_list|(
literal|"testStringWithSpaces"
argument_list|)
argument_list|,
literal|"test-spaces.plist"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"s tr"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyListSerialization
operator|.
name|propertyListToFile
argument_list|(
name|plistFile
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|readList
init|=
name|PropertyListSerialization
operator|.
name|propertyListFromFile
argument_list|(
name|plistFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|readList
operator|instanceof
name|List
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
argument_list|,
name|readList
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStringWithBraces
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|plistFile
init|=
operator|new
name|File
argument_list|(
name|setupTestDirectory
argument_list|(
literal|"testStringWithBraces"
argument_list|)
argument_list|,
literal|"test-braces.plist"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"s{t)r"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyListSerialization
operator|.
name|propertyListToFile
argument_list|(
name|plistFile
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|readList
init|=
name|PropertyListSerialization
operator|.
name|propertyListFromFile
argument_list|(
name|plistFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|readList
operator|instanceof
name|List
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
argument_list|,
name|readList
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStringWithSlashes
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|plistFile
init|=
operator|new
name|File
argument_list|(
name|setupTestDirectory
argument_list|(
literal|"testStringWithSlashes"
argument_list|)
argument_list|,
literal|"test-slashes.plist"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"s/t\\r"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyListSerialization
operator|.
name|propertyListToFile
argument_list|(
name|plistFile
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|readList
init|=
name|PropertyListSerialization
operator|.
name|propertyListFromFile
argument_list|(
name|plistFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|readList
operator|instanceof
name|List
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
argument_list|,
name|readList
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStringWithQuotes1
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|plistFile
init|=
operator|new
name|File
argument_list|(
name|setupTestDirectory
argument_list|(
literal|"testStringWithQuotes1"
argument_list|)
argument_list|,
literal|"test-quotes1.plist"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"like"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"\"*003*\""
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyListSerialization
operator|.
name|propertyListToFile
argument_list|(
name|plistFile
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|readList
init|=
name|PropertyListSerialization
operator|.
name|propertyListFromFile
argument_list|(
name|plistFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|readList
operator|instanceof
name|List
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
argument_list|,
name|readList
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStringWithPlusMinus
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|plistFile
init|=
operator|new
name|File
argument_list|(
name|setupTestDirectory
argument_list|(
literal|"testStringWithPlusMinus"
argument_list|)
argument_list|,
literal|"test-plus-minus.plist"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"a+b"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"a-b"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"a+-b"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyListSerialization
operator|.
name|propertyListToFile
argument_list|(
name|plistFile
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|readList
init|=
name|PropertyListSerialization
operator|.
name|propertyListFromFile
argument_list|(
name|plistFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|readList
operator|instanceof
name|List
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
argument_list|,
name|readList
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStringWithLessGreater
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|plistFile
init|=
operator|new
name|File
argument_list|(
name|setupTestDirectory
argument_list|(
literal|"testStringWithLessGreater"
argument_list|)
argument_list|,
literal|"test-less-greater.plist"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"a<b"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"a>b"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"a<>b"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyListSerialization
operator|.
name|propertyListToFile
argument_list|(
name|plistFile
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|plistFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|readList
init|=
name|PropertyListSerialization
operator|.
name|propertyListFromFile
argument_list|(
name|plistFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|readList
operator|instanceof
name|List
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
argument_list|,
name|readList
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

