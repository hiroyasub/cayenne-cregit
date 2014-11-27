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
name|util
operator|.
name|ArrayList
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|loader
operator|.
name|filters
operator|.
name|EntityFilters
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
name|access
operator|.
name|loader
operator|.
name|filters
operator|.
name|FilterFactory
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
name|config
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
name|tools
operator|.
name|dbimport
operator|.
name|config
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
name|tools
operator|.
name|dbimport
operator|.
name|config
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
name|tools
operator|.
name|dbimport
operator|.
name|config
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
name|tools
operator|.
name|dbimport
operator|.
name|config
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
name|tools
operator|.
name|dbimport
operator|.
name|config
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
name|tools
operator|.
name|dbimport
operator|.
name|config
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
name|tools
operator|.
name|dbimport
operator|.
name|config
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
name|tools
operator|.
name|dbimport
operator|.
name|config
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

begin_comment
comment|/**  * Maven mojo to reverse engineer datamap from DB.  *   * @since 3.0  *   * @phase generate-sources  * @goal cdbimport  */
end_comment

begin_class
specifier|public
class|class
name|DbImporterMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * DataMap XML file to use as a base for DB importing.      *       * @parameter expression="${cdbimport.map}"      * @required      */
specifier|private
name|File
name|map
decl_stmt|;
comment|/**      * A default package for ObjEntity Java classes. If not specified, and the      * existing DataMap already has the default package, the existing package      * will be used.      *       * @parameter expression="${cdbimport.defaultPackage}"      * @since 4.0      */
specifier|private
name|String
name|defaultPackage
decl_stmt|;
comment|/**      * Indicates that the old mapping should be completely removed and replaced      * with the new data based on reverse engineering. Default is      *<code>true</code>.      *       * @parameter expression="${cdbimport.overwrite}" default-value="true"      */
specifier|private
name|boolean
name|overwrite
decl_stmt|;
comment|/**      * @parameter expression="${cdbimport.meaningfulPkTables}"      * @since 4.0      */
specifier|private
name|String
name|meaningfulPkTables
decl_stmt|;
comment|/**      * Java class implementing org.apache.cayenne.map.naming.NamingStrategy.      * This is used to specify how ObjEntities will be mapped from the imported      * DB schema.      *       * The default is a basic naming strategy.      *       * @parameter expression="${cdbimport.namingStrategy}"      *            default-value="org.apache.cayenne.map.naming.DefaultNameGenerator"      */
specifier|private
name|String
name|namingStrategy
decl_stmt|;
comment|/**      * Java class implementing org.apache.cayenne.dba.DbAdapter. This attribute      * is optional, the default is AutoAdapter, i.e. Cayenne would try to guess      * the DB type.      *       * @parameter expression="${cdbimport.adapter}"      *            default-value="org.apache.cayenne.dba.AutoAdapter"      */
specifier|private
name|String
name|adapter
decl_stmt|;
comment|/**      * A class of JDBC driver to use for the target database.      *       * @parameter expression="${cdbimport.driver}"      * @required      */
specifier|private
name|String
name|driver
decl_stmt|;
comment|/**      * JDBC connection URL of a target database.      *       * @parameter expression="${cdbimport.url}"      * @required      */
specifier|private
name|String
name|url
decl_stmt|;
comment|/**      * Database user name.      *       * @parameter expression="${cdbimport.username}"      */
specifier|private
name|String
name|username
decl_stmt|;
comment|/**      * Database user password.      *       * @parameter expression="${cdbimport.password}"      */
specifier|private
name|String
name|password
decl_stmt|;
comment|/**      * If true, would use primitives instead of numeric and boolean classes.      *       * @parameter expression="${cdbimport.usePrimitives}" default-value="true"      */
specifier|private
name|boolean
name|usePrimitives
decl_stmt|;
specifier|private
specifier|final
name|EntityFilters
operator|.
name|Builder
name|filterBuilder
init|=
operator|new
name|EntityFilters
operator|.
name|Builder
argument_list|()
decl_stmt|;
comment|/**      * If true, would use primitives instead of numeric and boolean classes.      *      * @parameter expression="${cdbimport.reverseEngineering}"      */
specifier|private
name|ReverseEngineering
name|reverseEngineering
init|=
operator|new
name|ReverseEngineering
argument_list|()
decl_stmt|;
comment|/**      * DB schema to use for DB importing.      *      * @parameter expression="${cdbimport.schemaName}"      * @deprecated since 4.0 renamed to "schema"      */
specifier|private
name|String
name|schemaName
decl_stmt|;
specifier|private
name|void
name|setSchemaName
parameter_list|(
name|String
name|schemaName
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"'schemaName' property is deprecated. Use 'schema' instead"
argument_list|)
expr_stmt|;
name|filterBuilder
operator|.
name|schema
argument_list|(
name|schemaName
argument_list|)
expr_stmt|;
block|}
comment|/**      * DB schema to use for DB importing.      *      * @parameter expression="${cdbimport.schema}"      * @since 4.0      */
specifier|private
name|Schema
name|schema
decl_stmt|;
specifier|public
name|void
name|setSchema
parameter_list|(
name|Schema
name|schema
parameter_list|)
block|{
if|if
condition|(
name|schema
operator|.
name|isEmptyContainer
argument_list|()
condition|)
block|{
name|filterBuilder
operator|.
name|schema
argument_list|(
name|schema
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reverseEngineering
operator|.
name|addSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Pattern for tables to import from DB.      *      * The default is to match against all tables.      *      * @parameter expression="${cdbimport.tablePattern}"      */
specifier|private
name|String
name|tablePattern
decl_stmt|;
specifier|public
name|void
name|setTablePattern
parameter_list|(
name|String
name|tablePattern
parameter_list|)
block|{
name|filterBuilder
operator|.
name|includeTables
argument_list|(
name|tablePattern
argument_list|)
expr_stmt|;
block|}
comment|/**      * Indicates whether stored procedures should be imported.      *      * Default is<code>false</code>.      *      * @parameter expression="${cdbimport.importProcedures}"      *            default-value="false"      */
specifier|private
name|String
name|importProcedures
decl_stmt|;
specifier|public
name|void
name|setImportProcedures
parameter_list|(
name|boolean
name|importProcedures
parameter_list|)
block|{
name|filterBuilder
operator|.
name|setProceduresFilters
argument_list|(
name|importProcedures
condition|?
name|FilterFactory
operator|.
name|TRUE
else|:
name|FilterFactory
operator|.
name|NULL
argument_list|)
expr_stmt|;
block|}
comment|/**      * Pattern for stored procedures to import from DB. This is only meaningful      * if<code>importProcedures</code> is set to<code>true</code>.      *      * The default is to match against all stored procedures.      *      * @parameter expression="${cdbimport.procedurePattern}"      */
specifier|private
name|String
name|procedurePattern
decl_stmt|;
specifier|public
name|void
name|setProcedurePattern
parameter_list|(
name|String
name|procedurePattern
parameter_list|)
block|{
name|filterBuilder
operator|.
name|includeProcedures
argument_list|(
name|procedurePattern
argument_list|)
expr_stmt|;
block|}
comment|/**      * Indicates whether primary keys should be mapped as meaningful attributes      * in the object entities.      *      * Default is<code>false</code>.      *      * @parameter expression="${cdbimport.meaningfulPk}" default-value="false"      * @deprecated since 4.0 use meaningfulPkTables      */
specifier|private
name|boolean
name|meaningfulPk
decl_stmt|;
specifier|public
name|void
name|setMeaningfulPk
parameter_list|(
name|boolean
name|meaningfulPk
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"'meaningfulPk' property is deprecated. Use 'meaningfulPkTables' pattern instead"
argument_list|)
expr_stmt|;
name|this
operator|.
name|meaningfulPkTables
operator|=
name|meaningfulPk
condition|?
literal|"*"
else|:
literal|null
expr_stmt|;
block|}
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
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
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
name|setOverwrite
argument_list|(
name|overwrite
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
name|add
argument_list|(
name|filterBuilder
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|filtersConfig
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
comment|/**      * A comma-separated list of Perl5 regex that defines tables that should be      * included in import.      *      * @parameter expression="${cdbimport.includeTables}"      */
specifier|private
name|String
name|includeTables
decl_stmt|;
specifier|public
name|void
name|setIncludeTables
parameter_list|(
name|String
name|includeTables
parameter_list|)
block|{
name|filterBuilder
operator|.
name|includeTables
argument_list|(
name|includeTables
argument_list|)
expr_stmt|;
block|}
comment|/**      * A comma-separated list of Perl5 regex that defines tables that should be      * skipped from import.      *      * @parameter expression="${cdbimport.excludeTables}"      */
specifier|private
name|String
name|excludeTables
decl_stmt|;
specifier|public
name|void
name|setExcludeTables
parameter_list|(
name|String
name|excludeTables
parameter_list|)
block|{
name|filterBuilder
operator|.
name|excludeTables
argument_list|(
name|excludeTables
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addSchema
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
comment|/**      * DB schema to use for DB importing.      *      * @parameter expression="${cdbimport.catalog}"      * @since 4.0      */
specifier|private
name|Catalog
name|catalog
index|[]
decl_stmt|;
specifier|public
name|void
name|addCatalog
parameter_list|(
name|Catalog
name|catalog
parameter_list|)
block|{
if|if
condition|(
name|catalog
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|catalog
operator|.
name|isEmptyContainer
argument_list|()
condition|)
block|{
name|filterBuilder
operator|.
name|catalog
argument_list|(
name|catalog
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reverseEngineering
operator|.
name|addCatalog
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
block|}
block|}
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

