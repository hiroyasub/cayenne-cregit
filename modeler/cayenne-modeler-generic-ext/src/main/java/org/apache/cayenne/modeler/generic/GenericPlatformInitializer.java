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
name|generic
package|;
end_package

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
name|UIManager
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
name|com
operator|.
name|jgoodies
operator|.
name|looks
operator|.
name|plastic
operator|.
name|PlasticLookAndFeel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|looks
operator|.
name|plastic
operator|.
name|PlasticTheme
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|looks
operator|.
name|plastic
operator|.
name|PlasticXPLookAndFeel
import|;
end_import

begin_class
specifier|public
class|class
name|GenericPlatformInitializer
implements|implements
name|PlatformInitializer
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
name|GenericPlatformInitializer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|DEFAULT_LAF_NAME
init|=
name|PlasticXPLookAndFeel
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// note that another theme - "Desert Blue" doesn't support Chinese and
comment|// Japanese chars
specifier|static
specifier|final
name|String
name|DEFAULT_THEME_NAME
init|=
literal|"Sky Bluer"
decl_stmt|;
specifier|public
name|void
name|setupMenus
parameter_list|(
name|JFrame
name|frame
parameter_list|)
block|{
comment|// noop - default menus are fine
block|}
specifier|public
name|void
name|initLookAndFeel
parameter_list|()
block|{
name|PlasticTheme
name|theme
init|=
name|findTheme
argument_list|()
decl_stmt|;
if|if
condition|(
name|theme
operator|!=
literal|null
condition|)
block|{
name|PlasticLookAndFeel
operator|.
name|setCurrentTheme
argument_list|(
name|theme
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|UIManager
operator|.
name|setLookAndFeel
argument_list|(
name|DEFAULT_LAF_NAME
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Error installing L&F: "
operator|+
name|DEFAULT_LAF_NAME
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|PlasticTheme
name|findTheme
parameter_list|()
block|{
for|for
control|(
name|Object
name|object
range|:
name|PlasticLookAndFeel
operator|.
name|getInstalledThemes
argument_list|()
control|)
block|{
name|PlasticTheme
name|theme
init|=
operator|(
name|PlasticTheme
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|DEFAULT_THEME_NAME
operator|.
name|equals
argument_list|(
name|theme
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|theme
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

