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
name|reflect
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
name|map
operator|.
name|Embeddable
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|EmbeddableDescriptor
block|{
comment|/**      * Creates a new instance of an embeddable class described by this object.      */
name|Object
name|createObject
parameter_list|(
name|Object
name|owner
parameter_list|,
name|String
name|embeddedProperty
parameter_list|)
function_decl|;
comment|/**      * Returns an embeddable class mapped by this descriptor.      */
name|Class
argument_list|<
name|?
argument_list|>
name|getObjectClass
parameter_list|()
function_decl|;
comment|/**      * Returns a metadata object for this descriptor.      */
name|Embeddable
name|getEmbeddable
parameter_list|()
function_decl|;
block|}
end_interface

end_unit
