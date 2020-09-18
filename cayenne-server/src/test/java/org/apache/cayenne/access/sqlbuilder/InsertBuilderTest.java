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
operator|.
name|sqlbuilder
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|InsertNode
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|Node
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
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|sqlbuilder
operator|.
name|SQLBuilder
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|instanceOf
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
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|InsertBuilderTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testInsert
parameter_list|()
block|{
name|InsertBuilder
name|builder
init|=
operator|new
name|InsertBuilder
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|Node
name|node
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|node
argument_list|,
name|instanceOf
argument_list|(
name|InsertNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSQL
argument_list|(
literal|"INSERT INTO test"
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInsertWithColumns
parameter_list|()
block|{
name|InsertBuilder
name|builder
init|=
operator|new
name|InsertBuilder
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|column
argument_list|(
name|column
argument_list|(
literal|"col1"
argument_list|)
argument_list|)
operator|.
name|column
argument_list|(
name|column
argument_list|(
literal|"col2"
argument_list|)
argument_list|)
operator|.
name|column
argument_list|(
name|column
argument_list|(
literal|"col3"
argument_list|)
argument_list|)
expr_stmt|;
name|Node
name|node
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|node
argument_list|,
name|instanceOf
argument_list|(
name|InsertNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSQL
argument_list|(
literal|"INSERT INTO test( col1, col2, col3)"
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInsertWithValues
parameter_list|()
block|{
name|InsertBuilder
name|builder
init|=
operator|new
name|InsertBuilder
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|value
argument_list|(
name|value
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|value
argument_list|(
name|value
argument_list|(
literal|"test"
argument_list|)
argument_list|)
operator|.
name|value
argument_list|(
name|value
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|Node
name|node
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|node
argument_list|,
name|instanceOf
argument_list|(
name|InsertNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSQL
argument_list|(
literal|"INSERT INTO test VALUES( 1, 'test', NULL)"
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInsertWithColumnsAndValues
parameter_list|()
block|{
name|InsertBuilder
name|builder
init|=
operator|new
name|InsertBuilder
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|column
argument_list|(
name|column
argument_list|(
literal|"col1"
argument_list|)
argument_list|)
operator|.
name|value
argument_list|(
name|value
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|column
argument_list|(
name|column
argument_list|(
literal|"col2"
argument_list|)
argument_list|)
operator|.
name|value
argument_list|(
name|value
argument_list|(
literal|"test"
argument_list|)
argument_list|)
operator|.
name|column
argument_list|(
name|column
argument_list|(
literal|"col3"
argument_list|)
argument_list|)
operator|.
name|value
argument_list|(
name|value
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|Node
name|node
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|node
argument_list|,
name|instanceOf
argument_list|(
name|InsertNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSQL
argument_list|(
literal|"INSERT INTO test( col1, col2, col3) VALUES( 1, 'test', NULL)"
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertSQL
parameter_list|(
name|String
name|expected
parameter_list|,
name|Node
name|node
parameter_list|)
block|{
name|SQLGenerationVisitor
name|visitor
init|=
operator|new
name|SQLGenerationVisitor
argument_list|(
operator|new
name|StringBuilderAppendable
argument_list|()
argument_list|)
decl_stmt|;
name|node
operator|.
name|visit
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|visitor
operator|.
name|getSQLString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

