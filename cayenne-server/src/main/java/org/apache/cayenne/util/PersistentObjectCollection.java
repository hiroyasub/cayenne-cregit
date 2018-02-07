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
name|util
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

begin_comment
comment|/**  * Simple interface to unify PersistentObject* collections in way of  * setting properties directly  * @see org.apache.cayenne.reflect.ToManyMapProperty  * @since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|PersistentObjectCollection
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Collection
argument_list|<
name|E
argument_list|>
block|{
comment|/**      * Adds an object without triggering an event       */
name|void
name|addDirectly
parameter_list|(
name|E
name|target
parameter_list|)
function_decl|;
comment|/**      * Removes an object without triggering an event       */
name|void
name|removeDirectly
parameter_list|(
name|E
name|target
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

