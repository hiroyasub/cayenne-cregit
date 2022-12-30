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
name|inheritance_vertical
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
name|inheritance_vertical
operator|.
name|IvRoot
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
name|inheritance_vertical
operator|.
name|IvSub2
import|;
end_import

begin_comment
comment|/**  * Class _IvSub2 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_IvSub2
extends|extends
name|IvRoot
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
name|IvSub2
argument_list|>
name|SELF
init|=
name|PropertyFactory
operator|.
name|createSelf
argument_list|(
name|IvSub2
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
name|SUB2ATTR
init|=
name|PropertyFactory
operator|.
name|createString
argument_list|(
literal|"sub2Attr"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|StringProperty
argument_list|<
name|String
argument_list|>
name|SUB2NAME
init|=
name|PropertyFactory
operator|.
name|createString
argument_list|(
literal|"sub2Name"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|sub2Attr
decl_stmt|;
specifier|protected
name|String
name|sub2Name
decl_stmt|;
specifier|public
name|void
name|setSub2Attr
parameter_list|(
name|String
name|sub2Attr
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"sub2Attr"
argument_list|,
name|this
operator|.
name|sub2Attr
argument_list|,
name|sub2Attr
argument_list|)
expr_stmt|;
name|this
operator|.
name|sub2Attr
operator|=
name|sub2Attr
expr_stmt|;
block|}
specifier|public
name|String
name|getSub2Attr
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"sub2Attr"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|sub2Attr
return|;
block|}
specifier|public
name|void
name|setSub2Name
parameter_list|(
name|String
name|sub2Name
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"sub2Name"
argument_list|,
name|this
operator|.
name|sub2Name
argument_list|,
name|sub2Name
argument_list|)
expr_stmt|;
name|this
operator|.
name|sub2Name
operator|=
name|sub2Name
expr_stmt|;
block|}
specifier|public
name|String
name|getSub2Name
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"sub2Name"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|sub2Name
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
literal|"sub2Attr"
case|:
return|return
name|this
operator|.
name|sub2Attr
return|;
case|case
literal|"sub2Name"
case|:
return|return
name|this
operator|.
name|sub2Name
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
literal|"sub2Attr"
case|:
name|this
operator|.
name|sub2Attr
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"sub2Name"
case|:
name|this
operator|.
name|sub2Name
operator|=
operator|(
name|String
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
name|sub2Attr
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|sub2Name
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
name|sub2Attr
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
name|sub2Name
operator|=
operator|(
name|String
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

