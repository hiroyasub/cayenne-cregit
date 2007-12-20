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
name|unit
operator|.
name|CayenneResources
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|oro
operator|.
name|text
operator|.
name|perl
operator|.
name|Perl5Util
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
name|types
operator|.
name|FileList
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
name|types
operator|.
name|Path
import|;
end_import

begin_class
specifier|public
class|class
name|CayenneGeneratorTaskCrossMapRelationshipsTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|Perl5Util
name|regexUtil
init|=
operator|new
name|Perl5Util
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Project
name|project
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
specifier|protected
name|CayenneGeneratorTask
name|task
decl_stmt|;
specifier|public
name|void
name|setUp
parameter_list|()
block|{
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
annotation|@
name|Override
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|task
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Test pairs generation with a cross-DataMap relationship (v1.1).      *       * @deprecated since 3.0 1.1 version is deprecated.      */
specifier|public
name|void
name|testCrossDataMapRelationships_v1_1
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prepare destination directory
name|File
name|destDir
init|=
operator|new
name|File
argument_list|(
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getTestDir
argument_list|()
argument_list|,
literal|"cgen11"
argument_list|)
decl_stmt|;
comment|// prepare destination directory
if|if
condition|(
operator|!
name|destDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|assertTrue
argument_list|(
name|destDir
operator|.
name|mkdirs
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|File
name|map
init|=
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
literal|"testmap-dependent.map.xml"
argument_list|)
decl_stmt|;
name|CayenneResources
operator|.
name|copyResourceToFile
argument_list|(
literal|"testmap-dependent.map.xml"
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|File
name|additionalMaps
index|[]
init|=
operator|new
name|File
index|[
literal|1
index|]
decl_stmt|;
name|additionalMaps
index|[
literal|0
index|]
operator|=
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
literal|"testmap.map.xml"
argument_list|)
expr_stmt|;
name|CayenneResources
operator|.
name|copyResourceToFile
argument_list|(
literal|"testmap.map.xml"
argument_list|,
name|additionalMaps
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|FileList
name|additionalMapsFilelist
init|=
operator|new
name|FileList
argument_list|()
decl_stmt|;
name|additionalMapsFilelist
operator|.
name|setDir
argument_list|(
name|additionalMaps
index|[
literal|0
index|]
operator|.
name|getParentFile
argument_list|()
argument_list|)
expr_stmt|;
name|additionalMapsFilelist
operator|.
name|setFiles
argument_list|(
name|additionalMaps
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Path
name|additionalMapsPath
init|=
operator|new
name|Path
argument_list|(
name|task
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
name|additionalMapsPath
operator|.
name|addFilelist
argument_list|(
name|additionalMapsFilelist
argument_list|)
expr_stmt|;
comment|// setup task
name|task
operator|.
name|setMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|task
operator|.
name|setAdditionalMaps
argument_list|(
name|additionalMapsPath
argument_list|)
expr_stmt|;
name|task
operator|.
name|setVersion
argument_list|(
literal|"1.1"
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
name|setOverwrite
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMode
argument_list|(
literal|"entity"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setIncludeEntities
argument_list|(
literal|"MyArtGroup"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setDestDir
argument_list|(
name|destDir
argument_list|)
expr_stmt|;
name|task
operator|.
name|setSuperpkg
argument_list|(
literal|"org.apache.art2.auto"
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
name|destDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/art2/MyArtGroup.java"
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
literal|"MyArtGroup"
argument_list|,
literal|"org.apache.art2"
argument_list|,
literal|"_MyArtGroup"
argument_list|)
expr_stmt|;
name|File
name|_a
init|=
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/art2/auto/_MyArtGroup.java"
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
literal|"_MyArtGroup"
argument_list|,
literal|"org.apache.art2.auto"
argument_list|,
literal|"CayenneDataObject"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|_a
argument_list|,
literal|"org.apache.art.ArtGroup getToParentGroup()"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|_a
argument_list|,
literal|"setToParentGroup(org.apache.art.ArtGroup toParentGroup)"
argument_list|)
expr_stmt|;
block|}
comment|/** Test pairs generation with a cross-DataMap relationship. */
specifier|public
name|void
name|testCrossDataMapRelationships
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prepare destination directory
name|File
name|destDir
init|=
operator|new
name|File
argument_list|(
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getTestDir
argument_list|()
argument_list|,
literal|"cgen12"
argument_list|)
decl_stmt|;
comment|// prepare destination directory
if|if
condition|(
operator|!
name|destDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|assertTrue
argument_list|(
name|destDir
operator|.
name|mkdirs
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|File
name|map
init|=
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
literal|"testmap-dependent.map.xml"
argument_list|)
decl_stmt|;
name|CayenneResources
operator|.
name|copyResourceToFile
argument_list|(
literal|"testmap-dependent.map.xml"
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|File
name|additionalMaps
index|[]
init|=
operator|new
name|File
index|[
literal|1
index|]
decl_stmt|;
name|additionalMaps
index|[
literal|0
index|]
operator|=
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
literal|"testmap.map.xml"
argument_list|)
expr_stmt|;
name|CayenneResources
operator|.
name|copyResourceToFile
argument_list|(
literal|"testmap.map.xml"
argument_list|,
name|additionalMaps
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|FileList
name|additionalMapsFilelist
init|=
operator|new
name|FileList
argument_list|()
decl_stmt|;
name|additionalMapsFilelist
operator|.
name|setDir
argument_list|(
name|additionalMaps
index|[
literal|0
index|]
operator|.
name|getParentFile
argument_list|()
argument_list|)
expr_stmt|;
name|additionalMapsFilelist
operator|.
name|setFiles
argument_list|(
name|additionalMaps
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Path
name|additionalMapsPath
init|=
operator|new
name|Path
argument_list|(
name|task
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
name|additionalMapsPath
operator|.
name|addFilelist
argument_list|(
name|additionalMapsFilelist
argument_list|)
expr_stmt|;
comment|// setup task
name|task
operator|.
name|setMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|task
operator|.
name|setAdditionalMaps
argument_list|(
name|additionalMapsPath
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
name|setOverwrite
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMode
argument_list|(
literal|"entity"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setIncludeEntities
argument_list|(
literal|"MyArtGroup"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setDestDir
argument_list|(
name|destDir
argument_list|)
expr_stmt|;
name|task
operator|.
name|setSuperpkg
argument_list|(
literal|"org.apache.art2.auto"
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
name|destDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/art2/MyArtGroup.java"
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
literal|"MyArtGroup"
argument_list|,
literal|"org.apache.art2"
argument_list|,
literal|"_MyArtGroup"
argument_list|)
expr_stmt|;
name|File
name|_a
init|=
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
name|convertPath
argument_list|(
literal|"org/apache/art2/auto/_MyArtGroup.java"
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
literal|"_MyArtGroup"
argument_list|,
literal|"org.apache.art2.auto"
argument_list|,
literal|"CayenneDataObject"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|_a
argument_list|,
literal|"import org.apache.art.ArtGroup;"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|_a
argument_list|,
literal|" ArtGroup getToParentGroup()"
argument_list|)
expr_stmt|;
name|assertContents
argument_list|(
name|_a
argument_list|,
literal|"setToParentGroup(ArtGroup toParentGroup)"
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
name|content
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
name|s
operator|.
name|contains
argument_list|(
name|content
argument_list|)
condition|)
return|return;
block|}
name|fail
argument_list|(
literal|"<"
operator|+
name|content
operator|+
literal|"> not found in "
operator|+
name|f
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"."
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
name|regexUtil
operator|.
name|match
argument_list|(
literal|"/^package\\s+([^\\s;]+);/"
argument_list|,
name|s
operator|+
literal|'\n'
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
name|s
operator|.
name|contains
argument_list|(
name|packageName
argument_list|)
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
name|regexUtil
operator|.
name|match
argument_list|(
literal|"/class\\s+([^\\s]+)\\s+extends\\s+([^\\s]+)/"
argument_list|,
name|s
operator|+
literal|'\n'
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
name|s
operator|.
name|contains
argument_list|(
name|className
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|contains
argument_list|(
name|extendsName
argument_list|)
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

