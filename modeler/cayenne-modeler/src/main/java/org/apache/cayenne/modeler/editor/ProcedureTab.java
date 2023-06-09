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
name|EventObject
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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|configuration
operator|.
name|event
operator|.
name|ProcedureEvent
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
name|Procedure
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
name|components
operator|.
name|JCayenneCheckBox
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
name|event
operator|.
name|ProcedureDisplayEvent
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
name|ProcedureDisplayListener
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
name|ProjectUtil
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
name|TextAdapter
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
name|project
operator|.
name|extension
operator|.
name|info
operator|.
name|ObjectInfo
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
name|util
operator|.
name|Util
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
name|validation
operator|.
name|ValidationException
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
comment|/**  * A panel for editing stored procedure general settings, such as name, schema, etc.  */
end_comment

begin_class
specifier|public
class|class
name|ProcedureTab
extends|extends
name|JPanel
implements|implements
name|ProcedureDisplayListener
implements|,
name|ExistingSelectionProcessor
block|{
specifier|protected
name|ProjectController
name|eventController
decl_stmt|;
specifier|protected
name|TextAdapter
name|name
decl_stmt|;
specifier|protected
name|TextAdapter
name|schema
decl_stmt|;
specifier|protected
name|TextAdapter
name|catalog
decl_stmt|;
specifier|protected
name|TextAdapter
name|comment
decl_stmt|;
specifier|protected
name|JCheckBox
name|returnsValue
decl_stmt|;
specifier|protected
name|boolean
name|ignoreChange
decl_stmt|;
specifier|public
name|ProcedureTab
parameter_list|(
name|ProjectController
name|eventController
parameter_list|)
block|{
name|this
operator|.
name|eventController
operator|=
name|eventController
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
comment|// create widgets
name|this
operator|.
name|name
operator|=
operator|new
name|TextAdapter
argument_list|(
operator|new
name|JTextField
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|setProcedureName
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|this
operator|.
name|schema
operator|=
operator|new
name|TextAdapter
argument_list|(
operator|new
name|JTextField
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|setSchema
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|this
operator|.
name|catalog
operator|=
operator|new
name|TextAdapter
argument_list|(
operator|new
name|JTextField
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|setCatalog
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|this
operator|.
name|comment
operator|=
operator|new
name|TextAdapter
argument_list|(
operator|new
name|JTextField
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|setComment
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|this
operator|.
name|returnsValue
operator|=
operator|new
name|JCayenneCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|returnsValue
operator|.
name|setToolTipText
argument_list|(
literal|"first parameter will be used as return value"
argument_list|)
expr_stmt|;
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"right:pref, 3dlu, fill:200dlu"
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
name|appendSeparator
argument_list|(
literal|"Stored Procedure Configuration"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Procedure Name:"
argument_list|,
name|name
operator|.
name|getComponent
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Catalog:"
argument_list|,
name|catalog
operator|.
name|getComponent
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Schema:"
argument_list|,
name|schema
operator|.
name|getComponent
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Returns Value:"
argument_list|,
name|returnsValue
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Comment:"
argument_list|,
name|comment
operator|.
name|getComponent
argument_list|()
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
specifier|private
name|void
name|initController
parameter_list|()
block|{
name|returnsValue
operator|.
name|addItemListener
argument_list|(
name|e
lambda|->
block|{
name|Procedure
name|procedure
init|=
name|eventController
operator|.
name|getCurrentProcedure
argument_list|()
decl_stmt|;
if|if
condition|(
name|procedure
operator|!=
literal|null
operator|&&
operator|!
name|ignoreChange
condition|)
block|{
name|procedure
operator|.
name|setReturningValue
argument_list|(
name|returnsValue
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|fireProcedureEvent
argument_list|(
operator|new
name|ProcedureEvent
argument_list|(
name|ProcedureTab
operator|.
name|this
argument_list|,
name|procedure
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|addProcedureDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|processExistingSelection
parameter_list|(
name|EventObject
name|e
parameter_list|)
block|{
name|ProcedureDisplayEvent
name|pde
init|=
operator|new
name|ProcedureDisplayEvent
argument_list|(
name|this
argument_list|,
name|eventController
operator|.
name|getCurrentProcedure
argument_list|()
argument_list|,
name|eventController
operator|.
name|getCurrentDataMap
argument_list|()
argument_list|,
operator|(
name|DataChannelDescriptor
operator|)
name|eventController
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
argument_list|)
decl_stmt|;
name|eventController
operator|.
name|fireProcedureDisplayEvent
argument_list|(
name|pde
argument_list|)
expr_stmt|;
block|}
comment|/**      * Invoked when currently selected Procedure object is changed.      */
specifier|public
name|void
name|currentProcedureChanged
parameter_list|(
name|ProcedureDisplayEvent
name|e
parameter_list|)
block|{
name|Procedure
name|procedure
init|=
name|e
operator|.
name|getProcedure
argument_list|()
decl_stmt|;
if|if
condition|(
name|procedure
operator|==
literal|null
operator|||
operator|!
name|e
operator|.
name|isProcedureChanged
argument_list|()
condition|)
block|{
return|return;
block|}
name|name
operator|.
name|setText
argument_list|(
name|procedure
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|schema
operator|.
name|setText
argument_list|(
name|procedure
operator|.
name|getSchema
argument_list|()
argument_list|)
expr_stmt|;
name|catalog
operator|.
name|setText
argument_list|(
name|procedure
operator|.
name|getCatalog
argument_list|()
argument_list|)
expr_stmt|;
name|comment
operator|.
name|setText
argument_list|(
name|getComment
argument_list|(
name|procedure
argument_list|)
argument_list|)
expr_stmt|;
name|ignoreChange
operator|=
literal|true
expr_stmt|;
name|returnsValue
operator|.
name|setSelected
argument_list|(
name|procedure
operator|.
name|isReturningValue
argument_list|()
argument_list|)
expr_stmt|;
name|ignoreChange
operator|=
literal|false
expr_stmt|;
block|}
name|void
name|setProcedureName
parameter_list|(
name|String
name|newName
parameter_list|)
block|{
if|if
condition|(
name|newName
operator|!=
literal|null
operator|&&
name|newName
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|newName
operator|=
literal|null
expr_stmt|;
block|}
name|Procedure
name|procedure
init|=
name|eventController
operator|.
name|getCurrentProcedure
argument_list|()
decl_stmt|;
if|if
condition|(
name|procedure
operator|==
literal|null
operator|||
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|newName
argument_list|,
name|procedure
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|newName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Procedure name is required."
argument_list|)
throw|;
block|}
if|else if
condition|(
name|procedure
operator|.
name|getDataMap
argument_list|()
operator|.
name|getProcedure
argument_list|(
name|newName
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// completely new name, set new name for entity
name|ProcedureEvent
name|e
init|=
operator|new
name|ProcedureEvent
argument_list|(
name|this
argument_list|,
name|procedure
argument_list|,
name|procedure
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|ProjectUtil
operator|.
name|setProcedureName
argument_list|(
name|procedure
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|procedure
argument_list|,
name|newName
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|fireProcedureEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// there is an entity with the same name
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"There is another procedure with name '"
operator|+
name|newName
operator|+
literal|"'."
argument_list|)
throw|;
block|}
block|}
name|void
name|setSchema
parameter_list|(
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|!=
literal|null
operator|&&
name|text
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|text
operator|=
literal|null
expr_stmt|;
block|}
name|Procedure
name|procedure
init|=
name|eventController
operator|.
name|getCurrentProcedure
argument_list|()
decl_stmt|;
if|if
condition|(
name|procedure
operator|!=
literal|null
operator|&&
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|procedure
operator|.
name|getSchema
argument_list|()
argument_list|,
name|text
argument_list|)
condition|)
block|{
name|procedure
operator|.
name|setSchema
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|fireProcedureEvent
argument_list|(
operator|new
name|ProcedureEvent
argument_list|(
name|this
argument_list|,
name|procedure
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|setCatalog
parameter_list|(
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|!=
literal|null
operator|&&
name|text
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|text
operator|=
literal|null
expr_stmt|;
block|}
name|Procedure
name|procedure
init|=
name|eventController
operator|.
name|getCurrentProcedure
argument_list|()
decl_stmt|;
if|if
condition|(
name|procedure
operator|!=
literal|null
operator|&&
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|procedure
operator|.
name|getCatalog
argument_list|()
argument_list|,
name|text
argument_list|)
condition|)
block|{
name|procedure
operator|.
name|setCatalog
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|fireProcedureEvent
argument_list|(
operator|new
name|ProcedureEvent
argument_list|(
name|this
argument_list|,
name|procedure
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|setComment
parameter_list|(
name|String
name|comment
parameter_list|)
block|{
name|Procedure
name|procedure
init|=
name|eventController
operator|.
name|getCurrentProcedure
argument_list|()
decl_stmt|;
if|if
condition|(
name|procedure
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|ObjectInfo
operator|.
name|putToMetaData
argument_list|(
name|eventController
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
argument_list|,
name|procedure
argument_list|,
name|ObjectInfo
operator|.
name|COMMENT
argument_list|,
name|comment
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|fireProcedureEvent
argument_list|(
operator|new
name|ProcedureEvent
argument_list|(
name|this
argument_list|,
name|procedure
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|getComment
parameter_list|(
name|Procedure
name|procedure
parameter_list|)
block|{
return|return
name|ObjectInfo
operator|.
name|getFromMetaData
argument_list|(
name|eventController
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
argument_list|,
name|procedure
argument_list|,
name|ObjectInfo
operator|.
name|COMMENT
argument_list|)
return|;
block|}
block|}
end_class

end_unit

