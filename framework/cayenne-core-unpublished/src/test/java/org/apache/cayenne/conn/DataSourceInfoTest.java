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
name|conn
package|;
end_package

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
name|util
operator|.
name|Util
import|;
end_import

begin_class
specifier|public
class|class
name|DataSourceInfoTest
extends|extends
name|TestCase
block|{
specifier|private
name|DataSourceInfo
name|dsi
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|dsi
operator|=
operator|new
name|DataSourceInfo
argument_list|()
expr_stmt|;
name|dsi
operator|.
name|setUserName
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|setPassword
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|setMinConnections
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|setMaxConnections
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|setJdbcDriver
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|setDataSourceUrl
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|setAdapterClassName
argument_list|(
literal|"d"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefaultValues
parameter_list|()
throws|throws
name|java
operator|.
name|lang
operator|.
name|Exception
block|{
name|DataSourceInfo
name|localDsi
init|=
operator|new
name|DataSourceInfo
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|localDsi
operator|.
name|getMinConnections
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|localDsi
operator|.
name|getMinConnections
argument_list|()
operator|<=
name|localDsi
operator|.
name|getMaxConnections
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testClone
parameter_list|()
throws|throws
name|java
operator|.
name|lang
operator|.
name|Exception
block|{
name|DataSourceInfo
name|dsiClone
init|=
name|dsi
operator|.
name|cloneInfo
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|dsi
argument_list|,
name|dsiClone
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dsi
operator|!=
name|dsiClone
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSerialize
parameter_list|()
throws|throws
name|java
operator|.
name|lang
operator|.
name|Exception
block|{
name|DataSourceInfo
name|dsiUnserialized
init|=
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|dsi
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|dsi
argument_list|,
name|dsiUnserialized
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dsi
operator|!=
name|dsiUnserialized
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
