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
name|itest
operator|.
name|ch2
package|;
end_package

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
name|cayenne
operator|.
name|itest
operator|.
name|jpa
operator|.
name|EntityManagerCase
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
name|itest
operator|.
name|ch2
operator|.
name|entity
operator|.
name|CollectionFieldPersistenceEntity
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
name|itest
operator|.
name|ch2
operator|.
name|entity
operator|.
name|FieldPersistenceEntity
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
name|itest
operator|.
name|ch2
operator|.
name|entity
operator|.
name|HelperEntity1
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
name|itest
operator|.
name|ch2
operator|.
name|entity
operator|.
name|HelperEntity2
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
name|itest
operator|.
name|ch2
operator|.
name|entity
operator|.
name|HelperEntity3
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
name|itest
operator|.
name|ch2
operator|.
name|entity
operator|.
name|HelperEntity4
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
name|itest
operator|.
name|ch2
operator|.
name|entity
operator|.
name|MapFieldPersistenceEntity
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
name|itest
operator|.
name|ch2
operator|.
name|entity
operator|.
name|PropertyPersistenceEntity
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
name|itest
operator|.
name|ch2
operator|.
name|entity
operator|.
name|TransientFieldsEntity
import|;
end_import

begin_class
specifier|public
class|class
name|_2_1_1_PeristentFieldsAndPropertiesTest
extends|extends
name|EntityManagerCase
block|{
specifier|public
name|void
name|testFieldBasedPersistence
parameter_list|()
throws|throws
name|Exception
block|{
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"FieldPersistenceEntity"
argument_list|)
expr_stmt|;
name|FieldPersistenceEntity
name|o1
init|=
operator|new
name|FieldPersistenceEntity
argument_list|()
decl_stmt|;
name|getEntityManager
argument_list|()
operator|.
name|persist
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|getEntityManager
argument_list|()
operator|.
name|getTransaction
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|FieldPersistenceEntity
operator|.
name|INITIAL_VALUE
argument_list|,
name|getDbHelper
argument_list|()
operator|.
name|getObject
argument_list|(
literal|"FieldPersistenceEntity"
argument_list|,
literal|"property1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPropertyBasedPersistence
parameter_list|()
throws|throws
name|Exception
block|{
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"PropertyPersistenceEntity"
argument_list|)
expr_stmt|;
name|PropertyPersistenceEntity
name|o1
init|=
operator|new
name|PropertyPersistenceEntity
argument_list|()
decl_stmt|;
name|o1
operator|.
name|setProperty1
argument_list|(
literal|"p1"
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setProperty2
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|getEntityManager
argument_list|()
operator|.
name|persist
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|getEntityManager
argument_list|()
operator|.
name|getTransaction
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"p1"
argument_list|,
name|getDbHelper
argument_list|()
operator|.
name|getObject
argument_list|(
literal|"PropertyPersistenceEntity"
argument_list|,
literal|"property1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSkipTransientProperties
parameter_list|()
block|{
name|TransientFieldsEntity
name|o1
init|=
operator|new
name|TransientFieldsEntity
argument_list|()
decl_stmt|;
name|getEntityManager
argument_list|()
operator|.
name|persist
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|getEntityManager
argument_list|()
operator|.
name|getTransaction
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
comment|// TODO: andrus 8/30/2006 - fails
specifier|public
name|void
name|_testCollectionTypesProperties
parameter_list|()
block|{
name|CollectionFieldPersistenceEntity
name|o1
init|=
operator|new
name|CollectionFieldPersistenceEntity
argument_list|()
decl_stmt|;
name|o1
operator|.
name|setCollection
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
operator|new
name|HelperEntity1
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setSet
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
operator|new
name|HelperEntity2
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setList
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|HelperEntity3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|getEntityManager
argument_list|()
operator|.
name|persist
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|getEntityManager
argument_list|()
operator|.
name|getTransaction
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
comment|// TODO: andrus 8/30/2006 - fails
specifier|public
name|void
name|testMapProperties
parameter_list|()
block|{
name|MapFieldPersistenceEntity
name|o1
init|=
operator|new
name|MapFieldPersistenceEntity
argument_list|()
decl_stmt|;
name|o1
operator|.
name|setMap
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|HelperEntity4
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|getEntityManager
argument_list|()
operator|.
name|persist
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|getEntityManager
argument_list|()
operator|.
name|getTransaction
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
comment|// TODO: andrus 8/30/2006 - implement
specifier|public
name|void
name|testExceptionInPropertyAccessors
parameter_list|()
block|{
comment|// Runtime exceptions thrown by property accessor methods cause the current
comment|// transaction to be rolled back.
comment|// Exceptions thrown by such methods when used by the persistence runtime to load
comment|// or store persistent state cause the persistence runtime to rollback the current
comment|// transaction and to throw a PersistenceException that wraps the application
comment|// exception.
block|}
block|}
end_class

end_unit

