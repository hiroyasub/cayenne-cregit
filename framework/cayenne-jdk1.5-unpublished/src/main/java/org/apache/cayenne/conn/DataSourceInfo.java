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
name|Serializable
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
name|PasswordEncoding
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
name|PlainTextPasswordEncoder
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
comment|/**  * Helper JavaBean class that holds DataSource login information.  *<p>  *<i>For more information see<a href="../../../../../../userguide/index.html"  * target="_top">Cayenne User Guide.</a></i>  *</p>  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DataSourceInfo
implements|implements
name|Cloneable
implements|,
name|Serializable
block|{
specifier|protected
name|String
name|userName
decl_stmt|;
specifier|protected
name|String
name|password
decl_stmt|;
specifier|protected
name|String
name|jdbcDriver
decl_stmt|;
specifier|protected
name|String
name|dataSourceUrl
decl_stmt|;
specifier|protected
name|String
name|adapterClassName
decl_stmt|;
specifier|protected
name|int
name|minConnections
init|=
literal|1
decl_stmt|;
specifier|protected
name|int
name|maxConnections
init|=
literal|1
decl_stmt|;
comment|// Constants for passwordLocation
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD_LOCATION_CLASSPATH
init|=
literal|"classpath"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD_LOCATION_EXECUTABLE
init|=
literal|"executable"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD_LOCATION_MODEL
init|=
literal|"model"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD_LOCATION_URL
init|=
literal|"url"
decl_stmt|;
comment|// Extended parameters
specifier|protected
name|String
name|passwordEncoderClass
init|=
name|PasswordEncoding
operator|.
name|standardEncoders
index|[
literal|0
index|]
decl_stmt|;
specifier|protected
name|String
name|passwordEncoderKey
init|=
literal|""
decl_stmt|;
specifier|protected
name|String
name|passwordLocation
init|=
name|PASSWORD_LOCATION_MODEL
decl_stmt|;
specifier|protected
name|String
name|passwordSourceExecutable
init|=
literal|""
decl_stmt|;
specifier|protected
name|String
name|passwordSourceFilename
init|=
literal|""
decl_stmt|;
specifier|protected
name|String
name|passwordSourceModel
init|=
literal|"Not Applicable"
decl_stmt|;
specifier|protected
name|String
name|passwordSourceUrl
init|=
literal|""
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
name|this
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|obj
operator|.
name|getClass
argument_list|()
operator|!=
name|this
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|DataSourceInfo
name|dsi
init|=
operator|(
name|DataSourceInfo
operator|)
name|obj
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|userName
argument_list|,
name|dsi
operator|.
name|userName
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|password
argument_list|,
name|dsi
operator|.
name|password
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|jdbcDriver
argument_list|,
name|dsi
operator|.
name|jdbcDriver
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|dataSourceUrl
argument_list|,
name|dsi
operator|.
name|dataSourceUrl
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|adapterClassName
argument_list|,
name|dsi
operator|.
name|adapterClassName
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|this
operator|.
name|minConnections
operator|!=
name|dsi
operator|.
name|minConnections
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|this
operator|.
name|maxConnections
operator|!=
name|dsi
operator|.
name|maxConnections
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|passwordEncoderClass
argument_list|,
name|dsi
operator|.
name|passwordEncoderClass
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|passwordEncoderKey
argument_list|,
name|dsi
operator|.
name|passwordEncoderKey
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|passwordSourceFilename
argument_list|,
name|dsi
operator|.
name|passwordSourceFilename
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|passwordSourceModel
argument_list|,
name|dsi
operator|.
name|passwordSourceModel
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|passwordSourceUrl
argument_list|,
name|dsi
operator|.
name|passwordSourceUrl
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|passwordLocation
argument_list|,
name|dsi
operator|.
name|passwordLocation
argument_list|)
condition|)
return|return
literal|false
return|;
return|return
literal|true
return|;
block|}
specifier|public
name|DataSourceInfo
name|cloneInfo
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|DataSourceInfo
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cloning error"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"["
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n   user name: "
argument_list|)
operator|.
name|append
argument_list|(
name|userName
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n   password: "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"**********"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\n   driver: "
argument_list|)
operator|.
name|append
argument_list|(
name|jdbcDriver
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n   db adapter class: "
argument_list|)
operator|.
name|append
argument_list|(
name|adapterClassName
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n   url: "
argument_list|)
operator|.
name|append
argument_list|(
name|dataSourceUrl
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n   min. connections: "
argument_list|)
operator|.
name|append
argument_list|(
name|minConnections
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n   max. connections: "
argument_list|)
operator|.
name|append
argument_list|(
name|maxConnections
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|PlainTextPasswordEncoder
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|passwordEncoderClass
argument_list|)
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\n   encoder class: "
argument_list|)
operator|.
name|append
argument_list|(
name|passwordEncoderClass
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n   encoder key: "
argument_list|)
operator|.
name|append
argument_list|(
name|passwordEncoderKey
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|PASSWORD_LOCATION_MODEL
operator|.
name|equals
argument_list|(
name|passwordLocation
argument_list|)
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\n   password location: "
argument_list|)
operator|.
name|append
argument_list|(
name|passwordLocation
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n   password source: "
argument_list|)
operator|.
name|append
argument_list|(
name|getPasswordSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"\n]"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|getAdapterClassName
parameter_list|()
block|{
return|return
name|adapterClassName
return|;
block|}
specifier|public
name|void
name|setAdapterClassName
parameter_list|(
name|String
name|adapterClassName
parameter_list|)
block|{
name|this
operator|.
name|adapterClassName
operator|=
name|adapterClassName
expr_stmt|;
block|}
specifier|public
name|void
name|setMinConnections
parameter_list|(
name|int
name|minConnections
parameter_list|)
block|{
name|this
operator|.
name|minConnections
operator|=
name|minConnections
expr_stmt|;
block|}
specifier|public
name|int
name|getMinConnections
parameter_list|()
block|{
return|return
name|minConnections
return|;
block|}
specifier|public
name|void
name|setMaxConnections
parameter_list|(
name|int
name|maxConnections
parameter_list|)
block|{
name|this
operator|.
name|maxConnections
operator|=
name|maxConnections
expr_stmt|;
block|}
specifier|public
name|int
name|getMaxConnections
parameter_list|()
block|{
return|return
name|maxConnections
return|;
block|}
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
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
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
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
specifier|public
name|void
name|setJdbcDriver
parameter_list|(
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
name|getJdbcDriver
parameter_list|()
block|{
return|return
name|jdbcDriver
return|;
block|}
specifier|public
name|void
name|setDataSourceUrl
parameter_list|(
name|String
name|dataSourceUrl
parameter_list|)
block|{
name|this
operator|.
name|dataSourceUrl
operator|=
name|dataSourceUrl
expr_stmt|;
block|}
specifier|public
name|String
name|getDataSourceUrl
parameter_list|()
block|{
return|return
name|dataSourceUrl
return|;
block|}
specifier|public
name|PasswordEncoding
name|getPasswordEncoder
parameter_list|()
block|{
name|PasswordEncoding
name|encoder
init|=
literal|null
decl_stmt|;
try|try
block|{
name|encoder
operator|=
operator|(
name|PasswordEncoding
operator|)
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|getPasswordEncoderClass
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
comment|// encoder = (PasswordEncoding)
comment|// Class.forName(getPasswordEncoderClass()).newInstance();
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|exception
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|exception
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|exception
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|exception
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|exception
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|exception
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|encoder
return|;
block|}
comment|/**      * @return the passwordEncoderClass      */
specifier|public
name|String
name|getPasswordEncoderClass
parameter_list|()
block|{
return|return
name|passwordEncoderClass
return|;
block|}
comment|/**      * @param passwordEncoderClass the passwordEncoderClass to set      */
specifier|public
name|void
name|setPasswordEncoderClass
parameter_list|(
name|String
name|passwordEncoderClass
parameter_list|)
block|{
if|if
condition|(
name|passwordEncoderClass
operator|==
literal|null
condition|)
name|this
operator|.
name|passwordEncoderClass
operator|=
name|PasswordEncoding
operator|.
name|standardEncoders
index|[
literal|0
index|]
expr_stmt|;
else|else
name|this
operator|.
name|passwordEncoderClass
operator|=
name|passwordEncoderClass
expr_stmt|;
block|}
comment|/**      * @return the passwordEncoderKey      */
specifier|public
name|String
name|getPasswordEncoderKey
parameter_list|()
block|{
return|return
name|passwordEncoderKey
return|;
block|}
comment|/**      * @param passwordEncoderKey the passwordEncoderKey to set      */
specifier|public
name|void
name|setPasswordEncoderKey
parameter_list|(
name|String
name|passwordEncoderKey
parameter_list|)
block|{
name|this
operator|.
name|passwordEncoderKey
operator|=
name|passwordEncoderKey
expr_stmt|;
block|}
comment|/**      * @return the passwordLocationFilename      */
specifier|public
name|String
name|getPasswordSourceFilename
parameter_list|()
block|{
return|return
name|passwordSourceFilename
return|;
block|}
comment|/**      * @param passwordSourceFilename the passwordSourceFilename to set      */
specifier|public
name|void
name|setPasswordSourceFilename
parameter_list|(
name|String
name|passwordSourceFilename
parameter_list|)
block|{
name|this
operator|.
name|passwordSourceFilename
operator|=
name|passwordSourceFilename
expr_stmt|;
block|}
comment|/**      * @return the passwordLocationModel      */
specifier|public
name|String
name|getPasswordSourceModel
parameter_list|()
block|{
return|return
name|passwordSourceModel
return|;
block|}
comment|/**      * @return the passwordLocationUrl      */
specifier|public
name|String
name|getPasswordSourceUrl
parameter_list|()
block|{
return|return
name|passwordSourceUrl
return|;
block|}
comment|/**      * @param passwordSourceUrl the passwordSourceUrl to set      */
specifier|public
name|void
name|setPasswordSourceUrl
parameter_list|(
name|String
name|passwordSourceUrl
parameter_list|)
block|{
name|this
operator|.
name|passwordSourceUrl
operator|=
name|passwordSourceUrl
expr_stmt|;
block|}
comment|/**      * @return the passwordLocationExecutable      */
specifier|public
name|String
name|getPasswordSourceExecutable
parameter_list|()
block|{
return|return
name|passwordSourceExecutable
return|;
block|}
comment|/**      * @param passwordSourceExecutable the passwordSourceExecutable to set      */
specifier|public
name|void
name|setPasswordSourceExecutable
parameter_list|(
name|String
name|passwordSourceExecutable
parameter_list|)
block|{
name|this
operator|.
name|passwordSourceExecutable
operator|=
name|passwordSourceExecutable
expr_stmt|;
block|}
specifier|public
name|String
name|getPasswordSource
parameter_list|()
block|{
if|if
condition|(
name|getPasswordLocation
argument_list|()
operator|.
name|equals
argument_list|(
name|PASSWORD_LOCATION_CLASSPATH
argument_list|)
condition|)
return|return
name|getPasswordSourceFilename
argument_list|()
return|;
if|else if
condition|(
name|getPasswordLocation
argument_list|()
operator|.
name|equals
argument_list|(
name|PASSWORD_LOCATION_EXECUTABLE
argument_list|)
condition|)
return|return
name|getPasswordSourceExecutable
argument_list|()
return|;
if|else if
condition|(
name|getPasswordLocation
argument_list|()
operator|.
name|equals
argument_list|(
name|PASSWORD_LOCATION_MODEL
argument_list|)
condition|)
return|return
name|getPasswordSourceModel
argument_list|()
return|;
if|else if
condition|(
name|getPasswordLocation
argument_list|()
operator|.
name|equals
argument_list|(
name|PASSWORD_LOCATION_URL
argument_list|)
condition|)
return|return
name|getPasswordSourceUrl
argument_list|()
return|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Invalid password source detected"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|setPasswordSource
parameter_list|(
name|String
name|passwordSource
parameter_list|)
block|{
comment|// The location for the model is omitted since it cannot change
if|if
condition|(
name|getPasswordLocation
argument_list|()
operator|.
name|equals
argument_list|(
name|PASSWORD_LOCATION_CLASSPATH
argument_list|)
condition|)
name|setPasswordSourceFilename
argument_list|(
name|passwordSource
argument_list|)
expr_stmt|;
if|else if
condition|(
name|getPasswordLocation
argument_list|()
operator|.
name|equals
argument_list|(
name|PASSWORD_LOCATION_EXECUTABLE
argument_list|)
condition|)
name|setPasswordSourceExecutable
argument_list|(
name|passwordSource
argument_list|)
expr_stmt|;
if|else if
condition|(
name|getPasswordLocation
argument_list|()
operator|.
name|equals
argument_list|(
name|PASSWORD_LOCATION_URL
argument_list|)
condition|)
name|setPasswordSourceUrl
argument_list|(
name|passwordSource
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return the passwordLocation      */
specifier|public
name|String
name|getPasswordLocation
parameter_list|()
block|{
return|return
name|passwordLocation
return|;
block|}
comment|/**      * @param passwordLocation the passwordLocation to set      */
specifier|public
name|void
name|setPasswordLocation
parameter_list|(
name|String
name|passwordLocation
parameter_list|)
block|{
if|if
condition|(
name|passwordLocation
operator|==
literal|null
condition|)
name|this
operator|.
name|passwordLocation
operator|=
name|DataSourceInfo
operator|.
name|PASSWORD_LOCATION_MODEL
expr_stmt|;
else|else
name|this
operator|.
name|passwordLocation
operator|=
name|passwordLocation
expr_stmt|;
block|}
block|}
end_class

end_unit

