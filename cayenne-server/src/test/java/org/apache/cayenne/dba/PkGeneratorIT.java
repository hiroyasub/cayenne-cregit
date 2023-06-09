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
name|dba
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
name|DataNode
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
name|DbEntity
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
name|UnitDbAdapter
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
name|ArrayList
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
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|PkGeneratorIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|accessStackAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataNode
name|node
decl_stmt|;
specifier|private
name|PkGenerator
name|pkGenerator
decl_stmt|;
specifier|private
name|DbEntity
name|paintingEntity
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
name|pkGenerator
operator|=
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getPkGenerator
argument_list|()
expr_stmt|;
name|paintingEntity
operator|=
name|node
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DbEntity
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|DbEntity
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|paintingEntity
argument_list|)
expr_stmt|;
name|pkGenerator
operator|.
name|createAutoPk
argument_list|(
name|node
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|pkGenerator
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGeneratePkForDbEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|pkList
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|testSize
init|=
operator|(
name|pkGenerator
operator|instanceof
name|JdbcPkGenerator
operator|)
condition|?
operator|(
operator|(
name|JdbcPkGenerator
operator|)
name|pkGenerator
operator|)
operator|.
name|getPkCacheSize
argument_list|()
operator|*
literal|2
else|:
literal|25
decl_stmt|;
if|if
condition|(
name|testSize
operator|<
literal|25
condition|)
block|{
name|testSize
operator|=
literal|25
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|testSize
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|pk
init|=
name|pkGenerator
operator|.
name|generatePk
argument_list|(
name|node
argument_list|,
name|paintingEntity
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pk
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pk
operator|instanceof
name|Number
argument_list|)
expr_stmt|;
comment|// check that the number is continuous
comment|// of course this assumes a single-threaded test
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsBatchPK
argument_list|()
operator|&&
name|pkList
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Number
name|last
init|=
operator|(
name|Number
operator|)
name|pkList
operator|.
name|get
argument_list|(
name|pkList
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|last
operator|.
name|intValue
argument_list|()
operator|+
literal|1
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|pk
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|pkList
operator|.
name|add
argument_list|(
name|pk
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

