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
name|tools
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
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|CayenneGeneratorIntegrationTest
block|{
specifier|private
name|File
name|testDir
decl_stmt|;
specifier|private
name|void
name|startTest
parameter_list|(
name|String
name|testName
parameter_list|)
block|{
name|testDir
operator|=
operator|new
name|File
argument_list|(
literal|"target/generated-tests"
argument_list|,
name|testName
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|testDir
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test single classes with a non-standard template.      */
annotation|@
name|Test
specifier|public
name|void
name|testSingleClassesCustTemplate
parameter_list|()
throws|throws
name|Exception
block|{
name|startTest
argument_list|(
literal|"single-classes-cust-template"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/Artist.java"
argument_list|,
literal|"Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"CayenneDataObject"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/_Artist.java"
argument_list|)
expr_stmt|;
block|}
comment|/** Test single classes generation including full package path. */
annotation|@
name|Test
specifier|public
name|void
name|testSingleClasses1
parameter_list|()
throws|throws
name|Exception
block|{
name|startTest
argument_list|(
literal|"single-classes1"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/Artist.java"
argument_list|,
literal|"Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"BaseDataObject"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/_Artist.java"
argument_list|)
expr_stmt|;
block|}
comment|/** Test single classes generation ignoring package path. */
annotation|@
name|Test
specifier|public
name|void
name|testSingleClasses2
parameter_list|()
throws|throws
name|Exception
block|{
name|startTest
argument_list|(
literal|"single-classes2"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"Artist.java"
argument_list|,
literal|"Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"BaseDataObject"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
literal|"_Artist.java"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/Artist.java"
argument_list|)
expr_stmt|;
block|}
comment|/** Test pairs generation including full package path. */
annotation|@
name|Test
specifier|public
name|void
name|testPairs1
parameter_list|()
throws|throws
name|Exception
block|{
name|startTest
argument_list|(
literal|"pairs1"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/Artist.java"
argument_list|,
literal|"Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"_Artist"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/auto/_Artist.java"
argument_list|,
literal|"_Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap.auto"
argument_list|,
literal|"BaseDataObject"
argument_list|)
expr_stmt|;
block|}
comment|/** Test pairs generation in the same directory. */
annotation|@
name|Test
specifier|public
name|void
name|testPairs2
parameter_list|()
throws|throws
name|Exception
block|{
name|startTest
argument_list|(
literal|"pairs2"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"Artist.java"
argument_list|,
literal|"Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"_Artist"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"_Artist.java"
argument_list|,
literal|"_Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"BaseDataObject"
argument_list|)
expr_stmt|;
name|assertExists
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/Artist.java"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test pairs generation including full package path with superclass and      * subclass in different packages.      */
annotation|@
name|Test
specifier|public
name|void
name|testPairs3
parameter_list|()
throws|throws
name|Exception
block|{
name|startTest
argument_list|(
literal|"pairs3"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/Artist.java"
argument_list|,
literal|"Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"_Artist"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/superart/_Artist.java"
argument_list|,
literal|"_Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap.superart"
argument_list|,
literal|"BaseDataObject"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPairsEmbeddable3
parameter_list|()
throws|throws
name|Exception
block|{
name|startTest
argument_list|(
literal|"pairs-embeddables3"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"org/apache/cayenne/testdo/embeddable/EmbedEntity1.java"
argument_list|,
literal|"EmbedEntity1"
argument_list|,
literal|"org.apache.cayenne.testdo.embeddable"
argument_list|,
literal|"_EmbedEntity1"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"org/apache/cayenne/testdo/embeddable/auto/_EmbedEntity1.java"
argument_list|,
literal|"_EmbedEntity1"
argument_list|,
literal|"org.apache.cayenne.testdo.embeddable.auto"
argument_list|,
literal|"BaseDataObject"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"org/apache/cayenne/testdo/embeddable/Embeddable1.java"
argument_list|,
literal|"Embeddable1"
argument_list|,
literal|"org.apache.cayenne.testdo.embeddable"
argument_list|,
literal|"_Embeddable1"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
literal|"org/apache/cayenne/testdo/embeddable/auto/_Embeddable1.java"
argument_list|,
literal|"_Embeddable1"
argument_list|,
literal|"org.apache.cayenne.testdo.embeddable.auto"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|convertPath
parameter_list|(
name|String
name|unixPath
parameter_list|)
block|{
return|return
name|unixPath
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
name|File
operator|.
name|separatorChar
argument_list|)
return|;
block|}
specifier|private
name|void
name|assertContents
parameter_list|(
name|String
name|filePath
parameter_list|,
name|String
name|className
parameter_list|,
name|String
name|packageName
parameter_list|,
name|String
name|extendsName
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
name|convertPath
argument_list|(
name|filePath
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Not a file: "
operator|+
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|f
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|f
argument_list|,
name|className
argument_list|,
name|packageName
argument_list|,
name|extendsName
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertExists
parameter_list|(
name|String
name|filePath
parameter_list|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
name|convertPath
argument_list|(
name|filePath
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertContents
parameter_list|(
name|File
name|f
parameter_list|,
name|String
name|className
parameter_list|,
name|String
name|packageName
parameter_list|,
name|String
name|extendsName
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|BufferedReader
name|in
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
argument_list|)
argument_list|)
init|)
block|{
name|assertPackage
argument_list|(
name|in
argument_list|,
name|packageName
argument_list|)
expr_stmt|;
name|assertClass
argument_list|(
name|in
argument_list|,
name|className
argument_list|,
name|extendsName
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|assertPackage
parameter_list|(
name|BufferedReader
name|in
parameter_list|,
name|String
name|packageName
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|s
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|s
operator|=
name|in
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Pattern
operator|.
name|matches
argument_list|(
literal|"^package\\s+([^\\s;]+);"
argument_list|,
name|s
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
name|s
operator|.
name|indexOf
argument_list|(
name|packageName
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|fail
argument_list|(
literal|"No package declaration found."
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertClass
parameter_list|(
name|BufferedReader
name|in
parameter_list|,
name|String
name|className
parameter_list|,
name|String
name|extendsName
parameter_list|)
throws|throws
name|Exception
block|{
name|Pattern
name|classPattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^public\\s+"
argument_list|)
decl_stmt|;
name|String
name|s
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|s
operator|=
name|in
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|classPattern
operator|.
name|matcher
argument_list|(
name|s
argument_list|)
operator|.
name|find
argument_list|()
condition|)
block|{
name|assertTrue
argument_list|(
name|s
operator|.
name|indexOf
argument_list|(
name|className
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|extendsName
operator|!=
literal|null
condition|)
block|{
name|assertTrue
argument_list|(
name|s
operator|.
name|indexOf
argument_list|(
name|extendsName
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|indexOf
argument_list|(
name|className
argument_list|)
operator|<
name|s
operator|.
name|indexOf
argument_list|(
name|extendsName
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
block|}
name|fail
argument_list|(
literal|"No class declaration found."
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

