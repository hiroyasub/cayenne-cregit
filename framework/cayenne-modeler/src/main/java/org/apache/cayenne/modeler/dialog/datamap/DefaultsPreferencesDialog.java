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
name|dialog
operator|.
name|datamap
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
name|FlowLayout
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ButtonGroup
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
name|org
operator|.
name|scopemvc
operator|.
name|view
operator|.
name|swing
operator|.
name|SButton
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|view
operator|.
name|swing
operator|.
name|SPanel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|view
operator|.
name|swing
operator|.
name|SRadioButton
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|view
operator|.
name|swing
operator|.
name|SwingView
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DefaultsPreferencesDialog
extends|extends
name|SPanel
block|{
specifier|public
name|DefaultsPreferencesDialog
parameter_list|(
name|String
name|allControl
parameter_list|,
name|String
name|uninitializedControl
parameter_list|)
block|{
name|initView
argument_list|(
name|allControl
argument_list|,
name|uninitializedControl
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initView
parameter_list|(
name|String
name|allControl
parameter_list|,
name|String
name|uninitializedControl
parameter_list|)
block|{
name|SRadioButton
name|updateAll
init|=
operator|new
name|SRadioButton
argument_list|(
name|allControl
argument_list|,
name|DefaultsPreferencesModel
operator|.
name|ALL_ENTITIES_SELECTOR
argument_list|)
decl_stmt|;
name|SRadioButton
name|updateEmpty
init|=
operator|new
name|SRadioButton
argument_list|(
name|uninitializedControl
argument_list|,
name|DefaultsPreferencesModel
operator|.
name|UNINITIALIZED_ENTITIES_SELECTOR
argument_list|)
decl_stmt|;
name|ButtonGroup
name|buttonGroup
init|=
operator|new
name|ButtonGroup
argument_list|()
decl_stmt|;
name|buttonGroup
operator|.
name|add
argument_list|(
name|updateAll
argument_list|)
expr_stmt|;
name|buttonGroup
operator|.
name|add
argument_list|(
name|updateEmpty
argument_list|)
expr_stmt|;
name|SButton
name|updateButton
init|=
operator|new
name|SButton
argument_list|(
name|DefaultsPreferencesController
operator|.
name|UPDATE_CONTROL
argument_list|)
decl_stmt|;
name|SButton
name|cancelButton
init|=
operator|new
name|SButton
argument_list|(
name|DefaultsPreferencesController
operator|.
name|CANCEL_CONTROL
argument_list|)
decl_stmt|;
comment|// assemble
name|JPanel
name|buttonPanel
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
name|buttonPanel
operator|.
name|add
argument_list|(
name|updateButton
argument_list|)
expr_stmt|;
name|buttonPanel
operator|.
name|add
argument_list|(
name|cancelButton
argument_list|)
expr_stmt|;
name|CellConstraints
name|cc
init|=
operator|new
name|CellConstraints
argument_list|()
decl_stmt|;
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"left:max(180dlu;pref)"
argument_list|,
literal|"p, 3dlu, p, 3dlu"
argument_list|)
decl_stmt|;
name|PanelBuilder
name|builder
init|=
operator|new
name|PanelBuilder
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
name|add
argument_list|(
name|updateAll
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|updateEmpty
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
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
name|buttonPanel
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
name|setDisplayMode
argument_list|(
name|SwingView
operator|.
name|MODAL_DIALOG
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

