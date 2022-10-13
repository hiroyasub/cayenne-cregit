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
name|editor
operator|.
name|templateeditor
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
name|configuration
operator|.
name|xml
operator|.
name|DefaultDataChannelMetaData
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
name|gen
operator|.
name|ClassGenerationAction
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
name|gen
operator|.
name|TemplateType
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
name|map
operator|.
name|DataMap
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
name|map
operator|.
name|Embeddable
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
name|map
operator|.
name|ObjEntity
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
name|map
operator|.
name|QueryDescriptor
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
name|editor
operator|.
name|cgen
operator|.
name|templateeditor
operator|.
name|ArtefactsConfigurator
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
name|editor
operator|.
name|cgen
operator|.
name|templateeditor
operator|.
name|DataMapArtefactsConfigurator
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
name|editor
operator|.
name|cgen
operator|.
name|templateeditor
operator|.
name|EmbeddableArtefactsConfigurator
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
name|editor
operator|.
name|cgen
operator|.
name|templateeditor
operator|.
name|EntityArtefactsConfigurator
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
name|editor
operator|.
name|cgen
operator|.
name|templateeditor
operator|.
name|PreviewActionConfigurator
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
name|editor
operator|.
name|cgen
operator|.
name|templateeditor
operator|.
name|TemplateEditorController
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_class
specifier|public
class|class
name|PreviewActionConfiguratorTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_TEMPLATE_TEXT
init|=
literal|"TestTemplate"
decl_stmt|;
specifier|private
name|TemplateEditorController
name|editorController
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|config
parameter_list|()
block|{
name|DataMap
name|dataMap
init|=
name|configureDataMap
argument_list|()
decl_stmt|;
name|Application
name|application
init|=
name|mock
argument_list|(
name|Application
operator|.
name|class
argument_list|)
decl_stmt|;
name|this
operator|.
name|editorController
operator|=
name|mock
argument_list|(
name|TemplateEditorController
operator|.
name|class
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|editorController
operator|.
name|getApplication
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|application
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|application
operator|.
name|getMetaData
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|DefaultDataChannelMetaData
argument_list|()
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|editorController
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DataMap
name|configureDataMap
parameter_list|()
block|{
name|DataMap
name|dataMap
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|dataMap
operator|.
name|setName
argument_list|(
literal|"dataMap"
argument_list|)
expr_stmt|;
name|ObjEntity
name|objEntity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"objEntity"
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|addObjEntity
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
name|Embeddable
name|embeddable
init|=
operator|new
name|Embeddable
argument_list|(
literal|"embeddable"
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|addEmbeddable
argument_list|(
name|embeddable
argument_list|)
expr_stmt|;
name|QueryDescriptor
name|descriptor
init|=
name|QueryDescriptor
operator|.
name|descriptor
argument_list|(
name|QueryDescriptor
operator|.
name|SELECT_QUERY
argument_list|)
decl_stmt|;
name|descriptor
operator|.
name|setName
argument_list|(
literal|"queryDescriptor"
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|addQueryDescriptor
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
return|return
name|dataMap
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|previewActionEntityTest
parameter_list|()
throws|throws
name|Exception
block|{
name|actionTest
argument_list|(
literal|"objEntity"
argument_list|,
name|TemplateType
operator|.
name|ENTITY_SUBCLASS
argument_list|,
operator|new
name|EntityArtefactsConfigurator
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|previewActionEmbeddableTest
parameter_list|()
throws|throws
name|Exception
block|{
name|actionTest
argument_list|(
literal|"embeddable"
argument_list|,
name|TemplateType
operator|.
name|EMBEDDABLE_SUBCLASS
argument_list|,
operator|new
name|EmbeddableArtefactsConfigurator
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|previewActionDataMapTest
parameter_list|()
throws|throws
name|Exception
block|{
name|actionTest
argument_list|(
literal|"queryDescriptor"
argument_list|,
name|TemplateType
operator|.
name|DATAMAP_SUBCLASS
argument_list|,
operator|new
name|DataMapArtefactsConfigurator
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|actionTest
parameter_list|(
name|String
name|artifactName
parameter_list|,
name|TemplateType
name|type
parameter_list|,
name|ArtefactsConfigurator
name|configurator
parameter_list|)
throws|throws
name|Exception
block|{
name|Mockito
operator|.
name|when
argument_list|(
name|editorController
operator|.
name|getSelectedArtifactName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|artifactName
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|editorController
operator|.
name|getTemplateType
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|editorController
operator|.
name|getArtefactsConfigurator
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|configurator
argument_list|)
expr_stmt|;
name|PreviewActionConfigurator
name|actionConfigurator
init|=
operator|new
name|PreviewActionConfigurator
argument_list|(
name|editorController
argument_list|)
decl_stmt|;
name|ClassGenerationAction
name|action
init|=
name|actionConfigurator
operator|.
name|preparePreviewAction
argument_list|(
name|TEST_TEMPLATE_TEXT
argument_list|)
decl_stmt|;
name|action
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Writer
name|writer
init|=
name|actionConfigurator
operator|.
name|getWriter
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|TEST_TEMPLATE_TEXT
argument_list|,
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

