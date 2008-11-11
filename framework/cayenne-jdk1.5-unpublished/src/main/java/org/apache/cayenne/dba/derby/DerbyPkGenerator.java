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
name|dba
operator|.
name|derby
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
name|PreparedStatement
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
name|util
operator|.
name|Collections
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
name|CayenneException
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
name|QueryLogger
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
name|JdbcPkGenerator
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

begin_comment
comment|/**  * Default PK generator for Derby that uses updateable ResultSet to get the next id from  * the lookup table.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|DerbyPkGenerator
extends|extends
name|JdbcPkGenerator
block|{
specifier|static
specifier|final
name|String
name|SELECT_QUERY
init|=
literal|"SELECT NEXT_ID FROM AUTO_PK_SUPPORT"
operator|+
literal|" WHERE TABLE_NAME = ? FOR UPDATE"
decl_stmt|;
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|protected
name|long
name|longPkFromDatabase
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DbEntity
name|entity
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|QueryLogger
operator|.
name|isLoggable
argument_list|()
condition|)
block|{
name|QueryLogger
operator|.
name|logQuery
argument_list|(
name|SELECT_QUERY
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Connection
name|c
init|=
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|PreparedStatement
name|select
init|=
name|c
operator|.
name|prepareStatement
argument_list|(
name|SELECT_QUERY
argument_list|,
name|ResultSet
operator|.
name|TYPE_FORWARD_ONLY
argument_list|,
name|ResultSet
operator|.
name|CONCUR_UPDATABLE
argument_list|)
decl_stmt|;
name|select
operator|.
name|setString
argument_list|(
literal|1
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ResultSet
name|rs
init|=
name|select
operator|.
name|executeQuery
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"PK lookup failed for table: "
operator|+
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|long
name|nextId
init|=
name|rs
operator|.
name|getLong
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|rs
operator|.
name|updateLong
argument_list|(
literal|1
argument_list|,
name|nextId
operator|+
name|pkCacheSize
argument_list|)
expr_stmt|;
name|rs
operator|.
name|updateRow
argument_list|()
expr_stmt|;
if|if
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"More than one PK record for table: "
operator|+
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
name|select
operator|.
name|close
argument_list|()
expr_stmt|;
name|c
operator|.
name|commit
argument_list|()
expr_stmt|;
return|return
name|nextId
return|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @deprecated since 3.0      */
annotation|@
name|Override
specifier|protected
name|int
name|pkFromDatabase
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DbEntity
name|entity
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|QueryLogger
operator|.
name|isLoggable
argument_list|()
condition|)
block|{
name|QueryLogger
operator|.
name|logQuery
argument_list|(
name|SELECT_QUERY
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Connection
name|c
init|=
name|node
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|PreparedStatement
name|select
init|=
name|c
operator|.
name|prepareStatement
argument_list|(
name|SELECT_QUERY
argument_list|,
name|ResultSet
operator|.
name|TYPE_FORWARD_ONLY
argument_list|,
name|ResultSet
operator|.
name|CONCUR_UPDATABLE
argument_list|)
decl_stmt|;
name|select
operator|.
name|setString
argument_list|(
literal|1
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ResultSet
name|rs
init|=
name|select
operator|.
name|executeQuery
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"PK lookup failed for table: "
operator|+
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|int
name|nextId
init|=
name|rs
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|rs
operator|.
name|updateInt
argument_list|(
literal|1
argument_list|,
name|nextId
operator|+
name|pkCacheSize
argument_list|)
expr_stmt|;
name|rs
operator|.
name|updateRow
argument_list|()
expr_stmt|;
if|if
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"More than one PK record for table: "
operator|+
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
name|select
operator|.
name|close
argument_list|()
expr_stmt|;
name|c
operator|.
name|commit
argument_list|()
expr_stmt|;
return|return
name|nextId
return|;
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

