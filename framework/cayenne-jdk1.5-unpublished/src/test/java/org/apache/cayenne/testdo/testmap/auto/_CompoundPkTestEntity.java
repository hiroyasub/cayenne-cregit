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
name|testmap
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
name|testmap
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
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|KEY1_PROPERTY
init|=
literal|"key1"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|KEY2_PROPERTY
init|=
literal|"key2"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|NAME_PROPERTY
init|=
literal|"name"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|COMPOUND_FK_ARRAY_PROPERTY
init|=
literal|"compoundFkArray"
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
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"key1"
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
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"key2"
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
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"name"
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
operator|new
name|Property
argument_list|<
name|List
argument_list|<
name|CompoundFkTestEntity
argument_list|>
argument_list|>
argument_list|(
literal|"compoundFkArray"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setKey1
parameter_list|(
name|String
name|key1
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"key1"
argument_list|,
name|key1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getKey1
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"key1"
argument_list|)
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
name|writeProperty
argument_list|(
literal|"key2"
argument_list|,
name|key2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getKey2
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"key2"
argument_list|)
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
block|}
end_class

end_unit

