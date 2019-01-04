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
name|TrimmingColumnNode
extends|extends
name|Node
block|{
specifier|protected
specifier|final
name|ColumnNode
name|columnNode
decl_stmt|;
specifier|public
name|TrimmingColumnNode
parameter_list|(
name|ColumnNode
name|columnNode
parameter_list|)
block|{
name|this
operator|.
name|columnNode
operator|=
name|columnNode
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
name|boolean
name|isResult
init|=
name|isResultNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|columnNode
operator|.
name|getAlias
argument_list|()
operator|==
literal|null
operator|||
name|isResult
condition|)
block|{
if|if
condition|(
name|isCharType
argument_list|()
condition|)
block|{
name|appendRtrim
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|appendAlias
argument_list|(
name|buffer
argument_list|,
name|isResult
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|isComparisionWithClob
argument_list|()
condition|)
block|{
name|appendClobColumnNode
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|appendAlias
argument_list|(
name|buffer
argument_list|,
name|isResult
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|columnNode
operator|.
name|append
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|appendAlias
argument_list|(
name|buffer
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
return|;
block|}
specifier|private
name|boolean
name|isComparisionWithClob
parameter_list|()
block|{
return|return
operator|(
name|getParent
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
name|NodeType
operator|.
name|EQUALITY
operator|||
name|getParent
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
name|NodeType
operator|.
name|LIKE
operator|)
operator|&&
name|columnNode
operator|.
name|getAttribute
argument_list|()
operator|!=
literal|null
operator|&&
name|columnNode
operator|.
name|getAttribute
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|CLOB
return|;
block|}
specifier|protected
name|void
name|appendRtrim
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" RTRIM("
argument_list|)
expr_stmt|;
name|appendColumnNode
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|isCharType
parameter_list|()
block|{
return|return
name|columnNode
operator|.
name|getAttribute
argument_list|()
operator|!=
literal|null
operator|&&
name|columnNode
operator|.
name|getAttribute
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|CHAR
return|;
block|}
specifier|protected
name|boolean
name|isResultNode
parameter_list|()
block|{
name|Node
name|parent
init|=
name|getParent
argument_list|()
decl_stmt|;
while|while
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|parent
operator|.
name|getType
argument_list|()
operator|==
name|NodeType
operator|.
name|RESULT
condition|)
block|{
return|return
literal|true
return|;
block|}
name|parent
operator|=
name|parent
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|void
name|appendClobColumnNode
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" CAST("
argument_list|)
expr_stmt|;
name|appendColumnNode
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|" AS VARCHAR("
argument_list|)
operator|.
name|append
argument_list|(
name|getColumnSize
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"))"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|appendColumnNode
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
if|if
condition|(
name|columnNode
operator|.
name|getTable
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|appendQuoted
argument_list|(
name|columnNode
operator|.
name|getTable
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|appendQuoted
argument_list|(
name|columnNode
operator|.
name|getColumn
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|appendAlias
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|,
name|boolean
name|isResult
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isResult
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|columnNode
operator|.
name|getAlias
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|appendQuoted
argument_list|(
name|columnNode
operator|.
name|getAlias
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|int
name|getColumnSize
parameter_list|()
block|{
name|int
name|size
init|=
name|columnNode
operator|.
name|getAttribute
argument_list|()
operator|.
name|getMaxLength
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
return|return
name|size
return|;
block|}
name|int
name|siblings
init|=
name|getParent
argument_list|()
operator|.
name|getChildrenCount
argument_list|()
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
name|siblings
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|sibling
init|=
name|getParent
argument_list|()
operator|.
name|getChild
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|sibling
operator|==
name|this
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|sibling
operator|.
name|getType
argument_list|()
operator|==
name|NodeType
operator|.
name|VALUE
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|ValueNode
operator|)
name|sibling
operator|)
operator|.
name|getValue
argument_list|()
operator|instanceof
name|CharSequence
condition|)
block|{
return|return
operator|(
operator|(
name|CharSequence
operator|)
operator|(
operator|(
name|ValueNode
operator|)
name|sibling
operator|)
operator|.
name|getValue
argument_list|()
operator|)
operator|.
name|length
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|255
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
name|TrimmingColumnNode
argument_list|(
name|columnNode
operator|.
name|deepCopy
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

