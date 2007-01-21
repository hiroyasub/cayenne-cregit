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
name|swing
operator|.
name|control
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
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
name|ActionListener
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
name|JButton
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
name|javax
operator|.
name|swing
operator|.
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTextField
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|SwingUtilities
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|filechooser
operator|.
name|FileFilter
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

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|builder
operator|.
name|DefaultFormBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|FormLayout
import|;
end_import

begin_comment
comment|/**  * A control that renders as a text field and a button to choose a file. Fires a property  * change event when a current directory is changed, either explictly or during a file  * selection by the user.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|FileChooser
extends|extends
name|JPanel
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CURRENT_DIRECTORY_PROPERTY
init|=
literal|"currentDirectory"
decl_stmt|;
specifier|protected
name|boolean
name|existingOnly
decl_stmt|;
specifier|protected
name|boolean
name|allowFiles
decl_stmt|;
specifier|protected
name|boolean
name|allowDirectories
decl_stmt|;
specifier|protected
name|FileFilter
name|fileFilter
decl_stmt|;
specifier|protected
name|String
name|title
decl_stmt|;
specifier|protected
name|File
name|currentDirectory
decl_stmt|;
specifier|protected
name|JTextField
name|fileName
decl_stmt|;
specifier|protected
name|JButton
name|chooseButton
decl_stmt|;
specifier|public
name|FileChooser
parameter_list|()
block|{
name|this
operator|.
name|allowFiles
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|allowFiles
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|fileName
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|this
operator|.
name|chooseButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"..."
argument_list|)
expr_stmt|;
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"pref:grow, 3dlu, pref"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|DefaultFormBuilder
name|builder
init|=
operator|new
name|DefaultFormBuilder
argument_list|(
name|layout
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|fileName
argument_list|,
name|chooseButton
argument_list|)
expr_stmt|;
name|chooseButton
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|chooseFileAction
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FileChooser
parameter_list|(
name|String
name|title
parameter_list|,
name|boolean
name|allowFiles
parameter_list|,
name|boolean
name|allowDirectories
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|allowFiles
operator|=
name|allowFiles
expr_stmt|;
name|this
operator|.
name|allowDirectories
operator|=
name|allowDirectories
expr_stmt|;
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
specifier|public
name|File
name|getFile
parameter_list|()
block|{
name|String
name|value
init|=
name|fileName
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingOnly
operator|&&
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|file
return|;
block|}
specifier|public
name|void
name|setFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|fileName
operator|.
name|setText
argument_list|(
name|file
operator|!=
literal|null
condition|?
name|file
operator|.
name|getAbsolutePath
argument_list|()
else|:
literal|""
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|chooseFileAction
parameter_list|()
block|{
name|int
name|mode
init|=
name|getSelectionMode
argument_list|()
decl_stmt|;
name|JFileChooser
name|chooser
init|=
operator|new
name|JFileChooser
argument_list|()
decl_stmt|;
name|chooser
operator|.
name|setFileSelectionMode
argument_list|(
name|mode
argument_list|)
expr_stmt|;
name|chooser
operator|.
name|setDialogType
argument_list|(
name|JFileChooser
operator|.
name|OPEN_DIALOG
argument_list|)
expr_stmt|;
name|chooser
operator|.
name|setAcceptAllFileFilterUsed
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|fileFilter
operator|!=
literal|null
condition|)
block|{
name|chooser
operator|.
name|setFileFilter
argument_list|(
name|fileFilter
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|currentDirectory
operator|!=
literal|null
condition|)
block|{
name|chooser
operator|.
name|setCurrentDirectory
argument_list|(
name|currentDirectory
argument_list|)
expr_stmt|;
block|}
name|chooser
operator|.
name|setDialogTitle
argument_list|(
name|makeTitle
argument_list|(
name|mode
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|result
init|=
name|chooser
operator|.
name|showOpenDialog
argument_list|(
name|SwingUtilities
operator|.
name|getWindowAncestor
argument_list|(
name|this
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
name|JFileChooser
operator|.
name|APPROVE_OPTION
condition|)
block|{
name|File
name|selected
init|=
name|chooser
operator|.
name|getSelectedFile
argument_list|()
decl_stmt|;
name|fileName
operator|.
name|setText
argument_list|(
name|selected
operator|!=
literal|null
condition|?
name|selected
operator|.
name|getAbsolutePath
argument_list|()
else|:
literal|""
argument_list|)
expr_stmt|;
block|}
name|setCurrentDirectory
argument_list|(
name|chooser
operator|.
name|getCurrentDirectory
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|makeTitle
parameter_list|(
name|int
name|selectionMode
parameter_list|)
block|{
if|if
condition|(
name|title
operator|!=
literal|null
condition|)
block|{
return|return
name|title
return|;
block|}
switch|switch
condition|(
name|selectionMode
condition|)
block|{
case|case
name|JFileChooser
operator|.
name|FILES_AND_DIRECTORIES
case|:
return|return
literal|"Choose a file or a directory"
return|;
case|case
name|JFileChooser
operator|.
name|DIRECTORIES_ONLY
case|:
return|return
literal|"Choose a directory"
return|;
default|default:
return|return
literal|"Choose a file"
return|;
block|}
block|}
specifier|protected
name|int
name|getSelectionMode
parameter_list|()
block|{
if|if
condition|(
name|allowFiles
operator|&&
name|allowDirectories
condition|)
block|{
return|return
name|JFileChooser
operator|.
name|FILES_AND_DIRECTORIES
return|;
block|}
if|else if
condition|(
name|allowFiles
operator|&&
operator|!
name|allowDirectories
condition|)
block|{
return|return
name|JFileChooser
operator|.
name|FILES_ONLY
return|;
block|}
if|else if
condition|(
operator|!
name|allowFiles
operator|&&
name|allowDirectories
condition|)
block|{
return|return
name|JFileChooser
operator|.
name|DIRECTORIES_ONLY
return|;
block|}
else|else
block|{
comment|// invalid combination... return files...
return|return
name|JFileChooser
operator|.
name|FILES_ONLY
return|;
block|}
block|}
specifier|public
name|boolean
name|isAllowDirectories
parameter_list|()
block|{
return|return
name|allowDirectories
return|;
block|}
specifier|public
name|void
name|setAllowDirectories
parameter_list|(
name|boolean
name|allowDirectories
parameter_list|)
block|{
name|this
operator|.
name|allowDirectories
operator|=
name|allowDirectories
expr_stmt|;
block|}
specifier|public
name|boolean
name|isAllowFiles
parameter_list|()
block|{
return|return
name|allowFiles
return|;
block|}
specifier|public
name|void
name|setAllowFiles
parameter_list|(
name|boolean
name|allowFiles
parameter_list|)
block|{
name|this
operator|.
name|allowFiles
operator|=
name|allowFiles
expr_stmt|;
block|}
specifier|public
name|FileFilter
name|getFileFilter
parameter_list|()
block|{
return|return
name|fileFilter
return|;
block|}
specifier|public
name|void
name|setFileFilter
parameter_list|(
name|FileFilter
name|filteFiler
parameter_list|)
block|{
name|this
operator|.
name|fileFilter
operator|=
name|filteFiler
expr_stmt|;
block|}
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExistingOnly
parameter_list|()
block|{
return|return
name|existingOnly
return|;
block|}
specifier|public
name|void
name|setExistingOnly
parameter_list|(
name|boolean
name|existingOnly
parameter_list|)
block|{
name|this
operator|.
name|existingOnly
operator|=
name|existingOnly
expr_stmt|;
block|}
specifier|public
name|void
name|setColumns
parameter_list|(
name|int
name|col
parameter_list|)
block|{
name|fileName
operator|.
name|setColumns
argument_list|(
name|col
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getColumns
parameter_list|()
block|{
return|return
name|fileName
operator|.
name|getColumns
argument_list|()
return|;
block|}
comment|/**      * Returns the last directory visited when picking a file.      */
specifier|public
name|File
name|getCurrentDirectory
parameter_list|()
block|{
return|return
name|currentDirectory
return|;
block|}
specifier|public
name|void
name|setCurrentDirectory
parameter_list|(
name|File
name|currentDirectory
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|currentDirectory
argument_list|,
name|currentDirectory
argument_list|)
condition|)
block|{
name|File
name|oldValue
init|=
name|this
operator|.
name|currentDirectory
decl_stmt|;
name|this
operator|.
name|currentDirectory
operator|=
name|currentDirectory
expr_stmt|;
name|firePropertyChange
argument_list|(
name|CURRENT_DIRECTORY_PROPERTY
argument_list|,
name|oldValue
argument_list|,
name|currentDirectory
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

