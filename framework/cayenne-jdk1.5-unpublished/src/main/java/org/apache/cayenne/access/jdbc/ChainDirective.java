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
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|context
operator|.
name|InternalContextAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|exception
operator|.
name|MethodInvocationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|exception
operator|.
name|ParseErrorException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|exception
operator|.
name|ResourceNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|directive
operator|.
name|Directive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|parser
operator|.
name|node
operator|.
name|ASTDirective
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|parser
operator|.
name|node
operator|.
name|Node
import|;
end_import

begin_comment
comment|/**  * A custom Velocity directive to conditionally join a number of {@link ChunkDirective chunks}.  * Usage of chain is the following:  *   *<pre>  * #chain(operator) - e.g. #chain(' AND ')  * #chain(operator prefix) - e.g. #chain(' AND ' 'WHERE ')</pre>  *   *<p><code>operator</code> (e.g. AND, OR, etc.) is used to join chunks that are included  * in a chain.<code>prefix</code> is inserted if a chain contains at least one chunk.  *</p>  *   * @since 1.1  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ChainDirective
extends|extends
name|Directive
block|{
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"chain"
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|BLOCK
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|render
parameter_list|(
name|InternalContextAdapter
name|context
parameter_list|,
name|Writer
name|writer
parameter_list|,
name|Node
name|node
parameter_list|)
throws|throws
name|IOException
throws|,
name|ResourceNotFoundException
throws|,
name|ParseErrorException
throws|,
name|MethodInvocationException
block|{
name|int
name|size
init|=
name|node
operator|.
name|jjtGetNumChildren
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// BLOCK is the last child
name|Node
name|block
init|=
name|node
operator|.
name|jjtGetChild
argument_list|(
name|node
operator|.
name|jjtGetNumChildren
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|String
name|join
init|=
operator|(
name|size
operator|>
literal|1
operator|)
condition|?
operator|(
name|String
operator|)
name|node
operator|.
name|jjtGetChild
argument_list|(
literal|0
argument_list|)
operator|.
name|value
argument_list|(
name|context
argument_list|)
else|:
literal|""
decl_stmt|;
name|String
name|prefix
init|=
operator|(
name|size
operator|>
literal|2
operator|)
condition|?
operator|(
name|String
operator|)
name|node
operator|.
name|jjtGetChild
argument_list|(
literal|1
argument_list|)
operator|.
name|value
argument_list|(
name|context
argument_list|)
else|:
literal|""
decl_stmt|;
comment|// if there is a conditional prefix, use a separate buffer ofr children
name|StringWriter
name|childWriter
init|=
operator|new
name|StringWriter
argument_list|(
literal|30
argument_list|)
decl_stmt|;
name|int
name|len
init|=
name|block
operator|.
name|jjtGetNumChildren
argument_list|()
decl_stmt|;
name|int
name|includedChunks
init|=
literal|0
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|block
operator|.
name|jjtGetChild
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// if this is a "chunk", evaluate its expression and prepend join if included...
if|if
condition|(
name|child
operator|instanceof
name|ASTDirective
operator|&&
literal|"chunk"
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|ASTDirective
operator|)
name|child
operator|)
operator|.
name|getDirectiveName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|child
operator|.
name|jjtGetNumChildren
argument_list|()
operator|<
literal|2
operator|||
name|child
operator|.
name|jjtGetChild
argument_list|(
literal|0
argument_list|)
operator|.
name|evaluate
argument_list|(
name|context
argument_list|)
condition|)
block|{
if|if
condition|(
name|includedChunks
operator|>
literal|0
condition|)
block|{
name|childWriter
operator|.
name|write
argument_list|(
name|join
argument_list|)
expr_stmt|;
block|}
name|includedChunks
operator|++
expr_stmt|;
block|}
block|}
name|child
operator|.
name|render
argument_list|(
name|context
argument_list|,
name|childWriter
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includedChunks
operator|>
literal|0
condition|)
block|{
name|childWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|childWriter
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

