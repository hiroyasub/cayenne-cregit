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
package|;
end_package

begin_comment
comment|/**  * A generic unchecked exception that may be thrown by Cayenne framework. All runtime  * exceptions in Cayenne inherit from this class.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|CayenneRuntimeException
extends|extends
name|RuntimeException
block|{
comment|/**      * Creates new CayenneRuntimeException without detail message.      */
specifier|public
name|CayenneRuntimeException
parameter_list|()
block|{
block|}
comment|/**      * Constructs an<code>CayenneRuntimeException</code> with the specified detail      * message.      *       * @param message the detail message.      */
specifier|public
name|CayenneRuntimeException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructs an<code>CayenneRuntimeException</code> that wraps      *<code>exception</code> thrown elsewhere.      */
specifier|public
name|CayenneRuntimeException
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
name|CayenneRuntimeException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns exception message without Cayenne version label.      *       * @since 1.1      */
specifier|public
name|String
name|getUnlabeledMessage
parameter_list|()
block|{
return|return
name|super
operator|.
name|getMessage
argument_list|()
return|;
block|}
comment|/**      * Returns message that includes Cayenne version label and the actual exception      * message.      */
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
name|String
name|message
init|=
name|super
operator|.
name|getMessage
argument_list|()
decl_stmt|;
return|return
operator|(
name|message
operator|!=
literal|null
operator|)
condition|?
name|CayenneException
operator|.
name|getExceptionLabel
argument_list|()
operator|+
name|message
else|:
name|CayenneException
operator|.
name|getExceptionLabel
argument_list|()
operator|+
literal|"(no message)"
return|;
block|}
block|}
end_class

end_unit

