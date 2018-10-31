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
name|project
operator|.
name|extension
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
name|ConfigurationNodeVisitor
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|XMLEncoder
import|;
end_import

begin_comment
comment|/**  * Delegate that handles saving XML of extension.  * {@link BaseSaverDelegate} should be used as a base class for custom delegates.  *  * @since 4.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|SaverDelegate
extends|extends
name|ConfigurationNodeVisitor
argument_list|<
name|Void
argument_list|>
block|{
comment|/**      * @param encoder provided by caller      */
name|void
name|setXMLEncoder
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
function_decl|;
comment|/**      * @param parentDelegate parent delegate, provided by caller      */
name|void
name|setParentDelegate
parameter_list|(
name|SaverDelegate
name|parentDelegate
parameter_list|)
function_decl|;
name|SaverDelegate
name|getParentDelegate
parameter_list|()
function_decl|;
name|Resource
name|getBaseDirectory
parameter_list|()
function_decl|;
name|void
name|setBaseDirectory
parameter_list|(
name|Resource
name|baseDirectory
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

