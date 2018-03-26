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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|AbstractMojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoFailureException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|LifecyclePhase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Mojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
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

begin_comment
comment|/**  * Maven mojo to reverse engineer datamap from DB.  *  * @since 3.0  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"cdbimport"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|GENERATE_SOURCES
argument_list|)
specifier|public
class|class
name|DbImporterMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * Java class implementing org.apache.cayenne.dba.DbAdapter. This attribute      * is optional, the default is AutoAdapter, i.e. Cayenne would try to guess      * the DB type.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"org.apache.cayenne.dba.AutoAdapter"
argument_list|)
specifier|private
name|String
name|adapter
decl_stmt|;
comment|/**      * Connection properties.      *      * @see DbImportDataSourceConfig      * @since 4.0      */
annotation|@
name|Parameter
specifier|private
name|DbImportDataSourceConfig
name|dataSource
init|=
operator|new
name|DbImportDataSourceConfig
argument_list|()
decl_stmt|;
comment|/**      * DataMap XML file to use as a base for DB importing.      */
annotation|@
name|Parameter
argument_list|(
name|required
operator|=
literal|true
argument_list|)
specifier|private
name|File
name|map
decl_stmt|;
comment|/**      * Project XML file to use. If set cayenneProject will be created or updated after DB importing.      * This is optional parameter.      * @since 4.1      */
annotation|@
name|Parameter
specifier|private
name|File
name|cayenneProject
decl_stmt|;
comment|/**      * An object that contains reverse engineering rules.      */
annotation|@
name|Parameter
argument_list|(
name|name
operator|=
literal|"dbimport"
argument_list|,
name|property
operator|=
literal|"dbimport"
argument_list|,
name|alias
operator|=
literal|"dbImport"
argument_list|)
specifier|private
name|ReverseEngineering
name|dbImportConfig
init|=
operator|new
name|ReverseEngineering
argument_list|()
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
name|Logger
name|logger
init|=
operator|new
name|MavenLogger
argument_list|(
name|this
argument_list|)
decl_stmt|;
comment|// check missing data source parameters
name|dataSource
operator|.
name|validate
argument_list|()
expr_stmt|;
name|DbImportConfiguration
name|config
init|=
name|createConfig
argument_list|(
name|logger
argument_list|)
decl_stmt|;
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
name|DbImportConfigurationValidator
name|validator
init|=
operator|new
name|DbImportConfigurationValidator
argument_list|(
name|dbImportConfig
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
name|MojoExecutionException
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
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|message
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
name|DbImportConfiguration
name|createConfig
parameter_list|(
name|Logger
name|logger
parameter_list|)
block|{
name|DbImportConfiguration
name|config
init|=
operator|new
name|DbImportConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbImportConfig
operator|.
name|getCatalogs
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|&&
name|dbImportConfig
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
name|setDefaultPackage
argument_list|(
name|dbImportConfig
operator|.
name|getDefaultPackage
argument_list|()
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
name|setFiltersConfig
argument_list|(
operator|new
name|FiltersConfigBuilder
argument_list|(
name|dbImportConfig
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setForceDataMapCatalog
argument_list|(
name|dbImportConfig
operator|.
name|isForceDataMapCatalog
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setForceDataMapSchema
argument_list|(
name|dbImportConfig
operator|.
name|isForceDataMapSchema
argument_list|()
argument_list|)
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
name|setMeaningfulPkTables
argument_list|(
name|dbImportConfig
operator|.
name|getMeaningfulPkTables
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setNamingStrategy
argument_list|(
name|dbImportConfig
operator|.
name|getNamingStrategy
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
name|setSkipRelationshipsLoading
argument_list|(
name|dbImportConfig
operator|.
name|getSkipRelationshipsLoading
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSkipPrimaryKeyLoading
argument_list|(
name|dbImportConfig
operator|.
name|getSkipPrimaryKeyLoading
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setStripFromTableNames
argument_list|(
name|dbImportConfig
operator|.
name|getStripFromTableNames
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setTableTypes
argument_list|(
name|dbImportConfig
operator|.
name|getTableTypes
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setTargetDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|config
operator|.
name|setCayenneProject
argument_list|(
name|cayenneProject
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
name|setUsePrimitives
argument_list|(
name|dbImportConfig
operator|.
name|isUsePrimitives
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUseJava7Types
argument_list|(
name|dbImportConfig
operator|.
name|isUseJava7Types
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
specifier|public
name|File
name|getMap
parameter_list|()
block|{
return|return
name|map
return|;
block|}
comment|/**      * Used only in tests, Maven will inject value directly into the "map" field      */
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
comment|/**      * This setter is used by Maven when defined {@code<dbimport>} tag      */
specifier|public
name|void
name|setDbimport
parameter_list|(
name|ReverseEngineering
name|dbImportConfig
parameter_list|)
block|{
name|this
operator|.
name|dbImportConfig
operator|=
name|dbImportConfig
expr_stmt|;
block|}
comment|/**      * This setter is used by Maven {@code<dbImport>} tag      */
specifier|public
name|void
name|setDbImport
parameter_list|(
name|ReverseEngineering
name|dbImportConfig
parameter_list|)
block|{
name|this
operator|.
name|dbImportConfig
operator|=
name|dbImportConfig
expr_stmt|;
block|}
specifier|public
name|ReverseEngineering
name|getReverseEngineering
parameter_list|()
block|{
return|return
name|dbImportConfig
return|;
block|}
block|}
end_class

end_unit

