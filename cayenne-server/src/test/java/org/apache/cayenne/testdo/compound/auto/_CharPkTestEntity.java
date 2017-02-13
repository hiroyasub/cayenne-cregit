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
name|compound
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
name|compound
operator|.
name|CharFkTestEntity
import|;
end_import

begin_comment
comment|/**  * Class _CharPkTestEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_CharPkTestEntity
extends|extends
name|CayenneDataObject
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
name|PK_COL_PK_COLUMN
init|=
literal|"PK_COL"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|OTHER_COL
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"otherCol"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|PK_COL
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"pkCol"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|List
argument_list|<
name|CharFkTestEntity
argument_list|>
argument_list|>
name|CHAR_FKS
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"charFKs"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setOtherCol
parameter_list|(
name|String
name|otherCol
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"otherCol"
argument_list|,
name|otherCol
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getOtherCol
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"otherCol"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPkCol
parameter_list|(
name|String
name|pkCol
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"pkCol"
argument_list|,
name|pkCol
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPkCol
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"pkCol"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToCharFKs
parameter_list|(
name|CharFkTestEntity
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"charFKs"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromCharFKs
parameter_list|(
name|CharFkTestEntity
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"charFKs"
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
name|CharFkTestEntity
argument_list|>
name|getCharFKs
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|CharFkTestEntity
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"charFKs"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

