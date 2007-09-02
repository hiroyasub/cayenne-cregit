begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|jpa
operator|.
name|itest
operator|.
name|ch6
operator|.
name|entity
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_class
annotation|@
name|Entity
specifier|public
class|class
name|UndeclaredEntity1
block|{
annotation|@
name|Id
specifier|protected
name|int
name|id
decl_stmt|;
block|}
end_class

end_unit

