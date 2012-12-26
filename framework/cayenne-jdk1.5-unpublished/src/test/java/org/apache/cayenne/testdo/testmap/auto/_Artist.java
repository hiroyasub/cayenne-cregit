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
name|ArtGroup
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
name|Painting
import|;
end_import

begin_comment
comment|/**  * Class _Artist was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Artist
extends|extends
name|CayenneDataObject
block|{
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|ARTIST_NAME_PROPERTY
init|=
literal|"artistName"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|DATE_OF_BIRTH_PROPERTY
init|=
literal|"dateOfBirth"
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
name|GROUP_ARRAY_PROPERTY
init|=
literal|"groupArray"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|PAINTING_ARRAY_PROPERTY
init|=
literal|"paintingArray"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ARTIST_ID_PK_COLUMN
init|=
literal|"ARTIST_ID"
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
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"artistName"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Date
argument_list|>
name|DATE_OF_BIRTH
init|=
operator|new
name|Property
argument_list|<
name|Date
argument_list|>
argument_list|(
literal|"dateOfBirth"
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
name|List
argument_list|<
name|ArtGroup
argument_list|>
argument_list|>
name|GROUP_ARRAY
init|=
operator|new
name|Property
argument_list|<
name|List
argument_list|<
name|ArtGroup
argument_list|>
argument_list|>
argument_list|(
literal|"groupArray"
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
operator|new
name|Property
argument_list|<
name|List
argument_list|<
name|Painting
argument_list|>
argument_list|>
argument_list|(
literal|"paintingArray"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setArtistName
parameter_list|(
name|String
name|artistName
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"artistName"
argument_list|,
name|artistName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getArtistName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"artistName"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDateOfBirth
parameter_list|(
name|Date
name|dateOfBirth
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"dateOfBirth"
argument_list|,
name|dateOfBirth
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Date
name|getDateOfBirth
parameter_list|()
block|{
return|return
operator|(
name|Date
operator|)
name|readProperty
argument_list|(
literal|"dateOfBirth"
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
name|addToGroupArray
parameter_list|(
name|ArtGroup
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"groupArray"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromGroupArray
parameter_list|(
name|ArtGroup
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"groupArray"
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
name|ArtGroup
argument_list|>
name|getGroupArray
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|ArtGroup
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"groupArray"
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

