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
name|configuration
operator|.
name|server
operator|.
name|ServerModule
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
name|dbsync
operator|.
name|DbSyncModule
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
name|action
operator|.
name|OpenProjectAction
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
name|init
operator|.
name|CayenneModelerModule
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
name|init
operator|.
name|platform
operator|.
name|PlatformInitializer
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
name|ProjectModule
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
name|SwingUtilities
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
name|Preferences
import|;
end_import

begin_comment
comment|/**  * Main class responsible for starting CayenneModeler.  */
end_comment

begin_class
specifier|public
class|class
name|Main
block|{
specifier|private
specifier|static
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Main
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
index|[]
name|args
decl_stmt|;
comment|/**      * Main method that starts the CayenneModeler.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
operator|new
name|Main
argument_list|(
name|args
argument_list|)
operator|.
name|launch
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
block|}
specifier|protected
name|void
name|launch
parameter_list|()
block|{
comment|// TODO: use module auto-loading...
specifier|final
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|appendModules
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Module
argument_list|>
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// init look and feel before using any Swing classes...
name|injector
operator|.
name|getInstance
argument_list|(
name|PlatformInitializer
operator|.
name|class
argument_list|)
operator|.
name|initLookAndFeel
argument_list|()
expr_stmt|;
comment|// logger should go after Look And Feel or Logger Console will be without style
name|logger
operator|.
name|info
argument_list|(
literal|"Starting CayenneModeler."
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"JRE v."
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.version"
argument_list|)
operator|+
literal|" at "
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.home"
argument_list|)
argument_list|)
expr_stmt|;
name|SwingUtilities
operator|.
name|invokeLater
argument_list|(
parameter_list|()
lambda|->
block|{
name|Application
name|application
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|Application
operator|.
name|class
argument_list|)
decl_stmt|;
name|Application
operator|.
name|setInstance
argument_list|(
name|application
argument_list|)
expr_stmt|;
name|application
operator|.
name|startup
argument_list|()
expr_stmt|;
comment|// start initial project AFTER the app startup, as we need Application
comment|// preferences to be bootstrapped.
name|File
name|project
init|=
name|initialProjectFromArgs
argument_list|()
decl_stmt|;
if|if
condition|(
name|project
operator|==
literal|null
condition|)
block|{
name|project
operator|=
name|initialProjectFromPreferences
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|project
operator|!=
literal|null
condition|)
block|{
operator|new
name|OpenProjectAction
argument_list|(
name|application
argument_list|)
operator|.
name|openProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Collection
argument_list|<
name|Module
argument_list|>
name|appendModules
parameter_list|(
name|Collection
argument_list|<
name|Module
argument_list|>
name|modules
parameter_list|)
block|{
comment|// TODO: this is dirty... ServerModule is out of place inside the Modeler...
comment|// If we need ServerRuntime for certain operations, those should start their own stack...
name|modules
operator|.
name|add
argument_list|(
operator|new
name|ServerModule
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|ProjectModule
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|DbSyncModule
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|CayenneModelerModule
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|modules
return|;
block|}
specifier|protected
name|File
name|initialProjectFromPreferences
parameter_list|()
block|{
name|Preferences
name|autoLoadLastProject
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
operator|(
name|autoLoadLastProject
operator|!=
literal|null
operator|)
operator|&&
name|autoLoadLastProject
operator|.
name|getBoolean
argument_list|(
name|GeneralPreferences
operator|.
name|AUTO_LOAD_PROJECT_PREFERENCE
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|File
argument_list|>
name|lastFiles
init|=
name|ModelerPreferences
operator|.
name|getLastProjFiles
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|lastFiles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|lastFiles
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|File
name|initialProjectFromArgs
parameter_list|()
block|{
if|if
condition|(
name|args
operator|!=
literal|null
operator|&&
name|args
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|isFile
argument_list|()
operator|&&
name|f
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"cayenne"
argument_list|)
operator|&&
name|f
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
condition|)
block|{
return|return
name|f
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

