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

begin_comment
comment|/**  * A translator of EJBQL UPDATE statements into SQL.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|EJBQLUpdateTranslator
extends|extends
name|EJBQLBaseVisitor
block|{
specifier|private
name|EJBQLTranslationContext
name|context
decl_stmt|;
specifier|private
name|int
name|itemCount
decl_stmt|;
name|EJBQLUpdateTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
name|EJBQLTranslationContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
specifier|public
name|boolean
name|visitUpdate
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|"UPDATE"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitFrom
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
name|expression
operator|.
name|visit
argument_list|(
operator|new
name|EJBQLFromTranslator
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
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
parameter_list|)
block|{
if|if
condition|(
name|itemCount
operator|++
operator|>
literal|0
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
else|else
block|{
name|context
operator|.
name|append
argument_list|(
literal|" SET"
argument_list|)
expr_stmt|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
name|visitUpdateValue
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

