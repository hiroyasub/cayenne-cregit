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

begin_comment
comment|/**  * Class _ROPainting was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ROPainting
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
name|ESTIMATED_PRICE_PROPERTY
init|=
literal|"estimatedPrice"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|PAINTING_TITLE_PROPERTY
init|=
literal|"paintingTitle"
decl_stmt|;
annotation|@
name|Deprecated
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
name|PAINTING_ID_PK_COLUMN
init|=
literal|"PAINTING_ID"
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
argument_list|<
name|BigDecimal
argument_list|>
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
name|PAINTING_TITLE
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"paintingTitle"
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
argument_list|<
name|Artist
argument_list|>
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
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
block|}
end_class

end_unit

