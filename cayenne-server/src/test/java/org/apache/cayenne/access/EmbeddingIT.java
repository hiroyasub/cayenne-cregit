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
name|PersistenceState
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionFactory
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
name|ObjectSelect
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|test
operator|.
name|jdbc
operator|.
name|TableHelper
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
name|Before
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
name|java
operator|.
name|util
operator|.
name|List
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|EMBEDDABLE_PROJECT
argument_list|)
specifier|public
class|class
name|EmbeddingIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|tEmbedEntity1
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|tEmbedEntity1
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"EMBED_ENTITY1"
argument_list|)
expr_stmt|;
name|tEmbedEntity1
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"NAME"
argument_list|,
literal|"EMBEDDED10"
argument_list|,
literal|"EMBEDDED20"
argument_list|,
literal|"EMBEDDED30"
argument_list|,
literal|"EMBEDDED40"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createSelectDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tEmbedEntity1
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tEmbedEntity1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"n1"
argument_list|,
literal|"e1"
argument_list|,
literal|"e2"
argument_list|,
literal|"e3"
argument_list|,
literal|"e4"
argument_list|)
expr_stmt|;
name|tEmbedEntity1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"n2"
argument_list|,
literal|"ex1"
argument_list|,
literal|"ex2"
argument_list|,
literal|"ex3"
argument_list|,
literal|"ex4"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createUpdateDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tEmbedEntity1
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tEmbedEntity1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"n1"
argument_list|,
literal|"e1"
argument_list|,
literal|"e2"
argument_list|,
literal|"e3"
argument_list|,
literal|"e4"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelect
parameter_list|()
throws|throws
name|Exception
block|{
name|createSelectDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|<>
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
name|NAME
operator|.
name|asc
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
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
annotation|@
name|Test
specifier|public
name|void
name|testEmbeddablePropertiesInWhere
parameter_list|()
throws|throws
name|Exception
block|{
name|createSelectDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|EmbedEntity1
argument_list|>
name|result
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|EmbedEntity1
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|EmbedEntity1
operator|.
name|EMBEDDED1_EMBEDDED10
operator|.
name|eq
argument_list|(
literal|"e1"
argument_list|)
argument_list|)
operator|.
name|orderBy
argument_list|(
name|EmbedEntity1
operator|.
name|EMBEDDED2_EMBEDDED10
operator|.
name|asc
argument_list|()
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e1"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEmbedded1
argument_list|()
operator|.
name|getEmbedded10
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInsert
parameter_list|()
throws|throws
name|Exception
block|{
name|EmbedEntity1
name|o1
init|=
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
comment|// init after it was set on the owning object
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
argument_list|<
name|DataRow
argument_list|>
name|query
init|=
name|SelectQuery
operator|.
name|dataRowQuery
argument_list|(
name|EmbedEntity1
operator|.
name|class
argument_list|)
decl_stmt|;
name|DataRow
name|row
init|=
name|query
operator|.
name|selectOne
argument_list|(
name|context
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
annotation|@
name|Test
specifier|public
name|void
name|testUpdateEmbeddedProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|createUpdateDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|<>
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
name|NAME
operator|.
name|asc
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
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
name|Embeddable1
name|e11
init|=
name|o1
operator|.
name|getEmbedded1
argument_list|()
decl_stmt|;
name|e11
operator|.
name|setEmbedded10
argument_list|(
literal|"x1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|o1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|DataRow
argument_list|>
name|query1
init|=
name|SelectQuery
operator|.
name|dataRowQuery
argument_list|(
name|EmbedEntity1
operator|.
name|class
argument_list|)
decl_stmt|;
name|DataRow
name|row
init|=
operator|(
name|DataRow
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query1
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x1"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"EMBEDDED10"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUpdateEmbedded
parameter_list|()
throws|throws
name|Exception
block|{
name|createUpdateDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|<>
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
name|NAME
operator|.
name|asc
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
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
name|Embeddable1
name|e11
init|=
operator|new
name|Embeddable1
argument_list|()
decl_stmt|;
name|e11
operator|.
name|setEmbedded10
argument_list|(
literal|"x1"
argument_list|)
expr_stmt|;
name|e11
operator|.
name|setEmbedded20
argument_list|(
literal|"x2"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setEmbedded1
argument_list|(
name|e11
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|o1
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SelectQuery
argument_list|<
name|DataRow
argument_list|>
name|query1
init|=
name|SelectQuery
operator|.
name|dataRowQuery
argument_list|(
name|EmbedEntity1
operator|.
name|class
argument_list|)
decl_stmt|;
name|DataRow
name|row
init|=
name|query1
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x1"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"EMBEDDED10"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

