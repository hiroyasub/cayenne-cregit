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
name|project
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
name|net
operator|.
name|URL
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
name|conf
operator|.
name|Configuration
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
name|CayenneCase
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

begin_comment
comment|/**  * @deprecated since 3.0. {@link ProjectConfigurator} approach turned out to be not  *             usable, and is in fact rarely used (if ever). It will be removed in  *             subsequent releases.  */
end_comment

begin_class
specifier|public
class|class
name|PartialProjectTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|File
name|testProjectFile
decl_stmt|;
specifier|protected
name|PartialProject
name|project
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|// create new test directory, copy cayenne.xml in there
name|File
name|baseDir
init|=
name|super
operator|.
name|getTestDir
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|File
name|tmpDir
init|=
operator|new
name|File
argument_list|(
name|baseDir
argument_list|,
literal|"partial-project-"
operator|+
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|tmpDir
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|tmpDir
operator|.
name|mkdir
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Can't create "
operator|+
name|tmpDir
argument_list|)
throw|;
block|}
name|testProjectFile
operator|=
operator|new
name|File
argument_list|(
name|tmpDir
argument_list|,
name|Configuration
operator|.
name|DEFAULT_DOMAIN_FILE
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
comment|// copy cayenne.xml
name|URL
name|src
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"lightweight-cayenne.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|copy
argument_list|(
name|src
argument_list|,
name|testProjectFile
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Can't copy from "
operator|+
name|src
argument_list|)
throw|;
block|}
name|project
operator|=
operator|new
name|PartialProject
argument_list|(
name|testProjectFile
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testParentFile
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|testProjectFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|getCanonicalFile
argument_list|()
argument_list|,
name|project
operator|.
name|getProjectDirectory
argument_list|()
operator|.
name|getCanonicalFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProjectFile
parameter_list|()
throws|throws
name|Exception
block|{
name|ProjectFile
name|f
init|=
name|project
operator|.
name|findFile
argument_list|(
name|project
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Wrong main file type: "
operator|+
name|f
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|f
operator|instanceof
name|ApplicationProjectFile
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Null delegate"
argument_list|,
operator|(
operator|(
name|ApplicationProjectFile
operator|)
name|f
operator|)
operator|.
name|getSaveDelegate
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMainFile
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|project
operator|.
name|findFile
argument_list|(
name|project
argument_list|)
operator|.
name|resolveFile
argument_list|()
argument_list|,
name|project
operator|.
name|getMainFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDomains
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|project
operator|.
name|getChildren
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNodes
parameter_list|()
throws|throws
name|Exception
block|{
name|PartialProject
operator|.
name|DomainMetaData
name|d2
init|=
name|project
operator|.
name|domains
operator|.
name|get
argument_list|(
literal|"d2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|d2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|d2
operator|.
name|nodes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSave
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|testProjectFile
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Can't delete project file: "
operator|+
name|testProjectFile
argument_list|)
throw|;
block|}
name|PartialProject
name|old
init|=
name|project
decl_stmt|;
name|old
operator|.
name|save
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|testProjectFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// reinit shared project and run one of the other tests
name|project
operator|=
operator|new
name|PartialProject
argument_list|(
name|testProjectFile
argument_list|)
expr_stmt|;
name|testNodes
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

