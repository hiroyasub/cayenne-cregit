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
name|util
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
name|CayenneException
import|;
end_import

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
name|io
operator|.
name|FileWriter
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
name|assertSame
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
name|UtilTest
block|{
specifier|private
name|File
name|fTmpFileInCurrentDir
decl_stmt|;
specifier|private
name|String
name|fTmpFileName
decl_stmt|;
specifier|private
name|File
name|fTmpFileCopy
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
name|fTmpFileName
operator|=
literal|"."
operator|+
name|File
operator|.
name|separator
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|".tmp"
expr_stmt|;
name|fTmpFileInCurrentDir
operator|=
operator|new
name|File
argument_list|(
name|fTmpFileName
argument_list|)
expr_stmt|;
comment|// right some garbage to the temp file, so that it is not empty
name|FileWriter
name|fout
init|=
operator|new
name|FileWriter
argument_list|(
name|fTmpFileInCurrentDir
argument_list|)
decl_stmt|;
name|fout
operator|.
name|write
argument_list|(
literal|"This is total garbage.."
argument_list|)
expr_stmt|;
name|fout
operator|.
name|close
argument_list|()
expr_stmt|;
name|fTmpFileCopy
operator|=
operator|new
name|File
argument_list|(
name|fTmpFileName
operator|+
literal|".copy"
argument_list|)
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
if|if
condition|(
operator|!
name|fTmpFileInCurrentDir
operator|.
name|delete
argument_list|()
condition|)
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Error deleting temporary file: "
operator|+
name|fTmpFileInCurrentDir
argument_list|)
throw|;
if|if
condition|(
name|fTmpFileCopy
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|fTmpFileCopy
operator|.
name|delete
argument_list|()
condition|)
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Error deleting temporary file: "
operator|+
name|fTmpFileCopy
argument_list|)
throw|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetJavaClass
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|byte
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Util
operator|.
name|getJavaClass
argument_list|(
literal|"byte"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|byte
index|[]
operator|.
expr|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Util
operator|.
name|getJavaClass
argument_list|(
literal|"byte[]"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
index|[]
operator|.
expr|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Util
operator|.
name|getJavaClass
argument_list|(
literal|"java.lang.String[]"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|UtilTest
index|[
literal|0
index|]
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|Util
operator|.
name|getJavaClass
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"[]"
argument_list|)
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
name|testToMap
parameter_list|()
block|{
name|Object
index|[]
name|keys
init|=
operator|new
name|Object
index|[]
block|{
literal|"a"
block|,
literal|"b"
block|}
decl_stmt|;
name|Object
index|[]
name|values
init|=
operator|new
name|Object
index|[]
block|{
literal|"1"
block|,
literal|"2"
block|}
decl_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|Util
operator|.
name|toMap
argument_list|(
name|keys
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
comment|// check that map is mutable
name|map
operator|.
name|put
argument_list|(
literal|"c"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
comment|// check that two null maps work
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|emptyMap
init|=
name|Util
operator|.
name|toMap
argument_list|(
literal|null
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|emptyMap
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|emptyMap
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
comment|// check arrays with different sizes
name|Object
index|[]
name|values2
init|=
operator|new
name|Object
index|[]
block|{
literal|"1"
block|}
decl_stmt|;
try|try
block|{
name|Util
operator|.
name|toMap
argument_list|(
name|keys
argument_list|,
name|values2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"must have thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStripLineBreaks
parameter_list|()
block|{
comment|// no breaks
name|assertEquals
argument_list|(
literal|"PnMusdkams34 H AnYtk M"
argument_list|,
name|Util
operator|.
name|stripLineBreaks
argument_list|(
literal|"PnMusdkams34 H AnYtk M"
argument_list|,
literal|'A'
argument_list|)
argument_list|)
expr_stmt|;
comment|// Windows
name|assertEquals
argument_list|(
literal|"TyusdsdsdQaAbAc"
argument_list|,
name|Util
operator|.
name|stripLineBreaks
argument_list|(
literal|"TyusdsdsdQa\r\nb\r\nc"
argument_list|,
literal|'A'
argument_list|)
argument_list|)
expr_stmt|;
comment|// Mac
name|assertEquals
argument_list|(
literal|"aBbBc"
argument_list|,
name|Util
operator|.
name|stripLineBreaks
argument_list|(
literal|"a\rb\rc"
argument_list|,
literal|'B'
argument_list|)
argument_list|)
expr_stmt|;
comment|// UNIX
name|assertEquals
argument_list|(
literal|"aCbCc"
argument_list|,
name|Util
operator|.
name|stripLineBreaks
argument_list|(
literal|"a\nb\nc"
argument_list|,
literal|'C'
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCloneViaSerialization
parameter_list|()
throws|throws
name|Exception
block|{
comment|// need a special subclass of Object to make "clone" method public
name|MockSerializable
name|o1
init|=
operator|new
name|MockSerializable
argument_list|()
decl_stmt|;
name|Object
name|o2
init|=
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|o1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o1
operator|!=
name|o2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPackagePath1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedPath
init|=
literal|"org/apache/cayenne/util"
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedPath
argument_list|,
name|Util
operator|.
name|getPackagePath
argument_list|(
name|UtilTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPackagePath2
parameter_list|()
throws|throws
name|Exception
block|{
comment|// inner class
class|class
name|TmpTest
extends|extends
name|Object
block|{         }
name|String
name|expectedPath
init|=
literal|"org/apache/cayenne/util"
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedPath
argument_list|,
name|Util
operator|.
name|getPackagePath
argument_list|(
name|TmpTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPackagePath3
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|Util
operator|.
name|getPackagePath
argument_list|(
literal|"ClassWithNoPackage"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsEmptyString1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|Util
operator|.
name|isEmptyString
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsEmptyString2
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|Util
operator|.
name|isEmptyString
argument_list|(
literal|"  "
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsEmptyString3
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|Util
operator|.
name|isEmptyString
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBackslashFix
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|strBefore
init|=
literal|"abcd\\12345\\"
decl_stmt|;
name|String
name|strAfter
init|=
literal|"abcd/12345/"
decl_stmt|;
name|assertEquals
argument_list|(
name|strAfter
argument_list|,
name|Util
operator|.
name|substBackslashes
argument_list|(
name|strBefore
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNullSafeEquals
parameter_list|()
throws|throws
name|Exception
block|{
comment|// need a special subclass of Object to make "clone" method public
class|class
name|CloneableObject
implements|implements
name|Cloneable
block|{
annotation|@
name|Override
specifier|public
name|Object
name|clone
parameter_list|()
throws|throws
name|CloneNotSupportedException
block|{
return|return
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
return|return
literal|false
return|;
comment|// for the purpose of this test
comment|// all objects of this class considered equal
comment|// (since they carry no state)
return|return
name|obj
operator|.
name|getClass
argument_list|()
operator|==
name|this
operator|.
name|getClass
argument_list|()
return|;
block|}
block|}
name|CloneableObject
name|o1
init|=
operator|new
name|CloneableObject
argument_list|()
decl_stmt|;
name|Object
name|o2
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|o3
init|=
name|o1
operator|.
name|clone
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|o3
operator|.
name|equals
argument_list|(
name|o1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|o1
argument_list|,
name|o1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|o1
argument_list|,
name|o3
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|o1
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
literal|null
argument_list|,
name|o1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractFileExtension1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|fullName
init|=
literal|"n.ext"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ext"
argument_list|,
name|Util
operator|.
name|extractFileExtension
argument_list|(
name|fullName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractFileExtension2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|fullName
init|=
literal|"n"
decl_stmt|;
name|assertNull
argument_list|(
name|Util
operator|.
name|extractFileExtension
argument_list|(
name|fullName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractFileExtension3
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|fullName
init|=
literal|".ext"
decl_stmt|;
name|assertNull
argument_list|(
name|Util
operator|.
name|extractFileExtension
argument_list|(
name|fullName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStripFileExtension1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|fullName
init|=
literal|"n.ext"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"n"
argument_list|,
name|Util
operator|.
name|stripFileExtension
argument_list|(
name|fullName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStripFileExtension2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|fullName
init|=
literal|"n"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"n"
argument_list|,
name|Util
operator|.
name|stripFileExtension
argument_list|(
name|fullName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStripFileExtension3
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|fullName
init|=
literal|".ext"
decl_stmt|;
name|assertEquals
argument_list|(
literal|".ext"
argument_list|,
name|Util
operator|.
name|stripFileExtension
argument_list|(
name|fullName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncodeXmlAttribute1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|unencoded
init|=
literal|"normalstring"
decl_stmt|;
name|assertEquals
argument_list|(
name|unencoded
argument_list|,
name|Util
operator|.
name|encodeXmlAttribute
argument_list|(
name|unencoded
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncodeXmlAttribute2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|unencoded
init|=
literal|"<a>"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"&lt;a&gt;"
argument_list|,
name|Util
operator|.
name|encodeXmlAttribute
argument_list|(
name|unencoded
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncodeXmlAttribute3
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|unencoded
init|=
literal|"a&b"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a&amp;b"
argument_list|,
name|Util
operator|.
name|encodeXmlAttribute
argument_list|(
name|unencoded
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnwindException1
parameter_list|()
throws|throws
name|Exception
block|{
name|Throwable
name|e
init|=
operator|new
name|Throwable
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|e
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnwindException2
parameter_list|()
throws|throws
name|Exception
block|{
name|CayenneException
name|e
init|=
operator|new
name|CayenneException
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|e
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnwindException3
parameter_list|()
throws|throws
name|Exception
block|{
name|Throwable
name|root
init|=
operator|new
name|Throwable
argument_list|()
decl_stmt|;
name|CayenneException
name|e
init|=
operator|new
name|CayenneException
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|root
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrettyTrim1
parameter_list|()
throws|throws
name|Exception
block|{
comment|// size is too short, must throw
try|try
block|{
name|Util
operator|.
name|prettyTrim
argument_list|(
literal|"abc"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrettyTrim2
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|Util
operator|.
name|prettyTrim
argument_list|(
literal|"123"
argument_list|,
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123456"
argument_list|,
name|Util
operator|.
name|prettyTrim
argument_list|(
literal|"123456"
argument_list|,
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1...67"
argument_list|,
name|Util
operator|.
name|prettyTrim
argument_list|(
literal|"1234567"
argument_list|,
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1...78"
argument_list|,
name|Util
operator|.
name|prettyTrim
argument_list|(
literal|"12345678"
argument_list|,
literal|6
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

