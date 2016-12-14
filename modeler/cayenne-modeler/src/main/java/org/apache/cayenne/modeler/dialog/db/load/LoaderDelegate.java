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
name|CayenneRuntimeException
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
name|dbload
operator|.
name|DefaultDbLoaderDelegate
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
name|DbEntity
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
name|DbRelationship
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
name|event
operator|.
name|EntityEvent
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
name|event
operator|.
name|MapEvent
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

begin_class
specifier|final
class|class
name|LoaderDelegate
extends|extends
name|DefaultDbLoaderDelegate
block|{
specifier|private
name|DbLoaderContext
name|context
decl_stmt|;
name|LoaderDelegate
parameter_list|(
name|DbLoaderContext
name|dbLoaderContext
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|dbLoaderContext
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|dbEntityAdded
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|checkCanceled
argument_list|()
expr_stmt|;
name|context
operator|.
name|setStatusNote
argument_list|(
literal|"Importing table '"
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|"'..."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|dbEntityRemoved
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|checkCanceled
argument_list|()
expr_stmt|;
if|if
condition|(
name|context
operator|.
name|isExistingDataMap
argument_list|()
condition|)
block|{
name|context
operator|.
name|getProjectController
argument_list|()
operator|.
name|fireDbEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|entity
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|dbRelationship
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|checkCanceled
argument_list|()
expr_stmt|;
name|context
operator|.
name|setStatusNote
argument_list|(
literal|"Load relationships for '"
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|"'..."
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|dbRelationshipLoaded
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbRelationship
name|relationship
parameter_list|)
block|{
name|checkCanceled
argument_list|()
expr_stmt|;
name|context
operator|.
name|setStatusNote
argument_list|(
literal|"Load relationship: '"
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|"'; '"
operator|+
name|relationship
operator|.
name|getName
argument_list|()
operator|+
literal|"'..."
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|checkCanceled
parameter_list|()
block|{
if|if
condition|(
name|context
operator|.
name|isStopping
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Reengineering was canceled."
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

