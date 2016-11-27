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
name|inheritance_vertical
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
name|inheritance_vertical
operator|.
name|auto
operator|.
name|_IvSub3
import|;
end_import

begin_class
specifier|public
class|class
name|IvSub3
extends|extends
name|_IvSub3
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|onPrePersist
parameter_list|()
block|{
comment|//        if(getIvRoot() == null) {
comment|//            throw new IllegalStateException("IvRoot must be set");
comment|//        }
block|}
block|}
end_class

end_unit

