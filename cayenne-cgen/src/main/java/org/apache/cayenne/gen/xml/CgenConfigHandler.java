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
name|gen
operator|.
name|xml
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
name|net
operator|.
name|URL
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
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
name|xml
operator|.
name|DataChannelMetaData
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
name|xml
operator|.
name|NamespaceAwareNestedTagHandler
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
name|CgenConfiguration
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
name|CgenConfigList
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
name|CgenTemplate
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
name|map
operator|.
name|DataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|Attributes
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|CgenConfigHandler
extends|extends
name|NamespaceAwareNestedTagHandler
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CONFIG_TAG
init|=
literal|"cgen"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"name"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OUTPUT_DIRECTORY_TAG
init|=
literal|"destDir"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GENERATION_MODE_TAG
init|=
literal|"mode"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SUBCLASS_TEMPLATE_TAG
init|=
literal|"template"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SUPERCLASS_TEMPLATE_TAG
init|=
literal|"superTemplate"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EMBEDDABLE_TEMPLATE_TAG
init|=
literal|"embeddableTemplate"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EMBEDDABLE_SUPER_TEMPLATE_TAG
init|=
literal|"embeddableSuperTemplate"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DATAMAP_TEMPLATE_TAG
init|=
literal|"dataMapTemplate"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DATAMAP_SUPER_TEMPLATE_TAG
init|=
literal|"dataMapSuperTemplate"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OUTPUT_PATTERN_TAG
init|=
literal|"outputPattern"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MAKE_PAIRS_TAG
init|=
literal|"makePairs"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USE_PKG_PATH_TAG
init|=
literal|"usePkgPath"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OVERWRITE_SUBCLASSES_TAG
init|=
literal|"overwrite"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CREATE_PROPERTY_NAMES_TAG
init|=
literal|"createPropertyNames"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXCLUDE_ENTITIES_TAG
init|=
literal|"excludeEntities"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXCLUDE_EMBEDDABLES_TAG
init|=
literal|"excludeEmbeddables"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CREATE_PK_PROPERTIES
init|=
literal|"createPKProperties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SUPER_PKG_TAG
init|=
literal|"superPkg"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TRUE
init|=
literal|"true"
decl_stmt|;
specifier|private
name|DataChannelMetaData
name|metaData
decl_stmt|;
specifier|private
name|CgenConfiguration
name|configuration
decl_stmt|;
name|CgenConfigHandler
parameter_list|(
name|NamespaceAwareNestedTagHandler
name|parentHandler
parameter_list|,
name|DataChannelMetaData
name|metaData
parameter_list|)
block|{
name|super
argument_list|(
name|parentHandler
argument_list|)
expr_stmt|;
name|this
operator|.
name|metaData
operator|=
name|metaData
expr_stmt|;
name|this
operator|.
name|targetNamespace
operator|=
name|CgenExtension
operator|.
name|NAMESPACE
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
operator|new
name|CgenConfiguration
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|processElement
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
block|{
if|if
condition|(
name|CONFIG_TAG
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|createConfig
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processCharData
parameter_list|(
name|String
name|localName
parameter_list|,
name|String
name|data
parameter_list|)
block|{
switch|switch
condition|(
name|localName
condition|)
block|{
case|case
name|NAME
case|:
name|setName
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|OUTPUT_DIRECTORY_TAG
case|:
name|createOutputDir
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|GENERATION_MODE_TAG
case|:
name|createGenerationMode
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|EXCLUDE_ENTITIES_TAG
case|:
name|createExcludeEntities
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|EXCLUDE_EMBEDDABLES_TAG
case|:
name|createExcludeEmbeddables
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|SUBCLASS_TEMPLATE_TAG
case|:
name|createSubclassTemplate
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|SUPERCLASS_TEMPLATE_TAG
case|:
name|createSuperclassTemplate
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|EMBEDDABLE_TEMPLATE_TAG
case|:
name|createEmbeddableTemplate
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|EMBEDDABLE_SUPER_TEMPLATE_TAG
case|:
name|createEmbeddableSuperTemplate
argument_list|(
name|data
argument_list|)
expr_stmt|;
case|case
name|DATAMAP_TEMPLATE_TAG
case|:
name|createDataMapTemplate
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|DATAMAP_SUPER_TEMPLATE_TAG
case|:
name|createDataMapSuperTemplate
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|OUTPUT_PATTERN_TAG
case|:
name|createOutputPattern
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|MAKE_PAIRS_TAG
case|:
name|createMakePairs
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|USE_PKG_PATH_TAG
case|:
name|createUsePkgPath
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|OVERWRITE_SUBCLASSES_TAG
case|:
name|createOverwriteSubclasses
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|CREATE_PROPERTY_NAMES_TAG
case|:
name|createPropertyNamesTag
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|CREATE_PK_PROPERTIES
case|:
name|createPkPropertiesTag
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|SUPER_PKG_TAG
case|:
name|createSuperPkg
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
specifier|private
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|configuration
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createOutputDir
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|configuration
operator|.
name|setRelPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createGenerationMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
if|if
condition|(
name|mode
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|configuration
operator|.
name|setArtifactsGenerationMode
argument_list|(
name|mode
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createExcludeEntities
parameter_list|(
name|String
name|entities
parameter_list|)
block|{
if|if
condition|(
name|entities
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|configuration
operator|.
name|loadEntities
argument_list|(
name|entities
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createExcludeEmbeddables
parameter_list|(
name|String
name|embeddables
parameter_list|)
block|{
if|if
condition|(
name|embeddables
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|configuration
operator|.
name|loadEmbeddables
argument_list|(
name|embeddables
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createSubclassTemplate
parameter_list|(
name|String
name|template
parameter_list|)
block|{
if|if
condition|(
name|template
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|TemplateType
operator|.
name|isDefault
argument_list|(
name|template
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setTemplate
argument_list|(
name|TemplateType
operator|.
name|ENTITY_SUBCLASS
operator|.
name|defaultTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setTemplate
argument_list|(
operator|new
name|CgenTemplate
argument_list|(
name|template
argument_list|,
literal|false
argument_list|,
name|TemplateType
operator|.
name|ENTITY_SUBCLASS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createSuperclassTemplate
parameter_list|(
name|String
name|template
parameter_list|)
block|{
if|if
condition|(
name|template
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|TemplateType
operator|.
name|isDefault
argument_list|(
name|template
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setSuperTemplate
argument_list|(
name|TemplateType
operator|.
name|ENTITY_SUPERCLASS
operator|.
name|defaultTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setSuperTemplate
argument_list|(
operator|new
name|CgenTemplate
argument_list|(
name|template
argument_list|,
literal|false
argument_list|,
name|TemplateType
operator|.
name|ENTITY_SUBCLASS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createEmbeddableTemplate
parameter_list|(
name|String
name|template
parameter_list|)
block|{
if|if
condition|(
name|template
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|TemplateType
operator|.
name|isDefault
argument_list|(
name|template
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setEmbeddableTemplate
argument_list|(
name|TemplateType
operator|.
name|EMBEDDABLE_SUBCLASS
operator|.
name|defaultTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setEmbeddableTemplate
argument_list|(
operator|new
name|CgenTemplate
argument_list|(
name|template
argument_list|,
literal|false
argument_list|,
name|TemplateType
operator|.
name|EMBEDDABLE_SUBCLASS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createEmbeddableSuperTemplate
parameter_list|(
name|String
name|template
parameter_list|)
block|{
if|if
condition|(
name|template
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|TemplateType
operator|.
name|isDefault
argument_list|(
name|template
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setEmbeddableSuperTemplate
argument_list|(
name|TemplateType
operator|.
name|EMBEDDABLE_SUPERCLASS
operator|.
name|defaultTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setEmbeddableSuperTemplate
argument_list|(
operator|new
name|CgenTemplate
argument_list|(
name|template
argument_list|,
literal|false
argument_list|,
name|TemplateType
operator|.
name|EMBEDDABLE_SUPERCLASS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createDataMapTemplate
parameter_list|(
name|String
name|template
parameter_list|)
block|{
if|if
condition|(
name|template
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|TemplateType
operator|.
name|isDefault
argument_list|(
name|template
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setDataMapTemplate
argument_list|(
name|TemplateType
operator|.
name|DATAMAP_SUBCLASS
operator|.
name|defaultTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setDataMapTemplate
argument_list|(
operator|new
name|CgenTemplate
argument_list|(
name|template
argument_list|,
literal|false
argument_list|,
name|TemplateType
operator|.
name|DATAMAP_SUBCLASS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createDataMapSuperTemplate
parameter_list|(
name|String
name|template
parameter_list|)
block|{
if|if
condition|(
name|template
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|TemplateType
operator|.
name|isDefault
argument_list|(
name|template
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setDataMapSuperTemplate
argument_list|(
name|TemplateType
operator|.
name|DATAMAP_SUPERCLASS
operator|.
name|defaultTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setDataMapSuperTemplate
argument_list|(
operator|new
name|CgenTemplate
argument_list|(
name|template
argument_list|,
literal|false
argument_list|,
name|TemplateType
operator|.
name|DATAMAP_SUPERCLASS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createOutputPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|pattern
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|configuration
operator|.
name|setOutputPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createMakePairs
parameter_list|(
name|String
name|makePairs
parameter_list|)
block|{
if|if
condition|(
name|makePairs
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|configuration
operator|.
name|setMakePairs
argument_list|(
name|makePairs
operator|.
name|equals
argument_list|(
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createUsePkgPath
parameter_list|(
name|String
name|data
parameter_list|)
block|{
if|if
condition|(
name|data
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|configuration
operator|.
name|setUsePkgPath
argument_list|(
name|data
operator|.
name|equals
argument_list|(
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createOverwriteSubclasses
parameter_list|(
name|String
name|data
parameter_list|)
block|{
if|if
condition|(
name|data
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|configuration
operator|.
name|setOverwrite
argument_list|(
name|data
operator|.
name|equals
argument_list|(
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createPropertyNamesTag
parameter_list|(
name|String
name|data
parameter_list|)
block|{
if|if
condition|(
name|data
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|configuration
operator|.
name|setCreatePropertyNames
argument_list|(
name|data
operator|.
name|equals
argument_list|(
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createPkPropertiesTag
parameter_list|(
name|String
name|data
parameter_list|)
block|{
if|if
condition|(
name|data
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|configuration
operator|.
name|setCreatePKProperties
argument_list|(
name|data
operator|.
name|equals
argument_list|(
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createSuperPkg
parameter_list|(
name|String
name|data
parameter_list|)
block|{
if|if
condition|(
name|data
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|configuration
operator|.
name|setSuperPkg
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createConfig
parameter_list|()
block|{
name|loaderContext
operator|.
name|addDataMapListener
argument_list|(
name|dataMap
lambda|->
block|{
name|configuration
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRootPath
argument_list|(
name|buildRootPath
argument_list|(
name|dataMap
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|resolveExcludeEntities
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|resolveExcludeEmbeddables
argument_list|()
expr_stmt|;
name|CgenConfigList
name|configurations
init|=
name|CgenConfigHandler
operator|.
name|this
operator|.
name|metaData
operator|.
name|get
argument_list|(
name|dataMap
argument_list|,
name|CgenConfigList
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|configurations
operator|==
literal|null
condition|)
block|{
name|configurations
operator|=
operator|new
name|CgenConfigList
argument_list|()
expr_stmt|;
name|CgenConfigHandler
operator|.
name|this
operator|.
name|metaData
operator|.
name|add
argument_list|(
name|dataMap
argument_list|,
name|configurations
argument_list|)
expr_stmt|;
block|}
name|configurations
operator|.
name|add
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Path
name|buildRootPath
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|URL
name|url
init|=
name|dataMap
operator|.
name|getConfigurationSource
argument_list|()
operator|.
name|getURL
argument_list|()
decl_stmt|;
name|Path
name|resourcePath
decl_stmt|;
try|try
block|{
name|resourcePath
operator|=
name|Paths
operator|.
name|get
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unable to read cgen path"
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|Files
operator|.
name|isRegularFile
argument_list|(
name|resourcePath
argument_list|)
condition|)
block|{
name|resourcePath
operator|=
name|resourcePath
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
return|return
name|resourcePath
return|;
block|}
block|}
end_class

end_unit

