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
name|table_primitives
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
comment|/**  * Class _TablePrimitives was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_TablePrimitives
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
name|Boolean
argument_list|>
name|BOOLEAN_COLUMN
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"booleanColumn"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|INT_COLUMN
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"intColumn"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setBooleanColumn
parameter_list|(
name|boolean
name|booleanColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"booleanColumn"
argument_list|,
name|booleanColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isBooleanColumn
parameter_list|()
block|{
name|Boolean
name|value
init|=
operator|(
name|Boolean
operator|)
name|readProperty
argument_list|(
literal|"booleanColumn"
argument_list|)
decl_stmt|;
return|return
operator|(
name|value
operator|!=
literal|null
operator|)
condition|?
name|value
operator|.
name|booleanValue
argument_list|()
else|:
literal|false
return|;
block|}
specifier|public
name|void
name|setIntColumn
parameter_list|(
name|int
name|intColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"intColumn"
argument_list|,
name|intColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getIntColumn
parameter_list|()
block|{
name|Object
name|value
init|=
name|readProperty
argument_list|(
literal|"intColumn"
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
block|}
end_class

end_unit

