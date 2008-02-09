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
name|art
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|auto
operator|.
name|_ArtistCallbackTest
import|;
end_import

begin_comment
comment|/**  * Class for testing callbacks  *  * @author Vasil Tarasevich  */
end_comment

begin_class
specifier|public
class|class
name|ArtistCallbackTest
extends|extends
name|_ArtistCallbackTest
block|{
specifier|public
name|void
name|prePersistEntityObjEntity
parameter_list|()
block|{
block|}
specifier|public
name|void
name|postPersistEntityObjEntity
parameter_list|()
block|{
block|}
specifier|public
name|void
name|preUpdateEntityObjEntity
parameter_list|()
block|{
block|}
specifier|public
name|void
name|postUpdateEntityObjEntity
parameter_list|()
block|{
block|}
specifier|public
name|void
name|preRemoveEntityObjEntity
parameter_list|()
block|{
block|}
specifier|public
name|void
name|postRemoveEntityObjEntity
parameter_list|()
block|{
block|}
specifier|public
name|void
name|postLoadEntityObjEntity
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

