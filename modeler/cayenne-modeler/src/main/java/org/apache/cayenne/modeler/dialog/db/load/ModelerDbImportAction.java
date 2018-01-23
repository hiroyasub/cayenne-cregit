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
name|dialog
operator|.
name|db
operator|.
name|load
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
name|configuration
operator|.
name|DataMapLoader
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
name|server
operator|.
name|DataSourceFactory
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
name|server
operator|.
name|DbAdapterFactory
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
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactoryProvider
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
name|project
operator|.
name|ProjectSaver
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
name|DbImportConfiguration
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
name|DefaultDbImportAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
specifier|public
class|class
name|ModelerDbImportAction
extends|extends
name|DefaultDbImportAction
block|{
annotation|@
name|Inject
specifier|private
name|DataMap
name|targetMap
decl_stmt|;
specifier|public
name|ModelerDbImportAction
parameter_list|(
annotation|@
name|Inject
name|Logger
name|logger
parameter_list|,
annotation|@
name|Inject
name|ProjectSaver
name|projectSaver
parameter_list|,
annotation|@
name|Inject
name|DataSourceFactory
name|dataSourceFactory
parameter_list|,
annotation|@
name|Inject
name|DbAdapterFactory
name|adapterFactory
parameter_list|,
annotation|@
name|Inject
name|DataMapLoader
name|mapLoader
parameter_list|,
annotation|@
name|Inject
name|MergerTokenFactoryProvider
name|mergerTokenFactoryProvider
parameter_list|,
annotation|@
name|Inject
name|DataChannelDescriptorLoader
name|dataChannelDescriptorLoader
parameter_list|)
block|{
name|super
argument_list|(
name|logger
argument_list|,
name|projectSaver
argument_list|,
name|dataSourceFactory
argument_list|,
name|adapterFactory
argument_list|,
name|mapLoader
argument_list|,
name|mergerTokenFactoryProvider
argument_list|,
name|dataChannelDescriptorLoader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|DataMap
name|existingTargetMap
parameter_list|(
name|DbImportConfiguration
name|configuration
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|targetMap
return|;
block|}
block|}
end_class

end_unit

