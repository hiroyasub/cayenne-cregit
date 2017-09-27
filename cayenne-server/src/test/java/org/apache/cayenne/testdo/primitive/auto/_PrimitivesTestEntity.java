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
name|primitive
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
name|Property
import|;
end_import

begin_comment
comment|/**  * Class _PrimitivesTestEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_PrimitivesTestEntity
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
name|ID_PK_COLUMN
init|=
literal|"ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Boolean
argument_list|>
name|BOOLEAN_COLUMN
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"booleanColumn"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Character
argument_list|>
name|CHAR_COLUMN
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"charColumn"
argument_list|,
name|Character
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
name|INT_COLUMN
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"intColumn"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Boolean
name|booleanColumn
decl_stmt|;
specifier|protected
name|Character
name|charColumn
decl_stmt|;
specifier|protected
name|int
name|intColumn
decl_stmt|;
specifier|public
name|void
name|setBooleanColumn
parameter_list|(
name|boolean
name|booleanColumn
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"booleanColumn"
argument_list|,
name|this
operator|.
name|booleanColumn
argument_list|,
name|booleanColumn
argument_list|)
expr_stmt|;
name|this
operator|.
name|booleanColumn
operator|=
name|booleanColumn
expr_stmt|;
block|}
specifier|public
name|boolean
name|isBooleanColumn
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"booleanColumn"
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|booleanColumn
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|this
operator|.
name|booleanColumn
return|;
block|}
specifier|public
name|void
name|setCharColumn
parameter_list|(
name|char
name|charColumn
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"charColumn"
argument_list|,
name|this
operator|.
name|charColumn
argument_list|,
name|charColumn
argument_list|)
expr_stmt|;
name|this
operator|.
name|charColumn
operator|=
name|charColumn
expr_stmt|;
block|}
specifier|public
name|char
name|getCharColumn
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"charColumn"
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|charColumn
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|this
operator|.
name|charColumn
return|;
block|}
specifier|public
name|void
name|setIntColumn
parameter_list|(
name|int
name|intColumn
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"intColumn"
argument_list|,
name|this
operator|.
name|intColumn
argument_list|,
name|intColumn
argument_list|)
expr_stmt|;
name|this
operator|.
name|intColumn
operator|=
name|intColumn
expr_stmt|;
block|}
specifier|public
name|int
name|getIntColumn
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"intColumn"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|intColumn
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
literal|"booleanColumn"
case|:
return|return
name|this
operator|.
name|booleanColumn
return|;
case|case
literal|"charColumn"
case|:
return|return
name|this
operator|.
name|charColumn
return|;
case|case
literal|"intColumn"
case|:
return|return
name|this
operator|.
name|intColumn
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
literal|"booleanColumn"
case|:
name|this
operator|.
name|booleanColumn
operator|=
operator|(
name|Boolean
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"charColumn"
case|:
name|this
operator|.
name|charColumn
operator|=
operator|(
name|Character
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"intColumn"
case|:
name|this
operator|.
name|intColumn
operator|=
name|val
operator|==
literal|null
condition|?
literal|0
else|:
operator|(
name|int
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
name|booleanColumn
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|charColumn
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeInt
argument_list|(
name|this
operator|.
name|intColumn
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
name|booleanColumn
operator|=
operator|(
name|Boolean
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|charColumn
operator|=
operator|(
name|Character
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|intColumn
operator|=
name|in
operator|.
name|readInt
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

