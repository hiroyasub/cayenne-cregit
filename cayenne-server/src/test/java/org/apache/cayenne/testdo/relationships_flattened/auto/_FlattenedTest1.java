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
name|FlattenedTest3
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
name|FlattenedTest5
import|;
end_import

begin_comment
comment|/**  * Class _FlattenedTest1 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_FlattenedTest1
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
name|FT1_ID_PK_COLUMN
init|=
literal|"FT1_ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|NAME
init|=
name|Property
operator|.
name|create
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
name|Property
argument_list|<
name|List
argument_list|<
name|FlattenedTest2
argument_list|>
argument_list|>
name|FT2ARRAY
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"ft2Array"
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
name|FlattenedTest3
argument_list|>
argument_list|>
name|FT3ARRAY
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"ft3Array"
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
name|FlattenedTest3
argument_list|>
argument_list|>
name|FT3OVER_COMPLEX
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"ft3OverComplex"
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
name|FlattenedTest4
argument_list|>
argument_list|>
name|FT4ARRAY_FOR1
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"ft4ArrayFor1"
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
name|FlattenedTest5
argument_list|>
argument_list|>
name|FT5ARRAY
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"ft5Array"
argument_list|,
name|List
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
name|ft2Array
decl_stmt|;
specifier|protected
name|Object
name|ft3Array
decl_stmt|;
specifier|protected
name|Object
name|ft3OverComplex
decl_stmt|;
specifier|protected
name|Object
name|ft4ArrayFor1
decl_stmt|;
specifier|protected
name|Object
name|ft5Array
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
name|name
return|;
block|}
specifier|public
name|void
name|addToFt2Array
parameter_list|(
name|FlattenedTest2
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"ft2Array"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromFt2Array
parameter_list|(
name|FlattenedTest2
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"ft2Array"
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
name|FlattenedTest2
argument_list|>
name|getFt2Array
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|FlattenedTest2
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"ft2Array"
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
name|FlattenedTest3
argument_list|>
name|getFt3Array
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|FlattenedTest3
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"ft3Array"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToFt3OverComplex
parameter_list|(
name|FlattenedTest3
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"ft3OverComplex"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromFt3OverComplex
parameter_list|(
name|FlattenedTest3
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"ft3OverComplex"
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
name|FlattenedTest3
argument_list|>
name|getFt3OverComplex
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|FlattenedTest3
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"ft3OverComplex"
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
name|FlattenedTest4
argument_list|>
name|getFt4ArrayFor1
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
literal|"ft4ArrayFor1"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToFt5Array
parameter_list|(
name|FlattenedTest5
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"ft5Array"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromFt5Array
parameter_list|(
name|FlattenedTest5
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"ft5Array"
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
name|FlattenedTest5
argument_list|>
name|getFt5Array
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|FlattenedTest5
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"ft5Array"
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
literal|"ft2Array"
case|:
return|return
name|this
operator|.
name|ft2Array
return|;
case|case
literal|"ft3Array"
case|:
return|return
name|this
operator|.
name|ft3Array
return|;
case|case
literal|"ft3OverComplex"
case|:
return|return
name|this
operator|.
name|ft3OverComplex
return|;
case|case
literal|"ft4ArrayFor1"
case|:
return|return
name|this
operator|.
name|ft4ArrayFor1
return|;
case|case
literal|"ft5Array"
case|:
return|return
name|this
operator|.
name|ft5Array
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
literal|"ft2Array"
case|:
name|this
operator|.
name|ft2Array
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"ft3Array"
case|:
name|this
operator|.
name|ft3Array
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"ft3OverComplex"
case|:
name|this
operator|.
name|ft3OverComplex
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"ft4ArrayFor1"
case|:
name|this
operator|.
name|ft4ArrayFor1
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"ft5Array"
case|:
name|this
operator|.
name|ft5Array
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
name|name
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|ft2Array
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|ft3Array
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|ft3OverComplex
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|ft4ArrayFor1
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|ft5Array
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
name|ft2Array
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|ft3Array
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|ft3OverComplex
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|ft4ArrayFor1
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|ft5Array
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

