begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|auto
package|;
end_package

begin_comment
comment|/** Class _BooleanTestEntity was generated by Cayenne.   * It is probably a good idea to avoid changing this class manually,    * since it may be overwritten next time code is regenerated.    * If you need to make any customizations, please use subclass.    */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_BooleanTestEntity
extends|extends
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneDataObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|BOOLEAN_COLUMN_PROPERTY
init|=
literal|"booleanColumn"
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
name|setBooleanColumn
parameter_list|(
name|Boolean
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
name|Boolean
name|getBooleanColumn
parameter_list|()
block|{
return|return
operator|(
name|Boolean
operator|)
name|readProperty
argument_list|(
literal|"booleanColumn"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

