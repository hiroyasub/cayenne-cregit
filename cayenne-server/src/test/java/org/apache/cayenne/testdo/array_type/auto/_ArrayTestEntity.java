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
name|array_type
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
comment|/**  * Class _ArrayTestEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ArrayTestEntity
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
name|Double
index|[]
argument_list|>
name|DOUBLE_ARRAY
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"doubleArray"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setDoubleArray
parameter_list|(
name|Double
index|[]
name|doubleArray
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"doubleArray"
argument_list|,
name|doubleArray
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Double
index|[]
name|getDoubleArray
parameter_list|()
block|{
return|return
operator|(
name|Double
index|[]
operator|)
name|readProperty
argument_list|(
literal|"doubleArray"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

