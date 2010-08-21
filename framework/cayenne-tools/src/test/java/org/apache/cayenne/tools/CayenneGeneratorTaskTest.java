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
name|tools
package|;
end_package

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
name|test
operator|.
name|file
operator|.
name|FileUtil
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
name|test
operator|.
name|resource
operator|.
name|ResourceUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Location
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_class
specifier|public
class|class
name|CayenneGeneratorTaskTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|File
name|baseDir
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|File
name|map
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|File
name|mapEmbeddables
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|File
name|template
decl_stmt|;
static|static
block|{
name|baseDir
operator|=
name|FileUtil
operator|.
name|baseTestDirectory
argument_list|()
expr_stmt|;
name|map
operator|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
literal|"antmap.xml"
argument_list|)
expr_stmt|;
name|mapEmbeddables
operator|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
literal|"antmap-embeddables.xml"
argument_list|)
expr_stmt|;
name|template
operator|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
literal|"velotemplate.vm"
argument_list|)
expr_stmt|;
name|ResourceUtil
operator|.
name|copyResourceToFile
argument_list|(
literal|"testmap.map.xml"
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|ResourceUtil
operator|.
name|copyResourceToFile
argument_list|(
literal|"embeddable.map.xml"
argument_list|,
name|mapEmbeddables
argument_list|)
expr_stmt|;
name|ResourceUtil
operator|.
name|copyResourceToFile
argument_list|(
literal|"org/apache/cayenne/tools/velotemplate.vm"
argument_list|,
name|template
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|CayenneGeneratorTask
name|task
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|Project
name|project
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|project
operator|.
name|setBaseDir
argument_list|(
name|baseDir
argument_list|)
expr_stmt|;
name|task
operator|=
operator|new
name|CayenneGeneratorTask
argument_list|()
expr_stmt|;
name|task
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|task
operator|.
name|setTaskName
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setLocation
argument_list|(
name|Location
operator|.
name|UNKNOWN_LOCATION
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test single classes with a non-standard template.      */
specifier|public
name|void
name|testSingleClassesCustTemplate
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prepare destination directory
name|File
name|mapDir
init|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
literal|"single-classes-custtempl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mapDir
operator|.
name|mkdirs
argument_list|()
argument_list|)
expr_stmt|;
comment|// setup task
name|task
operator|.
name|setDestDir
argument_list|(
name|mapDir
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMakepairs
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|task
operator|.
name|setUsepkgpath
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setTemplate
argument_list|(
name|template
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
comment|// run task
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// check results
name|File
name|a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|a
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|a
argument_list|,
literal|"Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"CayenneDataObject"
argument_list|)
expr_stmt|;
name|File
name|_a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/_Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|_a
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Test single classes generation including full package path. */
specifier|public
name|void
name|testSingleClasses1
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prepare destination directory
name|File
name|mapDir
init|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
literal|"single-classes-tree"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mapDir
operator|.
name|mkdirs
argument_list|()
argument_list|)
expr_stmt|;
comment|// setup task
name|task
operator|.
name|setDestDir
argument_list|(
name|mapDir
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMakepairs
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|task
operator|.
name|setUsepkgpath
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// run task
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// check results
name|File
name|a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|a
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|a
argument_list|,
literal|"Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"CayenneDataObject"
argument_list|)
expr_stmt|;
name|File
name|_a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/_Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|_a
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Test single classes generation ignoring package path. */
specifier|public
name|void
name|testSingleClasses2
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prepare destination directory
name|File
name|mapDir
init|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
literal|"single-classes-flat"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mapDir
operator|.
name|mkdirs
argument_list|()
argument_list|)
expr_stmt|;
comment|// setup task
name|task
operator|.
name|setDestDir
argument_list|(
name|mapDir
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMakepairs
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|task
operator|.
name|setUsepkgpath
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// run task
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// check results
name|File
name|a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|a
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|a
argument_list|,
literal|"Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"CayenneDataObject"
argument_list|)
expr_stmt|;
name|File
name|_a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"_Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|_a
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|pkga
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|pkga
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Test pairs generation including full package path. */
specifier|public
name|void
name|testPairs1
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prepare destination directory
name|File
name|mapDir
init|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
literal|"pairs-tree"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mapDir
operator|.
name|mkdirs
argument_list|()
argument_list|)
expr_stmt|;
comment|// setup task
name|task
operator|.
name|setDestDir
argument_list|(
name|mapDir
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMakepairs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setUsepkgpath
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// run task
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// check results
name|File
name|a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|a
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|a
argument_list|,
literal|"Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"_Artist"
argument_list|)
expr_stmt|;
name|File
name|_a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/_Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|_a
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|_a
argument_list|,
literal|"_Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"CayenneDataObject"
argument_list|)
expr_stmt|;
block|}
comment|/** Test pairs generation in the same directory. */
specifier|public
name|void
name|testPairs2
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prepare destination directory
name|File
name|mapDir
init|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
literal|"pairs-flat"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mapDir
operator|.
name|mkdirs
argument_list|()
argument_list|)
expr_stmt|;
comment|// setup task
name|task
operator|.
name|setDestDir
argument_list|(
name|mapDir
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMakepairs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setUsepkgpath
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// run task
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// check results
name|File
name|a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|a
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|a
argument_list|,
literal|"Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"_Artist"
argument_list|)
expr_stmt|;
name|File
name|_a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"_Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|_a
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|_a
argument_list|,
literal|"_Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"CayenneDataObject"
argument_list|)
expr_stmt|;
name|File
name|pkga
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|pkga
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test pairs generation including full package path with superclass and subclass in      * different packages.      */
specifier|public
name|void
name|testPairs3
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prepare destination directory
name|File
name|mapDir
init|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
literal|"pairs-tree-split"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mapDir
operator|.
name|mkdirs
argument_list|()
argument_list|)
expr_stmt|;
comment|// setup task
name|task
operator|.
name|setDestDir
argument_list|(
name|mapDir
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMakepairs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setUsepkgpath
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setSuperpkg
argument_list|(
literal|"org.apache.cayenne.testdo.testmap.superart"
argument_list|)
expr_stmt|;
comment|// run task
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// check results
name|File
name|a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|a
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|a
argument_list|,
literal|"Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"_Artist"
argument_list|)
expr_stmt|;
name|File
name|_a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/testmap/superart/_Artist.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|_a
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|_a
argument_list|,
literal|"_Artist"
argument_list|,
literal|"org.apache.cayenne.testdo.testmap.superart"
argument_list|,
literal|"CayenneDataObject"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPairsEmbeddable3
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prepare destination directory
name|File
name|mapDir
init|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
literal|"pairs-embeddables3-split"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mapDir
operator|.
name|mkdirs
argument_list|()
argument_list|)
expr_stmt|;
comment|// setup task
name|task
operator|.
name|setDestDir
argument_list|(
name|mapDir
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMap
argument_list|(
name|mapEmbeddables
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMakepairs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setUsepkgpath
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setSuperpkg
argument_list|(
literal|"org.apache.cayenne.testdo.embeddable.auto"
argument_list|)
expr_stmt|;
comment|// run task
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// check entity results
name|File
name|a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/embeddable/EmbedEntity1.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|a
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|a
argument_list|,
literal|"EmbedEntity1"
argument_list|,
literal|"org.apache.cayenne.testdo.embeddable"
argument_list|,
literal|"_EmbedEntity1"
argument_list|)
expr_stmt|;
name|File
name|_a
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/embeddable/auto/_EmbedEntity1.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|_a
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|_a
argument_list|,
literal|"_EmbedEntity1"
argument_list|,
literal|"org.apache.cayenne.testdo.embeddable.auto"
argument_list|,
literal|"CayenneDataObject"
argument_list|)
expr_stmt|;
comment|// check embeddable results
name|File
name|e
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/embeddable/Embeddable1.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|e
argument_list|,
literal|"Embeddable1"
argument_list|,
literal|"org.apache.cayenne.testdo.embeddable"
argument_list|,
literal|"_Embeddable1"
argument_list|)
expr_stmt|;
name|File
name|_e
init|=
operator|new
name|File
argument_list|(
name|mapDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/cayenne/testdo/embeddable/auto/_Embeddable1.java"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|_e
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|_e
argument_list|,
literal|"_Embeddable1"
argument_list|,
literal|"org.apache.cayenne.testdo.embeddable.auto"
argument_list|,
literal|"Object"
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
decl_stmt|;
try|try
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
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
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

