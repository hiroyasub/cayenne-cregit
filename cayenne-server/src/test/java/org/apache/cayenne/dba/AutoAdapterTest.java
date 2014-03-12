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
name|dba
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
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
name|access
operator|.
name|jdbc
operator|.
name|SQLTemplateAction
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
name|di
operator|.
name|Inject
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
name|di
operator|.
name|Provider
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
name|log
operator|.
name|NoopJdbcEventLogger
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
name|query
operator|.
name|SQLTemplate
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
name|testdo
operator|.
name|testmap
operator|.
name|Artist
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|AutoAdapterTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataNode
name|dataNode
decl_stmt|;
specifier|public
name|void
name|testGetAdapter_Proxy
parameter_list|()
throws|throws
name|Exception
block|{
name|Provider
argument_list|<
name|DbAdapter
argument_list|>
name|adapterProvider
init|=
name|mock
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|adapterProvider
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dataNode
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|AutoAdapter
name|adapter
init|=
operator|new
name|AutoAdapter
argument_list|(
name|adapterProvider
argument_list|,
name|NoopJdbcEventLogger
operator|.
name|getInstance
argument_list|()
argument_list|)
decl_stmt|;
name|DbAdapter
name|detected
init|=
name|adapter
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|dataNode
operator|.
name|getAdapter
argument_list|()
argument_list|,
name|detected
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateSQLTemplateAction
parameter_list|()
block|{
name|Provider
argument_list|<
name|DbAdapter
argument_list|>
name|adapterProvider
init|=
name|mock
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|adapterProvider
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dataNode
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|AutoAdapter
name|autoAdapter
init|=
operator|new
name|AutoAdapter
argument_list|(
name|adapterProvider
argument_list|,
name|NoopJdbcEventLogger
operator|.
name|getInstance
argument_list|()
argument_list|)
decl_stmt|;
name|SQLTemplateAction
name|action
init|=
operator|(
name|SQLTemplateAction
operator|)
name|autoAdapter
operator|.
name|getAction
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"select * from artist"
argument_list|)
argument_list|,
name|dataNode
argument_list|)
decl_stmt|;
comment|// it is important for SQLTemplateAction to be used with unwrapped
comment|// adapter, as the
comment|// adapter class name is used as a key to the correct SQL template.
name|assertNotNull
argument_list|(
name|action
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|action
operator|.
name|getAdapter
argument_list|()
operator|instanceof
name|AutoAdapter
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|dataNode
operator|.
name|getAdapter
argument_list|()
argument_list|,
name|action
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

