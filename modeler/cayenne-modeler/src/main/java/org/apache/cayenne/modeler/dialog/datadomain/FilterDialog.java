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
name|datadomain
package|;
end_package

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
name|StayOpenJCheckBoxMenuItem
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
name|javax
operator|.
name|swing
operator|.
name|JPopupMenu
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
name|Set
import|;
end_import

begin_class
specifier|public
class|class
name|FilterDialog
extends|extends
name|JPopupMenu
block|{
specifier|private
name|String
name|SHOW_ALL
init|=
literal|"Show all"
decl_stmt|;
specifier|private
name|StayOpenJCheckBoxMenuItem
name|dbEntity
decl_stmt|;
specifier|private
name|StayOpenJCheckBoxMenuItem
name|objEntity
decl_stmt|;
specifier|private
name|StayOpenJCheckBoxMenuItem
name|embeddable
decl_stmt|;
specifier|private
name|StayOpenJCheckBoxMenuItem
name|procedure
decl_stmt|;
specifier|private
name|StayOpenJCheckBoxMenuItem
name|query
decl_stmt|;
specifier|private
name|StayOpenJCheckBoxMenuItem
name|all
decl_stmt|;
specifier|private
name|ProjectController
name|eventController
decl_stmt|;
specifier|private
name|FilterController
name|filterController
decl_stmt|;
specifier|public
name|Boolean
name|getDbEntityFilter
parameter_list|()
block|{
return|return
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"dbEntity"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDbEntityFilter
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|put
argument_list|(
literal|"dbEntity"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getObjEntityFilter
parameter_list|()
block|{
return|return
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"objEntity"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setObjEntityFilter
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|put
argument_list|(
literal|"objEntity"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getEmbeddableFilter
parameter_list|()
block|{
return|return
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"embeddable"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setEmbeddableFilter
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|put
argument_list|(
literal|"embeddable"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getProcedureFilter
parameter_list|()
block|{
return|return
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"procedure"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setProcedureFilter
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|put
argument_list|(
literal|"procedure"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getQueryFilter
parameter_list|()
block|{
return|return
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"query"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setQueryFilter
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|put
argument_list|(
literal|"query"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getAllFilter
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
if|if
condition|(
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|!=
literal|true
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|setAllFilter
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
block|}
specifier|public
name|FilterDialog
parameter_list|(
name|FilterController
name|filterController
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|filterController
operator|=
name|filterController
expr_stmt|;
name|this
operator|.
name|eventController
operator|=
name|filterController
operator|.
name|getEventController
argument_list|()
expr_stmt|;
name|initView
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|initView
parameter_list|()
block|{
name|all
operator|=
operator|new
name|StayOpenJCheckBoxMenuItem
argument_list|(
name|SHOW_ALL
argument_list|)
expr_stmt|;
name|dbEntity
operator|=
operator|new
name|StayOpenJCheckBoxMenuItem
argument_list|(
literal|"DbEntity"
argument_list|)
expr_stmt|;
name|objEntity
operator|=
operator|new
name|StayOpenJCheckBoxMenuItem
argument_list|(
literal|"ObjEntity"
argument_list|)
expr_stmt|;
name|embeddable
operator|=
operator|new
name|StayOpenJCheckBoxMenuItem
argument_list|(
literal|"Embeddable"
argument_list|)
expr_stmt|;
name|procedure
operator|=
operator|new
name|StayOpenJCheckBoxMenuItem
argument_list|(
literal|"Procedure"
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|StayOpenJCheckBoxMenuItem
argument_list|(
literal|"Query"
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|all
argument_list|)
expr_stmt|;
name|addSeparator
argument_list|()
expr_stmt|;
name|add
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|embeddable
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|procedure
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initController
parameter_list|()
block|{
name|BindingBuilder
name|builder
init|=
operator|new
name|BindingBuilder
argument_list|(
name|eventController
operator|.
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
name|bindToStateChange
argument_list|(
name|dbEntity
argument_list|,
literal|"dbEntityFilter"
argument_list|)
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|builder
operator|.
name|bindToStateChange
argument_list|(
name|objEntity
argument_list|,
literal|"objEntityFilter"
argument_list|)
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|builder
operator|.
name|bindToStateChange
argument_list|(
name|embeddable
argument_list|,
literal|"embeddableFilter"
argument_list|)
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|builder
operator|.
name|bindToStateChange
argument_list|(
name|procedure
argument_list|,
literal|"procedureFilter"
argument_list|)
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|builder
operator|.
name|bindToStateChange
argument_list|(
name|query
argument_list|,
literal|"queryFilter"
argument_list|)
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|builder
operator|.
name|bindToStateChange
argument_list|(
name|all
argument_list|,
literal|"allFilter"
argument_list|)
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|dbEntity
operator|.
name|addActionListener
argument_list|(
operator|new
name|CheckListener
argument_list|(
literal|"dbEntity"
argument_list|)
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|addActionListener
argument_list|(
operator|new
name|CheckListener
argument_list|(
literal|"objEntity"
argument_list|)
argument_list|)
expr_stmt|;
name|embeddable
operator|.
name|addActionListener
argument_list|(
operator|new
name|CheckListener
argument_list|(
literal|"embeddable"
argument_list|)
argument_list|)
expr_stmt|;
name|procedure
operator|.
name|addActionListener
argument_list|(
operator|new
name|CheckListener
argument_list|(
literal|"procedure"
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|.
name|addActionListener
argument_list|(
operator|new
name|CheckListener
argument_list|(
literal|"query"
argument_list|)
argument_list|)
expr_stmt|;
name|all
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|all
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|dbEntity
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|embeddable
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|procedure
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|query
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|all
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|filterController
operator|.
name|getTreeModel
argument_list|()
operator|.
name|setFiltered
argument_list|(
name|filterController
operator|.
name|getFilterMap
argument_list|()
argument_list|)
expr_stmt|;
name|filterController
operator|.
name|getTree
argument_list|()
operator|.
name|updateUI
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|void
name|checkAllStates
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isAll
argument_list|()
condition|)
block|{
name|all
operator|.
name|setSelected
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|all
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|all
operator|.
name|setSelected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|all
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|isAll
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
if|if
condition|(
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|==
literal|false
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
class|class
name|CheckListener
implements|implements
name|ActionListener
block|{
name|String
name|key
decl_stmt|;
specifier|public
name|CheckListener
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|filterController
operator|.
name|getFilterMap
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
operator|(
operator|(
name|StayOpenJCheckBoxMenuItem
operator|)
name|e
operator|.
name|getSource
argument_list|()
operator|)
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
name|filterController
operator|.
name|getTreeModel
argument_list|()
operator|.
name|setFiltered
argument_list|(
name|filterController
operator|.
name|getFilterMap
argument_list|()
argument_list|)
expr_stmt|;
name|filterController
operator|.
name|getTree
argument_list|()
operator|.
name|updateUI
argument_list|()
expr_stmt|;
name|checkAllStates
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

