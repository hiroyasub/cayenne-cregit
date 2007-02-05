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
operator|.
name|itest
operator|.
name|ch5
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManagerFactory
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
name|itest
operator|.
name|jpa
operator|.
name|ItestSetup
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
name|itest
operator|.
name|jpa
operator|.
name|JpaTestCase
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
name|jpa
operator|.
name|itest
operator|.
name|ch5
operator|.
name|entity
operator|.
name|SimpleEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|transaction
operator|.
name|jta11
operator|.
name|GeronimoTransactionManagerJTA11
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|openejb
operator|.
name|client
operator|.
name|LocalInitialContextFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|openejb
operator|.
name|persistence
operator|.
name|JtaEntityManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|openejb
operator|.
name|persistence
operator|.
name|JtaEntityManagerRegistry
import|;
end_import

begin_class
specifier|public
class|class
name|_5_5_1_JtaEntityManagerTest
extends|extends
name|JpaTestCase
block|{
specifier|public
name|void
name|testPersist
parameter_list|()
throws|throws
name|Exception
block|{
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"SimpleEntity"
argument_list|)
expr_stmt|;
name|GeronimoTransactionManagerJTA11
name|tm
init|=
operator|new
name|GeronimoTransactionManagerJTA11
argument_list|()
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|Context
operator|.
name|INITIAL_CONTEXT_FACTORY
argument_list|,
name|LocalInitialContextFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// somehow OpenEJB LocalInitialContextFactory requires 2 IC's to be initilaized to
comment|// fully bootstrap the environment
operator|new
name|InitialContext
argument_list|()
expr_stmt|;
operator|new
name|InitialContext
argument_list|()
operator|.
name|bind
argument_list|(
literal|"java:comp/TransactionSynchronizationRegistry"
argument_list|,
name|tm
argument_list|)
expr_stmt|;
name|EntityManagerFactory
name|factory
init|=
name|ItestSetup
operator|.
name|getInstance
argument_list|()
operator|.
name|createEntityManagerFactory
argument_list|()
decl_stmt|;
name|JtaEntityManagerRegistry
name|registry
init|=
operator|new
name|JtaEntityManagerRegistry
argument_list|(
name|tm
argument_list|)
decl_stmt|;
name|tm
operator|.
name|begin
argument_list|()
expr_stmt|;
name|EntityManager
name|entityManager
init|=
operator|new
name|JtaEntityManager
argument_list|(
name|registry
argument_list|,
name|factory
argument_list|,
operator|new
name|Properties
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|SimpleEntity
name|e
init|=
operator|new
name|SimpleEntity
argument_list|()
decl_stmt|;
name|e
operator|.
name|setProperty1
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|persist
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|tm
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getDbHelper
argument_list|()
operator|.
name|getRowCount
argument_list|(
literal|"SimpleEntity"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// TODO: andrus, 1/3/2007 - implement - need to emulate the container environment
comment|// public void testPersistTransactionRequiredException() throws Exception {
comment|// // throws TransactionRequiredException if invoked on a
comment|// // container-managed entity manager of type
comment|// // PersistenceContextType.TRANSACTION and there is
comment|// // no transaction.
comment|//
comment|// EntityManager em = getEntityManager();
comment|//
comment|// SimpleEntity e = new SimpleEntity();
comment|// e.setProperty1("XXX");
comment|//
comment|// try {
comment|// em.persist(e);
comment|// em.getTransaction().commit();
comment|// fail("Must have thrown TransactionRequiredException");
comment|// }
comment|// catch (TransactionRequiredException ex) {
comment|// // expected
comment|// }
comment|// }
block|}
end_class

end_unit

