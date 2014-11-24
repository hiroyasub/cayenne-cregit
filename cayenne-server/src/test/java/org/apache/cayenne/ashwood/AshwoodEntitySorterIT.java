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
name|ashwood
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
name|map
operator|.
name|ObjEntity
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
name|relationships
operator|.
name|ReflexiveAndToOne
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
name|sql
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_PROJECT
argument_list|)
specifier|public
class|class
name|AshwoodEntitySorterIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
specifier|protected
name|TableHelper
name|tRelationshipHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|tReflexiveAndToOne
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
name|tRelationshipHelper
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"RELATIONSHIP_HELPER"
argument_list|)
expr_stmt|;
name|tRelationshipHelper
operator|.
name|setColumns
argument_list|(
literal|"RELATIONSHIP_HELPER_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tReflexiveAndToOne
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"REFLEXIVE_AND_TO_ONE"
argument_list|)
expr_stmt|;
name|tReflexiveAndToOne
operator|.
name|setColumns
argument_list|(
literal|"REFLEXIVE_AND_TO_ONE_ID"
argument_list|,
literal|"PARENT_ID"
argument_list|,
literal|"RELATIONSHIP_HELPER_ID"
argument_list|,
literal|"NAME"
argument_list|)
operator|.
name|setColumnTypes
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSortObjectsForEntityReflexiveWithFaults
parameter_list|()
throws|throws
name|Exception
block|{
name|tRelationshipHelper
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"rh1"
argument_list|)
expr_stmt|;
name|tReflexiveAndToOne
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|null
argument_list|,
literal|1
argument_list|,
literal|"r1"
argument_list|)
expr_stmt|;
name|tReflexiveAndToOne
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|"r2"
argument_list|)
expr_stmt|;
name|tReflexiveAndToOne
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|2
argument_list|,
literal|1
argument_list|,
literal|"r3"
argument_list|)
expr_stmt|;
name|AshwoodEntitySorter
name|sorter
init|=
operator|new
name|AshwoodEntitySorter
argument_list|()
decl_stmt|;
name|sorter
operator|.
name|setEntityResolver
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|ReflexiveAndToOne
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|ReflexiveAndToOne
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|shuffle
argument_list|(
name|objects
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|sorter
operator|.
name|sortObjectsForEntity
argument_list|(
name|entity
argument_list|,
name|objects
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"r3"
argument_list|,
operator|(
operator|(
name|ReflexiveAndToOne
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"r2"
argument_list|,
operator|(
operator|(
name|ReflexiveAndToOne
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"r1"
argument_list|,
operator|(
operator|(
name|ReflexiveAndToOne
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

