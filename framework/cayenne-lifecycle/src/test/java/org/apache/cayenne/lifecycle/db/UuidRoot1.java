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
name|db
operator|.
name|auto
operator|.
name|_UuidRoot1
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
name|relationship
operator|.
name|ObjectIdRelationship
import|;
end_import

begin_class
annotation|@
name|ObjectIdRelationship
argument_list|(
name|_UuidRoot1
operator|.
name|UUID_PROPERTY
argument_list|)
specifier|public
class|class
name|UuidRoot1
extends|extends
name|_UuidRoot1
block|{  }
end_class

end_unit

