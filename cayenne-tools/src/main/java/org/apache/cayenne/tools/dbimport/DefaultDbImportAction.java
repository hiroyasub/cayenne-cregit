begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
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
operator|.
name|dbimport
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
name|ConfigurationTree
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
name|DataNodeDescriptor
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
name|server
operator|.
name|DataSourceFactory
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
name|server
operator|.
name|DbAdapterFactory
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
name|dba
operator|.
name|DbAdapter
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
name|NameFilter
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
name|merge
operator|.
name|AbstractToModelToken
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
name|merge
operator|.
name|AddRelationshipToDb
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
name|merge
operator|.
name|DbMerger
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
name|merge
operator|.
name|MergerContext
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
name|merge
operator|.
name|MergerToken
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
name|merge
operator|.
name|ModelMergeDelegate
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
name|merge
operator|.
name|ProxyModelMergeDelegate
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
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
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
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactoryProvider
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
name|naming
operator|.
name|ObjectNameGenerator
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
name|db
operator|.
name|DbLoader
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
name|db
operator|.
name|DbLoaderConfiguration
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
name|filters
operator|.
name|CatalogFilter
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
name|filters
operator|.
name|FiltersConfig
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
name|filters
operator|.
name|PatternFilter
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
name|Inject
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
name|EntityResolver
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
name|MapLoader
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
name|ObjRelationship
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
name|Procedure
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
name|Project
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
name|ProjectSaver
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
name|URLResource
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
name|SimpleValidationFailure
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
name|ValidationFailure
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
name|ValidationResult
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
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|io
operator|.
name|FileNotFoundException
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
name|sql
operator|.
name|Connection
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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

