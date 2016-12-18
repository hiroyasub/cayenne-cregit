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
name|java
operator|.
name|math
operator|.
name|BigDecimal
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

begin_import
import|import
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
name|Artist
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
name|testdo
operator|.
name|testmap
operator|.
name|Gallery
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
name|testdo
operator|.
name|testmap
operator|.
name|PaintingInfo
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
name|testdo
operator|.
name|testmap
operator|.
name|ROArtist
import|;
end_import

begin_comment
comment|/**  * Class _CompoundPaintingLongNames was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_CompoundPaintingLongNames
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
name|PAINTING_ID_PK_COLUMN
init|=
literal|"PAINTING_ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|ARTIST_LONG_NAME
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"artistLongName"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|BigDecimal
argument_list|>
name|ESTIMATED_PRICE
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"estimatedPrice"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|GALLERY_LONG_NAME
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"galleryLongName"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|PAINTING_DESCRIPTION
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"paintingDescription"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|PAINTING_TITLE
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"paintingTitle"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|TEXT_LONG_REVIEW
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"textLongReview"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Artist
argument_list|>
name|TO_ARTIST
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|ROArtist
argument_list|>
name|TO_ARTIST1
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"toArtist1"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Gallery
argument_list|>
name|TO_GALLERY
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"toGallery"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|PaintingInfo
argument_list|>
name|TO_PAINTING_INFO
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"toPaintingInfo"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setArtistLongName
parameter_list|(
name|String
name|artistLongName
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"artistLongName"
argument_list|,
name|artistLongName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getArtistLongName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"artistLongName"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setEstimatedPrice
parameter_list|(
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
name|BigDecimal
name|getEstimatedPrice
parameter_list|()
block|{
return|return
operator|(
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
name|setGalleryLongName
parameter_list|(
name|String
name|galleryLongName
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"galleryLongName"
argument_list|,
name|galleryLongName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getGalleryLongName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"galleryLongName"
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
name|setTextLongReview
parameter_list|(
name|String
name|textLongReview
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"textLongReview"
argument_list|,
name|textLongReview
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTextLongReview
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"textLongReview"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToArtist
parameter_list|(
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
name|Artist
name|getToArtist
parameter_list|()
block|{
return|return
operator|(
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
name|setToArtist1
parameter_list|(
name|ROArtist
name|toArtist1
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toArtist1"
argument_list|,
name|toArtist1
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ROArtist
name|getToArtist1
parameter_list|()
block|{
return|return
operator|(
name|ROArtist
operator|)
name|readProperty
argument_list|(
literal|"toArtist1"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToGallery
parameter_list|(
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
name|Gallery
name|getToGallery
parameter_list|()
block|{
return|return
operator|(
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
name|PaintingInfo
name|getToPaintingInfo
parameter_list|()
block|{
return|return
operator|(
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

