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
name|inheritance_flat
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
name|inheritance_flat
operator|.
name|Group
import|;
end_import

begin_comment
comment|/**  * Class _GroupProperties was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_GroupProperties
extends|extends
name|CayenneDataObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DESCRIPTION_PROPERTY
init|=
literal|"description"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ID_PROPERTY
init|=
literal|"id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GROUP_PROPERTY
init|=
literal|"group"
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
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"description"
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"description"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Long
name|getId
parameter_list|()
block|{
return|return
operator|(
name|Long
operator|)
name|readProperty
argument_list|(
literal|"id"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setGroup
parameter_list|(
name|Group
name|group
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"group"
argument_list|,
name|group
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Group
name|getGroup
parameter_list|()
block|{
return|return
operator|(
name|Group
operator|)
name|readProperty
argument_list|(
literal|"group"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

