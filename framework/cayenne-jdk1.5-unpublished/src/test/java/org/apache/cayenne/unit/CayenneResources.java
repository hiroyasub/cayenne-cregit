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
name|unit
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|DataNode
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
name|ConnectionProperties
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
name|conn
operator|.
name|PoolDataSource
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
name|unit
operator|.
name|util
operator|.
name|SQLTemplateCustomizer
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|BeanFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|BeanFactoryAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|XmlBeanFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|InputStreamResource
import|;
end_import

begin_comment
comment|/**  * Initializes connections for Cayenne unit tests.  *   */
end_comment

begin_comment
comment|// TODO: switch to Spring
end_comment

begin_class
specifier|public
class|class
name|CayenneResources
implements|implements
name|BeanFactoryAware
block|{
specifier|private
specifier|static
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CayenneResources
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEST_RESOURCES_DESCRIPTOR
init|=
literal|"spring-test-resources.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONNECTION_NAME_KEY
init|=
literal|"cayenneTestConnection"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SKIP_SCHEMA_KEY
init|=
literal|"cayenne.test.schema.skip"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEST_DIR_KEY
init|=
literal|"cayenne.test.dir"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_TEST_DIR
init|=
literal|"target/testrun"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SCHEMA_SETUP_STACK
init|=
literal|"SchemaSetupStack"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SQL_TEMPLATE_CUSTOMIZER
init|=
literal|"SQLTemplateCustomizer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_CONNECTION_KEY
init|=
literal|"internal_embedded_datasource"
decl_stmt|;
specifier|private
specifier|static
name|CayenneResources
name|resources
decl_stmt|;
specifier|private
specifier|static
name|CayenneResources
name|loadResources
parameter_list|()
block|{
name|InputStream
name|in
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|TEST_RESOURCES_DESCRIPTOR
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
name|logObj
operator|.
name|error
argument_list|(
literal|"Can't locate resource: "
operator|+
name|TEST_RESOURCES_DESCRIPTOR
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Can't locate resource descriptor in the ClassLoader: "
operator|+
name|TEST_RESOURCES_DESCRIPTOR
argument_list|)
throw|;
block|}
name|BeanFactory
name|factory
init|=
operator|new
name|XmlBeanFactory
argument_list|(
operator|new
name|InputStreamResource
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|CayenneResources
name|resources
init|=
operator|(
name|CayenneResources
operator|)
name|factory
operator|.
name|getBean
argument_list|(
literal|"TestResources"
argument_list|,
name|CayenneResources
operator|.
name|class
argument_list|)
decl_stmt|;
name|resources
operator|.
name|setConnectionKey
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|CONNECTION_NAME_KEY
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|resources
return|;
block|}
specifier|protected
name|File
name|testDir
decl_stmt|;
specifier|protected
name|DataSourceInfo
name|connectionInfo
decl_stmt|;
specifier|protected
name|DataSource
name|dataSource
decl_stmt|;
specifier|protected
name|BeanFactory
name|beanFactory
decl_stmt|;
specifier|protected
name|Map
name|adapterMap
decl_stmt|;
comment|/**      * Returns shared test resource instance.      *       * @return CayenneTestResources      */
specifier|public
specifier|static
name|CayenneResources
name|getResources
parameter_list|()
block|{
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
name|resources
operator|=
name|loadResources
argument_list|()
expr_stmt|;
comment|// rebuild schema after the resources static var is initialized so that after
comment|// possible initial failure we don't attempt rebuilding schema in subsequent
comment|// tests
try|try
block|{
name|resources
operator|.
name|rebuildSchema
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|logObj
operator|.
name|error
argument_list|(
literal|"Error generating schema..."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error generating schema"
argument_list|)
throw|;
block|}
block|}
return|return
name|resources
return|;
block|}
comment|/**      * Copies resources to a file, thus making it available to the caller as File.      */
specifier|public
specifier|static
name|void
name|copyResourceToFile
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|File
name|file
parameter_list|)
block|{
name|URL
name|in
init|=
name|getResourceURL
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|file
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error copying resource to file : "
operator|+
name|file
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns a guaranteed non-null resource for a given name.      */
specifier|public
specifier|static
name|URL
name|getResourceURL
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|URL
name|in
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// Fix for the issue described at https://issues.apache.org/struts/browse/SB-35
comment|// Basically, spaces in filenames make maven cry.
try|try
block|{
name|in
operator|=
operator|new
name|URL
argument_list|(
name|in
operator|.
name|toExternalForm
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|"%20"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error constructing URL."
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Resource not found: "
operator|+
name|name
argument_list|)
throw|;
block|}
return|return
name|in
return|;
block|}
comment|/**      * Returns a guaranteed non-null resource for a given name.      */
specifier|public
specifier|static
name|InputStream
name|getResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|InputStream
name|in
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Resource not found: "
operator|+
name|name
argument_list|)
throw|;
block|}
return|return
name|in
return|;
block|}
specifier|public
name|CayenneResources
parameter_list|(
name|Map
name|adapterMap
parameter_list|)
block|{
name|this
operator|.
name|adapterMap
operator|=
name|adapterMap
expr_stmt|;
name|setupTestDir
argument_list|()
expr_stmt|;
block|}
comment|/**      * Completely rebuilds test schema.      */
name|void
name|rebuildSchema
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|SKIP_SCHEMA_KEY
argument_list|)
argument_list|)
condition|)
block|{
name|logObj
operator|.
name|info
argument_list|(
literal|"skipping schema generation... "
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// generate schema using a special AccessStack that
comment|// combines all DataMaps that require schema support
comment|// schema generation is done like that instead of
comment|// per stack on demand, to avoid conflicts when
comment|// dropping and generating PK objects.
name|AccessStack
name|stack
init|=
name|getAccessStack
argument_list|(
name|SCHEMA_SETUP_STACK
argument_list|)
decl_stmt|;
name|stack
operator|.
name|dropSchema
argument_list|()
expr_stmt|;
name|stack
operator|.
name|dropPKSupport
argument_list|()
expr_stmt|;
name|stack
operator|.
name|createSchema
argument_list|()
expr_stmt|;
name|stack
operator|.
name|createPKSupport
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setConnectionKey
parameter_list|(
name|String
name|connectionKey
parameter_list|)
block|{
name|connectionInfo
operator|=
name|ConnectionProperties
operator|.
name|getInstance
argument_list|()
operator|.
name|getConnectionInfo
argument_list|(
name|connectionKey
argument_list|)
expr_stmt|;
comment|// attempt default if invalid key is specified
if|if
condition|(
name|connectionInfo
operator|==
literal|null
condition|)
block|{
name|logObj
operator|.
name|info
argument_list|(
literal|"Invalid connection key '"
operator|+
name|connectionKey
operator|+
literal|"', trying default: "
operator|+
name|DEFAULT_CONNECTION_KEY
argument_list|)
expr_stmt|;
name|connectionInfo
operator|=
name|ConnectionProperties
operator|.
name|getInstance
argument_list|()
operator|.
name|getConnectionInfo
argument_list|(
name|DEFAULT_CONNECTION_KEY
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|connectionInfo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Null connection info for key: "
operator|+
name|connectionKey
argument_list|)
throw|;
block|}
name|logObj
operator|.
name|info
argument_list|(
literal|"test connection info: "
operator|+
name|connectionInfo
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataSource
operator|=
name|createDataSource
argument_list|()
expr_stmt|;
block|}
comment|/**      * BeanFactoryAware implementation to store BeanFactory.      */
specifier|public
name|void
name|setBeanFactory
parameter_list|(
name|BeanFactory
name|beanFactory
parameter_list|)
throws|throws
name|BeansException
block|{
name|this
operator|.
name|beanFactory
operator|=
name|beanFactory
expr_stmt|;
block|}
specifier|public
name|AccessStack
name|getAccessStack
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|AccessStack
operator|)
name|beanFactory
operator|.
name|getBean
argument_list|(
name|name
argument_list|,
name|AccessStack
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|SQLTemplateCustomizer
name|getSQLTemplateCustomizer
parameter_list|()
block|{
name|BeanFactory
name|child
init|=
operator|(
name|BeanFactory
operator|)
name|beanFactory
operator|.
name|getBean
argument_list|(
name|SQL_TEMPLATE_CUSTOMIZER
argument_list|,
name|BeanFactory
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|(
name|SQLTemplateCustomizer
operator|)
name|child
operator|.
name|getBean
argument_list|(
name|SQL_TEMPLATE_CUSTOMIZER
argument_list|,
name|SQLTemplateCustomizer
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Returns DB-specific testing adapter.      */
specifier|public
name|AccessStackAdapter
name|getAccessStackAdapter
parameter_list|(
name|Class
name|adapterClass
parameter_list|)
block|{
name|AccessStackAdapter
name|stackAdapter
init|=
operator|(
name|AccessStackAdapter
operator|)
name|adapterMap
operator|.
name|get
argument_list|(
name|adapterClass
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|stackAdapter
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No AccessStackAdapter for DbAdapter class: "
operator|+
name|adapterClass
argument_list|)
throw|;
block|}
comment|// post init
name|stackAdapter
operator|.
name|unchecked
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|stackAdapter
return|;
block|}
comment|/**      * Returns shared DataSource.      */
specifier|public
name|DataSource
name|getDataSource
parameter_list|()
block|{
return|return
name|dataSource
return|;
block|}
comment|/**      * Returns a test directory that is used as a scratch area.      */
specifier|public
name|File
name|getTestDir
parameter_list|()
block|{
return|return
name|testDir
return|;
block|}
comment|/**      * Creates new DataNode.      */
specifier|public
name|DataNode
name|newDataNode
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|AccessStackAdapter
name|adapter
init|=
name|getAccessStackAdapter
argument_list|(
name|Class
operator|.
name|forName
argument_list|(
name|connectionInfo
operator|.
name|getAdapterClassName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|DataNode
name|node
init|=
operator|new
name|DataNode
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|node
operator|.
name|setDataSource
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
name|node
operator|.
name|setAdapter
argument_list|(
name|adapter
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
comment|/**      * Returns connection information.      */
specifier|public
name|DataSourceInfo
name|getConnectionInfo
parameter_list|()
block|{
return|return
name|connectionInfo
return|;
block|}
specifier|public
name|DataSource
name|createDataSource
parameter_list|()
block|{
try|try
block|{
name|PoolDataSource
name|poolDS
init|=
operator|new
name|PoolDataSource
argument_list|(
name|connectionInfo
operator|.
name|getJdbcDriver
argument_list|()
argument_list|,
name|connectionInfo
operator|.
name|getDataSourceUrl
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|PoolManager
argument_list|(
name|poolDS
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
name|connectionInfo
operator|.
name|getUserName
argument_list|()
argument_list|,
name|connectionInfo
operator|.
name|getPassword
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|logObj
operator|.
name|error
argument_list|(
literal|"Can not create shared data source."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Can not create shared data source."
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|setupTestDir
parameter_list|()
block|{
name|String
name|testDirName
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|TEST_DIR_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|testDirName
operator|==
literal|null
condition|)
block|{
name|testDirName
operator|=
name|DEFAULT_TEST_DIR
expr_stmt|;
name|logObj
operator|.
name|info
argument_list|(
literal|"No property '"
operator|+
name|TEST_DIR_KEY
operator|+
literal|"' set. Using default directory: '"
operator|+
name|testDirName
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
name|testDir
operator|=
operator|new
name|File
argument_list|(
name|testDirName
argument_list|)
expr_stmt|;
comment|// delete old tests
if|if
condition|(
name|testDir
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|delete
argument_list|(
name|testDirName
argument_list|,
literal|true
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error deleting test directory: "
operator|+
name|testDirName
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
operator|!
name|testDir
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error creating test directory: "
operator|+
name|testDirName
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

