begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|crypto
operator|.
name|db
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
comment|/**  * Class _Table4 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Table4
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
name|Integer
argument_list|>
name|PLAIN_INT
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"plainInt"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|PLAIN_STRING
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"plainString"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setPlainInt
parameter_list|(
name|int
name|plainInt
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"plainInt"
argument_list|,
name|plainInt
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getPlainInt
parameter_list|()
block|{
name|Object
name|value
init|=
name|readProperty
argument_list|(
literal|"plainInt"
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
name|setPlainString
parameter_list|(
name|String
name|plainString
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"plainString"
argument_list|,
name|plainString
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPlainString
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"plainString"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

