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
name|org
operator|.
name|gradle
operator|.
name|testkit
operator|.
name|runner
operator|.
name|GradleRunner
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
name|net
operator|.
name|URLDecoder
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|GradlePluginIT
extends|extends
name|BaseTaskIT
block|{
specifier|private
name|void
name|testDbImportWithGradleVersion
parameter_list|(
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|dbUrl
init|=
literal|"jdbc:derby:"
operator|+
name|projectDir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/build/"
operator|+
name|version
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'_'
argument_list|)
decl_stmt|;
name|dbUrl
operator|+=
literal|";create=true"
expr_stmt|;
name|GradleRunner
name|runner
init|=
name|createRunner
argument_list|(
literal|"dbimport_simple_db"
argument_list|,
literal|"cdbimport"
argument_list|,
literal|"--info"
argument_list|,
literal|"-PdbUrl="
operator|+
name|dbUrl
argument_list|)
decl_stmt|;
name|runner
operator|.
name|withGradleVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|runner
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|testCgenWithGradleVersion
parameter_list|(
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
name|GradleRunner
name|runner
init|=
name|createRunner
argument_list|(
literal|"cgen_default_config"
argument_list|,
literal|"cgen"
argument_list|,
literal|"-PdataMap="
operator|+
name|URLDecoder
operator|.
name|decode
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test_datamap.map.xml"
argument_list|)
operator|.
name|getFile
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|runner
operator|.
name|withGradleVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|runner
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGradleVersionsCompatibility
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|versions
decl_stmt|;
comment|// Old gradle versions will fail on Java 9
if|if
condition|(
name|getJavaMajorVersion
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.version"
argument_list|)
argument_list|)
operator|<
literal|9
condition|)
block|{
name|versions
operator|=
operator|new
name|String
index|[]
block|{
literal|"4.3"
block|,
literal|"4.0"
block|,
literal|"3.5"
block|,
literal|"3.3"
block|,
literal|"3.0"
block|,
literal|"2.12"
block|,
literal|"2.8"
block|}
expr_stmt|;
block|}
else|else
block|{
name|versions
operator|=
operator|new
name|String
index|[]
block|{
literal|"4.3.1"
block|,
literal|"4.3"
block|}
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|failedVersions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|version
range|:
name|versions
control|)
block|{
try|try
block|{
name|testDbImportWithGradleVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|testCgenWithGradleVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|failedVersions
operator|.
name|add
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
block|}
name|StringBuilder
name|versionString
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Failed versions:"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|version
range|:
name|failedVersions
control|)
block|{
name|versionString
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|versionString
operator|.
name|toString
argument_list|()
argument_list|,
name|failedVersions
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testVersion
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|getJavaMajorVersion
argument_list|(
literal|"1.7.0_25-b15"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|getJavaMajorVersion
argument_list|(
literal|"1.7.2+123"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|getJavaMajorVersion
argument_list|(
literal|"1.8.145"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|getJavaMajorVersion
argument_list|(
literal|"9-ea+19"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|getJavaMajorVersion
argument_list|(
literal|"9+100"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|getJavaMajorVersion
argument_list|(
literal|"9"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|getJavaMajorVersion
argument_list|(
literal|"9.0.1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|getJavaMajorVersion
argument_list|(
literal|"10-ea+38"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// will fail on Java 1.1 or earlier :)
specifier|private
specifier|static
name|int
name|getJavaMajorVersion
parameter_list|(
name|String
name|versionString
parameter_list|)
block|{
name|int
name|index
init|=
literal|0
decl_stmt|,
name|prevIndex
init|=
literal|0
decl_stmt|,
name|version
init|=
literal|0
decl_stmt|;
if|if
condition|(
operator|(
name|index
operator|=
name|versionString
operator|.
name|indexOf
argument_list|(
literal|"-"
argument_list|)
operator|)
operator|>=
literal|0
condition|)
block|{
name|versionString
operator|=
name|versionString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|index
operator|=
name|versionString
operator|.
name|indexOf
argument_list|(
literal|"+"
argument_list|)
operator|)
operator|>=
literal|0
condition|)
block|{
name|versionString
operator|=
name|versionString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|version
operator|<
literal|2
condition|)
block|{
name|index
operator|=
name|versionString
operator|.
name|indexOf
argument_list|(
literal|"."
argument_list|,
name|prevIndex
argument_list|)
expr_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
name|index
operator|=
name|versionString
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
name|version
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|versionString
operator|.
name|substring
argument_list|(
name|prevIndex
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
name|prevIndex
operator|=
name|index
operator|+
literal|1
expr_stmt|;
block|}
return|return
name|version
return|;
block|}
block|}
end_class

end_unit

