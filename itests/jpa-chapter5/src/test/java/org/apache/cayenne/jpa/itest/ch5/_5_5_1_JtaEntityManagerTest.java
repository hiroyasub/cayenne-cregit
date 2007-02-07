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
name|transaction
operator|.
name|TransactionManager
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
name|OpenEJBContainer
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
name|TransactionManager
name|tm
init|=
name|OpenEJBContainer
operator|.
name|getContainer
argument_list|()
operator|.
name|getTxManager
argument_list|()
decl_stmt|;
name|tm
operator|.
name|begin
argument_list|()
expr_stmt|;
name|EntityManager
name|entityManager
init|=
name|ItestSetup
operator|.
name|getInstance
argument_list|()
operator|.
name|createContainerManagedEntityManager
argument_list|()
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
specifier|public
name|void
name|testPersistTransactionRequiredException
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: andrus, 2/7/2007 - uncomment once
comment|// https://issues.apache.org/jira/browse/GERONIMO-2809 is fixed and we can test
comment|// transactional behavior
comment|// EntityManager entityManager = ItestSetup
comment|// .getInstance()
comment|// .createContainerManagedEntityManager();
comment|//
comment|// SimpleEntity e = new SimpleEntity();
comment|// e.setProperty1("XXX");
comment|//
comment|// assertFalse(OpenEJBContainer.getContainer().isActiveTransaction());
comment|//
comment|// // throws TransactionRequiredException if invoked on a
comment|// // container-managed entity manager of type
comment|// // PersistenceContextType.TRANSACTION and there is
comment|// // no transaction.
comment|//
comment|// try {
comment|// entityManager.persist(e);
comment|// fail("TransactionRequiredException wasn't thrown");
comment|// }
comment|// catch (TransactionRequiredException ex) {
comment|// // expected
comment|// }
block|}
block|}
end_class

end_unit