begin_comment
comment|/**  * A default implementation of {@link DbImportAction} that can load DB schema and merge it to a new or an existing  * DataMap.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DefaultDbImportAction
implements|implements
name|DbImportAction
block|{
specifier|private
specifier|final
name|ProjectSaver
name|projectSaver
decl_stmt|;
specifier|private
specifier|final
name|Log
name|logger
decl_stmt|;
specifier|private
specifier|final
name|DataSourceFactory
name|dataSourceFactory
decl_stmt|;
specifier|private
specifier|final
name|DbAdapterFactory
name|adapterFactory
decl_stmt|;
specifier|private
specifier|final
name|MapLoader
name|mapLoader
decl_stmt|;
specifier|private
specifier|final
name|MergerTokenFactoryProvider
name|mergerTokenFactoryProvider
decl_stmt|;
specifier|public
name|DefaultDbImportAction
parameter_list|(
annotation|@
name|Inject
name|Log
name|logger
parameter_list|,
annotation|@
name|Inject
name|ProjectSaver
name|projectSaver
parameter_list|,
annotation|@
name|Inject
name|DataSourceFactory
name|dataSourceFactory
parameter_list|,
annotation|@
name|Inject
name|DbAdapterFactory
name|adapterFactory
parameter_list|,
annotation|@
name|Inject
name|MapLoader
name|mapLoader
parameter_list|,
annotation|@
name|Inject
name|MergerTokenFactoryProvider
name|mergerTokenFactoryProvider
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
name|this
operator|.
name|projectSaver
operator|=
name|projectSaver
expr_stmt|;
name|this
operator|.
name|dataSourceFactory
operator|=
name|dataSourceFactory
expr_stmt|;
name|this
operator|.
name|adapterFactory
operator|=
name|adapterFactory
expr_stmt|;
name|this
operator|.
name|mapLoader
operator|=
name|mapLoader
expr_stmt|;
name|this
operator|.
name|mergerTokenFactoryProvider
operator|=
name|mergerTokenFactoryProvider
expr_stmt|;
block|}
specifier|protected
specifier|static
name|List
argument_list|<
name|MergerToken
argument_list|>
name|sort
parameter_list|(
name|List
argument_list|<
name|MergerToken
argument_list|>
name|reverse
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|reverse
argument_list|,
operator|new
name|Comparator
argument_list|<
name|MergerToken
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|MergerToken
name|o1
parameter_list|,
name|MergerToken
name|o2
parameter_list|)
block|{
if|if
condition|(
name|o1
operator|instanceof
name|AddRelationshipToDb
operator|&&
name|o2
operator|instanceof
name|AddRelationshipToDb
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o1
operator|instanceof
name|AddRelationshipToDb
operator|||
name|o2
operator|instanceof
name|AddRelationshipToDb
operator|)
condition|)
block|{
return|return
name|o1
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
return|;
block|}
return|return
name|o1
operator|instanceof
name|AddRelationshipToDb
condition|?
literal|1
else|:
operator|-
literal|1
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|reverse
return|;
block|}
comment|/**      * Flattens many-to-many relationships in the generated model.      */
specifier|protected
specifier|static
name|void
name|flattenManyToManyRelationships
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|loadedObjEntities
parameter_list|,
name|ObjectNameGenerator
name|objectNameGenerator
parameter_list|)
block|{
if|if
condition|(
name|loadedObjEntities
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|entitiesForDelete
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjEntity
name|curEntity
range|:
name|loadedObjEntities
control|)
block|{
name|ManyToManyCandidateEntity
name|entity
init|=
name|ManyToManyCandidateEntity
operator|.
name|build
argument_list|(
name|curEntity
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|entity
operator|.
name|optimizeRelationships
argument_list|(
name|objectNameGenerator
argument_list|)
expr_stmt|;
name|entitiesForDelete
operator|.
name|add
argument_list|(
name|curEntity
argument_list|)
expr_stmt|;
block|}
block|}
comment|// remove needed entities
for|for
control|(
name|ObjEntity
name|curDeleteEntity
range|:
name|entitiesForDelete
control|)
block|{
name|map
operator|.
name|removeObjEntity
argument_list|(
name|curDeleteEntity
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|loadedObjEntities
operator|.
name|removeAll
argument_list|(
name|entitiesForDelete
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|DbImportConfiguration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"DB connection: "
operator|+
name|config
operator|.
name|getDataSourceInfo
argument_list|()
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
name|boolean
name|hasChanges
init|=
literal|false
decl_stmt|;
name|DataNodeDescriptor
name|dataNodeDescriptor
init|=
name|config
operator|.
name|createDataNodeDescriptor
argument_list|()
decl_stmt|;
name|DataSource
name|dataSource
init|=
name|dataSourceFactory
operator|.
name|getDataSource
argument_list|(
name|dataNodeDescriptor
argument_list|)
decl_stmt|;
name|DbAdapter
name|adapter
init|=
name|adapterFactory
operator|.
name|createAdapter
argument_list|(
name|dataNodeDescriptor
argument_list|,
name|dataSource
argument_list|)
decl_stmt|;
name|DataMap
name|sourceDataMap
decl_stmt|;
try|try
init|(
name|Connection
name|connection
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
init|)
block|{
name|sourceDataMap
operator|=
name|load
argument_list|(
name|config
argument_list|,
name|adapter
argument_list|,
name|connection
argument_list|)
expr_stmt|;
block|}
name|DataMap
name|targetDataMap
init|=
name|existingTargetMap
argument_list|(
name|config
argument_list|)
decl_stmt|;
if|if
condition|(
name|targetDataMap
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Map file does not exist. Loaded db model will be saved into '"
operator|+
operator|(
name|config
operator|.
name|getTargetDataMap
argument_list|()
operator|==
literal|null
condition|?
literal|"null"
else|:
name|config
operator|.
name|getTargetDataMap
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"'"
operator|)
argument_list|)
expr_stmt|;
name|hasChanges
operator|=
literal|true
expr_stmt|;
name|targetDataMap
operator|=
name|newTargetDataMap
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
name|MergerTokenFactory
name|mergerTokenFactory
init|=
name|mergerTokenFactoryProvider
operator|.
name|get
argument_list|(
name|adapter
argument_list|)
decl_stmt|;
name|DbLoaderConfiguration
name|loaderConfig
init|=
name|config
operator|.
name|getDbLoaderConfig
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
name|DbMerger
operator|.
name|builder
argument_list|(
name|mergerTokenFactory
argument_list|)
operator|.
name|filters
argument_list|(
name|loaderConfig
operator|.
name|getFiltersConfig
argument_list|()
argument_list|)
operator|.
name|skipPKTokens
argument_list|(
name|loaderConfig
operator|.
name|isSkipPrimaryKeyLoading
argument_list|()
argument_list|)
operator|.
name|skipRelationshipsTokens
argument_list|(
name|loaderConfig
operator|.
name|isSkipRelationshipsLoading
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|createMergeTokens
argument_list|(
name|targetDataMap
argument_list|,
name|sourceDataMap
argument_list|)
decl_stmt|;
name|hasChanges
operator||=
name|syncDataMapProperties
argument_list|(
name|targetDataMap
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|hasChanges
operator||=
name|applyTokens
argument_list|(
name|config
operator|.
name|createMergeDelegate
argument_list|()
argument_list|,
name|targetDataMap
argument_list|,
name|log
argument_list|(
name|sort
argument_list|(
name|reverse
argument_list|(
name|mergerTokenFactory
argument_list|,
name|tokens
argument_list|)
argument_list|)
argument_list|)
argument_list|,
name|config
operator|.
name|createNameGenerator
argument_list|()
argument_list|,
name|config
operator|.
name|createMeaningfulPKFilter
argument_list|()
argument_list|,
name|config
operator|.
name|isUsePrimitives
argument_list|()
argument_list|)
expr_stmt|;
name|hasChanges
operator||=
name|syncProcedures
argument_list|(
name|targetDataMap
argument_list|,
name|sourceDataMap
argument_list|,
name|loaderConfig
operator|.
name|getFiltersConfig
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasChanges
condition|)
block|{
name|saveLoaded
argument_list|(
name|targetDataMap
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|syncDataMapProperties
parameter_list|(
name|DataMap
name|targetDataMap
parameter_list|,
name|DbImportConfiguration
name|config
parameter_list|)
block|{
name|String
name|defaultPackage
init|=
name|config
operator|.
name|getDefaultPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|defaultPackage
operator|==
literal|null
operator|||
name|defaultPackage
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
return|return
literal|false
return|;
block|}
if|if
condition|(
name|defaultPackage
operator|.
name|equals
argument_list|(
name|targetDataMap
operator|.
name|getDefaultPackage
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|targetDataMap
operator|.
name|setDefaultPackage
argument_list|(
name|defaultPackage
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|relationshipsSanity
parameter_list|(
name|DataMap
name|executed
parameter_list|)
block|{
for|for
control|(
name|ObjEntity
name|objEntity
range|:
name|executed
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
name|List
argument_list|<
name|ObjRelationship
argument_list|>
name|rels
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|(
name|objEntity
operator|.
name|getRelationships
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjRelationship
name|rel
range|:
name|rels
control|)
block|{
if|if
condition|(
name|rel
operator|.
name|getSourceEntity
argument_list|()
operator|==
literal|null
operator|||
name|rel
operator|.
name|getTargetEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Incorrect obj relationship source or target entity is null: "
operator|+
name|rel
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|removeRelationship
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|log
parameter_list|(
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
name|tokens
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Detected changes: No changes to import."
argument_list|)
expr_stmt|;
return|return
name|tokens
return|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"Detected changes: "
argument_list|)
expr_stmt|;
for|for
control|(
name|MergerToken
name|token
range|:
name|tokens
control|)
block|{
name|logger
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"    %-20s %s"
argument_list|,
name|token
operator|.
name|getTokenName
argument_list|()
argument_list|,
name|token
operator|.
name|getTokenValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
return|return
name|tokens
return|;
block|}
specifier|protected
name|DataMap
name|existingTargetMap
parameter_list|(
name|DbImportConfiguration
name|configuration
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|file
init|=
name|configuration
operator|.
name|getTargetDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
operator|&&
name|file
operator|.
name|exists
argument_list|()
operator|&&
name|file
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|DataMap
name|dataMap
init|=
name|mapLoader
operator|.
name|loadDataMap
argument_list|(
operator|new
name|InputSource
argument_list|(
name|file
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|setNamespace
argument_list|(
operator|new
name|EntityResolver
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|dataMap
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setConfigurationSource
argument_list|(
operator|new
name|URLResource
argument_list|(
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|dataMap
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|DataMap
name|newTargetDataMap
parameter_list|(
name|DbImportConfiguration
name|config
parameter_list|)
throws|throws
name|IOException
block|{
name|DataMap
name|dataMap
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|dataMap
operator|.
name|setName
argument_list|(
name|config
operator|.
name|getDataMapName
argument_list|()
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setConfigurationSource
argument_list|(
operator|new
name|URLResource
argument_list|(
name|config
operator|.
name|getTargetDataMap
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setNamespace
argument_list|(
operator|new
name|EntityResolver
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|dataMap
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// update map defaults
comment|// do not override default package of existing DataMap unless it is
comment|// explicitly requested by the plugin caller
name|String
name|defaultPackage
init|=
name|config
operator|.
name|getDefaultPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|defaultPackage
operator|!=
literal|null
operator|&&
name|defaultPackage
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|dataMap
operator|.
name|setDefaultPackage
argument_list|(
name|defaultPackage
argument_list|)
expr_stmt|;
block|}
name|CatalogFilter
index|[]
name|catalogs
init|=
name|config
operator|.
name|getDbLoaderConfig
argument_list|()
operator|.
name|getFiltersConfig
argument_list|()
operator|.
name|getCatalogs
argument_list|()
decl_stmt|;
if|if
condition|(
name|catalogs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
comment|// do not override default catalog of existing DataMap unless it is
comment|// explicitly requested by the plugin caller, and the provided catalog is
comment|// not a pattern
name|String
name|catalog
init|=
name|catalogs
index|[
literal|0
index|]
operator|.
name|name
decl_stmt|;
if|if
condition|(
name|catalog
operator|!=
literal|null
operator|&&
name|catalog
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
name|catalog
operator|.
name|indexOf
argument_list|(
literal|'%'
argument_list|)
operator|<
literal|0
condition|)
block|{
name|dataMap
operator|.
name|setDefaultCatalog
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
block|}
comment|// do not override default schema of existing DataMap unless it is
comment|// explicitly requested by the plugin caller, and the provided schema is
comment|// not a pattern
name|String
name|schema
init|=
name|catalogs
index|[
literal|0
index|]
operator|.
name|schemas
index|[
literal|0
index|]
operator|.
name|name
decl_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
operator|&&
name|schema
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
name|schema
operator|.
name|indexOf
argument_list|(
literal|'%'
argument_list|)
operator|<
literal|0
condition|)
block|{
name|dataMap
operator|.
name|setDefaultSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|dataMap
return|;
block|}
specifier|private
name|List
argument_list|<
name|MergerToken
argument_list|>
name|reverse
parameter_list|(
name|MergerTokenFactory
name|mergerTokenFactory
parameter_list|,
name|Iterable
argument_list|<
name|MergerToken
argument_list|>
name|mergeTokens
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|MergerToken
name|token
range|:
name|mergeTokens
control|)
block|{
if|if
condition|(
name|token
operator|instanceof
name|AbstractToModelToken
condition|)
block|{
continue|continue;
block|}
name|tokens
operator|.
name|addAll
argument_list|(
name|token
operator|.
name|createReverse
argument_list|(
name|mergerTokenFactory
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|tokens
return|;
block|}
specifier|private
name|boolean
name|applyTokens
parameter_list|(
name|ModelMergeDelegate
name|mergeDelegate
parameter_list|,
name|DataMap
name|targetDataMap
parameter_list|,
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|,
name|ObjectNameGenerator
name|nameGenerator
parameter_list|,
name|NameFilter
name|meaningfulPKFilter
parameter_list|,
name|boolean
name|usingPrimitives
parameter_list|)
block|{
if|if
condition|(
name|tokens
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Detected changes: No changes to import."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
specifier|final
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|loadedObjEntities
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|mergeDelegate
operator|=
operator|new
name|ProxyModelMergeDelegate
argument_list|(
name|mergeDelegate
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|objEntityAdded
parameter_list|(
name|ObjEntity
name|ent
parameter_list|)
block|{
name|loadedObjEntities
operator|.
name|add
argument_list|(
name|ent
argument_list|)
expr_stmt|;
name|super
operator|.
name|objEntityAdded
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|MergerContext
name|mergerContext
init|=
name|MergerContext
operator|.
name|builder
argument_list|(
name|targetDataMap
argument_list|)
operator|.
name|delegate
argument_list|(
name|mergeDelegate
argument_list|)
operator|.
name|nameGenerator
argument_list|(
name|nameGenerator
argument_list|)
operator|.
name|usingPrimitives
argument_list|(
name|usingPrimitives
argument_list|)
operator|.
name|meaningfulPKFilter
argument_list|(
name|meaningfulPKFilter
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
for|for
control|(
name|MergerToken
name|token
range|:
name|tokens
control|)
block|{
try|try
block|{
name|token
operator|.
name|execute
argument_list|(
name|mergerContext
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|String
name|message
init|=
literal|"Migration Error. Can't apply changes from token: "
operator|+
name|token
operator|.
name|getTokenName
argument_list|()
operator|+
literal|" ("
operator|+
name|token
operator|.
name|getTokenValue
argument_list|()
operator|+
literal|")"
decl_stmt|;
name|logger
operator|.
name|error
argument_list|(
name|message
argument_list|,
name|th
argument_list|)
expr_stmt|;
name|mergerContext
operator|.
name|getValidationResult
argument_list|()
operator|.
name|addFailure
argument_list|(
operator|new
name|SimpleValidationFailure
argument_list|(
name|th
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|ValidationResult
name|failures
init|=
name|mergerContext
operator|.
name|getValidationResult
argument_list|()
decl_stmt|;
if|if
condition|(
name|failures
operator|.
name|hasFailures
argument_list|()
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Migration Complete."
argument_list|)
expr_stmt|;
name|logger
operator|.
name|warn
argument_list|(
literal|"Migration finished. The following problem(s) were encountered and ignored."
argument_list|)
expr_stmt|;
for|for
control|(
name|ValidationFailure
name|failure
range|:
name|failures
operator|.
name|getFailures
argument_list|()
control|)
block|{
name|logger
operator|.
name|warn
argument_list|(
name|failure
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Migration Complete Successfully."
argument_list|)
expr_stmt|;
block|}
name|flattenManyToManyRelationships
argument_list|(
name|targetDataMap
argument_list|,
name|loadedObjEntities
argument_list|,
name|nameGenerator
argument_list|)
expr_stmt|;
name|relationshipsSanity
argument_list|(
name|targetDataMap
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|syncProcedures
parameter_list|(
name|DataMap
name|targetDataMap
parameter_list|,
name|DataMap
name|loadedDataMap
parameter_list|,
name|FiltersConfig
name|filters
parameter_list|)
block|{
name|Collection
argument_list|<
name|Procedure
argument_list|>
name|procedures
init|=
name|loadedDataMap
operator|.
name|getProcedures
argument_list|()
decl_stmt|;
if|if
condition|(
name|procedures
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|hasChanges
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Procedure
name|procedure
range|:
name|procedures
control|)
block|{
name|PatternFilter
name|proceduresFilter
init|=
name|filters
operator|.
name|proceduresFilter
argument_list|(
name|procedure
operator|.
name|getCatalog
argument_list|()
argument_list|,
name|procedure
operator|.
name|getSchema
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|proceduresFilter
operator|==
literal|null
operator|||
operator|!
name|proceduresFilter
operator|.
name|isIncluded
argument_list|(
name|procedure
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Procedure
name|oldProcedure
init|=
name|targetDataMap
operator|.
name|getProcedure
argument_list|(
name|procedure
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// maybe we need to compare oldProcedure's and procedure's fully qualified names?
if|if
condition|(
name|oldProcedure
operator|!=
literal|null
condition|)
block|{
name|targetDataMap
operator|.
name|removeProcedure
argument_list|(
name|procedure
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Replace procedure "
operator|+
name|procedure
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Add new procedure "
operator|+
name|procedure
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|targetDataMap
operator|.
name|addProcedure
argument_list|(
name|procedure
argument_list|)
expr_stmt|;
name|hasChanges
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|hasChanges
return|;
block|}
specifier|protected
name|void
name|saveLoaded
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
name|ConfigurationTree
argument_list|<
name|DataMap
argument_list|>
name|projectRoot
init|=
operator|new
name|ConfigurationTree
argument_list|<>
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
name|Project
name|project
init|=
operator|new
name|Project
argument_list|(
name|projectRoot
argument_list|)
decl_stmt|;
name|projectSaver
operator|.
name|save
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|DataMap
name|load
parameter_list|(
name|DbImportConfiguration
name|config
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|Connection
name|connection
parameter_list|)
throws|throws
name|Exception
block|{
name|DataMap
name|dataMap
init|=
operator|new
name|DataMap
argument_list|(
literal|"_import_source_"
argument_list|)
decl_stmt|;
name|DbLoader
name|loader
init|=
name|config
operator|.
name|createLoader
argument_list|(
name|adapter
argument_list|,
name|connection
argument_list|,
name|config
operator|.
name|createLoaderDelegate
argument_list|()
argument_list|)
decl_stmt|;
name|loader
operator|.
name|load
argument_list|(
name|dataMap
argument_list|,
name|config
operator|.
name|getDbLoaderConfig
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|dataMap
return|;
block|}
block|}
end_class

end_unit

