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
name|java
operator|.
name|util
operator|.
name|List
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
name|ClientServerChannel
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
name|exp
operator|.
name|ExpressionFactory
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
name|query
operator|.
name|SelectQuery
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
name|ClientChannel
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
name|service
operator|.
name|LocalConnection
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
name|mt
operator|.
name|ClientMtTable1
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
name|mt
operator|.
name|ClientMtTable1Subclass
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
name|mt
operator|.
name|MtTable1
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
name|AccessStack
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
name|CayenneCase
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
name|CayenneResources
import|;
end_import

begin_class
specifier|public
class|class
name|CayenneContextInheritanceTest
extends|extends
name|CayenneCase
block|{
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteTestData
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|AccessStack
name|buildAccessStack
parameter_list|()
block|{
return|return
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getAccessStack
argument_list|(
name|MULTI_TIER_ACCESS_STACK
argument_list|)
return|;
block|}
specifier|private
name|CayenneContext
name|createClientContext
parameter_list|()
block|{
name|ClientServerChannel
name|serverChannel
init|=
operator|new
name|ClientServerChannel
argument_list|(
name|getDomain
argument_list|()
argument_list|)
decl_stmt|;
name|LocalConnection
name|connection
init|=
operator|new
name|LocalConnection
argument_list|(
name|serverChannel
argument_list|,
name|LocalConnection
operator|.
name|HESSIAN_SERIALIZATION
argument_list|)
decl_stmt|;
name|ClientChannel
name|clientChannel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
argument_list|)
decl_stmt|;
return|return
operator|new
name|CayenneContext
argument_list|(
name|clientChannel
argument_list|)
return|;
block|}
specifier|public
name|void
name|testPerformQueryInheritanceLeaf
parameter_list|()
block|{
name|ObjectContext
name|setupContext
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|setupContext
operator|.
name|performQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|,
literal|"INSERT INTO MT_TABLE1 (TABLE1_ID, GLOBAL_ATTRIBUTE1, SERVER_ATTRIBUTE1) VALUES (1, 'xxx', 'yyy')"
argument_list|)
argument_list|)
expr_stmt|;
name|setupContext
operator|.
name|performQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|,
literal|"INSERT INTO MT_TABLE1 (TABLE1_ID, GLOBAL_ATTRIBUTE1, SERVER_ATTRIBUTE1, SUBCLASS_ATTRIBUTE1) VALUES (2, 'sub1', 'zzz', 'sa1')"
argument_list|)
argument_list|)
expr_stmt|;
name|setupContext
operator|.
name|performQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|,
literal|"INSERT INTO MT_TABLE1 (TABLE1_ID, GLOBAL_ATTRIBUTE1, SERVER_ATTRIBUTE1) VALUES (3, '1111', 'aaa')"
argument_list|)
argument_list|)
expr_stmt|;
name|CayenneContext
name|context
init|=
name|createClientContext
argument_list|()
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1Subclass
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ClientMtTable1Subclass
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sa1"
argument_list|,
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getSubclassAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPerformQueryInheritanceSuper
parameter_list|()
block|{
name|ObjectContext
name|setupContext
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|setupContext
operator|.
name|performQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|,
literal|"INSERT INTO MT_TABLE1 (TABLE1_ID, GLOBAL_ATTRIBUTE1, SERVER_ATTRIBUTE1) VALUES (1, 'a', 'yyy')"
argument_list|)
argument_list|)
expr_stmt|;
name|setupContext
operator|.
name|performQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|,
literal|"INSERT INTO MT_TABLE1 (TABLE1_ID, GLOBAL_ATTRIBUTE1, SERVER_ATTRIBUTE1, SUBCLASS_ATTRIBUTE1) VALUES (2, 'sub1', 'zzz', 'sa1')"
argument_list|)
argument_list|)
expr_stmt|;
name|setupContext
operator|.
name|performQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|,
literal|"INSERT INTO MT_TABLE1 (TABLE1_ID, GLOBAL_ATTRIBUTE1, SERVER_ATTRIBUTE1) VALUES (3, 'z', 'aaa')"
argument_list|)
argument_list|)
expr_stmt|;
name|CayenneContext
name|context
init|=
name|createClientContext
argument_list|()
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ClientMtTable1
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sa1"
argument_list|,
operator|(
operator|(
name|ClientMtTable1Subclass
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|getSubclassAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPerformQueryWithQualifierInheritanceSuper
parameter_list|()
block|{
name|ObjectContext
name|setupContext
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|setupContext
operator|.
name|performQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|,
literal|"INSERT INTO MT_TABLE1 (TABLE1_ID, GLOBAL_ATTRIBUTE1, SERVER_ATTRIBUTE1) VALUES (1, 'a', 'XX')"
argument_list|)
argument_list|)
expr_stmt|;
name|setupContext
operator|.
name|performQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|,
literal|"INSERT INTO MT_TABLE1 (TABLE1_ID, GLOBAL_ATTRIBUTE1, SERVER_ATTRIBUTE1, SUBCLASS_ATTRIBUTE1) VALUES (2, 'sub1', 'XXA', 'sa1')"
argument_list|)
argument_list|)
expr_stmt|;
name|setupContext
operator|.
name|performQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|MtTable1
operator|.
name|class
argument_list|,
literal|"INSERT INTO MT_TABLE1 (TABLE1_ID, GLOBAL_ATTRIBUTE1, SERVER_ATTRIBUTE1) VALUES (3, 'z', 'MM')"
argument_list|)
argument_list|)
expr_stmt|;
name|CayenneContext
name|context
init|=
name|createClientContext
argument_list|()
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
name|ClientMtTable1
operator|.
name|SERVER_ATTRIBUTE1_PROPERTY
argument_list|,
literal|"X%"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ClientMtTable1
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sa1"
argument_list|,
operator|(
operator|(
name|ClientMtTable1Subclass
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|getSubclassAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

