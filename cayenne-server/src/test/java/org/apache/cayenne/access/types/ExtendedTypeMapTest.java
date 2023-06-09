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
name|access
operator|.
name|types
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
name|List
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
name|ExtendedTypeMapTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testRegisterType
parameter_list|()
throws|throws
name|Exception
block|{
name|ExtendedTypeMap
name|map
init|=
operator|new
name|ExtendedTypeMap
argument_list|()
decl_stmt|;
name|ExtendedType
name|tstType
init|=
operator|new
name|MockExtendedType
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|map
operator|.
name|getDefaultType
argument_list|()
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|tstType
operator|.
name|getClassName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
name|tstType
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|tstType
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|tstType
operator|.
name|getClassName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|unregisterType
argument_list|(
name|tstType
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|map
operator|.
name|getDefaultType
argument_list|()
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|tstType
operator|.
name|getClassName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegisterTypeSubclasses
parameter_list|()
throws|throws
name|Exception
block|{
name|ExtendedTypeMap
name|map
init|=
operator|new
name|ExtendedTypeMap
argument_list|()
decl_stmt|;
name|ExtendedType
name|tstType1
init|=
operator|new
name|MockExtendedType
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|ExtendedType
name|tstType2
init|=
operator|new
name|MockExtendedType
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// List
name|map
operator|.
name|registerType
argument_list|(
name|tstType1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|tstType1
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|List
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|tstType1
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|=
operator|new
name|ExtendedTypeMap
argument_list|()
expr_stmt|;
comment|// ArrayList
name|map
operator|.
name|registerType
argument_list|(
name|tstType2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|tstType2
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|List
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|tstType2
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|=
operator|new
name|ExtendedTypeMap
argument_list|()
expr_stmt|;
comment|// both
name|map
operator|.
name|registerType
argument_list|(
name|tstType1
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
name|tstType2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|tstType1
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|List
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|tstType2
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|=
operator|new
name|ExtendedTypeMap
argument_list|()
expr_stmt|;
comment|// both - different order
name|map
operator|.
name|registerType
argument_list|(
name|tstType2
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
name|tstType1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|tstType2
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|tstType1
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|List
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegisterArrayType
parameter_list|()
throws|throws
name|Exception
block|{
name|ExtendedTypeMap
name|map
init|=
operator|new
name|ExtendedTypeMap
argument_list|()
decl_stmt|;
name|ByteArrayType
name|tstType
init|=
operator|new
name|ByteArrayType
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|map
operator|.
name|registerType
argument_list|(
name|tstType
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|tstType
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|tstType
operator|.
name|getClassName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|tstType
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|unregisterType
argument_list|(
name|tstType
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
comment|// will return serializable ExtendedType inner class
name|assertTrue
argument_list|(
name|map
operator|.
name|getRegisteredType
argument_list|(
name|tstType
operator|.
name|getClassName
argument_list|()
argument_list|)
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"SerializableTypeFactory"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegisteredTypeName
parameter_list|()
throws|throws
name|Exception
block|{
name|ExtendedTypeMap
name|map
init|=
operator|new
name|ExtendedTypeMap
argument_list|()
decl_stmt|;
name|ExtendedType
name|tstType
init|=
operator|new
name|MockExtendedType
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
operator|.
name|getRegisteredTypeNames
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|getRegisteredTypeNames
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
name|tstType
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|map
operator|.
name|getRegisteredTypeNames
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|getRegisteredTypeNames
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tstType
operator|.
name|getClassName
argument_list|()
argument_list|,
name|map
operator|.
name|getRegisteredTypeNames
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

