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
comment|/** Class _Painting was generated by Cayenne.   * It is probably a good idea to avoid changing this class manually,    * since it may be overwritten next time code is regenerated.    * If you need to make any customizations, please use subclass.    */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Painting
extends|extends
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|ArtDataObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ESTIMATED_PRICE_PROPERTY
init|=
literal|"estimatedPrice"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PAINTING_DESCRIPTION_PROPERTY
init|=
literal|"paintingDescription"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PAINTING_TITLE_PROPERTY
init|=
literal|"paintingTitle"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TO_ARTIST_PROPERTY
init|=
literal|"toArtist"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TO_GALLERY_PROPERTY
init|=
literal|"toGallery"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TO_PAINTING_INFO_PROPERTY
init|=
literal|"toPaintingInfo"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PAINTING_ID_PK_COLUMN
init|=
literal|"PAINTING_ID"
decl_stmt|;
specifier|public
name|void
name|setEstimatedPrice
parameter_list|(
name|java
operator|.
name|math
operator|.
name|BigDecimal
name|estimatedPrice
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"estimatedPrice"
argument_list|,
name|estimatedPrice
argument_list|)
expr_stmt|;
block|}
specifier|public
name|java
operator|.
name|math
operator|.
name|BigDecimal
name|getEstimatedPrice
parameter_list|()
block|{
return|return
operator|(
name|java
operator|.
name|math
operator|.
name|BigDecimal
operator|)
name|readProperty
argument_list|(
literal|"estimatedPrice"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPaintingDescription
parameter_list|(
name|String
name|paintingDescription
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"paintingDescription"
argument_list|,
name|paintingDescription
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPaintingDescription
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"paintingDescription"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPaintingTitle
parameter_list|(
name|String
name|paintingTitle
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"paintingTitle"
argument_list|,
name|paintingTitle
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPaintingTitle
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"paintingTitle"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToArtist
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|Artist
name|toArtist
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toArtist"
argument_list|,
name|toArtist
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
name|Artist
name|getToArtist
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
name|Artist
operator|)
name|readProperty
argument_list|(
literal|"toArtist"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToGallery
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|Gallery
name|toGallery
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toGallery"
argument_list|,
name|toGallery
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
name|Gallery
name|getToGallery
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
name|Gallery
operator|)
name|readProperty
argument_list|(
literal|"toGallery"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToPaintingInfo
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|PaintingInfo
name|toPaintingInfo
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toPaintingInfo"
argument_list|,
name|toPaintingInfo
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
name|PaintingInfo
name|getToPaintingInfo
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
name|PaintingInfo
operator|)
name|readProperty
argument_list|(
literal|"toPaintingInfo"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

