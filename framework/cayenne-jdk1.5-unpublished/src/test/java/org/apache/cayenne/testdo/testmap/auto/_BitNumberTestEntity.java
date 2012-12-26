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
name|testmap
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
comment|/**  * Class _BitNumberTestEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_BitNumberTestEntity
extends|extends
name|CayenneDataObject
block|{
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|BIT_COLUMN_PROPERTY
init|=
literal|"bitColumn"
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
name|Integer
argument_list|>
name|BIT_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"bitColumn"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setBitColumn
parameter_list|(
name|Integer
name|bitColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"bitColumn"
argument_list|,
name|bitColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Integer
name|getBitColumn
parameter_list|()
block|{
return|return
operator|(
name|Integer
operator|)
name|readProperty
argument_list|(
literal|"bitColumn"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

