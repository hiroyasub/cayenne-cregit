begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|testdo
operator|.
name|generic
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|testdo
operator|.
name|generic
operator|.
name|auto
operator|.
name|_Generic
import|;
end_import

begin_class
specifier|public
class|class
name|Generic
extends|extends
name|_Generic
block|{
specifier|private
specifier|static
name|Generic
name|instance
decl_stmt|;
specifier|private
name|Generic
parameter_list|()
block|{
block|}
specifier|public
specifier|static
name|Generic
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
name|Generic
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

