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
name|reflect
package|;
end_package

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
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|reflect
operator|.
name|PropertyUtils
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
name|PropertyUtilsTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testGetProperty
parameter_list|()
block|{
name|TestJavaBean
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
operator|new
name|Integer
argument_list|(
name|o1
operator|.
name|getIntField
argument_list|()
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
operator|new
name|Boolean
argument_list|(
name|o1
operator|.
name|isBooleanField
argument_list|()
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
specifier|public
name|void
name|testSetProperty
parameter_list|()
block|{
name|TestJavaBean
name|o1
init|=
name|createBean
argument_list|()
decl_stmt|;
name|TestJavaBean
name|o2
init|=
operator|new
name|TestJavaBean
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
operator|new
name|Integer
argument_list|(
name|o1
operator|.
name|getIntField
argument_list|()
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
operator|new
name|Boolean
argument_list|(
name|o1
operator|.
name|isBooleanField
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetPropertyMap
parameter_list|()
block|{
name|Map
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
specifier|public
name|void
name|testSetPropertyMap
parameter_list|()
block|{
name|Map
name|o1
init|=
name|createMap
argument_list|()
decl_stmt|;
name|Map
name|o2
init|=
operator|new
name|HashMap
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
specifier|public
name|void
name|testSetConverted
parameter_list|()
block|{
name|TestJavaBean
name|o1
init|=
operator|new
name|TestJavaBean
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
operator|new
name|Integer
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
comment|// string to primitive
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
block|}
specifier|public
name|void
name|testSetNull
parameter_list|()
block|{
name|TestJavaBean
name|o1
init|=
operator|new
name|TestJavaBean
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
block|}
specifier|public
name|void
name|testSetConverted
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
specifier|protected
name|TestJavaBean
name|createBean
parameter_list|()
block|{
name|TestJavaBean
name|o1
init|=
operator|new
name|TestJavaBean
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
operator|new
name|Integer
argument_list|(
literal|33
argument_list|)
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
specifier|protected
name|Map
name|createMap
parameter_list|()
block|{
name|Map
name|o1
init|=
operator|new
name|HashMap
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
operator|new
name|Integer
argument_list|(
literal|33
argument_list|)
argument_list|)
expr_stmt|;
name|o1
operator|.
name|put
argument_list|(
literal|"intField"
argument_list|,
operator|new
name|Integer
argument_list|(
operator|-
literal|44
argument_list|)
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

