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
name|reflect
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
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
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
name|types
operator|.
name|MockEnum
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
name|types
operator|.
name|MockEnumHolder
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
name|PropertyUtilsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testAccessor
parameter_list|()
block|{
name|Accessor
name|accessor
init|=
name|PropertyUtils
operator|.
name|accessor
argument_list|(
literal|"byteArrayField"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|accessor
argument_list|)
expr_stmt|;
name|TstJavaBean
name|o1
init|=
name|createBean
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|o1
operator|.
name|getByteArrayField
argument_list|()
argument_list|,
name|accessor
operator|.
name|getValue
argument_list|(
name|o1
argument_list|)
argument_list|)
expr_stmt|;
name|TstJavaBean
name|o2
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|o2
operator|.
name|getByteArrayField
argument_list|()
argument_list|)
expr_stmt|;
name|accessor
operator|.
name|setValue
argument_list|(
name|o2
argument_list|,
name|o1
operator|.
name|getByteArrayField
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|o2
operator|.
name|getByteArrayField
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
operator|.
name|getByteArrayField
argument_list|()
argument_list|,
name|o2
operator|.
name|getByteArrayField
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAccessor_Cache
parameter_list|()
block|{
name|Accessor
name|accessor
init|=
name|PropertyUtils
operator|.
name|accessor
argument_list|(
literal|"p1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|accessor
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|accessor
argument_list|,
name|PropertyUtils
operator|.
name|accessor
argument_list|(
literal|"p1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|accessor
argument_list|,
name|PropertyUtils
operator|.
name|accessor
argument_list|(
literal|"p1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|accessor
argument_list|,
name|PropertyUtils
operator|.
name|accessor
argument_list|(
literal|"p2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAccessor_CacheNested
parameter_list|()
block|{
name|Accessor
name|accessor
init|=
name|PropertyUtils
operator|.
name|accessor
argument_list|(
literal|"p1.p2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|accessor
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|accessor
argument_list|,
name|PropertyUtils
operator|.
name|accessor
argument_list|(
literal|"p1.p2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|accessor
argument_list|,
name|PropertyUtils
operator|.
name|accessor
argument_list|(
literal|"p1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|accessor
argument_list|,
name|PropertyUtils
operator|.
name|accessor
argument_list|(
literal|"p2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAccessorNested
parameter_list|()
block|{
name|Accessor
name|accessor
init|=
name|PropertyUtils
operator|.
name|accessor
argument_list|(
literal|"related.byteArrayField"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|accessor
argument_list|)
expr_stmt|;
name|TstJavaBean
name|o1
init|=
name|createBean
argument_list|()
decl_stmt|;
name|o1
operator|.
name|setRelated
argument_list|(
operator|new
name|TstJavaBean
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|getRelated
argument_list|()
operator|.
name|setByteArrayField
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|3
block|,
literal|4
block|,
literal|5
block|}
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
operator|.
name|getRelated
argument_list|()
operator|.
name|getByteArrayField
argument_list|()
argument_list|,
name|accessor
operator|.
name|getValue
argument_list|(
name|o1
argument_list|)
argument_list|)
expr_stmt|;
name|TstJavaBean
name|o2
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|o2
operator|.
name|setRelated
argument_list|(
operator|new
name|TstJavaBean
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|b1
init|=
operator|new
name|byte
index|[]
block|{
literal|6
block|,
literal|7
block|,
literal|8
block|}
decl_stmt|;
name|accessor
operator|.
name|setValue
argument_list|(
name|o2
argument_list|,
name|b1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|b1
argument_list|,
name|o2
operator|.
name|getRelated
argument_list|()
operator|.
name|getByteArrayField
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAccessorNested_Null
parameter_list|()
block|{
name|Accessor
name|accessor
init|=
name|PropertyUtils
operator|.
name|accessor
argument_list|(
literal|"related.byteArrayField"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|accessor
argument_list|)
expr_stmt|;
name|TstJavaBean
name|o1
init|=
name|createBean
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|accessor
operator|.
name|getValue
argument_list|(
name|o1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProperty
parameter_list|()
block|{
name|TstJavaBean
name|o1
init|=
name|createBean
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|o1
operator|.
name|getByteArrayField
argument_list|()
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"byteArrayField"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
operator|.
name|getIntegerField
argument_list|()
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"integerField"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o1
operator|.
name|getIntField
argument_list|()
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"intField"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
operator|.
name|getNumberField
argument_list|()
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"numberField"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
operator|.
name|getObjectField
argument_list|()
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"objectField"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
operator|.
name|getStringField
argument_list|()
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"stringField"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o1
operator|.
name|isBooleanField
argument_list|()
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"booleanField"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProperty_Nested
parameter_list|()
block|{
name|TstJavaBean
name|o1
init|=
name|createBean
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"related.integerField"
argument_list|)
argument_list|)
expr_stmt|;
name|TstJavaBean
name|o1related
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|o1related
operator|.
name|setIntegerField
argument_list|(
literal|44
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setRelated
argument_list|(
name|o1related
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|44
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"related.integerField"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetProperty_NestedOuter
parameter_list|()
block|{
name|TstJavaBean
name|o1
init|=
name|createBean
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"related+.integerField"
argument_list|)
argument_list|)
expr_stmt|;
name|TstJavaBean
name|o1related
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|o1related
operator|.
name|setIntegerField
argument_list|(
literal|42
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setRelated
argument_list|(
name|o1related
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|42
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"related+.integerField"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetProperty
parameter_list|()
block|{
name|TstJavaBean
name|o1
init|=
name|createBean
argument_list|()
decl_stmt|;
name|TstJavaBean
name|o2
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"byteArrayField"
argument_list|,
name|o1
operator|.
name|getByteArrayField
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"integerField"
argument_list|,
name|o1
operator|.
name|getIntegerField
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"intField"
argument_list|,
name|o1
operator|.
name|getIntField
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"numberField"
argument_list|,
name|o1
operator|.
name|getNumberField
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"objectField"
argument_list|,
name|o1
operator|.
name|getObjectField
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"stringField"
argument_list|,
name|o1
operator|.
name|getStringField
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"booleanField"
argument_list|,
name|o1
operator|.
name|isBooleanField
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetPropertyMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|o1
init|=
name|createMap
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|o1
operator|.
name|get
argument_list|(
literal|"byteArrayField"
argument_list|)
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"byteArrayField"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
operator|.
name|get
argument_list|(
literal|"integerField"
argument_list|)
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"integerField"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o1
operator|.
name|get
argument_list|(
literal|"intField"
argument_list|)
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"intField"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
operator|.
name|get
argument_list|(
literal|"numberField"
argument_list|)
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"numberField"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
operator|.
name|get
argument_list|(
literal|"objectField"
argument_list|)
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"objectField"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
operator|.
name|get
argument_list|(
literal|"stringField"
argument_list|)
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"stringField"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o1
operator|.
name|get
argument_list|(
literal|"booleanField"
argument_list|)
argument_list|,
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o1
argument_list|,
literal|"booleanField"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetProperty_Nested
parameter_list|()
block|{
name|TstJavaBean
name|o1
init|=
name|createBean
argument_list|()
decl_stmt|;
name|TstJavaBean
name|o1related
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|o1related
operator|.
name|setIntegerField
argument_list|(
literal|44
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setRelated
argument_list|(
name|o1related
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"related.integerField"
argument_list|,
literal|55
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|55
argument_list|)
argument_list|,
name|o1related
operator|.
name|getIntegerField
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetProperty_Null
parameter_list|()
block|{
name|TstJavaBean
name|o1
init|=
name|createBean
argument_list|()
decl_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"related.integerField"
argument_list|,
literal|55
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetPropertyMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|o1
init|=
name|createMap
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|o2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"byteArrayField"
argument_list|,
name|o1
operator|.
name|get
argument_list|(
literal|"byteArrayField"
argument_list|)
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"integerField"
argument_list|,
name|o1
operator|.
name|get
argument_list|(
literal|"integerField"
argument_list|)
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"intField"
argument_list|,
name|o1
operator|.
name|get
argument_list|(
literal|"intField"
argument_list|)
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"numberField"
argument_list|,
name|o1
operator|.
name|get
argument_list|(
literal|"numberField"
argument_list|)
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"objectField"
argument_list|,
name|o1
operator|.
name|get
argument_list|(
literal|"objectField"
argument_list|)
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"stringField"
argument_list|,
name|o1
operator|.
name|get
argument_list|(
literal|"stringField"
argument_list|)
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o2
argument_list|,
literal|"booleanField"
argument_list|,
name|o1
operator|.
name|get
argument_list|(
literal|"booleanField"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetConverted
parameter_list|()
block|{
name|TstJavaBean
name|o1
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
comment|// Object -> String
name|Object
name|object
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"stringField"
argument_list|,
name|object
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|object
operator|.
name|toString
argument_list|()
argument_list|,
name|o1
operator|.
name|getStringField
argument_list|()
argument_list|)
expr_stmt|;
comment|// String to number
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"integerField"
argument_list|,
literal|"25"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|,
name|o1
operator|.
name|getIntegerField
argument_list|()
argument_list|)
expr_stmt|;
comment|// string to byte primitive
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"byteField"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|o1
operator|.
name|getByteField
argument_list|()
argument_list|)
expr_stmt|;
comment|// string to short primitive
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"shortField"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|o1
operator|.
name|getShortField
argument_list|()
argument_list|)
expr_stmt|;
comment|// string to int primitive
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"intField"
argument_list|,
literal|"28"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|28
argument_list|,
name|o1
operator|.
name|getIntField
argument_list|()
argument_list|)
expr_stmt|;
comment|// string to long primitive
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"longField"
argument_list|,
literal|"29"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|29
argument_list|,
name|o1
operator|.
name|getLongField
argument_list|()
argument_list|)
expr_stmt|;
comment|// string to float primitive
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"floatField"
argument_list|,
literal|"4.5"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4.5f
argument_list|,
name|o1
operator|.
name|getFloatField
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// string to double primitive
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"doubleField"
argument_list|,
literal|"5.5"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5.5
argument_list|,
name|o1
operator|.
name|getDoubleField
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// string to boolean
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"booleanField"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o1
operator|.
name|isBooleanField
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"booleanField"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|o1
operator|.
name|isBooleanField
argument_list|()
argument_list|)
expr_stmt|;
comment|// int to boolean
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"booleanField"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o1
operator|.
name|isBooleanField
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"booleanField"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|o1
operator|.
name|isBooleanField
argument_list|()
argument_list|)
expr_stmt|;
comment|// long to boolean
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"booleanField"
argument_list|,
literal|1L
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o1
operator|.
name|isBooleanField
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"booleanField"
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|o1
operator|.
name|isBooleanField
argument_list|()
argument_list|)
expr_stmt|;
comment|// long to date
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"dateField"
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Date
argument_list|(
literal|0L
argument_list|)
argument_list|,
name|o1
operator|.
name|getDateField
argument_list|()
argument_list|)
expr_stmt|;
comment|// long to timestamp
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"timestampField"
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Timestamp
argument_list|(
literal|0L
argument_list|)
argument_list|,
name|o1
operator|.
name|getTimestampField
argument_list|()
argument_list|)
expr_stmt|;
comment|// arbitrary string/object to field
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"stringBuilderField"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|o1
operator|.
name|getStringBuilderField
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetConvertedWithCustomConverter
parameter_list|()
block|{
comment|// save old converter for restore
name|Converter
argument_list|<
name|Date
argument_list|>
name|oldConverter
init|=
name|ConverterFactory
operator|.
name|factory
operator|.
name|getConverter
argument_list|(
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|ConverterFactory
operator|.
name|addConverter
argument_list|(
name|Date
operator|.
name|class
argument_list|,
operator|new
name|Converter
argument_list|<>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Date
name|convert
parameter_list|(
name|Object
name|value
parameter_list|,
name|Class
argument_list|<
name|Date
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
return|return
literal|null
return|;
if|if
condition|(
name|value
operator|instanceof
name|Date
condition|)
block|{
return|return
operator|(
name|Date
operator|)
name|value
return|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|SimpleDateFormat
name|format
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd"
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|format
operator|.
name|parse
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unable to convert '"
operator|+
name|value
operator|+
literal|"' to a Date"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unable to convert '"
operator|+
name|value
operator|+
literal|"' to a Date"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
name|TstJavaBean
name|o1
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
comment|// String to date
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"dateField"
argument_list|,
literal|"2013-08-01"
argument_list|)
expr_stmt|;
name|Calendar
name|cal
init|=
operator|new
name|GregorianCalendar
argument_list|(
literal|2013
argument_list|,
name|Calendar
operator|.
name|AUGUST
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
argument_list|,
name|o1
operator|.
name|getDateField
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// restore global date converter
name|ConverterFactory
operator|.
name|addConverter
argument_list|(
name|Date
operator|.
name|class
argument_list|,
name|oldConverter
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetNull
parameter_list|()
block|{
name|TstJavaBean
name|o1
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|o1
operator|.
name|setStringField
argument_list|(
literal|"xyz"
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"stringField"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|o1
operator|.
name|getStringField
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setBooleanField
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"booleanField"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|o1
operator|.
name|isBooleanField
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setByteField
argument_list|(
operator|(
name|byte
operator|)
literal|2
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"byteField"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
name|o1
operator|.
name|getByteField
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setShortField
argument_list|(
operator|(
name|short
operator|)
literal|3
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"shortField"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|,
name|o1
operator|.
name|getShortField
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setIntField
argument_list|(
literal|99
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"intField"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|o1
operator|.
name|getIntField
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setLongField
argument_list|(
literal|98
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"longField"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0L
argument_list|,
name|o1
operator|.
name|getLongField
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setFloatField
argument_list|(
literal|4.5f
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"floatField"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0f
argument_list|,
name|o1
operator|.
name|getFloatField
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setDoubleField
argument_list|(
literal|5.5f
argument_list|)
expr_stmt|;
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"doubleField"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0
argument_list|,
name|o1
operator|.
name|getDoubleField
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetConvertedEnum
parameter_list|()
block|{
name|MockEnumHolder
name|o1
init|=
operator|new
name|MockEnumHolder
argument_list|()
decl_stmt|;
comment|// String to Enum
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"mockEnum"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|MockEnum
operator|.
name|b
argument_list|,
name|o1
operator|.
name|getMockEnum
argument_list|()
argument_list|)
expr_stmt|;
comment|// check that regular converters still work
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|o1
argument_list|,
literal|"number"
argument_list|,
literal|"445"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|445
argument_list|,
name|o1
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|TstJavaBean
name|createBean
parameter_list|()
block|{
name|TstJavaBean
name|o1
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|o1
operator|.
name|setByteArrayField
argument_list|(
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
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setIntegerField
argument_list|(
literal|33
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setIntField
argument_list|(
operator|-
literal|44
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setNumberField
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"11111"
argument_list|)
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setObjectField
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setStringField
argument_list|(
literal|"aaaaa"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setBooleanField
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|o1
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|createMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|o1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|o1
operator|.
name|put
argument_list|(
literal|"byteArrayField"
argument_list|,
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
argument_list|)
expr_stmt|;
name|o1
operator|.
name|put
argument_list|(
literal|"integerField"
argument_list|,
literal|33
argument_list|)
expr_stmt|;
name|o1
operator|.
name|put
argument_list|(
literal|"intField"
argument_list|,
operator|-
literal|44
argument_list|)
expr_stmt|;
name|o1
operator|.
name|put
argument_list|(
literal|"numberField"
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|"11111"
argument_list|)
argument_list|)
expr_stmt|;
name|o1
operator|.
name|put
argument_list|(
literal|"objectField"
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|o1
operator|.
name|put
argument_list|(
literal|"stringField"
argument_list|,
literal|"aaaaa"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|put
argument_list|(
literal|"booleanField"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|o1
return|;
block|}
block|}
end_class

end_unit

