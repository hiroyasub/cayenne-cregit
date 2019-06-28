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
name|relationships_flattened
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
name|relationships_flattened
operator|.
name|FlattenedTest1
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
name|relationships_flattened
operator|.
name|FlattenedTest2
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
name|relationships_flattened
operator|.
name|FlattenedTest4
import|;
end_import

begin_comment
comment|/**  * Class _FlattenedTest3 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_FlattenedTest3
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
name|FT3_ID_PK_COLUMN
init|=
literal|"FT3_ID"
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
name|FlattenedTest4
argument_list|>
name|FT4ARRAY
init|=
name|PropertyFactory
operator|.
name|createList
argument_list|(
literal|"ft4Array"
argument_list|,
name|FlattenedTest4
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|EntityProperty
argument_list|<
name|FlattenedTest1
argument_list|>
name|TO_FT1
init|=
name|PropertyFactory
operator|.
name|createEntity
argument_list|(
literal|"toFT1"
argument_list|,
name|FlattenedTest1
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|EntityProperty
argument_list|<
name|FlattenedTest2
argument_list|>
name|TO_FT2
init|=
name|PropertyFactory
operator|.
name|createEntity
argument_list|(
literal|"toFT2"
argument_list|,
name|FlattenedTest2
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
name|ft4Array
decl_stmt|;
specifier|protected
name|Object
name|toFT1
decl_stmt|;
specifier|protected
name|Object
name|toFT2
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
specifier|public
name|void
name|addToFt4Array
parameter_list|(
name|FlattenedTest4
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"ft4Array"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromFt4Array
parameter_list|(
name|FlattenedTest4
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"ft4Array"
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
name|FlattenedTest4
argument_list|>
name|getFt4Array
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|FlattenedTest4
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"ft4Array"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToFT1
parameter_list|(
name|FlattenedTest1
name|toFT1
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toFT1"
argument_list|,
name|toFT1
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FlattenedTest1
name|getToFT1
parameter_list|()
block|{
return|return
operator|(
name|FlattenedTest1
operator|)
name|readProperty
argument_list|(
literal|"toFT1"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToFT2
parameter_list|(
name|FlattenedTest2
name|toFT2
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toFT2"
argument_list|,
name|toFT2
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FlattenedTest2
name|getToFT2
parameter_list|()
block|{
return|return
operator|(
name|FlattenedTest2
operator|)
name|readProperty
argument_list|(
literal|"toFT2"
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
literal|"ft4Array"
case|:
return|return
name|this
operator|.
name|ft4Array
return|;
case|case
literal|"toFT1"
case|:
return|return
name|this
operator|.
name|toFT1
return|;
case|case
literal|"toFT2"
case|:
return|return
name|this
operator|.
name|toFT2
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
literal|"ft4Array"
case|:
name|this
operator|.
name|ft4Array
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"toFT1"
case|:
name|this
operator|.
name|toFT1
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"toFT2"
case|:
name|this
operator|.
name|toFT2
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
name|ft4Array
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|toFT1
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|toFT2
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
name|ft4Array
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|toFT1
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|toFT2
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

