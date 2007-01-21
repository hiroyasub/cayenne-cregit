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
name|Collection
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
name|JOptionPane
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
name|pref
operator|.
name|Domain
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
name|DomainPreference
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
name|HSQLEmbeddedPreferenceEditor
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
name|HSQLEmbeddedPreferenceService
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
name|PreferenceService
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
name|swing
operator|.
name|BindingFactory
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
comment|/**  * A main modeler application class that provides a number of services to the Modeler  * components. Configuration properties:  *<ul>  *<li>cayenne.modeler.application.name - name of the application, 'CayenneModeler' is  * default. Used to locate prerferences domain among other things.</li>  *<li>cayenne.modeler.pref.version - a version of the preferences DB schema. Default is  * "1.1".</li>  *</ul>  *   * @author Andrus Adamchik  */
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
name|HSQLEmbeddedPreferenceService
name|preferenceService
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
comment|// static methods that should probabaly go away eventually...
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
comment|/**      * Returns Application preferences service.      */
specifier|public
name|PreferenceService
name|getPreferenceService
parameter_list|()
block|{
return|return
name|preferenceService
return|;
block|}
comment|/**      * Returns top preferences Domain for the application.      */
specifier|public
name|Domain
name|getPreferenceDomain
parameter_list|()
block|{
return|return
name|getPreferenceService
argument_list|()
operator|.
name|getDomain
argument_list|(
name|getName
argument_list|()
argument_list|,
literal|true
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
name|Domain
name|classLoaderDomain
init|=
name|getPreferenceDomain
argument_list|()
operator|.
name|getSubdomain
argument_list|(
name|FileClassLoadingService
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
name|details
init|=
name|classLoaderDomain
operator|.
name|getPreferences
argument_list|()
decl_stmt|;
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
name|DomainPreference
name|pref
init|=
operator|(
name|DomainPreference
operator|)
name|object
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|pref
operator|.
name|getKey
argument_list|()
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
name|HSQLEmbeddedPreferenceService
name|service
init|=
operator|new
name|HSQLEmbeddedPreferenceService
argument_list|(
name|preferencesDB
argument_list|,
name|PREFERENCES_MAP_PACKAGE
argument_list|,
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|service
operator|.
name|stopOnShutdown
argument_list|()
expr_stmt|;
name|this
operator|.
name|preferenceService
operator|=
name|service
expr_stmt|;
name|this
operator|.
name|preferenceService
operator|.
name|startService
argument_list|()
expr_stmt|;
comment|// test service
name|getPreferenceDomain
argument_list|()
expr_stmt|;
block|}
specifier|static
specifier|final
class|class
name|PreferencesDelegate
implements|implements
name|HSQLEmbeddedPreferenceEditor
operator|.
name|Delegate
block|{
specifier|static
specifier|final
name|String
name|message
init|=
literal|"Preferences Database is locked by another application. "
operator|+
literal|"Do you want to remove the lock?"
decl_stmt|;
specifier|static
specifier|final
name|String
name|failureMessage
init|=
literal|"Failed to remove database lock. "
operator|+
literal|"Preferences will we saved for this session only."
decl_stmt|;
specifier|static
specifier|final
name|HSQLEmbeddedPreferenceEditor
operator|.
name|Delegate
name|sharedInstance
init|=
operator|new
name|PreferencesDelegate
argument_list|()
decl_stmt|;
specifier|public
name|boolean
name|deleteMasterLock
parameter_list|(
name|File
name|lock
parameter_list|)
block|{
name|int
name|result
init|=
name|JOptionPane
operator|.
name|showConfirmDialog
argument_list|(
literal|null
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
name|JOptionPane
operator|.
name|YES_OPTION
operator|||
name|result
operator|==
name|JOptionPane
operator|.
name|OK_OPTION
condition|)
block|{
if|if
condition|(
operator|!
name|lock
operator|.
name|delete
argument_list|()
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
literal|null
argument_list|,
name|failureMessage
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
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
block|}
end_class

end_unit

