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

begin_class
specifier|public
class|class
name|IndexPropertyListTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSort
parameter_list|()
block|{
name|IndexedObject
name|o1
init|=
operator|new
name|IndexedObject
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|IndexedObject
name|o2
init|=
operator|new
name|IndexedObject
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|IndexedObject
name|o3
init|=
operator|new
name|IndexedObject
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|IndexedObject
name|o4
init|=
operator|new
name|IndexedObject
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|List
name|list1
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|o2
block|,
name|o4
block|,
name|o3
block|,
name|o1
block|}
argument_list|)
decl_stmt|;
name|IndexPropertyList
name|indexedList
init|=
operator|new
name|IndexPropertyList
argument_list|(
literal|"order"
argument_list|,
name|list1
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// sort should be done implictly on get...
name|assertEquals
argument_list|(
name|o1
argument_list|,
name|indexedList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o2
argument_list|,
name|indexedList
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o3
argument_list|,
name|indexedList
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o4
argument_list|,
name|indexedList
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|list2
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|o2
block|,
name|o4
block|,
name|o3
block|,
name|o1
block|}
argument_list|)
decl_stmt|;
name|IndexPropertyList
name|indexedUnsortedList
init|=
operator|new
name|IndexPropertyList
argument_list|(
literal|"order"
argument_list|,
name|list2
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// sort should be done implictly on get...
name|assertEquals
argument_list|(
name|o2
argument_list|,
name|indexedUnsortedList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o4
argument_list|,
name|indexedUnsortedList
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o3
argument_list|,
name|indexedUnsortedList
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o1
argument_list|,
name|indexedUnsortedList
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAppend
parameter_list|()
block|{
name|IndexedObject
name|o1
init|=
operator|new
name|IndexedObject
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|IndexedObject
name|o2
init|=
operator|new
name|IndexedObject
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|IndexedObject
name|o3
init|=
operator|new
name|IndexedObject
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|IndexedObject
name|o4
init|=
operator|new
name|IndexedObject
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|List
name|list1
init|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|o2
block|,
name|o4
block|,
name|o3
block|,
name|o1
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|IndexPropertyList
name|indexedList
init|=
operator|new
name|IndexPropertyList
argument_list|(
literal|"order"
argument_list|,
name|list1
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|IndexedObject
name|o5
init|=
operator|new
name|IndexedObject
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
name|indexedList
operator|.
name|add
argument_list|(
name|o5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|o4
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o4
operator|.
name|getOrder
argument_list|()
operator|<
name|o5
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInsert
parameter_list|()
block|{
name|IndexedObject
name|o1
init|=
operator|new
name|IndexedObject
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|IndexedObject
name|o2
init|=
operator|new
name|IndexedObject
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|IndexedObject
name|o3
init|=
operator|new
name|IndexedObject
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|IndexedObject
name|o4
init|=
operator|new
name|IndexedObject
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|List
name|list1
init|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|o2
block|,
name|o4
block|,
name|o3
block|,
name|o1
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|IndexPropertyList
name|indexedList
init|=
operator|new
name|IndexPropertyList
argument_list|(
literal|"order"
argument_list|,
name|list1
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|IndexedObject
name|o5
init|=
operator|new
name|IndexedObject
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
name|indexedList
operator|.
name|add
argument_list|(
literal|1
argument_list|,
name|o5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o1
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o1
operator|.
name|getOrder
argument_list|()
operator|<
name|o5
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o5
operator|.
name|getOrder
argument_list|()
operator|<
name|o2
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o2
operator|.
name|getOrder
argument_list|()
operator|<
name|o3
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o3
operator|.
name|getOrder
argument_list|()
operator|<
name|o4
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

