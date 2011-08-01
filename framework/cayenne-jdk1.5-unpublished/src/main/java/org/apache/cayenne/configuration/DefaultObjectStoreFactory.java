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
name|configuration
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|Persistent
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
name|NoSyncObjectStore
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
name|ObjectMapRetainStrategy
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
name|di
operator|.
name|Inject
import|;
end_import

begin_comment
comment|/**  * A default implementation of {@link ObjectStoreFactory} which makes decision to  * turn {@link ObjectStore}'s syncing with parent {@link DataRowStore} on or off   * basing on {@link RuntimeProperties}.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DefaultObjectStoreFactory
implements|implements
name|ObjectStoreFactory
block|{
annotation|@
name|Inject
specifier|protected
name|RuntimeProperties
name|runtimeProperties
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ObjectMapRetainStrategy
name|retainStrategy
decl_stmt|;
specifier|public
name|ObjectStore
name|createObjectStore
parameter_list|(
name|DataRowStore
name|dataRowCache
parameter_list|)
block|{
return|return
name|createObjectStore
argument_list|(
name|dataRowCache
argument_list|,
name|retainStrategy
operator|.
name|createObjectMap
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|ObjectStore
name|createObjectStore
parameter_list|(
name|DataRowStore
name|dataRowCache
parameter_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Persistent
argument_list|>
name|objectMap
parameter_list|)
block|{
name|boolean
name|sync
init|=
name|runtimeProperties
operator|.
name|getBoolean
argument_list|(
name|SYNC_PROPERTY
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
name|sync
condition|?
operator|new
name|ObjectStore
argument_list|(
name|dataRowCache
argument_list|,
name|objectMap
argument_list|)
else|:
operator|new
name|NoSyncObjectStore
argument_list|(
name|dataRowCache
argument_list|,
name|objectMap
argument_list|)
return|;
block|}
block|}
end_class

end_unit

