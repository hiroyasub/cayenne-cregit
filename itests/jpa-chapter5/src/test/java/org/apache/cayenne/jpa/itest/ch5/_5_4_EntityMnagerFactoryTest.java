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
name|ch5
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Persistence
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
name|JpaTestCase
import|;
end_import

begin_class
specifier|public
class|class
name|_5_4_EntityMnagerFactoryTest
extends|extends
name|JpaTestCase
block|{
specifier|public
name|void
name|testCreateEntityManager
parameter_list|()
block|{
name|EntityManagerFactory
name|factory
init|=
name|Persistence
operator|.
name|createEntityManagerFactory
argument_list|(
literal|"itest-non-jta"
argument_list|)
decl_stmt|;
name|EntityManager
name|em
init|=
name|factory
operator|.
name|createEntityManager
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|em
argument_list|)
expr_stmt|;
name|EntityManager
name|em1
init|=
name|factory
operator|.
name|createEntityManager
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
name|em
argument_list|,
name|em1
argument_list|)
expr_stmt|;
comment|// per JPA spec, unrecognized properties must be quietly ignored
name|Map
name|properties
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"dummy"
argument_list|,
literal|"property"
argument_list|)
expr_stmt|;
name|EntityManager
name|em2
init|=
name|factory
operator|.
name|createEntityManager
argument_list|(
name|properties
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|em2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|em
argument_list|,
name|em2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testClose
parameter_list|()
block|{
name|EntityManagerFactory
name|factory
init|=
name|Persistence
operator|.
name|createEntityManagerFactory
argument_list|(
literal|"itest-non-jta"
argument_list|)
decl_stmt|;
name|EntityManager
name|em
init|=
name|factory
operator|.
name|createEntityManager
argument_list|()
decl_stmt|;
name|Map
name|properties
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"dummy"
argument_list|,
literal|"property"
argument_list|)
expr_stmt|;
name|EntityManager
name|em2
init|=
name|factory
operator|.
name|createEntityManager
argument_list|(
name|properties
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|factory
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|em
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|em2
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|factory
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|em
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|em2
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|factory
operator|.
name|createEntityManager
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Closed EMF - expected to throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
block|}
try|try
block|{
name|factory
operator|.
name|createEntityManager
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Closed EMF - expected to throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
block|}
block|}
block|}
end_class

end_unit

