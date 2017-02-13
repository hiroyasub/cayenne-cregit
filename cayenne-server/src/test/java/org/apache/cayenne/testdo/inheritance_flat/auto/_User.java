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
name|exp
operator|.
name|Property
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
name|Role
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
name|UserProperties
import|;
end_import

begin_comment
comment|/**  * Class _User was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_User
extends|extends
name|Role
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
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
specifier|static
specifier|final
name|Property
argument_list|<
name|UserProperties
argument_list|>
name|USER_PROPERTIES
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"userProperties"
argument_list|,
name|UserProperties
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setUserProperties
parameter_list|(
name|UserProperties
name|userProperties
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"userProperties"
argument_list|,
name|userProperties
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|UserProperties
name|getUserProperties
parameter_list|()
block|{
return|return
operator|(
name|UserProperties
operator|)
name|readProperty
argument_list|(
literal|"userProperties"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

