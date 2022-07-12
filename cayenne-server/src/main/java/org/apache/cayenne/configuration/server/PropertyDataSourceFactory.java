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
name|configuration
operator|.
name|server
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
name|configuration
operator|.
name|DataNodeDescriptor
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
name|RuntimeProperties
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
name|DataSourceBuilder
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
name|UnmanagedPoolingDataSource
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
name|Inject
import|;
end_import

begin_comment
comment|/**  * A DataSourceFactrory that creates a DataSource based on system properties.  * Properties can be set per domain/node name or globally, applying to all nodes  * without explicit property set. The following properties are supported:  *<ul>  *<li>cayenne.jdbc.driver[.domain_name.node_name]  *<li>cayenne.jdbc.url[.domain_name.node_name]  *<li>cayenne.jdbc.username[.domain_name.node_name]  *<li>cayenne.jdbc.password[.domain_name.node_name]  *<li>cayenne.jdbc.min.connections[.domain_name.node_name]  *<li>cayenne.jdbc.max.conections[.domain_name.node_name]  *</ul>  * At least url and driver properties must be specified for this factory to  * return a valid DataSource.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|PropertyDataSourceFactory
implements|implements
name|DataSourceFactory
block|{
annotation|@
name|Inject
specifier|protected
name|RuntimeProperties
name|properties
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
annotation|@
name|Override
specifier|public
name|DataSource
name|getDataSource
parameter_list|(
name|DataNodeDescriptor
name|nodeDescriptor
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|suffix
init|=
literal|"."
operator|+
name|nodeDescriptor
operator|.
name|getDataChannelDescriptor
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|nodeDescriptor
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|driverClass
init|=
name|getProperty
argument_list|(
name|Constants
operator|.
name|JDBC_DRIVER_PROPERTY
argument_list|,
name|suffix
argument_list|)
decl_stmt|;
name|String
name|url
init|=
name|getProperty
argument_list|(
name|Constants
operator|.
name|JDBC_URL_PROPERTY
argument_list|,
name|suffix
argument_list|)
decl_stmt|;
name|String
name|username
init|=
name|getProperty
argument_list|(
name|Constants
operator|.
name|JDBC_USERNAME_PROPERTY
argument_list|,
name|suffix
argument_list|)
decl_stmt|;
name|String
name|password
init|=
name|getProperty
argument_list|(
name|Constants
operator|.
name|JDBC_PASSWORD_PROPERTY
argument_list|,
name|suffix
argument_list|)
decl_stmt|;
name|int
name|minConnections
init|=
name|getIntProperty
argument_list|(
name|Constants
operator|.
name|JDBC_MIN_CONNECTIONS_PROPERTY
argument_list|,
name|suffix
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|int
name|maxConnections
init|=
name|getIntProperty
argument_list|(
name|Constants
operator|.
name|JDBC_MAX_CONNECTIONS_PROPERTY
argument_list|,
name|suffix
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|long
name|maxQueueWaitTime
init|=
name|properties
operator|.
name|getLong
argument_list|(
name|Constants
operator|.
name|JDBC_MAX_QUEUE_WAIT_TIME
argument_list|,
name|UnmanagedPoolingDataSource
operator|.
name|MAX_QUEUE_WAIT_DEFAULT
argument_list|)
decl_stmt|;
name|String
name|validationQuery
init|=
name|properties
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|JDBC_VALIDATION_QUERY_PROPERTY
argument_list|)
decl_stmt|;
name|Driver
name|driver
init|=
operator|(
name|Driver
operator|)
name|objectFactory
operator|.
name|getJavaClass
argument_list|(
name|driverClass
argument_list|)
operator|.
name|getDeclaredConstructor
argument_list|()
operator|.
name|newInstance
argument_list|()
decl_stmt|;
return|return
name|DataSourceBuilder
operator|.
name|url
argument_list|(
name|url
argument_list|)
operator|.
name|driver
argument_list|(
name|driver
argument_list|)
operator|.
name|userName
argument_list|(
name|username
argument_list|)
operator|.
name|password
argument_list|(
name|password
argument_list|)
operator|.
name|pool
argument_list|(
name|minConnections
argument_list|,
name|maxConnections
argument_list|)
operator|.
name|maxQueueWaitTime
argument_list|(
name|maxQueueWaitTime
argument_list|)
operator|.
name|validationQuery
argument_list|(
name|validationQuery
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|protected
name|int
name|getIntProperty
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|String
name|suffix
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
name|String
name|string
init|=
name|getProperty
argument_list|(
name|propertyName
argument_list|,
name|suffix
argument_list|)
decl_stmt|;
if|if
condition|(
name|string
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
try|try
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|string
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"Invalid int property '%s': '%s'"
argument_list|,
name|propertyName
argument_list|,
name|string
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|String
name|getProperty
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|String
name|suffix
parameter_list|)
block|{
name|String
name|value
init|=
name|properties
operator|.
name|get
argument_list|(
name|propertyName
operator|+
name|suffix
argument_list|)
decl_stmt|;
return|return
name|value
operator|!=
literal|null
condition|?
name|value
else|:
name|properties
operator|.
name|get
argument_list|(
name|propertyName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

