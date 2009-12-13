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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|di
operator|.
name|Inject
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
name|cayenne
operator|.
name|resource
operator|.
name|ResourceLocator
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
name|dbcp
operator|.
name|BasicDataSourceFactory
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A {@link DataSourceFactory} based on DBCP connection pool library.  *   * @since 3.1  */
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
name|DBCP_PROPERTIES
init|=
literal|"dbcp.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DBCPDataSourceFactory
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ResourceLocator
name|resourceLocator
decl_stmt|;
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
literal|"No explicit DBCP config location, will use default location: "
operator|+
name|DBCP_PROPERTIES
argument_list|)
expr_stmt|;
name|location
operator|=
name|DBCP_PROPERTIES
expr_stmt|;
block|}
name|DataChannelDescriptor
name|parent
init|=
name|nodeDescriptor
operator|.
name|getParent
argument_list|()
decl_stmt|;
name|Resource
name|dbcpConfiguration
init|=
name|parent
operator|.
name|getConfigurationSource
argument_list|()
operator|.
name|getRelativeResource
argument_list|(
name|location
argument_list|)
decl_stmt|;
if|if
condition|(
name|dbcpConfiguration
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null 'configurationResource' for nodeDescriptor '%s'"
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
name|dbcpConfiguration
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
literal|"DBCP Properties: "
operator|+
name|properties
argument_list|)
expr_stmt|;
block|}
name|properties
operator|=
name|filteredDeprecatedProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
return|return
name|BasicDataSourceFactory
operator|.
name|createDataSource
argument_list|(
name|properties
argument_list|)
return|;
block|}
comment|/**      * Converts old-style cayene.dbcp.xyz properties to just cayenne.dbcp.      */
specifier|private
name|Properties
name|filteredDeprecatedProperties
parameter_list|(
name|Properties
name|unfiltered
parameter_list|)
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
specifier|final
name|String
name|deprecatedPrefix
init|=
literal|"cayenne.dbcp."
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|unfiltered
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|instanceof
name|String
operator|&&
name|key
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
name|deprecatedPrefix
argument_list|)
condition|)
block|{
name|String
name|oldKey
init|=
name|key
operator|.
name|toString
argument_list|()
decl_stmt|;
name|key
operator|=
name|oldKey
operator|.
name|substring
argument_list|(
name|deprecatedPrefix
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Deprecated use of 'cayenne.dbcp.' prefix in '"
operator|+
name|oldKey
operator|+
literal|"', converting to "
operator|+
name|key
argument_list|)
expr_stmt|;
block|}
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|properties
return|;
block|}
specifier|private
name|Properties
name|getProperties
parameter_list|(
name|Resource
name|dbcpConfiguration
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
name|InputStream
name|in
init|=
name|dbcpConfiguration
operator|.
name|getURL
argument_list|()
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|properties
operator|.
name|load
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
block|}
block|}
return|return
name|properties
return|;
block|}
block|}
end_class

end_unit

