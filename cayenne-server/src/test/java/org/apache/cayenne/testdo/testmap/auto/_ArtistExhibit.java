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
name|property
operator|.
name|EntityProperty
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
name|property
operator|.
name|NumericIdProperty
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
name|property
operator|.
name|PropertyFactory
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
name|Exhibit
import|;
end_import

begin_comment
comment|/**  * Class _ArtistExhibit was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ArtistExhibit
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
name|EntityProperty
argument_list|<
name|ArtistExhibit
argument_list|>
name|SELF
init|=
name|PropertyFactory
operator|.
name|createSelf
argument_list|(
name|ArtistExhibit
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|NumericIdProperty
argument_list|<
name|Long
argument_list|>
name|ARTIST_ID_PK_PROPERTY
init|=
name|PropertyFactory
operator|.
name|createNumericId
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|"ArtistExhibit"
argument_list|,
name|Long
operator|.
name|class
argument_list|)
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
name|NumericIdProperty
argument_list|<
name|Integer
argument_list|>
name|EXHIBIT_ID_PK_PROPERTY
init|=
name|PropertyFactory
operator|.
name|createNumericId
argument_list|(
literal|"EXHIBIT_ID"
argument_list|,
literal|"ArtistExhibit"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
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
name|EntityProperty
argument_list|<
name|Artist
argument_list|>
name|TO_ARTIST
init|=
name|PropertyFactory
operator|.
name|createEntity
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
name|EntityProperty
argument_list|<
name|Exhibit
argument_list|>
name|TO_EXHIBIT
init|=
name|PropertyFactory
operator|.
name|createEntity
argument_list|(
literal|"toExhibit"
argument_list|,
name|Exhibit
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Object
name|toArtist
decl_stmt|;
specifier|protected
name|Object
name|toExhibit
decl_stmt|;
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
name|setToExhibit
parameter_list|(
name|Exhibit
name|toExhibit
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toExhibit"
argument_list|,
name|toExhibit
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Exhibit
name|getToExhibit
parameter_list|()
block|{
return|return
operator|(
name|Exhibit
operator|)
name|readProperty
argument_list|(
literal|"toExhibit"
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
literal|"toArtist"
case|:
return|return
name|this
operator|.
name|toArtist
return|;
case|case
literal|"toExhibit"
case|:
return|return
name|this
operator|.
name|toExhibit
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
literal|"toExhibit"
case|:
name|this
operator|.
name|toExhibit
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
name|toArtist
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|toExhibit
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
name|toArtist
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|toExhibit
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

