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
name|conf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|TemporalType
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
name|map
operator|.
name|JpaBasic
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
name|JpaId
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
name|JpaManyToOne
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
name|JpaOneToMany
import|;
end_import

begin_class
specifier|public
class|class
name|EntityMapDefaultsProcessorTest
extends|extends
name|TestCase
block|{
specifier|protected
name|JpaEntity
name|entity
decl_stmt|;
specifier|protected
name|EntityMapLoaderContext
name|context
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
comment|// sanity check - test object must not be serializable to be rejected...
name|assertFalse
argument_list|(
name|Serializable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|MockAnnotatedBean3
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|EntityMapLoaderContext
argument_list|(
operator|new
name|MockPersistenceUnitInfo
argument_list|()
argument_list|)
expr_stmt|;
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
name|MockAnnotatedBean1
operator|.
name|class
argument_list|)
expr_stmt|;
name|loader
operator|.
name|loadClassMapping
argument_list|(
name|MockAnnotatedBean3
operator|.
name|class
argument_list|)
expr_stmt|;
name|loader
operator|.
name|loadClassMapping
argument_list|(
name|MockAnnotatedBean5
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// apply defaults
name|EntityMapDefaultsProcessor
name|defaultsProcessor
init|=
operator|new
name|EntityMapDefaultsProcessor
argument_list|()
decl_stmt|;
name|defaultsProcessor
operator|.
name|applyDefaults
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|JpaEntityMap
name|map
init|=
name|context
operator|.
name|getEntityMap
argument_list|()
decl_stmt|;
name|entity
operator|=
name|map
operator|.
name|entityForClass
argument_list|(
name|MockAnnotatedBean3
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMissingAttributeAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|context
operator|.
name|getConflicts
argument_list|()
operator|.
name|hasFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getConflicts
argument_list|()
operator|.
name|getFailures
argument_list|(
name|MockAnnotatedBean3
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"attribute1"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|getConflicts
argument_list|()
operator|.
name|getFailures
argument_list|(
name|MockAnnotatedBean3
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"attribute2"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|getBasicAttribute
argument_list|(
literal|"attribute1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|getBasicAttribute
argument_list|(
literal|"attribute2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSkipCayennePersistentProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|JpaEntity
name|e5
init|=
name|context
operator|.
name|getEntityMap
argument_list|()
operator|.
name|entityForClass
argument_list|(
name|MockAnnotatedBean5
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e5
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e5
operator|.
name|getAttributes
argument_list|()
operator|.
name|getBasicAttribute
argument_list|(
literal|"attribute1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|e5
operator|.
name|getAttributes
argument_list|()
operator|.
name|getBasicAttribute
argument_list|(
literal|"objectId"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTargetEntityNameToOne
parameter_list|()
block|{
name|JpaManyToOne
name|toBean2
init|=
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|getManyToOneRelationship
argument_list|(
literal|"toBean2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toBean2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MockAnnotatedBean1
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|toBean2
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTargetEntityNameCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|context
operator|.
name|getConflicts
argument_list|()
operator|.
name|getFailures
argument_list|(
name|MockAnnotatedBean3
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"toBean2s1"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|JpaOneToMany
name|toBean2s1
init|=
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|getOneToManyRelationship
argument_list|(
literal|"toBean2s1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toBean2s1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MockAnnotatedBean1
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|toBean2s1
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Expected failure"
argument_list|,
name|context
operator|.
name|getConflicts
argument_list|()
operator|.
name|getFailures
argument_list|(
name|MockAnnotatedBean3
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"toBean2s2"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|JpaOneToMany
name|toBean2s2
init|=
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|getOneToManyRelationship
argument_list|(
literal|"toBean2s2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|toBean2s2
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|toBean2s2
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testId
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|getIds
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JpaId
name|id
init|=
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|getIds
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"pk"
argument_list|,
name|id
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|id
operator|.
name|getColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pk"
argument_list|,
name|id
operator|.
name|getColumn
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDate
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|context
operator|.
name|getConflicts
argument_list|()
operator|.
name|getFailures
argument_list|(
name|MockAnnotatedBean3
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"date"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|JpaBasic
name|date
init|=
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|getBasicAttribute
argument_list|(
literal|"date"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|TemporalType
operator|.
name|TIMESTAMP
argument_list|,
name|date
operator|.
name|getTemporal
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

