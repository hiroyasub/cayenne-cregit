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
name|groovy
operator|.
name|lang
operator|.
name|Closure
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
name|ClassLoaderManager
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
name|tools
operator|.
name|model
operator|.
name|DataSourceConfig
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
name|model
operator|.
name|DbImportConfig
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
name|Internal
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
name|OutputFile
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
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|TaskExecutionException
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
name|spi
operator|.
name|DefaultClassLoaderManager
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DbImportTask
extends|extends
name|BaseCayenneTask
block|{
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|adapter
decl_stmt|;
annotation|@
name|Internal
specifier|private
name|DataSourceConfig
name|dataSource
init|=
operator|new
name|DataSourceConfig
argument_list|()
decl_stmt|;
annotation|@
name|Internal
specifier|private
name|DbImportConfig
name|config
init|=
operator|new
name|DbImportConfig
argument_list|()
decl_stmt|;
annotation|@
name|Internal
specifier|private
name|ReverseEngineering
name|reverseEngineering
decl_stmt|;
specifier|private
name|File
name|cayenneProject
decl_stmt|;
specifier|public
name|DbImportTask
parameter_list|()
block|{
comment|// this task should be executed every invocation, so it is never up to date.
name|getOutputs
argument_list|()
operator|.
name|upToDateWhen
argument_list|(
name|task
lambda|->
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|TaskAction
specifier|public
name|void
name|runImport
parameter_list|()
block|{
name|dataSource
operator|.
name|validate
argument_list|()
expr_stmt|;
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
name|getLogger
argument_list|()
argument_list|)
argument_list|,
operator|new
name|DbImportModule
argument_list|()
argument_list|,
name|binder
lambda|->
name|binder
operator|.
name|bind
argument_list|(
name|ClassLoaderManager
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|DefaultClassLoaderManager
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|DbImportConfiguration
name|config
init|=
name|createConfig
argument_list|()
decl_stmt|;
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
name|TaskExecutionException
argument_list|(
name|this
argument_list|,
name|e
argument_list|)
throw|;
block|}
specifier|final
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
name|TaskExecutionException
argument_list|(
name|this
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
specifier|final
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
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
name|this
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
name|DbImportConfiguration
name|createConfig
parameter_list|()
block|{
name|reverseEngineering
operator|=
name|config
operator|.
name|toReverseEngineering
argument_list|()
expr_stmt|;
name|DbImportConfiguration
name|config
init|=
operator|new
name|DbImportConfiguration
argument_list|()
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
name|config
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|config
operator|.
name|setDriver
argument_list|(
name|dataSource
operator|.
name|getDriver
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setLogger
argument_list|(
name|getLogger
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setPassword
argument_list|(
name|dataSource
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setTargetDataMap
argument_list|(
name|getDataMapFile
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUrl
argument_list|(
name|dataSource
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUsername
argument_list|(
name|dataSource
operator|.
name|getUsername
argument_list|()
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
name|setStripFromTableNames
argument_list|(
name|reverseEngineering
operator|.
name|getStripFromTableNames
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
name|config
operator|.
name|setMeaningfulPkTables
argument_list|(
name|reverseEngineering
operator|.
name|getMeaningfulPkTables
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setNamingStrategy
argument_list|(
name|reverseEngineering
operator|.
name|getNamingStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setForceDataMapCatalog
argument_list|(
name|reverseEngineering
operator|.
name|isForceDataMapCatalog
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setForceDataMapSchema
argument_list|(
name|reverseEngineering
operator|.
name|isForceDataMapSchema
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setDefaultPackage
argument_list|(
name|reverseEngineering
operator|.
name|getDefaultPackage
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUsePrimitives
argument_list|(
name|reverseEngineering
operator|.
name|isUsePrimitives
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUseJava7Types
argument_list|(
name|reverseEngineering
operator|.
name|isUseJava7Types
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setCayenneProject
argument_list|(
name|cayenneProject
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
annotation|@
name|OutputFile
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
specifier|public
name|void
name|dbImport
parameter_list|(
specifier|final
name|Closure
argument_list|<
name|?
argument_list|>
name|closure
parameter_list|)
block|{
name|getProject
argument_list|()
operator|.
name|configure
argument_list|(
name|config
argument_list|,
name|closure
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dbimport
parameter_list|(
specifier|final
name|Closure
argument_list|<
name|?
argument_list|>
name|closure
parameter_list|)
block|{
name|dbImport
argument_list|(
name|closure
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dataSource
parameter_list|(
specifier|final
name|Closure
argument_list|<
name|?
argument_list|>
name|closure
parameter_list|)
block|{
name|getProject
argument_list|()
operator|.
name|configure
argument_list|(
name|dataSource
argument_list|,
name|closure
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DataSourceConfig
name|getDataSource
parameter_list|()
block|{
return|return
name|dataSource
return|;
block|}
specifier|public
name|DbImportConfig
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
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
specifier|final
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
specifier|public
name|void
name|adapter
parameter_list|(
specifier|final
name|String
name|adapter
parameter_list|)
block|{
name|setAdapter
argument_list|(
name|adapter
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
annotation|@
name|OutputFile
annotation|@
name|Optional
specifier|public
name|File
name|getCayenneProject
parameter_list|()
block|{
return|return
name|cayenneProject
return|;
block|}
specifier|public
name|void
name|setCayenneProject
parameter_list|(
specifier|final
name|File
name|cayenneProject
parameter_list|)
block|{
name|this
operator|.
name|cayenneProject
operator|=
name|cayenneProject
expr_stmt|;
block|}
specifier|public
name|void
name|cayenneProject
parameter_list|(
specifier|final
name|File
name|cayenneProject
parameter_list|)
block|{
name|this
operator|.
name|cayenneProject
operator|=
name|cayenneProject
expr_stmt|;
block|}
specifier|public
name|void
name|cayenneProject
parameter_list|(
specifier|final
name|String
name|cayenneProjectFileName
parameter_list|)
block|{
name|this
operator|.
name|cayenneProject
operator|=
name|getProject
argument_list|()
operator|.
name|file
argument_list|(
name|cayenneProjectFileName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

