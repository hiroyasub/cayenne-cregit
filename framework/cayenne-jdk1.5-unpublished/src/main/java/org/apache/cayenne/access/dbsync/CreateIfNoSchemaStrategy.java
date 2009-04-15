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
name|Iterator
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
name|DbEntity
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
comment|/**  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|CreateIfNoSchemaStrategy
extends|extends
name|BaseSchemaUpdateStrategy
block|{
specifier|final
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CreateIfNoSchemaStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|BaseSchemaUpdateStrategy
name|getSchema
parameter_list|()
block|{
return|return
name|currentSchema
return|;
block|}
specifier|public
name|CreateIfNoSchemaStrategy
parameter_list|()
block|{
name|currentSchema
operator|=
name|this
expr_stmt|;
block|}
specifier|public
name|void
name|updateSchema
parameter_list|(
name|DataNode
name|dataNode
parameter_list|)
throws|throws
name|SQLException
block|{
name|super
operator|.
name|generateUpdateSchema
argument_list|(
name|dataNode
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateUpdateSchema
parameter_list|(
name|DataNode
name|dataNode
parameter_list|)
throws|throws
name|SQLException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|nameTables
init|=
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|entities
init|=
name|dataNode
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntities
argument_list|()
decl_stmt|;
name|boolean
name|generate
init|=
literal|true
decl_stmt|;
name|Iterator
argument_list|<
name|DbEntity
argument_list|>
name|it
init|=
name|entities
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|nameTables
operator|.
name|get
argument_list|(
name|it
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|generate
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|generate
condition|)
block|{
name|generate
argument_list|(
name|dataNode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logObj
operator|.
name|info
argument_list|(
literal|"DbGenerator no create, because one of the tables, modeled in Cayenne, already exist in DB"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|synchronized
name|void
name|generate
parameter_list|(
name|DataNode
name|dataNode
parameter_list|)
block|{
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|map
init|=
name|dataNode
operator|.
name|getDataMaps
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|DataMap
argument_list|>
name|iterator
init|=
name|map
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbGenerator
name|gen
init|=
operator|new
name|DbGenerator
argument_list|(
name|dataNode
operator|.
name|getAdapter
argument_list|()
argument_list|,
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|gen
operator|.
name|setShouldCreateTables
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setShouldDropTables
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setShouldCreateFKConstraints
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setShouldCreatePKSupport
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|gen
operator|.
name|setShouldDropPKSupport
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|gen
operator|.
name|runGenerator
argument_list|(
name|dataNode
operator|.
name|getDataSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
block|}
block|}
comment|/**      * Returns all the table names in database.      *       * @throws SQLException      */
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|getNameTablesInDB
parameter_list|(
name|DataNode
name|dataNode
parameter_list|)
throws|throws
name|SQLException
block|{
name|String
name|tableLabel
init|=
name|dataNode
operator|.
name|getAdapter
argument_list|()
operator|.
name|tableTypeForTable
argument_list|()
decl_stmt|;
name|Connection
name|con
init|=
literal|null
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|nameTables
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
name|con
operator|=
name|dataNode
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
expr_stmt|;
try|try
block|{
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
decl_stmt|;
try|try
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
name|name
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"TABLE_NAME"
argument_list|)
decl_stmt|;
name|nameTables
operator|.
name|put
argument_list|(
name|name
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|con
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|nameTables
return|;
block|}
block|}
end_class

end_unit

