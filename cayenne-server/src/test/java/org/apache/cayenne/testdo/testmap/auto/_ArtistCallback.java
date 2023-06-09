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
name|util
operator|.
name|Date
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
name|DateProperty
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
name|exp
operator|.
name|property
operator|.
name|SelfProperty
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
name|StringProperty
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
name|ArtistCallback
import|;
end_import

begin_comment
comment|/**  * Class _ArtistCallback was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ArtistCallback
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
name|SelfProperty
argument_list|<
name|ArtistCallback
argument_list|>
name|SELF
init|=
name|PropertyFactory
operator|.
name|createSelf
argument_list|(
name|ArtistCallback
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|NumericIdProperty
argument_list|<
name|Integer
argument_list|>
name|ARTIST_ID_PK_PROPERTY
init|=
name|PropertyFactory
operator|.
name|createNumericId
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|"ArtistCallback"
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
name|ARTIST_ID_PK_COLUMN
init|=
literal|"ARTIST_ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|StringProperty
argument_list|<
name|String
argument_list|>
name|ARTIST_NAME
init|=
name|PropertyFactory
operator|.
name|createString
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
name|DateProperty
argument_list|<
name|Date
argument_list|>
name|DATE_OF_BIRTH
init|=
name|PropertyFactory
operator|.
name|createDate
argument_list|(
literal|"dateOfBirth"
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|artistName
decl_stmt|;
specifier|protected
name|Date
name|dateOfBirth
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
name|setDateOfBirth
parameter_list|(
name|Date
name|dateOfBirth
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"dateOfBirth"
argument_list|,
name|this
operator|.
name|dateOfBirth
argument_list|,
name|dateOfBirth
argument_list|)
expr_stmt|;
name|this
operator|.
name|dateOfBirth
operator|=
name|dateOfBirth
expr_stmt|;
block|}
specifier|public
name|Date
name|getDateOfBirth
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"dateOfBirth"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|dateOfBirth
return|;
block|}
specifier|protected
specifier|abstract
name|void
name|prePersistEntityObjEntity
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|void
name|preRemoveEntityObjEntity
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|void
name|preUpdateEntityObjEntity
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|void
name|postPersistEntityObjEntity
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|void
name|postRemoveEntityObjEntity
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|void
name|postUpdateEntityObjEntity
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|void
name|postLoadEntityObjEntity
parameter_list|()
function_decl|;
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
literal|"dateOfBirth"
case|:
return|return
name|this
operator|.
name|dateOfBirth
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
literal|"dateOfBirth"
case|:
name|this
operator|.
name|dateOfBirth
operator|=
operator|(
name|Date
operator|)
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
name|dateOfBirth
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
name|dateOfBirth
operator|=
operator|(
name|Date
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

