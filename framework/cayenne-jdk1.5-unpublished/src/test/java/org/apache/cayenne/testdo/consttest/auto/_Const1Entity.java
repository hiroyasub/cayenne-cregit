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
name|consttest
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
name|consttest
operator|.
name|Const1Type
import|;
end_import

begin_comment
comment|/**  * Class _Const1Entity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Const1Entity
extends|extends
name|CayenneDataObject
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
name|STATUS_PROPERTY
init|=
literal|"status"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_PROPERTY
init|=
literal|"type"
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
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"name"
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
literal|"name"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setStatus
parameter_list|(
name|Integer
name|status
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"status"
argument_list|,
name|status
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Integer
name|getStatus
parameter_list|()
block|{
return|return
operator|(
name|Integer
operator|)
name|readProperty
argument_list|(
literal|"status"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|Const1Type
name|type
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Const1Type
name|getType
parameter_list|()
block|{
return|return
operator|(
name|Const1Type
operator|)
name|readProperty
argument_list|(
literal|"type"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

