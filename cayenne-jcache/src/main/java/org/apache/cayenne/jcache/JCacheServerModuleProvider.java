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
name|jcache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|configuration
operator|.
name|server
operator|.
name|CayenneServerModuleProvider
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
name|server
operator|.
name|ServerModule
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
name|Module
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|JCacheServerModuleProvider
implements|implements
name|CayenneServerModuleProvider
block|{
annotation|@
name|Override
specifier|public
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|JCacheModule
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
name|moduleType
parameter_list|()
block|{
return|return
name|JCacheModule
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
argument_list|>
name|overrides
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|ServerModule
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

