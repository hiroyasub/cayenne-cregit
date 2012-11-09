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

begin_class
specifier|public
class|class
name|DbImporterTask
extends|extends
name|CayenneTask
block|{
comment|// DbImporter options.
specifier|private
name|boolean
name|overwriteExisting
init|=
literal|true
decl_stmt|;
comment|/**      * @deprecated since 3.2 in favor of "schema"      */
specifier|private
name|String
name|schemaName
decl_stmt|;
specifier|private
name|String
name|schema
decl_stmt|;
specifier|private
name|String
name|catalog
decl_stmt|;
specifier|private
name|String
name|tablePattern
decl_stmt|;
specifier|private
name|boolean
name|importProcedures
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|procedurePattern
decl_stmt|;
specifier|private
name|boolean
name|meaningfulPk
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|namingStrategy
init|=
literal|"org.apache.cayenne.map.naming.SmartNamingStrategy"
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|log
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
name|userName
argument_list|,
name|password
argument_list|)
argument_list|,
name|Project
operator|.
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
name|log
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
name|getSchema
argument_list|()
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
argument_list|,
name|Project
operator|.
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
name|validateAttributes
argument_list|()
expr_stmt|;
try|try
block|{
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
name|userName
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|Injector
name|injector
init|=
name|getInjector
argument_list|()
decl_stmt|;
name|DbAdapter
name|adapter
init|=
name|getAdapter
argument_list|(
name|injector
argument_list|,
name|dataSource
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
name|adapter
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
specifier|final
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
name|String
name|schema
init|=
name|getSchema
argument_list|()
decl_stmt|;
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
name|String
index|[]
name|types
init|=
name|loader
operator|.
name|getDefaultTableTypes
argument_list|()
decl_stmt|;
name|loader
operator|.
name|load
argument_list|(
name|dataMap
argument_list|,
name|catalog
argument_list|,
name|schema
argument_list|,
name|tablePattern
argument_list|,
name|types
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
name|loadProcedures
argument_list|(
name|dataMap
argument_list|,
name|catalog
argument_list|,
name|schema
argument_list|,
name|procedurePattern
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
name|map
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
name|driver
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
name|url
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
specifier|public
name|void
name|setOverwriteExisting
parameter_list|(
name|boolean
name|overwriteExisting
parameter_list|)
block|{
name|this
operator|.
name|overwriteExisting
operator|=
name|overwriteExisting
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
name|this
operator|.
name|schema
operator|=
name|schema
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
name|this
operator|.
name|tablePattern
operator|=
name|tablePattern
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
name|this
operator|.
name|importProcedures
operator|=
name|importProcedures
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
name|this
operator|.
name|procedurePattern
operator|=
name|procedurePattern
expr_stmt|;
block|}
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
specifier|public
name|void
name|setNamingStrategy
parameter_list|(
name|String
name|namingStrategy
parameter_list|)
block|{
name|this
operator|.
name|namingStrategy
operator|=
name|namingStrategy
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
annotation|@
name|Override
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
annotation|@
name|Override
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
name|log
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
annotation|@
name|Override
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
name|log
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
annotation|@
name|Override
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
name|log
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
annotation|@
name|Override
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
name|log
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
block|}
end_class

end_unit

