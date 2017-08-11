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
name|things
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
name|things
operator|.
name|Ball
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
name|things
operator|.
name|Box
import|;
end_import

begin_comment
comment|/**  * Class _Thing was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Thing
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
name|ID_PK_COLUMN
init|=
literal|"ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|VOLUME
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"volume"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|WEIGHT
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"weight"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Ball
argument_list|>
name|BALL
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"ball"
argument_list|,
name|Ball
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
name|Box
argument_list|>
argument_list|>
name|BOX
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"box"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Integer
name|volume
decl_stmt|;
specifier|protected
name|Integer
name|weight
decl_stmt|;
specifier|protected
name|Object
name|ball
decl_stmt|;
specifier|protected
name|Object
name|box
decl_stmt|;
specifier|public
name|void
name|setVolume
parameter_list|(
name|Integer
name|volume
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"volume"
argument_list|,
name|this
operator|.
name|volume
argument_list|,
name|volume
argument_list|)
expr_stmt|;
name|this
operator|.
name|volume
operator|=
name|volume
expr_stmt|;
block|}
specifier|public
name|Integer
name|getVolume
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"volume"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|volume
return|;
block|}
specifier|public
name|void
name|setWeight
parameter_list|(
name|Integer
name|weight
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"weight"
argument_list|,
name|this
operator|.
name|weight
argument_list|,
name|weight
argument_list|)
expr_stmt|;
name|this
operator|.
name|weight
operator|=
name|weight
expr_stmt|;
block|}
specifier|public
name|Integer
name|getWeight
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"weight"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|weight
return|;
block|}
specifier|public
name|void
name|setBall
parameter_list|(
name|Ball
name|ball
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"ball"
argument_list|,
name|ball
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Ball
name|getBall
parameter_list|()
block|{
return|return
operator|(
name|Ball
operator|)
name|readProperty
argument_list|(
literal|"ball"
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
argument_list|<
name|Box
argument_list|>
name|getBox
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Box
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"box"
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
literal|"volume"
case|:
return|return
name|this
operator|.
name|volume
return|;
case|case
literal|"weight"
case|:
return|return
name|this
operator|.
name|weight
return|;
case|case
literal|"ball"
case|:
return|return
name|this
operator|.
name|ball
return|;
case|case
literal|"box"
case|:
return|return
name|this
operator|.
name|box
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
literal|"volume"
case|:
name|this
operator|.
name|volume
operator|=
operator|(
name|Integer
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"weight"
case|:
name|this
operator|.
name|weight
operator|=
operator|(
name|Integer
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"ball"
case|:
name|this
operator|.
name|ball
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"box"
case|:
name|this
operator|.
name|box
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
name|volume
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|weight
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|ball
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|box
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
name|volume
operator|=
operator|(
name|Integer
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|weight
operator|=
operator|(
name|Integer
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|ball
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|box
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

