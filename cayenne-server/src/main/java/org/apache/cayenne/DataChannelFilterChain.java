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
name|graph
operator|.
name|GraphDiff
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
name|query
operator|.
name|Query
import|;
end_import

begin_comment
comment|/**  * Provides DataChannelFilters with API to pass control to the next filter in the chain or  * the underlying DataChannel for the last chain filter.  *   * @since 3.1  * @deprecated since 4.1 in favor of {@link DataChannelSyncFilterChain} and {@link DataChannelQueryFilterChain}  */
end_comment

begin_interface
specifier|public
interface|interface
name|DataChannelFilterChain
block|{
specifier|default
name|QueryResponse
name|onQuery
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|default
name|GraphDiff
name|onSync
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|GraphDiff
name|changes
parameter_list|,
name|int
name|syncType
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_interface

end_unit

