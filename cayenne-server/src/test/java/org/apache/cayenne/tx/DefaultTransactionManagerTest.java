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
name|tx
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
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
name|configuration
operator|.
name|server
operator|.
name|TransactionFactory
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DefaultTransactionManagerTest
extends|extends
name|ServerCase
block|{
specifier|public
name|void
name|testPerformInTransaction_NoTx
parameter_list|()
block|{
specifier|final
name|BaseTransaction
name|tx
init|=
name|mock
argument_list|(
name|BaseTransaction
operator|.
name|class
argument_list|)
decl_stmt|;
name|TransactionFactory
name|txFactory
init|=
name|mock
argument_list|(
name|TransactionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|txFactory
operator|.
name|createTransaction
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|tx
argument_list|)
expr_stmt|;
name|DefaultTransactionManager
name|txManager
init|=
operator|new
name|DefaultTransactionManager
argument_list|(
name|txFactory
argument_list|,
name|mock
argument_list|(
name|JdbcEventLogger
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|expectedResult
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|txManager
operator|.
name|performInTransaction
argument_list|(
operator|new
name|TransactionalOperation
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|perform
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|BaseTransaction
operator|.
name|getThreadTransaction
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|expectedResult
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|expectedResult
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPerformInTransaction_ExistingTx
parameter_list|()
block|{
specifier|final
name|BaseTransaction
name|tx1
init|=
name|mock
argument_list|(
name|BaseTransaction
operator|.
name|class
argument_list|)
decl_stmt|;
name|TransactionFactory
name|txFactory
init|=
name|mock
argument_list|(
name|TransactionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|txFactory
operator|.
name|createTransaction
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|tx1
argument_list|)
expr_stmt|;
name|DefaultTransactionManager
name|txManager
init|=
operator|new
name|DefaultTransactionManager
argument_list|(
name|txFactory
argument_list|,
name|mock
argument_list|(
name|JdbcEventLogger
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|BaseTransaction
name|tx2
init|=
name|mock
argument_list|(
name|BaseTransaction
operator|.
name|class
argument_list|)
decl_stmt|;
name|BaseTransaction
operator|.
name|bindThreadTransaction
argument_list|(
name|tx2
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|Object
name|expectedResult
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|txManager
operator|.
name|performInTransaction
argument_list|(
operator|new
name|TransactionalOperation
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|perform
parameter_list|()
block|{
name|assertSame
argument_list|(
name|tx2
argument_list|,
name|BaseTransaction
operator|.
name|getThreadTransaction
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|expectedResult
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|expectedResult
argument_list|,
name|result
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
block|}
block|}
block|}
end_class

end_unit

