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
name|testdo
operator|.
name|horizontalinherit
operator|.
name|AbstractSuperEntity
import|;
end_import

begin_comment
comment|/**  * Class _SubEntity1 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_SubEntity1
extends|extends
name|AbstractSuperEntity
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SUB_ENTITY_STRING_ATTR_PROPERTY
init|=
literal|"subEntityStringAttr"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ID_PK_COLUMN
init|=
literal|"id"
decl_stmt|;
specifier|public
name|void
name|setSubEntityStringAttr
parameter_list|(
name|String
name|subEntityStringAttr
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"subEntityStringAttr"
argument_list|,
name|subEntityStringAttr
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getSubEntityStringAttr
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"subEntityStringAttr"
argument_list|)
return|;
block|}
block|}
end_class

end_unit
