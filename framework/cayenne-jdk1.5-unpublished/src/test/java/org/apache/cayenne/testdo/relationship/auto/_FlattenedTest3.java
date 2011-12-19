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
name|FlattenedTest2
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
name|FlattenedTest4
import|;
end_import

begin_comment
comment|/**  * Class _FlattenedTest3 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_FlattenedTest3
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
name|FT4ARRAY_PROPERTY
init|=
literal|"ft4Array"
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
name|TO_FT2_PROPERTY
init|=
literal|"toFT2"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FT3_ID_PK_COLUMN
init|=
literal|"FT3_ID"
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
name|addToFt4Array
parameter_list|(
name|FlattenedTest4
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
name|FT4ARRAY_PROPERTY
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromFt4Array
parameter_list|(
name|FlattenedTest4
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
name|FT4ARRAY_PROPERTY
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
name|FlattenedTest4
argument_list|>
name|getFt4Array
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|FlattenedTest4
argument_list|>
operator|)
name|readProperty
argument_list|(
name|FT4ARRAY_PROPERTY
argument_list|)
return|;
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
specifier|public
name|void
name|setToFT2
parameter_list|(
name|FlattenedTest2
name|toFT2
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
name|TO_FT2_PROPERTY
argument_list|,
name|toFT2
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FlattenedTest2
name|getToFT2
parameter_list|()
block|{
return|return
operator|(
name|FlattenedTest2
operator|)
name|readProperty
argument_list|(
name|TO_FT2_PROPERTY
argument_list|)
return|;
block|}
block|}
end_class

end_unit

