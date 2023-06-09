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
name|map
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
name|reflect
operator|.
name|PersistentDescriptor
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
name|List
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|IntStream
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

begin_class
specifier|public
class|class
name|DefaultEntityResultSegmentTest
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|expectedColumnPath
init|=
name|List
operator|.
name|of
argument_list|(
literal|"key1"
argument_list|,
literal|"key2"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fields
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
name|Map
operator|.
name|of
argument_list|(
literal|"key1"
argument_list|,
literal|"value1"
argument_list|,
literal|"key2"
argument_list|,
literal|"value2"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|DefaultEntityResultSegment
name|resultSegment
init|=
operator|new
name|DefaultEntityResultSegment
argument_list|(
operator|new
name|PersistentDescriptor
argument_list|()
argument_list|,
name|fields
argument_list|,
name|fields
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testGetColumnPath
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|actualColumnPath
init|=
name|fields
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|resultSegment
operator|::
name|getColumnPath
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedColumnPath
operator|.
name|size
argument_list|()
argument_list|,
name|actualColumnPath
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|IntStream
operator|.
name|range
argument_list|(
literal|0
argument_list|,
name|actualColumnPath
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|i
lambda|->
name|assertEquals
argument_list|(
name|expectedColumnPath
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|actualColumnPath
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

