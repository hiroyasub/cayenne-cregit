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
name|assertNotSame
import|;
end_import

begin_class
specifier|public
class|class
name|DataRowTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testHessianSerializability
parameter_list|()
throws|throws
name|Exception
block|{
name|DataRow
name|s1
init|=
operator|new
name|DataRow
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|s1
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|DataRow
name|s2
init|=
operator|(
name|DataRow
operator|)
name|HessianUtil
operator|.
name|cloneViaServerClientSerialization
argument_list|(
name|s1
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|s1
operator|.
name|getVersion
argument_list|()
argument_list|,
name|s2
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|s1
operator|.
name|getReplacesVersion
argument_list|()
argument_list|,
name|s2
operator|.
name|getReplacesVersion
argument_list|()
argument_list|)
expr_stmt|;
comment|// at the moment there are no serializers that can go from client to
comment|// server.
comment|// DataRow s3 = (DataRow) HessianUtil.cloneViaClientServerSerialization(
comment|// s1,
comment|// new EntityResolver());
comment|//
comment|// assertNotSame(s1, s3);
comment|// assertEquals(s1, s3);
block|}
block|}
end_class

end_unit

