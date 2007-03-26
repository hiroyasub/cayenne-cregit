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
name|ejbql
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
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|EJBQLExpressionVisitor
import|;
end_import

begin_comment
comment|/**  * A base node for the EJBQL concrete nodes that satisfies JJTree requirements.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|SimpleNode
implements|implements
name|Node
implements|,
name|Serializable
implements|,
name|EJBQLExpression
block|{
specifier|final
name|int
name|id
decl_stmt|;
name|SimpleNode
name|parent
decl_stmt|;
name|SimpleNode
index|[]
name|children
decl_stmt|;
name|boolean
name|not
decl_stmt|;
name|String
name|text
decl_stmt|;
specifier|public
name|SimpleNode
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
specifier|public
name|boolean
name|isNegated
parameter_list|()
block|{
return|return
name|not
return|;
block|}
comment|/**      * A recursive visit method that passes a visitor to this node and all its children,      * depth first.      */
specifier|public
name|boolean
name|visit
parameter_list|(
name|EJBQLExpressionVisitor
name|visitor
parameter_list|)
block|{
if|if
condition|(
operator|!
name|visitNode
argument_list|(
name|visitor
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|len
init|=
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
name|len
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|visitChild
argument_list|(
name|visitor
argument_list|,
name|i
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Visits this not without recursion. Default implementation simply returns true.      * Subclasses override this method to call an appropriate visitor method.      */
specifier|protected
name|boolean
name|visitNode
parameter_list|(
name|EJBQLExpressionVisitor
name|visitor
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Recursively visits a child at the specified index. Subclasses override this method      * if they desire to implement callbacks after visiting each child.      */
specifier|protected
name|boolean
name|visitChild
parameter_list|(
name|EJBQLExpressionVisitor
name|visitor
parameter_list|,
name|int
name|childIndex
parameter_list|)
block|{
return|return
name|children
index|[
name|childIndex
index|]
operator|.
name|visit
argument_list|(
name|visitor
argument_list|)
return|;
block|}
specifier|public
name|EJBQLExpression
name|getChild
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
operator|(
name|EJBQLExpression
operator|)
name|jjtGetChild
argument_list|(
name|index
argument_list|)
return|;
block|}
specifier|public
name|int
name|getChildrenCount
parameter_list|()
block|{
return|return
name|jjtGetNumChildren
argument_list|()
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
name|String
name|className
init|=
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|i
init|=
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|"EJBQL"
argument_list|)
decl_stmt|;
return|return
name|i
operator|>=
literal|0
condition|?
name|className
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|5
argument_list|)
else|:
name|className
return|;
block|}
specifier|public
name|void
name|jjtOpen
parameter_list|()
block|{
block|}
specifier|public
name|void
name|jjtClose
parameter_list|()
block|{
block|}
specifier|public
name|void
name|jjtSetParent
parameter_list|(
name|Node
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
operator|(
name|SimpleNode
operator|)
name|parent
expr_stmt|;
block|}
specifier|public
name|Node
name|jjtGetParent
parameter_list|()
block|{
return|return
name|this
operator|.
name|parent
return|;
block|}
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
if|if
condition|(
name|children
operator|==
literal|null
condition|)
block|{
name|children
operator|=
operator|new
name|SimpleNode
index|[
name|i
operator|+
literal|1
index|]
expr_stmt|;
block|}
if|else if
condition|(
name|i
operator|>=
name|children
operator|.
name|length
condition|)
block|{
name|SimpleNode
name|c
index|[]
init|=
operator|new
name|SimpleNode
index|[
name|i
operator|+
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|children
argument_list|,
literal|0
argument_list|,
name|c
argument_list|,
literal|0
argument_list|,
name|children
operator|.
name|length
argument_list|)
expr_stmt|;
name|children
operator|=
name|c
expr_stmt|;
block|}
name|children
index|[
name|i
index|]
operator|=
operator|(
name|SimpleNode
operator|)
name|n
expr_stmt|;
block|}
specifier|public
name|Node
name|jjtGetChild
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|children
index|[
name|i
index|]
return|;
block|}
specifier|public
name|int
name|jjtGetNumChildren
parameter_list|()
block|{
return|return
operator|(
name|children
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|children
operator|.
name|length
return|;
block|}
name|void
name|setText
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|dump
argument_list|(
name|pw
argument_list|,
literal|""
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|pw
operator|.
name|close
argument_list|()
expr_stmt|;
name|buffer
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
name|void
name|dump
parameter_list|(
name|PrintWriter
name|out
parameter_list|,
name|String
name|prefix
parameter_list|,
name|boolean
name|text
parameter_list|)
block|{
name|out
operator|.
name|println
argument_list|(
name|prefix
operator|+
name|getName
argument_list|()
operator|+
operator|(
name|text
operator|&&
name|this
operator|.
name|text
operator|!=
literal|null
condition|?
literal|" ["
operator|+
name|this
operator|.
name|text
operator|+
literal|"]"
else|:
literal|""
operator|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|children
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|children
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|SimpleNode
name|n
init|=
operator|(
name|SimpleNode
operator|)
name|children
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|n
operator|!=
literal|null
condition|)
block|{
name|n
operator|.
name|dump
argument_list|(
name|out
argument_list|,
name|prefix
operator|+
literal|" "
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

