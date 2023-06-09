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
name|toone
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ValueHolder
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
name|testdo
operator|.
name|toone
operator|.
name|ClientTooneDep
import|;
end_import

begin_comment
comment|/**  * A generated persistent class mapped as "TooneMaster" Cayenne entity. It is a good idea to  * avoid changing this class manually, since it will be overwritten next time code is  * regenerated. If you need to make any customizations, put them in a subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ClientTooneMaster
extends|extends
name|PersistentObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TO_DEPENDENT_PROPERTY
init|=
literal|"toDependent"
decl_stmt|;
specifier|protected
name|ValueHolder
name|toDependent
decl_stmt|;
specifier|public
name|ClientTooneDep
name|getToDependent
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
literal|"toDependent"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|ClientTooneDep
operator|)
name|toDependent
operator|.
name|getValue
argument_list|()
return|;
block|}
specifier|public
name|void
name|setToDependent
parameter_list|(
name|ClientTooneDep
name|toDependent
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
literal|"toDependent"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|toDependent
operator|.
name|setValue
argument_list|(
name|toDependent
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

