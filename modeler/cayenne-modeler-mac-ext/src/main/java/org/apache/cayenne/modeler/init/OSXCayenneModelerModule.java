begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|init
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
name|di
operator|.
name|Binder
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
name|di
operator|.
name|Module
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
name|modeler
operator|.
name|init
operator|.
name|platform
operator|.
name|OSXPlatformInitializer
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
name|modeler
operator|.
name|init
operator|.
name|platform
operator|.
name|PlatformInitializer
import|;
end_import

begin_class
specifier|public
class|class
name|OSXCayenneModelerModule
implements|implements
name|Module
block|{
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|PlatformInitializer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|OSXPlatformInitializer
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

