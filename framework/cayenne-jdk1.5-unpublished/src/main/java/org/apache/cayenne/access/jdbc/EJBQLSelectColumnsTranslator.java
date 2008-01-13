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
name|DbAttribute
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
name|DbEntity
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
comment|/**  * Translator of the EJBQL select clause.  *   * @author Andrus Adamchik  * @since 3.0  */
end_comment

begin_class
class|class
name|EJBQLSelectColumnsTranslator
extends|extends
name|EJBQLBaseVisitor
block|{
specifier|private
name|EJBQLTranslationContext
name|context
decl_stmt|;
specifier|private
name|int
name|expressionsCount
decl_stmt|;
name|EJBQLSelectColumnsTranslator
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
annotation|@
name|Override
specifier|public
name|boolean
name|visitSelectExpression
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
if|if
condition|(
name|expressionsCount
operator|++
operator|>
literal|0
condition|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitAggregate
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|expression
operator|.
name|visit
argument_list|(
name|context
operator|.
name|getTranslatorFactory
argument_list|()
operator|.
name|getAggregateColumnTranslator
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
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
name|EJBQLPathTranslator
name|pathTranslator
init|=
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
throw|throw
operator|new
name|EJBQLException
argument_list|(
literal|"Can't use multi-column paths in column clause"
argument_list|)
throw|;
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
name|DbEntity
name|table
init|=
name|currentEntity
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|String
name|alias
init|=
name|this
operator|.
name|lastAlias
operator|!=
literal|null
condition|?
name|lastAlias
else|:
name|context
operator|.
name|getTableAlias
argument_list|(
name|idPath
argument_list|,
name|table
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
decl_stmt|;
name|DbAttribute
name|dbAttribute
init|=
name|attribute
operator|.
name|getDbAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|isAppendingResultColumns
argument_list|()
condition|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|" #result('"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|append
argument_list|(
name|alias
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|dbAttribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|context
operator|.
name|isAppendingResultColumns
argument_list|()
condition|)
block|{
name|String
name|columnAlias
init|=
name|context
operator|.
name|nextColumnAlias
argument_list|()
decl_stmt|;
comment|// TODO: andrus 6/27/2007 - the last parameter is an unofficial
comment|// "jdbcType"
comment|// pending CAY-813 implementation, switch to #column directive
name|context
operator|.
name|append
argument_list|(
literal|"' '"
argument_list|)
operator|.
name|append
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"' '"
argument_list|)
operator|.
name|append
argument_list|(
name|columnAlias
argument_list|)
operator|.
name|append
argument_list|(
literal|"' '"
argument_list|)
operator|.
name|append
argument_list|(
name|columnAlias
argument_list|)
operator|.
name|append
argument_list|(
literal|"' "
operator|+
name|dbAttribute
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
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
annotation|@
name|Override
specifier|public
name|boolean
name|visitIdentifier
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|expression
operator|.
name|visit
argument_list|(
name|context
operator|.
name|getTranslatorFactory
argument_list|()
operator|.
name|getIdentifierColumnsTranslator
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

