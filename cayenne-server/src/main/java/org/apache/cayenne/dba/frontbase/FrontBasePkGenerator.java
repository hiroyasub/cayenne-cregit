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
name|frontbase
package|;
end_package

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
name|DataRow
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
name|OperationObserver
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
name|util
operator|.
name|DoNothingOperationObserver
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
name|Query
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|FrontBasePkGenerator
extends|extends
name|JdbcPkGenerator
block|{
specifier|public
name|FrontBasePkGenerator
parameter_list|(
name|JdbcAdapter
name|adapter
parameter_list|)
block|{
name|super
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|pkStartValue
operator|=
literal|1000000
expr_stmt|;
block|}
comment|/** 	 * Returns zero as PK caching is not supported by FrontBaseAdapter. 	 */
annotation|@
name|Override
specifier|public
name|int
name|getPkCacheSize
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createAutoPk
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|)
throws|throws
name|Exception
block|{
comment|// For each entity (re)set the unique counter
for|for
control|(
name|DbEntity
name|entity
range|:
name|dbEntities
control|)
block|{
name|runUpdate
argument_list|(
name|node
argument_list|,
name|pkCreateString
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|createAutoPkStatements
parameter_list|(
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntity
name|entity
range|:
name|dbEntities
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|pkCreateString
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|dropAutoPk
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|)
throws|throws
name|Exception
block|{
block|}
annotation|@
name|Override
specifier|protected
name|String
name|pkTableCreateString
parameter_list|()
block|{
return|return
literal|""
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|pkDeleteString
parameter_list|(
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|)
block|{
return|return
literal|"-- The 'Drop Primary Key Support' option is unavailable"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|pkCreateString
parameter_list|(
name|String
name|entName
parameter_list|)
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"SET UNIQUE = "
argument_list|)
operator|.
name|append
argument_list|(
name|pkStartValue
argument_list|)
operator|.
name|append
argument_list|(
literal|" FOR \""
argument_list|)
operator|.
name|append
argument_list|(
name|entName
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|pkSelectString
parameter_list|(
name|String
name|entName
parameter_list|)
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"SELECT UNIQUE FROM \""
argument_list|)
operator|.
name|append
argument_list|(
name|entName
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|pkUpdateString
parameter_list|(
name|String
name|entName
parameter_list|)
block|{
return|return
literal|""
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|dropAutoPkString
parameter_list|()
block|{
return|return
literal|""
return|;
block|}
comment|/** 	 * @since 3.0 	 */
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
name|String
name|template
init|=
literal|"SELECT #result('UNIQUE' 'long') FROM "
operator|+
name|entity
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|long
index|[]
name|pkHolder
init|=
operator|new
name|long
index|[
literal|1
index|]
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|entity
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|OperationObserver
name|observer
init|=
operator|new
name|DoNothingOperationObserver
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|nextRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|dataRows
parameter_list|)
block|{
if|if
condition|(
name|dataRows
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error fetching PK. Expected one row, got "
operator|+
name|dataRows
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
name|DataRow
name|row
init|=
operator|(
name|DataRow
operator|)
name|dataRows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Number
name|pk
init|=
operator|(
name|Number
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"UNIQUE"
argument_list|)
decl_stmt|;
name|pkHolder
index|[
literal|0
index|]
operator|=
name|pk
operator|.
name|longValue
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|node
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
return|return
name|pkHolder
index|[
literal|0
index|]
return|;
block|}
block|}
end_class

end_unit

