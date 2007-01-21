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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|UtilTest
extends|extends
name|TestCase
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
specifier|protected
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
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|java
operator|.
name|lang
operator|.
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
specifier|public
name|void
name|testStripLineBreaks
parameter_list|()
block|{
comment|// Windows
name|assertEquals
argument_list|(
literal|"aAbAc"
argument_list|,
name|Util
operator|.
name|stripLineBreaks
argument_list|(
literal|"a\r\nb\r\nc"
argument_list|,
literal|"A"
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
literal|"B"
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
literal|"C"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCopyFile
parameter_list|()
throws|throws
name|java
operator|.
name|lang
operator|.
name|Exception
block|{
name|assertFalse
argument_list|(
literal|"Temp file "
operator|+
name|fTmpFileCopy
operator|+
literal|" is on the way, please delete it manually."
argument_list|,
name|fTmpFileCopy
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Util
operator|.
name|copy
argument_list|(
name|fTmpFileInCurrentDir
argument_list|,
name|fTmpFileCopy
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|fTmpFileCopy
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|fTmpFileCopy
operator|.
name|length
argument_list|()
argument_list|,
name|fTmpFileInCurrentDir
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCopyFileUrl
parameter_list|()
throws|throws
name|java
operator|.
name|lang
operator|.
name|Exception
block|{
name|assertFalse
argument_list|(
literal|"Temp file "
operator|+
name|fTmpFileCopy
operator|+
literal|" is on the way, please delete it manually."
argument_list|,
name|fTmpFileCopy
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Util
operator|.
name|copy
argument_list|(
name|fTmpFileInCurrentDir
operator|.
name|toURL
argument_list|()
argument_list|,
name|fTmpFileCopy
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|fTmpFileCopy
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|fTmpFileCopy
operator|.
name|length
argument_list|()
argument_list|,
name|fTmpFileInCurrentDir
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCopyJarUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|fileInJar
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"testfile1.txt"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|fileInJar
argument_list|)
expr_stmt|;
comment|// skipping test if file not in jar
if|if
condition|(
operator|!
name|fileInJar
operator|.
name|toExternalForm
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"jar:"
argument_list|)
condition|)
block|{
return|return;
block|}
name|assertTrue
argument_list|(
name|Util
operator|.
name|copy
argument_list|(
name|fileInJar
argument_list|,
name|fTmpFileCopy
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|fTmpFileCopy
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// check file size in a jar
name|InputStream
name|in
init|=
literal|null
decl_stmt|;
try|try
block|{
name|in
operator|=
name|fileInJar
operator|.
name|openConnection
argument_list|()
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|int
name|len
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|in
operator|.
name|read
argument_list|()
operator|>=
literal|0
condition|)
block|{
name|len
operator|++
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|len
argument_list|,
name|fTmpFileCopy
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
name|fail
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testDeleteFile
parameter_list|()
throws|throws
name|java
operator|.
name|lang
operator|.
name|Exception
block|{
comment|// delete file
name|assertFalse
argument_list|(
literal|"Temp file "
operator|+
name|fTmpFileCopy
operator|+
literal|" is on the way, please delete it manually."
argument_list|,
name|fTmpFileCopy
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Util
operator|.
name|copy
argument_list|(
name|fTmpFileInCurrentDir
argument_list|,
name|fTmpFileCopy
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Util
operator|.
name|delete
argument_list|(
name|fTmpFileCopy
operator|.
name|getPath
argument_list|()
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
comment|// delete empty dir with no recursion
name|String
name|tmpDirName
init|=
literal|"tmpdir_"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|File
name|tmpDir
init|=
operator|new
name|File
argument_list|(
name|tmpDirName
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tmpDir
operator|.
name|mkdir
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Util
operator|.
name|delete
argument_list|(
name|tmpDirName
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tmpDir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// delete dir with files with recurions
name|assertTrue
argument_list|(
name|tmpDir
operator|.
name|mkdir
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|tmpDir
argument_list|,
literal|"aaa"
argument_list|)
operator|.
name|createNewFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Util
operator|.
name|delete
argument_list|(
name|tmpDirName
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tmpDir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// fail delete dir with files with no recurions
name|assertTrue
argument_list|(
name|tmpDir
operator|.
name|mkdir
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|tmpDir
argument_list|,
literal|"aaa"
argument_list|)
operator|.
name|createNewFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Util
operator|.
name|delete
argument_list|(
name|tmpDirName
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tmpDir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Util
operator|.
name|delete
argument_list|(
name|tmpDirName
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tmpDir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCloneViaSerialization
parameter_list|()
throws|throws
name|java
operator|.
name|lang
operator|.
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
block|}
end_class

end_unit

