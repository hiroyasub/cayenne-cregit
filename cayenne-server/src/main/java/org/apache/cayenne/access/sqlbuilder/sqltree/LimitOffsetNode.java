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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
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
name|access
operator|.
name|sqlbuilder
operator|.
name|QuotingAppendable
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|LimitOffsetNode
extends|extends
name|Node
block|{
specifier|protected
specifier|final
name|int
name|limit
decl_stmt|;
specifier|protected
specifier|final
name|int
name|offset
decl_stmt|;
specifier|public
name|LimitOffsetNode
parameter_list|(
name|int
name|limit
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
name|super
argument_list|(
name|NodeType
operator|.
name|LIMIT_OFFSET
argument_list|)
expr_stmt|;
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
name|this
operator|.
name|offset
operator|=
name|offset
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|QuotingAppendable
name|append
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
if|if
condition|(
name|limit
operator|==
literal|0
operator|&&
name|offset
operator|==
literal|0
condition|)
block|{
return|return
name|buffer
return|;
block|}
return|return
name|buffer
operator|.
name|append
argument_list|(
literal|" LIMIT "
argument_list|)
operator|.
name|append
argument_list|(
name|limit
argument_list|)
operator|.
name|append
argument_list|(
literal|" OFFSET "
argument_list|)
operator|.
name|append
argument_list|(
name|offset
argument_list|)
return|;
block|}
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
specifier|public
name|int
name|getOffset
parameter_list|()
block|{
return|return
name|offset
return|;
block|}
annotation|@
name|Override
specifier|public
name|Node
name|copy
parameter_list|()
block|{
return|return
operator|new
name|LimitOffsetNode
argument_list|(
name|limit
argument_list|,
name|offset
argument_list|)
return|;
block|}
block|}
end_class

end_unit

