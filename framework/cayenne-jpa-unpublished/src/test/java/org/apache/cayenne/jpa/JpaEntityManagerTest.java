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
name|jpa
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityTransaction
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|FlushModeType
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|JpaEntityManagerTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testOpenClose
parameter_list|()
throws|throws
name|Exception
block|{
name|JpaEntityManagerFactory
name|factory
init|=
operator|new
name|JpaEntityManagerFactory
argument_list|(
literal|null
argument_list|,
operator|new
name|MockPersistenceUnitInfo
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isOpen
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|JpaEntityManager
name|m
init|=
operator|new
name|MockJpaEntityManager
argument_list|(
name|factory
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|EntityTransaction
name|createResourceLocalTransaction
parameter_list|()
block|{
return|return
operator|new
name|MockEntityTransaction
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isActive
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
return|;
block|}
block|}
decl_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|m
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
comment|// check that all methods throw ... or at least some :-)
try|try
block|{
name|m
operator|.
name|close
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Closed EntityManager is supposed to throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|m
operator|.
name|contains
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Closed EntityManager is supposed to throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|m
operator|.
name|createNamedQuery
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Closed EntityManager is supposed to throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|m
operator|.
name|createNativeQuery
argument_list|(
literal|"SELECT * FROM A"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Closed EntityManager is supposed to throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|m
operator|.
name|setFlushMode
argument_list|(
name|FlushModeType
operator|.
name|AUTO
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Closed EntityManager is supposed to throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|public
name|void
name|testCloseActiveTransactionInProgress
parameter_list|()
block|{
name|JpaEntityManagerFactory
name|factory
init|=
operator|new
name|JpaEntityManagerFactory
argument_list|(
literal|null
argument_list|,
operator|new
name|MockPersistenceUnitInfo
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isOpen
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|JpaEntityManager
name|m
init|=
operator|new
name|MockJpaEntityManager
argument_list|(
name|factory
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|EntityTransaction
name|createResourceLocalTransaction
parameter_list|()
block|{
return|return
operator|new
name|MockEntityTransaction
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isActive
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
return|;
block|}
block|}
decl_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
comment|// make sure we trigger transaction creation
name|assertNotNull
argument_list|(
name|m
operator|.
name|getTransaction
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|m
operator|.
name|close
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"EntityManager is supposed to throw on 'close' when it has transaction in progress"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|public
name|void
name|testCloseFactoryClosed
parameter_list|()
block|{
specifier|final
name|boolean
index|[]
name|factoryCloseState
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|JpaEntityManagerFactory
name|factory
init|=
operator|new
name|JpaEntityManagerFactory
argument_list|(
literal|null
argument_list|,
operator|new
name|MockPersistenceUnitInfo
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isOpen
parameter_list|()
block|{
return|return
operator|!
name|factoryCloseState
index|[
literal|0
index|]
return|;
block|}
block|}
decl_stmt|;
name|JpaEntityManager
name|m
init|=
operator|new
name|MockJpaEntityManager
argument_list|(
name|factory
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
name|factoryCloseState
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
name|assertFalse
argument_list|(
name|m
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

