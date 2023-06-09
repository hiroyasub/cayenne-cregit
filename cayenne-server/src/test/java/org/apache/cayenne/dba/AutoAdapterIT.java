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
name|dba
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
name|ObjectSelect
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
name|CayenneProjects
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
name|assertFalse
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
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|AutoAdapterIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataNode
name|dataNode
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testGetAdapter_Proxy
parameter_list|()
block|{
name|AutoAdapter
name|adapter
init|=
operator|new
name|AutoAdapter
argument_list|(
parameter_list|()
lambda|->
name|dataNode
operator|.
name|getAdapter
argument_list|()
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
annotation|@
name|Test
specifier|public
name|void
name|testCreateSQLTemplateAction
parameter_list|()
block|{
name|AutoAdapter
name|autoAdapter
init|=
operator|new
name|AutoAdapter
argument_list|(
parameter_list|()
lambda|->
name|dataNode
operator|.
name|getAdapter
argument_list|()
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
comment|// it is important for SQLTemplateAction to be used with unwrapped adapter,
comment|// as the adapter class name is used as a key to the correct SQL template.
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
annotation|@
name|Test
specifier|public
name|void
name|testCorrectProxyMethods
parameter_list|()
block|{
name|DbAdapter
name|adapter
init|=
name|dataNode
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|AutoAdapter
name|autoAdapter
init|=
operator|new
name|AutoAdapter
argument_list|(
parameter_list|()
lambda|->
name|adapter
argument_list|,
name|NoopJdbcEventLogger
operator|.
name|getInstance
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectSelect
argument_list|<
name|Artist
argument_list|>
name|select
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// query related methods
name|assertEquals
argument_list|(
name|adapter
operator|.
name|supportsBatchUpdates
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|supportsBatchUpdates
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|adapter
operator|.
name|supportsGeneratedKeys
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|supportsGeneratedKeys
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|adapter
operator|.
name|supportsGeneratedKeysForBatchInserts
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|supportsGeneratedKeysForBatchInserts
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|adapter
operator|.
name|getBatchTerminator
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|getBatchTerminator
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|adapter
operator|.
name|getPkGenerator
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|getPkGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|getQuotingStrategy
argument_list|()
argument_list|)
expr_stmt|;
comment|// returns a new instance for each call
name|assertSame
argument_list|(
name|adapter
operator|.
name|getSqlTreeProcessor
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|getSqlTreeProcessor
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|adapter
operator|.
name|getExtendedTypes
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|getExtendedTypes
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|adapter
operator|.
name|getEjbqlTranslatorFactory
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|getEjbqlTranslatorFactory
argument_list|()
argument_list|)
expr_stmt|;
comment|// returns a new instance for each call
name|assertSame
argument_list|(
name|adapter
operator|.
name|getSelectTranslator
argument_list|(
name|select
argument_list|,
name|dataNode
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|getSelectTranslator
argument_list|(
name|select
argument_list|,
name|dataNode
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
comment|// reverse engineering related methods
name|assertEquals
argument_list|(
name|adapter
operator|.
name|supportsCatalogsOnReverseEngineering
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|supportsCatalogsOnReverseEngineering
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|adapter
operator|.
name|getSystemCatalogs
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|getSystemCatalogs
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|adapter
operator|.
name|getSystemSchemas
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|getSystemSchemas
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|adapter
operator|.
name|tableTypeForTable
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|tableTypeForTable
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|adapter
operator|.
name|tableTypeForView
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|tableTypeForView
argument_list|()
argument_list|)
expr_stmt|;
comment|// db generation related methods
name|assertEquals
argument_list|(
name|adapter
operator|.
name|supportsUniqueConstraints
argument_list|()
argument_list|,
name|autoAdapter
operator|.
name|supportsUniqueConstraints
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

