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
name|access
package|;
end_package

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
name|QueryResponse
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
name|QueryChain
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
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|DataContextQueryChainTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testSelectQuery
parameter_list|()
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
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
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|QueryResponse
name|r
init|=
name|context
operator|.
name|performGenericQuery
argument_list|(
name|chain
argument_list|)
decl_stmt|;
comment|// data comes back as datarows
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
name|r
operator|.
name|reset
argument_list|()
expr_stmt|;
name|r
operator|.
name|next
argument_list|()
expr_stmt|;
name|List
name|l1
init|=
name|r
operator|.
name|currentList
argument_list|()
decl_stmt|;
name|r
operator|.
name|next
argument_list|()
expr_stmt|;
name|List
name|l2
init|=
name|r
operator|.
name|currentList
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|l1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|DataRow
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|l2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|DataRow
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

