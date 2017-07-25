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
name|xml
operator|.
name|NamespaceAwareNestedTagHandler
import|;
end_import

begin_comment
comment|/**  * Delegate that handles loading process for extension specific parts of XML document.  *  * @since 4.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|LoaderDelegate
block|{
comment|/**      * @return target namespace that this extension is using      */
name|String
name|getTargetNamespace
parameter_list|()
function_decl|;
comment|/**      * Create handler that will handle parsing process further.      *      * @param parent parent handler      * @param tag current tag that in question      * @return new handler that will process tag or null if there is no interest in tag      */
name|NamespaceAwareNestedTagHandler
name|createHandler
parameter_list|(
name|NamespaceAwareNestedTagHandler
name|parent
parameter_list|,
name|String
name|tag
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

