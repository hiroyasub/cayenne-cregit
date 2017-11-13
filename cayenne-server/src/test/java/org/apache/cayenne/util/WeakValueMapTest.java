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
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ConcurrentModificationException
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
comment|/**  * As WeakValueMap and SoftValueMap share almost all code from their super class  * only one test is present for both of them.  *  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|WeakValueMapTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testEmptyConstructor
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|WeakValueMap
argument_list|<>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"nonexistent_key1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
literal|42
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"nonexistent_key2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|42
argument_list|)
argument_list|,
name|map
operator|.
name|getOrDefault
argument_list|(
literal|"nonexistent_key2"
argument_list|,
literal|42
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|values
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCapacityConstructor
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|WeakValueMap
argument_list|<>
argument_list|(
literal|42
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"nonexistent_key1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
literal|42
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"nonexistent_key2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|42
argument_list|)
argument_list|,
name|map
operator|.
name|getOrDefault
argument_list|(
literal|"nonexistent_key2"
argument_list|,
literal|42
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|values
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMapConstructor
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|data
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|data
operator|.
name|put
argument_list|(
literal|"key_1"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|data
operator|.
name|put
argument_list|(
literal|"key_2"
argument_list|,
literal|42
argument_list|)
expr_stmt|;
name|data
operator|.
name|put
argument_list|(
literal|"key_3"
argument_list|,
literal|543
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|WeakValueMap
argument_list|<>
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|data
operator|.
name|size
argument_list|()
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"nonexistent_key1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"key_3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
literal|321
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
literal|42
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"nonexistent_key2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|543
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"key_3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|123
argument_list|)
argument_list|,
name|map
operator|.
name|getOrDefault
argument_list|(
literal|"key_1"
argument_list|,
literal|42
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|data
operator|.
name|size
argument_list|()
argument_list|,
name|map
operator|.
name|values
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|data
operator|.
name|size
argument_list|()
argument_list|,
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|data
operator|.
name|size
argument_list|()
argument_list|,
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|values
argument_list|()
operator|.
name|containsAll
argument_list|(
name|data
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|containsAll
argument_list|(
name|data
operator|.
name|keySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|containsAll
argument_list|(
name|data
operator|.
name|entrySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleOperations
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|data
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|data
operator|.
name|put
argument_list|(
literal|"key_1"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|data
operator|.
name|put
argument_list|(
literal|"key_2"
argument_list|,
literal|42
argument_list|)
expr_stmt|;
name|data
operator|.
name|put
argument_list|(
literal|"key_3"
argument_list|,
literal|543
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|WeakValueMap
argument_list|<>
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_4"
argument_list|,
literal|44
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|44
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"key_4"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"key_4"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
literal|44
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|old
init|=
name|map
operator|.
name|remove
argument_list|(
literal|"key_4"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|44
argument_list|,
name|old
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"key_4"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
literal|44
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEntrySetUpdateValue
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|WeakValueMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_1"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_2"
argument_list|,
literal|42
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_3"
argument_list|,
literal|543
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|counter
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
literal|"key_2"
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|int
name|old
init|=
name|entry
operator|.
name|setValue
argument_list|(
literal|24
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|42
argument_list|,
name|old
argument_list|)
expr_stmt|;
block|}
name|counter
operator|++
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|counter
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|24
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"key_2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSerializationSupport
parameter_list|()
throws|throws
name|Exception
block|{
name|WeakValueMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|WeakValueMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_1"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_2"
argument_list|,
literal|42
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_3"
argument_list|,
literal|543
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_4"
argument_list|,
operator|new
name|TestSerializable
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|WeakValueMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|clone
init|=
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|clone
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|42
argument_list|,
name|clone
operator|.
name|get
argument_list|(
literal|"key_2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|clone
operator|.
name|containsKey
argument_list|(
literal|"key_3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|clone
operator|.
name|containsValue
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|clone
operator|.
name|containsKey
argument_list|(
literal|"key_4"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualsAndHashCode
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map1
init|=
operator|new
name|WeakValueMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map1
operator|.
name|put
argument_list|(
literal|"key_1"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|map1
operator|.
name|put
argument_list|(
literal|"key_2"
argument_list|,
literal|42
argument_list|)
expr_stmt|;
name|map1
operator|.
name|put
argument_list|(
literal|"key_3"
argument_list|,
literal|543
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|map1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map2
operator|.
name|put
argument_list|(
literal|"key_1"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|map2
operator|.
name|put
argument_list|(
literal|"key_2"
argument_list|,
literal|42
argument_list|)
expr_stmt|;
name|map2
operator|.
name|put
argument_list|(
literal|"key_3"
argument_list|,
literal|543
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map1
argument_list|,
name|map2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map1
operator|.
name|hashCode
argument_list|()
argument_list|,
name|map2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ConcurrentModificationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testConcurrentModification
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|WeakValueMap
argument_list|<>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_1"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_2"
argument_list|,
literal|42
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_3"
argument_list|,
literal|543
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_4"
argument_list|,
literal|321
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
literal|"key_2"
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|map
operator|.
name|remove
argument_list|(
literal|"key_2"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUnsupportedEntryIteratorRemoval
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|WeakValueMap
argument_list|<>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_1"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_2"
argument_list|,
literal|42
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"key_3"
argument_list|,
literal|543
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|iterator
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|NullPointerException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testPutNullValue
parameter_list|()
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|WeakValueMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"1"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|NullPointerException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testPutAllNullValue
parameter_list|()
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|values
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|values
operator|.
name|put
argument_list|(
literal|"123"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|WeakValueMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|putAll
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|TestSerializable
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|8726479278547192134L
decl_stmt|;
block|}
block|}
end_class

end_unit

