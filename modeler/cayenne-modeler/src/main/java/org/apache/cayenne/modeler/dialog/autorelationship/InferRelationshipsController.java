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
name|autorelationship
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
name|DefaultComboBoxModel
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
name|map
operator|.
name|DbJoin
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
name|DbRelationship
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
name|Entity
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
name|event
operator|.
name|MapEvent
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
name|event
operator|.
name|RelationshipEvent
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
name|dbsync
operator|.
name|naming
operator|.
name|ObjectNameGenerator
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
name|ClassLoadingService
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
name|ErrorDebugDialog
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
name|undo
operator|.
name|CreateRelationshipUndoableEdit
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
name|undo
operator|.
name|InferRelationshipsUndoableEdit
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
name|modeler
operator|.
name|util
operator|.
name|NameGeneratorPreferences
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
specifier|public
class|class
name|InferRelationshipsController
extends|extends
name|InferRelationshipsControllerBase
block|{
specifier|public
specifier|static
specifier|final
name|int
name|SELECT
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|CANCEL
init|=
literal|0
decl_stmt|;
specifier|private
specifier|static
name|Logger
name|logObj
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ErrorDebugDialog
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|InferRelationshipsDialog
name|view
decl_stmt|;
specifier|protected
name|InferRelationshipsTabController
name|entitySelector
decl_stmt|;
specifier|protected
name|ObjectNameGenerator
name|strategy
decl_stmt|;
specifier|public
name|InferRelationshipsController
parameter_list|(
name|CayenneController
name|parent
parameter_list|,
name|DataMap
name|dataMap
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
name|strategy
operator|=
name|createNamingStrategy
argument_list|(
name|NameGeneratorPreferences
operator|.
name|getInstance
argument_list|()
operator|.
name|getLastUsedStrategies
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|setNamingStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|setRelationships
argument_list|()
expr_stmt|;
name|this
operator|.
name|entitySelector
operator|=
operator|new
name|InferRelationshipsTabController
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ObjectNameGenerator
name|createNamingStrategy
parameter_list|(
name|String
name|strategyClass
parameter_list|)
block|{
try|try
block|{
name|ClassLoadingService
name|classLoader
init|=
name|application
operator|.
name|getClassLoadingService
argument_list|()
decl_stmt|;
return|return
name|classLoader
operator|.
name|loadClass
argument_list|(
name|ObjectNameGenerator
operator|.
name|class
argument_list|,
name|strategyClass
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|logObj
operator|.
name|error
argument_list|(
literal|"Error in "
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|th
argument_list|)
expr_stmt|;
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|view
argument_list|,
literal|"Naming Strategy Initialization Error: "
operator|+
name|th
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"Naming Strategy Initialization Error"
argument_list|,
name|JOptionPane
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|public
name|void
name|startup
parameter_list|()
block|{
comment|// show dialog even on empty DataMap, as custom generation may still take
comment|// advantage of it
name|view
operator|=
operator|new
name|InferRelationshipsDialog
argument_list|(
name|entitySelector
operator|.
name|getView
argument_list|()
argument_list|)
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
name|view
operator|.
name|pack
argument_list|()
expr_stmt|;
name|view
operator|.
name|setModal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|centerView
argument_list|()
expr_stmt|;
name|makeCloseableOnEscape
argument_list|()
expr_stmt|;
name|view
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|getCancelButton
argument_list|()
argument_list|,
literal|"cancelAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getGenerateButton
argument_list|()
argument_list|,
literal|"generateAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|this
argument_list|,
literal|"entitySelectedAction()"
argument_list|,
name|SELECTED_PROPERTY
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getStrategyCombo
argument_list|()
argument_list|,
literal|"strategyComboAction()"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|entitySelectedAction
parameter_list|()
block|{
name|int
name|size
init|=
name|getSelectedEntitiesSize
argument_list|()
decl_stmt|;
name|String
name|label
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
name|label
operator|=
literal|"No DbRelationships selected"
expr_stmt|;
block|}
if|else if
condition|(
name|size
operator|==
literal|1
condition|)
block|{
name|label
operator|=
literal|"One DbRelationships selected"
expr_stmt|;
block|}
else|else
block|{
name|label
operator|=
name|size
operator|+
literal|" DbRelationships selected"
expr_stmt|;
block|}
name|view
operator|.
name|getEntityCount
argument_list|()
operator|.
name|setText
argument_list|(
name|label
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|strategyComboAction
parameter_list|()
block|{
try|try
block|{
name|String
name|strategyClass
init|=
operator|(
name|String
operator|)
name|view
operator|.
name|getStrategyCombo
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
name|this
operator|.
name|strategy
operator|=
name|createNamingStrategy
argument_list|(
name|strategyClass
argument_list|)
expr_stmt|;
comment|/**              * Be user-friendly and update preferences with specified strategy              */
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|NameGeneratorPreferences
operator|.
name|getInstance
argument_list|()
operator|.
name|addToLastUsedStrategies
argument_list|(
name|strategyClass
argument_list|)
expr_stmt|;
name|view
operator|.
name|getStrategyCombo
argument_list|()
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|NameGeneratorPreferences
operator|.
name|getInstance
argument_list|()
operator|.
name|getLastUsedStrategies
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|logObj
operator|.
name|error
argument_list|(
literal|"Error in "
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|th
argument_list|)
expr_stmt|;
return|return;
block|}
name|setNamingStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|createNames
argument_list|()
expr_stmt|;
name|entitySelector
operator|.
name|initBindings
argument_list|()
expr_stmt|;
name|view
operator|.
name|setChoice
argument_list|(
name|SELECT
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ObjectNameGenerator
name|getNamingStrategy
parameter_list|()
block|{
return|return
name|strategy
return|;
block|}
specifier|public
name|void
name|cancelAction
parameter_list|()
block|{
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|generateAction
parameter_list|()
block|{
name|ProjectController
name|mediator
init|=
name|application
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
decl_stmt|;
name|InferRelationshipsUndoableEdit
name|undoableEdit
init|=
operator|new
name|InferRelationshipsUndoableEdit
argument_list|()
decl_stmt|;
for|for
control|(
name|InferredRelationship
name|temp
range|:
name|selectedEntities
control|)
block|{
name|DbRelationship
name|rel
init|=
operator|new
name|DbRelationship
argument_list|(
name|uniqueRelName
argument_list|(
name|temp
operator|.
name|getSource
argument_list|()
argument_list|,
name|temp
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|RelationshipEvent
name|e
init|=
operator|new
name|RelationshipEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|rel
argument_list|,
name|temp
operator|.
name|getSource
argument_list|()
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
decl_stmt|;
name|mediator
operator|.
name|fireDbRelationshipEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setSourceEntity
argument_list|(
name|temp
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setTargetEntityName
argument_list|(
name|temp
operator|.
name|getTarget
argument_list|()
argument_list|)
expr_stmt|;
name|DbJoin
name|join
init|=
operator|new
name|DbJoin
argument_list|(
name|rel
argument_list|,
name|temp
operator|.
name|getJoinSource
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|temp
operator|.
name|getJoinTarget
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|rel
operator|.
name|addJoin
argument_list|(
name|join
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setToMany
argument_list|(
name|temp
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|temp
operator|.
name|getSource
argument_list|()
operator|.
name|addRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
name|undoableEdit
operator|.
name|addEdit
argument_list|(
operator|new
name|CreateRelationshipUndoableEdit
argument_list|(
name|temp
operator|.
name|getSource
argument_list|()
argument_list|,
operator|new
name|DbRelationship
index|[]
block|{
name|rel
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
name|getSelectedEntitiesSize
argument_list|()
operator|+
literal|" relationships generated"
argument_list|)
expr_stmt|;
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|uniqueRelName
parameter_list|(
name|Entity
name|entity
parameter_list|,
name|String
name|preferredName
parameter_list|)
block|{
name|int
name|currentSuffix
init|=
literal|1
decl_stmt|;
name|String
name|relName
init|=
name|preferredName
decl_stmt|;
while|while
condition|(
name|entity
operator|.
name|getRelationship
argument_list|(
name|relName
argument_list|)
operator|!=
literal|null
operator|||
name|entity
operator|.
name|getAttribute
argument_list|(
name|relName
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|relName
operator|=
name|preferredName
operator|+
name|currentSuffix
expr_stmt|;
name|currentSuffix
operator|++
expr_stmt|;
block|}
return|return
name|relName
return|;
block|}
block|}
end_class

end_unit

