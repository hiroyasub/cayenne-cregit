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
name|text
operator|.
name|JTextComponent
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
name|query
operator|.
name|EJBQLQuery
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|QueryCacheStrategy
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
name|EjbqlQueryPropertiesPanel
extends|extends
name|SelectPropertiesPanel
block|{
specifier|public
name|EjbqlQueryPropertiesPanel
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
literal|"Fetch Offset, Rows:"
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
literal|"Fetch Limit, Rows:"
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
literal|"Page Size:"
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
literal|11
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
name|EJBQLQuery
name|ejbqlQuery
init|=
operator|(
name|EJBQLQuery
operator|)
name|query
decl_stmt|;
name|DefaultComboBoxModel
name|cacheModel
init|=
operator|new
name|DefaultComboBoxModel
argument_list|(
name|CACHE_POLICIES
argument_list|)
decl_stmt|;
name|QueryCacheStrategy
name|selectedStrategy
init|=
name|ejbqlQuery
operator|.
name|getCacheStrategy
argument_list|()
decl_stmt|;
name|cacheModel
operator|.
name|setSelectedItem
argument_list|(
name|selectedStrategy
operator|!=
literal|null
condition|?
name|selectedStrategy
else|:
name|QueryCacheStrategy
operator|.
name|getDefaultStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|cacheStrategy
operator|.
name|setModel
argument_list|(
name|cacheModel
argument_list|)
expr_stmt|;
name|fetchOffset
operator|.
name|setText
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|ejbqlQuery
operator|.
name|getFetchOffset
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|fetchLimit
operator|.
name|setText
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|ejbqlQuery
operator|.
name|getFetchLimit
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|//       pageSize.setText(String.valueOf(ejbqlQuery.getPageSize()));
name|JTextComponent
name|ps
init|=
name|pageSize
operator|.
name|getComponent
argument_list|()
decl_stmt|;
name|ps
operator|.
name|setEditable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

