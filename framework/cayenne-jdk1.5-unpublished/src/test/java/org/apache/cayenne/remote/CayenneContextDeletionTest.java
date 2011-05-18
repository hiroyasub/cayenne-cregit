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
name|remote
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
name|testdo
operator|.
name|mt
operator|.
name|ClientMtTable1
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
literal|"cayenne-multi-tier.xml"
argument_list|)
specifier|public
class|class
name|CayenneContextDeletionTest
extends|extends
name|RemoteCayenneCase
block|{
specifier|public
name|void
name|testDeletion
parameter_list|()
block|{
name|ClientMtTable1
name|object
init|=
name|clientContext
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|clientContext
operator|.
name|deleteObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
comment|// now check that the object is unregistered
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|clientContext
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

