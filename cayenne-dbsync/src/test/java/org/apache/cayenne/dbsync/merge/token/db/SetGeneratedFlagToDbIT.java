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
name|dbsync
operator|.
name|merge
operator|.
name|token
operator|.
name|MergerToken
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
name|di
operator|.
name|Inject
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
name|unit
operator|.
name|UnitDbAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_comment
comment|/**  * Test synchronization of generated keys to DB (SetGeneratedFlagToDB merge token)  *  * As there are not many DBMS that fully support create/alter/drop for generated columns  * this test has several actual paths of execution.  *  * 1. If DB has no support for generated values at all  * this test will check that no meaningful tokens are created in sync process  *  * 2. If DB can create generated columns but can't alter them  * this test will check that proper exception it thrown when applying token  *  * 3. If DB can alter generated columns then full check will be performed  * (here is actually two variants as some DB can only drop whereas some can only add generated attribute)  *  * @see DbAdapter#supportsGeneratedKeys()  * @see UnitDbAdapter#supportsGeneratedKeys()  * @see UnitDbAdapter#supportsGeneratedKeysAdd()  * @see UnitDbAdapter#supportsGeneratedKeysDrop()  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|SetGeneratedFlagToDbIT
extends|extends
name|MergeCase
block|{
annotation|@
name|Inject
name|UnitDbAdapter
name|dbAdapter
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|setGeneratedFlag
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|dbEntity
init|=
name|createTestTable
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|DbAttribute
name|attribute
init|=
name|dbEntity
operator|.
name|getAttribute
argument_list|(
literal|"ID"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|attribute
operator|.
name|isGenerated
argument_list|()
argument_list|)
expr_stmt|;
name|attribute
operator|.
name|setGenerated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
name|createMergeTokens
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|dbAdapter
operator|.
name|supportsGeneratedKeys
argument_list|()
condition|)
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|MergerToken
name|token
init|=
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|token
operator|instanceof
name|SetGeneratedFlagToDb
argument_list|)
expr_stmt|;
try|try
block|{
name|execute
argument_list|(
name|token
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dbAdapter
operator|.
name|supportsGeneratedKeysAdd
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"SetGeneratedFlagToDb should fail on current DB"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|ignored
parameter_list|)
block|{
return|return;
block|}
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|dropGeneratedFlag
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|dbEntity
init|=
name|createTestTable
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|DbAttribute
name|attribute
init|=
name|dbEntity
operator|.
name|getAttribute
argument_list|(
literal|"ID"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attribute
operator|.
name|isGenerated
argument_list|()
argument_list|)
expr_stmt|;
name|attribute
operator|.
name|setGenerated
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
name|createMergeTokens
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|dbAdapter
operator|.
name|supportsGeneratedKeys
argument_list|()
condition|)
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|MergerToken
name|token
init|=
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|token
operator|instanceof
name|SetGeneratedFlagToDb
argument_list|)
expr_stmt|;
try|try
block|{
name|execute
argument_list|(
name|token
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dbAdapter
operator|.
name|supportsGeneratedKeysDrop
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"SetGeneratedFlagToDb should fail on current DB"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|ignored
parameter_list|)
block|{
return|return;
block|}
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DbEntity
name|createTestTable
parameter_list|(
name|boolean
name|generated
parameter_list|)
throws|throws
name|Exception
block|{
name|dropTestTables
argument_list|()
expr_stmt|;
name|DbEntity
name|withGenKey
init|=
operator|new
name|DbEntity
argument_list|(
literal|"NEW_TABLE"
argument_list|)
decl_stmt|;
name|DbAttribute
name|attribute
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"ID"
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
name|withGenKey
argument_list|)
decl_stmt|;
name|attribute
operator|.
name|setMandatory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|attribute
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|attribute
operator|.
name|setGenerated
argument_list|(
name|generated
argument_list|)
expr_stmt|;
name|withGenKey
operator|.
name|addAttribute
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|withGenKey
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
return|return
name|withGenKey
return|;
block|}
annotation|@
name|After
specifier|public
name|void
name|dropTestTables
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"NEW_TABLE"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|removeDbEntity
argument_list|(
literal|"NEW_TABLE"
argument_list|)
expr_stmt|;
block|}
name|dropTableIfPresent
argument_list|(
literal|"NEW_TABLE"
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
end_class

end_unit

