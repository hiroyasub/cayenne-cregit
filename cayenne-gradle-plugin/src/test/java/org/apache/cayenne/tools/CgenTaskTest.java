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
name|apache
operator|.
name|cayenne
operator|.
name|gen
operator|.
name|CgenConfiguration
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
name|gen
operator|.
name|ClassGenerationAction
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
name|map
operator|.
name|DataMap
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

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|CgenTaskTest
block|{
annotation|@
name|Rule
specifier|public
name|TemporaryFolder
name|temp
init|=
operator|new
name|TemporaryFolder
argument_list|()
decl_stmt|;
name|DataMap
name|dataMap
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
specifier|private
name|CgenTask
name|createCgenTaskMock
parameter_list|()
block|{
name|CgenTask
name|mock
init|=
name|mock
argument_list|(
name|CgenTask
operator|.
name|class
argument_list|)
decl_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setClient
argument_list|(
name|anyBoolean
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setAdditionalMaps
argument_list|(
name|any
argument_list|(
name|File
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setCreatePropertyNames
argument_list|(
name|anyBoolean
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setEmbeddableSuperTemplate
argument_list|(
name|anyString
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setEmbeddableTemplate
argument_list|(
name|anyString
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setEncoding
argument_list|(
name|anyString
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setExcludeEntities
argument_list|(
name|anyString
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setIncludeEntities
argument_list|(
name|anyString
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setMakePairs
argument_list|(
name|anyBoolean
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setMode
argument_list|(
name|anyString
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setOutputPattern
argument_list|(
name|anyString
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setSuperPkg
argument_list|(
name|anyString
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setSuperTemplate
argument_list|(
name|anyString
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setOverwrite
argument_list|(
name|anyBoolean
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setUsePkgPath
argument_list|(
name|anyBoolean
argument_list|()
argument_list|)
expr_stmt|;
name|doCallRealMethod
argument_list|()
operator|.
name|when
argument_list|(
name|mock
argument_list|)
operator|.
name|setTemplate
argument_list|(
name|anyString
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mock
operator|.
name|buildConfiguration
argument_list|(
name|dataMap
argument_list|)
argument_list|)
operator|.
name|thenCallRealMethod
argument_list|()
expr_stmt|;
name|when
argument_list|(
name|mock
operator|.
name|createGenerator
argument_list|(
name|dataMap
argument_list|)
argument_list|)
operator|.
name|thenCallRealMethod
argument_list|()
expr_stmt|;
return|return
name|mock
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGeneratorCreation
parameter_list|()
block|{
name|CgenTask
name|task
init|=
name|createCgenTaskMock
argument_list|()
decl_stmt|;
name|task
operator|.
name|setEmbeddableSuperTemplate
argument_list|(
literal|"superTemplate"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setEmbeddableTemplate
argument_list|(
literal|"template"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setExcludeEntities
argument_list|(
literal|"entity1"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setIncludeEntities
argument_list|(
literal|"entity2"
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
name|setOutputPattern
argument_list|(
literal|"pattern"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setSuperPkg
argument_list|(
literal|"org.example.model.auto"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setSuperTemplate
argument_list|(
literal|"superTemplate"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setTemplate
argument_list|(
literal|"template"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setMakePairs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setCreatePropertyNames
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setOverwrite
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setUsePkgPath
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ClassGenerationAction
name|createdAction
init|=
name|task
operator|.
name|createGenerator
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
name|CgenConfiguration
name|cgenConfiguration
init|=
name|createdAction
operator|.
name|getCgenConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|cgenConfiguration
operator|.
name|getEmbeddableSuperTemplate
argument_list|()
argument_list|,
literal|"superTemplate"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|cgenConfiguration
operator|.
name|getEmbeddableTemplate
argument_list|()
argument_list|,
literal|"template"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|cgenConfiguration
operator|.
name|getEncoding
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|cgenConfiguration
operator|.
name|getArtifactsGenerationMode
argument_list|()
argument_list|,
literal|"entity"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|cgenConfiguration
operator|.
name|getOutputPattern
argument_list|()
argument_list|,
literal|"pattern"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|cgenConfiguration
operator|.
name|getSuperPkg
argument_list|()
argument_list|,
literal|"org.example.model.auto"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|cgenConfiguration
operator|.
name|getSuperTemplate
argument_list|()
argument_list|,
literal|"superTemplate"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|cgenConfiguration
operator|.
name|getTemplate
argument_list|()
argument_list|,
literal|"template"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cgenConfiguration
operator|.
name|isMakePairs
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cgenConfiguration
operator|.
name|isCreatePropertyNames
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cgenConfiguration
operator|.
name|isOverwrite
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cgenConfiguration
operator|.
name|isUsePkgPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

