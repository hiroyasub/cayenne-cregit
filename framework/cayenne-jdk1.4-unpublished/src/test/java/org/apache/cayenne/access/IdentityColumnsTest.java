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
name|org
operator|.
name|apache
operator|.
name|art
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
name|art
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
name|art
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
name|art
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
name|art
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
name|art
operator|.
name|MeaningfulGeneratedColumnTestEntity
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
name|DataObjectUtils
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|IdentityColumnsTest
extends|extends
name|CayenneCase
block|{
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
operator|(
name|GeneratedColumnTestEntity
operator|)
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
name|DataObjectUtils
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
operator|(
name|GeneratedColumnTestEntity
operator|)
name|DataObjectUtils
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
operator|(
name|GeneratedColumnTestEntity
operator|)
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
operator|(
name|GeneratedColumnDep
operator|)
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
operator|new
name|Object
index|[]
block|{
name|m
block|,
name|d
block|}
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
operator|(
name|GeneratedColumnTestEntity
operator|)
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
operator|(
name|GeneratedColumnTest2
operator|)
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
operator|(
name|GeneratedColumnTestEntity
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|)
block|,
operator|(
name|GeneratedColumnTestEntity
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|GeneratedColumnTestEntity
operator|.
name|class
argument_list|)
block|,
operator|(
name|GeneratedColumnTestEntity
operator|)
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
name|DataObjectUtils
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
operator|(
name|GeneratedColumnTestEntity
operator|)
name|DataObjectUtils
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
operator|(
name|GeneratedColumnCompMaster
operator|)
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
operator|(
name|GeneratedColumnCompKey
operator|)
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
operator|(
name|GeneratedColumnCompKey
operator|)
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
name|DataObjectUtils
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
operator|new
name|Object
index|[]
block|{
name|master
block|,
name|dep1
block|,
name|dep2
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|fetchedDep2
init|=
name|DataObjectUtils
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
operator|(
name|GeneratedColumnTestEntity
operator|)
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
operator|(
name|GeneratedColumnDep
operator|)
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
operator|(
name|GeneratedColumnTestEntity
operator|)
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
name|DataObjectUtils
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
name|DataObjectUtils
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
operator|new
name|Object
index|[]
block|{
name|master2
block|,
name|dependent
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|DataObjectUtils
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
name|DataObjectUtils
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
operator|(
name|GeneratedColumnTestEntity
operator|)
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
operator|(
name|GeneratedColumnDep
operator|)
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
name|DataObjectUtils
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
name|DataObjectUtils
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
operator|new
name|Object
index|[]
block|{
name|idObject
block|,
name|dependent
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|DataObjectUtils
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
name|DataObjectUtils
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

