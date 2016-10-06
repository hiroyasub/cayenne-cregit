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

begin_comment
comment|/**  * Maven mojo to reverse engineer datamap from DB.  *  * @phase generate-sources  * @goal cdbimport  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|DbImporterMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * Java class implementing org.apache.cayenne.dba.DbAdapter. This attribute      * is optional, the default is AutoAdapter, i.e. Cayenne would try to guess      * the DB type.      *      * @parameter adapter="adapter"      * default-value="org.apache.cayenne.dba.AutoAdapter"      */
specifier|private
name|String
name|adapter
decl_stmt|;
comment|/**      * A default package for ObjEntity Java classes. If not specified, and the      * existing DataMap already has the default package, the existing package      * will be used.      *      * @parameter defaultPackage="defaultPackage"      * @since 4.0      */
specifier|private
name|String
name|defaultPackage
decl_stmt|;
comment|/**      * A class of JDBC driver to use for the target database.      *      * @parameter driver="driver"      * @required      */
specifier|private
name|String
name|driver
decl_stmt|;
comment|/**      * DataMap XML file to use as a base for DB importing.      *      * @parameter map="map"      * @required      */
specifier|private
name|File
name|map
decl_stmt|;
comment|/**      * A comma-separated list of Perl5 patterns that defines which imported tables should have their primary key columns      * mapped as ObjAttributes. "*" would indicate all tables.      *      * @parameter meaningfulPkTables="meaningfulPkTables"      * @since 4.0      */
specifier|private
name|String
name|meaningfulPkTables
decl_stmt|;
comment|/**      * Object layer naming generator implementation. Should be fully qualified Java class name implementing      * "org.apache.cayenne.dbsync.naming.ObjectNameGenerator". The default is      * "org.apache.cayenne.dbsync.naming.DefaultObjectNameGenerator".      *      * @parameter namingStrategy="namingStrategy"      * default-value="org.apache.cayenne.dbsync.naming.DefaultObjectNameGenerator"      */
specifier|private
name|String
name|namingStrategy
decl_stmt|;
comment|/**      * Database user password.      *      * @parameter password="password"      */
specifier|private
name|String
name|password
decl_stmt|;
comment|/**      * An object that contains reverse engineering rules.      *      * @parameter reverseEngineering="reverseEngineering"      */
specifier|private
name|ReverseEngineering
name|reverseEngineering
init|=
operator|new
name|ReverseEngineering
argument_list|()
decl_stmt|;
comment|/**      * JDBC connection URL of a target database.      *      * @parameter url="url"      * @required      */
specifier|private
name|String
name|url
decl_stmt|;
comment|/**      * If true, would use primitives instead of numeric and boolean classes.      *      * @parameter usePrimitives="usePrimitives" default-value="true"      */
specifier|private
name|boolean
name|usePrimitives
decl_stmt|;
comment|/**      * Database user name.      *      * @parameter username="username"      */
specifier|private
name|String
name|username
decl_stmt|;
comment|/**      * Flag which defines from where to take the configuration of cdbImport. If      * we define the config of cdbImport in pom.xml we should set it to true or      * it will be set to true automatically if we define some configuration      * parameters in pom.xml. Else it remains default(false) and for cdbImport      * we use the configuration defined in signed dataMap      *      * @parameter isReverseEngineeringDefined="isReverseEngineeringDefined"      * default-value="false"      */
comment|// TODO: get rid of this fork...
specifier|private
name|boolean
name|isReverseEngineeringDefined
init|=
literal|false
decl_stmt|;
specifier|public
name|void
name|setIsReverseEngineeringDefined
parameter_list|(
name|boolean
name|isReverseEngineeringDefined
parameter_list|)
block|{
name|this
operator|.
name|isReverseEngineeringDefined
operator|=
name|isReverseEngineeringDefined
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
name|Log
name|logger
init|=
operator|new
name|MavenLogger
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|DbImportConfiguration
name|config
init|=
name|toParameters
argument_list|()
decl_stmt|;
name|config
operator|.
name|setLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
name|File
name|dataMapFile
init|=
name|config
operator|.
name|getDataMapFile
argument_list|()
decl_stmt|;
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
try|try
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
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|MojoExecutionException
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
name|MojoExecutionException
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
name|MojoExecutionException
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
name|MojoExecutionException
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
name|DbImportConfiguration
name|toParameters
parameter_list|()
block|{
name|DbImportConfiguration
name|config
init|=
operator|new
name|DbImportConfiguration
argument_list|()
decl_stmt|;
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
name|defaultPackage
argument_list|)
expr_stmt|;
name|config
operator|.
name|setDriver
argument_list|(
name|driver
argument_list|)
expr_stmt|;
name|config
operator|.
name|setDataMapFile
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|config
operator|.
name|setMeaningfulPkTables
argument_list|(
name|meaningfulPkTables
argument_list|)
expr_stmt|;
name|config
operator|.
name|setNamingStrategy
argument_list|(
name|namingStrategy
argument_list|)
expr_stmt|;
name|config
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUsePrimitives
argument_list|(
name|usePrimitives
argument_list|)
expr_stmt|;
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
specifier|public
name|String
name|getDriver
parameter_list|()
block|{
return|return
name|driver
return|;
block|}
specifier|public
name|void
name|setDriver
parameter_list|(
name|String
name|driver
parameter_list|)
block|{
name|this
operator|.
name|driver
operator|=
name|driver
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
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
name|void
name|setReverseEngineering
parameter_list|(
name|ReverseEngineering
name|reverseEngineering
parameter_list|)
block|{
name|isReverseEngineeringDefined
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|reverseEngineering
operator|=
name|reverseEngineering
expr_stmt|;
block|}
block|}
end_class

end_unit

