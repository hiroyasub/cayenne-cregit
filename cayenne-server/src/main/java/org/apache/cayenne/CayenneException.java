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
name|LocalizedStringsHandler
import|;
end_import

begin_comment
comment|/**  * @deprecated please @since 4.1 use {@link CayenneRuntimeException}  *  * A generic checked exception that may be thrown by Cayenne framework. All checked  * exceptions in Cayenne inherit from this class.  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|CayenneException
extends|extends
name|Exception
block|{
specifier|private
specifier|static
name|String
name|exceptionLabel
decl_stmt|;
static|static
block|{
name|String
name|version
init|=
name|LocalizedStringsHandler
operator|.
name|getString
argument_list|(
literal|"cayenne.version"
argument_list|)
decl_stmt|;
name|String
name|date
init|=
name|LocalizedStringsHandler
operator|.
name|getString
argument_list|(
literal|"cayenne.build.date"
argument_list|)
decl_stmt|;
name|exceptionLabel
operator|=
literal|"[v."
operator|+
name|version
operator|+
literal|" "
operator|+
name|date
operator|+
literal|"] "
expr_stmt|;
block|}
comment|/**      *   @deprecated please @since 4.1 use {@link CayenneRuntimeException#getExceptionLabel()}      */
annotation|@
name|Deprecated
specifier|public
specifier|static
name|String
name|getExceptionLabel
parameter_list|()
block|{
return|return
name|exceptionLabel
return|;
block|}
comment|/**      * Creates new<code>CayenneException</code> without detail message.      */
specifier|public
name|CayenneException
parameter_list|()
block|{
block|}
comment|/**      * Constructs an<code>CayenneException</code> with the specified detail message.      *       * @param messageFormat the detail message format string.      */
specifier|public
name|CayenneException
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
name|String
operator|.
name|format
argument_list|(
name|messageFormat
argument_list|,
name|messageArgs
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructs an<code>CayenneException</code> that wraps a<code>cause</code> thrown      * elsewhere.      */
specifier|public
name|CayenneException
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
name|CayenneException
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
name|String
operator|.
name|format
argument_list|(
name|messageFormat
argument_list|,
name|messageArgs
argument_list|)
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
annotation|@
name|Override
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
name|getExceptionLabel
argument_list|()
operator|+
name|message
else|:
name|getExceptionLabel
argument_list|()
operator|+
literal|"(no message)"
return|;
block|}
block|}
end_class

end_unit

