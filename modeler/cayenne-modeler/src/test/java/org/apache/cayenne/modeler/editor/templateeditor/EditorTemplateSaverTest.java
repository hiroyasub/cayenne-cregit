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
name|gen
operator|.
name|CgenConfiguration
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
name|modeler
operator|.
name|editor
operator|.
name|cgen
operator|.
name|templateeditor
operator|.
name|TemplateEditorView
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
name|EditorTemplateSaver
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
specifier|public
class|class
name|EditorTemplateSaverTest
block|{
specifier|private
name|CgenConfiguration
name|configuration
decl_stmt|;
specifier|private
name|EditorTemplateSaver
name|saver
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CUSTOM_TPL
init|=
literal|"Custom tpl"
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|createCgenConfiguration
parameter_list|()
block|{
name|this
operator|.
name|configuration
operator|=
operator|new
name|CgenConfiguration
argument_list|()
expr_stmt|;
name|TemplateEditorView
name|editorView
init|=
operator|new
name|TemplateEditorView
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
decl_stmt|;
name|editorView
operator|.
name|getEditingTemplatePane
argument_list|()
operator|.
name|setText
argument_list|(
name|CUSTOM_TPL
argument_list|)
expr_stmt|;
name|this
operator|.
name|saver
operator|=
operator|new
name|EditorTemplateSaver
argument_list|(
name|configuration
argument_list|,
name|editorView
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSaveCustom
parameter_list|()
block|{
name|configuration
operator|.
name|setMakePairs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|saver
operator|.
name|save
argument_list|(
name|TemplateType
operator|.
name|ENTITY_SUBCLASS
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|String
name|customTemplate
init|=
name|configuration
operator|.
name|getTemplate
argument_list|()
operator|.
name|getData
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|CUSTOM_TPL
argument_list|,
name|customTemplate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSaveDefault
parameter_list|()
block|{
name|configuration
operator|.
name|setMakePairs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|saver
operator|.
name|save
argument_list|(
name|TemplateType
operator|.
name|ENTITY_SUPERCLASS
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|configuration
operator|.
name|getSuperTemplate
argument_list|()
argument_list|,
name|TemplateType
operator|.
name|ENTITY_SUPERCLASS
operator|.
name|pathFromSourceRoot
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSaveSingleDefault
parameter_list|()
block|{
name|configuration
operator|.
name|setMakePairs
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|saver
operator|.
name|save
argument_list|(
name|TemplateType
operator|.
name|ENTITY_SUBCLASS
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|configuration
operator|.
name|getTemplate
argument_list|()
argument_list|,
name|TemplateType
operator|.
name|ENTITY_SINGLE_CLASS
operator|.
name|pathFromSourceRoot
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

