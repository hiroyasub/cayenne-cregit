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
name|exp
operator|.
name|parser
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
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
name|Arrays
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|ObjectId
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
name|Persistent
import|;
end_import

begin_class
specifier|public
class|class
name|ASTListTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testConstructorWithCollection
parameter_list|()
block|{
name|ObjectId
name|objectId
init|=
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Persistent
name|artist
init|=
name|mock
argument_list|(
name|Persistent
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|artist
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|objectId
argument_list|)
expr_stmt|;
name|ASTList
name|exp
init|=
operator|new
name|ASTList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|artist
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exp
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Persistent
argument_list|>
name|collection
init|=
operator|new
name|ArrayList
argument_list|<
name|Persistent
argument_list|>
argument_list|()
decl_stmt|;
name|collection
operator|.
name|add
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|exp
operator|=
operator|new
name|ASTList
argument_list|(
name|collection
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exp
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

