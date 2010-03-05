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
name|awt
operator|.
name|Dialog
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Frame
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Window
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
name|Collection
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
name|JFrame
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JRootPane
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|CayenneServerModule
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
name|DataChannelDescriptor
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
name|di
operator|.
name|DIBootstrap
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
name|di
operator|.
name|Injector
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
name|di
operator|.
name|Module
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
name|LogConsole
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
name|ClasspathPreferences
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
name|undo
operator|.
name|CayenneUndoManager
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
name|AdapterMapping
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
name|CayenneDialog
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
name|CayenneUserDir
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
name|CayenneProjectPreferences
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
name|project2
operator|.
name|CayenneProjectModule
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
name|project2
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
name|swing
operator|.
name|BindingFactory
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
name|collections
operator|.
name|CollectionUtils
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
name|collections
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|controller
operator|.
name|basic
operator|.
name|ViewContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|controller
operator|.
name|swing
operator|.
name|SwingContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|core
operator|.
name|View
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|util
operator|.
name|UIStrings
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|view
operator|.
name|swing
operator|.
name|SwingView
import|;
end_import

begin_comment
comment|/**  * A main modeler application class that provides a number of services to the Modeler  * components. Configuration properties:  *<ul>  *<li>cayenne.modeler.application.name - name of the application, 'CayenneModeler' is  * default. Used to locate preferences domain among other things.</li>  *<li>cayenne.modeler.pref.version - a version of the preferences DB schema. Default is  * "1.1".</li>  *</ul>  *   */
end_comment

