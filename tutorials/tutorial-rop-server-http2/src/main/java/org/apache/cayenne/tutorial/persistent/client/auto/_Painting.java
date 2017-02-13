begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|tutorial
operator|.
name|persistent
operator|.
name|client
operator|.
name|auto
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|PersistentObject
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
name|ValueHolder
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
name|tutorial
operator|.
name|persistent
operator|.
name|client
operator|.
name|Artist
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
name|tutorial
operator|.
name|persistent
operator|.
name|client
operator|.
name|Gallery
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
name|util
operator|.
name|PersistentObjectHolder
import|;
end_import

begin_comment
comment|/**  * A generated persistent class mapped as "Painting" Cayenne entity. It is a good idea to  * avoid changing this class manually, since it will be overwritten next time code is  * regenerated. If you need to make any customizations, put them in a subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Painting
extends|extends
name|PersistentObject
block|{
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
name|Artist
argument_list|>
name|ARTIST
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"artist"
argument_list|,
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Gallery
argument_list|>
name|GALLERY
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"gallery"
argument_list|,
name|Gallery
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|ValueHolder
name|artist
decl_stmt|;
specifier|protected
name|ValueHolder
name|gallery
decl_stmt|;
specifier|public
name|String
name|getName
parameter_list|()
block|{
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|prepareForAccess
argument_list|(
name|this
argument_list|,
literal|"name"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|name
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
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|prepareForAccess
argument_list|(
name|this
argument_list|,
literal|"name"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|Object
name|oldValue
init|=
name|this
operator|.
name|name
decl_stmt|;
comment|// notify objectContext about simple property change
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|propertyChanged
argument_list|(
name|this
argument_list|,
literal|"name"
argument_list|,
name|oldValue
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Artist
name|getArtist
parameter_list|()
block|{
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|prepareForAccess
argument_list|(
name|this
argument_list|,
literal|"artist"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|this
operator|.
name|artist
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|artist
operator|=
operator|new
name|PersistentObjectHolder
argument_list|(
name|this
argument_list|,
literal|"artist"
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|Artist
operator|)
name|artist
operator|.
name|getValue
argument_list|()
return|;
block|}
specifier|public
name|void
name|setArtist
parameter_list|(
name|Artist
name|artist
parameter_list|)
block|{
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|prepareForAccess
argument_list|(
name|this
argument_list|,
literal|"artist"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|this
operator|.
name|artist
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|artist
operator|=
operator|new
name|PersistentObjectHolder
argument_list|(
name|this
argument_list|,
literal|"artist"
argument_list|)
expr_stmt|;
block|}
comment|// note how we notify ObjectContext of change BEFORE the object is actually
comment|// changed... this is needed to take a valid current snapshot
name|Object
name|oldValue
init|=
name|this
operator|.
name|artist
operator|.
name|getValueDirectly
argument_list|()
decl_stmt|;
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|propertyChanged
argument_list|(
name|this
argument_list|,
literal|"artist"
argument_list|,
name|oldValue
argument_list|,
name|artist
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|artist
operator|.
name|setValue
argument_list|(
name|artist
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Gallery
name|getGallery
parameter_list|()
block|{
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|prepareForAccess
argument_list|(
name|this
argument_list|,
literal|"gallery"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|this
operator|.
name|gallery
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|gallery
operator|=
operator|new
name|PersistentObjectHolder
argument_list|(
name|this
argument_list|,
literal|"gallery"
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|Gallery
operator|)
name|gallery
operator|.
name|getValue
argument_list|()
return|;
block|}
specifier|public
name|void
name|setGallery
parameter_list|(
name|Gallery
name|gallery
parameter_list|)
block|{
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|prepareForAccess
argument_list|(
name|this
argument_list|,
literal|"gallery"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|this
operator|.
name|gallery
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|gallery
operator|=
operator|new
name|PersistentObjectHolder
argument_list|(
name|this
argument_list|,
literal|"gallery"
argument_list|)
expr_stmt|;
block|}
comment|// note how we notify ObjectContext of change BEFORE the object is actually
comment|// changed... this is needed to take a valid current snapshot
name|Object
name|oldValue
init|=
name|this
operator|.
name|gallery
operator|.
name|getValueDirectly
argument_list|()
decl_stmt|;
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|propertyChanged
argument_list|(
name|this
argument_list|,
literal|"gallery"
argument_list|,
name|oldValue
argument_list|,
name|gallery
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|gallery
operator|.
name|setValue
argument_list|(
name|gallery
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

