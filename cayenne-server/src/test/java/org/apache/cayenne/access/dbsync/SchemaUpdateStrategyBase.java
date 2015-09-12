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
name|access
operator|.
name|dbsync
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

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
name|HashMap
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
name|ObjectContext
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
name|query
operator|.
name|SQLTemplate
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
name|di
operator|.
name|server
operator|.
name|ServerCase
import|;
end_import

begin_class
specifier|public
class|class
name|SchemaUpdateStrategyBase
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataNode
name|node
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DbAdapter
name|adapter
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|cleanUpDB
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|map
init|=
name|node
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"sus-map"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|existingTables
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|drop
range|:
name|adapter
operator|.
name|dropTableStatements
argument_list|(
name|map
operator|.
name|getDbEntity
argument_list|(
name|name
argument_list|)
argument_list|)
control|)
block|{
name|context
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|drop
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|setStrategy
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|SchemaUpdateStrategy
argument_list|>
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|node
operator|.
name|setSchemaUpdateStrategyName
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|.
name|setSchemaUpdateStrategy
argument_list|(
name|type
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|existingTables
parameter_list|()
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|present
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|e
range|:
name|tablesMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|present
operator|.
name|add
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|present
return|;
block|}
specifier|protected
name|void
name|createOneTable
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
name|DataMap
name|map
init|=
name|node
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"sus-map"
argument_list|)
decl_stmt|;
name|String
name|createTable
init|=
name|adapter
operator|.
name|createTable
argument_list|(
name|map
operator|.
name|getDbEntity
argument_list|(
name|entityName
argument_list|)
argument_list|)
decl_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|createTable
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|tablesMap
parameter_list|()
block|{
name|DataMap
name|map
init|=
name|node
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"sus-map"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|tables
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// add upper/lower case permutations
for|for
control|(
name|String
name|name
range|:
name|map
operator|.
name|getDbEntityMap
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|tables
operator|.
name|put
argument_list|(
name|name
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|tables
operator|.
name|put
argument_list|(
name|name
operator|.
name|toUpperCase
argument_list|()
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|presentInDB
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|map
operator|.
name|getDbEntityMap
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|presentInDB
operator|.
name|put
argument_list|(
name|name
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|String
name|tableLabel
init|=
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|tableTypeForTable
argument_list|()
decl_stmt|;
try|try
init|(
name|Connection
name|con
init|=
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|;
init|)
block|{
try|try
init|(
name|ResultSet
name|rs
init|=
name|con
operator|.
name|getMetaData
argument_list|()
operator|.
name|getTables
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"%"
argument_list|,
operator|new
name|String
index|[]
block|{
name|tableLabel
block|}
argument_list|)
init|;
init|)
block|{
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|String
name|dbName
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"TABLE_NAME"
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|tables
operator|.
name|get
argument_list|(
name|dbName
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|presentInDB
operator|.
name|put
argument_list|(
name|name
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|presentInDB
return|;
block|}
block|}
end_class

end_unit

