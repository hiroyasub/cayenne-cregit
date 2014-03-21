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
name|dba
operator|.
name|oracle
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|select
operator|.
name|QueryAssembler
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
name|select
operator|.
name|TrimmingQualifierTranslator
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|parser
operator|.
name|ASTIn
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
name|exp
operator|.
name|parser
operator|.
name|ASTList
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
name|exp
operator|.
name|parser
operator|.
name|ASTNegate
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
name|exp
operator|.
name|parser
operator|.
name|ASTNotIn
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
name|exp
operator|.
name|parser
operator|.
name|ASTPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|Transformer
import|;
end_import

begin_comment
comment|/**  * Oracle qualifier translator. In particular, trims INs with more than 1000 elements to  * an OR-set of INs with&lt;= 1000 elements  */
end_comment

begin_class
specifier|public
class|class
name|OracleQualifierTranslator
extends|extends
name|TrimmingQualifierTranslator
block|{
specifier|public
name|OracleQualifierTranslator
parameter_list|(
name|QueryAssembler
name|queryAssembler
parameter_list|)
block|{
name|super
argument_list|(
name|queryAssembler
argument_list|,
name|OracleAdapter
operator|.
name|TRIM_FUNCTION
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doAppendPart
parameter_list|(
name|Expression
name|rootNode
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|rootNode
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|rootNode
operator|=
name|rootNode
operator|.
name|transform
argument_list|(
operator|new
name|INTrimmer
argument_list|()
argument_list|)
expr_stmt|;
name|rootNode
operator|.
name|traverse
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|INTrimmer
implements|implements
name|Transformer
block|{
specifier|public
name|Expression
name|trimmedInExpression
parameter_list|(
name|Expression
name|exp
parameter_list|,
name|int
name|maxInSize
parameter_list|)
block|{
name|Expression
name|list
init|=
operator|(
name|Expression
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Object
index|[]
name|objects
init|=
operator|(
name|Object
index|[]
operator|)
name|list
operator|.
name|evaluate
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|objects
operator|.
name|length
operator|<=
name|maxInSize
condition|)
block|{
return|return
name|exp
return|;
block|}
name|Expression
name|trimmed
init|=
name|trimmedInExpression
argument_list|(
operator|(
name|ASTPath
operator|)
name|exp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|,
name|objects
argument_list|,
name|maxInSize
argument_list|)
decl_stmt|;
if|if
condition|(
name|exp
operator|instanceof
name|ASTNotIn
condition|)
block|{
return|return
operator|new
name|ASTNegate
argument_list|(
name|trimmed
argument_list|)
return|;
block|}
return|return
name|trimmed
return|;
block|}
name|Expression
name|trimmedInExpression
parameter_list|(
name|ASTPath
name|path
parameter_list|,
name|Object
index|[]
name|values
parameter_list|,
name|int
name|maxInSize
parameter_list|)
block|{
name|Expression
name|res
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|in
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|maxInSize
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|v
range|:
name|values
control|)
block|{
name|in
operator|.
name|add
argument_list|(
name|v
argument_list|)
expr_stmt|;
if|if
condition|(
name|in
operator|.
name|size
argument_list|()
operator|==
name|maxInSize
condition|)
block|{
name|Expression
name|inExp
init|=
operator|new
name|ASTIn
argument_list|(
name|path
argument_list|,
operator|new
name|ASTList
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|res
operator|=
name|res
operator|!=
literal|null
condition|?
name|res
operator|.
name|orExp
argument_list|(
name|inExp
argument_list|)
else|:
name|inExp
expr_stmt|;
name|in
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|maxInSize
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|in
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Expression
name|inExp
init|=
operator|new
name|ASTIn
argument_list|(
name|path
argument_list|,
operator|new
name|ASTList
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|res
operator|=
name|res
operator|!=
literal|null
condition|?
name|res
operator|.
name|orExp
argument_list|(
name|inExp
argument_list|)
else|:
name|inExp
expr_stmt|;
block|}
return|return
name|res
return|;
block|}
specifier|public
name|Object
name|transform
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
if|if
condition|(
name|input
operator|instanceof
name|ASTIn
operator|||
name|input
operator|instanceof
name|ASTNotIn
condition|)
block|{
return|return
name|trimmedInExpression
argument_list|(
operator|(
name|Expression
operator|)
name|input
argument_list|,
literal|1000
argument_list|)
return|;
block|}
return|return
name|input
return|;
block|}
block|}
block|}
end_class

end_unit

