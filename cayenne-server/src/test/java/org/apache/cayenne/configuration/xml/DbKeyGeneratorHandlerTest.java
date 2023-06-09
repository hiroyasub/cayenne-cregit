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
name|configuration
operator|.
name|xml
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
name|DbEntity
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
name|DbKeyGenerator
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
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|DbKeyGeneratorHandlerTest
extends|extends
name|BaseHandlerTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testParsing
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DbEntity
name|dbEntity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"TEST"
argument_list|)
decl_stmt|;
name|parse
argument_list|(
literal|"db-key-generator"
argument_list|,
operator|new
name|HandlerFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|NamespaceAwareNestedTagHandler
name|createHandler
parameter_list|(
name|NamespaceAwareNestedTagHandler
name|parent
parameter_list|)
block|{
return|return
operator|new
name|DbKeyGeneratorHandler
argument_list|(
name|parent
argument_list|,
name|dbEntity
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|dbEntity
operator|.
name|getPrimaryKeyGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"gallery_seq"
argument_list|,
name|dbEntity
operator|.
name|getPrimaryKeyGenerator
argument_list|()
operator|.
name|getGeneratorName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
operator|(
name|int
operator|)
name|dbEntity
operator|.
name|getPrimaryKeyGenerator
argument_list|()
operator|.
name|getKeyCacheSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DbKeyGenerator
operator|.
name|ORACLE_TYPE
argument_list|,
name|dbEntity
operator|.
name|getPrimaryKeyGenerator
argument_list|()
operator|.
name|getGeneratorType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

