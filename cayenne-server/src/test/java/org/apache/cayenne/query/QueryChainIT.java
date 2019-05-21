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
name|query
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|testdo
operator|.
name|testmap
operator|.
name|Artist
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
name|assertNull
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
name|QueryChainIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testSelectQuery
parameter_list|()
block|{
name|QueryChain
name|chain
init|=
operator|new
name|QueryChain
argument_list|()
decl_stmt|;
name|chain
operator|.
name|addQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|chain
operator|.
name|addQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|QueryMetadata
name|md
init|=
name|chain
operator|.
name|getMetaData
argument_list|(
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|md
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|md
operator|.
name|getObjEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectQueryDataRows
parameter_list|()
block|{
name|QueryChain
name|chain
init|=
operator|new
name|QueryChain
argument_list|()
decl_stmt|;
name|SelectQuery
argument_list|<
name|DataRow
argument_list|>
name|q1
init|=
name|SelectQuery
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|chain
operator|.
name|addQuery
argument_list|(
name|q1
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|DataRow
argument_list|>
name|q2
init|=
name|SelectQuery
operator|.
name|dataRowQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|chain
operator|.
name|addQuery
argument_list|(
name|q2
argument_list|)
expr_stmt|;
name|QueryMetadata
name|md
init|=
name|chain
operator|.
name|getMetaData
argument_list|(
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|md
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|md
operator|.
name|getObjEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

