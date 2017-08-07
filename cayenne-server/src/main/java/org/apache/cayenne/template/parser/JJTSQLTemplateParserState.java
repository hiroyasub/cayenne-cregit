begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/* Generated By:JavaCC: Do not edit this line. JJTSQLTemplateParserState.java Version 5.0 */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|template
operator|.
name|parser
package|;
end_package

begin_class
specifier|public
class|class
name|JJTSQLTemplateParserState
block|{
specifier|private
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|Node
argument_list|>
name|nodes
decl_stmt|;
specifier|private
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|Integer
argument_list|>
name|marks
decl_stmt|;
specifier|private
name|int
name|sp
decl_stmt|;
comment|// number of nodes on stack
specifier|private
name|int
name|mk
decl_stmt|;
comment|// current mark
specifier|private
name|boolean
name|node_created
decl_stmt|;
specifier|public
name|JJTSQLTemplateParserState
parameter_list|()
block|{
name|nodes
operator|=
operator|new
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<
name|Node
argument_list|>
argument_list|()
expr_stmt|;
name|marks
operator|=
operator|new
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
name|sp
operator|=
literal|0
expr_stmt|;
name|mk
operator|=
literal|0
expr_stmt|;
block|}
comment|/* Determines whether the current node was actually closed and      pushed.  This should only be called in the final user action of a      node scope.  */
specifier|public
name|boolean
name|nodeCreated
parameter_list|()
block|{
return|return
name|node_created
return|;
block|}
comment|/* Call this to reinitialize the node stack.  It is called      automatically by the parser's ReInit() method. */
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|nodes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|marks
operator|.
name|clear
argument_list|()
expr_stmt|;
name|sp
operator|=
literal|0
expr_stmt|;
name|mk
operator|=
literal|0
expr_stmt|;
block|}
comment|/* Returns the root node of the AST.  It only makes sense to call      this after a successful parse. */
specifier|public
name|Node
name|rootNode
parameter_list|()
block|{
return|return
name|nodes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/* Pushes a node on to the stack. */
specifier|public
name|void
name|pushNode
parameter_list|(
name|Node
name|n
parameter_list|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|n
argument_list|)
expr_stmt|;
operator|++
name|sp
expr_stmt|;
block|}
comment|/* Returns the node on the top of the stack, and remove it from the      stack.  */
specifier|public
name|Node
name|popNode
parameter_list|()
block|{
if|if
condition|(
operator|--
name|sp
operator|<
name|mk
condition|)
block|{
name|mk
operator|=
name|marks
operator|.
name|remove
argument_list|(
name|marks
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|nodes
operator|.
name|remove
argument_list|(
name|nodes
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/* Returns the node currently on the top of the stack. */
specifier|public
name|Node
name|peekNode
parameter_list|()
block|{
return|return
name|nodes
operator|.
name|get
argument_list|(
name|nodes
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/* Returns the number of children on the stack in the current node      scope. */
specifier|public
name|int
name|nodeArity
parameter_list|()
block|{
return|return
name|sp
operator|-
name|mk
return|;
block|}
specifier|public
name|void
name|clearNodeScope
parameter_list|(
name|Node
name|n
parameter_list|)
block|{
while|while
condition|(
name|sp
operator|>
name|mk
condition|)
block|{
name|popNode
argument_list|()
expr_stmt|;
block|}
name|mk
operator|=
name|marks
operator|.
name|remove
argument_list|(
name|marks
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|openNodeScope
parameter_list|(
name|Node
name|n
parameter_list|)
block|{
name|marks
operator|.
name|add
argument_list|(
name|mk
argument_list|)
expr_stmt|;
name|mk
operator|=
name|sp
expr_stmt|;
name|n
operator|.
name|jjtOpen
argument_list|()
expr_stmt|;
block|}
comment|/* A definite node is constructed from a specified number of      children.  That number of nodes are popped from the stack and      made the children of the definite node.  Then the definite node      is pushed on to the stack. */
specifier|public
name|void
name|closeNodeScope
parameter_list|(
name|Node
name|n
parameter_list|,
name|int
name|num
parameter_list|)
block|{
name|mk
operator|=
name|marks
operator|.
name|remove
argument_list|(
name|marks
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
while|while
condition|(
name|num
operator|--
operator|>
literal|0
condition|)
block|{
name|Node
name|c
init|=
name|popNode
argument_list|()
decl_stmt|;
name|c
operator|.
name|jjtSetParent
argument_list|(
name|n
argument_list|)
expr_stmt|;
name|n
operator|.
name|jjtAddChild
argument_list|(
name|c
argument_list|,
name|num
argument_list|)
expr_stmt|;
block|}
name|n
operator|.
name|jjtClose
argument_list|()
expr_stmt|;
name|pushNode
argument_list|(
name|n
argument_list|)
expr_stmt|;
name|node_created
operator|=
literal|true
expr_stmt|;
block|}
comment|/* A conditional node is constructed if its condition is true.  All      the nodes that have been pushed since the node was opened are      made children of the conditional node, which is then pushed      on to the stack.  If the condition is false the node is not      constructed and they are left on the stack. */
specifier|public
name|void
name|closeNodeScope
parameter_list|(
name|Node
name|n
parameter_list|,
name|boolean
name|condition
parameter_list|)
block|{
if|if
condition|(
name|condition
condition|)
block|{
name|int
name|a
init|=
name|nodeArity
argument_list|()
decl_stmt|;
name|mk
operator|=
name|marks
operator|.
name|remove
argument_list|(
name|marks
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
while|while
condition|(
name|a
operator|--
operator|>
literal|0
condition|)
block|{
name|Node
name|c
init|=
name|popNode
argument_list|()
decl_stmt|;
name|c
operator|.
name|jjtSetParent
argument_list|(
name|n
argument_list|)
expr_stmt|;
name|n
operator|.
name|jjtAddChild
argument_list|(
name|c
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
name|n
operator|.
name|jjtClose
argument_list|()
expr_stmt|;
name|pushNode
argument_list|(
name|n
argument_list|)
expr_stmt|;
name|node_created
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|mk
operator|=
name|marks
operator|.
name|remove
argument_list|(
name|marks
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|node_created
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
end_class

begin_comment
comment|/* JavaCC - OriginalChecksum=1706cef4cf4b627318940a448e5ee9ea (do not edit this line) */
end_comment

end_unit

