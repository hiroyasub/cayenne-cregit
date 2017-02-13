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
name|relationships_collection_to_many
operator|.
name|auto
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|relationships_collection_to_many
operator|.
name|CollectionToManyTarget
import|;
end_import

begin_comment
comment|/**  * Class _CollectionToMany was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_CollectionToMany
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
name|Collection
argument_list|<
name|CollectionToManyTarget
argument_list|>
argument_list|>
name|TARGETS
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"targets"
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|addToTargets
parameter_list|(
name|CollectionToManyTarget
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"targets"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromTargets
parameter_list|(
name|CollectionToManyTarget
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"targets"
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
name|Collection
argument_list|<
name|CollectionToManyTarget
argument_list|>
name|getTargets
parameter_list|()
block|{
return|return
operator|(
name|Collection
argument_list|<
name|CollectionToManyTarget
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"targets"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

