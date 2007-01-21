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
name|reflect
operator|.
name|FieldAccessor
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
name|util
operator|.
name|TestBean
import|;
end_import

begin_class
specifier|public
class|class
name|FieldAccessorTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testConstructor
parameter_list|()
block|{
name|FieldAccessor
name|accessor
init|=
operator|new
name|FieldAccessor
argument_list|(
name|TestBean
operator|.
name|class
argument_list|,
literal|"string"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"string"
argument_list|,
name|accessor
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGet
parameter_list|()
block|{
name|FieldAccessor
name|accessor
init|=
operator|new
name|FieldAccessor
argument_list|(
name|TestBean
operator|.
name|class
argument_list|,
literal|"string"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|TestBean
name|object
init|=
operator|new
name|TestBean
argument_list|()
decl_stmt|;
name|object
operator|.
name|setString
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|accessor
operator|.
name|getValue
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSetValue
parameter_list|()
block|{
name|TestFields
name|object
init|=
operator|new
name|TestFields
argument_list|()
decl_stmt|;
comment|// string
operator|new
name|FieldAccessor
argument_list|(
name|TestFields
operator|.
name|class
argument_list|,
literal|"stringField"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|setValue
argument_list|(
name|object
argument_list|,
literal|"aaa"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aaa"
argument_list|,
name|object
operator|.
name|stringField
argument_list|)
expr_stmt|;
comment|// primitive array
name|byte
index|[]
name|bytes
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
operator|new
name|FieldAccessor
argument_list|(
name|TestFields
operator|.
name|class
argument_list|,
literal|"byteArrayField"
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|setValue
argument_list|(
name|object
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|bytes
argument_list|,
name|object
operator|.
name|byteArrayField
argument_list|)
expr_stmt|;
comment|// object array
name|String
index|[]
name|strings
init|=
operator|new
name|String
index|[]
block|{
literal|"a"
block|,
literal|"b"
block|}
decl_stmt|;
operator|new
name|FieldAccessor
argument_list|(
name|TestFields
operator|.
name|class
argument_list|,
literal|"stringArrayField"
argument_list|,
name|String
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|setValue
argument_list|(
name|object
argument_list|,
name|strings
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|strings
argument_list|,
name|object
operator|.
name|stringArrayField
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSetValuePrimitive
parameter_list|()
block|{
name|TestFields
name|object
init|=
operator|new
name|TestFields
argument_list|()
decl_stmt|;
comment|// primitive int .. write non-null
operator|new
name|FieldAccessor
argument_list|(
name|TestFields
operator|.
name|class
argument_list|,
literal|"intField"
argument_list|,
name|Integer
operator|.
name|TYPE
argument_list|)
operator|.
name|setValue
argument_list|(
name|object
argument_list|,
operator|new
name|Integer
argument_list|(
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|object
operator|.
name|intField
argument_list|)
expr_stmt|;
comment|// primitive int .. write null
name|object
operator|.
name|intField
operator|=
literal|55
expr_stmt|;
operator|new
name|FieldAccessor
argument_list|(
name|TestFields
operator|.
name|class
argument_list|,
literal|"intField"
argument_list|,
name|Integer
operator|.
name|TYPE
argument_list|)
operator|.
name|setValue
argument_list|(
name|object
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|object
operator|.
name|intField
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

