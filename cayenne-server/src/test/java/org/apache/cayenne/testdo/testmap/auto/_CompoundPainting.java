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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

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
name|BaseDataObject
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

begin_comment
comment|/**  * Class _CompoundPainting was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_CompoundPainting
extends|extends
name|BaseDataObject
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
name|ARTIST_NAME
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"artistName"
argument_list|,
name|String
operator|.
name|class
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
name|Property
operator|.
name|create
argument_list|(
literal|"estimatedPrice"
argument_list|,
name|BigDecimal
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|GALLERY_NAME
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"galleryName"
argument_list|,
name|String
operator|.
name|class
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
name|Property
operator|.
name|create
argument_list|(
literal|"paintingTitle"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|TEXT_REVIEW
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"textReview"
argument_list|,
name|String
operator|.
name|class
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
name|Property
operator|.
name|create
argument_list|(
literal|"toArtist"
argument_list|,
name|Artist
operator|.
name|class
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
name|Property
operator|.
name|create
argument_list|(
literal|"toGallery"
argument_list|,
name|Gallery
operator|.
name|class
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
name|Property
operator|.
name|create
argument_list|(
literal|"toPaintingInfo"
argument_list|,
name|PaintingInfo
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|artistName
decl_stmt|;
specifier|protected
name|BigDecimal
name|estimatedPrice
decl_stmt|;
specifier|protected
name|String
name|galleryName
decl_stmt|;
specifier|protected
name|String
name|paintingTitle
decl_stmt|;
specifier|protected
name|String
name|textReview
decl_stmt|;
specifier|protected
name|Object
name|toArtist
decl_stmt|;
specifier|protected
name|Object
name|toGallery
decl_stmt|;
specifier|protected
name|Object
name|toPaintingInfo
decl_stmt|;
specifier|public
name|void
name|setArtistName
parameter_list|(
name|String
name|artistName
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"artistName"
argument_list|,
name|this
operator|.
name|artistName
argument_list|,
name|artistName
argument_list|)
expr_stmt|;
name|this
operator|.
name|artistName
operator|=
name|artistName
expr_stmt|;
block|}
specifier|public
name|String
name|getArtistName
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"artistName"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|artistName
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
name|beforePropertyWrite
argument_list|(
literal|"estimatedPrice"
argument_list|,
name|this
operator|.
name|estimatedPrice
argument_list|,
name|estimatedPrice
argument_list|)
expr_stmt|;
name|this
operator|.
name|estimatedPrice
operator|=
name|estimatedPrice
expr_stmt|;
block|}
specifier|public
name|BigDecimal
name|getEstimatedPrice
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"estimatedPrice"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|estimatedPrice
return|;
block|}
specifier|public
name|void
name|setGalleryName
parameter_list|(
name|String
name|galleryName
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"galleryName"
argument_list|,
name|this
operator|.
name|galleryName
argument_list|,
name|galleryName
argument_list|)
expr_stmt|;
name|this
operator|.
name|galleryName
operator|=
name|galleryName
expr_stmt|;
block|}
specifier|public
name|String
name|getGalleryName
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"galleryName"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|galleryName
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
name|beforePropertyWrite
argument_list|(
literal|"paintingTitle"
argument_list|,
name|this
operator|.
name|paintingTitle
argument_list|,
name|paintingTitle
argument_list|)
expr_stmt|;
name|this
operator|.
name|paintingTitle
operator|=
name|paintingTitle
expr_stmt|;
block|}
specifier|public
name|String
name|getPaintingTitle
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"paintingTitle"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|paintingTitle
return|;
block|}
specifier|public
name|void
name|setTextReview
parameter_list|(
name|String
name|textReview
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"textReview"
argument_list|,
name|this
operator|.
name|textReview
argument_list|,
name|textReview
argument_list|)
expr_stmt|;
name|this
operator|.
name|textReview
operator|=
name|textReview
expr_stmt|;
block|}
specifier|public
name|String
name|getTextReview
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"textReview"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|textReview
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
annotation|@
name|Override
specifier|public
name|Object
name|readPropertyDirectly
parameter_list|(
name|String
name|propName
parameter_list|)
block|{
if|if
condition|(
name|propName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
switch|switch
condition|(
name|propName
condition|)
block|{
case|case
literal|"artistName"
case|:
return|return
name|this
operator|.
name|artistName
return|;
case|case
literal|"estimatedPrice"
case|:
return|return
name|this
operator|.
name|estimatedPrice
return|;
case|case
literal|"galleryName"
case|:
return|return
name|this
operator|.
name|galleryName
return|;
case|case
literal|"paintingTitle"
case|:
return|return
name|this
operator|.
name|paintingTitle
return|;
case|case
literal|"textReview"
case|:
return|return
name|this
operator|.
name|textReview
return|;
case|case
literal|"toArtist"
case|:
return|return
name|this
operator|.
name|toArtist
return|;
case|case
literal|"toGallery"
case|:
return|return
name|this
operator|.
name|toGallery
return|;
case|case
literal|"toPaintingInfo"
case|:
return|return
name|this
operator|.
name|toPaintingInfo
return|;
default|default:
return|return
name|super
operator|.
name|readPropertyDirectly
argument_list|(
name|propName
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|writePropertyDirectly
parameter_list|(
name|String
name|propName
parameter_list|,
name|Object
name|val
parameter_list|)
block|{
if|if
condition|(
name|propName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
switch|switch
condition|(
name|propName
condition|)
block|{
case|case
literal|"artistName"
case|:
name|this
operator|.
name|artistName
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"estimatedPrice"
case|:
name|this
operator|.
name|estimatedPrice
operator|=
operator|(
name|BigDecimal
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"galleryName"
case|:
name|this
operator|.
name|galleryName
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"paintingTitle"
case|:
name|this
operator|.
name|paintingTitle
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"textReview"
case|:
name|this
operator|.
name|textReview
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"toArtist"
case|:
name|this
operator|.
name|toArtist
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"toGallery"
case|:
name|this
operator|.
name|toGallery
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"toPaintingInfo"
case|:
name|this
operator|.
name|toPaintingInfo
operator|=
name|val
expr_stmt|;
break|break;
default|default:
name|super
operator|.
name|writePropertyDirectly
argument_list|(
name|propName
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|writeObject
parameter_list|(
name|ObjectOutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|writeSerialized
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|readSerialized
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|writeState
parameter_list|(
name|ObjectOutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|writeState
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|artistName
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|estimatedPrice
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|galleryName
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|paintingTitle
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|textReview
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|toArtist
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|toGallery
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|toPaintingInfo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|readState
parameter_list|(
name|ObjectInputStream
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|super
operator|.
name|readState
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|this
operator|.
name|artistName
operator|=
operator|(
name|String
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|estimatedPrice
operator|=
operator|(
name|BigDecimal
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|galleryName
operator|=
operator|(
name|String
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|paintingTitle
operator|=
operator|(
name|String
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|textReview
operator|=
operator|(
name|String
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|toArtist
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|toGallery
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|toPaintingInfo
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

