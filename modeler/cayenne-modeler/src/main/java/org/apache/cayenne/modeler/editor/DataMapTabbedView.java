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

begin_comment
comment|/**  * Data map editing tabs container  *  */
end_comment

begin_class
specifier|public
class|class
name|DataMapTabbedView
extends|extends
name|JTabbedPane
block|{
name|ProjectController
name|mediator
decl_stmt|;
comment|/**      * constructor      *      * @param mediator mediator instance      */
specifier|public
name|DataMapTabbedView
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
name|initView
argument_list|()
expr_stmt|;
block|}
comment|/**      * create tabs      */
specifier|private
name|void
name|initView
parameter_list|()
block|{
name|setTabPlacement
argument_list|(
name|JTabbedPane
operator|.
name|TOP
argument_list|)
expr_stmt|;
comment|// add panels to tabs
comment|// note that those panels that have no internal scrollable tables
comment|// must be wrapped in a scroll pane
name|JScrollPane
name|dataMapView
init|=
operator|new
name|JScrollPane
argument_list|(
operator|new
name|DataMapView
argument_list|(
name|mediator
argument_list|)
argument_list|)
decl_stmt|;
name|JScrollPane
name|dbImportView
init|=
operator|new
name|JScrollPane
argument_list|(
operator|new
name|DbImportView
argument_list|(
name|mediator
argument_list|)
argument_list|)
decl_stmt|;
name|addTab
argument_list|(
literal|"DataMap"
argument_list|,
name|dataMapView
argument_list|)
expr_stmt|;
name|addTab
argument_list|(
literal|"DbImport"
argument_list|,
name|dbImportView
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

