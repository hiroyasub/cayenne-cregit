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
name|CompoundPkTestEntity
import|;
end_import

begin_comment
comment|/**  * Class _CompoundFkTestEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_CompoundFkTestEntity
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
name|TO_COMPOUND_PK_PROPERTY
init|=
literal|"toCompoundPk"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PKEY_PK_COLUMN
init|=
literal|"PKEY"
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
name|CompoundPkTestEntity
argument_list|>
name|TO_COMPOUND_PK
init|=
operator|new
name|Property
argument_list|<
name|CompoundPkTestEntity
argument_list|>
argument_list|(
literal|"toCompoundPk"
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
name|setToCompoundPk
parameter_list|(
name|CompoundPkTestEntity
name|toCompoundPk
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toCompoundPk"
argument_list|,
name|toCompoundPk
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CompoundPkTestEntity
name|getToCompoundPk
parameter_list|()
block|{
return|return
operator|(
name|CompoundPkTestEntity
operator|)
name|readProperty
argument_list|(
literal|"toCompoundPk"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

