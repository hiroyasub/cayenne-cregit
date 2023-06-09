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
name|tx
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
name|ObjectContext
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
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|util
operator|.
name|Map
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
name|UserTransactionIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
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
name|testCommit
parameter_list|()
throws|throws
name|Exception
block|{
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
name|TxWrapper
name|t
init|=
operator|new
name|TxWrapper
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
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|t
operator|.
name|rollback
argument_list|()
expr_stmt|;
name|BaseTransaction
operator|.
name|bindThreadTransaction
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|t
operator|.
name|commitCount
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|t
operator|.
name|getConnections
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
class|class
name|TxWrapper
implements|implements
name|Transaction
block|{
name|int
name|commitCount
decl_stmt|;
specifier|private
name|Transaction
name|delegate
decl_stmt|;
name|TxWrapper
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
name|commitCount
operator|++
expr_stmt|;
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
annotation|@
name|Override
specifier|public
name|Connection
name|getOrCreateConnection
parameter_list|(
name|String
name|connectionName
parameter_list|,
name|DataSource
name|dataSource
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|delegate
operator|.
name|getOrCreateConnection
argument_list|(
name|connectionName
argument_list|,
name|dataSource
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Connection
argument_list|>
name|getConnections
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getConnections
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addListener
parameter_list|(
name|TransactionListener
name|listener
parameter_list|)
block|{
name|delegate
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isExternal
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

