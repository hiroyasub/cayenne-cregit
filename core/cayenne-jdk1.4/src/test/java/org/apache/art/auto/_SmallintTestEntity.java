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
comment|/** Class _SmallintTestEntity was generated by Cayenne.   * It is probably a good idea to avoid changing this class manually,    * since it may be overwritten next time code is regenerated.    * If you need to make any customizations, please use subclass.    */
end_comment

begin_class
specifier|public
class|class
name|_SmallintTestEntity
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
name|SMALLINT_COL_PROPERTY
init|=
literal|"smallintCol"
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
name|setSmallintCol
parameter_list|(
name|Short
name|smallintCol
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"smallintCol"
argument_list|,
name|smallintCol
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Short
name|getSmallintCol
parameter_list|()
block|{
return|return
operator|(
name|Short
operator|)
name|readProperty
argument_list|(
literal|"smallintCol"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

