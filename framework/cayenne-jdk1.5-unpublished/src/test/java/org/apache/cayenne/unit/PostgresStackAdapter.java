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
name|Types
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
name|access
operator|.
name|DataContextProcedureQueryTest
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
name|ProcedureParameter
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|PostgresStackAdapter
extends|extends
name|AccessStackAdapter
block|{
specifier|public
name|PostgresStackAdapter
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
name|tablesToDrop
parameter_list|)
throws|throws
name|Exception
block|{
comment|// avoid dropping constraints...
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
name|canMakeObjectsOutOfProcedures
parameter_list|()
block|{
comment|// we are a victim of CAY-148 - column capitalization...
return|return
literal|false
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
literal|"postgresql"
argument_list|,
literal|"create-select-sp.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"postgresql"
argument_list|,
literal|"create-update-sp.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"postgresql"
argument_list|,
literal|"create-update-sp2.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"postgresql"
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
name|tweakProcedure
parameter_list|(
name|Procedure
name|proc
parameter_list|)
block|{
if|if
condition|(
name|DataContextProcedureQueryTest
operator|.
name|OUT_STORED_PROCEDURE
operator|.
name|equals
argument_list|(
name|proc
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|proc
operator|.
name|getCallParameters
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|2
condition|)
block|{
name|proc
operator|.
name|clearCallParameters
argument_list|()
expr_stmt|;
name|proc
operator|.
name|addCallParameter
argument_list|(
operator|new
name|ProcedureParameter
argument_list|(
literal|"out_param"
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
name|ProcedureParameter
operator|.
name|OUT_PARAMETER
argument_list|)
argument_list|)
expr_stmt|;
name|proc
operator|.
name|addCallParameter
argument_list|(
operator|new
name|ProcedureParameter
argument_list|(
literal|"in_param"
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
name|ProcedureParameter
operator|.
name|IN_PARAMETER
argument_list|)
argument_list|)
expr_stmt|;
name|proc
operator|.
name|setReturningValue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

