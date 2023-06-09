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
name|cache
operator|.
name|invalidation
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
name|Persistent
import|;
end_import

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
name|function
operator|.
name|Function
import|;
end_import

begin_comment
comment|/**  * A pluggable handler to invalidate cache groups on changes in certain objects.  * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|InvalidationHandler
block|{
comment|/**      * @return invalidation function or null if this handler does not support invalidation of the given type.      */
name|Function
argument_list|<
name|Persistent
argument_list|,
name|Collection
argument_list|<
name|CacheGroupDescriptor
argument_list|>
argument_list|>
name|canHandle
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Persistent
argument_list|>
name|type
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

