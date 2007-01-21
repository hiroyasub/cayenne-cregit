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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Class _ArtGroup was generated by Cayenne.   * It is probably a good idea to avoid changing this class manually,    * since it may be overwritten next time code is regenerated.    * If you need to make any customizations, please use subclass.    */
end_comment

begin_class
specifier|public
class|class
name|_ArtGroup
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
name|ARTIST_ARRAY_PROPERTY
init|=
literal|"artistArray"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CHILD_GROUPS_ARRAY_PROPERTY
init|=
literal|"childGroupsArray"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TO_PARENT_GROUP_PROPERTY
init|=
literal|"toParentGroup"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GROUP_ID_PK_COLUMN
init|=
literal|"GROUP_ID"
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
name|addToArtistArray
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|Artist
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"artistArray"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromArtistArray
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|Artist
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"artistArray"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
name|getArtistArray
parameter_list|()
block|{
return|return
operator|(
name|List
operator|)
name|readProperty
argument_list|(
literal|"artistArray"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToChildGroupsArray
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|ArtGroup
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"childGroupsArray"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromChildGroupsArray
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|ArtGroup
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"childGroupsArray"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
name|getChildGroupsArray
parameter_list|()
block|{
return|return
operator|(
name|List
operator|)
name|readProperty
argument_list|(
literal|"childGroupsArray"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToParentGroup
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|ArtGroup
name|toParentGroup
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toParentGroup"
argument_list|,
name|toParentGroup
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
name|ArtGroup
name|getToParentGroup
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
name|ArtGroup
operator|)
name|readProperty
argument_list|(
literal|"toParentGroup"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

