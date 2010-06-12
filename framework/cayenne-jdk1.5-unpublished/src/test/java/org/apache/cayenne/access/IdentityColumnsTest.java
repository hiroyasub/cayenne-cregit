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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|ObjectId
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
name|Persistent
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
name|map
operator|.
name|DbAttribute
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
name|map
operator|.
name|DbEntity
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
name|GeneratedColumnCompKey
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
name|GeneratedColumnCompMaster
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
name|GeneratedColumnDep
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
name|GeneratedColumnTest2
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
name|GeneratedColumnTestEntity
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
name|GeneratedF1
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
name|GeneratedF2
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|IdentityColumnsTest
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
comment|/**      * Tests a bug casued by the ID Java type mismatch vs the default JDBC type of the ID      * column.      */
specifier|public
name|void
name|testCAY823
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|GeneratedColumnTestEntity
name|idObject
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|name
init|=
literal|"n_"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|idObject
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|idObject
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectId
name|id
init|=
name|idObject
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|idObject
argument_list|)
argument_list|)
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|setPageSize
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// per CAY-823 an attempt to resolve an object results in an exception
name|assertEquals
argument_list|(
name|id
argument_list|,
operator|(
operator|(
name|Persistent
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNewObject
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|GeneratedColumnTestEntity
name|idObject
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|name
init|=
literal|"n_"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|idObject
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|idObject
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// this will throw an exception if id wasn't generated one way or another
name|int
name|id
init|=
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|idObject
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|id
operator|>=
literal|0
argument_list|)
expr_stmt|;
comment|// make sure that id is the same as id in the DB
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|idObject
argument_list|)
argument_list|)
expr_stmt|;
name|GeneratedColumnTestEntity
name|object
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|name
argument_list|,
name|object
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGeneratedJoinInFlattenedRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|joinTableName
init|=
literal|"GENERATED_JOIN"
decl_stmt|;
name|TableHelper
name|joinTable
init|=
operator|new
name|TableHelper
argument_list|(
name|getDbHelper
argument_list|()
argument_list|,
name|joinTableName
argument_list|)
decl_stmt|;
name|joinTable
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
comment|// before saving objects, let's manually access PKGenerator to get a base PK value
comment|// for comparison
name|DbEntity
name|joinTableEntity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
name|joinTableName
argument_list|)
decl_stmt|;
name|DbAttribute
name|pkAttribute
init|=
operator|(
name|DbAttribute
operator|)
name|joinTableEntity
operator|.
name|getAttribute
argument_list|(
literal|"ID"
argument_list|)
decl_stmt|;
name|Number
name|pk
init|=
operator|(
name|Number
operator|)
name|getNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|generatePk
argument_list|(
name|getNode
argument_list|()
argument_list|,
name|pkAttribute
argument_list|)
decl_stmt|;
name|GeneratedF1
name|f1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedF1
operator|.
name|class
argument_list|)
decl_stmt|;
name|GeneratedF2
name|f2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedF2
operator|.
name|class
argument_list|)
decl_stmt|;
name|f1
operator|.
name|addToF2
argument_list|(
name|f2
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
name|joinTable
operator|.
name|getInt
argument_list|(
literal|"ID"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|id
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// this is a leap of faith that autoincrement-based IDs will not match
comment|// PkGenertor provided ids... This sorta works though if pk generator has a 200
comment|// base value
if|if
condition|(
name|getNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
operator|.
name|supportsGeneratedKeys
argument_list|()
condition|)
block|{
name|assertFalse
argument_list|(
literal|"Looks like auto-increment wasn't used for the join table. ID: "
operator|+
name|id
argument_list|,
name|id
operator|==
name|pk
operator|.
name|intValue
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
name|id
argument_list|,
name|pk
operator|.
name|intValue
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Tests CAY-422 bug.      */
specifier|public
name|void
name|testUnrelatedUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|GeneratedColumnTestEntity
name|m
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|m
operator|.
name|setName
argument_list|(
literal|"m"
argument_list|)
expr_stmt|;
name|GeneratedColumnDep
name|d
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnDep
operator|.
name|class
argument_list|)
decl_stmt|;
name|d
operator|.
name|setName
argument_list|(
literal|"d"
argument_list|)
expr_stmt|;
name|d
operator|.
name|setToMaster
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|m
argument_list|,
name|d
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|prepareForAccess
argument_list|(
name|d
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// this line caused CAY-422 error
name|d
operator|.
name|getToMaster
argument_list|()
expr_stmt|;
name|d
operator|.
name|setName
argument_list|(
literal|"new name"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
comment|/**      * Tests that insert in two tables with identity pk does not generate a conflict. See      * CAY-341 for the original bug.      */
specifier|public
name|void
name|testMultipleNewObjectsSeparateTables
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|GeneratedColumnTestEntity
name|idObject1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|idObject1
operator|.
name|setName
argument_list|(
literal|"o1"
argument_list|)
expr_stmt|;
name|GeneratedColumnTest2
name|idObject2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnTest2
operator|.
name|class
argument_list|)
decl_stmt|;
name|idObject2
operator|.
name|setName
argument_list|(
literal|"o2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testMultipleNewObjects
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|String
index|[]
name|names
init|=
operator|new
name|String
index|[]
block|{
literal|"n1_"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
block|,
literal|"n2_"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
block|,
literal|"n3_"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
block|}
decl_stmt|;
name|GeneratedColumnTestEntity
index|[]
name|idObjects
init|=
operator|new
name|GeneratedColumnTestEntity
index|[]
block|{
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|)
block|,
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|)
block|,
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|)
block|}
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|idObjects
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|idObjects
index|[
name|i
index|]
operator|.
name|setName
argument_list|(
name|names
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|int
index|[]
name|ids
init|=
operator|new
name|int
index|[
name|idObjects
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|idObjects
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ids
index|[
name|i
index|]
operator|=
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|idObjects
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ids
index|[
name|i
index|]
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|idObjects
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ids
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|GeneratedColumnTestEntity
name|object
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|,
name|ids
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|names
index|[
name|i
index|]
argument_list|,
name|object
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testCompoundPKWithGeneratedColumn
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|getAccessStackAdapter
argument_list|()
operator|.
name|getAdapter
argument_list|()
operator|.
name|supportsGeneratedKeys
argument_list|()
condition|)
block|{
comment|// only works for generated keys, as the entity tested has one Cayenne
comment|// auto-pk and one generated key
name|String
name|masterName
init|=
literal|"m_"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|String
name|depName1
init|=
literal|"dep1_"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|String
name|depName2
init|=
literal|"dep2_"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|GeneratedColumnCompMaster
name|master
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnCompMaster
operator|.
name|class
argument_list|)
decl_stmt|;
name|master
operator|.
name|setName
argument_list|(
name|masterName
argument_list|)
expr_stmt|;
name|GeneratedColumnCompKey
name|dep1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnCompKey
operator|.
name|class
argument_list|)
decl_stmt|;
name|dep1
operator|.
name|setName
argument_list|(
name|depName1
argument_list|)
expr_stmt|;
name|dep1
operator|.
name|setToMaster
argument_list|(
name|master
argument_list|)
expr_stmt|;
name|GeneratedColumnCompKey
name|dep2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnCompKey
operator|.
name|class
argument_list|)
decl_stmt|;
name|dep2
operator|.
name|setName
argument_list|(
name|depName2
argument_list|)
expr_stmt|;
name|dep2
operator|.
name|setToMaster
argument_list|(
name|master
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|int
name|masterId
init|=
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|master
argument_list|)
decl_stmt|;
name|ObjectId
name|id2
init|=
name|dep2
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
comment|// check propagated id
name|Number
name|propagatedID2
init|=
operator|(
name|Number
operator|)
name|id2
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
name|GeneratedColumnCompKey
operator|.
name|PROPAGATED_PK_PK_COLUMN
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|propagatedID2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|masterId
argument_list|,
name|propagatedID2
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// check Cayenne-generated ID
name|Number
name|cayenneGeneratedID2
init|=
operator|(
name|Number
operator|)
name|id2
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
name|GeneratedColumnCompKey
operator|.
name|AUTO_PK_PK_COLUMN
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cayenneGeneratedID2
argument_list|)
expr_stmt|;
comment|// check DB-generated ID
name|Number
name|dbGeneratedID2
init|=
operator|(
name|Number
operator|)
name|id2
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
name|GeneratedColumnCompKey
operator|.
name|GENERATED_COLUMN_PK_COLUMN
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dbGeneratedID2
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|master
argument_list|,
name|dep1
argument_list|,
name|dep2
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|fetchedDep2
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|id2
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|fetchedDep2
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testUpdateDependentWithNewMaster
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|GeneratedColumnTestEntity
name|master1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|master1
operator|.
name|setName
argument_list|(
literal|"aaa"
argument_list|)
expr_stmt|;
name|GeneratedColumnDep
name|dependent
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnDep
operator|.
name|class
argument_list|)
decl_stmt|;
name|dependent
operator|.
name|setName
argument_list|(
literal|"aaa"
argument_list|)
expr_stmt|;
name|dependent
operator|.
name|setToMaster
argument_list|(
name|master1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// change master
name|GeneratedColumnTestEntity
name|master2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|master2
operator|.
name|setName
argument_list|(
literal|"bbb"
argument_list|)
expr_stmt|;
comment|// TESTING THIS
name|dependent
operator|.
name|setToMaster
argument_list|(
name|master2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|int
name|id1
init|=
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|master2
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|id1
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|int
name|id2
init|=
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|dependent
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|id2
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id1
argument_list|,
name|id2
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|master2
argument_list|,
name|dependent
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|,
name|id1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|GeneratedColumnDep
operator|.
name|class
argument_list|,
name|id2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGeneratedDefaultValue
parameter_list|()
throws|throws
name|Exception
block|{
comment|// fail("TODO: test insert with DEFAULT generated column...need custom SQL to
comment|// build such table");
block|}
specifier|public
name|void
name|testPropagateToDependent
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|GeneratedColumnTestEntity
name|idObject
init|=
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|idObject
operator|.
name|setName
argument_list|(
literal|"aaa"
argument_list|)
expr_stmt|;
name|GeneratedColumnDep
name|dependent
init|=
name|idObject
operator|.
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|GeneratedColumnDep
operator|.
name|class
argument_list|)
decl_stmt|;
name|dependent
operator|.
name|setName
argument_list|(
literal|"aaa"
argument_list|)
expr_stmt|;
name|dependent
operator|.
name|setToMaster
argument_list|(
name|idObject
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// this will throw an exception if id wasn't generated
name|int
name|id1
init|=
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|idObject
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|id1
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|int
name|id2
init|=
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|dependent
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|id2
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id1
argument_list|,
name|id2
argument_list|)
expr_stmt|;
comment|// refetch from DB
name|context
operator|.
name|invalidateObjects
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|idObject
argument_list|,
name|dependent
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|,
name|id1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|GeneratedColumnDep
operator|.
name|class
argument_list|,
name|id2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

