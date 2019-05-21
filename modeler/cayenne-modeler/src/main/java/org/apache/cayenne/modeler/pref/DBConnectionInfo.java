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
name|modeler
operator|.
name|pref
package|;
end_package

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
name|prefs
operator|.
name|Preferences
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
name|AutoAdapter
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
name|ClassLoadingService
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
name|CayennePreference
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

begin_class
specifier|public
class|class
name|DBConnectionInfo
extends|extends
name|CayennePreference
block|{
specifier|private
specifier|static
specifier|final
name|String
name|EMPTY_STRING
init|=
literal|""
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DB_ADAPTER_PROPERTY
init|=
literal|"dbAdapter"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JDBC_DRIVER_PROPERTY
init|=
literal|"jdbcDriver"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD_PROPERTY
init|=
literal|"password"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|URL_PROPERTY
init|=
literal|"url"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USER_NAME_PROPERTY
init|=
literal|"userName"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DB_CONNECTION_INFO
init|=
literal|"dbConnectionInfo"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ID_PK_COLUMN
init|=
literal|"id"
decl_stmt|;
specifier|private
name|String
name|nodeName
decl_stmt|;
specifier|private
name|String
name|dbAdapter
decl_stmt|;
specifier|private
name|String
name|jdbcDriver
decl_stmt|;
specifier|private
name|String
name|password
decl_stmt|;
specifier|private
name|String
name|url
decl_stmt|;
specifier|private
name|String
name|userName
decl_stmt|;
specifier|private
name|Preferences
name|dbConnectionInfoPreferences
decl_stmt|;
specifier|public
name|DBConnectionInfo
parameter_list|()
block|{
name|dbConnectionInfoPreferences
operator|=
name|getCayennePreference
argument_list|()
operator|.
name|node
argument_list|(
name|DB_CONNECTION_INFO
argument_list|)
expr_stmt|;
name|setCurrentPreference
argument_list|(
name|dbConnectionInfoPreferences
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
specifier|public
name|DBConnectionInfo
parameter_list|(
specifier|final
name|String
name|nameNode
parameter_list|,
specifier|final
name|boolean
name|initFromPreferences
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setNodeName
argument_list|(
name|nameNode
argument_list|)
expr_stmt|;
if|if
condition|(
name|initFromPreferences
condition|)
block|{
name|initObjectPreference
argument_list|()
expr_stmt|;
block|}
block|}
empty_stmt|;
annotation|@
name|Override
specifier|public
name|Preferences
name|getCurrentPreference
parameter_list|()
block|{
if|if
condition|(
name|getNodeName
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|super
operator|.
name|getCurrentPreference
argument_list|()
return|;
block|}
return|return
name|dbConnectionInfoPreferences
operator|.
name|node
argument_list|(
name|getNodeName
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setObject
parameter_list|(
specifier|final
name|CayennePreference
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|DBConnectionInfo
condition|)
block|{
name|setUrl
argument_list|(
operator|(
operator|(
name|DBConnectionInfo
operator|)
name|object
operator|)
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|setUserName
argument_list|(
operator|(
operator|(
name|DBConnectionInfo
operator|)
name|object
operator|)
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|setPassword
argument_list|(
operator|(
operator|(
name|DBConnectionInfo
operator|)
name|object
operator|)
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|setJdbcDriver
argument_list|(
operator|(
operator|(
name|DBConnectionInfo
operator|)
name|object
operator|)
operator|.
name|getJdbcDriver
argument_list|()
argument_list|)
expr_stmt|;
name|setDbAdapter
argument_list|(
operator|(
operator|(
name|DBConnectionInfo
operator|)
name|object
operator|)
operator|.
name|getDbAdapter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|saveObjectPreference
parameter_list|()
block|{
if|if
condition|(
name|getCurrentPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getDbAdapter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|DB_ADAPTER_PROPERTY
argument_list|,
name|getDbAdapter
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getUrl
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|URL_PROPERTY
argument_list|,
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getUserName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|USER_NAME_PROPERTY
argument_list|,
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getPassword
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|PASSWORD_PROPERTY
argument_list|,
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getJdbcDriver
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|JDBC_DRIVER_PROPERTY
argument_list|,
name|getJdbcDriver
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|initObjectPreference
parameter_list|()
block|{
if|if
condition|(
name|getCurrentPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setDbAdapter
argument_list|(
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|DB_ADAPTER_PROPERTY
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|setUrl
argument_list|(
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|URL_PROPERTY
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|setUserName
argument_list|(
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|USER_NAME_PROPERTY
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|setPassword
argument_list|(
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|PASSWORD_PROPERTY
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|setJdbcDriver
argument_list|(
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|JDBC_DRIVER_PROPERTY
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|setNodeName
argument_list|(
name|getCurrentPreference
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getNodeName
parameter_list|()
block|{
return|return
name|nodeName
return|;
block|}
specifier|public
name|void
name|setNodeName
parameter_list|(
specifier|final
name|String
name|nodeName
parameter_list|)
block|{
name|this
operator|.
name|nodeName
operator|=
name|nodeName
expr_stmt|;
block|}
specifier|public
name|String
name|getDbAdapter
parameter_list|()
block|{
return|return
name|dbAdapter
return|;
block|}
specifier|public
name|void
name|setDbAdapter
parameter_list|(
specifier|final
name|String
name|dbAdapter
parameter_list|)
block|{
name|this
operator|.
name|dbAdapter
operator|=
name|dbAdapter
expr_stmt|;
block|}
specifier|public
name|String
name|getJdbcDriver
parameter_list|()
block|{
return|return
name|jdbcDriver
return|;
block|}
specifier|public
name|void
name|setJdbcDriver
parameter_list|(
specifier|final
name|String
name|jdbcDriver
parameter_list|)
block|{
name|this
operator|.
name|jdbcDriver
operator|=
name|jdbcDriver
expr_stmt|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
operator|==
literal|null
condition|?
name|EMPTY_STRING
else|:
name|password
return|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
specifier|final
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
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
specifier|final
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
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
operator|==
literal|null
condition|?
name|EMPTY_STRING
else|:
name|userName
return|;
block|}
specifier|public
name|void
name|setUserName
parameter_list|(
specifier|final
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
specifier|public
name|Preferences
name|getDbConnectionInfoPreferences
parameter_list|()
block|{
return|return
name|dbConnectionInfoPreferences
return|;
block|}
specifier|public
name|void
name|setDbConnectionInfoPreferences
parameter_list|(
specifier|final
name|Preferences
name|dbConnectionInfoPreferences
parameter_list|)
block|{
name|this
operator|.
name|dbConnectionInfoPreferences
operator|=
name|dbConnectionInfoPreferences
expr_stmt|;
block|}
comment|/** 	 * Creates a DbAdapter based on configured values. 	 */
specifier|public
name|DbAdapter
name|makeAdapter
parameter_list|(
specifier|final
name|ClassLoadingService
name|classLoader
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|adapterClassName
init|=
name|getDbAdapter
argument_list|()
decl_stmt|;
name|Application
name|appInstance
init|=
name|Application
operator|.
name|getInstance
argument_list|()
decl_stmt|;
if|if
condition|(
name|adapterClassName
operator|==
literal|null
operator|||
name|AutoAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|adapterClassName
argument_list|)
condition|)
block|{
return|return
name|appInstance
operator|.
name|getInjector
argument_list|()
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
literal|null
argument_list|,
name|makeDataSource
argument_list|(
name|classLoader
argument_list|)
argument_list|)
return|;
block|}
try|try
block|{
return|return
name|appInstance
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|AdhocObjectFactory
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|,
name|adapterClassName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|th
operator|=
name|Util
operator|.
name|unwindException
argument_list|(
name|th
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Exception
argument_list|(
literal|"DbAdapter load error: "
operator|+
name|th
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/** 	 * Returns a DataSource that uses connection information from this object. 	 * Returned DataSource is not pooling its connections. It can be wrapped in 	 * PoolManager if pooling is needed. 	 */
specifier|public
name|DataSource
name|makeDataSource
parameter_list|(
specifier|final
name|ClassLoadingService
name|classLoader
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// validate...
if|if
condition|(
name|getJdbcDriver
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"No JDBC driver set."
argument_list|)
throw|;
block|}
if|if
condition|(
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
literal|"No DB URL set."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|Util
operator|.
name|isBlank
argument_list|(
name|getPassword
argument_list|()
argument_list|)
operator|&&
name|Util
operator|.
name|isBlank
argument_list|(
name|getUserName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"No username when password is set."
argument_list|)
throw|;
block|}
comment|// load driver...
name|Driver
name|driver
decl_stmt|;
try|try
block|{
name|driver
operator|=
name|classLoader
operator|.
name|loadClass
argument_list|(
name|Driver
operator|.
name|class
argument_list|,
name|getJdbcDriver
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|th
operator|=
name|Util
operator|.
name|unwindException
argument_list|(
name|th
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Driver load error: "
operator|+
name|th
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
throw|;
block|}
return|return
operator|new
name|DriverDataSource
argument_list|(
name|driver
argument_list|,
name|getUrl
argument_list|()
argument_list|,
name|getUserName
argument_list|()
argument_list|,
name|getPassword
argument_list|()
argument_list|)
return|;
block|}
comment|/** 	 * Updates another DBConnectionInfo with this object's values. 	 */
specifier|public
name|boolean
name|copyTo
parameter_list|(
specifier|final
name|DBConnectionInfo
name|dataSourceInfo
parameter_list|)
block|{
name|boolean
name|updated
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dataSourceInfo
operator|.
name|getUrl
argument_list|()
argument_list|,
name|getUrl
argument_list|()
argument_list|)
condition|)
block|{
name|dataSourceInfo
operator|.
name|setUrl
argument_list|(
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dataSourceInfo
operator|.
name|getUserName
argument_list|()
argument_list|,
name|getUserName
argument_list|()
argument_list|)
condition|)
block|{
name|dataSourceInfo
operator|.
name|setUserName
argument_list|(
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dataSourceInfo
operator|.
name|getPassword
argument_list|()
argument_list|,
name|getPassword
argument_list|()
argument_list|)
condition|)
block|{
name|dataSourceInfo
operator|.
name|setPassword
argument_list|(
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dataSourceInfo
operator|.
name|getJdbcDriver
argument_list|()
argument_list|,
name|getJdbcDriver
argument_list|()
argument_list|)
condition|)
block|{
name|dataSourceInfo
operator|.
name|setJdbcDriver
argument_list|(
name|getJdbcDriver
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dataSourceInfo
operator|.
name|getDbAdapter
argument_list|()
argument_list|,
name|getDbAdapter
argument_list|()
argument_list|)
condition|)
block|{
name|dataSourceInfo
operator|.
name|setDbAdapter
argument_list|(
name|getDbAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|updated
return|;
block|}
comment|/** 	 * Updates DataSourceInfo with this object's values. 	 *<p> 	 *<i>Currently doesn't set the adapter property. Need to change the UI to 	 * handle adapter via DataSourceInfo first, and then it should be safe to do 	 * an adapter update here.</i> 	 *</p> 	 */
specifier|public
name|boolean
name|copyTo
parameter_list|(
specifier|final
name|DataSourceInfo
name|dataSourceInfo
parameter_list|)
block|{
name|boolean
name|updated
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dataSourceInfo
operator|.
name|getDataSourceUrl
argument_list|()
argument_list|,
name|getUrl
argument_list|()
argument_list|)
condition|)
block|{
name|dataSourceInfo
operator|.
name|setDataSourceUrl
argument_list|(
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dataSourceInfo
operator|.
name|getUserName
argument_list|()
argument_list|,
name|getUserName
argument_list|()
argument_list|)
condition|)
block|{
name|dataSourceInfo
operator|.
name|setUserName
argument_list|(
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dataSourceInfo
operator|.
name|getPassword
argument_list|()
argument_list|,
name|getPassword
argument_list|()
argument_list|)
condition|)
block|{
name|dataSourceInfo
operator|.
name|setPassword
argument_list|(
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dataSourceInfo
operator|.
name|getJdbcDriver
argument_list|()
argument_list|,
name|getJdbcDriver
argument_list|()
argument_list|)
condition|)
block|{
name|dataSourceInfo
operator|.
name|setJdbcDriver
argument_list|(
name|getJdbcDriver
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|updated
return|;
block|}
specifier|public
name|boolean
name|copyFrom
parameter_list|(
specifier|final
name|DataSourceInfo
name|dataSourceInfo
parameter_list|)
block|{
name|boolean
name|updated
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dataSourceInfo
operator|.
name|getDataSourceUrl
argument_list|()
argument_list|,
name|getUrl
argument_list|()
argument_list|)
condition|)
block|{
name|setUrl
argument_list|(
name|dataSourceInfo
operator|.
name|getDataSourceUrl
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dataSourceInfo
operator|.
name|getUserName
argument_list|()
argument_list|,
name|getUserName
argument_list|()
argument_list|)
condition|)
block|{
name|setUserName
argument_list|(
name|dataSourceInfo
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dataSourceInfo
operator|.
name|getPassword
argument_list|()
argument_list|,
name|getPassword
argument_list|()
argument_list|)
condition|)
block|{
name|setPassword
argument_list|(
name|dataSourceInfo
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dataSourceInfo
operator|.
name|getJdbcDriver
argument_list|()
argument_list|,
name|getJdbcDriver
argument_list|()
argument_list|)
condition|)
block|{
name|setJdbcDriver
argument_list|(
name|dataSourceInfo
operator|.
name|getJdbcDriver
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|updated
return|;
block|}
block|}
end_class

end_unit

