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
name|unit
operator|.
name|jira
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
name|access
operator|.
name|DataContext
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
name|assertSame
import|;
end_import

begin_comment
comment|/**  * Testing qualifier translator correctness on reflexive relationships.  */
end_comment

begin_comment
comment|// TODO: this is really a qualifier translator general test... need to
end_comment

begin_comment
comment|// find an appropriate place in unit tests..
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|RELATIONSHIPS_PROJECT
argument_list|)
specifier|public
class|class
name|CAY_194IT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|TableHelper
name|tReflexive
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"REFLEXIVE_AND_TO_ONE"
argument_list|)
decl_stmt|;
name|tReflexive
operator|.
name|setColumns
argument_list|(
literal|"REFLEXIVE_AND_TO_ONE_ID"
argument_list|,
literal|"PARENT_ID"
argument_list|)
expr_stmt|;
name|tReflexive
operator|.
name|update
argument_list|()
operator|.
name|set
argument_list|(
literal|"PARENT_ID"
argument_list|,
literal|null
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"REFLEXIVE_AND_TO_ONE"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testQualifyOnToMany
parameter_list|()
block|{
name|ReflexiveAndToOne
name|ox
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ReflexiveAndToOne
operator|.
name|class
argument_list|)
decl_stmt|;
name|ox
operator|.
name|setName
argument_list|(
literal|"ox"
argument_list|)
expr_stmt|;
name|ReflexiveAndToOne
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ReflexiveAndToOne
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setName
argument_list|(
literal|"o1"
argument_list|)
expr_stmt|;
name|ReflexiveAndToOne
name|o2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ReflexiveAndToOne
operator|.
name|class
argument_list|)
decl_stmt|;
name|o2
operator|.
name|setName
argument_list|(
literal|"o2"
argument_list|)
expr_stmt|;
name|o2
operator|.
name|setToParent
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Expression
name|qualifier
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"children"
argument_list|,
name|o2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|parents
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
argument_list|,
name|qualifier
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parents
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
argument_list|,
name|parents
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|qualifier
operator|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"children"
argument_list|,
name|o1
argument_list|)
expr_stmt|;
name|parents
operator|=
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
argument_list|,
name|qualifier
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|parents
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
name|testQualifyOnToOne
parameter_list|()
block|{
name|ReflexiveAndToOne
name|ox
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ReflexiveAndToOne
operator|.
name|class
argument_list|)
decl_stmt|;
name|ox
operator|.
name|setName
argument_list|(
literal|"ox"
argument_list|)
expr_stmt|;
name|ReflexiveAndToOne
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ReflexiveAndToOne
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setName
argument_list|(
literal|"o1"
argument_list|)
expr_stmt|;
name|ReflexiveAndToOne
name|o2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ReflexiveAndToOne
operator|.
name|class
argument_list|)
decl_stmt|;
name|o2
operator|.
name|setName
argument_list|(
literal|"o2"
argument_list|)
expr_stmt|;
name|o2
operator|.
name|setToParent
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Expression
name|qualifier
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"toParent"
argument_list|,
name|o1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|children
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
argument_list|,
name|qualifier
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|children
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o2
argument_list|,
name|children
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

