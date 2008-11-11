begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|unit
package|;
end_package

begin_comment
comment|/**  * A superclass of test cases using "inheritance" DataMap for its access stack.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|InheritanceCase
extends|extends
name|CayenneCase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|INHERITANCE_ACCESS_STACK
init|=
literal|"InheritanceStack"
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|AccessStack
name|buildAccessStack
parameter_list|()
block|{
return|return
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getAccessStack
argument_list|(
name|INHERITANCE_ACCESS_STACK
argument_list|)
return|;
block|}
block|}
end_class

end_unit

