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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|HashMap
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
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|map
operator|.
name|Embeddable
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
name|ObjEntity
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
name|QueryDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|Template
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|VelocityContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|app
operator|.
name|VelocityEngine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|context
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|tools
operator|.
name|ToolManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|tools
operator|.
name|config
operator|.
name|ConfigurationUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|tools
operator|.
name|config
operator|.
name|FactoryConfiguration
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

begin_class
specifier|public
class|class
name|ClassGenerationAction
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SUPERCLASS_PREFIX
init|=
literal|"_"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WILDCARD
init|=
literal|"*"
decl_stmt|;
comment|/** 	 * @since 4.1 	 */
specifier|protected
name|CgenConfiguration
name|cgenConfiguration
decl_stmt|;
specifier|protected
name|Logger
name|logger
decl_stmt|;
comment|// runtime ivars
specifier|protected
name|Context
name|context
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Template
argument_list|>
name|templateCache
decl_stmt|;
specifier|private
name|ToolsUtilsFactory
name|utilsFactory
decl_stmt|;
specifier|private
name|MetadataUtils
name|metadataUtils
decl_stmt|;
comment|/** 	Optionally allows user-defined tools besides {@link ImportUtils} for working with velocity templates.<br/> 	To use this feature, either set the java system property {@code -Dorg.apache.velocity.tools=tools.properties} 	or set the {@code externalToolConfig} property to "tools.properties" in {@code CgenConfiguration}. Then  	create the file "tools.properties" in the working directory or in the root of the classpath with content  	like this:<pre> 	tools.toolbox = application 	tools.application.myTool = com.mycompany.MyTool</pre> 	Then the methods in the MyTool class will be available for use in the template like ${myTool.myMethod(arg)} 	 */
specifier|public
name|ClassGenerationAction
parameter_list|(
name|CgenConfiguration
name|cgenConfig
parameter_list|)
block|{
name|this
operator|.
name|cgenConfiguration
operator|=
name|cgenConfig
expr_stmt|;
name|String
name|toolConfigFile
init|=
name|cgenConfig
operator|.
name|getExternalToolConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.velocity.tools"
argument_list|)
operator|!=
literal|null
operator|||
name|toolConfigFile
operator|!=
literal|null
condition|)
block|{
name|ToolManager
name|manager
init|=
operator|new
name|ToolManager
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|toolConfigFile
operator|!=
literal|null
condition|)
block|{
name|FactoryConfiguration
name|config
init|=
name|ConfigurationUtils
operator|.
name|find
argument_list|(
name|toolConfigFile
argument_list|)
decl_stmt|;
name|manager
operator|.
name|getToolboxFactory
argument_list|()
operator|.
name|configure
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|context
operator|=
name|manager
operator|.
name|createContext
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|context
operator|=
operator|new
name|VelocityContext
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|templateCache
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|customTemplateName
parameter_list|(
name|TemplateType
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|ENTITY_SINGLE_CLASS
case|:
case|case
name|ENTITY_SUBCLASS
case|:
return|return
name|cgenConfiguration
operator|.
name|getTemplate
argument_list|()
return|;
case|case
name|ENTITY_SUPERCLASS
case|:
return|return
name|cgenConfiguration
operator|.
name|getSuperTemplate
argument_list|()
return|;
case|case
name|EMBEDDABLE_SINGLE_CLASS
case|:
case|case
name|EMBEDDABLE_SUBCLASS
case|:
return|return
name|cgenConfiguration
operator|.
name|getEmbeddableTemplate
argument_list|()
return|;
case|case
name|EMBEDDABLE_SUPERCLASS
case|:
return|return
name|cgenConfiguration
operator|.
name|getEmbeddableSuperTemplate
argument_list|()
return|;
case|case
name|DATAMAP_SINGLE_CLASS
case|:
case|case
name|DATAMAP_SUBCLASS
case|:
return|return
name|cgenConfiguration
operator|.
name|getDataMapTemplate
argument_list|()
return|;
case|case
name|DATAMAP_SUPERCLASS
case|:
return|return
name|cgenConfiguration
operator|.
name|getDataMapSuperTemplate
argument_list|()
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid template type: "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
comment|/**      * VelocityContext initialization method called once per artifact.      */
specifier|public
name|void
name|resetContextForArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|StringUtils
name|stringUtils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|qualifiedClassName
init|=
name|artifact
operator|.
name|getQualifiedClassName
argument_list|()
decl_stmt|;
name|String
name|packageName
init|=
name|stringUtils
operator|.
name|stripClass
argument_list|(
name|qualifiedClassName
argument_list|)
decl_stmt|;
name|String
name|className
init|=
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|qualifiedClassName
argument_list|)
decl_stmt|;
name|String
name|qualifiedBaseClassName
init|=
name|artifact
operator|.
name|getQualifiedBaseClassName
argument_list|()
decl_stmt|;
name|String
name|basePackageName
init|=
name|stringUtils
operator|.
name|stripClass
argument_list|(
name|qualifiedBaseClassName
argument_list|)
decl_stmt|;
name|String
name|baseClassName
init|=
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|qualifiedBaseClassName
argument_list|)
decl_stmt|;
name|String
name|superClassName
init|=
name|SUPERCLASS_PREFIX
operator|+
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|qualifiedClassName
argument_list|)
decl_stmt|;
name|String
name|superPackageName
init|=
name|cgenConfiguration
operator|.
name|getSuperPkg
argument_list|()
decl_stmt|;
if|if
condition|(
name|superPackageName
operator|==
literal|null
operator|||
name|superPackageName
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|superPackageName
operator|=
name|packageName
operator|+
literal|".auto"
expr_stmt|;
block|}
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|BASE_CLASS_KEY
argument_list|,
name|baseClassName
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|BASE_PACKAGE_KEY
argument_list|,
name|basePackageName
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|SUB_CLASS_KEY
argument_list|,
name|className
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|SUB_PACKAGE_KEY
argument_list|,
name|packageName
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|SUPER_CLASS_KEY
argument_list|,
name|superClassName
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|SUPER_PACKAGE_KEY
argument_list|,
name|superPackageName
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|OBJECT_KEY
argument_list|,
name|artifact
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|STRING_UTILS_KEY
argument_list|,
name|stringUtils
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|CREATE_PROPERTY_NAMES
argument_list|,
name|cgenConfiguration
operator|.
name|isCreatePropertyNames
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|CREATE_PK_PROPERTIES
argument_list|,
name|cgenConfiguration
operator|.
name|isCreatePKProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * VelocityContext initialization method called once per each artifact and 	 * template type combination. 	 */
name|void
name|resetContextForArtifactTemplate
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|ImportUtils
name|importUtils
init|=
name|utilsFactory
operator|.
name|createImportUtils
argument_list|()
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|IMPORT_UTILS_KEY
argument_list|,
name|importUtils
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|PROPERTY_UTILS_KEY
argument_list|,
name|utilsFactory
operator|.
name|createPropertyUtils
argument_list|(
name|logger
argument_list|,
name|importUtils
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Artifact
operator|.
name|METADATA_UTILS_KEY
argument_list|,
name|metadataUtils
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|postInitContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Adds entities to the internal entity list. 	 * @param entities collection 	 * 	 * @since 4.0 throws exception 	 */
specifier|public
name|void
name|addEntities
parameter_list|(
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|entities
parameter_list|)
block|{
if|if
condition|(
name|entities
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ObjEntity
name|entity
range|:
name|entities
control|)
block|{
name|cgenConfiguration
operator|.
name|addArtifact
argument_list|(
operator|new
name|EntityArtifact
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|addEmbeddables
parameter_list|(
name|Collection
argument_list|<
name|Embeddable
argument_list|>
name|embeddables
parameter_list|)
block|{
if|if
condition|(
name|embeddables
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Embeddable
name|embeddable
range|:
name|embeddables
control|)
block|{
name|cgenConfiguration
operator|.
name|addArtifact
argument_list|(
operator|new
name|EmbeddableArtifact
argument_list|(
name|embeddable
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|addQueries
parameter_list|(
name|Collection
argument_list|<
name|QueryDescriptor
argument_list|>
name|queries
parameter_list|)
block|{
if|if
condition|(
name|cgenConfiguration
operator|.
name|getArtifactsGenerationMode
argument_list|()
operator|.
name|equals
argument_list|(
name|ArtifactsGenerationMode
operator|.
name|ALL
operator|.
name|getLabel
argument_list|()
argument_list|)
condition|)
block|{
comment|// TODO: andrus 10.12.2010 - why not also check for empty query list??
comment|// Or create a better API for enabling DataMapArtifact
if|if
condition|(
name|queries
operator|!=
literal|null
condition|)
block|{
name|Artifact
name|artifact
init|=
operator|new
name|DataMapArtifact
argument_list|(
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|queries
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cgenConfiguration
operator|.
name|getArtifacts
argument_list|()
operator|.
name|contains
argument_list|(
name|artifact
argument_list|)
condition|)
block|{
name|cgenConfiguration
operator|.
name|addArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/** 	 * @since 4.1 	 */
specifier|public
name|void
name|prepareArtifacts
parameter_list|()
block|{
name|cgenConfiguration
operator|.
name|getArtifacts
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|addEntities
argument_list|(
name|cgenConfiguration
operator|.
name|getEntities
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|entity
lambda|->
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|entity
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|addEmbeddables
argument_list|(
name|cgenConfiguration
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|embeddable
lambda|->
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
operator|.
name|getEmbeddable
argument_list|(
name|embeddable
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|addQueries
argument_list|(
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
operator|.
name|getQueryDescriptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Executes class generation once per each artifact. 	 */
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|validateAttributes
argument_list|()
expr_stmt|;
try|try
block|{
for|for
control|(
name|Artifact
name|artifact
range|:
name|cgenConfiguration
operator|.
name|getArtifacts
argument_list|()
control|)
block|{
name|execute
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// must reset engine at the end of class generator run to avoid
comment|// memory
comment|// leaks and stale templates
name|templateCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
comment|/** 	 * Executes class generation for a single artifact. 	 */
specifier|protected
name|void
name|execute
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|Exception
block|{
name|resetContextForArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|ArtifactGenerationMode
name|artifactMode
init|=
name|cgenConfiguration
operator|.
name|isMakePairs
argument_list|()
condition|?
name|ArtifactGenerationMode
operator|.
name|GENERATION_GAP
else|:
name|ArtifactGenerationMode
operator|.
name|SINGLE_CLASS
decl_stmt|;
name|TemplateType
index|[]
name|templateTypes
init|=
name|artifact
operator|.
name|getTemplateTypes
argument_list|(
name|artifactMode
argument_list|)
decl_stmt|;
for|for
control|(
name|TemplateType
name|type
range|:
name|templateTypes
control|)
block|{
try|try
init|(
name|Writer
name|out
init|=
name|openWriter
argument_list|(
name|type
argument_list|)
init|)
block|{
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|resetContextForArtifactTemplate
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|getTemplate
argument_list|(
name|type
argument_list|)
operator|.
name|merge
argument_list|(
name|context
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|Template
name|getTemplate
parameter_list|(
name|TemplateType
name|type
parameter_list|)
block|{
name|String
name|templateName
init|=
name|customTemplateName
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|templateName
operator|==
literal|null
condition|)
block|{
name|templateName
operator|=
name|type
operator|.
name|pathFromSourceRoot
argument_list|()
expr_stmt|;
block|}
comment|// Velocity< 1.5 has some memory problems, so we will create a VelocityEngine every time,
comment|// and store templates in an internal cache, to avoid uncontrolled memory leaks...
comment|// Presumably 1.5 fixes it.
name|Template
name|template
init|=
name|templateCache
operator|.
name|get
argument_list|(
name|templateName
argument_list|)
decl_stmt|;
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|initVelocityProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|VelocityEngine
name|velocityEngine
init|=
operator|new
name|VelocityEngine
argument_list|()
decl_stmt|;
name|velocityEngine
operator|.
name|init
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|template
operator|=
name|velocityEngine
operator|.
name|getTemplate
argument_list|(
name|templateName
argument_list|)
expr_stmt|;
name|templateCache
operator|.
name|put
argument_list|(
name|templateName
argument_list|,
name|template
argument_list|)
expr_stmt|;
block|}
return|return
name|template
return|;
block|}
specifier|protected
name|void
name|initVelocityProperties
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
name|props
operator|.
name|put
argument_list|(
literal|"resource.loaders"
argument_list|,
literal|"cayenne"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"resource.loader.cayenne.class"
argument_list|,
name|ClassGeneratorResourceLoader
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"resource.loader.cayenne.cache"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
if|if
condition|(
name|cgenConfiguration
operator|.
name|getRootPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
literal|"resource.loader.cayenne.root"
argument_list|,
name|cgenConfiguration
operator|.
name|getRootPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Validates the state of this class generator. 	 * Throws CayenneRuntimeException if it is in an inconsistent state. 	 * Called internally from "execute". 	 */
specifier|protected
name|void
name|validateAttributes
parameter_list|()
block|{
name|Path
name|dir
init|=
name|cgenConfiguration
operator|.
name|buildPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|dir
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"'rootPath' attribute is missing."
argument_list|)
throw|;
block|}
if|if
condition|(
name|Files
operator|.
name|notExists
argument_list|(
name|dir
argument_list|)
condition|)
block|{
try|try
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|dir
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"can't create directory"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
operator|!
name|Files
operator|.
name|isDirectory
argument_list|(
name|dir
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"'destDir' is not a directory."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|Files
operator|.
name|isWritable
argument_list|(
name|dir
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Do not have write permissions for %s"
argument_list|,
name|dir
argument_list|)
throw|;
block|}
block|}
comment|/** 	 * Opens a Writer to write generated output. Returned Writer is mapped to a 	 * filesystem file (although subclasses may override that). File location is 	 * determined from the current state of VelocityContext and the TemplateType 	 * passed as a parameter. Writer encoding is determined from the value of 	 * the "encoding" property. 	 */
specifier|protected
name|Writer
name|openWriter
parameter_list|(
name|TemplateType
name|templateType
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|outFile
init|=
operator|(
name|templateType
operator|.
name|isSuperclass
argument_list|()
operator|)
condition|?
name|fileForSuperclass
argument_list|()
else|:
name|fileForClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|outFile
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|logger
operator|!=
literal|null
condition|)
block|{
name|String
name|label
init|=
name|templateType
operator|.
name|isSuperclass
argument_list|()
condition|?
literal|"superclass"
else|:
literal|"class"
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Generating "
operator|+
name|label
operator|+
literal|" file: "
operator|+
name|outFile
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// return writer with specified encoding
name|FileOutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
decl_stmt|;
return|return
operator|(
name|cgenConfiguration
operator|.
name|getEncoding
argument_list|()
operator|!=
literal|null
operator|)
condition|?
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|,
name|cgenConfiguration
operator|.
name|getEncoding
argument_list|()
argument_list|)
else|:
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|)
return|;
block|}
comment|/** 	 * Returns a target file where a generated superclass must be saved. If null 	 * is returned, class shouldn't be generated. 	 */
specifier|private
name|File
name|fileForSuperclass
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|packageName
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|Artifact
operator|.
name|SUPER_PACKAGE_KEY
argument_list|)
decl_stmt|;
name|String
name|className
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|Artifact
operator|.
name|SUPER_CLASS_KEY
argument_list|)
decl_stmt|;
name|String
name|filename
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
operator|.
name|replaceWildcardInStringWithString
argument_list|(
name|WILDCARD
argument_list|,
name|cgenConfiguration
operator|.
name|getOutputPattern
argument_list|()
argument_list|,
name|className
argument_list|)
decl_stmt|;
name|File
name|dest
init|=
operator|new
name|File
argument_list|(
name|mkpath
argument_list|(
operator|new
name|File
argument_list|(
name|cgenConfiguration
operator|.
name|buildPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|packageName
argument_list|)
argument_list|,
name|filename
argument_list|)
decl_stmt|;
if|if
condition|(
name|dest
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|fileNeedUpdate
argument_list|(
name|dest
argument_list|,
name|cgenConfiguration
operator|.
name|getSuperTemplate
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|dest
return|;
block|}
comment|/** 	 * Returns a target file where a generated class must be saved. If null is 	 * returned, class shouldn't be generated. 	 */
specifier|private
name|File
name|fileForClass
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|packageName
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|Artifact
operator|.
name|SUB_PACKAGE_KEY
argument_list|)
decl_stmt|;
name|String
name|className
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|Artifact
operator|.
name|SUB_CLASS_KEY
argument_list|)
decl_stmt|;
name|String
name|filename
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
operator|.
name|replaceWildcardInStringWithString
argument_list|(
name|WILDCARD
argument_list|,
name|cgenConfiguration
operator|.
name|getOutputPattern
argument_list|()
argument_list|,
name|className
argument_list|)
decl_stmt|;
name|File
name|dest
init|=
operator|new
name|File
argument_list|(
name|mkpath
argument_list|(
operator|new
name|File
argument_list|(
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|cgenConfiguration
operator|.
name|buildPath
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|packageName
argument_list|)
argument_list|,
name|filename
argument_list|)
decl_stmt|;
if|if
condition|(
name|dest
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// no overwrite of subclasses
if|if
condition|(
name|cgenConfiguration
operator|.
name|isMakePairs
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// skip if said so
if|if
condition|(
operator|!
name|cgenConfiguration
operator|.
name|isOverwrite
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|fileNeedUpdate
argument_list|(
name|dest
argument_list|,
name|cgenConfiguration
operator|.
name|getTemplate
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
return|return
name|dest
return|;
block|}
comment|/** 	 * Ignore if the destination is newer than the map 	 * (internal timestamp), i.e. has been generated after the map was 	 * last saved AND the template is older than the destination file 	 */
specifier|protected
name|boolean
name|fileNeedUpdate
parameter_list|(
name|File
name|dest
parameter_list|,
name|String
name|templateFileName
parameter_list|)
block|{
if|if
condition|(
name|cgenConfiguration
operator|.
name|isForce
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|isOld
argument_list|(
name|dest
argument_list|)
condition|)
block|{
if|if
condition|(
name|templateFileName
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|File
name|templateFile
init|=
operator|new
name|File
argument_list|(
name|templateFileName
argument_list|)
decl_stmt|;
return|return
name|templateFile
operator|.
name|lastModified
argument_list|()
operator|>=
name|dest
operator|.
name|lastModified
argument_list|()
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/** 	 * Is file modified after internal timestamp (usually equal to mtime of datamap file) 	 */
specifier|protected
name|boolean
name|isOld
parameter_list|(
name|File
name|file
parameter_list|)
block|{
return|return
name|file
operator|.
name|lastModified
argument_list|()
operator|>
name|cgenConfiguration
operator|.
name|getTimestamp
argument_list|()
return|;
block|}
comment|/** 	 * Returns a File object corresponding to a directory where files that 	 * belong to<code>pkgName</code> package should reside. Creates any missing 	 * diectories below<code>dest</code>. 	 */
specifier|private
name|File
name|mkpath
parameter_list|(
name|File
name|dest
parameter_list|,
name|String
name|pkgName
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|cgenConfiguration
operator|.
name|isUsePkgPath
argument_list|()
operator|||
name|pkgName
operator|==
literal|null
condition|)
block|{
return|return
name|dest
return|;
block|}
name|String
name|path
init|=
name|pkgName
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
name|File
operator|.
name|separatorChar
argument_list|)
decl_stmt|;
name|File
name|fullPath
init|=
operator|new
name|File
argument_list|(
name|dest
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|fullPath
operator|.
name|isDirectory
argument_list|()
operator|&&
operator|!
name|fullPath
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Error making path: "
operator|+
name|fullPath
argument_list|)
throw|;
block|}
return|return
name|fullPath
return|;
block|}
comment|/** 	 * Injects an optional logger that will be used to trace generated files at 	 * the info level. 	 */
specifier|public
name|void
name|setLogger
parameter_list|(
name|Logger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
comment|/** 	 * @since 4.1 	 */
specifier|public
name|CgenConfiguration
name|getCgenConfiguration
parameter_list|()
block|{
return|return
name|cgenConfiguration
return|;
block|}
comment|/** 	 * Sets an optional shared VelocityContext. Useful with tools like VPP that 	 * can set custom values in the context, not known to Cayenne. 	 */
specifier|public
name|void
name|setContext
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
comment|/** 	 * @since 4.1 	 */
specifier|public
name|void
name|setCgenConfiguration
parameter_list|(
name|CgenConfiguration
name|cgenConfiguration
parameter_list|)
block|{
name|this
operator|.
name|cgenConfiguration
operator|=
name|cgenConfiguration
expr_stmt|;
block|}
specifier|public
name|ToolsUtilsFactory
name|getUtilsFactory
parameter_list|()
block|{
return|return
name|utilsFactory
return|;
block|}
specifier|public
name|void
name|setUtilsFactory
parameter_list|(
name|ToolsUtilsFactory
name|utilsFactory
parameter_list|)
block|{
name|this
operator|.
name|utilsFactory
operator|=
name|utilsFactory
expr_stmt|;
block|}
specifier|public
name|void
name|setMetadataUtils
parameter_list|(
name|MetadataUtils
name|metadataUtils
parameter_list|)
block|{
name|this
operator|.
name|metadataUtils
operator|=
name|metadataUtils
expr_stmt|;
block|}
specifier|public
name|MetadataUtils
name|getMetadataUtils
parameter_list|()
block|{
return|return
name|metadataUtils
return|;
block|}
block|}
end_class

end_unit

