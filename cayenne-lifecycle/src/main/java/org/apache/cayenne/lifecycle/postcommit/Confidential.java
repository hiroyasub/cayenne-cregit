begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|lifecycle
operator|.
name|postcommit
package|;
end_package

begin_comment
comment|/**  * A singleton representing a confidential property value.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|Confidential
block|{
specifier|private
specifier|static
specifier|final
name|Confidential
name|instance
init|=
operator|new
name|Confidential
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|Confidential
name|getInstance
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
specifier|private
name|Confidential
parameter_list|()
block|{
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"*******"
return|;
block|}
block|}
end_class

end_unit

