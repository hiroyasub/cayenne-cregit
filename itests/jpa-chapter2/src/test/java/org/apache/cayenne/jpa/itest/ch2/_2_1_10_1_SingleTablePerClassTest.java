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
name|ch2
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
name|ItestTableUtils
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

begin_class
specifier|public
class|class
name|_2_1_10_1_SingleTablePerClassTest
extends|extends
name|EntityManagerCase
block|{
specifier|public
name|void
name|testSelectSuper
parameter_list|()
throws|throws
name|Exception
block|{
name|ItestTableUtils
name|helper
init|=
name|getTableHelper
argument_list|(
literal|"ST_INHERITANCE"
argument_list|)
decl_stmt|;
name|helper
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|helper
operator|.
name|setColumns
argument_list|(
literal|"id"
argument_list|,
literal|"objectType"
argument_list|,
literal|"propertyA"
argument_list|,
literal|"propertyB"
argument_list|,
literal|"propertyC"
argument_list|)
expr_stmt|;
name|helper
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"A"
argument_list|,
literal|"1"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|helper
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"A"
argument_list|,
literal|"2"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|helper
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"B"
argument_list|,
literal|"3"
argument_list|,
literal|"BX"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|helper
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|"C"
argument_list|,
literal|"4"
argument_list|,
literal|null
argument_list|,
literal|"CX"
argument_list|)
expr_stmt|;
comment|//        Query query = getEntityManager().createQuery(
comment|//                "select a FROM SingleTableInheritanceSuper1 a ORDER BY a.propertyA");
comment|//        List<?> results = query.getResultList();
comment|//        assertEquals(4, results.size());
comment|//
comment|//        assertEquals(SingleTableInheritanceSuper1.class.getName(), results
comment|//                .get(0)
comment|//                .getClass()
comment|//                .getName());
comment|//        assertEquals(SingleTableInheritanceSuper1.class.getName(), results
comment|//                .get(1)
comment|//                .getClass()
comment|//                .getName());
comment|//        assertEquals(SingleTableInheritanceSub1.class.getName(), results
comment|//                .get(2)
comment|//                .getClass()
comment|//                .getName());
comment|//        assertEquals(SingleTableInheritanceSub2.class.getName(), results
comment|//                .get(3)
comment|//                .getClass()
comment|//                .getName());
block|}
specifier|public
name|void
name|testSelectSub
parameter_list|()
throws|throws
name|Exception
block|{
name|ItestTableUtils
name|helper
init|=
name|getTableHelper
argument_list|(
literal|"ST_INHERITANCE"
argument_list|)
decl_stmt|;
name|helper
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|helper
operator|.
name|setColumns
argument_list|(
literal|"id"
argument_list|,
literal|"objectType"
argument_list|,
literal|"propertyA"
argument_list|,
literal|"propertyB"
argument_list|,
literal|"propertyC"
argument_list|)
expr_stmt|;
name|helper
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"A"
argument_list|,
literal|"1"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|helper
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"A"
argument_list|,
literal|"2"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|helper
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"B"
argument_list|,
literal|"3"
argument_list|,
literal|"BX"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|helper
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|"C"
argument_list|,
literal|"4"
argument_list|,
literal|null
argument_list|,
literal|"CX"
argument_list|)
expr_stmt|;
comment|//        Query query = getEntityManager().createQuery(
comment|//                "select a FROM SingleTableInheritanceSub1 a ORDER BY a.propertyA");
comment|//        List<?> results = query.getResultList();
comment|//        assertEquals(1, results.size());
comment|//
comment|//        assertEquals(SingleTableInheritanceSub1.class.getName(), results
comment|//                .get(0)
comment|//                .getClass()
comment|//                .getName());
block|}
block|}
end_class

end_unit

