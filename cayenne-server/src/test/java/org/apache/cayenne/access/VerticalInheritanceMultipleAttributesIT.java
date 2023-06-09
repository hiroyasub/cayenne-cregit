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
name|access
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

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
name|inheritance_vertical
operator|.
name|IvImpl
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
name|inheritance_vertical
operator|.
name|IvOther
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
name|Before
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
name|assertNull
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

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|INHERITANCE_VERTICAL_PROJECT
argument_list|)
specifier|public
class|class
name|VerticalInheritanceMultipleAttributesIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ServerRuntime
name|runtime
decl_stmt|;
name|TableHelper
name|ivOtherTable
decl_stmt|,
name|ivBaseTable
decl_stmt|,
name|ivImplTable
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setupTableHelpers
parameter_list|()
throws|throws
name|Exception
block|{
name|ivOtherTable
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"IV_OTHER"
argument_list|)
expr_stmt|;
name|ivOtherTable
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"NAME"
argument_list|)
operator|.
name|setColumnTypes
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|)
expr_stmt|;
name|ivBaseTable
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"IV_BASE"
argument_list|)
expr_stmt|;
name|ivBaseTable
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"NAME"
argument_list|,
literal|"TYPE"
argument_list|)
operator|.
name|setColumnTypes
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|Types
operator|.
name|CHAR
argument_list|)
expr_stmt|;
name|ivImplTable
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"IV_IMPL"
argument_list|)
expr_stmt|;
name|ivImplTable
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"ATTR1"
argument_list|,
literal|"ATTR2"
argument_list|,
literal|"OTHER1_ID"
argument_list|,
literal|"OTHER2_ID"
argument_list|)
operator|.
name|setColumnTypes
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|)
expr_stmt|;
name|ivImplTable
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|ivBaseTable
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|ivOtherTable
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
block|}
comment|/**      * @link https://issues.apache.org/jira/browse/CAY-2282      */
annotation|@
name|Test
specifier|public
name|void
name|testUpdateTwoObjects
parameter_list|()
throws|throws
name|SQLException
block|{
comment|// Insert records we want to update
name|ivOtherTable
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"other1"
argument_list|)
expr_stmt|;
name|ivOtherTable
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"other2"
argument_list|)
expr_stmt|;
name|ivBaseTable
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"Impl 1"
argument_list|,
literal|"I"
argument_list|)
expr_stmt|;
name|ivBaseTable
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"Impl 2"
argument_list|,
literal|"I"
argument_list|)
expr_stmt|;
name|ivImplTable
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"attr1"
argument_list|,
literal|"attr2"
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ivImplTable
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"attr1"
argument_list|,
literal|"attr2"
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|// Fetch and update the records
name|IvOther
name|other1
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvOther
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|IvOther
operator|.
name|NAME
operator|.
name|eq
argument_list|(
literal|"other1"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|IvOther
name|other2
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvOther
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|IvOther
operator|.
name|NAME
operator|.
name|eq
argument_list|(
literal|"other2"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|IvImpl
argument_list|>
name|implResult
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|implResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|IvImpl
name|record
range|:
name|implResult
control|)
block|{
name|record
operator|.
name|setName
argument_list|(
name|record
operator|.
name|getName
argument_list|()
operator|+
literal|"-Change"
argument_list|)
expr_stmt|;
name|record
operator|.
name|setAttr1
argument_list|(
name|record
operator|.
name|getAttr1
argument_list|()
operator|+
literal|"-Change"
argument_list|)
expr_stmt|;
name|record
operator|.
name|setAttr2
argument_list|(
name|record
operator|.
name|getAttr2
argument_list|()
operator|+
literal|"-Change"
argument_list|)
expr_stmt|;
name|record
operator|.
name|setOther1
argument_list|(
name|other2
argument_list|)
expr_stmt|;
name|record
operator|.
name|setOther2
argument_list|(
name|other1
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// Check result via clean context
name|ObjectContext
name|cleanContext
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|implResult
operator|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
operator|.
name|select
argument_list|(
name|cleanContext
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|implResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|IvImpl
name|record
range|:
name|implResult
control|)
block|{
name|assertTrue
argument_list|(
name|record
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"-Change"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|record
operator|.
name|getAttr1
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"-Change"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|record
operator|.
name|getAttr2
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"-Change"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|other2
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|record
operator|.
name|getOther1
argument_list|()
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|other1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|record
operator|.
name|getOther2
argument_list|()
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateObjectsWithData
parameter_list|()
throws|throws
name|SQLException
block|{
name|ivOtherTable
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"other1"
argument_list|)
expr_stmt|;
name|ivOtherTable
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"other2"
argument_list|)
expr_stmt|;
name|IvOther
name|other1
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvOther
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|IvOther
operator|.
name|NAME
operator|.
name|eq
argument_list|(
literal|"other1"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|IvOther
name|other2
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvOther
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|IvOther
operator|.
name|NAME
operator|.
name|eq
argument_list|(
literal|"other2"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|IvImpl
name|impl1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|impl1
operator|.
name|setName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|impl1
operator|.
name|setAttr1
argument_list|(
literal|"attr1"
argument_list|)
expr_stmt|;
name|impl1
operator|.
name|setAttr2
argument_list|(
literal|"attr2"
argument_list|)
expr_stmt|;
name|impl1
operator|.
name|setOther1
argument_list|(
name|other1
argument_list|)
expr_stmt|;
name|impl1
operator|.
name|setOther2
argument_list|(
name|other2
argument_list|)
expr_stmt|;
name|IvImpl
name|impl2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|impl2
operator|.
name|setName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|impl2
operator|.
name|setAttr1
argument_list|(
literal|"attr1"
argument_list|)
expr_stmt|;
name|impl2
operator|.
name|setAttr2
argument_list|(
literal|"attr2"
argument_list|)
expr_stmt|;
name|impl2
operator|.
name|setOther1
argument_list|(
name|other1
argument_list|)
expr_stmt|;
name|impl2
operator|.
name|setOther2
argument_list|(
name|other2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// Check result via clean context
name|ObjectContext
name|cleanContext
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|IvImpl
argument_list|>
name|implResult
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
operator|.
name|select
argument_list|(
name|cleanContext
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|implResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|IvImpl
name|record
range|:
name|implResult
control|)
block|{
name|assertEquals
argument_list|(
literal|"name"
argument_list|,
name|record
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"attr1"
argument_list|,
name|record
operator|.
name|getAttr1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"attr2"
argument_list|,
name|record
operator|.
name|getAttr2
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|other1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|record
operator|.
name|getOther1
argument_list|()
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|other2
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|record
operator|.
name|getOther2
argument_list|()
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateEmptyObjects
parameter_list|()
throws|throws
name|SQLException
block|{
name|IvImpl
name|impl1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|impl1
operator|.
name|setName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|IvImpl
name|impl2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|impl2
operator|.
name|setName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|cleanContext
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|IvImpl
argument_list|>
name|implResult
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
operator|.
name|select
argument_list|(
name|cleanContext
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|implResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|IvImpl
name|record
range|:
name|implResult
control|)
block|{
name|assertEquals
argument_list|(
literal|"name"
argument_list|,
name|record
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|record
operator|.
name|getAttr1
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|record
operator|.
name|getAttr2
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|record
operator|.
name|getOther1
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|record
operator|.
name|getOther2
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateEmptyObjectsWithUpdate
parameter_list|()
throws|throws
name|SQLException
block|{
name|ivOtherTable
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"other1"
argument_list|)
expr_stmt|;
name|ivOtherTable
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"other2"
argument_list|)
expr_stmt|;
name|IvOther
name|other1
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvOther
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|IvOther
operator|.
name|NAME
operator|.
name|eq
argument_list|(
literal|"other1"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|IvOther
name|other2
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvOther
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|IvOther
operator|.
name|NAME
operator|.
name|eq
argument_list|(
literal|"other2"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|IvImpl
name|impl1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|impl1
operator|.
name|setName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|IvImpl
name|impl2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|impl2
operator|.
name|setName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|cleanContext
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|IvImpl
argument_list|>
name|implResult
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
operator|.
name|select
argument_list|(
name|cleanContext
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|implResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|IvImpl
name|record
range|:
name|implResult
control|)
block|{
name|assertEquals
argument_list|(
literal|"name"
argument_list|,
name|record
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|record
operator|.
name|getAttr1
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|record
operator|.
name|getAttr2
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|record
operator|.
name|getOther1
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|record
operator|.
name|getOther2
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|impl1
operator|.
name|setAttr1
argument_list|(
literal|"attr1"
argument_list|)
expr_stmt|;
name|impl1
operator|.
name|setAttr2
argument_list|(
literal|"attr2"
argument_list|)
expr_stmt|;
name|impl1
operator|.
name|setOther1
argument_list|(
name|other1
argument_list|)
expr_stmt|;
name|impl1
operator|.
name|setOther2
argument_list|(
name|other2
argument_list|)
expr_stmt|;
name|impl2
operator|.
name|setAttr1
argument_list|(
literal|"attr1"
argument_list|)
expr_stmt|;
name|impl2
operator|.
name|setAttr2
argument_list|(
literal|"attr2"
argument_list|)
expr_stmt|;
name|impl2
operator|.
name|setOther1
argument_list|(
name|other1
argument_list|)
expr_stmt|;
name|impl2
operator|.
name|setOther2
argument_list|(
name|other2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|cleanContext
operator|=
name|runtime
operator|.
name|newContext
argument_list|()
expr_stmt|;
name|implResult
operator|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
operator|.
name|select
argument_list|(
name|cleanContext
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|implResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|IvImpl
name|record
range|:
name|implResult
control|)
block|{
name|assertEquals
argument_list|(
literal|"name"
argument_list|,
name|record
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"attr1"
argument_list|,
name|record
operator|.
name|getAttr1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"attr2"
argument_list|,
name|record
operator|.
name|getAttr2
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|other1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|record
operator|.
name|getOther1
argument_list|()
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|other2
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|record
operator|.
name|getOther2
argument_list|()
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPartialCreateObjectsWithUpdate
parameter_list|()
throws|throws
name|SQLException
block|{
name|ivOtherTable
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"other1"
argument_list|)
expr_stmt|;
name|ivOtherTable
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"other2"
argument_list|)
expr_stmt|;
name|IvOther
name|other1
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvOther
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|IvOther
operator|.
name|NAME
operator|.
name|eq
argument_list|(
literal|"other1"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|IvOther
name|other2
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvOther
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|IvOther
operator|.
name|NAME
operator|.
name|eq
argument_list|(
literal|"other2"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|IvImpl
name|impl1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|impl1
operator|.
name|setName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|impl1
operator|.
name|setAttr1
argument_list|(
literal|"attr1"
argument_list|)
expr_stmt|;
name|IvImpl
name|impl2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|impl2
operator|.
name|setName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|impl2
operator|.
name|setAttr1
argument_list|(
literal|"attr1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|cleanContext
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|IvImpl
argument_list|>
name|implResult
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
operator|.
name|select
argument_list|(
name|cleanContext
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|implResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|IvImpl
name|record
range|:
name|implResult
control|)
block|{
name|assertEquals
argument_list|(
literal|"name"
argument_list|,
name|record
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"attr1"
argument_list|,
name|record
operator|.
name|getAttr1
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|record
operator|.
name|getAttr2
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|record
operator|.
name|getOther1
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|record
operator|.
name|getOther2
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|impl1
operator|.
name|setAttr1
argument_list|(
literal|"attr1"
argument_list|)
expr_stmt|;
name|impl1
operator|.
name|setAttr2
argument_list|(
literal|"attr2"
argument_list|)
expr_stmt|;
name|impl1
operator|.
name|setOther1
argument_list|(
name|other1
argument_list|)
expr_stmt|;
name|impl1
operator|.
name|setOther2
argument_list|(
name|other2
argument_list|)
expr_stmt|;
name|impl2
operator|.
name|setAttr1
argument_list|(
literal|"attr1"
argument_list|)
expr_stmt|;
name|impl2
operator|.
name|setAttr2
argument_list|(
literal|"attr2"
argument_list|)
expr_stmt|;
name|impl2
operator|.
name|setOther1
argument_list|(
name|other1
argument_list|)
expr_stmt|;
name|impl2
operator|.
name|setOther2
argument_list|(
name|other2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|cleanContext
operator|=
name|runtime
operator|.
name|newContext
argument_list|()
expr_stmt|;
name|implResult
operator|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
operator|.
name|select
argument_list|(
name|cleanContext
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|implResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|IvImpl
name|record
range|:
name|implResult
control|)
block|{
name|assertEquals
argument_list|(
literal|"name"
argument_list|,
name|record
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"attr1"
argument_list|,
name|record
operator|.
name|getAttr1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"attr2"
argument_list|,
name|record
operator|.
name|getAttr2
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|other1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|record
operator|.
name|getOther1
argument_list|()
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|other2
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|record
operator|.
name|getOther2
argument_list|()
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteObjects
parameter_list|()
throws|throws
name|SQLException
block|{
comment|// Insert records we want to update
name|ivOtherTable
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"other1"
argument_list|)
expr_stmt|;
name|ivOtherTable
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"other2"
argument_list|)
expr_stmt|;
name|ivBaseTable
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"Impl 1"
argument_list|,
literal|"I"
argument_list|)
expr_stmt|;
name|ivBaseTable
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"Impl 2"
argument_list|,
literal|"I"
argument_list|)
expr_stmt|;
name|ivImplTable
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"attr1"
argument_list|,
literal|"attr2"
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|ivImplTable
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"attr1"
argument_list|,
literal|"attr2"
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|IvImpl
argument_list|>
name|implResult
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|implResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|IvImpl
name|iv
range|:
name|implResult
control|)
block|{
name|context
operator|.
name|deleteObject
argument_list|(
name|iv
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0L
argument_list|,
name|ObjectSelect
operator|.
name|query
argument_list|(
name|IvImpl
operator|.
name|class
argument_list|)
operator|.
name|selectCount
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

