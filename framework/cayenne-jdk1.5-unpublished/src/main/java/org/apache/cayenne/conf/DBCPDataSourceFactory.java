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
name|conf
package|;
end_package

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
comment|/**  * A DataSourceFactory that creates a connection pool based on Apache Jakarta<a  * href="http://jakarta.apache.org/commons/dbcp/">commons-dbcp</a>. If you are using this  * factory, commons-pool and commons-dbcp jars must be present in runtime.  *<p/>  * DBCPDataSourceFactory can be selected in the Modeler for a DataNode. DBCP pool  * configuration is done via a properties file that is specified in the modeler. See this  *<a href="http://cwiki.apache.org/CAYDOC/DBCPDataSourceFactory">wiki page</a> for the  * list of supported properties.  *   * @since 1.2  * @deprecated since 3.1 replaced with {@link org.apache.cayenne.configuration.server.DBCPDataSourceFactory}  */
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
specifier|protected
name|Configuration
name|parentConfiguration
decl_stmt|;
comment|/**      * Stores parent configuration in an ivar, using it later to resolve resources.      */
specifier|public
name|void
name|initializeWithParentConfiguration
parameter_list|(
name|Configuration
name|parentConfiguration
parameter_list|)
block|{
name|this
operator|.
name|parentConfiguration
operator|=
name|parentConfiguration
expr_stmt|;
block|}
comment|/**      * Creates and returns a {{org.apache.commons.dbcp.PoolingDataSource}} instance.      */
specifier|public
name|DataSource
name|getDataSource
parameter_list|(
name|String
name|location
parameter_list|)
throws|throws
name|Exception
block|{
name|ResourceFinder
name|resourceFinder
decl_stmt|;
if|if
condition|(
name|parentConfiguration
operator|!=
literal|null
condition|)
block|{
name|resourceFinder
operator|=
name|parentConfiguration
operator|.
name|getResourceFinder
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ResourceLocator
name|resourceLocator
init|=
operator|new
name|ResourceLocator
argument_list|()
decl_stmt|;
name|resourceLocator
operator|.
name|setSkipAbsolutePath
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|resourceLocator
operator|.
name|setSkipHomeDirectory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resourceLocator
operator|.
name|setSkipClasspath
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|resourceLocator
operator|.
name|setSkipCurrentDirectory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|resourceFinder
operator|=
name|resourceLocator
expr_stmt|;
block|}
name|DBCPDataSourceProperties
name|properties
init|=
operator|new
name|DBCPDataSourceProperties
argument_list|(
name|resourceFinder
argument_list|,
name|location
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
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|DBCPDataSourceBuilder
name|builder
init|=
operator|new
name|DBCPDataSourceBuilder
argument_list|(
name|properties
argument_list|)
decl_stmt|;
return|return
name|builder
operator|.
name|createDataSource
argument_list|()
return|;
block|}
block|}
end_class

end_unit

