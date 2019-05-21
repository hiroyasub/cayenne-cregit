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
name|DataMap
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
name|SQLTemplateDescriptor
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

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|PrefetchTypeForSqlTemplateHandlerTest
extends|extends
name|BaseHandlerTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testLoad
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|parse
argument_list|(
literal|"query"
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
name|QueryDescriptorHandler
argument_list|(
name|parent
argument_list|,
name|map
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|SQLTemplateDescriptor
name|sqlTemplateDescriptor
init|=
operator|(
name|SQLTemplateDescriptor
operator|)
name|map
operator|.
name|getQueryDescriptor
argument_list|(
literal|"query"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|sqlTemplateDescriptor
operator|.
name|getPrefetchesMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
operator|(
name|int
operator|)
name|sqlTemplateDescriptor
operator|.
name|getPrefetchesMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"paintings"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
name|int
operator|)
name|sqlTemplateDescriptor
operator|.
name|getPrefetchesMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"paintings.artist"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
operator|(
name|int
operator|)
name|sqlTemplateDescriptor
operator|.
name|getPrefetchesMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"paintings.gallery"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

