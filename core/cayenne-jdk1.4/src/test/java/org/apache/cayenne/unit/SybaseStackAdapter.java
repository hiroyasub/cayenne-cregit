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
name|Statement
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
name|List
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
name|Procedure
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|SybaseStackAdapter
extends|extends
name|AccessStackAdapter
block|{
comment|/**      * Constructor for SybaseDelegate.      *       * @param adapter      */
specifier|public
name|SybaseStackAdapter
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|super
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|supportsStoredProcedures
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|createdTables
parameter_list|(
name|Connection
name|con
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|Procedure
name|proc
init|=
name|map
operator|.
name|getProcedure
argument_list|(
literal|"cayenne_tst_select_proc"
argument_list|)
decl_stmt|;
if|if
condition|(
name|proc
operator|!=
literal|null
operator|&&
name|proc
operator|.
name|getDataMap
argument_list|()
operator|==
name|map
condition|)
block|{
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"sybase"
argument_list|,
literal|"create-select-sp.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"sybase"
argument_list|,
literal|"create-update-sp.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"sybase"
argument_list|,
literal|"create-out-sp.sql"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|willDropTables
parameter_list|(
name|Connection
name|con
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|Collection
name|tablesToDrop
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|willDropTables
argument_list|(
name|con
argument_list|,
name|map
argument_list|,
name|tablesToDrop
argument_list|)
expr_stmt|;
name|Procedure
name|proc
init|=
name|map
operator|.
name|getProcedure
argument_list|(
literal|"cayenne_tst_select_proc"
argument_list|)
decl_stmt|;
if|if
condition|(
name|proc
operator|!=
literal|null
operator|&&
name|proc
operator|.
name|getDataMap
argument_list|()
operator|==
name|map
condition|)
block|{
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"sybase"
argument_list|,
literal|"drop-select-sp.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"sybase"
argument_list|,
literal|"drop-update-sp.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"sybase"
argument_list|,
literal|"drop-out-sp.sql"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|dropConstraints
parameter_list|(
name|Connection
name|con
parameter_list|,
name|String
name|tableName
parameter_list|)
throws|throws
name|Exception
block|{
name|List
name|names
init|=
operator|new
name|ArrayList
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|Statement
name|select
init|=
name|con
operator|.
name|createStatement
argument_list|()
decl_stmt|;
try|try
block|{
name|ResultSet
name|rs
init|=
name|select
operator|.
name|executeQuery
argument_list|(
literal|"SELECT t0.name "
operator|+
literal|"FROM sysobjects t0, sysconstraints t1, sysobjects t2 "
operator|+
literal|"WHERE t0.id = t1.constrid and t1.tableid = t2.id and t2.name = '"
operator|+
name|tableName
operator|+
literal|"'"
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
name|names
operator|.
name|add
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"name"
argument_list|)
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
name|select
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|names
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"alter table "
operator|+
name|tableName
operator|+
literal|" drop constraint "
operator|+
name|names
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|supportsLobs
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|handlesNullVsEmptyLOBs
parameter_list|()
block|{
comment|// TODO Sybase handling of this must be fixed
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

