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
comment|/** Class _MeaningfulFK was generated by Cayenne.   * It is probably a good idea to avoid changing this class manually,    * since it may be overwritten next time code is regenerated.    * If you need to make any customizations, please use subclass.    */
end_comment

begin_class
specifier|public
class|class
name|_MeaningfulFK
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
name|RELATIONSHIP_HELPER_ID_PROPERTY
init|=
literal|"relationshipHelperID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TO_RELATIONSHIP_HELPER_PROPERTY
init|=
literal|"toRelationshipHelper"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MEANIGNFUL_FK_ID_PK_COLUMN
init|=
literal|"MEANIGNFUL_FK_ID"
decl_stmt|;
specifier|public
name|void
name|setRelationshipHelperID
parameter_list|(
name|Integer
name|relationshipHelperID
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"relationshipHelperID"
argument_list|,
name|relationshipHelperID
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Integer
name|getRelationshipHelperID
parameter_list|()
block|{
return|return
operator|(
name|Integer
operator|)
name|readProperty
argument_list|(
literal|"relationshipHelperID"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToRelationshipHelper
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
name|RelationshipHelper
name|toRelationshipHelper
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toRelationshipHelper"
argument_list|,
name|toRelationshipHelper
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
name|cayenne
operator|.
name|testdo
operator|.
name|relationship
operator|.
name|RelationshipHelper
name|getToRelationshipHelper
parameter_list|()
block|{
return|return
operator|(
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
name|RelationshipHelper
operator|)
name|readProperty
argument_list|(
literal|"toRelationshipHelper"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

