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
name|access
operator|.
name|types
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
name|map
operator|.
name|EntityResolver
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
name|remote
operator|.
name|hessian
operator|.
name|service
operator|.
name|HessianUtil
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
name|util
operator|.
name|Util
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
name|ArrayList
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
name|assertSame
import|;
end_import

begin_comment
comment|/**  * A test case checking Cayenne handling of 1.5 Enums.  *   */
end_comment

begin_class
specifier|public
class|class
name|EnumTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSerializabilityWithHessianStandalone
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEnum
name|before
init|=
name|MockEnum
operator|.
name|a
decl_stmt|;
comment|// test standalone
name|Object
name|after
init|=
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|before
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|after
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|before
argument_list|,
name|after
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSerializabilityWithHessianInTheMap
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test in the Map
name|Map
argument_list|<
name|String
argument_list|,
name|MockEnum
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
name|MockEnum
operator|.
name|b
argument_list|)
expr_stmt|;
name|Map
name|after
init|=
operator|(
name|Map
operator|)
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
operator|(
name|Serializable
operator|)
name|map
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|MockEnum
operator|.
name|b
argument_list|,
name|after
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSerializabilityWithHessianObjectProperty
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test object property
name|MockEnumHolder
name|object
init|=
operator|new
name|MockEnumHolder
argument_list|()
decl_stmt|;
name|object
operator|.
name|setMockEnum
argument_list|(
name|MockEnum
operator|.
name|b
argument_list|)
expr_stmt|;
name|MockEnumHolder
name|after
init|=
operator|(
name|MockEnumHolder
operator|)
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|object
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|after
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|MockEnum
operator|.
name|b
argument_list|,
name|after
operator|.
name|getMockEnum
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSerializabilityWithHessianObjectPropertyInAList
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test that Enum properties are serialized properly...
name|MockEnumHolder
name|o1
init|=
operator|new
name|MockEnumHolder
argument_list|()
decl_stmt|;
name|o1
operator|.
name|setMockEnum
argument_list|(
name|MockEnum
operator|.
name|b
argument_list|)
expr_stmt|;
name|MockEnumHolder
name|o2
init|=
operator|new
name|MockEnumHolder
argument_list|()
decl_stmt|;
name|o2
operator|.
name|setMockEnum
argument_list|(
name|MockEnum
operator|.
name|c
argument_list|)
expr_stmt|;
name|ArrayList
argument_list|<
name|MockEnumHolder
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<
name|MockEnumHolder
argument_list|>
argument_list|()
decl_stmt|;
name|l
operator|.
name|add
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|l
operator|.
name|add
argument_list|(
name|o2
argument_list|)
expr_stmt|;
name|ArrayList
name|ld
init|=
operator|(
name|ArrayList
operator|)
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|l
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|ld
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|MockEnumHolder
name|o1d
init|=
operator|(
name|MockEnumHolder
operator|)
name|ld
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|MockEnumHolder
name|o2d
init|=
operator|(
name|MockEnumHolder
operator|)
name|ld
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|MockEnum
operator|.
name|b
argument_list|,
name|o1d
operator|.
name|getMockEnum
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|MockEnum
operator|.
name|c
argument_list|,
name|o2d
operator|.
name|getMockEnum
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSerializability
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEnum
name|before
init|=
name|MockEnum
operator|.
name|c
decl_stmt|;
name|Object
name|object
init|=
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|before
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|before
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

