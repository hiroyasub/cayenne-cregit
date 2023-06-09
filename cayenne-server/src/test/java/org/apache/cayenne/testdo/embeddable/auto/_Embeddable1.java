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
name|embeddable
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
name|Serializable
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
name|EmbeddableObject
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
name|Persistent
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

begin_comment
comment|/**   * Embeddable class _Embeddable1 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,   * since it may be overwritten next time code is regenerated.   * If you need to make any customizations, please use subclass.   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Embeddable1
implements|implements
name|EmbeddableObject
implements|,
name|Serializable
block|{
specifier|public
specifier|static
specifier|final
name|StringProperty
argument_list|<
name|String
argument_list|>
name|EMBEDDED20
init|=
name|PropertyFactory
operator|.
name|createString
argument_list|(
literal|"embedded20"
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
name|EMBEDDED10
init|=
name|PropertyFactory
operator|.
name|createString
argument_list|(
literal|"embedded10"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// special properties injected by Cayenne
specifier|private
name|Persistent
name|owner
decl_stmt|;
specifier|private
name|String
name|embeddedProperty
decl_stmt|;
comment|// declared properties
specifier|protected
name|String
name|embedded20
decl_stmt|;
specifier|protected
name|String
name|embedded10
decl_stmt|;
comment|// lifecycle methods
specifier|protected
name|void
name|propertyWillChange
parameter_list|(
name|String
name|property
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
if|if
condition|(
name|owner
operator|!=
literal|null
operator|&&
name|owner
operator|.
name|getObjectContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|owner
operator|.
name|getObjectContext
argument_list|()
operator|.
name|propertyChanged
argument_list|(
name|owner
argument_list|,
name|embeddedProperty
operator|+
literal|"."
operator|+
name|property
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
block|}
comment|// declared getters and setters
specifier|public
name|void
name|setEmbedded20
parameter_list|(
name|String
name|embedded20
parameter_list|)
block|{
name|propertyWillChange
argument_list|(
literal|"embedded20"
argument_list|,
name|this
operator|.
name|embedded20
argument_list|,
name|embedded20
argument_list|)
expr_stmt|;
name|this
operator|.
name|embedded20
operator|=
name|embedded20
expr_stmt|;
block|}
specifier|public
name|String
name|getEmbedded20
parameter_list|()
block|{
return|return
name|embedded20
return|;
block|}
specifier|public
name|void
name|setEmbedded10
parameter_list|(
name|String
name|embedded10
parameter_list|)
block|{
name|propertyWillChange
argument_list|(
literal|"embedded10"
argument_list|,
name|this
operator|.
name|embedded10
argument_list|,
name|embedded10
argument_list|)
expr_stmt|;
name|this
operator|.
name|embedded10
operator|=
name|embedded10
expr_stmt|;
block|}
specifier|public
name|String
name|getEmbedded10
parameter_list|()
block|{
return|return
name|embedded10
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
literal|"embedded20"
case|:
return|return
name|this
operator|.
name|embedded20
return|;
case|case
literal|"embedded10"
case|:
return|return
name|this
operator|.
name|embedded10
return|;
default|default:
return|return
literal|null
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
literal|"embedded20"
case|:
name|this
operator|.
name|embedded20
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"embedded10"
case|:
name|this
operator|.
name|embedded10
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown property: "
operator|+
name|propName
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

