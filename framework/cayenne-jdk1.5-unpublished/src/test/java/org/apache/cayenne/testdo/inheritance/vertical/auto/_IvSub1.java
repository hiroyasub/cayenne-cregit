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
name|IvRoot
import|;
end_import

begin_comment
comment|/**  * Class _IvSub1 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_IvSub1
extends|extends
name|IvRoot
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SUB1NAME_PROPERTY
init|=
literal|"sub1Name"
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
name|setSub1Name
parameter_list|(
name|String
name|sub1Name
parameter_list|)
block|{
name|writeProperty
argument_list|(
name|SUB1NAME_PROPERTY
argument_list|,
name|sub1Name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getSub1Name
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
name|SUB1NAME_PROPERTY
argument_list|)
return|;
block|}
block|}
end_class

end_unit

