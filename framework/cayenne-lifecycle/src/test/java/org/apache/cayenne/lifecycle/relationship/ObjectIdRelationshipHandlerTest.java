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
name|lifecycle
operator|.
name|relationship
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
name|Cayenne
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
name|ObjectContext
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|lifecycle
operator|.
name|db
operator|.
name|E1
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
name|lifecycle
operator|.
name|db
operator|.
name|UuidRoot1
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
name|lifecycle
operator|.
name|id
operator|.
name|IdCoder
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|test
operator|.
name|jdbc
operator|.
name|TableHelper
import|;
end_import

begin_class
specifier|public
class|class
name|ObjectIdRelationshipHandlerTest
extends|extends
name|TestCase
block|{
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
specifier|private
name|TableHelper
name|rootTable
decl_stmt|;
specifier|private
name|TableHelper
name|e1Table
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|runtime
operator|=
operator|new
name|ServerRuntime
argument_list|(
literal|"cayenne-lifecycle.xml"
argument_list|)
expr_stmt|;
comment|// a filter is required to invalidate root objects after commit
name|ObjectIdRelationshipFilter
name|filter
init|=
operator|new
name|ObjectIdRelationshipFilter
argument_list|()
decl_stmt|;
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
operator|.
name|addListener
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|DBHelper
name|dbHelper
init|=
operator|new
name|DBHelper
argument_list|(
name|runtime
operator|.
name|getDataSource
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|rootTable
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"UUID_ROOT1"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"UUID"
argument_list|)
expr_stmt|;
name|rootTable
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|e1Table
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"E1"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|e1Table
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|runtime
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testRelate_Existing
parameter_list|()
throws|throws
name|Exception
block|{
name|e1Table
operator|.
name|insert
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|E1
name|e1
init|=
operator|(
name|E1
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
operator|new
name|SelectQuery
argument_list|(
name|E1
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|UuidRoot1
name|r1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|UuidRoot1
operator|.
name|class
argument_list|)
decl_stmt|;
name|IdCoder
name|refHandler
init|=
operator|new
name|IdCoder
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectIdRelationshipHandler
name|handler
init|=
operator|new
name|ObjectIdRelationshipHandler
argument_list|(
name|refHandler
argument_list|)
decl_stmt|;
name|handler
operator|.
name|relate
argument_list|(
name|r1
argument_list|,
name|e1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"E1:1"
argument_list|,
name|r1
operator|.
name|getUuid
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e1
argument_list|,
name|r1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"cay:related:uuid"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Object
index|[]
name|r1x
init|=
name|rootTable
operator|.
name|select
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"E1:1"
argument_list|,
name|r1x
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRelate_New
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|E1
name|e1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|E1
operator|.
name|class
argument_list|)
decl_stmt|;
name|UuidRoot1
name|r1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|UuidRoot1
operator|.
name|class
argument_list|)
decl_stmt|;
name|IdCoder
name|refHandler
init|=
operator|new
name|IdCoder
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectIdRelationshipHandler
name|handler
init|=
operator|new
name|ObjectIdRelationshipHandler
argument_list|(
name|refHandler
argument_list|)
decl_stmt|;
name|handler
operator|.
name|relate
argument_list|(
name|r1
argument_list|,
name|e1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e1
argument_list|,
name|r1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"cay:related:uuid"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|int
name|id
init|=
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|e1
argument_list|)
decl_stmt|;
name|Object
index|[]
name|r1x
init|=
name|rootTable
operator|.
name|select
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"E1:"
operator|+
name|id
argument_list|,
name|r1x
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"E1:"
operator|+
name|id
argument_list|,
name|r1
operator|.
name|getUuid
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRelate_Change
parameter_list|()
throws|throws
name|Exception
block|{
name|e1Table
operator|.
name|insert
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|rootTable
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"E1:1"
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|UuidRoot1
name|r1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|UuidRoot1
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"E1:1"
argument_list|,
name|r1
operator|.
name|getUuid
argument_list|()
argument_list|)
expr_stmt|;
name|E1
name|e1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|E1
operator|.
name|class
argument_list|)
decl_stmt|;
name|IdCoder
name|refHandler
init|=
operator|new
name|IdCoder
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectIdRelationshipHandler
name|handler
init|=
operator|new
name|ObjectIdRelationshipHandler
argument_list|(
name|refHandler
argument_list|)
decl_stmt|;
name|handler
operator|.
name|relate
argument_list|(
name|r1
argument_list|,
name|e1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e1
argument_list|,
name|r1
operator|.
name|readPropertyDirectly
argument_list|(
literal|"cay:related:uuid"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|int
name|id
init|=
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|e1
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|1
operator|==
name|id
argument_list|)
expr_stmt|;
name|Object
index|[]
name|r1x
init|=
name|rootTable
operator|.
name|select
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"E1:"
operator|+
name|id
argument_list|,
name|r1x
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"E1:"
operator|+
name|id
argument_list|,
name|r1
operator|.
name|getUuid
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e1
argument_list|,
name|r1
operator|.
name|readProperty
argument_list|(
literal|"cay:related:uuid"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
