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
name|access
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
name|CayenneException
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
name|ObjEntity
import|;
end_import

begin_comment
comment|/**  * DbLoaderDelegate defines API that allows to control the behavior of DbLoader during the  * database reverse-engineering. Delegate is also notified of the progress of  * reverse-engineering.  *   */
end_comment

begin_interface
specifier|public
interface|interface
name|DbLoaderDelegate
block|{
comment|/**      * Returns true to tell DbLoader that it is OK to overwrite DbEntity that already      * exists in the model. If loading process should be stopped immediately, an exception      * is thrown.      */
specifier|public
name|boolean
name|overwriteDbEntity
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
throws|throws
name|CayenneException
function_decl|;
specifier|public
name|void
name|dbEntityAdded
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
function_decl|;
specifier|public
name|void
name|dbEntityRemoved
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
function_decl|;
specifier|public
name|void
name|objEntityAdded
parameter_list|(
name|ObjEntity
name|ent
parameter_list|)
function_decl|;
specifier|public
name|void
name|objEntityRemoved
parameter_list|(
name|ObjEntity
name|ent
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

