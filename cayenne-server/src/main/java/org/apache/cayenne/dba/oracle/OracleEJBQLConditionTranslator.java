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
name|dba
operator|.
name|oracle
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
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
name|access
operator|.
name|translator
operator|.
name|ejbql
operator|.
name|EJBQLConditionTranslator
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
name|access
operator|.
name|translator
operator|.
name|ejbql
operator|.
name|EJBQLMultiColumnOperand
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
name|access
operator|.
name|translator
operator|.
name|ejbql
operator|.
name|EJBQLPathTranslator
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
name|access
operator|.
name|translator
operator|.
name|ejbql
operator|.
name|EJBQLTranslationContext
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
name|parser
operator|.
name|Node
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
name|map
operator|.
name|ObjAttribute
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|OracleEJBQLConditionTranslator
extends|extends
name|EJBQLConditionTranslator
block|{
name|OracleEJBQLConditionTranslator
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
annotation|@
name|Override
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
name|expression
operator|.
name|visit
argument_list|(
operator|new
name|EJBQLPathTranslator
argument_list|(
name|context
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|appendMultiColumnPath
parameter_list|(
name|EJBQLMultiColumnOperand
name|operand
parameter_list|)
block|{
name|OracleEJBQLConditionTranslator
operator|.
name|this
operator|.
name|addMultiColumnOperand
argument_list|(
name|operand
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processTerminatingAttribute
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
if|if
condition|(
name|attribute
operator|.
name|getDbAttribute
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|CHAR
condition|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|OracleAdapter
operator|.
name|TRIM_FUNCTION
argument_list|)
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
name|super
operator|.
name|processTerminatingAttribute
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
name|context
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|processTerminatingAttribute
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|/**      * The order of arguments is inverted in Oracle.      * LOCATE(substr, str) -> INSTR(str, substr)      * @since 4.0      */
annotation|@
name|Override
specifier|public
name|boolean
name|visitLocate
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
operator|<
literal|0
condition|)
block|{
name|swapNodeChildren
argument_list|(
name|expression
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|append
argument_list|(
literal|" INSTR("
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|finishedChildIndex
operator|+
literal|1
operator|==
name|expression
operator|.
name|getChildrenCount
argument_list|()
condition|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
name|swapNodeChildren
argument_list|(
name|expression
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * @since 4.0      */
specifier|private
name|void
name|swapNodeChildren
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|i
parameter_list|,
name|int
name|j
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|expression
operator|instanceof
name|Node
operator|)
condition|)
block|{
return|return;
block|}
name|Node
name|node
init|=
operator|(
name|Node
operator|)
name|expression
decl_stmt|;
name|Node
name|tmp
init|=
name|node
operator|.
name|jjtGetChild
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|node
operator|.
name|jjtAddChild
argument_list|(
name|node
operator|.
name|jjtGetChild
argument_list|(
name|j
argument_list|)
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|node
operator|.
name|jjtAddChild
argument_list|(
name|tmp
argument_list|,
name|j
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