begin_class
specifier|public
class|class
name|Application
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PREFERENCES_VERSION
init|=
literal|"1.2"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PREFERENCES_DB_SUBDIRECTORY
init|=
literal|"prefs"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PREFERENCES_MAP_PACKAGE
init|=
literal|"pref"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|APPLICATION_NAME_PROPERTY
init|=
literal|"cayenne.modeler.application.name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PREFERENCES_VERSION_PROPERTY
init|=
literal|"cayenne.modeler.pref.version"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_APPLICATION_NAME
init|=
literal|"CayenneModeler"
decl_stmt|;
comment|// TODO: implement cleaner IoC approach to avoid using this singleton...
specifier|protected
specifier|static
name|Application
name|instance
decl_stmt|;
specifier|protected
name|FileClassLoadingService
name|modelerClassLoader
decl_stmt|;
specifier|protected
name|ActionManager
name|actionManager
decl_stmt|;
specifier|protected
name|CayenneModelerController
name|frameController
decl_stmt|;
specifier|protected
name|File
name|initialProject
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|String
name|preferencesDB
decl_stmt|;
specifier|protected
name|BindingFactory
name|bindingFactory
decl_stmt|;
specifier|protected
name|AdapterMapping
name|adapterMapping
decl_stmt|;
specifier|protected
name|CayenneUndoManager
name|undoManager
decl_stmt|;
specifier|protected
name|CayenneProjectPreferences
name|cayenneProjectPreferences
decl_stmt|;
comment|// This is for OS X support
specifier|private
name|boolean
name|isQuittingApplication
init|=
literal|false
decl_stmt|;
specifier|protected
name|CayennePreference
name|cayennePreference
decl_stmt|;
specifier|protected
name|Injector
name|injector
decl_stmt|;
specifier|public
specifier|static
name|Application
name|getInstance
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
comment|// static methods that should probably go away eventually...
specifier|public
specifier|static
name|CayenneModelerFrame
name|getFrame
parameter_list|()
block|{
return|return
operator|(
name|CayenneModelerFrame
operator|)
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getView
argument_list|()
return|;
block|}
specifier|public
name|Injector
name|getInjector
parameter_list|()
block|{
return|return
name|injector
return|;
block|}
specifier|public
specifier|static
name|Project
name|getProject
parameter_list|()
block|{
return|return
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
operator|.
name|getProject
argument_list|()
return|;
block|}
specifier|public
name|Application
parameter_list|(
name|File
name|initialProject
parameter_list|)
block|{
name|this
operator|.
name|initialProject
operator|=
name|initialProject
expr_stmt|;
name|Module
name|module
init|=
operator|new
name|CayenneProjectModule
argument_list|()
decl_stmt|;
name|Module
name|moduleSer
init|=
operator|new
name|CayenneServerModule
argument_list|(
literal|"CayenneModeler"
argument_list|)
decl_stmt|;
name|injector
operator|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|module
argument_list|,
name|moduleSer
argument_list|)
expr_stmt|;
comment|// configure startup settings
name|String
name|configuredName
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|APPLICATION_NAME_PROPERTY
argument_list|)
decl_stmt|;
name|this
operator|.
name|name
operator|=
operator|(
name|configuredName
operator|!=
literal|null
operator|)
condition|?
name|configuredName
else|:
name|DEFAULT_APPLICATION_NAME
expr_stmt|;
name|String
name|subdir
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|PREFERENCES_VERSION_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|subdir
operator|==
literal|null
condition|)
block|{
name|subdir
operator|=
name|PREFERENCES_VERSION
expr_stmt|;
block|}
name|File
name|dbDir
init|=
operator|new
name|File
argument_list|(
name|CayenneUserDir
operator|.
name|getInstance
argument_list|()
operator|.
name|resolveFile
argument_list|(
name|PREFERENCES_DB_SUBDIRECTORY
argument_list|)
argument_list|,
name|subdir
argument_list|)
decl_stmt|;
name|dbDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|this
operator|.
name|cayennePreference
operator|=
operator|new
name|CayennePreference
argument_list|()
expr_stmt|;
name|this
operator|.
name|preferencesDB
operator|=
operator|new
name|File
argument_list|(
name|dbDir
argument_list|,
literal|"db"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Preferences
name|getPreferencesNode
parameter_list|(
name|Class
name|className
parameter_list|,
name|String
name|path
parameter_list|)
block|{
return|return
name|cayennePreference
operator|.
name|getNode
argument_list|(
name|className
argument_list|,
name|path
argument_list|)
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|ClassLoadingService
name|getClassLoadingService
parameter_list|()
block|{
return|return
name|modelerClassLoader
return|;
block|}
specifier|public
name|AdapterMapping
name|getAdapterMapping
parameter_list|()
block|{
return|return
name|adapterMapping
return|;
block|}
comment|/**      * Returns an action for key.      */
specifier|public
name|CayenneAction
name|getAction
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * Returns action controller.      */
specifier|public
name|ActionManager
name|getActionManager
parameter_list|()
block|{
return|return
name|actionManager
return|;
block|}
comment|/**      * Returns undo-edits controller.      */
specifier|public
name|CayenneUndoManager
name|getUndoManager
parameter_list|()
block|{
return|return
name|undoManager
return|;
block|}
comment|/**      * Returns controller for the main frame.      */
specifier|public
name|CayenneModelerController
name|getFrameController
parameter_list|()
block|{
return|return
name|frameController
return|;
block|}
comment|/**      * Starts the application.      */
specifier|public
name|void
name|startup
parameter_list|()
block|{
comment|// init subsystems
name|initPreferences
argument_list|()
expr_stmt|;
name|initClassLoader
argument_list|()
expr_stmt|;
name|this
operator|.
name|bindingFactory
operator|=
operator|new
name|BindingFactory
argument_list|()
expr_stmt|;
name|this
operator|.
name|adapterMapping
operator|=
operator|new
name|AdapterMapping
argument_list|()
expr_stmt|;
comment|// ...Scope
comment|// TODO: this will go away if switch away from Scope
comment|// force Scope to use CayenneModeler properties
name|UIStrings
operator|.
name|setPropertiesName
argument_list|(
name|ModelerConstants
operator|.
name|DEFAULT_MESSAGE_BUNDLE
argument_list|)
expr_stmt|;
name|ViewContext
operator|.
name|clearThreadContext
argument_list|()
expr_stmt|;
comment|// init actions before the frame, as it will attempt to build menus out of
comment|// actions.
name|this
operator|.
name|actionManager
operator|=
operator|new
name|ActionManager
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|undoManager
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|undo
operator|.
name|CayenneUndoManager
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// ...create main frame
name|this
operator|.
name|frameController
operator|=
operator|new
name|CayenneModelerController
argument_list|(
name|this
argument_list|,
name|initialProject
argument_list|)
expr_stmt|;
comment|// update Scope to work nicely with main frame
name|ViewContext
operator|.
name|setGlobalContext
argument_list|(
operator|new
name|ModelerContext
argument_list|(
name|frameController
operator|.
name|getFrame
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// open up
name|frameController
operator|.
name|startupAction
argument_list|()
expr_stmt|;
comment|/**          * After prefs have been loaded, we can now show the console if needed          */
name|LogConsole
operator|.
name|getInstance
argument_list|()
operator|.
name|showConsoleIfNeeded
argument_list|()
expr_stmt|;
name|getFrame
argument_list|()
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BindingFactory
name|getBindingFactory
parameter_list|()
block|{
return|return
name|bindingFactory
return|;
block|}
specifier|public
name|CayenneProjectPreferences
name|getCayenneProjectPreferences
parameter_list|()
block|{
return|return
name|cayenneProjectPreferences
return|;
block|}
specifier|public
specifier|static
name|Preferences
name|getMainPreferenceForProject
parameter_list|()
block|{
name|String
name|path
init|=
name|CayennePreference
operator|.
name|filePathToPrefereceNodePath
argument_list|(
operator|(
operator|(
name|DataChannelDescriptor
operator|)
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
operator|)
operator|.
name|getConfigurationSource
argument_list|()
operator|.
name|getURL
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|Preferences
name|pref
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getPreferencesNode
argument_list|(
name|getProject
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|,
literal|""
argument_list|)
decl_stmt|;
return|return
name|pref
operator|.
name|node
argument_list|(
name|pref
operator|.
name|absolutePath
argument_list|()
operator|+
name|path
argument_list|)
return|;
block|}
comment|/**      * Returns a new instance of CodeTemplateManager.      */
specifier|public
name|CodeTemplateManager
name|getCodeTemplateManager
parameter_list|()
block|{
return|return
operator|new
name|CodeTemplateManager
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Reinitializes ModelerClassLoader from preferences.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|initClassLoader
parameter_list|()
block|{
specifier|final
name|FileClassLoadingService
name|classLoader
init|=
operator|new
name|FileClassLoadingService
argument_list|()
decl_stmt|;
comment|// init from preferences...
name|Preferences
name|classLoaderPreference
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getPreferencesNode
argument_list|(
name|ClasspathPreferences
operator|.
name|class
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|Collection
name|details
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
name|classLoaderPreference
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
comment|// do nothing
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|keys
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|details
operator|.
name|add
argument_list|(
name|keys
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|details
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// transform preference to file...
name|Transformer
name|transformer
init|=
operator|new
name|Transformer
argument_list|()
block|{
specifier|public
name|Object
name|transform
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|String
name|pref
init|=
operator|(
name|String
operator|)
name|object
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|pref
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|classLoader
operator|.
name|setPathFiles
argument_list|(
name|CollectionUtils
operator|.
name|collect
argument_list|(
name|details
argument_list|,
name|transformer
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|modelerClassLoader
operator|=
name|classLoader
expr_stmt|;
comment|// set as EventDispatch thread default class loader
if|if
condition|(
name|SwingUtilities
operator|.
name|isEventDispatchThread
argument_list|()
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|classLoader
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SwingUtilities
operator|.
name|invokeLater
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|classLoader
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|initPreferences
parameter_list|()
block|{
name|this
operator|.
name|cayenneProjectPreferences
operator|=
operator|new
name|CayenneProjectPreferences
argument_list|()
expr_stmt|;
block|}
specifier|final
class|class
name|ModelerContext
extends|extends
name|SwingContext
block|{
name|JFrame
name|frame
decl_stmt|;
specifier|public
name|ModelerContext
parameter_list|(
name|JFrame
name|frame
parameter_list|)
block|{
name|this
operator|.
name|frame
operator|=
name|frame
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|showViewInPrimaryWindow
parameter_list|(
name|SwingView
name|view
parameter_list|)
block|{
block|}
comment|/**          * Creates closeable dialogs.          */
annotation|@
name|Override
specifier|protected
name|void
name|showViewInDialog
parameter_list|(
name|SwingView
name|inView
parameter_list|)
block|{
comment|// NOTE:
comment|// copied from superclass, except that JDialog is substituted for
comment|// CayenneDialog
comment|// Keep in mind when upgrading Scope to the newer versions.
comment|// Make a JDialog to contain the view.
name|Window
name|parentWindow
init|=
name|getDefaultParentWindow
argument_list|()
decl_stmt|;
specifier|final
name|CayenneDialog
name|dialog
decl_stmt|;
if|if
condition|(
name|parentWindow
operator|instanceof
name|Dialog
condition|)
block|{
name|dialog
operator|=
operator|new
name|CayenneDialog
argument_list|(
operator|(
name|Dialog
operator|)
name|parentWindow
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dialog
operator|=
operator|new
name|CayenneDialog
argument_list|(
operator|(
name|Frame
operator|)
name|parentWindow
argument_list|)
expr_stmt|;
block|}
comment|// Set title, modality, resizability
if|if
condition|(
name|inView
operator|.
name|getTitle
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|dialog
operator|.
name|setTitle
argument_list|(
name|inView
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|inView
operator|.
name|getDisplayMode
argument_list|()
operator|==
name|SwingView
operator|.
name|MODAL_DIALOG
condition|)
block|{
name|dialog
operator|.
name|setModal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dialog
operator|.
name|setModal
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|dialog
operator|.
name|setResizable
argument_list|(
name|inView
operator|.
name|isResizable
argument_list|()
argument_list|)
expr_stmt|;
name|setupWindow
argument_list|(
name|dialog
operator|.
name|getRootPane
argument_list|()
argument_list|,
name|inView
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|dialog
operator|.
name|toFront
argument_list|()
expr_stmt|;
block|}
comment|/**          * Overrides super implementation to allow using Scope together with normal Swing          * code that CayenneModeler already has.          */
annotation|@
name|Override
specifier|public
name|JRootPane
name|findRootPaneFor
parameter_list|(
name|View
name|view
parameter_list|)
block|{
name|JRootPane
name|pane
init|=
name|super
operator|.
name|findRootPaneFor
argument_list|(
name|view
argument_list|)
decl_stmt|;
if|if
condition|(
name|pane
operator|!=
literal|null
condition|)
block|{
return|return
name|pane
return|;
block|}
if|if
condition|(
operator|(
operator|(
name|SwingView
operator|)
name|view
operator|)
operator|.
name|getDisplayMode
argument_list|()
operator|!=
name|SwingView
operator|.
name|PRIMARY_WINDOW
condition|)
block|{
return|return
name|pane
return|;
block|}
return|return
name|frame
operator|.
name|getRootPane
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Window
name|getDefaultParentWindow
parameter_list|()
block|{
return|return
name|frame
return|;
block|}
block|}
specifier|public
name|boolean
name|isQuittingApplication
parameter_list|()
block|{
return|return
name|isQuittingApplication
return|;
block|}
specifier|public
name|void
name|setQuittingApplication
parameter_list|(
name|boolean
name|isQuittingApplication
parameter_list|)
block|{
name|this
operator|.
name|isQuittingApplication
operator|=
name|isQuittingApplication
expr_stmt|;
block|}
block|}
end_class

end_unit

