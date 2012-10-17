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
name|map
operator|.
name|ObjAttribute
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
name|PersistentDescriptorTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testConstructor
parameter_list|()
block|{
name|PersistentDescriptor
name|d1
init|=
operator|new
name|PersistentDescriptor
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|d1
operator|.
name|getSuperclassDescriptor
argument_list|()
argument_list|)
expr_stmt|;
name|PersistentDescriptor
name|d2
init|=
operator|new
name|PersistentDescriptor
argument_list|()
decl_stmt|;
name|d2
operator|.
name|setSuperclassDescriptor
argument_list|(
name|d1
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|d1
operator|.
name|getSuperclassDescriptor
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|d1
argument_list|,
name|d2
operator|.
name|getSuperclassDescriptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCopyObjectProperties
parameter_list|()
block|{
name|PersistentDescriptor
name|d1
init|=
operator|new
name|PersistentDescriptor
argument_list|()
decl_stmt|;
name|ObjAttribute
name|attribute
init|=
name|mock
argument_list|(
name|ObjAttribute
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|PropertyDescriptor
name|property
init|=
operator|new
name|SimpleAttributeProperty
argument_list|(
name|d1
argument_list|,
name|accessor
argument_list|,
name|attribute
argument_list|)
decl_stmt|;
name|d1
operator|.
name|addDeclaredProperty
argument_list|(
name|property
argument_list|)
expr_stmt|;
name|TestBean
name|from
init|=
operator|new
name|TestBean
argument_list|()
decl_stmt|;
name|from
operator|.
name|setString
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|TestBean
name|to
init|=
operator|new
name|TestBean
argument_list|()
decl_stmt|;
name|d1
operator|.
name|shallowMerge
argument_list|(
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|to
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

