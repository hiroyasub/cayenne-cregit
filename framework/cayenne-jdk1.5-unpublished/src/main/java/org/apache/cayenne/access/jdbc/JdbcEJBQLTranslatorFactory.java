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
name|dba
operator|.
name|DbAdapter
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
name|dba
operator|.
name|DefaultQuotingStrategy
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
name|dba
operator|.
name|QuotingSupport
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
comment|/**  * A default EJBQLTranslatorFactory.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|JdbcEJBQLTranslatorFactory
implements|implements
name|EJBQLTranslatorFactory
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|JOIN_APPENDER_KEY
init|=
literal|"$JoinAppender"
decl_stmt|;
specifier|protected
name|boolean
name|caseInsensitive
init|=
literal|false
decl_stmt|;
specifier|protected
name|QuotingSupport
name|quotingSupport
decl_stmt|;
specifier|public
name|JdbcEJBQLTranslatorFactory
parameter_list|()
block|{
name|initQuoting
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|initQuoting
parameter_list|()
block|{
name|quotingSupport
operator|=
operator|new
name|QuotingSupport
argument_list|(
operator|new
name|DefaultQuotingStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|QuotingSupport
name|getQuotingSupport
parameter_list|()
block|{
return|return
name|this
operator|.
name|quotingSupport
return|;
block|}
specifier|public
name|EJBQLJoinAppender
name|getJoinAppender
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
name|EJBQLJoinAppender
name|appender
init|=
operator|(
name|EJBQLJoinAppender
operator|)
name|context
operator|.
name|getAttribute
argument_list|(
name|JOIN_APPENDER_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|appender
operator|==
literal|null
condition|)
block|{
name|appender
operator|=
operator|new
name|EJBQLJoinAppender
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
name|JOIN_APPENDER_KEY
argument_list|,
name|appender
argument_list|)
expr_stmt|;
block|}
return|return
name|appender
return|;
block|}
specifier|public
name|EJBQLExpressionVisitor
name|getDeleteTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
name|context
operator|.
name|setUsingAliases
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
operator|new
name|EJBQLDeleteTranslator
argument_list|(
name|context
argument_list|)
return|;
block|}
specifier|public
name|EJBQLExpressionVisitor
name|getSelectTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|EJBQLSelectTranslator
argument_list|(
name|context
argument_list|)
return|;
block|}
specifier|public
name|EJBQLExpressionVisitor
name|getUpdateTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
name|context
operator|.
name|setUsingAliases
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
operator|new
name|EJBQLUpdateTranslator
argument_list|(
name|context
argument_list|)
return|;
block|}
specifier|public
name|EJBQLExpressionVisitor
name|getAggregateColumnTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|EJBQLAggregateColumnTranslator
argument_list|(
name|context
argument_list|)
return|;
block|}
specifier|public
name|EJBQLExpressionVisitor
name|getConditionTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
name|context
operator|.
name|setCaseInsensitive
argument_list|(
name|caseInsensitive
argument_list|)
expr_stmt|;
return|return
operator|new
name|EJBQLConditionTranslator
argument_list|(
name|context
argument_list|)
return|;
block|}
specifier|public
name|EJBQLExpressionVisitor
name|getFromTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|EJBQLFromTranslator
argument_list|(
name|context
argument_list|)
return|;
block|}
specifier|public
name|EJBQLExpressionVisitor
name|getGroupByTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|EJBQLGroupByTranslator
argument_list|(
name|context
argument_list|)
return|;
block|}
specifier|public
name|EJBQLExpressionVisitor
name|getIdentifierColumnsTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|context
operator|.
name|getMetadata
argument_list|()
operator|.
name|getPageSize
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
operator|new
name|EJBQLIdColumnsTranslator
argument_list|(
name|context
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|EJBQLIdentifierColumnsTranslator
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
specifier|public
name|EJBQLExpressionVisitor
name|getOrderByTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|EJBQLOrderByTranslator
argument_list|(
name|context
argument_list|)
return|;
block|}
specifier|public
name|EJBQLExpressionVisitor
name|getSelectColumnsTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|EJBQLSelectColumnsTranslator
argument_list|(
name|context
argument_list|)
return|;
block|}
specifier|public
name|EJBQLExpressionVisitor
name|getUpdateItemTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|EJBQLUpdateItemTranslator
argument_list|(
name|context
argument_list|)
return|;
block|}
specifier|public
name|void
name|setCaseInsensitive
parameter_list|(
name|boolean
name|caseInsensitive
parameter_list|)
block|{
name|this
operator|.
name|caseInsensitive
operator|=
name|caseInsensitive
expr_stmt|;
block|}
block|}
end_class

end_unit

