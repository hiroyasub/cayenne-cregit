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
name|DataRow
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
name|ObjectIdQuery
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
name|testdo
operator|.
name|meaningful_pk
operator|.
name|MeaningfulPKDep
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
name|meaningful_pk
operator|.
name|MeaningfulPKTest1
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
name|meaningful_pk
operator|.
name|MeaningfulPk
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
name|meaningful_pk
operator|.
name|MeaningfulPkDep2
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
name|meaningful_pk
operator|.
name|MeaningfulPkTest2
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
name|Ignore
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|*
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|MEANINGFUL_PK_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextEntityWithMeaningfulPKIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testInsertWithMeaningfulPK
parameter_list|()
block|{
name|MeaningfulPKTest1
name|obj
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setPkAttribute
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setDescr
argument_list|(
literal|"aaa-aaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectId
name|objId
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"MeaningfulPKTest1"
argument_list|,
name|MeaningfulPKTest1
operator|.
name|PK_ATTRIBUTE_PK_COLUMN
argument_list|,
literal|1000
argument_list|)
decl_stmt|;
name|ObjectIdQuery
name|q
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|objId
argument_list|,
literal|true
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE_REFRESH
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|DataRow
argument_list|>
name|result
init|=
operator|(
name|List
argument_list|<
name|DataRow
argument_list|>
operator|)
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
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1000
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
name|MeaningfulPKTest1
operator|.
name|PK_ATTRIBUTE_PK_COLUMN
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGeneratedKey
parameter_list|()
block|{
name|MeaningfulPKTest1
name|obj
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setDescr
argument_list|(
literal|"aaa-aaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertNotEquals
argument_list|(
literal|0
argument_list|,
name|obj
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|obj
argument_list|,
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|MeaningfulPKTest1
operator|.
name|class
argument_list|,
name|obj
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|id
init|=
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|obj
argument_list|)
decl_stmt|;
name|Map
name|snapshot
init|=
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|obj
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|snapshot
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|snapshot
operator|.
name|containsKey
argument_list|(
name|MeaningfulPKTest1
operator|.
name|PK_ATTRIBUTE_PK_COLUMN
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|snapshot
operator|.
name|get
argument_list|(
name|MeaningfulPKTest1
operator|.
name|PK_ATTRIBUTE_PK_COLUMN
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testChangeKey
parameter_list|()
block|{
name|MeaningfulPKTest1
name|obj
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setPkAttribute
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setDescr
argument_list|(
literal|"aaa-aaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|obj
operator|.
name|setPkAttribute
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// assert that object id got fixed
name|ObjectId
name|id
init|=
name|obj
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2000
argument_list|,
name|id
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
literal|"PK_ATTRIBUTE"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToManyRelationshipWithMeaningfulPK1
parameter_list|()
block|{
name|MeaningfulPKTest1
name|obj
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setPkAttribute
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setDescr
argument_list|(
literal|"aaa-aaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must be able to resolve to-many relationship
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MeaningfulPKTest1
argument_list|>
name|objects
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|MeaningfulPKTest1
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
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|obj
operator|=
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|obj
operator|.
name|getMeaningfulPKDepArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToManyRelationshipWithMeaningfulPK2
parameter_list|()
block|{
name|MeaningfulPKTest1
name|obj
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setPkAttribute
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setDescr
argument_list|(
literal|"aaa-aaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must be able to set reverse relationship
name|MeaningfulPKDep
name|dep
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKDep
operator|.
name|class
argument_list|)
decl_stmt|;
name|dep
operator|.
name|setToMeaningfulPK
argument_list|(
name|obj
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
name|testGeneratedIntegerPK
parameter_list|()
block|{
name|MeaningfulPkTest2
name|obj1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPkTest2
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj1
operator|.
name|setIntegerAttribute
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|MeaningfulPkTest2
name|obj2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPkTest2
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj2
operator|.
name|setIntegerAttribute
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MeaningfulPkTest2
argument_list|>
name|objects
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|MeaningfulPkTest2
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
argument_list|,
name|obj1
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
argument_list|,
name|obj2
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|obj1
operator|.
name|getPkAttribute
argument_list|()
argument_list|,
name|obj2
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMeaningfulIntegerPK
parameter_list|()
block|{
name|MeaningfulPkTest2
name|obj1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPkTest2
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj1
operator|.
name|setIntegerAttribute
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|obj1
operator|.
name|setPkAttribute
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|MeaningfulPkTest2
name|obj2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPkTest2
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj2
operator|.
name|setIntegerAttribute
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|obj2
operator|.
name|setPkAttribute
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MeaningfulPkTest2
argument_list|>
name|objects
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|MeaningfulPkTest2
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|,
name|obj1
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|,
name|obj2
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGeneratedIntPK
parameter_list|()
block|{
name|MeaningfulPKTest1
name|obj1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj1
operator|.
name|setIntAttribute
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|MeaningfulPKTest1
name|obj2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj2
operator|.
name|setIntAttribute
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MeaningfulPKTest1
argument_list|>
name|objects
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|MeaningfulPKTest1
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
literal|0
argument_list|,
name|obj1
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
literal|0
argument_list|,
name|obj2
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|obj1
operator|.
name|getPkAttribute
argument_list|()
argument_list|,
name|obj2
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMeaningfulIntPK
parameter_list|()
block|{
name|MeaningfulPKTest1
name|obj1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj1
operator|.
name|setIntAttribute
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|obj1
operator|.
name|setPkAttribute
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|MeaningfulPKTest1
name|obj2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj2
operator|.
name|setIntAttribute
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|obj2
operator|.
name|setPkAttribute
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MeaningfulPKTest1
argument_list|>
name|objects
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|MeaningfulPKTest1
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
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|obj1
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|obj2
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Insert will fail"
argument_list|)
specifier|public
name|void
name|testInsertDelete
parameter_list|()
block|{
name|MeaningfulPk
name|pkObj
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPk
operator|.
name|class
argument_list|)
decl_stmt|;
name|pkObj
operator|.
name|setPk
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObject
argument_list|(
name|pkObj
argument_list|)
expr_stmt|;
name|MeaningfulPk
name|pkObj2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPk
operator|.
name|class
argument_list|)
decl_stmt|;
name|pkObj2
operator|.
name|setPk
argument_list|(
literal|"123"
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
annotation|@
name|Ignore
specifier|public
name|void
name|test_MeaningfulPkInsertDeleteCascade
parameter_list|()
block|{
comment|// setup
name|MeaningfulPKTest1
name|obj
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setPkAttribute
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setDescr
argument_list|(
literal|"aaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must be able to set reverse relationship
name|MeaningfulPKDep
name|dep
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKDep
operator|.
name|class
argument_list|)
decl_stmt|;
name|dep
operator|.
name|setToMeaningfulPK
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|dep
operator|.
name|setPk
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// test
name|context
operator|.
name|deleteObject
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|MeaningfulPKTest1
name|obj2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|obj2
operator|.
name|setPkAttribute
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|obj2
operator|.
name|setDescr
argument_list|(
literal|"bbb"
argument_list|)
expr_stmt|;
name|MeaningfulPKDep
name|dep2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPKDep
operator|.
name|class
argument_list|)
decl_stmt|;
name|dep2
operator|.
name|setToMeaningfulPK
argument_list|(
name|obj2
argument_list|)
expr_stmt|;
name|dep2
operator|.
name|setPk
argument_list|(
literal|10
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
name|testMeaningfulFKToOneInvalidate
parameter_list|()
block|{
name|MeaningfulPk
name|pk
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPk
operator|.
name|class
argument_list|)
decl_stmt|;
name|MeaningfulPkDep2
name|dep
init|=
name|context
operator|.
name|newObject
argument_list|(
name|MeaningfulPkDep2
operator|.
name|class
argument_list|)
decl_stmt|;
name|dep
operator|.
name|setMeaningfulPk
argument_list|(
name|pk
argument_list|)
expr_stmt|;
name|dep
operator|.
name|setDescr
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|ObjectContext
name|childContext
init|=
name|runtime
operator|.
name|newContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MeaningfulPkDep2
name|depChild
init|=
name|childContext
operator|.
name|localObject
argument_list|(
name|dep
argument_list|)
decl_stmt|;
name|depChild
operator|.
name|setDescr
argument_list|(
literal|"test2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test2"
argument_list|,
name|depChild
operator|.
name|getDescr
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|depChild
operator|.
name|getMeaningfulPk
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|depChild
operator|.
name|getMeaningfulPk
argument_list|()
operator|.
name|getPk
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

