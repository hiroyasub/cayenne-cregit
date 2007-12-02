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
name|tools
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|AbstractMojo
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|MavenLogger
implements|implements
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
block|{
specifier|private
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|logging
operator|.
name|Log
name|logger
decl_stmt|;
specifier|public
name|MavenLogger
parameter_list|(
name|AbstractMojo
name|parent
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|parent
operator|.
name|getLog
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|debug
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|th
parameter_list|)
block|{
name|logger
operator|.
name|debug
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|message
argument_list|)
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|debug
parameter_list|(
name|Object
name|message
parameter_list|)
block|{
name|logger
operator|.
name|debug
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|th
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|message
argument_list|)
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|Object
name|message
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|fatal
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|th
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|message
argument_list|)
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|fatal
parameter_list|(
name|Object
name|message
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|info
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|th
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|message
argument_list|)
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|info
parameter_list|(
name|Object
name|message
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDebugEnabled
parameter_list|()
block|{
return|return
name|logger
operator|.
name|isDebugEnabled
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isErrorEnabled
parameter_list|()
block|{
return|return
name|logger
operator|.
name|isErrorEnabled
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isFatalEnabled
parameter_list|()
block|{
return|return
name|logger
operator|.
name|isErrorEnabled
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isInfoEnabled
parameter_list|()
block|{
return|return
name|logger
operator|.
name|isInfoEnabled
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isTraceEnabled
parameter_list|()
block|{
return|return
name|logger
operator|.
name|isDebugEnabled
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isWarnEnabled
parameter_list|()
block|{
return|return
name|logger
operator|.
name|isWarnEnabled
argument_list|()
return|;
block|}
specifier|public
name|void
name|trace
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|th
parameter_list|)
block|{
name|logger
operator|.
name|debug
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|message
argument_list|)
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|trace
parameter_list|(
name|Object
name|message
parameter_list|)
block|{
name|logger
operator|.
name|debug
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|warn
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|th
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|message
argument_list|)
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|warn
parameter_list|(
name|Object
name|message
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

