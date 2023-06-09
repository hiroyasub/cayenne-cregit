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

begin_class
specifier|public
class|class
name|BeanAccessorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testByteArrayProperty
parameter_list|()
block|{
name|BeanAccessor
name|accessor
init|=
operator|new
name|BeanAccessor
argument_list|(
name|TstJavaBean
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
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|}
decl_stmt|;
name|TstJavaBean
name|o1
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|o1
operator|.
name|getByteArrayField
argument_list|()
argument_list|)
expr_stmt|;
name|accessor
operator|.
name|setValue
argument_list|(
name|o1
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|bytes
argument_list|,
name|o1
operator|.
name|getByteArrayField
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|bytes
argument_list|,
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
name|testStringProperty
parameter_list|()
block|{
name|BeanAccessor
name|accessor
init|=
operator|new
name|BeanAccessor
argument_list|(
name|TstJavaBean
operator|.
name|class
argument_list|,
literal|"stringField"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|TstJavaBean
name|o1
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|o1
operator|.
name|getStringField
argument_list|()
argument_list|)
expr_stmt|;
name|accessor
operator|.
name|setValue
argument_list|(
name|o1
argument_list|,
literal|"ABC"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"ABC"
argument_list|,
name|o1
operator|.
name|getStringField
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"ABC"
argument_list|,
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
name|testIntProperty
parameter_list|()
block|{
name|BeanAccessor
name|accessor
init|=
operator|new
name|BeanAccessor
argument_list|(
name|TstJavaBean
operator|.
name|class
argument_list|,
literal|"intField"
argument_list|,
name|Integer
operator|.
name|TYPE
argument_list|)
decl_stmt|;
name|TstJavaBean
name|o1
init|=
operator|new
name|TstJavaBean
argument_list|()
decl_stmt|;
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
name|accessor
operator|.
name|setValue
argument_list|(
name|o1
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|o1
operator|.
name|getIntField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|accessor
operator|.
name|getValue
argument_list|(
name|o1
argument_list|)
argument_list|)
expr_stmt|;
name|accessor
operator|.
name|setValue
argument_list|(
name|o1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Incorrectly set null default"
argument_list|,
literal|0
argument_list|,
name|o1
operator|.
name|getIntField
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInheritedCovariantProperty
parameter_list|()
block|{
name|BeanAccessor
name|accessor
init|=
operator|new
name|BeanAccessor
argument_list|(
name|TstJavaBeanChild
operator|.
name|class
argument_list|,
literal|"related"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|TstJavaBeanChild
name|o1
init|=
operator|new
name|TstJavaBeanChild
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|o1
operator|.
name|getRelated
argument_list|()
argument_list|)
expr_stmt|;
name|accessor
operator|.
name|setValue
argument_list|(
name|o1
argument_list|,
name|o1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
argument_list|,
name|o1
operator|.
name|getRelated
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|o1
argument_list|,
name|accessor
operator|.
name|getValue
argument_list|(
name|o1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

