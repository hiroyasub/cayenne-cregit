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
name|inheritance_vertical
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
name|inheritance_vertical
operator|.
name|Iv2Root
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
name|inheritance_vertical
operator|.
name|Iv2X
import|;
end_import

begin_comment
comment|/**  * Class _Iv2Sub1 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Iv2Sub1
extends|extends
name|Iv2Root
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
literal|"ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Iv2X
argument_list|>
name|X
init|=
operator|new
name|Property
argument_list|<
name|Iv2X
argument_list|>
argument_list|(
literal|"x"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setX
parameter_list|(
name|Iv2X
name|x
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"x"
argument_list|,
name|x
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Iv2X
name|getX
parameter_list|()
block|{
return|return
operator|(
name|Iv2X
operator|)
name|readProperty
argument_list|(
literal|"x"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

