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
name|modeler
operator|.
name|action
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Toolkit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|KeyEvent
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
name|javax
operator|.
name|swing
operator|.
name|KeyStroke
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
name|modeler
operator|.
name|Application
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
name|modeler
operator|.
name|event
operator|.
name|ProjectSavedEvent
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
name|pref
operator|.
name|RenamedPreferences
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
name|project
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
name|cayenne
operator|.
name|project
operator|.
name|ProjectSaver
import|;
end_import

begin_comment
comment|/**  * An action that saves a project using to its default location.  */
end_comment

begin_class
specifier|public
class|class
name|SaveAction
extends|extends
name|SaveAsAction
block|{
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Save"
return|;
block|}
specifier|public
name|SaveAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|getActionName
argument_list|()
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|KeyStroke
name|getAcceleratorKey
parameter_list|()
block|{
return|return
name|KeyStroke
operator|.
name|getKeyStroke
argument_list|(
name|KeyEvent
operator|.
name|VK_S
argument_list|,
name|Toolkit
operator|.
name|getDefaultToolkit
argument_list|()
operator|.
name|getMenuShortcutKeyMaskEx
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-save.png"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|saveAll
parameter_list|()
throws|throws
name|Exception
block|{
name|Project
name|p
init|=
name|getCurrentProject
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
operator|||
name|p
operator|.
name|getConfigurationResource
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|super
operator|.
name|saveAll
argument_list|()
return|;
block|}
name|String
name|oldPath
init|=
name|p
operator|.
name|getConfigurationResource
argument_list|()
operator|.
name|getURL
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|File
name|oldProjectFile
init|=
operator|new
name|File
argument_list|(
name|p
operator|.
name|getConfigurationResource
argument_list|()
operator|.
name|getURL
argument_list|()
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|getProjectController
argument_list|()
operator|.
name|getFileChangeTracker
argument_list|()
operator|.
name|pauseWatching
argument_list|()
expr_stmt|;
name|ProjectSaver
name|saver
init|=
name|getApplication
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|ProjectSaver
operator|.
name|class
argument_list|)
decl_stmt|;
name|saver
operator|.
name|save
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|RenamedPreferences
operator|.
name|removeOldPreferences
argument_list|()
expr_stmt|;
comment|// if change DataChanelDescriptor name - as result change name of xml file
comment|// we will need change preferences path
name|String
index|[]
name|path
init|=
name|oldPath
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|String
index|[]
name|newPath
init|=
name|p
operator|.
name|getConfigurationResource
argument_list|()
operator|.
name|getURL
argument_list|()
operator|.
name|getPath
argument_list|()
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|path
index|[
name|path
operator|.
name|length
operator|-
literal|1
index|]
operator|.
name|equals
argument_list|(
name|newPath
index|[
name|newPath
operator|.
name|length
operator|-
literal|1
index|]
argument_list|)
condition|)
block|{
name|String
name|newName
init|=
name|newPath
index|[
name|newPath
operator|.
name|length
operator|-
literal|1
index|]
operator|.
name|replace
argument_list|(
literal|".xml"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|RenamedPreferences
operator|.
name|copyPreferences
argument_list|(
name|newName
argument_list|,
name|getProjectController
argument_list|()
operator|.
name|getPreferenceForProject
argument_list|()
argument_list|)
expr_stmt|;
name|RenamedPreferences
operator|.
name|removeOldPreferences
argument_list|()
expr_stmt|;
block|}
name|File
name|newProjectFile
init|=
operator|new
name|File
argument_list|(
name|p
operator|.
name|getConfigurationResource
argument_list|()
operator|.
name|getURL
argument_list|()
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|changePathInLastProjListAction
argument_list|(
name|oldProjectFile
argument_list|,
name|newProjectFile
argument_list|)
expr_stmt|;
name|Application
operator|.
name|getFrame
argument_list|()
operator|.
name|fireRecentFileListChanged
argument_list|()
expr_stmt|;
comment|// Reset the watcher now
name|getProjectController
argument_list|()
operator|.
name|getFileChangeTracker
argument_list|()
operator|.
name|reconfigure
argument_list|()
expr_stmt|;
name|getProjectController
argument_list|()
operator|.
name|fireProjectSavedEvent
argument_list|(
operator|new
name|ProjectSavedEvent
argument_list|(
name|getProjectController
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

