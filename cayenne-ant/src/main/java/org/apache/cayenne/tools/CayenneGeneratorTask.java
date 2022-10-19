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
name|ArtifactsGenerationMode
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
name|ClassGenerationActionFactory
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
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|BuildException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|types
operator|.
name|Path
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
comment|/**  * An Ant task to perform class generation based on CayenneDataMap.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|CayenneGeneratorTask
extends|extends
name|CayenneTask
block|{
specifier|private
name|AntLogger
name|logger
decl_stmt|;
specifier|protected
name|String
name|includeEntitiesPattern
decl_stmt|;
specifier|protected
name|String
name|excludeEntitiesPattern
decl_stmt|;
comment|/**      * @since 4.1      */
specifier|protected
name|String
name|excludeEmbeddablesPattern
decl_stmt|;
specifier|protected
name|File
name|map
decl_stmt|;
specifier|protected
name|File
name|additionalMaps
index|[]
decl_stmt|;
specifier|protected
name|File
name|destDir
decl_stmt|;
specifier|protected
name|String
name|encoding
decl_stmt|;
specifier|protected
name|Boolean
name|makepairs
decl_stmt|;
specifier|protected
name|String
name|mode
decl_stmt|;
specifier|protected
name|String
name|outputPattern
decl_stmt|;
specifier|protected
name|Boolean
name|overwrite
decl_stmt|;
specifier|protected
name|String
name|superpkg
decl_stmt|;
specifier|protected
name|String
name|supertemplate
decl_stmt|;
specifier|protected
name|String
name|template
decl_stmt|;
specifier|protected
name|String
name|embeddabletemplate
decl_stmt|;
specifier|protected
name|String
name|embeddablesupertemplate
decl_stmt|;
specifier|protected
name|String
name|datamaptemplate
decl_stmt|;
specifier|protected
name|String
name|datamapsupertemplate
decl_stmt|;
specifier|protected
name|Boolean
name|usepkgpath
decl_stmt|;
specifier|protected
name|Boolean
name|createpropertynames
decl_stmt|;
comment|/**      * @since 4.1      */
specifier|private
name|boolean
name|force
decl_stmt|;
specifier|private
name|boolean
name|useConfigFromDataMap
decl_stmt|;
specifier|private
specifier|transient
name|Injector
name|injector
decl_stmt|;
comment|/**      * Create PK attributes as Properties      *      * @since 4.1      */
specifier|protected
name|Boolean
name|createpkproperties
decl_stmt|;
comment|/**      * Optional path (classpath or filesystem) to external velocity tool configuration file      * @since 4.2       */
specifier|protected
name|String
name|externaltoolconfig
decl_stmt|;
specifier|public
name|CayenneGeneratorTask
parameter_list|()
block|{
block|}
comment|/**      * Executes the task. It will be called by ant framework.      */
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
name|validateAttributes
argument_list|()
expr_stmt|;
name|ClassLoader
name|loader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|injector
operator|=
operator|new
name|ToolsInjectorBuilder
argument_list|()
operator|.
name|addModule
argument_list|(
operator|new
name|ToolsModule
argument_list|(
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CayenneGeneratorTask
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|logger
operator|=
operator|new
name|AntLogger
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|CayenneGeneratorMapLoaderAction
name|loadAction
init|=
operator|new
name|CayenneGeneratorMapLoaderAction
argument_list|(
name|injector
argument_list|)
decl_stmt|;
name|loadAction
operator|.
name|setMainDataMapFile
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|loadAction
operator|.
name|setAdditionalDataMapFiles
argument_list|(
name|additionalMaps
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|CayenneGeneratorTask
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|dataMap
init|=
name|loadAction
operator|.
name|getMainDataMap
argument_list|()
decl_stmt|;
name|ClassGenerationAction
name|generatorAction
init|=
name|createGenerator
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
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
name|logger
argument_list|,
name|includeEntitiesPattern
argument_list|,
name|excludeEntitiesPattern
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
name|logger
argument_list|,
literal|null
argument_list|,
name|excludeEmbeddablesPattern
argument_list|)
argument_list|)
expr_stmt|;
name|generatorAction
operator|.
name|setLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
if|if
condition|(
name|force
condition|)
block|{
comment|// will (re-)generate all files
name|generatorAction
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
name|generatorAction
operator|.
name|getCgenConfiguration
argument_list|()
operator|.
name|setTimestamp
argument_list|(
name|map
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
name|generatorAction
operator|.
name|prepareArtifacts
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|generatorAction
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
name|generatorAction
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
name|generatorAction
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
name|generatorAction
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
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
name|injector
operator|.
name|getInstance
argument_list|(
name|ClassGenerationActionFactory
operator|.
name|class
argument_list|)
operator|.
name|createAction
argument_list|(
name|cgenConfiguration
argument_list|)
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
name|encoding
operator|!=
literal|null
operator|||
name|excludeEntitiesPattern
operator|!=
literal|null
operator|||
name|excludeEmbeddablesPattern
operator|!=
literal|null
operator|||
name|includeEntitiesPattern
operator|!=
literal|null
operator|||
name|makepairs
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
name|superpkg
operator|!=
literal|null
operator|||
name|supertemplate
operator|!=
literal|null
operator|||
name|template
operator|!=
literal|null
operator|||
name|embeddabletemplate
operator|!=
literal|null
operator|||
name|embeddablesupertemplate
operator|!=
literal|null
operator|||
name|usepkgpath
operator|!=
literal|null
operator|||
name|createpropertynames
operator|!=
literal|null
operator|||
name|datamaptemplate
operator|!=
literal|null
operator|||
name|datamapsupertemplate
operator|!=
literal|null
operator|||
name|createpkproperties
operator|!=
literal|null
operator|||
name|force
operator|||
name|externaltoolconfig
operator|!=
literal|null
return|;
block|}
specifier|private
name|CgenConfiguration
name|buildConfiguration
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|CgenConfiguration
name|cgenConfiguration
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DataChannelMetaData
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
name|dataMap
argument_list|,
name|CgenConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|hasConfig
argument_list|()
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Using cgen config from pom.xml"
argument_list|)
expr_stmt|;
return|return
name|cgenConfigFromPom
argument_list|(
name|dataMap
argument_list|)
return|;
block|}
if|else if
condition|(
name|cgenConfiguration
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Using cgen config from "
operator|+
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|useConfigFromDataMap
operator|=
literal|true
expr_stmt|;
return|return
name|cgenConfiguration
return|;
block|}
else|else
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Using default cgen config."
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|=
operator|new
name|CgenConfiguration
argument_list|()
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
name|destDir
operator|!=
literal|null
condition|?
name|destDir
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
name|makepairs
operator|!=
literal|null
condition|?
name|makepairs
else|:
name|cgenConfiguration
operator|.
name|isMakePairs
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|mode
operator|!=
literal|null
operator|&&
name|mode
operator|.
name|equals
argument_list|(
literal|"datamap"
argument_list|)
condition|)
block|{
name|replaceDatamapGenerationMode
argument_list|()
expr_stmt|;
block|}
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
name|superpkg
operator|!=
literal|null
condition|?
name|superpkg
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
name|supertemplate
operator|!=
literal|null
condition|?
operator|new
name|CgenTemplate
argument_list|(
name|supertemplate
argument_list|,
literal|true
argument_list|,
name|TemplateType
operator|.
name|ENTITY_SUPERCLASS
argument_list|)
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
operator|new
name|CgenTemplate
argument_list|(
name|template
argument_list|,
literal|true
argument_list|,
name|TemplateType
operator|.
name|ENTITY_SUBCLASS
argument_list|)
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
name|embeddablesupertemplate
operator|!=
literal|null
condition|?
operator|new
name|CgenTemplate
argument_list|(
name|embeddablesupertemplate
argument_list|,
literal|true
argument_list|,
name|TemplateType
operator|.
name|EMBEDDABLE_SUPERCLASS
argument_list|)
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
name|embeddabletemplate
operator|!=
literal|null
condition|?
operator|new
name|CgenTemplate
argument_list|(
name|embeddabletemplate
argument_list|,
literal|true
argument_list|,
name|TemplateType
operator|.
name|EMBEDDABLE_SUBCLASS
argument_list|)
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
name|usepkgpath
operator|!=
literal|null
condition|?
name|usepkgpath
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
name|createpropertynames
operator|!=
literal|null
condition|?
name|createpropertynames
else|:
name|cgenConfiguration
operator|.
name|isCreatePropertyNames
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setDataMapTemplate
argument_list|(
name|datamaptemplate
operator|!=
literal|null
condition|?
operator|new
name|CgenTemplate
argument_list|(
name|datamaptemplate
argument_list|,
literal|true
argument_list|,
name|TemplateType
operator|.
name|DATAMAP_SUBCLASS
argument_list|)
else|:
name|cgenConfiguration
operator|.
name|getDataMapTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setDataMapSuperTemplate
argument_list|(
name|datamapsupertemplate
operator|!=
literal|null
condition|?
operator|new
name|CgenTemplate
argument_list|(
name|datamapsupertemplate
argument_list|,
literal|true
argument_list|,
name|TemplateType
operator|.
name|DATAMAP_SUPERCLASS
argument_list|)
else|:
name|cgenConfiguration
operator|.
name|getDataMapSuperTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setCreatePKProperties
argument_list|(
name|createpkproperties
operator|!=
literal|null
condition|?
name|createpkproperties
else|:
name|cgenConfiguration
operator|.
name|isCreatePKProperties
argument_list|()
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setExternalToolConfig
argument_list|(
name|externaltoolconfig
operator|!=
literal|null
condition|?
name|externaltoolconfig
else|:
name|cgenConfiguration
operator|.
name|getExternalToolConfig
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
name|TemplateType
operator|.
name|ENTITY_SINGLE_CLASS
operator|.
name|defaultTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|embeddabletemplate
operator|==
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|setEmbeddableTemplate
argument_list|(
name|TemplateType
operator|.
name|EMBEDDABLE_SINGLE_CLASS
operator|.
name|defaultTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|datamaptemplate
operator|==
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|setDataMapTemplate
argument_list|(
name|TemplateType
operator|.
name|DATAMAP_SINGLE_CLASS
operator|.
name|defaultTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cgenConfiguration
return|;
block|}
specifier|private
name|void
name|replaceDatamapGenerationMode
parameter_list|()
block|{
name|this
operator|.
name|mode
operator|=
name|ArtifactsGenerationMode
operator|.
name|ALL
operator|.
name|getLabel
argument_list|()
expr_stmt|;
name|this
operator|.
name|excludeEntitiesPattern
operator|=
literal|"*"
expr_stmt|;
name|this
operator|.
name|excludeEmbeddablesPattern
operator|=
literal|"*"
expr_stmt|;
name|this
operator|.
name|includeEntitiesPattern
operator|=
literal|""
expr_stmt|;
block|}
comment|/**      * Validates attributes that are not related to internal DefaultClassGenerator. Throws      * BuildException if attributes are invalid.      */
specifier|protected
name|void
name|validateAttributes
parameter_list|()
throws|throws
name|BuildException
block|{
if|if
condition|(
name|map
operator|==
literal|null
operator|&&
name|this
operator|.
name|getProject
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"either 'map' or 'project' is required."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Sets the map.      *       * @param map The map to set      */
specifier|public
name|void
name|setMap
parameter_list|(
name|File
name|map
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
comment|/**      * Sets the additional DataMaps.      *       * @param additionalMapsPath The additional DataMaps to set      */
specifier|public
name|void
name|setAdditionalMaps
parameter_list|(
name|Path
name|additionalMapsPath
parameter_list|)
block|{
name|String
index|[]
name|additionalMapFilenames
init|=
name|additionalMapsPath
operator|.
name|list
argument_list|()
decl_stmt|;
name|this
operator|.
name|additionalMaps
operator|=
operator|new
name|File
index|[
name|additionalMapFilenames
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|additionalMapFilenames
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|additionalMaps
index|[
name|i
index|]
operator|=
operator|new
name|File
argument_list|(
name|additionalMapFilenames
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sets the destDir.      */
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
comment|/**      * Sets<code>overwrite</code> property.      */
specifier|public
name|void
name|setOverwrite
parameter_list|(
name|boolean
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
comment|/**      * Sets<code>makepairs</code> property.      */
specifier|public
name|void
name|setMakepairs
parameter_list|(
name|boolean
name|makepairs
parameter_list|)
block|{
name|this
operator|.
name|makepairs
operator|=
name|makepairs
expr_stmt|;
block|}
comment|/**      * Sets<code>template</code> property.      */
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
comment|/**      * Sets<code>supertemplate</code> property.      */
specifier|public
name|void
name|setSupertemplate
parameter_list|(
name|String
name|supertemplate
parameter_list|)
block|{
name|this
operator|.
name|supertemplate
operator|=
name|supertemplate
expr_stmt|;
block|}
comment|/**      * Sets<code>datamaptemplate</code> property.      * @since 4.3 querytemplate renamed to datamaptemplate      */
specifier|public
name|void
name|setDataMapTemplate
parameter_list|(
name|String
name|datamaptemplate
parameter_list|)
block|{
name|this
operator|.
name|datamaptemplate
operator|=
name|datamaptemplate
expr_stmt|;
block|}
comment|/**      * Sets<code>datamapsupertemplate</code> property.      * @since 4.3 querysupertemplate renamed to datamapsupertemplate      */
specifier|public
name|void
name|setDataMapSupertemplate
parameter_list|(
name|String
name|datamapsupertemplate
parameter_list|)
block|{
name|this
operator|.
name|datamapsupertemplate
operator|=
name|datamapsupertemplate
expr_stmt|;
block|}
comment|/**      * Sets<code>usepkgpath</code> property.      */
specifier|public
name|void
name|setUsepkgpath
parameter_list|(
name|boolean
name|usepkgpath
parameter_list|)
block|{
name|this
operator|.
name|usepkgpath
operator|=
name|usepkgpath
expr_stmt|;
block|}
comment|/**      * Sets<code>superpkg</code> property.      */
specifier|public
name|void
name|setSuperpkg
parameter_list|(
name|String
name|superpkg
parameter_list|)
block|{
name|this
operator|.
name|superpkg
operator|=
name|superpkg
expr_stmt|;
block|}
comment|/**      * Sets<code>encoding</code> property that allows to generate files using non-default      * encoding.      */
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
comment|/**      * Sets<code>excludeEntitiesPattern</code> property.      */
specifier|public
name|void
name|setExcludeEntities
parameter_list|(
name|String
name|excludeEntitiesPattern
parameter_list|)
block|{
name|this
operator|.
name|excludeEntitiesPattern
operator|=
name|excludeEntitiesPattern
expr_stmt|;
block|}
comment|/**      * Sets<code>includeEntitiesPattern</code> property.      */
specifier|public
name|void
name|setIncludeEntities
parameter_list|(
name|String
name|includeEntitiesPattern
parameter_list|)
block|{
name|this
operator|.
name|includeEntitiesPattern
operator|=
name|includeEntitiesPattern
expr_stmt|;
block|}
comment|/**      * Sets<code>excludeEmbeddablesPattern</code> property.      */
specifier|public
name|void
name|setExcludeEmbeddablesPattern
parameter_list|(
name|String
name|excludeEmbeddablesPattern
parameter_list|)
block|{
name|this
operator|.
name|excludeEmbeddablesPattern
operator|=
name|excludeEmbeddablesPattern
expr_stmt|;
block|}
comment|/**      * Sets<code>outputPattern</code> property.      */
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
comment|/**      * Sets<code>mode</code> property.      */
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
comment|/**      * Sets<code>createpropertynames</code> property.      */
specifier|public
name|void
name|setCreatepropertynames
parameter_list|(
name|boolean
name|createpropertynames
parameter_list|)
block|{
name|this
operator|.
name|createpropertynames
operator|=
name|createpropertynames
expr_stmt|;
block|}
specifier|public
name|void
name|setEmbeddabletemplate
parameter_list|(
name|String
name|embeddabletemplate
parameter_list|)
block|{
name|this
operator|.
name|embeddabletemplate
operator|=
name|embeddabletemplate
expr_stmt|;
block|}
specifier|public
name|void
name|setEmbeddablesupertemplate
parameter_list|(
name|String
name|embeddablesupertemplate
parameter_list|)
block|{
name|this
operator|.
name|embeddablesupertemplate
operator|=
name|embeddablesupertemplate
expr_stmt|;
block|}
comment|/**      * @since 4.1      */
specifier|public
name|void
name|setCreatepkproperties
parameter_list|(
name|boolean
name|createpkproperties
parameter_list|)
block|{
name|this
operator|.
name|createpkproperties
operator|=
name|createpkproperties
expr_stmt|;
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
comment|/**      * @since 4.2      */
specifier|public
name|void
name|setExternaltoolconfig
parameter_list|(
name|String
name|externaltoolconfig
parameter_list|)
block|{
name|this
operator|.
name|externaltoolconfig
operator|=
name|externaltoolconfig
expr_stmt|;
block|}
block|}
end_class

end_unit

