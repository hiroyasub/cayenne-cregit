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

begin_comment
comment|/**  * A runtime exception thrown on failures in Cayenne configuration.  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurationException
extends|extends
name|CayenneRuntimeException
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|3991782831459956981L
decl_stmt|;
comment|/** 	 * Creates new<code>ConfigurationException</code> without detail message. 	 */
specifier|public
name|ConfigurationException
parameter_list|()
block|{
block|}
comment|/** 	 * Constructs an exception with the specified message with an optional list 	 * of message formatting arguments. Message formatting rules follow 	 * "String.format(..)" conventions. 	 */
specifier|public
name|ConfigurationException
parameter_list|(
name|String
name|messageFormat
parameter_list|,
name|Object
modifier|...
name|messageArgs
parameter_list|)
block|{
name|super
argument_list|(
name|messageFormat
argument_list|,
name|messageArgs
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Constructs an exception wrapping another exception thrown elsewhere. 	 */
specifier|public
name|ConfigurationException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ConfigurationException
parameter_list|(
name|String
name|messageFormat
parameter_list|,
name|Throwable
name|cause
parameter_list|,
name|Object
modifier|...
name|messageArgs
parameter_list|)
block|{
name|super
argument_list|(
name|messageFormat
argument_list|,
name|cause
argument_list|,
name|messageArgs
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

