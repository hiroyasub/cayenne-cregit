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
operator|.
name|di
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
name|InputStream
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
name|di
operator|.
name|Provider
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
name|AccessStack
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
name|CayenneResources
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

begin_class
specifier|public
class|class
name|CayenneResourcesProvider
implements|implements
name|Provider
argument_list|<
name|CayenneResources
argument_list|>
block|{
specifier|private
specifier|static
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CayenneResourcesProvider
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
name|DEFAULT_CONNECTION_KEY
init|=
literal|"internal_embedded_datasource"
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
name|SCHEMA_SETUP_STACK
init|=
literal|"SchemaSetupStack"
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataSourceInfo
name|dataSourceInfo
decl_stmt|;
specifier|public
name|CayenneResources
name|get
parameter_list|()
throws|throws
name|ConfigurationException
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
name|logger
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
name|setConnectionInfo
argument_list|(
name|dataSourceInfo
argument_list|)
expr_stmt|;
comment|// rebuild schema after the resources instance is loaded so that after
comment|// possible initial failure we don't attempt rebuilding schema in subsequent
comment|// tests
try|try
block|{
name|rebuildSchema
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|logger
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
return|return
name|resources
return|;
block|}
comment|/**      * Completely rebuilds test schema.      */
specifier|private
name|void
name|rebuildSchema
parameter_list|(
name|BeanFactory
name|beanFactory
parameter_list|)
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
name|logger
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
operator|(
name|AccessStack
operator|)
name|beanFactory
operator|.
name|getBean
argument_list|(
name|SCHEMA_SETUP_STACK
argument_list|,
name|AccessStack
operator|.
name|class
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
block|}
end_class

end_unit

