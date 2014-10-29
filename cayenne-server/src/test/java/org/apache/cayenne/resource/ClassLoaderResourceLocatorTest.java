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
name|resource
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
name|di
operator|.
name|spi
operator|.
name|DefaultClassLoaderManager
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
name|util
operator|.
name|Collection
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|ClassLoaderResourceLocatorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFindResources
parameter_list|()
block|{
name|ClassLoaderResourceLocator
name|locator
init|=
operator|new
name|ClassLoaderResourceLocator
argument_list|(
operator|new
name|DefaultClassLoaderManager
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Resource
argument_list|>
name|resources
init|=
name|locator
operator|.
name|findResources
argument_list|(
literal|"org/apache/cayenne/resource/ClassLoaderResourceLocatorTest.class"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resources
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resources
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Resource
name|resource
init|=
name|resources
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|resource
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resource
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resource
operator|.
name|getURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"org/apache/cayenne/resource/ClassLoaderResourceLocatorTest.class"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

