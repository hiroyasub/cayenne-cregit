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
name|DataSourceDescriptor
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
name|DbSyncModule
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
name|dbsync
operator|.
name|reverse
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
name|dbsync
operator|.
name|reverse
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
name|dbsync
operator|.
name|reverse
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|DbImportConfigurationValidator
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
name|dbsync
operator|.
name|reverse
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
name|dbsync
operator|.
name|reverse
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeRelationship
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
name|dbsync
operator|.
name|reverse
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
name|dbsync
operator|.
name|reverse
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
name|dbsync
operator|.
name|reverse
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
name|dbsync
operator|.
name|reverse
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
name|dbsync
operator|.
name|reverse
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
name|reverse
operator|.
name|filters
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
name|setUseJava7Types
argument_list|(
literal|false
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
name|reverseEngineering
operator|.
name|addExcludeTable
argument_list|(
name|excludeTable
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 4.1      */
specifier|public
name|void
name|addExcludeRelationship
parameter_list|(
name|ExcludeRelationship
name|excludeRelationship
parameter_list|)
block|{
name|reverseEngineering
operator|.
name|addExcludeRelationship
argument_list|(
name|excludeRelationship
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
name|Logger
name|logger
init|=
operator|new
name|AntLogger
argument_list|(
name|this
argument_list|)
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
name|DbSyncModule
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
if|if
condition|(
name|reverseEngineering
operator|.
name|getCatalogs
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|&&
name|reverseEngineering
operator|.
name|isEmptyContainer
argument_list|()
condition|)
block|{
name|config
operator|.
name|setUseDataMapReverseEngineering
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|DataSourceFactory
name|dataSourceFactory
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DataSourceFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|DbAdapterFactory
name|dbAdapterFactory
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DbAdapterFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|DataNodeDescriptor
name|dataNodeDescriptor
init|=
name|config
operator|.
name|createDataNodeDescriptor
argument_list|()
decl_stmt|;
try|try
block|{
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
name|dbAdapter
init|=
name|dbAdapterFactory
operator|.
name|createAdapter
argument_list|(
name|dataNodeDescriptor
argument_list|,
name|dataSource
argument_list|)
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
name|dataSource
argument_list|(
name|dataSource
argument_list|)
operator|.
name|dbAdapter
argument_list|(
name|dbAdapter
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
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
literal|"Error getting dataSource"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|validateAttributes
argument_list|()
expr_stmt|;
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
name|DbImportConfigurationValidator
name|validator
init|=
operator|new
name|DbImportConfigurationValidator
argument_list|(
name|reverseEngineering
argument_list|,
name|config
argument_list|,
name|injector
argument_list|)
decl_stmt|;
try|try
block|{
name|validator
operator|.
name|validate
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
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
name|getTargetDataMap
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
name|DataSourceDescriptor
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
comment|/**      * @since 4.0      */
specifier|public
name|void
name|setStripFromTableNames
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|config
operator|.
name|setStripFromTableNames
argument_list|(
name|pattern
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
comment|/**      * @deprecated since 5.0      */
annotation|@
name|Deprecated
specifier|public
name|void
name|setUsePrimitives
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
block|}
comment|/**      * @since 4.0      */
specifier|public
name|void
name|setUseJava7Types
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|config
operator|.
name|setUseJava7Types
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setForceDataMapCatalog
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|config
operator|.
name|setForceDataMapCatalog
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setForceDataMapSchema
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|config
operator|.
name|setForceDataMapSchema
argument_list|(
name|flag
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
name|getTargetDataMap
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
name|setTargetDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 4.1      */
specifier|public
name|File
name|getCayenneProject
parameter_list|()
block|{
return|return
name|config
operator|.
name|getCayenneProject
argument_list|()
return|;
block|}
comment|/**      * @since 4.1      */
specifier|public
name|void
name|setCayenneProject
parameter_list|(
name|File
name|cayenneProject
parameter_list|)
block|{
name|config
operator|.
name|setCayenneProject
argument_list|(
name|cayenneProject
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

