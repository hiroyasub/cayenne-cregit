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
name|mt
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
name|testdo
operator|.
name|mt
operator|.
name|MtTable1
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
name|mt
operator|.
name|MtTable3
import|;
end_import

begin_comment
comment|/**  * Class _MtTable2 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_MtTable2
extends|extends
name|CayenneDataObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|GLOBAL_ATTRIBUTE_PROPERTY
init|=
literal|"globalAttribute"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TABLE1_PROPERTY
init|=
literal|"table1"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TABLE3_PROPERTY
init|=
literal|"table3"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TABLE2_ID_PK_COLUMN
init|=
literal|"TABLE2_ID"
decl_stmt|;
specifier|public
name|void
name|setGlobalAttribute
parameter_list|(
name|String
name|globalAttribute
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"globalAttribute"
argument_list|,
name|globalAttribute
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getGlobalAttribute
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"globalAttribute"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setTable1
parameter_list|(
name|MtTable1
name|table1
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"table1"
argument_list|,
name|table1
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MtTable1
name|getTable1
parameter_list|()
block|{
return|return
operator|(
name|MtTable1
operator|)
name|readProperty
argument_list|(
literal|"table1"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setTable3
parameter_list|(
name|MtTable3
name|table3
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"table3"
argument_list|,
name|table3
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MtTable3
name|getTable3
parameter_list|()
block|{
return|return
operator|(
name|MtTable3
operator|)
name|readProperty
argument_list|(
literal|"table3"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

