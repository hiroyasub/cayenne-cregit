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
name|graph
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
name|image
operator|.
name|BufferedImage
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
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
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
name|JOptionPane
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
name|graph
operator|.
name|DataDomainGraphTab
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
name|pref
operator|.
name|FSPath
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
name|CayenneAction
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
name|FileFilters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgraph
operator|.
name|JGraph
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Action for saving graph as image  */
end_comment

begin_class
specifier|public
class|class
name|SaveAsImageAction
extends|extends
name|CayenneAction
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|logObj
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SaveAsImageAction
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|DataDomainGraphTab
name|dataDomainGraphTab
decl_stmt|;
specifier|public
name|SaveAsImageAction
parameter_list|(
name|DataDomainGraphTab
name|dataDomainGraphTab
parameter_list|,
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
literal|"Save As Image"
argument_list|,
name|application
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataDomainGraphTab
operator|=
name|dataDomainGraphTab
expr_stmt|;
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-save-as-image.png"
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
comment|// find start directory in preferences
name|FSPath
name|lastDir
init|=
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getLastDirectory
argument_list|()
decl_stmt|;
comment|// configure dialog
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
name|JFileChooser
operator|.
name|FILES_ONLY
argument_list|)
expr_stmt|;
name|lastDir
operator|.
name|updateChooser
argument_list|(
name|chooser
argument_list|)
expr_stmt|;
name|chooser
operator|.
name|setAcceptAllFileFilterUsed
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|String
name|ext
init|=
literal|"png"
decl_stmt|;
name|chooser
operator|.
name|addChoosableFileFilter
argument_list|(
name|FileFilters
operator|.
name|getExtensionFileFilter
argument_list|(
name|ext
argument_list|,
literal|"PNG Images"
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|status
init|=
name|chooser
operator|.
name|showSaveDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|status
operator|==
name|JFileChooser
operator|.
name|APPROVE_OPTION
condition|)
block|{
name|lastDir
operator|.
name|updateFromChooser
argument_list|(
name|chooser
argument_list|)
expr_stmt|;
name|String
name|path
init|=
name|chooser
operator|.
name|getSelectedFile
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|path
operator|.
name|endsWith
argument_list|(
literal|"."
operator|+
name|ext
argument_list|)
condition|)
block|{
name|path
operator|+=
literal|"."
operator|+
name|ext
expr_stmt|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
decl_stmt|;
try|try
block|{
name|JGraph
name|graph
init|=
name|dataDomainGraphTab
operator|.
name|getGraph
argument_list|()
decl_stmt|;
name|BufferedImage
name|img
init|=
name|graph
operator|.
name|getImage
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|int
name|response
init|=
name|JOptionPane
operator|.
name|showConfirmDialog
argument_list|(
literal|null
argument_list|,
literal|"Do you want to replace the existing file?"
argument_list|,
literal|"Confirm"
argument_list|,
name|JOptionPane
operator|.
name|YES_NO_OPTION
argument_list|,
name|JOptionPane
operator|.
name|QUESTION_MESSAGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|!=
name|JOptionPane
operator|.
name|YES_OPTION
condition|)
block|{
return|return;
block|}
block|}
try|try
init|(
name|OutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
init|)
block|{
name|ImageIO
operator|.
name|write
argument_list|(
name|img
argument_list|,
name|ext
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|logObj
operator|.
name|error
argument_list|(
literal|"Could not save image"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|"Could not save image."
argument_list|,
literal|"Error saving image"
argument_list|,
name|JOptionPane
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

