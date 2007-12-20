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
name|jpa
operator|.
name|bridge
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
name|jpa
operator|.
name|MockPersistenceUnitInfo
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
name|jpa
operator|.
name|bridge
operator|.
name|entity
operator|.
name|Entity1
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
name|jpa
operator|.
name|bridge
operator|.
name|entity
operator|.
name|Entity2
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
name|jpa
operator|.
name|bridge
operator|.
name|entity
operator|.
name|Entity3
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
name|jpa
operator|.
name|bridge
operator|.
name|entity
operator|.
name|Entity4
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
name|jpa
operator|.
name|conf
operator|.
name|EntityMapAnnotationLoader
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
name|jpa
operator|.
name|conf
operator|.
name|EntityMapDefaultsProcessor
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
name|jpa
operator|.
name|conf
operator|.
name|EntityMapLoaderContext
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
name|DataMap
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
name|map
operator|.
name|DbRelationship
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
name|ObjEntity
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
name|ObjRelationship
import|;
end_import

begin_class
specifier|public
class|class
name|DataMapConverterRelationshipsTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testBidiOM
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|dataMap2
init|=
name|load
argument_list|(
name|Entity2
operator|.
name|class
argument_list|,
name|Entity1
operator|.
name|class
argument_list|)
decl_stmt|;
name|bidiOMAssert
argument_list|(
name|dataMap2
argument_list|)
expr_stmt|;
comment|// note that CAY-860 problem (missing joins) is conditional on the entity load
comment|// order, so now reverse the order ...
name|DataMap
name|dataMap1
init|=
name|load
argument_list|(
name|Entity1
operator|.
name|class
argument_list|,
name|Entity2
operator|.
name|class
argument_list|)
decl_stmt|;
name|bidiOMAssert
argument_list|(
name|dataMap1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMapOM
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|dataMap
init|=
name|load
argument_list|(
name|Entity3
operator|.
name|class
argument_list|,
name|Entity4
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjEntity
name|e3
init|=
name|dataMap
operator|.
name|getObjEntity
argument_list|(
literal|"Entity3"
argument_list|)
decl_stmt|;
name|ObjEntity
name|e4
init|=
name|dataMap
operator|.
name|getObjEntity
argument_list|(
literal|"Entity4"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|e3
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|e4
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjRelationship
name|or3
init|=
operator|(
name|ObjRelationship
operator|)
name|e3
operator|.
name|getRelationship
argument_list|(
literal|"entity4"
argument_list|)
decl_stmt|;
name|ObjRelationship
name|or4
init|=
operator|(
name|ObjRelationship
operator|)
name|e4
operator|.
name|getRelationship
argument_list|(
literal|"entity3s"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"entity4"
argument_list|,
name|or3
operator|.
name|getDbRelationshipPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"entity3s"
argument_list|,
name|or4
operator|.
name|getDbRelationshipPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Entity4"
argument_list|,
name|or3
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Entity3"
argument_list|,
name|or4
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"key"
argument_list|,
name|or4
operator|.
name|getMapKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.util.Map"
argument_list|,
name|or4
operator|.
name|getCollectionType
argument_list|()
argument_list|)
expr_stmt|;
name|DbEntity
name|db3
init|=
name|dataMap
operator|.
name|getDbEntity
argument_list|(
literal|"Entity3"
argument_list|)
decl_stmt|;
name|DbEntity
name|db4
init|=
name|dataMap
operator|.
name|getDbEntity
argument_list|(
literal|"Entity4"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|db3
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|db4
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|db3
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|db4
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DbRelationship
name|dbr3
init|=
operator|(
name|DbRelationship
operator|)
name|db3
operator|.
name|getRelationship
argument_list|(
literal|"entity4"
argument_list|)
decl_stmt|;
name|DbRelationship
name|dbr4
init|=
operator|(
name|DbRelationship
operator|)
name|db4
operator|.
name|getRelationship
argument_list|(
literal|"entity3s"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dbr3
operator|.
name|getJoins
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dbr4
operator|.
name|getJoins
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|dbr4
argument_list|,
name|dbr3
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|dbr3
argument_list|,
name|dbr4
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|bidiOMAssert
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjEntity
name|e1
init|=
name|dataMap
operator|.
name|getObjEntity
argument_list|(
literal|"Entity1"
argument_list|)
decl_stmt|;
name|ObjEntity
name|e2
init|=
name|dataMap
operator|.
name|getObjEntity
argument_list|(
literal|"Entity2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|e1
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|e2
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DbEntity
name|db1
init|=
name|dataMap
operator|.
name|getDbEntity
argument_list|(
literal|"Entity1"
argument_list|)
decl_stmt|;
name|DbEntity
name|db2
init|=
name|dataMap
operator|.
name|getDbEntity
argument_list|(
literal|"Entity2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|db1
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|db2
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|db1
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|db2
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DbRelationship
name|dbr1
init|=
operator|(
name|DbRelationship
operator|)
name|db1
operator|.
name|getRelationship
argument_list|(
literal|"entity2"
argument_list|)
decl_stmt|;
name|DbRelationship
name|dbr2
init|=
operator|(
name|DbRelationship
operator|)
name|db2
operator|.
name|getRelationship
argument_list|(
literal|"entity1s"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dbr1
operator|.
name|getJoins
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dbr2
operator|.
name|getJoins
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|dbr2
argument_list|,
name|dbr1
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|dbr1
argument_list|,
name|dbr2
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DataMap
name|load
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|classes
parameter_list|)
block|{
name|EntityMapLoaderContext
name|context
init|=
operator|new
name|EntityMapLoaderContext
argument_list|(
operator|new
name|MockPersistenceUnitInfo
argument_list|()
argument_list|)
decl_stmt|;
name|EntityMapAnnotationLoader
name|loader
init|=
operator|new
name|EntityMapAnnotationLoader
argument_list|(
name|context
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|classes
control|)
block|{
name|loader
operator|.
name|loadClassMapping
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
operator|new
name|EntityMapDefaultsProcessor
argument_list|()
operator|.
name|applyDefaults
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Found conflicts: "
operator|+
name|context
operator|.
name|getConflicts
argument_list|()
argument_list|,
name|context
operator|.
name|getConflicts
argument_list|()
operator|.
name|hasFailures
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|dataMap
init|=
operator|new
name|DataMapConverter
argument_list|()
operator|.
name|toDataMap
argument_list|(
literal|"n1"
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Found DataMap conflicts: "
operator|+
name|context
operator|.
name|getConflicts
argument_list|()
argument_list|,
name|context
operator|.
name|getConflicts
argument_list|()
operator|.
name|hasFailures
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|dataMap
return|;
block|}
block|}
end_class

end_unit

