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
name|util
operator|.
name|Arrays
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
name|map
operator|.
name|Procedure
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|MySQLUnitDbAdapter
extends|extends
name|UnitDbAdapter
block|{
specifier|static
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|NO_CONSTRAINTS_TABLES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"REFLEXIVE_AND_TO_ONE"
argument_list|,
literal|"ARTGROUP"
argument_list|,
literal|"FK_OF_DIFFERENT_TYPE"
argument_list|)
decl_stmt|;
specifier|public
name|MySQLUnitDbAdapter
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
annotation|@
name|Override
specifier|public
name|boolean
name|supportsCatalogs
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsLobs
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsBitwiseOps
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsCaseSensitiveLike
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsStoredProcedures
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsTrimChar
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
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
if|if
condition|(
name|map
operator|.
name|getProcedureMap
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"cayenne_tst_select_proc"
argument_list|)
condition|)
block|{
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"mysql"
argument_list|,
literal|"create-select-sp.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"mysql"
argument_list|,
literal|"create-update-sp.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"mysql"
argument_list|,
literal|"create-update-sp2.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"mysql"
argument_list|,
literal|"create-out-sp.sql"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|willDropTables
parameter_list|(
name|Connection
name|conn
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|tablesToDrop
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
name|conn
argument_list|,
literal|"mysql"
argument_list|,
literal|"drop-select-sp.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|conn
argument_list|,
literal|"mysql"
argument_list|,
literal|"drop-update-sp.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|conn
argument_list|,
literal|"mysql"
argument_list|,
literal|"drop-update-sp2.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|conn
argument_list|,
literal|"mysql"
argument_list|,
literal|"drop-out-sp.sql"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsFKConstraints
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
comment|// MySQL supports that, but there are problems deleting objects from such
comment|// tables...
return|return
operator|!
name|NO_CONSTRAINTS_TABLES
operator|.
name|contains
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

