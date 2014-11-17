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
name|log
operator|.
name|JdbcEventLogger
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
name|testdo
operator|.
name|testmap
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
name|tx
operator|.
name|BaseTransaction
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
name|tx
operator|.
name|CayenneTransaction
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
name|tx
operator|.
name|Transaction
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
name|di
operator|.
name|server
operator|.
name|CayenneProjects
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
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
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
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
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
name|fail
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|TransactionThreadIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|JdbcEventLogger
name|logger
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testThreadConnectionReuseOnSelect
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionCounterTx
name|t
init|=
operator|new
name|ConnectionCounterTx
argument_list|(
operator|new
name|CayenneTransaction
argument_list|(
name|logger
argument_list|)
argument_list|)
decl_stmt|;
name|BaseTransaction
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
name|context
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
name|t
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
name|context
operator|.
name|performQuery
argument_list|(
name|q2
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|BaseTransaction
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
class|class
name|ConnectionCounterTx
implements|implements
name|Transaction
block|{
specifier|private
name|Transaction
name|delegate
decl_stmt|;
name|int
name|connectionCount
decl_stmt|;
name|ConnectionCounterTx
parameter_list|(
name|Transaction
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
specifier|public
name|void
name|begin
parameter_list|()
block|{
name|delegate
operator|.
name|begin
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|commit
parameter_list|()
block|{
name|delegate
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|rollback
parameter_list|()
block|{
name|delegate
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setRollbackOnly
parameter_list|()
block|{
name|delegate
operator|.
name|setRollbackOnly
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|isRollbackOnly
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|isRollbackOnly
argument_list|()
return|;
block|}
specifier|public
name|Connection
name|getConnection
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|getConnection
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|void
name|addConnection
parameter_list|(
name|String
name|name
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
name|delegate
operator|.
name|addConnection
argument_list|(
name|name
argument_list|,
name|connection
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

