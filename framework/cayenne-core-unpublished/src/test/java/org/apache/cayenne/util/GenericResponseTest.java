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
name|util
package|;
end_package

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
name|HashMap
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
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|EntityResolver
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
name|remote
operator|.
name|hessian
operator|.
name|service
operator|.
name|HessianUtil
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
name|util
operator|.
name|GenericResponse
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
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|GenericResponseTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testCreation
parameter_list|()
throws|throws
name|Exception
block|{
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|HashMap
argument_list|()
argument_list|)
expr_stmt|;
name|GenericResponse
name|r
init|=
operator|new
name|GenericResponse
argument_list|()
decl_stmt|;
name|r
operator|.
name|addBatchUpdateCount
argument_list|(
operator|new
name|int
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|}
argument_list|)
expr_stmt|;
name|r
operator|.
name|addResultList
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|r
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|r
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|r
operator|.
name|isList
argument_list|()
argument_list|)
expr_stmt|;
name|int
index|[]
name|srInt
init|=
name|r
operator|.
name|currentUpdateCount
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|srInt
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|srInt
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|r
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|r
operator|.
name|isList
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
argument_list|,
name|r
operator|.
name|currentList
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|r
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSerialization
parameter_list|()
throws|throws
name|Exception
block|{
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|HashMap
argument_list|()
argument_list|)
expr_stmt|;
name|GenericResponse
name|r
init|=
operator|new
name|GenericResponse
argument_list|()
decl_stmt|;
name|r
operator|.
name|addBatchUpdateCount
argument_list|(
operator|new
name|int
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|}
argument_list|)
expr_stmt|;
name|r
operator|.
name|addResultList
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|GenericResponse
name|sr
init|=
operator|(
name|GenericResponse
operator|)
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|sr
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sr
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sr
operator|.
name|isList
argument_list|()
argument_list|)
expr_stmt|;
name|int
index|[]
name|srInt
init|=
name|sr
operator|.
name|currentUpdateCount
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|srInt
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|srInt
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sr
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sr
operator|.
name|isList
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
argument_list|,
name|sr
operator|.
name|currentList
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sr
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSerializationWithHessian
parameter_list|()
throws|throws
name|Exception
block|{
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|HashMap
argument_list|()
argument_list|)
expr_stmt|;
name|GenericResponse
name|r
init|=
operator|new
name|GenericResponse
argument_list|()
decl_stmt|;
name|r
operator|.
name|addBatchUpdateCount
argument_list|(
operator|new
name|int
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|}
argument_list|)
expr_stmt|;
name|r
operator|.
name|addResultList
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|GenericResponse
name|sr
init|=
operator|(
name|GenericResponse
operator|)
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|r
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|sr
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sr
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sr
operator|.
name|isList
argument_list|()
argument_list|)
expr_stmt|;
name|int
index|[]
name|srInt
init|=
name|sr
operator|.
name|currentUpdateCount
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|srInt
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|srInt
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sr
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sr
operator|.
name|isList
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
argument_list|,
name|sr
operator|.
name|currentList
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sr
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
