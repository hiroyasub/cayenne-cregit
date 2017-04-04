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
name|graph
operator|.
name|action
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
name|ActionEvent
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
name|graph
operator|.
name|DataDomainGraphTab
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
name|graph
operator|.
name|GraphBuilder
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
name|CayenneAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgraph
operator|.
name|JGraph
import|;
end_import

begin_comment
comment|/**  * Action for zooming out graph  */
end_comment

begin_class
specifier|public
class|class
name|ZoomOutAction
extends|extends
name|CayenneAction
block|{
specifier|private
specifier|final
name|DataDomainGraphTab
name|dataDomainGraphTab
decl_stmt|;
specifier|public
name|ZoomOutAction
parameter_list|(
name|DataDomainGraphTab
name|dataDomainGraphTab
parameter_list|,
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
literal|"Zoom Out"
argument_list|,
name|application
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataDomainGraphTab
operator|=
name|dataDomainGraphTab
expr_stmt|;
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getIconName
parameter_list|()
block|{
return|return
literal|"icon-zoom-out.png"
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|JGraph
name|graph
init|=
name|dataDomainGraphTab
operator|.
name|getGraph
argument_list|()
decl_stmt|;
name|graph
operator|.
name|setScale
argument_list|(
name|graph
operator|.
name|getScale
argument_list|()
operator|/
name|GraphBuilder
operator|.
name|ZOOM_FACTOR
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

