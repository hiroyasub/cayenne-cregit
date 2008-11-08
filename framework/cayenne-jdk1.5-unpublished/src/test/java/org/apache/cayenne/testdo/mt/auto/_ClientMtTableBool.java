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
name|mt
operator|.
name|auto
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
name|PersistentObject
import|;
end_import

begin_comment
comment|/**  * A generated persistent class mapped as "MtTableBool" Cayenne entity. It is a good idea to  * avoid changing this class manually, since it will be overwritten next time code is  * regenerated. If you need to make any customizations, put them in a subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ClientMtTableBool
extends|extends
name|PersistentObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|BLABLACHECK_PROPERTY
init|=
literal|"blablacheck"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NUMBER_PROPERTY
init|=
literal|"number"
decl_stmt|;
specifier|protected
name|boolean
name|blablacheck
decl_stmt|;
specifier|protected
name|int
name|number
decl_stmt|;
specifier|public
name|boolean
name|isBlablacheck
parameter_list|()
block|{
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|prepareForAccess
argument_list|(
name|this
argument_list|,
literal|"blablacheck"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|blablacheck
return|;
block|}
specifier|public
name|void
name|setBlablacheck
parameter_list|(
name|boolean
name|blablacheck
parameter_list|)
block|{
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|prepareForAccess
argument_list|(
name|this
argument_list|,
literal|"blablacheck"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|Object
name|oldValue
init|=
name|this
operator|.
name|blablacheck
decl_stmt|;
name|this
operator|.
name|blablacheck
operator|=
name|blablacheck
expr_stmt|;
comment|// notify objectContext about simple property change
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|propertyChanged
argument_list|(
name|this
argument_list|,
literal|"blablacheck"
argument_list|,
name|oldValue
argument_list|,
name|blablacheck
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|getNumber
parameter_list|()
block|{
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|prepareForAccess
argument_list|(
name|this
argument_list|,
literal|"number"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|number
return|;
block|}
specifier|public
name|void
name|setNumber
parameter_list|(
name|int
name|number
parameter_list|)
block|{
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|prepareForAccess
argument_list|(
name|this
argument_list|,
literal|"number"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|Object
name|oldValue
init|=
name|this
operator|.
name|number
decl_stmt|;
name|this
operator|.
name|number
operator|=
name|number
expr_stmt|;
comment|// notify objectContext about simple property change
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|propertyChanged
argument_list|(
name|this
argument_list|,
literal|"number"
argument_list|,
name|oldValue
argument_list|,
name|number
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

