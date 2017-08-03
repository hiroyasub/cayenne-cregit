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
name|codegen
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|swing
operator|.
name|BindingBuilder
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
name|swing
operator|.
name|ImageRendererColumn
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
name|swing
operator|.
name|ObjectBinding
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
name|swing
operator|.
name|TableBindingBuilder
import|;
end_import

begin_class
specifier|public
class|class
name|ClassesTabController
extends|extends
name|CayenneController
block|{
specifier|public
specifier|static
specifier|final
name|String
name|GENERATE_PROPERTY
init|=
literal|"generate"
decl_stmt|;
specifier|protected
name|ClassesTabPanel
name|view
decl_stmt|;
specifier|protected
name|ObjectBinding
name|tableBinding
decl_stmt|;
specifier|public
name|ClassesTabController
parameter_list|(
name|CodeGeneratorControllerBase
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|=
operator|new
name|ClassesTabPanel
argument_list|()
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|CodeGeneratorControllerBase
name|getParentController
parameter_list|()
block|{
return|return
operator|(
name|CodeGeneratorControllerBase
operator|)
name|getParent
argument_list|()
return|;
block|}
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|protected
name|void
name|initBindings
parameter_list|()
block|{
name|BindingBuilder
name|builder
init|=
operator|new
name|BindingBuilder
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getBindingFactory
argument_list|()
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getCheckAll
argument_list|()
argument_list|,
literal|"checkAllAction()"
argument_list|)
expr_stmt|;
name|TableBindingBuilder
name|tableBuilder
init|=
operator|new
name|TableBindingBuilder
argument_list|(
name|builder
argument_list|)
decl_stmt|;
name|tableBuilder
operator|.
name|addColumn
argument_list|(
literal|""
argument_list|,
literal|"parent.setCurrentClass(#item), selected"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
literal|true
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|tableBuilder
operator|.
name|addColumn
argument_list|(
literal|"Class"
argument_list|,
literal|"parent.getItemName(#item)"
argument_list|,
name|JLabel
operator|.
name|class
argument_list|,
literal|false
argument_list|,
literal|"XXXXXXXXXXXXXX"
argument_list|)
expr_stmt|;
name|tableBuilder
operator|.
name|addColumn
argument_list|(
literal|"Comments, Warnings"
argument_list|,
literal|"parent.getProblem(#item)"
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|false
argument_list|,
literal|"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
argument_list|)
expr_stmt|;
name|this
operator|.
name|tableBinding
operator|=
name|tableBuilder
operator|.
name|bindToTable
argument_list|(
name|view
operator|.
name|getTable
argument_list|()
argument_list|,
literal|"parent.classes"
argument_list|)
expr_stmt|;
name|view
operator|.
name|getTable
argument_list|()
operator|.
name|getColumnModel
argument_list|()
operator|.
name|getColumn
argument_list|(
literal|1
argument_list|)
operator|.
name|setCellRenderer
argument_list|(
operator|new
name|ImageRendererColumn
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSelected
parameter_list|()
block|{
return|return
name|getParentController
argument_list|()
operator|.
name|isSelected
argument_list|()
return|;
block|}
specifier|public
name|void
name|setSelected
parameter_list|(
name|boolean
name|selected
parameter_list|)
block|{
name|getParentController
argument_list|()
operator|.
name|setSelected
argument_list|(
name|selected
argument_list|)
expr_stmt|;
name|classSelectedAction
argument_list|()
expr_stmt|;
block|}
comment|/**      * A callback action that updates the state of Select All checkbox.      */
specifier|public
name|void
name|classSelectedAction
parameter_list|()
block|{
name|int
name|selectedCount
init|=
name|getParentController
argument_list|()
operator|.
name|getSelectedEntitiesSize
argument_list|()
operator|+
name|getParentController
argument_list|()
operator|.
name|getSelectedEmbeddablesSize
argument_list|()
decl_stmt|;
if|if
condition|(
name|selectedCount
operator|==
literal|0
condition|)
block|{
name|view
operator|.
name|getCheckAll
argument_list|()
operator|.
name|setSelected
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|selectedCount
operator|==
name|getParentController
argument_list|()
operator|.
name|getClasses
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
name|view
operator|.
name|getCheckAll
argument_list|()
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * An action that updates entity check boxes in response to the Select All state      * change.      */
specifier|public
name|void
name|checkAllAction
parameter_list|()
block|{
if|if
condition|(
name|getParentController
argument_list|()
operator|.
name|updateSelection
argument_list|(
name|view
operator|.
name|getCheckAll
argument_list|()
operator|.
name|isSelected
argument_list|()
condition|?
name|o
lambda|->
literal|true
else|:
name|o
lambda|->
literal|false
argument_list|)
condition|)
block|{
name|tableBinding
operator|.
name|updateView
argument_list|()
block|;         }
block|}
block|}
end_class

end_unit

