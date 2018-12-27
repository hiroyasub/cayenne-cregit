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
name|mt
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
name|BaseProperty
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
name|mt
operator|.
name|MtTable2
import|;
end_import

begin_comment
comment|/**  * Class _MtTable3 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_MtTable3
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
name|NumericProperty
argument_list|<
name|Integer
argument_list|>
name|TABLE3_ID_PK_PROPERTY
init|=
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|ExpressionFactory
operator|.
name|dbPathExp
argument_list|(
literal|"TABLE3_ID"
argument_list|)
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
name|TABLE3_ID_PK_COLUMN
init|=
literal|"TABLE3_ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|BaseProperty
argument_list|<
name|byte
index|[]
argument_list|>
name|BINARY_COLUMN
init|=
name|PropertyFactory
operator|.
name|createBase
argument_list|(
literal|"binaryColumn"
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|StringProperty
argument_list|<
name|String
argument_list|>
name|CHAR_COLUMN
init|=
name|PropertyFactory
operator|.
name|createString
argument_list|(
literal|"charColumn"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|NumericProperty
argument_list|<
name|Integer
argument_list|>
name|INT_COLUMN
init|=
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
literal|"intColumn"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|ListProperty
argument_list|<
name|MtTable2
argument_list|>
name|TABLE2ARRAY
init|=
name|PropertyFactory
operator|.
name|createList
argument_list|(
literal|"table2Array"
argument_list|,
name|MtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|byte
index|[]
name|binaryColumn
decl_stmt|;
specifier|protected
name|String
name|charColumn
decl_stmt|;
specifier|protected
name|Integer
name|intColumn
decl_stmt|;
specifier|protected
name|Object
name|table2Array
decl_stmt|;
specifier|public
name|void
name|setBinaryColumn
parameter_list|(
name|byte
index|[]
name|binaryColumn
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"binaryColumn"
argument_list|,
name|this
operator|.
name|binaryColumn
argument_list|,
name|binaryColumn
argument_list|)
expr_stmt|;
name|this
operator|.
name|binaryColumn
operator|=
name|binaryColumn
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|getBinaryColumn
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"binaryColumn"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|binaryColumn
return|;
block|}
specifier|public
name|void
name|setCharColumn
parameter_list|(
name|String
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
name|String
name|getCharColumn
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"charColumn"
argument_list|)
expr_stmt|;
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
name|Integer
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
name|Integer
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
specifier|public
name|void
name|addToTable2Array
parameter_list|(
name|MtTable2
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"table2Array"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromTable2Array
parameter_list|(
name|MtTable2
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"table2Array"
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
name|MtTable2
argument_list|>
name|getTable2Array
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|MtTable2
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"table2Array"
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
literal|"binaryColumn"
case|:
return|return
name|this
operator|.
name|binaryColumn
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
case|case
literal|"table2Array"
case|:
return|return
name|this
operator|.
name|table2Array
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
literal|"binaryColumn"
case|:
name|this
operator|.
name|binaryColumn
operator|=
operator|(
name|byte
index|[]
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
name|String
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
operator|(
name|Integer
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"table2Array"
case|:
name|this
operator|.
name|table2Array
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
name|binaryColumn
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
name|writeObject
argument_list|(
name|this
operator|.
name|intColumn
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|table2Array
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
name|binaryColumn
operator|=
operator|(
name|byte
index|[]
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
name|String
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
name|table2Array
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

