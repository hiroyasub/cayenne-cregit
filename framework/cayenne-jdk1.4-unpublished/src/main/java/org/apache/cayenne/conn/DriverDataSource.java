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
name|conn
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
name|Connection
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
name|java
operator|.
name|sql
operator|.
name|DriverManager
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
name|Properties
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * A non-pooling DataSource implementation wrapping a JDBC driver.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DriverDataSource
implements|implements
name|DataSource
block|{
specifier|protected
name|Driver
name|driver
decl_stmt|;
specifier|protected
name|String
name|connectionUrl
decl_stmt|;
specifier|protected
name|String
name|userName
decl_stmt|;
specifier|protected
name|String
name|password
decl_stmt|;
specifier|protected
name|ConnectionEventLoggingDelegate
name|logger
decl_stmt|;
comment|/**      * Loads JDBC driver using current thread class loader.      *       * @since 3.0      */
specifier|private
specifier|static
name|Driver
name|loadDriver
parameter_list|(
name|String
name|driverClassName
parameter_list|)
throws|throws
name|SQLException
block|{
name|Class
name|driverClass
decl_stmt|;
try|try
block|{
name|driverClass
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|driverClassName
argument_list|,
literal|true
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Can not load JDBC driver named '"
operator|+
name|driverClassName
operator|+
literal|"': "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
try|try
block|{
return|return
operator|(
name|Driver
operator|)
name|driverClass
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Error instantiating driver '"
operator|+
name|driverClassName
operator|+
literal|"': "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Creates a new DriverDataSource.      */
specifier|public
name|DriverDataSource
parameter_list|(
name|String
name|driverClassName
parameter_list|,
name|String
name|connectionUrl
parameter_list|)
throws|throws
name|SQLException
block|{
name|this
argument_list|(
name|driverClassName
argument_list|,
name|connectionUrl
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|DriverDataSource
parameter_list|(
name|String
name|driverClassName
parameter_list|,
name|String
name|connectionUrl
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|SQLException
block|{
name|setDriverClassName
argument_list|(
name|driverClassName
argument_list|)
expr_stmt|;
name|this
operator|.
name|connectionUrl
operator|=
name|connectionUrl
expr_stmt|;
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
comment|/**      * Creates a new DriverDataSource wrapping a given Driver.      *       * @since 1.1      */
specifier|public
name|DriverDataSource
parameter_list|(
name|Driver
name|driver
parameter_list|,
name|String
name|connectionUrl
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|driver
operator|=
name|driver
expr_stmt|;
name|this
operator|.
name|connectionUrl
operator|=
name|connectionUrl
expr_stmt|;
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
comment|/**      * Returns a new database connection, using preconfigured data to locate the database      * and obtain a connection.      */
specifier|public
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|SQLException
block|{
comment|// login with internal credentials
return|return
name|getConnection
argument_list|(
name|userName
argument_list|,
name|password
argument_list|)
return|;
block|}
comment|/**      * Returns a new database connection using provided credentials to login to the      * database.      */
specifier|public
name|Connection
name|getConnection
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|SQLException
block|{
try|try
block|{
if|if
condition|(
name|logger
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|logConnect
argument_list|(
name|connectionUrl
argument_list|,
name|userName
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
name|Connection
name|c
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|driver
operator|==
literal|null
condition|)
block|{
name|c
operator|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|connectionUrl
argument_list|,
name|userName
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Properties
name|connectProperties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
if|if
condition|(
name|userName
operator|!=
literal|null
condition|)
block|{
name|connectProperties
operator|.
name|put
argument_list|(
literal|"user"
argument_list|,
name|userName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|password
operator|!=
literal|null
condition|)
block|{
name|connectProperties
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
name|c
operator|=
name|driver
operator|.
name|connect
argument_list|(
name|connectionUrl
argument_list|,
name|connectProperties
argument_list|)
expr_stmt|;
block|}
comment|// some drivers (Oracle) return null connections instead of throwing
comment|// an exception... fix it here
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Can't establish connection: "
operator|+
name|connectionUrl
argument_list|)
throw|;
block|}
if|if
condition|(
name|logger
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|logConnectSuccess
argument_list|()
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|sqlex
parameter_list|)
block|{
if|if
condition|(
name|logger
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|logConnectFailure
argument_list|(
name|sqlex
argument_list|)
expr_stmt|;
block|}
throw|throw
name|sqlex
throw|;
block|}
block|}
specifier|public
name|int
name|getLoginTimeout
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|void
name|setLoginTimeout
parameter_list|(
name|int
name|seconds
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// noop
block|}
specifier|public
name|PrintWriter
name|getLogWriter
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|DriverManager
operator|.
name|getLogWriter
argument_list|()
return|;
block|}
specifier|public
name|void
name|setLogWriter
parameter_list|(
name|PrintWriter
name|out
parameter_list|)
throws|throws
name|SQLException
block|{
name|DriverManager
operator|.
name|setLogWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ConnectionEventLoggingDelegate
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
specifier|public
name|void
name|setLogger
parameter_list|(
name|ConnectionEventLoggingDelegate
name|delegate
parameter_list|)
block|{
name|logger
operator|=
name|delegate
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|String
name|getConnectionUrl
parameter_list|()
block|{
return|return
name|connectionUrl
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|setConnectionUrl
parameter_list|(
name|String
name|connectionUrl
parameter_list|)
block|{
name|this
operator|.
name|connectionUrl
operator|=
name|connectionUrl
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|setPassword
parameter_list|(
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
comment|/**      * @since 3.0      */
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|setUserName
parameter_list|(
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
name|String
name|getDriverClassName
parameter_list|()
block|{
return|return
name|driver
operator|!=
literal|null
condition|?
name|driver
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|null
return|;
block|}
specifier|public
name|void
name|setDriverClassName
parameter_list|(
name|String
name|driverClassName
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|getDriverClassName
argument_list|()
argument_list|,
name|driverClassName
argument_list|)
condition|)
block|{
name|this
operator|.
name|driver
operator|=
name|driverClassName
operator|!=
literal|null
condition|?
name|loadDriver
argument_list|(
name|driverClassName
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

