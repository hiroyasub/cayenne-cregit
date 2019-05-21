begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *<p>  * https://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|rop
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
import|;
end_import

begin_class
specifier|public
class|class
name|ROPRequestContext
block|{
specifier|private
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|ROPRequestContext
argument_list|>
name|localContext
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|serviceId
decl_stmt|;
specifier|private
name|String
name|objectId
decl_stmt|;
specifier|private
name|ServletRequest
name|request
decl_stmt|;
specifier|private
name|ServletResponse
name|response
decl_stmt|;
specifier|private
name|int
name|count
decl_stmt|;
specifier|private
name|ROPRequestContext
parameter_list|()
block|{
block|}
specifier|public
specifier|static
name|void
name|start
parameter_list|(
name|String
name|serviceId
parameter_list|,
name|String
name|objectId
parameter_list|,
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|)
block|{
name|ROPRequestContext
name|context
init|=
name|localContext
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
operator|new
name|ROPRequestContext
argument_list|()
expr_stmt|;
name|localContext
operator|.
name|set
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|serviceId
operator|=
name|serviceId
expr_stmt|;
name|context
operator|.
name|objectId
operator|=
name|objectId
expr_stmt|;
name|context
operator|.
name|request
operator|=
name|request
expr_stmt|;
name|context
operator|.
name|response
operator|=
name|response
expr_stmt|;
name|context
operator|.
name|count
operator|++
expr_stmt|;
block|}
specifier|public
specifier|static
name|ROPRequestContext
name|getROPRequestContext
parameter_list|()
block|{
return|return
name|localContext
operator|.
name|get
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|getContextServiceId
parameter_list|()
block|{
name|ROPRequestContext
name|context
init|=
name|localContext
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
return|return
name|context
operator|.
name|serviceId
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
name|String
name|getContextObjectId
parameter_list|()
block|{
name|ROPRequestContext
name|context
init|=
name|localContext
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
return|return
name|context
operator|.
name|objectId
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
name|ServletRequest
name|getContextRequest
parameter_list|()
block|{
name|ROPRequestContext
name|context
init|=
name|localContext
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
return|return
name|context
operator|.
name|request
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
name|ServletResponse
name|getContextResponse
parameter_list|()
block|{
name|ROPRequestContext
name|context
init|=
name|localContext
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
return|return
name|context
operator|.
name|response
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
name|void
name|end
parameter_list|()
block|{
name|ROPRequestContext
name|context
init|=
name|localContext
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
operator|&&
operator|--
name|context
operator|.
name|count
operator|==
literal|0
condition|)
block|{
name|context
operator|.
name|request
operator|=
literal|null
expr_stmt|;
name|context
operator|.
name|response
operator|=
literal|null
expr_stmt|;
name|localContext
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

