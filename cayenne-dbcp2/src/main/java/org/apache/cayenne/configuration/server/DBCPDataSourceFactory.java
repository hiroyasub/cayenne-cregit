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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|resource
operator|.
name|Resource
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
name|dbcp2
operator|.
name|BasicDataSourceFactory
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

begin_comment
comment|/**  * A {@link DataSourceFactory} based on DBCP2 connection pool library.  */
end_comment

begin_class
specifier|public
class|class
name|DBCPDataSourceFactory
implements|implements
name|DataSourceFactory
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DBCP2_PROPERTIES
init|=
literal|"dbcp2.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DBCPDataSourceFactory
operator|.
name|class
argument_list|)
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
name|location
init|=
name|nodeDescriptor
operator|.
name|getParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|location
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"No explicit DBCP2 config location, will use default location: "
operator|+
name|DBCP2_PROPERTIES
argument_list|)
expr_stmt|;
name|location
operator|=
name|DBCP2_PROPERTIES
expr_stmt|;
block|}
name|Resource
name|baseConfiguration
init|=
name|nodeDescriptor
operator|.
name|getConfigurationSource
argument_list|()
decl_stmt|;
if|if
condition|(
name|baseConfiguration
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null 'configurationSource' for nodeDescriptor '%s'"
argument_list|,
name|nodeDescriptor
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|Resource
name|dbcp2Configuration
init|=
name|baseConfiguration
operator|.
name|getRelativeResource
argument_list|(
name|location
argument_list|)
decl_stmt|;
if|if
condition|(
name|dbcp2Configuration
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Missing DBCP2 configuration '%s' for nodeDescriptor '%s'"
argument_list|,
name|location
argument_list|,
name|nodeDescriptor
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|Properties
name|properties
init|=
name|getProperties
argument_list|(
name|dbcp2Configuration
argument_list|)
decl_stmt|;
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"DBCP2 Properties: "
operator|+
name|properties
argument_list|)
expr_stmt|;
block|}
return|return
name|BasicDataSourceFactory
operator|.
name|createDataSource
argument_list|(
name|properties
argument_list|)
return|;
block|}
specifier|private
name|Properties
name|getProperties
parameter_list|(
name|Resource
name|dbcp2Configuration
parameter_list|)
throws|throws
name|IOException
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|dbcp2Configuration
operator|.
name|getURL
argument_list|()
operator|.
name|openStream
argument_list|()
init|;
init|)
block|{
name|properties
operator|.
name|load
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
return|return
name|properties
return|;
block|}
block|}
end_class

end_unit

