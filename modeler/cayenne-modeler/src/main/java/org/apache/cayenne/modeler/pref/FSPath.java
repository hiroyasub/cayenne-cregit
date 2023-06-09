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
name|pref
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
name|util
operator|.
name|prefs
operator|.
name|Preferences
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JFileChooser
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
name|CayennePreference
import|;
end_import

begin_comment
comment|/**  * Represents a preferred directory or file.  *   */
end_comment

begin_class
specifier|public
class|class
name|FSPath
extends|extends
name|CayennePreference
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PATH_PROPERTY
init|=
literal|"path"
decl_stmt|;
specifier|public
name|FSPath
parameter_list|(
name|Preferences
name|preferences
parameter_list|)
block|{
name|setCurrentPreference
argument_list|(
name|preferences
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|updateFromChooser
parameter_list|(
name|JFileChooser
name|chooser
parameter_list|)
block|{
name|File
name|file
init|=
name|chooser
operator|.
name|getSelectedFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|setDirectory
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|updateChooser
parameter_list|(
name|JFileChooser
name|chooser
parameter_list|)
block|{
name|File
name|startDir
init|=
name|getExistingDirectory
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|startDir
operator|!=
literal|null
condition|)
block|{
name|chooser
operator|.
name|setCurrentDirectory
argument_list|(
name|startDir
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setDirectory
parameter_list|(
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
name|file
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|setPath
argument_list|(
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setPath
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|File
name|getExistingDirectory
parameter_list|(
name|boolean
name|create
parameter_list|)
block|{
if|if
condition|(
name|getPath
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|File
name|path
init|=
operator|new
name|File
argument_list|(
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return
name|path
return|;
block|}
if|if
condition|(
name|path
operator|.
name|isFile
argument_list|()
condition|)
block|{
return|return
name|path
operator|.
name|getParentFile
argument_list|()
return|;
block|}
if|if
condition|(
name|create
condition|)
block|{
name|path
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
return|return
name|path
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|PATH_PROPERTY
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|PATH_PROPERTY
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|getCurrentPreference
argument_list|()
operator|.
name|name
argument_list|()
return|;
block|}
block|}
end_class

end_unit

