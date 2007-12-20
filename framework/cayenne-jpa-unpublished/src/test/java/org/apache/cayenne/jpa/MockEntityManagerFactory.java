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
name|spi
operator|.
name|PersistenceUnitInfo
import|;
end_import

begin_class
specifier|public
class|class
name|MockEntityManagerFactory
implements|implements
name|EntityManagerFactory
block|{
specifier|protected
name|String
name|persistenceUnitName
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|properties
decl_stmt|;
specifier|protected
name|PersistenceUnitInfo
name|info
decl_stmt|;
specifier|public
name|MockEntityManagerFactory
parameter_list|()
block|{
block|}
specifier|public
name|MockEntityManagerFactory
parameter_list|(
name|String
name|persistenceUnitName
parameter_list|,
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|persistenceUnitName
operator|=
name|persistenceUnitName
expr_stmt|;
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
specifier|public
name|MockEntityManagerFactory
parameter_list|(
name|String
name|persistenceUnitName
parameter_list|,
name|PersistenceUnitInfo
name|info
parameter_list|)
block|{
name|this
operator|.
name|persistenceUnitName
operator|=
name|persistenceUnitName
expr_stmt|;
name|this
operator|.
name|info
operator|=
name|info
expr_stmt|;
block|}
specifier|public
name|EntityManager
name|createEntityManager
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|EntityManager
name|createEntityManager
parameter_list|(
name|Map
name|parameters
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
block|}
specifier|public
name|boolean
name|isOpen
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|String
name|getPersistenceUnitName
parameter_list|()
block|{
return|return
name|persistenceUnitName
return|;
block|}
specifier|public
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
specifier|public
name|PersistenceUnitInfo
name|getInfo
parameter_list|()
block|{
return|return
name|info
return|;
block|}
block|}
end_class

end_unit

