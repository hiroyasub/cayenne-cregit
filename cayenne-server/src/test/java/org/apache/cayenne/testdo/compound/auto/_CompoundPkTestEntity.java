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
name|compound
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
name|compound
operator|.
name|CompoundFkTestEntity
import|;
end_import

begin_comment
comment|/**  * Class _CompoundPkTestEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_CompoundPkTestEntity
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
name|KEY1_PK_COLUMN
init|=
literal|"KEY1"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KEY2_PK_COLUMN
init|=
literal|"KEY2"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|KEY1
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"key1"
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
name|String
argument_list|>
name|KEY2
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"key2"
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
name|CompoundFkTestEntity
argument_list|>
argument_list|>
name|COMPOUND_FK_ARRAY
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"compoundFkArray"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|key1
decl_stmt|;
specifier|protected
name|String
name|key2
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|Object
name|compoundFkArray
decl_stmt|;
specifier|public
name|void
name|setKey1
parameter_list|(
name|String
name|key1
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"key1"
argument_list|,
name|this
operator|.
name|key1
argument_list|,
name|key1
argument_list|)
expr_stmt|;
name|this
operator|.
name|key1
operator|=
name|key1
expr_stmt|;
block|}
specifier|public
name|String
name|getKey1
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"key1"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|key1
return|;
block|}
specifier|public
name|void
name|setKey2
parameter_list|(
name|String
name|key2
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"key2"
argument_list|,
name|this
operator|.
name|key2
argument_list|,
name|key2
argument_list|)
expr_stmt|;
name|this
operator|.
name|key2
operator|=
name|key2
expr_stmt|;
block|}
specifier|public
name|String
name|getKey2
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"key2"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|key2
return|;
block|}
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
name|addToCompoundFkArray
parameter_list|(
name|CompoundFkTestEntity
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"compoundFkArray"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromCompoundFkArray
parameter_list|(
name|CompoundFkTestEntity
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"compoundFkArray"
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
name|CompoundFkTestEntity
argument_list|>
name|getCompoundFkArray
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|CompoundFkTestEntity
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"compoundFkArray"
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
literal|"key1"
case|:
return|return
name|this
operator|.
name|key1
return|;
case|case
literal|"key2"
case|:
return|return
name|this
operator|.
name|key2
return|;
case|case
literal|"name"
case|:
return|return
name|this
operator|.
name|name
return|;
case|case
literal|"compoundFkArray"
case|:
return|return
name|this
operator|.
name|compoundFkArray
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
literal|"key1"
case|:
name|this
operator|.
name|key1
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"key2"
case|:
name|this
operator|.
name|key2
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
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
literal|"compoundFkArray"
case|:
name|this
operator|.
name|compoundFkArray
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
name|key1
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|key2
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
name|compoundFkArray
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
name|key1
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
name|key2
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
name|compoundFkArray
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

