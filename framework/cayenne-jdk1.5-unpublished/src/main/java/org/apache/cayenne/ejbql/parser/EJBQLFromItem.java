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
name|ejbql
operator|.
name|parser
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
name|ejbql
operator|.
name|EJBQLBaseVisitor
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
name|ejbql
operator|.
name|EJBQLExpression
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
name|ejbql
operator|.
name|EJBQLExpressionVisitor
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|EJBQLFromItem
extends|extends
name|SimpleNode
block|{
specifier|public
name|EJBQLFromItem
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns an id generated from the entity name. It is used when no user-specified id      * exists.      */
specifier|public
name|String
name|getSyntheticId
parameter_list|()
block|{
name|int
name|len
init|=
name|getChildrenCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|len
operator|<
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|String
index|[]
name|entityNames
init|=
operator|new
name|String
index|[
literal|1
index|]
decl_stmt|;
name|getChild
argument_list|(
literal|0
argument_list|)
operator|.
name|visit
argument_list|(
operator|new
name|EJBQLBaseVisitor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|visitIdentificationVariable
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|entityNames
index|[
literal|0
index|]
operator|=
name|expression
operator|.
name|getText
argument_list|()
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|entityNames
index|[
literal|0
index|]
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// id's are case insensitive, while entity names are. Using simple encoding to
comment|// transform the entity name in such way that two entities that differ only in
comment|// capitalization would produce different lowercase ids
name|StringBuilder
name|id
init|=
operator|new
name|StringBuilder
argument_list|(
name|entityNames
index|[
literal|0
index|]
operator|.
name|length
argument_list|()
operator|+
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|entityNames
index|[
literal|0
index|]
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|entityNames
index|[
literal|0
index|]
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isUpperCase
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|id
operator|.
name|append
argument_list|(
literal|'%'
argument_list|)
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toLowerCase
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|id
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|id
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
name|int
name|len
init|=
name|getChildrenCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|len
operator|<
literal|2
condition|)
block|{
return|return
name|getSyntheticId
argument_list|()
return|;
block|}
return|return
name|jjtGetChild
argument_list|(
name|len
operator|-
literal|1
argument_list|)
operator|.
name|getText
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|visitNode
parameter_list|(
name|EJBQLExpressionVisitor
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|visitFromItem
argument_list|(
name|this
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|visitChild
parameter_list|(
name|EJBQLExpressionVisitor
name|visitor
parameter_list|,
name|int
name|childIndex
parameter_list|)
block|{
return|return
name|super
operator|.
name|visitChild
argument_list|(
name|visitor
argument_list|,
name|childIndex
argument_list|)
operator|&&
name|visitor
operator|.
name|visitFromItem
argument_list|(
name|this
argument_list|,
name|childIndex
argument_list|)
return|;
block|}
block|}
end_class

end_unit

