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
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * Represents a placeholder for an unresolved relationship from a source object. Fault is  * resolved via {@link #resolveFault(Persistent, String)}. Depending on the type of fault  * it is resolved differently. Each type of fault is a singleton that can be obtained via  * corresponding static method.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|Fault
implements|implements
name|Serializable
block|{
specifier|protected
name|Fault
parameter_list|()
block|{
block|}
comment|/**      * Returns an object for a given source object and relationship.      */
specifier|public
specifier|abstract
name|Object
name|resolveFault
parameter_list|(
name|Persistent
name|sourceObject
parameter_list|,
name|String
name|relationshipName
parameter_list|)
function_decl|;
block|}
end_class

end_unit

