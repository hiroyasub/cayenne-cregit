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
comment|/** Class _GeneratedColumnDep was generated by Cayenne.   * It is probably a good idea to avoid changing this class manually,    * since it may be overwritten next time code is regenerated.    * If you need to make any customizations, please use subclass.    */
end_comment

begin_class
specifier|public
class|class
name|_GeneratedColumnDep
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
name|NAME_PROPERTY
init|=
literal|"name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TO_MASTER_PROPERTY
init|=
literal|"toMaster"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GENERATED_COLUMN_FK_PK_COLUMN
init|=
literal|"GENERATED_COLUMN_FK"
decl_stmt|;
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
name|setToMaster
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|GeneratedColumnTestEntity
name|toMaster
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toMaster"
argument_list|,
name|toMaster
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|GeneratedColumnTestEntity
name|getToMaster
parameter_list|()
block|{
return|return
operator|(
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|GeneratedColumnTestEntity
operator|)
name|readProperty
argument_list|(
literal|"toMaster"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

