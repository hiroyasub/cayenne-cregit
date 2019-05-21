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
name|dialog
operator|.
name|db
operator|.
name|load
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
name|action
operator|.
name|dbimport
operator|.
name|AddCatalogAction
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
name|action
operator|.
name|dbimport
operator|.
name|AddExcludeColumnAction
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
name|action
operator|.
name|dbimport
operator|.
name|AddExcludeProcedureAction
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
name|action
operator|.
name|dbimport
operator|.
name|AddExcludeTableAction
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
name|action
operator|.
name|dbimport
operator|.
name|AddIncludeColumnAction
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
name|action
operator|.
name|dbimport
operator|.
name|AddIncludeProcedureAction
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
name|action
operator|.
name|dbimport
operator|.
name|AddIncludeTableAction
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
name|action
operator|.
name|dbimport
operator|.
name|AddSchemaAction
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JMenu
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|RootPopUpMenu
extends|extends
name|DefaultPopUpMenu
block|{
specifier|private
specifier|static
specifier|final
name|int
name|FIRST_POSITION
init|=
literal|0
decl_stmt|;
specifier|protected
name|JMenuItem
name|addItem
decl_stmt|;
specifier|protected
name|JMenuItem
name|addCatalog
decl_stmt|;
specifier|protected
name|JMenuItem
name|addSchema
decl_stmt|;
specifier|protected
name|JMenuItem
name|addIncludeTable
decl_stmt|;
specifier|protected
name|JMenuItem
name|addExcludeTable
decl_stmt|;
specifier|protected
name|JMenuItem
name|addIncludeColumn
decl_stmt|;
specifier|protected
name|JMenuItem
name|addExcludeColumn
decl_stmt|;
specifier|protected
name|JMenuItem
name|addIncludeProcedure
decl_stmt|;
specifier|protected
name|JMenuItem
name|addExcludeProcedure
decl_stmt|;
specifier|public
name|RootPopUpMenu
parameter_list|()
block|{
name|initPopUpMenuElements
argument_list|()
expr_stmt|;
name|initListeners
argument_list|()
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|addItem
argument_list|,
name|FIRST_POSITION
argument_list|)
expr_stmt|;
name|delete
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|rename
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initListeners
parameter_list|()
block|{
name|addCatalog
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|AddCatalogAction
operator|.
name|class
argument_list|)
operator|.
name|actionPerformed
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|addSchema
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|AddSchemaAction
operator|.
name|class
argument_list|)
operator|.
name|actionPerformed
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|addIncludeTable
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|AddIncludeTableAction
operator|.
name|class
argument_list|)
operator|.
name|actionPerformed
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|addExcludeTable
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|AddExcludeTableAction
operator|.
name|class
argument_list|)
operator|.
name|actionPerformed
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|addIncludeColumn
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|AddIncludeColumnAction
operator|.
name|class
argument_list|)
operator|.
name|actionPerformed
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|addExcludeColumn
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|AddExcludeColumnAction
operator|.
name|class
argument_list|)
operator|.
name|actionPerformed
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|addIncludeProcedure
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|AddIncludeProcedureAction
operator|.
name|class
argument_list|)
operator|.
name|actionPerformed
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|addExcludeProcedure
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|AddExcludeProcedureAction
operator|.
name|class
argument_list|)
operator|.
name|actionPerformed
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initPopUpMenuElements
parameter_list|()
block|{
name|addItem
operator|=
operator|new
name|JMenu
argument_list|(
literal|"Add"
argument_list|)
expr_stmt|;
name|addCatalog
operator|=
operator|new
name|JMenuItem
argument_list|(
literal|"Catalog"
argument_list|)
expr_stmt|;
name|addSchema
operator|=
operator|new
name|JMenuItem
argument_list|(
literal|"Schema"
argument_list|)
expr_stmt|;
name|addIncludeTable
operator|=
operator|new
name|JMenuItem
argument_list|(
literal|"Include Table"
argument_list|)
expr_stmt|;
name|addExcludeTable
operator|=
operator|new
name|JMenuItem
argument_list|(
literal|"Exclude Table"
argument_list|)
expr_stmt|;
name|addIncludeColumn
operator|=
operator|new
name|JMenuItem
argument_list|(
literal|"Include Column"
argument_list|)
expr_stmt|;
name|addExcludeColumn
operator|=
operator|new
name|JMenuItem
argument_list|(
literal|"Exclude Column"
argument_list|)
expr_stmt|;
name|addIncludeProcedure
operator|=
operator|new
name|JMenuItem
argument_list|(
literal|"Include Procedure"
argument_list|)
expr_stmt|;
name|addExcludeProcedure
operator|=
operator|new
name|JMenuItem
argument_list|(
literal|"Exclude Procedure"
argument_list|)
expr_stmt|;
name|addItem
operator|.
name|add
argument_list|(
name|addSchema
argument_list|)
expr_stmt|;
name|addItem
operator|.
name|add
argument_list|(
name|addCatalog
argument_list|)
expr_stmt|;
name|addItem
operator|.
name|add
argument_list|(
name|addIncludeTable
argument_list|)
expr_stmt|;
name|addItem
operator|.
name|add
argument_list|(
name|addExcludeTable
argument_list|)
expr_stmt|;
name|addItem
operator|.
name|add
argument_list|(
name|addIncludeColumn
argument_list|)
expr_stmt|;
name|addItem
operator|.
name|add
argument_list|(
name|addExcludeColumn
argument_list|)
expr_stmt|;
name|addItem
operator|.
name|add
argument_list|(
name|addIncludeProcedure
argument_list|)
expr_stmt|;
name|addItem
operator|.
name|add
argument_list|(
name|addExcludeProcedure
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

