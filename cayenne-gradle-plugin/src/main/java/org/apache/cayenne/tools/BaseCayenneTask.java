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
name|File
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|DefaultTask
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|InvalidUserDataException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|Internal
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|BaseCayenneTask
extends|extends
name|DefaultTask
block|{
annotation|@
name|Internal
specifier|private
name|File
name|map
decl_stmt|;
annotation|@
name|Internal
specifier|private
name|String
name|mapFileName
decl_stmt|;
specifier|public
name|File
name|getMap
parameter_list|()
block|{
return|return
name|map
return|;
block|}
specifier|public
name|void
name|setMap
parameter_list|(
name|File
name|mapFile
parameter_list|)
block|{
name|map
operator|=
name|mapFile
expr_stmt|;
block|}
specifier|public
name|void
name|setMap
parameter_list|(
name|String
name|mapFileName
parameter_list|)
block|{
name|this
operator|.
name|mapFileName
operator|=
name|mapFileName
expr_stmt|;
block|}
specifier|public
name|void
name|map
parameter_list|(
name|String
name|mapFileName
parameter_list|)
block|{
name|setMap
argument_list|(
name|mapFileName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|map
parameter_list|(
name|File
name|mapFile
parameter_list|)
block|{
name|setMap
argument_list|(
name|mapFile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Internal
specifier|public
name|File
name|getDataMapFile
parameter_list|()
block|{
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
return|return
name|map
return|;
block|}
if|if
condition|(
name|mapFileName
operator|==
literal|null
condition|)
block|{
name|mapFileName
operator|=
name|getProject
argument_list|()
operator|.
name|getExtensions
argument_list|()
operator|.
name|getByType
argument_list|(
name|GradleCayenneExtension
operator|.
name|class
argument_list|)
operator|.
name|getDefaultDataMap
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|mapFileName
operator|!=
literal|null
condition|)
block|{
return|return
name|getProject
argument_list|()
operator|.
name|file
argument_list|(
name|mapFileName
argument_list|)
return|;
block|}
throw|throw
operator|new
name|InvalidUserDataException
argument_list|(
literal|"No datamap configured in task or in cayenne.defaultDataMap."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

