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
name|Collection
import|;
end_import

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
name|dba
operator|.
name|TypesMapping
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
name|jpa
operator|.
name|entity
operator|.
name|MockBasicEntity
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
name|entity
operator|.
name|MockIdColumnEntity
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
name|entity
operator|.
name|MockTypesEntity
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
name|entity
operator|.
name|cayenne
operator|.
name|MockCayenneEntity1
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
name|entity
operator|.
name|cayenne
operator|.
name|MockCayenneEntity2
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
name|entity
operator|.
name|cayenne
operator|.
name|MockCayenneEntityMap1
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
name|entity
operator|.
name|cayenne
operator|.
name|MockCayenneTargetEntity1
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
name|entity
operator|.
name|cayenne
operator|.
name|MockCayenneTargetEntity2
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
name|map
operator|.
name|JpaEntity
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
name|map
operator|.
name|JpaEntityListener
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
name|map
operator|.
name|JpaEntityListeners
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
name|map
operator|.
name|JpaEntityMap
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
name|map
operator|.
name|JpaLifecycleCallback
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
name|map
operator|.
name|JpaPersistenceUnitDefaults
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
name|map
operator|.
name|JpaPersistenceUnitMetadata
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
name|map
operator|.
name|EntityListener
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

begin_class
specifier|public
class|class
name|DataMapConverterTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testDefaultEntityListeners
parameter_list|()
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
name|JpaEntityMap
name|jpaMap
init|=
name|context
operator|.
name|getEntityMap
argument_list|()
decl_stmt|;
name|JpaPersistenceUnitMetadata
name|metadata
init|=
operator|new
name|JpaPersistenceUnitMetadata
argument_list|()
decl_stmt|;
name|jpaMap
operator|.
name|setPersistenceUnitMetadata
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
name|JpaPersistenceUnitDefaults
name|defaults
init|=
operator|new
name|JpaPersistenceUnitDefaults
argument_list|()
decl_stmt|;
name|metadata
operator|.
name|setPersistenceUnitDefaults
argument_list|(
name|defaults
argument_list|)
expr_stmt|;
name|JpaEntityListeners
name|listeners
init|=
operator|new
name|JpaEntityListeners
argument_list|()
decl_stmt|;
name|defaults
operator|.
name|setEntityListeners
argument_list|(
name|listeners
argument_list|)
expr_stmt|;
name|JpaEntityListener
name|l1
init|=
operator|new
name|JpaEntityListener
argument_list|()
decl_stmt|;
name|l1
operator|.
name|setClassName
argument_list|(
literal|"abc.C1"
argument_list|)
expr_stmt|;
name|l1
operator|.
name|setPostLoad
argument_list|(
operator|new
name|JpaLifecycleCallback
argument_list|(
literal|"xpl1"
argument_list|)
argument_list|)
expr_stmt|;
name|l1
operator|.
name|setPreRemove
argument_list|(
operator|new
name|JpaLifecycleCallback
argument_list|(
literal|"xpr1"
argument_list|)
argument_list|)
expr_stmt|;
name|listeners
operator|.
name|getEntityListeners
argument_list|()
operator|.
name|add
argument_list|(
name|l1
argument_list|)
expr_stmt|;
name|JpaEntityListener
name|l2
init|=
operator|new
name|JpaEntityListener
argument_list|()
decl_stmt|;
name|l2
operator|.
name|setClassName
argument_list|(
literal|"abc.C2"
argument_list|)
expr_stmt|;
name|l2
operator|.
name|setPostLoad
argument_list|(
operator|new
name|JpaLifecycleCallback
argument_list|(
literal|"xpl2"
argument_list|)
argument_list|)
expr_stmt|;
name|l2
operator|.
name|setPreRemove
argument_list|(
operator|new
name|JpaLifecycleCallback
argument_list|(
literal|"xpr2"
argument_list|)
argument_list|)
expr_stmt|;
name|listeners
operator|.
name|getEntityListeners
argument_list|()
operator|.
name|add
argument_list|(
name|l2
argument_list|)
expr_stmt|;
name|DataMap
name|cayenneMap
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
name|Collection
name|entityListeners
init|=
name|cayenneMap
operator|.
name|getDefaultEntityListeners
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|entityListeners
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
name|defaultListeners
init|=
name|cayenneMap
operator|.
name|getDefaultEntityListeners
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|defaultListeners
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|EntityListener
name|cl1
init|=
name|cayenneMap
operator|.
name|getDefaultEntityListener
argument_list|(
literal|"abc.C1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cl1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|l1
operator|.
name|getClassName
argument_list|()
argument_list|,
name|cl1
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cl1
operator|.
name|getCallbackMap
argument_list|()
operator|.
name|getPostLoad
argument_list|()
operator|.
name|getCallbackMethods
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
name|cl1
operator|.
name|getCallbackMap
argument_list|()
operator|.
name|getPreRemove
argument_list|()
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cl1
operator|.
name|getCallbackMap
argument_list|()
operator|.
name|getPostPersist
argument_list|()
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testEntityCallbackMethods
parameter_list|()
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
name|JpaEntityMap
name|jpaMap
init|=
name|context
operator|.
name|getEntityMap
argument_list|()
decl_stmt|;
name|JpaEntity
name|jpaEntity
init|=
operator|new
name|JpaEntity
argument_list|()
decl_stmt|;
name|jpaEntity
operator|.
name|setName
argument_list|(
literal|"E1"
argument_list|)
expr_stmt|;
name|jpaEntity
operator|.
name|setClassName
argument_list|(
literal|"abc.C2"
argument_list|)
expr_stmt|;
name|jpaEntity
operator|.
name|setPostLoad
argument_list|(
operator|new
name|JpaLifecycleCallback
argument_list|(
literal|"xpl2"
argument_list|)
argument_list|)
expr_stmt|;
name|jpaEntity
operator|.
name|setPreRemove
argument_list|(
operator|new
name|JpaLifecycleCallback
argument_list|(
literal|"xpr2"
argument_list|)
argument_list|)
expr_stmt|;
name|jpaMap
operator|.
name|getEntities
argument_list|()
operator|.
name|add
argument_list|(
name|jpaEntity
argument_list|)
expr_stmt|;
name|DataMap
name|cayenneMap
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
name|ObjEntity
name|entity
init|=
name|cayenneMap
operator|.
name|getObjEntity
argument_list|(
literal|"E1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|entity
operator|.
name|getCallbackMap
argument_list|()
operator|.
name|getPostLoad
argument_list|()
operator|.
name|getCallbackMethods
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
name|entity
operator|.
name|getCallbackMap
argument_list|()
operator|.
name|getPreRemove
argument_list|()
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|entity
operator|.
name|getCallbackMap
argument_list|()
operator|.
name|getPostPersist
argument_list|()
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testEntityListeners
parameter_list|()
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
name|JpaEntityMap
name|jpaMap
init|=
name|context
operator|.
name|getEntityMap
argument_list|()
decl_stmt|;
name|JpaEntity
name|jpaEntity
init|=
operator|new
name|JpaEntity
argument_list|()
decl_stmt|;
name|jpaEntity
operator|.
name|setName
argument_list|(
literal|"E1"
argument_list|)
expr_stmt|;
name|jpaEntity
operator|.
name|setClassName
argument_list|(
literal|"abc.C2"
argument_list|)
expr_stmt|;
name|jpaEntity
operator|.
name|setPostLoad
argument_list|(
operator|new
name|JpaLifecycleCallback
argument_list|(
literal|"xpl2"
argument_list|)
argument_list|)
expr_stmt|;
name|jpaEntity
operator|.
name|setPreRemove
argument_list|(
operator|new
name|JpaLifecycleCallback
argument_list|(
literal|"xpr2"
argument_list|)
argument_list|)
expr_stmt|;
name|jpaMap
operator|.
name|getEntities
argument_list|()
operator|.
name|add
argument_list|(
name|jpaEntity
argument_list|)
expr_stmt|;
name|JpaEntityListeners
name|listeners
init|=
operator|new
name|JpaEntityListeners
argument_list|()
decl_stmt|;
name|jpaEntity
operator|.
name|setEntityListeners
argument_list|(
name|listeners
argument_list|)
expr_stmt|;
name|JpaEntityListener
name|l1
init|=
operator|new
name|JpaEntityListener
argument_list|()
decl_stmt|;
name|l1
operator|.
name|setClassName
argument_list|(
literal|"abc.C1"
argument_list|)
expr_stmt|;
name|l1
operator|.
name|setPostLoad
argument_list|(
operator|new
name|JpaLifecycleCallback
argument_list|(
literal|"xpl1"
argument_list|)
argument_list|)
expr_stmt|;
name|l1
operator|.
name|setPreRemove
argument_list|(
operator|new
name|JpaLifecycleCallback
argument_list|(
literal|"xpr1"
argument_list|)
argument_list|)
expr_stmt|;
name|listeners
operator|.
name|getEntityListeners
argument_list|()
operator|.
name|add
argument_list|(
name|l1
argument_list|)
expr_stmt|;
name|JpaEntityListener
name|l2
init|=
operator|new
name|JpaEntityListener
argument_list|()
decl_stmt|;
name|l2
operator|.
name|setClassName
argument_list|(
literal|"abc.C2"
argument_list|)
expr_stmt|;
name|l2
operator|.
name|setPostLoad
argument_list|(
operator|new
name|JpaLifecycleCallback
argument_list|(
literal|"xpl2"
argument_list|)
argument_list|)
expr_stmt|;
name|l2
operator|.
name|setPreRemove
argument_list|(
operator|new
name|JpaLifecycleCallback
argument_list|(
literal|"xpr2"
argument_list|)
argument_list|)
expr_stmt|;
name|listeners
operator|.
name|getEntityListeners
argument_list|()
operator|.
name|add
argument_list|(
name|l2
argument_list|)
expr_stmt|;
name|DataMap
name|cayenneMap
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
name|ObjEntity
name|entity
init|=
name|cayenneMap
operator|.
name|getObjEntity
argument_list|(
literal|"E1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|Collection
name|entityListeners
init|=
name|entity
operator|.
name|getEntityListeners
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|entityListeners
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|EntityListener
name|cl1
init|=
name|entity
operator|.
name|getEntityListener
argument_list|(
literal|"abc.C1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cl1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|l1
operator|.
name|getClassName
argument_list|()
argument_list|,
name|cl1
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cl1
operator|.
name|getCallbackMap
argument_list|()
operator|.
name|getPostLoad
argument_list|()
operator|.
name|getCallbackMethods
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
name|cl1
operator|.
name|getCallbackMap
argument_list|()
operator|.
name|getPreRemove
argument_list|()
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cl1
operator|.
name|getCallbackMap
argument_list|()
operator|.
name|getPostPersist
argument_list|()
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDataMapDefaults
parameter_list|()
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
name|JpaEntityMap
name|jpaMap
init|=
name|context
operator|.
name|getEntityMap
argument_list|()
decl_stmt|;
name|jpaMap
operator|.
name|setPackageName
argument_list|(
literal|"p1"
argument_list|)
expr_stmt|;
name|jpaMap
operator|.
name|setSchema
argument_list|(
literal|"s1"
argument_list|)
expr_stmt|;
comment|// TODO: unsupported by DataMap
comment|// jpaMap.setCatalog("c1");
name|DataMap
name|cayenneMap
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
name|assertEquals
argument_list|(
literal|"n1"
argument_list|,
name|cayenneMap
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"p1"
argument_list|,
name|cayenneMap
operator|.
name|getDefaultPackage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"s1"
argument_list|,
name|cayenneMap
operator|.
name|getDefaultSchema
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLoadClassMapping
parameter_list|()
throws|throws
name|Exception
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
name|loader
operator|.
name|loadClassMapping
argument_list|(
name|MockCayenneEntity1
operator|.
name|class
argument_list|)
expr_stmt|;
name|loader
operator|.
name|loadClassMapping
argument_list|(
name|MockCayenneEntity2
operator|.
name|class
argument_list|)
expr_stmt|;
name|loader
operator|.
name|loadClassMapping
argument_list|(
name|MockCayenneTargetEntity1
operator|.
name|class
argument_list|)
expr_stmt|;
name|loader
operator|.
name|loadClassMapping
argument_list|(
name|MockCayenneTargetEntity2
operator|.
name|class
argument_list|)
expr_stmt|;
name|loader
operator|.
name|loadClassMapping
argument_list|(
name|MockCayenneEntityMap1
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// apply defaults before conversion
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
operator|new
name|DataMapMappingAssertion
argument_list|()
operator|.
name|testDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDataMapTypes
parameter_list|()
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
name|loader
operator|.
name|loadClassMapping
argument_list|(
name|MockTypesEntity
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// apply defaults before conversion
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
name|DbEntity
name|typesTable
init|=
name|dataMap
operator|.
name|getDbEntity
argument_list|(
literal|"MockTypesEntity"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|typesTable
argument_list|)
expr_stmt|;
name|DbAttribute
name|defaultCalColumn
init|=
operator|(
name|DbAttribute
operator|)
name|typesTable
operator|.
name|getAttribute
argument_list|(
literal|"defaultCalendar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|defaultCalColumn
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Invalid calendar type: "
operator|+
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|defaultCalColumn
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|,
name|Types
operator|.
name|TIMESTAMP
argument_list|,
name|defaultCalColumn
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|timeColumn
init|=
operator|(
name|DbAttribute
operator|)
name|typesTable
operator|.
name|getAttribute
argument_list|(
literal|"timeCalendar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|timeColumn
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|TIME
argument_list|,
name|timeColumn
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|dateColumn
init|=
operator|(
name|DbAttribute
operator|)
name|typesTable
operator|.
name|getAttribute
argument_list|(
literal|"dateCalendar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dateColumn
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|DATE
argument_list|,
name|dateColumn
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|timestampColumn
init|=
operator|(
name|DbAttribute
operator|)
name|typesTable
operator|.
name|getAttribute
argument_list|(
literal|"timestampCalendar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|timestampColumn
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|TIMESTAMP
argument_list|,
name|timestampColumn
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|defaultEnumColumn
init|=
operator|(
name|DbAttribute
operator|)
name|typesTable
operator|.
name|getAttribute
argument_list|(
literal|"defaultEnum"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|defaultEnumColumn
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|defaultEnumColumn
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|ordinalEnumColumn
init|=
operator|(
name|DbAttribute
operator|)
name|typesTable
operator|.
name|getAttribute
argument_list|(
literal|"ordinalEnum"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ordinalEnumColumn
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|ordinalEnumColumn
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|stringEnumColumn
init|=
operator|(
name|DbAttribute
operator|)
name|typesTable
operator|.
name|getAttribute
argument_list|(
literal|"stringEnum"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|stringEnumColumn
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|stringEnumColumn
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|byteArrayColumn
init|=
operator|(
name|DbAttribute
operator|)
name|typesTable
operator|.
name|getAttribute
argument_list|(
literal|"byteArray"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|byteArrayColumn
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|BINARY
argument_list|,
name|byteArrayColumn
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testColumnOverrides
parameter_list|()
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
name|loader
operator|.
name|loadClassMapping
argument_list|(
name|MockIdColumnEntity
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// apply defaults before conversion
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
name|DbEntity
name|table
init|=
name|dataMap
operator|.
name|getDbEntity
argument_list|(
literal|"MockIdColumnEntity"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|table
argument_list|)
expr_stmt|;
name|DbAttribute
name|pk
init|=
operator|(
name|DbAttribute
operator|)
name|table
operator|.
name|getAttribute
argument_list|(
literal|"pk_column"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pk
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pk
operator|.
name|isPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBasicOptionality
parameter_list|()
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
name|loader
operator|.
name|loadClassMapping
argument_list|(
name|MockBasicEntity
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// apply defaults before conversion
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
name|DbEntity
name|table
init|=
name|dataMap
operator|.
name|getDbEntity
argument_list|(
literal|"MockBasicEntity"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|table
argument_list|)
expr_stmt|;
name|DbAttribute
name|optional
init|=
operator|(
name|DbAttribute
operator|)
name|table
operator|.
name|getAttribute
argument_list|(
literal|"optionalBasic"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|optional
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|optional
operator|.
name|isMandatory
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|required
init|=
operator|(
name|DbAttribute
operator|)
name|table
operator|.
name|getAttribute
argument_list|(
literal|"requiredBasic"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|required
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|required
operator|.
name|isMandatory
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

