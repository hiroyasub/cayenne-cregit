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
name|BuildResult
import|;
end_import

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
name|gradle
operator|.
name|testkit
operator|.
name|runner
operator|.
name|TaskOutcome
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|CgenTaskIT
extends|extends
name|BaseTaskIT
block|{
annotation|@
name|Test
specifier|public
name|void
name|classGeneratingWithDefaultConfigSuccess
parameter_list|()
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
argument_list|)
decl_stmt|;
name|BuildResult
name|result
init|=
name|runner
operator|.
name|build
argument_list|()
decl_stmt|;
name|String
name|generatedDirectoryPath
init|=
name|projectDir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/org/example/cayenne/persistent/"
decl_stmt|;
name|String
name|generatedClassPath
init|=
name|generatedDirectoryPath
operator|+
literal|"City.java"
decl_stmt|;
name|String
name|generatedParentClassPath
init|=
name|generatedDirectoryPath
operator|+
literal|"auto/_City.java"
decl_stmt|;
name|File
name|generatedClass
init|=
operator|new
name|File
argument_list|(
name|generatedClassPath
argument_list|)
decl_stmt|;
name|File
name|generatedParentClass
init|=
operator|new
name|File
argument_list|(
name|generatedParentClassPath
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|generatedClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|generatedParentClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TaskOutcome
operator|.
name|SUCCESS
argument_list|,
name|result
operator|.
name|task
argument_list|(
literal|":cgen"
argument_list|)
operator|.
name|getOutcome
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|classGeneratingWithCustomConfigSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|GradleRunner
name|runner
init|=
name|createRunner
argument_list|(
literal|"cgen_custom_config"
argument_list|,
literal|"cgen"
argument_list|,
literal|"-PdataMap="
operator|+
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
argument_list|)
decl_stmt|;
name|BuildResult
name|result
init|=
name|runner
operator|.
name|build
argument_list|()
decl_stmt|;
name|String
name|generatedDirectoryPath
init|=
name|projectDir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/customDirectory/org/example/cayenne/persistent/"
decl_stmt|;
name|String
name|generatedClassPath
init|=
name|generatedDirectoryPath
operator|+
literal|"City.groovy"
decl_stmt|;
name|String
name|excludedClassPath
init|=
name|generatedDirectoryPath
operator|+
literal|"Artist.groovy"
decl_stmt|;
name|String
name|generatedParentClassPath
init|=
name|generatedDirectoryPath
operator|+
literal|"auto/_City.groovy"
decl_stmt|;
name|String
name|excludedParentClassPath
init|=
name|generatedDirectoryPath
operator|+
literal|"auto/_Artist.groovy"
decl_stmt|;
name|File
name|generatedClass
init|=
operator|new
name|File
argument_list|(
name|generatedClassPath
argument_list|)
decl_stmt|;
name|File
name|excludedClass
init|=
operator|new
name|File
argument_list|(
name|excludedClassPath
argument_list|)
decl_stmt|;
name|File
name|generatedParentClass
init|=
operator|new
name|File
argument_list|(
name|generatedParentClassPath
argument_list|)
decl_stmt|;
name|File
name|excludedParentClass
init|=
operator|new
name|File
argument_list|(
name|excludedParentClassPath
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|generatedClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|excludedClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|excludedParentClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|generatedParentClass
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TaskOutcome
operator|.
name|SUCCESS
argument_list|,
name|result
operator|.
name|task
argument_list|(
literal|":cgen"
argument_list|)
operator|.
name|getOutcome
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

