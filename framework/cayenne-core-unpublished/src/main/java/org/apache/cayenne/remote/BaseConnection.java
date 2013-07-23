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
name|remote
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|CayenneRuntimeException
import|;
end_import

begin_comment
comment|/**  * A common base class for concrete ClientConnection implementations. Provides message  * logging functionality via commons-logging.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseConnection
implements|implements
name|ClientConnection
block|{
specifier|protected
name|Log
name|logger
decl_stmt|;
specifier|protected
name|long
name|messageId
decl_stmt|;
specifier|protected
name|long
name|readTimeout
init|=
literal|0L
decl_stmt|;
comment|/**      * Default constructor that initializes logging and a single threaded EventManager.      */
specifier|protected
name|BaseConnection
parameter_list|()
block|{
name|this
operator|.
name|logger
operator|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Invokes 'beforeSendMessage' on self, then invokes 'doSendMessage'. Implements basic      * logging functionality. Do not override this method unless absolutely necessary.      * Override 'beforeSendMessage' and 'doSendMessage' instead.      */
specifier|public
name|Object
name|sendMessage
parameter_list|(
name|ClientMessage
name|message
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null message"
argument_list|)
throw|;
block|}
name|beforeSendMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// log start...
name|long
name|t0
init|=
literal|0
decl_stmt|;
name|String
name|messageLabel
init|=
literal|""
decl_stmt|;
comment|// using sequential number for message id ... it can be useful for some basic
comment|// connector stats.
name|long
name|messageId
init|=
name|this
operator|.
name|messageId
operator|++
decl_stmt|;
if|if
condition|(
name|logger
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|t0
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
name|messageLabel
operator|=
name|message
operator|.
name|toString
argument_list|()
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"--- Message "
operator|+
name|messageId
operator|+
literal|": "
operator|+
name|messageLabel
argument_list|)
expr_stmt|;
block|}
name|Object
name|response
decl_stmt|;
try|try
block|{
name|response
operator|=
name|doSendMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
comment|// log error
if|if
condition|(
name|logger
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|long
name|time
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|t0
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"*** Message error for "
operator|+
name|messageId
operator|+
literal|": "
operator|+
name|messageLabel
operator|+
literal|" - took "
operator|+
name|time
operator|+
literal|" ms."
argument_list|)
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
comment|// log success...
if|if
condition|(
name|logger
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|long
name|time
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|t0
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"=== Message "
operator|+
name|messageId
operator|+
literal|": "
operator|+
name|messageLabel
operator|+
literal|" done - took "
operator|+
name|time
operator|+
literal|" ms."
argument_list|)
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
comment|/**      * Returns a count of processed messages since the beginning of life of this      * connector.      */
specifier|public
name|long
name|getProcessedMessagesCount
parameter_list|()
block|{
return|return
name|messageId
operator|+
literal|1
return|;
block|}
comment|/**      * The socket timeout on requests in milliseconds. Defaults to infinity.      *       * @since 3.1      */
specifier|public
name|long
name|getReadTimeout
parameter_list|()
block|{
return|return
name|readTimeout
return|;
block|}
comment|/**      * Sets the socket timeout.      *       * @param readTimeout The socket timeout on requests in milliseconds.      *       * @since 3.1      */
specifier|public
name|void
name|setReadTimeout
parameter_list|(
name|long
name|readTimeout
parameter_list|)
block|{
name|this
operator|.
name|readTimeout
operator|=
name|readTimeout
expr_stmt|;
block|}
comment|/**      * Called before logging the beginning of message processing.      */
specifier|protected
specifier|abstract
name|void
name|beforeSendMessage
parameter_list|(
name|ClientMessage
name|message
parameter_list|)
throws|throws
name|CayenneRuntimeException
function_decl|;
comment|/**      * The worker method invoked to process message.      */
specifier|protected
specifier|abstract
name|Object
name|doSendMessage
parameter_list|(
name|ClientMessage
name|message
parameter_list|)
throws|throws
name|CayenneRuntimeException
function_decl|;
block|}
end_class

end_unit
