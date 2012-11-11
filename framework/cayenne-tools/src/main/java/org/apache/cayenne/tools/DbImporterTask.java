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

begin_class
specifier|public
class|class
name|DbImporterTask
extends|extends
name|Task
block|{
specifier|private
name|DbImportParameters
name|parameters
decl_stmt|;
comment|/**      * @deprecated since 3.2 in favor of "schema"      */
specifier|private
name|String
name|schemaName
decl_stmt|;
comment|/**      * @deprecated since 3.2 in favor of "meaningfulPkTable"      */
specifier|private
name|boolean
name|meaningfulPk
decl_stmt|;
specifier|public
name|DbImporterTask
parameter_list|()
block|{
name|parameters
operator|=
operator|new
name|DbImportParameters
argument_list|()
expr_stmt|;
name|parameters
operator|.
name|setOverwrite
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setImportProcedures
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setNamingStrategy
argument_list|(
literal|"org.apache.cayenne.map.naming.SmartNamingStrategy"
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
name|initSchema
argument_list|()
expr_stmt|;
name|initMeaningfulPkTables
argument_list|()
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
specifier|final
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
name|parameters
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
if|if
condition|(
name|parameters
operator|.
name|getDriver
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
name|parameters
operator|.
name|getUrl
argument_list|()
operator|==
literal|null
condition|)
block|{
name|error
operator|.
name|append
argument_list|(
literal|"The 'adapter' attribute must be set.\n"
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
comment|/**      * @since 3.2      */
specifier|public
name|void
name|setOverwrite
parameter_list|(
name|boolean
name|overwrite
parameter_list|)
block|{
name|parameters
operator|.
name|setOverwrite
argument_list|(
name|overwrite
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated since 3.2 use {@link #setSchema(String)}      */
specifier|public
name|void
name|setSchemaName
parameter_list|(
name|String
name|schemaName
parameter_list|)
block|{
name|this
operator|.
name|schemaName
operator|=
name|schemaName
expr_stmt|;
block|}
comment|/**      * @since 3.2      */
specifier|public
name|void
name|setSchema
parameter_list|(
name|String
name|schema
parameter_list|)
block|{
name|parameters
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.2      */
specifier|public
name|void
name|setDefaultPackage
parameter_list|(
name|String
name|defaultPackage
parameter_list|)
block|{
name|parameters
operator|.
name|setDefaultPackage
argument_list|(
name|defaultPackage
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setTablePattern
parameter_list|(
name|String
name|tablePattern
parameter_list|)
block|{
name|parameters
operator|.
name|setTablePattern
argument_list|(
name|tablePattern
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setImportProcedures
parameter_list|(
name|boolean
name|importProcedures
parameter_list|)
block|{
name|parameters
operator|.
name|setImportProcedures
argument_list|(
name|importProcedures
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setProcedurePattern
parameter_list|(
name|String
name|procedurePattern
parameter_list|)
block|{
name|parameters
operator|.
name|setProcedurePattern
argument_list|(
name|procedurePattern
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated since 3.2 use {@link #setMeaningfulPkTables(String)}      */
specifier|public
name|void
name|setMeaningfulPk
parameter_list|(
name|boolean
name|meaningfulPk
parameter_list|)
block|{
name|this
operator|.
name|meaningfulPk
operator|=
name|meaningfulPk
expr_stmt|;
block|}
comment|/**      * @since 3.2      */
specifier|public
name|void
name|setMeaningfulPkTables
parameter_list|(
name|String
name|meaningfulPkTables
parameter_list|)
block|{
name|parameters
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
name|parameters
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
name|parameters
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
name|parameters
operator|.
name|setDriver
argument_list|(
name|driver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setMap
parameter_list|(
name|File
name|map
parameter_list|)
block|{
name|parameters
operator|.
name|setDataMapFile
argument_list|(
name|map
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
name|parameters
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
name|parameters
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
name|parameters
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.2      */
specifier|public
name|void
name|setIncludeTables
parameter_list|(
name|String
name|includeTables
parameter_list|)
block|{
name|parameters
operator|.
name|setIncludeTables
argument_list|(
name|includeTables
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.2      */
specifier|public
name|void
name|setExcludeTables
parameter_list|(
name|String
name|excludeTables
parameter_list|)
block|{
name|parameters
operator|.
name|setExcludeTables
argument_list|(
name|excludeTables
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initSchema
parameter_list|()
block|{
if|if
condition|(
name|schemaName
operator|!=
literal|null
condition|)
block|{
name|log
argument_list|(
literal|"'schemaName' property is deprecated. Use 'schema' instead"
argument_list|,
name|Project
operator|.
name|MSG_WARN
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|.
name|getSchema
argument_list|()
operator|==
literal|null
condition|)
block|{
name|parameters
operator|.
name|setSchema
argument_list|(
name|schemaName
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|initMeaningfulPkTables
parameter_list|()
block|{
if|if
condition|(
name|meaningfulPk
condition|)
block|{
name|log
argument_list|(
literal|"'meaningfulPk' property is deprecated. Use 'meaningfulPkTables' pattern instead"
argument_list|,
name|Project
operator|.
name|MSG_WARN
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|.
name|getMeaningfulPkTables
argument_list|()
operator|==
literal|null
operator|&&
name|meaningfulPk
condition|)
block|{
name|parameters
operator|.
name|setMeaningfulPkTables
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

