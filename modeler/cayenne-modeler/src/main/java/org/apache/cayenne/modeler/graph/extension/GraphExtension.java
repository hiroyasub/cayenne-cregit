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
name|extension
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
name|configuration
operator|.
name|ConfigurationNodeVisitor
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
name|DataChannelDescriptor
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
name|di
operator|.
name|Inject
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
name|di
operator|.
name|Provider
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
name|GraphRegistry
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
name|project
operator|.
name|Project
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
name|project
operator|.
name|extension
operator|.
name|BaseNamingDelegate
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
name|project
operator|.
name|extension
operator|.
name|LoaderDelegate
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
name|project
operator|.
name|extension
operator|.
name|ProjectExtension
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
name|project
operator|.
name|extension
operator|.
name|SaverDelegate
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|GraphExtension
implements|implements
name|ProjectExtension
block|{
specifier|static
specifier|final
name|String
name|NAMESPACE
init|=
literal|"http://cayenne.apache.org/schema/"
operator|+
name|Project
operator|.
name|VERSION
operator|+
literal|"/graph"
decl_stmt|;
specifier|static
specifier|final
name|String
name|GRAPH_SUFFIX
init|=
literal|".graph.xml"
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|Provider
argument_list|<
name|Application
argument_list|>
name|applicationProvider
decl_stmt|;
annotation|@
name|Override
specifier|public
name|LoaderDelegate
name|createLoaderDelegate
parameter_list|()
block|{
return|return
operator|new
name|GraphLoaderDelegate
argument_list|(
name|applicationProvider
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|SaverDelegate
name|createSaverDelegate
parameter_list|()
block|{
return|return
operator|new
name|GraphSaverDelegate
argument_list|(
name|applicationProvider
operator|.
name|get
argument_list|()
operator|.
name|getMetaData
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNodeVisitor
argument_list|<
name|String
argument_list|>
name|createNamingDelegate
parameter_list|()
block|{
return|return
operator|new
name|BaseNamingDelegate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|visitDataChannelDescriptor
parameter_list|(
name|DataChannelDescriptor
name|channelDescriptor
parameter_list|)
block|{
comment|// if there is no registry, than there is no need to save anything
name|GraphRegistry
name|registry
init|=
name|applicationProvider
operator|.
name|get
argument_list|()
operator|.
name|getMetaData
argument_list|()
operator|.
name|get
argument_list|(
name|channelDescriptor
argument_list|,
name|GraphRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|channelDescriptor
operator|.
name|getName
argument_list|()
operator|+
name|GRAPH_SUFFIX
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

