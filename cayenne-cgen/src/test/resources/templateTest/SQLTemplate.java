begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|test
package|;
end_package

begin_import
import|import
name|test
operator|.
name|auto
operator|.
name|_SQLTemplate
import|;
end_import

begin_class
specifier|public
class|class
name|SQLTemplate
extends|extends
name|_SQLTemplate
block|{
specifier|private
specifier|static
name|SQLTemplate
name|instance
decl_stmt|;
specifier|private
name|SQLTemplate
parameter_list|()
block|{
block|}
specifier|public
specifier|static
name|SQLTemplate
name|getInstance
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
operator|new
name|SQLTemplate
argument_list|()
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
block|}
end_class

end_unit

