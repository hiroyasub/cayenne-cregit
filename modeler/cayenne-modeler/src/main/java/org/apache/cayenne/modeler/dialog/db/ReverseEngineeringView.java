begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *<p/>  * http://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  ****************************************************************/
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
name|dialog
operator|.
name|db
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
name|PanelBuilder
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
name|CellConstraints
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
name|CayenneRuntimeException
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
name|dbimport
operator|.
name|ReverseEngineering
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
name|modeler
operator|.
name|CayenneModelerController
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
name|db
operator|.
name|model
operator|.
name|DBModel
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
name|pref
operator|.
name|TreeEditor
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
name|pref
operator|.
name|XMLFileEditor
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
name|event
operator|.
name|DataMapDisplayEvent
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
name|event
operator|.
name|DataMapDisplayListener
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
name|ModelerUtil
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|BorderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|Icon
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
name|JSplitPane
import|;
end_import

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
name|FlowLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ReverseEngineeringView
extends|extends
name|JPanel
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CONFIGURATION_TEMPLATE
init|=
literal|"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
operator|+
literal|"<reverseEngineering\n"
operator|+
literal|"       xmlns=\"http://cayenne.apache.org/schema/8/reverseEngineering\"\n"
operator|+
literal|"       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
operator|+
literal|"       xsi:schemaLocation=\"http://cayenne.apache.org/schema/8/reverseEngineering "
operator|+
literal|"http://cayenne.apache.org/schema/8/reverseEngineering.xsd\">\n"
operator|+
literal|"\n"
operator|+
literal|"<skipRelationshipsLoading>false</skipRelationshipsLoading>\n"
operator|+
literal|"<skipPrimaryKeyLoading>false</skipPrimaryKeyLoading>\n"
operator|+
literal|"\n"
operator|+
literal|"<catalog>\n"
operator|+
literal|"<schema>\n"
operator|+
literal|"<includeTable>\n"
operator|+
literal|"</includeTable>\n"
operator|+
literal|"</schema>\n"
operator|+
literal|"</catalog>\n"
operator|+
literal|"<includeProcedure pattern=\".*\"/>\n"
operator|+
literal|"</reverseEngineering>"
decl_stmt|;
specifier|protected
name|ProjectController
name|controller
decl_stmt|;
specifier|protected
name|ReverseEngineeringController
name|reverseEngineeringController
decl_stmt|;
specifier|protected
name|JPanel
name|reverseEngineering
decl_stmt|;
specifier|protected
name|JComboBox
name|dataSources
decl_stmt|;
specifier|protected
name|JButton
name|configButton
decl_stmt|;
specifier|protected
name|JButton
name|syncButton
decl_stmt|;
specifier|protected
name|JButton
name|executeButton
decl_stmt|;
specifier|protected
name|PanelBuilder
name|builder
decl_stmt|;
specifier|protected
name|JSplitPane
name|splitPane
decl_stmt|;
specifier|protected
name|JLabel
name|xmlLabel
decl_stmt|;
specifier|protected
name|JLabel
name|treeLabel
decl_stmt|;
specifier|protected
name|Icon
name|icon
decl_stmt|;
specifier|protected
name|XMLFileEditor
name|xmlFileEditor
decl_stmt|;
specifier|protected
name|TreeEditor
name|treeEditor
decl_stmt|;
specifier|protected
name|DataMap
name|tempDataMap
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|DataMapViewModel
argument_list|>
name|reverseEngineeringViewMap
decl_stmt|;
specifier|public
name|ReverseEngineeringView
parameter_list|(
name|ProjectController
name|controller
parameter_list|)
block|{
name|this
operator|.
name|controller
operator|=
name|controller
expr_stmt|;
name|this
operator|.
name|reverseEngineeringViewMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|initView
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initView
parameter_list|()
block|{
name|this
operator|.
name|reverseEngineering
operator|=
operator|new
name|JPanel
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataSources
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|splitPane
operator|=
operator|new
name|JSplitPane
argument_list|(
name|JSplitPane
operator|.
name|HORIZONTAL_SPLIT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|configButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"..."
argument_list|)
expr_stmt|;
name|this
operator|.
name|configButton
operator|.
name|setToolTipText
argument_list|(
literal|"configure local DataSource"
argument_list|)
expr_stmt|;
name|this
operator|.
name|syncButton
operator|=
operator|new
name|JButton
argument_list|()
expr_stmt|;
name|this
operator|.
name|icon
operator|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-refresh.png"
argument_list|)
expr_stmt|;
name|this
operator|.
name|syncButton
operator|.
name|setIcon
argument_list|(
name|icon
argument_list|)
expr_stmt|;
name|this
operator|.
name|syncButton
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|executeButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Execute"
argument_list|)
expr_stmt|;
name|this
operator|.
name|treeLabel
operator|=
operator|new
name|JLabel
argument_list|(
literal|"Preview"
argument_list|)
expr_stmt|;
name|this
operator|.
name|xmlLabel
operator|=
operator|new
name|JLabel
argument_list|(
literal|"Reverse Engineering XML Editor"
argument_list|)
expr_stmt|;
name|this
operator|.
name|treeLabel
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|,
literal|5
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|xmlLabel
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|5
argument_list|,
literal|5
argument_list|,
literal|10
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|treeEditor
operator|=
operator|new
name|TreeEditor
argument_list|(
name|controller
argument_list|)
expr_stmt|;
name|this
operator|.
name|xmlFileEditor
operator|=
operator|new
name|XMLFileEditor
argument_list|(
name|controller
argument_list|)
expr_stmt|;
name|CellConstraints
name|cc
init|=
operator|new
name|CellConstraints
argument_list|()
decl_stmt|;
name|this
operator|.
name|builder
operator|=
operator|new
name|PanelBuilder
argument_list|(
operator|new
name|FormLayout
argument_list|(
literal|"210dlu:grow, pref, 0dlu, fill:max(172dlu;pref), 3dlu, fill:20dlu"
argument_list|,
literal|"p"
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setDefaultDialogBorder
argument_list|()
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|dataSources
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|4
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|configButton
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|6
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|JPanel
name|buttons
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|RIGHT
argument_list|)
argument_list|)
decl_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|executeButton
argument_list|)
expr_stmt|;
name|JPanel
name|treeHeaderComponent
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|LEFT
argument_list|)
argument_list|)
decl_stmt|;
name|treeHeaderComponent
operator|.
name|add
argument_list|(
name|treeLabel
argument_list|)
expr_stmt|;
name|treeHeaderComponent
operator|.
name|add
argument_list|(
name|syncButton
argument_list|)
expr_stmt|;
name|JPanel
name|leftComponent
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
name|leftComponent
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|leftComponent
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|leftComponent
operator|.
name|add
argument_list|(
name|xmlLabel
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
name|leftComponent
operator|.
name|add
argument_list|(
name|xmlFileEditor
operator|.
name|getView
argument_list|()
operator|.
name|getScrollPane
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|JPanel
name|rightComponent
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
name|rightComponent
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|rightComponent
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|rightComponent
operator|.
name|add
argument_list|(
name|treeHeaderComponent
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
name|rightComponent
operator|.
name|add
argument_list|(
name|treeEditor
operator|.
name|getView
argument_list|()
operator|.
name|getScrollPane
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|splitPane
operator|.
name|setLeftComponent
argument_list|(
name|leftComponent
argument_list|)
expr_stmt|;
name|splitPane
operator|.
name|setRightComponent
argument_list|(
name|rightComponent
argument_list|)
expr_stmt|;
name|splitPane
operator|.
name|setResizeWeight
argument_list|(
literal|0.5
argument_list|)
expr_stmt|;
name|JPanel
name|splitWithErrorsPanel
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
name|splitWithErrorsPanel
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|splitWithErrorsPanel
operator|.
name|add
argument_list|(
name|splitPane
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|xmlFileEditor
operator|.
name|getView
argument_list|()
operator|.
name|getLabel
argument_list|()
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createEmptyBorder
argument_list|(
literal|0
argument_list|,
literal|10
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|splitWithErrorsPanel
operator|.
name|add
argument_list|(
name|xmlFileEditor
operator|.
name|getView
argument_list|()
operator|.
name|getLabel
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
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
name|NORTH
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|splitWithErrorsPanel
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|buttons
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initController
parameter_list|()
block|{
name|controller
operator|.
name|addDataMapDisplayListener
argument_list|(
operator|new
name|DataMapDisplayListener
argument_list|()
block|{
specifier|public
name|void
name|currentDataMapChanged
parameter_list|(
name|DataMapDisplayEvent
name|e
parameter_list|)
block|{
name|DataMap
name|map
init|=
name|e
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|tempDataMap
operator|!=
literal|null
condition|)
block|{
name|String
name|mapName
init|=
name|tempDataMap
operator|.
name|getName
argument_list|()
decl_stmt|;
name|DataMapViewModel
name|dataMapViewModel
init|=
operator|new
name|DataMapViewModel
argument_list|(
name|mapName
argument_list|)
decl_stmt|;
name|String
name|xmlText
init|=
name|xmlFileEditor
operator|.
name|getView
argument_list|()
operator|.
name|getEditorPane
argument_list|()
operator|.
name|getText
argument_list|()
decl_stmt|;
name|dataMapViewModel
operator|.
name|setReverseEngineeringText
argument_list|(
name|xmlText
argument_list|)
expr_stmt|;
if|if
condition|(
name|reverseEngineeringViewMap
operator|.
name|containsKey
argument_list|(
name|mapName
argument_list|)
condition|)
block|{
name|DataMapViewModel
name|dataMapViewPrevious
init|=
name|reverseEngineeringViewMap
operator|.
name|get
argument_list|(
name|mapName
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataMapViewPrevious
operator|.
name|getReverseEngineeringTree
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|dataMapViewModel
operator|.
name|setReverseEngineeringTree
argument_list|(
name|dataMapViewPrevious
operator|.
name|getReverseEngineeringTree
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dataMapViewModel
operator|.
name|setReverseEngineeringTree
argument_list|(
operator|new
name|DBModel
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|dataMapViewModel
operator|.
name|setReverseEngineeringTree
argument_list|(
operator|new
name|DBModel
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|reverseEngineeringViewMap
operator|.
name|put
argument_list|(
name|mapName
argument_list|,
name|dataMapViewModel
argument_list|)
expr_stmt|;
block|}
name|tempDataMap
operator|=
name|map
expr_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
name|loadPreviousData
argument_list|()
expr_stmt|;
name|xmlFileEditor
operator|.
name|removeAlertMessage
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|this
operator|.
name|reverseEngineeringController
operator|=
operator|new
name|ReverseEngineeringController
argument_list|(
name|controller
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|loadPreviousData
parameter_list|()
block|{
name|DataMap
name|dataMap
init|=
name|controller
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|dataMap
operator|!=
literal|null
condition|)
block|{
name|String
name|reverseEngineeringText
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|reverseEngineeringViewMap
operator|.
name|containsKey
argument_list|(
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|reverseEngineeringText
operator|=
name|reverseEngineeringViewMap
operator|.
name|get
argument_list|(
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getReverseEngineeringText
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|reverseEngineeringText
operator|!=
literal|null
condition|)
block|{
name|xmlFileEditor
operator|.
name|getView
argument_list|()
operator|.
name|getEditorPane
argument_list|()
operator|.
name|setText
argument_list|(
name|reverseEngineeringText
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|dataMap
operator|.
name|getReverseEngineering
argument_list|()
operator|==
literal|null
condition|)
block|{
name|getXmlFileEditor
argument_list|()
operator|.
name|getView
argument_list|()
operator|.
name|getEditorPane
argument_list|()
operator|.
name|setText
argument_list|(
name|CONFIGURATION_TEMPLATE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ReverseEngineering
name|reverseEngineering
init|=
name|dataMap
operator|.
name|getReverseEngineering
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverseEngineering
operator|.
name|getConfigurationSource
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|xmlFileEditor
operator|.
name|getView
argument_list|()
operator|.
name|getEditorPane
argument_list|()
operator|.
name|setPage
argument_list|(
name|reverseEngineering
operator|.
name|getConfigurationSource
argument_list|()
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|reverseEngineeringViewMap
operator|.
name|containsKey
argument_list|(
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|reverseEngineeringViewMap
operator|.
name|get
argument_list|(
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getReverseEngineeringTree
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|DBModel
name|loadedPreviousTree
init|=
name|reverseEngineeringViewMap
operator|.
name|get
argument_list|(
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getReverseEngineeringTree
argument_list|()
decl_stmt|;
name|treeEditor
operator|.
name|convertTreeViewIntoTreeNode
argument_list|(
name|loadedPreviousTree
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|treeEditor
operator|.
name|setRoot
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|treeEditor
operator|.
name|setRoot
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|(
operator|(
name|CayenneModelerController
operator|)
name|controller
operator|.
name|getParent
argument_list|()
operator|)
operator|.
name|getEditorView
argument_list|()
operator|.
name|getDataMapView
argument_list|()
operator|.
name|getSelectedIndex
argument_list|()
operator|==
literal|1
condition|)
block|{
operator|(
operator|(
name|CayenneModelerController
operator|)
name|controller
operator|.
name|getParent
argument_list|()
operator|)
operator|.
name|getEditorView
argument_list|()
operator|.
name|getDataMapView
argument_list|()
operator|.
name|setSelectedIndex
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid URL"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|JComboBox
name|getDataSources
parameter_list|()
block|{
return|return
name|dataSources
return|;
block|}
specifier|public
name|JButton
name|getConfigButton
parameter_list|()
block|{
return|return
name|configButton
return|;
block|}
specifier|public
name|JButton
name|getSyncButton
parameter_list|()
block|{
return|return
name|syncButton
return|;
block|}
specifier|public
name|TreeEditor
name|getTreeEditor
parameter_list|()
block|{
return|return
name|treeEditor
return|;
block|}
specifier|public
name|XMLFileEditor
name|getXmlFileEditor
parameter_list|()
block|{
return|return
name|xmlFileEditor
return|;
block|}
specifier|public
name|JButton
name|getExecuteButton
parameter_list|()
block|{
return|return
name|executeButton
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|DataMapViewModel
argument_list|>
name|getReverseEngineeringViewMap
parameter_list|()
block|{
return|return
name|reverseEngineeringViewMap
return|;
block|}
specifier|public
name|void
name|setTempDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|tempDataMap
operator|=
name|dataMap
expr_stmt|;
block|}
block|}
end_class

end_unit

