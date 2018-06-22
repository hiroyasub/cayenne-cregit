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
name|dialog
operator|.
name|codegen
operator|.
name|cgen
operator|.
name|CgenGlobalController
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

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

begin_class
specifier|public
class|class
name|CgenAction
extends|extends
name|CayenneAction
block|{
specifier|private
specifier|static
name|Logger
name|logObj
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CgenAction
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|CgenAction
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
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Generate All Classes"
return|;
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
operator|new
name|CgenGlobalController
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
argument_list|)
operator|.
name|startup
argument_list|()
expr_stmt|;
comment|//        Collection<DataMap> dataMaps;
comment|//        DataChannelMetaData metaData = getApplication().getMetaData();
comment|//
comment|//        try {
comment|//            Project project = getProjectController().getProject();
comment|//            dataMaps = ((DataChannelDescriptor) project.getRootNode()).getDataMaps();
comment|//            for (DataMap dataMap : dataMaps) {
comment|//                ClassGenerationAction classGenerationAction = metaData.get(dataMap, ClassGenerationAction.class);
comment|//                if (classGenerationAction != null) {
comment|//                    classGenerationAction.prepareArtifacts();
comment|//                    classGenerationAction.execute();
comment|//                }
comment|//            }
comment|//            JOptionPane.showMessageDialog(
comment|//                    this.getApplication().getFrameController().getView(),
comment|//                    "Class generation finished");
comment|//        } catch (Exception ex) {
comment|//            logObj.error("Error generating classes", e);
comment|//            JOptionPane.showMessageDialog(
comment|//                    this.getApplication().getFrameController().getView(),
comment|//                    "Error generating classes - " + ex.getMessage());
comment|//        }
block|}
block|}
end_class

end_unit

