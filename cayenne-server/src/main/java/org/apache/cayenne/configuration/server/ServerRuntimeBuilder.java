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
name|configuration
operator|.
name|server
package|;
end_package

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
name|LinkedHashSet
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
name|access
operator|.
name|DataDomain
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
name|Constants
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
name|Binder
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
name|MapBuilder
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
name|Module
import|;
end_import

begin_comment
comment|/**  * A convenience class to assemble custom ServerRuntime. It allows to easily  * configure custom modules, multiple config locations, or quickly create a  * global DataSource.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ServerRuntimeBuilder
block|{
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|configs
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Module
argument_list|>
name|modules
decl_stmt|;
specifier|private
name|DataSourceFactory
name|dataSourceFactory
decl_stmt|;
specifier|private
name|String
name|jdbcUrl
decl_stmt|;
specifier|private
name|String
name|jdbcDriver
decl_stmt|;
specifier|private
name|String
name|jdbcUser
decl_stmt|;
specifier|private
name|String
name|jdbcPassword
decl_stmt|;
specifier|private
name|int
name|jdbcMinConnections
decl_stmt|;
specifier|private
name|int
name|jdbcMaxConnections
decl_stmt|;
comment|/** 	 * Creates an empty builder. 	 */
specifier|public
name|ServerRuntimeBuilder
parameter_list|()
block|{
name|this
operator|.
name|configs
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|modules
operator|=
operator|new
name|ArrayList
argument_list|<
name|Module
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * An equivalent to creating builder with default constructor and calling 	 * {@link #addConfig(String)}. 	 */
specifier|public
name|ServerRuntimeBuilder
parameter_list|(
name|String
name|configurationLocation
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|addConfig
argument_list|(
name|configurationLocation
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Sets a DataSource that will override any DataSources found in the 	 * mapping. Moreover if the mapping contains no DataNodes, and the 	 * DataSource is set with this method, the builder would create a single 	 * default DataNode. 	 */
specifier|public
name|ServerRuntimeBuilder
name|dataSource
parameter_list|(
name|DataSource
name|dataSource
parameter_list|)
block|{
name|this
operator|.
name|dataSourceFactory
operator|=
operator|new
name|FixedDataSourceFactory
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Sets JNDI location for the default DataSource. 	 */
specifier|public
name|ServerRuntimeBuilder
name|jndiDataSource
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|this
operator|.
name|dataSourceFactory
operator|=
operator|new
name|FixedJNDIDataSourceFactory
argument_list|(
name|location
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Sets a database URL for the default DataSource. 	 */
specifier|public
name|ServerRuntimeBuilder
name|url
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|jdbcUrl
operator|=
name|url
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Sets a driver Java class for the default DataSource. 	 */
specifier|public
name|ServerRuntimeBuilder
name|jdbcDriver
parameter_list|(
name|String
name|driver
parameter_list|)
block|{
comment|// TODO: guess the driver from URL
name|this
operator|.
name|jdbcDriver
operator|=
name|driver
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Sets a user name for the default DataSource. 	 */
specifier|public
name|ServerRuntimeBuilder
name|user
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|this
operator|.
name|jdbcUser
operator|=
name|user
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Sets a password for the default DataSource. 	 */
specifier|public
name|ServerRuntimeBuilder
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|jdbcPassword
operator|=
name|password
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ServerRuntimeBuilder
name|minConnections
parameter_list|(
name|int
name|minConnections
parameter_list|)
block|{
name|this
operator|.
name|jdbcMinConnections
operator|=
name|minConnections
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ServerRuntimeBuilder
name|maxConnections
parameter_list|(
name|int
name|maxConnections
parameter_list|)
block|{
name|this
operator|.
name|jdbcMaxConnections
operator|=
name|maxConnections
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ServerRuntimeBuilder
name|addConfig
parameter_list|(
name|String
name|configurationLocation
parameter_list|)
block|{
name|configs
operator|.
name|add
argument_list|(
name|configurationLocation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ServerRuntimeBuilder
name|addConfigs
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|configurationLocations
parameter_list|)
block|{
name|configs
operator|.
name|addAll
argument_list|(
name|configurationLocations
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ServerRuntimeBuilder
name|addModule
parameter_list|(
name|Module
name|module
parameter_list|)
block|{
name|modules
operator|.
name|add
argument_list|(
name|module
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ServerRuntimeBuilder
name|addModules
parameter_list|(
name|Collection
argument_list|<
name|Module
argument_list|>
name|modules
parameter_list|)
block|{
name|this
operator|.
name|modules
operator|.
name|addAll
argument_list|(
name|modules
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ServerRuntime
name|build
parameter_list|()
block|{
name|buildModules
argument_list|()
expr_stmt|;
name|String
index|[]
name|configs
init|=
name|this
operator|.
name|configs
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|this
operator|.
name|configs
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|Module
index|[]
name|modules
init|=
name|this
operator|.
name|modules
operator|.
name|toArray
argument_list|(
operator|new
name|Module
index|[
name|this
operator|.
name|modules
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
return|return
operator|new
name|ServerRuntime
argument_list|(
name|configs
argument_list|,
name|modules
argument_list|)
return|;
block|}
specifier|private
name|void
name|buildModules
parameter_list|()
block|{
if|if
condition|(
name|dataSourceFactory
operator|!=
literal|null
condition|)
block|{
name|prepend
argument_list|(
operator|new
name|Module
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|DataDomain
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|SyntheticNodeDataDomainProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataSourceFactory
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|dataSourceFactory
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// URL and driver are the minimal requirement for
comment|// DelegatingDataSourceFactory to work
if|else if
condition|(
name|jdbcUrl
operator|!=
literal|null
operator|&&
name|jdbcDriver
operator|!=
literal|null
condition|)
block|{
name|prepend
argument_list|(
operator|new
name|Module
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|DataDomain
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|SyntheticNodeDataDomainProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|MapBuilder
argument_list|<
name|Object
argument_list|>
name|props
init|=
name|binder
operator|.
name|bindMap
argument_list|(
name|Constants
operator|.
name|PROPERTIES_MAP
argument_list|)
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|JDBC_DRIVER_PROPERTY
argument_list|,
name|jdbcDriver
argument_list|)
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|JDBC_URL_PROPERTY
argument_list|,
name|jdbcUrl
argument_list|)
decl_stmt|;
if|if
condition|(
name|jdbcUser
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|JDBC_USERNAME_PROPERTY
argument_list|,
name|jdbcUser
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jdbcPassword
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|JDBC_PASSWORD_PROPERTY
argument_list|,
name|jdbcPassword
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jdbcMinConnections
operator|>
literal|0
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|JDBC_MIN_CONNECTIONS_PROPERTY
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|jdbcMinConnections
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jdbcMaxConnections
operator|>
literal|0
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|JDBC_MAX_CONNECTIONS_PROPERTY
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|jdbcMaxConnections
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|prepend
parameter_list|(
name|Module
name|module
parameter_list|)
block|{
comment|// prepend any special modules BEFORE custom modules, to allow callers
comment|// to override our stuff
name|modules
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|module
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

