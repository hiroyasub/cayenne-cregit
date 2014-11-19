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
name|unit
operator|.
name|di
operator|.
name|server
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
name|ConfigurationException
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
name|di
operator|.
name|Provider
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
import|;
end_import

begin_class
specifier|public
class|class
name|DBCleanerProvider
implements|implements
name|Provider
argument_list|<
name|DBCleaner
argument_list|>
block|{
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|Provider
argument_list|<
name|ServerCaseProperties
argument_list|>
name|propertiesProvider
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|SchemaBuilder
name|schemaBuilder
decl_stmt|;
annotation|@
name|Override
specifier|public
name|DBCleaner
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
return|return
operator|new
name|DBCleaner
argument_list|(
operator|(
name|FlavoredDBHelper
operator|)
name|dbHelper
argument_list|,
name|schemaBuilder
operator|.
name|getDomain
argument_list|()
argument_list|,
name|propertiesProvider
operator|.
name|get
argument_list|()
operator|.
name|getConfigurationLocation
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

