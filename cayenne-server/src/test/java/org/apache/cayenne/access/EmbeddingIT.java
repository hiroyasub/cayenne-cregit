begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|EmbedChild
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
name|EmbedEntity2
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
name|EmbedRoot
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
specifier|protected
name|TableHelper
name|tEmbedEntity2
decl_stmt|;
specifier|protected
name|TableHelper
name|tEmbedRoot
decl_stmt|;
specifier|protected
name|TableHelper
name|tEmbedChild
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
name|tEmbedEntity2
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"EMBED_ENTITY2"
argument_list|)
expr_stmt|;
name|tEmbedEntity2
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"NAME"
argument_list|,
literal|"ENTITY1_ID"
argument_list|,
literal|"EMBEDDED10"
argument_list|,
literal|"EMBEDDED20"
argument_list|)
expr_stmt|;
name|tEmbedRoot
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"EMBED_ROOT"
argument_list|)
expr_stmt|;
name|tEmbedRoot
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
literal|"TYPE"
argument_list|)
expr_stmt|;
name|tEmbedChild
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"EMBED_CHILD"
argument_list|)
expr_stmt|;
name|tEmbedChild
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"CHILD_ATTR"
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
name|createSelectDataSet2
parameter_list|()
throws|throws
name|Exception
block|{
name|createSelectDataSet
argument_list|()
expr_stmt|;
name|tEmbedEntity2
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"n2-1"
argument_list|,
literal|1
argument_list|,
literal|"e1"
argument_list|,
literal|"e2"
argument_list|)
expr_stmt|;
name|tEmbedEntity2
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"n2-1"
argument_list|,
literal|2
argument_list|,
literal|"e1"
argument_list|,
literal|"e2"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createSelectDataSetInheritance
parameter_list|()
throws|throws
name|Exception
block|{
name|tEmbedRoot
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"root1"
argument_list|,
literal|"e1-1"
argument_list|,
literal|"e2-1"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|tEmbedRoot
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"root2"
argument_list|,
literal|"e1-2"
argument_list|,
literal|"e2-2"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tEmbedChild
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"child-attr1"
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
name|List
argument_list|<
name|EmbedEntity1
argument_list|>
name|results
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
name|orderBy
argument_list|(
name|EmbedEntity1
operator|.
name|NAME
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
name|EMBEDDED1
operator|.
name|dot
argument_list|(
name|Embeddable1
operator|.
name|EMBEDDED10
argument_list|)
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
name|EMBEDDED1
operator|.
name|dot
argument_list|(
name|Embeddable1
operator|.
name|EMBEDDED10
argument_list|)
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
name|DataRow
name|row
init|=
name|ObjectSelect
operator|.
name|dataRowQuery
argument_list|(
name|EmbedEntity1
operator|.
name|class
argument_list|)
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
name|List
argument_list|<
name|EmbedEntity1
argument_list|>
name|results
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
name|orderBy
argument_list|(
name|EmbedEntity1
operator|.
name|NAME
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
name|EmbedEntity1
name|o1
init|=
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
name|DataRow
name|row
init|=
name|ObjectSelect
operator|.
name|dataRowQuery
argument_list|(
name|EmbedEntity1
operator|.
name|class
argument_list|)
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
name|List
argument_list|<
name|EmbedEntity1
argument_list|>
name|results
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
name|orderBy
argument_list|(
name|EmbedEntity1
operator|.
name|NAME
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
name|EmbedEntity1
name|o1
init|=
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
name|DataRow
name|row
init|=
name|ObjectSelect
operator|.
name|dataRowQuery
argument_list|(
name|EmbedEntity1
operator|.
name|class
argument_list|)
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
annotation|@
name|Test
specifier|public
name|void
name|testPropertyExpression
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
name|EMBEDDED1
operator|.
name|dot
argument_list|(
name|Embeddable1
operator|.
name|EMBEDDED10
argument_list|)
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
name|EMBEDDED2
operator|.
name|dot
argument_list|(
name|Embeddable1
operator|.
name|EMBEDDED20
argument_list|)
operator|.
name|desc
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRelatedEmbedded
parameter_list|()
throws|throws
name|Exception
block|{
name|createSelectDataSet2
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|EmbedEntity2
argument_list|>
name|result
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|EmbedEntity2
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|EmbedEntity2
operator|.
name|ENTITY1
operator|.
name|dot
argument_list|(
name|EmbedEntity1
operator|.
name|EMBEDDED1
argument_list|)
operator|.
name|dot
argument_list|(
name|Embeddable1
operator|.
name|EMBEDDED10
argument_list|)
operator|.
name|eq
argument_list|(
literal|"e1"
argument_list|)
argument_list|)
operator|.
name|orderBy
argument_list|(
name|EmbedEntity2
operator|.
name|ENTITY1
operator|.
name|dot
argument_list|(
name|EmbedEntity1
operator|.
name|EMBEDDED2
argument_list|)
operator|.
name|dot
argument_list|(
name|Embeddable1
operator|.
name|EMBEDDED20
argument_list|)
operator|.
name|desc
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrefetchWithEmbedded
parameter_list|()
throws|throws
name|Exception
block|{
name|createSelectDataSet2
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|EmbedEntity2
argument_list|>
name|result
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|EmbedEntity2
operator|.
name|class
argument_list|)
operator|.
name|prefetch
argument_list|(
name|EmbedEntity2
operator|.
name|ENTITY1
operator|.
name|joint
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
literal|2
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEntity1
argument_list|()
operator|.
name|getEmbedded1
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getEntity1
argument_list|()
operator|.
name|getEmbedded1
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInMemoryFilteringByEmbeddable
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
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|EmbedEntity1
argument_list|>
name|filtered
init|=
name|EmbedEntity1
operator|.
name|EMBEDDED1
operator|.
name|dot
argument_list|(
name|Embeddable1
operator|.
name|EMBEDDED10
argument_list|)
operator|.
name|eq
argument_list|(
literal|"e1"
argument_list|)
operator|.
name|filterObjects
argument_list|(
name|result
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|filtered
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"n1"
argument_list|,
name|filtered
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testColumnSelect
parameter_list|()
throws|throws
name|Exception
block|{
name|createSelectDataSet2
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Embeddable1
argument_list|>
name|result
init|=
name|ObjectSelect
operator|.
name|columnQuery
argument_list|(
name|EmbedEntity1
operator|.
name|class
argument_list|,
name|EmbedEntity1
operator|.
name|EMBEDDED2
argument_list|)
operator|.
name|orderBy
argument_list|(
name|EmbedEntity1
operator|.
name|EMBEDDED2
operator|.
name|dot
argument_list|(
name|Embeddable1
operator|.
name|EMBEDDED10
argument_list|)
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
literal|2
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e3"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEmbedded10
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e4"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEmbedded20
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ex3"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getEmbedded10
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ex4"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getEmbedded20
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|setEmbedded10
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testColumnSelectMultiple
parameter_list|()
throws|throws
name|Exception
block|{
name|createSelectDataSet2
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|result
init|=
name|ObjectSelect
operator|.
name|columnQuery
argument_list|(
name|EmbedEntity1
operator|.
name|class
argument_list|,
name|EmbedEntity1
operator|.
name|EMBEDDED1
argument_list|,
name|EmbedEntity1
operator|.
name|EMBEDDED2
argument_list|)
operator|.
name|orderBy
argument_list|(
name|EmbedEntity1
operator|.
name|EMBEDDED2
operator|.
name|dot
argument_list|(
name|Embeddable1
operator|.
name|EMBEDDED10
argument_list|)
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
literal|2
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e3"
argument_list|,
operator|(
operator|(
name|Embeddable1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|1
index|]
operator|)
operator|.
name|getEmbedded10
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e4"
argument_list|,
operator|(
operator|(
name|Embeddable1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|1
index|]
operator|)
operator|.
name|getEmbedded20
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ex3"
argument_list|,
operator|(
operator|(
name|Embeddable1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
index|[
literal|1
index|]
operator|)
operator|.
name|getEmbedded10
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ex4"
argument_list|,
operator|(
operator|(
name|Embeddable1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
index|[
literal|1
index|]
operator|)
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
name|testColumnSelectMixed
parameter_list|()
throws|throws
name|Exception
block|{
name|createSelectDataSet2
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|result
init|=
name|ObjectSelect
operator|.
name|columnQuery
argument_list|(
name|EmbedEntity1
operator|.
name|class
argument_list|,
name|EmbedEntity1
operator|.
name|EMBEDDED1
operator|.
name|dot
argument_list|(
name|Embeddable1
operator|.
name|EMBEDDED10
argument_list|)
argument_list|,
name|EmbedEntity1
operator|.
name|EMBEDDED2
argument_list|)
operator|.
name|orderBy
argument_list|(
name|EmbedEntity1
operator|.
name|EMBEDDED2
operator|.
name|dot
argument_list|(
name|Embeddable1
operator|.
name|EMBEDDED10
argument_list|)
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
literal|2
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e3"
argument_list|,
operator|(
operator|(
name|Embeddable1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|1
index|]
operator|)
operator|.
name|getEmbedded10
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e4"
argument_list|,
operator|(
operator|(
name|Embeddable1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|1
index|]
operator|)
operator|.
name|getEmbedded20
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ex3"
argument_list|,
operator|(
operator|(
name|Embeddable1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
index|[
literal|1
index|]
operator|)
operator|.
name|getEmbedded10
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ex4"
argument_list|,
operator|(
operator|(
name|Embeddable1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
index|[
literal|1
index|]
operator|)
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
name|testWhere
parameter_list|()
throws|throws
name|Exception
block|{
name|createSelectDataSet2
argument_list|()
expr_stmt|;
name|Embeddable1
name|embeddable1
init|=
operator|new
name|Embeddable1
argument_list|()
decl_stmt|;
name|embeddable1
operator|.
name|setEmbedded10
argument_list|(
literal|"e1"
argument_list|)
expr_stmt|;
name|embeddable1
operator|.
name|setEmbedded20
argument_list|(
literal|"e2"
argument_list|)
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
name|EMBEDDED1
operator|.
name|eq
argument_list|(
name|embeddable1
argument_list|)
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectWithInheritance
parameter_list|()
throws|throws
name|Exception
block|{
name|createSelectDataSetInheritance
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|EmbedRoot
argument_list|>
name|roots
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|EmbedRoot
operator|.
name|class
argument_list|)
operator|.
name|orderBy
argument_list|(
name|EmbedRoot
operator|.
name|NAME
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
literal|2
argument_list|,
name|roots
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|EmbedRoot
name|root
init|=
name|roots
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|EmbedRoot
name|child
init|=
name|roots
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|child
operator|instanceof
name|EmbedChild
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e1-1"
argument_list|,
name|root
operator|.
name|getEmbedded
argument_list|()
operator|.
name|getEmbedded10
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e2-1"
argument_list|,
name|root
operator|.
name|getEmbedded
argument_list|()
operator|.
name|getEmbedded20
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e1-2"
argument_list|,
name|child
operator|.
name|getEmbedded
argument_list|()
operator|.
name|getEmbedded10
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e2-2"
argument_list|,
name|child
operator|.
name|getEmbedded
argument_list|()
operator|.
name|getEmbedded20
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"child-attr1"
argument_list|,
operator|(
operator|(
name|EmbedChild
operator|)
name|child
operator|)
operator|.
name|getChildAttr
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInsertWithInheritance
parameter_list|()
block|{
block|{
name|EmbedRoot
name|root
init|=
name|context
operator|.
name|newObject
argument_list|(
name|EmbedRoot
operator|.
name|class
argument_list|)
decl_stmt|;
name|root
operator|.
name|setName
argument_list|(
literal|"root"
argument_list|)
expr_stmt|;
name|Embeddable1
name|embeddable1
init|=
operator|new
name|Embeddable1
argument_list|()
decl_stmt|;
name|embeddable1
operator|.
name|setEmbedded10
argument_list|(
literal|"root-10"
argument_list|)
expr_stmt|;
name|embeddable1
operator|.
name|setEmbedded20
argument_list|(
literal|"root-20"
argument_list|)
expr_stmt|;
name|root
operator|.
name|setEmbedded
argument_list|(
name|embeddable1
argument_list|)
expr_stmt|;
block|}
block|{
name|EmbedChild
name|child
init|=
name|context
operator|.
name|newObject
argument_list|(
name|EmbedChild
operator|.
name|class
argument_list|)
decl_stmt|;
name|child
operator|.
name|setName
argument_list|(
literal|"child"
argument_list|)
expr_stmt|;
name|Embeddable1
name|embeddable1
init|=
operator|new
name|Embeddable1
argument_list|()
decl_stmt|;
name|embeddable1
operator|.
name|setEmbedded10
argument_list|(
literal|"child-10"
argument_list|)
expr_stmt|;
name|embeddable1
operator|.
name|setEmbedded20
argument_list|(
literal|"child-20"
argument_list|)
expr_stmt|;
name|child
operator|.
name|setEmbedded
argument_list|(
name|embeddable1
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

