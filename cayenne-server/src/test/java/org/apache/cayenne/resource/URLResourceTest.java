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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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

begin_class
specifier|public
class|class
name|URLResourceTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testURL
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"http://cayenne.apache.org"
argument_list|)
decl_stmt|;
name|URLResource
name|resource
init|=
operator|new
name|URLResource
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|url
argument_list|,
name|resource
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetRelativeResource
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"http://cayenne.apache.org"
argument_list|)
decl_stmt|;
name|URLResource
name|resource
init|=
operator|new
name|URLResource
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|Resource
name|relativeResource
init|=
name|resource
operator|.
name|getRelativeResource
argument_list|(
literal|"/docs"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|relativeResource
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://cayenne.apache.org/docs"
argument_list|,
name|relativeResource
operator|.
name|getURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

