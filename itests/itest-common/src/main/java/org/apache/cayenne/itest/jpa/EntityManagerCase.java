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
name|itest
operator|.
name|jpa
package|;
end_package

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
name|ItestSetup
import|;
end_import

begin_comment
comment|/**  * A TestCase superclass that provides an entity manager and transaction management.  *   */
end_comment

begin_class
specifier|public
class|class
name|EntityManagerCase
extends|extends
name|JpaTestCase
block|{
specifier|protected
name|EntityManager
name|entityManager
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
name|entityManager
operator|=
name|ItestSetup
operator|.
name|getInstance
argument_list|()
operator|.
name|createEntityManager
argument_list|()
expr_stmt|;
name|entityManager
operator|.
name|getTransaction
argument_list|()
operator|.
name|begin
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|entityManager
operator|.
name|getTransaction
argument_list|()
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|entityManager
operator|.
name|getTransaction
argument_list|()
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
name|entityManager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|EntityManager
name|getEntityManager
parameter_list|()
block|{
return|return
name|entityManager
return|;
block|}
specifier|protected
name|void
name|setEntityManager
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
name|this
operator|.
name|entityManager
operator|=
name|em
expr_stmt|;
block|}
block|}
end_class

end_unit

