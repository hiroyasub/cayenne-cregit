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
name|test
operator|.
name|jdbc
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
name|ParameterMetaData
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
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Time
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
name|List
import|;
end_import

begin_comment
comment|/**  * JDBC utility class for setting up and analyzing the DB data sets. DBHelper  * intentionally bypasses Cayenne stack.  */
end_comment

begin_class
specifier|public
class|class
name|DBHelper
block|{
specifier|protected
name|DataSource
name|dataSource
decl_stmt|;
specifier|public
name|DBHelper
parameter_list|(
name|DataSource
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
comment|/** 	 * Quotes a SQL identifier as appropriate for the given DB. This 	 * implementation returns the identifier unchanged, while subclasses can 	 * implement a custom quoting strategy. 	 */
specifier|protected
name|String
name|quote
parameter_list|(
name|String
name|sqlIdentifier
parameter_list|)
block|{
return|return
name|sqlIdentifier
return|;
block|}
comment|/** 	 * Selects a single row. 	 */
specifier|public
name|Object
index|[]
name|select
parameter_list|(
name|String
name|table
parameter_list|,
specifier|final
name|String
index|[]
name|columns
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|columns
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No columns"
argument_list|)
throw|;
block|}
name|StringBuilder
name|sql
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"select "
argument_list|)
decl_stmt|;
name|sql
operator|.
name|append
argument_list|(
name|quote
argument_list|(
name|columns
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|columns
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|sql
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|quote
argument_list|(
name|columns
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sql
operator|.
name|append
argument_list|(
literal|" from "
argument_list|)
operator|.
name|append
argument_list|(
name|quote
argument_list|(
name|table
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|RowTemplate
argument_list|<
name|Object
index|[]
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|Object
index|[]
name|readRow
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
name|Object
index|[]
name|result
init|=
operator|new
name|Object
index|[
name|columns
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|result
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|result
index|[
name|i
operator|-
literal|1
index|]
operator|=
name|rs
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|selectAll
parameter_list|(
name|String
name|table
parameter_list|,
specifier|final
name|String
index|[]
name|columns
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|columns
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No columns"
argument_list|)
throw|;
block|}
name|StringBuilder
name|sql
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"select "
argument_list|)
decl_stmt|;
name|sql
operator|.
name|append
argument_list|(
name|quote
argument_list|(
name|columns
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|columns
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|sql
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|quote
argument_list|(
name|columns
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sql
operator|.
name|append
argument_list|(
literal|" from "
argument_list|)
operator|.
name|append
argument_list|(
name|quote
argument_list|(
name|table
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|ResultSetTemplate
argument_list|<
name|List
argument_list|<
name|Object
index|[]
argument_list|>
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|readResultSet
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|Object
index|[]
name|row
init|=
operator|new
name|Object
index|[
name|columns
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|row
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|row
index|[
name|i
operator|-
literal|1
index|]
operator|=
name|rs
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
comment|/** 	 * Inserts a single row. Columns types can be null and will be determined 	 * from ParameterMetaData in this case. The later scenario will not work if 	 * values contains nulls and the DB is Oracle. 	 */
specifier|public
name|void
name|insert
parameter_list|(
name|String
name|table
parameter_list|,
name|String
index|[]
name|columns
parameter_list|,
name|Object
index|[]
name|values
parameter_list|,
name|int
index|[]
name|columnTypes
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|columns
operator|.
name|length
operator|!=
name|values
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Columns and values arrays have different sizes: "
operator|+
name|columns
operator|.
name|length
operator|+
literal|" and "
operator|+
name|values
operator|.
name|length
argument_list|)
throw|;
block|}
if|if
condition|(
name|columns
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No columns"
argument_list|)
throw|;
block|}
name|StringBuilder
name|sql
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"INSERT INTO "
argument_list|)
decl_stmt|;
name|sql
operator|.
name|append
argument_list|(
name|quote
argument_list|(
name|table
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
operator|.
name|append
argument_list|(
name|quote
argument_list|(
name|columns
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|columns
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|sql
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|quote
argument_list|(
name|columns
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sql
operator|.
name|append
argument_list|(
literal|") VALUES (?"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|values
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|sql
operator|.
name|append
argument_list|(
literal|", ?"
argument_list|)
expr_stmt|;
block|}
name|sql
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
try|try
init|(
name|Connection
name|c
init|=
name|getConnection
argument_list|()
init|)
block|{
name|String
name|sqlString
init|=
name|sql
operator|.
name|toString
argument_list|()
decl_stmt|;
name|UtilityLogger
operator|.
name|log
argument_list|(
name|sqlString
argument_list|)
expr_stmt|;
name|ParameterMetaData
name|parameters
init|=
literal|null
decl_stmt|;
try|try
init|(
name|PreparedStatement
name|st
init|=
name|c
operator|.
name|prepareStatement
argument_list|(
name|sqlString
argument_list|)
init|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|values
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|values
index|[
name|i
index|]
operator|==
literal|null
condition|)
block|{
name|int
name|type
decl_stmt|;
if|if
condition|(
name|columnTypes
operator|==
literal|null
condition|)
block|{
comment|// check for the right NULL type
if|if
condition|(
name|parameters
operator|==
literal|null
condition|)
block|{
name|parameters
operator|=
name|st
operator|.
name|getParameterMetaData
argument_list|()
expr_stmt|;
block|}
name|type
operator|=
name|parameters
operator|.
name|getParameterType
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
name|columnTypes
index|[
name|i
index|]
expr_stmt|;
block|}
name|st
operator|.
name|setNull
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|columnTypes
operator|!=
literal|null
condition|)
block|{
name|st
operator|.
name|setObject
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|values
index|[
name|i
index|]
argument_list|,
name|columnTypes
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|st
operator|.
name|setObject
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|values
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|st
operator|.
name|executeUpdate
argument_list|()
expr_stmt|;
block|}
name|c
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|deleteAll
parameter_list|(
name|String
name|tableName
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|delete
argument_list|(
name|tableName
argument_list|)
operator|.
name|execute
argument_list|()
return|;
block|}
specifier|public
name|UpdateBuilder
name|update
parameter_list|(
name|String
name|tableName
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
operator|new
name|UpdateBuilder
argument_list|(
name|this
argument_list|,
name|tableName
argument_list|)
return|;
block|}
specifier|public
name|DeleteBuilder
name|delete
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
return|return
operator|new
name|DeleteBuilder
argument_list|(
name|this
argument_list|,
name|tableName
argument_list|)
return|;
block|}
specifier|public
name|int
name|getRowCount
parameter_list|(
name|String
name|table
parameter_list|)
throws|throws
name|SQLException
block|{
name|String
name|sql
init|=
literal|"select count(*) from "
operator|+
name|quote
argument_list|(
name|table
argument_list|)
decl_stmt|;
return|return
operator|new
name|RowTemplate
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|Integer
name|readRow
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|String
name|getString
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|sql
init|=
literal|"select "
operator|+
name|quote
argument_list|(
name|column
argument_list|)
operator|+
literal|" from "
operator|+
name|quote
argument_list|(
name|table
argument_list|)
decl_stmt|;
return|return
operator|new
name|RowTemplate
argument_list|<
name|String
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|String
name|readRow
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getString
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getObject
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|sql
init|=
literal|"select "
operator|+
name|quote
argument_list|(
name|column
argument_list|)
operator|+
literal|" from "
operator|+
name|quote
argument_list|(
name|table
argument_list|)
decl_stmt|;
return|return
operator|new
name|RowTemplate
argument_list|<
name|Object
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|Object
name|readRow
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|byte
name|getByte
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|sql
init|=
literal|"select "
operator|+
name|quote
argument_list|(
name|column
argument_list|)
operator|+
literal|" from "
operator|+
name|quote
argument_list|(
name|table
argument_list|)
decl_stmt|;
return|return
operator|new
name|RowTemplate
argument_list|<
name|Byte
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|Byte
name|readRow
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getByte
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|byte
index|[]
name|getBytes
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|sql
init|=
literal|"select "
operator|+
name|quote
argument_list|(
name|column
argument_list|)
operator|+
literal|" from "
operator|+
name|quote
argument_list|(
name|table
argument_list|)
decl_stmt|;
return|return
operator|new
name|RowTemplate
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|byte
index|[]
name|readRow
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getBytes
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|int
name|getInt
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|sql
init|=
literal|"select "
operator|+
name|quote
argument_list|(
name|column
argument_list|)
operator|+
literal|" from "
operator|+
name|quote
argument_list|(
name|table
argument_list|)
decl_stmt|;
return|return
operator|new
name|RowTemplate
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|Integer
name|readRow
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|long
name|getLong
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|sql
init|=
literal|"select "
operator|+
name|quote
argument_list|(
name|column
argument_list|)
operator|+
literal|" from "
operator|+
name|quote
argument_list|(
name|table
argument_list|)
decl_stmt|;
return|return
operator|new
name|RowTemplate
argument_list|<
name|Long
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|Long
name|readRow
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getLong
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|double
name|getDouble
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|sql
init|=
literal|"select "
operator|+
name|quote
argument_list|(
name|column
argument_list|)
operator|+
literal|" from "
operator|+
name|quote
argument_list|(
name|table
argument_list|)
decl_stmt|;
return|return
operator|new
name|RowTemplate
argument_list|<
name|Double
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|Double
name|readRow
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getDouble
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|getBoolean
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|sql
init|=
literal|"select "
operator|+
name|quote
argument_list|(
name|column
argument_list|)
operator|+
literal|" from "
operator|+
name|quote
argument_list|(
name|table
argument_list|)
decl_stmt|;
return|return
operator|new
name|RowTemplate
argument_list|<
name|Boolean
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|Boolean
name|readRow
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getBoolean
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|java
operator|.
name|util
operator|.
name|Date
name|getUtilDate
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
name|Timestamp
name|ts
init|=
name|getTimestamp
argument_list|(
name|table
argument_list|,
name|column
argument_list|)
decl_stmt|;
return|return
name|ts
operator|!=
literal|null
condition|?
operator|new
name|java
operator|.
name|util
operator|.
name|Date
argument_list|(
name|ts
operator|.
name|getTime
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
specifier|public
name|java
operator|.
name|sql
operator|.
name|Date
name|getSqlDate
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|sql
init|=
literal|"select "
operator|+
name|quote
argument_list|(
name|column
argument_list|)
operator|+
literal|" from "
operator|+
name|quote
argument_list|(
name|table
argument_list|)
decl_stmt|;
return|return
operator|new
name|RowTemplate
argument_list|<
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|java
operator|.
name|sql
operator|.
name|Date
name|readRow
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getDate
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|Time
name|getTime
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|sql
init|=
literal|"select "
operator|+
name|quote
argument_list|(
name|column
argument_list|)
operator|+
literal|" from "
operator|+
name|quote
argument_list|(
name|table
argument_list|)
decl_stmt|;
return|return
operator|new
name|RowTemplate
argument_list|<
name|Time
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|Time
name|readRow
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getTime
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|Timestamp
name|getTimestamp
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
specifier|final
name|String
name|sql
init|=
literal|"select "
operator|+
name|quote
argument_list|(
name|column
argument_list|)
operator|+
literal|" from "
operator|+
name|quote
argument_list|(
name|table
argument_list|)
decl_stmt|;
return|return
operator|new
name|RowTemplate
argument_list|<
name|Timestamp
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
name|Timestamp
name|readRow
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|rs
operator|.
name|getTimestamp
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
return|;
block|}
specifier|public
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|SQLException
block|{
name|Connection
name|connection
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|connection
operator|.
name|setAutoCommit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
try|try
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ignored
parameter_list|)
block|{
block|}
block|}
return|return
name|connection
return|;
block|}
block|}
end_class

end_unit

