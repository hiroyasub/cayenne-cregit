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
name|map
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DbKeyGeneratorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testName
parameter_list|()
throws|throws
name|Exception
block|{
name|DbKeyGenerator
name|generator
init|=
operator|new
name|DbKeyGenerator
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setGeneratorName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|generator
operator|.
name|getGeneratorName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSize
parameter_list|()
throws|throws
name|Exception
block|{
name|DbKeyGenerator
name|generator
init|=
operator|new
name|DbKeyGenerator
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setKeyCacheSize
argument_list|(
operator|new
name|Integer
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|3
argument_list|)
argument_list|,
name|generator
operator|.
name|getKeyCacheSize
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setKeyCacheSize
argument_list|(
operator|new
name|Integer
argument_list|(
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|generator
operator|.
name|getKeyCacheSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

