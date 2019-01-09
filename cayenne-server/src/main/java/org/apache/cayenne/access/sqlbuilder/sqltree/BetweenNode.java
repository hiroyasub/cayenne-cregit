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
name|BetweenNode
extends|extends
name|ExpressionNode
block|{
specifier|private
specifier|final
name|boolean
name|not
decl_stmt|;
specifier|public
name|BetweenNode
parameter_list|(
name|boolean
name|not
parameter_list|)
block|{
name|this
operator|.
name|not
operator|=
name|not
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|appendChildrenSeparator
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|,
name|int
name|childIdx
parameter_list|)
block|{
if|if
condition|(
name|childIdx
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|not
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" NOT"
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|" BETWEEN"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" AND"
argument_list|)
expr_stmt|;
block|}
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
name|BetweenNode
argument_list|(
name|not
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isNot
parameter_list|()
block|{
return|return
name|not
return|;
block|}
block|}
end_class

end_unit

