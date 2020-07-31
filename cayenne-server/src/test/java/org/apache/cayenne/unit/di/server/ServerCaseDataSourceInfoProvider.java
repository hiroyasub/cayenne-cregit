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
name|unit
operator|.
name|di
operator|.
name|server
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
name|ConfigurationException
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
name|DataSourceInfo
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
name|derby
operator|.
name|DerbyAdapter
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
name|h2
operator|.
name|H2Adapter
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
name|hsqldb
operator|.
name|HSQLDBAdapter
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
name|sqlite
operator|.
name|SQLiteAdapter
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
name|Provider
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
name|slf4j
operator|.
name|LoggerFactory
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
name|io
operator|.
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_class
specifier|public
class|class
name|ServerCaseDataSourceInfoProvider
implements|implements
name|Provider
argument_list|<
name|DataSourceInfo
argument_list|>
block|{
specifier|private
specifier|static
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ServerCaseDataSourceInfoProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTIES_FILE
init|=
literal|"connection.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONNECTION_NAME_KEY
init|=
literal|"cayenneTestConnection"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ADAPTER_KEY_MAVEN
init|=
literal|"cayenneAdapter"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USER_NAME_KEY_MAVEN
init|=
literal|"cayenneJdbcUsername"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PASSWORD_KEY_MAVEN
init|=
literal|"cayenneJdbcPassword"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|URL_KEY_MAVEN
init|=
literal|"cayenneJdbcUrl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DRIVER_KEY_MAVEN
init|=
literal|"cayenneJdbcDriver"
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DataSourceInfo
argument_list|>
name|inMemoryDataSources
decl_stmt|;
specifier|private
name|ConnectionProperties
name|connectionProperties
decl_stmt|;
specifier|public
name|ServerCaseDataSourceInfoProvider
parameter_list|()
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|propertiesMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|File
name|file
init|=
name|connectionPropertiesFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|forEach
argument_list|(
parameter_list|(
name|k
parameter_list|,
name|v
parameter_list|)
lambda|->
name|propertiesMap
operator|.
name|put
argument_list|(
name|k
operator|.
name|toString
argument_list|()
argument_list|,
name|v
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|connectionProperties
operator|=
operator|new
name|ConnectionProperties
argument_list|(
name|propertiesMap
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Loaded  "
operator|+
name|connectionProperties
operator|.
name|size
argument_list|()
operator|+
literal|" DataSource configurations from properties file"
argument_list|)
expr_stmt|;
name|this
operator|.
name|inMemoryDataSources
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
comment|// preload default in-memory DataSources. Will use them as defaults if
comment|// nothing is configured in ~/.cayenne/connection.properties
name|DataSourceInfo
name|hsqldb
init|=
operator|new
name|DataSourceInfo
argument_list|()
decl_stmt|;
name|hsqldb
operator|.
name|setAdapterClassName
argument_list|(
name|HSQLDBAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|hsqldb
operator|.
name|setUserName
argument_list|(
literal|"sa"
argument_list|)
expr_stmt|;
name|hsqldb
operator|.
name|setPassword
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|hsqldb
operator|.
name|setDataSourceUrl
argument_list|(
literal|"jdbc:hsqldb:mem:aname;sql.regular_names=false"
argument_list|)
expr_stmt|;
name|hsqldb
operator|.
name|setJdbcDriver
argument_list|(
literal|"org.hsqldb.jdbcDriver"
argument_list|)
expr_stmt|;
name|hsqldb
operator|.
name|setMinConnections
argument_list|(
name|ConnectionProperties
operator|.
name|MIN_CONNECTIONS
argument_list|)
expr_stmt|;
name|hsqldb
operator|.
name|setMaxConnections
argument_list|(
name|ConnectionProperties
operator|.
name|MAX_CONNECTIONS
argument_list|)
expr_stmt|;
name|inMemoryDataSources
operator|.
name|put
argument_list|(
literal|"hsql"
argument_list|,
name|hsqldb
argument_list|)
expr_stmt|;
name|DataSourceInfo
name|h2
init|=
operator|new
name|DataSourceInfo
argument_list|()
decl_stmt|;
name|h2
operator|.
name|setAdapterClassName
argument_list|(
name|H2Adapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|h2
operator|.
name|setUserName
argument_list|(
literal|"sa"
argument_list|)
expr_stmt|;
name|h2
operator|.
name|setPassword
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|h2
operator|.
name|setDataSourceUrl
argument_list|(
literal|"jdbc:h2:mem:aname;DB_CLOSE_DELAY=-1"
argument_list|)
expr_stmt|;
name|h2
operator|.
name|setJdbcDriver
argument_list|(
literal|"org.h2.Driver"
argument_list|)
expr_stmt|;
name|h2
operator|.
name|setMinConnections
argument_list|(
name|ConnectionProperties
operator|.
name|MIN_CONNECTIONS
argument_list|)
expr_stmt|;
name|h2
operator|.
name|setMaxConnections
argument_list|(
name|ConnectionProperties
operator|.
name|MAX_CONNECTIONS
argument_list|)
expr_stmt|;
name|inMemoryDataSources
operator|.
name|put
argument_list|(
literal|"h2"
argument_list|,
name|h2
argument_list|)
expr_stmt|;
name|DataSourceInfo
name|derby
init|=
operator|new
name|DataSourceInfo
argument_list|()
decl_stmt|;
name|derby
operator|.
name|setAdapterClassName
argument_list|(
name|DerbyAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|derby
operator|.
name|setUserName
argument_list|(
literal|"sa"
argument_list|)
expr_stmt|;
name|derby
operator|.
name|setPassword
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|derby
operator|.
name|setDataSourceUrl
argument_list|(
literal|"jdbc:derby:target/testdb;create=true"
argument_list|)
expr_stmt|;
name|derby
operator|.
name|setJdbcDriver
argument_list|(
literal|"org.apache.derby.jdbc.EmbeddedDriver"
argument_list|)
expr_stmt|;
name|derby
operator|.
name|setMinConnections
argument_list|(
name|ConnectionProperties
operator|.
name|MIN_CONNECTIONS
argument_list|)
expr_stmt|;
name|derby
operator|.
name|setMaxConnections
argument_list|(
name|ConnectionProperties
operator|.
name|MAX_CONNECTIONS
argument_list|)
expr_stmt|;
name|inMemoryDataSources
operator|.
name|put
argument_list|(
literal|"derby"
argument_list|,
name|derby
argument_list|)
expr_stmt|;
name|DataSourceInfo
name|sqlite
init|=
operator|new
name|DataSourceInfo
argument_list|()
decl_stmt|;
name|sqlite
operator|.
name|setAdapterClassName
argument_list|(
name|SQLiteAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sqlite
operator|.
name|setUserName
argument_list|(
literal|"sa"
argument_list|)
expr_stmt|;
name|sqlite
operator|.
name|setPassword
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|sqlite
operator|.
name|setDataSourceUrl
argument_list|(
literal|"jdbc:sqlite:file:memdb?mode=memory&cache=shared&date_class=text"
argument_list|)
expr_stmt|;
name|sqlite
operator|.
name|setJdbcDriver
argument_list|(
literal|"org.sqlite.JDBC"
argument_list|)
expr_stmt|;
name|sqlite
operator|.
name|setMinConnections
argument_list|(
name|ConnectionProperties
operator|.
name|MIN_CONNECTIONS
argument_list|)
expr_stmt|;
name|sqlite
operator|.
name|setMaxConnections
argument_list|(
name|ConnectionProperties
operator|.
name|MAX_CONNECTIONS
argument_list|)
expr_stmt|;
name|inMemoryDataSources
operator|.
name|put
argument_list|(
literal|"sqlite"
argument_list|,
name|sqlite
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|DataSourceInfo
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
name|String
name|connectionKey
init|=
name|property
argument_list|(
name|CONNECTION_NAME_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectionKey
operator|==
literal|null
condition|)
block|{
name|connectionKey
operator|=
literal|"hsql"
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"Connection key: "
operator|+
name|connectionKey
argument_list|)
expr_stmt|;
name|DataSourceInfo
name|connectionInfo
init|=
name|connectionProperties
operator|.
name|getConnection
argument_list|(
name|connectionKey
argument_list|)
decl_stmt|;
comment|// attempt default if invalid key is specified
if|if
condition|(
name|connectionInfo
operator|==
literal|null
condition|)
block|{
name|connectionInfo
operator|=
name|inMemoryDataSources
operator|.
name|get
argument_list|(
name|connectionKey
argument_list|)
expr_stmt|;
block|}
name|connectionInfo
operator|=
name|applyOverrides
argument_list|(
name|connectionInfo
argument_list|)
expr_stmt|;
if|if
condition|(
name|connectionInfo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"No connection info for key: "
operator|+
name|connectionKey
argument_list|)
throw|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"loaded connection info: "
operator|+
name|connectionInfo
argument_list|)
expr_stmt|;
return|return
name|connectionInfo
return|;
block|}
specifier|private
name|File
name|connectionPropertiesFile
parameter_list|()
block|{
return|return
operator|new
name|File
argument_list|(
name|cayenneUserDir
argument_list|()
argument_list|,
name|PROPERTIES_FILE
argument_list|)
return|;
block|}
specifier|private
name|File
name|cayenneUserDir
parameter_list|()
block|{
name|File
name|homeDir
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|homeDir
argument_list|,
literal|".cayenne"
argument_list|)
return|;
block|}
specifier|private
name|DataSourceInfo
name|applyOverrides
parameter_list|(
name|DataSourceInfo
name|connectionInfo
parameter_list|)
block|{
name|String
name|adapter
init|=
name|property
argument_list|(
name|ADAPTER_KEY_MAVEN
argument_list|)
decl_stmt|;
name|String
name|user
init|=
name|property
argument_list|(
name|USER_NAME_KEY_MAVEN
argument_list|)
decl_stmt|;
name|String
name|pass
init|=
name|property
argument_list|(
name|PASSWORD_KEY_MAVEN
argument_list|)
decl_stmt|;
name|String
name|url
init|=
name|property
argument_list|(
name|URL_KEY_MAVEN
argument_list|)
decl_stmt|;
name|String
name|driver
init|=
name|property
argument_list|(
name|DRIVER_KEY_MAVEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectionInfo
operator|==
literal|null
condition|)
block|{
comment|// only create a brand new DSI if overrides contains a DB url...
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|connectionInfo
operator|=
operator|new
name|DataSourceInfo
argument_list|()
expr_stmt|;
name|connectionInfo
operator|.
name|setMinConnections
argument_list|(
name|ConnectionProperties
operator|.
name|MIN_CONNECTIONS
argument_list|)
expr_stmt|;
name|connectionInfo
operator|.
name|setMaxConnections
argument_list|(
name|ConnectionProperties
operator|.
name|MAX_CONNECTIONS
argument_list|)
expr_stmt|;
block|}
name|connectionInfo
operator|=
name|connectionInfo
operator|.
name|cloneInfo
argument_list|()
expr_stmt|;
if|if
condition|(
name|adapter
operator|!=
literal|null
condition|)
block|{
name|connectionInfo
operator|.
name|setAdapterClassName
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
name|connectionInfo
operator|.
name|setUserName
argument_list|(
name|user
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pass
operator|!=
literal|null
condition|)
block|{
name|connectionInfo
operator|.
name|setPassword
argument_list|(
name|pass
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|connectionInfo
operator|.
name|setDataSourceUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|driver
operator|!=
literal|null
condition|)
block|{
name|connectionInfo
operator|.
name|setJdbcDriver
argument_list|(
name|driver
argument_list|)
expr_stmt|;
block|}
return|return
name|connectionInfo
return|;
block|}
specifier|private
name|String
name|property
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|p
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|p
operator|==
literal|null
operator|||
name|p
operator|.
name|startsWith
argument_list|(
literal|"$"
argument_list|)
condition|?
literal|null
else|:
name|p
return|;
block|}
block|}
end_class

end_unit

