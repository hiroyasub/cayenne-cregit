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
name|sql
operator|.
name|SQLException
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
name|access
operator|.
name|jdbc
operator|.
name|BatchQueryBuilderFactory
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
name|jdbc
operator|.
name|DefaultBatchQueryBuilderFactory
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
comment|/**  * Initializes connections for Cayenne unit tests.  */
end_comment

begin_class
specifier|public
class|class
name|CayenneResources
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
name|CayenneResources
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SQL_TEMPLATE_CUSTOMIZER
init|=
literal|"SQLTemplateCustomizer"
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
name|Map
argument_list|<
name|String
argument_list|,
name|AccessStackAdapter
argument_list|>
name|adapterMap
decl_stmt|;
specifier|public
name|CayenneResources
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|AccessStackAdapter
argument_list|>
name|adapterMap
parameter_list|)
block|{
name|this
operator|.
name|adapterMap
operator|=
name|adapterMap
expr_stmt|;
comment|// kludge until we stop using Spring for unit tests and use Cayenne DI
name|BatchQueryBuilderFactory
name|factory
init|=
operator|new
name|DefaultBatchQueryBuilderFactory
argument_list|()
decl_stmt|;
for|for
control|(
name|AccessStackAdapter
name|adapter
range|:
name|adapterMap
operator|.
name|values
argument_list|()
control|)
block|{
operator|(
operator|(
name|JdbcAdapter
operator|)
name|adapter
operator|.
name|getAdapter
argument_list|()
operator|)
operator|.
name|setBatchQueryBuilderFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setConnectionInfo
parameter_list|(
name|DataSourceInfo
name|connectionInfo
parameter_list|)
block|{
name|this
operator|.
name|connectionInfo
operator|=
name|connectionInfo
expr_stmt|;
name|this
operator|.
name|dataSource
operator|=
name|createDataSource
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns DB-specific testing adapter.      */
specifier|public
name|AccessStackAdapter
name|getAccessStackAdapter
parameter_list|(
name|String
name|adapterClassName
parameter_list|)
block|{
name|AccessStackAdapter
name|stackAdapter
init|=
name|adapterMap
operator|.
name|get
argument_list|(
name|adapterClassName
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
name|adapterClassName
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
name|connectionInfo
operator|.
name|getAdapterClassName
argument_list|()
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
block|{
annotation|@
name|Override
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|SQLException
block|{
comment|// noop - make sure we are not shutdown by the test scope, but at the
comment|// same time PoolManager methods are exposed (so we can't wrap
comment|// PoolManager)
block|}
block|}
return|;
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
block|}
end_class

end_unit

