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
name|access
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
name|UuidPkEntity
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
name|UuidTestEntity
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
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|UUIDIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|private
name|TableHelper
name|uuidPkEntity
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"UUID_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"UUID_PK_ENTITY"
argument_list|)
expr_stmt|;
name|uuidPkEntity
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"UUID_PK_ENTITY"
argument_list|,
literal|"ID"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUUID
parameter_list|()
throws|throws
name|Exception
block|{
name|UuidTestEntity
name|test
init|=
name|context
operator|.
name|newObject
argument_list|(
name|UuidTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|UUID
name|id
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
decl_stmt|;
name|test
operator|.
name|setUuid
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|UuidTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|UuidTestEntity
name|testRead
init|=
operator|(
name|UuidTestEntity
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|testRead
operator|.
name|getUuid
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|testRead
operator|.
name|getUuid
argument_list|()
argument_list|)
expr_stmt|;
name|test
operator|.
name|setUuid
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUUIDMeaningfulPkInsert
parameter_list|()
throws|throws
name|Exception
block|{
name|UUID
name|id
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
decl_stmt|;
name|UuidPkEntity
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|UuidPkEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|o1
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|String
name|fetched
init|=
name|uuidPkEntity
operator|.
name|getString
argument_list|(
literal|"ID"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|UUID
operator|.
name|fromString
argument_list|(
name|fetched
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUUIDMeaningfulPkSelect
parameter_list|()
throws|throws
name|Exception
block|{
name|UUID
name|id
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
decl_stmt|;
name|uuidPkEntity
operator|.
name|insert
argument_list|(
name|id
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|UuidPkEntity
name|o1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|UuidPkEntity
operator|.
name|class
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|o1
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|o1
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
literal|"ID"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

