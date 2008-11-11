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
name|Collections
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
name|spi
operator|.
name|PersistenceUnitInfo
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
name|access
operator|.
name|DataDomain
import|;
end_import

begin_comment
comment|/**  * A Cayenne EntityManagerFactory that supports resource-local transactions.  *   */
end_comment

begin_class
specifier|public
class|class
name|ResourceLocalEntityManagerFactory
implements|implements
name|EntityManagerFactory
block|{
specifier|protected
name|DataDomain
name|domain
decl_stmt|;
specifier|protected
name|boolean
name|open
decl_stmt|;
specifier|protected
name|PersistenceUnitInfo
name|unitInfo
decl_stmt|;
specifier|protected
name|Provider
name|provider
decl_stmt|;
comment|/**      * Non-public constructor used mostly for unit testing.      */
name|ResourceLocalEntityManagerFactory
parameter_list|(
name|PersistenceUnitInfo
name|unitInfo
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|unitInfo
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new JpaEntityManagerFactory.      */
specifier|public
name|ResourceLocalEntityManagerFactory
parameter_list|(
name|Provider
name|provider
parameter_list|,
name|DataDomain
name|domain
parameter_list|,
name|PersistenceUnitInfo
name|unitInfo
parameter_list|)
block|{
name|this
operator|.
name|unitInfo
operator|=
name|unitInfo
expr_stmt|;
name|this
operator|.
name|open
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
name|this
operator|.
name|provider
operator|=
name|provider
expr_stmt|;
block|}
comment|/**      * Returns wrapped unit.      */
specifier|protected
name|PersistenceUnitInfo
name|getPersistenceUnitInfo
parameter_list|()
block|{
return|return
name|unitInfo
return|;
block|}
comment|/**      * Indicates whether the factory is open. Returns true until the factory has been      * closed.      */
specifier|public
name|boolean
name|isOpen
parameter_list|()
block|{
return|return
name|open
return|;
block|}
comment|/**      * Close the factory, releasing any resources that it holds. After a factory instance      * is closed, all methods invoked on it will throw an IllegalStateException, except      * for isOpen, which will return false.      */
specifier|public
name|void
name|close
parameter_list|()
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|this
operator|.
name|open
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * Create a new EntityManager. Returns a new EntityManager instance every time it is      * invoked. The {@link EntityManager#isOpen()} method will return true of the returned      * instance.      *       * @return a new EntityManager instance.      */
specifier|public
name|EntityManager
name|createEntityManager
parameter_list|()
block|{
return|return
name|createEntityManager
argument_list|(
name|Collections
operator|.
name|EMPTY_MAP
argument_list|)
return|;
block|}
comment|/**      * Creates a new resource-local EntityManager with the specified map of properties.      * Returns a new EntityManager instance every time it is invoked. The      * {@link EntityManager#isOpen()} method will return true of the returned instance.      * Parameter map is ignored as Cayenne provider defines no properties for      * EntityManager as of now.      *       * @return a new EntityManager instance.      */
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
name|map
parameter_list|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|CayenneEntityManager
name|em
init|=
operator|new
name|ResourceLocalEntityManager
argument_list|(
name|createObjectContext
argument_list|()
argument_list|,
name|this
argument_list|)
decl_stmt|;
return|return
operator|new
name|TypeCheckingEntityManager
argument_list|(
name|em
argument_list|)
return|;
block|}
comment|/**      * Creates a new Cayenne {@link ObjectContext} based on this factory DataDomain.      * Returned context has lifecycle callbacks enabled, as expected in the JPA      * environment.      */
specifier|protected
name|ObjectContext
name|createObjectContext
parameter_list|()
block|{
return|return
name|domain
operator|.
name|createDataContext
argument_list|()
return|;
block|}
comment|/**      * A convenience method that throws an exception if called on closed factory.      */
name|void
name|checkClosed
parameter_list|()
throws|throws
name|IllegalStateException
block|{
if|if
condition|(
operator|!
name|isOpen
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"An attempt to access closed EntityManagerFactory."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns a parent persistence provider.      */
specifier|public
name|Provider
name|getProvider
parameter_list|()
block|{
return|return
name|provider
return|;
block|}
comment|/**      * Returns PersistenceUnitInfo used by this factory.      */
name|PersistenceUnitInfo
name|getUnitInfo
parameter_list|()
block|{
return|return
name|unitInfo
return|;
block|}
block|}
end_class

end_unit

