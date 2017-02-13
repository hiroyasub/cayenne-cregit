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
name|map_to_many
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
name|map_to_many
operator|.
name|MapToMany
import|;
end_import

begin_comment
comment|/**  * Class _MapToManyTarget was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_MapToManyTarget
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
name|MapToMany
argument_list|>
name|MAP_TO_MANY
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"mapToMany"
argument_list|,
name|MapToMany
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"name"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setMapToMany
parameter_list|(
name|MapToMany
name|mapToMany
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"mapToMany"
argument_list|,
name|mapToMany
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MapToMany
name|getMapToMany
parameter_list|()
block|{
return|return
operator|(
name|MapToMany
operator|)
name|readProperty
argument_list|(
literal|"mapToMany"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

