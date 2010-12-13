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
package|;
end_package

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
name|Collections
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
name|PreferenceChangeEvent
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
name|PreferenceChangeListener
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
name|pref
operator|.
name|CayennePreference
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
name|Preference
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
name|UpgradeCayennePreference
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

begin_comment
comment|/**  * ModelerPreferences class supports persistent user preferences. Preferences are saved in  * the user home directory in "<code>$HOME/.cayenne/modeler.preferences</code>" file.  *<p>  *<i>This class is obsolete; its users will be migrated to use preference service.</i>  *</p>  */
end_comment

begin_class
specifier|public
class|class
name|ModelerPreferences
implements|implements
name|PreferenceChangeListener
block|{
specifier|private
specifier|static
name|Preferences
name|cayennePrefs
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ModelerPreferences
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/** Name of the log file. */
specifier|public
specifier|static
specifier|final
name|String
name|LOGFILE_NAME
init|=
literal|"modeler.log"
decl_stmt|;
comment|/** List of the last 12 opened project files. */
specifier|public
specifier|static
specifier|final
name|int
name|LAST_PROJ_FILES_SIZE
init|=
literal|12
decl_stmt|;
comment|/** Log file */
specifier|public
specifier|static
specifier|final
name|String
name|EDITOR_LOGFILE_ENABLED
init|=
literal|"logfileEnabled"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EDITOR_LOGFILE
init|=
literal|"logfile"
decl_stmt|;
comment|/**      * Number of items in combobox visible without scrolling      */
specifier|public
specifier|static
specifier|final
name|int
name|COMBOBOX_MAX_VISIBLE_SIZE
init|=
literal|12
decl_stmt|;
comment|/**      * Returns Cayenne preferences singleton.      */
specifier|public
specifier|static
name|Preferences
name|getPreferences
parameter_list|()
block|{
if|if
condition|(
name|cayennePrefs
operator|==
literal|null
condition|)
block|{
name|Preference
name|decoratedPref
init|=
operator|new
name|UpgradeCayennePreference
argument_list|(
operator|new
name|CayennePreference
argument_list|()
argument_list|)
decl_stmt|;
name|cayennePrefs
operator|=
name|decoratedPref
operator|.
name|getCayennePreference
argument_list|()
expr_stmt|;
name|cayennePrefs
operator|.
name|addPreferenceChangeListener
argument_list|(
operator|new
name|ModelerPreferences
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|cayennePrefs
return|;
block|}
specifier|public
specifier|static
name|Preferences
name|getEditorPreferences
parameter_list|()
block|{
return|return
name|getPreferences
argument_list|()
operator|.
name|node
argument_list|(
name|CayennePreference
operator|.
name|EDITOR
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Preferences
name|getLastProjFilesPref
parameter_list|()
block|{
return|return
name|getEditorPreferences
argument_list|()
operator|.
name|node
argument_list|(
name|CayennePreference
operator|.
name|LAST_PROJ_FILES
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getLastProjFiles
parameter_list|()
block|{
name|Preferences
name|filesPrefs
init|=
name|getLastProjFilesPref
argument_list|()
decl_stmt|;
name|ArrayList
argument_list|<
name|String
argument_list|>
name|arrayLastProjFiles
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|String
index|[]
name|keys
init|=
literal|null
decl_stmt|;
try|try
block|{
name|keys
operator|=
name|filesPrefs
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
name|logObj
operator|.
name|warn
argument_list|(
literal|"Error reading preferences file."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keys
operator|!=
literal|null
condition|)
block|{
name|int
name|len
init|=
name|keys
operator|.
name|length
decl_stmt|;
name|ArrayList
argument_list|<
name|Integer
argument_list|>
name|keysInteger
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|keysInteger
operator|.
name|add
argument_list|(
operator|new
name|Integer
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|keysInteger
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|arrayLastProjFiles
operator|.
name|add
argument_list|(
name|filesPrefs
operator|.
name|get
argument_list|(
name|keysInteger
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|arrayLastProjFiles
return|;
block|}
specifier|public
name|void
name|preferenceChange
parameter_list|(
name|PreferenceChangeEvent
name|evt
parameter_list|)
block|{
name|evt
operator|.
name|getNode
argument_list|()
operator|.
name|put
argument_list|(
name|evt
operator|.
name|getKey
argument_list|()
argument_list|,
name|evt
operator|.
name|getNewValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

