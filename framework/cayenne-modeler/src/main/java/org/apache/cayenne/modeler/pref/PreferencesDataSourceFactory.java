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
name|modeler
operator|.
name|pref
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
name|sql
operator|.
name|SQLException
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|Cayenne
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
name|CayenneRuntimeException
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
name|DataContext
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
name|conf
operator|.
name|Configuration
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
name|conf
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
name|conn
operator|.
name|PoolManager
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionFactory
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
name|modeler
operator|.
name|Application
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
name|modeler
operator|.
name|util
operator|.
name|CayenneUserDir
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
name|pref
operator|.
name|DomainPreference
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
name|pref
operator|.
name|HSQLEmbeddedPreferenceService
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
name|query
operator|.
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * A DataSourceFactory that loads DataSources from CayenneModeler preferences. Allows  * integrating Cayenne runtime with preferences engine. Currently JNDIDataSourceFactory  * uses this factory as a failover loading mechanism, instantiating it via reflection.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|PreferencesDataSourceFactory
implements|implements
name|DataSourceFactory
block|{
specifier|protected
name|int
name|minPoolSize
decl_stmt|;
specifier|protected
name|int
name|maxPoolSize
decl_stmt|;
specifier|public
name|PreferencesDataSourceFactory
parameter_list|()
block|{
comment|// init pool size default
name|this
argument_list|(
literal|1
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PreferencesDataSourceFactory
parameter_list|(
name|int
name|minPoolSize
parameter_list|,
name|int
name|maxPoolSize
parameter_list|)
block|{
name|this
operator|.
name|minPoolSize
operator|=
name|minPoolSize
expr_stmt|;
name|this
operator|.
name|maxPoolSize
operator|=
name|maxPoolSize
expr_stmt|;
block|}
specifier|public
name|int
name|getMaxPoolSize
parameter_list|()
block|{
return|return
name|maxPoolSize
return|;
block|}
specifier|public
name|int
name|getMinPoolSize
parameter_list|()
block|{
return|return
name|minPoolSize
return|;
block|}
specifier|public
name|void
name|initializeWithParentConfiguration
parameter_list|(
name|Configuration
name|configuaration
parameter_list|)
block|{
comment|// noop
block|}
comment|/**      * Attempts to read named DataSource info from preferences and create a DataSource out      * of it. If no matching DataSource is found, throws CayenneRuntimeException.      */
specifier|public
name|DataSource
name|getDataSource
parameter_list|(
specifier|final
name|String
name|location
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|location
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null location"
argument_list|)
throw|;
block|}
comment|// figure out preferences DB location...
comment|// TODO: once prefs package becomes a part of Cayenne, remove dependency on
comment|// Application class... also this code is redundant with what Application does in
comment|// constructor
name|String
name|configuredName
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|Application
operator|.
name|APPLICATION_NAME_PROPERTY
argument_list|)
decl_stmt|;
name|String
name|name
init|=
operator|(
name|configuredName
operator|!=
literal|null
operator|)
condition|?
name|configuredName
else|:
name|Application
operator|.
name|DEFAULT_APPLICATION_NAME
decl_stmt|;
name|String
name|subdir
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|Application
operator|.
name|PREFERENCES_VERSION_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|subdir
operator|==
literal|null
condition|)
block|{
name|subdir
operator|=
name|Application
operator|.
name|PREFERENCES_VERSION
expr_stmt|;
block|}
name|File
name|dbDir
init|=
operator|new
name|File
argument_list|(
name|CayenneUserDir
operator|.
name|getInstance
argument_list|()
operator|.
name|resolveFile
argument_list|(
name|Application
operator|.
name|PREFERENCES_DB_SUBDIRECTORY
argument_list|)
argument_list|,
name|subdir
argument_list|)
decl_stmt|;
comment|// check if preferences even exist...
if|if
condition|(
operator|!
name|dbDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No preferences database directory exists: "
operator|+
name|dbDir
argument_list|)
throw|;
block|}
if|else if
condition|(
operator|!
operator|new
name|File
argument_list|(
name|dbDir
argument_list|,
literal|"db.properties"
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No preferences database exists in directory "
operator|+
name|dbDir
argument_list|)
throw|;
block|}
name|String
name|preferencesDB
init|=
operator|new
name|File
argument_list|(
name|dbDir
argument_list|,
literal|"db"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
comment|// create custom preferences service...
name|HSQLEmbeddedPreferenceService
name|service
init|=
operator|new
name|HSQLEmbeddedPreferenceService
argument_list|(
name|preferencesDB
argument_list|,
name|Application
operator|.
name|PREFERENCES_MAP_PACKAGE
argument_list|,
name|name
argument_list|)
block|{
specifier|protected
name|void
name|startTimer
parameter_list|()
block|{
comment|// noop: disable commit timer
block|}
specifier|protected
name|void
name|initPreferences
parameter_list|()
block|{
comment|// noop: disable commit timer
block|}
specifier|protected
name|void
name|initSchema
parameter_list|()
block|{
comment|// throw - no schema means no DataSource data
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No preferences matching location: "
operator|+
name|location
argument_list|)
throw|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|service
operator|.
name|startService
argument_list|()
expr_stmt|;
return|return
name|toDataSource
argument_list|(
name|service
operator|.
name|getDataContext
argument_list|()
argument_list|,
name|location
argument_list|)
return|;
block|}
finally|finally
block|{
comment|// make sure we cleanup after ourselves...
try|try
block|{
name|service
operator|.
name|stopService
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
comment|// ignore..
block|}
block|}
block|}
name|DataSource
name|toDataSource
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|String
name|location
parameter_list|)
throws|throws
name|Exception
block|{
comment|// grep through all domains ... maybe a bit naive...
name|Expression
name|locationFilter
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|DomainPreference
operator|.
name|KEY_PROPERTY
argument_list|,
name|location
argument_list|)
decl_stmt|;
name|List
name|preferences
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|DomainPreference
operator|.
name|class
argument_list|,
name|locationFilter
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|preferences
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No preferences matching location: "
operator|+
name|location
argument_list|)
throw|;
block|}
name|Collection
name|ids
init|=
operator|new
name|ArrayList
argument_list|(
name|preferences
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|preferences
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DomainPreference
name|pref
init|=
operator|(
name|DomainPreference
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|Cayenne
operator|.
name|pkForObject
argument_list|(
name|pref
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Expression
name|qualifier
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"db:"
operator|+
name|DBConnectionInfo
operator|.
name|ID_PK_COLUMN
operator|+
literal|" in $ids"
argument_list|)
decl_stmt|;
name|Map
name|params
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"ids"
argument_list|,
name|ids
argument_list|)
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|DBConnectionInfo
operator|.
name|class
argument_list|,
name|qualifier
operator|.
name|expWithParameters
argument_list|(
name|params
argument_list|)
argument_list|)
decl_stmt|;
comment|// narrow down the results to just DBConnectionInfo
name|List
name|connectionData
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectionData
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No preferences matching location: "
operator|+
name|location
argument_list|)
throw|;
block|}
if|if
condition|(
name|connectionData
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"More than one preference matched location: "
operator|+
name|location
argument_list|)
throw|;
block|}
name|DBConnectionInfo
name|info
init|=
operator|(
name|DBConnectionInfo
operator|)
name|connectionData
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|getJdbcDriver
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Incomplete connection info: no JDBC driver set."
argument_list|)
throw|;
block|}
if|if
condition|(
name|info
operator|.
name|getUrl
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Incomplete connection info: no DB URL set."
argument_list|)
throw|;
block|}
comment|// use default values for connection pool ... no info is available from
comment|// preferences...
return|return
operator|new
name|PoolManager
argument_list|(
name|info
operator|.
name|getJdbcDriver
argument_list|()
argument_list|,
name|info
operator|.
name|getUrl
argument_list|()
argument_list|,
literal|1
argument_list|,
literal|5
argument_list|,
name|info
operator|.
name|getUserName
argument_list|()
argument_list|,
name|info
operator|.
name|getPassword
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

