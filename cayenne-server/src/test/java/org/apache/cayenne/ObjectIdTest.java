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
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|map
operator|.
name|LinkedMap
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
name|math
operator|.
name|BigDecimal
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
name|Map
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
name|assertSame
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
specifier|public
class|class
name|ObjectIdTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testConstructor
parameter_list|()
block|{
name|ObjectId
name|temp1
init|=
operator|new
name|ObjectId
argument_list|(
literal|"e"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"e"
argument_list|,
name|temp1
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|temp1
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|temp1
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|key
init|=
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|}
decl_stmt|;
name|ObjectId
name|temp2
init|=
operator|new
name|ObjectId
argument_list|(
literal|"e1"
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"e1"
argument_list|,
name|temp2
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|temp2
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|key
argument_list|,
name|temp2
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSerializabilityTemp
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|temp1
init|=
operator|new
name|ObjectId
argument_list|(
literal|"e"
argument_list|)
decl_stmt|;
name|ObjectId
name|temp2
init|=
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|temp1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|temp1
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|temp1
argument_list|,
name|temp2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|temp1
argument_list|,
name|temp2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSerializabilityPerm
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|perm1
init|=
operator|new
name|ObjectId
argument_list|(
literal|"e"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
comment|// make sure hashcode is resolved
name|int
name|h
init|=
name|perm1
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|h
argument_list|,
name|perm1
operator|.
name|hashCode
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|perm1
operator|.
name|hashCode
operator|!=
literal|0
argument_list|)
expr_stmt|;
name|ObjectId
name|perm2
init|=
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|perm1
argument_list|)
decl_stmt|;
comment|// make sure hashCode is reset to 0
name|assertTrue
argument_list|(
name|perm2
operator|.
name|hashCode
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|perm2
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|perm1
argument_list|,
name|perm2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|perm1
argument_list|,
name|perm2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEquals0
parameter_list|()
block|{
name|ObjectId
name|oid1
init|=
operator|new
name|ObjectId
argument_list|(
literal|"TE"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|oid1
argument_list|,
name|oid1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|oid1
operator|.
name|hashCode
argument_list|()
argument_list|,
name|oid1
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEquals1
parameter_list|()
block|{
name|ObjectId
name|oid1
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|ObjectId
name|oid2
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|oid1
argument_list|,
name|oid2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|oid1
operator|.
name|hashCode
argument_list|()
argument_list|,
name|oid2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEquals2
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|ObjectId
name|oid1
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm
argument_list|)
decl_stmt|;
name|ObjectId
name|oid2
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|oid1
argument_list|,
name|oid2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|oid1
operator|.
name|hashCode
argument_list|()
argument_list|,
name|oid2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEquals3
parameter_list|()
block|{
name|String
name|pknm
init|=
literal|"xyzabc"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|hm1
operator|.
name|put
argument_list|(
name|pknm
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|hm2
operator|.
name|put
argument_list|(
name|pknm
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|ObjectId
name|oid1
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm1
argument_list|)
decl_stmt|;
name|ObjectId
name|oid2
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|oid1
argument_list|,
name|oid2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|oid1
operator|.
name|hashCode
argument_list|()
argument_list|,
name|oid2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This is a test case reproducing conditions for the bug "8458963".      */
annotation|@
name|Test
specifier|public
name|void
name|testEquals5
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|hm1
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|hm1
operator|.
name|put
argument_list|(
literal|"key2"
argument_list|,
literal|11
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|hm2
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
literal|11
argument_list|)
expr_stmt|;
name|hm2
operator|.
name|put
argument_list|(
literal|"key2"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ObjectId
name|ref
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm1
argument_list|)
decl_stmt|;
name|ObjectId
name|oid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm2
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|ref
operator|.
name|equals
argument_list|(
name|oid
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Multiple key objectId      */
annotation|@
name|Test
specifier|public
name|void
name|testEquals6
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|hm1
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|hm1
operator|.
name|put
argument_list|(
literal|"key2"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|hm2
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|hm2
operator|.
name|put
argument_list|(
literal|"key2"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ObjectId
name|ref
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm1
argument_list|)
decl_stmt|;
name|ObjectId
name|oid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm2
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ref
operator|.
name|equals
argument_list|(
name|oid
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ref
operator|.
name|hashCode
argument_list|()
argument_list|,
name|oid
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks that hashCode works even if keys are inserted in the map in a      * different order...      */
annotation|@
name|Test
specifier|public
name|void
name|testEquals7
parameter_list|()
block|{
comment|// create maps with guaranteed iteration order
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm1
init|=
operator|new
name|LinkedMap
argument_list|()
decl_stmt|;
name|hm1
operator|.
name|put
argument_list|(
literal|"KEY1"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|hm1
operator|.
name|put
argument_list|(
literal|"KEY2"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm2
init|=
operator|new
name|LinkedMap
argument_list|()
decl_stmt|;
comment|// put same keys but in different order
name|hm2
operator|.
name|put
argument_list|(
literal|"KEY2"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|hm2
operator|.
name|put
argument_list|(
literal|"KEY1"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ObjectId
name|ref
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm1
argument_list|)
decl_stmt|;
name|ObjectId
name|oid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm2
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ref
operator|.
name|equals
argument_list|(
name|oid
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ref
operator|.
name|hashCode
argument_list|()
argument_list|,
name|oid
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualsBinaryKey
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|hm1
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
operator|new
name|byte
index|[]
block|{
literal|3
block|,
literal|4
block|,
literal|10
block|,
operator|-
literal|1
block|}
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|hm2
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
operator|new
name|byte
index|[]
block|{
literal|3
block|,
literal|4
block|,
literal|10
block|,
operator|-
literal|1
block|}
argument_list|)
expr_stmt|;
name|ObjectId
name|ref
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm1
argument_list|)
decl_stmt|;
name|ObjectId
name|oid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ref
operator|.
name|hashCode
argument_list|()
argument_list|,
name|oid
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ref
operator|.
name|equals
argument_list|(
name|oid
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualsNull
parameter_list|()
block|{
name|ObjectId
name|o
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|42
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|o
operator|.
name|equals
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIdAsMapKey
parameter_list|()
block|{
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Object
name|o1
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|String
name|pknm
init|=
literal|"xyzabc"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|hm1
operator|.
name|put
argument_list|(
name|pknm
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|hm2
operator|.
name|put
argument_list|(
name|pknm
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|ObjectId
name|oid1
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm1
argument_list|)
decl_stmt|;
name|ObjectId
name|oid2
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm2
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|oid1
argument_list|,
name|o1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|oid2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotEqual1
parameter_list|()
block|{
name|ObjectId
name|oid1
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T1"
argument_list|)
decl_stmt|;
name|ObjectId
name|oid2
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T2"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|oid1
operator|.
name|equals
argument_list|(
name|oid2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotEqual2
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|hm1
operator|.
name|put
argument_list|(
literal|"pk1"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|hm2
operator|.
name|put
argument_list|(
literal|"pk2"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|ObjectId
name|oid1
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm1
argument_list|)
decl_stmt|;
name|ObjectId
name|oid2
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm2
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|oid1
operator|.
name|equals
argument_list|(
name|oid2
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test different numeric types.      */
annotation|@
name|Test
specifier|public
name|void
name|testEquals8
parameter_list|()
block|{
comment|// create maps with guaranteed iteration order
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm1
init|=
operator|new
name|LinkedMap
argument_list|()
decl_stmt|;
name|hm1
operator|.
name|put
argument_list|(
literal|"KEY1"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|hm1
operator|.
name|put
argument_list|(
literal|"KEY2"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|hm2
init|=
operator|new
name|LinkedMap
argument_list|()
decl_stmt|;
comment|// put same keys but in different order
name|hm2
operator|.
name|put
argument_list|(
literal|"KEY2"
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|2.00
argument_list|)
argument_list|)
expr_stmt|;
name|hm2
operator|.
name|put
argument_list|(
literal|"KEY1"
argument_list|,
literal|1L
argument_list|)
expr_stmt|;
name|ObjectId
name|ref
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm1
argument_list|)
decl_stmt|;
name|ObjectId
name|oid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"T"
argument_list|,
name|hm2
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ref
operator|.
name|equals
argument_list|(
name|oid
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ref
operator|.
name|hashCode
argument_list|()
argument_list|,
name|oid
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|m1
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|m1
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|ObjectId
name|i1
init|=
operator|new
name|ObjectId
argument_list|(
literal|"e1"
argument_list|,
name|m1
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|m2
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|m2
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|ObjectId
name|i2
init|=
operator|new
name|ObjectId
argument_list|(
literal|"e1"
argument_list|,
name|m2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|i1
argument_list|,
name|i2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|i1
operator|.
name|toString
argument_list|()
argument_list|,
name|i2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

