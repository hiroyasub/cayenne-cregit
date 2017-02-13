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
name|meaningful_pk
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

begin_comment
comment|/**  * Class _MeaningfulPk was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_MeaningfulPk
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
name|PK_PK_COLUMN
init|=
literal|"PK"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|PK
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"pk"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setPk
parameter_list|(
name|String
name|pk
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"pk"
argument_list|,
name|pk
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPk
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"pk"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

