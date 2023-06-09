begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|server
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|DataChannel
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
name|DataContext
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
name|DataRowStore
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
name|DataRowStoreFactory
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
name|ObjectStore
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
name|cache
operator|.
name|NestedQueryCache
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
name|cache
operator|.
name|QueryCache
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
name|configuration
operator|.
name|ObjectContextFactory
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
name|configuration
operator|.
name|ObjectStoreFactory
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
name|di
operator|.
name|Inject
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
name|event
operator|.
name|EventManager
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
name|tx
operator|.
name|TransactionFactory
import|;
end_import

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DataContextFactory
implements|implements
name|ObjectContextFactory
block|{
annotation|@
name|Inject
specifier|protected
name|DataDomain
name|dataDomain
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|EventManager
name|eventManager
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataRowStoreFactory
name|dataRowStoreFactory
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ObjectStoreFactory
name|objectStoreFactory
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|QueryCache
name|queryCache
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|TransactionFactory
name|transactionFactory
decl_stmt|;
annotation|@
name|Override
specifier|public
name|ObjectContext
name|createContext
parameter_list|()
block|{
return|return
name|createdFromDataDomain
argument_list|(
name|dataDomain
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ObjectContext
name|createContext
parameter_list|(
name|DataChannel
name|parent
parameter_list|)
block|{
comment|// this switch may go away once we figure out clean property configuration...
if|if
condition|(
name|parent
operator|instanceof
name|DataDomain
condition|)
block|{
return|return
name|createdFromDataDomain
argument_list|(
operator|(
name|DataDomain
operator|)
name|parent
argument_list|)
return|;
block|}
if|else if
condition|(
name|parent
operator|instanceof
name|DataContext
condition|)
block|{
return|return
name|createFromDataContext
argument_list|(
operator|(
name|DataContext
operator|)
name|parent
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|createFromGenericChannel
argument_list|(
name|parent
argument_list|)
return|;
block|}
block|}
specifier|protected
name|ObjectContext
name|createFromGenericChannel
parameter_list|(
name|DataChannel
name|parent
parameter_list|)
block|{
comment|// for new dataRowStores use the same name for all stores
comment|// it makes it easier to track the event subject
name|DataRowStore
name|snapshotCache
init|=
operator|(
name|dataDomain
operator|.
name|isSharedCacheEnabled
argument_list|()
operator|)
condition|?
name|dataDomain
operator|.
name|getSharedSnapshotCache
argument_list|()
else|:
name|dataRowStoreFactory
operator|.
name|createDataRowStore
argument_list|(
name|dataDomain
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|DataContext
name|context
init|=
name|newInstance
argument_list|(
name|parent
argument_list|,
name|objectStoreFactory
operator|.
name|createObjectStore
argument_list|(
name|snapshotCache
argument_list|)
argument_list|)
decl_stmt|;
name|context
operator|.
name|setValidatingObjectsOnCommit
argument_list|(
name|dataDomain
operator|.
name|isValidatingObjectsOnCommit
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setQueryCache
argument_list|(
operator|new
name|NestedQueryCache
argument_list|(
name|queryCache
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
specifier|protected
name|ObjectContext
name|createFromDataContext
parameter_list|(
name|DataContext
name|parent
parameter_list|)
block|{
comment|// child ObjectStore should not have direct access to snapshot cache, so do not
comment|// pass it in constructor.
name|ObjectStore
name|objectStore
init|=
name|objectStoreFactory
operator|.
name|createObjectStore
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|DataContext
name|context
init|=
name|newInstance
argument_list|(
name|parent
argument_list|,
name|objectStore
argument_list|)
decl_stmt|;
name|context
operator|.
name|setValidatingObjectsOnCommit
argument_list|(
name|parent
operator|.
name|isValidatingObjectsOnCommit
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setUsingSharedSnapshotCache
argument_list|(
name|parent
operator|.
name|isUsingSharedSnapshotCache
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setQueryCache
argument_list|(
operator|new
name|NestedQueryCache
argument_list|(
name|queryCache
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|setTransactionFactory
argument_list|(
name|transactionFactory
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
specifier|protected
name|ObjectContext
name|createdFromDataDomain
parameter_list|(
name|DataDomain
name|parent
parameter_list|)
block|{
comment|// for new dataRowStores use the same name for all stores
comment|// it makes it easier to track the event subject
name|DataRowStore
name|snapshotCache
init|=
operator|(
name|parent
operator|.
name|isSharedCacheEnabled
argument_list|()
operator|)
condition|?
name|parent
operator|.
name|getSharedSnapshotCache
argument_list|()
else|:
name|dataRowStoreFactory
operator|.
name|createDataRowStore
argument_list|(
name|parent
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|DataContext
name|context
init|=
name|newInstance
argument_list|(
name|parent
argument_list|,
name|objectStoreFactory
operator|.
name|createObjectStore
argument_list|(
name|snapshotCache
argument_list|)
argument_list|)
decl_stmt|;
name|context
operator|.
name|setValidatingObjectsOnCommit
argument_list|(
name|parent
operator|.
name|isValidatingObjectsOnCommit
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setQueryCache
argument_list|(
operator|new
name|NestedQueryCache
argument_list|(
name|queryCache
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|setTransactionFactory
argument_list|(
name|transactionFactory
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
specifier|protected
name|DataContext
name|newInstance
parameter_list|(
name|DataChannel
name|parent
parameter_list|,
name|ObjectStore
name|objectStore
parameter_list|)
block|{
return|return
operator|new
name|DataContext
argument_list|(
name|parent
argument_list|,
name|objectStore
argument_list|)
return|;
block|}
block|}
end_class

end_unit

