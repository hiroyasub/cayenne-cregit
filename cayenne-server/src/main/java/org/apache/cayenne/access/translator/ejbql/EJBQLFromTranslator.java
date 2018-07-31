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
name|translator
operator|.
name|ejbql
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
name|parser
operator|.
name|EJBQLFromItem
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
name|EJBQLJoin
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|EJBQLFromTranslator
extends|extends
name|EJBQLBaseVisitor
block|{
specifier|protected
name|EJBQLTranslationContext
name|context
decl_stmt|;
specifier|private
name|String
name|lastId
decl_stmt|;
specifier|private
name|EJBQLJoinAppender
name|joinAppender
decl_stmt|;
specifier|public
name|EJBQLFromTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|joinAppender
operator|=
name|context
operator|.
name|getTranslatorFactory
argument_list|()
operator|.
name|getJoinAppender
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
if|if
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
if|if
condition|(
name|lastId
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|markCurrentPosition
argument_list|(
name|EJBQLJoinAppender
operator|.
name|makeJoinTailMarker
argument_list|(
name|lastId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitFromItem
parameter_list|(
name|EJBQLFromItem
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
name|String
name|id
init|=
name|expression
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastId
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|context
operator|.
name|markCurrentPosition
argument_list|(
name|EJBQLJoinAppender
operator|.
name|makeJoinTailMarker
argument_list|(
name|lastId
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|lastId
operator|=
name|id
expr_stmt|;
name|joinAppender
operator|.
name|appendTable
argument_list|(
operator|new
name|EJBQLTableId
argument_list|(
name|id
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
name|visitInnerFetchJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
block|{
name|joinAppender
operator|.
name|appendInnerJoin
argument_list|(
literal|null
argument_list|,
operator|new
name|EJBQLTableId
argument_list|(
name|join
operator|.
name|getLeftHandSideId
argument_list|()
argument_list|)
argument_list|,
operator|new
name|EJBQLTableId
argument_list|(
name|join
operator|.
name|getRightHandSideId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|markCurrentPosition
argument_list|(
name|EJBQLJoinAppender
operator|.
name|makeJoinTailMarker
argument_list|(
name|join
operator|.
name|getRightHandSideId
argument_list|()
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
name|visitInnerJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
block|{
name|joinAppender
operator|.
name|appendInnerJoin
argument_list|(
literal|null
argument_list|,
operator|new
name|EJBQLTableId
argument_list|(
name|join
operator|.
name|getLeftHandSideId
argument_list|()
argument_list|)
argument_list|,
operator|new
name|EJBQLTableId
argument_list|(
name|join
operator|.
name|getRightHandSideId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|//fix 1341-mark current join position for probable future joins to this join
name|context
operator|.
name|markCurrentPosition
argument_list|(
name|EJBQLJoinAppender
operator|.
name|makeJoinTailMarker
argument_list|(
name|join
operator|.
name|getRightHandSideId
argument_list|()
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
name|visitOuterFetchJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
block|{
name|joinAppender
operator|.
name|appendOuterJoin
argument_list|(
literal|null
argument_list|,
operator|new
name|EJBQLTableId
argument_list|(
name|join
operator|.
name|getLeftHandSideId
argument_list|()
argument_list|)
argument_list|,
operator|new
name|EJBQLTableId
argument_list|(
name|join
operator|.
name|getRightHandSideId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|markCurrentPosition
argument_list|(
name|EJBQLJoinAppender
operator|.
name|makeJoinTailMarker
argument_list|(
name|join
operator|.
name|getRightHandSideId
argument_list|()
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
name|visitOuterJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
block|{
name|joinAppender
operator|.
name|appendOuterJoin
argument_list|(
literal|null
argument_list|,
operator|new
name|EJBQLTableId
argument_list|(
name|join
operator|.
name|getLeftHandSideId
argument_list|()
argument_list|)
argument_list|,
operator|new
name|EJBQLTableId
argument_list|(
name|join
operator|.
name|getRightHandSideId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|markCurrentPosition
argument_list|(
name|EJBQLJoinAppender
operator|.
name|makeJoinTailMarker
argument_list|(
name|join
operator|.
name|getRightHandSideId
argument_list|()
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

