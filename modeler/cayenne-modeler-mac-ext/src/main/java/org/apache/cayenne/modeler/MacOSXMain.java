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
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * Main class to start CayenneModeler on MacOSX.  *   * @since 1.2  * @author Michael Gentry  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|MacOSXMain
extends|extends
name|Main
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_LAF_OSX_NAME
init|=
literal|"apple.laf.AquaLookAndFeel"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_THEME_OSX_NAME
init|=
literal|"Aqua"
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
name|MacOSXMain
name|main
init|=
operator|new
name|MacOSXMain
argument_list|()
decl_stmt|;
comment|// if configured, redirect all logging to the log file
name|main
operator|.
name|configureLogging
argument_list|()
expr_stmt|;
comment|// check jdk version
if|if
condition|(
operator|!
name|main
operator|.
name|checkJDKVersion
argument_list|()
condition|)
block|{
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|File
name|projectFile
init|=
name|Main
operator|.
name|projectFileFromArgs
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|main
operator|.
name|runModeler
argument_list|(
name|projectFile
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|boolean
name|isMacOSX
parameter_list|()
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"mac"
argument_list|)
operator|>=
literal|0
return|;
block|}
specifier|protected
name|void
name|runModeler
parameter_list|(
name|File
name|projectFile
parameter_list|)
block|{
name|configureMacOSX
argument_list|()
expr_stmt|;
name|super
operator|.
name|runModeler
argument_list|(
name|projectFile
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|configureMacOSX
parameter_list|()
block|{
try|try
block|{
name|MacOSXSetup
operator|.
name|configureMacOSX
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore... not a mac
block|}
block|}
specifier|protected
name|String
name|getLookAndFeelName
parameter_list|()
block|{
if|if
condition|(
name|isMacOSX
argument_list|()
condition|)
block|{
name|ModelerPreferences
name|prefs
init|=
name|ModelerPreferences
operator|.
name|getPreferences
argument_list|()
decl_stmt|;
return|return
name|prefs
operator|.
name|getString
argument_list|(
name|ModelerPreferences
operator|.
name|EDITOR_LAFNAME
argument_list|,
name|MacOSXMain
operator|.
name|DEFAULT_LAF_OSX_NAME
argument_list|)
return|;
block|}
else|else
return|return
name|super
operator|.
name|getLookAndFeelName
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getThemeName
parameter_list|()
block|{
if|if
condition|(
name|isMacOSX
argument_list|()
condition|)
block|{
name|ModelerPreferences
name|prefs
init|=
name|ModelerPreferences
operator|.
name|getPreferences
argument_list|()
decl_stmt|;
return|return
name|prefs
operator|.
name|getString
argument_list|(
name|ModelerPreferences
operator|.
name|EDITOR_THEMENAME
argument_list|,
name|MacOSXMain
operator|.
name|DEFAULT_THEME_OSX_NAME
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getThemeName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

