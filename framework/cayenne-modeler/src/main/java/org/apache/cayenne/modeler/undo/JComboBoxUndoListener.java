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
name|undo
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ItemEvent
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
name|ItemListener
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
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|UndoManager
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

begin_class
specifier|public
class|class
name|JComboBoxUndoListener
implements|implements
name|ItemListener
block|{
specifier|private
name|Object
name|deselectedItem
decl_stmt|;
specifier|public
name|void
name|itemStateChanged
parameter_list|(
name|ItemEvent
name|e
parameter_list|)
block|{
name|int
name|stateChange
init|=
name|e
operator|.
name|getStateChange
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|stateChange
condition|)
block|{
case|case
name|ItemEvent
operator|.
name|DESELECTED
case|:
name|deselectedItem
operator|=
name|e
operator|.
name|getItem
argument_list|()
expr_stmt|;
break|break;
case|case
name|ItemEvent
operator|.
name|SELECTED
case|:
name|UndoManager
name|undoManager
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getUndoManager
argument_list|()
decl_stmt|;
name|undoManager
operator|.
name|addEdit
argument_list|(
operator|new
name|JComboBoxUndoableEdit
argument_list|(
operator|(
name|JComboBox
operator|)
name|e
operator|.
name|getSource
argument_list|()
argument_list|,
name|deselectedItem
argument_list|,
name|e
operator|.
name|getItem
argument_list|()
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
end_class

end_unit

