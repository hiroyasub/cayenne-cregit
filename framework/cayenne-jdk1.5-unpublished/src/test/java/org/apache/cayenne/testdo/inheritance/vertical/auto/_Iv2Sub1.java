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
name|inheritance
operator|.
name|vertical
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
name|testdo
operator|.
name|inheritance
operator|.
name|vertical
operator|.
name|Iv2Root
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
name|inheritance
operator|.
name|vertical
operator|.
name|Iv2Sub2
import|;
end_import

begin_comment
comment|/**  * Class _Iv2Sub1 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Iv2Sub1
extends|extends
name|Iv2Root
block|{
specifier|public
specifier|static
specifier|final
name|String
name|IV2SUB2S_PROPERTY
init|=
literal|"iv2sub2s"
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
name|void
name|addToIv2sub2s
parameter_list|(
name|Iv2Sub2
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"iv2sub2s"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromIv2sub2s
parameter_list|(
name|Iv2Sub2
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"iv2sub2s"
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
name|Iv2Sub2
argument_list|>
name|getIv2sub2s
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Iv2Sub2
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"iv2sub2s"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

