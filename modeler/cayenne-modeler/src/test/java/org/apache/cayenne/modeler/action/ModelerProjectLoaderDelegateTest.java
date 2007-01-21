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
name|modeler
operator|.
name|action
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

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
name|access
operator|.
name|DataNode
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
name|conf
operator|.
name|MockConfiguration
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mockrunner
operator|.
name|mock
operator|.
name|jdbc
operator|.
name|MockDataSource
import|;
end_import

begin_class
specifier|public
class|class
name|ModelerProjectLoaderDelegateTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testDataSource
parameter_list|()
block|{
name|ModelerProjectLoadDelegate
name|loader
init|=
operator|new
name|ModelerProjectLoadDelegate
argument_list|(
operator|new
name|MockConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|DataNode
name|node
init|=
name|loader
operator|.
name|createDataNode
argument_list|(
literal|"ABC"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ABC"
argument_list|,
name|node
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|DataSource
name|ds1
init|=
operator|new
name|MockDataSource
argument_list|()
decl_stmt|;
name|node
operator|.
name|setDataSource
argument_list|(
name|ds1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Project DataNode must not wrap the DataSource"
argument_list|,
name|ds1
argument_list|,
name|node
operator|.
name|getDataSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

