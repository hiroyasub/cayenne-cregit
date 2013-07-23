begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|project
operator|.
name|upgrade
operator|.
name|v7
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
name|Inject
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
name|Injector
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
name|project
operator|.
name|upgrade
operator|.
name|ProjectUpgrader
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
name|project
operator|.
name|upgrade
operator|.
name|UpgradeHandler
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
name|resource
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * A ProjectUpgrader that handles project upgrades from version 3.0.0.1 and 6  * to version 7  */
end_comment

begin_class
specifier|public
class|class
name|ProjectUpgrader_V7
implements|implements
name|ProjectUpgrader
block|{
annotation|@
name|Inject
specifier|protected
name|Injector
name|injector
decl_stmt|;
specifier|public
name|UpgradeHandler
name|getUpgradeHandler
parameter_list|(
name|Resource
name|projectSource
parameter_list|)
block|{
name|UpgradeHandler_V7
name|handler
init|=
operator|new
name|UpgradeHandler_V7
argument_list|(
name|projectSource
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"PH OK"
argument_list|)
expr_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|handler
argument_list|)
expr_stmt|;
return|return
name|handler
return|;
block|}
block|}
end_class

end_unit

