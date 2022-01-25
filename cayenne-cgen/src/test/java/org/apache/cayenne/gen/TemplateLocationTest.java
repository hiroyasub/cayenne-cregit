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
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|TemporaryFolder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|TemplateLocationTest
block|{
annotation|@
name|Rule
specifier|public
name|TemporaryFolder
name|tempFolder
init|=
operator|new
name|TemporaryFolder
argument_list|()
decl_stmt|;
specifier|private
name|CgenConfiguration
name|cgenConfiguration
decl_stmt|;
specifier|private
name|ClassGenerationAction
name|action
decl_stmt|;
specifier|private
name|TemplateType
name|templateType
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|cgenConfiguration
operator|=
operator|new
name|CgenConfiguration
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|action
operator|=
operator|new
name|ClassGenerationAction
argument_list|(
name|cgenConfiguration
argument_list|)
expr_stmt|;
name|templateType
operator|=
name|TemplateType
operator|.
name|ENTITY_SUBCLASS
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|upperLevel
parameter_list|()
throws|throws
name|Exception
block|{
name|cgenConfiguration
operator|.
name|setRootPath
argument_list|(
name|tempFolder
operator|.
name|newFolder
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
name|tempFolder
operator|.
name|newFile
argument_list|(
literal|"testTemplate.vm"
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setTemplate
argument_list|(
literal|"../testTemplate.vm"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|action
operator|.
name|getTemplate
argument_list|(
name|templateType
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|sameLevel
parameter_list|()
throws|throws
name|Exception
block|{
name|cgenConfiguration
operator|.
name|setRootPath
argument_list|(
name|tempFolder
operator|.
name|getRoot
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
name|tempFolder
operator|.
name|newFile
argument_list|(
literal|"testTemplate2.vm"
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setTemplate
argument_list|(
literal|"testTemplate2.vm"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|action
operator|.
name|getTemplate
argument_list|(
name|templateType
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|aboveLevel
parameter_list|()
throws|throws
name|Exception
block|{
name|cgenConfiguration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|tempFolder
operator|.
name|getRoot
argument_list|()
operator|.
name|getParent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|tempFolder
operator|.
name|newFile
argument_list|(
literal|"testTemplate3.vm"
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setTemplate
argument_list|(
name|tempFolder
operator|.
name|getRoot
argument_list|()
operator|+
literal|"/testTemplate3.vm"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|action
operator|.
name|getTemplate
argument_list|(
name|templateType
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

