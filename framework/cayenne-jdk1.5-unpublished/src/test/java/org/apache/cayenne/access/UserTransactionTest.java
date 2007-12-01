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
name|access
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
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|Artist
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
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|UserTransactionTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"AAA"
argument_list|)
expr_stmt|;
specifier|final
name|boolean
index|[]
name|willAddConnectionCalled
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
specifier|final
name|boolean
index|[]
name|willCommitCalled
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
specifier|final
name|boolean
index|[]
name|didCommitCalled
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|TransactionDelegate
name|delegate
init|=
operator|new
name|MockTransactionDelegate
argument_list|()
block|{
specifier|public
name|boolean
name|willAddConnection
parameter_list|(
name|Transaction
name|transaction
parameter_list|,
name|Connection
name|connection
parameter_list|)
block|{
name|willAddConnectionCalled
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|willCommit
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
block|{
name|willCommitCalled
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|didCommit
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
block|{
name|didCommitCalled
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
block|}
block|}
decl_stmt|;
name|Transaction
name|t
init|=
name|Transaction
operator|.
name|internalTransaction
argument_list|(
name|delegate
argument_list|)
decl_stmt|;
name|Transaction
operator|.
name|bindThreadTransaction
argument_list|(
name|t
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"User transaction was ignored"
argument_list|,
name|willAddConnectionCalled
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"User transaction was unexpectedly committed"
argument_list|,
name|willCommitCalled
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"User transaction was unexpectedly committed"
argument_list|,
name|didCommitCalled
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|t
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
comment|// ignore
block|}
name|Transaction
operator|.
name|bindThreadTransaction
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

