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
name|util
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
name|io
operator|.
name|File
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
name|Action
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
name|ModelerPreferences
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
name|OpenProjectAction
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
name|RecentFileListListener
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
name|control
operator|.
name|FileMenuItem
import|;
end_import

begin_comment
comment|/**  * Menu that contains a list of previously used files. It is built from CayenneModeler  * preferences by calling<code>rebuildFromPreferences</code>.  *   */
end_comment

begin_class
specifier|public
class|class
name|RecentFileMenu
extends|extends
name|JMenu
implements|implements
name|RecentFileListListener
block|{
comment|/**      * Constructor for RecentFileMenu.      */
specifier|public
name|RecentFileMenu
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|super
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see javax.swing.JMenu#add(JMenuItem)      */
specifier|public
name|FileMenuItem
name|add
parameter_list|(
name|FileMenuItem
name|menuItem
parameter_list|)
block|{
return|return
operator|(
name|FileMenuItem
operator|)
name|super
operator|.
name|add
argument_list|(
name|menuItem
argument_list|)
return|;
block|}
comment|/**      * Rebuilds internal menu items list with the files stored in CayenneModeler      * preferences.      */
specifier|public
name|void
name|rebuildFromPreferences
parameter_list|()
block|{
name|List
argument_list|<
name|File
argument_list|>
name|arr
init|=
name|ModelerPreferences
operator|.
name|getLastProjFiles
argument_list|()
decl_stmt|;
while|while
condition|(
name|arr
operator|.
name|size
argument_list|()
operator|>
name|ModelerPreferences
operator|.
name|LAST_PROJ_FILES_SIZE
condition|)
block|{
name|arr
operator|.
name|remove
argument_list|(
name|arr
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// read menus
name|Component
index|[]
name|comps
init|=
name|getMenuComponents
argument_list|()
decl_stmt|;
name|int
name|curSize
init|=
name|comps
operator|.
name|length
decl_stmt|;
name|int
name|prefSize
init|=
name|arr
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|prefSize
condition|;
name|i
operator|++
control|)
block|{
name|String
name|name
init|=
name|arr
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|<
name|curSize
condition|)
block|{
comment|// update existing one
name|FileMenuItem
name|item
init|=
operator|(
name|FileMenuItem
operator|)
name|comps
index|[
name|i
index|]
decl_stmt|;
name|item
operator|.
name|setText
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// add a new one
name|FileMenuItem
name|item
init|=
operator|new
name|FileMenuItem
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|item
operator|.
name|setAction
argument_list|(
name|findAction
argument_list|()
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|item
argument_list|)
expr_stmt|;
block|}
block|}
comment|// remove any hanging items
for|for
control|(
name|int
name|i
init|=
name|curSize
operator|-
literal|1
init|;
name|i
operator|>=
name|prefSize
condition|;
name|i
operator|--
control|)
block|{
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Action
name|findAction
parameter_list|()
block|{
return|return
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getActionManager
argument_list|()
operator|.
name|getAction
argument_list|(
name|OpenProjectAction
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|void
name|recentFileListChanged
parameter_list|()
block|{
name|rebuildFromPreferences
argument_list|()
expr_stmt|;
name|setEnabled
argument_list|(
name|getMenuComponentCount
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

