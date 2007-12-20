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
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
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
name|spi
operator|.
name|PersistenceUnitInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|TransactionSynchronizationRegistry
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
comment|/**  * An EntityManagerFactory that registers all EntityManagers that it creates with an  * active JTA Transaction so that they could flush the object state to the database during  * commit.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|JtaEntityManagerFactory
extends|extends
name|ResourceLocalEntityManagerFactory
block|{
specifier|static
specifier|final
name|String
name|TX_SYNC_REGISTRY_KEY
init|=
literal|"java:comp/TransactionSynchronizationRegistry"
decl_stmt|;
specifier|protected
name|TransactionSynchronizationRegistry
name|transactionRegistry
decl_stmt|;
comment|/**      * Non-public constructor used for unit testing.      */
name|JtaEntityManagerFactory
parameter_list|(
name|PersistenceUnitInfo
name|unitInfo
parameter_list|)
block|{
name|super
argument_list|(
name|unitInfo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JtaEntityManagerFactory
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
name|super
argument_list|(
name|provider
argument_list|,
name|domain
argument_list|,
name|unitInfo
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns JTA 11 TransactionSynchronizationRegistry, looking it up via JNDI on first      * access, and caching it for the following invocations.      */
specifier|protected
name|TransactionSynchronizationRegistry
name|getTransactionRegistry
parameter_list|()
block|{
if|if
condition|(
name|transactionRegistry
operator|==
literal|null
condition|)
block|{
name|InitialContext
name|jndiContext
decl_stmt|;
try|try
block|{
name|jndiContext
operator|=
operator|new
name|InitialContext
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JpaProviderException
argument_list|(
literal|"Error creating JNDI context"
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|transactionRegistry
operator|=
operator|(
name|TransactionSynchronizationRegistry
operator|)
name|jndiContext
operator|.
name|lookup
argument_list|(
name|TX_SYNC_REGISTRY_KEY
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JpaProviderException
argument_list|(
literal|"Failed to look up "
operator|+
name|TX_SYNC_REGISTRY_KEY
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|transactionRegistry
return|;
block|}
comment|/**      * Returns whether there is a JTA transaction in progress.      */
specifier|protected
name|boolean
name|isActiveTransaction
parameter_list|()
block|{
name|int
name|txStatus
init|=
name|getTransactionRegistry
argument_list|()
operator|.
name|getTransactionStatus
argument_list|()
decl_stmt|;
return|return
name|txStatus
operator|==
name|Status
operator|.
name|STATUS_ACTIVE
operator|||
name|txStatus
operator|==
name|Status
operator|.
name|STATUS_MARKED_ROLLBACK
return|;
block|}
annotation|@
name|Override
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
name|JtaEntityManager
argument_list|(
name|createObjectContext
argument_list|()
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|em
operator|=
operator|new
name|TypeCheckingEntityManager
argument_list|(
name|em
argument_list|)
expr_stmt|;
if|if
condition|(
name|isActiveTransaction
argument_list|()
condition|)
block|{
name|em
operator|.
name|joinTransaction
argument_list|()
expr_stmt|;
block|}
return|return
name|em
return|;
block|}
block|}
end_class

end_unit

