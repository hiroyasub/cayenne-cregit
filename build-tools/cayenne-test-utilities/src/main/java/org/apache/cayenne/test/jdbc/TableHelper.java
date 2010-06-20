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
name|test
operator|.
name|jdbc
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

begin_comment
comment|/**  * JDBC utility class for setting up and analyzing the DB data sets for a single table.  * TableHelper intentionally bypasses Cayenne stack.  */
end_comment

begin_class
specifier|public
class|class
name|TableHelper
block|{
specifier|protected
name|String
name|tableName
decl_stmt|;
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|String
index|[]
name|columns
decl_stmt|;
specifier|public
name|TableHelper
parameter_list|(
name|DBHelper
name|dbHelper
parameter_list|,
name|String
name|tableName
parameter_list|)
block|{
name|this
operator|.
name|dbHelper
operator|=
name|dbHelper
expr_stmt|;
name|this
operator|.
name|tableName
operator|=
name|tableName
expr_stmt|;
block|}
specifier|public
name|TableHelper
parameter_list|(
name|DBHelper
name|dbHelper
parameter_list|,
name|String
name|tableName
parameter_list|,
name|String
modifier|...
name|columns
parameter_list|)
block|{
name|this
argument_list|(
name|dbHelper
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
name|setColumns
argument_list|(
name|columns
argument_list|)
expr_stmt|;
block|}
specifier|public
name|UpdateBuilder
name|update
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|update
argument_list|(
name|tableName
argument_list|)
return|;
block|}
specifier|public
name|DeleteBuilder
name|delete
parameter_list|()
block|{
return|return
name|dbHelper
operator|.
name|delete
argument_list|(
name|tableName
argument_list|)
return|;
block|}
specifier|public
name|int
name|deleteAll
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|deleteAll
argument_list|(
name|tableName
argument_list|)
return|;
block|}
specifier|public
name|String
name|getTableName
parameter_list|()
block|{
return|return
name|tableName
return|;
block|}
comment|/**      * Sets columns that will be implicitly used in subsequent inserts and selects.      */
specifier|public
name|TableHelper
name|setColumns
parameter_list|(
name|String
modifier|...
name|columns
parameter_list|)
block|{
name|this
operator|.
name|columns
operator|=
name|columns
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|TableHelper
name|insert
parameter_list|(
name|Object
modifier|...
name|values
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|this
operator|.
name|columns
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Call 'setColumns' to prepare insert"
argument_list|)
throw|;
block|}
if|if
condition|(
name|this
operator|.
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
literal|"Columns and values arrays are of different size"
argument_list|)
throw|;
block|}
name|dbHelper
operator|.
name|insert
argument_list|(
name|tableName
argument_list|,
name|columns
argument_list|,
name|values
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Selects a single row from the mapped table.      */
specifier|public
name|Object
index|[]
name|select
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|select
argument_list|(
name|tableName
argument_list|,
name|columns
argument_list|)
return|;
block|}
specifier|public
name|int
name|getRowCount
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|getRowCount
argument_list|(
name|tableName
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getObject
parameter_list|(
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|getObject
argument_list|(
name|tableName
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|byte
name|getByte
parameter_list|(
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|getByte
argument_list|(
name|tableName
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|byte
index|[]
name|getBytes
parameter_list|(
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|getBytes
argument_list|(
name|tableName
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|int
name|getInt
parameter_list|(
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|getInt
argument_list|(
name|tableName
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|long
name|getLong
parameter_list|(
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|getLong
argument_list|(
name|tableName
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|double
name|getDouble
parameter_list|(
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|getDouble
argument_list|(
name|tableName
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|getBoolean
parameter_list|(
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|getBoolean
argument_list|(
name|tableName
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|String
name|getString
parameter_list|(
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|getString
argument_list|(
name|tableName
argument_list|,
name|column
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
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|getUtilDate
argument_list|(
name|tableName
argument_list|,
name|column
argument_list|)
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
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|getSqlDate
argument_list|(
name|tableName
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|Time
name|getTime
parameter_list|(
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|getTime
argument_list|(
name|tableName
argument_list|,
name|column
argument_list|)
return|;
block|}
specifier|public
name|Timestamp
name|getTimestamp
parameter_list|(
name|String
name|column
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|dbHelper
operator|.
name|getTimestamp
argument_list|(
name|tableName
argument_list|,
name|column
argument_list|)
return|;
block|}
block|}
end_class

end_unit

