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
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|ArtistExhibit
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

begin_comment
comment|/**  * Class _Exhibit was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Exhibit
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
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|CLOSING_DATE_PROPERTY
init|=
literal|"closingDate"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|OPENING_DATE_PROPERTY
init|=
literal|"openingDate"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|ARTIST_EXHIBIT_ARRAY_PROPERTY
init|=
literal|"artistExhibitArray"
decl_stmt|;
annotation|@
name|Deprecated
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
name|EXHIBIT_ID_PK_COLUMN
init|=
literal|"EXHIBIT_ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Date
argument_list|>
name|CLOSING_DATE
init|=
operator|new
name|Property
argument_list|<
name|Date
argument_list|>
argument_list|(
literal|"closingDate"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Date
argument_list|>
name|OPENING_DATE
init|=
operator|new
name|Property
argument_list|<
name|Date
argument_list|>
argument_list|(
literal|"openingDate"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|List
argument_list|<
name|ArtistExhibit
argument_list|>
argument_list|>
name|ARTIST_EXHIBIT_ARRAY
init|=
operator|new
name|Property
argument_list|<
name|List
argument_list|<
name|ArtistExhibit
argument_list|>
argument_list|>
argument_list|(
literal|"artistExhibitArray"
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
argument_list|<
name|Gallery
argument_list|>
argument_list|(
literal|"toGallery"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setClosingDate
parameter_list|(
name|Date
name|closingDate
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"closingDate"
argument_list|,
name|closingDate
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Date
name|getClosingDate
parameter_list|()
block|{
return|return
operator|(
name|Date
operator|)
name|readProperty
argument_list|(
literal|"closingDate"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setOpeningDate
parameter_list|(
name|Date
name|openingDate
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"openingDate"
argument_list|,
name|openingDate
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Date
name|getOpeningDate
parameter_list|()
block|{
return|return
operator|(
name|Date
operator|)
name|readProperty
argument_list|(
literal|"openingDate"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToArtistExhibitArray
parameter_list|(
name|ArtistExhibit
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"artistExhibitArray"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromArtistExhibitArray
parameter_list|(
name|ArtistExhibit
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"artistExhibitArray"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
argument_list|<
name|ArtistExhibit
argument_list|>
name|getArtistExhibitArray
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|ArtistExhibit
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"artistExhibitArray"
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
block|}
end_class

end_unit
