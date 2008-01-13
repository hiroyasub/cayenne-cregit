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
name|query
operator|.
name|SQLTemplate
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
name|SelectQuery
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
name|TransactionThreadTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testThreadConnectionReuseOnSelect
parameter_list|()
throws|throws
name|Exception
block|{
name|Delegate
name|delegate
init|=
operator|new
name|Delegate
argument_list|()
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
name|SelectQuery
name|q1
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|q1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|delegate
operator|.
name|connectionCount
argument_list|)
expr_stmt|;
comment|// delegate will fail if the second query opens a new connection
name|SelectQuery
name|q2
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|q2
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Transaction
operator|.
name|bindThreadTransaction
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|t
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testThreadConnectionReuseOnQueryFromWillCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|Artist
name|a
init|=
name|createDataContext
argument_list|()
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
literal|"aaa"
argument_list|)
expr_stmt|;
name|Delegate
name|delegate
init|=
operator|new
name|Delegate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|willCommit
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
block|{
comment|// insert another artist directly
name|SQLTemplate
name|template
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"insert into ARTIST (ARTIST_ID, ARTIST_NAME) values (1, 'bbb')"
argument_list|)
decl_stmt|;
name|createDataContext
argument_list|()
operator|.
name|performNonSelectingQuery
argument_list|(
name|template
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|getDomain
argument_list|()
operator|.
name|setTransactionDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
try|try
block|{
name|a
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|getDomain
argument_list|()
operator|.
name|setTransactionDelegate
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|createDataContext
argument_list|()
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
class|class
name|Delegate
implements|implements
name|TransactionDelegate
block|{
name|int
name|connectionCount
decl_stmt|;
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
if|if
condition|(
name|connectionCount
operator|++
operator|>
literal|0
condition|)
block|{
name|fail
argument_list|(
literal|"Invalid attempt to add connection"
argument_list|)
expr_stmt|;
block|}
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
block|}
specifier|public
name|void
name|didRollback
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
block|{
block|}
specifier|public
name|boolean
name|willCommit
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|willMarkAsRollbackOnly
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|willRollback
parameter_list|(
name|Transaction
name|transaction
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

