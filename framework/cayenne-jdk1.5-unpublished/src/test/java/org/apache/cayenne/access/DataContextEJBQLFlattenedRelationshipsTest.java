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
name|Cayenne
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
name|query
operator|.
name|EJBQLQuery
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
name|relationship
operator|.
name|FlattenedTest1
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
name|RelationshipCase
import|;
end_import

begin_class
specifier|public
class|class
name|DataContextEJBQLFlattenedRelationshipsTest
extends|extends
name|RelationshipCase
block|{
specifier|public
name|void
name|testCollectionMemberOfThetaJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testCollectionMemberOfThetaJoin"
argument_list|)
expr_stmt|;
name|String
name|ejbql
init|=
literal|"SELECT f FROM FlattenedTest3 f, FlattenedTest1 ft "
operator|+
literal|"WHERE f MEMBER OF ft.ft3Array AND ft = :ft"
decl_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|FlattenedTest1
name|ft
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|FlattenedTest1
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameter
argument_list|(
literal|"ft"
argument_list|,
name|ft
argument_list|)
expr_stmt|;
comment|// TODO: andrus 2008/06/09 - this fails until we fix CAY-1069 (for correlated join
comment|// case see EJBQLConditionTranslator.visitMemberOf(..)
comment|// List<?> objects = context.performQuery(query);
comment|// assertEquals(2, objects.size());
comment|//
comment|// Set<Object> ids = new HashSet<Object>();
comment|// Iterator<?> it = objects.iterator();
comment|// while (it.hasNext()) {
comment|// Object id = Cayenne.pkForObject((Persistent) it.next());
comment|// ids.add(id);
comment|// }
comment|//
comment|// assertTrue(ids.contains(new Integer(2)));
comment|// assertTrue(ids.contains(new Integer(3)));
block|}
block|}
end_class

end_unit

