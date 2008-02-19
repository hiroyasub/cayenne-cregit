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
name|ch3
package|;
end_package

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
name|Query
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
name|EntityManagerCase
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
name|ch3
operator|.
name|entity
operator|.
name|SimpleEntity
import|;
end_import

begin_class
specifier|public
class|class
name|_3_6_6_SQLQueriesTest
extends|extends
name|EntityManagerCase
block|{
specifier|public
name|void
name|testNativeEntityQuery
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
name|getTableHelper
argument_list|(
literal|"SimpleEntity"
argument_list|)
operator|.
name|deleteAll
argument_list|()
operator|.
name|setColumns
argument_list|(
literal|"id"
argument_list|,
literal|"property1"
argument_list|)
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"X"
argument_list|)
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"Y"
argument_list|)
expr_stmt|;
name|EntityManager
name|em
init|=
name|getEntityManager
argument_list|()
decl_stmt|;
name|Query
name|query
init|=
name|em
operator|.
name|createNativeQuery
argument_list|(
literal|"SELECT id, property1 FROM SimpleEntity ORDER BY property1"
argument_list|,
name|SimpleEntity
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// TODO: andrus 2/18/2008 - this fails because of wrong column name
comment|// capitalization... need to figure a portable solution
comment|// List result = query.getResultList();
comment|// assertNotNull(result);
comment|// assertEquals(2, result.size());
comment|// assertTrue(result.get(0) instanceof SimpleEntity);
block|}
specifier|public
name|void
name|testNativeEntityQueryMappedResult
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
name|getTableHelper
argument_list|(
literal|"SimpleEntity"
argument_list|)
operator|.
name|deleteAll
argument_list|()
operator|.
name|setColumns
argument_list|(
literal|"id"
argument_list|,
literal|"property1"
argument_list|)
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"X"
argument_list|)
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"Y"
argument_list|)
expr_stmt|;
comment|// TODO: andrus 2/18/2008 map named JPA SQLResultSetMappings to Cayenne
comment|// EntityManager em = getEntityManager();
comment|// Query query = em.createNativeQuery(
comment|// "SELECT ID as X, ID + 5 as Y, ID + 6 as Z FROM SimpleEntity ORDER BY ID",
comment|// "rs1");
comment|//
comment|// List result = query.getResultList();
comment|// assertNotNull(result);
comment|// assertEquals(2, result.size());
comment|// assertTrue(result.get(0) instanceof Object[]);
block|}
block|}
end_class

end_unit

