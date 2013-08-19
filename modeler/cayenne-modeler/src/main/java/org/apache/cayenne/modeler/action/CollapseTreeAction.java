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
name|action
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
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
name|Application
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
name|util
operator|.
name|CayenneAction
import|;
end_import

begin_class
specifier|public
class|class
name|CollapseTreeAction
extends|extends
name|CayenneAction
block|{
specifier|private
specifier|final
specifier|static
name|String
name|COLLAPSE
init|=
literal|"collapse"
decl_stmt|;
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Collapse tree"
return|;
block|}
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-tree-collapse.png"
return|;
block|}
specifier|public
name|CollapseTreeAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|getActionName
argument_list|()
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getEditorView
argument_list|()
operator|.
name|getFilterController
argument_list|()
operator|.
name|treeExpOrCollPath
argument_list|(
name|COLLAPSE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

