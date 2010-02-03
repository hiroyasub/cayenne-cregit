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
name|java
operator|.
name|util
operator|.
name|List
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
comment|/**  * Class _Role was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Role
extends|extends
name|CayenneDataObject
block|{
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
name|NAME_PROPERTY
init|=
literal|"name"
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
name|ROLE_GROUPS_PROPERTY
init|=
literal|"roleGroups"
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
name|setId
parameter_list|(
name|long
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
name|long
name|getId
parameter_list|()
block|{
name|Object
name|value
init|=
name|readProperty
argument_list|(
literal|"id"
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
name|Long
operator|)
name|value
else|:
literal|0
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
name|setType
parameter_list|(
name|int
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
name|int
name|getType
parameter_list|()
block|{
name|Object
name|value
init|=
name|readProperty
argument_list|(
literal|"type"
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
name|addToRoleGroups
parameter_list|(
name|Group
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"roleGroups"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromRoleGroups
parameter_list|(
name|Group
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"roleGroups"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
argument_list|<
name|Group
argument_list|>
name|getRoleGroups
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Group
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"roleGroups"
argument_list|)
return|;
block|}
specifier|protected
specifier|abstract
name|void
name|onPostPersist
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|void
name|onPostUpdate
parameter_list|()
function_decl|;
block|}
end_class

end_unit

