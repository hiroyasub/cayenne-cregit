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
name|hsqldb
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
name|parser
operator|.
name|ASTFunctionCall
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
name|PatternMatchNode
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|HSQLQualifierTranslator
extends|extends
name|TrimmingQualifierTranslator
block|{
specifier|public
name|HSQLQualifierTranslator
parameter_list|(
name|QueryAssembler
name|queryAssembler
parameter_list|)
block|{
name|super
argument_list|(
name|queryAssembler
argument_list|,
name|HSQLDBAdapter
operator|.
name|TRIM_FUNCTION
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendLikeEscapeCharacter
parameter_list|(
name|PatternMatchNode
name|patternMatchNode
parameter_list|)
throws|throws
name|IOException
block|{
name|char
name|escapeChar
init|=
name|patternMatchNode
operator|.
name|getEscapeChar
argument_list|()
decl_stmt|;
if|if
condition|(
literal|'?'
operator|==
name|escapeChar
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"the escape character of '?' is illegal for LIKE clauses."
argument_list|)
throw|;
block|}
if|if
condition|(
literal|0
operator|!=
name|escapeChar
condition|)
block|{
comment|// this is a difference with super implementation - HSQL driver seems does not
comment|// support JDBC escape syntax, so creating an explicit SQL escape:
name|out
operator|.
name|append
argument_list|(
literal|" ESCAPE '"
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|escapeChar
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendFunction
parameter_list|(
name|ASTFunctionCall
name|functionExpression
parameter_list|)
block|{
comment|// from documentation:
comment|// CURRENT_TIME returns a value of TIME WITH TIME ZONE type.
comment|// LOCALTIME returns a value of TIME type.
comment|// CURTIME() is a synonym for LOCALTIME.
comment|// use LOCALTIME to better align with other DBs
if|if
condition|(
literal|"CURRENT_TIME"
operator|.
name|equals
argument_list|(
name|functionExpression
operator|.
name|getFunctionName
argument_list|()
argument_list|)
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"LOCALTIME"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|appendFunction
argument_list|(
name|functionExpression
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

