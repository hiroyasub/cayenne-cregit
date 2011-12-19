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
name|inheritance
operator|.
name|vertical
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
comment|/**  * Class _IvRoot was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_IvRoot
extends|extends
name|CayenneDataObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DISCRIMINATOR_PROPERTY
init|=
literal|"discriminator"
decl_stmt|;
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
name|ID_PK_COLUMN
init|=
literal|"ID"
decl_stmt|;
specifier|public
name|void
name|setDiscriminator
parameter_list|(
name|String
name|discriminator
parameter_list|)
block|{
name|writeProperty
argument_list|(
name|DISCRIMINATOR_PROPERTY
argument_list|,
name|discriminator
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getDiscriminator
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
name|DISCRIMINATOR_PROPERTY
argument_list|)
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
name|writeProperty
argument_list|(
name|NAME_PROPERTY
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
name|NAME_PROPERTY
argument_list|)
return|;
block|}
block|}
end_class

end_unit

