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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|ConfigurationNameMapper
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
name|XMLDataMapLoader
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
name|dbimport
operator|.
name|Catalog
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
name|dbimport
operator|.
name|DefaultReverseEngineeringLoader
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
name|dbimport
operator|.
name|ExcludeColumn
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
name|dbimport
operator|.
name|ExcludeProcedure
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
name|dbimport
operator|.
name|ExcludeTable
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
name|dbimport
operator|.
name|IncludeColumn
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
name|dbimport
operator|.
name|IncludeProcedure
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
name|dbimport
operator|.
name|IncludeTable
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
name|dbimport
operator|.
name|ReverseEngineering
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
name|dbimport
operator|.
name|Schema
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
name|CayenneDbSyncModule
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
name|FiltersConfigBuilder
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
name|dbsync
operator|.
name|reverse
operator|.
name|naming
operator|.
name|DefaultObjectNameGenerator
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
name|tools
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
name|tools
operator|.
name|dbimport
operator|.
name|DbImportAction
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
name|tools
operator|.
name|dbimport
operator|.
name|DbImportConfiguration
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
name|tools
operator|.
name|dbimport
operator|.
name|DbImportModule
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
name|Util
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
name|Project
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
name|Task
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
name|net
operator|.
name|MalformedURLException
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

begin_class
specifier|public
class|class
name|DbImporterTask
extends|extends
name|Task
block|{
specifier|private
specifier|final
name|DbImportConfiguration
name|config
decl_stmt|;
specifier|private
name|ReverseEngineering
name|reverseEngineering
decl_stmt|;
specifier|private
name|boolean
name|isReverseEngineeringDefined
decl_stmt|;
specifier|public
name|DbImporterTask
parameter_list|()
block|{
name|this
operator|.
name|config
operator|=
operator|new
name|DbImportConfiguration
argument_list|()
expr_stmt|;
name|this
operator|.
name|config
operator|.
name|setUsePrimitives
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|.
name|setNamingStrategy
argument_list|(
name|DefaultObjectNameGenerator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// reverse engineering config is flattened into task...
name|this
operator|.
name|reverseEngineering
operator|=
operator|new
name|ReverseEngineering
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|addIncludeColumn
parameter_list|(
name|IncludeColumn
name|includeColumn
parameter_list|)
block|{
name|isReverseEngineeringDefined
operator|=
literal|true
expr_stmt|;
name|reverseEngineering
operator|.
name|addIncludeColumn
argument_list|(
name|includeColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addExcludeColumn
parameter_list|(
name|ExcludeColumn
name|excludeColumn
parameter_list|)
block|{
name|isReverseEngineeringDefined
operator|=
literal|true
expr_stmt|;
name|reverseEngineering
operator|.
name|addExcludeColumn
argument_list|(
name|excludeColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addIncludeTable
parameter_list|(
name|IncludeTable
name|includeTable
parameter_list|)
block|{
name|isReverseEngineeringDefined
operator|=
literal|true
expr_stmt|;
name|reverseEngineering
operator|.
name|addIncludeTable
argument_list|(
name|includeTable
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addExcludeTable
parameter_list|(
name|ExcludeTable
name|excludeTable
parameter_list|)
block|{
name|isReverseEngineeringDefined
operator|=
literal|true
expr_stmt|;
name|reverseEngineering
operator|.
name|addExcludeTable
argument_list|(
name|excludeTable
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addIncludeProcedure
parameter_list|(
name|IncludeProcedure
name|includeProcedure
parameter_list|)
block|{
name|isReverseEngineeringDefined
operator|=
literal|true
expr_stmt|;
name|reverseEngineering
operator|.
name|addIncludeProcedure
argument_list|(
name|includeProcedure
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addExcludeProcedure
parameter_list|(
name|ExcludeProcedure
name|excludeProcedure
parameter_list|)
block|{
name|isReverseEngineeringDefined
operator|=
literal|true
expr_stmt|;
name|reverseEngineering
operator|.
name|addExcludeProcedure
argument_list|(
name|excludeProcedure
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSkipRelationshipsLoading
parameter_list|(
name|boolean
name|skipRelationshipsLoading
parameter_list|)
block|{
name|isReverseEngineeringDefined
operator|=
literal|true
expr_stmt|;
name|reverseEngineering
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
name|boolean
name|skipPrimaryKeyLoading
parameter_list|)
block|{
name|isReverseEngineeringDefined
operator|=
literal|true
expr_stmt|;
name|reverseEngineering
operator|.
name|setSkipPrimaryKeyLoading
argument_list|(
name|skipPrimaryKeyLoading
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addConfiguredTableType
parameter_list|(
name|AntTableType
name|type
parameter_list|)
block|{
name|isReverseEngineeringDefined
operator|=
literal|true
expr_stmt|;
name|reverseEngineering
operator|.
name|addTableType
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addConfiguredSchema
parameter_list|(
name|Schema
name|schema
parameter_list|)
block|{
name|isReverseEngineeringDefined
operator|=
literal|true
expr_stmt|;
name|reverseEngineering
operator|.
name|addSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addCatalog
parameter_list|(
name|Catalog
name|catalog
parameter_list|)
block|{
name|isReverseEngineeringDefined
operator|=
literal|true
expr_stmt|;
name|reverseEngineering
operator|.
name|addCatalog
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|File
name|dataMapFile
init|=
name|config
operator|.
name|getDataMapFile
argument_list|()
decl_stmt|;
name|config
operator|.
name|setFiltersConfig
argument_list|(
operator|new
name|FiltersConfigBuilder
argument_list|(
name|reverseEngineering
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|validateAttributes
argument_list|()
expr_stmt|;
name|Log
name|logger
init|=
operator|new
name|AntLogger
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|config
operator|.
name|setLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSkipRelationshipsLoading
argument_list|(
name|reverseEngineering
operator|.
name|getSkipRelationshipsLoading
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSkipPrimaryKeyLoading
argument_list|(
name|reverseEngineering
operator|.
name|getSkipPrimaryKeyLoading
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setTableTypes
argument_list|(
name|reverseEngineering
operator|.
name|getTableTypes
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: get rid of this fork...
if|if
condition|(
name|isReverseEngineeringDefined
condition|)
block|{
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|CayenneDbSyncModule
argument_list|()
argument_list|,
operator|new
name|ToolsModule
argument_list|(
name|logger
argument_list|)
argument_list|,
operator|new
name|DbImportModule
argument_list|()
argument_list|)
decl_stmt|;
name|validateDbImportConfiguration
argument_list|(
name|config
argument_list|,
name|injector
argument_list|)
expr_stmt|;
try|try
block|{
name|injector
operator|.
name|getInstance
argument_list|(
name|DbImportAction
operator|.
name|class
argument_list|)
operator|.
name|execute
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Throwable
name|th
init|=
name|Util
operator|.
name|unwindException
argument_list|(
name|ex
argument_list|)
decl_stmt|;
name|String
name|message
init|=
literal|"Error importing database schema"
decl_stmt|;
if|if
condition|(
name|th
operator|.
name|getLocalizedMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|+=
literal|": "
operator|+
name|th
operator|.
name|getLocalizedMessage
argument_list|()
expr_stmt|;
block|}
name|log
argument_list|(
name|message
argument_list|,
name|Project
operator|.
name|MSG_ERR
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
name|message
argument_list|,
name|th
argument_list|)
throw|;
block|}
finally|finally
block|{
name|injector
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|dataMapFile
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|URL
name|url
init|=
name|dataMapFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|URLResource
name|resource
init|=
operator|new
name|URLResource
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|XMLDataMapLoader
name|xmlDataMapLoader
init|=
operator|new
name|XMLDataMapLoader
argument_list|()
decl_stmt|;
name|DataMap
name|dataMap
init|=
name|xmlDataMapLoader
operator|.
name|load
argument_list|(
name|resource
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataMap
operator|.
name|getReverseEngineering
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|CayenneDbSyncModule
argument_list|()
argument_list|,
operator|new
name|ToolsModule
argument_list|(
name|logger
argument_list|)
argument_list|,
operator|new
name|DbImportModule
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|ConfigurationNameMapper
name|nameMapper
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|ConfigurationNameMapper
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|reverseEngineeringLocation
init|=
name|nameMapper
operator|.
name|configurationLocation
argument_list|(
name|ReverseEngineering
operator|.
name|class
argument_list|,
name|dataMap
operator|.
name|getReverseEngineering
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Resource
name|reverseEngineeringResource
init|=
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
operator|.
name|getRelativeResource
argument_list|(
name|reverseEngineeringLocation
argument_list|)
decl_stmt|;
name|DefaultReverseEngineeringLoader
name|reverseEngineeringLoader
init|=
operator|new
name|DefaultReverseEngineeringLoader
argument_list|()
decl_stmt|;
name|ReverseEngineering
name|reverseEngineering
init|=
name|reverseEngineeringLoader
operator|.
name|load
argument_list|(
name|reverseEngineeringResource
operator|.
name|getURL
argument_list|()
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|reverseEngineering
operator|.
name|setName
argument_list|(
name|dataMap
operator|.
name|getReverseEngineering
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|setConfigurationSource
argument_list|(
name|reverseEngineeringResource
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setReverseEngineering
argument_list|(
name|reverseEngineering
argument_list|)
expr_stmt|;
name|FiltersConfigBuilder
name|filtersConfigBuilder
init|=
operator|new
name|FiltersConfigBuilder
argument_list|(
name|dataMap
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
decl_stmt|;
name|config
operator|.
name|getDbLoaderConfig
argument_list|()
operator|.
name|setFiltersConfig
argument_list|(
name|filtersConfigBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|validateDbImportConfiguration
argument_list|(
name|config
argument_list|,
name|injector
argument_list|)
expr_stmt|;
name|injector
operator|.
name|getInstance
argument_list|(
name|DbImportAction
operator|.
name|class
argument_list|)
operator|.
name|execute
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Throwable
name|th
init|=
name|Util
operator|.
name|unwindException
argument_list|(
name|ex
argument_list|)
decl_stmt|;
name|String
name|message
init|=
literal|"Error importing database schema"
decl_stmt|;
if|if
condition|(
name|th
operator|.
name|getLocalizedMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|+=
literal|": "
operator|+
name|th
operator|.
name|getLocalizedMessage
argument_list|()
expr_stmt|;
block|}
name|log
argument_list|(
name|message
argument_list|,
name|Project
operator|.
name|MSG_ERR
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
name|message
argument_list|,
name|th
argument_list|)
throw|;
block|}
finally|finally
block|{
name|injector
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|log
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|Project
operator|.
name|MSG_ERR
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|validateDbImportConfiguration
parameter_list|(
name|DbImportConfiguration
name|config
parameter_list|,
name|Injector
name|injector
parameter_list|)
throws|throws
name|BuildException
block|{
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
literal|null
decl_stmt|;
name|DbAdapter
name|adapter
init|=
literal|null
decl_stmt|;
try|try
block|{
name|dataSource
operator|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DataSourceFactory
operator|.
name|class
argument_list|)
operator|.
name|getDataSource
argument_list|(
name|dataNodeDescriptor
argument_list|)
expr_stmt|;
name|adapter
operator|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DbAdapterFactory
operator|.
name|class
argument_list|)
operator|.
name|createAdapter
argument_list|(
name|dataNodeDescriptor
argument_list|,
name|dataSource
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|adapter
operator|.
name|supportsCatalogsOnReverseEngineering
argument_list|()
operator|&&
name|reverseEngineering
operator|.
name|getCatalogs
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|reverseEngineering
operator|.
name|getCatalogs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|message
init|=
literal|"Your database does not support catalogs on reverse engineering. "
operator|+
literal|"It allows to connect to only one at the moment. Please don't note catalogs as param."
decl_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
name|message
argument_list|)
throw|;
block|}
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
literal|"Error creating DataSource ("
operator|+
name|dataSource
operator|+
literal|") or DbAdapter ("
operator|+
name|adapter
operator|+
literal|") for DataNodeDescriptor ("
operator|+
name|dataNodeDescriptor
operator|+
literal|")"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Validates attributes that are not related to internal      * DefaultClassGenerator. Throws BuildException if attributes are invalid.      */
specifier|protected
name|void
name|validateAttributes
parameter_list|()
throws|throws
name|BuildException
block|{
name|StringBuilder
name|error
init|=
operator|new
name|StringBuilder
argument_list|(
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getDataMapFile
argument_list|()
operator|==
literal|null
condition|)
block|{
name|error
operator|.
name|append
argument_list|(
literal|"The 'map' attribute must be set.\n"
argument_list|)
expr_stmt|;
block|}
name|DataSourceInfo
name|dataSourceInfo
init|=
name|config
operator|.
name|getDataSourceInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataSourceInfo
operator|.
name|getJdbcDriver
argument_list|()
operator|==
literal|null
condition|)
block|{
name|error
operator|.
name|append
argument_list|(
literal|"The 'driver' attribute must be set.\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dataSourceInfo
operator|.
name|getDataSourceUrl
argument_list|()
operator|==
literal|null
condition|)
block|{
name|error
operator|.
name|append
argument_list|(
literal|"The 'url' attribute must be set.\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|error
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|error
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * @since 4.0      */
specifier|public
name|void
name|setDefaultPackage
parameter_list|(
name|String
name|defaultPackage
parameter_list|)
block|{
name|config
operator|.
name|setDefaultPackage
argument_list|(
name|defaultPackage
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 4.0      */
specifier|public
name|void
name|setMeaningfulPkTables
parameter_list|(
name|String
name|meaningfulPkTables
parameter_list|)
block|{
name|config
operator|.
name|setMeaningfulPkTables
argument_list|(
name|meaningfulPkTables
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setNamingStrategy
parameter_list|(
name|String
name|namingStrategy
parameter_list|)
block|{
name|config
operator|.
name|setNamingStrategy
argument_list|(
name|namingStrategy
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAdapter
parameter_list|(
name|String
name|adapter
parameter_list|)
block|{
name|config
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDriver
parameter_list|(
name|String
name|driver
parameter_list|)
block|{
name|config
operator|.
name|setDriver
argument_list|(
name|driver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|config
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|config
operator|.
name|setUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|config
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ReverseEngineering
name|getReverseEngineering
parameter_list|()
block|{
return|return
name|reverseEngineering
return|;
block|}
specifier|public
name|File
name|getMap
parameter_list|()
block|{
return|return
name|config
operator|.
name|getDataMapFile
argument_list|()
return|;
block|}
specifier|public
name|void
name|setMap
parameter_list|(
name|File
name|map
parameter_list|)
block|{
name|config
operator|.
name|setDataMapFile
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DbImportConfiguration
name|toParameters
parameter_list|()
block|{
return|return
name|config
return|;
block|}
block|}
end_class

end_unit

