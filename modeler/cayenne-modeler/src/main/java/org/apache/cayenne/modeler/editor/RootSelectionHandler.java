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
name|QueryDescriptor
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
name|awt
operator|.
name|event
operator|.
name|FocusEvent
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
name|FocusListener
import|;
end_import

begin_comment
comment|/**  * Handler to user's actions with root selection combobox  */
end_comment

begin_class
class|class
name|RootSelectionHandler
implements|implements
name|FocusListener
implements|,
name|ActionListener
block|{
specifier|private
name|String
name|newName
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|needChangeName
decl_stmt|;
specifier|private
specifier|final
name|BaseQueryMainTab
name|queryTab
decl_stmt|;
name|RootSelectionHandler
parameter_list|(
name|BaseQueryMainTab
name|queryTab
parameter_list|)
block|{
name|this
operator|.
name|queryTab
operator|=
name|queryTab
expr_stmt|;
block|}
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|ae
parameter_list|)
block|{
name|QueryDescriptor
name|query
init|=
name|queryTab
operator|.
name|getQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|Entity
name|root
init|=
operator|(
name|Entity
operator|)
name|queryTab
operator|.
name|getQueryRoot
argument_list|()
operator|.
name|getModel
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|setRoot
argument_list|(
name|root
argument_list|)
expr_stmt|;
if|if
condition|(
name|needChangeName
condition|)
block|{
comment|//not changed by user
comment|/*                      * Doing auto name change, following CAY-888 #2                      */
name|String
name|newPrefix
init|=
name|root
operator|.
name|getName
argument_list|()
operator|+
literal|"Query"
decl_stmt|;
name|newName
operator|=
name|newPrefix
expr_stmt|;
name|DataMap
name|map
init|=
name|queryTab
operator|.
name|getMediator
argument_list|()
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
name|long
name|postfix
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|map
operator|.
name|getQueryDescriptor
argument_list|(
name|newName
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|newName
operator|=
name|newPrefix
operator|+
operator|(
name|postfix
operator|++
operator|)
expr_stmt|;
block|}
name|queryTab
operator|.
name|getNameField
argument_list|()
operator|.
name|setText
argument_list|(
name|newName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|focusGained
parameter_list|(
name|FocusEvent
name|e
parameter_list|)
block|{
comment|//reset new name tracking
name|newName
operator|=
literal|null
expr_stmt|;
name|QueryDescriptor
name|query
init|=
name|queryTab
operator|.
name|getQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|needChangeName
operator|=
name|hasDefaultName
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|needChangeName
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|focusLost
parameter_list|(
name|FocusEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|newName
operator|!=
literal|null
condition|)
block|{
name|queryTab
operator|.
name|setQueryName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
block|}
name|newName
operator|=
literal|null
expr_stmt|;
name|needChangeName
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * @return whether specified's query name is 'default' i.e. Cayenne generated      * A query's name is 'default' if it starts with 'UntitledQuery' or with root name.      *      * We cannot follow user input because tab might be opened many times      */
name|boolean
name|hasDefaultName
parameter_list|(
name|QueryDescriptor
name|query
parameter_list|)
block|{
name|String
name|prefix
init|=
name|query
operator|.
name|getRoot
argument_list|()
operator|==
literal|null
condition|?
literal|"UntitledQuery"
else|:
name|CellRenderers
operator|.
name|asString
argument_list|(
name|query
operator|.
name|getRoot
argument_list|()
argument_list|)
operator|+
literal|"Query"
decl_stmt|;
return|return
name|queryTab
operator|.
name|getNameField
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|getText
argument_list|()
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
return|;
block|}
block|}
end_class

end_unit

