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
name|unsupported_distinct_types
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
name|unsupported_distinct_types
operator|.
name|Customer
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
name|unsupported_distinct_types
operator|.
name|Product
import|;
end_import

begin_comment
comment|/**  * Class _Product was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Product
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
name|String
argument_list|>
name|LONGVARCHAR_COL
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"longvarcharCol"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|List
argument_list|<
name|Product
argument_list|>
argument_list|>
name|BASE
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"base"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|List
argument_list|<
name|Product
argument_list|>
argument_list|>
name|CONTAINED
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"contained"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|List
argument_list|<
name|Customer
argument_list|>
argument_list|>
name|ORDER_BY
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"orderBy"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setLongvarcharCol
parameter_list|(
name|String
name|longvarcharCol
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"longvarcharCol"
argument_list|,
name|longvarcharCol
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getLongvarcharCol
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"longvarcharCol"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToBase
parameter_list|(
name|Product
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"base"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromBase
parameter_list|(
name|Product
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"base"
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
name|Product
argument_list|>
name|getBase
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Product
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"base"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToContained
parameter_list|(
name|Product
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"contained"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromContained
parameter_list|(
name|Product
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"contained"
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
name|Product
argument_list|>
name|getContained
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Product
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"contained"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToOrderBy
parameter_list|(
name|Customer
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"orderBy"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromOrderBy
parameter_list|(
name|Customer
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"orderBy"
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
name|Customer
argument_list|>
name|getOrderBy
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Customer
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"orderBy"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

