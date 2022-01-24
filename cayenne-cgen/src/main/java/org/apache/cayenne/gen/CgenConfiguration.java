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
name|Serializable
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
name|*
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
name|configuration
operator|.
name|ConfigurationNodeVisitor
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
name|xml
operator|.
name|CgenExtension
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
name|util
operator|.
name|XMLEncoder
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
name|util
operator|.
name|XMLSerializable
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
name|validation
operator|.
name|ValidationException
import|;
end_import

begin_comment
comment|/**  * Used to keep config of class generation action.  * Previously was the part of ClassGeneretionAction class.  * Now CgenConfiguration is saved in dataMap file.  * You can reuse it in next cgen actions.  *  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|CgenConfiguration
implements|implements
name|Serializable
implements|,
name|XMLSerializable
block|{
specifier|private
name|Collection
argument_list|<
name|Artifact
argument_list|>
name|artifacts
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|entityArtifacts
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|excludeEntityArtifacts
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|embeddableArtifacts
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|excludeEmbeddableArtifacts
decl_stmt|;
specifier|private
name|String
name|superPkg
decl_stmt|;
specifier|private
name|DataMap
name|dataMap
decl_stmt|;
specifier|private
name|ArtifactsGenerationMode
name|artifactsGenerationMode
decl_stmt|;
specifier|private
name|boolean
name|makePairs
decl_stmt|;
specifier|private
name|Path
name|rootPath
decl_stmt|;
specifier|private
name|Path
name|relPath
decl_stmt|;
specifier|private
name|boolean
name|overwrite
decl_stmt|;
specifier|private
name|boolean
name|usePkgPath
decl_stmt|;
specifier|private
name|String
name|template
decl_stmt|;
specifier|private
name|String
name|superTemplate
decl_stmt|;
specifier|private
name|String
name|embeddableTemplate
decl_stmt|;
specifier|private
name|String
name|embeddableSuperTemplate
decl_stmt|;
specifier|private
name|String
name|queryTemplate
decl_stmt|;
specifier|private
name|String
name|querySuperTemplate
decl_stmt|;
specifier|private
name|long
name|timestamp
decl_stmt|;
specifier|private
name|String
name|outputPattern
decl_stmt|;
specifier|private
name|String
name|encoding
decl_stmt|;
specifier|private
name|boolean
name|createPropertyNames
decl_stmt|;
specifier|private
name|boolean
name|force
decl_stmt|;
comment|// force run generator
comment|/**      * @since 4.1      */
specifier|private
name|boolean
name|createPKProperties
decl_stmt|;
specifier|private
name|boolean
name|client
decl_stmt|;
comment|/**      * @since 4.2      */
specifier|private
name|String
name|externalToolConfig
decl_stmt|;
specifier|public
name|CgenConfiguration
parameter_list|(
name|boolean
name|client
parameter_list|)
block|{
comment|/**          * {@link #isDefault()} method should be in sync with the following values          */
name|this
operator|.
name|outputPattern
operator|=
literal|"*.java"
expr_stmt|;
name|this
operator|.
name|timestamp
operator|=
literal|0L
expr_stmt|;
name|this
operator|.
name|usePkgPath
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|makePairs
operator|=
literal|true
expr_stmt|;
name|setArtifactsGenerationMode
argument_list|(
literal|"entity"
argument_list|)
expr_stmt|;
name|this
operator|.
name|artifacts
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|entityArtifacts
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|excludeEntityArtifacts
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|embeddableArtifacts
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|excludeEmbeddableArtifacts
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|artifactsGenerationMode
operator|=
name|ArtifactsGenerationMode
operator|.
name|ENTITY
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
if|if
condition|(
operator|!
name|client
condition|)
block|{
name|this
operator|.
name|template
operator|=
name|ClassGenerationAction
operator|.
name|SUBCLASS_TEMPLATE
expr_stmt|;
name|this
operator|.
name|superTemplate
operator|=
name|ClassGenerationAction
operator|.
name|SUPERCLASS_TEMPLATE
expr_stmt|;
name|this
operator|.
name|queryTemplate
operator|=
name|ClassGenerationAction
operator|.
name|DATAMAP_SUBCLASS_TEMPLATE
expr_stmt|;
name|this
operator|.
name|querySuperTemplate
operator|=
name|ClassGenerationAction
operator|.
name|DATAMAP_SUPERCLASS_TEMPLATE
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|template
operator|=
name|ClientClassGenerationAction
operator|.
name|SUBCLASS_TEMPLATE
expr_stmt|;
name|this
operator|.
name|superTemplate
operator|=
name|ClientClassGenerationAction
operator|.
name|SUPERCLASS_TEMPLATE
expr_stmt|;
name|this
operator|.
name|queryTemplate
operator|=
name|ClientClassGenerationAction
operator|.
name|DMAP_SUBCLASS_TEMPLATE
expr_stmt|;
name|this
operator|.
name|querySuperTemplate
operator|=
name|ClientClassGenerationAction
operator|.
name|DMAP_SUPERCLASS_TEMPLATE
expr_stmt|;
block|}
name|this
operator|.
name|embeddableTemplate
operator|=
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SUBCLASS_TEMPLATE
expr_stmt|;
name|this
operator|.
name|embeddableSuperTemplate
operator|=
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SUPERCLASS_TEMPLATE
expr_stmt|;
block|}
specifier|public
name|void
name|resetCollections
parameter_list|()
block|{
name|embeddableArtifacts
operator|.
name|clear
argument_list|()
expr_stmt|;
name|entityArtifacts
operator|.
name|clear
argument_list|()
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
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
specifier|public
name|void
name|setDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
block|}
specifier|public
name|void
name|setArtifactsGenerationMode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
if|if
condition|(
name|ArtifactsGenerationMode
operator|.
name|ENTITY
operator|.
name|getLabel
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|mode
argument_list|)
condition|)
block|{
name|this
operator|.
name|artifactsGenerationMode
operator|=
name|ArtifactsGenerationMode
operator|.
name|ENTITY
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|artifactsGenerationMode
operator|=
name|ArtifactsGenerationMode
operator|.
name|ALL
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getArtifactsGenerationMode
parameter_list|()
block|{
return|return
name|artifactsGenerationMode
operator|.
name|getLabel
argument_list|()
return|;
block|}
specifier|public
name|boolean
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
name|boolean
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
name|Path
name|getRootPath
parameter_list|()
block|{
return|return
name|rootPath
return|;
block|}
specifier|public
name|void
name|setRootPath
parameter_list|(
name|Path
name|rootPath
parameter_list|)
block|{
name|this
operator|.
name|rootPath
operator|=
name|rootPath
expr_stmt|;
block|}
specifier|public
name|void
name|setRelPath
parameter_list|(
name|Path
name|relPath
parameter_list|)
block|{
name|this
operator|.
name|relPath
operator|=
name|relPath
expr_stmt|;
block|}
specifier|public
name|void
name|setRelPath
parameter_list|(
name|String
name|pathStr
parameter_list|)
block|{
name|Path
name|path
init|=
name|Paths
operator|.
name|get
argument_list|(
name|pathStr
argument_list|)
decl_stmt|;
if|if
condition|(
name|rootPath
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|rootPath
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Root path : "
operator|+
literal|'"'
operator|+
name|rootPath
operator|.
name|toString
argument_list|()
operator|+
literal|'"'
operator|+
literal|"should be absolute"
argument_list|)
throw|;
block|}
if|if
condition|(
name|path
operator|.
name|isAbsolute
argument_list|()
operator|&&
name|rootPath
operator|.
name|getRoot
argument_list|()
operator|.
name|equals
argument_list|(
name|path
operator|.
name|getRoot
argument_list|()
argument_list|)
condition|)
block|{
name|this
operator|.
name|relPath
operator|=
name|rootPath
operator|.
name|relativize
argument_list|(
name|path
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|this
operator|.
name|relPath
operator|=
name|path
expr_stmt|;
block|}
specifier|public
name|boolean
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
specifier|public
name|boolean
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
name|boolean
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
name|String
name|getQueryTemplate
parameter_list|()
block|{
return|return
name|queryTemplate
return|;
block|}
specifier|public
name|void
name|setQueryTemplate
parameter_list|(
name|String
name|queryTemplate
parameter_list|)
block|{
name|this
operator|.
name|queryTemplate
operator|=
name|queryTemplate
expr_stmt|;
block|}
specifier|public
name|String
name|getQuerySuperTemplate
parameter_list|()
block|{
return|return
name|querySuperTemplate
return|;
block|}
specifier|public
name|void
name|setQuerySuperTemplate
parameter_list|(
name|String
name|querySuperTemplate
parameter_list|)
block|{
name|this
operator|.
name|querySuperTemplate
operator|=
name|querySuperTemplate
expr_stmt|;
block|}
specifier|public
name|long
name|getTimestamp
parameter_list|()
block|{
return|return
name|timestamp
return|;
block|}
specifier|public
name|void
name|setTimestamp
parameter_list|(
name|long
name|timestamp
parameter_list|)
block|{
name|this
operator|.
name|timestamp
operator|=
name|timestamp
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
name|boolean
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
name|boolean
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
name|boolean
name|isCreatePKProperties
parameter_list|()
block|{
return|return
name|createPKProperties
return|;
block|}
specifier|public
name|void
name|setCreatePKProperties
parameter_list|(
name|boolean
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
name|Path
name|getRelPath
parameter_list|()
block|{
return|return
name|relPath
return|;
block|}
specifier|public
name|String
name|buildRelPath
parameter_list|()
block|{
if|if
condition|(
name|relPath
operator|==
literal|null
operator|||
name|relPath
operator|.
name|toString
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|"."
return|;
block|}
return|return
name|relPath
operator|.
name|toString
argument_list|()
return|;
block|}
name|Collection
argument_list|<
name|Artifact
argument_list|>
name|getArtifacts
parameter_list|()
block|{
return|return
name|artifacts
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getEntities
parameter_list|()
block|{
return|return
name|entityArtifacts
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getEmbeddables
parameter_list|()
block|{
return|return
name|embeddableArtifacts
return|;
block|}
specifier|public
name|boolean
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
name|boolean
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
name|String
name|getExternalToolConfig
parameter_list|()
block|{
return|return
name|externalToolConfig
return|;
block|}
specifier|public
name|void
name|setExternalToolConfig
parameter_list|(
name|String
name|config
parameter_list|)
block|{
name|this
operator|.
name|externalToolConfig
operator|=
name|config
expr_stmt|;
block|}
name|void
name|addArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Path
name|buildPath
parameter_list|()
block|{
return|return
name|rootPath
operator|!=
literal|null
condition|?
name|relPath
operator|!=
literal|null
condition|?
name|rootPath
operator|.
name|resolve
argument_list|(
name|relPath
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|normalize
argument_list|()
else|:
name|rootPath
else|:
name|relPath
return|;
block|}
specifier|public
name|void
name|loadEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
if|if
condition|(
operator|!
name|entity
operator|.
name|isGeneric
argument_list|()
condition|)
block|{
name|entityArtifacts
operator|.
name|add
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|loadEmbeddable
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|embeddableArtifacts
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|loadEntities
parameter_list|(
name|String
name|entities
parameter_list|)
block|{
name|excludeEntityArtifacts
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|entities
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getExcludeEntites
parameter_list|()
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|excludeEntities
init|=
name|dataMap
operator|.
name|getObjEntities
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|ObjEntity
operator|::
name|getName
argument_list|)
operator|.
name|filter
argument_list|(
name|name
lambda|->
operator|!
name|entityArtifacts
operator|.
name|contains
argument_list|(
name|name
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
decl_stmt|;
return|return
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|excludeEntities
argument_list|)
return|;
block|}
specifier|public
name|void
name|loadEmbeddables
parameter_list|(
name|String
name|embeddables
parameter_list|)
block|{
name|excludeEmbeddableArtifacts
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|embeddables
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getExcludeEmbeddables
parameter_list|()
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|excludeEmbeddable
init|=
name|dataMap
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Embeddable
operator|::
name|getClassName
argument_list|)
operator|.
name|filter
argument_list|(
name|className
lambda|->
operator|!
name|embeddableArtifacts
operator|.
name|contains
argument_list|(
name|className
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
decl_stmt|;
return|return
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|excludeEmbeddable
argument_list|)
return|;
block|}
specifier|public
name|void
name|resolveExcludeEntities
parameter_list|()
block|{
name|entityArtifacts
operator|=
name|dataMap
operator|.
name|getObjEntities
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|ObjEntity
operator|::
name|getName
argument_list|)
operator|.
name|filter
argument_list|(
name|name
lambda|->
operator|!
name|excludeEntityArtifacts
operator|.
name|contains
argument_list|(
name|name
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|resolveExcludeEmbeddables
parameter_list|()
block|{
name|embeddableArtifacts
operator|=
name|dataMap
operator|.
name|getEmbeddables
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Embeddable
operator|::
name|getClassName
argument_list|)
operator|.
name|filter
argument_list|(
name|className
lambda|->
operator|!
name|excludeEmbeddableArtifacts
operator|.
name|contains
argument_list|(
name|className
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getExcludeEntityArtifacts
parameter_list|()
block|{
return|return
name|excludeEntityArtifacts
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getExcludeEmbeddableArtifacts
parameter_list|()
block|{
return|return
name|excludeEmbeddableArtifacts
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|,
name|ConfigurationNodeVisitor
name|delegate
parameter_list|)
block|{
name|encoder
operator|.
name|start
argument_list|(
literal|"cgen"
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"xmlns"
argument_list|,
name|CgenExtension
operator|.
name|NAMESPACE
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"excludeEntities"
argument_list|,
name|getExcludeEntites
argument_list|()
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"excludeEmbeddables"
argument_list|,
name|getExcludeEmbeddables
argument_list|()
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"destDir"
argument_list|,
name|separatorsToUnix
argument_list|(
name|buildRelPath
argument_list|()
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"mode"
argument_list|,
name|this
operator|.
name|artifactsGenerationMode
operator|.
name|getLabel
argument_list|()
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"template"
argument_list|,
name|separatorsToUnix
argument_list|(
name|this
operator|.
name|template
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"superTemplate"
argument_list|,
name|separatorsToUnix
argument_list|(
name|this
operator|.
name|superTemplate
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"embeddableTemplate"
argument_list|,
name|separatorsToUnix
argument_list|(
name|this
operator|.
name|embeddableTemplate
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"embeddableSuperTemplate"
argument_list|,
name|separatorsToUnix
argument_list|(
name|this
operator|.
name|embeddableSuperTemplate
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"queryTemplate"
argument_list|,
name|separatorsToUnix
argument_list|(
name|this
operator|.
name|queryTemplate
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"querySuperTemplate"
argument_list|,
name|separatorsToUnix
argument_list|(
name|this
operator|.
name|querySuperTemplate
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"outputPattern"
argument_list|,
name|this
operator|.
name|outputPattern
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"makePairs"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|this
operator|.
name|makePairs
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"usePkgPath"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|this
operator|.
name|usePkgPath
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"overwrite"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|this
operator|.
name|overwrite
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"createPropertyNames"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|this
operator|.
name|createPropertyNames
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"superPkg"
argument_list|,
name|separatorsToUnix
argument_list|(
name|this
operator|.
name|superPkg
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"createPKProperties"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|this
operator|.
name|createPKProperties
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"client"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|client
argument_list|)
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"externalToolConfig"
argument_list|,
name|this
operator|.
name|externalToolConfig
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
comment|/**      * @return is this configuration with all values set to the default      */
specifier|public
name|boolean
name|isDefault
parameter_list|()
block|{
comment|// this must be is sync with actual default values
return|return
name|isMakePairs
argument_list|()
operator|&&
name|usePkgPath
operator|&&
operator|!
name|overwrite
operator|&&
operator|!
name|createPKProperties
operator|&&
operator|!
name|createPropertyNames
operator|&&
literal|"*.java"
operator|.
name|equals
argument_list|(
name|outputPattern
argument_list|)
operator|&&
operator|(
name|template
operator|.
name|equals
argument_list|(
name|ClassGenerationAction
operator|.
name|SUBCLASS_TEMPLATE
argument_list|)
operator|||
name|template
operator|.
name|equals
argument_list|(
name|ClientClassGenerationAction
operator|.
name|SUBCLASS_TEMPLATE
argument_list|)
operator|)
operator|&&
operator|(
name|superTemplate
operator|.
name|equals
argument_list|(
name|ClassGenerationAction
operator|.
name|SUPERCLASS_TEMPLATE
argument_list|)
operator|||
name|superTemplate
operator|.
name|equals
argument_list|(
name|ClientClassGenerationAction
operator|.
name|SUPERCLASS_TEMPLATE
argument_list|)
operator|)
operator|&&
operator|(
name|superPkg
operator|==
literal|null
operator|||
name|superPkg
operator|.
name|isEmpty
argument_list|()
operator|)
operator|&&
operator|(
name|externalToolConfig
operator|==
literal|null
operator|||
name|externalToolConfig
operator|.
name|isEmpty
argument_list|()
operator|)
return|;
block|}
specifier|private
name|String
name|separatorsToUnix
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
return|return
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

