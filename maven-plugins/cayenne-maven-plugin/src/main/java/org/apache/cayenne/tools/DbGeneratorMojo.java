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
name|access
operator|.
name|DbGenerator
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
name|datasource
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
name|log
operator|.
name|NoopJdbcEventLogger
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
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|sql
operator|.
name|Driver
import|;
end_import

begin_comment
comment|/**  * Maven mojo to perform class generation from data map. This class is a Maven  * adapter to DefaultClassGenerator class.  *   * @since 3.0  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"cdbgen"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|PRE_INTEGRATION_TEST
argument_list|)
specifier|public
class|class
name|DbGeneratorMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * DataMap XML file to use as a schema descriptor.      */
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
comment|/**      * Java class implementing org.apache.cayenne.dba.DbAdapter. While this      * attribute is optional (a generic JdbcAdapter is used if not set), it is      * highly recommended to specify correct target adapter.      */
annotation|@
name|Parameter
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
comment|/**      * Defines whether cdbgen should drop the tables before attempting to create      * new ones. Default is<code>false</code>.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
specifier|private
name|boolean
name|dropTables
decl_stmt|;
comment|/**      * Defines whether cdbgen should drop Cayenne primary key support objects.      * Default is<code>false</code>.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
specifier|private
name|boolean
name|dropPK
decl_stmt|;
comment|/**      * Defines whether cdbgen should create new tables. Default is      *<code>true</code>.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
specifier|private
name|boolean
name|createTables
decl_stmt|;
comment|/**      * Defines whether cdbgen should create Cayenne-specific auto PK objects.      * Default is<code>true</code>.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
specifier|private
name|boolean
name|createPK
decl_stmt|;
comment|/**      * Defines whether cdbgen should create foreign key copnstraints. Default is      *<code>true</code>.      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
specifier|private
name|boolean
name|createFK
decl_stmt|;
comment|/**      * @deprecated use {@code<dataSource>} tag to set connection properties      */
annotation|@
name|Deprecated
annotation|@
name|Parameter
argument_list|(
name|name
operator|=
literal|"driver"
argument_list|,
name|property
operator|=
literal|"driver"
argument_list|)
specifier|private
specifier|final
name|String
name|oldDriver
init|=
literal|""
decl_stmt|;
comment|// TODO remove in 4.0.BETA
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
name|logger
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"connection settings - [driver: %s, url: %s, username: %s]"
argument_list|,
name|dataSource
operator|.
name|getDriver
argument_list|()
argument_list|,
name|dataSource
operator|.
name|getUrl
argument_list|()
argument_list|,
name|dataSource
operator|.
name|getUsername
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"generator options - [dropTables: %s, dropPK: %s, createTables: %s, createPK: %s, createFK: %s]"
argument_list|,
name|dropTables
argument_list|,
name|dropPK
argument_list|,
name|createTables
argument_list|,
name|createPK
argument_list|,
name|createFK
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|DbAdapter
name|adapterInst
init|=
operator|(
name|adapter
operator|==
literal|null
operator|)
condition|?
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
comment|// Load the data map and run the db generator.
name|DataMap
name|dataMap
init|=
name|loadDataMap
argument_list|()
decl_stmt|;
name|DbGenerator
name|generator
init|=
operator|new
name|DbGenerator
argument_list|(
name|adapterInst
argument_list|,
name|dataMap
argument_list|,
name|NoopJdbcEventLogger
operator|.
name|getInstance
argument_list|()
argument_list|)
decl_stmt|;
name|generator
operator|.
name|setShouldCreateFKConstraints
argument_list|(
name|createFK
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldCreatePKSupport
argument_list|(
name|createPK
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldCreateTables
argument_list|(
name|createTables
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldDropPKSupport
argument_list|(
name|dropPK
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldDropTables
argument_list|(
name|dropTables
argument_list|)
expr_stmt|;
comment|// load driver taking custom CLASSPATH into account...
name|DriverDataSource
name|driverDataSource
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
name|dataSource
operator|.
name|getDriver
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
argument_list|,
name|dataSource
operator|.
name|getUrl
argument_list|()
argument_list|,
name|dataSource
operator|.
name|getUsername
argument_list|()
argument_list|,
name|dataSource
operator|.
name|getPassword
argument_list|()
argument_list|)
decl_stmt|;
name|generator
operator|.
name|runGenerator
argument_list|(
name|driverDataSource
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
literal|"Error generating database"
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
name|logger
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
comment|/** Loads and returns DataMap based on<code>map</code> attribute. */
specifier|private
name|DataMap
name|loadDataMap
parameter_list|()
throws|throws
name|Exception
block|{
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
annotation|@
name|Deprecated
specifier|public
name|void
name|setDriver
parameter_list|(
name|String
name|driver
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Connection properties were replaced with<dataSource> tag since 4.0.M5.\n"
operator|+
literal|"\tFor additional information see http://cayenne.apache.org/docs/4.0/cayenne-guide/including-cayenne-in-project.html#maven-projects"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

