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

begin_class
specifier|public
class|class
name|_5_5_2_ResourceLocalEntityManagerTest
extends|extends
name|JpaTestCase
block|{
specifier|public
name|void
name|testResourceLocalEntityManager
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: andrus 2/11/2007 - this fails due to some classloader issues with peer
comment|// tests
comment|// getDbHelper().deleteAll("RLEntity1");
comment|//
comment|// TransactionManager tm = OpenEJBContainer.getContainer().getTxManager();
comment|// tm.begin();
comment|//
comment|// EntityManager entityManager = ItestSetup
comment|// .getInstance("itest-non-jta")
comment|// .createEntityManager();
comment|//
comment|// RLEntity1 e = new RLEntity1();
comment|// e.setProperty1("XXX");
comment|// entityManager.persist(e);
comment|//
comment|// tm.commit();
comment|// assertEquals(0, getDbHelper().getRowCount("RLEntity1"));
comment|// entityManager.getTransaction().commit();
comment|// assertEquals(1, getDbHelper().getRowCount("RLEntity1"));
block|}
block|}
end_class

end_unit

