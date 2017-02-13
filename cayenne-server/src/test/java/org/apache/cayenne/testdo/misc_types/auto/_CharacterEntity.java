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
name|misc_types
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
name|exp
operator|.
name|Property
import|;
end_import

begin_comment
comment|/**  * Class _CharacterEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_CharacterEntity
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
name|ID_PK_COLUMN
init|=
literal|"ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Character
argument_list|>
name|CHARACTER_FIELD
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"characterField"
argument_list|,
name|Character
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setCharacterField
parameter_list|(
name|Character
name|characterField
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"characterField"
argument_list|,
name|characterField
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Character
name|getCharacterField
parameter_list|()
block|{
return|return
operator|(
name|Character
operator|)
name|readProperty
argument_list|(
literal|"characterField"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

