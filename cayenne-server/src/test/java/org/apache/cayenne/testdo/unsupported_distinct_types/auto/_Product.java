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
name|unsupported_distinct_types
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
name|unsupported_distinct_types
operator|.
name|Customer
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
name|unsupported_distinct_types
operator|.
name|Product
import|;
end_import

begin_comment
comment|/**  * Class _Product was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Product
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
name|Integer
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
name|LONGVARCHAR_COL
init|=
name|PropertyFactory
operator|.
name|createString
argument_list|(
literal|"longvarcharCol"
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
name|Product
argument_list|>
name|BASE
init|=
name|PropertyFactory
operator|.
name|createList
argument_list|(
literal|"base"
argument_list|,
name|Product
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|ListProperty
argument_list|<
name|Product
argument_list|>
name|CONTAINED
init|=
name|PropertyFactory
operator|.
name|createList
argument_list|(
literal|"contained"
argument_list|,
name|Product
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|ListProperty
argument_list|<
name|Customer
argument_list|>
name|ORDER_BY
init|=
name|PropertyFactory
operator|.
name|createList
argument_list|(
literal|"orderBy"
argument_list|,
name|Customer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|longvarcharCol
decl_stmt|;
specifier|protected
name|Object
name|base
decl_stmt|;
specifier|protected
name|Object
name|contained
decl_stmt|;
specifier|protected
name|Object
name|orderBy
decl_stmt|;
specifier|public
name|void
name|setLongvarcharCol
parameter_list|(
name|String
name|longvarcharCol
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"longvarcharCol"
argument_list|,
name|this
operator|.
name|longvarcharCol
argument_list|,
name|longvarcharCol
argument_list|)
expr_stmt|;
name|this
operator|.
name|longvarcharCol
operator|=
name|longvarcharCol
expr_stmt|;
block|}
specifier|public
name|String
name|getLongvarcharCol
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"longvarcharCol"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|longvarcharCol
return|;
block|}
specifier|public
name|void
name|addToBase
parameter_list|(
name|Product
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"base"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromBase
parameter_list|(
name|Product
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"base"
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
name|Product
argument_list|>
name|getBase
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Product
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"base"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToContained
parameter_list|(
name|Product
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"contained"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromContained
parameter_list|(
name|Product
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"contained"
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
name|Product
argument_list|>
name|getContained
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Product
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"contained"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToOrderBy
parameter_list|(
name|Customer
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"orderBy"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromOrderBy
parameter_list|(
name|Customer
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"orderBy"
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
name|Customer
argument_list|>
name|getOrderBy
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Customer
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"orderBy"
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
literal|"longvarcharCol"
case|:
return|return
name|this
operator|.
name|longvarcharCol
return|;
case|case
literal|"base"
case|:
return|return
name|this
operator|.
name|base
return|;
case|case
literal|"contained"
case|:
return|return
name|this
operator|.
name|contained
return|;
case|case
literal|"orderBy"
case|:
return|return
name|this
operator|.
name|orderBy
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
literal|"longvarcharCol"
case|:
name|this
operator|.
name|longvarcharCol
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"base"
case|:
name|this
operator|.
name|base
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"contained"
case|:
name|this
operator|.
name|contained
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"orderBy"
case|:
name|this
operator|.
name|orderBy
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
name|longvarcharCol
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|base
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|contained
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|orderBy
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
name|longvarcharCol
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
name|base
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|contained
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|orderBy
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

