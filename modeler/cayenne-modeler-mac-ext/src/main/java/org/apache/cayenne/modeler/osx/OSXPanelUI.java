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
name|osx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JComponent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|SwingUtilities
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|plaf
operator|.
name|ComponentUI
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|plaf
operator|.
name|basic
operator|.
name|BasicPanelUI
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
name|CayenneModelerFrame
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|OSXPanelUI
extends|extends
name|BasicPanelUI
block|{
specifier|private
specifier|static
specifier|final
name|Color
name|BACKGROUND
init|=
operator|new
name|Color
argument_list|(
literal|0xEEEEEE
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|OSXPanelUI
name|INSTANCE
decl_stmt|;
static|static
block|{
name|BasicPanelUI
name|delegate
decl_stmt|;
try|try
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Class
argument_list|<
name|?
extends|extends
name|BasicPanelUI
argument_list|>
name|delegateClass
init|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|BasicPanelUI
argument_list|>
operator|)
name|Class
operator|.
name|forName
argument_list|(
literal|"com.apple.laf.AquaPanelUI"
argument_list|)
decl_stmt|;
name|delegate
operator|=
name|delegateClass
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|delegate
operator|=
operator|new
name|BasicPanelUI
argument_list|()
expr_stmt|;
block|}
name|INSTANCE
operator|=
operator|new
name|OSXPanelUI
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
specifier|private
name|BasicPanelUI
name|delegate
decl_stmt|;
specifier|private
name|OSXPanelUI
parameter_list|(
name|BasicPanelUI
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
specifier|public
specifier|static
name|ComponentUI
name|createUI
parameter_list|(
name|JComponent
name|c
parameter_list|)
block|{
return|return
name|INSTANCE
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|installDefaults
parameter_list|(
specifier|final
name|JPanel
name|p
parameter_list|)
block|{
name|super
operator|.
name|installDefaults
argument_list|(
name|p
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|instanceof
name|CayenneModelerFrame
operator|.
name|SearchPanel
condition|)
block|{
name|SwingUtilities
operator|.
name|invokeLater
argument_list|(
operator|(
operator|(
name|CayenneModelerFrame
operator|.
name|SearchPanel
operator|)
name|p
operator|)
operator|::
name|hideSearchLabel
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|p
operator|.
name|setBackground
argument_list|(
name|BACKGROUND
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|update
parameter_list|(
name|Graphics
name|g
parameter_list|,
name|JComponent
name|c
parameter_list|)
block|{
name|delegate
operator|.
name|update
argument_list|(
name|g
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

