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
name|tools
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
name|DIBootstrap
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
name|Injector
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
name|map
operator|.
name|DataMap
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
name|map
operator|.
name|MapLoader
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
name|tools
operator|.
name|configuration
operator|.
name|ToolsModule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Task
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|types
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|types
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_comment
comment|/**  * Base task for all Cayenne ant tasks, providing support for common  * configuration items.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CayenneTask
extends|extends
name|Task
block|{
specifier|protected
name|Path
name|classpath
decl_stmt|;
specifier|protected
name|String
name|adapter
decl_stmt|;
specifier|protected
name|File
name|map
decl_stmt|;
specifier|protected
name|String
name|driver
decl_stmt|;
specifier|protected
name|String
name|url
decl_stmt|;
specifier|protected
name|String
name|userName
decl_stmt|;
specifier|protected
name|String
name|password
decl_stmt|;
comment|/**      * Sets the classpath used by the task.      *       * @param path      *            The classpath to set.      */
specifier|public
name|void
name|setClasspath
parameter_list|(
name|Path
name|path
parameter_list|)
block|{
name|createClasspath
argument_list|()
operator|.
name|append
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the classpath reference used by the task.      *       * @param reference      *            The classpath reference to set.      */
specifier|public
name|void
name|setClasspathRef
parameter_list|(
name|Reference
name|reference
parameter_list|)
block|{
name|createClasspath
argument_list|()
operator|.
name|setRefid
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
comment|/**      * Convenience method for creating a classpath instance to be used for the      * task.      *       * @return The new classpath.      */
specifier|private
name|Path
name|createClasspath
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|==
name|classpath
condition|)
block|{
name|classpath
operator|=
operator|new
name|Path
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|classpath
operator|.
name|createPath
argument_list|()
return|;
block|}
comment|/**      * Sets the map.      *       * @param map      *            The map to set      */
specifier|public
name|void
name|setMap
parameter_list|(
name|File
name|map
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
comment|/**      * Sets the db adapter.      *       * @param adapter      *            The db adapter to set.      */
specifier|public
name|void
name|setAdapter
parameter_list|(
name|String
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
block|}
comment|/**      * Sets the JDBC driver used to connect to the database server.      *       * @param driver      *            The driver to set.      */
specifier|public
name|void
name|setDriver
parameter_list|(
name|String
name|driver
parameter_list|)
block|{
name|this
operator|.
name|driver
operator|=
name|driver
expr_stmt|;
block|}
comment|/**      * Sets the JDBC URL used to connect to the database server.      *       * @param url      *            The url to set.      */
specifier|public
name|void
name|setUrl
parameter_list|(
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
comment|/**      * Sets the username used to connect to the database server.      */
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|username
expr_stmt|;
block|}
comment|/**      * Sets the password used to connect to the database server.      *       * @param password      *            The password to set.      */
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
comment|/** Loads and returns DataMap based on<code>map</code> attribute. */
specifier|protected
name|DataMap
name|loadDataMap
parameter_list|()
throws|throws
name|Exception
block|{
name|InputSource
name|in
init|=
operator|new
name|InputSource
argument_list|(
name|map
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|MapLoader
argument_list|()
operator|.
name|loadDataMap
argument_list|(
name|in
argument_list|)
return|;
block|}
specifier|protected
name|Injector
name|getInjector
parameter_list|()
block|{
return|return
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|ToolsModule
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|DbAdapter
name|getAdapter
parameter_list|(
name|Injector
name|injector
parameter_list|,
name|DataSource
name|dataSource
parameter_list|)
throws|throws
name|Exception
block|{
name|DbAdapterFactory
name|adapterFactory
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DbAdapterFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|DataNodeDescriptor
name|nodeDescriptor
init|=
operator|new
name|DataNodeDescriptor
argument_list|()
decl_stmt|;
name|nodeDescriptor
operator|.
name|setAdapterType
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
return|return
name|adapterFactory
operator|.
name|createAdapter
argument_list|(
name|nodeDescriptor
argument_list|,
name|dataSource
argument_list|)
return|;
block|}
block|}
end_class

end_unit

