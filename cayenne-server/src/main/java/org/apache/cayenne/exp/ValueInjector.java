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
name|exp
package|;
end_package

begin_comment
comment|/**  * Describes expressions, that can "inject" value to an object, i.e.  * do something reverse to "match" method. Preferrably, after  *   *<code>valueInjectorExpression.inject(object)</code>,  * valueInjectorExpression.match(object) returns<code>true</code>,  * but this is not a requirement, e.g. "and" expression can be able  * to inject only part of its operands  */
end_comment

begin_interface
specifier|public
interface|interface
name|ValueInjector
block|{
name|void
name|injectValue
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

