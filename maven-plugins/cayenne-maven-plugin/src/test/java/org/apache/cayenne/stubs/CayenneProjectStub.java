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
name|stubs
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|testing
operator|.
name|stubs
operator|.
name|MavenProjectStub
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
specifier|public
class|class
name|CayenneProjectStub
extends|extends
name|MavenProjectStub
block|{
specifier|public
name|CayenneProjectStub
parameter_list|()
block|{
name|Set
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
operator|new
name|ArtifactStub
argument_list|(
literal|"assembly"
argument_list|,
literal|"dependency-artifact1"
argument_list|,
literal|"1.0"
argument_list|,
literal|"jar"
argument_list|,
name|Artifact
operator|.
name|SCOPE_COMPILE
argument_list|)
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
operator|new
name|ArtifactStub
argument_list|(
literal|"assembly"
argument_list|,
literal|"dependency-artifact2"
argument_list|,
literal|"1.0"
argument_list|,
literal|"jar"
argument_list|,
name|Artifact
operator|.
name|SCOPE_RUNTIME
argument_list|)
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
operator|new
name|ArtifactStub
argument_list|(
literal|"assembly"
argument_list|,
literal|"dependency-artifact3"
argument_list|,
literal|"1.0"
argument_list|,
literal|"jar"
argument_list|,
name|Artifact
operator|.
name|SCOPE_TEST
argument_list|)
argument_list|)
expr_stmt|;
name|setDependencyArtifacts
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

