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
name|ConfigurationException
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
name|resource
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * An object that can load a named {@link DataChannelDescriptor} from some configuration  * source.  *   * @since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|DataChannelDescriptorLoader
block|{
comment|/**      * Loads a DataChannelDescriptor from some configuration resource, usually an XML file      * found on classpath.      */
name|ConfigurationTree
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|load
parameter_list|(
name|Resource
name|configurationResource
parameter_list|)
throws|throws
name|ConfigurationException
function_decl|;
block|}
end_interface

end_unit

