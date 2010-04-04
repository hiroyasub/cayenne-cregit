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
name|conf
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_comment
comment|/**  * A pluggable factory for {@link DataSource} instances used by Cayenne runtime.  *   * @deprecated since 3.1, replaced by  *             {@link org.apache.cayenne.configuration.server.DataSourceFactory}  */
end_comment

begin_interface
specifier|public
interface|interface
name|DataSourceFactory
block|{
comment|/**      * Initializes factory with the parent configuration object.      */
name|void
name|initializeWithParentConfiguration
parameter_list|(
name|Configuration
name|conf
parameter_list|)
function_decl|;
comment|/**      * Returns DataSource object corresponding to<code>location</code>. Concrete      * implementations may treat location differently - as a file path, JNDI location,      * etc.      */
name|DataSource
name|getDataSource
parameter_list|(
name|String
name|location
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

