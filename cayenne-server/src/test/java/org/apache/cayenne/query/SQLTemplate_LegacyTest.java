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
name|assertNotSame
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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

begin_class
specifier|public
class|class
name|SQLTemplate_LegacyTest
block|{
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Deprecated
specifier|public
name|void
name|testQueryWithParameters
parameter_list|()
block|{
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
literal|"E1"
argument_list|,
literal|"SELECT"
argument_list|)
decl_stmt|;
name|Query
name|q2
init|=
name|q1
operator|.
name|queryWithParameters
argument_list|(
name|Collections
operator|.
name|EMPTY_MAP
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|q1
argument_list|,
name|q2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|q2
operator|instanceof
name|SQLTemplate
argument_list|)
expr_stmt|;
name|Query
name|q3
init|=
name|q1
operator|.
name|queryWithParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q3
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|q1
argument_list|,
name|q3
argument_list|)
expr_stmt|;
name|Query
name|q4
init|=
name|q1
operator|.
name|queryWithParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q4
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|q3
argument_list|,
name|q4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
annotation|@
name|Deprecated
specifier|public
name|void
name|testSetParameters_SingleParameterSet
parameter_list|()
throws|throws
name|Exception
block|{
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|params
argument_list|,
name|query
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|query
operator|.
name|parametersIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|params
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameters
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|it
operator|=
name|query
operator|.
name|parametersIterator
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Deprecated
specifier|public
name|void
name|testSetParameters_BatchParameterSet
parameter_list|()
throws|throws
name|Exception
block|{
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params1
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params2
operator|.
name|put
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
operator|new
name|Map
index|[]
block|{
name|params1
block|,
name|params2
block|,
literal|null
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|params1
argument_list|,
name|query
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|query
operator|.
name|parametersIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|params1
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|params2
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|it
operator|.
name|next
argument_list|()
operator|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
operator|(
name|Map
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|it
operator|=
name|query
operator|.
name|parametersIterator
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

