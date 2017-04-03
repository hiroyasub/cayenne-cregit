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
name|lifecycle
operator|.
name|cache
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
name|Binder
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
name|ListBuilder
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
name|TransactionFilter
import|;
end_import

begin_comment
comment|/**  * This module is autoloaded, all extensions should be done via {@link CacheInvalidationModuleBuilder}.  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|CacheInvalidationModule
implements|implements
name|Module
block|{
specifier|static
name|ListBuilder
argument_list|<
name|InvalidationHandler
argument_list|>
name|contributeInvalidationHandler
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
return|return
name|binder
operator|.
name|bindList
argument_list|(
name|InvalidationHandler
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|contributeInvalidationHandler
argument_list|(
name|binder
argument_list|)
expr_stmt|;
comment|// want the filter to be INSIDE transaction
name|ServerModule
operator|.
name|contributeDomainFilters
argument_list|(
name|binder
argument_list|)
operator|.
name|insertBefore
argument_list|(
name|CacheInvalidationFilter
operator|.
name|class
argument_list|,
name|TransactionFilter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

