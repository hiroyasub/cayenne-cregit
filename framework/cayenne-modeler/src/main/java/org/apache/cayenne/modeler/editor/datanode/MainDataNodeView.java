begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

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
name|datanode
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|BorderLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|CardLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Font
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JButton
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JComboBox
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JLabel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTextField
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
name|CayenneWidgetFactory
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

begin_comment
comment|/**  * A view for the main DataNode editor tab.  *   */
end_comment

begin_class
specifier|public
class|class
name|MainDataNodeView
extends|extends
name|JPanel
block|{
specifier|protected
name|JTextField
name|dataNodeName
decl_stmt|;
specifier|protected
name|JComboBox
name|factories
decl_stmt|;
specifier|protected
name|JPanel
name|dataSourceDetail
decl_stmt|;
specifier|protected
name|CardLayout
name|dataSourceDetailLayout
decl_stmt|;
specifier|protected
name|JComboBox
name|localDataSources
decl_stmt|;
specifier|protected
name|JButton
name|configLocalDataSources
decl_stmt|;
specifier|protected
name|JComboBox
name|schemaUpdateStrategy
decl_stmt|;
specifier|public
name|MainDataNodeView
parameter_list|()
block|{
comment|// create widgets
name|this
operator|.
name|dataNodeName
operator|=
name|CayenneWidgetFactory
operator|.
name|createUndoableTextField
argument_list|()
expr_stmt|;
name|this
operator|.
name|factories
operator|=
name|CayenneWidgetFactory
operator|.
name|createUndoableComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|localDataSources
operator|=
name|CayenneWidgetFactory
operator|.
name|createUndoableComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|schemaUpdateStrategy
operator|=
name|CayenneWidgetFactory
operator|.
name|createUndoableComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataSourceDetailLayout
operator|=
operator|new
name|CardLayout
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataSourceDetail
operator|=
operator|new
name|JPanel
argument_list|(
name|dataSourceDetailLayout
argument_list|)
expr_stmt|;
name|this
operator|.
name|configLocalDataSources
operator|=
operator|new
name|JButton
argument_list|(
literal|"..."
argument_list|)
expr_stmt|;
name|this
operator|.
name|configLocalDataSources
operator|.
name|setToolTipText
argument_list|(
literal|"configure local DataSource"
argument_list|)
expr_stmt|;
comment|// assemble
name|DefaultFormBuilder
name|topPanelBuilder
init|=
operator|new
name|DefaultFormBuilder
argument_list|(
operator|new
name|FormLayout
argument_list|(
literal|"right:80dlu, 3dlu, fill:177dlu, 3dlu, fill:20dlu"
argument_list|,
literal|""
argument_list|)
argument_list|)
decl_stmt|;
name|topPanelBuilder
operator|.
name|setDefaultDialogBorder
argument_list|()
expr_stmt|;
name|topPanelBuilder
operator|.
name|appendSeparator
argument_list|(
literal|"DataNode Configuration"
argument_list|)
expr_stmt|;
name|topPanelBuilder
operator|.
name|append
argument_list|(
literal|"DataNode Name:"
argument_list|,
name|dataNodeName
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|topPanelBuilder
operator|.
name|append
argument_list|(
literal|"Schema Update Strategy:"
argument_list|,
name|schemaUpdateStrategy
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|DefaultFormBuilder
name|builderForLabel
init|=
operator|new
name|DefaultFormBuilder
argument_list|(
operator|new
name|FormLayout
argument_list|(
literal|"right:199dlu"
argument_list|)
argument_list|)
decl_stmt|;
name|JLabel
name|label
init|=
operator|new
name|JLabel
argument_list|(
literal|"You can enter custom class implementing SchemaUpdateStrategy"
argument_list|)
decl_stmt|;
name|Font
name|font
init|=
operator|new
name|Font
argument_list|(
name|getFont
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|Font
operator|.
name|PLAIN
argument_list|,
name|getFont
argument_list|()
operator|.
name|getSize
argument_list|()
operator|-
literal|2
argument_list|)
decl_stmt|;
name|label
operator|.
name|setFont
argument_list|(
name|font
argument_list|)
expr_stmt|;
name|builderForLabel
operator|.
name|append
argument_list|(
name|label
argument_list|)
expr_stmt|;
name|topPanelBuilder
operator|.
name|append
argument_list|(
literal|""
argument_list|,
name|builderForLabel
operator|.
name|getPanel
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|topPanelBuilder
operator|.
name|append
argument_list|(
literal|"Local DataSource (opt.):"
argument_list|,
name|localDataSources
argument_list|,
name|configLocalDataSources
argument_list|)
expr_stmt|;
name|topPanelBuilder
operator|.
name|append
argument_list|(
literal|"DataSource Factory:"
argument_list|,
name|factories
argument_list|,
literal|3
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
name|topPanelBuilder
operator|.
name|getPanel
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|dataSourceDetail
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JComboBox
name|getSchemaUpdateStrategy
parameter_list|()
block|{
return|return
name|schemaUpdateStrategy
return|;
block|}
specifier|public
name|JTextField
name|getDataNodeName
parameter_list|()
block|{
return|return
name|dataNodeName
return|;
block|}
specifier|public
name|JPanel
name|getDataSourceDetail
parameter_list|()
block|{
return|return
name|dataSourceDetail
return|;
block|}
specifier|public
name|JComboBox
name|getLocalDataSources
parameter_list|()
block|{
return|return
name|localDataSources
return|;
block|}
specifier|public
name|CardLayout
name|getDataSourceDetailLayout
parameter_list|()
block|{
return|return
name|dataSourceDetailLayout
return|;
block|}
specifier|public
name|JComboBox
name|getFactories
parameter_list|()
block|{
return|return
name|factories
return|;
block|}
specifier|public
name|JButton
name|getConfigLocalDataSources
parameter_list|()
block|{
return|return
name|configLocalDataSources
return|;
block|}
block|}
end_class

end_unit

