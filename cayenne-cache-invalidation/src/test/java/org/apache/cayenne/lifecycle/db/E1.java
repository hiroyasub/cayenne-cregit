begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|lifecycle
operator|.
name|db
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
name|lifecycle
operator|.
name|cache
operator|.
name|CacheGroups
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
name|lifecycle
operator|.
name|db
operator|.
name|auto
operator|.
name|_E1
import|;
end_import

begin_class
annotation|@
name|CacheGroups
argument_list|(
block|{
literal|"g1"
block|,
literal|"g2"
block|}
argument_list|)
specifier|public
class|class
name|E1
extends|extends
name|_E1
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
block|}
end_class

end_unit

