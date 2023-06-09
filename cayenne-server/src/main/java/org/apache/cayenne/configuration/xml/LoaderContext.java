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
name|configuration
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|configuration
operator|.
name|DataChannelDescriptorLoader
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
name|xml
operator|.
name|sax
operator|.
name|XMLReader
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|LoaderContext
block|{
name|Collection
argument_list|<
name|DataMapLoaderListener
argument_list|>
name|dataMapListeners
decl_stmt|;
name|Collection
argument_list|<
name|DataChannelLoaderListener
argument_list|>
name|dataChannelListeners
decl_stmt|;
specifier|private
name|XMLReader
name|xmlReader
decl_stmt|;
specifier|private
name|HandlerFactory
name|factory
decl_stmt|;
specifier|public
name|LoaderContext
parameter_list|(
name|XMLReader
name|reader
parameter_list|,
name|HandlerFactory
name|factory
parameter_list|)
block|{
name|this
operator|.
name|xmlReader
operator|=
name|reader
expr_stmt|;
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|dataMapListeners
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|dataChannelListeners
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|HandlerFactory
name|getFactory
parameter_list|()
block|{
return|return
name|factory
return|;
block|}
specifier|public
name|XMLReader
name|getXmlReader
parameter_list|()
block|{
return|return
name|xmlReader
return|;
block|}
specifier|public
name|void
name|addDataMapListener
parameter_list|(
name|DataMapLoaderListener
name|listener
parameter_list|)
block|{
name|dataMapListeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dataMapLoaded
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
for|for
control|(
name|DataMapLoaderListener
name|listener
range|:
name|dataMapListeners
control|)
block|{
name|listener
operator|.
name|onDataMapLoaded
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|addDataChannelListener
parameter_list|(
name|DataChannelLoaderListener
name|listener
parameter_list|)
block|{
name|dataChannelListeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dataChannelLoaded
parameter_list|(
name|DataChannelDescriptor
name|descriptor
parameter_list|)
block|{
for|for
control|(
name|DataChannelLoaderListener
name|listener
range|:
name|dataChannelListeners
control|)
block|{
name|listener
operator|.
name|onDataChannelLoaded
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

