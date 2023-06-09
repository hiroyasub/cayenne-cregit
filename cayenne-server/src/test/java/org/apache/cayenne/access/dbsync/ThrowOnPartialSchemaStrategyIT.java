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
name|dbsync
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
name|CayenneRuntimeException
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
name|MockOperationObserver
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
name|Query
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
name|Collections
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
name|fail
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|SUS_PROJECT
argument_list|)
specifier|public
class|class
name|ThrowOnPartialSchemaStrategyIT
extends|extends
name|SchemaUpdateStrategyBase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testThrowOnPartialStrategyTableNoExist
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|template
init|=
literal|"SELECT #result('ARTIST_ID' 'int') FROM ARTIST ORDER BY ARTIST_ID"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|setStrategy
argument_list|(
name|ThrowOnPartialSchemaStrategy
operator|.
name|class
argument_list|)
expr_stmt|;
try|try
block|{
name|node
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|node
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThrowOnPartialStrategyTableExist
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|template
init|=
literal|"SELECT #result('ARTIST_ID' 'int') FROM ARTIST ORDER BY ARTIST_ID"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|createOneTable
argument_list|(
literal|"SUS1"
argument_list|)
expr_stmt|;
name|createOneTable
argument_list|(
literal|"SUS2"
argument_list|)
expr_stmt|;
name|setStrategy
argument_list|(
name|ThrowOnPartialSchemaStrategy
operator|.
name|class
argument_list|)
expr_stmt|;
name|node
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThrowOnPartialStrategyWithOneTable
parameter_list|()
throws|throws
name|Exception
block|{
name|createOneTable
argument_list|(
literal|"SUS1"
argument_list|)
expr_stmt|;
name|setStrategy
argument_list|(
name|ThrowOnPartialSchemaStrategy
operator|.
name|class
argument_list|)
expr_stmt|;
name|String
name|template
init|=
literal|"SELECT #result('ARTIST_ID' 'int') FROM ARTIST ORDER BY ARTIST_ID"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
try|try
block|{
name|node
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|existingTables
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Must have thrown on partial schema"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
end_class

end_unit

