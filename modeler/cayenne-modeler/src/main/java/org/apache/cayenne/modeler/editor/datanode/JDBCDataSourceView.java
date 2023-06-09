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
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPasswordField
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
name|JTextFieldValidator
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
name|JTextFieldUndoable
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

begin_class
specifier|public
class|class
name|JDBCDataSourceView
extends|extends
name|JPanel
block|{
specifier|protected
name|JTextField
name|driver
decl_stmt|;
specifier|protected
name|JTextField
name|url
decl_stmt|;
specifier|protected
name|JTextField
name|userName
decl_stmt|;
specifier|protected
name|JPasswordField
name|password
decl_stmt|;
specifier|protected
name|JTextField
name|minConnections
decl_stmt|;
specifier|protected
name|JTextField
name|maxConnections
decl_stmt|;
specifier|protected
name|JButton
name|syncWithLocal
decl_stmt|;
specifier|public
name|JDBCDataSourceView
parameter_list|()
block|{
name|driver
operator|=
operator|new
name|JTextFieldUndoable
argument_list|()
expr_stmt|;
name|url
operator|=
operator|new
name|JTextFieldUndoable
argument_list|()
expr_stmt|;
name|userName
operator|=
operator|new
name|JTextFieldUndoable
argument_list|()
expr_stmt|;
name|password
operator|=
operator|new
name|JPasswordField
argument_list|()
expr_stmt|;
name|minConnections
operator|=
operator|new
name|JTextFieldUndoable
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|maxConnections
operator|=
operator|new
name|JTextFieldUndoable
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|syncWithLocal
operator|=
operator|new
name|JButton
argument_list|(
literal|"Sync with Local"
argument_list|)
expr_stmt|;
name|syncWithLocal
operator|.
name|setToolTipText
argument_list|(
literal|"Update from local DataSource"
argument_list|)
expr_stmt|;
name|JTextFieldValidator
operator|.
name|addValidation
argument_list|(
name|driver
argument_list|,
name|text
lambda|->
name|text
operator|.
name|length
argument_list|()
operator|!=
name|text
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
argument_list|,
literal|"There are some whitespaces in this field"
argument_list|)
expr_stmt|;
name|JTextFieldValidator
operator|.
name|addValidation
argument_list|(
name|url
argument_list|,
name|text
lambda|->
name|text
operator|.
name|length
argument_list|()
operator|!=
name|text
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
argument_list|,
literal|"There are some whitespaces in this field"
argument_list|)
expr_stmt|;
comment|// assemble
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
literal|"right:80dlu, 3dlu, fill:50dlu, 3dlu, fill:74dlu, 3dlu, fill:70dlu"
argument_list|,
comment|// Columns
literal|"p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p"
argument_list|)
decl_stmt|;
comment|// Rows
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
name|addSeparator
argument_list|(
literal|"JDBC Configuration"
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|7
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"JDBC Driver:"
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
name|builder
operator|.
name|add
argument_list|(
name|driver
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|3
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"DB URL:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|url
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Username:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|userName
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|7
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Password:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|9
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|password
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|9
argument_list|,
literal|5
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Min Connections:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|11
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|minConnections
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|11
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Max Connections:"
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|1
argument_list|,
literal|13
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|maxConnections
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|13
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|syncWithLocal
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|7
argument_list|,
literal|15
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
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
specifier|public
name|JTextField
name|getDriver
parameter_list|()
block|{
return|return
name|driver
return|;
block|}
specifier|public
name|JPasswordField
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
specifier|public
name|JTextField
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|JTextField
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
specifier|public
name|JTextField
name|getMaxConnections
parameter_list|()
block|{
return|return
name|maxConnections
return|;
block|}
specifier|public
name|JTextField
name|getMinConnections
parameter_list|()
block|{
return|return
name|minConnections
return|;
block|}
specifier|public
name|JButton
name|getSyncWithLocal
parameter_list|()
block|{
return|return
name|syncWithLocal
return|;
block|}
block|}
end_class

end_unit

