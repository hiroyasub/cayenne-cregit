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
name|mt
operator|.
name|MtTable2
import|;
end_import

begin_comment
comment|/**  * Class _MtTable1 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_MtTable1
extends|extends
name|CayenneDataObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|GLOBAL_ATTRIBUTE1_PROPERTY
init|=
literal|"globalAttribute1"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_ATTRIBUTE1_PROPERTY
init|=
literal|"serverAttribute1"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TABLE2ARRAY_PROPERTY
init|=
literal|"table2Array"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TABLE1_ID_PK_COLUMN
init|=
literal|"TABLE1_ID"
decl_stmt|;
specifier|public
name|void
name|setGlobalAttribute1
parameter_list|(
name|String
name|globalAttribute1
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"globalAttribute1"
argument_list|,
name|globalAttribute1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getGlobalAttribute1
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"globalAttribute1"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setServerAttribute1
parameter_list|(
name|String
name|serverAttribute1
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"serverAttribute1"
argument_list|,
name|serverAttribute1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getServerAttribute1
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"serverAttribute1"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToTable2Array
parameter_list|(
name|MtTable2
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"table2Array"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromTable2Array
parameter_list|(
name|MtTable2
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"table2Array"
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
name|MtTable2
argument_list|>
name|getTable2Array
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|MtTable2
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"table2Array"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

