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
name|java
operator|.
name|awt
operator|.
name|Component
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|DbAttribute
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
name|DbEntity
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
name|validator
operator|.
name|ValidationDisplayHandler
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
name|project2
operator|.
name|Project
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
name|project2
operator|.
name|validation
operator|.
name|ValidationInfo
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
name|project2
operator|.
name|validation
operator|.
name|ValidationResults
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
name|project2
operator|.
name|validation
operator|.
name|ProjectValidator
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|TableSelectorController
extends|extends
name|CayenneController
block|{
specifier|protected
name|TableSelectorView
name|view
decl_stmt|;
specifier|protected
name|ObjectBinding
name|tableBinding
decl_stmt|;
specifier|protected
name|DbEntity
name|table
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|DbEntity
argument_list|>
name|tables
decl_stmt|;
specifier|protected
name|int
name|permanentlyExcludedCount
decl_stmt|;
specifier|protected
name|Map
name|excludedTables
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|DbEntity
argument_list|>
name|selectableTablesList
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|validationMessages
decl_stmt|;
specifier|public
name|TableSelectorController
parameter_list|(
name|ProjectController
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
name|TableSelectorView
argument_list|()
expr_stmt|;
name|this
operator|.
name|excludedTables
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|selectableTablesList
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|this
operator|.
name|validationMessages
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
block|}
comment|// ----- properties -----
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
comment|/**      * Called by table binding script to set current table.      */
specifier|public
name|void
name|setTable
parameter_list|(
name|DbEntity
name|table
parameter_list|)
block|{
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
block|}
comment|/**      * Returns DbEntities that are excluded from DB generation.      */
specifier|public
name|Collection
name|getExcludedTables
parameter_list|()
block|{
return|return
name|excludedTables
operator|.
name|values
argument_list|()
return|;
block|}
specifier|public
name|List
name|getTables
parameter_list|()
block|{
return|return
name|tables
return|;
block|}
specifier|public
name|boolean
name|isIncluded
parameter_list|()
block|{
if|if
condition|(
name|table
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
operator|!
name|excludedTables
operator|.
name|containsKey
argument_list|(
name|table
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|setIncluded
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
if|if
condition|(
name|table
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|b
condition|)
block|{
name|excludedTables
operator|.
name|remove
argument_list|(
name|table
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|excludedTables
operator|.
name|put
argument_list|(
name|table
operator|.
name|getName
argument_list|()
argument_list|,
name|table
argument_list|)
expr_stmt|;
block|}
name|tableSelectedAction
argument_list|()
expr_stmt|;
block|}
comment|/**      * A callback action that updates the state of Select All checkbox.      */
specifier|public
name|void
name|tableSelectedAction
parameter_list|()
block|{
name|int
name|unselectedCount
init|=
name|excludedTables
operator|.
name|size
argument_list|()
operator|-
name|permanentlyExcludedCount
decl_stmt|;
if|if
condition|(
name|unselectedCount
operator|==
name|selectableTablesList
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
literal|false
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|unselectedCount
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
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|getProblem
parameter_list|()
block|{
return|return
operator|(
name|table
operator|!=
literal|null
operator|)
condition|?
name|validationMessages
operator|.
name|get
argument_list|(
name|table
operator|.
name|getName
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
comment|// ------ other stuff ------
specifier|protected
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
literal|"setTable(#item), included"
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
literal|"Table"
argument_list|,
literal|"#item.name"
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|false
argument_list|,
literal|"XXXXXXXXXXXXXXXX"
argument_list|)
expr_stmt|;
name|tableBuilder
operator|.
name|addColumn
argument_list|(
literal|"Problems"
argument_list|,
literal|"setTable(#item), problem"
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|false
argument_list|,
literal|"XXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
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
name|getTables
argument_list|()
argument_list|,
literal|"tables"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Performs validation of DbEntities in the current DataMap. Returns a collection of      * ValidationInfo objects describing the problems.      */
specifier|public
name|void
name|updateTables
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|this
operator|.
name|tables
operator|=
operator|new
name|ArrayList
argument_list|(
name|dataMap
operator|.
name|getDbEntities
argument_list|()
argument_list|)
expr_stmt|;
name|excludedTables
operator|.
name|clear
argument_list|()
expr_stmt|;
name|validationMessages
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// if there were errors, filter out those related to
comment|// non-derived DbEntities...
comment|// TODO: this is inefficient.. we need targeted validation
comment|// instead of doing it on the whole project
name|Project
name|project
init|=
name|getApplication
argument_list|()
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|ProjectValidator
name|projectValidator
init|=
name|getApplication
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|ProjectValidator
operator|.
name|class
argument_list|)
decl_stmt|;
name|ValidationResults
name|validationResults
init|=
name|projectValidator
operator|.
name|validate
argument_list|(
name|project
operator|.
name|getRootNode
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|validationCode
init|=
name|validationResults
operator|.
name|getMaxSeverity
argument_list|()
decl_stmt|;
if|if
condition|(
name|validationCode
operator|>=
name|ValidationDisplayHandler
operator|.
name|WARNING
condition|)
block|{
for|for
control|(
name|ValidationInfo
name|nextProblem
range|:
name|validationResults
operator|.
name|getValidationResults
argument_list|()
control|)
block|{
name|Entity
name|failedEntity
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|nextProblem
operator|.
name|getObject
argument_list|()
operator|instanceof
name|DbAttribute
condition|)
block|{
name|DbAttribute
name|failedAttribute
init|=
operator|(
name|DbAttribute
operator|)
name|nextProblem
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|failedEntity
operator|=
name|failedAttribute
operator|.
name|getEntity
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|nextProblem
operator|.
name|getObject
argument_list|()
operator|instanceof
name|DbRelationship
condition|)
block|{
name|DbRelationship
name|failedRelationship
init|=
operator|(
name|DbRelationship
operator|)
name|nextProblem
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|failedEntity
operator|=
name|failedRelationship
operator|.
name|getSourceEntity
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|nextProblem
operator|.
name|getObject
argument_list|()
operator|instanceof
name|DbEntity
condition|)
block|{
name|failedEntity
operator|=
operator|(
name|Entity
operator|)
name|nextProblem
operator|.
name|getObject
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|failedEntity
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|excludedTables
operator|.
name|put
argument_list|(
name|failedEntity
operator|.
name|getName
argument_list|()
argument_list|,
name|failedEntity
argument_list|)
expr_stmt|;
name|validationMessages
operator|.
name|put
argument_list|(
name|failedEntity
operator|.
name|getName
argument_list|()
argument_list|,
name|nextProblem
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Find selectable tables
name|permanentlyExcludedCount
operator|=
name|excludedTables
operator|.
name|size
argument_list|()
expr_stmt|;
name|selectableTablesList
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|DbEntity
name|table
range|:
name|tables
control|)
block|{
if|if
condition|(
literal|false
operator|==
name|excludedTables
operator|.
name|containsKey
argument_list|(
name|table
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|selectableTablesList
operator|.
name|add
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
block|}
name|tableBinding
operator|.
name|updateView
argument_list|()
expr_stmt|;
name|tableSelectedAction
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|checkAllAction
parameter_list|()
block|{
name|boolean
name|isCheckAllSelected
init|=
name|view
operator|.
name|getCheckAll
argument_list|()
operator|.
name|isSelected
argument_list|()
decl_stmt|;
if|if
condition|(
name|isCheckAllSelected
condition|)
block|{
name|selectableTablesList
operator|.
name|clear
argument_list|()
expr_stmt|;
name|selectableTablesList
operator|.
name|addAll
argument_list|(
name|tables
argument_list|)
expr_stmt|;
name|excludedTables
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|excludedTables
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|DbEntity
name|table
range|:
name|tables
control|)
block|{
name|excludedTables
operator|.
name|put
argument_list|(
name|table
operator|.
name|getName
argument_list|()
argument_list|,
name|table
argument_list|)
expr_stmt|;
block|}
name|selectableTablesList
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|tableBinding
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

