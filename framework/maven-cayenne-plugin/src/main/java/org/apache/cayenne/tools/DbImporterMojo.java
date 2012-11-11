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
name|tools
operator|.
name|dbimport
operator|.
name|DbImportParameters
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
comment|/**      * A default package for ObjEntity Java classes. If not specified, and the      * existing DataMap already has the default package, the existing package      * will be used.      *       * @parameter expression="${cdbimport.defaultPackage}"      * @since 3.2      */
specifier|private
name|String
name|defaultPackage
decl_stmt|;
comment|/**      * Indicates that the old mapping should be completely removed and replaced      * with the new data based on reverse engineering. Default is      *<code>true</code>.      *       * @parameter expression="${cdbimport.overwrite}" default-value="true"      */
specifier|private
name|boolean
name|overwrite
decl_stmt|;
comment|/**      * DB schema to use for DB importing.      *       * @parameter expression="${cdbimport.schemaName}"      * @deprecated since 3.2 renamed to "schema"      */
specifier|private
name|String
name|schemaName
decl_stmt|;
comment|/**      * DB schema to use for DB importing.      *       * @parameter expression="${cdbimport.catalog}"      * @since 3.2      */
specifier|private
name|String
name|catalog
decl_stmt|;
comment|/**      * DB schema to use for DB importing.      *       * @parameter expression="${cdbimport.schema}"      * @since 3.2      */
specifier|private
name|String
name|schema
decl_stmt|;
comment|/**      * Pattern for tables to import from DB.      *       * The default is to match against all tables.      *       * @parameter expression="${cdbimport.tablePattern}"      */
specifier|private
name|String
name|tablePattern
decl_stmt|;
comment|/**      * A comma-separated list of Perl5 regex that defines tables that should be      * included in import.      *       * @parameter expression="${cdbimport.includeTables}"      */
specifier|private
name|String
name|includeTables
decl_stmt|;
comment|/**      * A comma-separated list of Perl5 regex that defines tables that should be      * skipped from import.      *       * @parameter expression="${cdbimport.excludeTables}"      */
specifier|private
name|String
name|excludeTables
decl_stmt|;
comment|/**      * Indicates whether stored procedures should be imported.      *       * Default is<code>false</code>.      *       * @parameter expression="${cdbimport.importProcedures}"      *            default-value="false"      */
specifier|private
name|boolean
name|importProcedures
decl_stmt|;
comment|/**      * Pattern for stored procedures to import from DB. This is only meaningful      * if<code>importProcedures</code> is set to<code>true</code>.      *       * The default is to match against all stored procedures.      *       * @parameter expression="${cdbimport.procedurePattern}"      */
specifier|private
name|String
name|procedurePattern
decl_stmt|;
comment|/**      * Indicates whether primary keys should be mapped as meaningful attributes      * in the object entities.      *       * Default is<code>false</code>.      *       * @parameter expression="${cdbimport.meaningfulPk}" default-value="false"      * @deprecated since 3.2 use meaningfulPkTables      */
specifier|private
name|boolean
name|meaningfulPk
decl_stmt|;
comment|/**      * @parameter expression="${cdbimport.meaningfulPkTables}"      * @since 3.2      */
specifier|private
name|String
name|meaningfulPkTables
decl_stmt|;
comment|/**      * Java class implementing org.apache.cayenne.map.naming.NamingStrategy.      * This is used to specify how ObjEntities will be mapped from the imported      * DB schema.      *       * The default is a basic naming strategy.      *       * @parameter expression="${cdbimport.namingStrategy}"      *            default-value="org.apache.cayenne.map.naming.SmartNamingStrategy"      */
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
name|DbImportParameters
name|parameters
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
name|parameters
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
name|DbImportParameters
name|toParameters
parameter_list|()
block|{
name|DbImportParameters
name|parameters
init|=
operator|new
name|DbImportParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setCatalog
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setDefaultPackage
argument_list|(
name|defaultPackage
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setDriver
argument_list|(
name|driver
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setImportProcedures
argument_list|(
name|importProcedures
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setDataMapFile
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setMeaningfulPkTables
argument_list|(
name|getMeaningfulPkTables
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setNamingStrategy
argument_list|(
name|namingStrategy
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setOverwrite
argument_list|(
name|overwrite
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setProcedurePattern
argument_list|(
name|procedurePattern
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setSchema
argument_list|(
name|getSchema
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setTablePattern
argument_list|(
name|tablePattern
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setIncludeTables
argument_list|(
name|includeTables
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setExcludeTables
argument_list|(
name|excludeTables
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setUsePrimitives
argument_list|(
name|usePrimitives
argument_list|)
expr_stmt|;
return|return
name|parameters
return|;
block|}
specifier|private
name|String
name|getSchema
parameter_list|()
block|{
if|if
condition|(
name|schemaName
operator|!=
literal|null
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"'schemaName' property is deprecated. Use 'schema' instead"
argument_list|)
expr_stmt|;
block|}
return|return
name|schema
operator|!=
literal|null
condition|?
name|schema
else|:
name|schemaName
return|;
block|}
specifier|private
name|String
name|getMeaningfulPkTables
parameter_list|()
block|{
if|if
condition|(
name|meaningfulPk
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"'meaningfulPk' property is deprecated. Use 'meaningfulPkTables' pattern instead"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|meaningfulPkTables
operator|!=
literal|null
condition|)
block|{
return|return
name|meaningfulPkTables
return|;
block|}
return|return
name|meaningfulPk
condition|?
literal|"*"
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

