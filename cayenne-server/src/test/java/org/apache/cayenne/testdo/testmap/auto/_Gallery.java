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
name|Exhibit
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
name|Painting
import|;
end_import

begin_comment
comment|/**  * Class _Gallery was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Gallery
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
name|GALLERY_ID_PK_COLUMN
init|=
literal|"GALLERY_ID"
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
name|List
argument_list|<
name|Exhibit
argument_list|>
argument_list|>
name|EXHIBIT_ARRAY
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"exhibitArray"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|List
argument_list|<
name|Painting
argument_list|>
argument_list|>
name|PAINTING_ARRAY
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"paintingArray"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setGalleryName
parameter_list|(
name|String
name|galleryName
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"galleryName"
argument_list|,
name|galleryName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getGalleryName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"galleryName"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToExhibitArray
parameter_list|(
name|Exhibit
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"exhibitArray"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromExhibitArray
parameter_list|(
name|Exhibit
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"exhibitArray"
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
name|Exhibit
argument_list|>
name|getExhibitArray
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Exhibit
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"exhibitArray"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToPaintingArray
parameter_list|(
name|Painting
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"paintingArray"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromPaintingArray
parameter_list|(
name|Painting
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"paintingArray"
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
name|Painting
argument_list|>
name|getPaintingArray
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Painting
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"paintingArray"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

