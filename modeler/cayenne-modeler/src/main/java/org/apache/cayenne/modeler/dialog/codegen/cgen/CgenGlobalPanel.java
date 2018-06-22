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
name|dialog
operator|.
name|codegen
operator|.
name|cgen
package|;
end_package

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|builder
operator|.
name|DefaultFormBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|FormLayout
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
name|ProjectController
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|CgenGlobalPanel
extends|extends
name|JPanel
block|{
specifier|private
name|JButton
name|generateButton
decl_stmt|;
specifier|private
name|JTextField
name|outputFolder
decl_stmt|;
specifier|private
name|JButton
name|selectOutputFolder
decl_stmt|;
specifier|private
name|JComboBox
name|generationMode
decl_stmt|;
specifier|private
name|JComboBox
name|subclassTemplate
decl_stmt|;
specifier|private
name|JComboBox
name|superclassTemplate
decl_stmt|;
specifier|private
name|JComboBox
name|embeddableTemplate
decl_stmt|;
specifier|private
name|JComboBox
name|embeddableSuperTemplate
decl_stmt|;
specifier|private
name|JComboBox
name|dataMapTemplate
decl_stmt|;
specifier|private
name|JComboBox
name|dataMapSuperTemplate
decl_stmt|;
specifier|private
name|JCheckBox
name|pairs
decl_stmt|;
specifier|private
name|JCheckBox
name|overwrite
decl_stmt|;
specifier|private
name|JCheckBox
name|usePackagePath
decl_stmt|;
specifier|private
name|JTextField
name|outputPattern
decl_stmt|;
specifier|private
name|JCheckBox
name|createPropertyNames
decl_stmt|;
specifier|private
name|JTextField
name|superclassPackage
decl_stmt|;
specifier|private
name|JTextField
name|encoding
decl_stmt|;
specifier|private
name|JButton
name|resetFolder
decl_stmt|;
specifier|private
name|JButton
name|resetMode
decl_stmt|;
specifier|private
name|JButton
name|resetDataMapTemplate
decl_stmt|;
specifier|private
name|JButton
name|resetDataMapSuperTemplate
decl_stmt|;
specifier|private
name|JButton
name|resetTemplate
decl_stmt|;
specifier|private
name|JButton
name|resetSuperTemplate
decl_stmt|;
specifier|private
name|JButton
name|resetEmbeddableTemplate
decl_stmt|;
specifier|private
name|JButton
name|resetEmbeddableSuperTemplate
decl_stmt|;
specifier|private
name|JButton
name|resetPattern
decl_stmt|;
specifier|private
name|JButton
name|resetEncoding
decl_stmt|;
specifier|private
name|JButton
name|resetPairs
decl_stmt|;
specifier|private
name|JButton
name|resetPath
decl_stmt|;
specifier|private
name|JButton
name|resetOverwrite
decl_stmt|;
specifier|private
name|JButton
name|resetNames
decl_stmt|;
specifier|private
name|JButton
name|resetPackage
decl_stmt|;
specifier|private
name|ProjectController
name|projectController
decl_stmt|;
name|CgenGlobalPanel
parameter_list|(
name|ProjectController
name|projectController
parameter_list|)
block|{
name|this
operator|.
name|projectController
operator|=
name|projectController
expr_stmt|;
name|this
operator|.
name|generateButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Generate All classes"
argument_list|)
expr_stmt|;
name|this
operator|.
name|outputFolder
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|this
operator|.
name|selectOutputFolder
operator|=
operator|new
name|JButton
argument_list|(
literal|"Select"
argument_list|)
expr_stmt|;
name|this
operator|.
name|generationMode
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|subclassTemplate
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|superclassTemplate
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|embeddableTemplate
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|embeddableSuperTemplate
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataMapTemplate
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataMapSuperTemplate
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|pairs
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|overwrite
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|usePackagePath
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|outputPattern
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|this
operator|.
name|createPropertyNames
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|encoding
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|this
operator|.
name|superclassPackage
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
comment|// assemble
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"right:100dlu, 3dlu, fill:50:grow, 6dlu, fill:70dlu, 3dlu"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|DefaultFormBuilder
name|builder
init|=
operator|new
name|DefaultFormBuilder
argument_list|(
name|layout
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setDefaultDialogBorder
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Generate Classes for dataMaps"
argument_list|,
name|generateButton
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|appendSeparator
argument_list|()
expr_stmt|;
name|resetFolder
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset Folder"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Output Directory:"
argument_list|,
name|outputFolder
argument_list|,
name|selectOutputFolder
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|resetFolder
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetMode
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset Mode"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Generation Mode:"
argument_list|,
name|generationMode
argument_list|,
name|resetMode
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetDataMapTemplate
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset Template"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"DataMap Template:"
argument_list|,
name|dataMapTemplate
argument_list|,
name|resetDataMapTemplate
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetDataMapSuperTemplate
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset Template"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"DataMap Superclass Template"
argument_list|,
name|dataMapSuperTemplate
argument_list|,
name|resetDataMapSuperTemplate
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetTemplate
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset Template"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Subclass Template:"
argument_list|,
name|subclassTemplate
argument_list|,
name|resetTemplate
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetSuperTemplate
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset Template"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Superclass Template:"
argument_list|,
name|superclassTemplate
argument_list|,
name|resetSuperTemplate
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetEmbeddableTemplate
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset Template"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Embeddable Template"
argument_list|,
name|embeddableTemplate
argument_list|,
name|resetEmbeddableTemplate
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetEmbeddableSuperTemplate
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset Template"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Embeddable Super Template"
argument_list|,
name|embeddableSuperTemplate
argument_list|,
name|resetEmbeddableSuperTemplate
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetPattern
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset pattern"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Output Pattern:"
argument_list|,
name|outputPattern
argument_list|,
name|resetPattern
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetEncoding
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset encoding"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Encoding"
argument_list|,
name|encoding
argument_list|,
name|resetEncoding
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetPairs
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset pairs"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Make Pairs:"
argument_list|,
name|pairs
argument_list|,
name|resetPairs
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetPath
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset path"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Use Package Path:"
argument_list|,
name|usePackagePath
argument_list|,
name|resetPath
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetOverwrite
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset overwrite"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Overwrite Subclasses:"
argument_list|,
name|overwrite
argument_list|,
name|resetOverwrite
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetNames
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset Names"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Create Property Names:"
argument_list|,
name|createPropertyNames
argument_list|,
name|resetNames
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|resetPackage
operator|=
operator|new
name|JButton
argument_list|(
literal|"Reset Package"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Superclass package"
argument_list|,
name|superclassPackage
argument_list|,
name|resetPackage
argument_list|)
expr_stmt|;
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|builder
operator|.
name|getPanel
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|builder
operator|.
name|getPanel
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JButton
name|getGenerateButton
parameter_list|()
block|{
return|return
name|generateButton
return|;
block|}
specifier|public
name|JTextField
name|getOutputFolder
parameter_list|()
block|{
return|return
name|outputFolder
return|;
block|}
specifier|public
name|JButton
name|getSelectOutputFolder
parameter_list|()
block|{
return|return
name|selectOutputFolder
return|;
block|}
specifier|public
name|JComboBox
name|getGenerationMode
parameter_list|()
block|{
return|return
name|generationMode
return|;
block|}
specifier|public
name|JComboBox
name|getSubclassTemplate
parameter_list|()
block|{
return|return
name|subclassTemplate
return|;
block|}
specifier|public
name|JComboBox
name|getSuperclassTemplate
parameter_list|()
block|{
return|return
name|superclassTemplate
return|;
block|}
specifier|public
name|JComboBox
name|getEmbeddableTemplate
parameter_list|()
block|{
return|return
name|embeddableTemplate
return|;
block|}
specifier|public
name|JComboBox
name|getEmbeddableSuperTemplate
parameter_list|()
block|{
return|return
name|embeddableSuperTemplate
return|;
block|}
specifier|public
name|JComboBox
name|getDataMapTemplate
parameter_list|()
block|{
return|return
name|dataMapTemplate
return|;
block|}
specifier|public
name|JComboBox
name|getDataMapSuperTemplate
parameter_list|()
block|{
return|return
name|dataMapSuperTemplate
return|;
block|}
specifier|public
name|JCheckBox
name|getPairs
parameter_list|()
block|{
return|return
name|pairs
return|;
block|}
specifier|public
name|JCheckBox
name|getOverwrite
parameter_list|()
block|{
return|return
name|overwrite
return|;
block|}
specifier|public
name|JCheckBox
name|getUsePackagePath
parameter_list|()
block|{
return|return
name|usePackagePath
return|;
block|}
specifier|public
name|JTextField
name|getOutputPattern
parameter_list|()
block|{
return|return
name|outputPattern
return|;
block|}
specifier|public
name|JCheckBox
name|getCreatePropertyNames
parameter_list|()
block|{
return|return
name|createPropertyNames
return|;
block|}
specifier|public
name|JTextField
name|getSuperclassPackage
parameter_list|()
block|{
return|return
name|superclassPackage
return|;
block|}
specifier|public
name|JTextField
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
specifier|public
name|JButton
name|getResetFolder
parameter_list|()
block|{
return|return
name|resetFolder
return|;
block|}
specifier|public
name|JButton
name|getResetMode
parameter_list|()
block|{
return|return
name|resetMode
return|;
block|}
specifier|public
name|JButton
name|getResetDataMapTemplate
parameter_list|()
block|{
return|return
name|resetDataMapTemplate
return|;
block|}
specifier|public
name|JButton
name|getResetDataMapSuperTemplate
parameter_list|()
block|{
return|return
name|resetDataMapSuperTemplate
return|;
block|}
specifier|public
name|JButton
name|getResetTemplate
parameter_list|()
block|{
return|return
name|resetTemplate
return|;
block|}
specifier|public
name|JButton
name|getResetSuperTemplate
parameter_list|()
block|{
return|return
name|resetSuperTemplate
return|;
block|}
specifier|public
name|JButton
name|getResetEmbeddableTemplate
parameter_list|()
block|{
return|return
name|resetEmbeddableTemplate
return|;
block|}
specifier|public
name|JButton
name|getResetEmbeddableSuperTemplate
parameter_list|()
block|{
return|return
name|resetEmbeddableSuperTemplate
return|;
block|}
specifier|public
name|JButton
name|getResetPattern
parameter_list|()
block|{
return|return
name|resetPattern
return|;
block|}
specifier|public
name|JButton
name|getResetEncoding
parameter_list|()
block|{
return|return
name|resetEncoding
return|;
block|}
specifier|public
name|JButton
name|getResetPairs
parameter_list|()
block|{
return|return
name|resetPairs
return|;
block|}
specifier|public
name|JButton
name|getResetPath
parameter_list|()
block|{
return|return
name|resetPath
return|;
block|}
specifier|public
name|JButton
name|getResetOverwrite
parameter_list|()
block|{
return|return
name|resetOverwrite
return|;
block|}
specifier|public
name|JButton
name|getResetNames
parameter_list|()
block|{
return|return
name|resetNames
return|;
block|}
specifier|public
name|JButton
name|getResetPackage
parameter_list|()
block|{
return|return
name|resetPackage
return|;
block|}
block|}
end_class

end_unit

