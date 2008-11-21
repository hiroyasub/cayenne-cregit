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
name|event
operator|.
name|ActionEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|JCheckBox
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
name|ObjEntity
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
name|CayenneWidgetFactory
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
name|CellRenderers
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
name|Comparators
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
name|query
operator|.
name|Query
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
comment|/**  * A panel that supports editing the properties a query not based on ObjEntity, but still  * supporting DataObjects retrieval.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|RawQueryPropertiesPanel
extends|extends
name|SelectPropertiesPanel
block|{
specifier|protected
name|JCheckBox
name|dataObjects
decl_stmt|;
specifier|protected
name|JComboBox
name|entities
decl_stmt|;
specifier|public
name|RawQueryPropertiesPanel
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
name|super
argument_list|(
name|mediator
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initController
parameter_list|()
block|{
name|super
operator|.
name|initController
argument_list|()
expr_stmt|;
name|dataObjects
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|event
parameter_list|)
block|{
name|setFetchingDataObjects
argument_list|(
name|dataObjects
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|entities
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|event
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
operator|(
name|ObjEntity
operator|)
name|entities
operator|.
name|getModel
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|PanelBuilder
name|createPanelBuilder
parameter_list|()
block|{
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
literal|"right:max(80dlu;pref), 3dlu, left:max(10dlu;pref), "
operator|+
literal|"3dlu, left:max(37dlu;pref), 3dlu, fill:max(147dlu;pref)"
argument_list|,
literal|"p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p"
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
name|addSeparator
argument_list|(
literal|"Select Properties"
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
literal|"Result Caching:"
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
name|cacheStrategy
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
literal|"Fetch Data Objects:"
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
name|dataObjects
argument_list|,
name|cc
operator|.
name|xy
argument_list|(
literal|3
argument_list|,
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|entities
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|5
argument_list|,
literal|7
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Fetch Offset, Rows:"
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
name|fetchOffset
operator|.
name|getComponent
argument_list|()
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|9
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Fetch Limit, Rows:"
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
name|fetchLimit
operator|.
name|getComponent
argument_list|()
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|11
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addLabel
argument_list|(
literal|"Page Size:"
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
name|pageSize
operator|.
name|getComponent
argument_list|()
argument_list|,
name|cc
operator|.
name|xywh
argument_list|(
literal|3
argument_list|,
literal|13
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
specifier|protected
name|void
name|initView
parameter_list|()
block|{
name|super
operator|.
name|initView
argument_list|()
expr_stmt|;
comment|// create widgets
name|dataObjects
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|entities
operator|=
name|CayenneWidgetFactory
operator|.
name|createComboBox
argument_list|()
expr_stmt|;
name|entities
operator|.
name|setRenderer
argument_list|(
name|CellRenderers
operator|.
name|listRendererWithIcons
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
name|createPanelBuilder
argument_list|()
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
comment|/**      * Updates the view from the current model state. Invoked when a currently displayed      * query is changed.      */
specifier|public
name|void
name|initFromModel
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
name|super
operator|.
name|initFromModel
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|boolean
name|fetchingDO
init|=
operator|!
name|query
operator|.
name|getMetaData
argument_list|(
name|mediator
operator|.
name|getCurrentDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|isFetchingDataRows
argument_list|()
decl_stmt|;
name|dataObjects
operator|.
name|setSelected
argument_list|(
name|fetchingDO
argument_list|)
expr_stmt|;
comment|// TODO: now we only allow ObjEntities from the current map,
comment|// since query root is fully resolved during map loading,
comment|// making it impossible to reference other DataMaps.
name|DataMap
name|map
init|=
name|mediator
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|List
name|objEntities
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|objEntities
operator|.
name|addAll
argument_list|(
name|map
operator|.
name|getObjEntities
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|objEntities
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|objEntities
argument_list|,
name|Comparators
operator|.
name|getDataMapChildrenComparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|entities
operator|.
name|setEnabled
argument_list|(
name|fetchingDO
operator|&&
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|DefaultComboBoxModel
name|model
init|=
operator|new
name|DefaultComboBoxModel
argument_list|(
name|objEntities
operator|.
name|toArray
argument_list|()
argument_list|)
decl_stmt|;
name|model
operator|.
name|setSelectedItem
argument_list|(
name|getEntity
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|entities
operator|.
name|setModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|void
name|setEntity
parameter_list|(
name|ObjEntity
name|selectedEntity
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|ObjEntity
name|getEntity
parameter_list|(
name|Query
name|query
parameter_list|)
function_decl|;
specifier|protected
name|void
name|setFetchingDataObjects
parameter_list|(
name|boolean
name|dataObjects
parameter_list|)
block|{
name|entities
operator|.
name|setEnabled
argument_list|(
name|dataObjects
operator|&&
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dataObjects
condition|)
block|{
name|entities
operator|.
name|getModel
argument_list|()
operator|.
name|setSelectedItem
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|setQueryProperty
argument_list|(
literal|"fetchingDataRows"
argument_list|,
name|dataObjects
condition|?
name|Boolean
operator|.
name|FALSE
else|:
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

