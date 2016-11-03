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
name|pref
operator|.
name|DBConnectionInfoEditor
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
name|CayenneController
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DataSourceWizardView
extends|extends
name|JDialog
block|{
specifier|protected
name|JComboBox
argument_list|<
name|String
argument_list|>
name|dataSources
decl_stmt|;
specifier|protected
name|JButton
name|configButton
decl_stmt|;
specifier|protected
name|JButton
name|okButton
decl_stmt|;
specifier|protected
name|JButton
name|cancelButton
decl_stmt|;
specifier|protected
name|DBConnectionInfoEditor
name|connectionInfo
decl_stmt|;
specifier|public
name|DataSourceWizardView
parameter_list|(
name|CayenneController
name|controller
parameter_list|)
block|{
name|super
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataSources
operator|=
operator|new
name|JComboBox
argument_list|<>
argument_list|()
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
name|okButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Continue"
argument_list|)
expr_stmt|;
name|this
operator|.
name|cancelButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Cancel"
argument_list|)
expr_stmt|;
name|this
operator|.
name|connectionInfo
operator|=
operator|new
name|DBConnectionInfoEditor
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
name|PanelBuilder
name|builder
init|=
operator|new
name|PanelBuilder
argument_list|(
operator|new
name|FormLayout
argument_list|(
literal|"20dlu:grow, pref, 3dlu, fill:max(150dlu;pref), 3dlu, fill:20dlu"
argument_list|,
literal|"p"
argument_list|)
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setDefaultDialogBorder
argument_list|()
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Saved DataSources:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
argument_list|)
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
name|cancelButton
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|okButton
argument_list|)
expr_stmt|;
name|getContentPane
argument_list|()
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|getContentPane
argument_list|()
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
name|NORTH
argument_list|)
expr_stmt|;
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|connectionInfo
operator|.
name|getView
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|buttons
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
name|setTitle
argument_list|(
literal|"DB Connection Info"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JComboBox
argument_list|<
name|String
argument_list|>
name|getDataSources
parameter_list|()
block|{
return|return
name|dataSources
return|;
block|}
specifier|public
name|JButton
name|getCancelButton
parameter_list|()
block|{
return|return
name|cancelButton
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
name|getOkButton
parameter_list|()
block|{
return|return
name|okButton
return|;
block|}
specifier|public
name|DBConnectionInfoEditor
name|getConnectionInfo
parameter_list|()
block|{
return|return
name|connectionInfo
return|;
block|}
block|}
end_class

end_unit

