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
name|enhancer
package|;
end_package

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|ClassVisitor
import|;
end_import

begin_comment
comment|/**  * A factory for the ASM ClassVisitors used during class enhancement.  *   * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|EnhancerVisitorFactory
block|{
comment|/**      * Creates and returns an ASM ClassVisitor for enhancing a class. Returned visitor is      * either null if no enhancement of this class is needed, or a wrapper around provided      * "out" ClassVisitor. Often it is a chain of visitors, each doing its own      * enhancement.      */
name|ClassVisitor
name|createVisitor
parameter_list|(
name|String
name|className
parameter_list|,
name|ClassVisitor
name|out
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

