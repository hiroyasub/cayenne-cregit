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
name|SQLTemplate
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
name|assertFalse
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
name|GENERIC_PROJECT
argument_list|)
specifier|public
class|class
name|GenericMappingIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testInsertSingle
parameter_list|()
block|{
name|DataObject
name|g1
init|=
operator|(
name|DataObject
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Generic1"
argument_list|)
decl_stmt|;
name|g1
operator|.
name|writeProperty
argument_list|(
literal|"name"
argument_list|,
literal|"G1 Name"
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
name|testInsertRelated
parameter_list|()
block|{
name|DataObject
name|g1
init|=
operator|(
name|DataObject
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Generic1"
argument_list|)
decl_stmt|;
name|g1
operator|.
name|writeProperty
argument_list|(
literal|"name"
argument_list|,
literal|"G1 Name"
argument_list|)
expr_stmt|;
name|DataObject
name|g2
init|=
operator|(
name|DataObject
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Generic2"
argument_list|)
decl_stmt|;
name|g2
operator|.
name|writeProperty
argument_list|(
literal|"name"
argument_list|,
literal|"G2 Name"
argument_list|)
expr_stmt|;
name|g2
operator|.
name|setToOneTarget
argument_list|(
literal|"toGeneric1"
argument_list|,
name|g1
argument_list|,
literal|true
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
name|testSelect
parameter_list|()
block|{
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
literal|"Generic1"
argument_list|,
literal|"INSERT INTO GENERIC1 (ID, NAME) VALUES (1, 'AAAA')"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
literal|"Generic1"
argument_list|,
literal|"INSERT INTO GENERIC1 (ID, NAME) VALUES (2, 'BBBB')"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
literal|"Generic1"
argument_list|,
literal|"INSERT INTO GENERIC2 (GENERIC1_ID, ID, NAME) VALUES (1, 1, 'CCCCC')"
argument_list|)
argument_list|)
expr_stmt|;
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"name"
argument_list|,
literal|"AAAA"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|result
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Object
operator|.
name|class
argument_list|,
literal|"Generic1"
argument_list|)
operator|.
name|where
argument_list|(
name|qual
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
name|testUpdateRelated
parameter_list|()
block|{
name|DataObject
name|g1
init|=
operator|(
name|DataObject
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Generic1"
argument_list|)
decl_stmt|;
name|g1
operator|.
name|writeProperty
argument_list|(
literal|"name"
argument_list|,
literal|"G1 Name"
argument_list|)
expr_stmt|;
name|DataObject
name|g2
init|=
operator|(
name|DataObject
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Generic2"
argument_list|)
decl_stmt|;
name|g2
operator|.
name|writeProperty
argument_list|(
literal|"name"
argument_list|,
literal|"G2 Name"
argument_list|)
expr_stmt|;
name|g2
operator|.
name|setToOneTarget
argument_list|(
literal|"toGeneric1"
argument_list|,
name|g1
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|r1
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|g1
operator|.
name|readProperty
argument_list|(
literal|"generic2s"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|r1
operator|.
name|contains
argument_list|(
name|g2
argument_list|)
argument_list|)
expr_stmt|;
name|DataObject
name|g11
init|=
operator|(
name|DataObject
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Generic1"
argument_list|)
decl_stmt|;
name|g11
operator|.
name|writeProperty
argument_list|(
literal|"name"
argument_list|,
literal|"G11 Name"
argument_list|)
expr_stmt|;
name|g2
operator|.
name|setToOneTarget
argument_list|(
literal|"toGeneric1"
argument_list|,
name|g11
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|r11
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|g11
operator|.
name|readProperty
argument_list|(
literal|"generic2s"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|r11
operator|.
name|contains
argument_list|(
name|g2
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|r1_1
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|g1
operator|.
name|readProperty
argument_list|(
literal|"generic2s"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|r1_1
operator|.
name|contains
argument_list|(
name|g2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

