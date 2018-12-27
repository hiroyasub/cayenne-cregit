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
name|ExpressionFactory
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
name|ListProperty
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
name|NumericProperty
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
name|Thing
import|;
end_import

begin_comment
comment|/**  * Class _Bag was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Bag
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
name|NumericProperty
argument_list|<
name|Long
argument_list|>
name|ID_PK_PROPERTY
init|=
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|ExpressionFactory
operator|.
name|dbPathExp
argument_list|(
literal|"ID"
argument_list|)
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
name|ID_PK_COLUMN
init|=
literal|"ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|StringProperty
argument_list|<
name|String
argument_list|>
name|NAME
init|=
name|PropertyFactory
operator|.
name|createString
argument_list|(
literal|"name"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|ListProperty
argument_list|<
name|Ball
argument_list|>
name|BALLS
init|=
name|PropertyFactory
operator|.
name|createList
argument_list|(
literal|"balls"
argument_list|,
name|Ball
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|ListProperty
argument_list|<
name|Box
argument_list|>
name|BOXES
init|=
name|PropertyFactory
operator|.
name|createList
argument_list|(
literal|"boxes"
argument_list|,
name|Box
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|ListProperty
argument_list|<
name|Thing
argument_list|>
name|THINGS
init|=
name|PropertyFactory
operator|.
name|createList
argument_list|(
literal|"things"
argument_list|,
name|Thing
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|Object
name|balls
decl_stmt|;
specifier|protected
name|Object
name|boxes
decl_stmt|;
specifier|protected
name|Object
name|things
decl_stmt|;
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"name"
argument_list|,
name|this
operator|.
name|name
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|name
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
name|Ball
argument_list|>
name|getBalls
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Ball
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"balls"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToBoxes
parameter_list|(
name|Box
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"boxes"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromBoxes
parameter_list|(
name|Box
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"boxes"
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
name|Box
argument_list|>
name|getBoxes
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
literal|"boxes"
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
name|Thing
argument_list|>
name|getThings
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Thing
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"things"
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
literal|"name"
case|:
return|return
name|this
operator|.
name|name
return|;
case|case
literal|"balls"
case|:
return|return
name|this
operator|.
name|balls
return|;
case|case
literal|"boxes"
case|:
return|return
name|this
operator|.
name|boxes
return|;
case|case
literal|"things"
case|:
return|return
name|this
operator|.
name|things
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
literal|"name"
case|:
name|this
operator|.
name|name
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"balls"
case|:
name|this
operator|.
name|balls
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"boxes"
case|:
name|this
operator|.
name|boxes
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"things"
case|:
name|this
operator|.
name|things
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
name|name
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|balls
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|boxes
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|things
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
name|name
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
name|balls
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|boxes
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|things
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

