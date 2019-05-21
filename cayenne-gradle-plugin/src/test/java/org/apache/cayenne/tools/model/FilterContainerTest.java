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
name|tools
operator|.
name|model
package|;
end_package

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|Closure
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|Catalog
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|Schema
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
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|FilterContainerTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|includeTableClosure
parameter_list|()
block|{
name|FilterContainer
name|container
init|=
operator|new
name|FilterContainer
argument_list|()
decl_stmt|;
name|container
operator|.
name|includeTable
argument_list|(
operator|new
name|Closure
argument_list|<
name|IncludeTable
argument_list|>
argument_list|(
name|container
argument_list|,
name|container
argument_list|)
block|{
specifier|public
name|IncludeTable
name|doCall
parameter_list|(
name|IncludeTable
name|arg
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|arg
argument_list|)
expr_stmt|;
name|arg
operator|.
name|name
argument_list|(
literal|"table_from_closure"
argument_list|)
expr_stmt|;
return|return
name|arg
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Schema
name|schema
init|=
operator|new
name|Schema
argument_list|()
decl_stmt|;
name|container
operator|.
name|fillContainer
argument_list|(
name|schema
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|schema
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"table_from_closure"
argument_list|,
name|schema
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|includeTableNameAndClosure
parameter_list|()
block|{
name|FilterContainer
name|container
init|=
operator|new
name|FilterContainer
argument_list|()
decl_stmt|;
name|container
operator|.
name|includeTable
argument_list|(
literal|"start_name"
argument_list|,
operator|new
name|Closure
argument_list|<
name|IncludeTable
argument_list|>
argument_list|(
name|container
argument_list|,
name|container
argument_list|)
block|{
specifier|public
name|IncludeTable
name|doCall
parameter_list|(
name|IncludeTable
name|arg
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|arg
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"start_name"
argument_list|,
name|arg
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|arg
operator|.
name|name
argument_list|(
literal|"table_from_closure"
argument_list|)
expr_stmt|;
return|return
name|arg
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Schema
name|schema
init|=
operator|new
name|Schema
argument_list|()
decl_stmt|;
name|container
operator|.
name|fillContainer
argument_list|(
name|schema
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|schema
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"table_from_closure"
argument_list|,
name|schema
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|fillContainer
parameter_list|()
throws|throws
name|Exception
block|{
name|Catalog
name|catalog
init|=
operator|new
name|Catalog
argument_list|()
decl_stmt|;
name|FilterContainer
name|container
init|=
operator|new
name|FilterContainer
argument_list|()
decl_stmt|;
name|container
operator|.
name|setName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|container
operator|.
name|includeTable
argument_list|(
literal|"table1"
argument_list|)
expr_stmt|;
name|container
operator|.
name|includeTables
argument_list|(
literal|"table2"
argument_list|,
literal|"table3"
argument_list|)
expr_stmt|;
name|container
operator|.
name|excludeTable
argument_list|(
literal|"table4"
argument_list|)
expr_stmt|;
name|container
operator|.
name|excludeTables
argument_list|(
literal|"table5"
argument_list|,
literal|"table6"
argument_list|)
expr_stmt|;
name|container
operator|.
name|includeColumn
argument_list|(
literal|"column1"
argument_list|)
expr_stmt|;
name|container
operator|.
name|includeColumns
argument_list|(
literal|"column2"
argument_list|,
literal|"column3"
argument_list|)
expr_stmt|;
name|container
operator|.
name|excludeColumn
argument_list|(
literal|"column4"
argument_list|)
expr_stmt|;
name|container
operator|.
name|excludeColumns
argument_list|(
literal|"column5"
argument_list|,
literal|"collum6"
argument_list|)
expr_stmt|;
name|container
operator|.
name|includeProcedure
argument_list|(
literal|"proc1"
argument_list|)
expr_stmt|;
name|container
operator|.
name|includeProcedures
argument_list|(
literal|"proc2"
argument_list|,
literal|"proc3"
argument_list|)
expr_stmt|;
name|container
operator|.
name|excludeProcedure
argument_list|(
literal|"proc4"
argument_list|)
expr_stmt|;
name|container
operator|.
name|excludeProcedures
argument_list|(
literal|"proc5"
argument_list|,
literal|"proc6"
argument_list|)
expr_stmt|;
name|container
operator|.
name|fillContainer
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"name"
argument_list|,
name|catalog
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|catalog
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"table1"
argument_list|,
name|catalog
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|catalog
operator|.
name|getExcludeTables
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"table4"
argument_list|,
name|catalog
operator|.
name|getExcludeTables
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|catalog
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"column1"
argument_list|,
name|catalog
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|catalog
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"column4"
argument_list|,
name|catalog
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|catalog
operator|.
name|getIncludeProcedures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"proc1"
argument_list|,
name|catalog
operator|.
name|getIncludeProcedures
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|catalog
operator|.
name|getExcludeProcedures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"proc4"
argument_list|,
name|catalog
operator|.
name|getExcludeProcedures
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

