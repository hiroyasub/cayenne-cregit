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
name|InnerEnumHolder
operator|.
name|InnerEnum
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|ExtendedTypeMapEnumsTest
block|{
specifier|private
name|ExtendedTypeMap
name|map
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|this
operator|.
name|map
operator|=
operator|new
name|ExtendedTypeMap
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateType_NoFactory
parameter_list|()
block|{
name|assertNull
argument_list|(
name|map
operator|.
name|createType
argument_list|(
name|Object
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateType_Enum
parameter_list|()
block|{
name|ExtendedType
name|type1
init|=
name|map
operator|.
name|createType
argument_list|(
name|MockEnum
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|type1
operator|instanceof
name|EnumType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MockEnum
operator|.
name|class
argument_list|,
operator|(
operator|(
name|EnumType
argument_list|<
name|?
argument_list|>
operator|)
name|type1
operator|)
operator|.
name|enumClass
argument_list|)
expr_stmt|;
name|ExtendedType
name|type2
init|=
name|map
operator|.
name|createType
argument_list|(
name|MockEnum2
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|type1
argument_list|,
name|type2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateType_InnerEnum
parameter_list|()
block|{
name|ExtendedType
name|type
init|=
name|map
operator|.
name|createType
argument_list|(
name|InnerEnumHolder
operator|.
name|InnerEnum
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|type
operator|instanceof
name|EnumType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|InnerEnumHolder
operator|.
name|InnerEnum
operator|.
name|class
argument_list|,
operator|(
operator|(
name|EnumType
argument_list|<
name|?
argument_list|>
operator|)
name|type
operator|)
operator|.
name|enumClass
argument_list|)
expr_stmt|;
comment|// use a string name with $
name|ExtendedType
name|type1
init|=
name|map
operator|.
name|createType
argument_list|(
name|InnerEnumHolder
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"$InnerEnum"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|type1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|type
operator|.
name|getClassName
argument_list|()
argument_list|,
name|type1
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
comment|// use a string name with .
name|ExtendedType
name|type2
init|=
name|map
operator|.
name|createType
argument_list|(
name|InnerEnumHolder
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".InnerEnum"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|type2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|type
operator|.
name|getClassName
argument_list|()
argument_list|,
name|type2
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetRegisteredType
parameter_list|()
block|{
name|ExtendedType
name|type
init|=
name|map
operator|.
name|getRegisteredType
argument_list|(
name|MockEnum
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|type
operator|instanceof
name|EnumType
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|type
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|MockEnum
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|type
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|MockEnum
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetRegisteredType_InnerEnum
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|extendedTypeFactories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ExtendedType
name|byType
init|=
name|map
operator|.
name|getRegisteredType
argument_list|(
name|InnerEnum
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// this and subsequent tests verify that no memory leak occurs per CAY-2066
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|extendedTypeFactories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|byType
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|InnerEnum
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|extendedTypeFactories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|byType
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|InnerEnumHolder
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"$InnerEnum"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|extendedTypeFactories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|byType
argument_list|,
name|map
operator|.
name|getRegisteredType
argument_list|(
name|InnerEnumHolder
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".InnerEnum"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|extendedTypeFactories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

