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
operator|.
name|datanode
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
name|JScrollPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTabbedPane
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
name|CayenneController
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DataNodeEditor
extends|extends
name|CayenneController
block|{
specifier|protected
name|JTabbedPane
name|view
decl_stmt|;
specifier|public
name|DataNodeEditor
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
name|JTabbedPane
argument_list|()
expr_stmt|;
name|view
operator|.
name|addTab
argument_list|(
literal|"Main"
argument_list|,
operator|new
name|JScrollPane
argument_list|(
operator|new
name|MainDataNodeEditor
argument_list|(
name|parent
argument_list|,
name|this
argument_list|)
operator|.
name|getView
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|view
operator|.
name|addTab
argument_list|(
literal|"Adapter"
argument_list|,
operator|new
name|AdapterEditor
argument_list|(
name|parent
argument_list|)
operator|.
name|getView
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|addTab
argument_list|(
literal|"Password Encoder"
argument_list|,
operator|new
name|PasswordEncoderEditor
argument_list|(
name|parent
argument_list|)
operator|.
name|getView
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|JTabbedPane
name|getTabComponent
parameter_list|()
block|{
return|return
name|view
return|;
block|}
block|}
end_class

end_unit

