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
name|tools
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|Reference
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
name|dbsync
operator|.
name|filter
operator|.
name|NamePatternMatcher
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
name|reverse
operator|.
name|configuration
operator|.
name|ToolsModule
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
name|CgenModule
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
name|map
operator|.
name|DataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|Action
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|GradleException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|InvalidUserDataException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|plugins
operator|.
name|JavaPlugin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|Input
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|InputDirectory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|InputFile
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|OutputDirectory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|SourceSetContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|TaskAction
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
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|CgenTask
extends|extends
name|BaseCayenneTask
block|{
specifier|private
specifier|static
specifier|final
name|File
index|[]
name|NO_FILES
init|=
operator|new
name|File
index|[
literal|0
index|]
decl_stmt|;
specifier|private
name|File
name|additionalMaps
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|Boolean
name|client
decl_stmt|;
specifier|private
name|File
name|destDir
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|encoding
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|excludeEntities
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|includeEntities
decl_stmt|;
comment|/**      * @since 4.1      */
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|excludeEmbeddables
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|Boolean
name|makePairs
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|mode
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|outputPattern
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|Boolean
name|overwrite
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|superPkg
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|superTemplate
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|template
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|embeddableSuperTemplate
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|embeddableTemplate
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|Boolean
name|usePkgPath
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|Boolean
name|createPropertyNames
decl_stmt|;
comment|/**      * Force run (skip check for files modification time)      * @since 4.1      */
annotation|@
name|Input
specifier|private
name|boolean
name|force
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|queryTemplate
decl_stmt|;
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|querySuperTemplate
decl_stmt|;
comment|/**      * If set to<code>true</code>, will generate PK attributes as Properties.      * Default is<code>false</code>.      * @since 4.1      */
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|Boolean
name|createPKProperties
decl_stmt|;
specifier|private
name|String
name|destDirName
decl_stmt|;
specifier|private
name|DataChannelMetaData
name|metaData
decl_stmt|;
specifier|private
name|boolean
name|useConfigFromDataMap
decl_stmt|;
annotation|@
name|TaskAction
specifier|public
name|void
name|generate
parameter_list|()
block|{
name|File
name|dataMapFile
init|=
name|getDataMapFile
argument_list|()
decl_stmt|;
specifier|final
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|CgenModule
argument_list|()
argument_list|,
operator|new
name|ToolsModule
argument_list|(
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CgenTask
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|metaData
operator|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DataChannelMetaData
operator|.
name|class
argument_list|)
expr_stmt|;
name|CayenneGeneratorMapLoaderAction
name|loaderAction
init|=
operator|new
name|CayenneGeneratorMapLoaderAction
argument_list|(
name|injector
argument_list|)
decl_stmt|;
name|loaderAction
operator|.
name|setMainDataMapFile
argument_list|(
name|dataMapFile
argument_list|)
expr_stmt|;
name|CayenneGeneratorEntityFilterAction
name|filterEntityAction
init|=
operator|new
name|CayenneGeneratorEntityFilterAction
argument_list|()
decl_stmt|;
name|filterEntityAction
operator|.
name|setNameFilter
argument_list|(
name|NamePatternMatcher
operator|.
name|build
argument_list|(
name|getLogger
argument_list|()
argument_list|,
name|includeEntities
argument_list|,
name|excludeEntities
argument_list|)
argument_list|)
expr_stmt|;
name|CayenneGeneratorEmbeddableFilterAction
name|filterEmbeddableAction
init|=
operator|new
name|CayenneGeneratorEmbeddableFilterAction
argument_list|()
decl_stmt|;
name|filterEmbeddableAction
operator|.
name|setNameFilter
argument_list|(
name|NamePatternMatcher
operator|.
name|build
argument_list|(
name|getLogger
argument_list|()
argument_list|,
literal|null
argument_list|,
name|excludeEmbeddables
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|loaderAction
operator|.
name|setAdditionalDataMapFiles
argument_list|(
name|convertAdditionalDataMaps
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|dataMap
init|=
name|loaderAction
operator|.
name|getMainDataMap
argument_list|()
decl_stmt|;
name|ClassGenerationAction
name|generator
init|=
name|this
operator|.
name|createGenerator
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
name|filterEntityAction
operator|.
name|setClient
argument_list|(
name|generator
operator|.
name|getCgenConfiguration
argument_list|()
operator|.
name|isClient
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setLogger
argument_list|(
name|getLogger
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|force
operator|||
name|getProject
argument_list|()
operator|.
name|hasProperty
argument_list|(
literal|"force"
argument_list|)
condition|)
block|{
name|generator
operator|.
name|getCgenConfiguration
argument_list|()
operator|.
name|setForce
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|generator
operator|.
name|getCgenConfiguration
argument_list|()
operator|.
name|setTimestamp
argument_list|(
name|dataMapFile
operator|.
name|lastModified
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|hasConfig
argument_list|()
operator|&&
name|useConfigFromDataMap
condition|)
block|{
name|generator
operator|.
name|prepareArtifacts
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|generator
operator|.
name|addEntities
argument_list|(
name|filterEntityAction
operator|.
name|getFilteredEntities
argument_list|(
name|dataMap
argument_list|)
argument_list|)
expr_stmt|;
name|generator
operator|.
name|addEmbeddables
argument_list|(
name|filterEmbeddableAction
operator|.
name|getFilteredEmbeddables
argument_list|(
name|dataMap
argument_list|)
argument_list|)
expr_stmt|;
name|generator
operator|.
name|addQueries
argument_list|(
name|dataMap
operator|.
name|getQueryDescriptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|generator
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
throw|throw
operator|new
name|GradleException
argument_list|(
literal|"Error generating classes: "
argument_list|,
name|exception
argument_list|)
throw|;
block|}
block|}
specifier|private
name|File
index|[]
name|convertAdditionalDataMaps
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|additionalMaps
operator|==
literal|null
condition|)
block|{
return|return
name|NO_FILES
return|;
block|}
if|if
condition|(
operator|!
name|additionalMaps
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|GradleException
argument_list|(
literal|"'additionalMaps' must be a directory."
argument_list|)
throw|;
block|}
return|return
name|additionalMaps
operator|.
name|listFiles
argument_list|(
parameter_list|(
name|dir
parameter_list|,
name|name
parameter_list|)
lambda|->
name|name
operator|!=
literal|null
operator|&&
name|name
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".map.xml"
argument_list|)
argument_list|)
return|;
block|}
name|ClassGenerationAction
name|createGenerator
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
name|buildConfiguration
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
return|return
name|cgenConfiguration
operator|.
name|isClient
argument_list|()
condition|?
operator|new
name|ClientClassGenerationAction
argument_list|(
name|cgenConfiguration
argument_list|)
else|:
operator|new
name|ClassGenerationAction
argument_list|(
name|cgenConfiguration
argument_list|)
return|;
block|}
name|CgenConfiguration
name|buildConfiguration
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|CgenConfiguration
name|cgenConfiguration
decl_stmt|;
if|if
condition|(
name|hasConfig
argument_list|()
condition|)
block|{
return|return
name|cgenConfigFromPom
argument_list|(
name|dataMap
argument_list|)
return|;
block|}
if|else if
condition|(
name|metaData
operator|!=
literal|null
operator|&&
name|metaData
operator|.
name|get
argument_list|(
name|dataMap
argument_list|,
name|CgenConfiguration
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|useConfigFromDataMap
operator|=
literal|true
expr_stmt|;
name|cgenConfiguration
operator|=
name|metaData
operator|.
name|get
argument_list|(
name|dataMap
argument_list|,
name|CgenConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|cgenConfiguration
return|;
block|}
else|else
block|{
name|cgenConfiguration
operator|=
operator|new
name|CgenConfiguration
argument_list|()
expr_stmt|;
name|cgenConfiguration
operator|.
name|setRelPath
argument_list|(
name|getDestDirFile
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
return|return
name|cgenConfiguration
return|;
block|}
block|}
specifier|private
name|CgenConfiguration
name|cgenConfigFromPom
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
operator|new
name|CgenConfiguration
argument_list|()
decl_stmt|;
name|cgenConfiguration
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setRelPath
argument_list|(
name|getDestDirFile
argument_list|()
operator|!=
literal|null
condition|?
name|getDestDirFile
argument_list|()
operator|.
name|toPath
argument_list|()
else|:
name|cgenConfiguration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setEncoding
argument_list|(
name|encoding
operator|!=
literal|null
condition|?
name|encoding
else|:
name|cgenConfiguration
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setMakePairs
argument_list|(
name|makePairs
operator|!=
literal|null
condition|?
name|makePairs
else|:
name|cgenConfiguration
operator|.
name|isMakePairs
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setArtifactsGenerationMode
argument_list|(
name|mode
operator|!=
literal|null
condition|?
name|mode
else|:
name|cgenConfiguration
operator|.
name|getArtifactsGenerationMode
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setOutputPattern
argument_list|(
name|outputPattern
operator|!=
literal|null
condition|?
name|outputPattern
else|:
name|cgenConfiguration
operator|.
name|getOutputPattern
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setOverwrite
argument_list|(
name|overwrite
operator|!=
literal|null
condition|?
name|overwrite
else|:
name|cgenConfiguration
operator|.
name|isOverwrite
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setSuperPkg
argument_list|(
name|superPkg
operator|!=
literal|null
condition|?
name|superPkg
else|:
name|cgenConfiguration
operator|.
name|getSuperPkg
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setSuperTemplate
argument_list|(
name|superTemplate
operator|!=
literal|null
condition|?
name|superTemplate
else|:
name|cgenConfiguration
operator|.
name|getSuperTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setTemplate
argument_list|(
name|template
operator|!=
literal|null
condition|?
name|template
else|:
name|cgenConfiguration
operator|.
name|getTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setEmbeddableSuperTemplate
argument_list|(
name|embeddableSuperTemplate
operator|!=
literal|null
condition|?
name|embeddableSuperTemplate
else|:
name|cgenConfiguration
operator|.
name|getEmbeddableSuperTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setEmbeddableTemplate
argument_list|(
name|embeddableTemplate
operator|!=
literal|null
condition|?
name|embeddableTemplate
else|:
name|cgenConfiguration
operator|.
name|getEmbeddableTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setUsePkgPath
argument_list|(
name|usePkgPath
operator|!=
literal|null
condition|?
name|usePkgPath
else|:
name|cgenConfiguration
operator|.
name|isUsePkgPath
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setCreatePropertyNames
argument_list|(
name|createPropertyNames
operator|!=
literal|null
condition|?
name|createPropertyNames
else|:
name|cgenConfiguration
operator|.
name|isCreatePropertyNames
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setQueryTemplate
argument_list|(
name|queryTemplate
operator|!=
literal|null
condition|?
name|queryTemplate
else|:
name|cgenConfiguration
operator|.
name|getQueryTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setQuerySuperTemplate
argument_list|(
name|querySuperTemplate
operator|!=
literal|null
condition|?
name|querySuperTemplate
else|:
name|cgenConfiguration
operator|.
name|getQuerySuperTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setCreatePKProperties
argument_list|(
name|createPKProperties
operator|!=
literal|null
condition|?
name|createPKProperties
else|:
name|cgenConfiguration
operator|.
name|isCreatePKProperties
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setClient
argument_list|(
name|client
operator|!=
literal|null
condition|?
name|client
else|:
name|cgenConfiguration
operator|.
name|isClient
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|cgenConfiguration
operator|.
name|isMakePairs
argument_list|()
condition|)
block|{
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|setTemplate
argument_list|(
name|cgenConfiguration
operator|.
name|isClient
argument_list|()
condition|?
name|ClientClassGenerationAction
operator|.
name|SINGLE_CLASS_TEMPLATE
else|:
name|ClassGenerationAction
operator|.
name|SINGLE_CLASS_TEMPLATE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|embeddableTemplate
operator|==
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|setEmbeddableTemplate
argument_list|(
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SINGLE_CLASS_TEMPLATE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|queryTemplate
operator|==
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|setQueryTemplate
argument_list|(
name|cgenConfiguration
operator|.
name|isClient
argument_list|()
condition|?
name|ClientClassGenerationAction
operator|.
name|DATAMAP_SINGLE_CLASS_TEMPLATE
else|:
name|ClassGenerationAction
operator|.
name|DATAMAP_SINGLE_CLASS_TEMPLATE
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cgenConfiguration
return|;
block|}
specifier|private
name|boolean
name|hasConfig
parameter_list|()
block|{
return|return
name|destDir
operator|!=
literal|null
operator|||
name|destDirName
operator|!=
literal|null
operator|||
name|encoding
operator|!=
literal|null
operator|||
name|client
operator|!=
literal|null
operator|||
name|excludeEntities
operator|!=
literal|null
operator|||
name|excludeEmbeddables
operator|!=
literal|null
operator|||
name|includeEntities
operator|!=
literal|null
operator|||
name|makePairs
operator|!=
literal|null
operator|||
name|mode
operator|!=
literal|null
operator|||
name|outputPattern
operator|!=
literal|null
operator|||
name|overwrite
operator|!=
literal|null
operator|||
name|superPkg
operator|!=
literal|null
operator|||
name|superTemplate
operator|!=
literal|null
operator|||
name|template
operator|!=
literal|null
operator|||
name|embeddableTemplate
operator|!=
literal|null
operator|||
name|embeddableSuperTemplate
operator|!=
literal|null
operator|||
name|usePkgPath
operator|!=
literal|null
operator|||
name|createPropertyNames
operator|!=
literal|null
operator|||
name|force
operator|||
name|queryTemplate
operator|!=
literal|null
operator|||
name|querySuperTemplate
operator|!=
literal|null
operator|||
name|createPKProperties
operator|!=
literal|null
return|;
block|}
annotation|@
name|OutputDirectory
specifier|protected
name|File
name|getDestDirFile
parameter_list|()
block|{
specifier|final
name|Reference
argument_list|<
name|File
argument_list|>
name|javaSourceDir
init|=
operator|new
name|Reference
argument_list|<>
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|destDir
operator|!=
literal|null
condition|)
block|{
name|javaSourceDir
operator|.
name|set
argument_list|(
name|destDir
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|destDirName
operator|!=
literal|null
condition|)
block|{
name|javaSourceDir
operator|.
name|set
argument_list|(
name|getProject
argument_list|()
operator|.
name|file
argument_list|(
name|destDirName
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getProject
argument_list|()
operator|.
name|getPlugins
argument_list|()
operator|.
name|withType
argument_list|(
name|JavaPlugin
operator|.
name|class
argument_list|,
operator|new
name|Action
argument_list|<
name|JavaPlugin
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
specifier|final
name|JavaPlugin
name|plugin
parameter_list|)
block|{
name|SourceSetContainer
name|sourceSets
init|=
operator|(
name|SourceSetContainer
operator|)
name|getProject
argument_list|()
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
literal|"sourceSets"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|File
argument_list|>
name|sourceDirs
init|=
name|sourceSets
operator|.
name|getByName
argument_list|(
literal|"main"
argument_list|)
operator|.
name|getJava
argument_list|()
operator|.
name|getSrcDirs
argument_list|()
decl_stmt|;
if|if
condition|(
name|sourceDirs
operator|!=
literal|null
operator|&&
operator|!
name|sourceDirs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// find java directory, if there is no such dir, take first
for|for
control|(
name|File
name|dir
range|:
name|sourceDirs
control|)
block|{
if|if
condition|(
name|dir
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"java"
argument_list|)
condition|)
block|{
name|javaSourceDir
operator|.
name|set
argument_list|(
name|dir
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|javaSourceDir
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
name|javaSourceDir
operator|.
name|set
argument_list|(
name|sourceDirs
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|javaSourceDir
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidUserDataException
argument_list|(
literal|"cgen.destDir is not set and there is no Java source sets found."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|javaSourceDir
operator|.
name|get
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|javaSourceDir
operator|.
name|get
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
return|return
name|javaSourceDir
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|InputFile
specifier|public
name|File
name|getDataMapFile
parameter_list|()
block|{
return|return
name|super
operator|.
name|getDataMapFile
argument_list|()
return|;
block|}
annotation|@
name|Optional
annotation|@
name|OutputDirectory
specifier|public
name|File
name|getDestDir
parameter_list|()
block|{
return|return
name|destDir
return|;
block|}
specifier|public
name|void
name|setDestDir
parameter_list|(
name|File
name|destDir
parameter_list|)
block|{
name|this
operator|.
name|destDir
operator|=
name|destDir
expr_stmt|;
block|}
specifier|public
name|void
name|setDestDir
parameter_list|(
name|String
name|destDir
parameter_list|)
block|{
name|this
operator|.
name|destDirName
operator|=
name|destDir
expr_stmt|;
block|}
specifier|public
name|void
name|destDir
parameter_list|(
name|String
name|destDir
parameter_list|)
block|{
name|setDestDir
argument_list|(
name|destDir
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|destDir
parameter_list|(
name|File
name|destDir
parameter_list|)
block|{
name|setDestDir
argument_list|(
name|destDir
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Optional
annotation|@
name|InputDirectory
specifier|public
name|File
name|getAdditionalMaps
parameter_list|()
block|{
return|return
name|additionalMaps
return|;
block|}
specifier|public
name|void
name|setAdditionalMaps
parameter_list|(
name|File
name|additionalMaps
parameter_list|)
block|{
name|this
operator|.
name|additionalMaps
operator|=
name|additionalMaps
expr_stmt|;
block|}
specifier|public
name|void
name|additionalMaps
parameter_list|(
name|File
name|additionalMaps
parameter_list|)
block|{
name|setAdditionalMaps
argument_list|(
name|additionalMaps
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|isClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
specifier|public
name|void
name|setClient
parameter_list|(
name|Boolean
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
specifier|public
name|void
name|client
parameter_list|(
name|boolean
name|client
parameter_list|)
block|{
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
specifier|public
name|void
name|encoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|setEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getExcludeEntities
parameter_list|()
block|{
return|return
name|excludeEntities
return|;
block|}
specifier|public
name|void
name|setExcludeEntities
parameter_list|(
name|String
name|excludeEntities
parameter_list|)
block|{
name|this
operator|.
name|excludeEntities
operator|=
name|excludeEntities
expr_stmt|;
block|}
specifier|public
name|void
name|excludeEntities
parameter_list|(
name|String
name|excludeEntities
parameter_list|)
block|{
name|setExcludeEntities
argument_list|(
name|excludeEntities
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getIncludeEntities
parameter_list|()
block|{
return|return
name|includeEntities
return|;
block|}
specifier|public
name|void
name|setIncludeEntities
parameter_list|(
name|String
name|includeEntities
parameter_list|)
block|{
name|this
operator|.
name|includeEntities
operator|=
name|includeEntities
expr_stmt|;
block|}
specifier|public
name|void
name|includeEntities
parameter_list|(
name|String
name|includeEntities
parameter_list|)
block|{
name|setIncludeEntities
argument_list|(
name|includeEntities
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getExcludeEmbeddables
parameter_list|()
block|{
return|return
name|excludeEmbeddables
return|;
block|}
specifier|public
name|void
name|setExcludeEmbeddables
parameter_list|(
name|String
name|excludeEmbeddables
parameter_list|)
block|{
name|this
operator|.
name|excludeEmbeddables
operator|=
name|excludeEmbeddables
expr_stmt|;
block|}
comment|/**      * @since 4.1      * @param excludeEmbeddables      */
specifier|public
name|void
name|excludeEmbeddables
parameter_list|(
name|String
name|excludeEmbeddables
parameter_list|)
block|{
name|setExcludeEmbeddables
argument_list|(
name|excludeEmbeddables
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|isMakePairs
parameter_list|()
block|{
return|return
name|makePairs
return|;
block|}
specifier|public
name|void
name|setMakePairs
parameter_list|(
name|Boolean
name|makePairs
parameter_list|)
block|{
name|this
operator|.
name|makePairs
operator|=
name|makePairs
expr_stmt|;
block|}
specifier|public
name|void
name|makePairs
parameter_list|(
name|boolean
name|makePairs
parameter_list|)
block|{
name|setMakePairs
argument_list|(
name|makePairs
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
specifier|public
name|void
name|setMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
block|}
specifier|public
name|void
name|mode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|setMode
argument_list|(
name|mode
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getOutputPattern
parameter_list|()
block|{
return|return
name|outputPattern
return|;
block|}
specifier|public
name|void
name|setOutputPattern
parameter_list|(
name|String
name|outputPattern
parameter_list|)
block|{
name|this
operator|.
name|outputPattern
operator|=
name|outputPattern
expr_stmt|;
block|}
specifier|public
name|void
name|outputPattern
parameter_list|(
name|String
name|outputPattern
parameter_list|)
block|{
name|setOutputPattern
argument_list|(
name|outputPattern
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|isOverwrite
parameter_list|()
block|{
return|return
name|overwrite
return|;
block|}
specifier|public
name|void
name|setOverwrite
parameter_list|(
name|Boolean
name|overwrite
parameter_list|)
block|{
name|this
operator|.
name|overwrite
operator|=
name|overwrite
expr_stmt|;
block|}
specifier|public
name|void
name|overwrite
parameter_list|(
name|boolean
name|overwrite
parameter_list|)
block|{
name|setOverwrite
argument_list|(
name|overwrite
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getSuperPkg
parameter_list|()
block|{
return|return
name|superPkg
return|;
block|}
specifier|public
name|void
name|setSuperPkg
parameter_list|(
name|String
name|superPkg
parameter_list|)
block|{
name|this
operator|.
name|superPkg
operator|=
name|superPkg
expr_stmt|;
block|}
specifier|public
name|void
name|superPkg
parameter_list|(
name|String
name|superPkg
parameter_list|)
block|{
name|setSuperPkg
argument_list|(
name|superPkg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getSuperTemplate
parameter_list|()
block|{
return|return
name|superTemplate
return|;
block|}
specifier|public
name|void
name|setSuperTemplate
parameter_list|(
name|String
name|superTemplate
parameter_list|)
block|{
name|this
operator|.
name|superTemplate
operator|=
name|superTemplate
expr_stmt|;
block|}
specifier|public
name|void
name|superTemplate
parameter_list|(
name|String
name|superTemplate
parameter_list|)
block|{
name|setSuperTemplate
argument_list|(
name|superTemplate
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
specifier|public
name|void
name|setTemplate
parameter_list|(
name|String
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
specifier|public
name|void
name|template
parameter_list|(
name|String
name|template
parameter_list|)
block|{
name|setTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getEmbeddableSuperTemplate
parameter_list|()
block|{
return|return
name|embeddableSuperTemplate
return|;
block|}
specifier|public
name|void
name|setEmbeddableSuperTemplate
parameter_list|(
name|String
name|embeddableSuperTemplate
parameter_list|)
block|{
name|this
operator|.
name|embeddableSuperTemplate
operator|=
name|embeddableSuperTemplate
expr_stmt|;
block|}
specifier|public
name|void
name|embeddableSuperTemplate
parameter_list|(
name|String
name|embeddableSuperTemplate
parameter_list|)
block|{
name|setEmbeddableSuperTemplate
argument_list|(
name|embeddableSuperTemplate
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getEmbeddableTemplate
parameter_list|()
block|{
return|return
name|embeddableTemplate
return|;
block|}
specifier|public
name|void
name|setEmbeddableTemplate
parameter_list|(
name|String
name|embeddableTemplate
parameter_list|)
block|{
name|this
operator|.
name|embeddableTemplate
operator|=
name|embeddableTemplate
expr_stmt|;
block|}
specifier|public
name|void
name|embeddableTemplate
parameter_list|(
name|String
name|embeddableTemplate
parameter_list|)
block|{
name|setEmbeddableTemplate
argument_list|(
name|embeddableTemplate
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|isUsePkgPath
parameter_list|()
block|{
return|return
name|usePkgPath
return|;
block|}
specifier|public
name|void
name|setUsePkgPath
parameter_list|(
name|Boolean
name|usePkgPath
parameter_list|)
block|{
name|this
operator|.
name|usePkgPath
operator|=
name|usePkgPath
expr_stmt|;
block|}
specifier|public
name|void
name|usePkgPath
parameter_list|(
name|boolean
name|usePkgPath
parameter_list|)
block|{
name|setUsePkgPath
argument_list|(
name|usePkgPath
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|isCreatePropertyNames
parameter_list|()
block|{
return|return
name|createPropertyNames
return|;
block|}
specifier|public
name|void
name|setCreatePropertyNames
parameter_list|(
name|Boolean
name|createPropertyNames
parameter_list|)
block|{
name|this
operator|.
name|createPropertyNames
operator|=
name|createPropertyNames
expr_stmt|;
block|}
specifier|public
name|void
name|createPropertyNames
parameter_list|(
name|boolean
name|createPropertyNames
parameter_list|)
block|{
name|setCreatePropertyNames
argument_list|(
name|createPropertyNames
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isForce
parameter_list|()
block|{
return|return
name|force
return|;
block|}
specifier|public
name|void
name|setForce
parameter_list|(
name|boolean
name|force
parameter_list|)
block|{
name|this
operator|.
name|force
operator|=
name|force
expr_stmt|;
block|}
specifier|public
name|void
name|force
parameter_list|(
name|boolean
name|force
parameter_list|)
block|{
name|setForce
argument_list|(
name|force
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setCreatePKProperties
parameter_list|(
name|Boolean
name|createPKProperties
parameter_list|)
block|{
name|this
operator|.
name|createPKProperties
operator|=
name|createPKProperties
expr_stmt|;
block|}
specifier|public
name|void
name|createPKProperties
parameter_list|(
name|boolean
name|createPKProperties
parameter_list|)
block|{
name|setCreatePKProperties
argument_list|(
name|createPKProperties
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

