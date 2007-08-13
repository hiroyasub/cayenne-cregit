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
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|EJBQLException
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

begin_comment
comment|/**  * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|EJBQLUpdateItemTranslator
extends|extends
name|EJBQLConditionTranslator
block|{
name|EJBQLUpdateItemTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|visitPath
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
comment|// unexpected, but make sure super is not called....
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|visitUpdateItem
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
if|if
condition|(
name|finishedChildIndex
operator|==
name|expression
operator|.
name|getChildrenCount
argument_list|()
operator|-
literal|1
condition|)
block|{
comment|// check multicolumn match condition and undo op insertion and append it
comment|// from scratch if needed
if|if
condition|(
name|multiColumnOperands
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|multiColumnOperands
operator|.
name|size
argument_list|()
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|EJBQLException
argument_list|(
literal|"Invalid multi-column equals expression. Expected 2 multi-column operands, got "
operator|+
name|multiColumnOperands
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
name|context
operator|.
name|trim
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|EJBQLMultiColumnOperand
name|lhs
init|=
operator|(
name|EJBQLMultiColumnOperand
operator|)
name|multiColumnOperands
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|EJBQLMultiColumnOperand
name|rhs
init|=
operator|(
name|EJBQLMultiColumnOperand
operator|)
name|multiColumnOperands
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|lhs
operator|.
name|getKeys
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|key
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|lhs
operator|.
name|appendValue
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|context
operator|.
name|append
argument_list|(
literal|" ="
argument_list|)
expr_stmt|;
name|rhs
operator|.
name|appendValue
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
block|}
name|multiColumnOperands
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitUpdateField
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
name|EJBQLPathTranslator
name|pathTranslator
init|=
operator|new
name|EJBQLPathTranslator
argument_list|(
name|context
argument_list|)
block|{
specifier|protected
name|void
name|appendMultiColumnPath
parameter_list|(
name|EJBQLMultiColumnOperand
name|operand
parameter_list|)
block|{
name|EJBQLUpdateItemTranslator
operator|.
name|this
operator|.
name|addMultiColumnOperand
argument_list|(
name|operand
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|visitUpdateField
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|visitPath
argument_list|(
name|expression
argument_list|,
name|finishedChildIndex
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|expression
operator|.
name|visit
argument_list|(
name|pathTranslator
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|visitEquals
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
comment|// unlike super, Equals here has no children and is itself a child of UpdateItem
name|context
operator|.
name|append
argument_list|(
literal|" ="
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|visitUpdateValue
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
comment|// a criteria for NULL is UpdateValue with no children
if|if
condition|(
name|expression
operator|.
name|getChildrenCount
argument_list|()
operator|==
literal|0
condition|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|" NULL"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

