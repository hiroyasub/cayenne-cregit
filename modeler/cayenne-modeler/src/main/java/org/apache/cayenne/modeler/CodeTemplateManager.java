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
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|cayenne
operator|.
name|resource
operator|.
name|Resource
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
specifier|public
specifier|static
specifier|final
name|String
name|SINGLE_SERVER_CLASS
init|=
literal|"Single Server Class"
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
specifier|private
specifier|static
specifier|final
name|String
name|STANDARD_EMBEDDABLE_SUPERCLASS
init|=
literal|"Standard Embeddable Superclass"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STANDARD_EMBEDDABLE_SUBCLASS
init|=
literal|"Standard Embeddable Subclass"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SINGLE_EMBEDDABLE_CLASS
init|=
literal|"Single Embeddable Class"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STANDARD_SERVER_DATAMAP_SUPERCLASS
init|=
literal|"Standard Server DataMap Superclass"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STANDARD_SERVER_DATAMAP_SUBCLASS
init|=
literal|"Standard Server DataMap Subclass"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STANDARD_CLIENT_DATAMAP_SUPERCLASS
init|=
literal|"Standard Client DataMap Superclass"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STANDARD_CLIENT_DATAMAP_SUBCLASS
init|=
literal|"Standard Client DataMap Subclass"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SINGLE_DATAMAP_CLASS
init|=
literal|"Single DataMap Class"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NODE_NAME
init|=
literal|"codeTemplateManager"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|standardSubclassTemplates
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|standardSuperclassTemplates
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|standardClientSubclassTemplates
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|standardClientSuperclassTemplates
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|customTemplates
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|reverseCustomTemplate
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|standardTemplates
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|standardEmbeddableTemplates
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|standardEmbeddableSuperclassTemplates
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|standardServerDataMapTemplates
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|standardServerDataMapSuperclassTemplates
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|standardClientDataMapTemplates
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|standardClientDataMapSuperclassTemplates
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|reverseStandartTemplates
decl_stmt|;
specifier|private
specifier|static
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
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
argument_list|<>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|standardSuperclassTemplates
operator|.
name|add
argument_list|(
name|STANDARD_SERVER_SUPERCLASS
argument_list|)
expr_stmt|;
name|standardClientSuperclassTemplates
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|standardClientSuperclassTemplates
operator|.
name|add
argument_list|(
name|STANDARD_CLIENT_SUPERCLASS
argument_list|)
expr_stmt|;
name|standardSubclassTemplates
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|standardSubclassTemplates
operator|.
name|add
argument_list|(
name|SINGLE_SERVER_CLASS
argument_list|)
expr_stmt|;
name|standardSubclassTemplates
operator|.
name|add
argument_list|(
name|STANDARD_SERVER_SUBCLASS
argument_list|)
expr_stmt|;
name|standardClientSubclassTemplates
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|standardClientSubclassTemplates
operator|.
name|add
argument_list|(
name|STANDARD_CLIENT_SUBCLASS
argument_list|)
expr_stmt|;
name|standardEmbeddableTemplates
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|standardEmbeddableTemplates
operator|.
name|add
argument_list|(
name|STANDARD_EMBEDDABLE_SUBCLASS
argument_list|)
expr_stmt|;
name|standardEmbeddableTemplates
operator|.
name|add
argument_list|(
name|SINGLE_EMBEDDABLE_CLASS
argument_list|)
expr_stmt|;
name|standardEmbeddableSuperclassTemplates
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|standardEmbeddableSuperclassTemplates
operator|.
name|add
argument_list|(
name|STANDARD_EMBEDDABLE_SUPERCLASS
argument_list|)
expr_stmt|;
name|standardServerDataMapTemplates
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|standardServerDataMapTemplates
operator|.
name|add
argument_list|(
name|STANDARD_SERVER_DATAMAP_SUBCLASS
argument_list|)
expr_stmt|;
name|standardServerDataMapTemplates
operator|.
name|add
argument_list|(
name|SINGLE_DATAMAP_CLASS
argument_list|)
expr_stmt|;
name|standardServerDataMapSuperclassTemplates
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|standardServerDataMapSuperclassTemplates
operator|.
name|add
argument_list|(
name|STANDARD_SERVER_DATAMAP_SUPERCLASS
argument_list|)
expr_stmt|;
name|standardClientDataMapTemplates
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|standardClientDataMapTemplates
operator|.
name|add
argument_list|(
name|STANDARD_CLIENT_DATAMAP_SUBCLASS
argument_list|)
expr_stmt|;
name|standardClientDataMapSuperclassTemplates
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|standardClientDataMapSuperclassTemplates
operator|.
name|add
argument_list|(
name|STANDARD_CLIENT_DATAMAP_SUPERCLASS
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
name|reverseCustomTemplate
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|customTemplates
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|reverseCustomTemplate
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|standardTemplates
operator|=
operator|new
name|HashMap
argument_list|<>
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
name|SINGLE_SERVER_CLASS
argument_list|,
name|ClassGenerationAction
operator|.
name|SINGLE_CLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|standardTemplates
operator|.
name|put
argument_list|(
name|STANDARD_EMBEDDABLE_SUPERCLASS
argument_list|,
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SUPERCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|standardTemplates
operator|.
name|put
argument_list|(
name|STANDARD_EMBEDDABLE_SUBCLASS
argument_list|,
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SUBCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|standardTemplates
operator|.
name|put
argument_list|(
name|SINGLE_EMBEDDABLE_CLASS
argument_list|,
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SINGLE_CLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|standardTemplates
operator|.
name|put
argument_list|(
name|STANDARD_SERVER_DATAMAP_SUBCLASS
argument_list|,
name|ClassGenerationAction
operator|.
name|DATAMAP_SUBCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|standardTemplates
operator|.
name|put
argument_list|(
name|STANDARD_CLIENT_DATAMAP_SUBCLASS
argument_list|,
name|ClientClassGenerationAction
operator|.
name|DMAP_SUBCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|standardTemplates
operator|.
name|put
argument_list|(
name|SINGLE_DATAMAP_CLASS
argument_list|,
name|ClassGenerationAction
operator|.
name|DATAMAP_SINGLE_CLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|standardTemplates
operator|.
name|put
argument_list|(
name|STANDARD_SERVER_DATAMAP_SUPERCLASS
argument_list|,
name|ClassGenerationAction
operator|.
name|DATAMAP_SUPERCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|standardTemplates
operator|.
name|put
argument_list|(
name|STANDARD_CLIENT_DATAMAP_SUPERCLASS
argument_list|,
name|ClientClassGenerationAction
operator|.
name|DMAP_SUPERCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|reverseStandartTemplates
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|reverseStandartTemplates
operator|.
name|put
argument_list|(
name|ClassGenerationAction
operator|.
name|SUBCLASS_TEMPLATE
argument_list|,
name|STANDARD_SERVER_SUBCLASS
argument_list|)
expr_stmt|;
name|reverseStandartTemplates
operator|.
name|put
argument_list|(
name|ClientClassGenerationAction
operator|.
name|SUBCLASS_TEMPLATE
argument_list|,
name|STANDARD_CLIENT_SUBCLASS
argument_list|)
expr_stmt|;
name|reverseStandartTemplates
operator|.
name|put
argument_list|(
name|ClassGenerationAction
operator|.
name|SINGLE_CLASS_TEMPLATE
argument_list|,
name|SINGLE_SERVER_CLASS
argument_list|)
expr_stmt|;
name|reverseStandartTemplates
operator|.
name|put
argument_list|(
name|ClientClassGenerationAction
operator|.
name|SUPERCLASS_TEMPLATE
argument_list|,
name|STANDARD_CLIENT_SUPERCLASS
argument_list|)
expr_stmt|;
name|reverseStandartTemplates
operator|.
name|put
argument_list|(
name|ClassGenerationAction
operator|.
name|SUPERCLASS_TEMPLATE
argument_list|,
name|STANDARD_SERVER_SUPERCLASS
argument_list|)
expr_stmt|;
name|reverseStandartTemplates
operator|.
name|put
argument_list|(
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SUPERCLASS_TEMPLATE
argument_list|,
name|STANDARD_EMBEDDABLE_SUPERCLASS
argument_list|)
expr_stmt|;
name|reverseStandartTemplates
operator|.
name|put
argument_list|(
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SUBCLASS_TEMPLATE
argument_list|,
name|STANDARD_EMBEDDABLE_SUBCLASS
argument_list|)
expr_stmt|;
name|reverseStandartTemplates
operator|.
name|put
argument_list|(
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SINGLE_CLASS_TEMPLATE
argument_list|,
name|SINGLE_EMBEDDABLE_CLASS
argument_list|)
expr_stmt|;
name|reverseStandartTemplates
operator|.
name|put
argument_list|(
name|ClassGenerationAction
operator|.
name|DATAMAP_SUBCLASS_TEMPLATE
argument_list|,
name|STANDARD_SERVER_DATAMAP_SUBCLASS
argument_list|)
expr_stmt|;
name|reverseStandartTemplates
operator|.
name|put
argument_list|(
name|ClientClassGenerationAction
operator|.
name|DMAP_SUBCLASS_TEMPLATE
argument_list|,
name|STANDARD_CLIENT_DATAMAP_SUBCLASS
argument_list|)
expr_stmt|;
name|reverseStandartTemplates
operator|.
name|put
argument_list|(
name|ClassGenerationAction
operator|.
name|DATAMAP_SINGLE_CLASS_TEMPLATE
argument_list|,
name|SINGLE_DATAMAP_CLASS
argument_list|)
expr_stmt|;
name|reverseStandartTemplates
operator|.
name|put
argument_list|(
name|ClassGenerationAction
operator|.
name|DATAMAP_SUPERCLASS_TEMPLATE
argument_list|,
name|STANDARD_SERVER_DATAMAP_SUPERCLASS
argument_list|)
expr_stmt|;
name|reverseStandartTemplates
operator|.
name|put
argument_list|(
name|ClientClassGenerationAction
operator|.
name|DMAP_SUPERCLASS_TEMPLATE
argument_list|,
name|STANDARD_CLIENT_DATAMAP_SUPERCLASS
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Updates custom templates from preferences. 	 */
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
block|{}
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
argument_list|<>
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
name|String
name|key
range|:
name|keys
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
name|key
argument_list|)
argument_list|)
decl_stmt|;
name|customTemplates
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|path
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// TODO: andrus, 12/5/2007 - this should also take a "pairs" parameter to
comment|// correctly
comment|// assign standard templates
specifier|public
name|String
name|getTemplatePath
parameter_list|(
name|String
name|name
parameter_list|,
name|Resource
name|rootPath
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
try|try
block|{
if|if
condition|(
name|rootPath
operator|!=
literal|null
condition|)
block|{
name|Path
name|path
init|=
name|Paths
operator|.
name|get
argument_list|(
name|rootPath
operator|.
name|getURL
argument_list|()
operator|.
name|toURI
argument_list|()
argument_list|)
operator|.
name|getParent
argument_list|()
decl_stmt|;
name|value
operator|=
name|path
operator|.
name|relativize
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Path for template named '{}' could not be resolved"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
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
name|String
name|getNameByPath
parameter_list|(
name|String
name|name
parameter_list|,
name|Path
name|rootPath
parameter_list|)
block|{
name|String
name|fullPath
init|=
name|rootPath
operator|.
name|resolve
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
operator|.
name|normalize
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverseCustomTemplate
operator|.
name|containsKey
argument_list|(
name|fullPath
argument_list|)
condition|)
block|{
return|return
name|reverseCustomTemplate
operator|.
name|get
argument_list|(
name|fullPath
argument_list|)
return|;
block|}
else|else
block|{
name|Object
name|value
init|=
name|reverseStandartTemplates
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
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
name|getStandardClientSubclassTemplates
parameter_list|()
block|{
return|return
name|standardClientSubclassTemplates
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
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getStandardClientSuperclassTemplates
parameter_list|()
block|{
return|return
name|standardClientSuperclassTemplates
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getStandartEmbeddableTemplates
parameter_list|()
block|{
return|return
name|standardEmbeddableTemplates
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getStandartEmbeddableSuperclassTemplates
parameter_list|()
block|{
return|return
name|standardEmbeddableSuperclassTemplates
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getStandartDataMapTemplates
parameter_list|()
block|{
return|return
name|standardServerDataMapTemplates
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getStandartDataMapSuperclassTemplates
parameter_list|()
block|{
return|return
name|standardServerDataMapSuperclassTemplates
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getStandardClientDataMapTemplates
parameter_list|()
block|{
return|return
name|standardClientDataMapTemplates
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getStandardClientDataMapSuperclassTemplates
parameter_list|()
block|{
return|return
name|standardClientDataMapSuperclassTemplates
return|;
block|}
block|}
end_class

end_unit

