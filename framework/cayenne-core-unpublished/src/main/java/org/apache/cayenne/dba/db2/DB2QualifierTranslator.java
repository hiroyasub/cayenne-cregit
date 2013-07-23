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
name|db2
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
name|CayenneRuntimeException
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
name|trans
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
name|trans
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
name|dba
operator|.
name|TypesMapping
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
name|ASTEqual
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
name|ASTNotEqual
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
name|SimpleNode
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DB2QualifierTranslator
extends|extends
name|TrimmingQualifierTranslator
block|{
specifier|public
name|DB2QualifierTranslator
parameter_list|(
name|QueryAssembler
name|queryAssembler
parameter_list|,
name|String
name|trimFunction
parameter_list|)
block|{
name|super
argument_list|(
name|queryAssembler
argument_list|,
name|trimFunction
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendLiteralDirect
parameter_list|(
name|Object
name|val
parameter_list|,
name|DbAttribute
name|attr
parameter_list|,
name|Expression
name|parentExpression
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|castNeeded
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|parentExpression
operator|!=
literal|null
condition|)
block|{
name|int
name|type
init|=
name|parentExpression
operator|.
name|getType
argument_list|()
decl_stmt|;
name|castNeeded
operator|=
name|attr
operator|!=
literal|null
operator|&&
operator|(
name|type
operator|==
name|Expression
operator|.
name|LIKE
operator|||
name|type
operator|==
name|Expression
operator|.
name|LIKE_IGNORE_CASE
operator|||
name|type
operator|==
name|Expression
operator|.
name|NOT_LIKE
operator|||
name|type
operator|==
name|Expression
operator|.
name|NOT_LIKE_IGNORE_CASE
operator|)
expr_stmt|;
block|}
if|if
condition|(
name|castNeeded
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"CAST ("
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|appendLiteralDirect
argument_list|(
name|val
argument_list|,
name|attr
argument_list|,
name|parentExpression
argument_list|)
expr_stmt|;
if|if
condition|(
name|castNeeded
condition|)
block|{
name|int
name|jdbcType
init|=
name|attr
operator|.
name|getType
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|attr
operator|.
name|getMaxLength
argument_list|()
decl_stmt|;
comment|// determine CAST type
comment|// LIKE on CHAR may produce unpredictible results
comment|// LIKE on LONVARCHAR doesn't seem to be supported
if|if
condition|(
name|jdbcType
operator|==
name|Types
operator|.
name|CHAR
operator|||
name|jdbcType
operator|==
name|Types
operator|.
name|LONGVARCHAR
condition|)
block|{
name|jdbcType
operator|=
name|Types
operator|.
name|VARCHAR
expr_stmt|;
comment|// length is required for VARCHAR
if|if
condition|(
name|len
operator|<=
literal|0
condition|)
block|{
name|len
operator|=
literal|254
expr_stmt|;
block|}
block|}
name|out
operator|.
name|append
argument_list|(
literal|" AS "
argument_list|)
expr_stmt|;
name|String
index|[]
name|types
init|=
name|queryAssembler
operator|.
name|getAdapter
argument_list|()
operator|.
name|externalTypesForJdbcType
argument_list|(
name|jdbcType
argument_list|)
decl_stmt|;
if|if
condition|(
name|types
operator|==
literal|null
operator|||
name|types
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't find database type for JDBC type '"
operator|+
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|jdbcType
argument_list|)
argument_list|)
throw|;
block|}
name|out
operator|.
name|append
argument_list|(
name|types
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|len
operator|>
literal|0
operator|&&
name|TypesMapping
operator|.
name|supportsLength
argument_list|(
name|jdbcType
argument_list|)
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|len
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processColumnWithQuoteSqlIdentifiers
parameter_list|(
name|DbAttribute
name|dbAttr
parameter_list|,
name|Expression
name|pathExp
parameter_list|)
throws|throws
name|IOException
block|{
name|SimpleNode
name|parent
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|pathExp
operator|instanceof
name|SimpleNode
condition|)
block|{
name|parent
operator|=
operator|(
name|SimpleNode
operator|)
operator|(
operator|(
name|SimpleNode
operator|)
name|pathExp
operator|)
operator|.
name|jjtGetParent
argument_list|()
expr_stmt|;
block|}
comment|// problem in db2 : Comparisons between CLOB and CLOB are not supported.
comment|// we need do it by casting the Clob to VARCHAR.
if|if
condition|(
name|parent
operator|!=
literal|null
operator|&&
operator|(
name|parent
operator|instanceof
name|ASTEqual
operator|||
name|parent
operator|instanceof
name|ASTNotEqual
operator|)
operator|&&
name|dbAttr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|CLOB
operator|&&
name|parent
operator|.
name|getOperandCount
argument_list|()
operator|==
literal|2
operator|&&
name|parent
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|String
condition|)
block|{
name|Integer
name|size
init|=
name|parent
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
operator|.
name|toString
argument_list|()
operator|.
name|length
argument_list|()
operator|+
literal|1
decl_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|"CAST("
argument_list|)
expr_stmt|;
name|super
operator|.
name|processColumnWithQuoteSqlIdentifiers
argument_list|(
name|dbAttr
argument_list|,
name|pathExp
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|" AS VARCHAR("
operator|+
name|size
operator|+
literal|"))"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|processColumnWithQuoteSqlIdentifiers
argument_list|(
name|dbAttr
argument_list|,
name|pathExp
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
