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
name|horizontalinherit
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
name|CayenneDataObject
import|;
end_import

begin_comment
comment|/**  * Class _AbstractSuperEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_AbstractSuperEntity
extends|extends
name|CayenneDataObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SUPER_INT_ATTR_PROPERTY
init|=
literal|"superIntAttr"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUPER_STRING_ATTR_PROPERTY
init|=
literal|"superStringAttr"
decl_stmt|;
specifier|public
name|void
name|setSuperIntAttr
parameter_list|(
name|int
name|superIntAttr
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"superIntAttr"
argument_list|,
name|superIntAttr
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getSuperIntAttr
parameter_list|()
block|{
name|Object
name|value
init|=
name|readProperty
argument_list|(
literal|"superIntAttr"
argument_list|)
decl_stmt|;
return|return
operator|(
name|value
operator|!=
literal|null
operator|)
condition|?
operator|(
name|Integer
operator|)
name|value
else|:
literal|0
return|;
block|}
specifier|public
name|void
name|setSuperStringAttr
parameter_list|(
name|String
name|superStringAttr
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"superStringAttr"
argument_list|,
name|superStringAttr
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getSuperStringAttr
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"superStringAttr"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

