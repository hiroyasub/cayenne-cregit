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
name|modeler
operator|.
name|editor
operator|.
name|cgen
operator|.
name|domain
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Files
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
name|Path
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
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|prefs
operator|.
name|Preferences
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
name|configuration
operator|.
name|xml
operator|.
name|DataChannelMetaData
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
name|gen
operator|.
name|ClassGenerationActionFactory
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
name|ProjectController
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
name|dialog
operator|.
name|pref
operator|.
name|GeneralPreferences
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
name|editor
operator|.
name|GeneratorsTabController
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
name|DataMapDisplayEvent
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
name|util
operator|.
name|ModelerUtil
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
name|tools
operator|.
name|ToolsInjectorBuilder
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|CgenTabController
extends|extends
name|GeneratorsTabController
block|{
specifier|public
name|CgenTabController
parameter_list|(
name|ProjectController
name|projectController
parameter_list|)
block|{
name|super
argument_list|(
name|CgenConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|projectController
operator|=
name|projectController
expr_stmt|;
name|this
operator|.
name|view
operator|=
operator|new
name|CgenTab
argument_list|(
name|projectController
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|runGenerators
parameter_list|(
name|Set
argument_list|<
name|DataMap
argument_list|>
name|dataMaps
parameter_list|)
block|{
name|DataChannelMetaData
name|metaData
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataMaps
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|view
operator|.
name|showEmptyMessage
argument_list|()
expr_stmt|;
return|return;
block|}
name|boolean
name|generationFail
init|=
literal|false
decl_stmt|;
for|for
control|(
name|DataMap
name|dataMap
range|:
name|dataMaps
control|)
block|{
try|try
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
name|metaData
operator|.
name|get
argument_list|(
name|dataMap
argument_list|,
name|CgenConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cgenConfiguration
operator|==
literal|null
condition|)
block|{
name|cgenConfiguration
operator|=
name|createConfiguration
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
block|}
name|ClassGenerationAction
name|classGenerationAction
init|=
operator|new
name|ToolsInjectorBuilder
argument_list|()
operator|.
name|create
argument_list|()
operator|.
name|getInstance
argument_list|(
name|ClassGenerationActionFactory
operator|.
name|class
argument_list|)
operator|.
name|createAction
argument_list|(
name|cgenConfiguration
argument_list|)
decl_stmt|;
name|classGenerationAction
operator|.
name|prepareArtifacts
argument_list|()
expr_stmt|;
name|classGenerationAction
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logObj
operator|.
name|error
argument_list|(
literal|"Error generating classes"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|generationFail
operator|=
literal|true
expr_stmt|;
operator|(
operator|(
name|CgenTab
operator|)
name|view
operator|)
operator|.
name|showErrorMessage
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|generationFail
condition|)
block|{
operator|(
operator|(
name|CgenTab
operator|)
name|view
operator|)
operator|.
name|showSuccessMessage
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|CgenConfiguration
name|createConfiguration
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
operator|new
name|CgenConfiguration
argument_list|()
decl_stmt|;
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|injectMembers
argument_list|(
name|cgenConfiguration
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|Path
name|basePath
init|=
name|Paths
operator|.
name|get
argument_list|(
name|ModelerUtil
operator|.
name|initOutputFolder
argument_list|()
argument_list|)
decl_stmt|;
comment|// no destination folder
if|if
condition|(
name|basePath
operator|==
literal|null
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
literal|"Select directory for source files."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// no such folder
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
try|try
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
literal|"Can't create directory. "
operator|+
literal|". Select a different one."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|// not a directory
if|if
condition|(
operator|!
name|Files
operator|.
name|isDirectory
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
name|basePath
operator|+
literal|" is not a valid directory."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|cgenConfiguration
operator|.
name|setRootPath
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
name|Preferences
name|preferences
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getPreferencesNode
argument_list|(
name|GeneralPreferences
operator|.
name|class
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|preferences
operator|!=
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|setEncoding
argument_list|(
name|preferences
operator|.
name|get
argument_list|(
name|GeneralPreferences
operator|.
name|ENCODING_PREFERENCE
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|cgenConfiguration
operator|.
name|resolveExcludeEntities
argument_list|()
expr_stmt|;
name|cgenConfiguration
operator|.
name|resolveExcludeEmbeddables
argument_list|()
expr_stmt|;
return|return
name|cgenConfiguration
return|;
block|}
specifier|public
name|void
name|showConfig
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
if|if
condition|(
name|dataMap
operator|!=
literal|null
condition|)
block|{
name|projectController
operator|.
name|fireDataMapDisplayEvent
argument_list|(
operator|new
name|DataMapDisplayEvent
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
name|dataMap
argument_list|,
name|dataMap
operator|.
name|getDataChannelDescriptor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

