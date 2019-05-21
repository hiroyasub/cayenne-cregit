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
name|testdo
operator|.
name|inheritance_flat
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
name|inheritance_flat
operator|.
name|auto
operator|.
name|_Role
import|;
end_import

begin_class
specifier|public
class|class
name|Role
extends|extends
name|_Role
block|{
annotation|@
name|Override
specifier|protected
name|void
name|onPostPersist
parameter_list|()
block|{
comment|//TODO: implement onPostPersist
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onPostUpdate
parameter_list|()
block|{
comment|//TODO: implement onPostUpdate
block|}
block|}
end_class

end_unit

