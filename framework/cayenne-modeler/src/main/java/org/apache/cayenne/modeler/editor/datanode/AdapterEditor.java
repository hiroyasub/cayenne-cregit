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
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ComponentAdapter
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
name|ComponentEvent
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
name|access
operator|.
name|DataNode
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
name|event
operator|.
name|DataNodeEvent
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
name|dba
operator|.
name|AutoAdapter
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
name|dba
operator|.
name|DbAdapter
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
name|event
operator|.
name|DataNodeDisplayEvent
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
name|DataNodeDisplayListener
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
name|modeler
operator|.
name|util
operator|.
name|ModelerDbAdapter
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|AdapterEditor
extends|extends
name|CayenneController
block|{
specifier|protected
name|AdapterView
name|view
decl_stmt|;
specifier|protected
name|DataNode
name|node
decl_stmt|;
specifier|protected
name|ObjectBinding
name|adapterNameBinding
decl_stmt|;
specifier|public
name|AdapterEditor
parameter_list|(
name|CayenneController
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
name|AdapterView
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|initController
parameter_list|()
block|{
comment|// init bindings
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
name|adapterNameBinding
operator|=
name|builder
operator|.
name|bindToTextField
argument_list|(
name|view
operator|.
name|getCustomAdapter
argument_list|()
argument_list|,
literal|"adapterName"
argument_list|)
expr_stmt|;
comment|// init listenersÐ
operator|(
operator|(
name|ProjectController
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|addDataNodeDisplayListener
argument_list|(
operator|new
name|DataNodeDisplayListener
argument_list|()
block|{
specifier|public
name|void
name|currentDataNodeChanged
parameter_list|(
name|DataNodeDisplayEvent
name|e
parameter_list|)
block|{
name|refreshView
argument_list|(
name|e
operator|.
name|getDataNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|getView
argument_list|()
operator|.
name|addComponentListener
argument_list|(
operator|new
name|ComponentAdapter
argument_list|()
block|{
specifier|public
name|void
name|componentShown
parameter_list|(
name|ComponentEvent
name|e
parameter_list|)
block|{
name|refreshView
argument_list|(
name|node
operator|!=
literal|null
condition|?
name|node
else|:
operator|(
operator|(
name|ProjectController
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|getCurrentDataNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|refreshView
parameter_list|(
name|DataNode
name|node
parameter_list|)
block|{
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
name|getView
argument_list|()
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
name|adapterNameBinding
operator|.
name|updateView
argument_list|()
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
name|String
name|getAdapterName
parameter_list|()
block|{
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|DbAdapter
name|adapter
init|=
name|node
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
comment|// TODO, Andrus, 11/3/2005 - to simplify this logic, it would be nice to
comment|// consistently load CustomDbAdapter... this would require an ability to set a
comment|// load delegate in OpenProjectAction
if|if
condition|(
name|adapter
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|else if
condition|(
name|adapter
operator|instanceof
name|ModelerDbAdapter
condition|)
block|{
return|return
operator|(
operator|(
name|ModelerDbAdapter
operator|)
name|adapter
operator|)
operator|.
name|getAdapterClassName
argument_list|()
return|;
block|}
comment|// don't do "instanceof" here, as we maybe dealing with a custom subclass...
if|else if
condition|(
name|adapter
operator|.
name|getClass
argument_list|()
operator|==
name|AutoAdapter
operator|.
name|class
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|adapter
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
specifier|public
name|void
name|setAdapterName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|ModelerDbAdapter
name|adapter
init|=
operator|new
name|ModelerDbAdapter
argument_list|(
name|name
argument_list|,
name|node
operator|.
name|getDataSource
argument_list|()
argument_list|)
decl_stmt|;
name|adapter
operator|.
name|validate
argument_list|()
expr_stmt|;
name|node
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|DataNodeEvent
name|e
init|=
operator|new
name|DataNodeEvent
argument_list|(
name|AdapterEditor
operator|.
name|this
argument_list|,
name|node
argument_list|)
decl_stmt|;
operator|(
operator|(
name|ProjectController
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|fireDataNodeEvent
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

