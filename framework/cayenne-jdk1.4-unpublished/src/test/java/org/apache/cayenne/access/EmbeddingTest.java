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
name|java
operator|.
name|util
operator|.
name|List
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
name|DataObjectUtils
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
name|DataRow
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
name|embeddable
operator|.
name|EmbedEntity1
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
name|embeddable
operator|.
name|Embeddable1
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
name|AccessStack
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
name|CayenneCase
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
name|CayenneResources
import|;
end_import

begin_class
specifier|public
class|class
name|EmbeddingTest
extends|extends
name|CayenneCase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|EMBEDDING_ACCESS_STACK
init|=
literal|"EmbeddingStack"
decl_stmt|;
specifier|protected
name|AccessStack
name|buildAccessStack
parameter_list|()
block|{
return|return
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getAccessStack
argument_list|(
name|EMBEDDING_ACCESS_STACK
argument_list|)
return|;
block|}
specifier|public
name|void
name|testSelect
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testSelect"
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|EmbedEntity1
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addOrdering
argument_list|(
name|EmbedEntity1
operator|.
name|NAME_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|List
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|EmbedEntity1
name|o1
init|=
operator|(
name|EmbedEntity1
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"n1"
argument_list|,
name|o1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Embeddable1
name|e11
init|=
name|o1
operator|.
name|getEmbedded1
argument_list|()
decl_stmt|;
name|Embeddable1
name|e12
init|=
name|o1
operator|.
name|getEmbedded2
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|e11
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e12
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e1"
argument_list|,
name|e11
operator|.
name|getEmbedded10
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e2"
argument_list|,
name|e11
operator|.
name|getEmbedded20
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e3"
argument_list|,
name|e12
operator|.
name|getEmbedded10
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e4"
argument_list|,
name|e12
operator|.
name|getEmbedded20
argument_list|()
argument_list|)
expr_stmt|;
name|EmbedEntity1
name|o2
init|=
operator|(
name|EmbedEntity1
operator|)
name|results
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"n2"
argument_list|,
name|o2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Embeddable1
name|e21
init|=
name|o2
operator|.
name|getEmbedded1
argument_list|()
decl_stmt|;
name|Embeddable1
name|e22
init|=
name|o2
operator|.
name|getEmbedded2
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|e21
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e22
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ex1"
argument_list|,
name|e21
operator|.
name|getEmbedded10
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ex2"
argument_list|,
name|e21
operator|.
name|getEmbedded20
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ex3"
argument_list|,
name|e22
operator|.
name|getEmbedded10
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ex4"
argument_list|,
name|e22
operator|.
name|getEmbedded20
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInsert
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|EmbedEntity1
name|o1
init|=
operator|(
name|EmbedEntity1
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|EmbedEntity1
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setName
argument_list|(
literal|"NAME"
argument_list|)
expr_stmt|;
name|Embeddable1
name|e1
init|=
operator|new
name|Embeddable1
argument_list|()
decl_stmt|;
comment|// init before the embeddable was set on an owning object
name|e1
operator|.
name|setEmbedded10
argument_list|(
literal|"E11"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setEmbedded20
argument_list|(
literal|"E12"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setEmbedded1
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|Embeddable1
name|e2
init|=
operator|new
name|Embeddable1
argument_list|()
decl_stmt|;
name|o1
operator|.
name|setEmbedded2
argument_list|(
name|e2
argument_list|)
expr_stmt|;
comment|// init after it was set on the ownig object
name|e2
operator|.
name|setEmbedded10
argument_list|(
literal|"E21"
argument_list|)
expr_stmt|;
name|e2
operator|.
name|setEmbedded20
argument_list|(
literal|"E22"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|EmbedEntity1
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DataRow
name|row
init|=
operator|(
name|DataRow
operator|)
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"E11"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"EMBEDDED10"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"E12"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"EMBEDDED20"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"E21"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"EMBEDDED30"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"E22"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"EMBEDDED40"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//    public void testUpdateEmbeddedProperties() throws Exception {
comment|//        createTestData("testUpdate");
comment|//
comment|//        SelectQuery query = new SelectQuery(EmbedEntity1.class);
comment|//        query.addOrdering(EmbedEntity1.NAME_PROPERTY, true);
comment|//
comment|//        ObjectContext context = createDataContext();
comment|//        List results = context.performQuery(query);
comment|//        EmbedEntity1 o1 = (EmbedEntity1) results.get(0);
comment|//
comment|//        assertEquals("n1", o1.getName());
comment|//        Embeddable1 e11 = o1.getEmbedded1();
comment|//        e11.setEmbedded10("x1");
comment|//
comment|//        assertEquals(PersistenceState.MODIFIED, o1.getPersistenceState());
comment|//
comment|//        context.commitChanges();
comment|//        SelectQuery query1 = new SelectQuery(EmbedEntity1.class);
comment|//        query1.setFetchingDataRows(true);
comment|//        DataRow row = (DataRow) DataObjectUtils.objectForQuery(context, query1);
comment|//        assertNotNull(row);
comment|//        assertEquals("x1", row.get("EMBEDDED10"));
comment|//    }
block|}
end_class

end_unit

