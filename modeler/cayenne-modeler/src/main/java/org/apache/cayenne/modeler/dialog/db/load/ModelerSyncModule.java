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
name|di
operator|.
name|Binder
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
name|Module
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
name|DbImportAction
import|;
end_import

begin_class
class|class
name|ModelerSyncModule
implements|implements
name|Module
block|{
specifier|private
name|DbLoaderContext
name|dbLoaderContext
decl_stmt|;
name|ModelerSyncModule
parameter_list|(
name|DbLoaderContext
name|dbLoaderHelper
parameter_list|)
block|{
name|this
operator|.
name|dbLoaderContext
operator|=
name|dbLoaderHelper
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|ProjectController
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|dbLoaderContext
operator|.
name|getProjectController
argument_list|()
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ProjectSaver
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DbImportProjectSaver
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DbImportAction
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ModelerDbImportAction
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataMap
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|dbLoaderContext
operator|.
name|getDataMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

