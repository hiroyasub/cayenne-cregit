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
name|Iterator
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
name|dba
operator|.
name|oracle
operator|.
name|OracleAdapter
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
name|DbAttribute
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
name|OracleStackAdapter
extends|extends
name|AccessStackAdapter
block|{
comment|/**      * Constructor for OracleDelegate.      *       * @param adapter      */
specifier|public
name|OracleStackAdapter
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
comment|/**      * Oracle 8i does not support more then 1 "LONG xx" column per table PAINTING_INFO      * need to be fixed.      */
specifier|public
name|void
name|willCreateTables
parameter_list|(
name|Connection
name|con
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
name|DbEntity
name|paintingInfo
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"PAINTING_INFO"
argument_list|)
decl_stmt|;
if|if
condition|(
name|paintingInfo
operator|!=
literal|null
condition|)
block|{
name|DbAttribute
name|textReview
init|=
operator|(
name|DbAttribute
operator|)
name|paintingInfo
operator|.
name|getAttribute
argument_list|(
literal|"TEXT_REVIEW"
argument_list|)
decl_stmt|;
name|textReview
operator|.
name|setType
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|)
expr_stmt|;
name|textReview
operator|.
name|setMaxLength
argument_list|(
literal|255
argument_list|)
expr_stmt|;
block|}
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
literal|"oracle"
argument_list|,
literal|"create-types-pkg.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"oracle"
argument_list|,
literal|"create-select-sp.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"oracle"
argument_list|,
literal|"create-update-sp.sql"
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|con
argument_list|,
literal|"oracle"
argument_list|,
literal|"create-out-sp.sql"
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
name|SELECT_STORED_PROCEDURE
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
name|List
name|params
init|=
operator|new
name|ArrayList
argument_list|(
name|proc
operator|.
name|getCallParameters
argument_list|()
argument_list|)
decl_stmt|;
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
literal|"result"
argument_list|,
name|OracleAdapter
operator|.
name|getOracleCursorType
argument_list|()
argument_list|,
name|ProcedureParameter
operator|.
name|OUT_PARAMETER
argument_list|)
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|params
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
name|ProcedureParameter
name|param
init|=
operator|(
name|ProcedureParameter
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|proc
operator|.
name|addCallParameter
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
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

