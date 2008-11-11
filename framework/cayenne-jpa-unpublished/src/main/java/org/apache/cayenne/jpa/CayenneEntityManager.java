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
name|jpa
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
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
name|DataChannel
import|;
end_import

begin_comment
comment|/**  * An interface that allows to access Cayenne runtime hidden behind standard JPA classes.  * To do that, simply cast an EntityManager returned by Cayenne provider to  * "CayenneEntityManager". Note that a regular JPA application shouldn't normally attempt  * to do that. Otherwise it will not be portable across JPA providers.  *   */
end_comment

begin_interface
specifier|public
interface|interface
name|CayenneEntityManager
extends|extends
name|EntityManager
block|{
comment|/**      * Returns a Cayenne {@link DataChannel} that is used to link EntityManager with      * Cayenne runtime. DataChannel can be used for instance to obtain Cayenne metadata or      * add listeners.      */
name|DataChannel
name|getChannel
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

