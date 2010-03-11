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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|gen
operator|.
name|ClassGenerationAction
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
name|gen
operator|.
name|ClientClassGenerationAction
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
comment|/**  * Manages code generation templates.  */
end_comment

begin_class
specifier|public
class|class
name|CodeTemplateManager
block|{
specifier|public
specifier|static
specifier|final
name|String
name|STANDARD_SERVER_SUPERCLASS
init|=
literal|"Standard Server Superclass"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STANDARD_SERVER_SUBCLASS
init|=
literal|"Standard Server Subclass"
decl_stmt|;
specifier|static
specifier|final
name|String
name|STANDARD_CLIENT_SUPERCLASS
init|=
literal|"Standard Client Superclass"
decl_stmt|;
specifier|static
specifier|final
name|String
name|STANDARD_CLIENT_SUBCLASS
init|=
literal|"Standard Client Subclass"
decl_stmt|;
specifier|static
specifier|final
name|String
name|LIGHT_SERVER_SUPERCLASS
init|=
literal|"Light Server Superclass"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NODE_NAME
init|=
literal|"codeTemplateManager"
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|standardSubclassTemplates
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|standardSuperclassTemplates
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|customTemplates
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|standardTemplates
decl_stmt|;
specifier|private
specifier|static
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CodeTemplateManager
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|Preferences
name|getTemplatePreferences
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
return|return
name|application
operator|.
name|getPreferencesNode
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
name|NODE_NAME
argument_list|)
return|;
block|}
specifier|public
name|CodeTemplateManager
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|standardSuperclassTemplates
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|standardSuperclassTemplates
operator|.
name|add
argument_list|(
name|STANDARD_SERVER_SUPERCLASS
argument_list|)
expr_stmt|;
name|standardSuperclassTemplates
operator|.
name|add
argument_list|(
name|STANDARD_CLIENT_SUPERCLASS
argument_list|)
expr_stmt|;
name|standardSuperclassTemplates
operator|.
name|add
argument_list|(
name|LIGHT_SERVER_SUPERCLASS
argument_list|)
expr_stmt|;
name|standardSubclassTemplates
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|standardSubclassTemplates
operator|.
name|add
argument_list|(
name|STANDARD_SERVER_SUBCLASS
argument_list|)
expr_stmt|;
name|standardSubclassTemplates
operator|.
name|add
argument_list|(
name|STANDARD_CLIENT_SUBCLASS
argument_list|)
expr_stmt|;
name|updateCustomTemplates
argument_list|(
name|getTemplatePreferences
argument_list|(
name|application
argument_list|)
argument_list|)
expr_stmt|;
name|standardTemplates
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|standardTemplates
operator|.
name|put
argument_list|(
name|STANDARD_SERVER_SUPERCLASS
argument_list|,
name|ClassGenerationAction
operator|.
name|SUPERCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|standardTemplates
operator|.
name|put
argument_list|(
name|STANDARD_CLIENT_SUPERCLASS
argument_list|,
name|ClientClassGenerationAction
operator|.
name|SUPERCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|standardTemplates
operator|.
name|put
argument_list|(
name|STANDARD_SERVER_SUBCLASS
argument_list|,
name|ClassGenerationAction
operator|.
name|SUBCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|standardTemplates
operator|.
name|put
argument_list|(
name|STANDARD_CLIENT_SUBCLASS
argument_list|,
name|ClientClassGenerationAction
operator|.
name|SUBCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|standardTemplates
operator|.
name|put
argument_list|(
name|LIGHT_SERVER_SUPERCLASS
argument_list|,
name|ClientClassGenerationAction
operator|.
name|LIGHT_SUPERCLASS_TEMPLATE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Updates custom templates from preferences.      */
specifier|public
name|void
name|updateCustomTemplates
parameter_list|(
name|Preferences
name|preference
parameter_list|)
block|{
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
name|preference
operator|.
name|childrenNames
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
name|warn
argument_list|(
literal|"Error reading preferences"
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|customTemplates
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|keys
operator|.
name|length
argument_list|,
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|keys
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|FSPath
name|path
init|=
operator|new
name|FSPath
argument_list|(
name|preference
operator|.
name|node
argument_list|(
name|keys
index|[
name|j
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|customTemplates
operator|.
name|put
argument_list|(
name|keys
index|[
name|j
index|]
argument_list|,
name|path
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// TODO: andrus, 12/5/2007 - this should also take a "pairs" parameter to correctly
comment|// assign standard templates
specifier|public
name|String
name|getTemplatePath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Object
name|value
init|=
name|customTemplates
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
name|value
operator|=
name|standardTemplates
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getCustomTemplates
parameter_list|()
block|{
return|return
name|customTemplates
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getStandardSubclassTemplates
parameter_list|()
block|{
return|return
name|standardSubclassTemplates
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getStandardSuperclassTemplates
parameter_list|()
block|{
return|return
name|standardSuperclassTemplates
return|;
block|}
block|}
end_class

end_unit

