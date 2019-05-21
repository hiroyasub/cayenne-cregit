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
name|dbsync
operator|.
name|merge
operator|.
name|token
operator|.
name|db
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbsync
operator|.
name|merge
operator|.
name|MergeCase
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|SetAllowNullToDbIT
extends|extends
name|MergeCase
block|{
annotation|@
name|Test
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|dbEntity
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
comment|// create and add new column to model and db
name|DbAttribute
name|column
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"NEWCOL2"
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|dbEntity
argument_list|)
decl_stmt|;
try|try
block|{
name|column
operator|.
name|setMandatory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|column
operator|.
name|setMaxLength
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addAttribute
argument_list|(
name|column
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// check that is was merged
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// set null
name|column
operator|.
name|setMandatory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// merge to db
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// check that is was merged
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// clean up
block|}
finally|finally
block|{
name|dbEntity
operator|.
name|removeAttribute
argument_list|(
name|column
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

