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
name|conn
operator|.
name|DataSourceInfo
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
name|merge
operator|.
name|DbMergerConfig
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
name|DefaultModelMergeDelegate
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
name|EntityMergeSupport
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
name|db
operator|.
name|DbLoaderDelegate
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
name|DefaultDbLoaderDelegate
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
name|LoggingDbLoaderDelegate
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
name|reverse
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
name|DbEntity
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
name|naming
operator|.
name|LegacyNameGenerator
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
name|commons
operator|.
name|logging
operator|.
name|Log
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|Collections
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
operator|.
name|isNotEmpty
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DbImportConfiguration
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DATA_MAP_LOCATION_SUFFIX
init|=
literal|".map.xml"
decl_stmt|;
specifier|private
specifier|final
name|DataSourceInfo
name|dataSourceInfo
init|=
operator|new
name|DataSourceInfo
argument_list|()
decl_stmt|;
comment|/**      * DB schema to use for DB importing.      */
specifier|private
specifier|final
name|DbLoaderConfiguration
name|dbLoaderConfiguration
init|=
operator|new
name|DbLoaderConfiguration
argument_list|()
decl_stmt|;
comment|/**      * DataMap XML file to use as a base for DB importing.      */
specifier|private
name|File
name|dataMapFile
decl_stmt|;
comment|/**      * A default package for ObjEntity Java classes.      */
specifier|private
name|String
name|defaultPackage
decl_stmt|;
comment|/**      * Indicates that the old mapping should be completely removed and replaced      * with the new data based on reverse engineering.      */
specifier|private
name|boolean
name|overwrite
decl_stmt|;
specifier|private
name|String
name|meaningfulPkTables
decl_stmt|;
comment|/**      * Java class implementing org.apache.cayenne.dba.DbAdapter. This attribute      * is optional, the default is AutoAdapter, i.e. Cayenne would try to guess      * the DB type.      */
specifier|private
name|String
name|adapter
decl_stmt|;
specifier|private
name|boolean
name|usePrimitives
decl_stmt|;
specifier|private
name|Log
name|logger
decl_stmt|;
specifier|public
name|Log
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
specifier|public
name|void
name|setLogger
parameter_list|(
name|Log
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
specifier|public
name|File
name|getDataMapFile
parameter_list|()
block|{
return|return
name|dataMapFile
return|;
block|}
specifier|public
name|void
name|setDataMapFile
parameter_list|(
name|File
name|map
parameter_list|)
block|{
name|this
operator|.
name|dataMapFile
operator|=
name|map
expr_stmt|;
block|}
specifier|public
name|String
name|getDefaultPackage
parameter_list|()
block|{
return|return
name|defaultPackage
return|;
block|}
specifier|public
name|void
name|setDefaultPackage
parameter_list|(
name|String
name|defaultPackage
parameter_list|)
block|{
name|this
operator|.
name|defaultPackage
operator|=
name|defaultPackage
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
name|String
name|getNamingStrategy
parameter_list|()
block|{
return|return
name|dbLoaderConfiguration
operator|.
name|getNamingStrategy
argument_list|()
return|;
block|}
specifier|public
name|void
name|setNamingStrategy
parameter_list|(
name|String
name|namingStrategy
parameter_list|)
block|{
name|dbLoaderConfiguration
operator|.
name|setNamingStrategy
argument_list|(
name|namingStrategy
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getAdapter
parameter_list|()
block|{
return|return
name|adapter
return|;
block|}
specifier|public
name|void
name|setAdapter
parameter_list|(
name|String
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
block|}
comment|/**      * Returns a comma-separated list of Perl5 regular expressions that match      * table names for which {@link DbImportAction} should include ObjAttribute      * for PK.      */
specifier|public
name|String
name|getMeaningfulPkTables
parameter_list|()
block|{
return|return
name|meaningfulPkTables
return|;
block|}
specifier|public
name|void
name|setMeaningfulPkTables
parameter_list|(
name|String
name|meaningfulPkTables
parameter_list|)
block|{
name|this
operator|.
name|meaningfulPkTables
operator|=
name|meaningfulPkTables
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUsePrimitives
parameter_list|()
block|{
return|return
name|usePrimitives
return|;
block|}
specifier|public
name|void
name|setUsePrimitives
parameter_list|(
name|boolean
name|usePrimitives
parameter_list|)
block|{
name|this
operator|.
name|usePrimitives
operator|=
name|usePrimitives
expr_stmt|;
block|}
specifier|public
name|DbLoader
name|createLoader
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|Connection
name|connection
parameter_list|,
name|DbLoaderDelegate
name|loaderDelegate
parameter_list|)
throws|throws
name|InstantiationException
throws|,
name|IllegalAccessException
throws|,
name|ClassNotFoundException
block|{
specifier|final
name|NameFilter
name|meaningfulPkFilter
init|=
name|NamePatternMatcher
operator|.
name|build
argument_list|(
name|logger
argument_list|,
name|getMeaningfulPkTables
argument_list|()
argument_list|,
name|getMeaningfulPkTables
argument_list|()
operator|!=
literal|null
condition|?
literal|null
else|:
literal|"*"
argument_list|)
decl_stmt|;
name|DbLoader
name|loader
init|=
operator|new
name|DbLoader
argument_list|(
name|connection
argument_list|,
name|adapter
argument_list|,
name|loaderDelegate
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|EntityMergeSupport
name|createEntityMerger
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|EntityMergeSupport
name|emSupport
init|=
operator|new
name|EntityMergeSupport
argument_list|(
name|map
argument_list|,
name|DbImportConfiguration
operator|.
name|this
operator|.
name|getNameGenerator
argument_list|()
argument_list|,
literal|true
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|boolean
name|removePK
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|)
block|{
return|return
operator|!
name|meaningfulPkFilter
operator|.
name|isIncluded
argument_list|(
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|emSupport
operator|.
name|setUsePrimitives
argument_list|(
name|DbImportConfiguration
operator|.
name|this
operator|.
name|isUsePrimitives
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|emSupport
return|;
block|}
block|}
decl_stmt|;
comment|// TODO: load via DI AdhocObjectFactory
name|loader
operator|.
name|setNameGenerator
argument_list|(
name|getNameGenerator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|loader
return|;
block|}
specifier|public
name|ObjectNameGenerator
name|getNameGenerator
parameter_list|()
block|{
name|String
name|namingStrategy
init|=
name|getNamingStrategy
argument_list|()
decl_stmt|;
if|if
condition|(
name|namingStrategy
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
operator|(
name|ObjectNameGenerator
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|namingStrategy
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error creating name generator"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
operator|new
name|LegacyNameGenerator
argument_list|()
return|;
comment|// TODO
block|}
specifier|public
name|String
name|getDriver
parameter_list|()
block|{
return|return
name|dataSourceInfo
operator|.
name|getJdbcDriver
argument_list|()
return|;
block|}
specifier|public
name|void
name|setDriver
parameter_list|(
name|String
name|jdbcDriver
parameter_list|)
block|{
name|dataSourceInfo
operator|.
name|setJdbcDriver
argument_list|(
name|jdbcDriver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|dataSourceInfo
operator|.
name|getPassword
argument_list|()
return|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|dataSourceInfo
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|dataSourceInfo
operator|.
name|getUserName
argument_list|()
return|;
block|}
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|dataSourceInfo
operator|.
name|setUserName
argument_list|(
name|userName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|dataSourceInfo
operator|.
name|getDataSourceUrl
argument_list|()
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|dataSourceUrl
parameter_list|)
block|{
name|dataSourceInfo
operator|.
name|setDataSourceUrl
argument_list|(
name|dataSourceUrl
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DataNodeDescriptor
name|createDataNodeDescriptor
parameter_list|()
block|{
name|DataNodeDescriptor
name|nodeDescriptor
init|=
operator|new
name|DataNodeDescriptor
argument_list|()
decl_stmt|;
name|nodeDescriptor
operator|.
name|setAdapterType
argument_list|(
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|nodeDescriptor
operator|.
name|setDataSourceDescriptor
argument_list|(
name|dataSourceInfo
argument_list|)
expr_stmt|;
return|return
name|nodeDescriptor
return|;
block|}
specifier|public
name|DataMap
name|createDataMap
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|dataMapFile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null DataMap File."
argument_list|)
throw|;
block|}
return|return
name|initializeDataMap
argument_list|(
operator|new
name|DataMap
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|DataMap
name|initializeDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|dataMap
operator|.
name|setName
argument_list|(
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
name|dataMapFile
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
name|getDefaultPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|isNotEmpty
argument_list|(
name|defaultPackage
argument_list|)
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
name|dbLoaderConfiguration
operator|.
name|getFiltersConfig
argument_list|()
operator|.
name|catalogs
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
name|isNotEmpty
argument_list|(
name|catalog
argument_list|)
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
name|isNotEmpty
argument_list|(
name|schema
argument_list|)
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
specifier|public
name|String
name|getDataMapName
parameter_list|()
block|{
name|String
name|name
init|=
name|dataMapFile
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|name
operator|.
name|endsWith
argument_list|(
name|DATA_MAP_LOCATION_SUFFIX
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"DataMap file name must end with '%s': '%s'"
argument_list|,
name|DATA_MAP_LOCATION_SUFFIX
argument_list|,
name|name
argument_list|)
throw|;
block|}
return|return
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
name|DATA_MAP_LOCATION_SUFFIX
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|ModelMergeDelegate
name|createMergeDelegate
parameter_list|()
block|{
return|return
operator|new
name|DefaultModelMergeDelegate
argument_list|()
return|;
block|}
specifier|public
name|DbLoaderDelegate
name|createLoaderDelegate
parameter_list|()
block|{
if|if
condition|(
name|getLogger
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|LoggingDbLoaderDelegate
argument_list|(
name|getLogger
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|DefaultDbLoaderDelegate
argument_list|()
return|;
block|}
block|}
specifier|public
name|DbLoaderConfiguration
name|getDbLoaderConfig
parameter_list|()
block|{
return|return
name|dbLoaderConfiguration
return|;
block|}
specifier|public
name|void
name|setFiltersConfig
parameter_list|(
name|FiltersConfig
name|filtersConfig
parameter_list|)
block|{
name|dbLoaderConfiguration
operator|.
name|setFiltersConfig
argument_list|(
name|filtersConfig
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|res
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Importer options:"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|dbLoaderConfiguration
operator|.
name|toString
argument_list|()
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
control|)
block|{
name|res
operator|.
name|append
argument_list|(
literal|"    "
argument_list|)
operator|.
name|append
argument_list|(
name|line
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
return|return
name|res
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|DataSourceInfo
name|getDataSourceInfo
parameter_list|()
block|{
return|return
name|dataSourceInfo
return|;
block|}
specifier|public
name|void
name|setSkipRelationshipsLoading
parameter_list|(
name|Boolean
name|skipRelationshipsLoading
parameter_list|)
block|{
name|this
operator|.
name|dbLoaderConfiguration
operator|.
name|setSkipRelationshipsLoading
argument_list|(
name|skipRelationshipsLoading
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSkipPrimaryKeyLoading
parameter_list|(
name|Boolean
name|skipPrimaryKeyLoading
parameter_list|)
block|{
name|this
operator|.
name|dbLoaderConfiguration
operator|.
name|setSkipPrimaryKeyLoading
argument_list|(
name|skipPrimaryKeyLoading
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setTableTypes
parameter_list|(
name|String
index|[]
name|tableTypes
parameter_list|)
block|{
name|dbLoaderConfiguration
operator|.
name|setTableTypes
argument_list|(
name|tableTypes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DbMergerConfig
name|getDbMergerConfig
parameter_list|()
block|{
return|return
operator|new
name|DbMergerConfig
argument_list|(
name|getDbLoaderConfig
argument_list|()
operator|.
name|getFiltersConfig
argument_list|()
argument_list|,
name|getDbLoaderConfig
argument_list|()
operator|.
name|getSkipRelationshipsLoading
argument_list|()
argument_list|,
name|getDbLoaderConfig
argument_list|()
operator|.
name|getSkipPrimaryKeyLoading
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

