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
name|tools
package|;
end_package

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|Closure
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
name|DbGenerator
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
name|DataMapLoader
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
name|DataSourceFactory
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
name|configuration
operator|.
name|server
operator|.
name|PkGeneratorFactoryProvider
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
name|dba
operator|.
name|JdbcAdapter
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
name|PkGenerator
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
name|dbsync
operator|.
name|DbSyncModule
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
name|dbsync
operator|.
name|reverse
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
name|log
operator|.
name|NoopJdbcEventLogger
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
name|resource
operator|.
name|URLResource
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
name|model
operator|.
name|DataSourceConfig
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
name|gradle
operator|.
name|api
operator|.
name|GradleException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|Input
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|InputFile
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|Internal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|tasks
operator|.
name|TaskAction
import|;
end_import

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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DbGenerateTask
extends|extends
name|BaseCayenneTask
block|{
annotation|@
name|Input
annotation|@
name|Optional
specifier|private
name|String
name|adapter
decl_stmt|;
annotation|@
name|Internal
specifier|private
name|DataSourceConfig
name|dataSource
init|=
operator|new
name|DataSourceConfig
argument_list|()
decl_stmt|;
annotation|@
name|Input
specifier|private
name|boolean
name|dropTables
decl_stmt|;
annotation|@
name|Input
specifier|private
name|boolean
name|dropPK
decl_stmt|;
annotation|@
name|Input
specifier|private
name|boolean
name|createTables
init|=
literal|true
decl_stmt|;
annotation|@
name|Input
specifier|private
name|boolean
name|createPK
init|=
literal|true
decl_stmt|;
annotation|@
name|Input
specifier|private
name|boolean
name|createFK
init|=
literal|true
decl_stmt|;
annotation|@
name|InputFile
specifier|public
name|File
name|getDataMapFile
parameter_list|()
block|{
return|return
name|super
operator|.
name|getDataMapFile
argument_list|()
return|;
block|}
annotation|@
name|TaskAction
specifier|public
name|void
name|generateDb
parameter_list|()
throws|throws
name|GradleException
block|{
name|dataSource
operator|.
name|validate
argument_list|()
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"connection settings - [driver: {}, url: {}, username: {}]"
argument_list|,
name|dataSource
operator|.
name|getDriver
argument_list|()
argument_list|,
name|dataSource
operator|.
name|getUrl
argument_list|()
argument_list|,
name|dataSource
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"generator options - "
operator|+
literal|"[dropTables: {}, dropPK: {}, createTables: {}, createPK: {}, createFK: {}]"
argument_list|,
name|dropTables
argument_list|,
name|dropPK
argument_list|,
name|createTables
argument_list|,
name|createPK
argument_list|,
name|createFK
argument_list|)
expr_stmt|;
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|DbSyncModule
argument_list|()
argument_list|,
operator|new
name|ToolsModule
argument_list|(
name|getLogger
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|DataSource
name|realDataSource
init|=
name|createDataSource
argument_list|()
decl_stmt|;
name|DataMap
name|dataMap
init|=
name|loadDataMap
argument_list|(
name|injector
argument_list|)
decl_stmt|;
name|DbGenerator
name|generator
init|=
name|createGenerator
argument_list|(
name|dataMap
argument_list|,
name|injector
argument_list|,
name|realDataSource
argument_list|)
decl_stmt|;
name|generator
operator|.
name|runGenerator
argument_list|(
name|realDataSource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Throwable
name|th
init|=
name|Util
operator|.
name|unwindException
argument_list|(
name|ex
argument_list|)
decl_stmt|;
name|String
name|message
init|=
literal|"Error generating database"
decl_stmt|;
if|if
condition|(
name|th
operator|.
name|getLocalizedMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|+=
literal|": "
operator|+
name|th
operator|.
name|getLocalizedMessage
argument_list|()
expr_stmt|;
block|}
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|GradleException
argument_list|(
name|message
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
name|DbGenerator
name|createGenerator
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|Injector
name|injector
parameter_list|,
name|DataSource
name|realDataSource
parameter_list|)
throws|throws
name|Exception
block|{
name|DbAdapter
name|dbAdapter
init|=
name|createDbAdapter
argument_list|(
name|injector
argument_list|,
name|realDataSource
argument_list|)
decl_stmt|;
name|DbGenerator
name|generator
init|=
operator|new
name|DbGenerator
argument_list|(
name|dbAdapter
argument_list|,
name|dataMap
argument_list|,
name|NoopJdbcEventLogger
operator|.
name|getInstance
argument_list|()
argument_list|)
decl_stmt|;
name|generator
operator|.
name|setShouldCreateFKConstraints
argument_list|(
name|createFK
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldCreatePKSupport
argument_list|(
name|createPK
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldCreateTables
argument_list|(
name|createTables
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldDropPKSupport
argument_list|(
name|dropPK
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldDropTables
argument_list|(
name|dropTables
argument_list|)
expr_stmt|;
return|return
name|generator
return|;
block|}
name|DbAdapter
name|createDbAdapter
parameter_list|(
name|Injector
name|injector
parameter_list|,
name|DataSource
name|realDataSource
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
name|realDataSource
argument_list|)
return|;
block|}
name|DataSource
name|createDataSource
parameter_list|()
block|{
return|return
name|DataSourceBuilder
operator|.
name|url
argument_list|(
name|dataSource
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|driver
argument_list|(
name|dataSource
operator|.
name|getDriver
argument_list|()
argument_list|)
operator|.
name|userName
argument_list|(
name|dataSource
operator|.
name|getUsername
argument_list|()
argument_list|)
operator|.
name|password
argument_list|(
name|dataSource
operator|.
name|getPassword
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
name|DataMap
name|loadDataMap
parameter_list|(
name|Injector
name|injector
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|dataMapFile
init|=
name|getDataMapFile
argument_list|()
decl_stmt|;
return|return
name|injector
operator|.
name|getInstance
argument_list|(
name|DataMapLoader
operator|.
name|class
argument_list|)
operator|.
name|load
argument_list|(
operator|new
name|URLResource
argument_list|(
name|dataMapFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|// setters and getters that will be used by .gradle scripts
specifier|public
name|String
name|getAdapter
parameter_list|()
block|{
return|return
name|adapter
return|;
block|}
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
specifier|public
name|void
name|adapter
parameter_list|(
name|String
name|adapter
parameter_list|)
block|{
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DataSourceConfig
name|getDataSource
parameter_list|()
block|{
return|return
name|dataSource
return|;
block|}
specifier|public
name|void
name|setDataSource
parameter_list|(
name|DataSourceConfig
name|dataSource
parameter_list|)
block|{
name|this
operator|.
name|dataSource
operator|=
name|dataSource
expr_stmt|;
block|}
specifier|public
name|DataSourceConfig
name|dataSource
parameter_list|(
name|Closure
name|closure
parameter_list|)
block|{
name|dataSource
operator|=
operator|new
name|DataSourceConfig
argument_list|()
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|configure
argument_list|(
name|dataSource
argument_list|,
name|closure
argument_list|)
expr_stmt|;
return|return
name|dataSource
return|;
block|}
specifier|public
name|boolean
name|isDropTables
parameter_list|()
block|{
return|return
name|dropTables
return|;
block|}
specifier|public
name|void
name|setDropTables
parameter_list|(
name|boolean
name|dropTables
parameter_list|)
block|{
name|this
operator|.
name|dropTables
operator|=
name|dropTables
expr_stmt|;
block|}
specifier|public
name|void
name|dropTables
parameter_list|(
name|boolean
name|dropTables
parameter_list|)
block|{
name|setDropTables
argument_list|(
name|dropTables
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDropPK
parameter_list|()
block|{
return|return
name|dropPK
return|;
block|}
specifier|public
name|void
name|setDropPK
parameter_list|(
name|boolean
name|dropPK
parameter_list|)
block|{
name|this
operator|.
name|dropPK
operator|=
name|dropPK
expr_stmt|;
block|}
specifier|public
name|void
name|dropPK
parameter_list|(
name|boolean
name|dropPK
parameter_list|)
block|{
name|setDropPK
argument_list|(
name|dropPK
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isCreateTables
parameter_list|()
block|{
return|return
name|createTables
return|;
block|}
specifier|public
name|void
name|setCreateTables
parameter_list|(
name|boolean
name|createTables
parameter_list|)
block|{
name|this
operator|.
name|createTables
operator|=
name|createTables
expr_stmt|;
block|}
specifier|public
name|void
name|createTables
parameter_list|(
name|boolean
name|createTables
parameter_list|)
block|{
name|setCreateTables
argument_list|(
name|createTables
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isCreatePK
parameter_list|()
block|{
return|return
name|createPK
return|;
block|}
specifier|public
name|void
name|setCreatePK
parameter_list|(
name|boolean
name|createPK
parameter_list|)
block|{
name|this
operator|.
name|createPK
operator|=
name|createPK
expr_stmt|;
block|}
specifier|public
name|void
name|createPK
parameter_list|(
name|boolean
name|createPK
parameter_list|)
block|{
name|setCreatePK
argument_list|(
name|createPK
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isCreateFK
parameter_list|()
block|{
return|return
name|createFK
return|;
block|}
specifier|public
name|void
name|setCreateFK
parameter_list|(
name|boolean
name|createFK
parameter_list|)
block|{
name|this
operator|.
name|createFK
operator|=
name|createFK
expr_stmt|;
block|}
specifier|public
name|void
name|createFK
parameter_list|(
name|boolean
name|createFK
parameter_list|)
block|{
name|setCreateFK
argument_list|(
name|createFK
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

