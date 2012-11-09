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
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Driver
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
name|CayenneException
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
name|AbstractDbLoaderDelegate
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
name|configuration
operator|.
name|ToolModule
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
name|DriverDataSource
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
name|dba
operator|.
name|JdbcAdapter
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
name|AdhocObjectFactory
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
name|naming
operator|.
name|NamingStrategy
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
name|DeleteRuleUpdater
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
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
comment|/**      * Indicates whether existing DB and object entities should be overwritten.      * This is an all-or-nothing setting. If you need finer granularity, please      * use the Cayenne Modeler.      *       * Default is<code>true</code>.      *       * @parameter expression="${cdbimport.overwriteExisting}"      *            default-value="true"      */
specifier|private
name|boolean
name|overwriteExisting
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
comment|/**      * Indicates whether primary keys should be mapped as meaningful attributes      * in the object entities.      *       * Default is<code>false</code>.      *       * @parameter expression="${cdbimport.meaningfulPk}" default-value="false"      */
specifier|private
name|boolean
name|meaningfulPk
decl_stmt|;
comment|/**      * Java class implementing org.apache.cayenne.map.naming.NamingStrategy.      * This is used to specify how ObjEntities will be mapped from the imported      * DB schema.      *       * The default is a basic naming strategy.      *       * @parameter expression="${cdbimport.namingStrategy}"      *            default-value="org.apache.cayenne.map.naming.SmartNamingStrategy"      */
specifier|private
name|String
name|namingStrategy
decl_stmt|;
comment|/**      * Java class implementing org.apache.cayenne.dba.DbAdapter. While this      * attribute is optional (a generic JdbcAdapter is used if not set), it is      * highly recommended to specify correct target adapter.      *       * @parameter expression="${cdbimport.adapter}"      */
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
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"connection settings - [driver: %s, url: %s, username: %s, password: %s]"
argument_list|,
name|driver
argument_list|,
name|url
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"importer options - [map: %s, overwriteExisting: %s, schema: %s, tablePattern: %s, importProcedures: %s, procedurePattern: %s, meaningfulPk: %s, namingStrategy: %s]"
argument_list|,
name|map
argument_list|,
name|overwriteExisting
argument_list|,
name|schema
argument_list|,
name|tablePattern
argument_list|,
name|importProcedures
argument_list|,
name|procedurePattern
argument_list|,
name|meaningfulPk
argument_list|,
name|namingStrategy
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|doExecute
argument_list|()
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
specifier|private
name|void
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|schema
init|=
name|getSchema
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
name|ToolModule
argument_list|()
argument_list|)
decl_stmt|;
name|AdhocObjectFactory
name|objectFactory
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|AdhocObjectFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|DbAdapter
name|adapterInst
init|=
operator|(
name|adapter
operator|==
literal|null
operator|)
condition|?
operator|(
name|DbAdapter
operator|)
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|,
name|JdbcAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
else|:
operator|(
name|DbAdapter
operator|)
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|,
name|adapter
argument_list|)
decl_stmt|;
comment|// load driver taking custom CLASSPATH into account...
name|DriverDataSource
name|dataSource
init|=
operator|new
name|DriverDataSource
argument_list|(
operator|(
name|Driver
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|driver
argument_list|)
operator|.
name|newInstance
argument_list|()
argument_list|,
name|url
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
comment|// Load the data map and run the db importer.
specifier|final
name|LoaderDelegate
name|loaderDelegate
init|=
operator|new
name|LoaderDelegate
argument_list|()
decl_stmt|;
specifier|final
name|DbLoader
name|loader
init|=
operator|new
name|DbLoader
argument_list|(
name|dataSource
operator|.
name|getConnection
argument_list|()
argument_list|,
name|adapterInst
argument_list|,
name|loaderDelegate
argument_list|)
decl_stmt|;
name|loader
operator|.
name|setCreatingMeaningfulPK
argument_list|(
name|meaningfulPk
argument_list|)
expr_stmt|;
if|if
condition|(
name|namingStrategy
operator|!=
literal|null
condition|)
block|{
name|NamingStrategy
name|namingStrategyInst
init|=
operator|(
name|NamingStrategy
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
decl_stmt|;
name|loader
operator|.
name|setNamingStrategy
argument_list|(
name|namingStrategyInst
argument_list|)
expr_stmt|;
block|}
specifier|final
name|DataMap
name|dataMap
init|=
name|map
operator|.
name|exists
argument_list|()
condition|?
name|loadDataMap
argument_list|()
else|:
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|loader
operator|.
name|loadDataMapFromDB
argument_list|(
name|schema
argument_list|,
name|tablePattern
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
for|for
control|(
name|ObjEntity
name|addedObjEntity
range|:
name|loaderDelegate
operator|.
name|getAddedObjEntities
argument_list|()
control|)
block|{
name|DeleteRuleUpdater
operator|.
name|updateObjEntity
argument_list|(
name|addedObjEntity
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|importProcedures
condition|)
block|{
name|loader
operator|.
name|loadProceduresFromDB
argument_list|(
name|schema
argument_list|,
name|procedurePattern
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
block|}
comment|// Write the new DataMap out to disk.
name|map
operator|.
name|delete
argument_list|()
expr_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|XMLEncoder
name|encoder
init|=
operator|new
name|XMLEncoder
argument_list|(
name|pw
argument_list|,
literal|"\t"
argument_list|)
decl_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"utf-8\"?>"
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
name|pw
operator|.
name|close
argument_list|()
expr_stmt|;
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
specifier|final
class|class
name|LoaderDelegate
extends|extends
name|AbstractDbLoaderDelegate
block|{
specifier|public
name|boolean
name|overwriteDbEntity
parameter_list|(
specifier|final
name|DbEntity
name|ent
parameter_list|)
throws|throws
name|CayenneException
block|{
return|return
name|overwriteExisting
return|;
block|}
specifier|public
name|void
name|dbEntityAdded
parameter_list|(
specifier|final
name|DbEntity
name|ent
parameter_list|)
block|{
name|super
operator|.
name|dbEntityAdded
argument_list|(
name|ent
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Added DB entity: "
operator|+
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dbEntityRemoved
parameter_list|(
specifier|final
name|DbEntity
name|ent
parameter_list|)
block|{
name|super
operator|.
name|dbEntityRemoved
argument_list|(
name|ent
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Removed DB entity: "
operator|+
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objEntityAdded
parameter_list|(
specifier|final
name|ObjEntity
name|ent
parameter_list|)
block|{
name|super
operator|.
name|objEntityAdded
argument_list|(
name|ent
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Added obj entity: "
operator|+
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objEntityRemoved
parameter_list|(
specifier|final
name|ObjEntity
name|ent
parameter_list|)
block|{
name|super
operator|.
name|objEntityRemoved
argument_list|(
name|ent
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Removed obj entity: "
operator|+
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Loads and returns DataMap based on<code>map</code> attribute. */
specifier|protected
name|DataMap
name|loadDataMap
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|InputSource
name|in
init|=
operator|new
name|InputSource
argument_list|(
name|map
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|MapLoader
argument_list|()
operator|.
name|loadDataMap
argument_list|(
name|in
argument_list|)
return|;
block|}
block|}
end_class

end_unit

