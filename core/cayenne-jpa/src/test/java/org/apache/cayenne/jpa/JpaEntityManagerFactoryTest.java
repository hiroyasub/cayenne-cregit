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
name|spi
operator|.
name|MockPersistenceUnitInfo
import|;
end_import

begin_class
specifier|public
class|class
name|JpaEntityManagerFactoryTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testOpenClose
parameter_list|()
block|{
name|JpaEntityManagerFactory
name|f
init|=
operator|new
name|MockJpaEntityManagerFactory
argument_list|(
operator|new
name|MockPersistenceUnitInfo
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
name|f
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
comment|// check that all methods throw
try|try
block|{
name|f
operator|.
name|close
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Closed EntityManagerFactory is supposed to throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|f
operator|.
name|createEntityManager
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Closed EntityManagerFactory is supposed to throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|f
operator|.
name|createEntityManager
argument_list|(
operator|new
name|HashMap
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Closed EntityManagerFactory is supposed to throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
end_class

end_unit

