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
name|maven
operator|.
name|plugin
operator|.
name|aggregator
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_comment
comment|/**  * A goal to build aggregated jar artifacts from multiple other artifacts.  *   * @author Andrus Adamchik  * @goal aggregate-bin  * @phase package  * @requiresProject  */
end_comment

begin_class
specifier|public
class|class
name|BinAggregatorMojo
extends|extends
name|AbstractAggregatorMojo
block|{
comment|/**      * Default location used for mojo unless overridden in ArtifactItem      *       * @parameter expression="${unpackDirectory}"      *            default-value="${project.build.directory}/aggregate/unpack-bin"      * @required      */
specifier|private
name|File
name|unpackDirectory
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|unpackArtifacts
argument_list|(
name|unpackDirectory
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|packAggregatedArtifact
argument_list|(
name|unpackDirectory
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

