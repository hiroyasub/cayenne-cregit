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
name|relationship
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
name|testdo
operator|.
name|relationship
operator|.
name|FlattenedTest1
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
name|relationship
operator|.
name|FlattenedTest3
import|;
end_import

begin_comment
comment|/**  * Class _FlattenedTest2 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_FlattenedTest2
extends|extends
name|CayenneDataObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NAME_PROPERTY
init|=
literal|"name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FT3ARRAY_PROPERTY
init|=
literal|"ft3Array"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TO_FT1_PROPERTY
init|=
literal|"toFT1"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FT2_ID_PK_COLUMN
init|=
literal|"FT2_ID"
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
name|NAME_PROPERTY
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
name|NAME_PROPERTY
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToFt3Array
parameter_list|(
name|FlattenedTest3
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
name|FT3ARRAY_PROPERTY
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromFt3Array
parameter_list|(
name|FlattenedTest3
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
name|FT3ARRAY_PROPERTY
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
name|FT3ARRAY_PROPERTY
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToFT1
parameter_list|(
name|FlattenedTest1
name|toFT1
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
name|TO_FT1_PROPERTY
argument_list|,
name|toFT1
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FlattenedTest1
name|getToFT1
parameter_list|()
block|{
return|return
operator|(
name|FlattenedTest1
operator|)
name|readProperty
argument_list|(
name|TO_FT1_PROPERTY
argument_list|)
return|;
block|}
block|}
end_class

end_unit
