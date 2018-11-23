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
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|builder
operator|.
name|DefaultFormBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|FormLayout
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
name|configuration
operator|.
name|xml
operator|.
name|DataChannelMetaData
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ReverseEngineering
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
name|util
operator|.
name|ModelerUtil
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JButton
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JCheckBox
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JLabel
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
name|java
operator|.
name|awt
operator|.
name|BorderLayout
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|GeneratorsPanel
extends|extends
name|JPanel
block|{
specifier|private
name|JCheckBox
name|checkConfig
decl_stmt|;
specifier|private
name|JLabel
name|dataMapLabel
decl_stmt|;
specifier|private
name|JButton
name|toConfigButton
decl_stmt|;
specifier|private
name|DataMap
name|dataMap
decl_stmt|;
specifier|private
name|Class
name|type
decl_stmt|;
specifier|private
name|String
name|icon
decl_stmt|;
specifier|public
name|GeneratorsPanel
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|String
name|icon
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|icon
operator|=
name|icon
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
name|initView
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|initView
parameter_list|()
block|{
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"left:pref, 4dlu, fill:70dlu, 3dlu, fill:120, 3dlu, fill:120"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|DefaultFormBuilder
name|builder
init|=
operator|new
name|DefaultFormBuilder
argument_list|(
name|layout
argument_list|)
decl_stmt|;
name|this
operator|.
name|checkConfig
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataMapLabel
operator|=
operator|new
name|JLabel
argument_list|(
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataMapLabel
operator|.
name|setToolTipText
argument_list|(
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|DataChannelMetaData
name|metaData
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
name|this
operator|.
name|toConfigButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Edit Config"
argument_list|)
expr_stmt|;
if|if
condition|(
name|metaData
operator|.
name|get
argument_list|(
name|dataMap
argument_list|,
name|type
argument_list|)
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|type
operator|==
name|ReverseEngineering
operator|.
name|class
condition|)
block|{
name|checkConfig
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|toConfigButton
operator|.
name|setIcon
argument_list|(
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
name|icon
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|checkConfig
argument_list|,
name|dataMapLabel
argument_list|,
name|toConfigButton
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|builder
operator|.
name|getPanel
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JCheckBox
name|getCheckConfig
parameter_list|()
block|{
return|return
name|checkConfig
return|;
block|}
specifier|public
name|JButton
name|getToConfigButton
parameter_list|()
block|{
return|return
name|toConfigButton
return|;
block|}
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
block|}
end_class

end_unit

