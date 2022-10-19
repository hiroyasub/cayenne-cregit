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
name|dialog
operator|.
name|pref
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
name|TemplateType
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
name|CodeTemplateManager
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
name|CayenneController
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
name|CayennePreferenceEditor
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
name|PreferenceEditor
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
name|swing
operator|.
name|BindingBuilder
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
name|swing
operator|.
name|control
operator|.
name|FileChooser
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
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JCheckBox
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JDialog
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
name|javax
operator|.
name|swing
operator|.
name|SwingUtilities
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|BackingStoreException
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

begin_comment
comment|/**  * @since 4.3  */
end_comment

begin_class
specifier|public
class|class
name|CreateTemplateFromDefaultController
extends|extends
name|CayenneController
block|{
specifier|protected
name|CreateTemplateFromDefaultView
name|view
decl_stmt|;
specifier|protected
name|boolean
name|canceled
decl_stmt|;
specifier|protected
name|CayennePreferenceEditor
name|editor
decl_stmt|;
specifier|protected
name|Preferences
name|preferences
decl_stmt|;
specifier|private
specifier|final
name|CodeTemplateManager
name|codeTemplateManager
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CreateTemplateFromDefaultController
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|CreateTemplateFromDefaultController
parameter_list|(
name|TemplatePreferences
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|JDialog
name|parentDialog
init|=
operator|(
name|JDialog
operator|)
name|SwingUtilities
operator|.
name|getAncestorOfClass
argument_list|(
name|JDialog
operator|.
name|class
argument_list|,
name|parent
operator|.
name|getView
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|view
operator|=
operator|new
name|CreateTemplateFromDefaultView
argument_list|(
name|parentDialog
argument_list|)
expr_stmt|;
name|PreferenceEditor
name|parentEditor
init|=
name|parent
operator|.
name|getEditor
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentEditor
operator|instanceof
name|CayennePreferenceEditor
condition|)
block|{
name|this
operator|.
name|editor
operator|=
operator|(
name|CayennePreferenceEditor
operator|)
name|parentEditor
expr_stmt|;
block|}
name|this
operator|.
name|preferences
operator|=
name|parent
operator|.
name|getTemplatePreferences
argument_list|()
expr_stmt|;
name|this
operator|.
name|codeTemplateManager
operator|=
name|application
operator|.
name|getCodeTemplateManager
argument_list|()
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
name|initListeners
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|private
name|void
name|initListeners
parameter_list|()
block|{
name|view
operator|.
name|getSelectAll
argument_list|()
operator|.
name|addActionListener
argument_list|(
name|val
lambda|->
block|{
name|boolean
name|isSelected
init|=
name|view
operator|.
name|getSelectAll
argument_list|()
operator|.
name|isSelected
argument_list|()
decl_stmt|;
name|view
operator|.
name|getTemplatesCheckboxes
argument_list|()
operator|.
name|forEach
argument_list|(
name|template
lambda|->
name|template
operator|.
name|setSelected
argument_list|(
name|isSelected
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * find start directory in preferences      */
specifier|private
name|FSPath
name|getLastTemplateDirectory
parameter_list|()
block|{
name|FSPath
name|path
init|=
operator|new
name|FSPath
argument_list|(
name|application
operator|.
name|getPreferencesNode
argument_list|(
name|CodeTemplateManager
operator|.
name|class
argument_list|,
literal|"lastTemplate"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|getPath
argument_list|()
operator|==
literal|null
condition|)
block|{
name|path
operator|.
name|setPath
argument_list|(
name|getLastDirectory
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|path
return|;
block|}
specifier|protected
name|void
name|initBindings
parameter_list|()
block|{
name|BindingBuilder
name|builder
init|=
operator|new
name|BindingBuilder
argument_list|(
name|application
operator|.
name|getBindingFactory
argument_list|()
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getCancelButton
argument_list|()
argument_list|,
literal|"cancelAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getCreateButton
argument_list|()
argument_list|,
literal|"createAction()"
argument_list|)
expr_stmt|;
name|view
operator|.
name|getFolderChooser
argument_list|()
operator|.
name|setCurrentDirectory
argument_list|(
name|getLastTemplateDirectory
argument_list|()
operator|.
name|getExistingDirectory
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|view
operator|.
name|getFolderChooser
argument_list|()
operator|.
name|addPropertyChangeListener
argument_list|(
name|FileChooser
operator|.
name|CURRENT_DIRECTORY_PROPERTY
argument_list|,
name|evt
lambda|->
name|updateLastTemplateDirectory
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|public
name|void
name|createAction
parameter_list|()
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|view
operator|.
name|getPrefix
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
operator|&&
operator|(
name|view
operator|.
name|getFolderChooser
argument_list|()
operator|.
name|getFile
argument_list|()
operator|!=
literal|null
operator|)
condition|)
block|{
name|updateLastTemplateDirectory
argument_list|()
expr_stmt|;
name|canceled
operator|=
literal|false
expr_stmt|;
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|view
argument_list|,
literal|"Enter prefix and select folder, please"
argument_list|,
literal|"Error"
argument_list|,
name|JOptionPane
operator|.
name|WARNING_MESSAGE
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|updateLastTemplateDirectory
parameter_list|()
block|{
specifier|final
name|FSPath
name|path
init|=
name|getLastTemplateDirectory
argument_list|()
decl_stmt|;
name|File
name|directory
init|=
name|view
operator|.
name|getFolderChooser
argument_list|()
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|path
operator|.
name|setDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|public
name|void
name|cancelAction
parameter_list|()
block|{
name|canceled
operator|=
literal|true
expr_stmt|;
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
comment|/**      * Pops up a dialog and blocks current thread until the dialog is closed.      */
specifier|public
name|List
argument_list|<
name|FSPath
argument_list|>
name|startupAction
parameter_list|()
block|{
name|canceled
operator|=
literal|true
expr_stmt|;
name|view
operator|.
name|setModal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|view
operator|.
name|pack
argument_list|()
expr_stmt|;
name|view
operator|.
name|setResizable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|makeCloseableOnEscape
argument_list|()
expr_stmt|;
name|centerView
argument_list|()
expr_stmt|;
name|view
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|createTemplates
argument_list|()
return|;
block|}
specifier|protected
name|ArrayList
argument_list|<
name|FSPath
argument_list|>
name|createTemplates
parameter_list|()
block|{
name|ArrayList
argument_list|<
name|FSPath
argument_list|>
name|paths
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|canceled
condition|)
block|{
name|List
argument_list|<
name|JCheckBox
argument_list|>
name|selectedTemplates
init|=
name|view
operator|.
name|getSelectedTemplates
argument_list|()
decl_stmt|;
for|for
control|(
name|JCheckBox
name|template
range|:
name|selectedTemplates
control|)
block|{
if|if
condition|(
operator|!
name|canceled
condition|)
block|{
name|paths
operator|.
name|add
argument_list|(
name|createTemplate
argument_list|(
name|template
operator|.
name|getText
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
block|}
return|return
name|paths
return|;
block|}
specifier|private
name|FSPath
name|createTemplate
parameter_list|(
name|String
name|defaultTemplateName
parameter_list|)
block|{
name|String
name|prefix
init|=
name|view
operator|.
name|getPrefix
argument_list|()
operator|.
name|getText
argument_list|()
decl_stmt|;
name|File
name|destDir
init|=
name|view
operator|.
name|getFolderChooser
argument_list|()
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|String
name|newTemplateName
init|=
name|prefix
operator|+
name|defaultTemplateName
decl_stmt|;
name|int
name|result
init|=
name|isNameExist
argument_list|(
name|newTemplateName
argument_list|)
condition|?
name|showOverwriteDialog
argument_list|(
name|newTemplateName
argument_list|)
else|:
name|JOptionPane
operator|.
name|OK_OPTION
decl_stmt|;
name|TemplateType
name|typeByName
init|=
name|TemplateType
operator|.
name|byName
argument_list|(
name|defaultTemplateName
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
name|JOptionPane
operator|.
name|OK_OPTION
operator|&&
name|typeByName
operator|!=
literal|null
condition|)
block|{
name|String
name|newTemplatePath
init|=
name|destDir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
name|File
operator|.
name|separator
operator|+
name|prefix
comment|/*+typeByName.fullFileName()*/
decl_stmt|;
name|createTemplateFile
argument_list|(
name|newTemplatePath
argument_list|,
name|defaultTemplateName
argument_list|)
expr_stmt|;
name|FSPath
name|fsPath
init|=
name|codeTemplateManager
operator|.
name|addTemplate
argument_list|(
name|newTemplatePath
argument_list|,
name|newTemplateName
argument_list|)
decl_stmt|;
name|editor
operator|.
name|getAddedNode
argument_list|()
operator|.
name|add
argument_list|(
name|fsPath
operator|.
name|getCurrentPreference
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|fsPath
return|;
block|}
if|else if
condition|(
name|result
operator|==
name|JOptionPane
operator|.
name|CANCEL_OPTION
condition|)
block|{
name|canceled
operator|=
literal|true
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|boolean
name|isNameExist
parameter_list|(
name|String
name|templateName
parameter_list|)
block|{
try|try
block|{
return|return
name|preferences
operator|.
name|nodeExists
argument_list|(
name|templateName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|BackingStoreException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Error reading preferences"
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|void
name|createTemplateFile
parameter_list|(
name|String
name|newTemplatePath
parameter_list|,
name|String
name|templateName
parameter_list|)
block|{
name|Path
name|dest
init|=
name|Paths
operator|.
name|get
argument_list|(
name|newTemplatePath
argument_list|)
decl_stmt|;
name|int
name|result
init|=
name|dest
operator|.
name|toFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|?
name|showOverwriteDialog
argument_list|(
name|dest
operator|.
name|toString
argument_list|()
argument_list|)
else|:
name|JOptionPane
operator|.
name|OK_OPTION
decl_stmt|;
name|TemplateType
name|typeByName
init|=
name|TemplateType
operator|.
name|byName
argument_list|(
name|templateName
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
name|JOptionPane
operator|.
name|OK_OPTION
operator|&&
name|typeByName
operator|!=
literal|null
condition|)
block|{
name|writeFile
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|typeByName
operator|.
name|pathFromSourceRoot
argument_list|()
argument_list|)
argument_list|,
name|dest
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|result
operator|==
name|JOptionPane
operator|.
name|CANCEL_OPTION
condition|)
block|{
name|canceled
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|writeFile
parameter_list|(
name|Path
name|source
parameter_list|,
name|Path
name|target
parameter_list|)
block|{
try|try
block|{
name|InputStream
name|stream
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|source
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|stream
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|()
throw|;
block|}
name|Files
operator|.
name|copy
argument_list|(
name|stream
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|canceled
operator|=
literal|true
expr_stmt|;
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|view
argument_list|,
literal|"File writing error \n"
operator|+
name|target
argument_list|,
literal|"Error"
argument_list|,
name|JOptionPane
operator|.
name|WARNING_MESSAGE
argument_list|)
expr_stmt|;
name|logger
operator|.
name|warn
argument_list|(
literal|"File writing error {}"
argument_list|,
name|target
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Brings up a OVERWRITE_SKIP_CANCEL option dialog      *      * @return 0 if "OVERWRITE" was selected, 1 and 2 for the "SKIP" and "CANCEL respectively "      */
specifier|private
name|int
name|showOverwriteDialog
parameter_list|(
name|String
name|dest
parameter_list|)
block|{
name|Object
index|[]
name|options
init|=
block|{
literal|"Overwrite"
block|,
literal|"Skip"
block|,
literal|"Cancel"
block|}
decl_stmt|;
return|return
name|JOptionPane
operator|.
name|showOptionDialog
argument_list|(
literal|null
argument_list|,
name|dest
operator|+
literal|" \nis already exist, overwrite?"
argument_list|,
literal|"Replace or skip the file"
argument_list|,
name|JOptionPane
operator|.
name|YES_NO_CANCEL_OPTION
argument_list|,
name|JOptionPane
operator|.
name|QUESTION_MESSAGE
argument_list|,
literal|null
argument_list|,
name|options
argument_list|,
name|options
index|[
literal|0
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

