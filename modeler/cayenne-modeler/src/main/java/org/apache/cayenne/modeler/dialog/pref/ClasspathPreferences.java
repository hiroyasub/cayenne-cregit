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
name|dialog
operator|.
name|pref
package|;
end_package

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
name|HashMap
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
name|Map
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
name|filechooser
operator|.
name|FileFilter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|table
operator|.
name|AbstractTableModel
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_class
specifier|public
class|class
name|ClasspathPreferences
extends|extends
name|CayenneController
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ClasspathPreferences
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|ClasspathPreferencesView
name|view
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|classPathEntries
decl_stmt|;
specifier|protected
name|ClasspathTableModel
name|tableModel
decl_stmt|;
specifier|protected
name|CayennePreferenceEditor
name|editor
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|classPathKeys
decl_stmt|;
specifier|private
name|Preferences
name|preferences
decl_stmt|;
specifier|private
name|int
name|counter
decl_stmt|;
specifier|public
name|ClasspathPreferences
parameter_list|(
name|PreferenceDialog
name|parentController
parameter_list|)
block|{
name|super
argument_list|(
name|parentController
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|=
operator|new
name|ClasspathPreferencesView
argument_list|()
expr_stmt|;
comment|// this prefs node is shared with other dialog panels... be aware of
comment|// that when accessing the keys
name|this
operator|.
name|preferences
operator|=
name|getApplication
argument_list|()
operator|.
name|getPreferencesNode
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|PreferenceEditor
name|editor
init|=
name|parentController
operator|.
name|getEditor
argument_list|()
decl_stmt|;
if|if
condition|(
name|editor
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
name|editor
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|classPathEntries
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|classPathKeys
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|this
operator|.
name|counter
operator|=
name|loadPreferences
argument_list|(
name|classPathEntries
argument_list|,
name|classPathKeys
argument_list|)
expr_stmt|;
name|this
operator|.
name|classPathEntries
operator|=
name|classPathEntries
expr_stmt|;
name|this
operator|.
name|classPathKeys
operator|=
name|classPathKeys
expr_stmt|;
name|this
operator|.
name|tableModel
operator|=
operator|new
name|ClasspathTableModel
argument_list|()
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
block|}
specifier|private
name|int
name|loadPreferences
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|classPathEntries
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|classPathKeys
parameter_list|)
block|{
name|String
index|[]
name|cpKeys
decl_stmt|;
try|try
block|{
name|cpKeys
operator|=
name|preferences
operator|.
name|keys
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BackingStoreException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Error loading preferences"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
name|int
name|max
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|cpKey
range|:
name|cpKeys
control|)
block|{
name|int
name|c
decl_stmt|;
try|try
block|{
name|c
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|cpKey
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
comment|// we are sharing the 'preferences' node with other dialogs, and
comment|// this is a rather poor way of telling our preference keys from
comment|// other dialog keys ... ours are numeric, the rest are
comment|// string..
comment|// TODO: better key namespacing...
continue|continue;
block|}
if|if
condition|(
name|c
operator|>
name|max
condition|)
block|{
name|max
operator|=
name|c
expr_stmt|;
block|}
name|String
name|tempValue
init|=
name|preferences
operator|.
name|get
argument_list|(
name|cpKey
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|tempValue
argument_list|)
condition|)
block|{
name|classPathEntries
operator|.
name|add
argument_list|(
name|tempValue
argument_list|)
expr_stmt|;
name|classPathKeys
operator|.
name|add
argument_list|(
name|cpKey
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|max
return|;
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
specifier|protected
name|void
name|initBindings
parameter_list|()
block|{
name|view
operator|.
name|getTable
argument_list|()
operator|.
name|setModel
argument_list|(
name|tableModel
argument_list|)
expr_stmt|;
name|view
operator|.
name|getAddDirButton
argument_list|()
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
name|addClassDirectoryAction
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getRemoveEntryButton
argument_list|()
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
name|removeEntryAction
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|view
operator|.
name|getAddJarButton
argument_list|()
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
name|addJarOrZipAction
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|addJarOrZipAction
parameter_list|()
block|{
name|chooseClassEntry
argument_list|(
name|FileFilters
operator|.
name|getClassArchiveFilter
argument_list|()
argument_list|,
literal|"Select JAR or ZIP File."
argument_list|,
name|JFileChooser
operator|.
name|FILES_ONLY
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|addClassDirectoryAction
parameter_list|()
block|{
name|chooseClassEntry
argument_list|(
literal|null
argument_list|,
literal|"Select Java Class Directory."
argument_list|,
name|JFileChooser
operator|.
name|DIRECTORIES_ONLY
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|removeEntryAction
parameter_list|()
block|{
name|int
name|selected
init|=
name|view
operator|.
name|getTable
argument_list|()
operator|.
name|getSelectedRow
argument_list|()
decl_stmt|;
if|if
condition|(
name|selected
operator|<
literal|0
condition|)
block|{
return|return;
block|}
name|addRemovedPreferences
argument_list|(
name|classPathKeys
operator|.
name|get
argument_list|(
name|selected
argument_list|)
argument_list|)
expr_stmt|;
name|classPathEntries
operator|.
name|remove
argument_list|(
name|selected
argument_list|)
expr_stmt|;
name|classPathKeys
operator|.
name|remove
argument_list|(
name|selected
argument_list|)
expr_stmt|;
name|tableModel
operator|.
name|fireTableRowsDeleted
argument_list|(
name|selected
argument_list|,
name|selected
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|chooseClassEntry
parameter_list|(
name|FileFilter
name|filter
parameter_list|,
name|String
name|title
parameter_list|,
name|int
name|selectionMode
parameter_list|)
block|{
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
name|selectionMode
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
name|getLastDirectory
argument_list|()
operator|.
name|updateChooser
argument_list|(
name|chooser
argument_list|)
expr_stmt|;
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
name|chooser
operator|.
name|addChoosableFileFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
name|chooser
operator|.
name|setDialogTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|File
name|selected
init|=
literal|null
decl_stmt|;
name|int
name|result
init|=
name|chooser
operator|.
name|showOpenDialog
argument_list|(
name|view
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
name|selected
operator|=
name|chooser
operator|.
name|getSelectedFile
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|selected
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|classPathEntries
operator|.
name|contains
argument_list|(
name|selected
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
condition|)
block|{
comment|// store last dir in preferences
name|getLastDirectory
argument_list|()
operator|.
name|updateFromChooser
argument_list|(
name|chooser
argument_list|)
expr_stmt|;
name|int
name|len
init|=
name|classPathEntries
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|key
init|=
operator|++
name|counter
decl_stmt|;
name|String
name|value
init|=
name|selected
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|addChangedPreferences
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|key
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|classPathEntries
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|classPathKeys
operator|.
name|add
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|tableModel
operator|.
name|fireTableRowsInserted
argument_list|(
name|len
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|addChangedPreferences
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|editor
operator|.
name|getChangedPreferences
argument_list|()
operator|.
name|get
argument_list|(
name|preferences
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
name|map
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|editor
operator|.
name|getChangedPreferences
argument_list|()
operator|.
name|put
argument_list|(
name|preferences
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addRemovedPreferences
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|editor
operator|.
name|getRemovedPreferences
argument_list|()
operator|.
name|get
argument_list|(
name|preferences
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
name|map
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|editor
operator|.
name|getRemovedPreferences
argument_list|()
operator|.
name|put
argument_list|(
name|preferences
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
class|class
name|ClasspathTableModel
extends|extends
name|AbstractTableModel
block|{
specifier|public
name|int
name|getColumnCount
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
specifier|public
name|int
name|getRowCount
parameter_list|()
block|{
return|return
name|classPathEntries
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|Object
name|getValueAt
parameter_list|(
name|int
name|rowIndex
parameter_list|,
name|int
name|columnIndex
parameter_list|)
block|{
return|return
name|classPathEntries
operator|.
name|get
argument_list|(
name|rowIndex
argument_list|)
return|;
block|}
specifier|public
name|String
name|getColumnName
parameter_list|(
name|int
name|column
parameter_list|)
block|{
return|return
literal|"Custom ClassPath"
return|;
block|}
block|}
block|}
end_class

end_unit

