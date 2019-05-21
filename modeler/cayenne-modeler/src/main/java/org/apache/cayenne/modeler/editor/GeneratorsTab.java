begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|JButton
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JCheckBox
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
name|JOptionPane
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
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|GeneratorsTab
extends|extends
name|JPanel
block|{
specifier|protected
name|ProjectController
name|projectController
decl_stmt|;
specifier|private
name|GeneratorsTabController
name|additionalTabController
decl_stmt|;
specifier|private
name|TopGeneratorPanel
name|generationPanel
decl_stmt|;
specifier|public
name|GeneratorsTab
parameter_list|(
name|ProjectController
name|projectController
parameter_list|,
name|GeneratorsTabController
name|additionalTabController
parameter_list|,
name|String
name|icon
parameter_list|,
name|String
name|text
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
name|additionalTabController
operator|=
name|additionalTabController
expr_stmt|;
name|this
operator|.
name|generationPanel
operator|=
operator|new
name|TopGeneratorPanel
argument_list|(
name|icon
argument_list|)
expr_stmt|;
name|this
operator|.
name|generationPanel
operator|.
name|generateAll
operator|.
name|addActionListener
argument_list|(
name|action
lambda|->
name|additionalTabController
operator|.
name|runGenerators
argument_list|(
name|additionalTabController
operator|.
name|getSelectedDataMaps
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|generationPanel
operator|.
name|generateAll
operator|.
name|setToolTipText
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initView
parameter_list|()
block|{
name|removeAll
argument_list|()
expr_stmt|;
name|additionalTabController
operator|.
name|createPanels
argument_list|()
expr_stmt|;
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"left:pref, 4dlu"
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
name|ConcurrentMap
argument_list|<
name|DataMap
argument_list|,
name|GeneratorsPanel
argument_list|>
name|panels
init|=
name|additionalTabController
operator|.
name|getGeneratorsPanels
argument_list|()
decl_stmt|;
if|if
condition|(
name|panels
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|add
argument_list|(
operator|new
name|JLabel
argument_list|(
literal|"There are no datamaps."
argument_list|)
argument_list|,
name|BorderLayout
operator|.
name|NORTH
argument_list|)
expr_stmt|;
return|return;
block|}
name|builder
operator|.
name|append
argument_list|(
name|generationPanel
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|SortedSet
argument_list|<
name|DataMap
argument_list|>
name|keys
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|panels
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|DataMap
name|dataMap
range|:
name|keys
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|panels
operator|.
name|get
argument_list|(
name|dataMap
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|nextLine
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
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
name|void
name|showEmptyMessage
parameter_list|()
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
argument_list|,
literal|"Nothing to generate"
argument_list|)
expr_stmt|;
block|}
name|TopGeneratorPanel
name|getGenerationPanel
parameter_list|()
block|{
return|return
name|generationPanel
return|;
block|}
class|class
name|TopGeneratorPanel
extends|extends
name|JPanel
block|{
specifier|private
name|JCheckBox
name|selectAll
decl_stmt|;
name|JButton
name|generateAll
decl_stmt|;
name|TopGeneratorPanel
parameter_list|(
name|String
name|icon
parameter_list|)
block|{
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"left:pref, 4dlu, fill:70dlu, 3dlu, fill:120, 3dlu, fill:120"
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
name|this
operator|.
name|selectAll
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|generateAll
operator|=
operator|new
name|JButton
argument_list|(
literal|"Run"
argument_list|)
expr_stmt|;
name|generateAll
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|generateAll
operator|.
name|setIcon
argument_list|(
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
name|icon
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|selectAll
argument_list|,
operator|new
name|JLabel
argument_list|(
literal|"Select All"
argument_list|)
argument_list|,
name|generateAll
argument_list|)
expr_stmt|;
name|this
operator|.
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
name|JCheckBox
name|getSelectAll
parameter_list|()
block|{
return|return
name|selectAll
return|;
block|}
name|JButton
name|getGenerateAll
parameter_list|()
block|{
return|return
name|generateAll
return|;
block|}
block|}
block|}
end_class

end_unit

