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
name|exp
operator|.
name|parser
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * Superclass of pattern matching nodes. Assumes that subclass is a binary expression with  * the second operand being a pattern.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PatternMatchNode
extends|extends
name|ConditionNode
block|{
specifier|protected
specifier|transient
name|Pattern
name|pattern
decl_stmt|;
specifier|protected
specifier|transient
name|boolean
name|patternCompiled
decl_stmt|;
specifier|protected
name|boolean
name|ignoringCase
decl_stmt|;
specifier|protected
name|char
name|escapeChar
decl_stmt|;
name|PatternMatchNode
parameter_list|(
name|int
name|i
parameter_list|,
name|boolean
name|ignoringCase
parameter_list|)
block|{
name|super
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|this
operator|.
name|ignoringCase
operator|=
name|ignoringCase
expr_stmt|;
block|}
name|PatternMatchNode
parameter_list|(
name|int
name|i
parameter_list|,
name|boolean
name|ignoringCase
parameter_list|,
name|char
name|escapeChar
parameter_list|)
block|{
name|super
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|this
operator|.
name|ignoringCase
operator|=
name|ignoringCase
expr_stmt|;
name|setEscapeChar
argument_list|(
name|escapeChar
argument_list|)
expr_stmt|;
block|}
name|SimpleNode
name|wrap
parameter_list|(
name|Object
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|pattern
operator|instanceof
name|SimpleNode
condition|)
block|{
return|return
operator|(
name|SimpleNode
operator|)
name|pattern
return|;
block|}
return|return
operator|new
name|ASTScalar
argument_list|(
name|pattern
argument_list|)
return|;
block|}
comment|/**      *<p>This method will return an escape character for the like      * clause.  The escape character will eventually end up in the      * query as<code>...(t0.foo LIKE ?&#123;escape '|'&#125;)</code> where the      * pipe symbol is the escape character.</p>      *<p>Note that having no escape character is represented as      * the character 0.</p>      */
specifier|public
name|char
name|getEscapeChar
parameter_list|()
block|{
return|return
name|escapeChar
return|;
block|}
comment|/**      *<p>This method allows the setting of the escape character.      * The escape character can be used in a LIKE clause.  The      * character 0 signifies no escape character.  The escape      * character '?' is disallowed.</p>      */
specifier|public
name|void
name|setEscapeChar
parameter_list|(
name|char
name|value
parameter_list|)
block|{
if|if
condition|(
literal|'?'
operator|==
name|value
condition|)
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"the use of the '?' as an escape character in LIKE clauses is disallowed."
argument_list|)
throw|;
name|escapeChar
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * @since 4.2      */
specifier|public
name|boolean
name|isIgnoringCase
parameter_list|()
block|{
return|return
name|ignoringCase
return|;
block|}
specifier|protected
name|boolean
name|matchPattern
parameter_list|(
name|String
name|string
parameter_list|)
block|{
return|return
operator|(
name|string
operator|!=
literal|null
operator|)
operator|&&
name|getPattern
argument_list|()
operator|.
name|matcher
argument_list|(
name|string
argument_list|)
operator|.
name|find
argument_list|()
return|;
block|}
specifier|protected
name|Pattern
name|getPattern
parameter_list|()
block|{
comment|// compile pattern on demand
if|if
condition|(
operator|!
name|patternCompiled
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
operator|!
name|patternCompiled
condition|)
block|{
name|pattern
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|jjtGetNumChildren
argument_list|()
operator|<
literal|2
condition|)
block|{
name|patternCompiled
operator|=
literal|true
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// precompile pattern
name|Node
name|node
init|=
name|jjtGetChild
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|ASTScalar
condition|)
block|{
name|ASTScalar
name|patternNode
init|=
operator|(
name|ASTScalar
operator|)
name|node
decl_stmt|;
if|if
condition|(
name|patternNode
operator|==
literal|null
condition|)
block|{
name|patternCompiled
operator|=
literal|true
expr_stmt|;
return|return
literal|null
return|;
block|}
name|String
name|srcPattern
init|=
operator|(
name|String
operator|)
name|patternNode
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|srcPattern
operator|==
literal|null
condition|)
block|{
name|patternCompiled
operator|=
literal|true
expr_stmt|;
return|return
literal|null
return|;
block|}
name|pattern
operator|=
name|Util
operator|.
name|sqlPatternToPattern
argument_list|(
name|srcPattern
argument_list|,
name|ignoringCase
argument_list|)
expr_stmt|;
name|patternCompiled
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|pattern
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|jjtAddChild
parameter_list|(
name|Node
name|n
parameter_list|,
name|int
name|i
parameter_list|)
block|{
comment|// reset pattern if the node is modified
if|if
condition|(
name|i
operator|==
literal|1
condition|)
block|{
name|patternCompiled
operator|=
literal|false
expr_stmt|;
block|}
name|super
operator|.
name|jjtAddChild
argument_list|(
name|n
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendChildrenAsEJBQL
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|parameterAccumulator
parameter_list|,
name|Appendable
name|out
parameter_list|,
name|String
name|rootId
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|appendChildrenAsEJBQL
argument_list|(
name|parameterAccumulator
argument_list|,
name|out
argument_list|,
name|rootId
argument_list|)
expr_stmt|;
if|if
condition|(
literal|0
operator|!=
name|getEscapeChar
argument_list|()
condition|)
block|{
if|if
condition|(
literal|'\''
operator|==
name|getEscapeChar
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"unable to escape an EJBQL like clause with a single quote character"
argument_list|)
throw|;
block|}
name|out
operator|.
name|append
argument_list|(
literal|" escape '"
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|getEscapeChar
argument_list|()
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
block|}
end_class

end_unit

