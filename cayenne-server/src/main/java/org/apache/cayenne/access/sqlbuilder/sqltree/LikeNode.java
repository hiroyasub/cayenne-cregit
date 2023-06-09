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
comment|/**  * expressions: LIKE, ILIKE, NOT LIKE, NOT ILIKE + ESCAPE  *  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|LikeNode
extends|extends
name|ExpressionNode
block|{
specifier|protected
specifier|final
name|boolean
name|ignoreCase
decl_stmt|;
specifier|protected
specifier|final
name|boolean
name|not
decl_stmt|;
specifier|protected
specifier|final
name|char
name|escape
decl_stmt|;
specifier|public
name|LikeNode
parameter_list|(
name|boolean
name|ignoreCase
parameter_list|,
name|boolean
name|not
parameter_list|,
name|char
name|escape
parameter_list|)
block|{
name|super
argument_list|(
name|NodeType
operator|.
name|LIKE
argument_list|)
expr_stmt|;
name|this
operator|.
name|ignoreCase
operator|=
name|ignoreCase
expr_stmt|;
name|this
operator|.
name|not
operator|=
name|not
expr_stmt|;
name|this
operator|.
name|escape
operator|=
name|escape
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|appendChildrenStart
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
if|if
condition|(
name|ignoreCase
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" UPPER("
argument_list|)
expr_stmt|;
block|}
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
name|ignoreCase
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
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
literal|" LIKE"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ignoreCase
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" UPPER("
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|appendChildrenEnd
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
if|if
condition|(
name|ignoreCase
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|escape
operator|!=
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" ESCAPE '"
argument_list|)
operator|.
name|append
argument_list|(
name|escape
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
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
name|LikeNode
argument_list|(
name|ignoreCase
argument_list|,
name|not
argument_list|,
name|escape
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isIgnoreCase
parameter_list|()
block|{
return|return
name|ignoreCase
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
specifier|public
name|char
name|getEscape
parameter_list|()
block|{
return|return
name|escape
return|;
block|}
block|}
end_class

end_unit

