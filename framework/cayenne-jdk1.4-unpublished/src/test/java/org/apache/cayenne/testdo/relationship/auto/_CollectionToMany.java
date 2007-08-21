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
name|relationship
operator|.
name|auto
package|;
end_package

begin_comment
comment|/** Class _CollectionToMany was generated by Cayenne.   * It is probably a good idea to avoid changing this class manually,    * since it may be overwritten next time code is regenerated.    * If you need to make any customizations, please use subclass.    */
end_comment

begin_class
specifier|public
class|class
name|_CollectionToMany
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
name|TARGETS_PROPERTY
init|=
literal|"targets"
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
name|addToTargets
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|testdo
operator|.
name|relationship
operator|.
name|CollectionToManyTarget
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"targets"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromTargets
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|testdo
operator|.
name|relationship
operator|.
name|CollectionToManyTarget
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"targets"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|java
operator|.
name|util
operator|.
name|Collection
name|getTargets
parameter_list|()
block|{
return|return
operator|(
name|java
operator|.
name|util
operator|.
name|Collection
operator|)
name|readProperty
argument_list|(
literal|"targets"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

