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
name|deleterules
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
name|testdo
operator|.
name|deleterules
operator|.
name|ClientDeleteCascade
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
name|deleterules
operator|.
name|ClientDeleteDeny
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
name|deleterules
operator|.
name|ClientDeleteNullify
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

begin_comment
comment|/**  * A generated persistent class mapped as "DeleteRule" Cayenne entity. It is a good idea to  * avoid changing this class manually, since it will be overwritten next time code is  * regenerated. If you need to make any customizations, put them in a subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ClientDeleteRule
extends|extends
name|PersistentObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NAME_PROPERTY
init|=
literal|"name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FROM_CASCADE_PROPERTY
init|=
literal|"fromCascade"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FROM_DENY_PROPERTY
init|=
literal|"fromDeny"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FROM_NULLIFY_PROPERTY
init|=
literal|"fromNullify"
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ClientDeleteCascade
argument_list|>
name|fromCascade
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ClientDeleteDeny
argument_list|>
name|fromDeny
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ClientDeleteNullify
argument_list|>
name|fromNullify
decl_stmt|;
specifier|public
name|String
name|getName
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
literal|"name"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
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
literal|"name"
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
name|name
decl_stmt|;
name|this
operator|.
name|name
operator|=
name|name
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
literal|"name"
argument_list|,
name|oldValue
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|ClientDeleteCascade
argument_list|>
name|getFromCascade
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
literal|"fromCascade"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|fromCascade
return|;
block|}
specifier|public
name|void
name|addToFromCascade
parameter_list|(
name|ClientDeleteCascade
name|object
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
literal|"fromCascade"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|fromCascade
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromFromCascade
parameter_list|(
name|ClientDeleteCascade
name|object
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
literal|"fromCascade"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|fromCascade
operator|.
name|remove
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|ClientDeleteDeny
argument_list|>
name|getFromDeny
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
literal|"fromDeny"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|fromDeny
return|;
block|}
specifier|public
name|void
name|addToFromDeny
parameter_list|(
name|ClientDeleteDeny
name|object
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
literal|"fromDeny"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|fromDeny
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromFromDeny
parameter_list|(
name|ClientDeleteDeny
name|object
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
literal|"fromDeny"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|fromDeny
operator|.
name|remove
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|ClientDeleteNullify
argument_list|>
name|getFromNullify
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
literal|"fromNullify"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|fromNullify
return|;
block|}
specifier|public
name|void
name|addToFromNullify
parameter_list|(
name|ClientDeleteNullify
name|object
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
literal|"fromNullify"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|fromNullify
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromFromNullify
parameter_list|(
name|ClientDeleteNullify
name|object
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
literal|"fromNullify"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|fromNullify
operator|.
name|remove
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

